package cn.sliew.http.stream.remote.jst.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PageIntervalQuery extends PageQuery {

    /**
     * 修改起始时间,起始时间和 结束时间必须同时存在，时间间隔不能超过七天
     */
    @JsonProperty("modified_begin")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 修改起始时间,起始时间和 结束时间必须同时存在，时间间隔不能超过七天
     */
    @JsonProperty("modified_end")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
