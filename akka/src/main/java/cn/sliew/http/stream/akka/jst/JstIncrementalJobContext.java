package cn.sliew.http.stream.akka.jst;

import akka.actor.typed.ActorSystem;
import cn.sliew.http.stream.akka.framework.batch.BatchJobContext;
import cn.sliew.http.stream.akka.framework.incremental.IncrementalJobContext;
import cn.sliew.http.stream.dao.entity.job.JobSyncOffset;
import cn.sliew.http.stream.dao.mapper.job.JobSyncOffsetMapper;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.Properties;

public abstract class JstIncrementalJobContext extends BatchJobContext<JstIncrementalRootTask, JstSubTask>
        implements IncrementalJobContext<JobSyncOffset, JstIncrementalRootTask, JstSubTask> {

    protected final JobSyncOffsetMapper jobSyncOffsetMapper;

    public JstIncrementalJobContext(String jobName,
                                    Properties properties,
                                    MeterRegistry meterRegistry,
                                    ActorSystem actorSystem,
                                    JobSyncOffsetMapper jobSyncOffsetMapper) {
        super(jobName, properties, meterRegistry, actorSystem);
        this.jobSyncOffsetMapper = jobSyncOffsetMapper;
    }

    @Override
    public JobSyncOffset getSyncOffset(JstIncrementalRootTask rootTask) {
        JobSyncOffset jstSyncOffset = jobSyncOffsetMapper.selectByMethod(getJobName());
        if (jstSyncOffset != null) {
            return jstSyncOffset;
        }

        return initSyncOffset();
    }

    @Override
    public void updateSyncOffset(JstSubTask subTask) {
        JobSyncOffset syncOffset = new JobSyncOffset();
        syncOffset.setMethod(getJobName());
        syncOffset.setStartTime(subTask.getStartTime());
        syncOffset.setEndTime(subTask.getEndTime());
        syncOffset.setModifier("sync-task");
        jobSyncOffsetMapper.updateByMethod(syncOffset);
    }

    protected abstract JobSyncOffset initSyncOffset();
}
