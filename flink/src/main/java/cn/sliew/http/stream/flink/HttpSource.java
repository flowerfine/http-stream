package cn.sliew.http.stream.flink;

import cn.sliew.http.stream.flink.assigners.IntervalAssigner;
import cn.sliew.http.stream.flink.assigners.SimpleIntervalAssigner;
import cn.sliew.http.stream.flink.enumerator.HttpSourceSplitEnumerator;
import cn.sliew.http.stream.flink.impl.HttpEnumerator;
import cn.sliew.http.stream.flink.util.CheckpointedPosition;
import cn.sliew.http.stream.flink.util.HttpSourceParameters;
import org.apache.flink.api.connector.source.*;
import org.apache.flink.core.io.SimpleVersionedSerializer;

import java.util.ArrayList;
import java.util.Collection;

public class HttpSource<T, SplitT extends HttpSourceSplit>
        implements Source<T, SplitT, PendingSplitsCheckpoint<SplitT>> {

    private final HttpSourceSplitEnumerator.Provider splitEnumeratorFactory = null;
    private final CheckpointedPosition.Provider checkpointedPositionFactory = null;

    private final HttpSourceParameters parameters = null;

    @Override
    public Boundedness getBoundedness() {
        return Boundedness.CONTINUOUS_UNBOUNDED;
    }

    @Override
    public SourceReader<T, SplitT> createReader(SourceReaderContext sourceReaderContext) throws Exception {
        return null;
    }

    @Override
    public SplitEnumerator<SplitT, PendingSplitsCheckpoint<SplitT>> createEnumerator(SplitEnumeratorContext<SplitT> context) throws Exception {
        HttpSourceSplitEnumerator splitEnumerator = splitEnumeratorFactory.create();
        Collection<SplitT> splits = splitEnumerator.enumerateSplits(parameters);
        return createSplitEnumerator(context, splitEnumerator, splits);
    }

    @Override
    public SplitEnumerator<SplitT, PendingSplitsCheckpoint<SplitT>> restoreEnumerator(SplitEnumeratorContext<SplitT> context, PendingSplitsCheckpoint<SplitT> checkpoint) throws Exception {
        HttpSourceSplitEnumerator splitEnumerator = splitEnumeratorFactory.create();
        Collection<SplitT> splits = checkpoint.getSplits();
        return createSplitEnumerator(context, splitEnumerator, splits);
    }

    private SplitEnumerator<SplitT, PendingSplitsCheckpoint<SplitT>> createSplitEnumerator(
            SplitEnumeratorContext<SplitT> context,
            HttpSourceSplitEnumerator splitEnumerator,
            Collection<SplitT> splits) {

        IntervalAssigner<SplitT> intervalAssigner = new SimpleIntervalAssigner(new ArrayList<>(splits));
        return new HttpEnumerator<>(context, splitEnumerator, intervalAssigner, parameters);
    }

    @Override
    public SimpleVersionedSerializer<SplitT> getSplitSerializer() {
        return (SimpleVersionedSerializer<SplitT>) new HttpSourceSplitSerializer(checkpointedPositionFactory);
    }

    @Override
    public SimpleVersionedSerializer<PendingSplitsCheckpoint<SplitT>> getEnumeratorCheckpointSerializer() {
        return new PendingSplitsCheckpointSerializer(getSplitSerializer());
    }
}
