package cn.sliew.http.stream.flink;

import cn.sliew.http.stream.common.util.DateUtil;
import cn.sliew.http.stream.flink.assigners.IntervalAssigner;
import cn.sliew.http.stream.flink.assigners.SimpleIntervalAssigner;
import cn.sliew.http.stream.flink.enumerator.GradientSplittingIntervalEnumerator;
import cn.sliew.http.stream.flink.enumerator.IntervalEnumerator;
import cn.sliew.http.stream.flink.impl.HttpEnumerator;
import org.apache.flink.api.connector.source.*;
import org.apache.flink.core.io.SimpleVersionedSerializer;

import java.time.Duration;
import java.util.*;

public class HttpSource<T, SplitT extends HttpSourceSplit> implements Source<T, SplitT, PendingSplitsCheckpoint<SplitT>> {

    /**
     * 默认的时间梯度为: 1h, 30min, 15min, 5min, 2min, 1min, 30s, 15s, 10s, 5s
     * 其中 1h, 30min, 15min, 5min 为了处理历史数据, 2min, 1min, 30s, 15s, 10s, 5s 是为了实时数据
     */
    private final List<Duration> gradients = Arrays.asList(Duration.ofDays(5L),
            Duration.ofMinutes(30L),
            Duration.ofMinutes(15L),
            Duration.ofMinutes(5L),
            Duration.ofMinutes(2L),
            Duration.ofMinutes(1L),
            Duration.ofSeconds(30L),
            Duration.ofSeconds(15L),
            Duration.ofSeconds(10L),
            Duration.ofSeconds(5L));

    private final Date startTime = DateUtil.currentDate();

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
        IntervalEnumerator<SplitT> intervalEnumerator = new GradientSplittingIntervalEnumerator<>(gradients);
        Collection<SplitT> splits = intervalEnumerator.enumerateSplits(startTime, DateUtil.lastSecond());
        return createSplitEnumerator(context, intervalEnumerator, splits);
    }

    @Override
    public SplitEnumerator<SplitT, PendingSplitsCheckpoint<SplitT>> restoreEnumerator(SplitEnumeratorContext<SplitT> context, PendingSplitsCheckpoint<SplitT> checkpoint) throws Exception {
        IntervalEnumerator<SplitT> intervalEnumerator = new GradientSplittingIntervalEnumerator<>(gradients);
        Collection<SplitT> splits = checkpoint.getSplits();
        return createSplitEnumerator(context, intervalEnumerator, splits);
    }

    private SplitEnumerator<SplitT, PendingSplitsCheckpoint<SplitT>> createSplitEnumerator(
            SplitEnumeratorContext<SplitT> context,
            IntervalEnumerator intervalEnumerator,
            Collection<SplitT> splits) {

        IntervalAssigner<SplitT> intervalAssigner = new SimpleIntervalAssigner(new ArrayList<>(splits));
        return new HttpEnumerator<>(context, intervalEnumerator, intervalAssigner, startTime);
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
