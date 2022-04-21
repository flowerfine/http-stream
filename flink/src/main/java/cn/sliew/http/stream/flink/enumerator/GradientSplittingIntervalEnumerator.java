package cn.sliew.http.stream.flink.enumerator;

import cn.sliew.http.stream.common.util.SyncOffsetHelper;
import cn.sliew.http.stream.flink.HttpSourceSplit;
import cn.sliew.milky.common.collect.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GradientSplittingIntervalEnumerator<SplitT extends HttpSourceSplit>
        extends NonSplittingIntervalEnumerator<SplitT> {

    private List<Duration> gradients;

    public GradientSplittingIntervalEnumerator(List<Duration> gradients) {
        this.gradients = gradients;
    }

    @Override
    protected List<Tuple2<Date, Date>> split(Date startTime, Date endTime) {
        Optional<Duration> optional = gradients.stream()
                .filter(gradient -> SyncOffsetHelper.supportSplit(startTime, endTime, gradient))
                .findFirst();
        if (optional.isPresent() == false) {
            return Collections.singletonList(new Tuple2<>(startTime, endTime));
        }
        List<Tuple<Date, Date>> tuples = SyncOffsetHelper.split(startTime, endTime, optional.get(), Integer.MAX_VALUE);
        return tuples.stream().map(tuple -> new Tuple2(tuple.v1(), tuple.v2())).collect(Collectors.toList());
    }
}
