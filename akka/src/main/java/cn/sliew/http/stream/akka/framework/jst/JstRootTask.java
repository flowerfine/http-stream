package cn.sliew.http.stream.akka.framework.jst;

import akka.japi.Pair;
import cn.sliew.http.stream.akka.framework.RootTask;
import cn.sliew.http.stream.akka.framework.SyncOffsetHelper;
import cn.sliew.http.stream.akka.util.DateUtil;
import cn.sliew.http.stream.dao.entity.job.JobSyncOffset;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.*;

@Slf4j
public class JstRootTask implements RootTask<JobSyncOffset, JstSubTask> {

    /**
     * 默认的时间梯度为: 1h, 30min, 15min, 5min, 2min, 1min, 30s, 15s, 10s, 5s
     * 其中 1h, 30min, 15min, 5min 为了处理历史数据, 2min, 1min, 30s, 15s, 10s, 5s 是为了实时数据
     */
    private final List<Duration> gradients = Arrays.asList(Duration.ofHours(1L),
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
    public boolean supportSplit(JobSyncOffset jstSyncOffset) {
        Date startTime = jstSyncOffset.getEndTime();
        Date endTime = DateUtil.lastSecond();
        return gradients.stream().anyMatch(gradient -> SyncOffsetHelper.supportSplit(startTime, endTime, gradient));
    }

    @Override
    public List<JstSubTask> split(JobSyncOffset jstSyncOffset) {
        Date startTime = jstSyncOffset.getEndTime();
        Date endTime = DateUtil.lastSecond();
        Optional<Duration> optional = gradients.stream()
                .filter(gradient -> SyncOffsetHelper.supportSplit(startTime, endTime, gradient))
                .findFirst();
        if (optional.isPresent() == false) {
            return Collections.emptyList();
        }
        List<Pair<Date, Date>> pairs = SyncOffsetHelper.split(startTime, endTime, optional.get(), 15);

        List<JstSubTask> subTasks = new ArrayList<>(pairs.size());
        for (int i = 0; i < pairs.size(); i++) {
            Pair<Date, Date> pair = pairs.get(i);
            subTasks.add(new JstSubTask(Long.valueOf(i), pair.first(), pair.second()));
        }
        return subTasks;
    }
}
