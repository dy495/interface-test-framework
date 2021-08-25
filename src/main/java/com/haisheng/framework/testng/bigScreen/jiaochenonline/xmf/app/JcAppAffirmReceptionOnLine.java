package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.DataTemp;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.JcFunctionOnline;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.PublicParamOnline;
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
 * @description :pc完成接待,数据一致性 运行单个test时，需将inintal中的存储操作函数注释掉
 * @date :2020/12/18 16:45
 **/

public class JcAppAffirmReceptionOnLine extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.JC_ONLINE_JD;
    private static final EnumAccount account = EnumAccount.JC_ONLINE_LXQ;
    private final VisitorProxy visitor = new VisitorProxy(product);
    private final SceneUtil util = new SceneUtil(visitor);
    ScenarioUtil jc = new ScenarioUtil();
    String dataName = "app_receptionOnLine";
    JcFunctionOnline pf = new JcFunctionOnline();
    PublicParamOnline pp = new PublicParamOnline();

    public void initial1() {
        logger.debug("before class initial");
        jc.changeIpPort(product.getIp());
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.XMF.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(account.getReceptionShopId()).setRoleId(account.getRoleId()).setReferer(product.getReferer()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
        util.loginApp(account);
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

    //app登录
    public void appLogin(String username, String password) {
        String path = "/account-platform/login-m-app";
        JSONObject object = new JSONObject();
        object.put("phone", username);
        object.put("verification_code", password);
        httpPost(EnumTestProduct.JC_ONLINE_ZH.getIp(), path, object);
    }

    //操作
    public void BeforeStart() {
        try {
            DataTemp dataTemp = new DataTemp();
            dataTemp.setDataName(dataName);
            dataTemp.setPcAppointmentRecordNum(pf.pcReceptionPage());  //pc接待管理数
            dataTemp.setAppReceiptage(pf.appReceptionPage());            //app[任务-接待数]
            int[] appTodayTask = pf.appTask();
            dataTemp.setAppSurplusAppointment(appTodayTask[0]);
            dataTemp.setApp_all_appointment(appTodayTask[1]);
            dataTemp.setApp_surplus_reception(appTodayTask[2]);
            dataTemp.setApp_all_reception(appTodayTask[3]);
            //pc 完成接待
            Integer receptionId = qaDbUtil.selectDataTempOne("appointmentId", dataName);
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
            jc.finishReception(receptionId.longValue(), Long.parseLong(pp.shopIdZ));
            dataTemp.setAppointmentId((long) receptionId);
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
            int result1 = qaDbUtil.selectDataTempOne("pcAppointmentRecordNum", dataName);
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
            int result1 = qaDbUtil.selectDataTempOne("appReceiptage", dataName);
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
            int[] appTask = pf.appTask();  //先调取函数可先验证此接口，在验证数据

            int app_surplus_reception = qaDbUtil.selectDataTempOne("app_surplus_reception", dataName);
            int app_all_reception = qaDbUtil.selectDataTempOne("app_all_reception", dataName);
            Preconditions.checkArgument(appTask[2] - app_surplus_reception == -1, "接待后app今日任务appSurplusAppointment,接待前：" + app_surplus_reception + "接待后：" + appTask[2]);
            Preconditions.checkArgument(appTask[3] - app_all_reception == 0, "接待后app今日任务app_all_appointment,接待前：" + app_all_reception + "接待后：" + appTask[3]);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待后，app今日任务分子分母+1");
        }
    }

    @Test(description = "今日任务数==今日数据各列数据之和")  //ok
    public void taskEqualDate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginApp(account);
            String name = pp.nameJdgw;
            String type = "all";   //home \all
            //获取今日任务数
            int[] taskNum = pf.appTask();

            int appointmentCountZ = 0;  //预约
            int appointmentCountM = 0;

            int receptioncountZ = 0;  //接待
            int receptioncountM = 0;

            int followcountZ = 0;  //跟进
            int followcountM = 0;
            //今日数据
            JSONArray todaydate = jc.apptodayDate(type, null, 100).getJSONArray("list");

            for (int i = 0; i < todaydate.size(); i++) {
                JSONObject list_data = todaydate.getJSONObject(i);
                //待处理预约数和
                String pending_appointment = list_data.getString("pending_appointment");
                if (!pending_appointment.contains("-")) {
                    String[] appointment = pending_appointment.split("/");
                    appointmentCountZ += Integer.parseInt(appointment[0]);
                    appointmentCountM += Integer.parseInt(appointment[1]);
                }

                //接待
                String pending_reception = list_data.getString("pending_reception");
                if (!pending_reception.contains("-")) {
                    String[] reception = pending_reception.split("/");
                    receptioncountZ += Integer.parseInt(reception[0]);
                    receptioncountM += Integer.parseInt(reception[1]);
                }
                //跟进
                //接待
                String pending_foll = list_data.getString("pending_follow");
                if (!pending_reception.contains("-")) {
                    String[] reception = pending_foll.split("/");
                    followcountZ += Integer.parseInt(reception[0]);
                    followcountM += Integer.parseInt(reception[1]);
                }

            }
            Preconditions.checkArgument(taskNum[0] == appointmentCountZ, name + "今日任务未处理预约数:" + taskNum[0] + "!=今日数据处理数据和" + appointmentCountZ);
            Preconditions.checkArgument(taskNum[1] == appointmentCountM, name + "今日任务总预约数:" + taskNum[1] + "!=今日数据处理数据和" + appointmentCountM);
            Preconditions.checkArgument(taskNum[2] == receptioncountZ, name + "今日任务未处理接待数:" + taskNum[2] + "!=今日数据处理数据和" + receptioncountZ);
            Preconditions.checkArgument(taskNum[3] == receptioncountM, name + "今日任务总接待数:" + taskNum[3] + "!=今日数据处理数据和" + receptioncountM);

            Preconditions.checkArgument(taskNum[4] == followcountZ, name + "今日任务未处理跟进数:" + taskNum[4] + "!=今日数据处理数据和" + followcountZ);
            Preconditions.checkArgument(taskNum[5] == followcountM, name + "今日任务总跟进数:" + taskNum[5] + "!=今日数据处理数据和" + followcountM);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日任务数=今日数据各列数据之和");
        }
    }
}
