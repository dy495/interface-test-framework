package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumRefer;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Random;

public class JcAppletTockenOnline extends TestCaseCommon implements TestCaseStd {

    ScenarioUtilOnline jc = new ScenarioUtilOnline();


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
        commonConfig.checklistQaOwner = "夏明凤";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName() + commonConfig.checklistQaOwner);

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.referer = EnumRefer.JIAOCHEN_REFERER_ONLINE.getReferer();

        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = "45973";
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);


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
                EnumAppletToken.JC_XMF_ONLINE.getToken(),   //xmf
        };
    }

    @Test(dataProvider = "APPLET_TOKENS")
    public void applet4hour(String token) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appletLoginToken(token);
            JSONObject data = jc.appletbanner();
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序每4小时登陆一次，防止失效");
        }
    }
}
