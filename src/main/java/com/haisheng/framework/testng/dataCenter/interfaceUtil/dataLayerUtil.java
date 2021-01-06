package com.haisheng.framework.testng.dataCenter.interfaceUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.springframework.util.StringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class dataLayerUtil extends TestCaseCommon {
    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */
    private static volatile dataLayerUtil instance = null;

    private dataLayerUtil() {
    }
    public static dataLayerUtil getInstance() {

        if (null == instance) {
            synchronized (dataLayerUtil.class) {
                if (null == instance) {
                    //这里
                    instance = new dataLayerUtil();
                }
            }
        }
        return instance;
    }

    HttpClient client = null;


    @BeforeClass
    public  void init() throws HttpProcessException {
        client = HCB.custom()
                .pool(50, 10)
                .retry(3).build();
    }
    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String IpPort = "http://dev.api.winsenseos.com/retail/api/data/biz";
    public JSONObject http(String requestUrl ,Object requestJson){

        Header[] headers = HttpHeader.custom()
                .other("Accept", "application/json")
                .other("Content-Type", "application/json;charset=utf-8")
                .other("request_id", UUID.randomUUID().toString())
                .other("api_source", "BUSINESS_PATROL")
                .build();

        HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(requestJson)).client(client);
        try {
            String post = HttpClientUtil.post(config);
            return JSON.parseObject(post);
        } catch (HttpProcessException e) {
            // logger.error("http调用失败 requestUrl =  {} , requestJson =  {} , headers =  {}" ,requestUrl,requestJson,JSON.toJSONString(headers),e);
        }
        return null;
    }


}
