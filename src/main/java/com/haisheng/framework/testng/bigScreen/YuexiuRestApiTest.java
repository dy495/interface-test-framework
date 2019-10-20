package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.QADbUtil;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.LocalDate;

/**
 * @author : xiezhidong
 * @date :  2019/10/12  14:55
 */

public class YuexiuRestApiTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response   = "";
    private DateTimeUtil dt   = new DateTimeUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID    = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_YUEXIU_SALES_OFFICE_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/yuexiu-daily-test/buildWithParameters?case_name=";


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
    private String ENV = System.getProperty("ENV");
    private boolean DEBUG = false;


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

    private void checkResult(String result, String... checkColumnNames) {
        logger.info("result = {}", result);
        JSONObject res = JSONObject.parseObject(result);
        if (!res.getInteger("code").equals(1000)) {
            failReason = "result code is " + res.getInteger("code") + " not success code";
            return;
            //throw new RuntimeException("result code is " + res.getInteger("code") + " not success code");
        }
        for (String checkColumn : checkColumnNames) {
            Object column = res.getJSONObject("data").get(checkColumn);
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
        String authorization = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJ5dWV4aXUiLCJleHAiOjE1NzE0NzM1OTh9.QYK9oGRG48kdwzYlYgZIeF7H2svr3xgYDV8ghBtC-YUnLzfFpP_sDI39D2_00wiVONSelVd5qQrjtsXNxRUQ_A";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    private void httpPost(String path, String json, String... checkColumnNames) {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
        } catch (HttpProcessException e) {
            failReason = "http post 调用异常，url = " + queryUrl + "\n" + e;
            return;
            //throw new RuntimeException("http post 调用异常，url = " + queryUrl, e);
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        checkResult(response, checkColumnNames);
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

        if (! StringUtils.isEmpty(failReason) || StringUtils.isEmpty(aCase.getFailReason())) {
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    private void saveData(Case aCase, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        Assert.assertNull(aCase.getFailReason());
    }

    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeSuite
    public void login() {
        if (DEBUG) {
            this.ENV = "DAILY";
        } else if (! StringUtils.isEmpty(this.ENV) && this.ENV.toLowerCase().contains("online")){
            this.ENV = "ONLINE";
            this.CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_YUEXIU_SALES_OFFICE_ONLINE_SERVICE;
            this.CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/yuexiu-online-test/buildWithParameters?case_name=";
        } else {
            this.ENV = "DAILY";
        }
        qaDbUtil.openConnection();
        Case aCase = new Case();

        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        initHttpConfig();
        String path = "/yuexiu/login";
        String loginUrl = getIpPort() + path;
        String json = "{\"username\":\"yuexiu\",\"passwd\":\"e10adc3949ba59abbe56e057f20f883e\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        String result;
        try {
            result = HttpClientUtil.post(config);
            logger.info("authorization: {}", JSONObject.parseObject(result).getJSONObject("data").getString("token"));
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
    public void testShopList() {
        String path = "/yuexiu/shop/list";
        String json = "{}";
        String checkColumnName = "list";
        httpPost(path, json, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "获取shop名单");

    }

    @Test
    public void testRealTimeShopInfo() {
        String path = REAL_TIME_PREFIX + "shop";
        String json = getRealTimeParamJson();
        String[] checkColumnNames = {"uv", "pv", "stay_time"};
        httpPost(path, json, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "获取实时shop名单，校验 pv uv stay_time 非空");
    }

    @Test
    public void testRealTimeRegionInfo() {

        String path = REAL_TIME_PREFIX + "region";
        String json = getRealTimeParamJson();
        String[] checkColumnNames = {"regions", "map_url"};
        httpPost(path, json, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "获取实时region信息，校验 regions map_url非空");
    }

    @Test
    public void testRealTimePersonsAccumulated() {

        String path = REAL_TIME_PREFIX + "persons/accumulated";
        String checkColumnName = "statistics_data";
        String json = getRealTimeParamJson();
        httpPost(path, json, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试实时接口persons/accumulated，校验 statistics_data 非空");
    }

    @Test
    public void testRealTimeAgeGenderDistribution() {

        String path = REAL_TIME_PREFIX + "age-gender/distribution";
        String checkColumnName = "list";
        String json = getRealTimeParamJson();
        httpPost(path, json, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试实时接口age-gender/distribution，校验 list 非空");
    }

    @Test
    public void testRealTimeCustomerTypeDistribution() {

        String path = REAL_TIME_PREFIX + "customer-type/distribution";
        String checkColumnName = "list";
        String json = getRealTimeParamJson();
        httpPost(path, json, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试实时接口customer-type/distribution，校验 list 非空");
    }

    @Test
    public void testRealTimeEntranceRank() {

        String path = REAL_TIME_PREFIX + "entrance/rank";
        String checkColumnName = "list";
        String json = getRealTimeParamJson();
        httpPost(path, json, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试实时接口entrance/rank，校验 list 非空");
    }

    @Test
    public void testRealTimeRegionThermalMap() {

        String path = REAL_TIME_PREFIX + "region/thermal_map";
        String json = getRealTimeParamJson();
        String[] checkColumnNames = {"regions", "map_url", "thermal_map"};
        httpPost(path, json, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试实时接口region/thermal_map，校验 regions map_url thermal_map 非空");
    }

    @Test
    public void testHistoryShop() {

        String path = HISTORY_PREFIX + "shop";
        String json = getHistoryParamJson();
        String[] checkColumnNames = {"uv", "pv", "stay_time"};
        httpPost(path, json, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试历史门店信息，校验 pv uv stay_time 非空");
    }

    @Test
    public void testHistoryRegion() {

        String path = HISTORY_PREFIX + "region";
        String json = getHistoryParamJson();
        String[] checkColumnNames = {"regions", "map_url"};
        httpPost(path, json, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试历史区域信息，校验 regions map_url 非空");
    }

    @Test
    public void testHistoryPersonsAccumulated() {

        String path = HISTORY_PREFIX + "persons/accumulated";
        String checkColumnName = "statistics_data";
        String json = getHistoryParamJson();
        httpPost(path, json, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试历史接口persons/accumulated，校验 statistics_data 非空");
    }

    @Test
    public void testHistoryAgeGenderDistribution() {

        String path = HISTORY_PREFIX + "age-gender/distribution";
        String checkColumnName = "list";
        String json = getHistoryParamJson();
        httpPost(path, json, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试历史接口age-gender/distribution，校验 list 非空");
    }

    @Test
    public void testHistoryCustomerTypeDistribution() {

        String path = HISTORY_PREFIX + "customer-type/distribution";
        String checkColumnName = "list";
        String json = getHistoryParamJson();
        httpPost(path, json, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试历史接口customer-type/distribution，校验 list 非空");
    }

    @Test
    public void testHistoryEntranceRank() {

        String path = HISTORY_PREFIX + "entrance/rank";
        String checkColumnName = "list";
        String json = getHistoryParamJson();
        httpPost(path, json, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试历史接口entrance/rank，校验 list 非空");
    }

    @Test
    public void testHistoryRegionCycle() {

        String path = HISTORY_PREFIX + "region/cycle";
        String checkColumnName = "list";
        String json = getHistoryParamJson();
        httpPost(path, json, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试历史接口region/cycle，校验 list 非空");
    }

    @Test
    public void testRegionDataMovingDirection() {

        String path = REGION_DATA_PREFIX + "moving-direction";
        String[] checkColumnNames = {"regions", "relations"};
        String json = getHistoryParamJson();
        httpPost(path, json, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试接口moving-direction，校验 regions relations 非空");
    }

    @Test
    public void testRegionDataEnterRank() {

        String path = REGION_DATA_PREFIX + "enter/rank";
        String checkColumnName = "list";
        String json = getHistoryParamJson();
        httpPost(path, json, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试接口enter/rank，校验 list 非空");
    }

    @Test
    public void testRegionDataCrossData() {

        String path = REGION_DATA_PREFIX + "cross-data";
        String[] checkColumnNames = {"regions", "relations"};
        String json = getHistoryParamJson();
        httpPost(path, json, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试接口cross-data，校验 regions relations 非空");
    }

    @Test
    public void testRegionDataMoveLineRank() {

        String path = REGION_DATA_PREFIX + "move-line/rank";
        String checkColumnName = "list";
        String json = getHistoryParamJson();
        httpPost(path, json, checkColumnName);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试接口move-line/rank，校验 list 非空");
    }

    @Test
    public void testCustomerDataTrace() {

        String path = CUSTOMER_DATA_PREFIX + "trace";
        String[] checkColumnNames = {"regions", "traces", "moving_lines", "map_url"};
        String json = getCustomerParamJson();
        httpPost(path, json, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试接口trace，校验 \"regions\", \"traces\", \"moving_lines\", \"map_url\" 非空");
    }

    @Test
    public void testCustomerDataLabels() {

        String path = CUSTOMER_DATA_PREFIX + "labels";
        String[] checkColumnNames = {"list"};
        String json = getCustomerParamJson();
        httpPost(path, json, checkColumnNames);

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "测试接口labels，校验 list 非空");
    }


}
