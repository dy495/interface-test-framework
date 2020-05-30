package com.haisheng.framework.testng.commonCase;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.testng.commonDataStructure.LogMine;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;

import java.lang.reflect.Method;
import java.util.UUID;



/**
 * @author : yu
 * @date :  2020/05/30
 */

public class TestCaseCommon {

    public static Case caseResult = null;
    public LogMine logger    = new LogMine(LoggerFactory.getLogger(this.getClass()));;
    public String DEBUG      = System.getProperty("DEBUG", "true");
    public boolean FAIL      = false;

    private QADbUtil qaDbUtil = new QADbUtil();
    private static CommonConfig commonConfig = null;

    public void afterClassClean() {
        logger.info("clean");
        if (DEBUG.equals("true")) {
            return;
        }
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
        dingPushFinal(this.FAIL);
    }
    
    public void beforeClassInit(CommonConfig config) {
        System.setProperty("ENV_INFO", "DAILY");
        initialDB();
        commonConfig = config;
        logger.debug("initial config: " + commonConfig.checklistQaOwner);
    }

    public void initialDB() {
        logger.info("initial");
        if (DEBUG.equals("true")) {
            return;
        }
        qaDbUtil.openConnection();
    }

    public Case getFreshCaseResult(Method method) {
        caseResult = new Case();
        caseResult.setApplicationId(commonConfig.checklistAppId);
        caseResult.setConfigId(commonConfig.checklistConfId);
        caseResult.setQaOwner(commonConfig.checklistQaOwner);
        caseResult.setCiCmd(commonConfig.checklistCiCmd);
        caseResult.setCaseName(method.getName());

        logger.debug("fresh case: " + caseResult.getCiCmd());
        logger.debug("create a fresh case class to store current-case-result");
        return caseResult;
    }

    public void appendFailreason(String msg) {
        String failreason = caseResult.getFailReason() + "\n" + msg;
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
            logger.info("{} json param: {} requestid {}", router, json,requestId);
            ApiClient apiClient = new ApiClient(commonConfig.gateway, credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            caseResult.setResponse(JSON.toJSONString(apiResponse));

            logger.info("response: " + JSON.toJSONString(apiResponse));
            logger.info("{} time used {} ms", router, System.currentTimeMillis() - start);
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
            return JSON.toJSONString(apiResponse);

        } catch (Exception e) {
            logger.debug("apiCustomerRequest Exception: " + e.toString());
            throw e;
        }
    }

    public String apiCustomerRequestNotCheck(String router, String json) throws Exception {
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
            throw e;
        }
    }



    public ApiResponse sendRequest(String router, String[] resource, String json) {
        ApiResponse apiResponse = null;
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
            throw e;
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
        Assert.assertNull(caseResult.getFailReason());
    }

    public void saveData(String caseDesc) {
        if (DEBUG.equals("true")) {
            return;
        }
        setBasicParaToDB(caseDesc);
        qaDbUtil.saveToCaseTable(caseResult);
        logger.debug("insert case done");
        if (!StringUtils.isEmpty(caseResult.getFailReason())) {
            logger.error(caseResult.getFailReason());

            String message = commonConfig.message;
            String macroCaseDesc = commonConfig.CASE_DESC;
            String macroCaseFail = commonConfig.CASE_FAIL;

            message = message.replace(macroCaseDesc, caseResult.getCaseDescription());
            message = message.replace(macroCaseFail, caseResult.getFailReason());

            dingPushDaily(message);
        }
    }

    public void setBasicParaToDB(String caseDesc) {
        caseResult.setCaseDescription(caseDesc);
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


}
