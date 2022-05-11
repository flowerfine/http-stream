// Copyright 2021 LinkedIn Corporation. All rights reserved.
// Licensed under the BSD-2 Clause license.
// See LICENSE in the project root for license information.

package cn.sliew.http.stream.flink.util;

import cn.sliew.milky.common.parse.placeholder.PropertyPlaceholder;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.milky.log.Logger;
import cn.sliew.milky.log.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.Util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;


/**
 * utility to facilitate different params: headers、paths、querys and body param.
 */
public enum HttpRequestMethod {

    GET("GET") {
        @Override
        protected Request.Builder getRequest(RequestBody body) {
            return new Request.Builder().get();
        }
    },

    POST("POST") {
        @Override
        protected Request.Builder getRequest(RequestBody body) {
            return new Request.Builder().post(body);
        }
    },

    PUT("PUT") {
        @Override
        protected Request.Builder getRequest(RequestBody body) {
            return new Request.Builder().put(body);
        }
    },

    DELETE("DELETE") {
        @Override
        protected Request.Builder getRequest(RequestBody body) {
            return new Request.Builder().delete(body);
        }
    };

    private static final Logger log = LoggerFactory.getLogger(HttpRequestMethod.class);
    private final String name;

    HttpRequestMethod(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public Request getRequest(final String uriTemplate,
                              final ObjectNode parameters,
                              final Map<String, String> headers,
                              final ObjectNode querys,
                              final ObjectNode payloads) {
        Request.Builder requestBuilder = getRequest(getRequestBody(parameters, payloads));
        appendQuerys(requestBuilder, uriTemplate, parameters, querys);
        appendHeaders(requestBuilder, parameters, headers);
        return requestBuilder.build();
    }


    protected void appendQuerys(Request.Builder requestBuilder, String urlTemplate, ObjectNode parameters, ObjectNode querys) {
        try {
            String url = replace(urlTemplate, parameters);
            HttpUrl.Builder builder = HttpUrl.get(url).newBuilder();
            Iterator<Map.Entry<String, JsonNode>> iterator = querys.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = iterator.next();
                String replaced = replace(entry.getValue().toString(), parameters);
                builder.addQueryParameter(entry.getKey(), replaced);
            }
            requestBuilder.url(builder.build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void appendHeaders(Request.Builder requestBuilder, ObjectNode parameters, Map<String, String> headers) {
        Map<String, String> headersCopy = new HashMap<>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String replaced = replace(entry.getValue(), parameters);
            headersCopy.put(entry.getKey(), replaced);
        }
        for (Map.Entry<String, String> entry : headersCopy.entrySet()) {
            requestBuilder.header(entry.getKey(), entry.getValue());
        }
    }

    private RequestBody getRequestBody(ObjectNode parameters, ObjectNode body) {
        if (body == null || body.isEmpty()) {
            return Util.EMPTY_REQUEST;
        }
        ObjectNode bodyCopy = JacksonUtil.createObjectNode();
        Iterator<Map.Entry<String, JsonNode>> iterator = body.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            String replaced = replace(entry.getValue().toString(), parameters);
            bodyCopy.put(entry.getKey(), replaced);
        }
        return RequestBody.create(MediaType.parse("application/json"), bodyCopy.toString());
    }

    protected String replace(String text, ObjectNode parameters) {
        PropertyPlaceholder propertyPlaceholder = new PropertyPlaceholder(log, "{", "}");
        Properties properties = new Properties();
        Iterator<Map.Entry<String, JsonNode>> iterator = parameters.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            properties.put(entry.getKey(), entry.getValue().toString());
        }
        return propertyPlaceholder.replacePlaceholders(text, properties);
    }

    protected abstract Request.Builder getRequest(RequestBody body);
}
