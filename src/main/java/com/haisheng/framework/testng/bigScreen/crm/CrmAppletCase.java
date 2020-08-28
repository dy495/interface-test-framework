package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.experiment.enumerator.EnumAppletCode;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PackFunction;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicParm;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;
import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Integer.*;


/**
 * @description :小程序数据一致性---xia
 * @date :2020/7/10 11:10
 **/


public class CrmAppletCase extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp=new PublicParm();
    PackFunction pf=new PackFunction();
    public String adminname = pp.zongjingli;    //pc登录密码，最好销售总监或总经理权限
    public String adminpassword = pp.adminpassword;
    //售后 预约保养 维修 服务总监


    //销售总监  预约试驾 售前
    //预约使用参数
    public String customer_name = pp.customer_name;
    public String customer_phone_number = pp.customer_phone_number;
    public String appointment_date = dt.getHistoryDate(1);  //预约日期取当前天的前一天
    public Integer car_type = 1;
    public String car_type_name = "Panamera";
    public Long activity_id = 34L;

    public String filePath = pp.filePath;

    public String positions = "CAR_ACTIVITY";

    //预约剩余时段查询
    public Long timeList(String type,int i) throws Exception {
        return crm.timeList(type, appointment_date).getJSONArray("list").getJSONObject(i).getLong("id");
    }

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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "xmf";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "CRM 日常");

        //replace ding push conf
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
//        commonConfig.dingHook = DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
//        crm.login(adminname, adminpassword);
//        crm.appletLoginToken(EnumAppletCode.XMF.getCode());
        crm.appletLoginToken(EnumAppletCode.XMF.getCode());

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
            Integer car_type = 1;   //试驾车型
            Object[][] objects = emptyParaCheckList();
            for (Object[] object : objects) {
                String emptyPara = object[0].toString();
                caseDesc = "预约试驾" + emptyPara + "为空！";
                crm.addCheckListEmpty(customer_name, customer_phone_number, appointment_date, car_type, emptyPara, object[1].toString());
            }
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
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
            JSONObject data = crm.appointmentTestDrive("MALE", customer_name, customer_phone_number, appointment_date, car_type);
            //预约试驾成功后，页面显示数据
            Long appointment_id = data.getLong("appointment_id");
//            String appointment_statusA = data.getString("appointment_status");
//            String appointment_status_nameA = data.getString("appointment_status_name");
            String customer_nameA = data.getString("customer_name");
            String customer_phone_numberA = data.getString("customer_phone_number");
            String appointment_dateA = data.getString("appointment_date");
            String car_type_nameA = data.getString("car_type_name"); // 试驾车型名
            String sale_nameA = data.getString("sale_name");         //接待人名
            String sale_phoneA = data.getString("sale_phone");
            //填写预约信息 & 预约成功
            checkArgument(customer_name.equals(customer_nameA), "预约试驾成功页联系人名显示错误");
            checkArgument(customer_phone_number.equals(customer_phone_numberA), "预约试驾成功页联系人电话显示错误");
            checkArgument(appointment_date.equals(appointment_dateA), "预约试驾成功页预约日期显示错误");
            checkArgument(car_type_nameA.equals(car_type_name), "预约试驾成功页试驾车型显示错误");
            //试驾车型--固定赋值

            //预约消息
            JSONObject detail = crm.appointmentInfo(appointment_id);

//            String appointment_idB = detail.getString("id");
//            String appointment_statusB = detail.getString("status_commnet");
//            String appointment_status_nameB = detail.getString("appointment_status_name");
            String customer_nameB = detail.getString("name");
            String customer_phone_numberB = detail.getString("phone");
            String appointment_dateB = detail.getString("date");
            String car_type_nameB = detail.getString("car_type_name");
            String sale_nameB = detail.getString("receptionist");
            String sale_phoneB = detail.getString("receptionist_phone");
//            String star_ratingB = detail.getString("star_rating");
//            String service_descriptionB = detail.getString("service_description");
//            String is_need_evaluateB = detail.getString("is_need_evaluate");
            //填写预约信息 & 预约消息
            checkArgument(customer_name.equals(customer_nameB), "预约试驾消息页联系人名显示错误");
            checkArgument(customer_phone_number.equals(customer_phone_numberB), "预约试驾消息页联系人电话显示错误");
            checkArgument(appointment_date.equals(appointment_dateB), "预约试驾消息页预约日期显示错误");
            checkArgument(car_type_nameB.equals(car_type_name), "预约试驾成功页试驾车型显示错误");
            //预约成功 & 预约消息
            checkArgument(sale_nameA.equals(sale_nameB), "预约试驾消息页接待人名显示错误");
            checkArgument(sale_phoneA.equals(sale_phoneB), "预约试驾消息页接待人电话显示错误");
            checkArgument(car_type_nameB.equals(car_type_nameA), "预约试驾成功页试驾车型显示错误");
            crm.cancle(appointment_id);

            //接待人分配是否轮询

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("预约试驾成功后，页面间一致性验证，填写预约信息页+预约成功+预约消息");
        }
    }

    /**
     * @description :预约试驾成功，页面间一致性验证；applet & pc & app
     * @date :2020/7/10 14:29
     **/
    @Test(priority = 1)
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
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONObject data = crm.appointmentTestDrive("MALE", customer_name, customer_phone_number, appointment_date, car_type);
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
            String car_type_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("car_type_name");  //试驾车型
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
            String car_type_nameapp = appdataList.getString("car_type_name");
//            String car_typeapp = appdataList.getString("car_type");
//            String service_statusapp = appdataList.getString("service_status");
//            String service_status_nameapp = appdataList.getString("service_status_name");
//            String phone_appointmentapp = appdataList.getString("phone_appointment");
            String customer_phone_numberapp = appdataList.getString("customer_phone_number");
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
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
            checkArgument(reception_sale_nameapp.equals(sale_namepc));

            //TODO:试驾车型；今日总计，本月总计，当天去重，隔天不去重；故一天只能运行一次，且首次运行 pc
//            Preconditions.checkArgument((Long.parseLong(today_numberA)-Long.parseLong(today_number))==1,"预约试驾成功后，pc预约试驾今日总计没+1");
//            Preconditions.checkArgument((Long.parseLong(month_numberA)-Long.parseLong(month_number))==1,"预约试驾成功后，pc预约试驾本月共计没+1");
//            Preconditions.checkArgument((Long.parseLong(total_numberA)-Long.parseLong(total_number))==1,"预约试驾成功后，pc预约试驾全部累计没有+1");
//
//            //TODO:试驾车型；今日总计，本月总计，当天去重，隔天不去重；故一天只能运行一次，且首次运行 app
//            Preconditions.checkArgument((Long.parseLong(appointment_today_numberB)-Long.parseLong(appointment_today_numberA))==1,"预约试驾成功后，app预约试驾今日总计没有+1");
//            Preconditions.checkArgument((Long.parseLong(appointment_total_numberB)-Long.parseLong(appointment_total_numberA))==1,"预约试驾成功后，app预约试驾全部累计没+1");

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("预约试驾成功后，pc和app预约试驾信息校验");
        }
    }

    /**
     * @description :添加车辆，数量+1 & 数据校验
     * @date :2020/7/10 18:03
     **/
    @Test(priority = 1)
    public void mycarConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String plate_number = "辽GBBA26";
            JSONObject carData = crm.myCarList();
            JSONArray list = carData.getJSONArray("list");
            int count;
            if (list == null || list.size() == 0) {
                count = 0;
            } else {
                count = list.size();
            }
            crm.myCarAdd(car_type, plate_number);
            JSONArray listB = crm.myCarList().getJSONArray("list");
            int aftercount = listB.size();
            String car_type_nameBefore = listB.getJSONObject(0).getString("car_type_name");
            String plate_numberBefore = listB.getJSONObject(0).getString("plate_number");    //车牌号
            Integer car_idBefore = listB.getJSONObject(0).getInteger("my_car_id");    //车牌号

            checkArgument((aftercount - count) == 1, "增加车辆，我的车辆列表没加1");
            checkArgument(car_type_nameBefore.equals(car_type_name), "增加车辆，我的车辆列表车型显示错误");
            checkArgument(plate_numberBefore.equals(plate_number), "增加车辆，我的车辆列表车牌号显示错误");
            crm.myCarDelete(Integer.toString(car_idBefore));

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加车辆，applet我的车辆列表加1");
        }
    }
    /**
     * @description :添加重复车牌失败
     * @date :2020/7/10 18:03
     **/
    @Test(priority = 1)
    public void sameCarFail() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String plate_number = "豫GBBA96";

            crm.myCarAdd(car_type, plate_number);
            Long code = crm.myCarAddCode(car_type, plate_number);
            JSONArray listB = crm.myCarList().getJSONArray("list");
            Integer car_idBefore = listB.getJSONObject(0).getInteger("my_car_id");    //车牌号
            crm.myCarDelete(Integer.toString(car_idBefore));
            checkArgument(code==1001,"添加相同车牌应该失败");

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
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
            String plate_number = "豫GBBA25";
            crm.myCarAdd(car_type, plate_number);
            JSONObject carData = crm.myCarList();
            JSONArray list = carData.getJSONArray("list");
            int count;
            if (list == null || list.size() == 0) {
                throw new Exception("暂无车辆可删除");
            } else {
                count = list.size();
            }
            String my_car_id = list.getJSONObject(0).getString("my_car_id");
            crm.myCarDelete(my_car_id);
            JSONArray listB = crm.myCarList().getJSONArray("list");
            int aftercount;
            if (listB != null) {
                aftercount = listB.size();
            } else {aftercount = 0;}
            checkArgument((count - aftercount) == 1, "删除车辆，数量没-1");


        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("删除车辆，列表数量-1");
        }
    }


    /**
     * @description :【我的】添加车辆，10辆边界
     * @date :2020/7/27 19:43
     **/
    @Test(priority = 3)
    public void myCarTen() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject carData = crm.myCarList();
            JSONArray list = carData.getJSONArray("list");
            int count;
            if (list == null) {
                count = 0;
            } else {
                count = list.size();
            }
            int limit = 10 - count;
            for (int i = 0; i < limit; i++) {
                String plate_number;
                plate_number = "豫GBBA3" + i;
                crm.myCarAdd(car_type, plate_number);
            }
            //删除新增的车辆
            JSONArray listB = crm.myCarList().getJSONArray("list");
            for (int j = 0; j < limit; j++) {
                Integer car_id = listB.getJSONObject(j).getInteger("my_car_id");
                crm.myCarDelete(Integer.toString(car_id));
            }
//            JSONArray listq = crm.myCarList().getJSONArray("list");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
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
            JSONObject carData = crm.myCarList();
            JSONArray list = carData.getJSONArray("list");
            int count;
            if (list == null) {
                count = 0;
            } else {
                count = list.size();
            }
            int limit = 10 - count;
            for (int i = 0; i < limit; i++) {
                String plate_number = "吉GBBA3" + i;
                crm.myCarAddCode(car_type, plate_number);
            }
            String plate_number = "豫GBBA11";
            Long code = crm.myCarAddCode(car_type, plate_number);
            checkArgument(code == 1001, "我的车辆上限10辆车");
            if (limit == 0) {
                return;
            } else {
                //删除新增的车辆
                JSONArray listB = crm.myCarList().getJSONArray("list");
                for (int j = 0; j < limit; j++) {
                    Integer car_id = listB.getJSONObject(j).getInteger("my_car_id");
                    crm.myCarDelete(Integer.toString(car_id));
                }
//                JSONArray listq = crm.myCarList().getJSONArray("list");
            }
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("小程序我的车辆，增加十一辆异常验证");
        }
    }


    /**
     * @description :预约保养 小程序页面间数据一致性
     * @date :2020/7/10 22:52
     **/
//    @Test(priority = 2)
    public void mainTain() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String appointment_time = "09:00";
            long timelist = timeList("REAPIR",2);

            JSONObject data = crm.appointmentMaintain(pp.mycarId, customer_name, customer_phone_number, appointment_date, appointment_time, timelist);
            //预约成功
            Long appointment_idM = data.getLong("appointment_id");
            String appointment_statusM = data.getString("appointment_status");
            String appointment_status_nameM = data.getString("appointment_status_name");
            String customer_nameM = data.getString("customer_name");
            String customer_phone_numberM = data.getString("customer_phone_number");
            String appointment_dateM = data.getString("appointment_date");
            String appointment_timeM = data.getString("appointment_time");
            String sale_nameM = data.getString("sale_name");
            String sale_phoneM = data.getString("sale_phone");
            String plate_numberM = data.getString("plate_number");
            String car_type_nameM = data.getString("car_type_name");
            //预约保养成功&预约保养
//            Preconditions.checkArgument(appointment_status_nameM.equals(""),"预约成功状态显示异常");
            checkArgument(customer_nameM.equals(customer_name), "预约保养成功页客户名错误");
            checkArgument(customer_phone_numberM.equals(customer_phone_number), "预约保养成功页客户电话错误");
            checkArgument(appointment_dateM.equals(appointment_date), "预约保养成功预约日期错误");
            checkArgument(appointment_timeM.equals(appointment_time), "预约保养成功预约时间错误");
            //我的 预约消息
            JSONObject dataB = crm.appointmentInfo(appointment_idM);

            String appointment_status_nameB = dataB.getString("appointment_status_name");
            String customer_nameB = dataB.getString("customer_name");
            String customer_phone_numberB = dataB.getString("customer_phone_number");
            String appointment_dateB = dataB.getString("appointment_date");
            String car_type_nameB = dataB.getString("car_type_name");
            String sale_nameB = dataB.getString("sale_name");
            String sale_phoneB = dataB.getString("sale_phone");
//            String appointment_idB = dataB.getString("appointment_id");
//            String appointment_statusB = dataB.getString("appointment_status");
//            String star_ratingB = dataB.getString("star_rating");
//            String service_descriptionB = dataB.getString("service_description");
//            String is_need_evaluateB = dataB.getString("is_need_evaluate");
            //预约消息&填写预约信息页
            checkArgument(customer_nameB.equals(customer_name), "预约消息客户名显示错误");
            checkArgument(customer_phone_numberB.equals(customer_phone_number), "预约消息客户手机号显示错误");
            checkArgument(appointment_dateB.equals(appointment_date), "预约消息预约日期显示错误");

            //预约消息&预约成功
            checkArgument(sale_nameB.equals(sale_nameM), "预约保养接待销售名显示错误");
            checkArgument(sale_phoneB.equals(sale_phoneM), "预约保养接待销售电话显示错误");
            checkArgument(car_type_nameB.equals(car_type_nameM), "预约保养成功页客户试驾车型错误");
            checkArgument(appointment_status_nameB.equals(appointment_status_nameM), "预约保养成功页预约状态错误");
            crm.cancle(appointment_idM);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("预约保养，保养成功，预约消息数据校验");
        }
    }

    /**
     * @description :预约保养成功，页面一致性验证；applet & pc & app
     * @date :2020/7/10 14:29
     **/
//    @Test(priority = 2)
    public void maintian_pcConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //预约保养前：  1.pc端登录，记录原始预约保养总数 A
            crm.login(adminname, adminpassword);
            JSONObject pcdataA = crm.mainAppointmentpage(1, 10);
            String totalA = pcdataA.getString("total");
            String today_numberA = pcdataA.getString("today_number");
            String month_numberA = pcdataA.getString("month_number");
            String total_numberA = pcdataA.getString("total_number");
            //app 今日保养人数和累计试驾总数
            crm.login(pp.fuwuZongjian, pp.adminpassword);
            JSONObject appdataA = crm.mainAppointmentDriverNum();
            String appointment_total_numberA = appdataA.getString("appointment_total_number");
            String appointment_today_numberA = appdataA.getString("appointment_today_number");
            //app 今日试驾列表总数
            JSONObject appListA = crm.mainAppointmentlist();
            String apptotalListA = appListA.getString("total");

            //2.预约试驾
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            String plate_number = "津GBBA29";
            crm.myCarAdd(car_type, plate_number);
            JSONArray list = crm.myCarList().getJSONArray("list");
            if (list == null) {
                throw new Exception("暂无车辆");
            }
            String my_car_id = list.getJSONObject(0).getString("my_car_id");
            String appointment_time = "09:00";
            long timelist = timeList("MAINTAIN",1);

            JSONObject data = crm.appointmentMaintain(Long.parseLong(my_car_id), customer_name, customer_phone_number, appointment_date, appointment_time, timelist);

            //预约试驾后：  pc销售总监总经理权限登录
            crm.login(adminname, adminpassword);
            //3.pc页面显示数据
            JSONObject pcdata = crm.mainAppointmentpage(1, 10);
            String total = pcdata.getString("total");
            String today_number = pcdata.getString("today_number");
            String month_number = pcdata.getString("month_number");
            String total_number = pcdata.getString("total_number");

            String customer_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String customer_phone_numberpc = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_phone_number");
            String order_datepc = pcdata.getJSONArray("list").getJSONObject(0).getString("order_date");
            String car_type_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("car_type_name");  //试驾车型
            String order_numberpc = pcdata.getJSONArray("list").getJSONObject(0).getString("order_number"); //预约次数
            String sale_idpc = pcdata.getJSONArray("list").getJSONObject(0).getString("sale_id");  //销售id
            String sale_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("sale_name");
            String service_status_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("service_status_name"); //接待状态
            String risk_status_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("risk_status_name"); //风险预警

            //app显示数据  B
            crm.login(pp.fuwuZongjian, pp.adminpassword);
            JSONObject appdataB = crm.mainAppointmentDriverNum();
            String appointment_total_numberB = appdataB.getString("appointment_total_number");
            String appointment_today_numberB = appdataB.getString("appointment_today_number");

            JSONObject appdataList1 = crm.mainAppointmentlist();
            String appListtotalB = appdataList1.getString("total");
            JSONObject appdataList = appdataList1.getJSONArray("list").getJSONObject(0);
            String reception_sale_nameapp = appdataList.getString("reception_sale_name");
            String appointment_dateapp = appdataList.getString("appointment_date");
            String customer_nameapp = appdataList.getString("customer_name");
            String car_typeapp = appdataList.getString("car_type");
            String car_type_nameapp = appdataList.getString("car_type_name");
            String service_statusapp = appdataList.getString("service_status");
            String service_status_nameapp = appdataList.getString("service_status_name");
            String customer_phone_numberapp = appdataList.getString("customer_phone_number");
            String phone_appointmentapp = appdataList.getString("phone_appointment");

            Long appoint_id = data.getLong("appointment_id");
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            crm.cancle(appoint_id);
            crm.myCarDelete(my_car_id);

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
            checkArgument(reception_sale_nameapp.equals(sale_namepc));


            //TODO:试驾车型；今日总计，本月总计，当天去重，隔天不去重；故一天只能运行一次，且首次运行 pc
//            Preconditions.checkArgument((Long.parseLong(today_numberA)-Long.parseLong(today_number))==1,"预约试驾成功后，pc预约试驾今日总计没+1");
//            Preconditions.checkArgument((Long.parseLong(month_numberA)-Long.parseLong(month_number))==1,"预约试驾成功后，pc预约试驾本月共计没+1");
//            Preconditions.checkArgument((Long.parseLong(total_numberA)-Long.parseLong(total_number))==1,"预约试驾成功后，pc预约试驾全部累计没有+1");
//
//            //TODO:试驾车型；今日总计，本月总计，当天去重，隔天不去重；故一天只能运行一次，且首次运行 app
//            Preconditions.checkArgument((Long.parseLong(appointment_today_numberB)-Long.parseLong(appointment_today_numberA))==1,"预约试驾成功后，app预约试驾今日总计没有+1");
//            Preconditions.checkArgument((Long.parseLong(appointment_total_numberB)-Long.parseLong(appointment_total_numberA))==1,"预约试驾成功后，app预约试驾全部累计没+1");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("预约保养成功后，pc和app预约试驾信息校验");
        }
    }

    /**
     * @description :预约维修，小程序页面间数据一致性
     * @date :2020/7/11 13:48
     **/
//    @Test(priority = 2)
    public void repair() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String appointment_time = "10:30";
            String description = "故障说明";
            long timelist = timeList("REAPIR",1);
            JSONObject data = crm.appointmentRepair(pp.mycarId, customer_name, customer_phone_number, appointment_date, appointment_time, description, timelist);
            //预约成功
            Long appointment_idM = data.getLong("appointment_id");
            String appointment_statusM = data.getString("appointment_status");
            String appointment_status_nameM = data.getString("appointment_status_name");
            String customer_nameM = data.getString("customer_name");
            String customer_phone_numberM = data.getString("customer_phone_number");
            String appointment_dateM = data.getString("appointment_date");
            String appointment_timeM = data.getString("appointment_time");
            String sale_nameM = data.getString("sale_name");
            String sale_phoneM = data.getString("sale_phone");
            String plate_numberM = data.getString("plate_number");
            String car_type_nameM = data.getString("car_type_name");
            String descriptionM = data.getString("description");
            //预约保养成功&预约保养
            checkArgument(customer_nameM.equals(customer_name), "预约维修成功页客户名错误");
            checkArgument(customer_phone_numberM.equals(customer_phone_number), "预约维修成功页客户电话错误");
            checkArgument(appointment_dateM.equals(appointment_date), "预约维修成功预约日期错误");
            checkArgument(appointment_timeM.equals(appointment_time), "预约维修成功预约时间错误");
            checkArgument(descriptionM.equals(description), "预约维修故障说明显示错误错误");
            //我的 预约消息
            JSONObject dataB = crm.appointmentInfo(appointment_idM);

            String customer_nameB = dataB.getString("name");
            String customer_phone_numberB = dataB.getString("phone");
            String appointment_dateB = dataB.getString("date");
            String car_type_nameB = dataB.getString("car_type_name");
            String sale_nameB = dataB.getString("receptionist");
            String sale_phoneB = dataB.getString("receptionist_phone");
//            String appointment_idB = dataB.getString("id");
//            String appointment_statusB = dataB.getString("status_commnet");
//            String appointment_status_nameB = dataB.getString("appointment_status_name");
//            String star_ratingB = dataB.getString("star_rating");
//            String service_descriptionB = dataB.getString("service_description");
//            String is_need_evaluateB = dataB.getString("is_need_evaluate");
            String descriptionB = data.getString("description");
            //预约消息&填写预约信息页
            checkArgument(customer_nameB.equals(customer_name), "预约消息客户名显示错误");
            checkArgument(customer_phone_numberB.equals(customer_phone_number), "预约消息客户手机号显示错误");
            checkArgument(appointment_dateB.equals(appointment_date), "预约消息预约日期显示错误");
            checkArgument(descriptionB.equals(description), "预约消息故障说明显示错误");

            //预约消息&预约成功
            checkArgument(sale_nameB.equals(sale_nameM), "预约维修接待销售名显示错误");
            checkArgument(sale_phoneB.equals(sale_phoneM), "预约维修接待销售电话显示错误");
            checkArgument(car_type_nameB.equals(car_type_nameM), "预约维修成功页客户试驾车型错误");
            checkArgument(descriptionB.equals(descriptionM), "预约维修成功页故障说明错误");
            crm.cancle(appointment_idM);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("预约维修，小程序页面间数据一致性，填写预约信息&预约成功&我的预约消息");
        }

    }

    /**
     * @description :预约维修成功，页面间一致性验证；applet & pc & app
     * @date :2020/7/10 14:29
     **/
//    @Test(priority = 2)
    public void repair_pcConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //预约维修前：  1.pc端登录，记录原始预约保养总数 A
            crm.login(adminname, adminpassword);
            JSONObject pcdataA = crm.repairAppointmentpage(1, 10);
            String totalA = pcdataA.getString("total");
//            String today_numberA = pcdataA.getString("today_number");
//            String month_numberA = pcdataA.getString("month_number");
//            String total_numberA = pcdataA.getString("total_number");
            //app 今日试驾人数和累计试驾总数
            crm.login(pp.fuwuZongjian, pp.adminpassword);
            JSONObject appdataA = crm.repairAppointmentDriverNum();
//            String appointment_total_numberA = appdataA.getString("appointment_total_number");
//            String appointment_today_numberA = appdataA.getString("appointment_today_number");
            //app 今日试驾列表总数
            JSONObject appListA = crm.repairAppointmentlist();
//            String apptotalListA = appListA.getString("total");
            //2.预约试驾
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());

            String appointment_time = "10:00";
            String description = "故障说明";
            long timelist = timeList("REAPIR",1);
            JSONObject data = crm.appointmentRepair(pp.mycarId, customer_name, customer_phone_number, appointment_date, appointment_time, description, timelist);
            //预约试驾后：  pc销售总监权限登录
            crm.login(adminname, adminpassword);
            //3.pc页面显示数据
            JSONObject pcdata = crm.repairAppointmentpage(1, 10);
            String total = pcdata.getString("total");
//            String today_number = pcdata.getString("today_number");
//            String month_number = pcdata.getString("month_number");
//            String total_number = pcdata.getString("total_number");

            String customer_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String customer_phone_numberpc = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_phone_number");
            String order_datepc = pcdata.getJSONArray("list").getJSONObject(0).getString("order_date");
            String car_type_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("car_type_name");  //试驾车型
            String sale_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("sale_name");
//            String order_numberpc = pcdata.getJSONArray("list").getJSONObject(0).getString("order_number"); //预约次数
//            String sale_idpc = pcdata.getJSONArray("list").getJSONObject(0).getString("sale_id");  //销售id
//            String service_status_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("service_status_name"); //接待状态
//            String risk_status_namepc = pcdata.getJSONArray("list").getJSONObject(0).getString("risk_status_name"); //风险预警

            //app显示数据  B
            crm.login(pp.fuwuZongjian, pp.adminpassword);
            JSONObject appdataB = crm.repairAppointmentDriverNum();
//            String appointment_total_numberB = appdataB.getString("appointment_total_number");
//            String appointment_today_numberB = appdataB.getString("appointment_today_number");

            JSONObject appdataList1 = crm.repairAppointmentlist();
            JSONObject appdataList = appdataList1.getJSONArray("list").getJSONObject(0);
            String reception_sale_nameapp = appdataList.getString("reception_sale_name");
            String appointment_dateapp = appdataList.getString("appointment_date");
            String customer_nameapp = appdataList.getString("customer_name");
            String car_type_nameapp = appdataList.getString("car_type_name");
            String customer_phone_numberapp = appdataList.getString("customer_phone_number");
//            String appListtotalB = appdataList1.getString("total");
//            String car_typeapp = appdataList.getString("car_type");
//            String service_statusapp = appdataList.getString("service_status");
//            String service_status_nameapp = appdataList.getString("service_status_name");
//            String phone_appointmentapp = appdataList.getString("phone_appointment");
            Long appoint_id=data.getLong("appointment_id");
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            crm.cancle(appoint_id);

            checkArgument((parseInt(total) - parseInt(totalA)) == 1, "预约试驾成功后，pc预约试驾条数没有+1");
            checkArgument(customer_namepc.equals(customer_name), "pc预约试驾客户名异常");
            checkArgument(customer_phone_numberpc.equals(customer_phone_number), "pc预约试驾客户手机号异常");
            checkArgument(order_datepc.equals(appointment_date), "pc预约试驾预约时间异常");
            checkArgument(car_type_namepc.equals(car_type_name), "pc预约试驾试驾车型显示异常");


//            Preconditions.checkArgument((Long.parseLong(appListtotalB)-Long.parseLong(apptotalListA))==1,"预约试驾成功后，app预约试驾条数没有+1");
            checkArgument(customer_nameapp.equals(customer_name), "app预约试驾客户名异常");
            checkArgument(customer_phone_numberapp.equals(customer_phone_number), "app预约试驾客户手机号异常");
            checkArgument(appointment_dateapp.equals(appointment_date), "app预约试驾预约时间异常");
            checkArgument(car_type_nameapp.equals(car_type_name), "app预约试驾试驾车型显示异常");

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
            appendFailreason(e.toString());
        } finally {
            saveData("预约维修成功后，pc和app预约试驾信息校验");
        }
    }

    //预约保养
    public String[] maintainP(String date, Long carid, String ifreception) throws Exception {
        String[] a = new String[3]; //0销售登陆账号 1预约记录id 2 接待记录id
        //小程序登陆
        crm.appletLoginToken(EnumAppletCode.XMF.getCode());

        String appointment_time = "09:00";
        long timelist = timeList("MAINTAIN",1);
        JSONObject obj = crm.appointmentMaintain(carid, customer_name, customer_phone_number, date, appointment_time, timelist);
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




    /**
     * @description :人员管理，app完成接待接待次数+1 见保养评价ok
     * @date :2020/8/2 10:43
     **/
//     @Test
    public void finnalRecept() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //完成接待前，接待次数
            //预约接待
            //完成接待后，接待次数

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("完成接待，pc预约记录接待次数+1");
        }
    }

    /**
     * @description :保养评价  ok,预约早上9点，完成接待，3.0时，此case,只运行一次; && 完成接待接待次数+1 ok  TODO:
     * @date :2020/8/2 10:29
     **/
//    @Test(priority = 12)
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
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
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
            appendFailreason(e.toString());
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
            String appointment_date=dt.getHistoryDate(5);
            crm.appletLoginToken(EnumAppletCode.WM.getCode());
            String type = "MAINTAIN";
            String appointment_time = "11:00";
            int i=0;     //预约时段下标
            JSONArray list = crm.timeList(type, appointment_date).getJSONArray("list");
            Integer leftNum = list.getJSONObject(i).getInteger("left_num");
            //预约
            long timelist = timeList(type,i);
            JSONObject data = crm.appointmentMaintain(pp.mycarId, customer_name, customer_phone_number, appointment_date, appointment_time, timelist);
            Long appoint_id = data.getLong("appointment_id");
            JSONArray list2 = crm.timeList(type, appointment_date).getJSONArray("list");
            Integer leftNum2 = list2.getJSONObject(i).getInteger("left_num");
            crm.cancle(appoint_id);    //取消预约
            JSONArray list3 = crm.timeList(type, appointment_date).getJSONArray("list");
            Integer leftNum3 = list3.getJSONObject(i).getInteger("left_num");
            checkArgument((leftNum - leftNum2) == 1, "预约后时段剩余名额没有-1");
            checkArgument((leftNum3 - leftNum2) == 1, "预约后时段剩余名额没有+1");

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("预约该时段剩余-1，取消+1");
        }
    }

    /**
     * @description :预约同一天其他时段失败    1次
     * @date :2020/8/12 12:57
     **/
//    @Test
    public void timeListFail() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
//            String type = "REPAIR";
            String appointment_time = "11:00";
//            JSONArray list = crm.timeList(type, appointment_date).getJSONArray("list");
//            Integer leftNum = list.getJSONObject(i).getInteger("left_num");
            //预约
            long timelist = timeList("MAINTAIN",2);
            JSONObject data = crm.appointmentMaintain((pp.mycarId), customer_name, customer_phone_number, appointment_date, appointment_time, timelist);
            Long appoint_id = data.getLong("appointment_id");

            long timelist2 = crm.timeList("MAINTAIN", appointment_date).getJSONArray("list").getJSONObject(1).getLong("id");
            JSONObject res = crm.appointmentMaintain((pp.mycarId), customer_name, customer_phone_number, appointment_date, appointment_time, timelist2);
//            Long appoint_id2 = res.getJSONObject("data").getLong("appointment_id");
            Long code = res.getLong("code");
            checkArgument(code == 1001, "预约同一天其他时段应该失败");

            crm.cancle(appoint_id);    //取消预约
//            crm.cancle(appoint_id2);    //取消预约

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("预约该时段剩余-1，取消+1");
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
            JSONObject data = crm.appointmentTestDrive("MALE", customer_name, customer_phone_number, appointment_date, car_type);
            Long appoint_id = data.getLong("appointment_id");
            //预约试驾后：  pc销售总监总经理权限登录
            crm.login(adminname, adminpassword);
            //预约记录，接待状态
            String service_status_name = crm.appointmentpage(1, 10).getJSONArray("list").getJSONObject(0).getString("service_status_name");
            //取消预约
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            crm.cancle(appoint_id);

            crm.login(adminname, adminpassword);
            String service_status_nameD = crm.appointmentpage(1, 10).getJSONArray("list").getJSONObject(0).getString("service_status_name");
            checkArgument(service_status_name.equals("预约中"), "预约记录接待状态错误");

            checkArgument(service_status_nameD.equals("已取消"), "预约记录接待状态错误");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
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
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            String yuyueriqi = dt.getHistoryDate(0);
            JSONObject data = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num);
            //报名提交后
            String appointment_idA = data.getString("appointment_id");
//            String appointment_statusA = data.getString("appointment_status");
//            String appointment_status_nameA = data.getString("appointment_status_name");
//            String customer_nameA = data.getString("customer_name");
//            String appointment_dateA = data.getString("appointment_date");
//            String customer_numberA = data.getString("customer_number");
            String customer_phone_numberA = data.getString("customer_phone_number");
            String car_type_nameA = data.getString("car_type_name");
            String sale_nameA = data.getString("sale_name");
            String sale_phoneA = data.getString("sale_phone");

//            Preconditions.checkArgument(customer_nameA.equals(customer_name), "报名成功页信息 客户姓名错误");
            checkArgument(customer_phone_numberA.equals(customer_phone_number), "报名成功页信息 客户姓名错误");
            checkArgument(car_type_nameA.equals(car_type_name), "报名成功页信息 客户姓名错误");

            JSONObject dataB = crm.appointmentInfo(Long.parseLong(appointment_idA));
            //我的 预约消息

            String customer_phone_numberB = dataB.getString("phone");
            String appointment_dateB = dataB.getString("date");
            String car_type_nameB = dataB.getString("car_type_name");
            String sale_nameB = dataB.getString("receptionist");
            String sale_phoneB = dataB.getString("receptionist_phone");
//            String appointment_idB = dataB.getString("id");
//            String appointment_statusB = dataB.getString("status_commnet");
//            String appointment_status_nameB = dataB.getString("appointment_status_name");
//            String customer_nameB = dataB.getString("name");
//            String star_ratingB = dataB.getString("star_rating");
//            String service_descriptionB = dataB.getString("service_description");
//            String is_need_evaluateB = dataB.getString("is_need_evaluate");
            //预约消息&填写报名信息页
//            Preconditions.checkArgument(customer_nameB.equals(customer_name), "预约消息客户名显示错误");
            checkArgument(customer_phone_numberB.equals(customer_phone_number), "预约消息客户手机号显示错误");
            checkArgument(appointment_dateB.equals(yuyueriqi), "预约消息预约日期显示错误");

            //预约消息&报名成功
            checkArgument(sale_nameB.equals(sale_nameA), "活动报名接待销售名显示错误");
            checkArgument(sale_phoneB.equals(sale_phoneA), "活动报名接待销售电话显示错误");
            checkArgument(car_type_nameB.equals(car_type_nameA), "活动报名提交成功页客户试驾车型错误");

            //取消报名，以便可以运行其他case
            crm.cancle(Long.parseLong(appointment_idA));
            crm.login(adminname, adminpassword);
            crm.articleStatusChange(arcile_id);
            crm.articleDelete(arcile_id);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("预约活动，小程序页面间数据一致性");
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());

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
            appendFailreason(e.toString());
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
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            String other_brand = "奥迪";
            String customer_num = "2";
            String time = dt.getHistoryDate(0);
            JSONObject data = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num);
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

            String car_type_nameB = pcdata.getJSONArray("list").getJSONObject(0).getString("car_type_name");
            String appointment_dateB = pcdata.getJSONArray("list").getJSONObject(0).getString("appointment_date");

            checkArgument(customer_nameB.equals(customer_name), "pc报名管理联系人名错误");
            checkArgument(customer_phone_numberB.equals(customer_phone_number), "pc报名管理联系人电话错误");
            checkArgument(appointment_dateB.equals(time), "pc报名管理接待时间错误"); //报名客户没有接待日期只有报名日期
            checkArgument(car_type_nameB.equals(car_type_name), "pc报名管理试驾车型错误");
            crm.articleStatusChange(arcile_id);
            crm.articleDelete(arcile_id);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("applet活动报名,pc报名客户+1");
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());

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
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONObject data1 = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num);

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
            int a = registered_numA;
            int b = customer_maxA;

            checkArgument(2 == a, "pc已报名人数错误");
            checkArgument(50 == b, "pc总报名人数错误");

            crm.login(adminname, adminpassword);
            crm.articleStatusChange(article_id);
            crm.articleDelete(article_id);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
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
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            String other_brand = "奥迪";
            String customer_num = "2";
            JSONObject data1 = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num);
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
            appendFailreason(e.toString());
        } finally {
            saveData("取消预约/活动/，我的预约消息状态改变为已取消");
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());

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
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
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
            appendFailreason(e.toString());
        } finally {
            saveData("pc新建活动，applet报名人数=假定基数+报名人数");
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());

        }
    }

    /**
     * @description :报名活动，小程序报名人数++
     * @date :2020/7/12 14:16
     **/
    @Test(priority = 5)
    public void checkActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String vailtime = dt.getHistoryDate(0);
            Long[] aid = pf.createAArcile_id(vailtime, "9");
            Long activity_id = aid[1];
            Long arcile_id = aid[0];
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            Integer registered_num = crm.appartilceDetail(arcile_id, positions).getInteger("registered_num");
            String other_brand = "奥迪";
            String customer_num = "2";
            JSONObject data = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num);
            //需求变更，审核不通过也显示再小程序已报名人数中
            // String appointment_id=data.getString("appointment_id");
//            crm.login(adminname,adminpassword);
//            JSONObject pcdata=crm.activityList(1,10,activity_id);
//            String totalB=pcdata.getString("total");
            //客户id
            //String customer_id=pcdata.getJSONArray("list").getJSONObject(0).getString("customer_id");
            //customer_idF=customer_id;
            //crm.chackActivity(1,appointment_id);  //pc 审核通过
            // crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            Integer registered_numA = crm.appartilceDetail(arcile_id, positions).getInteger("registered_num");  //文章详情
            checkArgument((registered_numA - registered_num) == parseInt(customer_num), "报名活动人数写2，报名活动页报名人数未加2");
            crm.login(adminname, adminpassword);
            crm.articleStatusChange(arcile_id);
            crm.articleDelete(arcile_id);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("报名活动，小程序报名人数+1");
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());

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
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());

            JSONObject data = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num);
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
            appendFailreason(e.toString());
        } finally {
            saveData("pc把审核通过的报名活动加入黑名单，小程序总报名人数--，报名活动列表总数不变");
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());

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
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());

            checkArgument(list.size() == listpc.size(), "applet看车和pc商品管理车辆列表数量一致");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("applet看车和pc商品管理车辆列表数量一致");
        }
    }

    /**
     * @description :预约试驾预约次数+1
     * @date :2020/8/21 17:01
     **/
    @Test
    public void orderNum(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //小程序预约
            Long id = crm.appointmentTestDrive("MALE", pp.customer_name, pp.customer_phone_number, dt.getHistoryDate(0), car_type).getLong("appointment_id");
            //pc查看预约次数
            crm.login(pp.zongjingli,pp.adminpassword);
            JSONArray ord=crm.appointmentpage(1,10).getJSONArray("list");
            int num=ord.getJSONObject(0).getInteger("order_number");

            //预约
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            Long id2 = crm.appointmentTestDrive("MALE", pp.customer_name, pp.customer_phone_number, dt.getHistoryDate(0), car_type).getLong("appointment_id");

            crm.login(pp.zongjingli,pp.adminpassword);
            int num2=crm.appointmentpage(1,10).getJSONArray("list").getJSONObject(0).getInteger("order_number");
            checkArgument((num2-num)==1,"预约试驾pc预约次数没+1");
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            crm.cancle(id);
            crm.cancle(id2);


        }catch (AssertionError | Exception e){
            appendFailreason(e.toString());
        }
    }

    /**
     * @description :预约试驾，我的试驾消息+1
     * @date :2020/8/25 10:38
     **/
    @Test
    public void driverMessage(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String type="TEST_DRIVE";    //ACTIVITY
            //消息数量
            Long total=crm.appointmentList(0L,type,20).getLong("total");
            JSONObject data = crm.appointmentTestDrive("MALE", customer_name, customer_phone_number, appointment_date, car_type);
            //预约试驾成功后，页面显示数据
            Long appointment_id = data.getLong("appointment_id");
            Long total2=crm.appointmentList(0L,type,20).getLong("total");
            crm.cancle(appointment_id);
            checkArgument((total2-total)==1,"预约试驾，试驾消息没+1");

        }catch (AssertionError | Exception e){
            appendFailreason(e.toString());
        } finally {
            saveData("预约试驾，我的试驾消息+1");
        }
    }
    /**
     * @description :预约活动，我的活动消息+1
     * @date :2020/8/25 10:38
     **/
    @Test
    public void activityMessage(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String type=pp.positions;    //ACTIVITY
            //消息数量
            Long total=crm.appointmentList(0L,type,20).getLong("total");
           //创建活动
            Long[] aid = pf.createAArcile_id(dt.getHistoryDate(0), "8");
            Long activity_id = aid[1];
            Long article_id = aid[0];
            //pc文章详情

            //参加活动，报名人数统计
            String other_brand = "奥迪";
            String customer_num = "2";
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONObject data1 = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num);

            crm.login(adminname, adminpassword);
            crm.articleStatusChange(article_id);
            crm.articleDelete(article_id);
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            Long total2=crm.appointmentList(0L,type,20).getLong("total");
//            crm.cancle(appointment_id);
            checkArgument((total2-total)==1,"预约活动，我的活动消息没+1");



        }catch (AssertionError | Exception e){
            appendFailreason(e.toString());
        } finally {
            saveData("预约活动，我的活动消息+1");
        }
    }

    /**
     * @description :车牌号数量
     * @date :2020/8/24 19:54
     **/
    @Test
    public void provinceList(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.provinceList();
            JSONArray list=data.getJSONArray("list");
//            String p=list.getJSONObject(0).getString("province_name");
            checkArgument(list.size()==31,"车牌号省份不是31");
//            Preconditions.checkArgument(p.equals("苏"),"省份默认不是苏");
        }catch (AssertionError | Exception e){

            appendFailreason(e.toString());
        } finally {
            saveData("车牌号验证");
        }
    }

    @Test
    public void current(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            crm.shop();

        }catch (AssertionError | Exception e){

            appendFailreason(e.toString());
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
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            String other_brand = "奥迪";
            String customer_num = "2";
            for (int i = 0; i < 3; i++) {
                JSONObject data1 = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num);
                String appointment_id = data1.getString("appointment_id");
                crm.cancle(Long.parseLong(appointment_id));  //取消预约
            }
            Long code1 = crm.joinActivityCode(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num).getLong("code");
            checkArgument(code1 == 1001, "报名取消三次后再报名应该失败");

            crm.login(adminname, adminpassword);
            crm.articleStatusChange(article_id);
            crm.articleDelete(article_id);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("报名取消三次后再报名失败");
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
        }
    }


    


    /**
     * @description :我的车辆页==添加车辆后我的预约维修、保养  好像是一个接口。。。。。。
     * @date :2020/7/10 22:29
     **/


    //    public JSONObject addCheckListEmpty(String customer_name,String customer_phone,String appointment_date,Integer car_type, String emptyPara, String message) throws Exception {
//        String url = "/WeChat-applet/porsche/a/appointment/test-drive";
//
//        JSONObject json1=new JSONObject();
//        json1.put("customer_name",customer_name);
//        json1.put("customer_phone_number",customer_phone);
//        json1.put("appointment_date",appointment_date);
//        json1.put("car_type",car_type);
//        json1.put(emptyPara, "");
//
//        String json=json1.toJSONString();
//
////        JSONObject temp = JSON.parseObject(json);
////        temp.put(emptyPara,"");
////
////        json = temp.toJSONString();
//
////        String res = httpPost(url,stringUtil.trimStr(json),getProscheShop());
//        String res = httpPost(url,json,crm.IpPort);
//
//        checkCode(res, StatusCode.BAD_REQUEST, "预约试驾，" + emptyPara + "为空！");
//        checkMessage("预约试驾，" + emptyPara + "为空！", res, message);
//
//        return JSON.parseObject(res).getJSONObject("data");
//    }
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
