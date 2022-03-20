package cn.sliew.http.stream.akka.framework.jst.order;

import cn.sliew.http.stream.akka.framework.jst.JstJobContext;
import cn.sliew.http.stream.akka.framework.jst.JstMethodEnum;
import cn.sliew.http.stream.akka.util.DateUtil;
import cn.sliew.http.stream.dao.entity.job.JobSyncOffset;
import cn.sliew.http.stream.dao.mapper.job.JobSyncOffsetMapper;

import java.time.LocalDateTime;

class OrderJobContext extends JstJobContext {

    public OrderJobContext(JobSyncOffsetMapper jobSyncOffsetMapper) {
        super(jobSyncOffsetMapper);
    }

    @Override
    public String getJobName() {
        return JstMethodEnum.ORDERS_SINGLE_QUERY.getMethod();
    }

    @Override
    protected JobSyncOffset initSyncOffset() {
        JobSyncOffset syncOffset = new JobSyncOffset();
        syncOffset.setMethod(getJobName());
        syncOffset.setStartTime(DateUtil.toDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0)));
        syncOffset.setEndTime(DateUtil.toDate(LocalDateTime.of(2020, 1, 2, 0, 0, 0)));
        syncOffset.setCreator("sync-task");
        syncOffset.setModifier("sync-task");
        jobSyncOffsetMapper.insertSelective(syncOffset);
        return jobSyncOffsetMapper.selectByMethod(getJobName());
    }


}
