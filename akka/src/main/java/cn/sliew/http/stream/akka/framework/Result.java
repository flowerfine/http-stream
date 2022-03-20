package cn.sliew.http.stream.akka.framework;

public interface Result {

    boolean isSuccess();

    String getMessage();

    Throwable getThrowable();

    SubTask getSubTask();
}
