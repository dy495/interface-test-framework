package com.haisheng.framework.testng.bigScreen;

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
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.model.bean.ProtectTime;
import com.haisheng.framework.model.bean.ReportTime;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    public String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    public String DEBUG = System.getProperty("DEBUG", "true");

    DateTimeUtil dt = new DateTimeUtil();

    public  String lxq = "https://thumbnail0.baidupcs.com/thumbnail/a096cb0e3p286ff77a27687d8fa3f6f8?fid=4209926431-250528-714156240075946&time=1586401200&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-aKKjXIEXnuKAKQdvvJQwl7pKUss%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=2306939513356739217&dp-callid=0&size=c710_u400&quality=100&vuk=-&ft=video"; //吕雪晴正常
    public  String yhs = "https://thumbnail0.baidupcs.com/thumbnail/7d5105873hbbc9c95b7f0956a45adccd?fid=4209926431-250528-31392995574561&time=1586401200&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-%2FlV79JPwQt5sAg0zf1mUwmEmpGY%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=2306865972269625128&dp-callid=0&size=c710_u400&quality=100&vuk=-&ft=video"; //于老师正常
    public  String lxr = "https://thumbnail0.baidupcs.com/thumbnail/34f0ab4c9n8e91ba4801cb6aed220846?fid=4209926431-250528-592843236387658&time=1586422800&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-1HqZnGtwMJF6%2FT9e0cnbFsxD%2BD8%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=2312670824049826485&dp-callid=0&size=c710_u400&quality=100&vuk=-&ft=video"; //祥茹正常
    public String ltt = "https://thumbnail0.baidupcs.com/thumbnail/f875a24d8g9cda1029d1a07359b8775f?fid=4209926431-250528-451552793641815&time=1586422800&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-aDkPY3u6qJzOvhd97I5us4tk%2FZk%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=2312691024525370753&dp-callid=0&size=c710_u400&quality=100&vuk=-&ft=video"; //婷婷正常
    public String cat = "https://thumbnail0.baidupcs.com/thumbnail/09ec1d8e1r04cf5a564b0e098e2133f3?fid=4209926431-250528-946203800536222&time=1586401200&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-UQF%2BpTCIYwiKqwWKD9AVdgHGc%2Fo%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=2306887787239245735&dp-callid=0&size=c710_u400&quality=100&vuk=-&ft=video"; //猫
    public String view = "https://thumbnail0.baidupcs.com/thumbnail/073bffe7cpc649d9fc3fd1c3e3c0124a?fid=4209926431-250528-320699373258419&time=1586401200&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-RxR1ZSmAbeohIlHACoCv4RjkTW0%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=898830406237994&dp-callid=0&size=c710_u400&quality=100&vuk=-&ft=video"; //风景图

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
        String url = "/business/passage/SCOPE_DELETE/v1.0";
        String json = "{\n" +
                "   \"scope_name\":\"" + scopeName + "\",\n";
        if (!parentID.equals("")){
            json = json + "   \"parent_id\":\"" + parentID + "\",\n";

        }
        json = json + "   \"scope_type\":\"" + scopeType + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 删除层级
     */
    public JSONObject scopeDelete(String scope, String scopeType) throws Exception {
        String url = "/business/passage/SCOPE_DELETE/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"scope_type\":\"" + scopeType + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 层级列表
     */
    public JSONObject scopeList(String scope, String scopeType) throws Exception {
        String url = "/business/passage/SCOPE_LIST/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"scope_type\":\"" + scopeType + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //----------------------------人物管理--------------------------

    /**
     * 人物注册
     */
    public JSONObject userAdd(String scope, String userID, String imageType, String faceImage, String cardKey, String username) throws Exception {
        String url = "/business/passage/USER_ADD/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"user_id\":\"" + userID + "\",\n" +
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

    /**
     * 人物更新
     */
    public JSONObject userUpdate(String scope, String userID, String imageType, String faceImage, String cardKey) throws Exception {
        String url = "/business/passage/USER_ADD/v1.0";
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
        json = json+ "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequest(url, json);

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

    /**
     * 人物信息解绑
     */
    public JSONObject userInfoDelete(String scope, String userID,String deleteType) throws Exception {
        String url = "/business/passage/USER_DELETE/v1.0";
        String json = "{\n" +
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"delete_type\":\"" + deleteType + "\",\n" +
                "   \"user_id\":\"" + userID + "\"\n}";
        String res = apiCustomerRequest(url, json);

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

    /**
     * 人物二维码获取
     */
    public JSONObject userQRCode(String scope, String userID) throws Exception {
        String url = "/business/passage/SCOPE_LIST/v1.0";
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
                "   \"scope\":\"" + scope + "\",\n" +
                "   \"name\":\"" + name + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 设备列表
     */
    public JSONObject deviceList(String scope) throws Exception {
        String url = "/business/passage/DEVICE_LIST/v1.0";
        String json = "{\n" +
                    "   \"scope\":\"" + scope + "\"\n";

        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res).getJSONObject("data");
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
     * 通行权限配置
     */
    public JSONObject authAdd(List deviceID, String scpoe, List userID, String authType, JSONObject authConfig) throws Exception {
        String url = "/business/passage/AUTH_ADD/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\",\n";
        if (!scpoe.equals("")) {
            json = json + "   \"scpoe\":\"" + scpoe + "\",\n";
        }
        if (!userID.equals("")) {
            json = json + "   \"user_id\":\"" + userID + "\",\n";
        }
        json = json +
                "   \"auth_type\":\"" + authType + "\",\n" +
                "   \"auth_config\":\"" + authConfig + "\"\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 删除通行权限
     */
    public JSONObject authDelete(String authID, String deviceID, List userID, String authType) throws Exception {
        String url = "/business/passage/AUTH_DELETE/v1.0";
        String json = "{\n" +
                "   \"auth_id\":\"" + authID + "\",\n" +
                "   \"device_id\":\"" + deviceID + "\",\n" +
                "   \"user_id\":\"" + userID + "\",\n" +
                "   \"auth_type\":\"" + authType + "\"\n}"; //DEVICE/USER

        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 使用权限id删除通行权限
     */
    public JSONObject authDelete(String authID) throws Exception {
        String url = "/business/passage/AUTH_DELETE/v1.0";
        String json = "{\n" +
                "   \"auth_id\":\"" + authID + "\",\n" +
                "\n}";

        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }

    /**
     * 使用renyid删除通行权限
     */
    public JSONObject authDelete(List userID) throws Exception {
        String url = "/business/passage/AUTH_DELETE/v1.0";
        String json = "{\n" +
                "   \"user_id\":\"" + userID + "\",\n" +
                "   \"auth_type\":\"" + "USER" + "\"\n}"; //DEVICE/USER

        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }



    /**
     * 通行权限列表
     */
    public JSONObject authList(List deviceID, List userID) throws Exception {
        String url = "/business/passage/AUTH_LIST/v1.0";
        String json = "{\n" +
                "   \"device_id\":\"" + deviceID + "\",\n" +
                "   \"user_id\":\"" + userID + "\"\n}";
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

        return JSON.parseObject(res).getJSONObject("data");
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

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 通行日志上传
     */


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
            logger.info("{} json param: {}", router, json);
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.cn/retail/api/data/biz", credential);
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

    @Test
    public void  aaa() throws Exception {
        String json = "{\"scope\":\"4116\"}";
        apiCustomerRequest("/business/passage/DEVICE_LIST/v1.0",json);
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
        return yesterdray;
    }



}
