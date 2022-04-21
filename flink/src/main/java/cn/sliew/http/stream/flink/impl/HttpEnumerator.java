package cn.sliew.http.stream.flink.impl;

import cn.sliew.http.stream.flink.HttpSourceSplit;
import cn.sliew.http.stream.flink.PendingSplitsCheckpoint;
import org.apache.flink.api.connector.source.SplitEnumerator;
import org.apache.flink.api.connector.source.SplitEnumeratorContext;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public class HttpEnumerator<SplitT extends HttpSourceSplit>
        implements SplitEnumerator<SplitT, PendingSplitsCheckpoint<SplitT>> {

    private final SplitEnumeratorContext<SplitT> context;

    public HttpEnumerator(SplitEnumeratorContext<SplitT> context) {
        this.context = context;
    }

    @Override
    public void start() {

    }

    @Override
    public void handleSplitRequest(int subtaskId, @Nullable String requesterHostname) {

    }

    @Override
    public void addSplitsBack(List<SplitT> splits, int subtaskId) {

    }

    @Override
    public void addReader(int subtaskId) {

    }

    @Override
    public PendingSplitsCheckpoint<SplitT> snapshotState(long checkpointId) throws Exception {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
