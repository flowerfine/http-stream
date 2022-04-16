package cn.sliew.http.stream.flink.enumerator;

import cn.sliew.http.stream.flink.HttpSourceSplit;
import cn.sliew.http.stream.flink.PendingSplitsCheckpoint;
import org.apache.flink.api.connector.source.SplitEnumerator;

public abstract class HttpEnumerator<SplitT extends HttpSourceSplit>
        implements SplitEnumerator<SplitT, PendingSplitsCheckpoint<SplitT>> {


}
