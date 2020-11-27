package com.haisheng.framework.testng.bigScreen.jiaochen;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Random;

/**
 * @description :校验但接口列表返回值各项不为空
 * @date :2020/11/27 15:30
 **/


public class emunListCheckNotNull extends TestCaseCommon implements TestCaseStd {

    ScenarioUtil jc = ScenarioUtil.getInstance();
    JsonPathUtil jpu = new JsonPathUtil();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp = new PublicParm();
    FileUtil file = new FileUtil();
    Random random = new Random();


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "xmf";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "汽车-轿辰 日常X");

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);
        jc.pcLogin(pp.gwphone, pp.gwpassword);


    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test
    public void Jc_erCode() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.apperCOde();
            String jsonpath = "$.er_code_url1";
            jpu.spiltString(data.toJSONString(), jsonpath);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-app个人中心，小程序码返回结果不为空");
        }
    }



}
