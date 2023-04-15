package cn.sliew.http.stream.akka.framework;

import java.util.concurrent.CompletableFuture;

public interface SubTask<Context extends JobContext> {

    Long getIdentifier();

    CompletableFuture<ProcessResult> execute(Context context);
}
