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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class FeidanMiniApiInterfaceNotNullOnline {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_ONLINE_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-online-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    private HttpConfig config;

    String channelId = "19";

    String genderMale = "MALE";
    String genderFemale = "FEMALE";

    private String getIpPort() {
        return "http://store.winsenseos.com";
    }

    private void checkResult(String result, String... checkColumnNames) {
        logger.info("result = {}", result);
        JSONObject res = JSONObject.parseObject(result);
        if (!res.getInteger("code").equals(1000)) {
            throw new RuntimeException("result code is " + res.getInteger("code") + " not success code");
        }
        for (String checkColumn : checkColumnNames) {
            Object column = res.getJSONObject("data").get(checkColumn);
            logger.info("{} : {}", checkColumn, column);
            if (column == null) {
                throw new RuntimeException("result does not contains column " + checkColumn);
            }
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
            //throw new RuntimeException("初始化http配置异常", e);
        }
        //String authorization = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJ5dWV4aXUiLCJleHAiOjE1NzE0NzM1OTh9.QYK9oGRG48kdwzYlYgZIeF7H2svr3xgYDV8ghBtC-YUnLzfFpP_sDI39D2_00wiVONSelVd5qQrjtsXNxRUQ_A";
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

    private String httpPostWithCheckCode(String path, String json, String... checkColumnNames) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        checkResult(response, checkColumnNames);
        return response;
    }

    private String httpPost(String path, String json, String... checkColumnNames) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
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

    private String getStartTime(int n) throws ParseException { //前第n天的开始时间（当天的0点）
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, - n);
        Date d = c.getTime();
        String day = format.format(d);
        return day;
    }

    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeSuite
    public void login() {
        qaDbUtil.openConnection();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        initHttpConfig();
        String path = "/risk-login";
        String loginUrl = getIpPort() + path;
        String json = "{\"username\":\"demo@winsense.ai\",\"passwd\":\"f2064e9d2477a6bc75c132615fe3294c\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            this.authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            logger.info("authorization: {}", this.authorization);
        } catch (Exception e) {
            aCase.setFailReason("http post 调用异常，url = " + loginUrl + "\n" + e);
            logger.error(aCase.getFailReason());
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        saveData(aCase, caseName, caseName, "登录获取authentication");
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
        dingPushFinal();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

    @Test
    public void testShopList() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            String path = "/risk/shop/list";
            String json = "{}";
            String checkColumnName = "list";
            httpPostWithCheckCode(path, json, checkColumnName);

        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, caseName, "校验shop");
        }

    }



    /**
     * 校验 门店列表（/risk/shop/list）字段非空
     */
    @Test
    public void shopListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：门店列表（/risk/shop/list）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = shopList();
            for (Object obj : shopListNotNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 校验 渠道列表（/risk/channel/list）字段非空
     */
    @Test
    public void channelListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：渠道列表（/risk/channel/list）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = channelList();
            for (Object obj : channelListNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 校验 置业顾问列表（/risk/staff/consultant/list） 字段非空
     */
    @Test
    public void consultantListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：置业顾问列表（/risk/staff/consultant/list）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = consultantList();
            for (Object obj : consultantListNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 校验 员工类型列表（/risk/staff/type/list） 字段非空 （后续可能取消）
     */
    @Test
    public void stafftypeListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：员工类型列表（/risk/staff/type/list）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = stafftypeList();
            for (Object obj : stafftypeListNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 校验 案场二维码 字段非空
     **/
    @Test
    public void registerQrCodeNotNull() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            JSONObject data = registerQrCode();

            String qrcode = data.getString("qrcode");
            if (qrcode == null || "".equals(qrcode.trim())) {
                throw new Exception("案场二维码中[qrcode]为空！\n");
            }
            String url = data.getString("url");
            if (url == null || "".equals(url.trim())) {
                throw new Exception("案场二维码中[url]为空！\n");
            } else if (url.contains(".cn")) {
                throw new Exception("案场二维码中[url]不是线上url， url: " + url + "\n");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：案场二维码不为空");
        }
    }

    /**
     * 校验 订单详情（/risk/order/list）字段非空
     */
    @Test
    public void orderDetailNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：订单详情关键字段非空\n";

        String key = "";

        try {
            JSONArray list = orderList(1, "", 1, pageSize).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                String orderId = single.getString("order_id");
                System.out.println("orderId:" + orderId);
                JSONObject data = orderDetail(orderId);
                for (Object obj : orderDetailNotNull()) {
                    key = obj.toString();
                    checkUtil.checkNotNull(function, data, key);
                }
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 校验 订单关键环节（/risk/order/link/list） 字段非空
     */
    @Test
    public void linkNoteNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：订单关键环节字段非空\n";

        String key = "";

        try {
            String orderId = "";
            JSONObject data = orderLinkList(orderId);
            for (Object obj : orderLinkListNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 校验 顾客列表（/risk/customer/list） 字段非空
     */
    @Test
    public void customerListNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：顾客列表关键字段非空\n";

        String key = "";

        try {
            JSONObject data = customerList();
            for (Object obj : customerListNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     * 校验 订单列表（/risk/order/list） 字段非空
     */
    @Test
    public void orderListNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：订单列表关键字段非空\n";

        String key = "";

        try {

            JSONObject data = orderList(1, "", 1, pageSize);
            for (Object obj : orderListNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     * 校验 渠道详情（/risk/channel/detail） 字段非空
     */
    @Test
    public void channelDetailNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：渠道详情关键字段非空\n";

        String key = "";

        try {
            String orderId = "";
            String channelId =  channelPage(1,1).getJSONArray("list").getJSONObject(0).getString("channel_id");
            JSONObject data = channelDetail(channelId);
            for (Object obj : channelDetailNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     * V 2.4校验 下载风控单（/risk/evidence/risk-report/download） 字段非空 ！！得先核验再下载
     */
    @Test
    public void reportDownloadNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：下载风控单关键字段非空\n";

        String key = "";

        try {
            JSONArray list = orderList_audited(1, pageSize, "true").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                String orderId = single.getString("order_id");
                JSONObject data = reportDownload(orderId);
                for (Object obj : reportDownloadNotNull()) {
                    key = obj.toString();
                    checkUtil.checkNotNull(function, data, key);
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：下载风控单关键字段非空\n");
        }
    }



    /**
     * V2.4校验 人脸轨迹搜索（/risk/evidence/face/traces） 字段非空
     */
    @Test
    public void faceTracesNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸轨迹关键字段非空\n";

        String key = "";

        try {

            JSONObject data = faceTraces("https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/witness/100000000000235625/d020e3fe-8050-47bb-9c16-49a2aebdc8f0?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1612575519&Signature=5nntV5uCcxSdhDul3HP4FcJeQDg%3D"); //用了刷证的图片
            JSONArray list = data.getJSONArray("list");
            for (Object obj : faceTracesNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     * V3.0校验 渠道列表分页（/risk/channel/page）字段非空
     */
    @Test
    public void channelPageNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：渠道列表分页（/risk/channel/page）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = channelPage(1, pageSize);
            for (Object obj : channelPageNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * V3.0 校验 渠道业务员列表(2020.02.12)（/risk/channel/staff/list）字段非空
     */
    @Test
    public void  channelStaffListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：渠道业务员列表（/risk/channel/staff/list）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = channelStaffList();
            for (Object obj : channelStaffListNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * V3.0 校验 顾问top6(2020.02.12)（/risk/staff/top/）字段非空
     */
    @Test
    public void  riskStaffTopNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：顾问top6（/risk/staff/top/）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = riskStaffTop();
            for (Object obj : riskStaffTopNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * V3.0 校验 人物抓拍列表(2020-02-12)（/risk/evidence/person-catch/page）字段非空
     */
    @Test
    public void  personCatchNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人物抓拍列表（/risk/evidence/person-catch/page）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = personCatch(1,pageSize);
            for (Object obj : personCatchNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * V3.0 校验 风控规则列表(2020.02.12)（/risk/rule/list）字段非空
     */
    @Test
    public void  riskRuleListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：风控规则列表（/risk/rule/list）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = riskRuleList(1,pageSize);
            for (Object obj : riskRuleListNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * V3.0 校验 OCR二维码拉取-PC(2020.02.12)（/risk/shop/ocr/qrcode）字段非空  框架 要改
     */
    @Test
    public void  ocrQrcodeNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：OCR二维码拉取-PC（/risk/shop/ocr/qrcode）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = ocrQrcode();
            for (Object obj : ocrQrcodeNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * V3.0 校验 历史信息数据(2020.02.12)（/risk/history/rule/detail）字段非空  框架 要改
     */
    @Test
    public void  historyRuleDetailNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：历史信息数据（/risk/history/rule/detail）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = historyRuleDetail();
            for (Object obj : historyRuleDetailNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * V3.0 校验 订单数据趋势(2020.02.12)（/risk/history/rule/order/trend）字段非空  框架 要改
     */
    @Test
    public void  historyOrderTrendNotNullChk() throws ParseException {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：订单数据趋势（/risk/history/rule/order/trend）关键字段非空\n";

        String key = "";
        String start = getStartTime(7);
        String end = getStartTime(1);

        try {
            JSONObject data = historyOrderTrend(start,end);
            for (Object obj : historyOrderTrendNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     * V3.0 校验 访客数据趋势(2020.02.12)（/risk/history/rule/customer/trend）字段非空  框架 要改
     */
    @Test
    public void  historyCustomerTrendNotNullChk() throws ParseException {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：访客数据趋势（/risk/history/rule/customer/trend）关键字段非空\n";

        String key = "";
        String start = getStartTime(7);
        String end = getStartTime(1);

        try {
            JSONObject data = historyCustomerTrend(start,end);
            for (Object obj : historyCustomerTrendNotNull()) {
                key = obj.toString();
                checkUtil.checkNotNull(function, data, key);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }




    private Object getShopId() {
        return "97";
    }

    private final int pageSize = 50;


    private static final String ORDER_DETAIL = "/risk/order/detail";
    private static final String CUSTOMER_LIST = "/risk/customer/list";
    private static final String ORDER_LIST = "/risk/order/list";
    private static final String STAFF_LIST = "/risk/staff/page";
    private static final String STAFF_TYPE_LIST = "/risk/staff/type/list";



    private static String CUSTOMER_LIST_JSON = "{\"search_type\":\"${searchType}\"," +
            "\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";



    //    顾客列表中是size参数控制一页显示的条数，订单列表中是pageSize控制
    private static String ORDER_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"page_size\":\"${pageSize}\"}";

    private static String ORDER_DETAIL_JSON = "{\"order_id\":\"${orderId}\"," +
            "\"shop_id\":${shopId}}";

    private static String ORDER_STEP_LOG_JSON = ORDER_DETAIL_JSON;


    private static String STAFF_TYPE_LIST_JSON = "{\"shop_id\":${shopId}}";

    private static String STAFF_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";







    public String getValue(JSONObject data, String key) {
        String value = data.getString(key);

        if (value == null || "".equals(value)) {
            value = "";
        }

        return value;
    }


    /**
     * 顾客列表
     */
    public JSONObject customerList() throws Exception {
        String url = "/risk/customer/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"search_type\":\"CHANCE\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":100\n" +
                        "}";


        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 门店列表（订单列表）
     */
    public JSONObject shopList() throws Exception {
        String url = "/risk/shop/list";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 渠道列表
     */
    public JSONObject channelList() throws Exception {
        String url = "/risk/channel/list";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 置业顾问列表
     */
    public JSONObject consultantList() throws Exception {
        String url = "/risk/staff/consultant/list";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 员工类型列表
     */
    public JSONObject stafftypeList() throws Exception {
        String url = "/risk/staff/type/list";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 渠道详情
     */
    public JSONObject channelDetail(String channelId) throws Exception {
        String url = "/risk/channel/detail";
        String json =
                "{" +
                        "    \"shop_id\":\"" + getShopId() + "\"," +
                        "    \"channel_id\":\"" + channelId + "\"" +
                        "}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 订单详情
     */
    public JSONObject orderDetail(String orderId) throws Exception {
        String json =
                "{" +
                        "   \"shop_id\" : " + getShopId() + ",\n" +
                        "\"order_id\":" + orderId +
                        "}";
        String url = "/risk/order/detail";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /*订单列表根据审核过进行筛选*/
    public JSONObject orderList_audited(int page, int pageSize, String is_audited) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"page\":" + page + ",\n"+
                        "   \"is_audited\":\"" + is_audited + "\",\n"+
                        "    \"size\":" + pageSize + "\n" +
                        "}";
        String[] checkColumnNames = {};
        String res = httpPostWithCheckCode(url, json, checkColumnNames);

        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * 订单关键步骤接口
     */
    public JSONObject orderLinkList(String orderId) throws Exception {
        String url = "/risk/order/link/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"orderId\":\"" + orderId + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);//订单详情与订单跟进详情入参json一样

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 订单列表
     */
    public JSONObject orderList(int status, String namePhone, int page, int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"page\":" + page + ",\n";
        if (status != -1) {
            json += "    \"status\":" + status + ",\n";
        }

        if (!"".equals(namePhone)) {
            json += "    \"customer_name\":\"" + namePhone + "\",\n";
        }

        json += "    \"size\":" + pageSize + "\n" +
                "}";
        String[] checkColumnNames = {};
        String res = httpPostWithCheckCode(url, json, checkColumnNames);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 查询自主注册二维码信息
     */
    public JSONObject registerQrCode() throws Exception {

        String requestUrl = "/risk/shop/self-register/qrcode";

        String json = "{\"shop_id\":" + getShopId() + "}";

        String res = httpPostWithCheckCode(requestUrl, json);
        JSONObject data = JSON.parseObject(res).getJSONObject("data");

        return data;
    }

    /**
     * 下载风控单
     */
    public JSONObject reportDownload(String orderId) throws Exception {
        String url = "/risk/evidence/risk-report/download";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"orderId\":\"" + orderId + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 人脸搜索
     */
    public JSONObject faceTraces(String showUrl) throws Exception {
        String url = "/risk/evidence/face/traces";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"show_url\":\"" + showUrl + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 渠道列表分页
     */
    public JSONObject channelPage(int page, int pageSize) throws Exception {
        String url = "/risk/channel/page";
        String json = "{\n" +
                "   \"page\":" + page + ",\n" +
                "   \"size\":" + pageSize + ",\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 渠道业务员列表(2020.02.12)
     */
    public JSONObject channelStaffList() throws Exception {
        String url = "/risk/channel/staff/list";
        String json = "{\n" +
                "   \"channel_id\":" + channelId + ",\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 顾问top6(2020.02.12)
     */
    public JSONObject riskStaffTop() throws Exception {
        String url = "/risk/staff/top/";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 人物抓拍列表(2020-02-12)
     */
    public JSONObject personCatch(int page, int pageSize) throws Exception {
        String url = "/risk/evidence/person-catch/page";
        String json = "{\n" +
                "   \"page\":" + page + ",\n" +
                "   \"size\":" + pageSize + ",\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 风控规则列表(2020.02.12)
     */
    public JSONObject riskRuleList(int page, int pageSize) throws Exception {
        String url = "/risk/rule/list";
        String json = "{\n" +
                "   \"page\":" + page + ",\n" +
                "   \"size\":" + pageSize + ",\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * OCR二维码拉取-PC(2020.02.12) 框架 要改
     */
    public JSONObject ocrQrcode() throws Exception {
        String url = "/risk/shop/ocr/qrcode";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 历史信息数据(2020.02.12) 框架 要改
     */
    public JSONObject historyRuleDetail() throws Exception {
        String url = "/risk/history/rule/detail";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 订单数据趋势(2020.02.12) 框架 要改
     */
    public JSONObject  historyOrderTrend(String start,String end) throws Exception {
        String url = "/risk/history/rule/order/trend";
        String json = "{\n" +
                "   \"trend_type\":\"" + "SEVEN" + "\",\n" +
                "   \"start_day\":\"" + start + "\",\n" +
                "   \"end_day\":\"" + end + "\",\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     *  访客数据趋势(2020.02.12) 框架 要改
     */
    public JSONObject  historyCustomerTrend(String start,String end) throws Exception {
        String url = "/risk/history/rule/customer/trend";
        String json = "{\n" +
                "   \"trend_type\":\"" + "SEVEN" + "\",\n" +
                "   \"start_day\":\"" + start + "\",\n" +
                "   \"end_day\":\"" + end + "\",\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }


//    --------------------------------------------------------接口方法-------------------------------------------------------


    public String getOneStaffType() throws Exception {
        JSONArray staffTypeList = staffTypeList();
        Random random = new Random();
        return staffTypeList.getJSONObject(random.nextInt(3)).getString("staff_type");
    }


    public JSONArray customerList(String searchType, int page, int pageSize) throws Exception {
        return customerListReturnData(searchType, page, pageSize).getJSONArray("list");
    }

    public JSONObject customerListReturnData(String searchType, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(
                CUSTOMER_LIST_JSON, ImmutableMap.builder()
                        .put("shopId", getShopId())
                        .put("searchType", searchType)
                        .put("page", page)
                        .put("pageSize", pageSize)
                        .build()
        );

        String res = httpPostWithCheckCode(CUSTOMER_LIST, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONArray orderList(int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(ORDER_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );
        String[] checkColumnNames = {};
        String res = httpPostWithCheckCode(ORDER_LIST, json, checkColumnNames);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    public JSONArray staffList(int page, int pageSize) throws Exception {
        return staffListReturnData(page, pageSize).getJSONArray("list");
    }

    public JSONObject staffListReturnData(int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(STAFF_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );

        String res = httpPostWithCheckCode(STAFF_LIST, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONArray staffTypeList() throws Exception {
        String json = StrSubstitutor.replace(STAFF_TYPE_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .build()
        );

        String res = httpPostWithCheckCode(STAFF_TYPE_LIST, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    public String genPhoneNum() {
        Random random = new Random();
        long num = 17700000000L + random.nextInt(99999999);

        return String.valueOf(num);
    }





    private void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    private void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());
            dingPush("飞单日常 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        if (DEBUG.trim().toLowerCase().equals("false")) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.ONLINE_OPEN_MANAGEMENT_PLATFORM_GRP);

            alarmPush.onlineRgn(msg);
            this.FAIL = true;
        }
        Assert.assertNull(aCase.getFailReason());
    }

    private void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.ONLINE_OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 华成裕
            //18513118484 杨航
            //15011479599 谢志东
            //18600872221 蔡思明
            String[] rd = {"18513118484", //杨航
                    "15011479599", //谢志东
                    "15898182672"}; //华成裕
            alarmPush.alarmToRd(rd);
        }
    }

    @DataProvider(name = "SEARCH_TYPE")
    private static Object[] searchType() {
        return new Object[]{
                "CHANCE",
//                "CHECKED",
//                "REPORTED"
        };
    }

    @DataProvider(name = "ALL_DEAL_IDCARD_PHONE")
    private static Object[][] dealIdCardPhone() {
        return new Object[][]{
                new Object[]{
                        "17800000002", "111111111111111111", "于海生", "2019-12-13 13:44:26"
                },
                new Object[]{
                        "19811111111", "222222222222222222", "廖祥茹", "2019-12-13 13:40:53"
                },
                new Object[]{
                        "18831111111", "333333333333333333", "华成裕", "2019-12-13 15:27:22"
                },
                new Object[]{
                        "18888811111", "333333333333333335", "傅天宇", "2019-12-13 15:05:53"
                },
                new Object[]{
                        "14111111135", "111111111111111114", "李俊延", "2019-12-17 16:51:31"
                }
        };
    }

    @DataProvider(name = "ALL_DEAL_PHONE")
    private static Object[] dealPhone() {
        return new Object[]{
                "17800000002",
                "19811111111",
                "18831111111",
                "18888811111",
                "14111111135"
        };
    }



    private Object[] customerListNotNull() {
        return new Object[]{
                "[list]-customer_name",
                "[list]-phone",
                "[list]-gmt_create"
        };
    }

    private Object[] orderListNotNull() {
        return new Object[]{
                "[list]-order_id",
                "[list]-customer_name",
                "[list]-customer_phone",
                "[list]-is_audited",

        };
    }

    private Object[] orderLinkListNotNull() {
        return new Object[]{

        };
    }

    private Object[] channelDetailNotNull() {
        return new Object[]{
                "channel_id",
                "channel_name",
                "owner_principal",
                "phone",
                "rule_id", //V3.0
                "rule_name", //V3.0
        };
    }

    private Object[] shopListNotNotNull() {
        return new Object[]{
                "[list]-shop_id",
                "[list]-auth_level",
                "[list]-shop_name",
        };
    }

    private Object[] channelListNotNull() {
        return new Object[]{
                "[list]-channel_id",
                "[list]-channel_name",
                "[list]-owner_principal",
                "[list]-phone",
                "[list]-register_time",
        };
    }

    private Object[] consultantListNotNull() {
        return new Object[]{
                "[list]-id", //V3.0
                "[list]-staff_id", //V3.0
                "[list]-staff_name", //V3.0
                "[list]-phone", //V3.0
        };
    }

    private Object[] stafftypeListNotNull() {
        return new Object[]{
                "[list]-type_name",
                "[list]-staff_type",
        };
    }

    private Object[] orderDetailNotNull() {
        return new Object[]{
                "customer_name",
                "phone",
                //"report_time",
                "risk_link",
                "shop_id",
                "total_link",
                "normal_link",
                "order_id",
                "order_status",
                "order_status_tips",
                "is_audited",
        };
    }

    private Object[] reportDownloadNotNull() {
        return new Object[]{
                "file_url",
        };
    }

    private Object[] faceTracesNotNull() {
        return new Object[]{
                "[list]-shoot_time",
                "[list]-camera_name",
                "[list]-face_url",
                "[list]-similar",
                "[list]-original_url",
        };
    }

    private Object[] channelPageNotNull(){
        return new Object[]{
                "[list]-channel_id",
                "[list]-channel_name",
                "[list]-owner_principal",
                "[list]-total_customers",
                "[list]-register_time",
                "[list]-rule_id",
                "[list]-rule_name",
        };
    }

    private Object[] channelStaffListNotNull(){
        return new Object[]{
                "[list]-id",
                "[list]-staff_id",
                "[list]-staff_name",
                "[list]-phone",
        };
    }

    private Object[] riskStaffTopNotNull(){
        return new Object[]{
                "[list]-id",
                "[list]-staff_id",
                "[list]-staff_name",
                "[list]-phone",
        };
    }

    private Object[] personCatchNotNull(){
        return new Object[]{
                "[list]-shoot_time",
                "[list]-camera_name",
                "[list]-face_url",
                "[list]-person_type",
                "[list]-person_type_name",
                "[list]-customer_id",
                "[list]-original_url",
        };
    }

    private Object[] riskRuleListNotNull(){
        return new Object[]{
                "[list]-id",
                "[list]-name",
                "[list]-ahead_report_time",
                "[list]-type",
                "[list]-gmt_create",
        };
    }

    private Object[] ocrQrcodeNotNull(){ //框架 要改
        return new Object[]{
                "qrcode",
                "url",
                "code",
        };
    }

    private Object[] historyRuleDetailNotNull(){
        return new Object[]{
                "trading_days",
                "abnormal_link",
                "money",
                "natural_visitor",
                "channel_visitor",
                "normal_order",
                "unknow_order",
                "risk_order",
        };
    }

    private Object[] historyOrderTrendNotNull(){
        return new Object[]{
                "[list]-day",
                "[list]-all_order",
                "[list]-normal_order",
                "[list]-unknow_order",
                "[list]-risk_order",
        };
    }

    private Object[] historyCustomerTrendNotNull(){
        return new Object[]{
                "[list]-all_visitor",
                "[list]-channel_visitor",
                "[list]-day",
                "[list]-natural_visitor",
        };
    }

}
