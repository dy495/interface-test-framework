package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumRefer;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumShopId;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.applet.AppointmentTestDriverScene;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.wm.util.BusinessUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author : yu
 * @date :  2020/05/30
 */

public class AppletLogin extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    CrmScenarioUtilOnline crmOnline = CrmScenarioUtilOnline.getInstance();
    ScenarioUtil jc = ScenarioUtil.getInstance();
    ScenarioUtilOnline jcOnline = ScenarioUtilOnline.getInstance();
    CommonConfig commonConfig = new CommonConfig();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //replace ding push conf
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        //set shop id
        beforeClassInit(commonConfig);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }


    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test(dataProvider = "BSJ_APPLET_TOKENS_DAILY", dataProviderClass = AppletLogin.class)
    public void BSJ_applet_daily(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            commonConfig.shopId = EnumShopId.PORSCHE_DAILY.getShopId();
            commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getName());
            String date = DateTimeUtil.addDayFormat(new Date(), 100);
            String customerName = "自动化";
            String customerPhoneNumber = "15037296015";
            IScene scene = AppointmentTestDriverScene.builder().customerGender("MALE").customerName(customerName)
                    .customerPhoneNumber(customerPhoneNumber).appointmentDate(date).carModel(36).carStyle(1).build();
            crm.appletLoginToken(token);
            JSONObject response = crm.invokeApi(scene, false);
            int code = response.getInteger("code");
            Preconditions.checkArgument(code == 1000, token + " " + response.getString("msg"));
            Long appointId = response.getJSONObject("data").getLong("appointment_id");
            crm.cancle(appointId);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("BSJ_小程序每4小时登陆一次，防止失效");
        }
    }

    @Test(dataProvider = "BSJ_APPLET_TOKENS_ONLINE", dataProviderClass = AppletLogin.class)
    public void BSJ_applet_online(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            commonConfig.shopId = EnumShopId.WINSENSE_PORSCHE_ONLINE.getShopId();
            commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_ONLINE.getName());
            commonConfig.referer = EnumRefer.PORSCHE_REFERER_ONLINE.getReferer();
            String date = DateTimeUtil.addDayFormat(new Date(), 100);
            String customerName = "自动化";
            String customerPhoneNumber = "15037296015";
            IScene scene = AppointmentTestDriverScene.builder().customerGender("MALE").customerName(customerName)
                    .customerPhoneNumber(customerPhoneNumber).appointmentDate(date).carModel(81).carStyle(1).build();
            crmOnline.appletLoginToken(token);
            JSONObject response = crmOnline.invokeApi(scene, false);
            int code = response.getInteger("code");
            Preconditions.checkArgument(code == 1000, token + " " + response.getString("msg"));
            Long appointId = response.getJSONObject("data").getLong("appointment_id");
            crmOnline.cancle(appointId);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("BSJ_小程序每4小时登陆一次，防止失效");
        }
    }

    @Test(dataProvider = "JC_APPLET_TOKENS_DAILY", dataProviderClass = AppletLogin.class)
    public void JC_applet_daily(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            commonConfig.shopId = EnumShopId.JIAOCHEN_DAILY.getShopId();
            commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName());
            commonConfig.referer = EnumRefer.JIAOCHEN_REFERER_DAILY.getReferer();
            jc.appletLoginToken(token);
            new BusinessUtil().getVoucherListSize();
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("JC_小程序每4小时登陆一次，防止失效");
        }
    }

    @Test(dataProvider = "JC_APPLET_TOKENS_ONLINE", dataProviderClass = AppletLogin.class)
    public void JC_applet_online(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            commonConfig.shopId = EnumShopId.JIAOCHEN_ONLINE.getShopId();
            commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName());
            commonConfig.referer = EnumRefer.JIAOCHEN_REFERER_ONLINE.getReferer();
            jcOnline.appletLoginToken(token);
            new BusinessUtilOnline().getVoucherListSize();
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("JC_小程序每4小时登陆一次，防止失效");
        }
    }

    @DataProvider(name = "BSJ_APPLET_TOKENS_DAILY")
    public static Object[] bsj_appletTokens_daily() {
        return new String[]{
                EnumAppletToken.BSJ_WM_DAILY.getToken(),
                EnumAppletToken.BSJ_WM_SMALL_DAILY.getToken(),
                EnumAppletToken.BSJ_XMF_DAILY.getToken(),
                EnumAppletToken.BSJ_GLY_DAILY.getToken(),
        };
    }

    @DataProvider(name = "BSJ_APPLET_TOKENS_ONLINE")
    public static Object[] bsj_appletTokens_online() {
        return new String[]{
                EnumAppletToken.BSJ_WM_ONLINE.getToken(),
                EnumAppletToken.BSJ_WM_SMALL_ONLINE.getToken(),
                EnumAppletToken.BSJ_XMF_ONLINE.getToken(),
        };
    }

    @DataProvider(name = "JC_APPLET_TOKENS_DAILY")
    public static Object[] jc_appletTokens_daily() {
        return new String[]{
                EnumAppletToken.JC_WM_DAILY.getToken(),
                EnumAppletToken.JC_XMF_DAILY.getToken()
        };
    }

    @DataProvider(name = "JC_APPLET_TOKENS_ONLINE")
    public static Object[] jc_appletTokens_online() {
        return new String[]{
                EnumAppletToken.JC_WM_ONLINE.getToken(),
                EnumAppletToken.JC_XMF_ONLINE.getToken(),
        };
    }
}
