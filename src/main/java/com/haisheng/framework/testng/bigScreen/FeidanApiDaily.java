package com.haisheng.framework.testng.bigScreen;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.QADbUtil;
import org.apache.commons.lang.text.StrSubstitutor;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.collect.ImmutableMap;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class FeidanApiDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    /**
     * http工具 maven添加以下配置
     * <dependency>
     * <groupId>com.arronlong</groupId>
     * <artifactId>httpclientutil</artifactId>
     * <version>1.0.4</version>
     * </dependency>
     */
    private HttpConfig config;



    private String getIpPort() {
        return "http://dev.store.winsenseos.com";
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
            throw new RuntimeException("初始化http配置异常", e);
        }
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    private String httpPost(String path, String json, String... checkColumnNames) {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        String result;
        try {
            result = HttpClientUtil.post(config);
        } catch (HttpProcessException e) {
            throw new RuntimeException("http post 调用异常，url = " + queryUrl, e);
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        checkResult(result, checkColumnNames);
        return result;
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
        String json = "{\"username\":\"yuexiu@test.com\",\"passwd\":\"f5b3e737510f31b88eb2d4b5d0cd2fb4\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        String result = "";
        try {
            result = HttpClientUtil.post(config);
            this.authorization = JSONObject.parseObject(result).getJSONObject("data").getString("token");
            logger.info("authorization: {}", this.authorization);
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
            httpPost(path, json, checkColumnName);

        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "校验shop");
        }

    }

    private Object getShopId() {
        return "4116";
    }


    private static final String ADD_ORDER = "/risk/order/createOrder";
    private static final String SEARCH_ORDER = "/risk/order/detail";
    private static final String AUDIT_ORDER = "/risk/order/status/audit";

    private static String CREATE_ORDER_JSON = "{\"request_id\":\"${requestId}\"," +
            "\"shop_id\":${shopId},\"id_card\":\"${idCard}\",\"phone\":\"${phone}\"," +
            "\"order_stage\":\"SIGN\"}";

    private static String DETAIL_ORDER_JSON = "{\"request_id\":\"${requestId}\"," +
            "\"shop_id\":${shopId},\"order_id\":\"${orderId}\"}";

    private static String AUDIT_ORDER_JSON = "{\"is_customer_introduced\":${isCustomerIntroduced}," +
            "\"introduce_checked_person\":\"${introduceCheckedPerson}\"," +
            "\"is_channel_staff_show_dialog\":${isChannelStaffShowDialog}," +
            "\"dialog_path\":\"FEIDAN/undefined/30830a3179a3d75c634335a7104553fa\"," +
            "\"shop_id\":${shopId}," +
            "\"order_id\":\"${orderId}\"}";

    /**
     * 于海生，现场自然成交，订单状态：正常 ，核验状态：无需核验
     * 18811111111
     * 111111111111111111
     */
    @Test
    public void arrivalAndDeal() {
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            // 创建订单
            String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("idCard", "111111111111111111")
                    .put("phone", "18811111111")
                    .build()
            );
            String[] checkColumnNames = {};
            String res = httpPost(ADD_ORDER, json, checkColumnNames);

            // 查询订单
            JSONObject result = JSON.parseObject(res);
            Object id = JSONPath.eval(result, "$.data.order_id");
            json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                    .put("requestId", UUID.randomUUID().toString())
                    .put("shopId", getShopId())
                    .put("orderId", id)
                    .build()
            );
            checkColumnNames = new String[]{"order_status", "is_need_audit"};
            res = httpPost(SEARCH_ORDER, json, checkColumnNames);
            result = JSON.parseObject(res);

            Object orderStatus = JSONPath.eval(result, "$.data.order_status");
            Assert.assertEquals(1, orderStatus, "订单状态不正常");

            Object isNeedAudit = JSONPath.eval(result, "$.data.is_need_audit");
            Assert.assertEquals(false, isNeedAudit, "核验状态不正常");

        } catch (AssertionError e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.getMessage();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, caseName, "现场自然成交，订单状态：正常 ，核验状态：无需核验");
        }

    }


    /**
     * 刘峤，报备-到场-成交，订单状态：正常 ，核验状态：未核验，修改状态为正常，查询订单详情和订单列表，该订单状态为：正常，已核验
     * 12111111119
     * 111111111111111113
     */
    @Test
    public void testOrder2() {
        // 创建订单
        String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("idCard", "111111111111111113")
                .put("phone", "12111111119")
                .build()
        );
        String[] checkColumnNames = {"order_id"};
        String res = httpPost(ADD_ORDER, json, checkColumnNames);


        JSONObject result = JSON.parseObject(res);
        Object id = JSONPath.eval(result, "$.data.order_id");

        // 查询订单
        json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("orderId", id)
                .build()
        );
        checkColumnNames = new String[]{"order_status", "is_audited"};
        res = httpPost(SEARCH_ORDER, json, checkColumnNames);
        result = JSON.parseObject(res);

        Object orderStatus = JSONPath.eval(result, "$.data.order_status");
        Assert.assertEquals(1, orderStatus, "订单状态不正常");

        Object isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
        Assert.assertEquals(false, isNeedAudit, "核验状态不正常");

        // 审核订单
        json = StrSubstitutor.replace(AUDIT_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("orderId", id)
                .put("isCustomerIntroduced", 1)
                .put("introduceCheckedPerson", 1)
                .put("isChannelStaffShowDialog", 1)
                .build()
        );
        checkColumnNames = new String[]{};
        res = httpPost(AUDIT_ORDER, json, checkColumnNames);
        result = JSON.parseObject(res);

        // 查询订单
        json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("orderId", id)
                .build()
        );
        checkColumnNames = new String[]{"order_status", "is_audited"};
        res = httpPost(SEARCH_ORDER, json, checkColumnNames);
        result = JSON.parseObject(res);

        orderStatus = JSONPath.eval(result, "$.data.order_status");
        Assert.assertEquals(1, orderStatus, "订单状态不正常");

        isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
        Assert.assertEquals(true, isNeedAudit, "核验状态不正常");
    }

    /**
     * 李俊延，报备-到场-修改报备手机号-创单，订单状态：风险 ，核验状态：未核验。修改状态为风险，查询订单详情和订单列表，该订单状态为：风险，已核验
     * 12111111135
     * 111111111111111114
     */
    @Test
    public void testOrder4() {
        // 创建订单
        String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("idCard", "111111111111111114")
                .put("phone", "12111111135")
                .build()
        );
        String[] checkColumnNames = {};
        String res = httpPost(ADD_ORDER, json, checkColumnNames);

        // 查询订单
        JSONObject result = JSON.parseObject(res);
        Object id = JSONPath.eval(result, "$.data.order_id");
        json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("orderId", id)
                .build()
        );
        checkColumnNames = new String[]{"order_status"};
        res = httpPost(SEARCH_ORDER, json, checkColumnNames);
        result = JSON.parseObject(res);

        Object orderStatus = JSONPath.eval(result, "$.data.order_status");
        Assert.assertEquals(3, orderStatus, "订单状态不正常");

        Object isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
        Assert.assertEquals(false, isNeedAudit, "核验状态不正常");

        // 审核订单
        json = StrSubstitutor.replace(AUDIT_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("orderId", id)
                .put("isCustomerIntroduced", 0)
                .put("introduceCheckedPerson", 2)
                .put("isChannelStaffShowDialog", 0)
                .build()
        );
        checkColumnNames = new String[]{};
        res = httpPost(AUDIT_ORDER, json, checkColumnNames);
        result = JSON.parseObject(res);

        // 查询订单
        json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("orderId", id)
                .build()
        );
        checkColumnNames = new String[]{"order_status", "is_audited"};
        res = httpPost(SEARCH_ORDER, json, checkColumnNames);
        result = JSON.parseObject(res);

        orderStatus = JSONPath.eval(result, "$.data.order_status");
        Assert.assertEquals(3, orderStatus, "订单状态不正常");

        isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
        Assert.assertEquals(true, isNeedAudit, "核验状态不正常");
    }

    /**
     * 谢志东，顾客到场-H5报备-成交 ，订单状态：风险 ，核验状态：未核验。修改状态为正常，查询订单详情和订单列表，该订单状态为：正常，已核验
     * 12111111311
     * 111111111111111115
     */
    @Test
    public void testOrder5() {
        // 创建订单
        String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("idCard", "111111111111111115")
                .put("phone", "12111111311")
                .build()
        );
        String[] checkColumnNames = {};
        String res = httpPost(ADD_ORDER, json, checkColumnNames);

        // 查询订单
        JSONObject result = JSON.parseObject(res);
        Object id = JSONPath.eval(result, "$.data.order_id");
        json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("orderId", id)
                .build()
        );
        checkColumnNames = new String[]{"order_status"};
        res = httpPost(SEARCH_ORDER, json, checkColumnNames);
        result = JSON.parseObject(res);

        Object orderStatus = JSONPath.eval(result, "$.data.order_status");
        Assert.assertEquals(3, orderStatus, "订单状态不正常");

        Object isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
        Assert.assertEquals(false, isNeedAudit, "核验状态不正常");

        // 审核订单
        json = StrSubstitutor.replace(AUDIT_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("orderId", id)
                .put("isCustomerIntroduced", 1)
                .put("introduceCheckedPerson", 1)
                .put("isChannelStaffShowDialog", 1)
                .build()
        );
        checkColumnNames = new String[]{};
        res = httpPost(AUDIT_ORDER, json, checkColumnNames);
        result = JSON.parseObject(res);

        // 查询订单
        json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("orderId", id)
                .build()
        );
        checkColumnNames = new String[]{"order_status", "is_audited"};
        res = httpPost(SEARCH_ORDER, json, checkColumnNames);
        result = JSON.parseObject(res);

        orderStatus = JSONPath.eval(result, "$.data.order_status");
        Assert.assertEquals(1, orderStatus, "订单状态不正常");

        isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
        Assert.assertEquals(true, isNeedAudit, "核验状态不正常");
    }

    /**
     * 刘博，未到场-自然成交，订单状态：正常 ，核验状态：无需核验
     * 16600000003
     * 111111111111111116
     */
    @Test
    public void testOrder6() {
        // 创建订单
        String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("idCard", "111111111111111116")
                .put("phone", "16600000003")
                .build()
        );
        String[] checkColumnNames = {};
        String res = httpPost(ADD_ORDER, json, checkColumnNames);

        // 查询订单
        JSONObject result = JSON.parseObject(res);
        Object id = JSONPath.eval(result, "$.data.order_id");
        json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("orderId", id)
                .build()
        );
        checkColumnNames = new String[]{"order_status"};
        res = httpPost(SEARCH_ORDER, json, checkColumnNames);
        result = JSON.parseObject(res);

        Object orderStatus = JSONPath.eval(result, "$.data.order_status");
        Assert.assertEquals(1, orderStatus, "订单状态不正常");

        Object isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
        Assert.assertEquals(false, isNeedAudit, "核验状态不正常");
    }

    /**
     * 未到场B，未到场-报备-成交，订单状态：风险 ，核验状态：未核验
     * 16600000002
     * 111111111111111117
     */
    @Test
    public void testOrder7() {
        // 创建订单
        String json = StrSubstitutor.replace(CREATE_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("idCard", "111111111111111117")
                .put("phone", "16600000002")
                .build()
        );
        String[] checkColumnNames = {};
        String res = httpPost(ADD_ORDER, json, checkColumnNames);

        // 查询订单
        JSONObject result = JSON.parseObject(res);
        Object id = JSONPath.eval(result, "$.data.order_id");
        json = StrSubstitutor.replace(DETAIL_ORDER_JSON, ImmutableMap.builder()
                .put("requestId", UUID.randomUUID().toString())
                .put("shopId", getShopId())
                .put("orderId", id)
                .build()
        );
        checkColumnNames = new String[]{"order_status"};
        res = httpPost(SEARCH_ORDER, json, checkColumnNames);
        result = JSON.parseObject(res);

        Object orderStatus = JSONPath.eval(result, "$.data.order_status");
        Assert.assertEquals(3, orderStatus, "订单状态不正常");

        Object isNeedAudit = JSONPath.eval(result, "$.data.is_audited");
        Assert.assertEquals(false, isNeedAudit, "核验状态不正常");
    }

    private void setBasicParaToDB(Case aCase, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + caseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("订单创建&状态核查");
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
            //15011479599 谢志东
            String[] rd = {"15011479599"};
            alarmPush.alarmToRd(rd);
        }
    }
}

