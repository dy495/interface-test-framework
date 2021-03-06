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
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.Api;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.testng.commonDataStructure.LogMine;
import com.haisheng.framework.util.*;
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
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.*;


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
    public static String authorization;
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
            caseResult.setCaseName(method.getName());
        }
        caseResult.setCiCmd(commonConfig.checklistCiCmd + caseResult.getCaseName());
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
            // ??????request??????
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
            // client ??????
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
            logger.debug("apiCustomerRequest Exception: " + e.getMessage());
            throw e;
        }
    }

    public String apiCustomerRequestNotCheck(String router, String json) {
        try {
            long start = System.currentTimeMillis();
            Credential credential = new Credential(commonConfig.ak, commonConfig.sk);
            // ??????request??????
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

            // client ??????
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
            throw new Exception("?????????????????????status???" + status + "???path???" + path);
        }
    }

    public String uploadFile(String ipPort, String path, String filePath) {
        String url = ipPort + path;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        commonConfig.getHeaders().entrySet().stream().filter(e -> !StringUtils.isEmpty(e.getValue())).forEach(e -> httpPost.addHeader(e.getKey(), e.getValue()));
        httpPost.addHeader("authorization", authorization);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        File file = new File(filePath);
        String res = "";
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
            res = EntityUtils.toString(responseEntity, "UTF-8");
            logger.info("response: " + res);
        } catch (Exception e) {
            appendFailReason(e.toString());
        }
        return res;
    }

    public String httpGet(String port, String path, Map<String, Object> paramMap) throws HttpProcessException {
        StringBuilder sb = new StringBuilder();
        paramMap.forEach((key, value) -> sb.append("&").append(key).append("=").append(value));
        String param = sb.toString().replaceFirst("&", "");
        path = path + "?" + param;
        return httpGet(port, path);
    }

    /**
     * ??????????????????????????????
     */
    public String httpGet(String ipPort, String path) throws HttpProcessException {
        Api api = new Api.Builder().method("GET").ipPort(ipPort).path(path).build();
        return execute(api, false, false);
    }

    /**
     * ??????????????????????????????
     */
    public String httpPostWithCheckCode(String path, String json, String IpPort) {
        return httpPost(IpPort, path, json, true, false);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param path   ??????
     * @param object ?????????
     * @param port   ??????
     */
    public void httpPost(String port, String path, JSONObject object) {
        httpPost(port, path, JSONObject.toJSONString(object), true, true);
    }

    /**
     * ??????????????????????????????
     */
    public String httpPost(String path, String json, String IpPort) throws Exception {
        return httpPost(IpPort, path, json, false, false);
    }

    /**
     * ??????????????????????????????
     */
    public String httpPost(String port, String path, String requestBody, boolean checkCode, boolean hasToken) {
        Api api = new Api.Builder().method("POST").ipPort(port).path(path).requestBody(requestBody).build();
        return execute(api, checkCode, hasToken);
    }

    public String execute(Api api, boolean checkCode, boolean hasToken) {
        long start = System.currentTimeMillis();
        try {
            getResponse(api);
            if (checkCode) {
                checkCode(response, StatusCode.SUCCESS, api.getPath());
            }
            if (hasToken) {
                authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
                logger.info("authorization:" + authorization);
            }
        } catch (Exception e) {
            collectMessage(e);
        }
        logger.info("{} time used {} ms", api.getPath(), System.currentTimeMillis() - start);
        caseResult.setResponse(response);
        return response;
    }

    public void getResponse(@NotNull Api api) throws HttpProcessException {
        initHttpConfig();
        logger.info("url: {}", api.getUrl());
        logger.info("body: {}", api.getRequestBody());
        if (api.getMethod().equals("POST")) {
            config.url(api.getUrl()).json(api.getRequestBody());
            response = HttpClientUtil.post(config);
        } else if (api.getMethod().equals("GET")) {
            config.url(api.getUrl());
            response = HttpClientUtil.get(config);
        } else {
            throw new RuntimeException("?????????????????????");
        }
        logger.info("response???{}", response);
    }

    public void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            String failReason = "?????????http????????????" + "\n" + e;
            caseResult.setFailReason(failReason);
            return;
        }
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";
        Header[] headers;
        HttpHeader httpHeader = HttpHeader.custom()
                .contentType("application/json; charset=utf-8")
                .userAgent(userAgent)
                .authorization(authorization);
        commonConfig.getHeaders().entrySet().stream().filter(e -> !StringUtils.isEmpty(e.getValue())).forEach(e -> httpHeader.other(e.getKey(), e.getValue()));
        headers = httpHeader.build();
        logger.info("headers:{}", Arrays.toString(headers));
        config = HttpConfig.custom().headers(headers).client(client);
    }

    private void dingPushFinal(boolean isFAIL) {
        if (DEBUG.trim().equalsIgnoreCase("false") && isFAIL) {
            AlarmPush alarmPush = new AlarmPush();
            alarmPush.setDingWebhook(commonConfig.dingHook);
            alarmPush.alarmToRd(commonConfig.pushRd);
        }
    }

    public void dingPushDaily(String msg) {
        logger.info("ding msg: " + msg);

        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().equalsIgnoreCase("false")) {
            alarmPush.setDingWebhook(commonConfig.dingHook);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        alarmPush.dailyRgn(msg);
        if (commonConfig.pushQa != null) {
            alarmPush.alarmToRd(commonConfig.pushQa);
        }
        this.FAIL = true;
        logger.info("failReason:{}", caseResult.getFailReason());
        Assert.assertNull(caseResult.getFailReason());
    }

    public void saveData(String caseDesc) {
        setBasicParaToDB(caseDesc);
        if (DEBUG.trim().equalsIgnoreCase("false")) {
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
        String desc = StringUtils.isEmpty(commonConfig.getHeaders().get("product")) ? caseDesc : commonConfig.getHeaders().get("product") + "_" + caseDesc;
        caseResult.setCaseDescription(desc);
        caseResult.setExpect("?????????");

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
                failReason = failReason.replace("java.lang.Exception", "??????");
            } else if (failReason.contains("java.lang.AssertionError")) {
                failReason = failReason.replace("java.lang.AssertionError", "??????");
            } else if (failReason.contains("java.lang.IllegalArgumentException")) {
                failReason = failReason.replace("java.lang.IllegalArgumentException", "??????");
            } else if (failReason.contains("java.net.UnknownHostException")) {
                failReason = failReason.replace("com.arronlong.httpclientutil.exception.HttpProcessException: java.net.UnknownHostException", "????????????????????????");
            } else if (failReason.contains("org.apache.http.conn.HttpHostConnectException")) {
                failReason = failReason.replace("com.arronlong.httpclientutil.exception.HttpProcessException: org.apache.http.conn.HttpHostConnectException", "?????????????????????");
            }
            caseResult.setFailReason(failReason);
        } else {
            caseResult.setResult("PASS");
        }

    }

    /**
     * ????????????
     *
     * @param second ???
     */
    public void sleep(long second) throws InterruptedException {
        Thread.sleep(second * 1000);
    }

    public void collectMessage(Throwable e) {
        e.printStackTrace();
        appendFailReason(e.toString());
    }

    /**
     * ??????dingPush?????????
     *
     * @param str str
     * @return newStr
     */
    public String dingPushResult(String str) {
        String result = str;
        for (EnumTestProduct p : EnumTestProduct.values()) {
            if (str.contains(p.getAbbreviation())) {
                result = CommonUtil.replace(str, "_");
            }
        }
        return result;
    }
}
