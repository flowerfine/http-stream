package cn.sliew.http.stream.flink.enumerator;

import cn.sliew.http.stream.flink.HttpSourceSplit;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.Date;
import java.util.List;

public class GradientSplittingIntervalEnumerator<SplitT extends HttpSourceSplit>
        extends NonSplittingIntervalEnumerator<SplitT> {

    @Override
    protected List<Tuple2<Date, Date>> split(Date startTime, Date endTime) {
        return super.split(startTime, endTime);
    }


}
