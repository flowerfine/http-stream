package cn.sliew.http.stream.flink.enumerator;

import cn.sliew.http.stream.flink.HttpSourceSplit;
import cn.sliew.http.stream.flink.util.HttpSourceParameters;

import java.io.Serializable;
import java.util.Collection;

public interface HttpSourceSplitEnumerator<SplitT extends HttpSourceSplit> {

    Collection<SplitT> enumerateSplits(HttpSourceParameters parameters);

    /**
     * Factory for the {@code HttpSourceEnumerator}.
     */
    @FunctionalInterface
    interface Provider extends Serializable {

        HttpSourceSplitEnumerator create();
    }
}
