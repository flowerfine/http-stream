package cn.sliew.http.stream.remote.github.client;

import cn.sliew.milky.common.util.JacksonUtil;
import feign.QueryMapEncoder;

import java.util.Map;

public class JacksonQueryMapEncoder implements QueryMapEncoder {

    @Override
    public Map<String, Object> encode(Object o) {
        String jsonString = JacksonUtil.toJsonString(o);
        return JacksonUtil.parseJsonString(jsonString, Map.class);
    }
}
