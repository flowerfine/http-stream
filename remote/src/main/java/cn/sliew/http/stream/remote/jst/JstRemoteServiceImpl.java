package cn.sliew.http.stream.remote.jst;

import cn.sliew.http.stream.remote.jst.client.JstClient;
import cn.sliew.http.stream.remote.jst.client.JstResult;
import cn.sliew.http.stream.remote.jst.client.order.JstOrdersResult;
import cn.sliew.http.stream.remote.jst.client.order.OrdersSingleQuery;
import feign.RetryableException;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.SocketException;
import java.net.SocketTimeoutException;

@Slf4j
@Service
public class JstRemoteServiceImpl implements JstRemoteService, InitializingBean {

    @Autowired
    private MeterRegistry meterRegistry;
    @Autowired
    private RetryRegistry retryRegistry;

    @Autowired
    private JstClient client;

    @Override
    public void afterPropertiesSet() throws Exception {
//        RetryConfig defaultConfig = retryRegistry.getDefaultConfig();
//        RetryConfig config = RetryConfig.from(defaultConfig)
//                .retryOnResult(result -> ((JstResult) result).isSuccess() == false)
//                .retryExceptions(SocketException.class, SocketTimeoutException.class, RetryableException.class)
//                .build();
//        retry = retryRegistry.retry("jst_client", config);
    }

    @Override
    public JstOrdersResult ordersSingleQuery(OrdersSingleQuery query) {
        return client.ordersSingleQuery("orders.single.query", query);
    }

}
