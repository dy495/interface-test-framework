package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.DataTemp;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
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

public class JcAppointmentRelateOnLine extends TestCaseCommon implements TestCaseStd {
    EnumTestProduct product = EnumTestProduct.JC_ONLINE_JD;
    ScenarioUtil jc = new ScenarioUtil();
    private final QADbProxy qaDbProxy = QADbProxy.getInstance();
    public QADbUtil qaDbUtil = qaDbProxy.getQaUtil();
    String dataName = "pc_appointmentPageOnLine";

    JcFunctionOnline pf = new JcFunctionOnline();
    PublicParmOnline pp = new PublicParmOnline();
    int num = pp.num;   //预约天数控制


    public void initial1() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        jc.changeIpPort(product.getIp());


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "夏明凤";
        commonConfig.referer = product.getReferer();
        commonConfig.product = product.getAbbreviation();

        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);


        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};
//        commonConfig.referer="http://dev.dealer-jc.winsenseos.cn/authpage/login";
        //set shop id
        commonConfig.shopId = pp.shopIdZ;
        commonConfig.roleId = pp.roleidJdgw;
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);
        jc.pcLogin(pp.jdgw, pp.jdgwpassword);


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
            dataTemp.setPcAppointmentRecordNum(pf.pcAppointmentRecodePage());
            dataTemp.setAppReceiptage(pf.appReceiptPage());
            dataTemp.setPcAppointmentNUmber(pf.appointmentNUmber(num));
            int appTodayTask[] = pf.appTask();
            dataTemp.setAppSurplusAppointment(appTodayTask[0]);
            dataTemp.setApp_all_appointment(appTodayTask[1]);
            dataTemp.setApp_surplus_reception(appTodayTask[2]);
            dataTemp.setApp_all_reception(appTodayTask[3]);
            dataTemp.setPc_appointment_times(pf.pcAppointmentTimes());
            dataTemp.setApplet_myappointment(pf.appletmyAppointment());

            //预约
            logger.info("开始预约");
            dataTemp.setAppointmentId(pf.appletAppointment(num));

            System.out.println("PcNUm:" + dataTemp.getPcAppointmentNUmber());

            qaDbUtil.updateDataAll(dataTemp);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            logger.info("start:");
        }

    }

    @Test()  //预约后，预约记录数+1
    public void Pc_appointmentMessage() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int result2 = pf.pcAppointmentRecodePage();  //先调取函数可先验证此接口，在验证数据
            int result1 = qaDbUtil.selsetDataTempOne("pcAppointmentRecordNum", "pc_appointmentPage");
            System.out.println(result1 + ":" + result2);
            Preconditions.checkArgument(result2 - result1 == 1, "预约后预约记录数没+1,预约前：" + result1 + "预约后：" + result2);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约后，预约记录数+1");
        }
    }

    @Test()  //预约后，预约看板预约数+1
    public void Pc_appointmentTimeTable() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int result2 = pf.appointmentNUmber(num);  //先调取函数可先验证此接口，在验证数据
            int result1 = qaDbUtil.selsetDataTempOne("pcAppointmentNUmber", "pc_appointmentPage");
            System.out.println(result1 + ":" + result2);
            Preconditions.checkArgument(result2 - result1 == 1, "预约后预约看板数没+1,预约前：" + result1 + "预约后：" + result2);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约后，预约看板数+1");
        }
    }

    @Test()  //预约后，该小程序客户预约次数
    public void Pc_customerAppointmentTimes() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int result2 = pf.pcAppointmentTimes();
            int result1 = qaDbUtil.selsetDataTempOne("pc_appointment_times", "pc_appointmentPage");
            System.out.println(result1 + ":" + result2);
            Preconditions.checkArgument(result2 - result1 == 1, "预约后,该小程序客户预约次数没+1，预约前：" + result1 + "预约后：" + result2);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约后，该小程序客户预约次数");
        }
    }


    @Test()  //预约后，app预约任务列数
    public void AppAppointmentTask() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int result2 = pf.appReceiptPage();  //先调取函数可先验证此接口，在验证数据
            int result1 = qaDbUtil.selsetDataTempOne("appReceiptage", "pc_appointmentPage");
            System.out.println(result1 + ":" + result2);
            Preconditions.checkArgument(result2 - result1 == 1, "预约后app预约任务列数,预约前：" + result1 + "预约后：" + result2);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约后，app预约任务列数");
        }
    }

    @Test()  //预约后，app今日任务分子分母+1
    public void AppAppointmentTodayTask() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int appTask[] = pf.appTask();  //先调取函数可先验证此接口，在验证数据

            int appSurplusAppointment = qaDbUtil.selsetDataTempOne("appSurplusAppointment", "pc_appointmentPage");
            int app_all_appointment = qaDbUtil.selsetDataTempOne("app_all_appointment", "pc_appointmentPage");
            Preconditions.checkArgument(appTask[0] - appSurplusAppointment == 1, "预约后app今日任务appSurplusAppointment,预约前：" + appSurplusAppointment + "预约后：" + appTask[0]);
            Preconditions.checkArgument(appTask[1] - app_all_appointment == 1, "预约后app今日任务app_all_appointment,预约前：" + app_all_appointment + "预约后：" + appTask[1]);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约后，app今日任务分子分母+1");
        }
    }

    @Test()  //预约后，小程序预约消息列表数+1
    public void AppletMyAppointment() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int totalA = pf.appletmyAppointment();
            int total = qaDbUtil.selsetDataTempOne("applet_myappointment", "pc_appointmentPage");
            Preconditions.checkArgument(totalA - total == 1, "预约后app今日任务app_all_appointment,预约前：" + total + "预约后：" + totalA);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约后，app今日任务分子分母+1");
        }
    }

    @Test()  //预约后，pc预约记录信息校验
    public void pcAppointmentRecodeCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appointmentRecordManage("", "1", "10", "customer_phone", pp.customerPhone).getJSONArray("list").getJSONObject(0);
            String customer_name = data.getString("customer_name");
            String customer_phone = data.getString("customer_phone");
            String plate_number = data.getString("plate_number");
            String customer_manager = data.getString("customer_manager");
            Preconditions.checkArgument(customer_manager.equals(pp.jdgwName), "接待人名异常");
            Preconditions.checkArgument(customer_name.equals("自动夏"), "预约用户名异常");
            Preconditions.checkArgument(customer_phone.equals(pp.customerPhone), "预约手机号异常");
            Preconditions.checkArgument(plate_number.equals(pp.carplate7), "预约车牌号异常");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约后pc预约记录页信息校验");
        }
    }


    //    @Test()  //测试函数
    public void test1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

//            Integer re=qaDbUtil.selsetDataTempOne("pcAppointmentRecordNum","pc_appointmentPage");
//            System.out.println("re:"+re);

//            qaDbUtil.updateDataNum("pc_appointmentPage",66);
//            System.out.println("test");

//            DataTemp dt1=new DataTemp();
//            dt1.setPcAppointmentRecordNum(83);
//            System.out.println(dt1.getPcAppointmentRecordNum());
//            qaDbUtil.updateDataAll(dt1);
//
            DataTemp result1 = qaDbUtil.selsetDataTemp("pc_appointmentPage");
            System.out.println(result1.getPcAppointmentRecordNum());
//
//            int num=qaDbUtil.selsetDataTempOne("pcAppointmentRecordNum","pc_appointmentPage");
//            System.out.println(num);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
//            saveData("今日任务数=今日数据各列数据之和");
        }
    }


}
