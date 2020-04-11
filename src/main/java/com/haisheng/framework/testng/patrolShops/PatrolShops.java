package com.haisheng.framework.testng.patrolShops;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class PatrolShops {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    public String failReason = "";
    public String response = "";
    public boolean FAIL = false;
    public Case aCase = new Case();

    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    public QADbUtil qaDbUtil = new QADbUtil();
    public int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    public int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    public String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";
    public String CI_CMD_1 = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan_today_data/buildWithParameters?case_name=";

    public String DEBUG = System.getProperty("DEBUG", "true");

    public String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    public HttpConfig config;

    private final String IPPort = "";
    private final String SHOP_ID = "";

    DateTimeUtil dt = new DateTimeUtil();



//    #########################################################接口调用方法########################################################

//    ***************************************************** 一、权限相关接口************************************************************


    /**
     * @description: 1.1 获取登陆验证码
     * @author: liao
     * @time:
     */
    public JSONObject getSmsCode( String phone) throws Exception {
        String url = "/patrol-verification-code";
        String json =
                "{\n" +
                        "\"phone\":\"" + phone + "\"," +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.2 登陆
     * @author: liao
     * @time:
     */    
    public JSONObject login( int type, String userName, String passwd, String phone,String veriCode) throws Exception {
        String url = "/patrol-login";
        String json =
                "{\n" +
                        "    \"type\":" + type + ",\n" +
                        "    \"username\":\"" +  userName + "\",\n" +
                        "    \"password\":\"" +  passwd + "\",\n" +
                        "    \"phone\":\"" +  phone + "\",\n" +
                        "    \"verification_code\":\"" +  veriCode + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.3 登出
     * @author: liao
     * @time:
     */
    public JSONObject userDetail( int type, String userName, String passwd, String phone,String veriCode) throws Exception {
        String url = "/patrol-logout";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


//    ********************************************************二、巡店通用接口********************************************************

    /**
     * @description: 2.1 获取区划
     * @author: liao
     * @time:
     */
    public JSONObject patrolDistricts( int type, String userName, String passwd, String phone,String veriCode) throws Exception {
        String url = "/patrol/districts";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.2 图片上传
     * @author: liao
     * @time:
     */
    public JSONObject uploadImage( String picData, int type) throws Exception {
        String url = "/patrol/pic/base64/upload";
        String json =
                "{\n" +
                        "    \"pic_data\":\"" + picData + "\",\n" +
                        "    \"shop_id\":" + SHOP_ID + ",\n" +
                        "    \"type\":" + type + "0\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

//    ************************************************三. 定检任务相关接口*******************************************************
    /**
     * @description: 3.1 获取巡检员列表
     * @author: liao
     * @time:
     */
    public JSONObject inspectorList() throws Exception {
        String url = "/patrol/schedule-check/inspector/list";
        String json =
                "{}";

        String res = httpPostNoPrintPara(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 3.2 获取可巡查门店
     * @author: liao
     * @time:
     */
    public JSONObject checkShopList(String inspectorId,String districtCode) throws Exception {
        String url = "/patrol/schedule-check/shop/list";
        String json =
                "{\n" +
                        "    \"inspector_id\":\"" +  inspectorId + "\",\n" +
                        "    \"district_code\":\"" + districtCode + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }




//    #########################################################接口调用方法########################################################


//    #########################################################数据验证方法########################################################


    public String httpPostWithCheckCode(String path, String json) throws Exception {
        response = httpPost(path, json);
        checkCode(response, StatusCode.SUCCESS, path);
        return response;
    }

    public String httpPost(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = IPPort + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    public String httpPostNoPrintPara(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = IPPort + path;
        config.url(queryUrl).json(json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        checkCode(response, StatusCode.SUCCESS, path);

        return response;
    }

    public void checkNotCode(String response, int expectNot, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            if (expectNot == code) {
                Assert.assertNotEquals(code, expectNot, message + resJo.getString("message"));
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
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
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }

    public void checkMessage(String function, String response, String message) throws Exception {

        String messageRes = JSON.parseObject(response).getString("message");
        if (!message.equals(messageRes)) {
            throw new Exception(function + "，提示信息与期待不符，期待=" + message + "，实际=" + messageRes);
        }
    }

    public void login() {
        qaDbUtil.openConnection();
        qaDbUtil.openConnectionRdDaily();
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        initHttpConfig();
        String path = "/risk-login";
        String loginUrl = IPPort + path;
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

        saveData(aCase, ciCaseName, caseName, failReason, "登录获取authentication");
    }

    public void clean() {
        qaDbUtil.closeConnection();
        qaDbUtil.closeConnectionRdDaily();
        dingPushFinal();
    }

    public void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 华成裕
            //18513118484 杨航
            //15011479599 谢志东
            //18600872221 蔡思明
            String[] rd = {"18513118484", //杨航
                    "15011479599", //谢志东
                    "15898182672"}; //华成裕
            alarmPush.alarmToRd(rd);
        }

        FAIL = false;
    }

    public void saveData(Case aCase, String ciCaseName, String caseName, String failReason, String caseDescription) {
        setBasicParaToDB(aCase, ciCaseName, caseName, failReason, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());

            failReason = aCase.getFailReason();

            String message = "飞单日常 \n" +
                    "验证：" + aCase.getCaseDescription() +
                    " \n\n" + failReason;

            dingPush(aCase, message);
        }
    }

    public void dingPush(Case aCase, String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
//            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        alarmPush.dailyRgn(msg);
        this.FAIL = true;
        Assert.assertNull(aCase.getFailReason());
    }

    public void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String failReason, String caseDesc) {
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

    public void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            failReason = "初始化http配置异常" + "\n" + e;
            return;
        }
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .other("shop_id", SHOP_ID)
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

}
