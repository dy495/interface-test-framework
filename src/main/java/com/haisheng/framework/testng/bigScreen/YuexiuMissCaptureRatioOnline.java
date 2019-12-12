package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.text.DecimalFormat;

public class YuexiuMissCaptureRatioOnline {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

    private String loginPathOnline = "/yuexiu-login";
    private String jsonOnline = "{\"username\":\"yuexiu@yuexiu.com\",\"passwd\":\"f2c7219953b54583ea11065215f22a8b\"}";

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    private QADbUtil qaDbUtil = new QADbUtil();

    private HttpConfig config;

    private long SHOP_ID_ENV = 889;

    String URL_PREFIX = "http://123.57.114.205";

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLlrp7pqozlrqREZW1vIiwidWlkIjoidWlkXzdmYzc4ZDI0IiwibG9naW5UaW1lIjoxNTcxNTM3OTYxMjU4fQ.lmIXi-cmw3VsuD6RZrPZDJw70TvWuozEtLqV6yFHXVY";

    @Test
    public void missRatio() throws Exception {

        JSONObject data = realTimeRegions();

        checkSort(data);
        
        calMissRatio(data);


    }

    private void calMissRatio(JSONObject data) {

        JSONArray regions = data.getJSONArray("regions");


        JSONObject first = regions.getJSONObject(0);

        int max = first.getJSONObject("statistics").getInteger("uv");

        String regionId = first.getString("region_id");

//        892是大堂区
        if (!"892".equals(regionId)){
            for (int i = 0; i < regions.size(); i++) {
                JSONObject single = regions.getJSONObject(i);
                if ("892".equals(single.getString("region_id"))){
                    int target = single.getJSONObject("statistics").getInteger("uv");
                    double missRatio = (double)target*100.00/(double)max;
                    DecimalFormat df = new DecimalFormat("0.00");
                    String missRatioStr = df.format(missRatio);

                    System.out.println("漏抓率【" + missRatioStr + "%】" );
                    break;
                }
            }
        }
    }

    private void checkSort(JSONObject data) throws Exception {

        JSONArray regions = data.getJSONArray("regions");
        for (int i = 0; i < regions.size()-1; i++) {

            JSONObject singleB = regions.getJSONObject(i);
            String regionB = singleB.getString("region_name");
            int uvB = singleB.getJSONObject("statistics").getInteger("uv");

            JSONObject singleA = regions.getJSONObject(i+1);
            String regionA = singleA.getString("region_name");
            int uvA = singleA.getJSONObject("statistics").getInteger("uv");

            if (uvB<uvA){
                throw new Exception("排名第【" + i + "】的区域【" + regionB + "】的uv数是【" + uvB + "】," +
                        "排名第" + (i+1) + "的区域【" + regionA + "】的uv数是【" + uvA + "】" );
            }
        }

    }


    public JSONObject realTimeRegions() throws Exception {
        String json = getRealTimeParamJson();
        String path = "/yuexiu/data/statistics/real-time/region";
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        return data;
    }

    private String getRealTimeParamJson() {

        return "{\"shop_id\":" + SHOP_ID_ENV + "}";
    }

    private String getCustomerListParamJson(String customerType, String gender, String ageGroupId) {

        String json =
                "{\n";
        if (!"".equals(customerType)) {
            json += "    \"customer_type\":\"" + customerType + "\",\n";
        }

        if (!"".equals(gender)) {
            json += "    \"gender\":\"" + gender + "\",\n";
        }

        if (!"".equals(gender)) {
            json += "    \"age_group_id\":\"" + ageGroupId + "\",\n";
        }

        json += "    \"shop_id\":" + SHOP_ID_ENV + ",\n" +
                "    \"page\":1,\n" +
                "    \"size\":100\n" +
                "}";

        return json;
    }

    private String httpPost(String path, String json, int expectCode) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        try {
            response = HttpClientUtil.post(config);
        } catch (HttpProcessException e) {
            failReason = "http post 调用异常，url = " + queryUrl + "\n" + e;
            return response;
            //throw new RuntimeException("http post 调用异常，url = " + queryUrl, e);
        }

        logger.info("result = {}", response);
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        checkCode(response, expectCode, "");

        return response;
    }

    private void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            failReason = "初始化http配置异常" + "\n" + e;
            return;
            //throw new RuntimeException("初始化http配置异常", e);
        }
        //String authorization = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJ5dWV4aXUiLCJleHAiOjE1NzE0NzM1OTh9.QYK9oGRG48kdwzYlYgZIeF7H2svr3xgYDV8ghBtC-YUnLzfFpP_sDI39D2_00wiVONSelVd5qQrjtsXNxRUQ_A";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    private void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            message += resJo.getString("message");

            if (expect != code) {
                throw new Exception(message + " expect code: " + expect + ",actual: " + code);
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }

    private String getIpPort() {

        return URL_PREFIX;
    }


    @BeforeSuite
    public void login() {

        String json = this.jsonOnline;
        String path = this.loginPathOnline;
        qaDbUtil.openConnection();

        initHttpConfig();
        String loginUrl = getIpPort() + path;

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        String result;
        try {
            result = HttpClientUtil.post(config);
            this.authorization = JSONObject.parseObject(result).getJSONObject("data").getString("token");
            logger.info("authorization: {}", authorization);
        } catch (Exception e) {
            aCase.setFailReason("http post 调用异常，url = " + loginUrl + "\n" + e);
            logger.error(aCase.getFailReason());
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }
}
