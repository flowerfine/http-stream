package cn.sliew.http.stream.flink.util;

import cn.sliew.http.stream.flink.HttpSourceSplit;

import java.util.Collection;
import java.util.function.Supplier;

public interface SplitHelper<SplitT extends HttpSourceSplit> {

    Collection<SplitT> split(Supplier<String> splitIdCreator, HttpSourceParameters parameters);

    CheckpointedPosition toCheckpoint(HttpSourceParameters parameters);
}
