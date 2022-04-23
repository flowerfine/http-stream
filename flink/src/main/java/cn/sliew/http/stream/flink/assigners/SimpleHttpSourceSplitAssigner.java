package cn.sliew.http.stream.flink.assigners;

import cn.sliew.http.stream.flink.HttpSourceSplit;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class SimpleHttpSourceSplitAssigner<SplitT extends HttpSourceSplit>
        implements HttpSourceSplitAssigner<SplitT> {

    private final ArrayList<SplitT> splits;

    public SimpleHttpSourceSplitAssigner(ArrayList<SplitT> splits) {
        this.splits = splits;
    }

    @Override
    public Optional<SplitT> getNext(@Nullable String hostname) {
        final int size = splits.size();
        return size == 0 ? Optional.empty() : Optional.of(splits.remove(size - 1));
    }

    @Override
    public void addSplits(Collection<SplitT> splits) {
        this.splits.addAll(splits);
    }

    @Override
    public Collection<SplitT> remainingSplits() {
        return splits;
    }
}
