package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.Response;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.applet.AppletPorscheAMessageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletVoucherListScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted.AppletIntegralRecordScene;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * @author : wangmin
 * @date :  2020/05/30
 */

public class AppletLogin extends TestCaseCommon implements TestCaseStd {
    CommonConfig commonConfig = new CommonConfig();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
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

    @AfterMethod
    public void restoreProductInMsg() {
        //还原message
        for (EnumTestProduce item : EnumTestProduce.values()) {
            commonConfig.message = commonConfig.message.replace(item.getDesc(), commonConfig.TEST_PRODUCT);
        }
        commonConfig.pushQa = null;
        commonConfig.pushRd = null;
    }

    @Test(dataProvider = "BSJ_APPLET_TOKENS_DAILY", dataProviderClass = AppletLogin.class, enabled = false)
    public void BSJ_applet_daily(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            EnumTestProduce produce = EnumTestProduce.PORSCHE_DAILY;
            commonConfig.shopId = produce.getShopId();
            commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, produce.getDesc());
            commonConfig.referer = produce.getReferer();
            commonConfig.pushQa = new String[]{EnumAppletToken.getPhoneByToken(token)};
            VisitorProxy visitor = new VisitorProxy(produce);
            visitor.login(token);
            Response response = invokePorsche(visitor);
            Preconditions.checkArgument(response.getCode() == 1000, token + " " + response.getMsg());
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("保时捷小程序每小时登陆一次，防止失效");
        }
    }

    @Test(dataProvider = "BSJ_APPLET_TOKENS_ONLINE", dataProviderClass = AppletLogin.class, enabled = false)
    public void BSJ_applet_online(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            EnumTestProduce produce = EnumTestProduce.PORSCHE_ONLINE;
            commonConfig.shopId = produce.getShopId();
            commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, produce.getDesc());
            commonConfig.referer = produce.getReferer();
            commonConfig.pushQa = new String[]{EnumAppletToken.getPhoneByToken(token)};
            VisitorProxy visitor = new VisitorProxy(produce);
            visitor.login(token);
            Response response = invokePorsche(visitor);
            Preconditions.checkArgument(response.getCode() == 1000, token + " " + response.getMsg());
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("保时捷小程序每小时登陆一次，防止失效");
        }
    }

    @Test(dataProvider = "JC_APPLET_TOKENS_DAILY", dataProviderClass = AppletLogin.class)
    public void JC_applet_daily(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            EnumTestProduce produce = EnumTestProduce.JC_DAILY;
            commonConfig.shopId = produce.getShopId();
            commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, produce.getDesc());
            commonConfig.referer = produce.getReferer();
            commonConfig.pushQa = new String[]{EnumAppletToken.getPhoneByToken(token)};
            VisitorProxy visitor = new VisitorProxy(produce);
            visitor.login(token);
            Response response = invokeJC(visitor);
            Preconditions.checkArgument(response.getCode() == 1000, token + " " + response.getMessage());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("轿辰小程序每小时登陆一次，防止失效");
        }
    }

    @Test(dataProvider = "JC_APPLET_TOKENS_ONLINE", dataProviderClass = AppletLogin.class)
    public void JC_applet_online(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            EnumTestProduce produce = EnumTestProduce.JC_ONLINE;
            commonConfig.shopId = produce.getShopId();
            commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, produce.getDesc());
            commonConfig.referer = produce.getReferer();
            commonConfig.pushQa = new String[]{EnumAppletToken.getPhoneByToken(token)};
            VisitorProxy visitor = new VisitorProxy(produce);
            visitor.login(token);
            Response response = invokeJC(visitor);
            Preconditions.checkArgument(response.getCode() == 1000, token + " " + response.getMessage());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("轿辰小程序每小时登陆一次，防止失效");
        }
    }

    @Test(dataProvider = "INS_APPLET_TOKENS_DAILY", dataProviderClass = AppletLogin.class)
    public void INS_applet_daily(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            EnumTestProduce produce = EnumTestProduce.INS_DAILY;
            commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, produce.getDesc());
            commonConfig.referer = produce.getReferer();
            commonConfig.pushQa = new String[]{EnumAppletToken.getPhoneByToken(token)};
            VisitorProxy visitor = new VisitorProxy(produce);
            visitor.login(token);
            Response response = invokeIns(visitor);
            Preconditions.checkArgument(response.getCode() == 1000, token + " " + response.getMsg());
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("INS小程序每小时登陆一次，防止失效");
        }
    }

    @Test(dataProvider = "INS_APPLET_TOKENS_ONLINE", dataProviderClass = AppletLogin.class)
    public void INS_applet_online(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            EnumTestProduce produce = EnumTestProduce.INS_ONLINE;
            commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, produce.getDesc());
            commonConfig.referer = produce.getReferer();
            commonConfig.pushQa = new String[]{EnumAppletToken.getPhoneByToken(token)};
            VisitorProxy visitor = new VisitorProxy(produce);
            visitor.login(token);
            Response response = invokeIns(visitor);
            Preconditions.checkArgument(response.getCode() == 1000, token + " " + response.getMsg());
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("INS小程序每小时登陆一次，防止失效");
        }
    }

    private Response invokePorsche(VisitorProxy visitor) {
        JSONObject response = AppletPorscheAMessageListScene.builder().lastValue(null).size(20).build().invoke(visitor, false);
        return getResponseInfo(response);
    }

    private Response invokeJC(VisitorProxy visitor) {
        JSONObject response = AppletVoucherListScene.builder().type("GENERAL").size(20).build().invoke(visitor, false);
        return getResponseInfo(response);
    }

    private Response invokeIns(VisitorProxy visitor) {
        JSONObject response = AppletIntegralRecordScene.builder().lastValue(null).size(20).build().invoke(visitor, false);
        return getResponseInfo(response);
    }

    private Response getResponseInfo(JSONObject response) {
        return JSONObject.toJavaObject(response, Response.class);
    }

    @DataProvider(name = "BSJ_APPLET_TOKENS_DAILY")
    public static Object[] bsj_appletTokens_daily() {
        return new String[]{
                EnumAppletToken.BSJ_WM_DAILY.getToken(),
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
                EnumAppletToken.JC_XMF_DAILY.getToken(),
                EnumAppletToken.JC_GLY_DAILY.getToken(),
                EnumAppletToken.JC_LXQ_DAILY.getToken(),
        };
    }

    @DataProvider(name = "JC_APPLET_TOKENS_ONLINE")
    public static Object[] jc_appletTokens_online() {
        return new String[]{
                EnumAppletToken.JC_WM_ONLINE.getToken(),
                EnumAppletToken.JC_XMF_ONLINE.getToken(),
                EnumAppletToken.JC_GLY_ONLINE.getToken(),
                EnumAppletToken.JC_LXQ_ONLINE.getToken()
        };
    }

    @DataProvider(name = "INS_APPLET_TOKENS_DAILY")
    public static Object[] ins_appletTokens_daily() {
        return new String[]{
                EnumAppletToken.INS_WM_DAILY.getToken(),
                EnumAppletToken.INS_ZT_DAILY.getToken(),
        };
    }

    @DataProvider(name = "INS_APPLET_TOKENS_ONLINE")
    public static Object[] ins_appletTokens_online() {
        return new String[]{
                EnumAppletToken.INS_WM_ONLINE.getToken(),
                EnumAppletToken.INS_ZT_ONLINE.getToken(),
        };
    }
}
