// Copyright 2021 LinkedIn Corporation. All rights reserved.
// Licensed under the BSD-2 Clause license.
// See LICENSE in the project root for license information.

package cn.sliew.http.stream.flink.util;

import cn.sliew.milky.common.util.JacksonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * utility to facilitate different params: headers、paths、querys and body param.
 */
public enum HttpRequestMethod {
    GET("GET") {
        @Override
        protected Request getHttpRequestContentJson(String uriTemplate,
                                                    JsonObject parameters, JsonObject payloads)
                throws UnsupportedEncodingException {
            Pair<String, JsonObject> replaced = replaceVariables(uriTemplate, parameters);
            //ignore payloads
            return new HttpGet(appendQueryParams(replaced.getKey(), replaced.getValue()));
        }

        @Override
        protected HttpUriRequest getHttpRequestContentUrlEncoded(String uriTemplate, JsonObject parameters)
                throws UnsupportedEncodingException {
            return getHttpRequestContentJson(uriTemplate, parameters, new JsonObject());
        }
    },

    POST("POST") {
        @Override
        protected HttpUriRequest getHttpRequestContentJson(String uriTemplate,
                                                           JsonObject parameters, JsonObject payloads)
                throws UnsupportedEncodingException {
            Pair<String, JsonObject> replaced = replaceVariables(uriTemplate, parameters);
            for (Map.Entry<String, JsonElement> entry : payloads.entrySet()) {
                replaced.getValue().add(entry.getKey(), entry.getValue());
            }
            return setEntity(new HttpPost(replaced.getKey()), replaced.getValue().toString());
        }

        @Override
        protected HttpUriRequest getHttpRequestContentUrlEncoded(String uriTemplate, JsonObject parameters)
                throws UnsupportedEncodingException {
            Pair<String, JsonObject> replaced = replaceVariables(uriTemplate, parameters);
            return setEntity(new HttpPost(replaced.getKey()), jsonToUrlEncodedEntity(replaced.getValue()));
        }
    },

    PUT("PUT") {
        @Override
        protected HttpUriRequest getHttpRequestContentJson(String uriTemplate,
                                                           JsonObject parameters, JsonObject payloads)
                throws UnsupportedEncodingException {
            Pair<String, JsonObject> replaced = replaceVariables(uriTemplate, parameters);
            for (Map.Entry<String, JsonElement> entry : payloads.entrySet()) {
                replaced.getValue().add(entry.getKey(), entry.getValue());
            }
            return setEntity(new HttpPut(replaced.getKey()), replaced.getValue().toString());
        }

        @Override
        protected HttpUriRequest getHttpRequestContentUrlEncoded(String uriTemplate, JsonObject parameters)
                throws UnsupportedEncodingException {
            Pair<String, JsonObject> replaced = replaceVariables(uriTemplate, parameters);
            return setEntity(new HttpPut(replaced.getKey()), jsonToUrlEncodedEntity(replaced.getValue()));
        }
    },

    DELETE("DELETE") {
        @Override
        protected HttpUriRequest getHttpRequestContentJson(String uriTemplate,
                                                           JsonObject parameters, JsonObject payloads)
                throws UnsupportedEncodingException {
            Pair<String, JsonObject> replaced = replaceVariables(uriTemplate, parameters);
            for (Map.Entry<String, JsonElement> entry : payloads.entrySet()) {
                replaced.getValue().add(entry.getKey(), entry.getValue());
            }
            return new HttpDelete(replaced.getKey());
        }

        @Override
        protected HttpUriRequest getHttpRequestContentUrlEncoded(String uriTemplate, JsonObject parameters)
                throws UnsupportedEncodingException {
            Pair<String, JsonObject> replaced = replaceVariables(uriTemplate, parameters);
            return new HttpDelete(replaced.getKey());
        }
    };

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestMethod.class);
    private final String name;

    HttpRequestMethod(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * This is the public method to generate HttpUriRequest for each type of Http Method
     *
     * @param uriTemplate input URI, which might contain place holders
     * @param parameters  parameters to be add to URI or to request Entity
     * @param headers     Http header tags
     * @return HttpUriRequest ready for connection
     */
    public HttpUriRequest getHttpRequest(final String uriTemplate, final JsonObject parameters, final Map<String, String> headers)
            throws UnsupportedEncodingException {
        return getHttpRequest(uriTemplate, parameters, headers, new JsonObject());
    }

    /**
     * This is the public method to generate HttpUriRequest for each type of Http Method
     *
     * @param uriTemplate input URI, which might contain place holders
     * @param headers     Http header tags
     * @param parameters  parameters to be add to URI or to request Entity
     * @param payloads    additional payloads to be included in the body of the Http request
     * @return HttpUriRequest ready for connection
     */
    public Request getRequest(final String uriTemplate,
                              final Map<String, String> headers,
                              final ObjectNode parameters,
                              final ObjectNode payloads)
            throws UnsupportedEncodingException {
        Request request;

        // substitute variables in headers
        Map<String, String> headersCopy = new HashMap<>();
        ObjectNode parametersCopy = JacksonUtil.deepCopy(parameters).getAsJsonObject();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            Pair<String, ObjectNode> replaced = VariableUtils.replaceWithTracking(entry.getValue(), parameters);
            if (!replaced.getLeft().equals(entry.getValue())) {
                parametersCopy = JsonUtils.deepCopy(replaced.getRight()).getAsJsonObject();
                headersCopy.put(entry.getKey(), replaced.getLeft());
                LOG.info("Substituted header string: {} = {}", entry.getKey(), replaced.getLeft());
            } else {
                headersCopy.put(entry.getKey(), entry.getValue());
            }
        }

        LOG.info("Final parameters for HttpRequest: {}", parametersCopy.toString());
        if (headersCopy.containsKey("Content-Type")
                && headersCopy.get("Content-Type").equals("application/x-www-form-urlencoded")) {
            request = getRequest(uriTemplate, parametersCopy);
        } else {
            request = getRequest(uriTemplate, parametersCopy, payloads);
        }

        for (Map.Entry<String, String> entry : headersCopy.entrySet()) {
            request.addHeader(entry.getKey(), entry.getValue());
        }
        return request;
    }

    protected abstract Request getRequest(String uriTemplate, ObjectNode parameters, ObjectNode payloads)
            throws UnsupportedEncodingException;

    protected abstract Request getRequest(String uriTemplate, ObjectNode parameters)
            throws UnsupportedEncodingException;

    protected Pair<String, ObjectNode> replaceVariables(String uriTemplate, ObjectNode parameters)
            throws UnsupportedEncodingException {
        return null;
    }

    protected String appendQueryParams(String url, ObjectNode params) {
        try {
            HttpUrl.Builder builder = HttpUrl.get(url).newBuilder();
            Iterator<Map.Entry<String, JsonNode>> iterator = params.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = iterator.next();
                builder.addQueryParameter(entry.getKey(), entry.getValue().toString());
            }
            return builder.build().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert Json formatted parameter set to Url Encoded Entity as requested by
     * Content-Type: application/x-www-form-urlencoded
     * Json Example:
     * {"param1": "value1", "param2": "value2"}
     * <p>
     * URL Encoded Example:
     * param1=value1&param2=value2
     *
     * @param parameters Json structured parameters
     * @return URL encoded entity
     */
    protected UrlEncodedFormEntity jsonToUrlEncodedEntity(JsonObject parameters) {
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            for (Map.Entry<String, JsonElement> entry : parameters.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().getAsString()));
            }
            return new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Request setEntity(HttpEntityEnclosingRequestBase requestBase, String stringEntity)
            throws UnsupportedEncodingException {
        return setEntity(requestBase, new StringEntity(stringEntity));
    }

    protected HttpUriRequest setEntity(HttpEntityEnclosingRequestBase requestBase, StringEntity stringEntity) {
        requestBase.setEntity(stringEntity);
        return requestBase;
    }
}
