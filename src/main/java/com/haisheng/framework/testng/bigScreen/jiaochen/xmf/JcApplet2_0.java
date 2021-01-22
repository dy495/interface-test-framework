package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PackFunction;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.LoginUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkArgument;

public class JcApplet2_0 extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = new ScenarioUtil();
    PublicParm pp = new PublicParm();
    CommonConfig commonConfig = new CommonConfig();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "夏明凤";
        commonConfig.product = EnumProduce.JC.name();

        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.referer = "https://servicewechat.com/wx4071a91527930b48/";

        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = "45973";
        beforeClassInit(commonConfig);
        jc.appletLoginToken(pp.appletTocken);
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

    @Test()
    public void recuse() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.pcLogin(pp.gwphone,pp.gwpassword);
            int total=jc.pcrescuePage(1,10).getInteger("total");

            jc.appletLoginToken(pp.appletTocken);
            jc.rescueApply(pp.shopIdZ,pp.coordinate);

            jc.pcLogin(pp.gwphone,pp.gwpassword);
            int totalAfter=jc.pcrescuePage(1,10).getInteger("total");
            Preconditions.checkArgument(totalAfter-total==1,"审批道路救援，pc救援列表+1");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("道路救援");
        }
    }
    @Test(description = "洗车，剩余次数-1")
    public void washCar() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.pcLogin(pp.gwphone,pp.gwpassword);
            int total=jc.pcrescuePage(1,10).getInteger("total");

            jc.appletLoginToken(pp.appletTocken);
            jc.rescueApply(pp.shopIdZ,pp.coordinate);

            jc.pcLogin(pp.gwphone,pp.gwpassword);
            int totalAfter=jc.pcrescuePage(1,10).getInteger("total");
            Preconditions.checkArgument(totalAfter-total==1,"审批道路救援，pc救援列表+1");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("道路救援");
        }
    }
  }
