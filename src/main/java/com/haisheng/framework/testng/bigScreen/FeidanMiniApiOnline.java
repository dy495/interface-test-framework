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
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class FeidanMiniApiOnline {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dt = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_ONLINE_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-online-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    private HttpConfig config;

    private String protect10000 = "926";

    String defaultRuleId = "907";
    String protect1DayRuleId = "924";


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

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
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

    private Object getShopId() {
        return "97";
    }

    String anShengId = "1005";
    String anShengName = "安生【勿动】";
    String anShengPhone = "12300000002";
    String normalOrderType = "NORMAL";
    String riskOrderType = "RISK";
    String firstAppearTime = "1582715836291";

    String faceUrl = "witness/100000000080571721/a944403e-672d-491c-9e8a-4cd9836fe066";

    String maiTianChannelName = "麦田";
    int maiTianChannelId = 19;
    String maiTianStaffName = "宫先生";
    String maiTianStaffPhone = "17610248107";
    String maiTianStaffId = "69";

    /**
     * 自助扫码(选自助)-顾客到场，置业顾问：安生
     * 保留此case，于海生
     */
    @Test
    public void A_S() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "12300000000";
            String selfCode = "805805";
            String smsCode = "805805";
            String customerName = caseName + "-" + getNamePro();

//            自助扫码
            selfRegister(customerName, customerPhone, selfCode, anShengId, "dd", "MALE");

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

//            校验
            String adviserName = anShengName;
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "正常";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";
            String visitor = "NATURE";
            String customerType = "自然访客";

            JSONObject orderLinkData = orderLinkList(orderId);

            JSONObject orderDetail = orderDetail(orderId);

//            订单详情
            checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderDetail);

//        订单详情，列表，关键环节中信息一致性
            detailListLinkConsist(orderId, customerPhone);

//        订单环节风险/正常
            checkNormalOrderLink(orderId, orderLinkData);

//        场内轨迹
            checkFirstVisitAndTrace(orderId, orderLinkData, true);

//            审核
            orderAudit(orderId, visitor);

//            校验风控单
            checkReport(orderId, orderStatusTips, orderLinkData.getJSONArray("list").size() + 1, customerType, orderDetail);

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "自助扫码（选自助）顾客到场--创单（选择无渠道）");
        }
    }

    /**
     * 顾客到场-PC(有渠道)，置业顾问：张钧甯
     * 选PC报备渠道
     * 注销此case，于海生
     */
//    @Test
    public void A_PCT() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            // PC报备
            String customerPhone = "12300000000";
            String smsCode = "805805";
            String customerName = caseName + "-" + getNamePro();
            String adviserName = anShengName;
            String adviserPhone = anShengPhone;
            int channelId = maiTianChannelId;
            String channelStaffName = maiTianStaffName;
            String channelStaffPhone = maiTianStaffPhone;

            newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

            JSONObject customer = customerList(customerName, channelId + "", anShengId, 1, 10).getJSONArray("list").getJSONObject(0);

            String reportTime = customer.getString("report_time");

//            刷证
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            创单
            createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            校验
            String channelName = maiTianChannelName;
            String orderStatusTips = "风险";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrderRiskLinkNum(orderId, orderLinkData, 2);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "订单风险状态:未知->风险", "存在2个异常环节");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "提前报备少于24h0min", "该顾客的风控规则为提前报备时间:24h0min");
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "顾客到场-PC（有渠道）-创单（选择PC报备渠道）");

        }
    }


    /**
     * 同一业务员报备同一顾客两次（全号）
     */
    @Test
    public void dupReport() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "重复报备";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "12300000001";

            String customerName = "麦田【勿动】";

            String res = newCustomerNoCheckCode(maiTianChannelId, maiTianStaffName, maiTianStaffPhone, "", "", customerPhone, customerName, "MALE");

            checkCode(res, StatusCode.BAD_REQUEST, "重复报备");

            checkMessage("重复报备", res, "报备失败！当前顾客信息已报备完成，请勿重复报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

//    --------------------------------------------规则页报备保护期验证-----------------------------------------------------------------------

    /**
     * 保护渠道报备 -> 其他渠道报备
     */
    @Test
    public void inProtectOthersFail() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备 -> 保护期内其他渠道报备";

        logger.info("\n\n" + caseName + "\n");

        try {

            String customerPhone = "13400000001";

            String customerName = "保护10000天【勿动】";

//            其他渠道报备
            String report2 = newCustomerNoCheckCode(maiTianChannelId, maiTianStaffName, maiTianStaffPhone, anShengName, anShengPhone, customerPhone, customerName, "MALE");

            checkCode(report2, StatusCode.BAD_REQUEST, "保护期内其他渠道报备");

            checkMessage("报备保护", report2, "报备失败！当前顾客信息处于(保护10000天)渠道报备保护期内，请勿重复报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void InProtectOthersComplete() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备-保护期内其他渠道补全手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "13400000001";

            String customerName = "保护10000天【勿动】";

//            其他渠道补全手机号

            String cid = "REGISTER-52b1da19-d913-4c4b-9c19-2abafa0b5c36";

            String res = customerEditPCNoCheckCode(cid, customerName, customerPhone, anShengName, anShengPhone);

            checkCode(res, StatusCode.BAD_REQUEST, "保护期内其他渠道修改手机号为当前顾客");

            checkMessage("报备保护", res, "修改顾客信息失败！该手机号已被其他拥有报备保护的渠道报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    /**
     * 其他渠道补全手机号为保护渠道报备的顾客手机号
     */
    @Test
    public void InProtectOthersChange() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "保护渠道报备-保护期内其他渠道修改手机号为保护渠道报备的顾客手机号";

        logger.info("\n\n" + caseName + "\n");

        try {
            // 报备
            String customerPhone = "13400000001";

            String customerName = "保护10000天【勿动】";

//            更改手机号

            String cid = "REGISTER-b9d0bb02-9323-40be-b831-e2dda6b35036";
            String res = customerEditPCNoCheckCode(cid, customerName, customerPhone, anShengName, anShengPhone);

            checkCode(res, StatusCode.BAD_REQUEST, "保护期内其他渠道修改手机号为当前顾客");

            checkMessage("报备保护", res, "修改顾客信息失败！该手机号已被其他拥有报备保护的渠道报备");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_NUM")
    public void ruleAheadInvalidStr(String number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "提前报备时间为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = addRiskRuleNoCheckCode("test", number, "10");

            checkNotCode(addRiskRule, StatusCode.SUCCESS, "提前报备时间为【" + number + "】");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_NUM_AHEAD")
    public void ruleAheadInvalidNumber(String number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "提前报备时间为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = addRiskRuleNoCheckCode("test", number, "10");

            checkCode(addRiskRule, StatusCode.BAD_REQUEST, "提前报备时间为【" + number + "】");

            checkMessage("新建风控规则", addRiskRule, "提前报备时间不能超过半年");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "VALID_NUM_AHEAD")
    public void ruleAheadValidNumber(int number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "提前报备时间为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String ruleName = getNamePro();

            addRiskRule(ruleName, number + "", "10");

            JSONObject data = riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String name = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("期待最后一条规则为【" + ruleName + "】，实际为【" + name + "】");
            }

            String id = ruleData.getString("id");

            deleteRiskRule(id);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_NUM")
    public void ruleProtectInvalidStr(String number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "报备保护期为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = addRiskRuleNoCheckCode("test", "60", number);

            checkNotCode(addRiskRule, StatusCode.SUCCESS, "");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_NUM_PROTECT")
    public void ruleProtectInvalidNum(String number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "报备保护期为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = addRiskRuleNoCheckCode("test", "60", number);

            checkCode(addRiskRule, StatusCode.BAD_REQUEST, "");

            checkMessage("新建风控规则", addRiskRule, "报备保护期不能超过10000天");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "VALID_NUM_PROTECT")
    public void ruleProtectValidNumber(int number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "报备保护期为【" + number + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String ruleName = getNamePro();

            addRiskRule(ruleName, number + "", "10");

            JSONObject data = riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String name = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("期待最后一条规则为【" + ruleName + "】，实际为【" + name + "】");
            }

            String id = ruleData.getString("id");

            deleteRiskRule(id);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_NAME")
    public void ruleNameInvalid(String name) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建风控规则名称为【" + name + "】";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = addRiskRuleNoCheckCode(name, "60", "60");

            checkCode(addRiskRule, StatusCode.BAD_REQUEST, "新建风控规则");

            if ("".equals(name.trim())) {
                checkMessage("新建风控规则", addRiskRule, "规则名称不可为空");
            } else if ("默认规则".equals(name)) {
                checkMessage("新建风控规则", addRiskRule, "规则名称不允许重复");
            } else {
                checkMessage("新建风控规则", addRiskRule, "规则名称最长为20个字符");
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "VALID_NAME")
    public void ruleNameValid(String name) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建风控规则名称为【" + name + "】";

        logger.info("\n\n" + caseName + "\n");

        try {
            addRiskRule(name, "60", "60");

            JSONObject data = riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String ruleName = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("期待最后一条规则为【" + ruleName + "】，实际为【" + name + "】");
            }

            String id = ruleData.getString("id");

            deleteRiskRule(id);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test(dataProvider = "INVALID_DELETE_RULE")
    public void romoveRuleInvalid(String id) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "非法删除规则";

        logger.info("\n\n" + caseName + "\n");

        try {

            String s = deleteRiskRuleNoCheckCode(id);

            checkCode(s, StatusCode.BAD_REQUEST, caseDesc);

            if (defaultRuleId.equals(id)) {
                checkMessage("新建风控规则", s, "只允许删除自定义规则");
            } else {
                checkMessage("新建风控规则", s, "规则已被渠道引用, 不可删除");
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

//    ----------------------------------------------新建顾客验证---------------------------------------------

//    @Test(dataProvider = "NEW_CUSTOMER_BAD")
    public void newCustomerBad(String message, int channelId, String channelStaffName, String channelStaffPhone, String adviserName,
                               String adviserPhone, String phone, String customerName, String gender) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + message;

        String caseDesc = "新建顾客-单个新建";

        logger.info("\n\n" + caseName + "\n");

        try {

            String s = newCustomerNoCheckCode(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, phone, customerName, gender);
            checkCode(s, StatusCode.BAD_REQUEST, message);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

//    @Test
    public void newCustomerXML() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建顾客-xml文件";

        logger.info("\n\n" + caseName + "\n");

        try {

            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/newCustomerFile";
            dirPath = dirPath.replace("/", File.separator);
            File file = new File(dirPath);
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                String xmlPath = dirPath + File.separator + files[i].getName();

                importFile(xmlPath);

                checkCode(this.response, StatusCode.BAD_REQUEST, files[i].getName() + ">>>");
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test
    public void visitor2Staff() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "到访人物转员工";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerType = "NEW";
            String deviceId = "6368038644646912";
            String startTime = LocalDate.now().minusDays(7).toString();
            String endTime = LocalDate.now().toString();

            JSONArray newB = catchList(customerType, deviceId, startTime, endTime, 1, 1).getJSONArray("list");

            String customerId = "";
            if (newB.size() > 0) {
                JSONObject onePerson = newB.getJSONObject(0);
                customerId = onePerson.getString("customer_id");

                visitor2Staff(customerId);

                Integer pages = catchList(customerType, deviceId, startTime, endTime, 1, 1).getInteger("pages");

                for (int i = 1; i <= pages; i++) {
                    JSONObject newA = catchList(customerType, deviceId, startTime, endTime, i, 50);
                    JSONArray list = newA.getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        if (customerId.equals(single.getString("customer_id"))) {
                            throw new Exception("转员工失败，有部分同一customerId的访客未转成员工。customerId=" + customerId);
                        }
                    }
                }

                pages = catchList("STAFF", deviceId, startTime, endTime, 1, 1).getInteger("pages");
                boolean isExist = false;
                for (int i = 1; i < pages; i++) {

                    JSONObject staff = catchList("STAFF", deviceId, startTime, endTime, i, 50);
                    JSONArray list = staff.getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        if (customerId.equals(single.getString("customer_id"))) {
                            isExist = true;
                            break;
                        }
                    }
                }

                if (!isExist) {
                    throw new Exception("转员工失败，员工列表中不存在该顾客，customerId=" + customerId);
                }
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test
    public void witnessUploadOcr() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "刷新验证码-确认登录-上传-刷新-用刷新之前的验证码登录-用刷新后的验证码登录-用刷新后的验证码登录";

        try {

            //        获取正确验证码
            String refreshCode = refreshQrcodeNoCheckCode();
            checkCode(refreshCode, StatusCode.SUCCESS, "刷新OCR验证码");

            String codeB = JSON.parseObject(refreshCode).getJSONObject("data").getString("code");

//        确认
            String confirmCode = confirmQrcodeNoCheckCode(codeB);

            checkCode(confirmCode, StatusCode.SUCCESS, "确认验证码");

            String token = JSON.parseObject(confirmCode).getJSONObject("data").getString("token");

//        上传身份信息
            String idCardPath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/idCard.jpg";
            idCardPath = idCardPath.replace("/", File.separator);
            String facePath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/share.jpg";
            facePath = facePath.replace("/", File.separator);

            ImageUtil imageUtil = new ImageUtil();
            String imageBinary = imageUtil.getImageBinary(idCardPath);
            imageBinary = stringUtil.trimStr(imageBinary);
            String faceBinary = imageUtil.getImageBinary(facePath);
            faceBinary  = stringUtil.trimStr(faceBinary);

            String ocrPicUpload = ocrPicUpload(token, imageBinary, faceBinary);
            checkCode(ocrPicUpload, StatusCode.SUCCESS, "案场OCR上传证件");

//        刷新
            refreshCode = refreshQrcodeNoCheckCode();
            checkCode(refreshCode, StatusCode.SUCCESS, "刷新OCR验证码");

            String codeA = JSON.parseObject(refreshCode).getJSONObject("data").getString("code");

//        原code确认
            String confirm = confirmQrcodeNoCheckCode(codeB);
            checkCode(confirm, StatusCode.BAD_REQUEST, "OCR确认-刷新之前的");

//        现code确认
            confirm = confirmQrcodeNoCheckCode(codeA);
            checkCode(confirm, StatusCode.SUCCESS, "OCR确认-刷新之后的");

//            现code再次确认
            confirm = confirmQrcodeNoCheckCode(codeA);
            checkCode(confirm, StatusCode.SUCCESS, "再次OCR确认-刷新之后的");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }


//    -----------------------------------------------------------其他方法------------------------------------------------------------

    /**
     * 17.3 OCR验证码确认-H5
     */
    private static String OCR_PIC_UPLOAD_JSON = "{\"shop_id\":${shopId},\"token\":\"${token}\"," +
            "\"identity_card\":\"${idCard}\",\"face\":\"${face}\"}";

    public String ocrPicUpload(String token, String idCard, String face) throws Exception {

        String url = "/external/ocr/pic/upload";
        String json = StrSubstitutor.replace(OCR_PIC_UPLOAD_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("token", token)
                .put("idCard", idCard)
                .put("face", face)
                .build()
        );

        String res = httpPostNoPrintPara(url, json);

        return res;
    }

    private String httpPostNoPrintPara(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        checkCode(response, StatusCode.SUCCESS, "");

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    public void visitor2Staff(String customerId) throws Exception {
        String url = "/risk/evidence/person-catch/toStaff";
        String json =
                "{\n" +
                        "    \"shop_id\":4116," +
                        "    \"customer_ids\":[" +
                        "        \"" + customerId + "\"" +
                        "    ]" +
                        "}";

        String res = httpPostWithCheckCode(url, json);
    }


    public JSONObject catchList(String customerType, String deviceId, String startTime, String ensTime, int page,
                                int size) throws Exception {
        String url = "/risk/evidence/person-catch/page";
        String json =
                "{\n" +
                        "\"person_type\":\"" + customerType + "\"," +
                        "\"device_id\":\"" + deviceId + "\"," +
                        "\"start_time\":\"" + startTime + "\"," +
                        "\"end_time\":\"" + ensTime + "\"," +
                        "\"page\":\"" + page + "\"," +
                        "\"size\":\"" + size + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject importFile(String imagePath) {
        String url = "http://store.winsenseos.com/risk/customer/file/import";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("authorization", authorization);
        httpPost.addHeader("shop_id", getShopId() + "");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        File file = new File(imagePath);
        try {
            builder.addBinaryBody(
                    "file",
                    new FileInputStream(file),
                    ContentType.IMAGE_JPEG,
                    file.getName()
            );

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            this.response = EntityUtils.toString(responseEntity, "UTF-8");

            logger.info("response: " + this.response);

        } catch (Exception e) {
            failReason = e.getMessage();
            e.printStackTrace();
        }

        return JSON.parseObject(this.response).getJSONObject("data");
    }

    public String deleteRiskRuleNoCheckCode(String id) throws Exception {

        String url = "/risk/rule/delete";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + id + "\"" +
                        "}";

        String s = httpPost(url, json);
        return s;
    }

    /**
     * 16.1 风控规则列表
     */
    public JSONObject riskRuleList() throws Exception {

        String url = "/risk/rule/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"page\":\"" + 1 + "\"," +
                        "    \"size\":\"" + 100 + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");


    }


    /**
     * 16.1 删除风控规则
     */
    public void deleteRiskRule(String id) throws Exception {

        String url = "/risk/rule/delete";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + id + "\"" +
                        "}";

        httpPostWithCheckCode(url, json);
    }


    /**
     * 16.1 新增风控规则
     */
    public void addRiskRule(String name, String aheadReportTime, String reportProtect) throws Exception {

        String url = "/risk/rule/add";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"name\":\"" + name + "\"," +
                        "    \"ahead_report_time\":\"" + aheadReportTime + "\"," +
                        "    \"report_protect\":\"" + reportProtect + "\"" +
                        "}";

        httpPostWithCheckCode(url, json);
    }

    /**
     * 16.1 新增风控规则
     */
    public String addRiskRuleNoCheckCode(String name, String aheadReportTime, String reportProtect) throws
            Exception {

        String url = "/risk/rule/add";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"name\":\"" + name + "\"," +
                        "    \"ahead_report_time\":\"" + aheadReportTime + "\"," +
                        "    \"report_protect\":\"" + reportProtect + "\"" +
                        "}";

        return httpPost(url, json);
    }

    /**
     * 3.10 修改顾客信息
     */
    public String customerEditPCNoCheckCode(String cid, String customerName, String phone, String
            adviserName, String adviserPhone) throws Exception {
        String url = "/risk/customer/edit/" + cid;
        String json =
                "{\n" +
                        "\"customer_name\":\"" + customerName + "\"," +
                        "\"phone\":\"" + phone + "\"," +
                        "\"adviser_name\":\"" + adviserName + "\"," +
                        "\"adviser_phone\":\"" + adviserPhone + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPost(url, json);

        return res;
    }

    /**
     * OCR二维码刷新-PC
     */
    public JSONObject refreshQrcode() throws Exception {

        String url = "/risk/shop/ocr/qrcode/refresh";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 17.3 OCR验证码确认-H5
     */
    public JSONObject confirmQrcode(String code) throws Exception {

        String url = "/external/ocr/code/confirm";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"code\":\"" + code + "\"" +
                        "}";


        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String confirmQrcodeNoCheckCode(String code) throws Exception {

        String url = "/external/ocr/code/confirm";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"code\":\"" + code + "\"" +
                        "}";


        String res = httpPost(url, json);

        return res;

    }

    public String refreshQrcodeNoCheckCode() throws Exception {

        String url = "/risk/shop/ocr/qrcode/refresh";
        String json =
                "{}";

        String res = httpPost(url, json);

        return res;
    }

    private void checkCode(String response, int expect, String message) throws Exception {
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

    private void checkMessage(String function, String response, String message) throws Exception {

        String messageRes = JSON.parseObject(response).getString("message");
        if (!message.equals(messageRes)) {
            throw new Exception(function + "，提示信息与期待不符，期待=" + message + "，实际=" + messageRes);
        }
    }

    public void checkOrderRiskLinkNum(String orderId, JSONObject data, int num) throws Exception {

        JSONArray list = data.getJSONArray("list");

        int riskNum = 0;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            int linkStatus = single.getInteger("link_status");
            if (linkStatus == 0) {
                riskNum++;
            }
        }

        if (riskNum != num) {
            throw new Exception("order_id=" + orderId + "，期待异常环节数=" + num + "，系统返回异常环节数=" + riskNum);
        }
    }

    public void checkReport(String orderId, String orderType, int riskNum, String customerType, JSONObject orderDetail) throws Exception {

        String txtPath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/riskReport.txt";
        txtPath = txtPath.replace("/", File.separator);
        String pdfPath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/riskReport.pdf";
        pdfPath = pdfPath.replace("/", File.separator);

        String pdfUrl = reportCreate(orderId).getString("file_url");

        File pdfFile = new File(pdfPath);
        File txtFile = new File(txtPath);
        pdfFile.delete();
        txtFile.delete();

//        下载pdf
        String currentTime1 = dt.timestampToDate("yyyy年MM月dd日 HH:mm", System.currentTimeMillis());
        downLoadPdf(pdfUrl);
        String currentTime = dt.timestampToDate("yyyy年MM月dd日 HH:mm", System.currentTimeMillis());

//        pdf转txt
        readPdf(pdfPath);

//        去掉所有空格
        String noSpaceStr = removebreakStr(txtPath);

//        获取所有环节信息
        Link[] links = getLinkMessage(orderId);
//
        String message = "";

//            1.1、风控单生成日期
        DateTimeUtil dt = new DateTimeUtil();

        currentTime = stringUtil.trimStr(currentTime);
        currentTime1 = stringUtil.trimStr(currentTime1);

        if (!(noSpaceStr.contains(currentTime) || noSpaceStr.contains(currentTime1))) {
            message += "【风控单生成日期】那一行有错误,显示的不是生成订单的时间\n\n";
        }

//            1.2生成操作者
        if (!noSpaceStr.contains("实验室Demo")) {
            message += "【生产操作者】显示的不是【实验室Demo】，\n\n";
        }

//            2、系统核验结果
        String s = "";

        if ("风险".equals(orderType)) {
            s = "风险存在" + riskNum + "个异常环节" + customerType;
        } else {
            s = "正常" + riskNum + "个环节均正常" + customerType;
        }
        if (!noSpaceStr.contains(s)) {
            message += "【系统核验结果】那一行有错误\n\n";
        }

//            订单详情
        String customerName = orderDetail.getString("customer_name");
        String phone = orderDetail.getString("phone");
        String adviserName = orderDetail.getString("adviser_name");
        if (adviserName == null) {
            adviserName = "-";
        }
        String channelName = orderDetail.getString("channel_name");
        if (channelName == null) {
            channelName = "-";
        }
        String channelStaffName = orderDetail.getString("channel_staff_name");
        if (channelStaffName == null) {
            channelStaffName = "-";
        }
        String firstAppearTime = "-";
        if (orderDetail.getLong("first_appear_time") != null) {
            firstAppearTime = dt.timestampToDate("yyyy/MM/dd HH:mm:ss", orderDetail.getLong("first_appear_time"));
        }
        String reportTime = "-";
        if (orderDetail.getLong("report_time") != null) {

            reportTime = dt.timestampToDate("yyyy/MM/dd HH:mm:ss", orderDetail.getLong("report_time"));
        }

        String dealTime = "-";
        if (orderDetail.getLong("deal_time") != null) {
            dealTime = dt.timestampToDate("yyyy/MM/dd HH:mm:ss", orderDetail.getLong("deal_time"));
        }

        s = "顾客" + customerName + "手机号码" + phone + "成单置业顾问" + adviserName + "成单渠道" + channelName + "渠道业务员" + channelStaffName + "报备时间" + reportTime + "首次到访" + firstAppearTime + "刷证时间" + dealTime + "当前风控状态" + orderType;
        String tem = stringUtil.trimStr(s);
        if (!noSpaceStr.contains(tem)) {
            message += "风控单中【风控详情】信息有错误";
        }

//            3、关键环节
        for (int i = 0; i < links.length; i++) {
            Link link = links[i];
            s = stringUtil.trimStr(link.linkTime + link.linkName + link.content + link.linkPoint);
            if (!noSpaceStr.contains(s)) {

                message += "风控单中【" + links[i].linkName +
                        "】环节有错误，环节时间为【" + links[i].linkTime + "】，没有找到【" + links[i].linkPoint + "】\n\n";
            }
        }

//            4、是否有空白页
        if (noSpaceStr.contains("页第")) {
            message += "有空白页\n\n";
        }

        if (!"".equals(message)) {
            throw new Exception("orderId=" + orderId + "，风控单中有以下错误\n\n" + message);
        }
    }

    public Link[] getLinkMessage(String orderId) throws Exception {
        JSONArray list = orderLinkList(orderId).getJSONArray("list");
        Link[] links = new Link[list.size()];
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            Link link = new Link();
            link.linkName = single.getString("link_name");
            JSONObject linkNote = single.getJSONObject("link_note");
            if (!linkNote.getBooleanValue("is_pic")) {
                link.content = linkNote.getString("content");
            }
            if (link.content == null || "".equals(link.content.trim())) {
                link.content = "";
            }
            String linkPoint = single.getString("link_point");
            link.linkPoint = linkPoint.replace("\n", " ");
            link.linkTime = dt.timestampToDate("yyyy-MM-dd HH:mm:ss", single.getLong("link_time"));
            links[i] = link;
        }

        return links;
    }

    public String removebreakStr(String fileName) {

        StringBuffer noSpaceStr = new StringBuffer();

        try {
            File srcFile = new File(fileName);
            boolean b = srcFile.exists();
            if (b) {    //判断是否是路径是否存在，是否是文件夹

                if (!srcFile.getName().endsWith("txt")) {    //判断是否是TXT文件
                    System.out.println(srcFile.getName() + "不是TXT文件！");
                }
//                Runtime.getRuntime().exec("notepad " + srcFile.getAbsolutePath());//打开待处理文件,参数是字符串，是个命令
                String str = null;
                String REGEX = "\\s+";    //空格、制表符正则表达式,\s匹配任何空白字符，包括空格、制表符、换页符等

                InputStreamReader stream = new InputStreamReader(new FileInputStream(srcFile), "UTF-8");    //读入字节流，并且设置编码为UTF-8
                BufferedReader reader = new BufferedReader(stream);    ////构造一个字符流的缓存器，存放在控制台输入的字节转换后成的字符

//                File newFile = new File(srcFile.getParent(),
//                 "new" + srcFile.getName());    //建立将要输出的文件和文件名

//                OutputStreamWriter outstream = new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8");    //写入字节流
//                BufferedWriter writer = new BufferedWriter(outstream);
                Pattern patt = Pattern.compile(REGEX);    //创建Pattern对象，处理正则表达式

                while ((str = reader.readLine()) != null) {
                    Matcher mat = patt.matcher(str);    //先处理每一行的空白字符
                    str = mat.replaceAll("");

                    if (str == "") {    //如果不想保留换行符直接写入就好，不用多此一举
                        continue;
                    } else {
                        noSpaceStr.append(str);
//                        writer.write(str);    //如果想保留换行符，可以利用str+"\r\n" 来在末尾写入换行符
                    }
                }
//                writer.close();
                reader.close();

                //打开修改后的文档
//                Runtime.getRuntime().exec("notepad " + newFile.getAbsolutePath());
//                System.out.println("文件修改完成！");
            } else {
//                System.out.println("文件夹路径不存在或输入的不是文件夹路径！");
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        return noSpaceStr.toString();
    }

    public static String readPdf(String fileStr) throws Exception {
        // 是否排序
        boolean sort = false;
        File pdfFile = new File(fileStr);
        // 输入文本文件名称
        String textFile = null;
        // 编码方式
        String encoding = "UTF-8";
        // 开始提取页数
        int startPage = 1;
        // 结束提取页数
        int endPage = Integer.MAX_VALUE;
        // 文件输入流，生成文本文件
        Writer output = null;
        // 内存中存储的PDF Document
        PDDocument document = null;
        try {

            //注意参数是File
            document = PDDocument.load(pdfFile);

            // 以原来PDF的名称来命名新产生的txt文件
            textFile = fileStr.replace(".pdf", ".txt");

            // 文件输入流，写入文件到textFile
            output = new OutputStreamWriter(new FileOutputStream(textFile), encoding);
            // PDFTextStripper来提取文本
            PDFTextStripper stripper = null;
            stripper = new PDFTextStripper();
            // 设置是否排序
            stripper.setSortByPosition(sort);
            // 设置起始页
            stripper.setStartPage(startPage);
            // 设置结束页
            stripper.setEndPage(endPage);
            // 调用PDFTextStripper的writeText提取并输出文本
            stripper.writeText(document, output);

            return textFile;
        } finally {
            if (output != null) {
                output.close();
            }
            if (document != null) {
                // 关闭PDF Document
                document.close();
            }
        }
    }

    private void checkFirstVisitAndTrace(String orderId, JSONObject data, boolean expectExist) throws Exception {
        JSONArray linkLists = data.getJSONArray("list");

        boolean isExist = false;

        String functionPre = "orderId=" + orderId + "，";

        for (int i = 0; i < linkLists.size(); i++) {
            String function;
            JSONObject link = linkLists.getJSONObject(i);
            String linkKey = link.getString("link_key");
            String id = link.getString("id");

            function = functionPre + "环节id=" + id + "，";

            if ("FIRST_APPEAR".equals(linkKey) || "TRACE".equals(linkKey)) {
                isExist = true;

                checkUtil.checkNotNull(function, link, "link_point");
                checkUtil.checkNotNull(function, link.getJSONObject("link_note"), "face_url");
                checkUtil.checkNotNull(function, link, "link_time");
                checkUtil.checkNotNull(function, link, "link_time");
                checkUtil.checkNotNull(function, link, "link_name");

                Integer linkStatus = link.getInteger("link_status");
                String linkName = link.getString("link_name");
                if (linkStatus != 1) {
                    throw new Exception("order_id=" + orderId + "，环节=" + linkName + "，环节id=" + id + "，应为正常环节，系统返回为异常!");
                }
            }
        }

        if (isExist != expectExist) {
            throw new Exception("是否期待存在场内轨迹，期待：" + expectExist + ",实际：" + isExist);
        }
    }

    public void checkNormalOrderLink(String orderId, JSONObject data) throws Exception {

        JSONArray linkLists = data.getJSONArray("list");

        for (int i = 0; i < linkLists.size(); i++) {
            JSONObject link = linkLists.getJSONObject(i);
            Integer linkStatus = link.getInteger("link_status");
            String linkName = link.getString("link_name");
            if (linkStatus != 1) {
                throw new Exception("order_id=" + orderId + "，环节=" + linkName + "，应为正常环节，系统返回为异常!");
            }
        }
    }

    private void detailListLinkConsist(String orderId, String phone) throws Exception {

        JSONArray list = orderList(3, phone, 100).getJSONArray("list");

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
                        throw new Exception("orderId=" + orderId + ",channel_name在订单列表中是空，在订单详情中=" + channelNameDetail);
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

    public void downLoadPdf(String pdfUrl) throws IOException {

        String downloadImagePath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/riskReport.pdf";

        URL url = new URL(pdfUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 得到输入流
        InputStream inputStream = conn.getInputStream();
        // 获取自己数组
        byte[] getData = readInputStream(inputStream);
        // 文件保存位置
        File file = new File(downloadImagePath);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    public static byte[] readInputStream(InputStream inputStream)
            throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    private void checkDetail(String orderId, String customerName, String phone, String adviserName, String
            channelName,
                             String channelStaffName, String orderStatusTips, String faceUrl, String firstAppearTime,
                             String reportTime, JSONObject orderDetail) throws Exception {

        String function = "订单详情-orderId=" + orderId + "，";

        if (!"-".equals(customerName)) {

            checkUtil.checkKeyValue(function, orderDetail, "customer_name", customerName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "customer_name");
        }

        if (!"-".equals(phone)) {

            checkUtil.checkKeyValue(function, orderDetail, "phone", phone, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "phone");
        }

        if (!"-".equals(adviserName)) {

            checkUtil.checkKeyValue(function, orderDetail, "adviser_name", adviserName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "adviser_name");
        }

        if (!"-".equals(channelName)) {

            checkUtil.checkKeyValue(function, orderDetail, "channel_name", channelName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "channel_name");
        }

        if (!"-".equals(channelStaffName)) {

            checkUtil.checkKeyValue(function, orderDetail, "channel_staff_name", channelStaffName, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "channel_staff_name");
        }

        if (!"-".equals(orderStatusTips)) {

            checkUtil.checkKeyValue(function, orderDetail, "order_status_tips", orderStatusTips, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "order_status_tips");
        }

        if (!"-".equals(faceUrl)) {
            checkUtil.checkKeyValue(function, orderDetail, "face_url", faceUrl, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "face_url");
        }

        if (!"-".equals(firstAppearTime)) {
            checkUtil.checkKeyValue(function, orderDetail, "first_appear_time", firstAppearTime, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "first_appear_time");
        }

        if (!"-".equals(reportTime)) {
            checkUtil.checkKeyValue(function, orderDetail, "report_time", reportTime, true);
        } else {
            checkUtil.checkNull(function, orderDetail, "report_time");
        }

        checkUtil.checkKeyValue(function, orderDetail, "deal_time", "", false);
    }


    public void checkOrderRiskLinkMess(String orderId, JSONObject data, String linkKey, String content, String linkPoint) throws Exception {

        JSONArray linkLists = data.getJSONArray("list");

        for (int i = 0; i < linkLists.size(); i++) {
            JSONObject link = linkLists.getJSONObject(i);

            String linkKeyRes = link.getString("link_key");
            if (linkKey.equals(linkKeyRes)) {

                String contentRes = link.getJSONObject("link_note").getString("content");

                if (content.equals(contentRes)) {

                    int linkStatus = link.getInteger("link_status");
                    if (linkStatus != 0) {
                        throw new Exception("order_id" + orderId + "，环节【" + linkKey + "】，应为异常环节，系统返回为正常！");
                    }

                    String linkPointRes = link.getString("link_point");

                    if (!linkPoint.equals(linkPointRes)) {
                        throw new Exception("order_id=" + orderId + "，环节【" + linkKey + "】的异常提示应为【" + linkPoint + "】，系统提示为【" + linkPointRes + "】");
                    }

                    break;
                }
            }
        }
    }

    public String genCardId() {
        Random random = new Random();
        long num = 100000000000000000L + random.nextInt(99999999);

        return String.valueOf(num);
    }


//    --------------------------------------------------------接口方法-------------------------------------------------------


    /**
     * 3.4 顾客列表
     */
    public JSONObject customerList(String phone, String channel, String adviser, int page, int pageSize) throws Exception {

        String url = "/risk/customer/list";

        String json =
                "{\n";

        if (!"".equals(phone)) {
            json += "    \"phone_or_name\":\"" + phone + "\",";
        }

        if (!"".equals(channel)) {
            json += "    \"channel_id\":" + channel + ",";
        }

        if (!"".equals(adviser)) {
            json += "    \"adviser_id\":" + adviser + ",";
        }

        json += "    \"shop_id\":" + getShopId() + "," +
                "    \"page\":" + page + "," +
                "    \"page_size\":" + pageSize +
                "}";


        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    private static String ORDER_DETAIL_JSON = "{\"order_id\":\"${orderId}\"," +
            "\"shop_id\":${shopId}}";

    public JSONObject orderDetail(String orderId) throws Exception {

        String url = "/risk/order/detail";

        String json = StrSubstitutor.replace(ORDER_DETAIL_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("orderId", orderId)
                .build()
        );
        String res = httpPostWithCheckCode(url, json, new String[0]);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 4.15 订单审核
     */
    public JSONObject orderAudit(String orderId, String visitor) throws Exception {
        String url = "/risk/order/status/audit";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"orderId\":\"" + orderId + "\"," +
                        "    \"visitor\":\"" + visitor + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 15.2 生成风险单
     */
    public JSONObject reportCreate(String orderId) throws Exception {
        String url = "/risk/evidence/risk-report/download";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"orderId\":\"" + orderId + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public void newCustomer(int channelId, String channelStaffName, String channelStaffPhone, String adviserName, String adviserPhone, String phone, String customerName, String gender) throws Exception {

        String url = "/risk/customer/insert";

        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\",";
        if (!"".equals(adviserName)) {
            json += "    \"adviser_name\":\"" + adviserName + "\",";
            json += "    \"adviser_phone\":\"" + adviserPhone + "\",";
        }

        if (channelId != -1) {
            json += "    \"channel_id\":" + channelId + "," +
                    "    \"channel_staff_name\":\"" + channelStaffName + "\"," +
                    "    \"channel_staff_phone\":\"" + channelStaffPhone + "\",";
        }

        json +=
                "    \"gender\":\"" + gender + "\"," +
                        "    \"shop_id\":" + getShopId() + "" +
                        "}";

        httpPostWithCheckCode(url, json);
    }

    public String newCustomerNoCheckCode(int channelId, String channelStaffName, String channelStaffPhone, String
            adviserName, String adviserPhone, String phone, String customerName, String gender) {

        String url = "/risk/customer/insert";

        String res = "";

        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\",";
        if (!"".equals(adviserName)) {
            json += "    \"adviser_name\":\"" + adviserName + "\",";
            json += "    \"adviser_phone\":\"" + adviserPhone + "\",";
        }

        if (channelId != -1) {
            json += "    \"channel_id\":" + channelId + "," +
                    "    \"channel_staff_name\":\"" + channelStaffName + "\"," +
                    "    \"channel_staff_phone\":\"" + channelStaffPhone + "\",";
        }

        json +=
                "    \"gender\":\"" + gender + "\"," +
                        "    \"shop_id\":" + getShopId() + "" +
                        "}";

        try {
            res = httpPost(url, json);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String getNamePro() {

        String tmp = UUID.randomUUID() + "";

        return tmp.substring(tmp.length() - 5);
    }

    /**
     * 4.6 订单关键步骤接口
     */
    public JSONObject orderLinkList(String orderId) throws Exception {
        String url = "/risk/order/link/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"orderId\":\"" + orderId + "\"," +
                        "    \"page\":\"" + 1 + "\"," +
                        "    \"size\":\"" + 100 + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);//订单详情与订单跟进详情入参json一样

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject orderList(int status, String namePhone, int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"page\":1" + ",";
        if (status != -1) {
            json += "    \"status\":\"" + status + "\",";
        }

        if (!"".equals(namePhone)) {
            json += "    \"customer_name\":\"" + namePhone + "\",";
        }

        json += "    \"size\":" + pageSize + "" +
                "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject createOrder(String phone, String orderId, String faceUrl, int channelId, String smsCode) throws Exception {

        String url = "/risk/order/createOrder";

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
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res);
    }


    public String witnessUpload(String cardId, String personName) throws Exception {
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
                        "        \"https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/witness/100000000000235625/d020e3fe-8050-47bb-9c16-49a2aebdc8f0?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1612575519&Signature=5nntV5uCcxSdhDul3HP4FcJeQDg%3D\"," +
                        "        \"https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/witness/100000000000235625/d020e3fe-8050-47bb-9c16-49a2aebdc8f0?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1612575519&Signature=5nntV5uCcxSdhDul3HP4FcJeQDg%3D\"" +
                        "    ],\n" +
                        "    \"system\":{" +
                        "        \"app_id\":\"111112a388c2\"," +
                        "        \"device_id\":\"6368038644646912\"," +
                        "        \"scope\":[" +
                        "            \"97\"" +
                        "        ]," +
                        "        \"service\":\"/business/risk/WITNESS_UPLOAD/v1.0\"," +
                        "        \"source\":\"DEVICE\"" +
                        "    }" +
                        "}";

        Thread.sleep(3000);

        return httpPostWithCheckCode(router, json);
    }

    private void checkNotCode(String response, int expectNot, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            if (expectNot == code) {
                Assert.assertNotEquals(code, expectNot, message+resJo.getString("message"));
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
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
            if (failReason.contains("java.lang.Exception:")) {
                failReason = failReason.replace("java.lang.Exception", "异常");
            } else if (failReason.contains("java.lang.AssertionError")) {
                failReason = failReason.replace("java.lang.AssertionError", "异常");
            }
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

            String failReason = aCase.getFailReason();

            dingPush("飞单线上 \n" +
                    "验证：" + aCase.getCaseDescription() +
                    " \n\n" + failReason);
        }
    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {

            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.ONLINE_OPEN_MANAGEMENT_PLATFORM_GRP);

            alarmPush.onlineRgn(msg);
            this.FAIL = true;
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        Assert.assertNull(aCase.getFailReason());
    }

    private void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.ONLINE_OPEN_MANAGEMENT_PLATFORM_GRP);

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

    @DataProvider(name = "INVALID_NUM")
    public Object[] invalidNum() {
        return new Object[]{
                "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890",
                "[]@-+~！#$^&()={}|;:'\\\"<>.?/",
                "·！￥……（）——【】、；：”‘《》。？、,%*",
                "-1",
                "20.20"
        };
    }

    @DataProvider(name = "INVALID_NUM_AHEAD")
    public Object[] invalidNumAhead() {
        return new Object[]{
                "260001"
        };
    }

    @DataProvider(name = "INVALID_NUM_PROTECT")
    public Object[] invalidNumProtect() {
        return new Object[]{
                "10001"
        };
    }

    @DataProvider(name = "VALID_NUM_AHEAD")
    public Object[] validNumAhead() {
        return new Object[]{
                0, 1, 60, 1440, 10080, 43200, 10000
        };
    }

    @DataProvider(name = "VALID_NUM_PROTECT")
    public Object[] validNumProtect() {
        return new Object[]{
                0, 1, 60, 1440, 10080, 43200, 10001, 260000
        };
    }

    @DataProvider(name = "INVALID_NAME")
    public Object[] invalidName() {
        return new Object[]{
                "",
                "   ",
                "qwer@tyui&opas.dfgh#？",
                "qwer tyui opas dfg  h",
                "默认规则"
        };
    }

    @DataProvider(name = "VALID_NAME")
    public Object[] validName() {
        return new Object[]{
                "正常一点-飞单V3.0",
                "qwer@tyui&opas.dfgh？",
                "qwer tyui opas dfgh ",
                "qwer tyui opas dfg h",
                "·！￥……（）——【】、；：‘《》。？、",
        };
    }

    @DataProvider(name = "INVALID_DELETE_RULE")
    public Object[] invalidDeleteRule() {
        return new Object[]{
                defaultRuleId,
                protect1DayRuleId
        };
    }


    @DataProvider(name = "NEW_CUSTOMER_BAD")
    public Object[][] newCUstomerBad() {
        return new Object[][]{
//channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE"
                new Object[]{
                        "顾客姓名为空，", maiTianChannelId, maiTianStaffName, maiTianStaffPhone, anShengName, anShengPhone, "12300000001", "", "MALE"
                },
                new Object[]{
                        "顾客手机号为空，", maiTianChannelId, maiTianStaffName, maiTianStaffPhone, anShengName, anShengPhone, "", "name", "MALE"
                },
                new Object[]{
                        "顾客性别为空，", maiTianChannelId, maiTianStaffName, maiTianStaffPhone, anShengName, anShengPhone, "12300000001", "name", ""
                },
                new Object[]{
                        "置业顾问手机号为空，", maiTianChannelId, maiTianStaffName, maiTianStaffPhone, anShengName, "", "12300000001", "name", "MALE"
                },
                new Object[]{
                        "置业顾问隐藏手机号，", maiTianChannelId, maiTianStaffName, maiTianStaffPhone, anShengName, "123****0000", "12300000001", "name", "MALE"
                },
                new Object[]{
                        "置业顾问手机号存在，姓名不同，", maiTianChannelId, maiTianStaffName, maiTianStaffPhone, "name", anShengPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "业务员手机号为空，", maiTianChannelId, maiTianStaffName, "", anShengName, anShengPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "业务员隐藏手机号，", maiTianChannelId, maiTianStaffName, "123****1111", anShengName, anShengPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "业务员姓名为空，", maiTianChannelId, "", maiTianStaffPhone, anShengName, anShengPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "业务员手机号存在，姓名不同，", maiTianChannelId, "name", maiTianStaffPhone, anShengName, anShengPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "有渠道，无业务员信息，", maiTianChannelId, "", "", anShengName, anShengPhone, "12300000001", "name", "MALE"
                },
        };
    }
}

