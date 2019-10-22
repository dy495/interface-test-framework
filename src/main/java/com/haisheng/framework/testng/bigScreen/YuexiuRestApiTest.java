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
import java.time.LocalDate;

/**
 * @author : xiezhidong
 * @date :  2019/10/12  14:55
 */

public class YuexiuRestApiTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private DateTimeUtil dt = new DateTimeUtil();
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
//    private String ENV = "";
    private String ENV = System.getProperty("ENV");
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

    private void checkNotNull(JSONObject jo, String... checkColumnNames) {

        for (String checkColumn : checkColumnNames) {
            Object column = jo.get(checkColumn);
            logger.info("{} : {}", checkColumn, column);
            if (column == null) {
                failReason = "result does not contains column " + checkColumn;
                return;
                //throw new RuntimeException("result does not contains column " + checkColumn);
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

    @Test
    public void testShopList() throws Exception {
        String function = "门店选择---";
        String path = "/yuexiu/shop/list";
        String json = "{}";
        String checkColumnNames = "list";
        String[] checkKeyValues = {"shop_id!2606", "shop_name!null", "icon!null"};
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnNames);

        JSONObject singList = JSON.parseObject(resStr).getJSONObject("data").getJSONArray("list").getJSONObject(0);

        checkKeyValues(singList, checkKeyValues, function);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "门店选择-数据验证---");
    }

    @Test
    public void testRealTimeShopInfo() throws Exception {
        String path = REAL_TIME_PREFIX + "shop";
        String json = getRealTimeParamJson();
        String[] checkColumnNames = {"uv", "pv", "stay_time"};
        String[] checkKeyLongValues = {"pv>=0", "uv>=0", "stay_time>=0"};
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "获取实时shop名单，校验 pv uv stay_time 非空");
    }

    @Test
    public void testRealTimeRegionInfo() throws Exception {

        String path = REAL_TIME_PREFIX + "region";
        String json = getRealTimeParamJson();
        String[] checkColumnNames = {"regions", "map_url"};
        String[] checkKeyValues = {"region_id", "region_name"};
        String[] checkKeyLongValues = {"pv>=0", "uv>=0", "stay_time>=0"};
        String resStr = httpPost(path, json, StatusCode.SUCCESS);
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "获取实时region信息，校验 regions map_url非空");
    }

    @Test
    public void testRealTimePersonsAccumulated() throws Exception {

        String path = REAL_TIME_PREFIX + "persons/accumulated";
        String checkColumnNames = "statistics_data";
        String json = getRealTimeParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试实时接口persons/accumulated，校验 statistics_data 非空");
    }

    @Test
    public void testRealTimeAgeGenderDistribution() throws Exception {

        String path = REAL_TIME_PREFIX + "age-gender/distribution";
        String checkColumnName = "list";
        String json = getRealTimeParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试实时接口age-gender/distribution，校验 list 非空");
    }

    @Test
    public void testRealTimeCustomerTypeDistribution() throws Exception {

        String path = REAL_TIME_PREFIX + "customer-type/distribution";
        String checkColumnName = "list";
        String json = getRealTimeParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试实时接口customer-type/distribution，校验 list 非空");
    }

    @Test
    public void testRealTimeEntranceRank() throws Exception {

        String path = REAL_TIME_PREFIX + "entrance/rank";
        String checkColumnName = "list";
        String json = getRealTimeParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试实时接口entrance/rank，校验 list 非空");
    }

    @Test
    public void testRealTimeRegionThermalMap() throws Exception {

        String path = REAL_TIME_PREFIX + "region/thermal_map";
        String json = getRealTimeParamJson();
        String[] checkColumnNames = {"regions", "map_url", "thermal_map"};
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试实时接口region/thermal_map，校验 regions map_url thermal_map 非空");
    }

    @Test
    public void testHistoryShop() throws Exception {

        String path = HISTORY_PREFIX + "shop";
        String json = getHistoryParamJson();
        String[] checkColumnNames = {"uv", "pv", "stay_time"};
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试历史门店信息，校验 pv uv stay_time 非空");
    }

    @Test
    public void testHistoryRegion() throws Exception {

        String path = HISTORY_PREFIX + "region";
        String json = getHistoryParamJson();
        String[] checkColumnNames = {"regions", "map_url"};
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试历史区域信息，校验 regions map_url 非空");
    }

    @Test
    public void testHistoryPersonsAccumulated() throws Exception {

        String path = HISTORY_PREFIX + "persons/accumulated";
        String checkColumnName = "statistics_data";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试历史接口persons/accumulated，校验 statistics_data 非空");
    }

    @Test
    public void testHistoryAgeGenderDistribution() throws Exception {

        String path = HISTORY_PREFIX + "age-gender/distribution";
        String checkColumnName = "list";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试历史接口age-gender/distribution，校验 list 非空");
    }

    @Test
    public void testHistoryCustomerTypeDistribution() throws Exception {

        String path = HISTORY_PREFIX + "customer-type/distribution";
        String checkColumnName = "list";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试历史接口customer-type/distribution，校验 list 非空");
    }

    @Test
    public void testHistoryEntranceRank() throws Exception {

        String path = HISTORY_PREFIX + "entrance/rank";
        String checkColumnName = "list";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试历史接口entrance/rank，校验 list 非空");
    }

    @Test
    public void testHistoryRegionCycle() throws Exception {

        String path = HISTORY_PREFIX + "region/cycle";
        String checkColumnName = "list";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试历史接口region/cycle，校验 list 非空");
    }

    @Test
    public void testRegionDataMovingDirection() throws Exception {

        String path = REGION_DATA_PREFIX + "moving-direction";
        String[] checkColumnNames = {"regions", "relations"};
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试接口moving-direction，校验 regions relations 非空");
    }

    @Test
    public void testRegionDataEnterRank() throws Exception {

        String path = REGION_DATA_PREFIX + "enter/rank";
        String checkColumnName = "list";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试接口enter/rank，校验 list 非空");
    }

    @Test
    public void testRegionDataCrossData() throws Exception {

        String path = REGION_DATA_PREFIX + "cross-data";
        String[] checkColumnNames = {"regions", "relations"};
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试接口cross-data，校验 regions relations 非空");
    }

    @Test
    public void testRegionDataMoveLineRank() throws Exception {

        String path = REGION_DATA_PREFIX + "move-line/rank";
        String checkColumnName = "list";
        String json = getHistoryParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试接口move-line/rank，校验 list 非空");
    }

    @Test
    public void testCustomerDataTrace() throws Exception {

        String path = CUSTOMER_DATA_PREFIX + "trace";
        String[] checkColumnNames = {"regions", "traces", "moving_lines", "map_url"};
        String json = getCustomerParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试接口trace，校验 \"regions\", \"traces\", \"moving_lines\", \"map_url\" 非空");
    }

    @Test
    public void testCustomerDataLabels() throws Exception {

        String path = CUSTOMER_DATA_PREFIX + "labels";
        String[] checkColumnNames = {"list"};
        String json = getCustomerParamJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试接口labels，校验 list 非空");
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

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

//    -----------------------------查询人物信息------------------------------------------------------

    @Test
    public void testCustomerDataDetailNotNull() throws Exception {

        String[] checkColumnNames = {"first_appear_time", "last_appear_time", "stay_time_per_times", "sex",
                "face_url", "customer_type_name", "customer_id", "age", "shop_id"};

        String resStr = postCustomerDataDetail();
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "查询顾客信息-验证返回数据非空>>>");
    }

    @Test
    public void customerDataDetailFirstLast() throws Exception {

        String funtion = "查询顾客信息";

        String[] checkColumnNames = {"first_appear_time[<=]last_appear_time"};

        String resStr = postCustomerDataDetail();
        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkKeyValues(data, checkColumnNames, funtion);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "查询顾客信息-验证返回数据first_appear_time <= last_appear_time>>>");
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


    @Test
    public void testCustomerTraceNotNull() throws Exception {
        String function = "区域人物轨迹---";
        String path = CUSTOMER_DATA_PREFIX + "trace";

        String[] checkColumnNames1 = {"last_query_time", "regions", "moving_lines", "map_url",
                "traces", "region_turn_points"};

        String json = getCustomerTraceJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnNames1);

        JSONArray regions = data.getJSONArray("regions");
        for (int i = 0; i < regions.size(); i++) {
            JSONObject singleRegion = regions.getJSONObject(i);
            String[] checkColumnNames2 = {"region_id", "location", "region_name"};
            checkNotNull(singleRegion, checkColumnNames2);
            JSONArray location = singleRegion.getJSONArray("location");
            for (int i1 = 0; i1 < location.size(); i1++) {
                JSONObject point = location.getJSONObject(i1);
                String[] keyKey = {"x>=0", "y>=0", "x<=1", "x<=1"};

                checkKeyValues(point, keyKey, function);
            }
        }

        JSONArray movingLines = data.getJSONArray("moving_lines");
        for (int i = 0; i < movingLines.size(); i++) {
            JSONObject line = movingLines.getJSONObject(i);
            String[] keys = {"move_times", "source", "target"};
            checkNotNull(line, keys);
        }

        JSONArray traces = data.getJSONArray("traces");
        for (int i = 0; i < traces.size(); i++) {
            JSONObject trace = traces.getJSONObject(i);
            String[] keys = {"location", "time", "face_url"};
            checkNotNull(trace, keys);
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "区域人物轨迹-验证返回数据非空>>>");
    }

    @Test
    public void testCustomerTrace() throws Exception {
        String function = "区域人物轨迹---";
        String path = CUSTOMER_DATA_PREFIX + "trace";

        String[] checkColumnNames1 = {"last_query_time", "regions", "moving_lines", "map_url",
                "traces", "region_turn_points"};

        String json = getCustomerTraceJson();
        String resStr = httpPost(path, json, StatusCode.SUCCESS);

        JSONObject data = JSON.parseObject(resStr).getJSONObject("data");

        checkNotNull(data, checkColumnNames1);

        JSONArray regions = data.getJSONArray("regions");
        for (int i = 0; i < regions.size(); i++) {
            JSONObject singleRegion = regions.getJSONObject(i);
            String[] checkColumnNames2 = {"region_id", "location", "region_name"};
            checkNotNull(singleRegion, checkColumnNames2);
            JSONArray location = singleRegion.getJSONArray("location");
            for (int i1 = 0; i1 < location.size(); i1++) {
                JSONObject point = location.getJSONObject(i1);
                String[] keyKey = {"x>=0", "y>=0", "x<=1", "x<=1"};

                checkKeyValues(point, keyKey, function);
            }
        }

        JSONArray movingLines = data.getJSONArray("moving_lines");
        for (int i = 0; i < movingLines.size(); i++) {
            JSONObject line = movingLines.getJSONObject(i);
            String[] keys = {"move_times", "source", "target"};
            checkNotNull(line, keys);
        }

        JSONArray traces = data.getJSONArray("traces");
        for (int i = 0; i < traces.size(); i++) {
            JSONObject trace = traces.getJSONObject(i);
            String[] keys = {"location", "time", "face_url"};
            checkNotNull(trace, keys);
            JSONArray location = trace.getJSONArray("location");
            for (int i1 = 0; i1 < location.size(); i1++) {
                JSONObject point = location.getJSONObject(i1);
                String[] keyKey = {"x>=0", "y>=0", "x<=1", "y<=1"};

                checkKeyValues(point, keyKey, function);
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

        checkNotNull(data, checkColumnNames1);

        JSONArray regions = data.getJSONArray("regions");
        for (int i = 0; i < regions.size(); i++) {
            JSONObject singleRegion = regions.getJSONObject(i);
            String[] checkColumnNames2 = {"region_id", "location", "region_name"};
            checkNotNull(singleRegion, checkColumnNames2);
            JSONArray location = singleRegion.getJSONArray("location");
            for (int i1 = 0; i1 < location.size(); i1++) {
                JSONObject point = location.getJSONObject(i1);
                String[] keyKey = {"x>=0", "y>=0", "x<=1", "x<=1"};

                checkKeyValues(point, keyKey, function);
            }
        }

        JSONArray movingLines = data.getJSONArray("moving_lines");
        for (int i = 0; i < movingLines.size(); i++) {
            JSONObject line = movingLines.getJSONObject(i);
            String[] keys = {"move_times", "source", "target"};
            checkNotNull(line, keys);
        }

        JSONArray traces = data.getJSONArray("traces");
        for (int i = 0; i < traces.size(); i++) {
            JSONObject trace = traces.getJSONObject(i);
            String[] keys = {"location", "time", "face_url"};
            checkNotNull(trace, keys);
            JSONArray location = trace.getJSONArray("location");
            for (int i1 = 0; i1 < location.size(); i1++) {
                JSONObject point = location.getJSONObject(i1);
                String[] keyKey = {"x>=0", "y>=0", "x<=1", "y<=1"};

                checkKeyValues(point, keyKey, function);
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

        checkNotNull(jo, key1);
        checkNotNull(jo, key2);

        double value1 = jo.getDouble(key1);
        double value2 = jo.getDouble(key2);

        //防止取值方面出现问题，value为空的时候也是不符合的
        if (!(value1 <= value2)) {
            throw new Exception(function + key1 + "=" + value1 + "，应该<=" + key2 + "=" + value2);
        }
    }

    private void checkKeyValues(JSONObject jo, String[] checkKeyValues, String function) throws Exception {

        for (String keyValue : checkKeyValues) {

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
}
