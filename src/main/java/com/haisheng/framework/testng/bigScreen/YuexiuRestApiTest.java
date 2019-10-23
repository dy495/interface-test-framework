package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import okhttp3.*;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * @author : xiezhidong
 * @date :  2019/10/12  14:55
 */

public class YuexiuRestApiTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private DateTimeUtil dateTimeUtil = new DateTimeUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_YUEXIU_SALES_OFFICE_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/yuexiu-daily-test/buildWithParameters?case_name=";

    private String loginPathDaily = "/yuexiu-login";
    private String jsonDaily = "{\"username\":\"demo@winsense.ai\",\"passwd\":\"fe01ce2a7fbac8fafaed7c982a04e229\"}";
    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLlrp7pqozlrqREZW1vIiwidWlkIjoidWlkXzdmYzc4ZDI0IiwibG9naW5UaW1lIjoxNTcxNTM3OTYxMjU4fQ.lmIXi-cmw3VsuD6RZrPZDJw70TvWuozEtLqV6yFHXVY";

    private String loginPathOnline = "/yuexiu/login";
    private String jsonOnline = "{\"username\":\"yuexiu\",\"passwd\":\"e10adc3949ba59abbe56e057f20f883e\"}";
    /**
     * http工具 maven添加以下配置
     * <dependency>
     * <groupId>com.arronlong</groupId>
     * <artifactId>httpclientutil</artifactId>
     * <version>1.0.4</version>
     * </dependency>
     */
    private HttpConfig config;

    private final static String REAL_TIME_PREFIX = "/yuexiu/data/statistics/real-time/";

    private final static String HISTORY_PREFIX = "/yuexiu/data/statistics/history/";

    private final static String REGION_DATA_PREFIX = "/yuexiu/data/statistics/region/";

    private final static String CUSTOMER_DATA_PREFIX = "/yuexiu/data/statistics/customer/";

    /**
     * 测试环境使用以下customerId  正式环境不确定哪些reId一定存在
     */
    private final static String customerId = "7dd129a1-ecd1-11e9-83b3-00163e0ae160";

    /**
     * 环境   线上为 ONLINE 测试为 DAILY
     */
    private String ENV = System.getProperty("ENV", "");
    private boolean DEBUG = false;

    private long SHOP_ID_DAILY = 2606;
    private long SHOP_ID_ENV = 889;


    private String getRealTimeParamJson() {

        if ("ONLINE".equals(ENV)) {
            return "{\"shop_id\":889}";
        }
        return "{\"shop_id\":2606}";
    }

    private String getHistoryParamJson() {

        long shop_id;
        if ("ONLINE".equals(ENV)) {
            shop_id = 889;
        } else {
            shop_id = 2606;
        }
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(7);
        return "{\"shop_id\":" + shop_id + ",\"start_time\":\"" +
                start + "\",\"end_time\":\"" + end + "\"}";
    }

    private String getCustomerParamJson() {

        long shop_id;
        if ("ONLINE".equals(ENV)) {
            shop_id = 889;
        } else {
            shop_id = 2606;
        }
        return "{\"shop_id\":" + shop_id + ",\"start_time\":\"2019-10-05\",\"end_time\":\"" +
                "2019-10-12\",\"customer_id\":\"" + customerId + "\"}";
    }

    private String getIpPort() {

        if ("ONLINE".equals(ENV)) {
            return "http://123.57.114.205";
        }
        return "http://123.57.114.36";
//        return "http://localhost:7020";
    }

    private void checkNotNull(String function, JSONObject jo, String... checkColumnNames) {

        for (String checkColumn : checkColumnNames) {
            Object column = jo.get(checkColumn);
            logger.info("{} : {}", checkColumn, column);
            if (column == null) {
                failReason = function + ", result does not contains column " + checkColumn;
                return;
            }
        }

    }

    private void checkDeepKeyNotNull(String function, JSONObject jo, String key) throws Exception {

        if (key.contains("]-")) {
            String parentKey = key.substring(1, key.indexOf("]"));
            String childKey = key.substring(key.indexOf("-") + 1);
            checkArrKeyNotNull(function, jo, parentKey, childKey);
        } else if (key.contains("}-")) {
            String parentKey = key.substring(1, key.indexOf("}"));
            String childKey = key.substring(key.indexOf("-") + 1);
            checkObjectKeyNotNull(function, jo, parentKey, childKey);
        } else {
            checkNotNull(function, jo, key);
        }
    }

    private void checkDeepKeyValidity(String function, JSONObject jo, String key) throws Exception {

        if (key.contains("]-")) {
            String parentKey = key.substring(1, key.indexOf("]"));
            String childKey = key.substring(key.indexOf("-") + 1);
            checkArrKeyValidity(function, jo, parentKey, childKey);

        } else if (key.contains("}-")) {
            String parentKey = key.substring(1, key.indexOf("}"));
            String childKey = key.substring(key.indexOf("-") + 1);
            checkObjectKeyValidity(function, jo, parentKey, childKey);

        }
    }

    private void checkArrKeyValidity(String function, JSONObject jo, String parentKey, String childKey) throws Exception {

        JSONArray parent = jo.getJSONArray(parentKey);
        for (int i = 0; i < parent.size(); i++) {
            JSONObject single = parent.getJSONObject(i);
            checkKeyValues(function, single, childKey);
        }
    }

    private void checkObjectKeyValidity(String function, JSONObject jo, String parentKey, String childKey) throws Exception {

        JSONObject parent = jo.getJSONObject(parentKey);
        checkKeyValues(function, parent, childKey);
    }


    private void checkArrKeyNotNull(String function, JSONObject jo, String parentKey, String childKey) throws Exception {

        checkNotNull(function, jo, parentKey);
        JSONArray parent = jo.getJSONArray(parentKey);
        for (int i = 0; i < parent.size(); i++) {
            JSONObject single = parent.getJSONObject(i);
            checkKeyValue(function, single, childKey, "", false);
        }
    }

    private void checkObjectKeyNotNull(String function, JSONObject jo, String parentKey, String childKey) throws Exception {

        checkNotNull(function, jo, parentKey);

        JSONObject parent = jo.getJSONObject(parentKey);

        checkKeyValue(function, parent, childKey, "", false);
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

    private String httpDelete(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        String response = "";

        try {
            response = HttpClientUtil.delete(config);
        } catch (HttpProcessException e) {
            failReason = "http post 调用异常，url = " + queryUrl + "\n" + e;
            return response;
            //throw new RuntimeException("http post 调用异常，url = " + queryUrl, e);
        }

        logger.info("result = {}", response);
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    private void setBasicParaToDB(Case aCase, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + caseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000 && key col not null");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    private void saveData(Case aCase, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason()) && this.ENV.equals("ONLINE")) {
            logger.error(aCase.getFailReason());
            dingPush("越秀线上 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
        Assert.assertNull(aCase.getFailReason());
    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();

        alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

        alarmPush.onlineMonitorPvuvAlarm(msg);
        Assert.assertTrue(false);

    }

    private void checkShopListData() {
        if (!StringUtils.isEmpty(this.failReason)) {
            return;
        }

        try {
            //check data of response
            JSONObject jsonRes = JSON.parseObject(this.response);

            JSONObject data = jsonRes.getJSONObject("data");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(data),
                    "data 为空");

        } catch (Exception e) {
            logger.error(e.toString());
            this.failReason = e.toString();
        }


    }


    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeSuite
    public void login() {

        String json = "";
        String path = "";
        if (DEBUG) {
            this.ENV = "DAILY";
            json = this.jsonDaily;
            path = this.loginPathDaily;

        } else if (!StringUtils.isEmpty(this.ENV) && this.ENV.toLowerCase().contains("online")) {
            this.ENV = "ONLINE";
            this.CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_YUEXIU_SALES_OFFICE_ONLINE_SERVICE;
            this.CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/yuexiu-online-test/buildWithParameters?case_name=";

            json = this.jsonOnline;
            path = this.loginPathOnline;
        } else {
            this.ENV = "DAILY";
            json = this.jsonDaily;
            path = this.loginPathDaily;
        }
        qaDbUtil.openConnection();
        Case aCase = new Case();

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        initHttpConfig();
        //String path = "/yuexiu/login";
        //String path = "/yuexiu-login";
        String loginUrl = getIpPort() + path;
        //String json = "{\"username\":\"yuexiu\",\"passwd\":\"e10adc3949ba59abbe56e057f20f883e\"}";

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

        saveData(aCase, caseName, "登录获取authentication");
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
    }

    //    -----------------------------------------------一、登录------------------------------------------------
//    -----------------------------------------------门店选择---------------------------------------------
    @Test(dataProvider = "SHOP_LIST_NOT_NULL")
    public void shopListNOtNull(String key) throws Exception {
        String function = "门店选择>>>";
        String path = "/yuexiu/shop/list";
        String json = "{}";
        String checkColumnName = "list";
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, checkColumnName);

        JSONObject singList = JSON.parseObject(resStr).getJSONObject("data").getJSONArray("list").getJSONObject(0);

        checkKeyValues(function, singList, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }


//    -----------------------------------------------二、实时统计接口--------------------------------------------------------
//--------------------------------------------------2.1 门店实时客流统计-----------------------------------------------------

    //    校验data内数据非空(仅需校验data内数据)
    @Test(dataProvider = "REAL_TIME_SHOP_DATA_NOT_NULL")
    public void realTimeShopDataNotNull(String key) throws Exception {
        String function = "门店实时客流统计>>>";
        String path = REAL_TIME_PREFIX + "shop";
        String json = getRealTimeParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    //验证pv，uv，stay_time均大于0，pv>=uv,stay_time<=600
    @Test(dataProvider = "REAL_TIME_SHOP_DATA_VALIDITY")
    public void realTimeShopDataValidity(String key) throws Exception {
        String function = "门店实时客流统计>>>";
        String path = REAL_TIME_PREFIX + "shop";
        String json = getRealTimeParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkKeyValues(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }


//--------------------------------------2.2 区域实时人数--------------------------------------------------

    @Test(dataProvider = "REAL_TIME_REGION_DATA_NOT_NULL")
    public void realTimeRegionDataNotNull(String key) throws Exception {

        String function = "区域实时人数>>>";
        String path = REAL_TIME_PREFIX + "region";
        String json = getRealTimeParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "REAL_TIME_REGION_REGIONS_NOT_NULL")
    public void realTimeRegionRegionsNotNull(String key) throws Exception {

        String function = "区域实时人数>>>";
        String path = REAL_TIME_PREFIX + "region";
        String json = getRealTimeParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        JSONArray regions = data.getJSONArray("regions");
        for (int i = 0; i < regions.size(); i++) {
            JSONObject jsonObject = regions.getJSONObject(i);
            checkDeepKeyNotNull(function, jsonObject, key);
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "REAL_TIME_REGION_REGIONS_VALIDITY")
    public void realTimeRegionRegionsValidity(String key) throws Exception {

        String function = "区域实时人数>>>";
        String path = REAL_TIME_PREFIX + "region";
        String json = getRealTimeParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");
        JSONArray regions = data.getJSONArray("regions");
        for (int i = 0; i < regions.size(); i++) {
            JSONObject jsonObject = regions.getJSONObject(i);
            checkDeepKeyValidity(function, jsonObject, key);
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key);
    }


//    --------------------------------------------2.3 全场累计客流---------------------------------

//    @Test(dataProvider = "REAL_TIME_PERSONS_ACCUMULATED_NOT_NULL")
    public void realTimePersonsAccumulatedDataNotNull(String key) throws Exception {

        String function = "全场累计客流>>>";

        String path = REAL_TIME_PREFIX + "persons/accumulated";
        String json = getRealTimeParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "REAL_TIME_AGE_GENDER_DISTRIBUTION_NOT_NULL")
    public void realTimeAgeGenderDistribution(String key) throws Exception {

        String function = "全场年龄性别分布>>>";

        String path = REAL_TIME_PREFIX + "age-gender/distribution";
        String json = getRealTimeParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "REAL_TIME_CUSTOMER_TYPE_DISTRIBUTION_NOT_NULL")
    public void realTimeCustomerTypeDistribution(String key) throws Exception {

        String function = "实时客流身份分布>>>";

        String path = REAL_TIME_PREFIX + "customer-type/distribution";
        String json = getRealTimeParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "REAL_TIME_ENTRANCE_RANK_NOT_NULL")
    public void realTimeEntranceRank(String key) throws Exception {

        String function = "实时出入口客流流量排行>>>";

        String path = REAL_TIME_PREFIX + "entrance/rank";
        String json = getRealTimeParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "REAL_TIME_REGION_THERMAL_MAP_NOT_NULL")
    public void realTimeRegionThermalMap(String key) throws Exception {

        String function = "实时热力图>>>";

        String path = REAL_TIME_PREFIX + "region/thermal_map";
        String json = getRealTimeParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "HISTORY_SHOP_NOT_NULL")
    public void historyShop(String key) throws Exception {

        String function = "门店历史客流统计>>>";

        String path = HISTORY_PREFIX + "shop";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "HISTORY_REGION_NOT_NULL")
    public void historyRegion(String key) throws Exception {

        String function = "区域历史人数>>>";

        String path = HISTORY_PREFIX + "region";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "HISTORY_PERSONS_ACCUMULATED_NOT_NULL")
    public void historyPersonsAccumulated(String key) throws Exception {

        String function = "历史累计客流>>>";

        String path = HISTORY_PREFIX + "persons/accumulated";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "HISTORY_AGE_GENDER_DISTRIBUTION_NOT_NULL")
    public void historyAgeGenderDistribution(String key) throws Exception {

        String function = "历史全场客流年龄/性别分布>>>";

        String path = HISTORY_PREFIX + "age-gender/distribution";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "HISTORY_CUSTOMER_TYPE_DISTRIBUTION_NOT_NULL")
    public void historyCustomerTypeDistribution(String key) throws Exception {

        String function = "历史客流身份分布";

        String path = HISTORY_PREFIX + "customer-type/distribution";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "HISTORY_ENTRANCE_RANK_NOT_NULL")
    public void historyEntranceRank(String key) throws Exception {

        String function = "历史出入口客流量排行>>>";

        String path = HISTORY_PREFIX + "entrance/rank";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "HISTORY_REGION_CYCLE_NOT_NULL")
    public void historyRegionCycle(String key) throws Exception {

        String function = "区域历史人数环比>>>";

        String path = HISTORY_PREFIX + "region/cycle";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "REGION_MOVING_DIRECTION_NOT_NULL")
    public void regionDataMovingDirection(String key) throws Exception {

        String function = "区域单向客流>>>";

        String path = REGION_DATA_PREFIX + "moving-direction";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "REGION_ENTER_RANK_NOT_NULL")
    public void regionDataEnterRank(String key) throws Exception {

        String function = "客流进入排行>>>";

        String path = REGION_DATA_PREFIX + "enter/rank";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "REGION_CROSS_DATA_NOT_NULL")
    public void regionDataCrossData(String key) throws Exception {

        String function = "区域交叉客流>>>";

        String path = REGION_DATA_PREFIX + "cross-data";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "REGION_MOVE_LINE_RANK_NOT_NULL")
    public void regionDataMoveLineRank(String key) throws Exception {

        String function = "热门动线排行>>>";

        String path = REGION_DATA_PREFIX + "move-line/rank";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "CUSTOMER_TRACE_NOT_NULL")
    public void customerDataTrace(String key) throws Exception {

        String function = "区域人物轨迹>>>";

        String path = CUSTOMER_DATA_PREFIX + "trace";
        String json = getCustomerParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "LIST_NOT_NULL")
    public void customerDataLabels(String key) throws Exception {

        String function = "查询顾客标签列表>>>";

        String path = CUSTOMER_DATA_PREFIX + "labels";
        String json = getCustomerParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }


    public String postCustomerDataDetail() throws IOException {
        String url = "http://123.57.114.36" + CUSTOMER_DATA_PREFIX + "detail";

        String imagePath = "src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\liao.jpg";
        imagePath = imagePath.replace("\\", File.separator);

        File file = new File(imagePath);

        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("shop_id", String.valueOf(SHOP_ID_DAILY));
        builder.addFormDataPart("face_data", file.getName(),
                RequestBody.create(MediaType.parse("application/octet-stream"), file));

        MultipartBody multipartBody = builder.build();

        Request request = new Request.Builder().
                url(url).
                addHeader("authorization", authorization).
                post(multipartBody).
                build();

        Response res = client.newCall(request).execute();
        response = res.body().string();

        logger.info("response: {}", response);

        return response;
    }

//    ---------------------------------------区域单人动线模块---------------------------------------------------

//    -----------------------------查询人物信息------------------------------------------------------

    @Test(dataProvider = "CUSTOMER_DATA_DETAIL_NOT_NULL")
    public void testCustomerDataDetailNotNull(String key) throws Exception {

        String function = "查询顾客信息>>>";

        String resStr = postCustomerDataDetail();
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

    @Test(dataProvider = "CUSTOMER_DATA_KEY_VALUES")
    public void customerDataDetailFirstLast(String key) throws Exception {

        String function = "查询顾客信息>>>";

        String resStr = postCustomerDataDetail();
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkKeyValues(function, data, key);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName + "-" + key, function + "校验" + key + "非空");
    }

//    ---------------------------------------------区域人物轨迹---------------------------------------------

    private String getCustomerTraceJson() {

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"customer_id\":\"a16a619f-20e9-4902-9d91-e6100f21\",\n" +
                        "    \"start_time\":\"2019-10-22\",\n" +
                        "    \"end_time\":\"2019-10-22\"\n" +
                        "}";

        return json;
    }

    @Test()
    public void testCustomerTraceNotNull() throws Exception {
        String function = "区域人物轨迹>>>";
        String path = CUSTOMER_DATA_PREFIX + "trace";

        String[] checkColumnNames1 = {"last_query_time", "regions", "moving_lines", "map_url",
                "traces", "region_turn_points"};

        String json = getCustomerTraceJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, checkColumnNames1);

        JSONArray regions = data.getJSONArray("regions");
        for (int i = 0; i < regions.size(); i++) {
            JSONObject singleRegion = regions.getJSONObject(i);
            String[] checkColumnNames2 = {"region_id", "location", "region_name"};
            checkNotNull(function, singleRegion, checkColumnNames2);
            JSONArray location = singleRegion.getJSONArray("location");
            for (int i1 = 0; i1 < location.size(); i1++) {
                JSONObject point = location.getJSONObject(i1);
                String[] keyKey = {"x>=0", "y>=0", "x<=1", "x<=1"};

                checkKeyValues(function, point, keyKey);
            }
        }

        JSONArray movingLines = data.getJSONArray("moving_lines");
        for (int i = 0; i < movingLines.size(); i++) {
            JSONObject line = movingLines.getJSONObject(i);
            String[] keys = {"move_times", "source", "target"};
            checkNotNull(function, line, keys);
        }

        JSONArray traces = data.getJSONArray("traces");
        for (int i = 0; i < traces.size(); i++) {
            JSONObject trace = traces.getJSONObject(i);
            String[] keys = {"location", "time", "face_url"};
            checkNotNull(function, trace, keys);
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "区域人物轨迹-验证返回数据非空>>>");
    }

    @Test
    public void singleMoveLineRank() throws Exception {
        String function = "区域单人动线-该人物累计动线分析---";
        String path = CUSTOMER_DATA_PREFIX + "trace";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"customer_id\":\"a16a619f-20e9-4902-9d91-e6100f21\",\n" +
                        "    \"start_time\":\"2019-10-21\",\n" +
                        "    \"end_time\":\"2019-10-21\"\n" +
                        "}";

        String res = httpPost(path, json, StatusCode.SUCCESS);

        JSONArray moveingLines = JSON.parseObject(res).getJSONObject("data").getJSONArray("moving_lines");
        checkRank(moveingLines, "move_times", function);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "区域交叉客流-热门动线排行>>>");
    }

    @Test
    public void customerTraceNotNulll() throws Exception {
        String function = "区域人物轨迹---";
        String path = CUSTOMER_DATA_PREFIX + "trace";

        String[] checkColumnNames1 = {"last_query_time", "regions", "moving_lines", "map_url",
                "traces", "region_turn_points"};

        String json = getCustomerTraceJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, checkColumnNames1);

        JSONArray regions = data.getJSONArray("regions");
        for (int i = 0; i < regions.size(); i++) {
            JSONObject singleRegion = regions.getJSONObject(i);
            String[] checkColumnNames2 = {"region_id", "location", "region_name"};
            checkNotNull(function, singleRegion, checkColumnNames2);
            JSONArray location = singleRegion.getJSONArray("location");
            for (int i1 = 0; i1 < location.size(); i1++) {
                JSONObject point = location.getJSONObject(i1);
                String[] keyKey = {"x>=0", "y>=0", "x<=1", "x<=1"};

//                checkKeyValues(point, keyKey, function);
            }
        }

        JSONArray movingLines = data.getJSONArray("moving_lines");
        for (int i = 0; i < movingLines.size(); i++) {
            JSONObject line = movingLines.getJSONObject(i);
            String[] keys = {"move_times", "source", "target"};
            checkNotNull(function, line, keys);
        }

        JSONArray traces = data.getJSONArray("traces");
        for (int i = 0; i < traces.size(); i++) {
            JSONObject trace = traces.getJSONObject(i);
            String[] keys = {"location", "time", "face_url"};
            checkNotNull(function, trace, keys);
            JSONArray location = trace.getJSONArray("location");
            for (int i1 = 0; i1 < location.size(); i1++) {
                JSONObject point = location.getJSONObject(i1);
                String[] keyKey = {"x>=0", "y>=0", "x<=1", "y<=1"};

//                checkKeyValues(point, keyKey, function);
            }
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "区域人物轨迹-验证返回数据非空>>>");
    }


    @Test
    public void customerTrace() throws Exception {
        String function = "区域人物轨迹---";
        String path = CUSTOMER_DATA_PREFIX + "trace";

        String[] checkColumnNames1 = {"last_query_time", "regions", "moving_lines", "map_url",
                "traces", "region_turn_points"};

        String json = getCustomerTraceJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(function, data, checkColumnNames1);

        JSONArray regions = data.getJSONArray("regions");
        for (int i = 0; i < regions.size(); i++) {
            JSONObject singleRegion = regions.getJSONObject(i);
            String[] checkColumnNames2 = {"region_id", "location", "region_name"};
            checkNotNull(function, singleRegion, checkColumnNames2);
            JSONArray location = singleRegion.getJSONArray("location");
            for (int i1 = 0; i1 < location.size(); i1++) {
                JSONObject point = location.getJSONObject(i1);
                String[] keyKey = {"x>=0", "y>=0", "x<=1", "x<=1"};

//                checkKeyValues(point, keyKey, function);
            }
        }

        JSONArray movingLines = data.getJSONArray("moving_lines");
        for (int i = 0; i < movingLines.size(); i++) {
            JSONObject line = movingLines.getJSONObject(i);
            String[] keys = {"move_times", "source", "target"};
            checkNotNull(function, line, keys);
        }

        JSONArray traces = data.getJSONArray("traces");
        for (int i = 0; i < traces.size(); i++) {
            JSONObject trace = traces.getJSONObject(i);
            String[] keys = {"location", "time", "face_url"};
            checkNotNull(function, trace, keys);
            JSONArray location = trace.getJSONArray("location");
            for (int i1 = 0; i1 < location.size(); i1++) {
                JSONObject point = location.getJSONObject(i1);
                String[] keyKey = {"x>=0", "y>=0", "x<=1", "y<=1"};

//                checkKeyValues(point, keyKey, function);
            }
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "区域人物轨迹-验证返回数据非空>>>");
    }

    public String addLabel(String labelName, int expectCode) throws Exception {
        String function = "添加标签---";
        String path = CUSTOMER_DATA_PREFIX + "label";

        String json =
                "{\n" +
                        "    \"label\":\"" + labelName + "\",\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"customer_id\":\"a16a619f-20e9-4902-9d91-e6100f21\"\n" +
                        "}";

        String resStr = httpPost(path, json, expectCode);

        return function;
    }

    public void deleteLabel(String labelId) throws Exception {
        String function = "删除标签---";
        String path = CUSTOMER_DATA_PREFIX + "label/" + labelId;

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"customer_id\":\"a16a619f-20e9-4902-9d91-e6100f21\"\n" +
                        "}";

        httpDelete(path, json);
    }

    public String listLabels() throws Exception {
        String function = "标签列表---";
        String path = CUSTOMER_DATA_PREFIX + "labels";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"customer_id\":\"a16a619f-20e9-4902-9d91-e6100f21\"\n" +
                        "}";

        return httpPost(path, json, StatusCode.SUCCESS);
    }

    public String checkIsExistByListLabel(String response, String labelName, boolean isExist) {
        JSONArray labels = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");
        String labelId = "";

        boolean isExistRes = false;
        for (int i = 0; i < labels.size(); i++) {
            JSONObject label = labels.getJSONObject(i);
            String labelNameRes = label.getString("label");
            if (labelName.equals(labelNameRes)) {
                isExistRes = true;
                labelId = label.getString("id");
            }
        }

        Assert.assertEquals(isExistRes, isExist, "期待该label存在：" + isExist + ",实际：" + isExistRes);

        return labelId;
    }

    public String getLabelIdByList(String response, String labelName) {
        return checkIsExistByListLabel(response, labelName, true);
    }

    @Test
    public void addDeleteLabel() throws Exception {
        String function = "验证添加删除标签是否成功---";
        String path = CUSTOMER_DATA_PREFIX + "trace";

        String label1 = "addDeleteLabel-1";
        String label2 = "addDeleteLabel-2";

//        1、添加标签
        addLabel(label1, StatusCode.SUCCESS);
        addLabel(label2, StatusCode.SUCCESS);

//        2、标签列表
        String response = listLabels();
        String labelId1 = getLabelIdByList(response, label1);
        checkIsExistByListLabel(response, label1, true);
        checkIsExistByListLabel(response, label2, true);

//        3、删除标签
        deleteLabel(labelId1);

//        4、标签列表
        response = listLabels();
        checkIsExistByListLabel(response, label1, false);
        checkIsExistByListLabel(response, label2, true);

        String labelId2 = getLabelIdByList(response, label2);
        deleteLabel(labelId2);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "区域人物轨迹-验证返回数据非空>>>");
    }

    @Test
    public void addLabelLen7() throws Exception {
        String function = "验证添加删除标签是否成功---";
        String path = CUSTOMER_DATA_PREFIX + "trace";

        String label = "addLabelLen7-1";

//        1、添加标签
        addLabel(label, StatusCode.SUCCESS);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "区域人物轨迹-验证返回数据非空>>>");
    }

    private String genRegionStatisticsJson() throws Exception {

        long startTimeL = dateTimeUtil.initLastWeek();
        String startTime = dateTimeUtil.timestampToDate("yyyy-MM-dd", startTimeL);
        long endTimeL = System.currentTimeMillis();
        String endTime = dateTimeUtil.timestampToDate("yyyy-MM-dd", endTimeL);

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID_DAILY + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\"\n" +
                        "}";

        return json;
    }

    @Test
    public void moveDirectionRate() throws Exception {
        String function = "标签列表---";
        String path = REGION_DATA_PREFIX + "moving-direction";

        String json = genRegionStatisticsJson();

        String res = httpPost(path, json, StatusCode.SUCCESS);

        checkDirectionRate(res);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "区域人物轨迹-验证返回数据非空>>>");
    }

    private void checkDirectionRate(String res) throws Exception {
        JSONArray relations = JSON.parseObject(res).getJSONObject("data").getJSONArray("relations");

        for (int i = 0; i < relations.size(); i++) {
            JSONObject relation = relations.getJSONObject(i);
            JSONArray directionList = relation.getJSONArray("direction_list");
            if (directionList != null) {
                int[] nums = new int[directionList.size()];
                String[] ratios = new String[directionList.size()];
                String[] regionIds = new String[directionList.size()];
                int total = 0;
                for (int j = 0; j < directionList.size(); j++) {
                    JSONObject direction = directionList.getJSONObject(j);
                    nums[j] = direction.getInteger("num");
                    String retioStr = direction.getString("ratio");
                    ratios[j] = retioStr.substring(0, retioStr.length() - 1);
                    regionIds[j] = direction.getString("region_id");
                    total += nums[j];
                }

                for (int k = 0; k < nums.length; k++) {
                    double actual = ((double) nums[k] / (double) total) * (double) 100;
                    DecimalFormat df = new DecimalFormat("#.00");
                    String actualStr = df.format(actual);

                    if (!ratios[k].equals(actualStr)) {
                        throw new Exception("region_id: " + regionIds[k] + " 对应的区域动线比例错误！返回：" + ratios[k] + ",实际：" + actualStr);
                    }
                }
            }
        }
    }

    @Test
    public void historyAgeGenderRate() throws Exception {
        String function = "区域单向客流-年龄性别分布---";
        String path = HISTORY_PREFIX + "age-gender/distribution";

        String json = genRegionStatisticsJson();

        String res = httpPost(path, json, StatusCode.SUCCESS);

        checkAgeGenderRate(res, function);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "区域单向客流-年龄性别分布>>>");
    }

    private void checkAgeGenderRate(String res, String function) throws Exception {
        JSONArray list = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");

        if (list == null || list.size() != 12) {
            throw new Exception("年龄性别分布的类别为空，或者是不是12个分类。");
        }

        String[] ageGrp = new String[12];
        String[] percents = new String[12];
        int[] nums = new int[12];
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String percent = single.getString("percent");
            percents[i] = percent.substring(0, percent.length() - 1);
            nums[i] = single.getInteger("num");
            ageGrp[i] = single.getString("age_group");
            total += nums[i];
        }

        for (int i = 0; i < nums.length; i++) {
            double actual = ((double) nums[i] / (double) total) * (double) 100;
            DecimalFormat df = new DecimalFormat("0.00");
            String actualStr = df.format(actual);

            if (!percents[i].equals(actualStr)) {
                throw new Exception(function + "age_group: " + ageGrp[i] + " 对应的年龄性别比例错误！返回：" + percents[i] + ",实际：" + actualStr);
            }
        }
    }

    @Test
    public void historyCustomerTypeRate() throws Exception {
        String function = "区域单向客流-客流身份分布---";
        String path = HISTORY_PREFIX + "customer-type/distribution";

        String json = genRegionStatisticsJson();

        String res = httpPost(path, json, StatusCode.SUCCESS);

        checkCustomerTypeRate(res, function);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "区域单向客流-客流身份分布>>>");
    }

    private void checkCustomerTypeRate(String res, String function) throws Exception {
        JSONArray list = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");

        if (list == null || list.size() != 4) {
            throw new Exception("客流身份分布的类别为空，或者不是4个分类。");
        }

        String[] typeNames = {"高活跃顾客", "流失客", "低活跃顾客", "新客"};

        String[] typeNamesRes = new String[4];
        String[] percentageStrs = new String[4];
        int[] nums = new int[4];
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String percentageStr = single.getString("percentage_str");
            percentageStrs[i] = percentageStr.substring(0, percentageStr.length() - 1);
            nums[i] = single.getInteger("num");
            typeNamesRes[i] = single.getString("type_name");
            total += nums[i];
        }

        Assert.assertEquals(typeNamesRes, typeNames, "返回的顾客类型与期待的不相符--返回：" +
                Arrays.toString(typeNamesRes) + ",期待：" + Arrays.toString(typeNames));

        for (int i = 0; i < nums.length; i++) {
            double actual = ((double) nums[i] / (double) total) * (double) 100;
            DecimalFormat df = new DecimalFormat("0.00");
            String actualStr = df.format(actual);

            if (!percentageStrs[i].equals(actualStr)) {
                throw new Exception(function + "type_name: " + typeNamesRes[i] + " 对应的客流身份比例错误！返回：" + percentageStrs[i] + ",实际：" + actualStr);
            }
        }
    }

    @Test
    public void regionEnterRank() throws Exception {
        String function = "区域单向客流-客流进入区域排行---";
        String path = REGION_DATA_PREFIX + "enter/rank";

        String json = genRegionStatisticsJson();

        String res = httpPost(path, json, StatusCode.SUCCESS);

        JSONArray list = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");

        checkRank(list, "num", function);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "区域单向客流-客流身份分布>>>");
    }

    private void checkRank(JSONArray ja, String key, String function) throws Exception {

        if (!(ja == null || ja.size() == 0)) {

            int[] nums = new int[ja.size()];

            for (int i = 0; i < ja.size(); i++) {
                JSONObject single = ja.getJSONObject(i);

                nums[i] = single.getInteger(key);

                if (i > 0) {
                    if (nums[i - 1] < nums[i]) {
                        throw new Exception(function + "没有正确排序！目前排序：" + Arrays.toString(nums));
                    }
                }
            }
        }
    }

    @Test
    public void regionMoveLineRank() throws Exception {
        String function = "区域单向客流-客流进入区域排行---";
        String path = REGION_DATA_PREFIX + "move-line/rank";

        String json = genRegionStatisticsJson();

        String res = httpPost(path, json, StatusCode.SUCCESS);

        JSONArray list = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");

        checkRank(list, "num", function);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "区域交叉客流-热门动线排行>>>");
    }

    private void checkKeyValue(String function, JSONObject jo, String key, String value, boolean expectExactValue) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        String valueRes = jo.getString(key);

        if (expectExactValue) {
            Assert.assertEquals(valueRes, value, key + "字段值不相符：列表返回：" + valueRes + ", 实际：" + value);
        } else {
            if (valueRes == null || "".equals(valueRes)) {
                throw new Exception(function + "---" + key + "字段值为空！");
            }
        }
    }

    private void checkKeyMoreOrEqualValue(String function, JSONObject jo, String key, double value) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        double valueRes = jo.getDouble(key);


        if (!(valueRes >= value)) {
            throw new Exception(key + "字段，应该>=" + value + "实际返回的value为：" + value);
        }
    }

    private void checkKeyLessOrEqualValue(String function, JSONObject jo, String key, double value) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        double valueRes = jo.getDouble(key);

        if (!(valueRes <= value)) {
            throw new Exception(key + "字段，应该>=" + value + "实际返回的value为：" + value);
        }
    }

    private void checkKeyLessOrEqualKey(JSONObject jo, String key1, String key2, String function) throws Exception {

        checkNotNull(function, jo, key1);
        checkNotNull(function, jo, key2);

        double value1 = jo.getDouble(key1);
        double value2 = jo.getDouble(key2);

        //防止取值方面出现问题，value为空的时候也是不符合的
        if (!(value1 <= value2)) {
            throw new Exception(function + key1 + "=" + value1 + "，应该<=" + key2 + "=" + value2);
        }
    }

    private void checkKeyValues(String function, JSONObject jo, String... keyValues) throws Exception {

        for (String keyValue : keyValues) {
            //            注意其他判断与=判断的顺序
            if (keyValue.contains("[<=]")) {
                String key1 = keyValue.substring(0, keyValue.indexOf("["));
                String key2 = keyValue.substring(keyValue.indexOf("]") + 1);
                checkKeyLessOrEqualKey(jo, key1, key2, function);
            } else if (keyValue.contains(">=")) {
                String key = keyValue.substring(0, keyValue.indexOf(">"));
                String value = keyValue.substring(keyValue.indexOf("=") + 1);
                checkKeyMoreOrEqualValue(function, jo, key, Double.valueOf(value));
            } else if (keyValue.contains("<=")) {
                String key = keyValue.substring(0, keyValue.indexOf("<"));
                String value = keyValue.substring(keyValue.indexOf("=") + 1);
                checkKeyLessOrEqualValue(function, jo, key, Double.valueOf(value));
            } else if (keyValue.contains("=")) {
                String key = keyValue.substring(0, keyValue.indexOf("="));
                String value = keyValue.substring(keyValue.indexOf("=") + 1);

                checkKeyValue(function, jo, key, value, true);

            }
        }
    }

    private void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);
        int code = resJo.getInteger("code");
        message = resJo.getString("message");

        if (expect != code) {
            throw new Exception(message + " expect code: " + expect + ",actual: " + code);
        }
    }


//    ---------------------------------------------门店实时客流统计------------------------------------------

    @DataProvider(name = "REAL_TIME_SHOP_DATA_NOT_NULL")
    private static Object[] realTimeShopNotNull() {
        return new Object[]{
                "uv",
                "pv",
                "stay_time"
        };
    }

    @DataProvider(name = "REAL_TIME_SHOP_DATA_VALIDITY")
    private static Object[] realTimeShopDataValidity() {
        return new Object[]{
                "uv>=0",
                "pv>=0",
                "stay_time>=0",
                "uv[<=]pv",
                "stay_time<=600"

        };
    }


    @DataProvider(name = "CUSTOMER_DATA_KEY_VALUES")
    private static Object[] customerDataDS() {
        return new Object[]{
                "first_appear_time[<=]last_appear_time",
//                "stay_time_per_times<=300"
        };
    }

    @DataProvider(name = "SHOP_LIST_NOT_NULL")
    private static Object[] shopListNotNull() {
        return new Object[]{
                "shop_id",
                "shop_name",
                "icon"
        };
    }

//---------------------------------------------区域实时人数--------------------------------------------

    @DataProvider(name = "REAL_TIME_REGION_DATA_NOT_NULL")
    private static Object[] realTimeRegionDataNotNull() {
        return new Object[]{
                "regions",
                "map_url",
        };
    }

    @DataProvider(name = "REAL_TIME_REGION_REGIONS_NOT_NULL")
    private static Object[] realTimeRegionNotNull() {
        return new Object[]{
                "{stay_num}-num",
                "{stay_num}-rank",
                "location",
                "region_name",
                "statistics",
                "{statistics}-uv",
                "{statistics}-pv",
                "{statistics}-stay_time",
        };
    }

    @DataProvider(name = "REAL_TIME_REGION_REGIONS_VALIDITY")
    private static Object[] realTimeRegionValidity() {
        return new Object[]{
                "{stay_num}-num>=0",
                "{stay_num}-rank>=1",
                "{statistics}-pv>=0",
                "{statistics}-uv>=0",
                "{statistics}-stay_time>=0",
                "[location]-x>=0",
                "[location]-x<=1",
                "[location]-y>=0",
                "[location]-y<=1",
                "{statistics}-uv>=0",
                "{statistics}-pv>=0",
                "{statistics}-uv[<=]pv",
                "{statistics}-stay_time>=0",
                "{statistics}-stay_time<=600",
        };
    }


    @DataProvider(name = "REAL_TIME_PERSONS_ACCUMULATED_NOT_NULL")
    private static Object[] realTimePersonsAccumulatedNotNull() {
        return new Object[]{
                "[statistics_data]-present_cycle",
                "[statistics_data]-chain_ratio",
                "[statistics_data]-last_cycle",
                "[statistics_data]-label",
                "last_statistics_time"
        };
    }

    @DataProvider(name = "REAL_TIME_AGE_GENDER_DISTRIBUTION_NOT_NULL")
    private static Object[] realTimeAgeGenderDIstributionNotNull() {
        return new Object[]{
                "list"
        };
    }

    @DataProvider(name = "REAL_TIME_CUSTOMER_TYPE_DISTRIBUTION_NOT_NULL")
    private static Object[] realTimeCustomerTypeDistributionNotNull() {
        return new Object[]{
                "list"
        };
    }

    @DataProvider(name = "REAL_TIME_ENTRANCE_RANK_NOT_NULL")
    private static Object[] realTimeEntranceRankNotNull() {
        return new Object[]{
                "list"
        };
    }

    @DataProvider(name = "REAL_TIME_REGION_THERMAL_MAP_NOT_NULL")
    private static Object[] realTimeRegionThermalMapNotNull() {
        return new Object[]{
                "regions",
                "map_url",
                "thermal_map"
        };
    }

    @DataProvider(name = "HISTORY_SHOP_NOT_NULL")
    private static Object[] historyShopNotNull() {
        return new Object[]{
                "uv",
                "pv",
                "stay_time"
        };
    }

    @DataProvider(name = "HISTORY_REGION_NOT_NULL")
    private static Object[] historyRegionNotNull() {
        return new Object[]{
                "regions",
                "map_url"
        };
    }

    @DataProvider(name = "HISTORY_PERSONS_ACCUMULATED_NOT_NULL")
    private static Object[] historyPersonsAccumulatedNotNull() {
        return new Object[]{
                "last_statistics_time",
                "statistics_data"
        };
    }

    @DataProvider(name = "HISTORY_AGE_GENDER_DISTRIBUTION_NOT_NULL")
    private static Object[] historyAgeGenderDistributionNotNull() {
        return new Object[]{
                "list"
        };
    }

    @DataProvider(name = "HISTORY_CUSTOMER_TYPE_DISTRIBUTION_NOT_NULL")
    private static Object[] historyCustomerTypeDistributionNotNull() {
        return new Object[]{
                "list"
        };
    }

    @DataProvider(name = "HISTORY_ENTRANCE_RANK_NOT_NULL")
    private static Object[] historyEntranceRankNotNull() {
        return new Object[]{
                "list"
        };
    }

    @DataProvider(name = "HISTORY_REGION_CYCLE_NOT_NULL")
    private static Object[] historyRegionCycleNotNull() {
        return new Object[]{
                "list"
        };
    }

    @DataProvider(name = "REGION_MOVING_DIRECTION_NOT_NULL")
    private static Object[] historyRegionMovingLineNotNull() {
        return new Object[]{
                "regions",
                "relations"
        };
    }

    @DataProvider(name = "REGION_ENTER_RANK_NOT_NULL")
    private static Object[] regionEnterRankNotNull() {
        return new Object[]{
                "list"
        };
    }

    @DataProvider(name = "REGION_CROSS_DATA_NOT_NULL")
    private static Object[] regionCrossDataNotNull() {
        return new Object[]{
                "regions",
                "relations"
        };
    }

    @DataProvider(name = "REGION_MOVE_LINE_RANK_NOT_NULL")
    private static Object[] regionMoveLineRankNotNull() {
        return new Object[]{
                "list"
        };
    }

    @DataProvider(name = "CUSTOMER_TRACE_NOT_NULL")
    private static Object[] customerTraceNotNull() {
        return new Object[]{
                "regions",
                "traces",
                "moving_lines",
                "map_url"
        };
    }

    @DataProvider(name = "LIST_NOT_NULL")
    private static Object[] listNotNull() {
        return new Object[]{
                "list"
        };
    }

    @DataProvider(name = "CUSTOMER_DATA_DETAIL_NOT_NULL")
    private static Object[] customerDataDetailNotNull() {
        return new Object[]{
                "first_appear_time",
                "last_appear_time",
                "stay_time_per_times",
                "sex",
                "face_url",
                "customer_type_name",
                "customer_id",
                "age",
                "shop_id"
        };
    }


}
