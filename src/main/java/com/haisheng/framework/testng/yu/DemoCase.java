package com.haisheng.framework.testng.yu;

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

public class DemoCase extends TestCaseCommon implements TestCaseStd {

    ScenarioUtil scenarioUtil = ScenarioUtil.getInstance();
    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     *
     */
    @BeforeClass
    @Override
    public void initial() {
        CommonConfig commonConfig = new CommonConfig();

        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENJIN_BE_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "于海生";

        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xxxx");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门禁日常");

        //replace ding push conf
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        beforeClassInit(commonConfig);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     *
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult.getCaseName());
    }

    @AfterMethod
    public void afterMethod() {
        logger.info("afterMethod fail: " + caseResult.getFailReason());
        logger.info("afterMethod result: " + caseResult.getResult());
        logger.info("afterMethod expect: " + caseResult.getExpect());
        logger.info("afterMethod response: " + caseResult.getResponse());
    }

    @Test
    public void test1() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            scenarioUtil.scopeAdd("xxxx", "2", "111");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("demo测试");
        }


    }

    @Test
    public void test23() {

        logger.logCaseStart(caseResult.getCaseName());

        try {
            throw new Exception("测试异常");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("demo测试2");
        }

    }

    @Test
    public void testConflict() {
        logger.info("dummy case");
    }



}
