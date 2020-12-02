package com.haisheng.framework.testng.bigScreen.crm;

import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumProduce;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * @author : yu
 * @date :  2020/05/30
 */

public class appletLogin extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();

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
        commonConfig.checklistQaOwner = "lxq";
        commonConfig.produce = EnumProduce.BSJ.name();

        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "CRM 日常");

        //replace ding push conf
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
//        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
//        crm.login(sh_name1, sh_pwd1);

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

    @Test(dataProvider = "APPLET_TOKENS", dataProviderClass = CrmScenarioUtil.class)
    public void applet4hour(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.appletLoginToken(token);
            String customer_name = "lxq自动化";
            String customer_namea = "@@@";
            String customer_phone_number = "13400000000";
            String customer_phone_numbera = "15037286014";
            String date = dt.getHistoryDate(0);
            String appointment_time = "23:00";
            Long appoint_id = crm.appointmentTestDrive("MALE", customer_namea, customer_phone_numbera, "2022-01-01", 1, 36).getLong("appointment_id");
            // crm.appointmentMaintain(69L,customer_name,customer_phone_number,date,appointment_time);
            crm.cancle(appoint_id);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序每4小时登陆一次，防止失效");
        }
    }
}
