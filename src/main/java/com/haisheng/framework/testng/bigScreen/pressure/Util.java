package com.haisheng.framework.testng.bigScreen.pressure;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.xundian.casedaily.hqq.fucPackage.StoreFuncPackage;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.haisheng.framework.testng.bigScreen.pressure.Http.getHttpClient;

public class Util extends TestCaseCommon {
    private final static Logger log= LoggerFactory.getLogger(Util.class);

    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */

    private static volatile Util instance = null;

    private Util() {
    }

    public static Util getInstance() {

        if (null == instance) {
            synchronized (StoreFuncPackage.class) {
                if (null == instance) {
                    //这里
                    instance = new Util();
                }
            }
        }

        return instance;
    }


    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String IpPort = "https://ezcloud.uniview.com";





    /**
     * 登录
     *
     * force		        是否强制登录 0=不强制登录，用户在线时提示失败 1=强制登录，把在线的用户给踢下去
     * login_mode		0=允许多点登录 1=不允许多点登录
     * login_type		登录端：1=web 2=pc 3=android 4=ios 5=other 10=设备 11=手机采集 12=通道登录 13=人脸服务器 20=子平台
     * expire	    	有效时间，单位（小时）：登录设置的过期时长为不调用其他接口下的token过期时长，调用使用其他接口后，有效期为调用后的1小时内
     * @return 返回的是 data 内容
     */
    public JSONObject getToken() {
        try {
            // http
            String path = "/openapi/user/app/token/get";
            String url = IpPort +path;

            JSONObject req = new JSONObject();
            req.put("appId", "25681984381");
            req.put("secretKey", "ef729cb09e294ad983f003010d0dea6b");

            logger.info("request json:{}", req);
            String response = this.postJsonHeader(buildHttpHeaders(null), url, JSON.toJSONString(req));
            return JSON.parseObject(response).getJSONObject("data");
        } catch (Exception e) {
            log.error("getToken OSDeviceException : ", e);
        }
        return null;
    }
    private Map<String, String> buildHttpHeaders(String accessToken) {
        Map<String, String> headers = new HashMap<>();
        if(StringUtils.isNotEmpty(accessToken)) {
            headers.put("Authorization", accessToken);
        }
        logger.info("header:{}", headers);
        return headers;
    }
    public static String postJsonHeader(Map<String, String> headers, String url, String jsonData)  {
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
                e.printStackTrace();
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

