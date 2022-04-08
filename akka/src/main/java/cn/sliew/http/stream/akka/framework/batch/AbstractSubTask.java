package cn.sliew.http.stream.akka.framework.batch;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import cn.sliew.http.stream.akka.framework.JobContext;
import cn.sliew.http.stream.akka.framework.ProcessResult;
import cn.sliew.http.stream.akka.framework.SubTask;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public abstract class AbstractSubTask<Context extends JobContext, T> implements SubTask<Context> {

    private final Long identifier;

    public AbstractSubTask(Long identifier) {
        this.identifier = identifier;
    }

    @Override
    public Long getIdentifier() {
        return identifier;
    }

    @Override
    public CompletableFuture<ProcessResult> execute(Context context) {
        Source<T, NotUsed> source = fetch(context);
        CompletionStage completionStage = source.runForeach(data -> persist(context, data), context.getActorSystem());
        return completionStage.thenApply(done -> ProcessResult.success(this)).toCompletableFuture();
    }

    protected abstract Source<T, NotUsed> fetch(Context context);

    protected abstract void persist(Context context, T data);
}
