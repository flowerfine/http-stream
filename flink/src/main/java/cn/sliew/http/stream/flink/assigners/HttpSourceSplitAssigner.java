package cn.sliew.http.stream.flink.assigners;

import cn.sliew.http.stream.flink.HttpSourceSplit;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

public interface HttpSourceSplitAssigner<SplitT extends HttpSourceSplit> {

    /**
     * Gets the next split.
     *
     * <p>When this method returns an empty {@code Optional}, then the set of splits is assumed to
     * be done and the source will finish once the readers finished their current splits.
     */
    Optional<SplitT> getNext(@Nullable String hostname);

    /**
     * Adds a set of splits to this assigner. This happens for example when some split processing
     * failed and the splits need to be re-added, or when new splits got discovered.
     */
    void addSplits(Collection<SplitT> splits);

    /** Gets the remaining splits that this assigner has pending. */
    Collection<SplitT> remainingSplits();

    // ------------------------------------------------------------------------

    /**
     * Factory for the {@code IntervalAssigner}, to allow the {@code IntervalAssigner} to be
     * eagerly initialized and to not be serializable.
     */
    @FunctionalInterface
    interface Provider extends Serializable {

        /**
         * Creates a new {@code IntervalAssigner} that starts with the given set of initial splits.
         */
        HttpSourceSplitAssigner create(Collection<HttpSourceSplit> initialSplits);
    }
}
