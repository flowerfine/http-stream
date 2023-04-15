package cn.sliew.http.stream.akka.jst;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public abstract class JstIncrementalSubTask<Context extends JstIncrementalJobContext, Query, Result> extends JstSubTask<Context, Query, Result> {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private final Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private final Date endTime;

    public JstIncrementalSubTask(Long identifier, Date startTime, Date endTime) {
        super(identifier);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}
