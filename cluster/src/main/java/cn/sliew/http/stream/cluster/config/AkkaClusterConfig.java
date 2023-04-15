package cn.sliew.http.stream.cluster.config;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.SpawnProtocol;
import akka.http.javadsl.model.Uri;
import akka.management.cluster.bootstrap.ClusterBootstrap;
import akka.management.javadsl.AkkaManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletionStage;

@Slf4j
@Configuration
public class AkkaClusterConfig {

    @Autowired
    private ActorSystem<SpawnProtocol.Command> actorSystem;

    @PostConstruct
    private void init() {
        // Akka Management hosts the HTTP routes used by bootstrap
        CompletionStage<Uri> future = AkkaManagement.get(actorSystem).start();
        future.whenComplete(((uri, throwable) -> {
            if (throwable != null) {
                log.error("Akka Management start failure!", throwable);
            } else {
                log.debug("Akka Management start success on {}", uri);
            }
        }));

        // Starting the bootstrap process needs to be done explicitly
        ClusterBootstrap.get(actorSystem).start();
    }



}
