package cn.sliew.http.stream.flink.impl;

import cn.sliew.http.stream.flink.HttpSourceSplit;
import cn.sliew.http.stream.flink.PendingSplitsCheckpoint;
import cn.sliew.http.stream.flink.assigners.HttpSourceSplitAssigner;
import cn.sliew.http.stream.flink.enumerator.HttpSourceSplitEnumerator;
import cn.sliew.http.stream.flink.util.HttpSourceParameters;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.connector.source.SplitEnumerator;
import org.apache.flink.api.connector.source.SplitEnumeratorContext;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

@Slf4j
public class HttpEnumerator<SplitT extends HttpSourceSplit>
        implements SplitEnumerator<SplitT, PendingSplitsCheckpoint<SplitT>> {

    private final SplitEnumeratorContext<SplitT> context;
    private final HttpSourceSplitEnumerator<SplitT> splitEnumerator;
    private final HttpSourceSplitAssigner splitAssigner;
    private final HttpSourceParameters parameters;

    private final LinkedHashMap<Integer, String> readersAwaitingSplit;

    public HttpEnumerator(SplitEnumeratorContext<SplitT> context, HttpSourceSplitEnumerator<SplitT> splitEnumerator, HttpSourceSplitAssigner splitAssigner, HttpSourceParameters parameters) {
        this.context = context;
        this.splitEnumerator = splitEnumerator;
        this.splitAssigner = splitAssigner;
        this.parameters = parameters;

        this.readersAwaitingSplit = new LinkedHashMap<>();
    }

    @Override
    public void start() {
        Collection<SplitT> splitTS = splitEnumerator.enumerateSplits(parameters);
        splitAssigner.addSplits(splitTS);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void handleSplitRequest(int subtaskId, @Nullable String requesterHostname) {
        readersAwaitingSplit.put(subtaskId, requesterHostname);
        assignSplits();
    }

    @Override
    public void addSplitsBack(List<SplitT> splits, int subtaskId) {
        log.debug("Http Source Enumerator adds splits back: {}", splits);
        splitAssigner.addSplits(splits);
    }

    @Override
    public void addReader(int subtaskId) {
//        this source is purely lazy-pull-based, nothing to do upon registration
    }

    @Override
    public PendingSplitsCheckpoint<SplitT> snapshotState(long checkpointId) throws Exception {
        return PendingSplitsCheckpoint.fromCollectionSnapshot(splitAssigner.remainingSplits());
    }

    private void assignSplits() {
        final Iterator<Map.Entry<Integer, String>> awaitingReader =
                readersAwaitingSplit.entrySet().iterator();

        while (awaitingReader.hasNext()) {
            final Map.Entry<Integer, String> nextAwaiting = awaitingReader.next();

            // if the reader that requested another split has failed in the meantime, remove
            // it from the list of waiting readers
            if (!context.registeredReaders().containsKey(nextAwaiting.getKey())) {
                awaitingReader.remove();
                continue;
            }

            final int awaitingSubtask = nextAwaiting.getKey();
            final String hostname = nextAwaiting.getValue();
            final Optional<SplitT> nextSplit = splitAssigner.getNext(hostname);
            if (nextSplit.isPresent()) {
                context.assignSplit(nextSplit.get(), awaitingSubtask);
                awaitingReader.remove();
            } else {
                break;
            }
        }
    }
}
