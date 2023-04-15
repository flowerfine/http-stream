package cn.sliew.http.stream.remote.config;

import cn.sliew.http.stream.remote.jst.client.JstOpenAPIInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class JstFeignConfig {

    @Value("${jst.partnerId}")
    protected String partnerId;
    @Value("${jst.token}")
    protected String token;
    @Value("${jst.partnerKey}")
    protected String partnerKey;

    @Bean
    public JstOpenAPIInterceptor jstOpenAPIInterceptor() {
        return new JstOpenAPIInterceptor(partnerId, token, partnerKey);
    }
}
