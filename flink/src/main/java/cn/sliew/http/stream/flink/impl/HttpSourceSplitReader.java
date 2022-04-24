package cn.sliew.http.stream.flink.impl;

import cn.sliew.http.stream.flink.HttpSourceSplit;
import cn.sliew.http.stream.flink.reader.BulkFormat;
import cn.sliew.http.stream.flink.util.RecordsAndPosition;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.connector.base.source.reader.RecordsWithSplitIds;
import org.apache.flink.connector.base.source.reader.splitreader.SplitReader;
import org.apache.flink.connector.base.source.reader.splitreader.SplitsAddition;
import org.apache.flink.connector.base.source.reader.splitreader.SplitsChange;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

@Slf4j
public class HttpSourceSplitReader <T, SplitT extends HttpSourceSplit>
        implements SplitReader<RecordsAndPosition<T>, SplitT> {

    private final Queue<SplitT> splits;

    @Nullable
    private BulkFormat.Reader<T> currentReader;
    @Nullable
    private String currentSplitId;

    public HttpSourceSplitReader() {
        this.splits = new ArrayDeque<>();
    }

    @Override
    public RecordsWithSplitIds<RecordsAndPosition<T>> fetch() throws IOException {
        return null;
    }

    @Override
    public void handleSplitsChanges(SplitsChange<SplitT> splitsChange) {
        if (!(splitsChange instanceof SplitsAddition)) {
            throw new UnsupportedOperationException(
                    String.format(
                            "The SplitChange type of %s is not supported.",
                            splitsChange.getClass()));
        }

        log.debug("Handling split change {}", splitsChange);
        splits.addAll(splitsChange.splits());
    }

    @Override
    public void wakeUp() {

    }

    @Override
    public void close() throws Exception {
        if (currentReader != null) {
            currentReader.close();
        }
    }
}
