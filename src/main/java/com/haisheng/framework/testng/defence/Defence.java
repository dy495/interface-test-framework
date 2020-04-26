package com.haisheng.framework.testng.defence;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import com.haisheng.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import java.util.Random;
import java.util.UUID;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class Defence {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);

    //    case相关变量
    String UID = "uid_7fc78d24";
    String APP_ID = "097332a388c2";
    private String AK = "77327ffc83b27f6d";
    private String SK = "7624d1e6e190fbc381d0e9e18f03ab81";
    private ApiResponse apiResponse = null;
    public final long VILLAGE_ID =8;

    //    工具类变量
    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dt = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    public QADbUtil qaDbUtil = new QADbUtil();

    //    自动化相关变量
    public int APP_ID_SAVE = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    public int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_AI_LIVING_AREA_DAILY_SERVICE;
    public String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/ai-livingArea-test/buildWithParameters?case_name=";
    public String DEBUG = System.getProperty("DEBUG", "true");
    public String failReason = "";
    public boolean FAIL = false;

//    #########################################################接口调用方法########################################################

//    ***************************************************** 一、基础数据管理***********************************************************

    /**
     * @description: 1.1 社区管理
     * @author: liao
     * @time:
     */
    public JSONObject villageList() throws Exception {
        String router = "/business/defence/VILLAGE_LIST/v1.0";
        String json =
                "{}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    /**
     * @description: 1.2 同步社区下设备列表
     * @author: liao
     * @time:
     */
    public JSONObject deviceList() throws Exception {
        String router = "/business/defence/DEVICE_LIST/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

//   ********************************************************人员管理***************************************************************

    /**
     * @description: 2.1 社区人员注册
     * @author: liao
     * @return: user_id, customer_id
     * @time:
     */
    public JSONObject customerReg(String faceUrl, String userId, String name, String phone, String type, String cardKey,
                                  String age, String sex, String address, String birthday) throws Exception {
        String router = "/business/defence/CUSTOMER_REGISTER/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID + "," +
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

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    public ApiResponse customerReg(String faceUrl, String userId, String name, String phone, String type, String cardKey,
                                  String age, String sex, String address, String birthday,int expectCode) throws Exception {
        String router = "/business/defence/CUSTOMER_REGISTER/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID + "," +
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

        ApiResponse apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));
        checkCode(apiResponse,router,expectCode);

        return apiResponse;
    }

    public JSONObject customerReg(String faceUrl, String userId) throws Exception {
        String router = "/business/defence/CUSTOMER_REGISTER/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID + "," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "    \"user_id\":\"" + userId + "\"," +
                        "    \"name\":\"" + "name" + "\"," +
                        "    \"phone\":\"" + genPhoneNum() + "\"," +
                        "    \"type\":\"" + "住户" + "\"," +
                        "    \"card_key\":\"" + "cardKey" + "\"," +
                        "    \"age\":\"" + 20 + "\"," +
                        "    \"sex\":\"" + "MALE" + "\"," +
                        "    \"address\":\"" + "address" + "\"," +
                        "    \"birthday\":\"" + "2000-01-01" + "\"" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    /**
     * @description: 2.2 社区人员删除
     * @author: liao
     * @time:
     */
    public JSONObject customerDelete(String userId) throws Exception {
        String router = "/business/defence/CUSTOMER_DELETE/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID + "," +
                        "    \"user_id\":\"" + userId + "\"" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    public ApiResponse customerDelete(long villageId, String userId,int expectCode) throws Exception {
        String router = "/business/defence/CUSTOMER_DELETE/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + villageId + "," +
                        "    \"user_id\":\"" + userId + "\"" +
                        "}";

        ApiResponse apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse,router,expectCode);

        return apiResponse;
    }


//    ***********************************************************三、告警管理************************************************************

    /**
     * @description: 3.1 人员黑名单管理-注册人员黑名单
     * @author: liao
     * @return: alarm_customer_id
     * @time:
     */
    public JSONObject customerRegBlack(String userId, String level, String label, String faceUrl, String name, String phone, String type, String cardKey,
                                       String age, String sex, String address) throws Exception {
        String router = "/business/defence/CUSTOMER_REGISTER_BLACK/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID + "," +
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

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    public JSONObject customerRegBlackUserId(String userId, String level, String label) throws Exception {
        String router = "/business/defence/CUSTOMER_REGISTER_BLACK/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID + "," +
                        "    \"user_id\":\"" + userId + "\",\n" +
                        "    \"level\":\"" + level + "\",\n" +
                        "    \"label\":\"" + label + "\",\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    public ApiResponse customerRegBlackUserId(String userId, String level, String label,int expectCode) throws Exception {
        String router = "/business/defence/CUSTOMER_REGISTER_BLACK/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID + "," +
                        "    \"user_id\":\"" + userId + "\",\n" +
                        "    \"level\":\"" + level + "\",\n" +
                        "    \"label\":\"" + label + "\",\n" +
                        "}";

        ApiResponse apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse,router,expectCode);

        return apiResponse;
    }

    public JSONObject customerRegBlackNewUser(String level, String label, String faceUrl, String name, String phone, String type, String cardKey,
                                              String age, String sex, String address) throws Exception {
        String router = "/business/defence/CUSTOMER_REGISTER_BLACK/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID + "," +
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

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    public JSONObject customerRegBlackNewUser(String level, String label, String faceUrl) throws Exception {
        String router = "/business/defence/CUSTOMER_REGISTER_BLACK/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID + "," +
                        "    \"level\":\"" + level + "\",\n" +
                        "    \"label\":\"" + label + "\",\n" +
                        "    \"new_user\":{\n" +
                        "        \"face_url\":\"" + faceUrl + "\",\n" +
                        "        \"name\":\"" + "name" + "\",\n" +
                        "        \"phone\":\"" + genPhoneNum() + "\",\n" +
                        "        \"type\":\"" + "type" + "\",\n" +
                        "        \"cardKey\":\"" + "cardKey" + "\",\n" +
                        "        \"age\":\"" + "age" + "\",\n" +
                        "        \"sex\":\"" + "sex" + "\",\n" +
                        "        \"address\":\"" + "address" + "\"\n" +
                        "    }\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    /**
     * @description: 3.2 人员黑名单管理-删除人员黑名单
     * @author: liao
     * @return: alarm_customer_id
     * @time:
     */
    public JSONObject customerDeleteBlack(String alarmCustomerId) throws Exception {
        String router = "/business/defence/CUSTOMER_DELETE_BLACK/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID + "," +
                        "    \"alarm_customer_id\":\"" + alarmCustomerId + "\"" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    public ApiResponse customerDeleteBlack(String alarmCustomerId,int expectCode) throws Exception {
        String router = "/business/defence/CUSTOMER_DELETE_BLACK/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID + "," +
                        "    \"alarm_customer_id\":\"" + alarmCustomerId + "\"" +
                        "}";

        ApiResponse apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse,router,expectCode);

        return apiResponse;
    }

    /**
     * @description: 3.3 人员黑名单管理-获取人员黑名单
     * @author: liao
     * @time:
     */
    public JSONObject customerBlackPage(int page, int size) throws Exception {
        String router = "/business/defence/CUSTOMER_BLACK_PAGE/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID + "," +
                        "    \"page\":" + page + "," +
                        "    \"size\":" + size +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    /**
     * @description: 3.4 周界报警-设置设备周界报警配置
     * @author: liao
     * @time:
     */
    public JSONObject boundaryAlarmAdd(String deviceId, double x1, double y1, double x2, double y2,double x3,double y3) throws Exception {
        String router = "/business/defence/BOUNDARY_ALARM_ADD/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
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
                        "        {\n" +
                        "            \"x\":" + x3 + ",\n" +
                        "            \"y\":" + y3 + "\n" +
                        "        },\n" +
                        "    ]\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    public JSONObject boundaryAlarmAdd(String deviceId) throws Exception {
        String router = "/business/defence/BOUNDARY_ALARM_ADD/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"boundary_axis\":[\n" +
                        "        {\n" +
                        "            \"x\":" + 0.5 + ",\n" +
                        "            \"y\":" + 0.5 + "\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":" + 0.4 + ",\n" +
                        "            \"y\":" + 0.6 + "\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":" + 0.7 + ",\n" +
                        "            \"y\":" + 0.8 + "\n" +
                        "        },\n" +
                        "    ]\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    /**
     * @description: 3.5 周界报警-删除设备周界报警配置
     * @author: liao
     * @time:
     */
    public JSONObject boundaryAlarmDelete(String deviceId) throws Exception {
        String router = "/business/defence/BOUNDARY_ALARM_DELETE/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\"\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    /**
     * @description: 3.6 周界报警-获取设备周界报警配置
     * @author: liao
     * @time:
     */
    public JSONObject boundaryAlarmInfo(String deviceId) throws Exception {
        String router = "/business/defence/BOUNDARY_ALARM_INFO/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\"\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    /**
     * @description: 3.7 告警记录(分页查询)
     * @author: liao
     * @time:
     */
    public JSONObject alarmLogPage(String deviceId, int page, int size) throws Exception {
        String router = "/business/defence/ALARM_LOG_PAGE/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"size\":\"" + size + "\"\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    /**
     * @description: 3.8 告警记录处理
     * @author: liao
     * @time:
     */
    public JSONObject alarmLogOperate(String alarmId, String operator, String optResult) throws Exception {
        String router = "/business/defence/ALARM_LOG_OPERATE/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"alarm_id\":\"" + alarmId + "\",\n" +
                        "    \"operator\":\"" + operator + "\",\n" +
                        "    \"opt_result\":\"" + optResult + "\"\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    /**
     * @description: 3.9 设备画面人数告警设置
     * @author: liao
     * @time:
     */
    public JSONObject deviceCustomerNumAlarmAdd(String deviceId, int threshold) throws Exception {
        String router = "/business/defence/DEIVCE_CUSTOMER_NUM_ALARM_ADD/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"threshold\":\"" + threshold + "\"\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    /**
     * @description: 3.9 设备画面人数告警删除
     * @author: liao
     * @time:
     */
    public JSONObject deviceCustomerNumAlarmDelete(String deviceId) throws Exception {
        String router = "/business/defence/DEIVCE_CUSTOMER_NUM_ALARM_ADD/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\"\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }


//    ******************************************************四、历史记录**********************************************************************

    /**
     * @description: 4.1 人脸识别记录分页查询
     * @author: liao
     * @time:
     */
    public JSONObject customerHistoryCapturePage(String namePhone, String deviceId, long startTime, long endTime,
                                                 int page, int size) throws Exception {
        String router = "/business/defence/CUSTOMER_HISTORY_CAPTURE_PAGE/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"name_phone\":\"" + namePhone + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\",\n" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"size\":\"" + size + "\"\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }


//    **********************************************************五、刑侦辅助**********************************************************

    /**
     * @description: 5.1 轨迹查询(人脸搜索)
     * @author: liao
     * @time:
     */
    public JSONObject customerFaceTraceList(String picUrl, long startTime, long endTime,
                                                 String similarity) throws Exception {
        String router = "/business/defence/CUSTOMER_FACE_TRACE_LIST/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"pic_url\":\"" + picUrl + "\",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\",\n" +
                        "    \"similarity\":\"" + similarity + "\"\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    /**
     * @description: 5.2 结构化检索(分页查询)
     * @author: liao
     * @time:
     */
    public JSONObject customerSearchList(String deviceId, long startTime, long endTime,
                                         String sex, String age, String hair, String clothes, String clothesColour,
                                         String trousers, String trousersColour, String hat, String knapsack) throws Exception {
        String router = "/business/defence/CUSTOMER_SEARCH_LIST/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\",\n" +
                        "    \"other_query\":{\n" +
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

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    public JSONObject customerSearchList(String deviceId, long startTime, long endTime) throws Exception {
        String router = "/business/defence/CUSTOMER_SEARCH_LIST/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\"\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    /**
     * @description: 5.3 人物详情信息
     * @author: liao
     * @time:
     */
    public JSONObject customerInfo(String userId, String customerId) throws Exception {
        String router = "/business/defence/CUSTOMER_INFO/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"user_id\":\"" + userId + "\",\n" +
                        "    \"customer_id\":\"" + customerId + "\"\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

//    ***************************************************五、消息通知**********************************************************

    /**
     * @description: 5.4 实时通知开关
     * @author: liao
     * @time:
     */
    public JSONObject messageSwitch(String messageSwitch,String messageType) throws Exception {
        String router = "/business/defence/MESSAGE_SWITCH/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"message_switch\":\"" + messageSwitch + "\"\n" +
                        "    \"message_type\":\"" + messageType + "\"\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }



//    *****************************************************六、监控************************************************************

    /**
     * @description: 6.1 设备画面播放(实时)
     * @author: liao
     * @time:
     */
    public JSONObject deviceStream(String deviceId) throws Exception {
        String router = "/business/defence/DEVICE_STREAM/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    /**
     * @description: 6.1 设备画面播放(历史)
     * @author: liao
     * @time:
     */
    public JSONObject deviceStream(String deviceId, long startTime, long endTime) throws Exception {
        String router = "/business/defence/DEVICE_STREAM/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\"\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

//    ***********************************************************七、统计信息*************************************************

    /**
     * @description: 7.1 设备实时-客流统计
     * @author: liao
     * @time:
     */
    public JSONObject deviceCustomerFlowStatistic(String deviceId) throws Exception {
        String router = "/business/defence/DEVICE_CUSTOMER_FLOW_STATISTIC/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\"\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    /**
     * @description: 7.2 设备实时-客流统计
     * @author: liao
     * @time:
     */
    public JSONObject deviceAlarmStatistic(String deviceId) throws Exception {
        String router = "/business/defence/DEVICE_ALARM_STATISTIC/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\"\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }


//    #########################################################接口调用方法########################################################


//    #########################################################数据验证方法########################################################

    public void checkNotCode(ApiResponse apiResponse, String router, int expectNot, String message) throws Exception {

        int code = apiResponse.getCode();

        String msg = "gateway: http://dev.api.winsenseos.cn/retail/api/data/biz, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse) +
                "expect don't return code: " + expectNot + ".";

        if (expectNot == code) {
            Assert.assertNotEquals(code, expectNot, msg);
        }
    }

    public void checkCode(ApiResponse apiResponse, String router, int expectCode) throws Exception {
        try {
            if (apiResponse.getCode() != expectCode) {
                String msg = "gateway: http://dev.api.winsenseos.cn/retail/api/data/biz, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse) +
                        "actual code: " + apiResponse.getCode() + " expect code: " + expectCode + ".";
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

    public String genPhoneNum() {
        Random random = new Random();
        String num = "177" + (random.nextInt(89999999) + 10000000);

        return num;
    }

    public String genRandom7() {

        String tmp = UUID.randomUUID() + "";

        return tmp.substring(tmp.length() - 7);
    }

    public String genRandom() {

        String tmp = UUID.randomUUID().toString();

        return tmp;
    }

//    #########################################数据验证方法######################################################

//##############################################发送请求，存储数据方法#############################################################

    public ApiResponse sendRequest(String router, String[] resource, String json) {
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

    public JSONObject sendRequestCode1000(String router, String[] resource, String json) throws Exception {

        ApiResponse apiResponse = sendRequest(router, resource, json);
        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return JSON.parseObject(JSON.toJSONString(apiResponse));
    }

    public void clean() {
        qaDbUtil.closeConnection();
        qaDbUtil.closeConnectionRdDaily();
        dingPushFinal();
    }

    public void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

//            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 华成裕
            //18513118484 杨航
            //15011479599 谢志东
            //18600872221 蔡思明
            String[] rd = {
                    "18513118484", //杨航
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

            String message = "立体安防（社区版本）日常 \n" +
                    "验证：" + aCase.getCaseDescription() +
                    " \n\n" + failReason;

            dingPush(aCase, message);
        }
    }

    public void dingPush(Case aCase, String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
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
        aCase.setQaOwner("廖祥茹");
        aCase.setExpect("code==1000");
        aCase.setResponse(JSON.toJSONString(apiResponse));

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            if (failReason.contains("java.lang.Exception:")) {
                failReason = failReason.replace("java.lang.Exception", "异常");
            } else if (failReason.contains("java.lang.AssertionError")) {
                failReason = failReason.replace("java.lang.AssertionError", "异常");
            } else if (failReason.contains("java.lang.IllegalArgumentException")) {
                failReason = failReason.replace("java.lang.IllegalArgumentException", "异常");
            }
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }
}
