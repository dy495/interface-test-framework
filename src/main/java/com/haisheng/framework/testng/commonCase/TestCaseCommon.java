package com.haisheng.framework.testng.commonCase;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumProduce;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.testng.commonDataStructure.LogMine;
import com.haisheng.framework.util.*;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;


/**
 * @author : yu
 * @date :  2020/05/30
 */

public class TestCaseCommon {

    public static Case caseResult = null;
    public static DateTimeUtil dt = new DateTimeUtil();
    public LogMine logger = new LogMine(LoggerFactory.getLogger(this.getClass()));
    public static HttpConfig config;
    public static String response = "";
    public static String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    private static CommonConfig commonConfig = null;
    private boolean FAIL = false;
    private final String DEBUG = System.getProperty("DEBUG", "true");
    private final QADbProxy qaDbProxy = QADbProxy.getInstance();


    public QADbUtil qaDbUtil = qaDbProxy.getQaUtil();


    public TestCaseCommon() {
        logger.debug("TestCaseCommon");
        logger.debug("qaDbProxy: " + qaDbProxy);
        logger.debug("qaDbUtil: " + qaDbUtil);
        logger.debug("caseResult: " + caseResult);
        logger.debug("config: " + config);
        logger.debug("response: " + response);
        logger.debug("authorization: " + authorization);
        logger.debug("commonConfig: " + commonConfig);
    }


    public void afterClassClean() {
        logger.info("clean");
        logger.debug("debug-close connect");
        if (null == caseResult) {
            logger.printImportant("case result is null");
        }
        if (null == commonConfig) {
            logger.printImportant("common config is null");
        }
        logger.debug(caseResult.getCiCmd());
        logger.debug(commonConfig.checklistCiCmd);
        qaDbUtil.closeConnection();
        qaDbUtil.closeConnectionRdDaily();
        dingPushFinal(this.FAIL);
    }

    public void beforeClassInit(CommonConfig config) {
        System.setProperty("ENV_INFO", "DAILY");
        initialDB();
        commonConfig = config;
        caseResult = new Case();
        caseResult.setApplicationId(commonConfig.checklistAppId);
        caseResult.setConfigId(commonConfig.checklistConfId);
        caseResult.setQaOwner(commonConfig.checklistQaOwner);
        caseResult.setCiCmd(commonConfig.checklistCiCmd);
        caseResult.setCaseName(commonConfig.caseName);
        logger.debug("beforeClassInit");
        logger.debug("config: " + commonConfig);
        logger.debug("case: " + caseResult);
        logger.debug("qaDbProxy: " + qaDbProxy);
        logger.debug("qaDbUtil: " + qaDbUtil);
        logger.debug("caseResult: " + caseResult);
        logger.debug("config: " + config);
        logger.debug("response: " + response);
        logger.debug("authorization: " + authorization);
        logger.debug("commonConfig: " + commonConfig);
    }

    public void initialDB() {
        logger.info("initial db");
        qaDbUtil.openConnection();
        qaDbUtil.openConnectionRdDaily();
    }

    public Case getFreshCaseResult(Method method) {
        caseResult = new Case();
        caseResult.setApplicationId(commonConfig.checklistAppId);
        caseResult.setConfigId(commonConfig.checklistConfId);
        caseResult.setQaOwner(commonConfig.checklistQaOwner);
        caseResult.setCiCmd(commonConfig.checklistCiCmd);
        if (StringUtils.isEmpty(method.getName())) {
            caseResult.setCaseName("login");
        } else {
            String caseName = StringUtils.isEmpty(commonConfig.produce) ? method.getName() : commonConfig.produce + "_" + method.getName();
            caseResult.setCaseName(caseName);
        }
        logger.debug("fresh case: " + caseResult);
        return caseResult;
    }

    public void appendFailReason(String msg) {
        String failreason = caseResult.getFailReason();
        if (StringUtils.isEmpty(failreason)) {
            failreason = msg;
        } else {
            failreason += "\n" + msg;
        }
        logger.info(failreason);
        caseResult.setFailReason(failreason);
    }

    public String apiCustomerRequest(String router, String json) throws Exception {
        try {
            long start = System.currentTimeMillis();
            Credential credential = new Credential(commonConfig.ak, commonConfig.sk);
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            logger.debug("try to invoke apirequest");
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(commonConfig.uid)
                    .appId(commonConfig.appId)
                    .version(SdkConstant.API_VERSION)
                    .requestId(requestId)
                    .router(router)
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();
            // client 请求
            logger.info("{} json param: {} requestid {}", router, json, requestId);
            ApiClient apiClient = new ApiClient(commonConfig.gateway, credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            caseResult.setResponse(JSON.toJSONString(apiResponse));
            logger.info("response: " + JSON.toJSONString(apiResponse));
            logger.info("{} time used {} ms", router, System.currentTimeMillis() - start);
            if (!apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
            return JSON.toJSONString(apiResponse);

        } catch (Exception e) {
            logger.debug("apiCustomerRequest Exception: " + e.toString());
            throw e;
        }
    }

    public String apiCustomerRequestNotCheck(String router, String json) {
        try {
            long start = System.currentTimeMillis();
            Credential credential = new Credential(commonConfig.ak, commonConfig.sk);
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(commonConfig.uid)
                    .appId(commonConfig.appId)
                    .version(SdkConstant.API_VERSION)
                    .requestId(requestId)
                    .router(router)
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            logger.info("{} json param: {}", router, json);
            ApiClient apiClient = new ApiClient(commonConfig.gateway, credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            caseResult.setResponse(JSON.toJSONString(apiResponse));

            logger.info(JSON.toJSONString(apiResponse));
            logger.info("{} time used {} ms", router, System.currentTimeMillis() - start);
            return JSON.toJSONString(apiResponse);


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ApiResponse sendRequest(String router, String[] resource, String json) {
        ApiResponse apiResponse;
        try {
            Credential credential = new Credential(commonConfig.ak, commonConfig.sk);
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(commonConfig.uid)
                    .appId(commonConfig.appId)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            ApiClient apiClient = new ApiClient(commonConfig.gateway, credential);
            apiResponse = apiClient.doRequest(apiRequest);
            caseResult.setResponse(JSON.toJSONString(apiResponse));

            logger.printImportant(JSON.toJSONString(apiRequest));
            logger.printImportant(JSON.toJSONString(apiResponse));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return apiResponse;
    }

    public JSONObject sendRequestCode1000(String router, String[] resource, String json) throws Exception {

        logger.info(router);

        ApiResponse apiResponse = sendRequest(router, resource, json);
        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return JSON.parseObject(JSON.toJSONString(apiResponse));
    }

    public void checkCode(String gateway, ApiResponse apiResponse, String router, int expectCode) throws Exception {
        try {
            if (null == apiResponse) {
                throw new Exception("api");
            }
            int codeRes = apiResponse.getCode();
            if (codeRes != expectCode) {
                String msg = "gateway: " + gateway + ", router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse) +
                        "actual code: " + codeRes + " expect code: " + expectCode + ".";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void checkCode(ApiResponse apiResponse, String router, int expectCode) throws Exception {
        try {
            if (null == apiResponse) {
                throw new Exception("api");
            }
            int codeRes = apiResponse.getCode();
            if (codeRes != expectCode) {
                String msg = "gateway: " + commonConfig.gateway + ", router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse) +
                        "actual code: " + codeRes + " expect code: " + expectCode + ".";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void checkCode(String response, int expect, String message) throws Exception {

        caseResult.setResponse(response);
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            message += resJo.getString("message");

            if (expect != code) {
                logger.info("info-----" + message + " expect code: " + expect + ",actual: " + code);
                throw new Exception(message + " expect code: " + expect + ",actual: " + code);
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }

    public void checkMessage(String function, ApiResponse apiResponse, String message) throws Exception {

        String messageRes = apiResponse.getMessage();
        if (!message.equals(messageRes)) {
            throw new Exception(function + "，提示信息与期待不符，期待=" + message + "，实际=" + messageRes);
        }
    }

    public void checkMessage(String function, ApiResponse apiResponse, String message, boolean isEqual) throws Exception {

        String messageRes = apiResponse.getMessage();
        if (!messageRes.contains(message)) {
            throw new Exception(function + "，提示信息与期待不符，期待提示信息包括-" + message + "，实际=" + messageRes);
        }
    }

    public String httpPostWithCheckCode(String path, String json, String IpPort) {
        initHttpConfig();
        String queryUrl = IpPort + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            logger.info("response: {}", response);
            checkCode(response, StatusCode.SUCCESS, path);
        } catch (Exception e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        caseResult.setResponse(response);
        return response;
    }

    public void httpPost(String path, JSONObject object, String IpPort) {
        initHttpConfig();
        String queryUrl = IpPort + path;
        config.url(queryUrl).json(JSONObject.toJSONString(object));
        logger.info("{} json param: {}", path, JSONObject.toJSONString(object));
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            checkCode(response, StatusCode.SUCCESS, path);
            authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            logger.info("authorization:" + authorization);
        } catch (Exception e) {
            appendFailReason(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
    }

    public String httpPost(String path, String json, String IpPort) throws Exception {
        initHttpConfig();
        String queryUrl = IpPort + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        caseResult.setResponse(response);
        return response;
    }

    public String httpPostFile(String path, String[] filepath, String type, String IpPort) throws Exception {
        initHttpConfig();
        String queryUrl = IpPort + path;
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        config.url(queryUrl).files(filepath, "file", true).map(map);
        logger.info("{} json param: {}", path, filepath, type);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        caseResult.setResponse(response);
        return response;
    }

    public String httpGet(String path, String json, String IpPort) throws Exception {
        initHttpConfig();
        String queryUrl = IpPort + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.get(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        caseResult.setResponse(response);
        return response;
    }

    public void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            String failReason = "初始化http配置异常" + "\n" + e;
            caseResult.setFailReason(failReason);
            return;
        }
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";
        Header[] headers;
        headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .other("shop_id", commonConfig.shopId)
                .userAgent(userAgent)
                .referer(commonConfig.referer)
                .authorization(authorization)
                .build();
        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    public String getProscheShop() {
        return "22728";
    }

    public String getProscheShopOline() {
        return "12928";
    }
    public String getJcRefer() {
        return "https://servicewechat.com/wx4071a91527930b48/";
    }

    public String getXundianShop() {
        return "4116";
    }

    public String getXunDianShop() {
        return "4116";
    }

    public String getXunDianShopOnline() {
        return "13260";
    }


    private void dingPushFinal(boolean isFAIL) {
        if (DEBUG.trim().toLowerCase().equals("false") && isFAIL) {
            AlarmPush alarmPush = new AlarmPush();
            alarmPush.setDingWebhook(commonConfig.dingHook);
            alarmPush.alarmToRd(commonConfig.pushRd);
        }
    }

    public void dingPushDaily(String msg) {
        logger.info("ding msg: " + msg);

        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
            alarmPush.setDingWebhook(commonConfig.dingHook);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        alarmPush.dailyRgn(msg);
        this.FAIL = true;
        System.out.println(caseResult.getFailReason());
        Assert.assertNull(caseResult.getFailReason());
    }

    public void saveData(String caseDesc) {
        setBasicParaToDB(caseDesc);
        if (DEBUG.trim().toLowerCase().equals("false")) {
            qaDbUtil.saveToCaseTable(caseResult);
        }
        logger.debug("insert case done");
        if (!StringUtils.isEmpty(caseResult.getFailReason())) {
            logger.error(caseResult.getFailReason());
            String message = commonConfig.message;
            String macroCaseName = commonConfig.CASE_NAME;
            String macroCaseDesc = commonConfig.CASE_DESC;
            String macroCaseFail = commonConfig.CASE_FAIL;
            String caseName = dingPushResult(caseResult.getCaseName());
            String caseDescription = dingPushResult(caseResult.getCaseDescription());
            message = message.replace(macroCaseName, caseName);
            message = message.replace(macroCaseDesc, caseDescription);
            message = message.replace(macroCaseFail, caseResult.getFailReason());

            dingPushDaily(message);
        }
    }

    public void setBasicParaToDB(String caseDesc) {
        String desc = StringUtils.isEmpty(commonConfig.produce) ? caseDesc : commonConfig.produce + "_" + caseDesc;
        caseResult.setCaseDescription(desc);
        caseResult.setExpect("见描述");

        logger.debug("save db");
        logger.debug("case name: " + caseResult.getCaseName());
        logger.debug("case desc: " + caseResult.getCaseDescription());
        logger.debug("case appid: " + caseResult.getApplicationId());
        logger.debug("case confid: " + caseResult.getConfigId());
        logger.debug("case cicmd: " + caseResult.getCiCmd());
        logger.debug("case fail: " + caseResult.getFailReason());

        String failReason = caseResult.getFailReason();
        if (!StringUtils.isEmpty(failReason)) {
            if (failReason.contains("java.lang.Exception:")) {
                failReason = failReason.replace("java.lang.Exception", "异常");
            } else if (failReason.contains("java.lang.AssertionError")) {
                failReason = failReason.replace("java.lang.AssertionError", "异常");
            } else if (failReason.contains("java.lang.IllegalArgumentException")) {
                failReason = failReason.replace("java.lang.IllegalArgumentException", "异常");
            }
            caseResult.setFailReason(failReason);
        } else {
            caseResult.setResult("PASS");
        }

    }

    /**
     * 线程等待
     *
     * @param second 秒
     */
    public void sleep(long second) throws InterruptedException {
        Thread.sleep(second * 1000);
    }

    public void collectMessage(Throwable e) {
        e.printStackTrace();
        appendFailReason(e.toString());
    }

    /**
     * 处理dingPush的结果
     *
     * @param str str
     * @return newStr
     */
    public String dingPushResult(String str) {
        String result = str;
        for (EnumProduce p : EnumProduce.values()) {
            if (str.contains(p.name())) {
                result = CommonUtil.replace(str, "_");
            }
        }
        return result;
    }
}
