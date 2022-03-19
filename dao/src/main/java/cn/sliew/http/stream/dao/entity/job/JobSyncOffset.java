package cn.sliew.http.stream.dao.entity.job;

import cn.sliew.http.stream.dao.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 任务同步位点
 */
@Getter
@Setter
public class JobSyncOffset extends BaseEntity {

    /**
     * 接口名称
     */
    private String method;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;
}
