package com.haisheng.framework.testng.bigScreen.feidanOnline;

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
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : lvxueqing
 * @date :  2020-03-25 15:23
 */

public class FeidanMiniApiInterfaceNotNullOnline {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    public String failReason = "";
    public String response = "";
    public boolean FAIL = false;
    public Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    public QADbUtil qaDbUtil = new QADbUtil();
    public int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    public int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_ONLINE_SERVICE;

    public String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-online-test/buildWithParameters?case_name=";

    public String DEBUG = System.getProperty("DEBUG", "true");

    public String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    public HttpConfig config;

    String channelId = "19";

    String genderMale = "MALE";
    String genderFemale = "FEMALE";

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
            //throw new RuntimeException("?????????http????????????", e);
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

    public String httpPost(String path, String json, String... checkColumnNames) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    public void checkCode(String response, int expect, String message) throws Exception {
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
            throw new Exception("?????????????????????status???" + status + ",path:" + path);
        }
    }

    public String getStartTime(int n) throws ParseException { //??????n??????????????????????????????0??????
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, - n);
        Date d = c.getTime();
        String day = format.format(d);
        return day;
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



    /**
     * ?????? ???????????????/risk/shop/list???????????????
     */
    @Test
    public void shopListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "????????????????????????/risk/shop/list?????????????????????\n";

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
     * ?????? ???????????????/risk/channel/list???????????????
     */
    @Test
    public void channelListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "????????????????????????/risk/channel/list?????????????????????\n";

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
     * ?????? ?????????????????????/risk/staff/consultant/list??? ????????????
     */
    @Test
    public void consultantListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "??????????????????????????????/risk/staff/consultant/list?????????????????????\n";

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
     * ?????? ?????????????????????/risk/staff/type/list??? ???????????? ????????????????????????
     */
    @Test
    public void stafftypeListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "??????????????????????????????/risk/staff/type/list?????????????????????\n";

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
     * ?????? ??????????????? ????????????
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
                throw new Exception("??????????????????[qrcode]?????????\n");
            }
            String url = data.getString("url");
            if (url == null || "".equals(url.trim())) {
                throw new Exception("??????????????????[url]?????????\n");
            } else if (url.contains(".cn")) {
                throw new Exception("??????????????????[url]????????????url??? url: " + url + "\n");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "?????????????????????????????????");
        }
    }

    /**
     * ?????? ???????????????/risk/order/list???????????????
     */
    @Test
    public void orderDetailNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "???????????????????????????????????????\n";

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
     * ?????? ?????????????????????/risk/order/link/list??? ????????????
     */
    @Test
    public void linkNoteNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "???????????????????????????????????????\n";

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
     * ?????? ???????????????/risk/customer/list??? ????????????
     */
    @Test
    public void customerListNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "???????????????????????????????????????\n";

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
     * ?????? ???????????????/risk/order/list??? ????????????
     */
    @Test
    public void orderListNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "???????????????????????????????????????\n";

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
     * ?????? ???????????????/risk/channel/detail??? ????????????
     */
    @Test
    public void channelDetailNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "???????????????????????????????????????\n";

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
     * V 2.4?????? ??????????????????/risk/evidence/risk-report/download??? ???????????? ???????????????????????????
     */
    @Test
    public void reportDownloadNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "??????????????????????????????????????????\n";

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
            saveData(aCase, ciCaseName, caseName, "??????????????????????????????????????????\n");
        }
    }



    /**
     * V2.4?????? ?????????????????????/risk/evidence/face/traces??? ????????????
     */
    @Test
    public void faceTracesNotNullChk() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "???????????????????????????????????????\n";

        String key = "";

        try {

            JSONObject data = faceTraces("https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/witness/100000000000235625/d020e3fe-8050-47bb-9c16-49a2aebdc8f0?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1612575519&Signature=5nntV5uCcxSdhDul3HP4FcJeQDg%3D"); //?????????????????????
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
     * V3.0?????? ?????????????????????/risk/channel/page???????????????
     */
    @Test
    public void channelPageNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "??????????????????????????????/risk/channel/page?????????????????????\n";

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
     * V3.0 ?????? ?????????????????????(2020.02.12)???/risk/channel/staff/list???????????????
     */
    @Test
    public void  channelStaffListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "?????????????????????????????????/risk/channel/staff/list?????????????????????\n";

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
     * V3.0 ?????? ??????top6(2020.02.12)???/risk/staff/top/???????????????
     */
    @Test
    public void  riskStaffTopNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "???????????????top6???/risk/staff/top/?????????????????????\n";

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
     * V3.0 ?????? ??????????????????(2020-02-12)???/risk/evidence/person-catch/page???????????????
     */
    @Test
    public void  personCatchNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "??????????????????????????????/risk/evidence/person-catch/page?????????????????????\n";

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
     * V3.0 ?????? ??????????????????(2020.02.12)???/risk/rule/list???????????????
     */
    @Test
    public void  riskRuleListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "??????????????????????????????/risk/rule/list?????????????????????\n";

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
     * V3.0 ?????? OCR???????????????-PC(2020.02.12)???/risk/shop/ocr/qrcode???????????????  ?????? ??????
     */
    @Test
    public void  ocrQrcodeNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "?????????OCR???????????????-PC???/risk/shop/ocr/qrcode?????????????????????\n";

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
     * V3.0 ?????? ??????????????????(2020.02.12)???/risk/history/rule/detail???????????????  ?????? ??????
     */
    @Test
    public void  historyRuleDetailNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "??????????????????????????????/risk/history/rule/detail?????????????????????\n";

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
     * V3.0 ?????? ??????????????????(2020.02.12)???/risk/history/rule/order/trend???????????????  ?????? ??????
     */
    @Test
    public void  historyOrderTrendNotNullChk() throws ParseException {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "??????????????????????????????/risk/history/rule/order/trend?????????????????????\n";

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
     * V3.0 ?????? ??????????????????(2020.02.12)???/risk/history/rule/customer/trend???????????????  ?????? ??????
     */
    @Test
    public void  historyCustomerTrendNotNullChk() throws ParseException {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "??????????????????????????????/risk/history/rule/customer/trend?????????????????????\n";

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



    /**
     * V3.1 ?????? ?????????????????? (2020-03-02)???/risk/channel/report/statistics???????????????
     */
    @Test
    public void  channelReptstatisticsNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "??????????????????????????????/risk/channel/report/statistics?????????????????????\n";
        String key = "";
        try {
            JSONObject data = channelReptstatistics();
            for (Object obj :  channelReptstatisticsNotNull()) {
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
     * V3.1 ?????? ??????????????????-?????????2020-03-02??????/risk/device/page???????????????
     */
    @Test
    public void  devicePageNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "????????? ??????????????????-?????????/risk/device/page?????????????????????\n";
        String key = "";
        try {
            JSONObject data = deviceList(1,10);
            for (Object obj :  devicePageNotNull()) {
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







    public Object getShopId() {
        return "97";
    }

    public final int pageSize = 50;


    public static final String ORDER_DETAIL = "/risk/order/detail";
    public static final String CUSTOMER_LIST = "/risk/customer/list";
    public static final String ORDER_LIST = "/risk/order/list";
    public static final String STAFF_LIST = "/risk/staff/page";
    public static final String STAFF_TYPE_LIST = "/risk/staff/type/list";



    public static String CUSTOMER_LIST_JSON = "{\"search_type\":\"${searchType}\"," +
            "\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";



    //    ??????????????????size??????????????????????????????????????????????????????pageSize??????
    public static String ORDER_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"page_size\":\"${pageSize}\"}";

    public static String ORDER_DETAIL_JSON = "{\"order_id\":\"${orderId}\"," +
            "\"shop_id\":${shopId}}";

    public static String ORDER_STEP_LOG_JSON = ORDER_DETAIL_JSON;


    public static String STAFF_TYPE_LIST_JSON = "{\"shop_id\":${shopId}}";

    public static String STAFF_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";







    public String getValue(JSONObject data, String key) {
        String value = data.getString(key);

        if (value == null || "".equals(value)) {
            value = "";
        }

        return value;
    }


    /**
     * ????????????
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
     * ??????????????????????????????
     */
    public JSONObject shopList() throws Exception {
        String url = "/risk/shop/list";
        String json = "{}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * ????????????
     */
    public JSONObject channelList() throws Exception {
        String url = "/risk/channel/list";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * ??????????????????
     */
    public JSONObject consultantList() throws Exception {
        String url = "/risk/staff/consultant/list";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * ??????????????????
     */
    public JSONObject stafftypeList() throws Exception {
        String url = "/risk/staff/type/list";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * ????????????
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
     * ????????????
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


    /*???????????????????????????????????????*/
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
     * ????????????????????????
     */
    public JSONObject orderLinkList(String orderId) throws Exception {
        String url = "/risk/order/link/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"orderId\":\"" + orderId + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);//???????????????????????????????????????json??????

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * ????????????
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
     * ?????????????????????????????????
     */
    public JSONObject registerQrCode() throws Exception {

        String requestUrl = "/risk/shop/self-register/qrcode";

        String json = "{\"shop_id\":" + getShopId() + "}";

        String res = httpPostWithCheckCode(requestUrl, json);
        JSONObject data = JSON.parseObject(res).getJSONObject("data");

        return data;
    }

    /**
     * ???????????????
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
     * ????????????
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
     * ??????????????????
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
     * ?????????????????????(2020.02.12)
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
     * ??????top6(2020.02.12)
     */
    public JSONObject riskStaffTop() throws Exception {
        String url = "/risk/staff/top/";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * ??????????????????(2020-02-12)
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
     * ??????????????????(2020.02.12)
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
     * OCR???????????????-PC(2020.02.12)
     */
    public JSONObject ocrQrcode() throws Exception {
        String url = "/risk/shop/ocr/qrcode";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * ??????????????????(2020.02.12)
     */
    public JSONObject historyRuleDetail() throws Exception {
        String url = "/risk/history/rule/detail";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * ??????????????????(2020.02.12)
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
     *  ??????????????????(2020.02.12)
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

    /**
     * ?????????????????? (2020-03-02)
     */
    public JSONObject channelReptstatistics() throws Exception {
        String url = "/risk/channel/report/statistics";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * ??????????????????-?????????2020-03-02???
     */
    public JSONObject devicePage() throws Exception {
        String url = "/risk/device/page";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     *????????????
     */
    public JSONObject deviceList(int page, int size) throws Exception {
        String url = "/risk/device/page";
        String json =
                "{\n" +
                        "\"page\":\"" + page + "\"," +
                        "\"size\":\"" + size + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

//    --------------------------------------------------------????????????-------------------------------------------------------


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
            dingPush("???????????? \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    public void dingPush(String msg) {
        if (DEBUG.trim().toLowerCase().equals("false")) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP);

            alarmPush.onlineRgn(msg);
            this.FAIL = true;
        }
        Assert.assertNull(aCase.getFailReason());
    }

    public void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP);

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

    @DataProvider(name = "SEARCH_TYPE")
    public static Object[] searchType() {
        return new Object[]{
                "CHANCE",
//                "CHECKED",
//                "REPORTED"
        };
    }

    @DataProvider(name = "ALL_DEAL_IDCARD_PHONE")
    public static Object[][] dealIdCardPhone() {
        return new Object[][]{
                new Object[]{
                        "17800000002", "111111111111111111", "?????????", "2019-12-13 13:44:26"
                },
                new Object[]{
                        "19811111111", "222222222222222222", "?????????", "2019-12-13 13:40:53"
                },
                new Object[]{
                        "18831111111", "333333333333333333", "?????????", "2019-12-13 15:27:22"
                },
                new Object[]{
                        "18888811111", "333333333333333335", "?????????", "2019-12-13 15:05:53"
                },
                new Object[]{
                        "14111111135", "111111111111111114", "?????????", "2019-12-17 16:51:31"
                }
        };
    }

    @DataProvider(name = "ALL_DEAL_PHONE")
    public static Object[] dealPhone() {
        return new Object[]{
                "17800000002",
                "19811111111",
                "18831111111",
                "18888811111",
                "14111111135"
        };
    }



    public Object[] customerListNotNull() {
        return new Object[]{
                "[list]-customer_name",
                "[list]-phone",
                "[list]-gmt_create"
        };
    }

    public Object[] orderListNotNull() {
        return new Object[]{
                "[list]-order_id",
                "[list]-customer_name",
                "[list]-customer_phone",
                "[list]-is_audited",

        };
    }

    public Object[] orderLinkListNotNull() {
        return new Object[]{

        };
    }

    public Object[] channelDetailNotNull() {
        return new Object[]{
                "channel_id",
                "channel_name",
                "owner_principal",
                "phone",
                "rule_id", //V3.0
                "rule_name", //V3.0
        };
    }

    public Object[] shopListNotNotNull() {
        return new Object[]{
                "[list]-shop_id",
                "[list]-auth_level",
                "[list]-shop_name",
        };
    }

    public Object[] channelListNotNull() {
        return new Object[]{
                "[list]-channel_id",
                "[list]-channel_name",
//                "[list]-owner_principal",
                "[list]-phone",
                "[list]-register_time",
        };
    }

    public Object[] consultantListNotNull() {
        return new Object[]{
                "[list]-id", //V3.0
                "[list]-staff_id", //V3.0
                "[list]-staff_name", //V3.0
                "[list]-phone", //V3.0
        };
    }

    public Object[] stafftypeListNotNull() {
        return new Object[]{
                "[list]-type_name",
                "[list]-staff_type",
        };
    }

    public Object[] orderDetailNotNull() {
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

    public Object[] reportDownloadNotNull() {
        return new Object[]{
                "file_url",
        };
    }

    public Object[] faceTracesNotNull() {
        return new Object[]{
                "[list]-shoot_time",
                "[list]-camera_name",
                "[list]-face_url",
                "[list]-similar",
                "[list]-original_url",
        };
    }

    public Object[] channelPageNotNull(){
        return new Object[]{
                "[list]-channel_id",
                "[list]-channel_name",
//                "[list]-owner_principal",
                "[list]-total_customers",
                "[list]-register_time",
                "[list]-rule_id",
                "[list]-rule_name",
        };
    }

    public Object[] channelStaffListNotNull(){
        return new Object[]{
                "[list]-id",
                "[list]-staff_id",
                "[list]-staff_name",
                "[list]-phone",
        };
    }

    public Object[] riskStaffTopNotNull(){
        return new Object[]{
                "[list]-id",
                "[list]-staff_id",
                "[list]-staff_name",
                "[list]-phone",
        };
    }

    public Object[] personCatchNotNull(){
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

    public Object[] riskRuleListNotNull(){
        return new Object[]{
                "[list]-id",
                "[list]-name",
                "[list]-ahead_report_time",
                "[list]-type",
                "[list]-gmt_create",
        };
    }

    public Object[] ocrQrcodeNotNull(){ //?????? ??????
        return new Object[]{
                "qrcode",
                "url",
                "code",
        };
    }

    public Object[] historyRuleDetailNotNull(){
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

    public Object[] historyOrderTrendNotNull(){
        return new Object[]{
                "[list]-day",
                "[list]-all_order",
                "[list]-normal_order",
                "[list]-unknow_order",
                "[list]-risk_order",
        };
    }

    public Object[] historyCustomerTrendNotNull(){
        return new Object[]{
                "[list]-all_visitor",
                "[list]-channel_visitor",
                "[list]-day",
                "[list]-natural_visitor",
        };
    }

    public Object[] channelReptstatisticsNotNull(){
        return new Object[]{
                "customer_total",
                "customer_today",
                "record_total",
                "record_today",
        };
    }

    public Object[] devicePageNotNull(){
        return new Object[]{
                "[list]-device_id",
                "[list]-name",
                "[list]-device_type",
                "[list]-type_name",
                "[list]-device_status",
                "[list]-status_name",
                "[list]-error",
        };
    }

}

