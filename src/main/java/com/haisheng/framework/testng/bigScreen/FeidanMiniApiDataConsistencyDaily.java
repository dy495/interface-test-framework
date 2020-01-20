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

public class FeidanMiniApiDataConsistencyDaily {

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

    String mineChannelStr = "5";
    int mineChannelInt = 5;
    String gongErIdStr = "12";
    int gongErIdInt = 12;
    String anShengIdStr = "15";
    int anShengIdInt = 15;

    String genderMale = "MALE";
    String genderFemale = "FEMALE";

    String lianjiaToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmtYvor5XjgJDli7_liqjjgJEiLCJ1aWQiOjIxMzYsImxvZ2luVGltZSI6MT" +
            "U3ODk5OTY2NjU3NH0.kQsEw_wGVmPQ4My1p-FNZ556FJC7W177g7jfjFarTu4";
    String lianjiaStaffIdStr = "2136";
    int lianjiaStaffIdInt = 2136;

    String wudongToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjgJDli7_li" +
            "qjjgJExIiwidWlkIjoyMDk4LCJsb2dpblRpbWUiOjE1Nzg1NzQ2MjM4NDB9.exDJ6avJKJd3ezQkYc4fmUkHvXaukqfgjThkpoYgnAw";

    String wudongStaffIdStr = "2098";
    int wudongStaffIdInt = 2098;

    int pageSize = 50;

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

    private ApiResponse sendRequestWithGate(String router, String[] resource, String json) throws Exception {
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

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    private String httpPostUrl(String path, String json) throws Exception {
        initHttpConfig();
        config.url(path).json(json);
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
        qaDbUtil.openConnectionRdDaily();
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
        qaDbUtil.closeConnectionRdDaily();
        dingPushFinal();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

    public void updateReportTime(String phone, String customerName, int channelId, int staffId) throws Exception {
        ReportTime reportTime = new ReportTime();
        reportTime.setShopId(4116);
        reportTime.setChannelId(channelId);
        reportTime.setChannelStaffId(staffId);
        reportTime.setPhone(phone);
        reportTime.setCustomerName(customerName);
        long timestamp = 1547014264000L;
        reportTime.setReportTime(String.valueOf(timestamp));
        reportTime.setGmtCreate(dateTimeUtil.changeDateToSqlTimestamp(timestamp));

        qaDbUtil.updateReportTime(reportTime);
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
            "\"shop_id\":${shopId},\"channel_id\":\"${mineChannelStr}\",\"page\":\"${page}\",\"size\":\"${pageSize}\"}";
//    顾客列表中是size参数控制一页显示的条数，订单列表中是pageSize控制

    //    顾客列表中是size参数控制一页显示的条数，订单列表中是pageSize控制
    private static String ORDER_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"page_size\":\"${pageSize}\"}";
    private static String ORDER_LIST_WITH_CHANNEL_JSON = "{\"shop_id\":${shopId},\"channel_id\":\"${mineChannelStr}\",\"page\":\"1\",\"page_size\":\"10000\"}";
    private static String ORDER_LIST_WITH_STATUS_JSON = "{\"shop_id\":${shopId},\"status\":\"${status}\",\"page\":\"1\",\"page_size\":\"10000\"}";
    private static String ORDER_LIST_WITH_PHONE_JSON = "{\"shop_id\":${shopId},\"customer_name\":\"${customerName}\",\"page\":\"1\",\"page_size\":\"10000\"}";

    private static String ORDER_DETAIL_JSON = "{\"order_id\":\"${orderId}\"," +
            "\"shop_id\":${shopId}}";

    private static String CHANNEL_STAFF_LIST_JSON = "{\"channel_id\":\"${mineChannelStr}\"," +
            "\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";
    private static String CHANNEL_STAFF_LIST_PHOEN_JSON = "{\"channel_id\":\"${mineChannelStr}\"," +
            "\"shop_id\":${shopId},\"name_phone\":\"${namePhone}\",\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

    private static String CUSTOMER_INSERT_JSON = "{\"shop_id\":\"${shopId}\",\"channel_id\":${mineChannelStr}," +
            "\"channel_staff_id\":\"${channelStaffId}\",\"adviser_id\":\"${adviserId}\"," +
            "\"gender\":\"${gender}\",\"customer_name\":\"${customerName}\",\"phone\":\"${phone}\"}";

    private static String CHANNEL_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"page_size\":\"${pageSize}\"}";

    private static String STAFF_TYPE_LIST_JSON = "{\"shop_id\":${shopId}}";

    private static String STAFF_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

    private static String STAFF_LIST_WITH_TYPE_JSON = "{\"shop_idaddStaffTestPage\":${shopId},\"staff_type\":\"${staffType}\",\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

    private static String ADD_CHANNEL_JSON = "{\"shop_id\":${shopId},\"channel_name\":\"${channelName}\"," +
            "\"owner_principal\":\"${owner}\",\"phone\":\"${phone}\",\"contract_code\":\"${contractCode}\"}";

    private static String ADD_CHANNEL_STAFF_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"channel_id\":\"${mineChannelStr}\",\"phone\":\"${phone}\"}";

    private static String ADD_CHANNEL_STAFF_WITH_PIC_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"channel_id\":\"${mineChannelStr}\",\"phone\":\"${phone}\",\"face_url\":\"${faceUrl}\"}";

    private static String EDIT_CHANNEL_STAFF_WITH_PIC_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"channel_id\":\"${mineChannelStr}\",\"face_url\":\"${faceUrl}\",\"phone\":\"${phone}\"}";

    private static String EDIT_CHANNEL_STAFF_JSON = "{\"shop_id\":${shopId},\"staff_name\":\"${staffName}\"," +
            "\"channel_id\":\"${mineChannelStr}\",\"phone\":\"${phone}\"}";

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
                .put("mineChannelStr", channelId)
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
                .put("mineChannelStr", channelId) //测试【勿动】
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
            phone = genPhone();
            newCustomer(channelId, channelStaffId, adviserId, phone, customerName, gender);
        }
    }

    public void newCustomer(int channelId, int channelStaffId, int adviserId, String phone, String customerName, String gender) throws Exception {

        String json = StrSubstitutor.replace(CUSTOMER_INSERT_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("mineChannelStr", channelId) //测试【勿动】
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
            phone = genPhone();
            newCustomer(channelId, channelStaffId, adviserId, phone, customerName, gender);
        }
    }

    public void newCustomerWithCode(int channelId, int channelStaffId, int adviserId, String phone, String customerName, String gender) throws Exception {

        String url = "";

        String json = StrSubstitutor.replace(CUSTOMER_INSERT_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("mineChannelStr", channelId) //测试【勿动】
                .put("channelStaffId", channelStaffId)//宫二
                .put("adviserId", adviserId)
                .put("phone", phone)
                .put("customerName", customerName)
                .put("gender", gender)
                .build()
        );

        httpPostWithCheckCode(CUSTOMER_INSERT, json, "");
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
    public JSONObject createOrder(String phone, String orderId, String faceUrl, int channelId, String smsCode) throws Exception {

        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "    \"order_id\":\"" + orderId + "\",";
        if (channelId != -1) {
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
                        "    \"order_id\":\"" + orderId + "\"\n" +
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
                        "    \"page\":1" + page+ ",\n";
        if (status != -1) {
            json += "    \"status\":\"" + status + "\",\n";
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
     *无状态的订单列表
     */
    public JSONObject orderListt(int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"page\":1" + ",\n" +
                         "    \"size\":" + pageSize + "\n" +
                "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONArray orderListWithChannel(String channelId, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(ORDER_LIST_WITH_CHANNEL_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("mineChannelStr", channelId)
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
    public JSONObject channelStaffList(String channelId, String namePhone, int page, int pageSize) throws Exception {
        String url = "/risk/channel/staff/page";

        String json =
                "{\n";
        if (!"".equals(namePhone)) {
            json += "    \"name_phone\":\"" + namePhone + "\",";

        }

        json +=
                "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"channel_id\":\"" + channelId + "\",\n" +
                        "    \"page\":" + page + ",\n" +
                        "    \"size\":" + pageSize +
                        "}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 6.6 合作渠道员工注册接口
     */
    public JSONObject addChannelStaff(String staffName, String channelId, String phone) throws Exception {
        String json = StrSubstitutor.replace(ADD_CHANNEL_STAFF_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("mineChannelStr", channelId)
                .put("phone", phone)
                .build()
        );
        String res = httpPostWithCheckCode(ADD_CHANNEL_STAFF, json);

        JSONObject result = JSON.parseObject(res);
        int codeRes = result.getInteger("code");
        String message = result.getString("message");

        if (codeRes == 1001) {
            if ("当前手机号已被使用".equals(message)) {
                phone = genPhone();
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
                .put("mineChannelStr", channelId)
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
                .put("mineChannelStr", channelId)
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
                phone = genPhone();
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
                .put("mineChannelStr", channelId)
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
                .put("mineChannelStr", channelId)
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
                phone = genPhone();
                res = editChannelStaffPic(staffId, staffName, channelId, phone, faceUrl);
            }
        }

        return res;
    }

    public String editChannelStaffPhone(String staffId, String staffName, String channelId, String phone) throws Exception {
        String json = StrSubstitutor.replace(EDIT_CHANNEL_STAFF_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffName", staffName)
                .put("mineChannelStr", channelId)
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
    public String staffDetailH5(String token) throws Exception {
        String url = "http://dev.store.winsenseos.cn/external/channel/staff/detail";
        String json =
                "{\n" +
                        "    \"token\":\"" + token + "\"," +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}\n";

        String response = httpPostUrl(url, json);

        return response;
    }

    /**
     * 6.15 渠道客户报备H5
     */
    public String customerReportH5(String staffId, String customerName, String phone, String gender, String token) throws Exception {
        String url = "/external/channel/customer/report";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + staffId + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"customer_phone\":\"" + phone + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"token\":\"" + token + "\"" +
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
    public String channelCustomerListH5(String token, int page, int pageSize) throws Exception {
        String url = "http://dev.store.winsenseos.cn/external/channel/customer/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"token\":\"" + token + "\"," +
                        "    \"page\":\"" + page + "\"," +
                        "    \"size\":\"" + pageSize + "\"" +
                        "}\n";

        String response = httpPostUrl(url, json);

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
                phone = genPhone();
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
    public JSONObject selfRegister(String customerName, String phone, String verifyCode, String adviserId, String hotPoints, String gender) throws Exception {
        String url = "/external/self-register/confirm";

        String json =
                "{\n" +
                        "    \"name\":\"" + customerName + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"verify_code\":\"" + verifyCode + "\",";
        if (!"".equals(adviserId)) {
            json += "    \"adviser_id\":\"" + adviserId + "\",";
        }
        if (!"".equals(hotPoints)) {
            json += "    \"hot_points\":[1],";
        }

        json += "    \"shop_id\":" + getShopId() + "}";

        String res = httpPostWithCheckCode(url, json, "");

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 8.11 置业顾问列表
     */
    public String consultantList() throws Exception {
        String url = "/external/self-register/confirm";

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

        sendRequestWithGate(router, resource, json);
    }

    public String witnessUpload(String cardId, String personName, String isPass, String cardPic, String capturePic) throws Exception {
        String router = "/risk-inner/witness/upload";
        String json =
                "{\n" +
                        "    \"data\":{\n" +
                        "        \"person_name\":\"" + personName + "\"," +
                        "        \"capture_pic\":\"@1\"," +
                        "        \"is_pass\":true," +
                        "        \"card_pic\":\"@0\"," +
                        "        \"card_id\":\"" + cardId + "\"" +
                        "    },\n" +
                        "    \"request_id\":\"" + UUID.randomUUID() + "\"," +
                        "    \"resource\":[" +
                        "        \"http://retail-huabei2.oss-cn-beijing.aliyuncs.com/dispatcher_daily/uid_ef6d2de5/49998b971ea0/54/20200110/d4f461ea-536e-4a98-86e6-6363035a738a_1?Expires=4732656640&OSSAccessKeyId=LTAIlYpjA39n18Yr&Signature=QcaSjfE4VHvxvt5VgQOt%2F4YILQM%3D\"," +
                        "        \"http://retail-huabei2.oss-cn-beijing.aliyuncs.com/dispatcher_daily/uid_ef6d2de5/49998b971ea0/54/20200110/d4f461ea-536e-4a98-86e6-6363035a738a_1?Expires=4732656640&OSSAccessKeyId=LTAIlYpjA39n18Yr&Signature=QcaSjfE4VHvxvt5VgQOt%2F4YILQM%3D\"" +
                        "    ],\n" +
                        "    \"system\":{" +
                        "        \"app_id\":\"49998b971ea0\"," +
                        "        \"device_id\":\"6934268400763904\"," +
                        "        \"scope\":[" +
                        "            \"4116\"" +
                        "        ]," +
                        "        \"service\":\"/business/risk/WITNESS_UPLOAD/v1.0\"," +
                        "        \"source\":\"DEVICE\"" +
                        "    }" +
                        "}";

        return httpPostWithCheckCode(router, json);
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

            String cardId = "222402199700000107";
            String personName = "1";
            String isPass = "true";
            String cardPic = "";
            String capturePic = "http";

            String s = witnessUpload(cardId, personName, isPass, cardPic, capturePic);

            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + s);

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

    //    --------------------------------------风控订单----------------------------------------------------------


    public void report1() throws Exception {

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmtYvor5XjgJDli7_liqjjgJEiLCJ1aWQiOjIxMzYsImxvZ2" +
                "luVGltZSI6MTU3ODk5OTY2NjU3NH0.kQsEw_wGVmPQ4My1p-FNZ556FJC7W177g7jfjFarTu4";

        String customerPhone = "14112342321";
        String customerName = "" + System.currentTimeMillis();

        // PC报备
        int adviserId = 8;
        int channelId = 1;
        int channelStaffId = 2136;
//            H5报备
        customerReportH5("2136", customerName, customerPhone, "MALE", token);

    }

    private void checkOrder(String orderId, String phone) throws Exception {

        JSONArray list = orderList(3, phone, 100,pageSize).getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String orderIdRes = single.getString("order_id");

            if (orderId.equals(orderIdRes)) {

                JSONObject orderDetail = orderDetail(orderId);
                String function = "订单详情>>>";

                String customerName = single.getString("customer_name");
                checkUtil.checkKeyValue(function, orderDetail, "customer_name", customerName, true);

                String adviserName = single.getString("adviser_name");
                if (adviserName == null || "".equals(adviserName)) {
                    String adviserNameDetail = orderDetail.getString("adviser_name");
                    if (adviserNameDetail != null && !"".equals(adviserNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",adviser_name在订单列表中是空，在订单详情中=" + adviserNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "adviser_name", adviserName, true);
                }

                String channelName = single.getString("channel_name");
                if (channelName == null || "".equals(channelName)) {
                    String channelNameDetail = orderDetail.getString("channel_name");
                    if (channelNameDetail != null && !"".equals(channelNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",adviser_name在订单列表中是空，在订单详情中=" + channelNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "channel_name", channelName, true);
                }

                String channelStaffName = single.getString("channel_staff_name");
                if (channelStaffName == null || "".equals(channelStaffName)) {
                    String channelStaffNameDetail = orderDetail.getString("channel_staff_name");
                    if (channelStaffNameDetail != null && !"".equals(channelStaffNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",channel_staff_name在订单列表中是空，在订单详情中=" + channelStaffNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "channel_staff_name", channelStaffName, true);
                }

                String firstAppearTime = single.getString("first_appear_time");
                if (firstAppearTime == null || "".equals(firstAppearTime)) {
                    String firstAppearTimeDetail = orderDetail.getString("first_appear_time");
                    if (firstAppearTimeDetail != null && !"".equals(firstAppearTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",first_appear_time在订单列表中是空，在订单详情中=" + firstAppearTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "first_appear_time", firstAppearTime, true);
                }

                String dealTime = single.getString("deal_time");
                if (dealTime == null || "".equals(dealTime)) {
                    String dealTimeDetail = orderDetail.getString("deal_time");
                    if (dealTimeDetail != null && !"".equals(dealTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",deal_time在订单列表中是空，在订单详情中=" + dealTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "deal_time", dealTime, true);
                }

                String reportTime = single.getString("report_time");
                if (reportTime == null || "".equals(reportTime)) {
                    String reportTimeDetail = orderDetail.getString("report_time");
                    if (reportTimeDetail != null && !"".equals(reportTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",report_time在订单列表中是空，在订单详情中=" + reportTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "report_time", reportTime, true);
                }


                String isAudited = single.getString("is_audited");
                if (isAudited == null || "".equals(isAudited)) {
                    String isAuditedDetail = orderDetail.getString("report_time");
                    if (isAuditedDetail != null && !"".equals(isAuditedDetail)) {
                        throw new Exception("orderId=" + orderId + ",is_audited在订单列表中是空，在订单详情中=" + isAuditedDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "is_audited", isAudited, true);
                }

                break;
            }
        }
    }

//    -------------------------------------------------数据一致性验证-------------------------------------------------------------

    @Test
    public void reportInfoEquals() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "H5页面业务员报备总数与H5页面内业务员报备条数一致>>>";

        try {

//            H5页面内报备数
            String staffPhone = "17722222221";
            String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjgJDli7_li" +
                    "qjjgJExIiwidWlkIjoyMDk4LCJsb2dpblRpbWUiOjE1Nzg1NzQ2MjM4NDB9.exDJ6avJKJd3ezQkYc4fmUkHvXaukqfgjThkpoYgnAw";

            customerReportH5("2098", caseName, genPhoneStar(), "MALE", token);

            String staffDetailH5 = staffDetailH5(token);

            int reportNumH5 = JSON.parseObject(staffDetailH5).getJSONObject("data").getInteger("report_num");

            String customerListH5 = channelCustomerListH5(token, 1, 200);

            int reportNumListNum = JSON.parseObject(customerListH5).getJSONObject("data").getJSONArray("list").size();
            int reportNumListTotal = JSON.parseObject(customerListH5).getJSONObject("data").getInteger("total");

            if (reportNumH5 != reportNumListNum) {
                throw new Exception("业务员手机号:" + staffPhone + "H5页面内的报备总数=" + reportNumH5 + ",H5页面内的报备条数=" + reportNumListNum);
            }

            if (reportNumListNum != reportNumListTotal) {
                throw new Exception("业务员手机号:" + staffPhone + "H5页面内列表中的报备条数=" + reportNumListTotal + ",H5页面内的显示的报备条数=" + reportNumListNum);
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
    public void reportNumPCEqualsH5() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "H5页面报备总数与PC页面内报备总数一致>>>";

        try {

            int reportNumPC = 0;

            String staffPhone = "17722222221";
            String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjgJDli7_li" +
                    "qjjgJExIiwidWlkIjoyMDk4LCJsb2dpblRpbWUiOjE1Nzg1NzQ2MjM4NDB9.exDJ6avJKJd3ezQkYc4fmUkHvXaukqfgjThkpoYgnAw";

            customerReportH5("2098", caseName, genPhoneStar(), "MALE", token);

            String staffDetailH5 = staffDetailH5(token);

            int reportNumH5 = JSON.parseObject(staffDetailH5).getJSONObject("data").getInteger("report_num");

            String channelId = "5";
            JSONArray staffList = channelStaffList(channelId, staffPhone, 1, pageSize).getJSONArray("list");
            for (int i = 0; i < staffList.size(); i++) {
                JSONObject single = staffList.getJSONObject(i);
                if ("2098".equals(single.getString("id"))) {
                    reportNumPC = single.getInteger("total_report");
                    break;
                } else {
                    throw new Exception("不存在手机号为：" + staffPhone + "，的业务员。");
                }
            }

            if (reportNumH5 != reportNumPC) {
                throw new Exception("业务员手机号:" + staffPhone + "H5页面内的报备数=" + reportNumH5 + ",PC端的报备数=" + reportNumPC);
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
    public void OrderListLinkEquals() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "证据页事项与风控列表中展示的信息一致>>>";

        try {

            JSONArray list = orderList(1, "", 1,pageSize).getJSONArray("list");

            checkOrderListEqualsLinkList(list);

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
    public void OrderListFilter() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "风控列表过滤项的子单数 <= 总单数>>>";

        try {

            checkOrderListFilter();

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



//---------------------------------------------------------------其他验证--------------------------------------------------------------

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
            JSONArray list = orderList(1, "",1, pageSize).getJSONArray("list");
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

                JSONArray staffList = channelStaffList(channelId, "", 1, pageSize).getJSONArray("list");
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
            String totalList = orderListt(pageSize).getString("total");
            int totalNum = Integer.parseInt(totalList);
            int normalNum = 0;
            int unknownNum = 0;
            int riskNum = 0;
//            获取正常订单数
            JSONArray normalList = orderList(1, "", 1, 150).getJSONArray("list");//1是正常，2是未知，3是风险
            normalNum = normalNum + normalList.size();

//            获取未知订单数
            JSONArray unknowlList = orderList(2, "", 1, 150).getJSONArray("list");//1是正常，2是未知，3是风险
            unknownNum = unknownNum + unknowlList.size();

//            获取风险订单数
            JSONArray riskList = orderList(3, "", 1, 200).getJSONArray("list");//1是正常，2是未知，3是风险
            riskNum = riskNum + riskList.size();

            if (normalNum + unknownNum + riskNum != totalNum) {
                throw new Exception("订单列表总数：" + totalNum + "正常订单数" + normalNum + "未知订单数" +unknownNum + "异常订单数" +riskNum);
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

                String phoneNum = genPhone();

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
**/
    /**
     * 渠道业务员列表每页显示核查

    @Test
    public void addChannelStaffTestPage() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String channelId = "5";
            int pageSizeTemp = 2;

            ArrayList<String> phones = new ArrayList<>();

            for (int i = 0; i < 2; i++) {
                String phoneNum = genPhone();
                addChannelStaff("change-test-" + i, channelId, phoneNum);
                JSONObject temp = channelStaffList(channelId, "", 1, pageSizeTemp);

                int totalPage = getTotalPage(temp);
                for (int j = 1; j <= totalPage; j++) {
                    JSONArray singlePage = channelStaffList(channelId, "", j, pageSizeTemp).getJSONArray("list");

                    if (j < totalPage) {
                        if (singlePage.size() != 2) {
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

            JSONArray staffList = channelStaffList(channelId, "", 1, pageSize).getJSONArray("list");
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
**/
    /**
     * 人脸注册渠道员工，期望成功

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

                String phoneNum = genPhone();

                phones.add(phoneNum);

                addChannelStaffWithPic("staff-" + i, channelId, phoneNum, uploadImage.getString("face_url"));
            }

            JSONArray staffList = channelStaffList(channelId, "", 1, pageSize).getJSONArray("list");
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
**/
    /**
     * 渠道列表每页显示是否正常

    @Test
    public void addChannelTestPage() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            int pageSizeTemp = 2;

            for (int i = 0; i < 2; i++) {
                String phoneNum = genPhone();
                addChannel("channel-" + System.currentTimeMillis(), "于老师", phoneNum, "test");
                JSONObject temp = channelList(1, pageSizeTemp);

                int totalPage = getTotalPage(temp);
                for (int j = 1; j <= totalPage; j++) {
                    JSONArray singlePage = channelList(j, pageSizeTemp).getJSONArray("list");

                    if (j < totalPage) {
                        if (singlePage.size() != 2) {
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
**/


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
            JSONArray jsonArray = orderList(1, "",1, pageSize).getJSONArray("list");
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
            JSONArray jsonArray = channelStaffList(mineChannelStr, "", 1, pageSize).getJSONArray("list");
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



//    --------------------------------------------------非空校验-----------------------------------------------------------------




    private void checkOrderListEqualsLinkList(JSONArray list) throws Exception {

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String orderId = single.getString("order_id");
            String adviserName = single.getString("adviser_name"); //置业顾问
            String channelName = single.getString("channel_name"); //成交渠道
            String firstappearTime = single.getString("first_appear_time"); //首次到访时间
            String dealTime = single.getString("deal_time"); //刷证时间
            JSONArray orderLinkList = orderLinkList(orderId).getJSONArray("list");
            for (int r = 1 ; r < orderLinkList.size(); r++) {
                JSONObject link = orderLinkList.getJSONObject(r);
                String linkPoint = link.getString("link_point");
                String linkName = link.getString("link_name");

                if (linkPoint.contains("置业顾问")) {
                    String content = link.getJSONObject("link_note").getString("content");
                    String adviserNameLink = content.substring(content.indexOf("为")+1);

                    if (!adviserName.equals(adviserNameLink)) {
                        throw new Exception("风控列表页，置业顾问是:" + adviserName + ",风控单页，置业顾问是：" + adviserNameLink);
                    }
                }
                if (channelName != null && !"".equals(channelName)) {
                    if ("渠道报备".equals(linkName)) {
                        String content = link.getJSONObject("link_note").getString("content");
                        String channleNameLink = content.substring(0,content.indexOf("-"));
                        if (!channleNameLink.equals(channelName)){
                            throw new Exception("风控列表页，成交渠道是："+ channelName +",风控单页，成交渠道是：" + channleNameLink);
                        }
                    }
                }


                if ("首次到访".equals(linkName)){
                    String apperTimeLink = link.getString("link_time");
                    if (!apperTimeLink.equals(firstappearTime)){
                        throw new Exception("风控列表页，首次到访时间为："+ firstappearTime + ",风控单页，首次到访时间为："+ apperTimeLink);
                    }
                }

                if ("正常:人证⽐对通过".equals(linkPoint)){
                    String dealTimeLink = link.getString("link_time");
                    if (!dealTimeLink.equals(dealTime)){
                        throw new Exception("风控列表页，刷证时间为："+ dealTime + ",风控单页，刷证时间为："+ dealTimeLink);
                    }
                }


            }
        }
    }

    private void checkOrderListFilter() throws Exception{
        String normal_list = orderList(1, "",1, pageSize).getString("total");
        System.out.println(normal_list);
        String risk_list = orderList(3, "", 1,pageSize).getString("total");
        System.out.println(risk_list);
        String unknown_list = orderList(2, "", 1,pageSize).getString("total");
        System.out.println(unknown_list);
        int total = Integer.parseInt(normal_list) + Integer.parseInt(risk_list) + Integer.parseInt(unknown_list);
        if (Integer.parseInt(normal_list) > total){
            throw new Exception("总单数为：" + total + ",正常单单数为："+ normal_list);
        }
        if (Integer.parseInt(risk_list) > total){
            throw new Exception("总单数为：" + total + ",风险单单数为："+ risk_list);
        }
        if ( Integer.parseInt(unknown_list) > total){
            throw new Exception("总单数为：" + total + ",未知单单数为："+ unknown_list);
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


    public String genPhone() {
        Random random = new Random();
        long num = 17700000000L + random.nextInt(99999999);

        return String.valueOf(num);
    }

    public String genCardId() {
        Random random = new Random();
        long num = 100000000000000000L + random.nextInt(99999999);

        return String.valueOf(num);
    }

    public String genPhoneStar() {
        Random random = new Random();
        String num = "177****" + (random.nextInt(8999) + 1000);

        return num;
    }

    private void checkChannelEqualsStaff(JSONArray list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String channelName = single.getString("channel_name");
            if (channelName.contains("channel-")) {
                break;
            }
            int reportNum = single.getInteger("total_customers");
            String channelId = single.getString("channel_id");
            JSONArray staffList = channelStaffList(channelId, "", 1, pageSize).getJSONArray("list");
            int totalstaff = Integer.parseInt(channelStaffList(channelId, "", 1, pageSize).getString("total"));
            int total = 0;
            for (int j = 0; j < totalstaff; j++) {
                JSONObject singleStaff = staffList.getJSONObject(j);
                total += singleStaff.getInteger("total_report");
            }

            if (reportNum != total) {
                throw new Exception("渠道：" + channelName + ",渠道报备数=" + reportNum + ",业务员总报备数=" + total);
            }
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



}

