package com.haisheng.framework.testng.bigScreen.crmOnline.xmf;

import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletToken;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * @author : yu
 * @date :  2020/05/30
 */

public class AppletLoginOnline extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtilOnlineX crm = CrmScenarioUtilOnlineX.getInstance();

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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "夏明凤";
        commonConfig.referer = "https://servicewechat.com/wx0cf070e8eed63e90/";



        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.PORSCHE_ONLINE.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
//        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = EnumTestProduce.PORSCHE_ONLINE.getShopId();
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

    //小程序token
    @DataProvider(name = "APPLET_TOKENS")
    public static Object[] appletTokens() {
        return new String[]{
                EnumAppletToken.BSJ_XMF_ONLINE.getToken(),
        };
    }

    @Test(dataProvider = "APPLET_TOKENS")
    public void applet4hour(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.appletLoginToken(token);
            String customer_namea = "@@@A";
            String customer_phone_numbera = "15037286014";
            Long appoint_id = crm.appointmentTestDrive("MALE", customer_namea, customer_phone_numbera, "2022-01-01", 1, 81).getLong("appointment_id");
            crm.cancle(appoint_id);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序每4小时登陆一次，防止失效");
        }
    }
}
