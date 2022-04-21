package cn.sliew.http.stream.flink.impl;

import cn.sliew.http.stream.common.util.DateUtil;
import cn.sliew.http.stream.flink.HttpSourceSplit;
import cn.sliew.http.stream.flink.PendingSplitsCheckpoint;
import cn.sliew.http.stream.flink.assigners.IntervalAssigner;
import cn.sliew.http.stream.flink.enumerator.IntervalEnumerator;
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
    private final IntervalEnumerator<SplitT> intervalEnumerator;
    private final IntervalAssigner intervalAssigner;

    private final Date startTime;

    private final LinkedHashMap<Integer, String> readersAwaitingSplit;

    public HttpEnumerator(SplitEnumeratorContext<SplitT> context, IntervalEnumerator<SplitT> intervalEnumerator, IntervalAssigner intervalAssigner, Date startTime) {
        this.context = context;
        this.intervalEnumerator = intervalEnumerator;
        this.intervalAssigner = intervalAssigner;
        this.startTime = startTime;

        this.readersAwaitingSplit = new LinkedHashMap<>();
    }

    @Override
    public void start() {
        Collection<SplitT> splitTS = intervalEnumerator.enumerateSplits(startTime, DateUtil.lastSecond());
        intervalAssigner.addSplits(splitTS);
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
        intervalAssigner.addSplits(splits);
    }

    @Override
    public void addReader(int subtaskId) {
// this source is purely lazy-pull-based, nothing to do upon registration
    }

    @Override
    public PendingSplitsCheckpoint<SplitT> snapshotState(long checkpointId) throws Exception {
        return PendingSplitsCheckpoint.fromCollectionSnapshot(intervalAssigner.remainingSplits());
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
            final Optional<SplitT> nextSplit = intervalAssigner.getNext(hostname);
            if (nextSplit.isPresent()) {
                context.assignSplit(nextSplit.get(), awaitingSubtask);
                awaitingReader.remove();
            } else {
                break;
            }
        }
    }
}
