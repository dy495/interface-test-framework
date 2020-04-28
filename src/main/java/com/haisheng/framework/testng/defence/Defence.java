package com.haisheng.framework.testng.defence;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
    public String yuFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E4%BA%8E%E6%B5%B7%E7%94%9F.jpg.png?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947974881&Signature=plWhwhEqrKWu2sKSqeJp4G2kNNo%3D";
    public String tianYuFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E5%82%85%E5%A4%A9%E5%AE%87.JPG?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947974926&Signature=U%2B5dLnWcZDp4C59X1SjoxUCkOTA%3D";
    public String qiaoFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E5%88%98%E5%B3%A4.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947974944&Signature=wBJVwlxXnImRRzlCYv%2BgkPqR5Hk%3D";
    public String huaFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E5%8D%8E%E6%88%90%E8%A3%95.JPG?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947974961&Signature=k4d1arYrxT6OhEQ6LsbuTLlC7lQ%3D";
    public String nalaFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E5%A8%9C%E6%8B%89.JPEG?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947974979&Signature=bQCvdDhiBteHpFkEyrEO8Zw0s3g%3D";
    public String kangLinFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E5%BA%B7%E7%90%B3.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947974997&Signature=62FiPW%2BxP8fhcFppLiCW848ZJmE%3D";
    public String liaoFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E5%BB%96%E7%A5%A5%E8%8C%B9.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1948075013&Signature=lAN0ho1qYA2X0AJbp5VUA%2BLq49o%3D";
    public String shengFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E5%BB%96%E8%83%9C%E6%89%8D.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947975036&Signature=sUYST7ZRx05Vp3ajblZEwtg8CeU%3D";
    public String nanhaiFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E5%BC%A0%E5%8D%97%E6%B5%B7.JPG?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947975051&Signature=5Fg3o4zrX%2BhN7ooSzthmQ1HyF%2FY%3D";
    public String zhangfanFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E5%BC%A0%E5%B8%86.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947975070&Signature=J74H9jN1KbeKYGBHyQtI5HOlqkQ%3D";
    public String xuyanFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E5%BE%90%E8%89%B3.JPG?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947975091&Signature=albEKKKGUUcl%2FUTwvC%2Fb2Evcng4%3D";
    public String junyanFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E6%9D%8E%E4%BF%8A%E5%BB%B6.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947975105&Signature=3LT%2FlbyD3ITptMPTADKd8dO1xqw%3D";
    public String tingtingFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E6%9D%8E%E5%A9%B7%E5%A9%B7.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947975120&Signature=UxMjtaA7O3b%2Ba88X4Oxw2AftvZg%3D";
    public String yanghangFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E6%9D%A8%E8%88%AA.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947975135&Signature=3jgpik%2FD5EqhlVyeHFqBnWue4rQ%3D";
    public String chuFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E6%A5%9A%E6%B1%9D%E5%B3%B0.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947975153&Signature=16zqjYYFcihHaQrQ1x24I8vMwx4%3D";
    public String wanghuanFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E7%8E%8B%E6%AC%A2.PNG?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947975166&Signature=bz3rhe5hrJZJwBaGV8nR9T8bpPY%3D";
    public String tiantianFaceUrlNew = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E7%94%98%E7%94%9C%E7%94%9C.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1947975185&Signature=bbpzT4HSYAXMxic9TbojWir%2Bjao%3D";

    public String device1Caiwu = "157";
    public String device1Huiyi = "151";
    public String deviceYilaoshi = "150";
    public String deviceXieduimen = "152";
    public String deviceChukou = "153";
    public String deviceDongbeijiao = "155";

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
//        String json =
//                "{\n" +
//                        "    \"village_id\":" + VILLAGE_ID +
//                        "}";

        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\"" +
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
                        "    \"type\":\"" + "RESIDENT" + "\"," +
                        "    \"card_key\":\"" + genRandom() + "\"," +
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

    public JSONObject customerRegBlackUserId(String userId) throws Exception {
        String router = "/business/defence/CUSTOMER_REGISTER_BLACK/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID + "," +
                        "    \"user_id\":\"" + userId + "\",\n" +
                        "    \"level\":\"" + "level" + "\",\n" +
                        "    \"label\":\"" + "label" + "\",\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    public ApiResponse customerRegBlackUserId(String userId,int expectCode) throws Exception {
        String router = "/business/defence/CUSTOMER_REGISTER_BLACK/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":" + VILLAGE_ID + "," +
                        "    \"user_id\":\"" + userId + "\",\n" +
                        "    \"level\":\"" + "level" + "\",\n" +
                        "    \"label\":\"" + "label" + "\",\n" +
                        "}";

        ApiResponse apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse,router,expectCode);

        return apiResponse;
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

    public ApiResponse customerRegBlackNewUser(String level, String label, String faceUrl, String name, String phone, String type, String cardKey,
                                              String age, String sex, String address,int expectCode) throws Exception {
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

        ApiResponse apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse,router,expectCode);

        return apiResponse;
    }

    public JSONObject customerRegBlackNewUser(String faceUrl, String level, String label) throws Exception {
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
                        "        \"type\":\"" + "RESIDENT" + "\",\n" +
                        "        \"cardKey\":\"" + genRandom() + "\",\n" +
                        "        \"age\":\"" + "20" + "\",\n" +
                        "        \"sex\":\"" + "MALE" + "\",\n" +
                        "        \"address\":\"" + "address" + "\"\n" +
                        "    }\n" +
                        "}";

        return sendRequestCode1000(router, new String[0], stringUtil.trimStr(json));
    }

    public ApiResponse customerRegBlackNewUser(String faceUrl, String level, String label,int expectCode) throws Exception {
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
                        "        \"type\":\"" + "RESIDENT" + "\",\n" +
                        "        \"cardKey\":\"" + genRandom() + "\",\n" +
                        "        \"age\":\"" + "20" + "\",\n" +
                        "        \"sex\":\"" + "MALE" + "\",\n" +
                        "        \"address\":\"" + "address" + "\"\n" +
                        "    }\n" +
                        "}";

        ApiResponse apiResponse = sendRequest(router, new String[0], stringUtil.trimStr(json));

        checkCode(apiResponse,router,expectCode);

        return apiResponse;
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
                        "            \"x\":" + 0.0 + ",\n" +
                        "            \"y\":" + 0.0 + "\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":" + 0.0 + ",\n" +
                        "            \"y\":" + 1.0 + "\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":" + 1.0 + ",\n" +
                        "            \"y\":" + 0.0 + "\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":" + 1.0 + ",\n" +
                        "            \"y\":" + 1.0 + "\n" +
                        "        }\n" +
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
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n";
        if (!"".equals(deviceId)){
            json += "    \"device_id\":\"" + deviceId + "\",\n";
        }

        json+=          "    \"page\":\"" + page + "\",\n" +
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
        String router = "/business/defence/DEIVCE_CUSTOMER_NUM_ALARM_DELETE/v1.0";
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
    public JSONObject customerHistoryCapturePage(String faceUrl, String deviceId, long startTime, long endTime,
                                                 int page, int size) throws Exception {
        String router = "/business/defence/CUSTOMER_HISTORY_CAPTURE_PAGE/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n";

        if (!"".equals(faceUrl)){
            json+="    \"face_url\":\"" + faceUrl + "\",\n";
        }

        if (!"".equals(faceUrl)){
            json+="    \"device_id\":\"" + deviceId + "\",\n";
        }
        json+=
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
                                         String trousers, String trousersColour, String hat, String knapsack,String similarity) throws Exception {
        String router = "/business/defence/CUSTOMER_SEARCH_LIST/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"end_time\":\"" + endTime + "\",\n" +
                        "    \"similarity\":\"" + similarity + "\",\n" +
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
    public JSONObject messageSwitch(String messageSwitch,String messageType,long frequency) throws Exception {
        String router = "/business/defence/MESSAGE_SWITCH/v1.0";
        String json =
                "{\n" +
                        "    \"village_id\":\"" + VILLAGE_ID + "\",\n" +
                        "    \"message_switch\":\"" + messageSwitch + "\",\n" +
                        "    \"message_type\":\"" + messageType + "\",\n" +
                        "    \"frequency\":\"" + frequency + "\"\n" +
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


    public void checkBlackList(String level, String label, String faceUrl, String name, String phone, String type, String cardKey,
                               String age, String sex, String address) throws Exception {

        JSONArray blackList = customerBlackPage(1, 10).getJSONObject("data").getJSONArray("list");

        for (int i = 0; i < blackList.size(); i++) {
            JSONObject single = blackList.getJSONObject(i);
            String alarmCustomerId = single.getString("alarm_customer_id");


        }


    }


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
            int codeRes = apiResponse.getCode();
            if (codeRes != expectCode) {
                String msg = "gateway: http://dev.api.winsenseos.cn/retail/api/data/biz, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse) +
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

    public String genCharLen(int length) {

        StringBuffer stringBuffer = new StringBuffer();

        String normalChar = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890-";
        Random random = new Random();
        int i = random.nextInt(normalChar.length());

        for (int j = 0; j < length; j++) {
            stringBuffer.append(normalChar.charAt(i));
        }

        return stringBuffer.toString();
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

        logger.info(router);

        ApiResponse apiResponse = sendRequest(router, resource, json);
        checkCode(apiResponse, router, StatusCode.SUCCESS);

        return JSON.parseObject(JSON.toJSONString(apiResponse));
    }

    public void initial() {
        qaDbUtil.openConnection();
        qaDbUtil.openConnectionRdDaily();
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
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
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

    public void test(){
        String url = "http://39.97.210.227/village/capture/2";

    }
}
