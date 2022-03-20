package cn.sliew.http.stream.akka.framework.jst;

import cn.sliew.http.stream.akka.framework.JobContext;
import cn.sliew.http.stream.dao.entity.job.JobSyncOffset;
import cn.sliew.http.stream.dao.mapper.job.JobSyncOffsetMapper;

public abstract class JstJobContext implements JobContext<JobSyncOffset, JstRootTask, JstSubTask> {

    protected final JobSyncOffsetMapper jobSyncOffsetMapper;

    public JstJobContext(JobSyncOffsetMapper jobSyncOffsetMapper) {
        this.jobSyncOffsetMapper = jobSyncOffsetMapper;
    }

    @Override
    public JobSyncOffset getSyncOffset(JstRootTask rootTask) {
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
