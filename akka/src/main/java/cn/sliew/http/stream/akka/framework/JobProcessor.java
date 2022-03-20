package cn.sliew.http.stream.akka.framework;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface JobProcessor<Root extends RootTask, Sub extends SubTask, SyncOffset> {

    List<Sub> map(JobContext<SyncOffset, Root, Sub> context, Root rootTask);

    CompletableFuture<ProcessResult> process(JobContext<SyncOffset, Root, Sub> context, Sub subTask);

    ProcessResult reduce(JobContext<SyncOffset, Root, Sub> context, ProcessResult result);

}
