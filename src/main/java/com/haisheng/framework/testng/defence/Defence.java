package com.haisheng.framework.testng.defence;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import com.haisheng.framework.util.*;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;

import java.util.UUID;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class Defence {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);
    public String failReason = "";
    public String response = "";
    public boolean FAIL = false;
    public Case aCase = new Case();

    String UID = "";
    String APP_ID = "";
    private String AK = "";
    private String SK = "";


    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dt = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    public QADbUtil qaDbUtil = new QADbUtil();
    public int APP_ID_SAVE = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    public int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    public String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    public String DEBUG = System.getProperty("DEBUG", "true");

    private ApiResponse apiResponse = null;

    private final String SHOP_ID = "";


//    #########################################################接口调用方法########################################################

//    ***************************************************** 一、基础数据管理***********************************************************

    /**
     * @description: 1.1 社区管理
     * @author: liao
     * @time:
     */
    public ApiResponse villageList() throws Exception {
        String router = "/business/defence/VILLAGE_LIST/v1.0";
        String json =
                "{}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

    /**
     * @description: 1.2 同步社区下设备列表
     * @author: liao
     * @time:
     */
    public ApiResponse deviceList(long villageId) throws Exception {
        String router = "/business/defence/DEVICE_LIST/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + villageId +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

//   ********************************************************人员管理***************************************************************

    /**
     * @description: 2.1 社区人员注册
     * @author: liao
     * @return: user_id, customer_id
     * @time:
     */
    public ApiResponse customerReg(long villageId, String faceUrl, String userId, String name, String phone, String type, String cardKey,
                                   String age, String sex, String address, String birthday) throws Exception {
        String router = "/business/defence/CUSTOMER_REGISTER/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + villageId + "," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "    \"user_id\":\"" + userId + "\"," +
                        "    \"name\":\"" + name + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"type\":\"" + type + "\"," +
                        "    \"card_key\":\"" + cardKey + "\"," +
                        "    \"age\":\"" + age + "\"," +
                        "    \"sex\":\"" + sex + "\"," +
                        "    \"address\":\"" + address + "\"," +
                        "    \"birthday\":\"" + birthday + "\"" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

    /**
     * @description: 2.2 社区人员删除
     * @author: liao
     * @time:
     */
    public ApiResponse customerDelete(long villageId, String userId) throws Exception {
        String router = "/business/defence/CUSTOMER_DELETE/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + villageId + "," +
                        "    \"user_id\":\"" + userId + "\"" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }


//    ***********************************************************三、告警管理************************************************************

    /**
     * @description: 3.1 人员黑名单管理-注册人员黑名单
     * @author: liao
     * @return: alarm_customer_id
     * @time:
     */
    public ApiResponse customerRegBlack(long villageId, String userId, String level, String label, String faceUrl, String name, String phone, String type, String cardKey,
                                        String age, String sex, String address) throws Exception {
        String router = "/business/defence/CUSTOMER_REGISTER_BLACK/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + villageId + "," +
                        "    \"user_id\":\"" + userId + "\",\n" +
                        "    \"level\":\"" + level + "\",\n" +
                        "    \"label\":\"" + label + "\",\n" +
                        "    \"new_user\":{\n" +
                        "        \"face_url\":\"" + faceUrl + "\",\n" +
                        "        \"name\":\"" + name + "\",\n" +
                        "        \"phone\":\"" + phone + "\",\n" +
                        "        \"type\":\"" + type + "\",\n" +
                        "        \"cardKey\":\"" + cardKey + "\",\n" +
                        "        \"age\":\"" + age + "\",\n" +
                        "        \"sex\":\"" + sex + "\",\n" +
                        "        \"address\":\"" + address + "\"\n" +
                        "    }\n" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

    /**
     * @description: 3.2 人员黑名单管理-删除人员黑名单
     * @author: liao
     * @return: alarm_customer_id
     * @time:
     */
    public ApiResponse customerDeleteBlack(long villageId, String alarmCustomerId) throws Exception {
        String router = "/business/defence/CUSTOMER_DELETE_BLACK/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + villageId + "," +
                        "    \"alarm_customer_id\":\"" + alarmCustomerId + "\"" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

    /**
     * @description: 3.3 人员黑名单管理-获取人员黑名单
     * @author: liao
     * @time:
     */
    public ApiResponse customerBlackPage(long villageId, int page, int size) throws Exception {
        String router = "/business/defence/CUSTOMER_BLACK_PAGE/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + villageId + "," +
                        "    \"page\":" + page + "," +
                        "    \"size\":" + size +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

    /**
     * @description: 3.4 周界报警-设置设备周界报警配置
     * @author: liao
     * @time:
     */
    public ApiResponse boundaryAlarmAdd(long villageId, String deviceId, double x1, double y1, double x2, double y2) throws Exception {
        String router = "/business/defence/BOUNDARY_ALARM_ADD/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + villageId + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"boundary_axis\":[\n" +
                        "        {\n" +
                        "            \"x\":" + x1 + ",\n" +
                        "            \"y\":" + y1 + "\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":" + x2 + ",\n" +
                        "            \"y\":" + y2 + "\n" +
                        "        },\n" +
                        "    ]\n" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

    /**
     * @description: 3.5 周界报警-删除设备周界报警配置
     * @author: liao
     * @time:
     */
    public ApiResponse boundaryAlarmDelete(long villageId, String deviceId) throws Exception {
        String router = "/business/defence/BOUNDARY_ALARM_DELETE/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + villageId + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\"\n" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

    /**
     * @description: 3.6 周界报警-获取设备周界报警配置
     * @author: liao
     * @time:
     */
    public ApiResponse boundaryAlarmInfo(long villageId, String deviceId) throws Exception {
        String router = "/business/defence/BOUNDARY_ALARM_INFO/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + villageId + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\"\n" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

    /**
     * @description: 3.7 告警记录(分页查询)
     * @author: liao
     * @time:
     */
    public ApiResponse alarmLogPage(long villageId, String deviceId, int page, int size) throws Exception {
        String router = "/business/defence/ALARM_LOG_PAGE/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + villageId + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"size\":\"" + size + "\"\n" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

    /**
     * @description: 3.8 告警记录处理
     * @author: liao
     * @time:
     */
    public ApiResponse alarmLogOperate(long villageId, String alarmId, String optResult, String operator) throws Exception {
        String router = "/business/defence/ALARM_LOG_OPERATE/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + villageId + "\",\n" +
                        "    \"alarm_id\":\"" + alarmId + "\",\n" +
                        "    \"opt_result\":\"" + optResult + "\",\n" +
                        "    \"operator\":\"" + operator + "\"\n" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

    /**
     * @description: 3.9 设备画面人数告警设置
     * @author: liao
     * @time:
     */
    public ApiResponse deivceCustomerNumAlarmAdd(long villageId, String deviceId, int threshold) throws Exception {
        String router = "/business/defence/DEIVCE_CUSTOMER_NUM_ALARM_ADD/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + villageId + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"threshold\":\"" + threshold + "\"\n" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }


//    ******************************************************四、历史记录**********************************************************************

    /**
     * @description: 4.1 人脸识别记录分页查询
     * @author: liao
     * @time:
     */
    public ApiResponse customerHistoryCapturePage(long villageId, String namePhone, String deviceId, long startTime, long endTime,
                                                  int page, int size) throws Exception {
        String router = "/business/defence/CUSTOMER_HISTORY_CAPTURE_PAGE/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + villageId + "\",\n" +
                        "    \"name_phone\":\"" + namePhone + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\",\n" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"size\":\"" + size + "\"\n" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }


//    **********************************************************五、刑侦辅助**********************************************************

    /**
     * @description: 5.1 轨迹查询(人脸搜索)
     * @author: liao
     * @time:
     */
    public ApiResponse customerHistoryCapturePage(long villageId, String picUrl, long startTime, long endTime,
                                                  String similarity) throws Exception {
        String router = "/business/defence/CUSTOMER_FACE_TRACE_LIST/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + villageId + "\",\n" +
                        "    \"pic_url\":\"" + picUrl + "\",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\",\n" +
                        "    \"similarity\":\"" + similarity + "\"\n" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

    /**
     * @description: 5.2 结构化检索(分页查询)
     * @author: liao
     * @time:
     */
    public ApiResponse customerSearchList(long villageId, String deviceId, long startTime, long endTime,
                                          String sex, String age, String hair, String clothes, String clothesColour,
                                          String trousers, String trousersColour, String hat, String knapsack) throws Exception {
        String router = "/business/defence/CUSTOMER_SEARCH_LIST/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + villageId + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\",\n" +
                        "\"other_query\":{\n" +
                        "        \"sex\":\"" + sex + "\",\n" +
                        "        \"age\":\"" + age + "\",\n" +
                        "        \"hair\":\"" + hair + "\",\n" +
                        "        \"clothes\":\"" + clothes + "\",\n" +
                        "        \"clothes_colour\":\"" + clothesColour + "\",\n" +
                        "        \"trousers\":\"" + trousers + "\",\n" +
                        "        \"trousers_colour\":\"" + trousersColour + "\",\n" +
                        "        \"hat\":\"" + hat + "\",\n" +
                        "        \"knapsack\":\"" + knapsack + "\"\n" +
                        "    }" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

    /**
     * @description: 5.3 人物详情信息
     * @author: liao
     * @time:
     */
    public ApiResponse customerInfo(long villageId, String customerId) throws Exception {
        String router = "/business/defence/CUSTOMER_INFO/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + villageId + "\",\n" +
                        "    \"customer_id\":\"" + customerId + "\"\n" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

//    *****************************************************六、监控************************************************************

    /**
     * @description: 6.1 设备画面播放(实时/历史)
     * @author: liao
     * @time:
     */
    public ApiResponse messageSwitch(long villageId, String deviceId, long startTime, long endTime) throws Exception {
        String router = "/business/defence/MESSAGE_SWITCH/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + villageId + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\"\n" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

//    ***********************************************************七、统计信息*************************************************

    /**
     * @description: 7.1 设备实时-客流统计
     * @author: liao
     * @time:
     */
    public ApiResponse deviceCustomerFlowStatistic(long villageId, String deviceId) throws Exception {
        String router = "/business/defence/DEVICE_CUSTOMER_FLOW_STATISTIC/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + villageId + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\"\n" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }

    /**
     * @description: 7.2 设备实时-客流统计
     * @author: liao
     * @time:
     */
    public ApiResponse deviceAlarmStatistic(long villageId, String deviceId) throws Exception {
        String router = "/business/defence/DEVICE_ALARM_STATISTIC/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + villageId + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\"\n" +
                        "}";

        apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return apiResponse;
    }


//    #########################################################接口调用方法########################################################


//    #########################################################数据验证方法########################################################

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

    public void checkCode(ApiResponse apiResponse, String router, int expectCode) throws Exception {
        try {
            String requestId = apiResponse.getRequestId();
            if (apiResponse.getCode() != expectCode) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse) +
                        "actual code: " + apiResponse.getCode() + " expect code: " + expectCode + ".";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void checkMessage(String function, String response, String message) throws Exception {

        String messageRes = JSON.parseObject(response).getString("message");
        if (!message.equals(messageRes)) {
            throw new Exception(function + "，提示信息与期待不符，期待=" + message + "，实际=" + messageRes);
        }
    }

//    #########################################数据验证方法######################################################

//##############################################发送请求，存储数据方法#############################################################

    private ApiResponse sendRequest(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Send request only！");
        try {
            Credential credential = new Credential(AK, SK);
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.cn/retail/api/data/biz", credential);
            apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiRequest));
            logMine.printImportant(JSON.toJSONString(apiResponse));
        } catch (Exception e) {
            throw e;
        }
        return apiResponse;
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
        aCase.setApplicationId(APP_ID_SAVE);
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
}
