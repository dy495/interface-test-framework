package com.haisheng.framework.testng.dataCenter.interfaceUtil;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

/**
 * User: liuqiao.lq
 * Date: 2018/8/10
 * Time: 4:06 PM
 */
public class HttpConnector {

    private static Logger log = LoggerFactory.getLogger(HttpConnector.class);

    protected static final long KeepAliveDurationMillis = 5000;
    protected static final int ConnectionTimeoutMillis = 1000;
    protected static final int SocketTimeoutMillis = 5000;
    protected static final int MaxPerRoute = 200;
    protected static final int MaxTotal = 1000;

    protected static CloseableHttpClient getHttpClient() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(MaxPerRoute);
        cm.setMaxTotal(MaxTotal);

        return HttpClientBuilder.create()
                .setConnectionManager(cm)
                .setKeepAliveStrategy((response, context) -> KeepAliveDurationMillis)
                .setRetryHandler(new DefaultHttpRequestRetryHandler())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(ConnectionTimeoutMillis)
                        .setSocketTimeout(SocketTimeoutMillis)
                        .build())
                .build();
    }

    public static String getJson(String requestId, String url) throws RuntimeException {
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("request_id", requestId);
            httpGet.setHeader("api_source", "INTERNAL");
            try {
                CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getEntity() != null) {
                    return EntityUtils.toString(httpResponse.getEntity());
                }
            } catch (IOException e) {
                log.error("[GET] url={}, error: {}", url, e);
                throw new RuntimeException("get url error");
            }
            return null;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("[GET] {} close http client error. {}", url, e);
            }
        }
    }

    public static String postJson(String requestId, String url, String content) {
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpPost httPost = new HttpPost(url);
            try {
                httPost.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
                httPost.setHeader("request_id", requestId);
                httPost.setHeader("api_source", "INTERNAL");
                CloseableHttpResponse httpResponse = httpClient.execute(httPost);
                if (httpResponse.getEntity() != null && httpResponse.getStatusLine().getStatusCode() == 200) {
                    return EntityUtils.toString(httpResponse.getEntity());
                }
            } catch (Exception e) {
                log.error("[POST] url={}, error={}", url, e);
            }
            return null;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("[POST] {} close http client error. {}", url, e);
            }
        }
    }

    public static String postJson(String url, Map<String, String> headers, String content) {
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpPost httpPost = new HttpPost(url);

            if (MapUtils.isNotEmpty(headers)) {
                for (String key : headers.keySet()) {
                    httpPost.addHeader(key, headers.get(key));
                }
            }

            try {
                httpPost.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
                CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
                if (httpResponse.getEntity() != null && httpResponse.getStatusLine().getStatusCode() == 200) {
                    return EntityUtils.toString(httpResponse.getEntity());
                }
            } catch (Exception e) {
                log.warn("[POST] url={}, error={}", url, e.getMessage());
            }
            return null;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("[POST] {} close http client error. {}", url, e.getMessage());
            }
        }
    }

    public static String deleteJson(String requestId, String url) throws RuntimeException {
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpDelete httpDelete = new HttpDelete(url);
            httpDelete.setHeader("request_id", requestId);
            httpDelete.setHeader("api_source", "INTERNAL");
            try {
                CloseableHttpResponse httpResponse = httpClient.execute(httpDelete);
                if (httpResponse.getEntity() != null) {
                    return EntityUtils.toString(httpResponse.getEntity());
                }
            } catch (IOException e) {
                log.error("[GET] url={}, error: {}", url, e);
                throw new RuntimeException("get url error");
            }
            return null;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("[GET] {} close http client error. {}", url, e);
            }
        }
    }

    public static byte[] get(String url, Map<String, String> params) throws RuntimeException {
        CloseableHttpClient httpClient = getHttpClient();
        try {
            if (params != null && !params.isEmpty()) {
                StringBuffer buffer = new StringBuffer(url);
                if (!url.endsWith("?")) {
                    buffer.append("?");
                }
                for (String key : params.keySet()) {
                    try {
                        buffer.append(key + "=" + URLEncoder.encode(params.get(key), "UTF-8") + "&");
                    } catch (UnsupportedEncodingException e) {
                        log.error("[GET] url not support encoding: ", e);
                        throw new RuntimeException("url not support encoding");
                    }
                }
                buffer.deleteCharAt(buffer.length() - 1);
                url = buffer.toString();
            }
            HttpGet httpGet = new HttpGet(url);
            try {
                CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getEntity() != null) {
                    return EntityUtils.toByteArray(httpResponse.getEntity());
                }
            } catch (IOException e) {
                log.error("[GET] download error: ", e);
                throw new RuntimeException("download url error");
            }
            return null;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("[GET] {} close http client error. {}", url, e);
            }
        }
    }

    public static String putJson(String requestId, String url, String content) {
        if (StringUtils.isEmpty(requestId)) {
            requestId = UUID.randomUUID().toString();
        }

        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpPut httPost = new HttpPut(url);
            httPost.setHeader("request_id", requestId);
            httPost.setHeader("api_source", "INTERNAL");

            try {
                httPost.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
                CloseableHttpResponse httpResponse = httpClient.execute(httPost);
                if (httpResponse.getEntity() != null && httpResponse.getStatusLine().getStatusCode() == 200) {
                    return EntityUtils.toString(httpResponse.getEntity());
                }
            } catch (Exception e) {
                log.error("[POST] url={}, error={}", url, e);
            }
            return null;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("[POST] {} close http client error. {}", url, e);
            }
        }
    }

}
