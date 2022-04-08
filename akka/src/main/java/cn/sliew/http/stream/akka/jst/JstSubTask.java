package cn.sliew.http.stream.akka.jst;

import cn.sliew.http.stream.akka.framework.batch.AbstractSubTask;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class JstSubTask<Context extends JstIncrementalJobContext, Query, Result>
        extends AbstractSubTask<Context, FetchResult<Query, Result>> {



    public JstSubTask(Long identifier, Date startTime, Date endTime) {
        super(identifier);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    protected abstract Query getQuery();

    protected abstract Result queryJst(Context context, Query query);

    protected abstract void persistData(Context context, Query query, Result result);
}