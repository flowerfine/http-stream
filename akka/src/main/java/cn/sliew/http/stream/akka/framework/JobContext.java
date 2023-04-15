package cn.sliew.http.stream.akka.framework;

import akka.actor.typed.ActorSystem;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.Properties;

public interface JobContext<Root extends RootTask, Sub extends SubTask> {

    String getJobName();

    Properties getProperties();

    MeterRegistry getMetrics();

    ActorSystem getActorSystem();
}
