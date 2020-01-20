
package com.haisheng.framework.testng.bigScreen;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.exception.SdkClientException;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.collect.ImmutableMap;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
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
import java.util.*;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class FeidanMiniApiInterfaceNotNullDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    private HttpConfig config;

    String channelId = "5";
    String gongErId = "12";
    String anShengId = "15";

    String genderMale = "MALE";
    String genderFemale = "FEMALE";

    int pageSize = 100;

    private String getIpPort() {
        return "http://dev.store.winsenseos.cn";
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

    ApiResponse apiResponse = new ApiResponse();
    String UID = "uid_7fc78d24";
    String APP_ID_LOGIN = "097332a388c2";
    String AK = "77327ffc83b27f6d";
    String SK = "7624d1e6e190fbc381d0e9e18f03ab81";
    private LogMine logMine = new LogMine(logger);

    private ApiResponse sendRequestWithGate(String router, String[] secKey, String json) throws Exception {
        try {
            Credential credential = new Credential(AK, SK);
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID_LOGIN)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataSecKey(secKey)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.cn/retail/api/data/device", credential);
            apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant("apiRequest" + JSON.toJSONString(apiRequest));
            logMine.printImportant("apiResponse" + JSON.toJSONString(apiResponse));
        } catch (SdkClientException e) {
            String msg = e.getMessage();
            throw new Exception(msg);
        } catch (Exception e) {
            throw e;
        }
        return apiResponse;
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

    private String httpPost(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    private String httpGet(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.get(config);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
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


    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeSuite
    public void login() {
        qaDbUtil.openConnection();
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        initHttpConfig();
        String path = "/risk-login";
        String loginUrl = getIpPort() + path;
        String json = "{\"username\":\"yuexiu@test.com\",\"passwd\":\"f5b3e737510f31b88eb2d4b5d0cd2fb4\"}";
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

        saveData(aCase, ciCaseName, caseName, "登录获取authentication");
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


    private Object getShopId() {
        return "4116";
    }

    private static final String ADD_ORDER = "/risk/order/createOrder";
    private static final String ORDER_DETAIL = "/risk/order/detail";
    private static final String CUSTOMER_LIST = "/risk/customer/list";
    private static final String ORDER_LIST = "/risk/order/list";
    private static final String STAFF_LIST = "/risk/staff/page";
    private static final String STAFF_TYPE_LIST = "/risk/staff/type/list";


    private static String CUSTOMER_LIST_WITH_CHANNEL_JSON = "{\"search_type\":\"${searchType}\"," +
            "\"shop_id\":${shopId},\"channel_id\":\"${channelId}\",\"page\":\"${page}\",\"size\":\"${pageSize}\"}";
//    顾客列表中是size参数控制一页显示的条数，订单列表中是pageSize控制

    //    顾客列表中是size参数控制一页显示的条数，订单列表中是pageSize控制
    private static String ORDER_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"page_size\":\"${pageSize}\"}";
    private static String ORDER_LIST_WITH_CHANNEL_JSON = "{\"shop_id\":${shopId},\"channel_id\":\"${channelId}\",\"page\":\"1\",\"page_size\":\"10000\"}";
    private static String ORDER_LIST_WITH_STATUS_JSON = "{\"shop_id\":${shopId},\"status\":\"${status}\",\"page\":\"1\",\"page_size\":\"10000\"}";
    private static String ORDER_LIST_WITH_PHONE_JSON = "{\"shop_id\":${shopId},\"customer_name\":\"${customerName}\",\"page\":\"1\",\"page_size\":\"10000\"}";

    private static String ORDER_DETAIL_JSON = "{\"order_id\":\"${orderId}\"," +
            "\"shop_id\":${shopId}}";

    private static String CHANNEL_STAFF_LIST_JSON = "{\"channel_id\":\"${channelId}\"," +
            "\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";
    private static String CHANNEL_STAFF_LIST_PHOEN_JSON = "{\"channel_id\":\"${channelId}\"," +
            "\"shop_id\":${shopId},\"name_phone\":\"${namePhone}\",\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

    private static String CUSTOMER_INSERT_JSON = "{\"shop_id\":\"${shopId}\",\"channel_id\":${channelId}," +
            "\"channel_staff_id\":\"${channelStaffId}\",\"adviser_id\":\"${adviserId}\"," +
            "\"gender\":\"${gender}\",\"customer_name\":\"${customerName}\",\"phone\":\"${phone}\"}";

    private static String CHANNEL_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"page_size\":\"${pageSize}\"}";

    private static String STAFF_TYPE_LIST_JSON = "{\"shop_id\":${shopId}}";

    private static String STAFF_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

    private static String STAFF_LIST_WITH_TYPE_JSON = "{\"shop_idaddStaffTestPage\":${shopId},\"staff_type\":\"${staffType}\",\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

    private static String ADD_CHANNEL_JSON = "{\"shop_id\":${shopId},\"channel_name\":\"${channelName}\"," +
            "\"owner_principal\":\"${owner}\",\"phone\":\"${phone}\",\"contract_code\":\"${contractCode}\"}";

    private static String ADD_CHANNEL_STAFF_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"channel_id\":\"${channelId}\",\"phone\":\"${phone}\"}";

    private static String ADD_CHANNEL_STAFF_WITH_PIC_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"channel_id\":\"${channelId}\",\"phone\":\"${phone}\",\"face_url\":\"${faceUrl}\"}";

    private static String EDIT_CHANNEL_STAFF_WITH_PIC_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"channel_id\":\"${channelId}\",\"face_url\":\"${faceUrl}\",\"phone\":\"${phone}\"}";

    private static String EDIT_CHANNEL_STAFF_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"channel_id\":\"${channelId}\",\"phone\":\"${phone}\"}";

    private static String ADD_STAFF_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"staff_type\":\"${staffType}\",\"phone\":\"${phone}\",\"face_url\":\"${faceUrl}\"}";

    private static String EDIT_STAFF_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"staff_type\":\"${staffType}\",\"phone\":\"${phone}\",\"face_url\":\"${faceUrl}\"}";


//    ----------------------------------------------接口方法--------------------------------------------------------------------


    @Test
    public void shopListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验shoplist关键字段非空\n";

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

    @Test
    public void channelListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验channellist关键字段非空\n";

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

    @Test
    public void consultantListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验consultantlist关键字段非空\n";

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
     * 员工类型列表，后续可能取消
     */
    @Test
    public void stafftypeListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验stafflistlist关键字段非空\n";

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
     * 案场二维码不为空
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
                throw new Exception("案场二维码中【qrcode】为空！");
            }
            String url = data.getString("url");
            if (url == null || "".equals(url.trim())) {
                throw new Exception("案场二维码中【url】为空！");
            } else if (!url.contains(".cn")) {
                throw new Exception("案场二维码中【url】不是日常url， url: " + url);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "案场二维码不为空");
        }
    }

    /**
     * 订单详情关键字非空
     */
    @Test
    public void orderDetailNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验订单详情关键字段非空\n";

        String key = "";

        try {
            JSONArray list = orderList(1,"",1,pageSize).getJSONArray("list");
            for (int i = 0;i< list.size();i++){
                JSONObject single = list.getJSONObject(i);
                String orderId = single.getString("order_id");
                System.out.println("orderId:"+ orderId);
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
     * 订单关键环节非空
     */
    @Test
    public void linkNoteNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验订单环节环节字段非空\n";

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
    @Test
    public void customerTypeListNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验顾客身份列表关键字段非空\n";

        String key = "";

        try {
            JSONObject data = customerTypeList();
            for (Object obj : customerTypeListNotNull()) {
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


    @Test
    public void timeRangeListNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验到访时间枚举列表校验字段非空\n";

        String key = "";

        try {
            JSONObject data = timeRangeList();
            for (Object obj : timeRangeListNotNull()) {
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
*/
    @Test
    public void customerListNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验顾客列表关键字段非空\n";

        String key = "";

        try {
            JSONObject data = customerList(channelId, anShengId);
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


    @Test
    public void orderListNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验订单列表关键字段非空\n";

        String key = "";

        try {

            JSONObject data = orderList(1, "", 1,pageSize);
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


    @Test
    public void channelDetailNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验渠道详情关键字段非空\n";

        String key = "";

        try {
            String orderId = "";
            JSONObject data = channelDetail("791");
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
     * 3.1 顾客身份列表
     */
    public JSONObject customerTypeList() throws Exception {

        String url = "/risk/customer/type/list";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "}";


        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 3.2 年龄分组
     */
    public JSONObject ageGroupList() throws Exception {

        String url = "/risk/age/group/list";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "}";


        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 3.3 到访时间枚举列表
     */
    public JSONObject timeRangeList() throws Exception {

        String url = "/risk/customer/timeRange/list";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "}";


        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 3.4 顾客列表
     */
    public JSONObject customerList(String channel, String adviser) throws Exception {

        String json =
                "{\n" +
                        "    \"adviser_id\":" + adviser + ",\n" +
                        "    \"channel_id\":" + channel + ",\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"search_type\":\"CHANCE\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":100\n" +
                        "}";


        String res = httpPostWithCheckCode(CUSTOMER_LIST, json);

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
     * 3.5 人脸搜索顾客列表
     */
    public JSONArray customerFaceSingle(String faceUrl) throws Exception {
        String url = "/risk/customer/face/single";
        String json =
                "{" +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "    \"face_url\":\"" + faceUrl + "\"" +
                        "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }


    /**
     * 4.5 订单详情
     */
    public JSONObject orderDetail(String orderId) throws Exception {
        String json = StrSubstitutor.replace(ORDER_DETAIL_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("orderId", orderId)
                .build()
        );
        String res = httpPostWithCheckCode(ORDER_DETAIL, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 4.6 订单关键步骤接口
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
     * 4.8 订单列表
     */
    public JSONObject orderList(int status, String namePhone, int page, int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"page\":" + page+  ",\n";
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
     * 8.1 员工身份列表
     */
    public JSONArray staffTypeList() throws Exception {
        String json = StrSubstitutor.replace(STAFF_TYPE_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .build()
        );

        String res = httpPostWithCheckCode(STAFF_TYPE_LIST, json);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    /**
     * 8.2 员工列表
     */
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

        String res = httpPostWithCheckCode(STAFF_LIST, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 11.1 查询自主注册二维码信息
     */
    public JSONObject registerQrCode() throws Exception {

        String requestUrl = "/risk/shop/self-register/qrcode";

        String json = "{\"shop_id\":" + getShopId() + "}";

        String res = httpPostWithCheckCode(requestUrl, json);
        JSONObject data = JSON.parseObject(res).getJSONObject("data");

        return data;
    }


    /**
     * 10.1 人证对比机数据上传接口
     */
    public void witnessUpload() throws Exception {
        String url = "/business/risk/WITNESS_UPLOAD/v1.0";
        String json =
                "{\n" +
                        "    \"card_id\":\"\",\n" +
                        "    \"person_name\":\"\",\n" +
                        "    \"is_pass\": true,\n" +
                        "    \"card_pic\":\"\",\n" +
                        "    \"capture_pic\":\"\"\n" +
                        "}\n";

        httpPostWithCheckCode(url, json);
    }


//    -----------------------------------------------测试case--------------------------------------------------------------


    /**
     * 顾客自主创建时，不填写全手机号（中间四位带*，或者是不符合手机号规范）
     **/
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

            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            alarmPush.dailyRgn(msg);
            this.FAIL = true;
        }
        Assert.assertNull(aCase.getFailReason());
    }

    private void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

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


    private Object[] customerTypeListNotNull() {
        return new Object[]{
                "[list]-customer_type",
                "[list]-desc",
                "[list]-type_name"
        };
    }

    private Object[] ageGroupListNotNull() {
        return new Object[]{
                "[list]-age_group_id",
                "[list]-type_name"
        };
    }

    private Object[] timeRangeListNotNull() {
        return new Object[]{
                "[time_range_list]-type",
                "[time_range_list]-type_name"
        };
    }

    private Object[] customerListNotNull() {
        return new Object[]{
                "[list]-customer_name",
                "[list]-phone",
                "[list]-adviser_name",
                "[list]-channel_name",
                "[list]-report_time"
        };
    }

    private Object[] customerSimpleInfoNotNull() {
        return new Object[]{
                "[list]-customer_name",
                "[list]-phone",
                "[list]-adviser_name",
                "[list]-channel_name"
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

    private Object[] adviserListNotNull() {
        return new Object[]{
                "[list]-face_url",
                "[list]-gender",
                "[list]-id",
                "[list]-is_delete",
                "[list]-phone",
                "[list]-staff_name",
                "[list]-type_name"
        };
    }

    private Object[] reportInfoNotNull() {
        return new Object[]{
                "[list]-cid",
                "[list]-channel_id",
                "[list]-desc"
        };
    }

    private Object[] orderLinkListNotNull() {
        return new Object[]{

        };
    }

    private Object[] orderStatusListNotNull() {
        return new Object[]{
                "[list]-cid",
                "[list]-type",
                "[list]-status_name",

        };
    }

    private Object[] channelDetailNotNull() {
        return new Object[]{
                "channel_id",
                "channel_name",
                "owner_principal",
                "phone",
        };
    }

    private Object[] shopListNotNotNull() {
        return new Object[]{
                "[list]-shop_id",
                "[list]-auth_level",
                "[list]-shop_name",
        };
    }

    private  Object[] channelListNotNull() {
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
                "[list]-type_name",
                "[list]-shop_id",
                "[list]-phone",
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


}