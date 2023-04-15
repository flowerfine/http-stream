package cn.sliew.http.stream.akka.jst;

import cn.sliew.http.stream.akka.framework.batch.AbstractSubTask;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class JstSubTask<Context extends JstIncrementalJobContext, Query, Result>
        extends AbstractSubTask<Context, FetchResult<Query, Result>> {

    public JstSubTask(Long identifier) {
        super(identifier);
    }

    protected abstract Result queryJst(Context context, Query query);

    protected abstract void persistData(Context context, Query query, Result result);
}
