package cn.sliew.http.stream.flink.impl;

import cn.sliew.http.stream.flink.HttpSourceSplit;
import cn.sliew.http.stream.flink.HttpSourceSplitState;
import cn.sliew.http.stream.flink.util.RecordsAndPosition;
import org.apache.flink.api.connector.source.SourceOutput;
import org.apache.flink.connector.base.source.reader.RecordEmitter;

public class HttpSourceRecordEmitter<T, SplitT extends HttpSourceSplit>
        implements RecordEmitter<RecordsAndPosition<T>, T, HttpSourceSplitState<SplitT>> {

    @Override
    public void emitRecord(
            RecordsAndPosition<T> elements,
            SourceOutput<T> output,
            HttpSourceSplitState<SplitT> splitState) throws Exception {
        elements.getRecords().forEach(output::collect);
        splitState.setPosition(elements.getPageIndex(), elements.getPageSize());
    }
}
