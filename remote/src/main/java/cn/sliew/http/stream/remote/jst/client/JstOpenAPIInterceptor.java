package cn.sliew.http.stream.remote.jst.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.util.Collection;
import java.util.Map;

@Slf4j
public class JstOpenAPIInterceptor implements RequestInterceptor {

    private final String partnerId;
    private final String token;
    private final String partnerKey;

    public JstOpenAPIInterceptor(String partnerId, String token, String partnerKey) {
        this.partnerId = partnerId;
        this.token = token;
        this.partnerKey = partnerKey;
    }

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
        if (log.isTraceEnabled()) {
            log.trace("{} ---> {}", method, new String(requestTemplate.body()));
        }
    }
}
