package cn.sliew.http.stream.akka.jst;

import cn.sliew.http.stream.akka.framework.ProcessResult;
import cn.sliew.http.stream.akka.framework.SubTask;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
public abstract class JstSubTask<Context extends JstJobContext> implements SubTask<Context> {

    private Long identifier;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @Override
    public Long getIdentifier() {
        return identifier;
    }

    @Override
    public CompletableFuture<ProcessResult> execute(Context context) {
        return null;
    }
}
