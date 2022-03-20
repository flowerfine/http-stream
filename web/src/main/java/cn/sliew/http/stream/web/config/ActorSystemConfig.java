package cn.sliew.http.stream.web.config;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.SpawnProtocol;
import akka.actor.typed.javadsl.Behaviors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActorSystemConfig {

    @Value("${spring.application.name: http-stream}")
    private String application;

    @Bean(destroyMethod = "terminate")
    public ActorSystem<SpawnProtocol.Command> actorSystem() {
        ActorSystem<SpawnProtocol.Command> actorSystem = ActorSystem.create(Behaviors.setup(ctx -> SpawnProtocol.create()), application);
        actorSystem.whenTerminated().onComplete(done -> {
            if (done.isSuccess()) {
                actorSystem.log().info("ActorSystem terminate success!");
            } else {
                actorSystem.log().error("ActorSystem terminate failure!", done.failed().get());
            }
            return done.get();
        }, actorSystem.executionContext());
        return actorSystem;
    }

}
