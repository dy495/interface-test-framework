package com.haisheng.framework.testng.bigScreen.pressure;


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
import java.net.URI;
import java.util.Map;
import java.util.UUID;

/**
 * User: x
 * Date: 2018/8/10
 * Time: 4:06 PM
 */
public class Http {

    private static Logger log = LoggerFactory.getLogger(Http.class);

    protected static final long KeepAliveDurationMillis = 5000;
    protected static final int ConnectionTimeoutMillis = 1000;
    protected static final int SocketTimeoutMillis = 5000;
    protected static final int MaxPerRoute = 30;
    protected static final int MaxTotal = 30;

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
                        .setConnectTimeout(ConnectionTimeoutMillis)
                        .setSocketTimeout(SocketTimeoutMillis)
                        .build())
                .build();
    }




    public static String postJsonHeader(Map<String, String> headers, String url, String jsonData) throws Exception {
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpPost httPost = new HttpPost(url);
            try {
                httPost.setEntity(new StringEntity(jsonData, ContentType.APPLICATION_JSON));
                if (null != headers) {
                    headers.forEach((k, v) -> httPost.addHeader(k, v));
                }
                CloseableHttpResponse httpResponse = httpClient.execute(httPost);
                if (httpResponse.getEntity() != null) {
                    return EntityUtils.toString(httpResponse.getEntity());
                }
            } catch (Exception e) {
                log.error("[HTTP POST] url={}, error={}", url, e);
              //  throw new OSDeviceException(DeviceSystemContext.get().getRequestId(), StatusCode.NET_ERROR);
            }
            return null;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("[HTTP POST] {} close http client error. {}", url, e);
            }
        }
    }


}
