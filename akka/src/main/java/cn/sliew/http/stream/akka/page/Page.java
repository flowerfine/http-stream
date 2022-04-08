package cn.sliew.http.stream.akka.page;

import java.util.Optional;

public interface Page<P, T> extends Iterable<T> {

    Optional<P> getNextPage();
}
