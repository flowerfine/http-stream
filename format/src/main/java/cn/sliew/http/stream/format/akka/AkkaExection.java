package cn.sliew.http.stream.format.akka;

import akka.NotUsed;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.SpawnProtocol;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import cn.sliew.http.stream.format.AkkaEnvironment;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.seatunnel.common.config.CheckResult;
import org.apache.seatunnel.env.Execution;
import org.apache.seatunnel.shade.com.typesafe.config.Config;

import java.util.ArrayList;
import java.util.List;

public class AkkaExection implements Execution<AkkaSource, AkkaTransform, AkkaSink> {

    private final AkkaEnvironment env;

    private Config config;

    public AkkaExection(AkkaEnvironment env) {
        this.env = env;
    }

    @Override
    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public CheckResult checkConfig() {
        return CheckResult.success();
    }

    @Override
    public void prepare(Void unused) {

    }

    @Override
    public void start(List<AkkaSource> sources, List<AkkaTransform> transforms, List<AkkaSink> sinks) throws Exception {
        List<Source<VectorSchemaRoot, NotUsed>> data = new ArrayList<>();
        for (AkkaSource akkaSource : sources) {
            data.add(akkaSource.getData(env));
        }

        Source<VectorSchemaRoot, NotUsed> source = data.get(0);
        for (AkkaTransform akkaTransform : transforms) {
            Flow<VectorSchemaRoot, VectorSchemaRoot, NotUsed> transform = akkaTransform.processStream(env);
            source = source.via(transform);
        }

        ActorSystem<SpawnProtocol.Command> actorSystem = env.getActorSystem();
        for (AkkaSink akkaSink : sinks) {
            Sink<VectorSchemaRoot, NotUsed> sink = akkaSink.outputStream(env);
            source.runWith(sink, actorSystem);
        }
    }
}
