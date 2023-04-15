package cn.sliew.http.stream.format;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.SpawnProtocol;
import akka.actor.typed.javadsl.Behaviors;
import org.apache.seatunnel.common.config.CheckResult;
import org.apache.seatunnel.env.RuntimeEnv;
import org.apache.seatunnel.shade.com.typesafe.config.Config;

public class AkkaEnvironment implements RuntimeEnv {

    private Config config;

    private ActorSystem<SpawnProtocol.Command> actorSystem;

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
    public void prepare(Boolean aBoolean) {
        this.actorSystem = ActorSystem.create(Behaviors.setup(ctx -> SpawnProtocol.create()), "akka-streams");
    }

    public ActorSystem<SpawnProtocol.Command> getActorSystem() {
        return actorSystem;
    }
}
