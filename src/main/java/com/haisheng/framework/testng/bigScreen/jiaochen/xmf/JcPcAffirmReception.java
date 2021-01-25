package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.DataTemp;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
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

public class JcPcAffirmReception extends TestCaseCommon implements TestCaseStd {

    ScenarioUtil jc = new ScenarioUtil();
    private QADbProxy qaDbProxy = QADbProxy.getInstance();
    public QADbUtil qaDbUtil = qaDbProxy.getQaUtil();
    JcFunction pf = new JcFunction();
    PublicParm pp = new PublicParm();
    String dataName = "pc_reception";


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

        logger.debug("jc: " + jc);
        jc.appLogin(pp.jdgw, pp.jdgwpassword);

    }


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        initial1();
        qaDbUtil.openConnection();
        BeforeStart();               //调试单个case时注释此行
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
        qaDbUtil.closeConnection();
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

    //操作
    public void BeforeStart() {
        try {
            DataTemp dataTemp = new DataTemp();
            dataTemp.setDataName(dataName);
            dataTemp.setPcAppointmentRecordNum(pf.pcReceptionPage());  //pc接待管理数
            dataTemp.setAppReceiptage(pf.appReceptionPage());            //app[任务-接待数]
            int appTodayTask[] = pf.appTask();
            dataTemp.setAppSurplusAppointment(appTodayTask[0]);
            dataTemp.setApp_all_appointment(appTodayTask[1]);
            dataTemp.setApp_surplus_reception(appTodayTask[2]);
            dataTemp.setApp_all_reception(appTodayTask[3]);
            //pc 确认接待
            Integer receptionId=qaDbUtil.selsetDataTempOne("appointmentId",dataName);
            jc.pcFinishReception((long)receptionId,pp.shopIdZ);
            dataTemp.setAppointmentId((long)receptionId);
            qaDbUtil.updateDataAll(dataTemp);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            logger.info("start:");
        }

    }

    @Test()  //接待后pc接待列表+1
    public void Pc_appointmentMessage() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int result2 = pf.pcReceptionPage();  //先调取函数可先验证此接口，在验证数据
            int result1 = qaDbUtil.selsetDataTempOne("pcAppointmentRecordNum", dataName);
            System.out.println(result1 + ":" + result2);
            Preconditions.checkArgument(result2 - result1 == 0, "接待后pc接待列表+1,接待前：" + result1 + "接待后：" + result2);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待后pc接待列表+1");
        }
    }

    @Test()  //app任务列数
    public void AppTask() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int result2 = pf.appReceptionPage();  //先调取函数可先验证此接口，在验证数据
            int result1 = qaDbUtil.selsetDataTempOne("appReceiptage", dataName);
            System.out.println(result1 + ":" + result2);
            Preconditions.checkArgument(result2 - result1 == -1, "接待后app接待任务列数,接待前：" + result1 + "接待后：" + result2);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待后，app接待任务列数+1");
        }
    }

    @Test()  //接待后，app今日任务分子分母+1
    public void AppAppointmentTodayTask() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int appTask[] = pf.appTask();  //先调取函数可先验证此接口，在验证数据

            int app_surplus_reception = qaDbUtil.selsetDataTempOne("app_surplus_reception", dataName);
            int app_all_reception = qaDbUtil.selsetDataTempOne("app_all_reception", dataName);
            Preconditions.checkArgument(appTask[2] - app_surplus_reception == -1, "接待后app今日任务appSurplusAppointment,接待前：" + app_surplus_reception + "接待后：" + appTask[2]);
            Preconditions.checkArgument(appTask[3] - app_all_reception == 0, "接待后app今日任务app_all_appointment,接待前：" + app_all_reception + "接待后：" + appTask[3]);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待后，app今日任务分子分母+1");
        }
    }


}