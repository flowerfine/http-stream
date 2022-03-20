package cn.sliew.http.stream.akka.framework.jst.order;

import akka.Done;
import akka.NotUsed;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.SpawnProtocol;
import akka.event.Logging;
import akka.japi.Pair;
import akka.stream.*;
import akka.stream.javadsl.*;
import cn.sliew.http.stream.akka.framework.ProcessResult;
import cn.sliew.http.stream.akka.framework.jst.JstRootTask;
import cn.sliew.http.stream.akka.framework.jst.JstSubTask;
import cn.sliew.http.stream.dao.mapper.job.JobSyncOffsetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionStage;

@Slf4j
@Component
public class OrderStreamJob implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    private ActorSystem<SpawnProtocol.Command> actorSystem;
    @Autowired
    private OrderJobProcessor processor;
    @Autowired
    private JobSyncOffsetMapper jobSyncOffsetMapper;

    private volatile UniqueKillSwitch killSwitch;

    public void execute() throws Exception {
        OrderJobContext context = new OrderJobContext(jobSyncOffsetMapper);
        Source<JstSubTask, UniqueKillSwitch> source = Source.single(new JstRootTask())
                .mapConcat(rootTask -> processor.map(context, rootTask))
                .viaMat(KillSwitches.single(), Keep.right());

        Flow<JstSubTask, ProcessResult, NotUsed> process = Flow.<JstSubTask>create()
                .map(subTask -> processor.process(context, subTask)).mapAsync(1, future -> future);

        Flow<JstSubTask, ProcessResult, NotUsed> subTasks =
                Flow.fromGraph(
                        GraphDSL.create(
                                b -> {
                                    int concurrency = 10;
                                    UniformFanOutShape<JstSubTask, JstSubTask> partition =
                                            b.add(Partition.create(concurrency, subTask -> Math.toIntExact(subTask.getIdentifier()) % concurrency));
                                    UniformFanInShape<ProcessResult, ProcessResult> merge =
                                            b.add(MergeSequence.create(concurrency, result -> result.getSubTask().getIdentifier()));

                                    for (int i = 0; i < concurrency; i++) {
                                        b.from(partition.out(i))
                                                .via(b.add(process.async()))
                                                .viaFanIn(merge);
                                    }

                                    return FlowShape.of(partition.in(), merge.out());
                                }));

        Pair<UniqueKillSwitch, CompletionStage<Done>> pair = source.via(subTasks)
                .log(context.getJobName())
                .withAttributes(
                        Attributes.createLogLevels(
                                Logging.DebugLevel(), // onElement
                                Logging.InfoLevel(),  // onFinish
                                Logging.ErrorLevel()  // onFailure
                        )
                )
                .toMat(Sink.foreach(result -> processor.reduce(context, result)), Keep.both())
                .run(actorSystem);

        killSwitch = pair.first();
        CompletionStage<Done> future = pair.second();
        future.toCompletableFuture().get();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        log.error("应用关闭，尝试关闭 akka-stream job!");
        if (killSwitch != null) {
            killSwitch.shutdown();
        }
    }
}
