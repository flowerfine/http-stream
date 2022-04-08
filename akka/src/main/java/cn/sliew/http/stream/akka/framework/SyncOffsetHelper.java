package cn.sliew.http.stream.akka.framework;

import akka.japi.Pair;
import cn.sliew.http.stream.akka.util.DateUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public enum  SyncOffsetHelper {
    ;

    public static boolean supportSplit(Date startTime, Date endTime, Duration gradient) {
        LocalDateTime start = DateUtil.toLocalDateTime(startTime);
        LocalDateTime end = DateUtil.toLocalDateTime(endTime);
        LocalDateTime nextStart = start.plus(gradient);
        return nextStart.isBefore(end);
    }

    public static List<Pair<Date, Date>> split(Date startTime, Date endTime, Duration gradient, int total) {
        LocalDateTime start = DateUtil.toLocalDateTime(startTime);
        LocalDateTime end = DateUtil.toLocalDateTime(endTime);

        List<Pair<Date, Date>> pairs = new LinkedList<>();
        LocalDateTime nextStart = start.plus(gradient);
        for (int i = 0; i < total; i++) {
            if (nextStart.isAfter(end)) {
                break;
            }
            pairs.add(new Pair<>(DateUtil.toDate(start), DateUtil.toDate(nextStart)));
            start = nextStart;
            nextStart = start.plus(gradient);
        }
        return pairs;
    }
}
