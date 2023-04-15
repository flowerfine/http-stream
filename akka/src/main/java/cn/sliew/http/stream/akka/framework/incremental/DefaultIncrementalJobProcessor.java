package cn.sliew.http.stream.akka.framework.incremental;

import cn.sliew.http.stream.akka.framework.DefaultJobProcessor;
import cn.sliew.http.stream.akka.framework.ProcessResult;
import cn.sliew.http.stream.akka.framework.RootTask;
import cn.sliew.http.stream.akka.framework.SubTask;
import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.milky.common.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultIncrementalJobProcessor<Context extends IncrementalJobContext, Root extends RootTask, Sub extends SubTask>
        extends DefaultJobProcessor<Context, Root, Sub>
        implements IncrementalJobProcessor<Context, Root, Sub> {

    public DefaultIncrementalJobProcessor(Context context) {
        super(context);
    }

    @Override
    public ProcessResult reduce(ProcessResult result) {
        Context context = getContext();
        if (result.isSuccess() == false) {
            log.error("{} 子任务处理失败: {}!", context.getJobName(), result.getMessage(), result.getThrowable());
            if (result.getThrowable() != null) {
                Rethrower.throwAs(result.getThrowable());
            }
            throw new RuntimeException(result.getMessage());
        }
        SubTask subTask = result.getSubTask();
        log.debug("{} 子任务处理成功! 子任务 id: {}, 子任务: {}", context.getJobName(),
                subTask.getIdentifier(), JacksonUtil.toJsonString(subTask));
        context.updateSyncOffset(subTask);
        return ProcessResult.success(result.getSubTask());
    }
}
