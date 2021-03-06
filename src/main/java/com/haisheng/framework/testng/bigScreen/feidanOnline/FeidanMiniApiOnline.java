package com.haisheng.framework.testng.bigScreen.feidanOnline;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.bigScreen.feidanDaily.Feidan;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class FeidanMiniApiOnline {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    public String failReason = "";
    public String response = "";
    public boolean FAIL = false;
    public Case aCase = new Case();
    Feidan feidan = new Feidan();
    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dt = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    public QADbUtil qaDbUtil = new QADbUtil();
    public int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    public int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_ONLINE_SERVICE;

    public String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-online-test/buildWithParameters?case_name=";

    public String DEBUG = System.getProperty("DEBUG", "true");

    public String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    public HttpConfig config;

    public String protect10000 = "926";

    String defaultRuleId = "907";
    String protect1DayRuleId = "924";

    public String maitianId = "19";
    public String maitianDisStaffName = "??????FREEZE";
    public String maitianDisStaffPhone = "12300000012";
    public String maitianDisStaffId = "1056";


    public String getIpPort() {
        return "http://store.winsenseos.com";
    }


    public void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            failReason = "?????????http????????????" + "\n" + e;
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

    public String httpPostWithCheckCode(String path, String json, String... checkColumnNames) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        CommonUtil.checkResult(response, checkColumnNames);
        return response;
    }

    public String httpPost(String path, String json) throws Exception {
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
     * ?????????????????? ??????????????????????????????initHttpConfig????????????authorization ????????????????????????????????????
     *
     * @ ??????
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
            aCase.setFailReason("http post ???????????????url = " + loginUrl + "\n" + e);
            logger.error(aCase.getFailReason());
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        saveData(aCase, caseName, caseName, "????????????authentication");
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
            saveData(aCase, caseName, caseName, "??????shop");
        }
    }

    public Object getShopId() {
        return "97";
    }

    String anShengId = "1005";
    String anShengName = "??????????????????";
    String anShengPhone = "12300000002";
    String firstAppearTime = "1585792274772";

    String faceUrl = "witness/100000000080571721/a944403e-672d-491c-9e8a-4cd9836fe066";

    String maiTianChannelName = "??????";
    int maiTianChannelInt = 19;
    String maiTianChannelIdStr = "19";
    String maiTianStaffName = "?????????";
    String maiTianStaffPhone = "17610248107";
    String maiTianStaffId = "69";


    String protectChannelName = "newProtect10000day";
    int protectChannelId = 36;
    String protectChannelIdStr = "36";


    /**
     * ????????????(?????????)-????????????????????????????????????
     * ?????????case????????????
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

//            ????????????
            selfRegister(customerName, customerPhone, selfCode, anShengId, "dd", "MALE");

//            ??????
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            ??????
            createOrder(customerPhone, orderId, faceUrl, -1, smsCode);

            list = customerList(customerName, "", "", 1, 10).getJSONArray("list");
            if (list.size() != 0) {
                throw new Exception("???????????????????????????????????????????????????customerName =" + customerName);
            }

//            ??????
            String adviserName = anShengName;
            String channelName = "-";
            String channelStaffName = "-";
            String orderStatusTips = "??????";
            String firstAppear = firstAppearTime + "";
            String reportTime = "-";
            String visitor = "NATURE";
            String customerType = "????????????";

            JSONObject orderLinkData = orderLinkList(orderId);

            JSONObject orderDetail = orderDetail(orderId);

//            ????????????
            checkDetail(orderId, customerName, customerPhone, adviserName, channelName, channelStaffName, orderStatusTips,
                    faceUrl, firstAppear, reportTime + "", orderDetail);

//        ??????????????????????????????????????????????????????
            detailListLinkConsist(orderId, customerPhone);

//        ??????????????????/??????
            checkNormalOrderLink(orderId, orderLinkData);

//        ????????????
            checkFirstVisitAndTrace(orderId, orderLinkData, true);

//            ??????
            orderAudit(orderId, visitor);

//            ???????????????
            checkReport(orderId, orderStatusTips, orderLinkData.getJSONArray("list").size() + 1, customerType, orderDetail);

        } catch (AssertionError e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();

            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "???????????????????????????????????????--???????????????????????????");
        }
    }

    /**
     * ????????????-PC(?????????)???????????????????????????
     * ???PC????????????
     * ?????????case????????????
     */
//    @Test
    public void A_PCT() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        try {
            // PC??????
            String customerPhone = "12300000000";
            String smsCode = "805805";
            String customerName = caseName + "-" + getNamePro();
            String adviserName = anShengName;
            String adviserPhone = anShengPhone;
            int channelId = maiTianChannelInt;
            String channelStaffName = maiTianStaffName;
            String channelStaffPhone = maiTianStaffPhone;

            newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");

            JSONObject customer = customerList(customerName, channelId + "", anShengId, 1, 10).getJSONArray("list").getJSONObject(0);

            String reportTime = customer.getString("report_time");

//            ??????
            witnessUpload(genCardId(), customerName);

            JSONArray list = orderList(-1, customerName, 10).getJSONArray("list");
            String orderId = list.getJSONObject(0).getString("order_id");

//            ??????
            createOrder(customerPhone, orderId, faceUrl, channelId, smsCode);

//            ??????
            String channelName = maiTianChannelName;
            String orderStatusTips = "??????";

            JSONObject orderLinkData = orderLinkList(orderId);

            checkOrderRiskLinkNum(orderId, orderLinkData, 2);

            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_STATUS_CHANGE", "??????????????????:??????->??????", "??????2???????????????");
            checkOrderRiskLinkMess(orderId, orderLinkData, "RISK_RULE", "??????????????????24h0min", "?????????????????????????????????????????????:24h0min");
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "????????????-PC???????????????-???????????????PC???????????????");

        }
    }


    /**
     * ???????????????????????????????????????????????????
     */
    @Test
    public void dupReport() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "????????????";

        logger.info("\n\n" + caseName + "\n");

        try {
            // ??????
            String customerPhone = "12300000001";

            String customerName = "??????FREEZE";

            String res = newCustomerNoCheckCode(maiTianChannelInt, maiTianStaffName, maiTianStaffPhone, "", "", customerPhone, customerName, "MALE");

            checkCode(res, StatusCode.BAD_REQUEST, "????????????");

            checkMessage("????????????", res, "?????????????????????????????????????????????????????????????????????");

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

//    --------------------------------------------??????????????????????????????-----------------------------------------------------------------------

    /**
     * ?????????????????? -> ??????????????????
     */
    @Test
    public void inProtectOthersFail() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "?????????????????? -> ??????????????????????????????";

        logger.info("\n\n" + caseName + "\n");

        try {

            String customerPhone = "12300000084";

            String customerName = "protect10000dayFREEZE";

//            ??????????????????
            String report2 = newCustomerNoCheckCode(maiTianChannelInt, maiTianStaffName, maiTianStaffPhone, anShengName, anShengPhone, customerPhone, customerName, "MALE");

            checkCode(report2, StatusCode.BAD_REQUEST, "??????????????????????????????");

            checkMessage("????????????", report2, "???????????????????????????????????????(newProtect10000day)?????????????????????????????????????????????");

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
     * ??????????????????????????????????????????????????????????????????
     */
    @Test
    public void InProtectOthersComplete() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "??????????????????-??????????????????????????????????????????????????????????????????????????????";

        logger.info("\n\n" + caseName + "\n");

        try {
            String customerPhone = "12300000084";

            String customerName = "protect10000dayFREEZE";

//            ???????????????????????????

            String cid = "REGISTER-cf78da86-5ec8-4f3c-86ce-ae041c58131b";//??????

            String res = customerEditPCNoCheckCode(cid, customerName, customerPhone, anShengName, anShengPhone);

            checkCode(res, StatusCode.BAD_REQUEST, "??????????????????????????????????????????????????????");

            checkMessage("????????????", res, "????????????????????????????????????????????????????????????????????????????????????");

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
     * ??????????????????????????????????????????????????????????????????
     */
    @Test
    public void InProtectOthersChange() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "??????????????????-??????????????????????????????????????????????????????????????????????????????";

        logger.info("\n\n" + caseName + "\n");

        try {
            // ??????
            String customerPhone = "12300000084";

            String customerName = "protect10000dayFREEZE";

//            ???????????????

            String cid = "REGISTER-8aa58ebb-1304-4e5a-acdb-99532dff0ca1";//??????
            String res = customerEditPCNoCheckCode(cid, customerName, customerPhone, anShengName, anShengPhone);

            checkCode(res, StatusCode.BAD_REQUEST, "??????????????????????????????????????????????????????");

            checkMessage("????????????", res, "????????????????????????????????????????????????????????????????????????????????????");
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

        String caseDesc = "????????????????????????" + number + "???";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = addRiskRuleNoCheckCode("test", number, "10");

            checkCode(addRiskRule, StatusCode.BAD_REQUEST, "????????????????????????" + number + "???");

            checkMessage("??????????????????", addRiskRule, "????????????????????????????????????");

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

        String caseDesc = "????????????????????????" + number + "???";

        logger.info("\n\n" + caseName + "\n");

        try {

            String ruleName = getNamePro();

            addRiskRule(ruleName, number + "", "10");

            JSONObject data = riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String name = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("??????????????????????????????" + ruleName + "??????????????????" + name + "???");
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

    @Test(dataProvider = "INVALID_NUM_PROTECT")
    public void ruleProtectInvalidNum(String number) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "?????????????????????" + number + "???";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = addRiskRuleNoCheckCode("test", "60", number);

            checkCode(addRiskRule, StatusCode.BAD_REQUEST, "");

            checkMessage("??????????????????", addRiskRule, "???????????????????????????10000???");

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

        String caseDesc = "?????????????????????" + number + "???";

        logger.info("\n\n" + caseName + "\n");

        try {

            String ruleName = getNamePro();

            addRiskRule(ruleName, number + "", "10");

            JSONObject data = riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String name = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("??????????????????????????????" + ruleName + "??????????????????" + name + "???");
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

        String caseDesc = "??????????????????????????????" + name + "???";

        logger.info("\n\n" + caseName + "\n");

        try {

            String addRiskRule = addRiskRuleNoCheckCode(name, "60", "60");

            checkCode(addRiskRule, StatusCode.BAD_REQUEST, "??????????????????");

            if ("".equals(name.trim())) {
                checkMessage("??????????????????", addRiskRule, "????????????????????????");
            } else if ("????????????".equals(name)) {
                checkMessage("??????????????????", addRiskRule, "???????????????????????????");
            } else {
                checkMessage("??????????????????", addRiskRule, "?????????????????????20?????????");
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

        String caseDesc = "??????????????????????????????" + name + "???";

        logger.info("\n\n" + caseName + "\n");

        try {
            addRiskRule(name, "60", "60");

            JSONObject data = riskRuleList();

            JSONArray list = data.getJSONArray("list");

            JSONObject ruleData = list.getJSONObject(list.size() - 1);

            String ruleName = ruleData.getString("name");
            if (!ruleName.equals(name)) {
                throw new Exception("??????????????????????????????" + ruleName + "??????????????????" + name + "???");
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

        String caseDesc = "??????????????????";

        logger.info("\n\n" + caseName + "\n");

        try {

            String s = deleteRiskRuleNoCheckCode(id);

            checkCode(s, StatusCode.BAD_REQUEST, caseDesc);

            if (defaultRuleId.equals(id)) {
                checkMessage("??????????????????", s, "??????????????????????????????");
            } else {
                checkMessage("??????????????????", s, "????????????????????????, ????????????");
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

//    ----------------------------------------------??????????????????---------------------------------------------

    @Test(dataProvider = "NEW_CUSTOMER_BAD")
    public void newCustomerBad(String message, int channelId, String channelStaffName, String channelStaffPhone, String adviserName,
                               String adviserPhone, String phone, String customerName, String gender) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + message;

        String caseDesc = "????????????-????????????";

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

    @Test
    public void newCustomerXML() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "????????????-xml??????";

        logger.info("\n\n" + caseName + "\n");

        try {

            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/newCustomerFile";
            dirPath = dirPath.replace("/", File.separator);
            File file = new File(dirPath);
            File[] files = file.listFiles();

            String xmlPath = "";

            for (int i = 0; i < files.length; i++) {
                xmlPath = dirPath + File.separator + files[i].getName();

                String res1 = importFile(xmlPath);

                checkCode(res1, StatusCode.BAD_REQUEST, files[i].getName() + ">>>");
            }

            xmlPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanDaily/Feidan.java";
            String res = importFile(xmlPath);
            checkCode(res, StatusCode.BAD_REQUEST, "??????java??????");
            checkMessage("??????java??????", res, "??????????????????????????????");

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

        String caseDesc = "?????????????????????";

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
                            throw new Exception("?????????????????????????????????customerId???????????????????????????customerId=" + customerId);
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
                    throw new Exception("??????????????????????????????????????????????????????customerId=" + customerId);
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
        String caseDesc = "???????????????-????????????-??????-??????-?????????????????????????????????-??????????????????????????????-??????????????????????????????";

        try {

            //        ?????????????????????
            String refreshCode = refreshQrcodeNoCheckCode();
            checkCode(refreshCode, StatusCode.SUCCESS, "??????OCR?????????");

            String codeB = JSON.parseObject(refreshCode).getJSONObject("data").getString("code");

//        ??????
            String confirmCode = confirmQrcodeNoCheckCode(codeB);

            checkCode(confirmCode, StatusCode.SUCCESS, "???????????????");

            String token = JSON.parseObject(confirmCode).getJSONObject("data").getString("token");

//        ??????????????????
//            String idCardPath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/idCard.png";
//            idCardPath = idCardPath.replace("/", File.separator);
//            String facePath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/share.png";
//            facePath = facePath.replace("/", File.separator);
//
//            ImageUtil imageUtil = new ImageUtil();
//            String imageBinary = imageUtil.getImageBinary(idCardPath);
//            imageBinary = stringUtil.trimStr(imageBinary);
//            String faceBinary = imageUtil.getImageBinary(facePath);
//            faceBinary = stringUtil.trimStr(faceBinary);
//
//            String ocrPicUpload = ocrPicUpload(token, imageBinary, faceBinary);
//            checkCode(ocrPicUpload, StatusCode.SUCCESS, "??????OCR????????????");


            String ocrPicUpload = feidan.ocrPicUpload(token, readTxt("src/main/java/com/haisheng/framework/testng/bigScreen/feidanOnline/idcard"), readTxt("src/main/java/com/haisheng/framework/testng/bigScreen/feidanOnline/idcard"));
            feidan.checkCode(ocrPicUpload, StatusCode.SUCCESS, "??????OCR????????????");

//        ??????
            refreshCode = refreshQrcodeNoCheckCode();
            checkCode(refreshCode, StatusCode.SUCCESS, "??????OCR?????????");

            String codeA = JSON.parseObject(refreshCode).getJSONObject("data").getString("code");

//        ???code??????
            String confirm = confirmQrcodeNoCheckCode(codeB);
            checkCode(confirm, StatusCode.BAD_REQUEST, "OCR??????-???????????????");

//        ???code??????
            confirm = confirmQrcodeNoCheckCode(codeA);
            checkCode(confirm, StatusCode.SUCCESS, "OCR??????-???????????????");

//            ???code????????????
            confirm = confirmQrcodeNoCheckCode(codeA);
            checkCode(confirm, StatusCode.SUCCESS, "??????OCR??????-???????????????");

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

    public static String readTxt(String filePath) {
        String lineTxt = null;
        try {
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);

                while ((lineTxt = br.readLine()) != null) {
//                    System.out.println(lineTxt);
                    return  lineTxt;
                }
                br.close();
            } else {
                System.out.println("???????????????!");
            }

        } catch (Exception e) {
            System.out.println("??????????????????!");
        }
        return  lineTxt;
    }

    @Test(dataProvider = "BAD_CHANNEL_STAFF")
    public void chanStaffSamePhoneToStaff(String caseDesc, String phoneNum, String message) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            String staffName = getNamePro();

//            ???????????????
            String res = addChannelStaffNoCode(maiTianChannelIdStr, staffName, phoneNum);
            checkCode(res, StatusCode.BAD_REQUEST, caseDesc);
            checkMessage(caseDesc, res, message);

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

    @Test(dataProvider = "BAD_ADVISER")
    public void adviserSamePhoneToChanStaff(String caseDesc, String phoneNum, String message) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            String staffName = getNamePro();

//            ??????????????????
            String addStaff = addAdviserNoCode(staffName, phoneNum, "");

            checkCode(addStaff, StatusCode.BAD_REQUEST, caseDesc);

            checkMessage(caseDesc, addStaff, message);
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

    @Test
    public void advisernewPicEditNoPic() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "???????????????????????????-???????????????????????????-????????????";

        logger.info("\n\n" + caseName + "\n");

        try {

            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages";

            String imagePath = dirPath + "/" + "Cris.jpg";
            imagePath = imagePath.replace("/", File.separator);
            JSONObject uploadImage = uploadImage(imagePath, "shopStaff");
            String phoneNum = genPhoneNum();
            String staffName = getNamePro();

//            ??????????????????
            addAdviser(staffName, phoneNum, uploadImage.getString("face_url"));

//            ????????????
            checkAdviserList(staffName, phoneNum, true);

//            ??????
            JSONObject staff = adviserList(phoneNum, 1, 1).getJSONArray("list").getJSONObject(0);
            String id = staff.getString("id");

            staffName = getNamePro();
            phoneNum = genPhoneNum();

            adviserEdit(id, staffName, phoneNum, "");

//            ??????
            checkAdviserList(staffName, phoneNum, false);

//            ??????
            adviserDelete(id);
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
    public void adviserNewNoPicEditPic() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "???????????????????????????-???????????????????????????-????????????";

        logger.info("\n\n" + caseName + "\n");

        try {

            String phoneNum = genPhoneNum();
            String staffName = getNamePro();

//            ??????????????????
            addAdviser(staffName, phoneNum, "");

//            ????????????
            checkAdviserList(staffName, phoneNum, false);

//            ??????
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages";

            String imagePath = dirPath + "/" + "Cris.jpg";
            imagePath = imagePath.replace("/", File.separator);
            JSONObject uploadImage = uploadImage(imagePath, "shopStaff");

            JSONObject staff = adviserList(phoneNum, 1, 1).getJSONArray("list").getJSONObject(0);
            String id = staff.getString("id");

            staffName = getNamePro();
            phoneNum = genPhoneNum();

            adviserEdit(id, staffName, phoneNum, uploadImage.getString("face_url"));

//            ??????
            checkAdviserList(staffName, phoneNum, true);

//            ??????
            adviserDelete(id);
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
    public void channelStaffCheck() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "???????????????-??????-??????-??????";

        try {

            String phoneNum = genPhoneNum();
            String staffName = getNamePro();

//            ???????????????
            addChannelStaff(protectChannelIdStr, staffName, phoneNum);

//            ????????????
            checkChannelStaffList(protectChannelIdStr, staffName, phoneNum, false);

//            ??????
            String dirPath = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages";

            String imagePath = dirPath + "/" + "Cris.jpg";
            imagePath = imagePath.replace("/", File.separator);
            JSONObject uploadImage = uploadImage(imagePath, "channelStaff");

            JSONObject staff = channelStaffList(protectChannelIdStr, 1, 10).getJSONArray("list").getJSONObject(0);
            String id = staff.getString("id");

            staffName = getNamePro();
            phoneNum = genPhoneNum();

            editChannelStaff(id, protectChannelIdStr, staffName, phoneNum, uploadImage.getString("face_url"));

//            ??????
            checkChannelStaffList(protectChannelIdStr, staffName, phoneNum, true);

//            ??????
            changeChannelStaffState(id);

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

    @Test
    public void disableThenReport() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "?????????????????????PC????????????????????????";

        try {

//            PC??????(???????????????????????????)
            String newCustomer = newCustomerNoCheckCode(maiTianChannelInt, maitianDisStaffName, maitianDisStaffPhone,
                    "", "", "12300000000", "name", "MALE");

            checkCode(newCustomer, StatusCode.BAD_REQUEST, caseDesc);

            checkMessage(caseDesc, newCustomer, "???????????????" + maitianDisStaffPhone + "??????????????????????????????????????????????????????????????????");
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

    @Test
    public void initAnatherSamePhoneStaff() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "???????????????????????????????????????";

        try {

            String id = "1156";//??????????????????FREEZE
            String res = changeStateNocode(id);

            checkCode(res, StatusCode.BAD_REQUEST, caseDesc);

            checkMessage(caseDesc, res, "????????????17610248107????????????????????????'??????'????????????,?????????????????????????????????/???????????????????????????????????????");

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

    @Test(dataProvider = "ORDER_LIST_CHECK")
    public void orderListCheck(String channelId, String status, String isAudited, String namePhone, int pageSize) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "";

        try {

            JSONArray list = orderList(channelId, status, isAudited, namePhone, 10).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);

                String channelName = single.getString("channel_name");
                if ("18".equals(channelId)) {
                    if (!"??????".equals(channelName)) {
                        throw new Exception("?????????????????????=????????????????????????????????????" + channelName + "??????????????????");
                    }
                } else if ("19".equals(channelId)) {
                    if (!"??????".equals(channelName)) {
                        throw new Exception("?????????????????????=????????????????????????????????????" + channelName + "??????????????????");
                    }
                }

                String statusRes = single.getString("status");
                if (!"".equals(status)) {
                    if (!status.equals(statusRes)) {
                        throw new Exception("???????????????status=" + status + "???????????????????????????status=" + statusRes + "?????????");
                    }
                }

                String isAuditedRes = single.getString("is_audited");
                if (!"".equals(isAudited)) {
                    if (!isAudited.equals(isAuditedRes)) {
                        throw new Exception("???????????????is_audited=" + isAudited + "???????????????????????????is_audited=" + isAuditedRes + "?????????");
                    }
                }

                String customerNameRes = single.getString("customer_name");
                if (!"".equals(namePhone)) {
                    if (!customerNameRes.contains(namePhone)) {
                        throw new Exception("???????????????namePhone=" + namePhone + "???????????????????????????namePhone=" + customerNameRes + "?????????");
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
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @Test
    public void activityBadName() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "????????????-???????????????????????????";

        try {

            String name = "~!@#$%^&*()_+~???@#???%??????&*????????????+??";
            String type = "OTHER";
            String contrastS = "2020-03-04";
            String contrastE = "2020-03-04";
            String start = "2020-03-13";
            String end = "2020-03-13";
            String influenceS = "2020-03-20";
            String influenceE = "2020-03-20";
            String s = addActivity(name, type, contrastS, contrastE, start, end, influenceS, influenceE);
            checkCode(s, StatusCode.BAD_REQUEST, caseDesc);
            checkMessage(caseDesc, s, "???????????????????????????????????????????????????????????????");
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

    @Test
    public void activityCheck() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "????????????-????????????";

        try {

            String name = caseName;
            String type = "OTHER";
            String contrastS = "2020-03-04";
            String contrastE = "2020-03-04";
            String start = "2020-03-13";
            String end = "2020-03-13";
            String influenceS = "2020-03-20";
            String influenceE = "2020-03-20";

//            ????????????
            String s = addActivity(name, type, contrastS, contrastE, start, end, influenceS, influenceE);
            checkCode(s, StatusCode.SUCCESS, caseDesc);

//            ????????????
            JSONArray list = listActivity(name, 1, 1).getJSONArray("list");
            String id = list.getJSONObject(0).getString("id");

//            ????????????
            deleteActivity(id);

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

    @Test
    public void deviceCheck() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "????????????????????????";

        try {

            checkDevice();

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

    @Test
    public void visitorSearchType() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "??????????????????-???????????????????????????";

        try {

            JSONObject typeList = personTypeList();

            JSONArray list = typeList.getJSONArray("list");

            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                String personType = single.getString("person_type");

                JSONArray catchList = catchList(personType, "", "", "", 1, 21).getJSONArray("list");

                for (int j = 0; j < catchList.size(); j++) {
                    JSONObject single1 = catchList.getJSONObject(j);
                    String personType1 = single1.getString("person_type");
                    Preconditions.checkArgument(personType.equals(personType1), "????????????????????????????????????person_type=" + personType +
                            "????????????????????????person_type=" + personType1 + "?????????");
                }
            }
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

    @Test
    public void visitorSearchDevice() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "??????????????????-??????????????????????????????";

        try {

            JSONArray devices = fetchDeviceList().getJSONArray("list");

            for (int i = 0; i < devices.size(); i++) {
                JSONObject single = devices.getJSONObject(i);
                String deviceId = single.getString("device_id");
                String deviceName = single.getString("device_name");

                JSONArray catchList = catchList("", deviceId, "", "", 1, 21).getJSONArray("list");

                for (int j = 0; j < catchList.size(); j++) {
                    JSONObject single1 = catchList.getJSONObject(j);
                    String cameraName = single1.getString("camera_name");
                    Preconditions.checkArgument(deviceName.equals(cameraName), "????????????????????????????????????camera_name=" + deviceName +
                            "????????????????????????camera_name=" + cameraName + "?????????");
                }
            }
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

    @Test(dataProvider = "CATCH_DATE")
    public void visitorSearchDate(String start, String end) {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "??????????????????-?????????????????????";

        DateTimeUtil dateTimeUtil = new DateTimeUtil();

        try {

            JSONArray catchList = catchList("", "", start, end, 1, 21).getJSONArray("list");

            if (start.equals(end)) {

                SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
                if (df1.format(new Date()).compareTo("08:00") < 0) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Preconditions.checkArgument(catchList.size() == 0, "????????????=" + df.format(new Date()) + "??????????????????");
                }
            } else {

                for (int i = 0; i < catchList.size(); i++) {
                    JSONObject single = catchList.getJSONObject(i);

                    String shootTime = dateTimeUtil.timestampToDate("yyyy-MM-dd", single.getLongValue("shoot_time"));
                    if (shootTime.compareTo(start) < 0 || shootTime.compareTo(end) > 0) {
                        throw new Exception("???????????????startTime=" + start + "???endTime=" + end + "????????????????????????" + shootTime + "???????????????");
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
            saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    //    ???????????????
//    @Test
    public void reSelf() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String caseDesc = "??????????????????-??????PC???????????????";

        try {

            String customerName = "??????FREEZE";
            String customerPhone = "12300000000";
            String smsCode = "805805";

            String res = selfRegisterHotNoCode(customerName, customerPhone, smsCode, "", 0, "MALE");
            checkCode(res, StatusCode.BAD_REQUEST, "????????????-????????????");
            checkMessage("????????????-????????????", res, "?????????????????????????????????????????????????????????????????????");

            String res1 = newCustomerNoCheckCode(-1, "", "", "", "", customerPhone, customerName, "MALE");
            checkCode(res1, StatusCode.BAD_REQUEST, "????????????-PC?????????????????????");
            checkMessage("????????????-PC???????????????", res1, "?????????????????????????????????????????????????????????????????????");

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


       // @Test
    public void witnessUploadChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        String function = "???????????????????????????>>>";

        try {

            String cardId = feidan.genCardId();
            String personName = "??????????????????";

            String s = witnessUpload(cardId, personName);

            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + s);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


//    -----------------------------------------------------------??????????????????------------------------------------------------------------


    public String genPhoneNum() {
        Random random = new Random();
        String num = "177" + (random.nextInt(89999999) + 10000000);

        return num;
    }

    public void checkAdviserList(String name, String phone, boolean hasPic) throws Exception {

        JSONObject staff = adviserList(phone, 1, 1).getJSONArray("list").getJSONObject(0);

        if (staff == null || staff.size() == 0) {
            throw new Exception("?????????????????????????????????=" + name + "????????????=" + phone);
        } else {
            checkUtil.checkKeyValue("????????????????????????", staff, "staff_name", name, true);
            checkUtil.checkKeyValue("????????????????????????", staff, "phone", phone, true);

            if (hasPic) {
                checkUtil.checkNotNull("????????????????????????", staff, "face_url");
            } else {
                checkUtil.checkNull("????????????????????????", staff, "face_url");
            }
        }
    }

    public void checkChannelStaffList(String channelId, String name, String phone, boolean hasPic) throws Exception {

        JSONObject staff = channelStaffList(channelId, 1, 1).getJSONArray("list").getJSONObject(0);

        if (staff == null || staff.size() == 0) {
            throw new Exception("??????????????????????????????????????????????????????=" + name + "????????????=" + phone);
        } else {
            checkUtil.checkKeyValue("???????????????????????????", staff, "staff_name", name, true);
            checkUtil.checkKeyValue("???????????????????????????", staff, "phone", phone, true);
            checkUtil.checkNotNull("???????????????????????????", staff, "total_report");

            if (hasPic) {
                checkUtil.checkNotNull("???????????????????????????", staff, "face_url");
            } else {
                checkUtil.checkNull("???????????????????????????", staff, "face_url");
            }
        }
    }

    public void checkDevice() throws Exception {

        JSONArray list = deviceList(1, 100).getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String statusName = single.getString("status_name");
            if (!"?????????".equals(statusName)) {
                String typeName = single.getString("type_name");
                Preconditions.checkArgument(single.getBooleanValue("error") == true, "????????????=" + typeName + "?????????=" + statusName + "????????????????????????");

            }
        }
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
            throw new Exception("?????????????????????status???" + status + ",path:" + path);
        }
    }

    public void checkMessage(String function, String response, String message) throws Exception {

        String messageRes = JSON.parseObject(response).getString("message");
        if (!message.equals(messageRes)) {
            throw new Exception(function + "???????????????????????????????????????=" + message + "?????????=" + messageRes);
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
            throw new Exception("order_id=" + orderId + "????????????????????????=" + num + "??????????????????????????????=" + riskNum);
        }
    }

    public void checkReport(String orderId, String orderType, int riskNum, String customerType, JSONObject orderDetail) throws Exception {

        String txtPath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/riskReport.txt";
        txtPath = txtPath.replace("/", File.separator);
        String pdfPath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/riskReport.pdf";
        pdfPath = pdfPath.replace("/", File.separator);

        String currentTime1 = dt.timestampToDate("yyyy???MM???dd??? HH:mm", System.currentTimeMillis());
        String pdfUrl = reportCreate(orderId).getString("file_url");

        File pdfFile = new File(pdfPath);
        File txtFile = new File(txtPath);
        pdfFile.delete();
        txtFile.delete();

//        ??????pdf
        downLoadPdf(pdfUrl);
        String currentTime = dt.timestampToDate("yyyy???MM???dd??? HH:mm", System.currentTimeMillis());

//        pdf???txt
        readPdf(pdfPath);

//        ??????????????????
        String noSpaceStr = removebreakStr(txtPath);

        logger.info("??????????????????????????????" + noSpaceStr);

//        ????????????????????????
        Link[] links = getLinkMessage(orderId);
//
        String message = "";

//            1.1????????????????????????
        DateTimeUtil dt = new DateTimeUtil();

        currentTime = stringUtil.trimStr(currentTime);
        currentTime1 = stringUtil.trimStr(currentTime1);

        if (!(noSpaceStr.contains(currentTime) || noSpaceStr.contains(currentTime1))) {
            message += "?????????????????????????????????????????????,????????????????????????????????????\n\n";
            System.out.println(currentTime);
            System.out.println(currentTime1);
            System.out.println(noSpaceStr);
        }

//            1.2???????????????
        if (!noSpaceStr.contains("?????????Demo")) {
            message += "????????????????????????????????????????????????Demo??????\n\n";
        }

//            2?????????????????????
        String s = "";

        if ("??????".equals(orderType)) {
            s = "????????????" + riskNum + "???????????????" + customerType;
        } else {
            s = "??????" + riskNum + "??????????????????" + customerType;
        }
        if (!noSpaceStr.contains(s)) {
            message += "??????????????????????????????????????????\n\n";
        }

//            ????????????
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

        s = "??????" + customerName + "????????????" + phone + "??????????????????" + adviserName + "????????????" + channelName + "???????????????" + channelStaffName + "????????????" + reportTime + "????????????" + firstAppearTime + "????????????" + dealTime + "??????????????????" + orderType;
        String tem = stringUtil.trimStr(s);
        if (!noSpaceStr.contains(tem)) {
            message += "?????????????????????????????????????????????";
        }

//            3???????????????
        for (int i = 0; i < links.length; i++) {
            Link link = links[i];
            s = stringUtil.trimStr(link.linkTime + link.linkName + link.content + link.linkPoint);
            if (!noSpaceStr.contains(s)) {

                message += "???????????????" + links[i].linkName +
                        "???????????????????????????????????????" + links[i].linkTime + "?????????????????????" + links[i].linkPoint + "???\n\n";
            }
        }

//            4?????????????????????
        if (noSpaceStr.contains("??????")) {
            message += "????????????\n\n";
        }

        if (!"".equals(message)) {
            throw new Exception("orderId=" + orderId + "??????????????????????????????\n\n" + message);
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
            if (b) {    //??????????????????????????????????????????????????????

                if (!srcFile.getName().endsWith("txt")) {    //???????????????TXT??????
                    System.out.println(srcFile.getName() + "??????TXT?????????");
                }
//                Runtime.getRuntime().exec("notepad " + srcFile.getAbsolutePath());//?????????????????????,?????????????????????????????????
                String str = null;
                String REGEX = "\\s+";    //?????????????????????????????????,\s??????????????????????????????????????????????????????????????????

                InputStreamReader stream = new InputStreamReader(new FileInputStream(srcFile), "UTF-8");    //???????????????????????????????????????UTF-8
                BufferedReader reader = new BufferedReader(stream);    ////??????????????????????????????????????????????????????????????????????????????????????????

//                File newFile = new File(srcFile.getParent(),
//                 "new" + srcFile.getName());    //???????????????????????????????????????

//                OutputStreamWriter outstream = new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8");    //???????????????
//                BufferedWriter writer = new BufferedWriter(outstream);
                Pattern patt = Pattern.compile(REGEX);    //??????Pattern??????????????????????????????

                while ((str = reader.readLine()) != null) {
                    Matcher mat = patt.matcher(str);    //?????????????????????????????????
                    str = mat.replaceAll("");

                    if (str == "") {    //??????????????????????????????????????????????????????????????????
                        continue;
                    } else {
                        noSpaceStr.append(str);
//                        writer.write(str);    //???????????????????????????????????????str+"\r\n" ???????????????????????????
                    }
                }
//                writer.close();
                reader.close();

                //????????????????????????
//                Runtime.getRuntime().exec("notepad " + newFile.getAbsolutePath());
//                System.out.println("?????????????????????");
            } else {
//                System.out.println("????????????????????????????????????????????????????????????");
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        return noSpaceStr.toString();
    }

    public static String readPdf(String fileStr) throws Exception {
        // ????????????
        boolean sort = false;
        File pdfFile = new File(fileStr);
        // ????????????????????????
        String textFile = null;
        // ????????????
        String encoding = "UTF-8";
        // ??????????????????
        int startPage = 1;
        // ??????????????????
        int endPage = Integer.MAX_VALUE;
        // ????????????????????????????????????
        Writer output = null;
        // ??????????????????PDF Document
        PDDocument document = null;
        try {

            //???????????????File
            document = PDDocument.load(pdfFile);

            // ?????????PDF??????????????????????????????txt??????
            textFile = fileStr.replace(".pdf", ".txt");

            // ?????????????????????????????????textFile
            output = new OutputStreamWriter(new FileOutputStream(textFile), encoding);
            // PDFTextStripper???????????????
            PDFTextStripper stripper = null;
            stripper = new PDFTextStripper();
            // ??????????????????
            stripper.setSortByPosition(sort);
            // ???????????????
            stripper.setStartPage(startPage);
            // ???????????????
            stripper.setEndPage(endPage);
            // ??????PDFTextStripper???writeText?????????????????????
            stripper.writeText(document, output);

            return textFile;
        } finally {
            if (output != null) {
                output.close();
            }
            if (document != null) {
                // ??????PDF Document
                document.close();
            }
        }
    }

    public void checkFirstVisitAndTrace(String orderId, JSONObject data, boolean expectExist) throws Exception {
        JSONArray linkLists = data.getJSONArray("list");

        boolean isExist = false;

        String functionPre = "orderId=" + orderId + "???";

        for (int i = 0; i < linkLists.size(); i++) {
            String function;
            JSONObject link = linkLists.getJSONObject(i);
            String linkKey = link.getString("link_key");
            String id = link.getString("id");

            function = functionPre + "??????id=" + id + "???";

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
                    throw new Exception("order_id=" + orderId + "?????????=" + linkName + "?????????id=" + id + "?????????????????????????????????????????????!");
                }
            }
        }

        if (isExist != expectExist) {
            throw new Exception("??????????????????????????????????????????" + expectExist + ",?????????" + isExist);
        }
    }

    public void checkNormalOrderLink(String orderId, JSONObject data) throws Exception {

        JSONArray linkLists = data.getJSONArray("list");

        for (int i = 0; i < linkLists.size(); i++) {
            JSONObject link = linkLists.getJSONObject(i);
            Integer linkStatus = link.getInteger("link_status");
            String linkName = link.getString("link_name");
            if (linkStatus != 1) {
                throw new Exception("order_id=" + orderId + "?????????=" + linkName + "?????????????????????????????????????????????!");
            }
        }
    }

    public void detailListLinkConsist(String orderId, String phone) throws Exception {

        JSONArray list = orderList(3, phone, 100).getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String orderIdRes = single.getString("order_id");

            if (orderId.equals(orderIdRes)) {

                JSONObject orderDetail = orderDetail(orderId);
                String function = "????????????>>>";

                String customerName = single.getString("customer_name");
                checkUtil.checkKeyValue(function, orderDetail, "customer_name", customerName, true);

                String adviserName = single.getString("adviser_name");
                if (adviserName == null || "".equals(adviserName)) {
                    String adviserNameDetail = orderDetail.getString("adviser_name");
                    if (adviserNameDetail != null && !"".equals(adviserNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",adviser_name?????????????????????????????????????????????=" + adviserNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "adviser_name", adviserName, true);
                }

                String channelName = single.getString("channel_name");
                if (channelName == null || "".equals(channelName)) {
                    String channelNameDetail = orderDetail.getString("channel_name");
                    if (channelNameDetail != null && !"".equals(channelNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",channel_name?????????????????????????????????????????????=" + channelNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "channel_name", channelName, true);
                }

                String channelStaffName = single.getString("channel_staff_name");
                if (channelStaffName == null || "".equals(channelStaffName)) {
                    String channelStaffNameDetail = orderDetail.getString("channel_staff_name");
                    if (channelStaffNameDetail != null && !"".equals(channelStaffNameDetail)) {
                        throw new Exception("orderId=" + orderId + ",channel_staff_name?????????????????????????????????????????????=" + channelStaffNameDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "channel_staff_name", channelStaffName, true);
                }

                String firstAppearTime = single.getString("first_appear_time");
                if (firstAppearTime == null || "".equals(firstAppearTime)) {
                    String firstAppearTimeDetail = orderDetail.getString("first_appear_time");
                    if (firstAppearTimeDetail != null && !"".equals(firstAppearTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",first_appear_time?????????????????????????????????????????????=" + firstAppearTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "first_appear_time", firstAppearTime, true);
                }

                String dealTime = single.getString("deal_time");
                if (dealTime == null || "".equals(dealTime)) {
                    String dealTimeDetail = orderDetail.getString("deal_time");
                    if (dealTimeDetail != null && !"".equals(dealTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",deal_time?????????????????????????????????????????????=" + dealTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "deal_time", dealTime, true);
                }

                String reportTime = single.getString("report_time");
                if (reportTime == null || "".equals(reportTime)) {
                    String reportTimeDetail = orderDetail.getString("report_time");
                    if (reportTimeDetail != null && !"".equals(reportTimeDetail)) {
                        throw new Exception("orderId=" + orderId + ",report_time?????????????????????????????????????????????=" + reportTimeDetail);
                    }
                } else {
                    checkUtil.checkKeyValue(function, orderDetail, "report_time", reportTime, true);
                }

                String isAudited = single.getString("is_audited");
                if (isAudited == null || "".equals(isAudited)) {
                    String isAuditedDetail = orderDetail.getString("report_time");
                    if (isAuditedDetail != null && !"".equals(isAuditedDetail)) {
                        throw new Exception("orderId=" + orderId + ",is_audited?????????????????????????????????????????????=" + isAuditedDetail);
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
        // ???????????????
        InputStream inputStream = conn.getInputStream();
        // ??????????????????
        byte[] getData = readInputStream(inputStream);
        // ??????????????????
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

    public void checkDetail(String orderId, String customerName, String phone, String adviserName, String
            channelName,
                            String channelStaffName, String orderStatusTips, String faceUrl, String firstAppearTime,
                            String reportTime, JSONObject orderDetail) throws Exception {

        String function = "????????????-orderId=" + orderId + "???";

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
                        throw new Exception("order_id" + orderId + "????????????" + linkKey + "???????????????????????????????????????????????????");
                    }

                    String linkPointRes = link.getString("link_point");

                    if (!linkPoint.equals(linkPointRes)) {
                        throw new Exception("order_id=" + orderId + "????????????" + linkKey + "???????????????????????????" + linkPoint + "????????????????????????" + linkPointRes + "???");
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

    public String getNamePro() {

        String tmp = UUID.randomUUID() + "";

        return tmp.substring(tmp.length() - 5);
    }

//-----------------------------------------------------????????????---------------------------------------------------------

    //    ------------------------------------------------???????????????-------------------------------------------
    public void changeChannelStaffState(String staffId) throws Exception {
        String json = "{}";

        httpPostWithCheckCode("/risk/channel/staff/state/change/" + staffId, json);
    }


    public JSONObject editChannelStaff(String id, String channelId, String staffName, String phone, String faceUrl) throws Exception {

        String url = "/risk/channel/staff/edit/" + id;

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"channel_id\":\"" + channelId + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject channelStaffList(String channelId, int page, int size) throws Exception {

        String url = "/risk/channel/staff/page";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"channel_id\":\"" + channelId + "\"," +
                        "    \"page\":\"" + page + "\"," +
                        "    \"size\":\"" + size + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject addChannelStaff(String channelId, String staffName, String phone) throws Exception {

        String url = "/risk/channel/staff/register";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"channel_id\":\"" + channelId + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String addChannelStaffNoCode(String channelId, String staffName, String phone) throws Exception {

        String url = "/risk/channel/staff/register";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"channel_id\":\"" + channelId + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}";

        String res = httpPost(url, json);

        return res;
    }

    //    ---------------------------------------------------------????????????----------------------------------------------
    public JSONObject adviserDelete(String id) throws Exception {
        String url = "/risk/staff/delete/" + id;
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject adviserEdit(String id, String staffName, String phone, String faceUrl) throws Exception {

        String url = "/risk/staff/edit/" + id;

        String json =
                "{\n" +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject adviserList(String namePhone, int page, int size) throws Exception {
        String url = "/risk/staff/page";
        String json =
                "{\n" +
                        "\"name_phone\":\"" + namePhone + "\"," +
                        "\"page\":\"" + page + "\"," +
                        "\"size\":\"" + size + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject addAdviser(String staffName, String phone, String faceUrl) throws Exception {

        String url = "/risk/staff/add";

        String json =
                "{\n" +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String addAdviserNoCode(String staffName, String phone, String faceUrl) throws Exception {

        String url = "/risk/staff/add";

        String json =
                "{\n" +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPost(url, json);

        return res;
    }

//    ------------------------------------------------------??????OCR------------------------------------------------------
    /**
     * 17.3 OCR???????????????-H5
     */
    public static String OCR_PIC_UPLOAD_JSON = "{\"shop_id\":${shopId},\"token\":\"${token}\"," +
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

    /**
     * OCR???????????????-PC
     */
    public JSONObject refreshQrcode() throws Exception {

        String url = "/risk/shop/ocr/qrcode/refresh";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 17.3 OCR???????????????-H5
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

//    --------------------------------------------???????????????--------------------------------------------

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


//    -------------------------------------------??????????????????-------------------------------------------------

    public JSONObject catchList(String customerType, String deviceId, String startTime, String endTime, int page,
                                int size) throws Exception {
        String url = "/risk/evidence/person-catch/page";
        String json =
                "{\n";
        if (!"".equals(customerType)) {
            json += "\"person_type\":\"" + customerType + "\",";
        }

        if (!"".equals(deviceId)) {
            json += "\"device_id\":\"" + deviceId + "\",";
        }

        if (!"".equals(startTime)) {
            json += "\"start_time\":\"" + startTime + "\"," +
                    "\"end_time\":\"" + endTime + "\",";
        }

        json += "\"page\":\"" + page + "\"," +
                "\"size\":\"" + size + "\"," +
                "\"shop_id\":" + getShopId() +
                "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


//    ------------------------------------------------------????????????--------------------------------------------------

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
     * 16.1 ??????????????????
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
     * 16.1 ??????????????????
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
     * 16.1 ??????????????????
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
     * 16.1 ??????????????????
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


//    --------------------------------------??????--------------------------------------------------

    /**
     * 3.10 ??????????????????
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

        Thread.sleep(1000);

        return res;
    }

    /**
     * 3.4 ????????????
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
     * 9.6 ????????????
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

    public String changeStateNocode(String id) throws Exception {
        String url = "/risk/channel/staff/state/change/" + id;
        String json =
                "{}";

        String res = httpPost(url, json);

        return res;
    }

//    ----------------------------------------------------??????----------------------------------------------------

    /**
     * 4.6 ????????????????????????
     */
    public JSONObject orderLinkList(String orderId) throws Exception {
        String url = "/risk/order/link/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"orderId\":\"" + orderId + "\"," +
                        "    \"page\":\"" + 1 + "\"," +
                        "    \"size\":\"" + 1000 + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);//???????????????????????????????????????json??????

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

    public JSONObject orderList(String channelId, String status, String isAudited, String namePhone, int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"page\":1" + ",";
        if (!"".equals(status)) {
            json += "    \"status\":\"" + status + "\",";
        }

        if (!"".equals(namePhone)) {
            json += "    \"customer_name\":\"" + namePhone + "\",";
        }

        if (!"".equals(isAudited)) {
            json += "    \"is_audited\":\"" + isAudited + "\",";
        }

        if (!"".equals(channelId)) {
            json += "    \"channel_id\":\"" + channelId + "\",";
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

    public static String ORDER_DETAIL_JSON = "{\"order_id\":\"${orderId}\"," +
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
     * 4.15 ????????????
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
     * 15.2 ???????????????
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

    //    ----------------------------------??????------------------------------------------------------
    public String addActivity(String name, String type, String contrastS, String contrastE, String startDate, String endDate,
                              String influenceS, String influenceE) throws Exception {
        String url = "/risk/manage/activity/add";
        String json =
                "{\n" +
                        "    \"activity_name\":\"" + name + "\"," +
                        "    \"activity_type\":\"" + type + "\"," +
                        "    \"contrast_start\":\"" + contrastS + "\"," +
                        "    \"contrast_end\":\"" + contrastE + "\"," +
                        "    \"start_date\":\"" + startDate + "\"," +
                        "    \"end_date\":\"" + endDate + "\"," +
                        "    \"influence_start\":\"" + influenceS + "\"," +
                        "    \"influence_end\":\"" + influenceE + "\"," +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}";

        String res = httpPost(url, json);

        return res;
    }

    public String deleteActivity(String id) throws Exception {
        String url = "/risk/manage/activity/delete";
        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\"," +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return res;
    }

    public JSONObject listActivity(String name, int page, int pageSize) throws Exception {
        String url = "/risk/manage/activity/list";
        String json =
                "{\n" +
                        "    \"activity_name\":\"" + name + "\"," +
                        "    \"page\":\"" + page + "\"," +
                        "    \"size\":\"" + pageSize + "\"," +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject deviceList(int page, int pageSize) throws Exception {
        String url = "/risk/device/page";
        String json =
                "{\n" +
                        "    \"page\":\"" + page + "\"," +
                        "    \"page_size\":\"" + pageSize + "\"," +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject personTypeList() throws Exception {
        String url = "/risk/evidence/person-type/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject fetchDeviceList() throws Exception {
        String url = "/risk/evidence/face-catch/devices";
        String json =
                "{\n" +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject selfRegisterHot(String customerName, String phone, String verifyCode, String adviserId, int
            hotPoints, String gender) throws Exception {
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

        if (hotPoints == 0) {
            json += "    \"hot_points\":[],";
        } else if (hotPoints == 1) {
            json += "    \"hot_points\":[10],";
        } else if (hotPoints == 2) {
            json += "    \"hot_points\":[10,11],";
        } else if (hotPoints == 3) {
            json += "    \"hot_points\":[10,11,12],";
        }

        json += "    \"shop_id\":" + getShopId() + "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String selfRegisterHotNoCode(String customerName, String phone, String verifyCode, String adviserId, int
            hotPoints, String gender) throws Exception {
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

        if (hotPoints == 0) {
            json += "    \"hot_points\":[],";
        } else if (hotPoints == 1) {
            json += "    \"hot_points\":[10],";
        } else if (hotPoints == 2) {
            json += "    \"hot_points\":[10,11],";
        } else if (hotPoints == 3) {
            json += "    \"hot_points\":[10,11,12],";
        }

        json += "    \"shop_id\":" + getShopId() + "}";

        String res = httpPost(url, json);

        return res;
    }


//    --------------------------------??????---------------------------------------------

    public JSONObject uploadImage(String imagePath, String pathText) {
        String url = "http://store.winsenseos.com/risk/imageUpload";
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

            builder.addTextBody("path", pathText, ContentType.MULTIPART_FORM_DATA);

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

    public String importFile(String imagePath) {
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

        return this.response;
    }

    public String httpPostNoPrintPara(String path, String json) throws Exception {
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

    public void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("?????????");
        aCase.setExpect("code==1000");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            if (failReason.contains("java.lang.Exception:")) {
                failReason = failReason.replace("java.lang.Exception", "??????");
            } else if (failReason.contains("java.lang.AssertionError")) {
                failReason = failReason.replace("java.lang.AssertionError", "??????");
            }
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    public void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);

        if (!StringUtils.isEmpty(aCase.getFailReason())) {

            logger.error(aCase.getFailReason());

            String failReason = aCase.getFailReason();

            dingPush("???????????? \n" +
                    "?????????" + aCase.getCaseDescription() +
                    " \n\n" + failReason);
        }
    }

    public void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();

        String s = DEBUG.trim().toLowerCase();

        if (DEBUG.trim().toLowerCase().equals("false")) {

//            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
            alarmPush.setDingWebhook(DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }

        alarmPush.onlineRgn(msg);
        this.FAIL = true;

        Assert.assertNull(aCase.getFailReason());
    }

    public void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 ?????????
            //18513118484 ??????
            //15011479599 ?????????
            //18600872221 ?????????
            String[] rd = {"18513118484", //??????
                    "15011479599", //?????????
                    "15898182672"}; //?????????
            alarmPush.alarmToRd(rd);
        }
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
                "qwer@tyui&opas.dfgh#???",
                "qwer tyui opas dfg  h",
                "????????????"
        };
    }

    @DataProvider(name = "VALID_NAME")
    public Object[] validName() {
        return new Object[]{
                "????????????-??????V3.0"
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
                        "?????????????????????", maiTianChannelInt, maiTianStaffName, maiTianStaffPhone, anShengName, anShengPhone, "12300000001", "", "MALE"
                },
                new Object[]{
                        "????????????????????????", maiTianChannelInt, maiTianStaffName, maiTianStaffPhone, anShengName, anShengPhone, "", "name", "MALE"
                },
                new Object[]{
                        "?????????????????????", maiTianChannelInt, maiTianStaffName, maiTianStaffPhone, anShengName, anShengPhone, "12300000001", "name", ""
                },
                new Object[]{
                        "??????????????????????????????", maiTianChannelInt, maiTianStaffName, maiTianStaffPhone, anShengName, "", "12300000001", "name", "MALE"
                },
                new Object[]{
                        "??????????????????????????????", maiTianChannelInt, maiTianStaffName, maiTianStaffPhone, anShengName, "123****0000", "12300000001", "name", "MALE"
                },
                new Object[]{
                        "?????????????????????????????????????????????", maiTianChannelInt, maiTianStaffName, maiTianStaffPhone, "name", anShengPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "???????????????????????????", maiTianChannelInt, maiTianStaffName, "", anShengName, anShengPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "???????????????????????????", maiTianChannelInt, maiTianStaffName, "123****1111", anShengName, anShengPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "????????????????????????", maiTianChannelInt, "", maiTianStaffPhone, anShengName, anShengPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "??????????????????????????????????????????", maiTianChannelInt, "name", maiTianStaffPhone, anShengName, anShengPhone, "12300000001", "name", "MALE"
                },
                new Object[]{
                        "?????????????????????????????????", maiTianChannelInt, "", "", anShengName, anShengPhone, "12300000001", "name", "MALE"
                },
        };
    }

    @DataProvider(name = "BAD_CHANNEL_STAFF")
    public Object[][] badChannelStaff() {
        return new Object[][]{
//String channelId, int status, boolean isAudited, String namePhone, int pageSize
                new Object[]{
                        "???????????????????????????????????????????????????", "12300000002", "???????????????????????????????????????????????????????????????????????????"
                },
                new Object[]{
                        "??????????????????????????????????????????????????????????????????", "17610248107", "???????????????17610248107????????????"
                },
                new Object[]{
                        "??????????????????????????????????????????????????????????????????", "12300000012", "???????????????12300000012??????????????????????????????????????????????????????????????????"
                },
                new Object[]{
                        "?????????????????????????????????????????????????????????????????????", "12300000013", "???????????????12300000013????????????"
                }
        };
    }

    @DataProvider(name = "BAD_ADVISER")
    public Object[][] badAdviser() {
        return new Object[][]{
//String channelId, int status, boolean isAudited, String namePhone, int pageSize
                new Object[]{
                        "????????????????????????????????????????????????????????????", "17610248107", "???????????????????????????"
                },
                new Object[]{
                        "??????????????????????????????????????????????????????", "12300000002", "???????????????????????????"
                }
        };
    }

    @DataProvider(name = "ORDER_LIST_CHECK")
    public Object[][] orderListCheck1() {
        return new Object[][]{
//String channelId, int status, boolean isAudited, String namePhone, int pageSize
                new Object[]{
                        "19", "1", "true", "?????????", 10
                },
                new Object[]{
                        "19", "2", "false", "", 10
                },
                new Object[]{
                        "18", "3", "true", "", 10
                },
        };
    }

    @DataProvider(name = "CATCH_DATE")
    public Object[][] catchDate() {
        return new Object[][]{
//start,end
                new Object[]{
                        LocalDate.now().toString(), LocalDate.now().toString()
                },
                new Object[]{
                        LocalDate.now().minusDays(7).toString(), LocalDate.now().toString()
                },
                new Object[]{
                        LocalDate.now().minusDays(30).toString(), LocalDate.now().toString()
                },
                new Object[]{
                        LocalDate.now().minusDays(365).toString(), LocalDate.now().toString()
                },
        };
    }
}

