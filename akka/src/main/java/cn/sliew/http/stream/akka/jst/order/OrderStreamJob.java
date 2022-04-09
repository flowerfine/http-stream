package cn.sliew.http.stream.akka.jst.order;

import akka.Done;
import akka.NotUsed;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.SpawnProtocol;
import akka.event.Logging;
import akka.japi.Pair;
import akka.stream.*;
import akka.stream.javadsl.*;
import cn.sliew.http.stream.akka.framework.JobProcessor;
import cn.sliew.http.stream.akka.framework.ProcessResult;
import cn.sliew.http.stream.akka.framework.incremental.DefaultIncrementalJobProcessor;
import cn.sliew.http.stream.akka.jst.JstMethodEnum;
import cn.sliew.http.stream.akka.jst.JstSubTask;
import cn.sliew.http.stream.dao.mapper.job.JobSyncOffsetMapper;
import cn.sliew.http.stream.dao.mapper.jst.JstOrderMapper;
import cn.sliew.http.stream.remote.jst.JstRemoteService;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.CompletionStage;

@Slf4j
@Component
public class OrderStreamJob implements ApplicationListener<ContextClosedEvent> {

    private String jobName = JstMethodEnum.ORDERS_SINGLE_QUERY.name();
    private Properties properties = new Properties();

    private MeterRegistry meterRegistry;
    private ActorSystem<SpawnProtocol.Command> actorSystem;
    private JobSyncOffsetMapper jobSyncOffsetMapper;
    private JstRemoteService jstRemoteService;
    private JstOrderMapper jstOrderMapper;

    private volatile UniqueKillSwitch killSwitch;

    public OrderStreamJob(MeterRegistry meterRegistry,
                          ActorSystem<SpawnProtocol.Command> actorSystem,
                          JobSyncOffsetMapper jobSyncOffsetMapper,
                          JstRemoteService jstRemoteService,
                          JstOrderMapper jstOrderMapper) {
        this.meterRegistry = meterRegistry;
        this.actorSystem = actorSystem;
        this.jobSyncOffsetMapper = jobSyncOffsetMapper;
        this.jstRemoteService = jstRemoteService;
        this.jstOrderMapper = jstOrderMapper;
    }

    public void execute() throws Exception {
        OrderJobContext context = new OrderJobContext(jobName, properties, meterRegistry, actorSystem, jobSyncOffsetMapper);
        JobProcessor processor = new DefaultIncrementalJobProcessor(context);

        Source<JstSubTask, UniqueKillSwitch> source = Source.single(new OrderRootTask(jstRemoteService, jstOrderMapper))
                .mapConcat(rootTask -> processor.map(rootTask))
                .viaMat(KillSwitches.single(), Keep.right());

        Flow<JstSubTask, ProcessResult, NotUsed> process = Flow.<JstSubTask>create()
                .map(subTask -> processor.process(subTask)).mapAsync(1, future -> future);

        Flow<JstSubTask, ProcessResult, NotUsed> subTasks =
                Flow.fromGraph(
                        GraphDSL.create(
                                b -> {
                                    int concurrency = 2;
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
                .toMat(Sink.foreach(result -> processor.reduce(result)), Keep.both())
                .run(actorSystem);

        killSwitch = pair.first();
        CompletionStage<Done> future = pair.second();
        future.toCompletableFuture().get();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        log.error("应用关闭，尝试关闭 akka-streams job: {}!", jobName);
        if (killSwitch != null) {
            killSwitch.shutdown();
        }
    }
}
