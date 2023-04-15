package cn.sliew.http.stream.akka.framework.batch;

import akka.actor.typed.ActorSystem;
import cn.sliew.http.stream.akka.framework.JobContext;
import cn.sliew.http.stream.akka.framework.RootTask;
import cn.sliew.http.stream.akka.framework.SubTask;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.Properties;

public class BatchJobContext<Root extends RootTask, Sub extends SubTask> implements JobContext<Root, Sub> {

    private final String jobName;
    private final Properties properties;
    private final MeterRegistry meterRegistry;
    private final ActorSystem actorSystem;

    public BatchJobContext(String jobName, Properties properties, MeterRegistry meterRegistry, ActorSystem actorSystem) {
        this.jobName = jobName;
        this.properties = properties;
        this.meterRegistry = meterRegistry;
        this.actorSystem = actorSystem;
    }

    @Override
    public String getJobName() {
        return jobName;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @JsonIgnore
    @Override
    public MeterRegistry getMetrics() {
        return meterRegistry;
    }

    @JsonIgnore
    @Override
    public ActorSystem getActorSystem() {
        return actorSystem;
    }
}
