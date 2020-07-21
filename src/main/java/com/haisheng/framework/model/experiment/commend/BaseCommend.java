package com.haisheng.framework.model.experiment.commend;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.haisheng.framework.model.experiment.core.Api;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * 网络请求抽象类
 * 子类继承需要实现 RequestBody
 * 抽象类需要的 Headers、url、由Api类提供
 *
 * @author wangmin
 * @date 2020/7/21 9:48
 */
public abstract class BaseCommend implements ICommend {
    private static final Logger logger = LoggerFactory.getLogger(BaseCommend.class);

    @Override
    public Response execute(Api api, String url) throws IllegalAccessException, IOException {
        if (api == null || StringUtils.isBlank(url)) {
            throw new IllegalAccessException("请求信息不能为空");
        }
        Request.Builder builder = new Request.Builder();
        //添加header
        for (String key : api.getHeaders().keySet()) {
            builder.addHeader(key, api.getHeaders().get(key));
        }
        //添加body&&mediaType
        buildRequest(builder, api);
        //添加url
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        Request request = builder.url(url).build();
        //如果是https请求需要忽略SSL
        if (isHttps(url) && api.isIgnoreSSL()) {
            ignoreSSL(okHttpClientBuilder);
        }
        OkHttpClient okHttpClient = okHttpClientBuilder.retryOnConnectionFailure(true).connectTimeout(3, TimeUnit.MINUTES).build();
        return okHttpClient.newCall(request).execute();
    }

    public abstract void buildRequest(Request.Builder builder, Api api);

    /**
     * 如果是https请求需要忽略SSL证书
     *
     * @param url url
     * @return boolean
     */
    private boolean isHttps(String url) {
        return url.contains("https://");
    }

    /**
     * 忽略证书
     * baidu
     */
    private void ignoreSSL(OkHttpClient.Builder okHttpClientBuilder) {
        logger.info("https请求忽略SSL证书");
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{TRUST_MANAGER}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            logger.error("SSLContext初始化异常:{}", e.getMessage());
        }
        SSLSocketFactory sslSocketFactory = sslContext != null ? sslContext.getSocketFactory() : null;
        if (sslSocketFactory != null) {
            okHttpClientBuilder.sslSocketFactory(sslSocketFactory, TRUST_MANAGER).hostnameVerifier(
                    (hostname, session) -> true);
        }
    }

    /**
     * X509TrustManager信任SSL证书
     * baidu
     */
    private static final X509TrustManager TRUST_MANAGER = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };

//    /**
//     * 封装参考
//     * 正常访问格式
//     */
//    private void formData() {
//        MediaType mediaType = MediaType.parse("");
//        String requestBody = "";
//        RequestBody body = RequestBody.create(mediaType, requestBody);
//        Request request = new Request.Builder().addHeader("", "").url("").post(body).build();
//    }
}
