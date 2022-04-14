package cn.sliew.http.stream.format.http;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SplitSource<Root, Sub, T> {

    List<Sub> map(Root rootTask);

    CompletableFuture<T> process(Sub subTask);
}
