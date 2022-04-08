package cn.sliew.http.stream.akka.framework;

public interface StreamJob<Context extends JobContext, Root extends RootTask, Sub extends SubTask> {

    JobProcessor<Context, Root, Sub> create(Context context);
}
