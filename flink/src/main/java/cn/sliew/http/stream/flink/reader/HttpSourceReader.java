package cn.sliew.http.stream.flink.reader;

import cn.sliew.http.stream.flink.HttpSourceSplit;
import cn.sliew.http.stream.flink.HttpSourceSplitState;
import org.apache.flink.api.connector.source.SourceReaderContext;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.base.source.reader.RecordEmitter;
import org.apache.flink.connector.base.source.reader.RecordsWithSplitIds;
import org.apache.flink.connector.base.source.reader.SourceReaderBase;
import org.apache.flink.connector.base.source.reader.fetcher.SplitFetcherManager;
import org.apache.flink.connector.base.source.reader.synchronization.FutureCompletingBlockingQueue;

public abstract class HttpSourceReader<E, T, SplitT extends HttpSourceSplit>
        extends SourceReaderBase<E, T, SplitT, HttpSourceSplitState<SplitT>> {

    public HttpSourceReader(FutureCompletingBlockingQueue<RecordsWithSplitIds<E>> elementsQueue, SplitFetcherManager<E, SplitT> splitFetcherManager, RecordEmitter<E, T, HttpSourceSplitState<SplitT>> recordEmitter, Configuration config, SourceReaderContext context) {
        super(elementsQueue, splitFetcherManager, recordEmitter, config, context);
    }
}
