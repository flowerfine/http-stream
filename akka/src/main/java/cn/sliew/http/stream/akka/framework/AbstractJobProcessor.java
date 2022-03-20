package cn.sliew.http.stream.akka.framework;

import cn.sliew.http.stream.akka.util.JacksonUtil;
import cn.sliew.http.stream.akka.util.Rethrower;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public abstract class AbstractJobProcessor<Root extends RootTask, Sub extends SubTask, SyncOffset>
        implements JobProcessor<Root, Sub, SyncOffset> {

    @Override
    public List<Sub> map(JobContext<SyncOffset, Root, Sub> context, Root rootTask) {
        SyncOffset syncOffset = context.getSyncOffset(rootTask);
        if (rootTask.supportSplit(syncOffset)) {
            return rootTask.split(syncOffset);
        }
        return Collections.emptyList();
    }

    @Override
    public ProcessResult reduce(JobContext<SyncOffset, Root, Sub> context, ProcessResult result) {
        if (result.isSuccess()) {
            SubTask subTask = result.getSubTask();
            log.debug("{} 子任务处理成功: {}! 子任务 id: {}, 子任务: {}", context.getJobName(), result.getMessage(),
                    subTask.getIdentifier(), JacksonUtil.toJsonString(subTask));
            context.updateSyncOffset((Sub) result.getSubTask());
            return ProcessResult.success(result.getSubTask());
        }

        log.error("{} 子任务处理失败: {}!", context.getJobName(), result.getMessage(), result.getThrowable());
        if (result.getThrowable() != null) {
            Rethrower.throwAs(result.getThrowable());
        }
        throw new RuntimeException(result.getMessage());
    }
}
