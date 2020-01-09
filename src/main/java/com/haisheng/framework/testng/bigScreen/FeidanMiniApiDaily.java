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

public class FeidanMiniApiDaily {

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

    int pageSize = 10000;

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

    private ApiResponse sendRequestWithGate(String router, String[] resource,String json) throws Exception {
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
                    .dataResource(resource)
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

    @Test
    public void testShopList() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String path = "/risk/shop/list";
            String json = "{}";
            String checkColumnName = "list";
            httpPostWithCheckCode(path, json, checkColumnName);

        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "校验shop");
        }

    }

    private Object getShopId() {
        return "4116";
    }

    private static final String ADD_ORDER = "/risk/order/createOrder";
    private static final String ORDER_DETAIL = "/risk/order/detail";
    private static final String CUSTOMER_LIST = "/risk/customer/list";
    private static final String ORDER_LIST = "/risk/order/list";
    private static final String CHANNEL_STAFF_PAGE = "/risk/channel/staff/page";
    private static final String CUSTOMER_INSERT = "/risk/customer/insert";
    private static final String CHANNEL_LIST = "/risk/channel/page";
    private static final String STAFF_LIST = "/risk/staff/page";
    private static final String STAFF_TYPE_LIST = "/risk/staff/type/list";
    private static final String ADD_CHANNEL = "/risk/channel/add";
    private static final String ADD_CHANNEL_STAFF = "/risk/channel/staff/register";
    private static final String EDIT_CHANNEL_STAFF = "/risk/channel/staff/edit/";
    private static final String ADD_STAFF = "/risk/staff/add";
    private static final String DELETE_STAFF = "/risk/staff/delete/";
    private static final String EDIT_STAFF = "/risk/staff/edit/";

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

    public JSONArray customerListWithChannel(String searchType, String channelId, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(CUSTOMER_LIST_WITH_CHANNEL_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("searchType", searchType)
                .put("channelId", channelId)
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );
        String res = httpPostWithCheckCode(CUSTOMER_LIST, json);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    public JSONObject uploadImage(String imagePath) {
        String url = "http://dev.store.winsenseos.cn/risk/imageUpload";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("authorization", authorization);
        httpPost.addHeader("shop_id", String.valueOf(getShopId()));
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        File file = new File(imagePath);
        try {
            builder.addBinaryBody(
                    "img_file",
                    new FileInputStream(file),
                    ContentType.IMAGE_JPEG,
                    file.getName()
            );

            builder.addTextBody("path", "shopStaff", ContentType.TEXT_PLAIN);

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            this.response = EntityUtils.toString(responseEntity, "UTF-8");

            checkCode(this.response, StatusCode.SUCCESS, file.getName() + ">>>");
            logger.info("response: " + this.response);

        } catch (Exception e) {
            failReason = e.toString();
            e.printStackTrace();
        }

        return JSON.parseObject(this.response).getJSONObject("data");
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
     * 3.5 顾客简要信息接口（判断顾客是否存在）
     */
    public JSONObject customerSimpleInfo(String phone) throws Exception {
        String url = "/risk/customer/sampleInfo";
        String json =
                "{" +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "    \"phone\":\"" + phone + "\"" +
                        "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 3.9 新建顾客
     */
    public void newCustomer(String channelId, String channelStaffId, String adviserId, String phone, String customerName, String gender) throws Exception {

        String json = StrSubstitutor.replace(CUSTOMER_INSERT_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("channelId", channelId) //测试【勿动】
                .put("channelStaffId", channelStaffId)//宫二
                .put("adviserId", adviserId)
                .put("phone", phone)
                .put("customerName", customerName)
                .put("gender", gender)
                .build()
        );

        String res = httpPost(CUSTOMER_INSERT, json);
        int codeRes = JSON.parseObject(res).getInteger("code");

        if (codeRes == 2002) {
            phone = genPhoneNum();
            newCustomer(channelId, channelStaffId, adviserId, phone, customerName, gender);
        }
    }

    /**
     * 3.10 修改顾客信息
     */
    public JSONObject customerEdit(String cid, String customerName, String phone, String adviserId) throws Exception {
        String url = "/risk/customer/edit/" + cid;
        String json =
                "{\n" +
                        "    \"cid\":\"" + cid + "\",\n" +
                        "    \"customer_name\":\"" + customerName + "\",\n";
        if (!"".equals(phone)) {
            json += "    \"phone\":\"" + phone + "\",\n";
        }

        json +=
                "    \"adviser_id\":" + adviserId + ",\n" +
                        "    \"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 4.1 根据手机号查询报备信息
     */
    public JSONObject searchReportInfoByPhone(String phone) throws Exception {
        String url = "/risk/order/searchReportInfoByPhone";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"phone\":\"" + phone + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);//订单详情与订单跟进详情入参json一样

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 4.4 创建订单
     */
    public JSONObject createOrder(String phone, String orderId, String channelId, String smsCode) throws Exception {

        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"order_id\":\"" + orderId + "\",";
        if ("".equals(channelId)) {
            json += "    \"channel_id\":\"" + channelId + "\",";
        }

        json += "    \"sms_code\":\"" + smsCode + "\"" +
                "}";
        String res = httpPostWithCheckCode(ADD_ORDER, json);

        return JSON.parseObject(res);
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
    public JSONObject orderList(int status, int pageSize) throws Exception {
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n";
        if (status != -1) {
            json += "    \"status\":\"" + status + "\",\n";
        }
        json += "    \"size\":" + pageSize + "\n" +
                "}";
        String[] checkColumnNames = {};
        String res = httpPostWithCheckCode(ORDER_LIST, json, checkColumnNames);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONArray orderListWithChannel(String channelId, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(ORDER_LIST_WITH_CHANNEL_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("channelId", channelId)
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );
        String res = httpPostWithCheckCode(ORDER_LIST, json);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    public JSONArray orderListWithPhone(String customerName, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(ORDER_LIST_WITH_PHONE_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("customerName", customerName)
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );
        String res = httpPostWithCheckCode(ORDER_LIST, json);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    /**
     * 4.9 订单状态列表
     */
    public JSONObject orderstatusList() throws Exception {
        String url = "/risk/order/status/audit";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() +
                        "}\n";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 4.15 订单人工核验-提交
     */
    public JSONArray orderstatusAudit(String orderId, String visitor) throws Exception {
        String url = "/risk/order/status/audit";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"order_id\":" + orderId + ",\n" +
                        "    \"visitor\":\"" + visitor + "\"\n" +
                        "}\n";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    /**
     * 6.1 渠道新增
     */
    public JSONObject addChannel(String channelName, String owner, String phone, String contractCode) throws Exception {
        String json = StrSubstitutor.replace(ADD_CHANNEL_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("channelName", channelName)
                .put("owner", owner)
                .put("phone", phone)
                .put("contractCode", contractCode)
                .build()
        );
        String res = httpPostWithCheckCode(ADD_CHANNEL, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 6.2 渠道列表
     */

    public JSONObject channelList(int page, int pageSize) throws Exception {
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"page\":" + page + "," +
                        "    \"size\":" + pageSize +
                        "}";
        String res = httpPostWithCheckCode(CHANNEL_LIST, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 6.3 渠道详情
     */
    public JSONObject channelDetail(String channelId) throws Exception {
        String url = "/risk/channel/detail";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"channel_id\":\"" + channelId + "\"\n" +
                        "}\n";
        String res = httpPostWithCheckCode(CHANNEL_LIST, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 6.5 渠道业务员列表
     */
    public JSONArray channelStaffList(String channelId, int page, int pageSize) throws Exception {

        return channelStaffListReturnData(channelId, page, pageSize).getJSONArray("list");
    }

    public JSONObject channelStaffListReturnData(String channelId, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(CHANNEL_STAFF_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("channelId", channelId)
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );
        String res = httpPostWithCheckCode(CHANNEL_STAFF_PAGE, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject channelStaffListWithPhone(String channelId, String namePhone, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(CHANNEL_STAFF_LIST_PHOEN_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("channelId", channelId)
                .put("namePhone", namePhone)
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );
        String res = httpPostWithCheckCode(CHANNEL_STAFF_PAGE, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 6.6 合作渠道员工注册接口
     */
    public JSONObject addChannelStaff(String staffName, String channelId, String phone) throws Exception {
        String json = StrSubstitutor.replace(ADD_CHANNEL_STAFF_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("channelId", channelId)
                .put("phone", phone)
                .build()
        );
        String res = httpPostWithCheckCode(ADD_CHANNEL_STAFF, json);

        JSONObject result = JSON.parseObject(res);
        int codeRes = result.getInteger("code");
        String message = result.getString("message");

        if (codeRes == 1001) {
            if ("当前手机号已被使用".equals(message)) {
                phone = genPhoneNum();
                addChannelStaff(staffName, channelId, phone);
            } else {
                String id = getIdOfStaff(res);

                if (!"".equals(id)) {
                    changeChannelStaffState(id);
                    deleteStaff(id);
                    addChannelStaff(staffName, channelId, phone);
                }
            }
        }

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String addChannelStaffRes(String staffName, String channelId, String phone) throws Exception {
        String json = StrSubstitutor.replace(ADD_CHANNEL_STAFF_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("channelId", channelId)
                .put("phone", phone)
                .build()
        );
        String res = httpPost(ADD_CHANNEL_STAFF, json);

        return res;
    }

    public JSONObject addChannelStaffWithPic(String staffName, String channelId, String phone, String pic) throws Exception {
        String json = StrSubstitutor.replace(ADD_CHANNEL_STAFF_WITH_PIC_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("channelId", channelId)
                .put("phone", phone)
                .put("faceUrl", pic)
                .build()
        );
        String res = httpPost(ADD_CHANNEL_STAFF, json);

        JSONObject result = JSON.parseObject(res);
        int codeRes = result.getInteger("code");
        String message = result.getString("message");

        if (codeRes == 1001) {
            if ("当前手机号已被使用".equals(message)) {
                phone = genPhoneNum();
                addChannelStaffWithPic(staffName, channelId, phone, pic);
            } else {
                String id = getIdOfStaff(res);

                if (!"".equals(id)) {
                    deleteStaff(id);
                    changeChannelStaffState(id);
                    addChannelStaffWithPic(staffName, channelId, phone, pic);
                }
            }
        }

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String addChannelStaffWithPicRes(String staffName, String channelId, String phone, String pic) throws Exception {
        String json = StrSubstitutor.replace(ADD_CHANNEL_STAFF_WITH_PIC_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("channelId", channelId)
                .put("phone", phone)
                .put("faceUrl", pic)
                .build()
        );
        String res = httpPost(ADD_CHANNEL_STAFF, json);

        return res;
    }

    /**
     * 6.7 合作渠道员工修改
     */
    public String editChannelStaffPic(String staffId, String staffName, String channelId, String phone, String faceUrl) throws Exception {
        String json = StrSubstitutor.replace(EDIT_CHANNEL_STAFF_WITH_PIC_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("channelId", channelId)
                .put("phone", phone)
                .put("faceUrl", faceUrl)
                .build()
        );
        String res = httpPost(EDIT_CHANNEL_STAFF + staffId, json);

        JSONObject result = JSON.parseObject(res);
        int codeRes = result.getInteger("code");
        String message = result.getString("message");

        if (codeRes == 1001) {
            if ("当前手机号已被使用".equals(message)) {
                phone = genPhoneNum();
                res = editChannelStaffPic(staffId, staffName, channelId, phone, faceUrl);
            }
        }

        return res;
    }

    public String editChannelStaffPhone(String staffId, String staffName, String channelId, String phone) throws Exception {
        String json = StrSubstitutor.replace(EDIT_CHANNEL_STAFF_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("channelId", channelId)
                .put("phone", phone)
                .build()
        );
        String res = httpPost(EDIT_CHANNEL_STAFF + staffId, json);

        return res;
    }

    /**
     * 6.8 合作渠道员工状态修改
     */
    public void changeChannelStaffState(String staffId) throws Exception {
        String json = "{}";

        httpPostWithCheckCode("/risk/channel/staff/state/change/" + staffId, json);
    }

    public String changeChannelStaffStateRes(String staffId) throws Exception {
        String json = "{}";

        String response = httpPost("/risk/channel/staff/state/change/" + staffId, json);

        return response;
    }

    /**
     * 6.11 渠道订单top3
     */
    public String channelTop() throws Exception {
        String url = "/risk/channel/top";
        String json =
                "{\n" +
                        "    \"shop_id\":\"" + getShopId() + "\"\n" +
                        "}\n";

        String response = httpPost(url, json);

        return response;
    }

    /**
     * 6.13 渠道业务员登陆H5
     */
    public String staffLogInH5(String phone, String password) throws Exception {
        String url = "/external/channel/staff/login";
        String json =
                "{\n" +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"password\":\"" + password + "\"," +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}\n";

        String response = httpPost(url, json);

        return response;
    }

    /**
     * 6.13.1 渠道业务员详情H5
     */
    public String staffDetailH5(String staffId, String token) throws Exception {
        String url = " /external/channel/staff/detail/" + staffId;
        String json =
                "{\n" +
                        "    \"token\":\"" + token + "\"," +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}\n";

        String response = httpPost(url, json);

        return response;
    }

    /**
     * 6.15 渠道客户报备H5
     */
    public String customerReportH5(String staffId, String customerName, String phone, String gender, String token) throws Exception {
        String url = "/external/channel/customer/report";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"id\":\"" + staffId + "\",\n" +
                        "    \"customer_name\":\"" + customerName + "\",\n" +
                        "    \"customer_phone\":\"" + phone + "\",\n" +
                        "    \"gender\":\"" + gender + "\",\n" +
                        "    \"token\":\"" + token + "\"\n" +
                        "}\n";

        String response = httpPostWithCheckCode(url, json);

        return response;
    }

    public String customerReportH5NoCode(String staffId, String customerName, String phone, String gender, String token) throws Exception {
        String url = "/external/channel/customer/report";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"id\":\"" + staffId + "\",\n" +
                        "    \"customer_name\":\"" + customerName + "\",\n" +
                        "    \"customer_phone\":\"" + phone + "\",\n" +
                        "    \"gender\":\"" + gender + "\",\n" +
                        "    \"token\":\"" + token + "\"\n" +
                        "}\n";

        String response = httpPost(url, json);

        return response;
    }

    /**
     * 6.16 渠道业务员登出H5
     */
    public String staffLogoutH5(String staffId, String customerName, String phone, String gender, String token) throws Exception {
        String url = "/external/channel/staff/loginout";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"token\":\"" + token + "\"\n" +
                        "}\n";

        String response = httpPost(url, json);

        return response;
    }

    /**
     * 6.17 渠道业务员注册H5
     */
    public String staffLogoutH5(String name, String phone, String password, String channelId) throws Exception {
        String url = "/external/channel/staff/register";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"name\":\"" + name + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"password\":\"" + password + "\"," +
                        "    \"channel_id\":\"" + channelId + "\"\n" +
                        "}\n";

        String response = httpPost(url, json);

        return response;
    }

    /**
     * 6.19 获取客户报备列表H5
     */
    public String channelCustomerListH5(String token,int page,int pageSize) throws Exception {
        String url = "/external/channel/customer/edit";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"token\":\"" + token + "\"," +
                        "    \"page\":\"" + page + "\"," +
                        "    \"page_size\":\"" + pageSize + "\"" +
                        "}\n";

        String response = httpPost(url, json);

        return response;
    }

    /**
     * 6.20 修改客户报备信息H5
     */
    public String editChannelCustomerH5(String cid, String phone, String customerName, String token) throws Exception {
        String url = "/external/channel/customer/edit";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"cid\":\"" + cid + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"token\":\"" + token + "\"\n" +
                        "}\n";

        String response = httpPost(url, json);

        return response;
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

    public JSONArray staffListWithType(String staffType, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(STAFF_LIST_WITH_TYPE_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffType", staffType)
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );

        String res = httpPostWithCheckCode(STAFF_LIST, json);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    /**
     * 8.2 顾问top6
     */
    public JSONArray staffTop() throws Exception {
        String url = "/risk/staff/top/";
        String json =
                "{\n" +
                        "    \"shop_id\":\"" + getShopId() + "\"\n" +
                        "}\n";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    /**
     * 8.3 员工新增
     */
    public JSONObject addStaff(String staffName, String staffType, String phone, String faceUrl) throws Exception {
        String json = StrSubstitutor.replace(ADD_STAFF_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("staffType", staffType)
                .put("phone", phone)
                .put("faceUrl", faceUrl)
                .build()
        );
        String res = httpPost(ADD_STAFF, json);

        JSONObject result = JSON.parseObject(res);
        int codeRes = result.getInteger("code");
        String message = result.getString("message");
        if (codeRes == 1001) {
            if ("当前手机号已被使用".equals(message)) {
                phone = genPhoneNum();
                addStaff(staffName, staffType, phone, faceUrl);
            } else {
                String id = getIdOfStaff(res);

                if (!"".equals(id)) {
                    deleteStaff(id);
                    addStaff(staffName, staffType, phone, faceUrl);
                }
            }
        }

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String addStaffRes(String staffName, String staffType, String phone, String faceUrl) throws Exception {
        String json = StrSubstitutor.replace(ADD_STAFF_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("staffType", staffType)
                .put("phone", phone)
                .put("faceUrl", faceUrl)
                .build()
        );
        String res = httpPost(ADD_STAFF, json);

        return res;
    }

    /**
     * 8.4 员工删除
     */
    public void deleteStaff(String staffId) throws Exception {
        String json = "{}";

        httpPostWithCheckCode(DELETE_STAFF + staffId, json);
    }

    /**
     * 8.5 员工编辑
     */
    public String editStaffRes(String id, String staffName, String staffType, String phone, String faceUrl) throws Exception {
        String json = StrSubstitutor.replace(EDIT_STAFF_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("staffType", staffType)
                .put("phone", phone)
                .put("faceUrl", faceUrl)
                .build()
        );
        String res = httpPost(EDIT_STAFF + id, json);

        return res;
    }

    /**
     * 9.6 自主注册
     */
    public JSONObject selfRegister(String customerName, String gender, String phone, String verifyCode, String hotPoints, String adviserId) throws Exception {
        String url = "/risk/staff/consultant/list";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"verify_code\":\"" + verifyCode + "\"," +
                        "    \"hot_points\":\"" + hotPoints + "\"," +
                        "    \"adviser_id\":\"" + adviserId + "\"" +
                        "}";

        String res = httpPost(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 8.11 置业顾问列表
     */
    public String consultantList() throws Exception {
        String url = "/risk/staff/consultant/list";

        String json =
                "{\n" +
                        "    \"shop_id\":\"" + getShopId() + "\"\n" +
                        "}\n";

        String res = httpPost(url, json);

        return res;
    }

    /**
     * 10.1 人证对比机数据上传接口
     */
    public void witnessUploadGate(String cardId, String personName, String isPass, String cardPic, String capturePic, String[] resource) throws Exception {
        String router = "/business/risk/WITNESS_UPLOAD/v1.0";
        String json =
                "{\n" +
                        "    \"card_id\":\"" + cardId + "\",\n" +
                        "    \"person_name\":\"" + personName + "\",\n" +
                        "    \"is_pass\":\"" + isPass + "\",\n" +
                        "    \"card_pic\":\"" + cardPic + "\",\n" +
                        "    \"capture_pic\":\"" + capturePic + "\"" +
                        "}\n";

        sendRequestWithGate(router,resource,json);
    }

    public void witnessUpload(String cardId, String personName, String isPass, String cardPic, String capturePic) throws Exception {
        String router = "/risk-inner/witness/upload";
        String json =
                "{\n" +
                        "    \"data\":{\n" +
                        "        \"person_name\":\"" + personName + "\",\n" +
                        "        \"capture_pic\":\"@1\",\n" +
                        "        \"is_pass\":true,\n" +
                        "        \"card_pic\":\"@0\",\n" +
                        "        \"card_id\":\"" + cardId + "\"\n" +
                        "    },\n" +
                        "    \"request_id\":\"1c32c393-21c2-48b2-afeb-11c197436194\",\n" +
                        "    \"resource\":[\n" +
                        "        \"http://retail-huabei2.oss-cn-beijing-internal.aliyuncs.com/dispatcher_daily/uid_ef6d2de5/49998b971ea0/54/20200109/1c32c393-21c2-48b2-afeb-11c197436194_0?Expires=1578639064&OSSAccessKeyId=LTAIlYpjA39n18Yr&Signature=10YN4vOhGdUaR5hsRrvNCB7Uxs4%3D\",\n" +
                        "        \"http://retail-huabei2.oss-cn-beijing-internal.aliyuncs.com/dispatcher_daily/uid_ef6d2de5/49998b971ea0/54/20200109/1c32c393-21c2-48b2-afeb-11c197436194_1?Expires=1578639064&OSSAccessKeyId=LTAIlYpjA39n18Yr&Signature=4VEKcHIuHxGEdAJG5Ze3sLktCLM%3D\"\n" +
                        "    ],\n" +
                        "    \"system\":{\n" +
                        "        \"app_id\":\"49998b971ea0\",\n" +
                        "        \"device_id\":\"6934268400763904\",\n" +
                        "        \"scope\":[\n" +
                        "            \"4116\"\n" +
                        "        ],\n" +
                        "        \"service\":\"/business/risk/WITNESS_UPLOAD/v1.0\",\n" +
                        "        \"source\":\"DEVICE\"\n" +
                        "    }\n" +
                        "}";

        httpPostWithCheckCode(router, json);
    }

    /**
     * 10.2 人证对比机数据上传接口
     */
    public void originalPicUpload() throws Exception {
        String url = "/business/risk/WITNESS_UPLOAD/v1.0";
        String json =
                "{\n" +
                        "    \"bucket\":\"oss的bucket\",\n" +
                        "    \"algo_request_id\":\"调用算法的request_id\",\n" +
                        "    \"file_path\":\"/xxxx/xxx/xxx.jpg\"\n" +
                        "}\n";

        httpPostWithCheckCode(url, json);
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
     * 11.2 自主报备页面门店信息
     */
    public JSONObject selfRegisterShopDetail() throws Exception {

        String requestUrl = "/external/self-register/shop/detail/" + getShopId();

        String json = "{\"shop_id\":" + getShopId() + "}";

        String res = httpGet(requestUrl, json);
        JSONObject data = JSON.parseObject(res).getJSONObject("data");

        return data;
    }

//    -----------------------------------------------测试case--------------------------------------------------------------

    @Test
    public void witnessUploadChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "认证过对比机数据上传>>>";

        try {

            String resource[] = new String[0];

            String cardId = "111222333444555001";
            String personName = "test";
            String isPass = "true";
            String cardPic = "";
            String capturePic = "http";

            witnessUpload(cardId,personName,isPass,cardPic,capturePic);

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

//    -------------------------------------------------数据一致性验证-------------------------------------------------------------


    @Test
    public void channelEqualsStaffReport() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "渠道总报备数==该渠道每个业务员的报备数>>>";

        try {

            JSONArray list = channelList(1, pageSize).getJSONArray("list");

            checkChannelEqualsStaff(list);

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
    public void reportNumPCEqualsH5() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "H5页面报备总数与PC页面内报备总数一致>>>";

        try {

            int reportNum;
            int reportNum1 = 0;

            String staffId = "";
            String staffPhone = "";
            String token = "";
            String detailH5 = staffDetailH5(staffId, token);
            JSONObject data = JSON.parseObject(detailH5).getJSONObject("data");
            reportNum = data.getInteger("report_num");
            String id = data.getString("id");

            String channelId = "";
            JSONArray staffList = channelStaffList(channelId, 1, pageSize);
            for (int i = 0; i < staffList.size(); i++) {
                JSONObject single = staffList.getJSONObject(i);
                if (id.equals(single.get("id"))){
                    reportNum1 = single.getInteger("report_num");
                    break;
                }
            }

            if (reportNum!=reportNum1){
                throw new Exception("业务员手机号:" + staffPhone + "H5页面内的报备数=" + reportNum + ",PC端的报备数=" + reportNum1);
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
    public void reportInfoEquals() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "H5页面业务员报备总数与H5页面内业务员报备条数一致>>>";

        try {

//            H5页面内报备数
            String staffPhone = "";
            String staffId = "";
            String token = "";
            String staffDetailH5 = staffDetailH5(staffId,token);

            int reportNumH5 = JSON.parseObject(staffDetailH5).getJSONObject("data").getInteger("report_num");

            String customerListH5 = channelCustomerListH5(token,1,pageSize);

            int reportNumListNum = JSON.parseObject(customerListH5).getJSONObject("data").getJSONArray("list").size();

            if (reportNumH5 != reportNumListNum){
                throw new Exception("业务员手机号:" + staffPhone + "H5页面内的报备总数=" + reportNumH5 + ",H5页面内的报备条数=" + reportNumListNum);
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
    public void OrderListLinkEquals() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "证据页事项与风控列表中展示的信息一致>>>";

        try {

            JSONArray list = orderList(1, pageSize).getJSONArray("list");

//            checkOrderListEqualsLinkList();

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

    private void checkOrderListEqualsLinkList(JSONArray list) throws Exception {

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String orderId = single.getString("order_id");
            String advisreName = single.getString("adviser_name");
            String channelName = single.getString("channel_name");

            JSONArray orderLinkList = orderLinkList(orderId).getJSONArray("list");
            for (int r = orderLinkList.size() - 1; r >= 0; r--) {
                JSONObject link = orderLinkList.getJSONObject(r);
                String linkPoint = link.getString("link_point");

                if (linkPoint.contains("置业顾问")){
                    link.getJSONObject("link_note").getString("");

                }

            }
        }
    }


    @Test
    public void customerTypeListNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "顾客身份列表>>>校验key非空-";

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
    public void ageGroupListNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "顾客年龄分组>>>校验key非空-";

        String key = "";

        try {
            JSONObject data = ageGroupList();
            for (Object obj : ageGroupListNotNull()) {
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

        String function = "到访时间枚举列表>>>校验key非空-";

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

    @Test
    public void customerListNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "顾客列表>>>校验key非空-";

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
    public void customerSimpleInfoNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "顾客简要信息>>>校验key非空-";

        String key = "";

        try {
            String phone = "";
            JSONObject data = customerSimpleInfo(phone);
            for (Object obj : customerSimpleInfoNotNull()) {
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

        String function = "订单列表>>>校验key非空-";

        String key = "";

        try {
            JSONObject data = orderList(3, pageSize);
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
    public void channelListNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "渠道列表>>>校验key非空-";

        String key = "";

        try {
            JSONObject data = channelList(1, pageSize);
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
    public void adviserListNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "置业顾问列表>>>校验key非空-";

        String key = "";

        try {
            JSONObject data = channelList(1, pageSize);
            for (Object obj : adviserListNotNull()) {
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
    public void searchReportInfoByPhoneChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "根据手机号查找报备信息>>>校验key非空-";

        String key = "";

        try {
            String phone = "";
            JSONObject data = searchReportInfoByPhone(phone);
            for (Object obj : reportInfoNotNull()) {
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
    public void orderDetailNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "订单详情>>>校验key非空-";

        String key = "";

        try {
            String orderId = "";
            JSONObject data = orderDetail(orderId);
            for (Object obj : orderDetailNotNull()) {
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
    public void linkNoteNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "订单关键步骤>>>校验key非空-";

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

    @Test
    public void orderStatusNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "订单状态列表>>>校验key非空-";

        String key = "";

        try {
            String orderId = "";
            JSONObject data = orderstatusList();
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

    @Test
    public void channelDetailNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "渠道详情>>>校验key非空-";

        String key = "";

        try {
            String orderId = "";
            JSONObject data = orderLinkList(orderId);
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

    @Test
    public void report() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "报备-刷证";

        String key = "";

        try {

            String staffPhone = "";
            String password = "";

            staffLogInH5(staffPhone, password);

//            1、报备

            String staffId = "";
            String customerName = "";
            String customerPhone = "";
            String gender = "";
            String token = "";
            customerReportH5(staffId, customerName, customerPhone, gender, token);

//            2、刷证

            String cardId = "";
            String personName = "";
            String isPass = "";
            String cardPic = "";
            String capturePic = "";

//            witnessUploadGate(cardId, personName, isPass, cardPic, capturePic);

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
     * 无渠道
     */
    @Test
    public void noChannelDeal() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;


        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            createOrder(phone, orderId, channelId, smsCode);

            String visitor = "";
            orderstatusAudit(orderId, visitor);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * 同一渠道，业务员a H5报备->顾客到场->业务员b H5报备，
     * 顾客选业务员a
     */
    @Test
    public void order1() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单

            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            result = orderDetail(orderId);

            checkOrder(result, 1, false);

            // 查询订单
            result = orderDetail(orderId);

            String visitor = "";
            orderstatusAudit(orderId, visitor);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * 同一渠道，业务员a H5报备->顾客到场->业务员b H5报备，
     * 顾客选业务员a
     */
    @Test
    public void order2() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 1, false);


            // 查询订单
            result = orderDetail(orderId);

            String visitor = "";
            orderstatusAudit(orderId, visitor);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "报备-到场-成交，订单状态：正常 ，核验状态：未核验");
        }
    }

    /**
     * H5->顾客到场->PC端，顾客选H5
     */
    @Test
    public void order3() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 3, false);

            // 审核订单

            // 查询订单
            result = orderDetail(orderId);

            String visitor = "";
            orderstatusAudit(orderId, visitor);

            //校验环节异常

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "创单报备，现场报备-成交，订单状态：风险");
        }
    }

    /**
     * H5->顾客到场->顾客自助扫码，顾客选H5
     */
    @Test
    public void order4() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 3, false);

            // 审核订单

            // 查询订单

            String visitor = "";
            orderstatusAudit(orderId, visitor);

            //校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "报备-到场-修改报备手机号-创单，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * PC端->顾客到场->顾客自助扫码，顾客选PC端
     */
    @Test
    public void order5() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 3, false);

            // 审核订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

            // 查询订单
            result = orderDetail(orderId);

            checkOrder(result, 1, true);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "顾客到场-H5报备-成交 ，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * H5->顾客到场->PC端->顾客自助扫码，顾客选H5
     */
    @Test
    public void order6() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            result = orderDetail(orderId);

            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验环节异常

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "未到场-自然成交，订单状态：正常");
        }
    }

    /**
     * H5->PC端->顾客到场->顾客自助扫码，顾客选H5
     */
    @Test
    public void order7() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "未到场-报备-成交，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * H5->PC端->顾客到场->顾客自助扫码，顾客选PC端
     */
    @Test
    public void order8() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "未到场-报备-成交，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * H5->顾客到场
     */
    @Test
    public void orderNatual1() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "未到场-报备-成交，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * PC端->顾客到场
     */
    @Test
    public void orderNatual2() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "未到场-报备-成交，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * 顾客自助扫码->顾客到场
     */
    @Test
    public void orderNatual3() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "未到场-报备-成交，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * H5+PC端->顾客到场
     */
    @Test
    public void orderNatual4() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "未到场-报备-成交，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * H5+顾客自助扫码->顾客到场
     */
    @Test
    public void orderNatual5() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "未到场-报备-成交，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * PC端+顾客自助扫码->顾客到场
     */
    @Test
    public void orderNatual6() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * H5+PC端+顾客自助扫码->顾客到场
     */
    @Test
    public void orderNatual7() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "未到场-报备-成交，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * PC端->顾客到场
     */
    @Test
    public void noChannel1() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "未到场-报备-成交，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * 顾客自助扫码->顾客到场
     */
    @Test
    public void noChannel2() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "未到场-报备-成交，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * PC端+顾客自助扫码->顾客到场
     */
    @Test
    public void noChannel3() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "未到场-报备-成交，订单状态：风险 ，核验状态：未核验");
        }
    }

    /**
     * 修改顾问1-2次
     */
    @Test
    public void noChannel4() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * 顾客到场->H5
     */
    @Test
    public void abnormal1() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * 顾客到场->PC端
     */
    @Test
    public void abnormal2() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }


    /**
     * 顾客到场->顾客自助扫码
     */
    @Test
    public void abnormal3() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * 顾客到场->H5+PC端
     */
    @Test
    public void abnormal4() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * 顾客到场->H5+顾客自助扫码
     */
    @Test
    public void abnormal5() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * 顾客到场->PC端+顾客自助扫码
     */
    @Test
    public void abnormal6() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * 顾客到场->H5+PC端+顾客自助扫码
     */
    @Test
    public void abnormal7() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * 业务员a H5报备->顾客到场->业务员b H5报备，顾客选业务员b,同一渠道
     */
    @Test
    public void abnormalSubject1() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * 业务员a H5报备->顾客到场->业务员b H5报备，顾客选业务员b,不同渠道
     */
    @Test
    public void abnormalSubject2() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * H5->顾客到场->PC端，顾客选PC端
     */
    @Test
    public void abnormalSubject3() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * H5->顾客到场->顾客自助扫码，顾客选顾客自
     * 助扫码（无渠道）
     */
    @Test
    public void abnormalSubject4() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * PC端->顾客到场->顾客自助扫码，顾客选顾客
     * 自助扫码（无渠道）
     */
    @Test
    public void abnormalSubject5() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * H5->顾客到场->PC端->顾客自助扫码，顾客选
     * PC端
     */
    @Test
    public void abnormalSubject6() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * H5->顾客到场->PC端->顾客自助扫码，顾客选
     * 顾客自助扫码（无渠道）
     */
    @Test
    public void abnormalSubject7() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * H5->PC端->顾客到场->顾客自助扫码，顾客选
     * 顾客自助扫码（无渠道）
     */
    @Test
    public void abnormalSubject8() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * 单一渠道报备，隐藏手机号
     */
    @Test
    public void abnormalSubjectB1() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * 多渠道报备，隐藏手机号
     */
    @Test
    public void abnormalSubjectB2() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    /**
     * 修改手机号1次
     */
    @Test
    public void abnormalSubjectB3() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            String visitor = "";
            orderstatusAudit(orderId, visitor);

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "");
        }
    }

    @Test
    public void diffGender() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 校验订单的风险状态
            String visitor = "";
            orderstatusAudit(orderId, visitor);

            //校验顾客性别冲突时，环节异常

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "报备时性别和身份证性别不一致！");
        }
    }

    @Test
    public void sameGender() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 校验订单的风险状态
            String visitor = "";
            orderstatusAudit(orderId, visitor);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "报备时性别和身份证性别一致！");
        }
    }

    /**
     * 订单详情与订单列表中信息是否一致
     **/
    @Test
    public void dealListEqualsDetail() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 订单列表
            JSONArray list = orderList(1, pageSize).getJSONArray("list");
            for (int i = 0; i < list.size() && i <= 20; i++) {
                JSONObject single = list.getJSONObject(i);
                String orderId = getValue(single, "order_id");
                String customerName = getValue(single, "customer_name");
                String adviserName = getValue(single, "adviser_name");
                String channelName = getValue(single, "channel_name");
                String firstAppearTime = getValue(single, "first_appear_time");
                String reportTime = getValue(single, "report_time");
                String signTime = getValue(single, "sign_time");
                String dealTime = getValue(single, "deal_time");
                String status = getValue(single, "status");
                String isAudited = getValue(single, "is_audited");

                if ("3".equals(orderId)) {

                    JSONObject data = orderDetail(orderId);
                    compareValue(data, "订单", orderId, "customer_name", customerName, "顾客姓名");
                    compareValue(data, "订单", orderId, "adviser_name", adviserName, "置业顾问");
                    compareValue(data, "订单", orderId, "channel_name", channelName, "渠道名称");
                    compareValue(data, "订单", orderId, "order_status", status, "订单状态");
                    compareValue(data, "订单", orderId, "is_audited", isAudited, "是否审核");

                    compareOrderTimeValue(data, "first_appear_time", orderId, firstAppearTime, "订单列表", "订单详情");
                    compareOrderTimeValue(data, "report_time", orderId, reportTime, "订单列表", "订单详情");
                    compareOrderTimeValue(data, "sign_time", orderId, signTime, "订单列表", "订单详情");
                    compareOrderTimeValue(data, "deal_time", orderId, dealTime, "订单列表", "订单详情");
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "订单详情与订单列表中信息是否一致");
        }
    }

    /**
     * 新建报备后，业务员累计报备数是否增加
     **/
    @Test
    public void channelStaffReportNum() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            //取出渠道员工宫二的报备数
            int channelStaffTotalReportBefore =
                    getChannelStaffReportNum(channelStaffList(channelId, 1, 10000));//"5"是测试【勿动】

//            新建报备
            String phoneNum = genPhoneNum();

            newCustomer(channelId,  //测试【勿动】
                    gongErId,  //宫二
                    anShengId,  //"矮大紧"
                    phoneNum,
                    "测试数量",
                    genderFemale
            );

            //取出渠道员工宫二的报备数
            int channelStaffTotalReportAfter =
                    getChannelStaffReportNum(channelStaffList("5", 1, 10000));//"5"是测试【勿动】

            //比较报备前后渠道员工的报备数
            if (channelStaffTotalReportAfter - 1 != channelStaffTotalReportBefore) {
                throw new Exception("新建报备后渠道员工--宫二的累计报备数没有加1。报备前：" +
                        channelStaffTotalReportBefore + ",报备后：" + channelStaffTotalReportAfter + "。顾客手机号:" + phoneNum);
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "新建报备后，业务员累计报备数是否增加");
        }
    }

    /**
     * 渠道的累计报备数==各个业务员的累计报备数之和
     **/
    @Test
    public void channelTotalEqualsStaffTotal() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            JSONArray channelList = channelList(1, pageSize).getJSONArray("list");

            for (int i = 0; i < channelList.size(); i++) {
                JSONObject singleChannel = channelList.getJSONObject(i);
                int channelNum = singleChannel.getInteger("total_customers");
                String channelId = singleChannel.getString("channel_id");
                if ("1".equals(channelId)) {
                    channelNum -= 4;
                }
                String channelName = singleChannel.getString("channel_name");

                JSONArray staffList = channelStaffList(channelId, 1, pageSize);
                int staffNum = 0;
                for (int j = 0; j < staffList.size(); j++) {
                    JSONObject singleStaff = staffList.getJSONObject(j);
                    staffNum += singleStaff.getInteger("total_report");
                }

                if (staffNum != channelNum) {
                    throw new Exception("渠道【" + channelName + "】,渠道累计报备数：" + channelNum + "，业务员累计报备数之和：" + staffNum);
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "渠道的累计报备数==各个业务员的累计报备数之和");
        }
    }

    /**
     * 顾客查询中的签约顾客数==渠道中的签约顾客数
     **/
    @Test
    public void customerOrderEqualschannelOrder() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            //查询渠道列表，获取channel_id
            JSONArray channelList = channelList(1, pageSize).getJSONArray("list");

            for (int i = 0; i < channelList.size(); i++) {
                JSONObject singleChannel = channelList.getJSONObject(i);
                String channelId = singleChannel.getString("channel_id");
                String channelName = singleChannel.getString("channel_name");

                int customerListSize = customerListWithChannel("CHECKED", channelId, 1, pageSize).size();

                JSONArray orderList = orderListWithChannel(channelId, 1, pageSize);
                HashMap<String, Integer> hm = new HashMap<>();
                int channelListSize = 0;
                for (int j = 0; j < orderList.size(); j++) {
                    JSONObject singleOrder = orderList.getJSONObject(j);
                    String customerPhone = singleOrder.getString("customer_phone");
                    if (!hm.containsKey(customerPhone)) {
                        hm.put(customerPhone, 1);
                        channelListSize++;
                    }
                }

                if (customerListSize != channelListSize) {
                    throw new Exception("渠道：" + channelName + ",顾客列表中的订单数：" + customerListSize + ", 渠道详情中的订单数：" + channelListSize);
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "顾客查询中的签约顾客数==渠道中的签约顾客数");
        }
    }

    /**
     * 渠道中的报备顾客数 >= 0
     **/
    @Test
    public void channelReportCustomerNum() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            //查询渠道列表，获取channel_id
            JSONArray channelList = channelList(1, pageSize).getJSONArray("list");

            for (int i = 0; i < channelList.size(); i++) {
                JSONObject singleChannel = channelList.getJSONObject(i);
                String channelName = singleChannel.getString("channel_name");
                Integer channelReportNum = singleChannel.getInteger("total_customers");

                if (null == channelReportNum || channelReportNum < 0) {
                    throw new Exception("渠道【" + channelName + "】, 渠道列表中的报备数：" + channelReportNum);
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "渠道中的报备顾客数 >= 0");
        }
    }

    /**
     * 订单列表中，风险+正常的订单数==订单列表总数
     **/
    @Test
    public void orderListDiffType() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            JSONArray totalList = orderList(-1, pageSize).getJSONArray("list");
            int totalNum = totalList.size();

//            获取正常订单数
            JSONArray normalList = orderList(1, pageSize).getJSONArray("list");//1是正常，3是风险
            int normalNum = normalList.size();

//            获取风险订单数
            JSONArray riskList = orderList(3, pageSize).getJSONArray("list");//1是正常，3是风险
            int riskNum = riskList.size();

            if (normalNum + riskNum != totalNum) {
                throw new Exception("订单列表总数：" + totalNum + ",正常订单数：" + normalNum + ",风险订单数：" + riskNum);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "订单列表中，风险+正常的订单数==订单列表总数");
        }
    }

    /**
     * 员工管理中，各类型员工数量统计是否正确
     **/
    @Test
    public void staffTypeNum() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

//            1、获取员工类型
            JSONArray staffTypeList = staffTypeList();

            HashMap<String, String> staffTypes = new HashMap<>();

            for (int i = 0; i < staffTypeList.size(); i++) {

                JSONObject singleType = staffTypeList.getJSONObject(i);

                staffTypes.put(singleType.getString("staff_type"), singleType.getString("type_name"));
            }

//            2、查询员工总体中各类型的员工数
            JSONArray totalList = staffList(1, pageSize);

            HashMap<String, Integer> staffNumHm = new HashMap<>();

            for (String key : staffTypes.keySet()) {
                staffNumHm.put(key, 0);
            }

            for (int j = 0; j < totalList.size(); j++) {
                String staffType = totalList.getJSONObject(j).getString("staff_type");
                staffNumHm.put(staffType, staffNumHm.get(staffType) + 1);
            }

//            3、查询各个类型的员工列表
            for (Map.Entry<String, String> entry : staffTypes.entrySet()) {
                String staffType = entry.getKey();

                JSONArray array = staffListWithType(staffType, 1, pageSize);
                int size = 0;
                if (array != null) {
                    size = array.size();
                }

                if (size != staffNumHm.get(staffType)) {
                    throw new Exception("不选员工类型时，列表返回结果中【" + staffTypes.get(staffType) + "】的数量为：" + staffNumHm.get(staffType) +
                            ", 选择类型查询时，查询结果中该类型员工数为：" + array.size());
                }
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "员工管理中，各类型员工数量统计是否正确");
        }
    }

    /**
     * 员工列表每页显示核查
     **/
    @Test
    public void addStaffTestPage() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages";

            int pageSizeTemp = 10;

            File file = new File(dirPath);
            File[] files = file.listFiles();

            ArrayList<String> phones = new ArrayList<>();

            for (int i = 0; i < files.length; i++) {

                String imagePath = dirPath + "/" + files[i].getName();

                imagePath = imagePath.replace("/", File.separator);

                JSONObject uploadImage = uploadImage(imagePath);

                String phoneNum = genPhoneNum();

                phones.add(phoneNum);

                addStaff("staff-" + i, getOneStaffType(), phoneNum, uploadImage.getString("face_url"));

                int totalPage = getTotalPage(staffListReturnData(1, pageSizeTemp));

                for (int j = 1; j <= totalPage; j++) {
                    JSONArray staffList = staffList(j, pageSizeTemp);

                    if (j < totalPage) {
                        if (staffList.size() != 10) {
                            throw new Exception("员工列表，第【" + j + "】页不是最后一页，仅有【" + staffList.size() + "】条记录。");
                        }
                    } else {
                        if (staffList.size() == 0) {
                            throw new Exception("员工列表，第【" + j + "】页显示为空");
                        }
                    }
                }
            }

            JSONArray staffList = staffList(1, pageSize);
            ArrayList<String> ids = getIdsByPhones(staffList, phones);
            for (int i = 0; i < ids.size(); i++) {
                deleteStaff(ids.get(i));
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "员工列表每页显示核查");
        }
    }

    /**
     * 渠道业务员列表每页显示核查
     **/
    @Test
    public void addChannelStaffTestPage() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String channelId = "5";
            int pageSizeTemp = 10;

            ArrayList<String> phones = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                String phoneNum = genPhoneNum();
                addChannelStaff("change-test-" + i, channelId, phoneNum);
                JSONObject temp = channelStaffListReturnData(channelId, 1, pageSizeTemp);

                int totalPage = getTotalPage(temp);
                for (int j = 1; j <= totalPage; j++) {
                    JSONArray singlePage = channelStaffList(channelId, j, pageSizeTemp);

                    if (j < totalPage) {
                        if (singlePage.size() != 10) {
                            throw new Exception("渠道业务员列表，第【" + j + "】页不是最后一页，仅有【" + singlePage.size() + "】条记录。");
                        }
                    } else {
                        if (singlePage.size() == 0) {
                            throw new Exception("渠道业务员列表，第【" + j + "】页显示为空");
                        }
                    }
                }

                phones.add(phoneNum);
            }

            JSONArray staffList = channelStaffList(channelId, 1, pageSize);
            ArrayList<String> ids = getIdsByPhones(staffList, phones);

            for (int i = 0; i < ids.size(); i++) {
                changeChannelStaffState(ids.get(i));
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "渠道业务员列表每页显示核查");
        }
    }

    /**
     * 人脸注册渠道员工，期望成功
     **/
    @Test
    public void addChannelStaffWithPicCheck() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages";

            String channelId = "5";

            File file = new File(dirPath);
            File[] files = file.listFiles();

            ArrayList<String> phones = new ArrayList<>();

//            只注册一张，用于测试用人脸注册渠道员工是否成功！
            for (int i = 0; i <= 1; i++) {

                String imagePath = dirPath + "/" + files[i].getName();

                imagePath = imagePath.replace("/", File.separator);

                JSONObject uploadImage = uploadImage(imagePath);

                String phoneNum = genPhoneNum();

                phones.add(phoneNum);

                addChannelStaffWithPic("staff-" + i, channelId, phoneNum, uploadImage.getString("face_url"));
            }

            JSONArray staffList = channelStaffList(channelId, 1, pageSize);
            ArrayList<String> ids = getIdsByPhones(staffList, phones);
            if (ids.size() == 0) {
                throw new Exception("用人脸注册渠道员工失败！");
            }
            for (int i = 0; i < ids.size(); i++) {
                changeChannelStaffState(ids.get(i));
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "人脸注册渠道员工，期望成功");
        }
    }

    /**
     * 渠道列表每页显示是否正常
     **/
    @Test
    public void addChannelTestPage() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            int pageSizeTemp = 10;

            for (int i = 0; i < 10; i++) {
                String phoneNum = genPhoneNum();
                addChannel("channel-" + System.currentTimeMillis(), "于老师", phoneNum, "test");
                JSONObject temp = channelList(1, pageSizeTemp);

                int totalPage = getTotalPage(temp);
                for (int j = 1; j <= totalPage; j++) {
                    JSONArray singlePage = channelList(j, pageSizeTemp).getJSONArray("list");

                    if (j < totalPage) {
                        if (singlePage.size() != 10) {
                            throw new Exception("渠道列表，第【" + j + "】页不是最后一页，仅有【" + singlePage.size() + "】条记录。");
                        }
                    } else {
                        if (singlePage.size() == 0) {
                            throw new Exception("渠道列表，第【" + j + "】页显示为空");
                        }
                    }
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "渠道列表每页显示是否正常");
        }
    }

    /**
     * 机会顾客列表每页显示是否正常
     **/
    @Test
    public void newCustomerTestPage() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            int pageSizeTemp = 10;
            String serachType = "CHANCE";

            for (int i = 0; i < 10; i++) {
                String phoneNum = genPhoneNum();
                newCustomer(channelId, gongErId, anShengId, phoneNum, "customer-testpage", genderMale);
                JSONObject temp = customerList("", "");

                int totalPage = getCustomerTotalPage(temp);
                for (int j = 1; j <= totalPage; j++) {
                    JSONArray singlePage = customerList("", "").getJSONArray("list");
                    if (j < totalPage) {
                        if (singlePage.size() != 10) {
                            throw new Exception("机会顾客列表，第【" + j + "】页不是最后一页，仅有【" + singlePage.size() + "】条记录。");
                        }
                    } else {
                        if (singlePage.size() == 0) {
                            throw new Exception("机会顾客列表，第【" + j + "】页显示为空");
                        }
                    }
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "机会顾客列表每页显示是否正常");
        }
    }

    /**
     * 业务员处于启用状态，不能新建一个与此业务员相似人脸的业务员
     **/
    @Test
    public void initThenRegChannelStaffSamePic() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//            新建一个相同人脸的业务员
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");

            String response = addChannelStaffWithPicRes("change-state-test", channelId, genPhoneNum(), faceUrl);
            checkCode(response, StatusCode.BAD_REQUEST, "添加业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "业务员处于启用状态，不能新建一个与此业务员相似人脸的业务员");
        }
    }

    /**
     * 业务员处于启用状态，不能新建一个与此业务员相似人脸的售楼处员工
     **/
    @Test
    public void initThenRegStaffSamePic() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//            新建一个相同人脸的业务员
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");

            String response = addStaffRes("change-state-test", getOneStaffType(), genPhoneNum(), faceUrl);
            checkCode(response, StatusCode.BAD_REQUEST, "添加业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "业务员处于启用状态，不能新建一个与此业务员相似人脸的售楼处员工");
        }
    }

    /**
     * 业务员处于启用状态，不能新建一个与此业务员相同手机号的业务员
     **/
    @Test
    public void initThenRegChannelStaffSamePhone() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String channelId = "5";
            String namePhone = "宫二";
            String phone = "17610248107";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//            新建一个相同手机号的业务员
            String response = addChannelStaffRes(caseName, channelId, phone);
            checkCode(response, StatusCode.BAD_REQUEST, "添加业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "业务员处于启用状态，不能新建一个与此业务员相同手机号的业务员");
        }
    }

    /**
     * 业务员处于启用状态，不能新建一个与此业务员相同手机号的售楼处员工
     **/
    @Test
    public void initThenRegStaffSamePhone() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String channelId = "5";
            String namePhone = "宫二";

            String phone = "17610248107";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//            新建一个相同手机号的售楼处员工
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");

            String response = addStaffRes(caseName, channelId, phone, faceUrl);
            checkCode(response, StatusCode.BAD_REQUEST, "添加售楼处员工");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "业务员处于启用状态，不能新建一个与此业务员相同手机号的售楼处员工");
        }
    }

    /**
     * 业务员处于启用状态，不能编辑另一业务员为相似人脸
     **/
    @Test
    public void initThenEditChannelStaffSamePic() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//           编辑一个业务员，用宫二的图片
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");


//            这是一个已经建好的业务员，phone：17771434896，id：733
            String id = "733";
            String phone = "17771434896";
            String name = "测试【勿动】";
            String response = editChannelStaffPic(id, name, channelId, phone, faceUrl);

            checkCode(response, StatusCode.BAD_REQUEST, "编辑业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "业务员处于启用状态，不能编辑另一业务员为相似人脸");
        }
    }

    /**
     * 业务员处于启用状态，不能编辑另一售楼处员工为相似人脸
     **/
    @Test
    public void initThenEditStaffSamePic() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/changestate.jpg";

            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//           编辑一个业务员，用宫二的图片
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");

//            这是一个已经建好的业务员
            String id = "15";
            String phone = "16622222222";
            String name = "安生";
            String staffType = "PROPERTY_CONSULTANT";
            String response = editStaffRes(id, name, staffType, phone, faceUrl);

            checkCode(response, StatusCode.BAD_REQUEST, "编辑售楼处员工");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "业务员处于启用状态，不能编辑另一售楼处员工为相似人脸");
        }
    }

    /**
     * 业务员处于启用状态，不能编辑另一业务员为相同手机号
     **/
    @Test
    public void initThenEditChannelStaffSamePhone() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//           编辑一个业务员，用宫二的手机号

//            这是一个已经建好的业务员
            String id = "733";
            String phone = "17610248107";
            String name = "测试【勿动】";
            String response = editChannelStaffPhone(id, name, channelId, phone);

            checkCode(response, StatusCode.BAD_REQUEST, "编辑业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "业务员处于启用状态，不能编辑另一业务员为相同手机号");
        }
    }

    /**
     * 业务员处于启用状态，不能编辑另一售楼处员工为相同手机号
     **/
    @Test
    public void initThenEditStaffSamePhone() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/ansheng.jpg";

            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//           编辑一个业务员，用宫二的手机号
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");

//            这是一个已经建好的业务员
            String id = "15";
            String phone = "17610248107";
            String name = "安生";
            String staffType = "PROPERTY_CONSULTANT";
            String response = editStaffRes(id, name, staffType, phone, faceUrl);

            checkCode(response, StatusCode.BAD_REQUEST, "编辑售楼处员工");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "业务员处于启用状态，不能编辑另一售楼处员工为相同手机号");
        }
    }

    /**
     * 业务员处于启用状态，不能启动另一有相同人脸图片的业务员
     **/
    @Test
    public void initThenInitChannelStaffSamePhone() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//           编辑一个业务员，用宫二的手机号

//            这是一个已经建好的业务员
            String id = "533";
//            String name = "相同手机号【勿动】";
            String response = changeChannelStaffStateRes(id);

            checkCode(response, StatusCode.BAD_REQUEST, "启动业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "业务员处于启用状态，不能启动另一有相同人脸图片的业务员");
        }
    }

    /**
     * 业务员处于启用状态，不能启动另一有相同手机号的业务员
     **/
    @Test
    public void initThenInitChannelStaffSamePic() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            String channelId = "5";
            String namePhone = "宫二";

//            保证业务员“宫二”处于启用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (isDelete) {
                changeChannelStaffState("12");
            }

//            启动一个已经相同人脸图片的业务员，这是一个已经建好的业务员，phone：17771434896，id：751
            String id = "751";
//            String phone = "17708829844";
//            String name = "change-state-test";
            String response = changeChannelStaffStateRes(id);

            checkCode(response, StatusCode.BAD_REQUEST, "启动业务员");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "业务员处于启用状态，不能启动另一有相同手机号的业务员");
        }
    }

    /**
     * 业务员处于禁用状态，可以新建一个与此业务员相似人脸的业务员
     **/
    @Test
    public void forbidThenRegChannelStaffSamePic() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/makun.jpg";

            String channelId = "5";
            String namePhone = "马锟";
            String id = "47";

//            保证业务员“马锟”处于禁用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (!isDelete) {
                changeChannelStaffState(id);
            }

//            新建一个相同人脸的业务员
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");

            String phone = genPhoneNum();
            String response = addChannelStaffWithPicRes(caseName, channelId, phone, faceUrl);
            checkCode(response, StatusCode.SUCCESS, "添加业务员");

            JSONObject data = channelStaffListWithPhone(channelId, phone, 1, pageSize);

            String idNew = getIdByPhone(data.getJSONArray("list"), phone);

            changeChannelStaffState(idNew);
        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "业务员处于禁用状态，可以新建一个与此业务员相似人脸的业务员");
        }
    }

    /**
     * 业务员处于禁用状态，可以新建一个与此业务员相似人脸的售楼处员工
     **/
    @Test
    public void forbidThenRegStaffSamePic() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/makun.jpg";

            String channelId = "5";
            String namePhone = "马锟";
            String id = "47";

//            保证业务员“马锟”处于禁用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (!isDelete) {
                changeChannelStaffState(id);
            }

//            新建一个相同人脸的售楼处员工
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");
            String phone = genPhoneNum();

            String response = addStaffRes(caseName, getOneStaffType(), phone, faceUrl);
            checkCode(response, StatusCode.SUCCESS, "添加售楼处员工");

            JSONArray staffList = staffList(1, pageSize);

            String idNew = getIdByPhone(staffList, phone);

            deleteStaff(idNew);

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "业务员处于禁用状态，可以新建一个与此业务员相似人脸的售楼处员工");
        }
    }

    /**
     * 业务员处于禁用状态，可以新建一个与此业务员相同手机号的业务员
     **/
    @Test
    public void forbidThenRegChannelStaffSamePhone() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String channelId = "5";
            String namePhone = "马锟";
            String phone = "16567676767";
            String id = "47";

//            保证业务员“马锟”处于禁用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (!isDelete) {
                changeChannelStaffState(id);
            }

//            新建一个相同手机号的业务员
            String response = addChannelStaffRes(caseName, channelId, phone);
            checkCode(response, StatusCode.SUCCESS, "添加业务员");

            JSONObject data = channelStaffListWithPhone(channelId, phone, 1, pageSize);

            String idNew = getIdByPhoneAndStatus(data.getJSONArray("list"), phone, false);

            changeChannelStaffState(idNew);

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "业务员处于禁用状态，可以新建一个与此业务员相同手机号的业务员");
        }
    }

    /**
     * 业务员处于禁用状态，可以新建一个与此业务员相同手机号的售楼处员工
     **/
    @Test
    public void forbidThenRegStaffSamePhone() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanForbid/makun.jpg";

            String channelId = "5";
            String namePhone = "马锟";
            String maphone = "16567676767";
            String id = "47";

//            保证业务员“马锟”处于禁用状态
            JSONObject staffListWithPhone = channelStaffListWithPhone(channelId, namePhone, 1, pageSize);
            JSONObject single = staffListWithPhone.getJSONArray("list").getJSONObject(0);

            boolean isDelete = single.getBooleanValue("is_delete");

            if (!isDelete) {
                changeChannelStaffState(id);
            }
//            新建一个相同手机号的售楼处员工
            dirPath = dirPath.replace("/", File.separator);

            String faceUrl = uploadImage(dirPath).getString("face_url");
            String phone = genPhoneNum();

            String response = addStaffRes(caseName, getOneStaffType(), phone, faceUrl);
            checkCode(response, StatusCode.SUCCESS, "添加售楼处员工");

            JSONArray staffList = staffList(1, pageSize);

            String idNew = getIdByPhone(staffList, phone);

            deleteStaff(idNew);

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "业务员处于禁用状态，可以新建一个与此业务员相同手机号的售楼处员工");
        }
    }

    /**
     * 同一个人新建的订单与此人第一个订单的首次到访时间是否一致
     **/
    @Test(dataProvider = "ALL_DEAL_IDCARD_PHONE")
    public void orderFirstAppearTimeEquals(String phone, String idCard, String customerName, String firstAppearTime) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + phone;

        try {
//            创建订单
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            Long firstAppearTimeL = orderDetail(orderId).getLong("first_appear_time");

            String firstAppearTimeA = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", firstAppearTimeL);

            if (!firstAppearTime.equals(firstAppearTimeA)) {
                throw new Exception("订单顾客姓名【" + customerName + "】，手机号【" + phone + "】，订单id【" + orderId + "】首次到访时间：【" +
                        firstAppearTimeA + "】，最初订单的首次到访时间：【" + firstAppearTime + "】");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "同一个人新建的订单与此人第一个订单的首次到访时间是否一致");
        }
    }

    /**
     * 更改置业顾问，成单置业顾问不变
     **/
    @Test
    public void adviserFreezeAfterDeal() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            JSONObject resultB = orderDetail(orderId);

            String adviserNameB = resultB.getString("adviser_name");

            String adviserCurrent = "";

//            更改置业顾问
            String cid = "REGISTER-8d69f6ed-7824-48c7-9350-7b3a1d87c791";
            String zhangZhenId = "11";
            String jinChengWuId = "6";

            if ("张震".equals(adviserNameB)) {
                adviserCurrent = "张震";
                customerEdit(cid, "", "", jinChengWuId);
                String adviserNameA = orderDetail(orderId).getString("adviser_name");
                if (!"张震".equals(adviserNameA)) {
                    throw new Exception("成单置业顾问改变，变更前【" + adviserNameB + "】，变更后【" + adviserNameA + "】");
                }
            } else {
                adviserCurrent = "金城武";
                customerEdit(cid, "", "", zhangZhenId);
                String adviserNameA = orderDetail(orderId).getString("adviser_name");
                if (!"金城武".equals(adviserNameA)) {
                    throw new Exception("成单置业顾问改变，变更前【" + adviserNameB + "】，变更后【" + adviserNameA + "】");
                }
            }

//            校验异常环节

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "更改置业顾问，成单置业顾问不变");
        }
    }

    /**
     * 正常订单的首次出现时间<报备时间<签约时间
     **/
    @Test
    public void normalOrderTimeTest() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            JSONObject resultB = orderDetail(orderId);
            Long firstAppearTime = resultB.getLong("first_appear_time");
//            dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss",firstAppearTime)
            Long reportTime = resultB.getLong("report_time");
            Long signTime = resultB.getLong("sign_time");

            if (firstAppearTime < reportTime) {
                throw new Exception("正常订单，手机号【" + phone + "】，订单号【" + orderId + "】，首次到访时间【" +
                        firstAppearTime + "】早于报备时间【" + reportTime + "】");
            }

            if (reportTime > signTime) {
                throw new Exception("正常订单，手机号【" + phone + "】，订单号【" + orderId + "】，报备时间【" +
                        reportTime + "】晚于签约时间【" + signTime + "】");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "正常订单的首次出现时间<报备时间<签约时间");
        }
    }

    /**
     * 由于报备时间晚于首次到访时间的风险订单，详情中的首次到访时间要真的晚于报备时间
     **/
    @Test
    public void riskOrderTimeTest() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 创建订单
            String phone = "";
            String orderId = "";
            String channelId = "";
            String smsCode = "";
            JSONObject result = createOrder(phone, orderId, channelId, smsCode);

            // 查询订单
            JSONObject resultB = orderDetail(orderId);
            Long firstAppearTime = resultB.getLong("first_appear_time");
//            dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss",firstAppearTime)
            Long reportTime = resultB.getLong("report_time");

            if (firstAppearTime > reportTime) {
                throw new Exception("风险订单，手机号【" + phone + "】，订单号【" + orderId + "】，首次到访时间【" +
                        firstAppearTime + "】晚于报备时间【" + reportTime + "】");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "由于报备时间晚于首次到访时间的风险订单，详情中的首次到访时间要真的晚于报备时间");
        }
    }

    /**
     * 订单列表按照新建时间倒排
     **/
    @Test
    public void orderListRank() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            // 订单列表
            JSONArray jsonArray = orderList(1, pageSize).getJSONArray("list");
            checkRank(jsonArray, "customer_phone", "订单列表>>>");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "订单列表按照新建时间倒排");
        }
    }

    /**
     * 员工列表按照新建时间倒排
     **/
    @Test
    public void staffListRank() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            // 员工列表
            JSONArray jsonArray = staffList(1, pageSize);
            checkRank(jsonArray, "phone", "员工列表>>>");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "员工列表按照新建时间倒排");
        }
    }

    /**
     * 渠道列表按照新建时间倒排
     **/
    @Test
    public void channelListRank() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            // 渠道列表
            JSONArray jsonArray = channelList(1, pageSize).getJSONArray("list");
            checkRank(jsonArray, "phone", "渠道列表>>>");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "渠道列表按照新建时间倒排");
        }
    }

    /**
     * 渠道员工列表按照新建时间倒排
     **/
    @Test
    public void channelStaffListRank() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            // 渠道员工列表
            JSONArray jsonArray = channelStaffList(channelId, 1, pageSize);
            checkRank(jsonArray, "phone", "渠道员工列表>>>");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "渠道员工列表按照新建时间倒排");
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
     * 顾客自主创建时，不填写全手机号（中间四位带*，或者是不符合手机号规范）
     **/
    @Test
    public void registerStarPhone() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            String cusotmerName = "";
            String gender = "";
            String phone = "";
            String verifyCode = "";
            String hotPoints = "";
            String adviserId = "";
            JSONObject data = selfRegister(cusotmerName, gender, phone, verifyCode, hotPoints, adviserId);

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

    private void checkRank(JSONArray list, String key, String function) throws Exception {
        for (int i = 0; i < list.size() - 1; i++) {
            JSONObject singleB = list.getJSONObject(i);
            long gmtCreateB = singleB.getLongValue("gmt_create");
            JSONObject singleA = list.getJSONObject(i + 1);
            long gmtCreateA = singleA.getLongValue("gmt_create");

            if (gmtCreateB < gmtCreateA) {
                String phoneB = singleB.getString(key);
                String phoneA = singleA.getString(key);

                throw new Exception(function + "没有按照创建时间倒排！前一条,phone:【" + phoneB + ",gmt_create【" + gmtCreateB +
                        "】，后一条phone【" + phoneA + ",gmt_create【" + gmtCreateA + "】");
            }
        }
    }

    public int getChannelStaffReportNum(JSONArray list) {
        int reportNum = 0;

        for (int i = 0; i < list.size(); i++) {
            JSONObject singleStaff = list.getJSONObject(i);
            String staffId = singleStaff.getString("id");
            if ("12".equals(staffId)) {//宫二的员工id
                reportNum = singleStaff.getInteger("total_report");
                break;
            }
        }
        return reportNum;
    }

    public void compareOrderTimeValue(JSONObject data, String key, String orderId, String valueExpect, String function1, String function2) throws Exception {
        String valueStr = data.getString(key);
        if (valueStr != null && !"".equals(valueStr)) {
            String firstStr = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", Long.valueOf(valueStr));
            if (!firstStr.equals(valueExpect)) {
                throw new Exception("订单id：" + orderId + ",【" + key + "】在" + function1 + "中是：" + valueExpect + ",在" + function2 + "中是：" + firstStr);
            }
        }
    }

    public void compareValue(JSONObject data, String function, String cid, String key, String valueExpect, String comment) throws Exception {

        String value = getValue(data, key);

        if (!valueExpect.equals(value)) {
            throw new Exception(function + "id：" + cid + ",列表中" + comment + "：" + valueExpect + ",详情中：" + value);
        }
    }

    public String getValue(JSONObject data, String key) {
        String value = data.getString(key);

        if (value == null || "".equals(value)) {
            value = "";
        }

        return value;
    }

    public ArrayList<String> getIdsByPhones(JSONArray staffList, ArrayList<String> phones) {
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < phones.size(); i++) {
            String id = getIdByPhone(staffList, phones.get(i));
            ids.add(id);
        }

        return ids;
    }

    public String getIdByPhone(JSONArray staffList, String phone) {
        String id = "";
        for (int j = 0; j < staffList.size(); j++) {
            JSONObject singleStaff = staffList.getJSONObject(j);
            String phoneRes = singleStaff.getString("phone");
            if (phone.equals(phoneRes)) {
                id = singleStaff.getString("id");
            }
        }

        return id;
    }

    public String getIdByPhoneAndStatus(JSONArray staffList, String phone, boolean status) {
        String id = "";
        for (int j = 0; j < staffList.size(); j++) {
            JSONObject singleStaff = staffList.getJSONObject(j);
            String phoneRes = singleStaff.getString("phone");
            boolean isDelete = singleStaff.getBooleanValue("is_delete");
            if (phone.equals(phoneRes) && (isDelete == status)) {
                id = singleStaff.getString("id");
            }
        }

        return id;
    }

    private void checkConflict(JSONArray logSteps, String orderId, boolean isExist) throws Exception {
        boolean isExistRes = false;
        for (int i = 0; i < logSteps.size(); i++) {
            JSONObject oneStep = logSteps.getJSONObject(i);
            String stepType = oneStep.getString("step_type");

            if ("GENDER_AUDIT".equals(stepType)) {
                isExistRes = true;
                if (!oneStep.getBooleanValue("is_in_risk")) {
                    throw new Exception("orderId[" + orderId + "],性别冲突时，环节没有标记为异常！");
                }
            }
        }

        if (!isExistRes == isExist) {
            throw new Exception("orderId[" + orderId + "],是否期待有“信息冲突”环节，期待：" + isExist + "，系统返回：" + isExistRes);
        }
    }

    public void checkOrder(JSONObject result, int expectStatus, boolean expectNeedAudit) {
        Object orderStatus = JSONPath.eval(result, "$.order_status");
        Assert.assertEquals(orderStatus, expectStatus, "订单状态不正常");

        Object isNeedAudit = JSONPath.eval(result, "$.is_audited");
        Assert.assertEquals(isNeedAudit, expectNeedAudit, "核验状态不正常");

    }

    public String getIdOfStaff(String res) {

        JSONObject resJo = JSON.parseObject(res);

        Integer resCode = resJo.getInteger("code");

        String id = "";

        if (resCode == StatusCode.BAD_REQUEST) {

            String message = resJo.getString("message");

            id = message.substring(message.indexOf(":") + 1, message.indexOf(")")).trim();
        }

        return id;
    }

    public int getTotalPage(JSONObject data) {
        return data.getInteger("pages");
    }

    public int getCustomerTotalPage(JSONObject data) {
        double total = data.getDoubleValue("total");

        return (int) Math.ceil(total / 10.0);
    }

    public String getOneStaffType() throws Exception {
        JSONArray staffTypeList = staffTypeList();
        Random random = new Random();
        return staffTypeList.getJSONObject(random.nextInt(3)).getString("staff_type");
    }


    public String genPhoneNum() {
        Random random = new Random();
        long num = 17700000000L + random.nextInt(99999999);

        return String.valueOf(num);
    }

    private void checkChannelEqualsStaff(JSONArray list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String channelName = single.getString("channel_name");
//            int reportNum = single.getInteger("report_num");
            String channelId = single.getString("channel_id");
            JSONArray staffList = channelStaffList(channelId, 1, pageSize);
            int total = 0;
            for (int j = 0; j < staffList.size(); j++) {
                JSONObject singleStaff = staffList.getJSONObject(j);
//                total += singleStaff.getInteger("report_num");
            }

//            if (reportNum!=total){
//                throw new Exception("渠道：" + channelName + ",渠道报备数=" + reportNum + ",业务员总报备数="+ total);
//            }
        }
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

    @DataProvider(name = "ALL_DEAL_IDCARD_PHONE")
    private static Object[][] dealIdCardPhone() {
        return new Object[][]{
                new Object[]{
                        "12111111123", "222222222222222221", "傅天宇", "2019-11-18 21:38:50"
                },
                new Object[]{
                        "12111111311", "111111111111111115", "谢志东", "2019-11-19 09:52:48"
                },
                new Object[]{
                        "14311111111", "111111111111111119", "杨航", "2019-11-25 20:33:03"
                },
                new Object[]{
                        "18411112112", "111111111111111112", "廖祥茹", "2019-11-26 08:58:29"
                },
                new Object[]{
                        "12111111119", "111111111111111113", "刘峤", "2019-11-26 10:26:46"
                },
                new Object[]{
                        "12111111115", "666666666666666666", "更改置业顾问", "2019-11-18 21:50:16"
                },
                new Object[]{
                        "14111111135", "111111111111111114", "李俊延", "2019-11-29 17:41:55"
                },
                new Object[]{
                        "16600000005", "222222222222222222", "华成裕", "2019-11-19 10:26:34"
                },
                new Object[]{
                        "18811111111", "111111111111111111", "于海生", "2019-11-18 21:14:05"
                },
                new Object[]{
                        "18888811111", "333333333333333335", "创单报备", "2019-11-19 12:42:40"
                },
//                new Object[]{
//                        "16600000003", "111111111111111116", "刘博", "2019-11-18 21:38:50"
//                },未到场
//                new Object[]{
//                        "16600000002", "111111111111111117", "未到场B", "2019-11-19 09:52:48"
//                },未到场
                new Object[]{
                        "19811111111", "555555555555555565", "康琳", "2019-11-26 16:37:24"
                },
                new Object[]{
                        "18831111111", "555555555555555555", "性别男", "2019-12-16 13:16:45"
                }
        };
    }

    @DataProvider(name = "ALL_DEAL_PHONE")
    private static Object[] dealPhone() {
        return new Object[]{
                "12111111123",
                "12111111311",
                "14311111111",
                "18411112112",
                "12111111119",
                "12111111115",
                "14111111135",
                "16600000005",
                "18811111111",
                "18888811111",
                "16600000003",
                "16600000002",
                "19811111111",
                "18831111111"
        };
    }

    @DataProvider(name = "CUSTOMER_TYPE_LIST_NOT_NULL")
    private static Object[] customerTypeListNotNull() {
        return new Object[]{
                "[list]-customer_type",
                "[list]-desc",
                "[list]-type_name"
        };
    }

    @DataProvider(name = "AGE_GROUP_LIST_NOT_NULL")
    private static Object[] ageGroupListNotNull() {
        return new Object[]{
                "[list]-age_group_id",
                "[list]-type_name"
        };
    }

    @DataProvider(name = "TIME_RANGE_LIST_NOT_NULL")
    private static Object[] timeRangeListNotNull() {
        return new Object[]{
                "[list]-type",
                "[list]-type_name"
        };
    }

    @DataProvider(name = "CUSTOMER_LIST_NOT_NULL")
    private static Object[] customerListNotNull() {
        return new Object[]{
                "[list]-customer_name",
                "[list]-phone",
                "[list]-adviser_name",
                "[list]-channel_name",
                "[list]-report_time"
        };
    }

    @DataProvider(name = "CUSTOMER_SIMPLE_INFO_NOT_NULL")
    private static Object[] customerSimpleInfoNotNull() {
        return new Object[]{
                "[list]-customer_name",
                "[list]-phone",
                "[list]-adviser_name",
                "[list]-channel_name"
        };
    }

    @DataProvider(name = "ORDER_LIST_NOT_NULL")
    private static Object[] orderListNotNull() {
        return new Object[]{
                "[list]-order_id",
                "[list]-customer_name",
                "[list]-first_appear_time",
                "[list]-adviser_name",
                "[list]-deal_time",
                "[list]-is_audited",
                "[list]-report_time",
                "[list]-status",

        };
    }

    @DataProvider(name = "CHANNEL_LIST_NOT_NULL")
    private static Object[] channelListNotNull() {
        return new Object[]{
                "[list]-channel_id",
                "[list]-channel_name",
                "[list]-channel_type_desc",
                "[list]-owner_principal",
                "[list]-phone",
                "[list]-register_time",
                "[list]-total_customers"
        };
    }

    @DataProvider(name = "ADVISER_LIST_NOT_NULL")
    private static Object[] adviserListNotNull() {
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

    @DataProvider(name = "REPORT_INFO")
    private static Object[] reportInfoNotNull() {
        return new Object[]{
                "[list]-cid",
                "[list]-channel_id",
                "[list]-desc"
        };
    }

    @DataProvider(name = "ORDER_DETAIL")
    private static Object[] orderDetailNotNull() {
        return new Object[]{

        };
    }

    @DataProvider(name = "ORDER_LINK_LIST")
    private static Object[] orderLinkListNotNull() {
        return new Object[]{

        };
    }

    @DataProvider(name = "ORDER_STATUS_LIST")
    private static Object[] orderStatusListNotNull() {
        return new Object[]{
                "[list]-cid",
                "[list]-type",
                "[list]-status_name",

        };
    }

    @DataProvider(name = "CHANNEL_DETAIL")
    private static Object[] channelDetailNotNull() {
        return new Object[]{

        };
    }
}

