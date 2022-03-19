package cn.sliew.http.stream.remote.jst;

import cn.sliew.http.stream.remote.jst.client.order.JstOrdersResult;
import cn.sliew.http.stream.remote.jst.client.order.OrdersSingleQuery;

public interface JstRemoteService {

    JstOrdersResult ordersSingleQuery(OrdersSingleQuery query);
}
