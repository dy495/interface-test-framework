package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.bsj.Response;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.applet.AppointmentTestDriverScene;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.VoucherList;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.*;

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

    @AfterMethod
    public void restoreProductInMsg() {
        //还原message
        for (EnumTestProduce item : EnumTestProduce.values()) {
            commonConfig.message = commonConfig.message.replace(item.getName(), commonConfig.TEST_PRODUCT);
        }
    }

    @Test(dataProvider = "BSJ_APPLET_TOKENS_DAILY", dataProviderClass = AppletLogin.class)
    public void BSJ_applet_daily(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            commonConfig.shopId = EnumTestProduce.CRM_DAILY.getShopId();
            commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getName());
            commonConfig.referer = EnumTestProduce.CRM_DAILY.getReferer();
            commonConfig.pushRd = new String[]{EnumAppletToken.getPhoneByToken(token)};
            crm.appletLoginToken(token);
            Response response = getPorsche(1);
            int code = response.getCode();
            Preconditions.checkArgument(code == 1000, token + " " + response.getMsg());
            Long appointId = response.getData().getLong("appointment_id");
            crm.cancle(appointId);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("保时捷小程序每4小时登陆一次，防止失效");
        }
    }

    @Test(dataProvider = "BSJ_APPLET_TOKENS_ONLINE", dataProviderClass = AppletLogin.class)
    public void BSJ_applet_online(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            commonConfig.shopId = EnumTestProduce.CRM_ONLINE.getShopId();
            commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_ONLINE.getName());
            commonConfig.referer = EnumTestProduce.CRM_ONLINE.getReferer();
            commonConfig.pushRd = new String[]{EnumAppletToken.getPhoneByToken(token)};
            crmOnline.appletLoginToken(token);
            Response response = getPorsche(2);
            int code = response.getCode();
            Preconditions.checkArgument(code == 1000, token + " " + response.getMsg());
            Long appointId = response.getData().getLong("appointment_id");
            crmOnline.cancle(appointId);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("保时捷小程序每4小时登陆一次，防止失效");
        }
    }

    @Test(dataProvider = "JC_APPLET_TOKENS_DAILY", dataProviderClass = AppletLogin.class)
    public void JC_applet_daily(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            commonConfig.shopId = EnumTestProduce.JIAOCHEN_DAILY.getShopId();
            commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName());
            commonConfig.referer = EnumTestProduce.JIAOCHEN_DAILY.getReferer();
            commonConfig.pushRd = new String[]{EnumAppletToken.getPhoneByToken(token)};
            jc.appletLoginToken(token);
            Response response = getJiaoChen(3);
            Preconditions.checkArgument(response.getCode() == 1000, token + " " + response.getMessage());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("轿辰小程序每4小时登陆一次，防止失效");
        }
    }

    @Test(dataProvider = "JC_APPLET_TOKENS_ONLINE", dataProviderClass = AppletLogin.class)
    public void JC_applet_online(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            commonConfig.shopId = EnumTestProduce.JIAOCHEN_ONLINE.getShopId();
            commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_ONLINE.getName());
            commonConfig.referer = EnumTestProduce.JIAOCHEN_ONLINE.getReferer();
            commonConfig.pushRd = new String[]{EnumAppletToken.getPhoneByToken(token)};
            jcOnline.appletLoginToken(token);
            Response response = getJiaoChen(4);
            Preconditions.checkArgument(response.getCode() == 1000, token + " " + response.getMessage());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("轿辰小程序每4小时登陆一次，防止失效");
        }
    }

    private Response getPorsche(int produceCode) {
        String date = DateTimeUtil.addDayFormat(new Date(), 100);
        String customerName = "自动化";
        String customerPhoneNumber = "15037296015";
        int carModel = produceCode == 1 ? 36 : 81;
        IScene scene = AppointmentTestDriverScene.builder().customerGender("MALE").customerName(customerName)
                .customerPhoneNumber(customerPhoneNumber).appointmentDate(date).carModel(carModel).carStyle(1).build();
        return getResponseInfo(produceCode, scene);
    }

    private Response getJiaoChen(int produceCode) {
        IScene scene = VoucherList.builder().type("GENERAL").size(20).build();
        return getResponseInfo(produceCode, scene);
    }

    private Response getResponseInfo(int produceCode, IScene scene) {
        JSONObject data;
        switch (produceCode) {
            case 1:
                data = crm.invokeApi(scene, false);
                break;
            case 2:
                data = crmOnline.invokeApi(scene, false);
                break;
            case 3:
                data = jc.invokeApi(scene, false);
                break;
            case 4:
                data = jcOnline.invokeApi(scene, false);
                break;
            default:
                throw new IllegalStateException("无此产品代号");
        }
        return JSON.parseObject(JSON.toJSONString(data), Response.class);
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
                EnumAppletToken.JC_XMF_DAILY.getToken(),
                EnumAppletToken.JC_GLY_DAILY.getToken()
        };
    }

    @DataProvider(name = "JC_APPLET_TOKENS_ONLINE")
    public static Object[] jc_appletTokens_online() {
        return new String[]{
                EnumAppletToken.JC_WM_ONLINE.getToken(),
                EnumAppletToken.JC_XMF_ONLINE.getToken(),
                EnumAppletToken.JC_GLY_ONLINE.getToken()
        };
    }
}
