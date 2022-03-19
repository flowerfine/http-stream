package cn.sliew.http.stream.remote.jst.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Collection;
import java.util.Map;

@Slf4j
@Component
public class JstOpenAPIInterceptor implements RequestInterceptor {

    @Value("${jst.partnerId}")
    protected String partnerId;
    @Value("${jst.token}")
    protected String token;
    @Value("${jst.partnerKey}")
    protected String partnerKey;

    private String sign(String method, String ts) {
        String toSign = String.format("%s%stoken%sts%s%s", method, partnerId, token, ts, partnerKey);
        return DigestUtils.md5DigestAsHex(toSign.getBytes());
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Map<String, Collection<String>> queryMap = requestTemplate.queries();
        String method = queryMap.get("method").stream().findFirst().get();
        String ts = String.valueOf(System.currentTimeMillis() / 1000);
        String sign = sign(method, ts);
        requestTemplate.query("partnerid", partnerId);
        requestTemplate.query("token", token);
        requestTemplate.query("ts", ts);
        requestTemplate.query("sign", sign);
        if (log.isDebugEnabled()) {
            log.debug("{} ---> {}", method, new String(requestTemplate.body()));
        }
    }
}
