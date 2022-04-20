package cn.sliew.http.stream.flink.parameters;

import cn.sliew.http.stream.flink.parsing.PropertyParser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import org.apache.flink.util.StringUtils;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class MessageParameters {

    private String url;

    private String method;
    private String mediaType = "application/json";

    private NameValuePair[] headers;
    private NameValuePair[] paths;
    private NameValuePair[] querys;
    private NameValuePair[] bodys;

    /**
     * 解析 url 中的占位符
     * 绑定 query
     */
    public String getUrl() {
        Properties properties = new Properties();
        for (NameValuePair path : paths) {
            properties.put(path.getName(), path.getValue());
        }
        String parsedUrl = PropertyParser.parse(url, properties);

        String queryParam = getQuery();
        if (StringUtils.isNullOrWhitespaceOnly(queryParam)) {
            return parsedUrl;
        }
        return parsedUrl + "?" + queryParam;
    }

    private String getQuery() {
        return Arrays.stream(querys).map(query -> query.getName() + "=" + query.getValue()).collect(Collectors.joining("&"));
    }

    public Request getRequest() {
        Request.Builder builder = new Request.Builder();
        builder.url(getUrl());
        for (NameValuePair header : headers) {
            builder.header(header.getName(), header.getValue());
        }
        RequestBody requestBody = Util.EMPTY_REQUEST;
        if (bodys != null && bodys.length > 0) {
            requestBody = RequestBody.create(MediaType.parse(mediaType), "");
        }
        builder.method(method, requestBody);
        return builder.build();
    }

}
