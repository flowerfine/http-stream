package cn.sliew.http.stream.akka.jst.order;

import akka.actor.typed.ActorSystem;
import cn.sliew.http.stream.akka.jst.JstIncrementalJobContext;
import cn.sliew.http.stream.common.util.DateUtil;
import cn.sliew.http.stream.dao.entity.job.JobSyncOffset;
import cn.sliew.http.stream.dao.mapper.job.JobSyncOffsetMapper;
import io.micrometer.core.instrument.MeterRegistry;

import java.time.LocalDateTime;
import java.util.Properties;

class OrderJobContext extends JstIncrementalJobContext {

    public OrderJobContext(String jobName,
                           Properties properties,
                           MeterRegistry meterRegistry,
                           ActorSystem actorSystem,
                           JobSyncOffsetMapper jobSyncOffsetMapper) {
        super(jobName, properties, meterRegistry, actorSystem, jobSyncOffsetMapper);
    }

    @Override
    protected JobSyncOffset initSyncOffset() {
        JobSyncOffset syncOffset = new JobSyncOffset();
        syncOffset.setMethod(getJobName());
        syncOffset.setStartTime(DateUtil.toDate(LocalDateTime.of(2021, 12, 31, 0, 0, 0)));
        syncOffset.setEndTime(DateUtil.toDate(LocalDateTime.of(2022, 1, 1, 0, 0, 0)));
        syncOffset.setCreator("sync-task");
        syncOffset.setModifier("sync-task");
        jobSyncOffsetMapper.insertSelective(syncOffset);
        return jobSyncOffsetMapper.selectByMethod(getJobName());
    }

}
