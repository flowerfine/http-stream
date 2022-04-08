package cn.sliew.http.stream.akka.page;

import cn.sliew.http.stream.remote.jst.JstRemoteService;
import cn.sliew.http.stream.remote.jst.client.order.JstOrdersResult;
import cn.sliew.http.stream.remote.jst.client.order.OrdersSingleQuery;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class JstOrderClient {

    private final JstRemoteService jstRemoteService;

    public JstOrderClient(JstRemoteService jstRemoteService) {
        this.jstRemoteService = jstRemoteService;
    }

    public CompletionStage<JstOrderPage> fetchPage(OrdersSingleQuery query) {
        JstOrdersResult jstOrdersResult = jstRemoteService.ordersSingleQuery(query);
        return CompletableFuture.completedFuture(new JstOrderPage(query, jstOrdersResult));
    }
}
