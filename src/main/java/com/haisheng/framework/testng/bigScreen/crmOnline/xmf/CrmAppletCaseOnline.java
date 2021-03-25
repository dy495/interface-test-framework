package com.haisheng.framework.testng.bigScreen.crmOnline.xmf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.inject.internal.util.$Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crmOnline.PublicParmOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Integer.parseInt;


/**
 * @description :小程序数据一致性---xia
 * @date :2020/7/10 11:10
 **/


public class CrmAppletCaseOnline extends TestCaseCommon implements TestCaseStd {
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
    public Integer car_model = pp.car_model;
    public String car_type_name = "Panamera";
    public Long activity_id = 34L;

    public String filePath = pp.filePath;

    public String positions = "CAR_ACTIVITY";

    //获取接待人员名称电话 --for createArticle
    public String[] manage(Integer role_id) throws Exception {
        JSONArray list;
        list = crm.manageList(role_id).getJSONArray("list");
        String[] name = new String[2];
        for (int i = 0; i < list.size(); i++) {
            int status = list.getJSONObject(i).getInteger("status");
            if (status == 1) {
                name[0] = list.getJSONObject(i).getString("name");
                name[1] = list.getJSONObject(i).getString("phone");
                return name;
            }
        }
        return name;
    }

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
        commonConfig.referer = ChecklistDbInfo.APPLET_ONLINE_REFER;


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.PORSCHE_ONLINE.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
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
     * @description :1.0预约试驾 必填项不填异常验证
     * @date :2020/7/8 18:43
     **/
    @Test(priority = 1)
    public void test_drive() {
        logger.logCaseStart(caseResult.getCaseName());
        String caseDesc = "";
        try {
//            Integer car_type = 1;   //试驾车型
            Object[][] objects = emptyParaCheckList();
            for (Object[] object : objects) {
                String emptyPara = object[0].toString();
                caseDesc = "预约试驾" + emptyPara + "为空！";
                sleep(2);
                crm.addCheckListEmpty(customer_name, customer_phone_number, appointment_date, car_type, emptyPara, object[1].toString(), car_model);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData(caseDesc);
        }
    }

    /**
     * @description :预约试驾成功，页面间一致性验证；预约成功+预约消息
     * @date :2020/7/10 11:10
     **/
    @Test(priority = 1)
    public void driver_dateConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            JSONObject data = crm.appointmentTestDrive("MALE", customer_name, customer_phone_number, appointment_date, car_type, car_model);
            //预约试驾成功后，页面显示数据
            Long appointment_id = data.getLong("appointment_id");
            //预约消息
            JSONObject detail = crm.appointmentInfo(appointment_id);


            String customer_nameB = detail.getString("name");
            String customer_phone_numberB = detail.getString("phone");
            String appointment_dateB = detail.getString("date");
            String car_type_nameB = detail.getString("car_style_name");

            //填写预约信息 & 预约消息
            checkArgument(customer_nameB.equals((customer_name + "先生")), "预约试驾消息页联系人名显示错误");
            checkArgument(customer_phone_number.equals(customer_phone_numberB), "预约试驾消息页联系人电话显示错误");
            checkArgument(appointment_date.equals(appointment_dateB), "预约试驾消息页预约日期显示错误");
            checkArgument(car_type_nameB.equals(car_type_name), "预约试驾消息页试驾车型显示错误");

            crm.cancle(appointment_id);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约试驾成功后，页面间一致性验证，填写预约信息页+预约成功+预约消息");
        }
    }

    /**
     * @description :预约试驾成功，页面间一致性验证；applet & pc & app
     * @date :2020/7/10 14:29
     **/
    @Test(priority = 12)
    public void driver_pcConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //预约试驾前：  1.pc端登录，记录原始预约试驾总数 A
            crm.login(adminname, adminpassword);
            JSONObject pcdataA = crm.appointmentpage(1, 10);
            String totalA = pcdataA.getString("total");
//            String today_numberA = pcdataA.getString("today_number");
//            String month_numberA = pcdataA.getString("month_number");
//            String total_numberA = pcdataA.getString("total_number");
            //app 今日试驾人数和累计试驾总数
            crm.login(pp.xiaoshouZongjian, pp.adminpassword);
//            JSONObject appdataA = crm.appointmentDriverNum();
//            String appointment_total_numberA = appdataA.getString("appointment_total_number");
//            String appointment_today_numberA = appdataA.getString("appointment_today_number");
            //app 今日试驾列表总数
            JSONObject appListA = crm.appointmentlist();
            String apptotalListA = appListA.getString("total");
            //2.预约试驾
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            JSONObject data = crm.appointmentTestDrive("MALE", customer_name, customer_phone_number, appointment_date, car_type, car_model);
            Long appoint_id = data.getLong("appointment_id");

            //预约试驾后：  pc销售总监权限登录
            crm.login(adminname, adminpassword);
            //3.pc页面显示数据
            JSONObject pcdata = crm.appointmentpage(1, 10);
            String total = pcdata.getString("total");
//            String today_number = pcdata.getString("today_number");
//            String month_number = pcdata.getString("month_number");
//            String total_number = pcdata.getString("total_number");

            String customer_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String customer_phone_numberpc = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_phone_number");
            String order_datepc = pcdata.getJSONArray("list").getJSONObject(0).getString("order_date");
            String car_type_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("car_style_name");  //试驾车型
            String sale_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("sale_name");
//            String order_numberpc = pcdata.getJSONArray("list").getJSONObject(0).getString("order_number"); //预约次数
//            String sale_idpc = pcdata.getJSONArray("list").getJSONObject(0).getString("sale_id");  //销售id
//            String service_status_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("service_status_name"); //接待状态
//            String risk_status_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("risk_status_name"); //风险预警

            //app显示数据  B
            crm.login(pp.xiaoshouZongjian, pp.adminpassword);
//            JSONObject appdataB = crm.appointmentDriverNum();
//            String appointment_total_numberB = appdataB.getString("appointment_total_number");
//            String appointment_today_numberB = appdataB.getString("appointment_today_number");

            JSONObject appdataList1 = crm.appointmentlist();
            String appListtotalB = appdataList1.getString("total");
            JSONObject appdataList = appdataList1.getJSONArray("list").getJSONObject(0);
            String reception_sale_nameapp = appdataList.getString("reception_sale_name");
            String appointment_dateapp = appdataList.getString("appointment_date");
            String customer_nameapp = appdataList.getString("customer_name");
            String car_type_nameapp = appdataList.getString("car_style_name");
//            String car_typeapp = appdataList.getString("car_type");
//            String service_statusapp = appdataList.getString("service_status");
//            String service_status_nameapp = appdataList.getString("service_status_name");
//            String phone_appointmentapp = appdataList.getString("phone_appointment");
            String customer_phone_numberapp = appdataList.getString("customer_phone_number");
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            crm.cancle(appoint_id);
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

            //pc & app 接待人员
            checkArgument(reception_sale_nameapp.equals(sale_namepc),"pc接待销售与app所属销售不一致");

            //TODO:试驾车型；今日总计，本月总计，当天去重，隔天不去重；故一天只能运行一次，且首次运行 pc
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
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("预约试驾成功后，pc和app预约试驾信息校验");
        }
    }

    /**
     * @description :添加车辆，车牌8位，数量+1 & 数据校验
     * @date :2020/7/10 18:03
     **/
    @Test(priority = 1)
    public void mycarConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String plate_number = "蒙GBBA261";
            JSONObject carData = crm.myCarList();
            JSONArray list = carData.getJSONArray("list");
            int count;
            if (list == null || list.size() == 0) {
                count = 0;
            } else {
                count = list.size();
            }
            Integer car_idBefore = crm.myCarAdd(car_type, plate_number, car_model).getInteger("my_car_id");
            JSONArray listB = crm.myCarList().getJSONArray("list");
            int aftercount = listB.size();
            String car_type_nameBefore = listB.getJSONObject(0).getString("car_style_name");
            String plate_numberBefore = listB.getJSONObject(0).getString("plate_number");    //车牌号

            checkArgument((aftercount - count) == 1, "增加车辆，我的车辆列表没加1");
            checkArgument(car_type_nameBefore.equals(car_type_name), "增加车辆，我的车辆列表车型显示错误");
            checkArgument(plate_numberBefore.equals(plate_number), "增加车辆，我的车辆列表车牌号显示错误");
            crm.myCarDelete(Integer.toString(car_idBefore));

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加车辆，applet我的车辆列表加1");
        }
    }

    /**
     * @description :添加重复车牌（车牌7位）失败，接口不异常提示，但不重复显示车牌
     * @date :2020/7/10 18:03
     **/
    @Test(priority = 2)
    public void sameCarFail() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int num=crm.myCarList().getJSONArray("list").size();
            String plate_number = "京AWE1234";
            crm.myCarAddCode(car_type, car_model, plate_number).getLong("code");
            int numA=crm.myCarList().getJSONArray("list").size();
            Preconditions.checkArgument(numA-num==0,"添加重复车牌，不重复显示");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加重复车牌验证");
        }
    }

    /**
     * @description :删除车辆，列表数量-1
     * @date :2020/7/10 22:37
     **/
    @Test(priority = 9)
    public void deleteMycar() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String plate_number = "豫GBBA24";
            String my_car_id=crm.myCarAdd(car_type, plate_number, car_model).getString("my_car_id");
            JSONObject carData = crm.myCarList();
            JSONArray list = carData.getJSONArray("list");
            int count=list.size();
            crm.myCarDelete(my_car_id);
            JSONArray listB = crm.myCarList().getJSONArray("list");
            int aftercount;
            if (listB != null) {
                aftercount = listB.size();
            } else {
                aftercount = 0;
            }
            checkArgument((count - aftercount) == 1, "删除车辆，数量没-1");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("删除车辆，列表数量-1");
        }
    }


    /**
     * @description :【我的】添加车辆，10辆边界
     * @date :2020/7/27 19:43
     **/
    @Test(priority = 2)
    public void myCarTen() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
//            crm.myCarDelete("62022");
            JSONObject carData = crm.myCarList();
            JSONArray list = carData.getJSONArray("list");
            int count;
            if (list == null) {
                count = 0;
            } else {
                count = list.size();
            }
            int limit = 10 - count;
            JSONArray carId=new JSONArray();
            for (int i = 0; i < limit; i++) {
                String plate_number;
                plate_number = "豫GBBA3" + i;
                int car_id=crm.myCarAdd(car_type, plate_number, car_model).getInteger("my_car_id");
                carId.add(car_id);
            }
            for (int j = 0; j < carId.size(); j++) {
                Integer car_id = carId.getInteger(j);
                crm.myCarDelete(Integer.toString(car_id));
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序我的车辆，增加十辆");
        }
    }

    /**
     * @description :【我的】添加车辆，11辆异常 ok
     * @date :2020/7/27 19:43
     **/
    @Test(priority = 3)
    public void myCarEven() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            JSONObject carData = crm.myCarList();
            JSONArray list = carData.getJSONArray("list");
            int count;
            if (list == null) {
                count = 0;
            } else {
                count = list.size();
            }
            int limit = 10 - count;
            JSONArray carId=new JSONArray();
            for (int i = 0; i < limit; i++) {
                String plate_number = "吉GBBA3" + i;
                int car_id=crm.myCarAdd(car_type,  plate_number,car_model).getInteger("my_car_id");
                carId.add(car_id);
            }
            String plate_number = "豫GBBA11";
            Long code = crm.myCarAddCode(car_type, car_model, plate_number).getLong("code");
            checkArgument(code == 1001, "我的车辆上限10辆车");
            if (limit == 0) {
                return;
            } else {
                //删除新增的车辆
                for (int j = 0; j < carId.size(); j++) {
                    Integer car_id = carId.getInteger(j);
                    crm.myCarDelete(Integer.toString(car_id));
                }
//                JSONArray listq = crm.myCarList().getJSONArray("list");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序我的车辆，增加十一辆异常验证");
        }
    }

    /**
     * @description :小程序取消预约，pc预约记录，接待状态预约中变更已取消 ok，受预约保养维修次数限制，此case 改为预约试驾 ok
     * @date :2020/7/30 12:41
     **/
    @Test
    public void appointmentStstus() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = crm.appointmentTestDrive("MALE", customer_name, customer_phone_number, appointment_date, car_type, car_model);
            Long appoint_id = data.getLong("appointment_id");
            //预约试驾后：  pc销售总监总经理权限登录
            crm.login(adminname, adminpassword);
            //预约记录，接待状态
            String service_status_name = crm.appointmentpage(1, 10).getJSONArray("list").getJSONObject(0).getString("service_status_name");
            //取消预约
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            crm.cancle(appoint_id);

            crm.login(adminname, adminpassword);
            String service_status_nameD = crm.appointmentpage(1, 10).getJSONArray("list").getJSONObject(0).getString("service_status_name");
            checkArgument(service_status_name.equals("预约中"), "预约记录接待状态错误");
            checkArgument(service_status_nameD.equals("已取消"), "预约记录接待状态错误");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("小程序取消预约，pc预约记录，接待状态预约中变更已取消");
        }
    }

    /**
     * @description :预约活动，小程序页面间数据一致性
     * @date :2020/7/11 14:10
     **/
    @Test(priority = 5)
    public void activity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long[] aid = pf.createAArcile_id(dt.getHistoryDate(0), "9");
            Long activity_id = aid[1];
            Long arcile_id = aid[0];

            logger.info("---------------------活动id:--------------{}", activity_id);
            String other_brand = "奥迪";
            String customer_num = "2";
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());

            JSONObject data = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num, car_model);
            //报名提交后
            String appointment_idA = data.getString("appointment_id");
            JSONObject dataB = crm.appointmentInfo(Long.parseLong(appointment_idA));
            //我的 预约消息
            String customer_phone_numberB = dataB.getString("phone");
            String appointment_dateB = dataB.getString("date");
            String car_type_nameB = dataB.getString("car_style_name");
            String sale_nameB = dataB.getString("receptionist");
            String sale_phoneB = dataB.getString("receptionist_phone");

            String customer_nameB = dataB.getString("name");

            Preconditions.checkArgument(customer_nameB.equals((customer_name + "先生")), "预约消息客户名显示错误");
            Preconditions.checkArgument(car_type_nameB.equals(pp.car_type_name), "预约消息座驾车系显示错误");
            Preconditions.checkArgument(sale_nameB.equals(pp.reception_name), "预约消息接待顾问名显示错误");
            Preconditions.checkArgument(sale_phoneB.equals(pp.reception_phone), "预约消息接待顾问电话显示错误");
            checkArgument(customer_phone_numberB.equals(customer_phone_number), "预约消息客户手机号显示错误");
            checkArgument(appointment_dateB.equals(dt.getHistoryDate(1)), "预约消息预约日期显示错误");

            //取消报名，以便可以运行其他case
            crm.cancle(Long.parseLong(appointment_idA));
            crm.login(adminname, adminpassword);
            crm.articleStatusChange(arcile_id);
            crm.articleDelete(arcile_id);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("预约活动，小程序页面间数据一致性");

        }
    }

    /**
     * @description :取消预约/活动/，我的预约消息状态改变为已取消" ok
     * @date :2020/7/11 23:11
     **/
    //@Test(priority = 3, dataProvider = "APPOINTMENT_TYPE", dataProviderClass = CrmScenarioUtil.class)
    public void cancleappointment(String appointment_type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = crm.appointmentList1();   //接口变更不适用
            JSONArray list = data.getJSONArray("list");
            if (list == null || list.size() == 0) {
                throw new Exception("没有预约消息");
            }
            for (int i = 0; i < list.size(); i++) {
                String appointment_typeA = list.getJSONObject(i).getString("appointment_type");
                if (appointment_typeA.equals(appointment_type)) {
                    String appointment_id = list.getJSONObject(i).getString("appointment_id");
                    crm.cancle(Long.parseLong(appointment_id));  //取消预约
                    String appointment_status_name = crm.appointmentInfo(Long.parseLong(appointment_id)).getString("appointment_status_name");
                    checkArgument(appointment_status_name.equals("已取消"), "预约保养成功页预约状态未变为已取消");
                    break;
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("取消预约/活动/，我的预约消息状态改变为已取消");
        }
    }

    /**
     * @description :活动报名 pc报名客户total+1& 报名管理数据校验
     * @date :2020/7/12 11:48
     **/
    @Test(priority = 5)
    public void activityConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //活动报名前
            Long[] aid = pf.createAArcile_id(dt.getHistoryDate(0), "2");
            Long activity_id = aid[1];
            Long arcile_id = aid[0];
            JSONObject dataA = crm.activityList(1, 10, activity_id);
            String total = dataA.getString("total");
            //活动报名
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            String other_brand = "奥迪";
            String customer_num = "2";
            String time = dt.getHistoryDate(1);
            JSONObject data = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num, car_model);
            String appointment_id = data.getString("appointment_id");
            crm.cancle(Long.parseLong(appointment_id));  //取消活动报名
            //活动报名后
            crm.login(adminname, adminpassword);
            JSONObject pcdata = crm.activityList(1, 10, activity_id);
            String totalB = pcdata.getString("total");

            checkArgument((Long.parseLong(totalB) - Long.parseLong(total) == 1), "小程序活动报名，pc报名客户未+1");

            String customer_nameB = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String customer_phone_numberB = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_phone_number");
//            String customer_numberB = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_number");
//            String sale_nameB = pcdata.getJSONArray("list").getJSONObject(0).getString("sale_name");
//            String service_statusB = pcdata.getJSONArray("list").getJSONObject(0).getString("service_status");
//            String service_status_nameB = pcdata.getJSONArray("list").getJSONObject(0).getString("service_status_name");
//            String audit_statusB = pcdata.getJSONArray("list").getJSONObject(0).getString("audit_status");
//            String show_auditB = pcdata.getJSONArray("list").getJSONObject(0).getString("show_audit");
//            String audit_status_nameB = pcdata.getJSONArray("list").getJSONObject(0).getString("audit_status_name");
//            String risk_statusB = pcdata.getJSONArray("list").getJSONObject(0).getString("risk_status");
//            String risk_status_nameB = pcdata.getJSONArray("list").getJSONObject(0).getString("risk_status_name");
//            String customer_typeB = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_type");
//            String customer_idB = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_id");
//            String car_typeB = pcdata.getJSONArray("list").getJSONObject(0).getString("car_type");
//            String appointment_activity_totalB = pcdata.getJSONArray("list").getJSONObject(0).getString("appointment_activity_total");

            String car_type_nameB = pcdata.getJSONArray("list").getJSONObject(0).getString("car_model_name");
            String appointment_dateB = pcdata.getJSONArray("list").getJSONObject(0).getString("appointment_date");

            checkArgument(customer_nameB.equals(customer_name), "pc报名管理联系人名错误");
            checkArgument(customer_phone_numberB.equals(customer_phone_number), "pc报名管理联系人电话错误");
            checkArgument(appointment_dateB.equals(time), "pc报名管理接待时间错误");   //报名客户没有接待日期只有报名日期
            checkArgument(car_type_nameB.equals(car_type_name), "pc报名管理试驾车型错误");
            crm.articleStatusChange(arcile_id);
            crm.articleDelete(arcile_id);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("applet活动报名,pc报名客户+1");

        }
    }

    /**
     * @description :活动报名，applet已报名人数++，剩余人数--，pc 总数--，已报名人数++
     * @date :2020/7/21 15:29
     **/
    @Test(priority = 5)
    public void pcappointmentSum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建活动
            String simulation_num = "8";
            String vailtime = dt.getHistoryDate(0);
            Long[] aid = pf.createAArcile_id(vailtime, simulation_num);
            Long activity_id = aid[1];
            Long article_id = aid[0];
            //pc文章详情

            //参加活动，报名人数统计
            String other_brand = "奥迪";
            String customer_num = "2";
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            JSONObject data1 = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num, car_model);

            JSONObject data = crm.appartilceDetail(article_id, positions);
            Integer registered_num = data.getInteger("registered_num");  //文章详情
            Integer customer_max = data.getInteger("customer_max");  //剩余人数
            int aa = registered_num;  //已报名人数
            int bb = customer_max;
            Preconditions.checkArgument(aa == 10, "applet已报名人数=假定基数+报名人数");
            checkArgument(bb == 48, "applet剩余名额=总人数-报名人数");
            crm.login(adminname, adminpassword);
            JSONObject dataA = crm.artilceDetailpc(article_id);
            Integer registered_numA = dataA.getInteger("registered_num");  //已报名人数
            Integer customer_maxA = dataA.getInteger("customer_max");  //总数


//            checkArgument(10 == registered_numA, "pc已报名人数错误");     //TODO:pc取消该字段显示？？？？
            checkArgument(50 == customer_maxA, "pc总报名人数错误");

            crm.login(adminname, adminpassword);
            crm.articleStatusChange(article_id);
            crm.articleDelete(article_id);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("活动报名，applet已报名人数++，剩余人数--，pc 总数--，已报名人数++");
        }
    }


    /**
     * @description :取消活动报名 报名人数--，剩余人数++
     * @date :2020/7/21 13:13
     **/
    @Test(priority = 5)
    public void cancleappointmentSum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建活动
            String simulation_num = "8";
            String vailtime = dt.getHistoryDate(0);
            Long[] aid;
            aid = pf.createAArcile_id(vailtime, simulation_num);
            Long activity_id = aid[1];
            Long article_id = aid[0];
            //参加活动，报名人数统计
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            String other_brand = "奥迪";
            String customer_num = "2";
            JSONObject data1 = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num, car_model);
            String appointment_id = data1.getString("appointment_id");
            JSONObject data = crm.appartilceDetail(article_id, positions);
            Integer registered_num = data.getInteger("registered_num");  //文章详情
            Integer customer_max = data.getInteger("customer_max");  //剩余人数
            int aa = registered_num;  //已报名人数
            int bb = customer_max;

            crm.cancle(Long.parseLong(appointment_id));  //取消预约
            JSONObject dataA = crm.appartilceDetail(article_id, positions);
            Integer registered_numA = dataA.getInteger("registered_num");  //文章详情
            Integer customer_maxA = dataA.getInteger("customer_max");  //剩余人数
            int a = registered_numA;
            int b = customer_maxA;

            checkArgument((aa - a) == parseInt(customer_num), "取消预约，已报名人数没减少");
            checkArgument(parseInt(customer_num) == (b - bb), "取消预约，剩余名额没增加");

            crm.login(adminname, adminpassword);
            crm.articleStatusChange(article_id);
            crm.articleDelete(article_id);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("取消预约/活动/，我的预约消息状态改变为已取消");

        }
    }

    /**
     * @description :pc新建活动，pc & app活动页数据一致 applet已报名人数=假定基数+报名人数（0：不预约） ok
     * @date :2020/7/13 20:35  换到applet case
     **/
    @Test(priority = 5)
    public void appletActivityPage() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String simulation_num = "7"; //假定基数
            String valid_start = dt.getHistoryDate(0);
            Long[] aid;  //创建活动方法
            aid = pf.createAArcile_id(valid_start, simulation_num);
            Long article_id = aid[0];
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            JSONObject data = crm.appartilceDetail(article_id, positions);
            Integer registered_numA = data.getInteger("registered_num");  //文章详情
            Integer customer_max = data.getInteger("customer_max");  //剩余人数
            //pc 创建活动 不预约，创建活动假定基数==applet报名人数
            checkArgument((Integer.toString(registered_numA)).equals(simulation_num), "创建后，不预约创建活动假定基数！=applet报名人数");
            checkArgument((Integer.toString(customer_max)).equals("50"), "创建后，不预约活动剩余人数!=活动总名额");
            crm.login(adminname, adminpassword);
            crm.articleStatusChange(article_id);
            crm.articleDelete(article_id);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("pc新建活动，applet报名人数=假定基数+报名人数");

        }
    }

    /**
     * @description :报名活动，小程序报名人数++
     * @date :2020/7/12 14:16
     **/
    @Test(priority = 6)
    public void checkActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String vailtime = dt.getHistoryDate(0);
            Long[] aid = pf.createAArcile_id(vailtime, "9");
            Long activity_id = aid[1];
            Long arcile_id = aid[0];
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            Integer registered_num = crm.appartilceDetail(arcile_id, positions).getInteger("registered_num");
            String other_brand = "奥迪";
            String customer_num = "2";
            JSONObject data = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num, car_model);
            //需求变更，审核不通过也显示再小程序已报名人数中
            // String appointment_id=data.getString("appointment_id");
//            crm.login(adminname,adminpassword);
//            JSONObject pcdata=crm.activityList(1,10,activity_id);
//            String totalB=pcdata.getString("total");
            //客户id
            //String customer_id=pcdata.getJSONArray("list").getJSONObject(0).getString("customer_id");
            //customer_idF=customer_id;
            //crm.chackActivity(1,appointment_id);  //pc 审核通过
            // crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            Integer registered_numA = crm.appartilceDetail(arcile_id, positions).getInteger("registered_num");  //文章详情
            checkArgument((registered_numA - registered_num) == parseInt(customer_num), "报名活动人数写2，报名活动页报名人数未加2");
            crm.login(adminname, adminpassword);
            crm.articleStatusChange(arcile_id);
            crm.articleDelete(arcile_id);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("报名活动，小程序报名人数+1");

        }
    }

    /**
     * @description :pc把未审核的报名活动加入黑名单，小程序总报名人数--，报名活动列表总数不变
     * @date :2020/7/13 20:44
     **/
    @Test(priority = 5)
    public void blackAddConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String vailtime = dt.getHistoryDate(0);
            Long[] aid;
            aid = pf.createAArcile_id(vailtime, "9");
            Long activity_id = aid[1];
            Long arcile_id = aid[0];
            String other_brand = "奥迪";
            String customer_num = "2";
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());

            JSONObject data = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num, car_model);
            String appointment_id = data.getString("appointment_id");
            Integer registered_num = crm.appartilceDetail(arcile_id, positions).getInteger("registered_num");  //小程序活动报名人数

            crm.login(adminname, adminpassword);
            JSONObject pcdata = crm.activityList(1, 10, activity_id);
            String totalB = pcdata.getString("total");  //pc报名总数
            //客户id
            String customer_id = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_id");
            //crm.chackActivity(1,appointment_id);  //pc 审核通过

            crm.blackadd(customer_id);                //加入黑名单
            //移除黑名单
            crm.blackRemove(customer_id);
            Integer registered_numA = crm.appartilceDetail(arcile_id, positions).getInteger("registered_num");
            checkArgument((registered_num - registered_numA) == 2, "pc把审核通过的报名活动加入黑名单，小程序总报名人数没--");

            JSONObject pcdataA = crm.activityList(1, 10, activity_id);
            String totalA = pcdataA.getString("total");
            checkArgument(totalA.equals(totalB), "pc把审核通过的报名活动加入黑名单,报名活动列表总数不变");
            crm.articleStatusChange(arcile_id);
            crm.articleDelete(arcile_id);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("pc把审核通过的报名活动加入黑名单，小程序总报名人数--，报名活动列表总数不变");


        }
    }

    /**
     * @description :黑名单客户报名失败
     * @date :2020/8/31 20:12
     **/
    @Test
    public void blackJoin() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long[] aid = pf.createAArcile_id(dt.getHistoryDate(0), "9");
            Long activity_id = aid[1];
            Long arcile_id = aid[0];
            String other_brand = "奥迪";
            String customer_num = "2";
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());

            crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num, car_model);
            crm.login(adminname, adminpassword);
            JSONObject pcdata = crm.activityList(1, 10, activity_id);

            //客户id
            String customer_id = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_id");
            //crm.chackActivity(1,appointment_id);  //pc 审核通过
            crm.blackadd(customer_id);                //加入黑名单
            //报名其他活动失败
            Long[] aid2 = pf.createAArcile_id(dt.getHistoryDate(0), "9");
            Long activity_id2 = aid2[1];
            Long arcile_id2 = aid2[0];
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            int code = crm.joinActivityCode(Long.toString(activity_id2), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num, car_model).getInteger("code");
            crm.login(pp.zongjingli, pp.adminpassword);
            //移除黑名单
            crm.blackRemove(customer_id);
            crm.articleStatusChange(arcile_id);
            crm.articleDelete(arcile_id);

            crm.articleStatusChange(arcile_id2);
            crm.articleDelete(arcile_id2);
            Preconditions.checkArgument(code == 1001, "黑名单用户不能报名活动");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("黑名单用户不能报名活动");

        }
    }

    /**
     * @description :applet看车和pc商品管理车辆列表数量一致
     * @date :2020/7/15 20:10
     **/
    @Test(priority = 5)
    public void kancheAppletPC() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //applet
            JSONArray list = crm.appletwatchCarList().getJSONArray("list");
            //pc
            crm.login(adminname, adminpassword);
            JSONArray listpc = crm.carList().getJSONArray("list");


            checkArgument(list.size() == listpc.size(), "applet看车和pc商品管理车辆列表数量一致");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("applet看车和pc商品管理车辆列表数量一致");
        }
    }

    /**
     * @description :预约试驾预约次数+1
     * @date :2020/8/21 17:01
     **/
    @Test
    public void orderNum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //小程序预约
            sleep(3);
            Long id = crm.appointmentTestDrive("MALE", pp.customer_name, pp.customer_phone_number, dt.getHistoryDate(0), car_type, car_model).getLong("appointment_id");
            //pc查看预约次数
            crm.login(pp.zongjingli, pp.adminpassword);
            JSONArray ord = crm.appointmentpage(1, 10).getJSONArray("list");
            int num = ord.getJSONObject(0).getInteger("order_number");

            //预约
            sleep(3);
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            Long id2 = crm.appointmentTestDrive("MALE", pp.customer_name, pp.customer_phone_number, dt.getHistoryDate(0), car_type, car_model).getLong("appointment_id");

            crm.login(pp.zongjingli, pp.adminpassword);
            int num2 = crm.appointmentpage(1, 10).getJSONArray("list").getJSONObject(0).getInteger("order_number");

            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            crm.cancle(id);
            crm.cancle(id2);
            checkArgument((num2 - num) == 1, "预约试驾pc预约次数没+1");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("预约试驾pc预约次数+1");
        }
    }

    /**
     * @description :预约试驾，我的试驾消息+1
     * @date :2020/8/25 10:38
     **/
    @Test
    public void driverMessage() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type = "TEST_DRIVE";    //ACTIVITY
            //消息数量
            Long total = crm.appointmentList(0L, type, 20).getLong("total");
            JSONObject data = crm.appointmentTestDrive("MALE", customer_name, customer_phone_number, appointment_date, car_type, car_model);
            //预约试驾成功后，页面显示数据
            Long appointment_id = data.getLong("appointment_id");
            Long total2 = crm.appointmentList(0L, type, 20).getLong("total");
            crm.cancle(appointment_id);
            checkArgument((total2 - total) == 1, "预约试驾，试驾消息没+1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约试驾，我的试驾消息+1");
        }
    }

    /**
     * @description :预约活动，我的活动消息+1
     * @date :2020/8/25 10:38
     **/
    @Test
    public void activityMessage() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type = "ACTIVITY";    //ACTIVITY
            //消息数量
            Long total = crm.appointmentList(0L, type, 20).getLong("total");
            //创建活动
            Long[] aid = pf.createAArcile_id(dt.getHistoryDate(0), "8");
            Long activity_id = aid[1];
            Long article_id = aid[0];
            //pc文章详情

            //参加活动，报名人数统计
            String other_brand = "奥迪";
            String customer_num = "2";
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            JSONObject data1 = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num, car_model);
            Long total2 = crm.appointmentList(0L, type, 20).getLong("total");

            crm.login(adminname, adminpassword);
            crm.articleStatusChange(article_id);
            crm.articleDelete(article_id);
            checkArgument((total2 - total) == 1, "预约活动，我的活动消息没+1");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("预约活动，我的活动消息+1");
        }
    }

    /**
     * @description :车牌号数量
     * @date :2020/8/24 19:54
     **/
    @Test
    public void provinceList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = crm.provinceList();
            JSONArray list = data.getJSONArray("list");
//            String Util=list.getJSONObject(0).getString("province_name");
            checkArgument(list.size() == 31, "车牌号省份不是31");
//            Preconditions.checkArgument(Util.equals("苏"),"省份默认不是苏");
        } catch (AssertionError | Exception e) {

            appendFailReason(e.toString());
        } finally {
            saveData("车牌号验证");
        }
    }

    /**
     * @description :编辑车辆，异常车牌验证
     * @date :2020/10/10 16:00
     **/
    @Test(dataProvider = "PLATE", dataProviderClass = CrmScenarioUtil.class)
    public void editplateab(String plate) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long code = crm.appletEditCar(pp.mycarId2, car_type, plate, car_model).getLong("code");
            $Preconditions.checkArgument(code == 1001, "编辑输入错误车牌，仍成功");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("车辆，车牌号异常验证");
        }
    }
    /**
     * @description :编辑车辆
     * @date :2020/10/10 16:00
     **/
    @Test(priority = 1)
    public void editplate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String plate="沪W336699";
            Long code = crm.appletEditCar(pp.mycarId2, car_type, plate, car_model).getLong("code");
            $Preconditions.checkArgument(code == 1000, "编辑车辆接口报错");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("编辑车辆");
        }
    }


    /**
     * @description :新增车辆，异常车牌验证
     * @date :2020/10/10 16:00
     **/
    @Test(dataProvider = "PLATE", dataProviderClass = CrmScenarioUtil.class)
    public void plateabnormal(String plate) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = crm.myCarAddCode(car_type, car_model, plate);
            Long code = data.getLong("code");
            $Preconditions.checkArgument(code == 1001, "编辑输入错误车牌，仍成功");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("applet-小程序新建车辆，车牌号异常验证");
        }
    }
    //预约试驾/维修/保养  长度/非数字---前端校验
//    @Test()
    public void yuyueAb() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long code1 = crm.appointmentTestDrivecode("MALE", pp.abString, customer_phone_number, appointment_date, car_type, car_model).getLong("code");
            Preconditions.checkArgument(code1 == 1001, "预约试驾，名称长度51仍成功");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约试驾，名称长度异常校验");
        }
    }
//    @Test()
    public void yuyueAb2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            long timelist = pf.appointmentTimeListO("MAINTAIN",dt.getHistoryDate(2)).getLong("time_id");
            int code1 = crm.appointmentMaintainCode(pp.mycarId, pp.abString, customer_phone_number, timelist).getInteger("code");
            Preconditions.checkArgument(code1 == 1001, "预约试驾，名称长度51仍成功");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约维修，名称长度异常校验");
        }
    }
    //预约试驾后：  pc销售总监总经理权限登录

    @Test(dataProvider = "PHONE", dataProviderClass = CrmScenarioUtil.class)
    public void yuyuephoneAb(String phone) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long code2= crm.appointmentTestDrivecode("MALE", pp.customer_name, phone, appointment_date, car_type, car_model).getLong("code");
            sleep(5);
            Preconditions.checkArgument(code2 == 1001, "预约试驾，电话异常仍成功");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约试驾，电话验证异常");
        }
    }


    //@Test
    public void current() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.shop();

        } catch (AssertionError | Exception e) {

            appendFailReason(e.toString());
        } finally {
            saveData("车牌号验证");
        }
    }

    /**
     * @description :报名取消三次，报名失败  TODO:功能未实现
     * @date :2020/8/12 13:08
     **/
    //@Test
    public void activityCancleThere() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建活动
            String simulation_num = "8";
            String vailtime = dt.getHistoryDate(0);
            Long[] aid = pf.createAArcile_id(vailtime, simulation_num);
            Long activity_id = aid[1];
            Long article_id = aid[0];
            //参加活动，报名人数统计
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            String other_brand = "奥迪";
            String customer_num = "2";
            for (int i = 0; i < 3; i++) {
                JSONObject data1 = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num, car_model);
                String appointment_id = data1.getString("appointment_id");
                crm.cancle(Long.parseLong(appointment_id));  //取消预约
            }
            Long code1 = crm.joinActivityCode(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num, car_model).getLong("code");
            checkArgument(code1 == 1001, "报名取消三次后再报名应该失败");

            crm.login(adminname, adminpassword);
            crm.articleStatusChange(article_id);
            crm.articleDelete(article_id);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletToken.BSJ_XMF_ONLINE.getToken());
            saveData("报名取消三次后再报名失败");
        }
    }

    @DataProvider(name = "EMPTY_PARA_CHECK_LIST")
    public Object[][] emptyParaCheckList() {
        return new Object[][]{
                new Object[]{
                        "customer_name", "顾客名不能为空"
                },
                new Object[]{
                        "customer_phone_number", "顾客手机号不能为空"
                },
        };
    }

    public void checkMessage(String function, String response, String message) throws Exception {

        String messageRes = JSON.parseObject(response).getString("message");
        if (!message.equals(messageRes)) {
            throw new Exception(function + "，提示信息与期待不符，期待=" + message + "，实际=" + messageRes);
        }
    }


}
