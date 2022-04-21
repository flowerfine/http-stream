package cn.sliew.http.stream.flink.enumerator;

import cn.sliew.http.stream.flink.HttpSourceSplit;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public interface IntervalEnumerator<SplitT extends HttpSourceSplit> {

    Collection<SplitT> enumerateSplits(Date startTime, Date endTime);

    /**
     * Factory for the {@code HttpSourceEnumerator}.
     */
    @FunctionalInterface
    interface Provider extends Serializable {

        IntervalEnumerator create();
    }
}
