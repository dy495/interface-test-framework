package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.AppletCustomer;
import com.haisheng.framework.model.bean.DataTemp;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.JcFunction;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.QADbProxy;
import com.haisheng.framework.util.QADbUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * @description :运行单个test时，需将inintal中的存储操作函数注释掉
 * @date :2020/12/18 16:45
 **/

public class JcAppletUtil extends TestCaseCommon implements TestCaseStd {

    private QADbProxy qaDbProxy = QADbProxy.getInstance();
    public QADbUtil qaDbUtil = qaDbProxy.getQaUtil();



    public void initial1() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "夏明凤";
        commonConfig.referer = EnumTestProduce.JIAOCHEN_DAILY.getReferer();
//        commonConfig.referer=getJcReferdaily();


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getDesc() + commonConfig.checklistQaOwner);

        //replace ding f
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = "-1";
        beforeClassInit(commonConfig);



    }


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        initial1();
        qaDbUtil.openConnectionRdDaily();
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
        qaDbUtil.closeConnectionRdDaily();
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
    public void AppAppointmentTodayTask45() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            AppletCustomer aa=qaDbUtil.selectAppletCustomer("oBG1v5TXqcugtyGQeidgkBv_Bj0A");
            System.out.println(aa.getCustomerName());
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
//            saveData("查询指定微信id用户名);
        }
    }

    @Test()
    public void appletCustomerReturnNew() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            qaDbUtil.updateAppletCustomer("oBG1v5TXqcugtyGQeidgkBv_Bj0A");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
//            saveData("更新指定微信id 用户为新用户");
        }
    }

}
