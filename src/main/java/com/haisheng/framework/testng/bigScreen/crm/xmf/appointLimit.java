package com.haisheng.framework.testng.bigScreen.crm.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PackFunction;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicParm;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
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

public class appointLimit extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp = new PublicParm();
    PackFunction pf = new PackFunction();
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
        commonConfig.referer= commonConfig.referer = EnumTestProduce.CRM_DAILY.getReferer();


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
//        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = EnumTestProduce.CRM_DAILY.getShopId();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
//        crm.login(adminname, adminpassword);
//        crm.appletLoginToken(EnumAppletToken.BSJ_XMF_DAILY.getToken());
        crm.appletLoginToken(EnumAppletToken.BSJ_XMF_DAILY.getToken());

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
     * @description :预约保养 小程序页面间数据一致性
     * @date :2020/7/10 22:52
     **/

    //预约保养
    public String[] maintainP(String date, Long carid, String ifreception) throws Exception {
        String[] a = new String[3]; //0销售登陆账号 1预约记录id 2 接待记录id
        //小程序登陆
        crm.appletLoginToken(EnumAppletToken.BSJ_XMF_DAILY.getToken());

        String appointment_time = "09:00";
        long timelist = pf.appointmentTimeList("MAINTAIN", 1, date);
        JSONObject obj = crm.appointmentMaintain(carid, customer_name, customer_phone_number, timelist);
        Long maintain_id = obj.getLong("appointment_id");
        a[1] = Long.toString(maintain_id);

//        String salephone = obj.getString("sale_phone");
//        //前台登陆
//
//        String userLoginName = "";
//        JSONArray userlist = crm.userPage(1,100).getJSONArray("list");
//        for (int i = 0 ; i <userlist.size();i++){
//            JSONObject obj1 = userlist.getJSONObject(i);
//            if (obj1.getString("user_phone").equals(salephone)){
//                userLoginName = obj1.getString("user_login_name");
//            }
//        }
//        a[0] = userLoginName;
        //保养顾问登陆，点击接待按钮
        crm.login(pp.baoyangGuwen, adminpassword);
        if (ifreception.equals("yes")) {
            Long after_record_id = crm.reception_customer(maintain_id).getLong("after_record_id");
            a[2] = Long.toString(after_record_id);
        } else {
            a[2] = "未点击接待按钮";
        }
        return a;

    }

//    @Test()
    public void AmainTain() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String appointment_date = dt.getHistoryDate(2);
            JSONObject dd = pf.appointmentTimeListO("MAINTAIN", appointment_date);
            long timelist = dd.getLong("time_id");
            String appointment_time = dd.getString("start_time");

            JSONObject data = crm.appointmentMaintain(pp.mycarId, customer_name, customer_phone_number, timelist);
            //预约成功
            Long appointment_idM = data.getLong("appointment_id");
//            Long appointment_idM = 16421L;

            //我的 预约消息
            JSONObject dataB = crm.appointmentInfo(appointment_idM);

            String customer_nameB = dataB.getString("name");
            String customer_phone_numberB = dataB.getString("phone");
            String appointment_dateB = dataB.getString("date");
            String car_type_nameB = dataB.getString("car_style_name");
            String sale_nameB = dataB.getString("receptionist");
            String sale_phoneB = dataB.getString("receptionist_phone");
            //预约消息&填写预约信息页
            checkArgument(customer_nameB.equals((customer_name + "先生")), "预约消息客户名显示错误");
            checkArgument(customer_phone_numberB.equals(customer_phone_number), "预约消息客户手机号显示错误");
            checkArgument(appointment_dateB.equals(appointment_date), "预约消息预约日期显示错误");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约保养，保养成功，预约消息数据校验");
        }
    }

    /**
     * @description :预约保养成功，页面一致性验证；applet & pc & app
     * @date :2020/7/10 14:29
     **/
    @Test()
    public void Amaintian_pcConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String appointment_date = dt.getHistoryDate(1);
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
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_DAILY.getToken());

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
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_DAILY.getToken());
            saveData("预约保养成功后，pc和app预约试驾信息校验");
        }
    }

    /**
     * @description :预约维修，小程序页面间数据一致性 ok
     * @date :2020/7/11 13:48
     **/
//    @Test()
    public void repair() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String appointment_date = dt.getHistoryDate(0);
            String description = "故障说明";
            JSONObject dd = pf.appointmentTimeListO("REPAIR", appointment_date);
            long timelist = dd.getLong("time_id");
            String appointment_time = dd.getString("start_time");
            JSONObject data = crm.appointmentRepair(pp.mycarId, customer_name, customer_phone_number, appointment_date, appointment_time, description, timelist);
            //预约成功
            Long appointment_idM = data.getLong("appointment_id");
            //我的 预约消息
            JSONObject dataB = crm.appointmentInfo(appointment_idM);

            String customer_nameB = dataB.getString("name");
            String customer_phone_numberB = dataB.getString("phone");
            String appointment_dateB = dataB.getString("date");
            String descriptionB = data.getString("description");
//            String car_type_nameB = dataB.getString("car_style_name");
//            String sale_nameB = dataB.getString("receptionist");
//            String sale_phoneB = dataB.getString("receptionist_phone");
            //预约消息&填写预约信息页
            checkArgument(customer_nameB.equals(customer_name + "先生"), "预约消息客户名显示错误");
            checkArgument(customer_phone_numberB.equals(customer_phone_number), "预约消息客户手机号显示错误");
            checkArgument(appointment_dateB.equals(appointment_date), "预约消息预约日期显示错误");
            checkArgument(descriptionB.equals(description), "预约消息故障说明显示错误");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约维修，小程序页面间数据一致性，填写预约信息&预约成功&我的预约消息");
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
            String appointment_date = dt.getHistoryDate(1);
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
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_DAILY.getToken());

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
//            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_DAILY.getToken());
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
     * @description :保养评价  ok,预约早上9点，完成接待，3.0时，此case,只运行一次; && 完成接待接待次数+1 ok  TODO:
     * @date :2020/8/2 10:29
     **/
//    @Test()
    public void appointEvaluate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //接待前pp.baoyangGuwen，接待次数
            crm.login(adminname, adminpassword);
            JSONArray list = crm.ManageList(16).getJSONArray("list");
            if (list == null || list.size() == 0) {
                return;
            }
            int num = 0;
            for (int i = 0; i < list.size(); i++) {
                String name = list.getJSONObject(i).getString("name");
                if (name.equals("pp.baoyangGuwen")) {
                    num = list.getJSONObject(i).getInteger("num");
                } else {
                    continue;
                }
            }
            //pc评价页总数
            JSONArray evaluateList = crm.evaluateList(1, 100, "", "", "").getJSONArray("list");
            int total = evaluateList.size();
            //预约接待完成
            String[] aa = maintainP(dt.getHistoryDate(0), pp.mycarId, "yes");
            String appoint_id = aa[1];

            int num2 = 0;
            JSONArray list2 = crm.ManageList(16).getJSONArray("list");
            for (int j = 0; j < list2.size(); j++) {
                String name2 = list2.getJSONObject(j).getString("name");
                if (name2.equals("pp.baoyangGuwen")) {
                    num2 = list2.getJSONObject(j).getInteger("num");
                } else {
                    continue;
                }
            }
            checkArgument((num2 - num) == 1, "接待完成，接待次数没+1");
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_DAILY.getToken());
//            SERVICE_QUALITY|PROCESS|PROFESSIONAL|EXPERIENCE
            int score = 4;
            JSONObject ll = new JSONObject();
            ll.put("score", score);
            ll.put("type_comment", "顾问接待服务质量");
            ll.put("type", "SERVICE_QUALITY");

            JSONObject ll2 = new JSONObject();
            ll2.put("score", score);
            ll2.put("type_comment", "保养待服务流程");
            ll2.put("type", "PROCESS");

            JSONObject ll3 = new JSONObject();
            ll3.put("score", score);
            ll3.put("type_comment", "保养车辆质量评价");
            ll3.put("type", "PROFESSIONAL");

            JSONArray array1 = new JSONArray();
            array1.add(0, ll);
            array1.add(1, ll2);
            array1.add(2, ll3);

            crm.appointmentEvaluate(Long.parseLong(appoint_id), "保养满意", array1);  //评价

            JSONArray evaluateListB = crm.evaluateList(1, 10, "", "", "").getJSONArray("list");
            int totalB = evaluateList.size();
            checkArgument((totalB - total) == 1, "评价后，pc评价列表没+1");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("保养评价");
        }
    }

    /**
     * @description : 预约该时段剩余-1，取消+1
     * @date :2020/8/12 12:06
     **/
//    @Test
    public void timeListR() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String appointment_date = dt.getHistoryDate(3);
            crm.appletLoginToken(EnumAppletToken.BSJ_WM_DAILY.getToken());
            String type = "MAINTAIN";
            String appointment_time = "11:00";
            int i = 0;     //预约时段下标
            JSONArray list = crm.timeList(type, appointment_date).getJSONArray("list");
            Integer leftNum = list.getJSONObject(i).getInteger("left_num");
            //预约
            long timelist = pf.appointmentTimeList(type, i, appointment_date);
            JSONObject data = crm.appointmentMaintain(pp.mycarId, customer_name, customer_phone_number,  timelist);
            Long appoint_id = data.getLong("appointment_id");
            JSONArray list2 = crm.timeList(type, appointment_date).getJSONArray("list");
            Integer leftNum2 = list2.getJSONObject(i).getInteger("left_num");
            crm.cancle(appoint_id);    //取消预约
            JSONArray list3 = crm.timeList(type, appointment_date).getJSONArray("list");
            Integer leftNum3 = list3.getJSONObject(i).getInteger("left_num");
            checkArgument((leftNum - leftNum2) == 1, "预约后时段剩余名额没有-1");
            checkArgument((leftNum3 - leftNum2) == 1, "预约后时段剩余名额没有+1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约该时段剩余-1，取消+1");
        }
    }

    /**
     * @description :预约同一天其他时段失败    1次
     * @date :2020/8/12 12:57
     **/
    @Test
    public void BtimeListFail() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_DAILY.getToken());
//            String type = "REPAIR";
            String type = "MAINTAIN";
//            JSONArray list = crm.timeList(type, appointment_date).getJSONArray("list");
//            Integer leftNum = list.getJSONObject(i).getInteger("left_num");

            long timelist2 = pf.appointmentTimeListO(type, appointment_date).getLong("time_id");
            JSONObject res = crm.appointmentMaintainCode((pp.mycarId), customer_name, customer_phone_number,  timelist2,null);
            Long code = res.getLong("code");
            checkArgument(code == 1001, "预约同一天其他时段应该失败");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约该时段剩余-1，取消+1");
        }
    }


}
