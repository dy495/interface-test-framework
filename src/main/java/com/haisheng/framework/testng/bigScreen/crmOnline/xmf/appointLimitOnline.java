package com.haisheng.framework.testng.bigScreen.crmOnline.xmf;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crmOnline.PublicParmOnline;
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
import static java.lang.Integer.parseInt;

public class appointLimitOnline extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtilOnlineX crm = CrmScenarioUtilOnlineX.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParmOnline pp = new PublicParmOnline();
    PackFunctionOnline pf = new PackFunctionOnline();
    public String adminname = pp.zongjingli;    //pc登录密码，最好销售总监或总经理权限
    public String adminpassword = pp.adminpassword;
    //售后 预约保养 维修 服务总监

    //销售总监  预约试驾 售前
    //预约使用参数
    public String customer_name = pp.customer_name;
    public String customer_phone_number = pp.customer_phone_number;
    public String appointment_date = dt.getHistoryDate(0);  //预约日期取当前天的前一天
    public Integer car_type = 1;
    public String car_type_name = "Panamera";
    public Long activity_id = 34L;

    public String filePath = pp.filePath;

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
        commonConfig.referer= EnumTestProduce.PORSCHE_ONLINE.getReferer();


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.PORSCHE_DAILY.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
//        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = EnumTestProduce.PORSCHE_ONLINE.getShopId();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());

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
    /**
     * @description :预约保养成功，页面一致性验证；applet & pc & app
     * @date :2020/7/10 14:29
     **/
    @Test()
    public void Amaintian_pcConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String appointment_date = dt.getHistoryDate(0);
            //预约保养前：  1.pc端登录，记录原始预约保养总数 A
            crm.login(adminname, adminpassword);
            JSONObject pcdataA = crm.mainAppointmentpage(1, 10);
            String totalA = pcdataA.getString("total");
//            String today_numberA = pcdataA.getString("today_number");
//            String month_numberA = pcdataA.getString("month_number");
//            String total_numberA = pcdataA.getString("total_number");
            //app 今日保养人数和累计试驾总数
            crm.login(pp.fuwuZongjian, pp.adminpassword);
            JSONObject appdataA = crm.mainAppointmentDriverNum();
//            String appointment_total_numberA = appdataA.getString("appointment_total_number");
//            String appointment_today_numberA = appdataA.getString("appointment_today_number");
            //app 今日试驾列表总数
            JSONObject appListA = crm.mainAppointmentList();
            String apptotalListA = appListA.getString("total");

            //2.预约试驾
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());

            String appointment_time = "09:00";
            long timelist = pf.appointmentTimeListO("MAINTAIN", appointment_date).getLong("time_id");
            JSONObject data = crm.appointmentMaintain(pp.mycarId, customer_name, customer_phone_number, timelist);

            //预约试驾后：  pc销售总监总经理权限登录
            crm.login(adminname, adminpassword);
            //3.pc页面显示数据
            JSONObject pcdata = crm.mainAppointmentpage(1, 10);
            String total = pcdata.getString("total");
//            String today_number = pcdata.getString("today_number");
//            String month_number = pcdata.getString("month_number");
//            String total_number = pcdata.getString("total_number");
            JSONObject pcdata1 = pcdata.getJSONArray("list").getJSONObject(0);
            String customer_namepc = pcdata1.getString("customer_name");
            String customer_phone_numberpc = pcdata1.getString("customer_phone_number");
            String order_datepc = pcdata1.getString("order_date");
            String car_type_namepc = pcdata1.getString("car_model_name");  //试驾车型
//            String order_numberpc = pcdata1.getString("order_number"); //预约次数
//            String sale_idpc = pcdata1.getString("sale_id");  //销售id
//            String sale_namepc = pcdata1.getString("sale_name");
//            String service_status_namepc = pcdata1.getString("service_status_name"); //接待状态
//            String risk_status_namepc = pcdata1.getString("risk_status_name"); //风险预警

            //app显示数据  B
            crm.login(pp.fuwuZongjian, pp.adminpassword);
//            JSONObject appdataB = crm.mainAppointmentDriverNum();
//            String appointment_total_numberB = appdataB.getString("appointment_total_number");
//            String appointment_today_numberB = appdataB.getString("appointment_today_number");

            JSONObject appdataList1 = crm.mainAppointmentList();
            String appListtotalB = appdataList1.getString("total");
            JSONObject appdataList = appdataList1.getJSONArray("list").getJSONObject(0);
            String appointment_dateapp = appdataList.getString("appointment_date");
            String customer_nameapp = appdataList.getString("customer_name");
            String car_type_nameapp = appdataList.getString("car_style_name");
            String customer_phone_numberapp = appdataList.getString("customer_phone_number");
//            String service_statusapp = appdataList.getString("service_status");
//            String service_status_nameapp = appdataList.getString("service_status_name");
//            String phone_appointmentapp = appdataList.getString("phone_appointment");
//            String reception_sale_nameapp = appdataList.getString("reception_sale_name");
//            String car_typeapp = appdataList.getString("car_type");


            checkArgument((Long.parseLong(total) - Long.parseLong(totalA)) == 1, "预约试驾成功后，pc预约试驾条数没有+1");
            checkArgument(customer_namepc.equals(customer_name), "pc预约试驾客户名异常");
            checkArgument(customer_phone_numberpc.equals(customer_phone_number), "pc预约试驾客户手机号异常");
            checkArgument(order_datepc.equals(appointment_date), "pc预约试驾预约时间异常");
            checkArgument(car_type_namepc.equals(car_type_name), "pc预约试驾试驾车型显示异常");


            checkArgument((Long.parseLong(appListtotalB) - Long.parseLong(apptotalListA)) == 1, "预约试驾成功后，app预约试驾条数没有+1");
            checkArgument(customer_nameapp.equals(customer_name), "app预约试驾客户名异常");
            checkArgument(customer_phone_numberapp.equals(customer_phone_number), "app预约试驾客户手机号异常");
            checkArgument(appointment_dateapp.equals(appointment_date), "app预约试驾预约时间异常");
            checkArgument(car_type_nameapp.equals(car_type_name), "app预约试驾试驾车型显示异常");

            //pc & app 接待人员  app端显示成了所属顾问，无法比对
//            checkArgument(reception_sale_nameapp.equals(sale_namepc));


            //TODO:试驾车型；今日总计，本月总计，当天去重，隔天不去重；故一天只能运行一次，且首次运行 pc
//            Preconditions.checkArgument((Long.parseLong(today_numberA)-Long.parseLong(today_number))==1,"预约试驾成功后，pc预约试驾今日总计没+1");
//            Preconditions.checkArgument((Long.parseLong(month_numberA)-Long.parseLong(month_number))==1,"预约试驾成功后，pc预约试驾本月共计没+1");
//            Preconditions.checkArgument((Long.parseLong(total_numberA)-Long.parseLong(total_number))==1,"预约试驾成功后，pc预约试驾全部累计没有+1");
//
//            //TODO:试驾车型；今日总计，本月总计，当天去重，隔天不去重；故一天只能运行一次，且首次运行 app
//            Preconditions.checkArgument((Long.parseLong(appointment_today_numberB)-Long.parseLong(appointment_today_numberA))==1,"预约试驾成功后，app预约试驾今日总计没有+1");
//            Preconditions.checkArgument((Long.parseLong(appointment_total_numberB)-Long.parseLong(appointment_total_numberA))==1,"预约试驾成功后，app预约试驾全部累计没+1");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("预约保养成功后，pc和app预约试驾信息校验");
        }
    }

    /**
     * @description :预约维修成功，页面间一致性验证ok；applet & pc & app
     * @date :2020/7/10 14:29
     **/
    @Test()
    public void Arepair_pcConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String appointment_date = dt.getHistoryDate(0);
            //预约维修前：  1.pc端登录，记录原始预约保养总数 A
            crm.login(adminname, adminpassword);
            JSONObject pcdataA = crm.repairAppointmentpage(1, 10);
            String totalA = pcdataA.getString("total");
//            String today_numberA = pcdataA.getString("today_number");
//            String month_numberA = pcdataA.getString("month_number");
//            String total_numberA = pcdataA.getString("total_number");
            //app 今日试驾人数和累计试驾总数
            crm.login(pp.fuwuZongjian, pp.adminpassword);
//            JSONObject appdataA = crm.repairAppointmentDriverNum();
//            String appointment_total_numberA = appdataA.getString("appointment_total_number");
//            String appointment_today_numberA = appdataA.getString("appointment_today_number");
            //app 今日试驾列表总数
            JSONObject appListA = crm.repairAppointmentlist();
            String apptotalListA = appListA.getString("total");
            //2.预约试驾
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());

            String description = "故障说明";
            JSONObject dd = pf.appointmentTimeListO("MAINTAIN", appointment_date);
            long timelist = dd.getLong("time_id");
            String appointment_time = dd.getString("start_time");
            JSONObject data = crm.appointmentRepair(pp.mycarId, customer_name, customer_phone_number, appointment_date, appointment_time, description, timelist);
            //预约试驾后：  pc销售总监权限登录
            crm.login(adminname, adminpassword);
            //3.pc页面显示数据
            JSONObject pcdata = crm.repairAppointmentpage(1, 10);
            String total = pcdata.getString("total");
//            String today_number = pcdata.getString("today_number");
//            String month_number = pcdata.getString("month_number");
//            String total_number = pcdata.getString("total_number");
            JSONObject pcdata1 = pcdata.getJSONArray("list").getJSONObject(0);
            String customer_namepc = pcdata1.getString("customer_name");
            String customer_phone_numberpc = pcdata1.getString("customer_phone_number");
            String order_datepc = pcdata1.getString("order_date");
            String car_type_namepc = pcdata1.getString("car_style_name");  //试驾车型
            String sale_namepc = pcdata1.getString("sale_name");
//            String order_numberpc = pcdata.getJSONArray("list").getJSONObject(0).getString("order_number"); //预约次数
//            String sale_idpc = pcdata.getJSONArray("list").getJSONObject(0).getString("sale_id");  //销售id
//            String service_status_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("service_status_name"); //接待状态
//            String risk_status_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("risk_status_name"); //风险预警

            //app显示数据  B
            crm.login(pp.fuwuZongjian, pp.adminpassword);
//            JSONObject appdataB = crm.repairAppointmentDriverNum();
//            String appointment_total_numberB = appdataB.getString("appointment_total_number");
//            String appointment_today_numberB = appdataB.getString("appointment_today_number");

            JSONObject appdataList1 = crm.repairAppointmentlist();
            JSONObject appdataList = appdataList1.getJSONArray("list").getJSONObject(0);
            String reception_sale_nameapp = appdataList.getString("reception_sale_name");
            String appointment_dateapp = appdataList.getString("appointment_date");
            String customer_nameapp = appdataList.getString("customer_name");
            String car_type_nameapp = appdataList.getString("car_model_name");
            String customer_phone_numberapp = appdataList.getString("customer_phone_number");
            String appListtotalB = appdataList1.getString("total");
//            String car_typeapp = appdataList.getString("car_type");
//            String service_statusapp = appdataList.getString("service_status");
//            String service_status_nameapp = appdataList.getString("service_status_name");
//            String phone_appointmentapp = appdataList.getString("phone_appointment");
//            Long appoint_id=data.getLong("appointment_id");
//            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
//            crm.cancle(appoint_id);

            checkArgument(customer_namepc.equals(customer_name), "pc预约试驾客户名异常");
            checkArgument(customer_phone_numberpc.equals(customer_phone_number), "pc预约试驾客户手机号异常");
            checkArgument(order_datepc.equals(appointment_date), "pc预约试驾预约时间异常");
            checkArgument(car_type_namepc.equals(car_type_name), "pc预约试驾试驾车型显示异常");


            checkArgument(customer_nameapp.equals(customer_name), "app预约试驾客户名异常");
            checkArgument(customer_phone_numberapp.equals(customer_phone_number), "app预约试驾客户手机号异常");
            checkArgument(appointment_dateapp.equals(appointment_date), "app预约试驾预约时间异常");
            checkArgument(car_type_nameapp.equals(car_type_name), "app预约试驾试驾车型显示异常");
            checkArgument((parseInt(total) - parseInt(totalA)) == 1, "预约试驾成功后，pc预约试驾条数没有+1");
            checkArgument((Long.parseLong(appListtotalB) - Long.parseLong(apptotalListA)) == 1, "预约试驾成功后，app预约试驾条数没有+1");


            //pc & app 接待人员
            checkArgument(reception_sale_nameapp.equals(sale_namepc));

//            //TODO:试驾车型；今日总计，本月总计，当天去重，隔天不去重；故一天只能运行一次，且首次运行 pc
//            Preconditions.checkArgument((Long.parseLong(today_numberA)-Long.parseLong(today_number))==1,"预约试驾成功后，pc预约试驾今日总计没+1");
//            Preconditions.checkArgument((Long.parseLong(month_numberA)-Long.parseLong(month_number))==1,"预约试驾成功后，pc预约试驾本月共计没+1");
//            Preconditions.checkArgument((Long.parseLong(total_numberA)-Long.parseLong(total_number))==1,"预约试驾成功后，pc预约试驾全部累计没有+1");
//
//            //TODO:试驾车型；今日总计，本月总计，当天去重，隔天不去重；故一天只能运行一次，且首次运行 app
//            Preconditions.checkArgument((Long.parseLong(appointment_today_numberB)-Long.parseLong(appointment_today_numberA))==1,"预约试驾成功后，app预约试驾今日总计没有+1");
//            Preconditions.checkArgument((Long.parseLong(appointment_total_numberB)-Long.parseLong(appointment_total_numberA))==1,"预约试驾成功后，app预约试驾全部累计没+1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约维修成功后，pc和app预约试驾信息校验");
        }
    }
    /**
     * @description :预约同一天其他时段失败
     * @date :2020/8/12 12:57
     **/
    @Test
    public void BtimeListFail() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            String type = "MAINTAIN";
            long timelist2 = pf.appointmentTimeListO(type, appointment_date).getLong("time_id");
            JSONObject res = crm.appointmentMaintainCode((pp.mycarId), customer_name, customer_phone_number,  timelist2);
            Long code = res.getLong("code");
            checkArgument(code == 1001, "预约同一天其他时段应该失败");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约该时段剩余-1，取消+1");
        }
    }


}
