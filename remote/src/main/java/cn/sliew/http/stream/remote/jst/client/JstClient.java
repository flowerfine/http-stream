package cn.sliew.http.stream.remote.jst.client;

import cn.sliew.http.stream.remote.jst.client.order.JstOrdersResult;
import cn.sliew.http.stream.remote.jst.client.order.OrdersSingleQuery;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jstClient", url = "${jst.api.url}")
public interface JstClient {

    @RateLimiter(name = "orders_single_query")
    @PostMapping(value = "query.aspx", produces = "application/json")
    JstOrdersResult ordersSingleQuery(@RequestParam("method") String method,
                                      @RequestBody OrdersSingleQuery query);
}
