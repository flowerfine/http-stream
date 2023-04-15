package cn.sliew.http.stream.flink;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;

import static org.apache.flink.util.Preconditions.checkNotNull;

public class PendingSplitsCheckpoint<SplitT extends HttpSourceSplit> {

    private final Collection<SplitT> splits;

    @Nullable
    byte[] serializedFormCache;

    public PendingSplitsCheckpoint(Collection<SplitT> splits) {
        this.splits = splits;
    }

    public Collection<SplitT> getSplits() {
        return splits;
    }

    public static <T extends HttpSourceSplit> PendingSplitsCheckpoint<T> fromCollectionSnapshot(
            final Collection<T> splits) {
        checkNotNull(splits);
        // create a copy of the collection to make sure this checkpoint is immutable
        return new PendingSplitsCheckpoint<>(new ArrayList<>(splits));
    }
}
