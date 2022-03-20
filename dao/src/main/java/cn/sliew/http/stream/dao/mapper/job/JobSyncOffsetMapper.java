package cn.sliew.http.stream.dao.mapper.job;

import cn.sliew.http.stream.dao.entity.job.JobSyncOffset;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface JobSyncOffsetMapper extends BaseMapper<JobSyncOffset> {

    JobSyncOffset selectByMethod(@Param("method") String method);

    int insertSelective(JobSyncOffset record);

    int updateByMethod(JobSyncOffset record);
}