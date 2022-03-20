package cn.sliew.http.stream.akka.util;

public enum Rethrower {
    ;

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void throwAs(Throwable t) throws T {
        throw (T) t;
    }
}