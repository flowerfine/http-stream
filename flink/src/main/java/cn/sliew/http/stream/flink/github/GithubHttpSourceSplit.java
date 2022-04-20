package cn.sliew.http.stream.flink.github;

import cn.sliew.http.stream.flink.HttpSourceSplit;

import java.util.Date;

public class GithubHttpSourceSplit extends HttpSourceSplit {

    private final Date startTime;
    private final Date endTime;

    public GithubHttpSourceSplit(String id, Date startTime, Date endTime) {
        super(id);
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
