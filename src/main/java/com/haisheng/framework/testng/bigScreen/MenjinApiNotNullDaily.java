
package com.haisheng.framework.testng.bigScreen;

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
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.QADbUtil;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author : lvxueqing
 * @date :  2020/04/07  10:37
 */

public class MenjinApiNotNullDaily {


    Menjin menjin = new Menjin();
    //    ----------------------------------------------变量定义--------------------------------------------------------------------
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String failReason = "";

    private String response = "";

    private boolean FAIL = false;

    private Case aCase = new Case();

    CheckUtil checkUtil = new CheckUtil();

    private QADbUtil qaDbUtil = new QADbUtil();

    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;

    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");


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


    @AfterClass //还没改
    public void clean() {
        qaDbUtil.closeConnection();
        dingPushFinal();
    }

    @BeforeMethod //还没改
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

    /**
     * 校验 层级列表（/business/passage/SCOPE_LIST/v1.0）字段非空
     */
    @Test
    public void shopListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：层级列表（/business/passage/SCOPE_LIST/v1.0）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = menjin.scopeList("","");
            for (Object obj : scopeListNotNull()) {
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
     * 校验 人物二维码获取（/business/passage/USER_QR_CODE/v1.0）字段非空
     */
    @Test
    public void userQRCodeNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人物二维码获取（/business/passage/USER_QR_CODE/v1.0）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = menjin.userQRCode("","");
            for (Object obj : userQRCodeNotNull()) {
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
     * 校验 设备列表（/business/passage/DEVICE_LIST/v1.0）字段非空
     */
    @Test
    public void deviceListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：设备列表（/business/passage/DEVICE_LIST/v1.0）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = menjin.deviceList("");
            for (Object obj : deviceListNotNull()) {
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
     * 校验 通行权限列表（/business/passage/AUTH_LIST/v1.0）字段非空
     */
    @Test
    public void authListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：通行权限列表（/business/passage/AUTH_LIST/v1.0）关键字段非空\n";

        String key = "";

        try {
            List<String> deviceID=new ArrayList<>();
            List<String> userID=new ArrayList<>();
            JSONObject data = menjin.authList(deviceID,userID);
            for (Object obj : authListNotNull()) {
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
     * 校验 通行记录查询（/business/passage/PASS_RECORD_LIST/v1.0）字段非空
     */
    @Test
    public void passRecdListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：通行记录查询（/business/passage/PASS_RECORD_LIST/v1.0）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = menjin.passRecdList(1586230260L,1586230260L,"","");
            for (Object obj : passRecdListNotNull()) {
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
     * 校验 人脸识别/门禁卡识别（/business/passage/IDENTIFY/v1.0）字段非空
     */
    @Test
    public void identifyNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸识别/门禁卡识别（/business/passage/IDENTIFY/v1.0）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = menjin.identify("","","");
            for (Object obj : identifyNotNull()) {
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
     * 校验 人物权限查询（/business/passage/AUTH/v1.0）字段非空
     */
    @Test
    public void authNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人物权限查询（/business/passage/AUTH/v1.0）关键字段非空\n";

        String key = "";

        try {
            JSONObject data = menjin.auth("","");
            for (Object obj : authListNotNull()) {
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

//    ---------------------------------------------------要判断的字段--------------------------------------------------------------
    private Object[] scopeListNotNull() {
        return new Object[]{
                "[list]-scope",
                "[list]-scope_name",

        };
    }

    private Object[] userQRCodeNotNull() {
        return new Object[]{
                "user_id",
                "qr_image_url",

        };
    }

    private Object[] deviceListNotNull() {
        return new Object[]{
                "[device_list]-device_id",
                "[device_list]-name",
                "[device_list]-type_name",
                "[device_list]-device_status",
                "[device_list]-status_name",
                "[device_list]-run_status",

        };
    }

    private Object[] authListNotNull() {
        return new Object[]{
                "[list]-device_id",
                "[list]-user_id",
                "[list]-auth_config",
                "[list]-auth_type",

        };
    }

    private Object[] passRecdListNotNull() {
        return new Object[]{
                "[list]-device_id",
                "[list]-user_id",
                "[list]-time",
                "[list]-pass_type",

        };
    }

    private Object[] identifyNotNull() {
        return new Object[]{
                "user_info",
                "has_auth",
                "user_info-user_id",
                "user_info-user_name",
                "user_info-card_Key",

        };
    }

    private Object[] authNotNull() {
        return new Object[]{
                "auth",
                "has_auth",
                "auth-pass_num",
                "auth-start_time",
                "auth-end_time",

        };
    }



    private void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        //setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());
            dingPush("门禁日常-非空校验 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
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






}