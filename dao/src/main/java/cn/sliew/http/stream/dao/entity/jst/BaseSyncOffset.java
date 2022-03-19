package cn.sliew.http.stream.dao.entity.jst;

import cn.sliew.http.stream.dao.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BaseSyncOffset extends BaseEntity {

    /**
     * 修改起始时间
     */
    private Date modifiedBegin;

    /**
     * 修改结束时间
     */
    private Date modifiedEnd;

    /**
     * 页数
     */
    private Integer pageIndex;

    /**
     * 条数
     */
    private Integer pageSize;
}
