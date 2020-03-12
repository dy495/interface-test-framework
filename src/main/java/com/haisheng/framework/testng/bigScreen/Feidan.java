package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.collect.ImmutableMap;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.model.bean.ReportTime;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class Feidan {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    private HttpConfig config;

    DateTimeUtil dt = new DateTimeUtil();

    String natureCustomer = "NATURE";
    String channelCustomer = "CHANNEL";

    //  -----------------------------------------渠道------------------------------------------
    String wudongChannelIdStr = "5";
    String wudongChannelNameStr = "测试【勿动】";
    int wudongChannelInt = 5;
    String wudongOwnerPhone = "16600000000";

    String maiTianChannelStr = "2";
    String maiTianChannelNameStr = "麦田";
    int maiTianChannelInt = 2;

    String lianjiaChannelStr = "1";
    int lianjiaChannelInt = 1;
    String lianjiaChannelName = "链家";
    String lianjiaOwnerPhone = "16600000001";

//  ------------------------------------------业务员-----------------------------------------------------

    String lianjiaToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmtYvor5XjgJDli7_liqjjgJEiLCJ1aWQiOjIxMzYsImxvZ2luVGltZSI6MT" +
            "U3ODk5OTY2NjU3NH0.kQsEw_wGVmPQ4My1p-FNZ556FJC7W177g7jfjFarTu4";
    String lianjiaFreezeStaffIdStr = "2136";
    int lianjiaFreezeStaffIdInt = 2136;
    String lianjiaFreezeStaffName = "链家-【勿动】";
    String lianjiaFreezeStaffPhone = "14112345678";

    String lianjiaStaffIdStr = "2136";
    int lianjiaStaffIdInt = 2136;
    String lianjiaStaffName = "链家业务员";
    String lianjiaStaffPhone = "17711111024";


    String wudongToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjgJDli7_li" +
            "qjjgJExIiwidWlkIjoyMDk4LCJsb2dpblRpbWUiOjE1Nzg1NzQ2MjM4NDB9.exDJ6avJKJd3ezQkYc4fmUkHvXaukqfgjThkpoYgnAw";

    String wudongStaffIdStr = "2098";
    int wudongStaffIdInt = 2098;

    String maiTianStaffStr = "2";
    int maiTianStaffInt = 2;
    int maitianStaffIdInt = 2449;
    String maitianStaffIdStr = "2449";
    String maitianStaffName = "喵喵喵";
    String maitianStaffPhone = "14422110039";


//    -------------------------------------------置业顾问-----------------------------------------------------

    String anShengIdStr = "15";
    String anShengName = "安生";
    String anShengPhone = "16622222222";

    String zhangIdStr = "8";
    int zhangIdInt = 8;
    String zhangName = "张钧甯";
    String zhangPhone = "19111311116";

    long wudongReportTime = 1547024264000L;//2019-01-09 16:57:44
    long lianjiaReportTime = 1547014265000L;//2019-01-09 14:11:05
    long noChannelReportTime = 1547034265000L;//2019-01-09 19:44:25

    long firstAppearTime = 1582684439509L;

    String normalOrderType = "NORMAL";
    String riskOrderType = "RISK";

    String defaultRuleId = "837";
    String ahead1hRuleId = "996";
    String ahead24hRuleId = "842";
    String ahead7dayRuleId = "844";
    String ahead30dayRuleId = "846";
    String aheadMaxRuleId = "1003";

    String protect1DayRuleId = "840";

    int pageSize = 10000;

//    ----------------------------------------------------渠道H5--------------------------------------------------

    //    H5业务员顾客列表
    public JSONObject customerListH5(int page, int size, String token) throws
            Exception {
        String url = "/external/channel/customer/list";
        String json =
                "{\n" +
                        "    \"page\":\"" + page + "\"," +
                        "    \"size\":\"" + size + "\"," +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"token\":\"" + token + "\"" +
                        "}\n";

        String response = httpPostWithCheckCode(url, json);

        return JSON.parseObject(response).getJSONObject("data");
    }

    public void customerEditH5(String cid, String customerName, String phone, String token) throws Exception {

        String url = "/external/channel/customer/edit";

        String json =
                "{\n" +
                        "    \"shop_id\":\"" + getShopId() + "\"," +
                        "    \"token\":\"" + token + "\"," +
                        "    \"cid\":\"" + cid + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}\n";

        httpPostWithCheckCode(url, json);
    }


    public String customerEditH5NoCode(String cid, String customerName, String phone, String token) throws Exception {

        String url = "/external/channel/customer/edit";

        String json =
                "{\n" +
                        "    \"shop_id\":\"" + getShopId() + "\"," +
                        "    \"token\":\"" + token + "\"," +
                        "    \"cid\":\"" + cid + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}\n";

        String s = httpPost(url, json);

        return s;
    }


//    --------------------------------------------------订单相关-----------------------------------------------------

    public JSONObject searchReportInfoByPhone(String orderId, String phone) throws Exception {

        String url = "/risk/order/searchReportInfoByPhone";

        String json =
                "{\n" +
                        "    \"shop_id\":\"" + getShopId() + "\"," +
                        "    \"order_id\":\"" + orderId + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}\n";

        String s = httpPostWithCheckCode(url, json);
        return JSON.parseObject(s).getJSONObject("data");
    }

    @Test
    public void test() throws Exception {

        String orderId  = "8903";
        String phone = "14422110014";
        searchReportInfoByPhone(orderId,phone);

    }


    private Object getShopId() {
        return "4116";
    }

    private String httpPostWithCheckCode(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        checkCode(response, StatusCode.SUCCESS, "");

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    private String httpPost(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    public void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            if (expect != code) {
                if (code != 1000) {
                    message += resJo.getString("message");
                }
                Assert.assertEquals(code, expect, message);
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }

    public void checkMessage(String function, String response, String message) throws Exception {

        String messageRes = JSON.parseObject(response).getString("message");
        if (!message.equals(messageRes)) {
            throw new Exception(function + "，提示信息与期待不符，期待=" + message + "，实际=" + messageRes);
        }
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
        }
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .other("shop_id", getShopId().toString())
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }


    private String getIpPort() {
        return "http://dev.store.winsenseos.cn";
    }

}
