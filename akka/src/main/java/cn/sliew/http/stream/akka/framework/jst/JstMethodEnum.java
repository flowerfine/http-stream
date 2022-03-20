package cn.sliew.http.stream.akka.framework.jst;

import lombok.Getter;

@Getter
public enum JstMethodEnum {

    ORDERS_HISTORY_QUERY("orders.history.query", "历史订单查询"),
    ORDERS_SINGLE_QUERY("orders.single.query", "订单查询"),
    ;

    private String method;
    private String desc;

    JstMethodEnum(String method, String desc) {
        this.method = method;
        this.desc = desc;
    }
}