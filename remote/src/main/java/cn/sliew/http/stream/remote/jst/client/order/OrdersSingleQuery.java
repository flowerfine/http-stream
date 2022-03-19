package cn.sliew.http.stream.remote.jst.client.order;

import cn.sliew.http.stream.remote.jst.client.PageIntervalQuery;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrdersSingleQuery extends PageIntervalQuery {

    /**
     * 店铺编号
     */
    @JsonProperty("shop_id")
    private Integer shopId;

    /**
     * 线上单号，最大限制20条
     */
    @JsonProperty("so_ids")
    private List<String> soIds;

    /**
     * 待付款：WaitPay；发货中：Delivering；被合并：Merged；异常：Question；被拆分：Split；等供销商|外仓发货：WaitOuterSent；已付款待审核：WaitConfirm；已客审待财审：WaitFConfirm；已发货：Sent；取消：Cancelled
     */
    @JsonProperty("status")
    private String status;
}
