package cn.sliew.http.stream.flink.enumerator;

import cn.sliew.http.stream.flink.HttpSourceSplit;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.*;

public class NonSplittingIntervalEnumerator<SplitT extends HttpSourceSplit>
        implements IntervalEnumerator<SplitT> {

    /**
     * The current Id as a mutable string representation. This covers more values than the integer
     * value range, so we should never overflow.
     */
    private final char[] currentId = "0000000000".toCharArray();

    @Override
    public Collection<SplitT> enumerateSplits(Date startTime, Date endTime) {
        List<Tuple2<Date, Date>> tuple2s = split(startTime, endTime);
        List<HttpSourceSplit> splits = new ArrayList<>(tuple2s.size());
        for (Tuple2<Date, Date> tuple2 : tuple2s) {
            HttpSourceSplit split = new HttpSourceSplit(getNextId(), tuple2.f0, tuple2.f1);
            splits.add(split);
        }
        return (Collection<SplitT>) Collections.unmodifiableCollection(splits);
    }

    protected List<Tuple2<Date, Date>> split(Date startTime, Date endTime) {
        return Collections.singletonList(new Tuple2<>(startTime, endTime));
    }

    protected final String getNextId() {
        // because we just increment numbers, we increment the char representation directly,
        // rather than incrementing an integer and converting it to a string representation
        // every time again (requires quite some expensive conversion logic).
        incrementCharArrayByOne(currentId, currentId.length - 1);
        return new String(currentId);
    }

    private static void incrementCharArrayByOne(char[] array, int pos) {
        char c = array[pos];
        c++;

        if (c > '9') {
            c = '0';
            incrementCharArrayByOne(array, pos - 1);
        }
        array[pos] = c;
    }
}
