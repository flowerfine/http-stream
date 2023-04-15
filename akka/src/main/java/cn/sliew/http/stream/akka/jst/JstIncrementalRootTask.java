package cn.sliew.http.stream.akka.jst;

import cn.sliew.http.stream.akka.framework.RootTask;
import cn.sliew.http.stream.common.util.DateUtil;
import cn.sliew.http.stream.common.util.SyncOffsetHelper;
import cn.sliew.http.stream.dao.entity.job.JobSyncOffset;
import cn.sliew.milky.common.collect.Tuple;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.*;

@Slf4j
public abstract class JstIncrementalRootTask implements RootTask<JstIncrementalJobContext, JstSubTask> {

    /**
     * 默认的时间梯度为: 1h, 30min, 15min, 5min, 2min, 1min, 30s, 15s, 10s, 5s
     * 其中 1h, 30min, 15min, 5min 为了处理历史数据, 2min, 1min, 30s, 15s, 10s, 5s 是为了实时数据
     */
    private final List<Duration> gradients = Arrays.asList(Duration.ofDays(5L),
            Duration.ofMinutes(30L),
            Duration.ofMinutes(15L),
            Duration.ofMinutes(5L),
            Duration.ofMinutes(2L),
            Duration.ofMinutes(1L),
            Duration.ofSeconds(30L),
            Duration.ofSeconds(15L),
            Duration.ofSeconds(10L),
            Duration.ofSeconds(5L));

    @Override
    public List<JstSubTask> split(JstIncrementalJobContext context) {
        JobSyncOffset syncOffset = context.getSyncOffset(this);
        Date startTime = syncOffset.getEndTime();
        Date endTime = DateUtil.lastSecond();
        Optional<Duration> optional = gradients.stream()
                .filter(gradient -> SyncOffsetHelper.supportSplit(startTime, endTime, gradient))
                .findFirst();
        if (optional.isPresent() == false) {
            return Collections.emptyList();
        }
        List<Tuple<Date, Date>> pairs = SyncOffsetHelper.split(startTime, endTime, optional.get(), 100);

        List<JstSubTask> subTasks = new ArrayList<>(pairs.size());
        for (int i = 0; i < pairs.size(); i++) {
            Tuple<Date, Date> tuple = pairs.get(i);
            subTasks.add(buildSubTask(Long.valueOf(i), tuple.v1(), tuple.v2()));
        }
        return subTasks;
    }

    protected abstract JstSubTask buildSubTask(Long id, Date startTime, Date endTime);

}
