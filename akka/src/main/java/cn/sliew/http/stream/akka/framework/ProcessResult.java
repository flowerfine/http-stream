package cn.sliew.http.stream.akka.framework;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessResult implements Result {

    private boolean success = false;

    private String message;

    private Throwable throwable;

    private SubTask subTask;

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public SubTask getSubTask() {
        return subTask;
    }

    public static ProcessResult success(SubTask subTask) {
        ProcessResult result = new ProcessResult();
        result.setSuccess(true);
        result.setSubTask(subTask);
        result.setMessage("success");
        return result;
    }

    public static ProcessResult failure(SubTask subTask) {
        return failure(subTask, "internal error");
    }

    public static ProcessResult failure(SubTask subTask, String message) {
        ProcessResult result = new ProcessResult();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

    public static ProcessResult failure(SubTask subTask, Throwable throwable) {
        ProcessResult result = new ProcessResult();
        result.setSuccess(false);
        result.setThrowable(throwable);
        result.setMessage(throwable.getMessage());
        return result;
    }

}
