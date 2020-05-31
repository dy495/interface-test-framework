package com.haisheng.framework.testng.bigScreen;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : lvxueqing
 * @date :  2020/04/07  14:03
 */

public class Menjin {

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
    public int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_MENJIN_BE_DAILY_SERVICE;

    public String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/menjin-daily-test/buildWithParameters?case_name=";

    public String DEBUG = System.getProperty("DEBUG", "true");

    DateTimeUtil dt = new DateTimeUtil();

    public  String lxq = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/lxq.JPG?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1902077328&Signature=fJencKaw6YntMvqHD8SbtoxhBNg%3D"; //我正常
    public  String yhs = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/yhs.png?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1902077463&Signature=KhvMMcIb4PBE%2B6U68RAqZI%2BlPHs%3D"; //于老师正常
    public  String lxr = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/lxr.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1902077350&Signature=91Z04ejiPVJADzNOmqJaycyYsAg%3D"; //祥茹正常
    public String ltt = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/ltt.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1902077310&Signature=MtykSi4KUlUD5plurItjqXPrU3A%3D"; //婷婷正常
    public String cat = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/cat.png?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1902077272&Signature=XdXbVE7oNXPeodLm0sL8YKIKVfI%3D"; //猫
    public String view = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/view.png?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1902077438&Signature=elxeeqn5DgGwoTbe5TPIM6oTEm0%3D"; //风景图
    public String roate90 = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/90rotate.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1902077154&Signature=CJEck7GolFFajOCVFfZg3GgxV8k%3D"; //90度旋转
    public String PeopleWithMask = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/PeopleWithMask.png?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1902077216&Signature=40jlMz2bK0Df8x03ZDzPvu7%2FJdI%3D"; //多人全遮挡
    public String lowQuality = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/lowQuality.png?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1902077289&Signature=GprWhJ0kGddNvsjc2el5PWQKhDQ%3D"; //低画质
    public String onlyOneNoMask = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/onlyOneNoMask.png?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1902077370&Signature=EHO3gOt12Qxs52nrF5vDA9GDj88%3D"; //仅一人未遮挡
    public String PeopleNoMask = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/peopleNoMask.png?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1902077394&Signature=WPsvMGKozDjoyODxvQ%2Bodt7MF%2B4%3D"; //多人全不遮挡
    public String personWithMask = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/personWithMask.png?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1902077418&Signature=OxyhSnDQnYOFFcUDmqHQZk5RxjE%3D"; //单人遮挡

    public String DisDevice = "13691"; //只放停止状态设备 层级
    public String EnDevice = "13687";//只放有启用中的设备 层级
    public String scopeUser = "13694";//人物放这个层级下 层级
    public String brand = "14635"; //一级层级
    public  String device = "7376096262751232";  //保持启用状态啊啊啊啊啊
    public  String beiyongdevice = "7376096266388480";
    public  String existUserid = "existpeopletest"; //存在的人物id
    public  String existUserscope = EnDevice; //存在的人物的层级
    public  String fifty_people = "15746"; //50个人物在的层级 //menjin.scopeAdd("50people","1","");

    String device_id1 = "7404548598596608";
    String device_id2 = "7404548601250816";
    String device_id3 = "7404548695819264";
    String device_id4 = "7404548751262720";
    String device_id5 = "7404548754244608";
    String device_id6 = "7404549893620736";
    String device_id7 = "7404550086132736";
    String device_id8 = "7404550112674816";
    String device_id9 = "7404550174344192";
    String device_id10 = "7404550270583808";
    String device_id11 = "7404550351979520";
    String device_id12 = "7404550433506304";
    String device_id13 = "7404550513132544";
    String device_id14 = "7404550608225280";
    String device_id15 = "7404550682477568";
    String device_id16 = "7404550768493568";
    String device_id17 = "7404550847267840";
    String device_id18 = "7404550938854400";
    String device_id19 = "7404551030834176";
    String device_id20 = "7404551114916864";






    public void clean() { // @AfterClass //还没改
        qaDbUtil.closeConnection();
        qaDbUtil.closeConnectionRdDaily();
        dingPushFinal();
    }

    public void initialVars() { //@BeforeMethod //还没改
        failReason = "";
        response = "";
        aCase = new Case();
    }

    private void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        //setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        //qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());
            dingPush("门禁日常-lxq\n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
//            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        msg = msg.replace("java.lang.Exception: ", "异常：");
        alarmPush.dailyRgn(msg);
        this.FAIL = true;
        Assert.assertNull(aCase.getFailReason());
    }

    private void dingPushFinal() {
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
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
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


    //-----------------------------------------------接口---------------------------------------------------------------

    //----------------------------层级管理--------------------------
    /**
     * 添加层级
     */
    public JSONObject scopeAdd(String scopeName, String scopeType, String parentID) throws Exception {
        String url = "/business/passage/SCOPE_ADD/v1.0";
        String json = "{\n" +
                "   \"scope_name\":\"" + scopeName + "\",\n";
        if (!parentID.equals("")){

            json = json + "   \"parent_id\":" + Long.parseLong(parentID) + ",\n";

        }
        json = json + "   \"scope_type\":" + Integer.parseInt(scopeType) + "\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject scopeAddNotCheck(String scopeName, String scopeType, String parentID) throws Exception {
        String url = "/business/passage/SCOPE_ADD/v1.0";
        String json = "{\n" +
                "   \"scope_name\":\"" + scopeName + "\",\n";
        if (!parentID.equals("")){

            json = json + "   \"parent_id\":" + Long.parseLong(parentID) + ",\n";

        }
        json = json + "   \"scope_type\":" + Integer.parseInt(scopeType) + "\n}";
        String res = apiCustomerRequestNotCheck(url, json);

        return JSON.parseObject(res);
    }


    /**
     * 删除层级
     */
    public JSONObject scopeDelete(String scope, String scopeType) throws Exception {
        String url = "/business/passage/SCOPE_DELETE/v1.0";
        String json = "{\n" +
                "   \"scope\":" + Long.parseLong(scope) + ",\n" +
                "   \"scope_type\":" + Integer.parseInt(scopeType) + "\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject scopeDeleteNotCheck(String scope, String scopeType) throws Exception {
        String url = "/business/passage/SCOPE_DELETE/v1.0";
        String json = "{\n" +
                "   \"scope\":" + Long.parseLong(scope) + ",\n" +
                "   \"scope_type\":" + Integer.parseInt(scopeType) + "\n}";
        String res = apiCustomerRequestNotCheck(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 层级列表
     */
    public JSONObject scopeList(Long scope) throws Exception {
        String url = "/business/passage/SCOPE_LIST/v1.0";
        String json = "{\n" +
                "   \"parent_id\":" + scope + "\n}" ;

        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject scopeList(String scope) throws Exception {
        String url = "/business/passage/SCOPE_LIST/v1.0";
        String json ="{}";
        if (!scope.equals("")){
            json = "{\n" +
                    "   \"parent_id\":" + Long.parseLong(scope) + "\n}" ;
        }

        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }


    //----------------------------人物管理--------------------------

    /**
     * 人物注册
     */
    public JSONObject userAdd(String scope, String userID, String imageType, String faceImage, String cardKey, String username) throws Exception {
        String url = "/business/passage/USER_ADD/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"image_type\":\"" + imageType + "\",\n"+
                "   \"face_image\":\"" + faceImage + "\",\n" ;
        if (!cardKey.equals("")){
            json = json + "   \"card_key\":\"" + cardKey + "\",\n";
        }
        if (!username.equals("")){
            json = json + "   \"user_name\":\"" + username + "\",\n";
        }
        json = json+ "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject userAddNotCheck(String scope, String userID, String imageType, String faceImage, String cardKey, String username) throws Exception {
        String url = "/business/passage/USER_ADD/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"image_type\":\"" + imageType + "\",\n"+
                "   \"face_image\":\"" + faceImage + "\",\n" ;
        if (!cardKey.equals("")){
            json = json + "   \"card_key\":\"" + cardKey + "\",\n";
        }
        if (!username.equals("")){
            json = json + "   \"user_name\":\"" + username + "\",\n";
        }
        json = json+ "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequestNotCheck(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 人物更新
     */
    public JSONObject userUpdate(String scope, String userID, String imageType, String faceImage, String cardKey,String user_name) throws Exception {
        String url = "/business/passage/USER_UPDATE/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"user_id\":\"" + userID + "\",\n" +
                "   \"image_type\":\"" + imageType + "\",\n";
        if (!faceImage.equals("")) {
            json = json + "   \"face_image\":\"" + faceImage + "\",\n";
        }
        if (!cardKey.equals("")){
            json = json + "   \"card_key\":\"" + cardKey + "\",\n";
        }
        if (!user_name.equals("")){
            json = json + "   \"user_name\":\"" + user_name + "\",\n";
        }
        json = json+ "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject userUpdateNotCheck(String scope, String userID, String imageType, String faceImage, String cardKey,String user_name) throws Exception {
        String url = "/business/passage/USER_UPDATE/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"user_id\":\"" + userID + "\",\n" +
                "   \"image_type\":\"" + imageType + "\",\n";
        if (!faceImage.equals("")) {
            json = json + "   \"face_image\":\"" + faceImage + "\",\n";
        }
        if (!cardKey.equals("")){
            json = json + "   \"card_key\":\"" + cardKey + "\",\n";
        }
        if (!user_name.equals("")){
            json = json + "   \"user_name\":\"" + user_name + "\",\n";
        }
        json = json+ "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequestNotCheck(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 人物删除
     */
    public JSONObject userDelete(String scope, String userID) throws Exception {
        String url = "/business/passage/USER_DELETE/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject userDeleteNotCheck(String scope, String userID) throws Exception {
        String url = "/business/passage/USER_DELETE/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequestNotCheck(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 人物信息解绑
     */
    public JSONObject userInfoDelete(String scope, String userID,String deleteType) throws Exception {
        String url = "/business/passage/USER_INFO_DELETE/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"delete_type\":\"" + deleteType + "\",\n" +
                "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject userInfoDeleteNotCheck(String scope, String userID,String deleteType) throws Exception {
        String url = "/business/passage/USER_INFO_DELETE/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"delete_type\":\"" + deleteType + "\",\n" +
                "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequestNotCheck(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 人物查询
     */
    public JSONObject userInfo(String scope, String userID) throws Exception {
        String url = "/business/passage/USER_INFO/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject userInfoNotCheck(String scope, String userID) throws Exception {
        String url = "/business/passage/USER_INFO/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequestNotCheck(url, json);

        return JSON.parseObject(res);
    }
    /**
     * 人物查询不填写userid
     */
    public JSONObject userInfo(String scope) throws Exception {
        String url = "/business/passage/USER_INFO/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\"\n";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 人物二维码获取
     */
    public JSONObject userQRCode(String scope, String userID) throws Exception {
        String url = "/business/passage/USER_QR_CODE/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //----------------------------设备管理--------------------------
    /**
     * 创建设备
     */
    public JSONObject deviceAdd(String scope, String name) throws Exception {
        String url = "/business/passage/DEVICE_ADD/v1.0";
        String json = "{\n" +
                "   \"scope\":" + Long.parseLong(scope) + ",\n" +
                "   \"name\":\"" + name + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject deviceAddNotCheck(String scope, String name) throws Exception {
        String url = "/business/passage/DEVICE_ADD/v1.0";
        String json = "{\n" +
                "   \"scope\":" + Long.parseLong(scope) + ",\n" +
                "   \"name\":\"" + name + "\"\n}";
        String res = apiCustomerRequestNotCheck(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 设备列表
     */
    public JSONObject deviceList(String scope) throws Exception {
        String url = "/business/passage/DEVICE_LIST/v1.0";
        String json ="{\n" +
                "   \"scope\":" + Long.parseLong(scope) + "\n}";

        String res = apiCustomerRequest(url, json);
        return JSON.parseObject(res);
    }
    public JSONObject deviceListNotCheck(String scope) throws Exception {
        String url = "/business/passage/DEVICE_LIST/v1.0";
        String json ="{\n" +
                "   \"scope\":" + Long.parseLong(scope) + "\n}";

        String res = apiCustomerRequestNotCheck(url, json);
        return JSON.parseObject(res);
    }

    /**
     * 启动/停止设备服务
     */
    public JSONObject operateDevice(String deviceID, String runType) throws Exception { //运行类型(ENABLE:可用/DISABLE:停用)
        String url = "/business/passage/OPERATE_DEVICE/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\",\n" +
                "   \"run_type\":\"" + runType + "\"\n}";

        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }

    //----------------------------设备管理--------------------------
    /**
     * 通行权限配置-批量
     */
    public JSONObject authAddBatch(List deviceID, String scpoe, List userID, String authType, JSONObject authConfig) throws Exception {

        String url = "/business/passage/AUTH_ADD_BATCH/v1.0";
        String json = "{\n" +
                "   \"device_id\":" + deviceID + ",\n";
        if (!scpoe.equals("")) {
            json = json + "   \"scope\":\"" + scpoe + "\",\n";
        }
        if (!userID.equals("")) {
            json = json + "   \"user_id\":" + userID + ",\n";
        }
        json = json +
                "   \"auth_type\":\"" + authType + "\",\n" +
                "   \"auth_config\":" + authConfig + "\n}";

        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject authAddBatchNotCheck(List deviceID, String scpoe, List userID, String authType, JSONObject authConfig) throws Exception {

        String url = "/business/passage/AUTH_ADD_BATCH/v1.0";
        String json = "{\n" +
                "   \"device_id\":" + deviceID + ",\n";
        if (!scpoe.equals("")) {
            json = json + "   \"scope\":\"" + scpoe + "\",\n";
        }
        if (!userID.equals("")) {
            json = json + "   \"user_id\":" + userID + ",\n";
        }
        json = json +
                "   \"auth_type\":\"" + authType + "\",\n" +
                "   \"auth_config\":" + authConfig + "\n}";

        String res = apiCustomerRequestNotCheck(url, json);

        return JSON.parseObject(res);
    }
    /**
     * 通行权限配置-单人
     */
    public JSONObject authAdd(String deviceID, String scpoe, String userID, String authType, JSONObject authConfig) throws Exception {
        String url = "/business/passage/AUTH_ADD/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\",\n";
        if (!scpoe.equals("")) {
            json = json + "   \"scope\":\"" + scpoe + "\",\n";
        }
        if (!userID.equals("")) {
            json = json + "   \"user_id\":\"" + userID + "\",\n";
        }
        json = json +
                "   \"auth_type\":\"" + authType + "\",\n" +
                "   \"auth_config\":" + authConfig + "\n}";

        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject authAddNotCheck(String deviceID, String scpoe, String userID, String authType, JSONObject authConfig) throws Exception {
        String url = "/business/passage/AUTH_ADD/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\",\n";
        if (!scpoe.equals("")) {
            json = json + "   \"scope\":\"" + scpoe + "\",\n";
        }
        if (!userID.equals("")) {
            json = json + "   \"user_id\":\"" + userID + "\",\n";
        }
        json = json +
                "   \"auth_type\":\"" + authType + "\",\n" +
                "   \"auth_config\":" + authConfig + "\n}";

        String res = apiCustomerRequestNotCheck(url, json);

        return JSON.parseObject(res);
    }



    public JSONObject authconfig(int pass_num,Long start_time, Long end_time,String interval){
        JSONObject config = new JSONObject();
        config.put("pass_num",pass_num);
        config.put("start_time",start_time);
        config.put("end_time",end_time);
        config.put("interval",interval);
        return config;
    }

    public JSONObject authconfig(int pass_num,String start_time, String end_time,String interval){
        JSONObject config = new JSONObject();
        config.put("pass_num",pass_num);
        config.put("start_time",start_time);
        config.put("end_time",end_time);
        config.put("interval",interval);
        return config;
    }

    /**
     * 删除通行权限
     */
    public JSONObject authDelete(String scope, String deviceID, List userID, String authType) throws Exception {
        String url = "/business/passage/AUTH_DELETE/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\",\n" +
                "   \"user_id\":" + userID + ",\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"auth_type\":\"" + authType + "\"\n}"; //DEVICE/USER

        String res = apiCustomerRequest(url, json);
        System.out.println(json);

        return JSON.parseObject(res);
    }

    /**
     * 使用权限id删除通行权限
     */
    public JSONObject authDelete(String authID) throws Exception {
        String url = "/business/passage/AUTH_DELETE/v1.0";
        String json = "{\n" +
                "   \"auth_id\":\"" + authID + "\"\n" +
                "\n}";

        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject authDeleteNotCheck(String authID) throws Exception {
        String url = "/business/passage/AUTH_DELETE/v1.0";
        String json = "{\n" +
                "   \"auth_id\":\"" + authID + "\"\n" +
                "\n}";

        String res = apiCustomerRequestNotCheck(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 删除设备上的全部通行权限
     */
    public JSONObject deviceauthDelete(String deviceid) throws Exception {
        String url = "/business/passage/AUTH_DELETE/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceid + "\"\n" +
                "\n}";

        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 仅删除设备的通行权限
     */
    public JSONObject deviceauthDelete(String deviceid,String authtype) throws Exception {
        String url = "/business/passage/AUTH_DELETE/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceid + "\",\n" +
                "   \"auth_type\":\"DEVICE\"\n" +
                "\n}";

        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 使用人物id删除通行权限
     */
    public JSONObject authDelete(List userID,String scope) throws Exception {
        String url = "/business/passage/AUTH_DELETE/v1.0";
        String json = "{\n" +
                "   \"user_id\":" + userID + ",\n" +
                "   \"scope\":" + scope + ",\n" +
                "   \"auth_type\":\"" + "USER" + "\"\n}"; //DEVICE/USER

        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject authDeleteNotCheck(List userID,String scope) throws Exception {
        String url = "/business/passage/AUTH_DELETE/v1.0";
        String json = "{\n" +
                "   \"user_id\":" + userID + ",\n" +
                "   \"scope\":" + scope + ",\n" +
                "   \"auth_type\":\"" + "USER" + "\"\n}"; //DEVICE/USER

        String res = apiCustomerRequestNotCheck(url, json);

        return JSON.parseObject(res);
    }



    /**
     * 通行权限列表
     */
    public JSONObject authList(String deviceID, String userID) throws Exception {
        String url = "/business/passage/AUTH_LIST/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\",\n" +
                "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }

    public JSONObject authListuser(String userID) throws Exception {
        String url = "/business/passage/AUTH_LIST/v1.0";
        String json = "{\n" +

                "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject authListdevice(String deviceID) throws Exception {
        String url = "/business/passage/AUTH_LIST/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 通行记录查询
     */
    public JSONObject passRecdList(long starttime, long endtime, String deviceID, String userID) throws Exception {
        String url = "/business/passage/PASS_RECORD_LIST/v1.0";
        String json = "{\n" +
                "   \"starttime\":\"" + starttime + "\",\n";
        if (!deviceID.equals("")){
            json = json + "   \"device_id\":\"" + deviceID + "\",\n";
        }
        if (!userID.equals("")){
            json = json + "   \"user_id\":\"" + userID + "\",\n";

        }
        json = json + "   \"endtime\":\"" + endtime + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject passRecdListNotCheck(long starttime, long endtime, String deviceID, String userID) throws Exception {
        String url = "/business/passage/PASS_RECORD_LIST/v1.0";
        String json = "{\n" +
                "   \"starttime\":\"" + starttime + "\",\n";
        if (!deviceID.equals("")){
            json = json + "   \"device_id\":\"" + deviceID + "\",\n";
        }
        if (!userID.equals("")){
            json = json + "   \"user_id\":\"" + userID + "\",\n";

        }
        json = json + "   \"endtime\":\"" + endtime + "\"\n}";
        String res = apiCustomerRequestNotCheck(url, json);

        return JSON.parseObject(res);
    }

    //----------------------------边缘端相关接口--------------------------
    /**
     * 人脸识别/门禁卡识别
     */
    public JSONObject edgeidentify(String deviceID, String type, String identifyStr) throws Exception {
        String url = "/business/passage/EDGE_IDENTIFY/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\",\n" +
                "   \"type\":\"" + type + "\",\n" +
                "   \"identify_str\":\"" + identifyStr + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
    public JSONObject edgeidentifyNotCheck(String deviceID, String type, String identifyStr) throws Exception {
        String url = "/business/passage/EDGE_IDENTIFY/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\",\n" +
                "   \"type\":\"" + type + "\",\n" +
                "   \"identify_str\":\"" + identifyStr + "\"\n}";
        String res = apiCustomerRequestNotCheck(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 人物权限查询
     */
    public JSONObject auth(String deviceID, String userID) throws Exception {
        String url = "/business/passage/EDGE_AUTH/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\",\n" +
                "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 权限配置信息同步
     */
    public JSONObject authSync(String deviceID) throws Exception {
        String url = "/business/passage/EDGE_AUTH_SYNC/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 通行日志上传
     *
     */
    public JSONObject passageUpload(String deviceID, String userID,long time, String type , String identify_str , String is_pass) throws Exception {
        String url = "/business/passage/EDGE_PASSAGE_UPLOAD/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\",\n" +
                "   \"time\":" + time + ",\n" +
                "   \"pass_type\":\"" + type + "\",\n" +
                "   \"identify_str\":\"" + identify_str + "\",\n" +
                "   \"is_pass\":\"" + is_pass + "\",\n" +
                "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }



    /**
     * 增量同步
     */
    public JSONObject incSync(String deviceID, String data_type,Long timestamp) throws Exception {
        String url = "/business/passage/EDGE_INCR_DATA_SYNC/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\",\n" +
                "   \"timestamp\":" + timestamp + ",\n" +
                "   \"data_type\":\"" + data_type + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 全量同步
     */
    public JSONObject fullSync(String deviceID, String fileType) throws Exception {
        String url = "/business/passage/EDGE_FULL_DATA_SYNC/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\",\n" +
                "   \"data_type\":\"" + fileType + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }


    private String apiCustomerRequest(String router, String json) throws Exception {
        try {

            long start = System.currentTimeMillis();
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid("uid_e0d1ebec")
                    .appId("a4d4d18741a8")
                    .version(SdkConstant.API_VERSION)
                    .requestId(requestId)
                    .router(router)
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            //System.out.println("aaaaaa"+JSON.toJSONString(apiRequest));
            logger.info("{} json param: {} requestid {}", router, json,requestId);
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.cn/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logger.info(JSON.toJSONString(apiResponse));
            logger.info("{} time used {} ms", router, System.currentTimeMillis() - start);
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                //Preconditions.checkArgument(1==2,"状态码"+apiResponse+"message:" + msg);
                throw new Exception(msg);
            }
            // printPvUvInfo(JSON.toJSONString(apiResponse));

            return JSON.toJSONString(apiResponse);


        } catch (Exception e) {
            throw e;
        }
    }

    private String apiCustomerRequestNotCheck(String router, String json) throws Exception {
        try {

            long start = System.currentTimeMillis();
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid("uid_e0d1ebec")
                    .appId("a4d4d18741a8")
                    .version(SdkConstant.API_VERSION)
                    .requestId(requestId)
                    .router(router)
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            //System.out.println("aaaaaa"+JSON.toJSONString(apiRequest));
            logger.info("{} json param: {}", router, json);
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.cn/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logger.info(JSON.toJSONString(apiResponse));
            logger.info("{} time used {} ms", router, System.currentTimeMillis() - start);
            return JSON.toJSONString(apiResponse);


        } catch (Exception e) {
            throw e;
        }
    }


    private String apiAlgorithmRequest(String router, String json) throws Exception {
        try {

            long start = System.currentTimeMillis();
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid("uid_e0d1ebec")
                    .appId("a4d4d18741a8")
                    .version(SdkConstant.API_VERSION)
                    .requestId(requestId)
                    .router(router)
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            logger.info("{} json param: {}", router, json);
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.cn/retail/api/data/device", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logger.info(JSON.toJSONString(apiResponse));
            //if(! apiResponse.isSuccess()) {
            //   String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
            //    throw new Exception(msg);
            // }
            // printPvUvInfo(JSON.toJSONString(apiResponse));
            logger.info("{} time used {} ms", router, System.currentTimeMillis() - start);
            return JSON.toJSONString(apiResponse);


        } catch (Exception e) {
            throw e;
        }
    }

    //-------------------------方法-------------------------
    public long todayStartLong() throws ParseException {//今天的00：00：00
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");//设置日期格式,今天的0点之前
        String datenow = df.format(new Date());// new Date()为获取当前系统时间，2020-02-18 00:00:00
        Date date = df.parse(datenow);
        long ts = date.getTime(); //转换为时间戳1581955200000
        System.out.println(ts);
        return ts;
    }

    public long todayEndLong() throws ParseException { //明天的00：00：00
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, +1);
        Date d = c.getTime();
        String day = format.format(d);
        long yesterdray = Long.parseLong(day);
        System.out.println(yesterdray);
        return yesterdray;
    }

    public String HHmmss(int n){ //返回时间格式，前n个小时的时间 19:08:39
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - n);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(calendar.getTime());

    }



}

