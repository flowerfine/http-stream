package cn.sliew.http.stream.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public enum DateUtil {
    ;

    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME);

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date currentHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date now() {
        return new Date();
    }

    public static Date lastSecond() {
        LocalDateTime lastSecond = LocalDateTime.now().minusSeconds(1L);
        return toDate(lastSecond);
    }

    public static String formatDateTime(Date date) {
        return DATE_TIME_FORMATTER.format(toLocalDateTime(date));
    }

    public static Date parseDateTime(String dateTime) {
        return toDate((LocalDateTime) DATE_TIME_FORMATTER.parse(dateTime));
    }

    public static Date plusSeconds(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    public static boolean isSameHour(Date first, Date second) {
        if (first == null || second == null) {
            return false;
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(first);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(second);
        return isSameHour(calendar1, calendar2);
    }

    public static boolean isSameHour(Calendar first, Calendar second) {
        if (first == null || second == null) {
            return false;
        }
        return first.get(Calendar.ERA) == second.get(Calendar.ERA) &&
                first.get(Calendar.YEAR) == second.get(Calendar.YEAR) &&
                first.get(Calendar.DAY_OF_YEAR) == second.get(Calendar.DAY_OF_YEAR) &&
                first.get(Calendar.HOUR_OF_DAY) == second.get(Calendar.HOUR_OF_DAY);
    }

    public static boolean isSameDay(Date first, Date second) {
        if (first == null || second == null) {
            return false;
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(first);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(second);
        return isSameDay(calendar1, calendar2);
    }

    public static boolean isSameDay(Calendar first, Calendar second) {
        if (first == null || second == null) {
            return false;
        }

        return first.get(Calendar.ERA) == second.get(Calendar.ERA) &&
                first.get(Calendar.YEAR) == second.get(Calendar.YEAR) &&
                first.get(Calendar.DAY_OF_YEAR) == second.get(Calendar.DAY_OF_YEAR);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Long toTimestamp(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
