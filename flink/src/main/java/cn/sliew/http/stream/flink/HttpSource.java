package cn.sliew.http.stream.flink;

import org.apache.flink.api.connector.source.*;
import org.apache.flink.core.io.SimpleVersionedSerializer;

public class HttpSource<T, SplitT extends HttpSourceSplit> implements Source<T, SplitT, PendingSplitsCheckpoint<SplitT>> {

    @Override
    public Boundedness getBoundedness() {
        return Boundedness.CONTINUOUS_UNBOUNDED;
    }

    @Override
    public SourceReader<T, SplitT> createReader(SourceReaderContext sourceReaderContext) throws Exception {
        return null;
    }

    @Override
    public SplitEnumerator<SplitT, PendingSplitsCheckpoint<SplitT>> createEnumerator(SplitEnumeratorContext<SplitT> splitEnumeratorContext) throws Exception {
        return null;
    }

    @Override
    public SplitEnumerator<SplitT, PendingSplitsCheckpoint<SplitT>> restoreEnumerator(SplitEnumeratorContext<SplitT> splitEnumeratorContext, PendingSplitsCheckpoint<SplitT> splitTPendingSplitsCheckpoint) throws Exception {
        return null;
    }

    @Override
    public SimpleVersionedSerializer<SplitT> getSplitSerializer() {
        return (SimpleVersionedSerializer<SplitT>) HttpSourceSplitSerializer.INSTANCE;
    }

    @Override
    public SimpleVersionedSerializer<PendingSplitsCheckpoint<SplitT>> getEnumeratorCheckpointSerializer() {
        return new PendingSplitsCheckpointSerializer(getSplitSerializer());
    }
}
