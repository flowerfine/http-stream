package cn.sliew.http.stream.akka.framework;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface JobProcessor<Context extends JobContext, Root extends RootTask, Sub extends SubTask> {

    Context getContext();

    List<Sub> map(Root rootTask);

    CompletableFuture<ProcessResult> process(Sub subTask);

    ProcessResult reduce(ProcessResult result);

}
