package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.StringUtil;
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;


/**
 * @description :小程序数据一致性---xia
 * @date :2020/7/10 11:10
 **/


public class CrmAppletCase extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    StringUtil stringUtil = new StringUtil();
    public String adminname = "xx";    //pc登录密码，最好销售总监或总经理权限
    public String adminpassword = "e10adc3949ba59abbe56e057f20f883e";
    public String code = "dZBhO0nkeaeCRUza2qlH+A==";  //小程序登录加密授权码
    //售后 预约保养 维修 服务总监
    public String adminnameapp = "baoyang";    //服务总监
    public String adminpasswordapp = "e10adc3949ba59abbe56e057f20f883e";

    public String baoyangr = "baoyangr"; //保养顾问
    public String weixiu = "weixiu";     //维修顾问

    public Long mycarID=357L; //TODO:车id待定

    //销售总监  预约试驾 售前
    public String adminnamexiaoshou = "销售总监";    //pc登录密码，最好销售总监或总经理权限
    public String adminpasswordxiaoshou = "e10adc3949ba59abbe56e057f20f883e";
    //预约使用参数
    public String customer_name = "@@@2";
    public String customer_phone_number = "15037286013";
    public String appointment_date = dt.getHistoryDate(1);  //预约日期取当前天的前一天
    public Integer car_type = 1;
    public String car_type_name = "Panamera";
    public Long activity_id = 34L;
    //活动报名客户id 记录 for blackAddConsistency
    public String customer_idF = "";
    public String filePath="src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic";


    //读取文件内容
    public String texFile(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str = in.readLine();
        return str;
    }

    //获取接待人员名称电话 --for createArticle
    public String[] manage(Integer role_id) throws Exception {
        JSONArray list = crm.manageList(role_id).getJSONArray("list");
        String[] name = new String[2];
        for (int i = 0; i < list.size(); i++) {
            int status = list.getJSONObject(i).getInteger("status");
            if (status == 1) {
                name[0] = list.getJSONObject(i).getString("name");
                name[1] = list.getJSONObject(i).getString("phone");
                return name;
            } else {
                continue;
            }
        }
        return name;
    }

    //pc新建活动方法，返回活动id
    public Long createActivity(String valid_start, String simulation_num) {
        Long article_id = 0L;
        try {
            crm.login(adminname, adminpassword);
            String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
            int[] customer_level = {};           //TODO:客户等级
            String[] customer_property = {};
            String[] positions = {"CAR_ACTIVITY"}; //投放位置车型推荐 单选
            // String [] positions={"MODEL_RECOMMENDATION","PURCHASE_GUIDE","BRAND_CULTURE","CAR_ACTIVITY"};
//            String valid_start = dt.getHistoryDate(0);
            String valid_end = dt.getHistoryDate(4);
            int[] car_types = {car_type};
            String article_title = "品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
            String article_bg_pic = texFile(filePath);  //base 64
            String article_content = "品牌上新，优惠多多，限时4天,活动内容";
            String article_remarks = "品牌上新，优惠多多，限时4天,备注";

            boolean is_online_activity = true;  //是否线上报名活动
//            String reception_name = manage(13)[0];  //接待人员名
//            String reception_phone = manage(13)[1]; //接待人员电话
            String reception_name = "xx";  //接待人员名
            String reception_phone = "15037286013"; //接待人员电话
            String customer_max = "50";                    //人数上限

            String activity_start = dt.getHistoryDate(0);
            String activity_end = dt.getHistoryDate(4);
            Integer role_id = 13;
            Boolean is_create_poster = true;//是否生成海报
            Integer task_customer_numa = crm.groupTotal(customer_types, car_types, customer_level, customer_property).getInteger("total");
            Integer task_customer_num = 5;
            //新建文章并返回文章/活动id
            article_id = crm.createArticle(positions, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title, article_bg_pic, article_content, article_remarks, is_online_activity, reception_name, reception_phone, customer_max, simulation_num, activity_start, activity_end, role_id, Integer.toString(task_customer_num), is_create_poster).getLong("id");
            activity_id = crm.appartilceDetail(article_id).getLong("activity_id");
            //activity_id = article_id;
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            logger.info("create activity ,activity_id:{}", activity_id);
        }
        return activity_id;
    }

    //pc新建活动方法，返回文章id
    public Long[] createAArcile_id(String valid_start, String simulation_num) {
        Long article_id = 0L;
        Long[] aid = new Long[2];
        try {
            crm.login(adminname, adminpassword);
            String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
            int[] customer_level = {};           //TODO:客户等级
            String[] customer_property = {};
            String[] positions = {"CAR_ACTIVITY"}; //投放位置车型推荐 单选
            // String [] positions={"MODEL_RECOMMENDATION","PURCHASE_GUIDE","BRAND_CULTURE","CAR_ACTIVITY"};
//            String valid_start = dt.getHistoryDate(0);
            String valid_end = dt.getHistoryDate(4);
            int[] car_types = {};
            String article_title = "品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
            String article_bg_pic = texFile(filePath);  //base 64
            String article_content = "品牌上新，优惠多多，限时4天,活动内容";
            String article_remarks = "品牌上新，优惠多多，限时4天,备注";

            boolean is_online_activity = true;  //是否线上报名活动
//            String reception_name = manage(13)[0];  //接待人员名
//            String reception_phone = manage(13)[1]; //接待人员电话
            String reception_name = "xx";  //接待人员名
            String reception_phone = "15037286013"; //接待人员电话
            String customer_max = "50";                    //人数上限

            String activity_start = dt.getHistoryDate(0);
            String activity_end = dt.getHistoryDate(4);
            Integer role_id = 13;
            Boolean is_create_poster = true;//是否生成海报
            Integer task_customer_numa = crm.groupTotal(customer_types, car_types, customer_level, customer_property).getInteger("total");
            Integer task_customer_num = 5;
            //新建文章并返回文章/活动id
            article_id = crm.createArticle(positions, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title, article_bg_pic, article_content, article_remarks, is_online_activity, reception_name, reception_phone, customer_max, simulation_num, activity_start, activity_end, role_id, Integer.toString(task_customer_num), is_create_poster).getLong("id");
            activity_id = crm.appartilceDetail(article_id).getLong("activity_id");
            long activity_id1 = article_id;
            aid[0] = article_id;  //文章id
            aid[1] = activity_id;  //活动id
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            logger.info("create activity ,activity_id:{}", activity_id);
        }
        return aid;
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
            crm.appletLogin(code);
            Integer car_type = 1;   //试驾车型
            Object[][] objects = emptyParaCheckList();
            for (int i = 0; i < objects.length; i++) {
                String emptyPara = objects[i][0].toString();
                caseDesc = "预约试驾" + emptyPara + "为空！";
                crm.addCheckListEmpty(customer_name, customer_phone_number, appointment_date, car_type, emptyPara, objects[i][1].toString());
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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
            crm.appletLogin(code);
            JSONObject data = crm.appointmentDrive(customer_name, customer_phone_number, appointment_date, car_type);
            //预约试驾成功后，页面显示数据
            Long appointment_id = data.getLong("appointment_id");
            String appointment_statusA = data.getString("appointment_status");
            String appointment_status_nameA = data.getString("appointment_status_name");
            String customer_nameA = data.getString("customer_name");
            String customer_phone_numberA = data.getString("customer_phone_number");
            String appointment_dateA = data.getString("appointment_date");
            String car_type_nameA = data.getString("car_type_name"); // 试驾车型名
            String sale_nameA = data.getString("sale_name");         //接待人名
            String sale_phoneA = data.getString("sale_phone");
            //填写预约信息 & 预约成功
            Preconditions.checkArgument(customer_name.equals(customer_nameA), "预约试驾成功页联系人名显示错误");
            Preconditions.checkArgument(customer_phone_number.equals(customer_phone_numberA), "预约试驾成功页联系人电话显示错误");
            Preconditions.checkArgument(appointment_date.equals(appointment_dateA), "预约试驾成功页预约日期显示错误");
            Preconditions.checkArgument(car_type_nameA.equals(car_type_name), "预约试驾成功页试驾车型显示错误");
            //TODO:试驾车型--固定赋值

            //预约消息
            JSONObject detail = crm.appointmentInfo(appointment_id);

            String appointment_idB = detail.getString("appointment_id");
            String appointment_statusB = detail.getString("appointment_status");
            String appointment_status_nameB = detail.getString("appointment_status_name");
            String customer_nameB = detail.getString("customer_name");
            String customer_phone_numberB = detail.getString("customer_phone_number");
            String appointment_dateB = detail.getString("appointment_date");
            String car_type_nameB = detail.getString("car_type_name");
            String sale_nameB = detail.getString("sale_name");
            String sale_phoneB = detail.getString("sale_phone");
            String star_ratingB = detail.getString("star_rating");
            String service_descriptionB = detail.getString("service_description");
            String is_need_evaluateB = detail.getString("is_need_evaluate");
            //填写预约信息 & 预约消息
            Preconditions.checkArgument(customer_name.equals(customer_nameB), "预约试驾消息页联系人名显示错误");
            Preconditions.checkArgument(customer_phone_number.equals(customer_phone_numberB), "预约试驾消息页联系人电话显示错误");
            Preconditions.checkArgument(appointment_date.equals(appointment_dateB), "预约试驾消息页预约日期显示错误");
            Preconditions.checkArgument(car_type_nameB.equals(car_type_name), "预约试驾成功页试驾车型显示错误");
            //预约成功 & 预约消息
            Preconditions.checkArgument(sale_nameA.equals(sale_nameB), "预约试驾消息页接待人名显示错误");
            Preconditions.checkArgument(sale_phoneA.equals(sale_phoneB), "预约试驾消息页接待人电话显示错误");
            Preconditions.checkArgument(appointment_date.equals(appointment_dateB), "预约试驾消息页预约日期显示错误");
            Preconditions.checkArgument(car_type_nameB.equals(car_type_nameA), "预约试驾成功页试驾车型显示错误");
            crm.cancle(appointment_id);

            //TODO：接待人分配是否轮询

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
            String today_numberA = pcdataA.getString("today_number");
            String month_numberA = pcdataA.getString("month_number");
            String total_numberA = pcdataA.getString("total_number");
            //app 今日试驾人数和累计试驾总数

            crm.login(adminnamexiaoshou, adminpasswordxiaoshou);
            JSONObject appdataA = crm.appointmentDriverNum();
            String appointment_total_numberA = appdataA.getString("appointment_total_number");
            String appointment_today_numberA = appdataA.getString("appointment_today_number");
            //app 今日试驾列表总数
            JSONObject appListA = crm.appointmentlist();

            String apptotalListA = appListA.getString("total");

            //2.预约试驾
            crm.appletLogin(code);
            JSONObject data = crm.appointmentDrive(customer_name, customer_phone_number, appointment_date, car_type);
            Long appoint_id=data.getLong("appointment_id");

            //预约试驾后：  pc销售总监权限登录
            crm.login(adminname, adminpassword);
            //3.pc页面显示数据
            JSONObject pcdata = crm.appointmentpage(1, 10);
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
            crm.login(adminnamexiaoshou, adminpasswordxiaoshou);
            JSONObject appdataB = crm.appointmentDriverNum();
            String appointment_total_numberB = appdataB.getString("appointment_total_number");
            String appointment_today_numberB = appdataB.getString("appointment_today_number");

            JSONObject appdataList1 = crm.appointmentlist();
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

            Preconditions.checkArgument((Long.parseLong(total) - Long.parseLong(totalA)) == 1, "预约试驾成功后，pc预约试驾条数没有+1");
            Preconditions.checkArgument(customer_namepc.equals(customer_name), "pc预约试驾客户名异常");
            Preconditions.checkArgument(customer_phone_numberpc.equals(customer_phone_number), "pc预约试驾客户手机号异常");
            Preconditions.checkArgument(order_datepc.equals(appointment_date), "pc预约试驾预约时间异常");
            Preconditions.checkArgument(car_type_namepc.equals(car_type_name), "pc预约试驾试驾车型显示异常");


            Preconditions.checkArgument((Long.parseLong(appListtotalB) - Long.parseLong(apptotalListA)) == 1, "预约试驾成功后，app预约试驾条数没有+1");
            Preconditions.checkArgument(customer_nameapp.equals(customer_name), "app预约试驾客户名异常");
            Preconditions.checkArgument(customer_phone_numberapp.equals(customer_phone_number), "app预约试驾客户手机号异常");
            Preconditions.checkArgument(appointment_dateapp.equals(appointment_date), "app预约试驾预约时间异常");
            Preconditions.checkArgument(car_type_nameapp.equals(car_type_name), "app预约试驾试驾车型显示异常");

            //pc & app 接待人员
            Preconditions.checkArgument(reception_sale_nameapp.equals(sale_namepc));
            crm.cancle(appoint_id);

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
            saveData("预约试驾成功后，pc和app预约试驾信息校验");
        }
    }

    /**
     * @description :添加车辆，数量+1 & 数据校验  此case第一个运行，为保养维修提供数据
     * @date :2020/7/10 18:03
     **/
    @Test(priority = 1)
    public void mycarConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.appletLogin(code);
            String plate_number = "豫GBBA29";
            JSONObject carData = crm.myCarList();
            JSONArray list = carData.getJSONArray("list");
            int count = 0;
            if (list == null||list.size()==0) {
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

            Preconditions.checkArgument((aftercount - count) == 1, "增加车辆，我的车辆列表没加1");
            Preconditions.checkArgument(car_type_nameBefore.equals(car_type_name), "增加车辆，我的车辆列表车型显示错误");
            Preconditions.checkArgument(plate_numberBefore.equals(plate_number), "增加车辆，我的车辆列表车牌号显示错误");
            crm.myCarDelete(Integer.toString(car_idBefore));

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加车辆，applet我的车辆列表加1");
        }
    }
    /**
     * @description :删除车辆，列表数量-1  此case最后运行，删除mycarConsistency创建的车辆
     * @date :2020/7/10 22:37
     **/
    @Test(priority = 9)
    public void deleteMycar() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.appletLogin(code);
            String plate_number = "豫GBBA29";
            crm.myCarAdd(car_type, plate_number);
            JSONObject carData = crm.myCarList();
            JSONArray list = carData.getJSONArray("list");
            int count = 0;
            if (list == null||list.size()==0) {
                throw new Exception("暂无车辆可删除");
            } else {
                count = list.size();
            }
            String my_car_id = list.getJSONObject(0).getString("my_car_id");
            crm.myCarDelete(my_car_id);
            JSONArray listB = crm.myCarList().getJSONArray("list");
            int aftercount = 0;
            if (listB == null) {
                aftercount = 0;
            } else {
                aftercount = listB.size();
            }
            Preconditions.checkArgument((count - aftercount) == 1, "删除车辆，数量没-1");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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
    public void myCarTen(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            crm.appletLogin(code);
            JSONObject carData = crm.myCarList();
            JSONArray list = carData.getJSONArray("list");
            int count;
            if (list == null) {
                count = 0;
            } else {
                count = list.size();
            }
            int limit=10-count;
            for(int i=0;i<limit;i++){
                String plate_number = "豫GBBA3"+Integer.toString(i);
                crm.myCarAdd(car_type, plate_number);
            }
            //删除新增的车辆
            JSONArray listB = crm.myCarList().getJSONArray("list");
            for(int j=0;j<limit;j++){
                Integer car_id = listB.getJSONObject(j).getInteger("my_car_id");
                crm.myCarDelete(Integer.toString(car_id));
            }
            JSONArray listq = crm.myCarList().getJSONArray("list");
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("小程序我的车辆，增加十辆");
        }
    }

    /**
     * @description :【我的】添加车辆，11辆异常 ok
     * @date :2020/7/27 19:43
     **/
    @Test(priority = 3)
    public void myCarEven(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            crm.appletLogin(code);
            JSONObject carData = crm.myCarList();
            JSONArray list = carData.getJSONArray("list");
            int count;
            if (list == null) {
                count = 0;
            } else {
                count = list.size();
            }
            int limit=10-count;
            for(int i=0;i<limit;i++){
                String plate_number = "豫GBBA3"+Integer.toString(i);
                crm.myCarAddCode(car_type, plate_number);
            }
            String plate_number = "豫GBBA11";
            Long code=crm.myCarAddCode(car_type, plate_number);
            Preconditions.checkArgument(code==1001,"我的车辆上限10辆车");
            if(limit==0) {return;}
            else{
                //删除新增的车辆
                JSONArray listB = crm.myCarList().getJSONArray("list");
                for (int j = 0; j <limit; j++) {
                    Integer car_id = listB.getJSONObject(j).getInteger("my_car_id");
                    crm.myCarDelete(Integer.toString(car_id));
                }
                JSONArray listq = crm.myCarList().getJSONArray("list");
            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("小程序我的车辆，增加十一辆异常验证");
        }
    }



    /**
     * @description :预约保养 小程序页面间数据一致性
     * @date :2020/7/10 22:52
     **/
    @Test(priority = 2)
    public void mainTain() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.appletLogin(code);
            String plate_number = "豫GBBA29";
            crm.myCarAdd(car_type, plate_number);
            JSONObject carData = crm.myCarList();
            JSONArray list = carData.getJSONArray("list");
            if (list == null||list.size()==0) {
                throw new Exception("暂无车辆");
            }
            String my_car_id = list.getJSONObject(0).getString("my_car_id");
            String appointment_time = "09:00";
            JSONObject data = crm.appointmentMaintain(Long.parseLong(my_car_id), customer_name, customer_phone_number, appointment_date, appointment_time);
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
            Preconditions.checkArgument(customer_nameM.equals(customer_name), "预约保养成功页客户名错误");
            Preconditions.checkArgument(customer_phone_numberM.equals(customer_phone_number), "预约保养成功页客户电话错误");
            Preconditions.checkArgument(appointment_dateM.equals(appointment_date), "预约保养成功预约日期错误");
            Preconditions.checkArgument(appointment_timeM.equals(appointment_time), "预约保养成功预约时间错误");
            //我的 预约消息
            JSONObject dataB = crm.appointmentInfo(appointment_idM);
            String appointment_idB = dataB.getString("appointment_id");
            String appointment_statusB = dataB.getString("appointment_status");
            String appointment_status_nameB = dataB.getString("appointment_status_name");
            String customer_nameB = dataB.getString("customer_name");
            String customer_phone_numberB = dataB.getString("customer_phone_number");
            String appointment_dateB = dataB.getString("appointment_date");
            String car_type_nameB = dataB.getString("car_type_name");
            String sale_nameB = dataB.getString("sale_name");
            String sale_phoneB = dataB.getString("sale_phone");
            String star_ratingB = dataB.getString("star_rating");
            String service_descriptionB = dataB.getString("service_description");
            String is_need_evaluateB = dataB.getString("is_need_evaluate");
            //预约消息&填写预约信息页
            Preconditions.checkArgument(customer_nameB.equals(customer_name), "预约消息客户名显示错误");
            Preconditions.checkArgument(customer_phone_numberB.equals(customer_phone_number), "预约消息客户手机号显示错误");
            Preconditions.checkArgument(appointment_dateB.equals(appointment_date), "预约消息预约日期显示错误");

            //预约消息&预约成功
            Preconditions.checkArgument(sale_nameB.equals(sale_nameM), "预约保养接待销售名显示错误");
            Preconditions.checkArgument(sale_phoneB.equals(sale_phoneM), "预约保养接待销售电话显示错误");
            Preconditions.checkArgument(car_type_nameB.equals(car_type_nameM), "预约保养成功页客户试驾车型错误");
            Preconditions.checkArgument(appointment_status_nameB.equals(appointment_status_nameM), "预约保养成功页预约状态错误");
            crm.cancle(appointment_idM);
            crm.myCarDelete(my_car_id);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("预约保养，保养成功，预约消息数据校验");
        }
    }

    /**
     * @description :预约保养成功，页面一致性验证；applet & pc & app
     * @date :2020/7/10 14:29
     **/
    @Test(priority = 2)
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
            crm.login(adminnameapp, adminpasswordapp);
            JSONObject appdataA = crm.mainAppointmentDriverNum();
            String appointment_total_numberA = appdataA.getString("appointment_total_number");
            String appointment_today_numberA = appdataA.getString("appointment_today_number");
            //app 今日试驾列表总数
            JSONObject appListA = crm.mainAppointmentlist();
            String apptotalListA = appListA.getString("total");

            //2.预约试驾
            crm.appletLogin(code);
            String plate_number = "豫GBBA29";
            crm.myCarAdd(car_type, plate_number);
            JSONArray list = crm.myCarList().getJSONArray("list");
            if (list == null) {
                throw new Exception("暂无车辆");
            }
            String my_car_id = list.getJSONObject(0).getString("my_car_id");
            String appointment_time = "10:00";
            JSONObject data = crm.appointmentMaintain(Long.parseLong(my_car_id), customer_name, customer_phone_number, appointment_date, appointment_time);

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
            crm.login(adminnameapp, adminpasswordapp);
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

            Preconditions.checkArgument((Long.parseLong(total) - Long.parseLong(totalA)) == 1, "预约试驾成功后，pc预约试驾条数没有+1");
            Preconditions.checkArgument(customer_namepc.equals(customer_name), "pc预约试驾客户名异常");
            Preconditions.checkArgument(customer_phone_numberpc.equals(customer_phone_number), "pc预约试驾客户手机号异常");
            Preconditions.checkArgument(order_datepc.equals(appointment_date), "pc预约试驾预约时间异常");
            Preconditions.checkArgument(car_type_namepc.equals(car_type_name), "pc预约试驾试驾车型显示异常");


            Preconditions.checkArgument((Long.parseLong(appListtotalB) - Long.parseLong(apptotalListA)) == 1, "预约试驾成功后，app预约试驾条数没有+1");
            Preconditions.checkArgument(customer_nameapp.equals(customer_name), "app预约试驾客户名异常");
            Preconditions.checkArgument(customer_phone_numberapp.equals(customer_phone_number), "app预约试驾客户手机号异常");
            Preconditions.checkArgument(appointment_dateapp.equals(appointment_date), "app预约试驾预约时间异常");
            Preconditions.checkArgument(car_type_nameapp.equals(car_type_name), "app预约试驾试驾车型显示异常");

            //pc & app 接待人员
            Preconditions.checkArgument(reception_sale_nameapp.equals(sale_namepc));
            Long appoint_id=data.getLong("appointment_id");
            crm.appletLogin(code);
            crm.cancle(appoint_id);
            crm.myCarDelete(my_car_id);

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
    @Test(priority = 2)
    public void repair() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.appletLogin(code);
            String plate_number = "豫GBBA29";
            crm.myCarAdd(car_type, plate_number);
            JSONObject carData = crm.myCarList();
            JSONArray list = carData.getJSONArray("list");
            if (list == null||list.size()==0) {
                throw new Exception("暂无车辆");
            }
            String my_car_id = list.getJSONObject(0).getString("my_car_id");
            String appointment_time = "10:30";
            String description = "故障说明";
            JSONObject data = crm.appointmentRepair(Long.parseLong(my_car_id), customer_name, customer_phone_number, appointment_date, appointment_time, description);
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
            Preconditions.checkArgument(customer_nameM.equals(customer_name), "预约维修成功页客户名错误");
            Preconditions.checkArgument(customer_phone_numberM.equals(customer_phone_number), "预约维修成功页客户电话错误");
            Preconditions.checkArgument(appointment_dateM.equals(appointment_date), "预约维修成功预约日期错误");
            Preconditions.checkArgument(appointment_timeM.equals(appointment_time), "预约维修成功预约时间错误");
            Preconditions.checkArgument(descriptionM.equals(description), "预约维修故障说明显示错误错误");
            //我的 预约消息
            JSONObject dataB = crm.appointmentInfo(appointment_idM);
            String appointment_idB = data.getString("appointment_id");
            String appointment_statusB = data.getString("appointment_status");
            String appointment_status_nameB = data.getString("appointment_status_name");
            String customer_nameB = data.getString("customer_name");
            String customer_phone_numberB = data.getString("customer_phone_number");
            String appointment_dateB = data.getString("appointment_date");
            String car_type_nameB = data.getString("car_type_name");
            String sale_nameB = data.getString("sale_name");
            String sale_phoneB = data.getString("sale_phone");
            String star_ratingB = data.getString("star_rating");
            String service_descriptionB = data.getString("service_description");
            String is_need_evaluateB = data.getString("is_need_evaluate");
            String descriptionB = data.getString("description");
            //预约消息&填写预约信息页
            Preconditions.checkArgument(customer_nameB.equals(customer_name), "预约消息客户名显示错误");
            Preconditions.checkArgument(customer_phone_numberB.equals(customer_phone_number), "预约消息客户手机号显示错误");
            Preconditions.checkArgument(appointment_dateB.equals(appointment_date), "预约消息预约日期显示错误");
            Preconditions.checkArgument(descriptionB.equals(description), "预约消息故障说明显示错误");

            //预约消息&预约成功
            Preconditions.checkArgument(sale_nameB.equals(sale_nameM), "预约维修接待销售名显示错误");
            Preconditions.checkArgument(sale_phoneB.equals(sale_phoneM), "预约维修接待销售电话显示错误");
            Preconditions.checkArgument(car_type_nameB.equals(car_type_nameM), "预约维修成功页客户试驾车型错误");
            Preconditions.checkArgument(descriptionB.equals(descriptionM), "预约维修成功页故障说明错误");
            crm.cancle(appointment_idM);
            crm.myCarDelete(my_car_id);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("预约维修，小程序页面间数据一致性，填写预约信息&预约成功&我的预约消息");
        }

    }

    /**
     * @description :预约维修成功，页面间一致性验证；applet & pc & app
     * @date :2020/7/10 14:29
     **/
    @Test(priority = 2)
    public void repair_pcConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //预约维修前：  1.pc端登录，记录原始预约保养总数 A
            crm.login(adminname, adminpassword);
            JSONObject pcdataA = crm.repairAppointmentpage(1, 10);
            String totalA = pcdataA.getString("total");
            String today_numberA = pcdataA.getString("today_number");
            String month_numberA = pcdataA.getString("month_number");
            String total_numberA = pcdataA.getString("total_number");
            //app 今日试驾人数和累计试驾总数
            crm.login(adminnameapp, adminpasswordapp);
            JSONObject appdataA = crm.repairAppointmentDriverNum();
            String appointment_total_numberA = appdataA.getString("appointment_total_number");
            String appointment_today_numberA = appdataA.getString("appointment_today_number");
            //app 今日试驾列表总数
            JSONObject appListA = crm.repairAppointmentlist();
            String apptotalListA = appListA.getString("total");

            //2.预约试驾
            crm.appletLogin(code);
            String plate_number = "豫GBBA29";
            crm.myCarAdd(car_type, plate_number);
            JSONArray list = crm.myCarList().getJSONArray("list");
            if (list == null) {
                throw new Exception("暂无车辆");
            }
            String my_car_id = list.getJSONObject(0).getString("my_car_id");
            String appointment_time = "10:00";
            String description = "故障说明";
            JSONObject data = crm.appointmentRepair(Long.parseLong(my_car_id), customer_name, customer_phone_number, appointment_date, appointment_time, description);
            //预约试驾后：  pc销售总监权限登录
            crm.login(adminname, adminpassword);
            //3.pc页面显示数据
            JSONObject pcdata = crm.repairAppointmentpage(1, 10);
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
            crm.login(adminnameapp, adminpasswordapp);
            JSONObject appdataB = crm.repairAppointmentDriverNum();
            String appointment_total_numberB = appdataB.getString("appointment_total_number");
            String appointment_today_numberB = appdataB.getString("appointment_today_number");

            JSONObject appdataList1 = crm.repairAppointmentlist();
            JSONObject appdataList = appdataList1.getJSONArray("list").getJSONObject(0);
            String appListtotalB = appdataList1.getString("total");
            String reception_sale_nameapp = appdataList.getString("reception_sale_name");
            String appointment_dateapp = appdataList.getString("appointment_date");
            String customer_nameapp = appdataList.getString("customer_name");
            String car_typeapp = appdataList.getString("car_type");
            String car_type_nameapp = appdataList.getString("car_type_name");
            String service_statusapp = appdataList.getString("service_status");
            String service_status_nameapp = appdataList.getString("service_status_name");
            String customer_phone_numberapp = appdataList.getString("customer_phone_number");
            String phone_appointmentapp = appdataList.getString("phone_appointment");
            Long appoint_id=data.getLong("appointment_id");
            crm.cancle(appoint_id);
            Preconditions.checkArgument((Integer.parseInt(total) - Integer.parseInt(totalA)) == 1, "预约试驾成功后，pc预约试驾条数没有+1");
            Preconditions.checkArgument(customer_namepc.equals(customer_name), "pc预约试驾客户名异常");
            Preconditions.checkArgument(customer_phone_numberpc.equals(customer_phone_number), "pc预约试驾客户手机号异常");
            Preconditions.checkArgument(order_datepc.equals(appointment_date), "pc预约试驾预约时间异常");
            Preconditions.checkArgument(car_type_namepc.equals(car_type_name), "pc预约试驾试驾车型显示异常");


//            Preconditions.checkArgument((Long.parseLong(appListtotalB)-Long.parseLong(apptotalListA))==1,"预约试驾成功后，app预约试驾条数没有+1");
            Preconditions.checkArgument(customer_nameapp.equals(customer_name), "app预约试驾客户名异常");
            Preconditions.checkArgument(customer_phone_numberapp.equals(customer_phone_number), "app预约试驾客户手机号异常");
            Preconditions.checkArgument(appointment_dateapp.equals(appointment_date), "app预约试驾预约时间异常");
            Preconditions.checkArgument(car_type_nameapp.equals(car_type_name), "app预约试驾试驾车型显示异常");

            //pc & app 接待人员
            Preconditions.checkArgument(reception_sale_nameapp.equals(sale_namepc));

//            //TODO:试驾车型；今日总计，本月总计，当天去重，隔天不去重；故一天只能运行一次，且首次运行 pc
//            Preconditions.checkArgument((Long.parseLong(today_numberA)-Long.parseLong(today_number))==1,"预约试驾成功后，pc预约试驾今日总计没+1");
//            Preconditions.checkArgument((Long.parseLong(month_numberA)-Long.parseLong(month_number))==1,"预约试驾成功后，pc预约试驾本月共计没+1");
//            Preconditions.checkArgument((Long.parseLong(total_numberA)-Long.parseLong(total_number))==1,"预约试驾成功后，pc预约试驾全部累计没有+1");
//
//            //TODO:试驾车型；今日总计，本月总计，当天去重，隔天不去重；故一天只能运行一次，且首次运行 app
//            Preconditions.checkArgument((Long.parseLong(appointment_today_numberB)-Long.parseLong(appointment_today_numberA))==1,"预约试驾成功后，app预约试驾今日总计没有+1");
//            Preconditions.checkArgument((Long.parseLong(appointment_total_numberB)-Long.parseLong(appointment_total_numberA))==1,"预约试驾成功后，app预约试驾全部累计没+1");
              crm.myCarDelete(my_car_id);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("预约维修成功后，pc和app预约试驾信息校验");
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
            String vailtime = dt.getHistoryDate(0);
            Long[] aid = createAArcile_id(vailtime, "9");
            Long activity_id = aid[1];
            Long arcile_id = aid[0];

            logger.info("---------------------活动id:--------------{}", activity_id);
            String other_brand = "奥迪";
            String customer_num = "2";
            crm.appletLogin(code);
            String yuyueriqi = dt.getHistoryDate(0);
            JSONObject data = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num);
            //报名提交后
            String appointment_idA = data.getString("appointment_id");
            String appointment_statusA = data.getString("appointment_status");
            String appointment_status_nameA = data.getString("appointment_status_name");
            String customer_nameA = data.getString("customer_name");
            String customer_phone_numberA = data.getString("customer_phone_number");
            String car_type_nameA = data.getString("car_type_name");
            String appointment_dateA = data.getString("appointment_date");
            String customer_numberA = data.getString("customer_number");
            String sale_nameA = data.getString("sale_name");
            String sale_phoneA = data.getString("sale_phone");

            Preconditions.checkArgument(customer_nameA.equals(customer_name), "报名成功页信息 客户姓名错误");
            Preconditions.checkArgument(customer_phone_numberA.equals(customer_phone_number), "报名成功页信息 客户姓名错误");
            Preconditions.checkArgument(car_type_nameA.equals(car_type_name), "报名成功页信息 客户姓名错误");
            Preconditions.checkArgument(car_type_nameA.equals(car_type_name), "报名成功页信息 客户姓名错误");

            JSONObject dataB = crm.appointmentInfo(Long.parseLong(appointment_idA));
            //我的 预约消息
            String appointment_idB = dataB.getString("appointment_id");
            String appointment_statusB = dataB.getString("appointment_status");
            String appointment_status_nameB = dataB.getString("appointment_status_name");
            String customer_nameB = dataB.getString("customer_name");
            String customer_phone_numberB = dataB.getString("customer_phone_number");
            String appointment_dateB = dataB.getString("appointment_date");
            String car_type_nameB = dataB.getString("car_type_name");
            String sale_nameB = dataB.getString("sale_name");
            String sale_phoneB = dataB.getString("sale_phone");
            String star_ratingB = dataB.getString("star_rating");
            String service_descriptionB = dataB.getString("service_description");
            String is_need_evaluateB = dataB.getString("is_need_evaluate");
            //预约消息&填写报名信息页
            Preconditions.checkArgument(customer_nameB.equals(customer_name), "预约消息客户名显示错误");
            Preconditions.checkArgument(customer_phone_numberB.equals(customer_phone_number), "预约消息客户手机号显示错误");
            Preconditions.checkArgument(appointment_dateB.equals(yuyueriqi), "预约消息预约日期显示错误");

            //预约消息&报名成功
            Preconditions.checkArgument(sale_nameB.equals(sale_nameA), "活动报名接待销售名显示错误");
            Preconditions.checkArgument(sale_phoneB.equals(sale_phoneA), "活动报名接待销售电话显示错误");
            Preconditions.checkArgument(car_type_nameB.equals(car_type_nameA), "活动报名提交成功页客户试驾车型错误");

            //取消报名，以便可以运行其他case
            crm.cancle(Long.parseLong(appointment_idA));
            crm.login(adminname, adminpassword);
            crm.articleStatusChange(arcile_id);
            crm.articleDelete(arcile_id);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("预约活动，小程序页面间数据一致性");
        }
    }

    /**
     * @description :取消预约/活动/，我的预约消息状态改变为已取消" ok
     * @date :2020/7/11 23:11
     **/
    @Test(priority = 3, dataProvider = "APPOINTMENT_TYPE", dataProviderClass = CrmScenarioUtil.class)
    public void cancleappointment(String appointment_type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = crm.appointmentList1();
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
                    Preconditions.checkArgument(appointment_status_name.equals("已取消"), "预约保养成功页预约状态未变为已取消");
                    break;
                } else continue;
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("取消预约/活动/，我的预约消息状态改变为已取消");
        }
    }

    /**
     * @description :活动报名 pc报名客户total+1& 报名管理数据校验  TODO:
     * @date :2020/7/12 11:48
     **/
    @Test(priority = 5)
    public void activityConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //活动报名前
            crm.login(adminname, adminpassword);
            Long[] aid = createAArcile_id(dt.getHistoryDate(0), "2");
            Long activity_id = aid[1];
            Long arcile_id = aid[0];
            JSONObject dataA = crm.activityList(1, 10, activity_id);
            String total = dataA.getString("total");
            //活动报名
            crm.appletLogin(code);
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

            Preconditions.checkArgument((Long.parseLong(totalB) - Long.parseLong(total) == 1), "小程序活动报名，pc报名客户未+1");

            String customer_nameB = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String customer_phone_numberB = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_phone_number");
            String customer_numberB = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_number");
            String sale_nameB = pcdata.getJSONArray("list").getJSONObject(0).getString("sale_name");
            String service_statusB = pcdata.getJSONArray("list").getJSONObject(0).getString("service_status");
            String service_status_nameB = pcdata.getJSONArray("list").getJSONObject(0).getString("service_status_name");
            String audit_statusB = pcdata.getJSONArray("list").getJSONObject(0).getString("audit_status");
            String show_auditB = pcdata.getJSONArray("list").getJSONObject(0).getString("show_audit");
            String audit_status_nameB = pcdata.getJSONArray("list").getJSONObject(0).getString("audit_status_name");
            String risk_statusB = pcdata.getJSONArray("list").getJSONObject(0).getString("risk_status");
            String risk_status_nameB = pcdata.getJSONArray("list").getJSONObject(0).getString("risk_status_name");
            String customer_typeB = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_type");
            String customer_idB = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_id");
            String car_typeB = pcdata.getJSONArray("list").getJSONObject(0).getString("car_type");
            String car_type_nameB = pcdata.getJSONArray("list").getJSONObject(0).getString("car_type_name");
            String appointment_dateB = pcdata.getJSONArray("list").getJSONObject(0).getString("appointment_date");
            String appointment_activity_totalB = pcdata.getJSONArray("list").getJSONObject(0).getString("appointment_activity_total");

            Preconditions.checkArgument(customer_nameB.equals(customer_name), "pc报名管理联系人名错误");
            Preconditions.checkArgument(customer_phone_numberB.equals(customer_phone_number), "pc报名管理联系人电话错误");
            Preconditions.checkArgument(appointment_dateB.equals(time), "pc报名管理接待时间错误"); //报名客户没有接待日期只有报名日期
            Preconditions.checkArgument(car_type_nameB.equals(car_type_name), "pc报名管理试驾车型错误");
            crm.articleStatusChange(arcile_id);
            crm.articleDelete(arcile_id);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("applet活动报名,pc报名客户+1");
        }
    }

    /**
     * @description :活动报名，applet已报名人数++，剩余人数--，pc 总数--，已报名人数++
     * @date :2020/7/21 15:29
     **/
    @Test(priority = 5)
    public void pcappointmentSum(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //创建活动
            crm.login(adminname,adminpassword);
            String simulation_num="8";
            String vailtime=dt.getHistoryDate(0);
            Long [] aid=createAArcile_id(vailtime,simulation_num);
            Long activity_id=aid[1];
            Long article_id=aid[0];
            //pc文章详情

            //参加活动，报名人数统计
            crm.appletLogin(code);
            String other_brand="奥迪";
            String customer_num="2";
            crm.appletLogin(code);
            JSONObject data1=crm.joinActivity(Long.toString(activity_id),customer_name,customer_phone_number,appointment_date,car_type,other_brand,customer_num);

            JSONObject data=crm.articleDetial(article_id);
            Integer registered_num=data.getInteger("registered_num");  //文章详情
            Integer customer_max=data.getInteger("customer_max");  //剩余人数
            int aa=registered_num.intValue();  //已报名人数
            int bb=customer_max.intValue();
            Preconditions.checkArgument(aa==10,"applet已报名人数=假定基数+报名人数");
            Preconditions.checkArgument(bb==48,"applet剩余名额=总人数-报名人数");
            crm.login(adminname,adminpassword);
            JSONObject dataA=crm.artilceDetailpc(article_id);
            Integer registered_numA=dataA.getInteger("registered_num");  //已报名人数
            Integer customer_maxA=dataA.getInteger("customer_max");  //总数
            int a=registered_numA.intValue();
            int b=customer_maxA.intValue();

            Preconditions.checkArgument(a==2,"pc已报名人数错误");
            Preconditions.checkArgument(b==50,"pc总报名人数错误");

            crm.login(adminname,adminpassword);
            crm.articleStatusChange(article_id);
            crm.articleDelete(article_id);

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
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
            crm.login(adminname, adminpassword);
            String simulation_num = "8";
            String vailtime = dt.getHistoryDate(0);
            Long[] aid = createAArcile_id(vailtime, simulation_num);
            Long activity_id = aid[1];
            Long article_id = aid[0];
            //参加活动，报名人数统计
            crm.appletLogin(code);
            String other_brand = "奥迪";
            String customer_num = "2";
            JSONObject data1 = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num);
            String appointment_id = data1.getString("appointment_id");
            JSONObject data = crm.articleDetial(article_id);
            Integer registered_num = data.getInteger("registered_num");  //文章详情
            Integer customer_max = data.getInteger("customer_max");  //剩余人数
            int aa = registered_num.intValue();  //已报名人数
            int bb = customer_max.intValue();

            crm.cancle(Long.parseLong(appointment_id));  //取消预约
            JSONObject dataA = crm.articleDetial(article_id);
            Integer registered_numA = dataA.getInteger("registered_num");  //文章详情
            Integer customer_maxA = dataA.getInteger("customer_max");  //剩余人数
            int a = registered_numA.intValue();
            int b = customer_maxA.intValue();

            Preconditions.checkArgument((aa - a) == Integer.valueOf(customer_num), "取消预约，已报名人数没减少");
            Preconditions.checkArgument((b - bb) == Integer.valueOf(customer_num), "取消预约，剩余名额没增加");

            crm.login(adminname,adminpassword);
            crm.articleStatusChange(article_id);
            crm.articleDelete(article_id);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
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
            Long[] aid = createAArcile_id(valid_start, simulation_num);  //创建活动方法
            Long article_id = aid[0];
            crm.appletLogin(code);
            JSONObject data = crm.articleDetial(article_id);
            Integer registered_numA = data.getInteger("registered_num");  //文章详情
            Integer customer_max = data.getInteger("customer_max");  //剩余人数
            //pc 创建活动 不预约，创建活动假定基数==applet报名人数
            Preconditions.checkArgument((Integer.toString(registered_numA)).equals(simulation_num), "创建后，不预约创建活动假定基数！=applet报名人数");
            Preconditions.checkArgument((Integer.toString(customer_max)).equals("50"), "创建后，不预约活动剩余人数!=活动总名额");
            crm.login(adminname, adminpassword);
            crm.articleStatusChange(article_id);
            crm.articleDelete(article_id);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc新建活动，applet报名人数=假定基数+报名人数");
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
            crm.login(adminname, adminpassword);
            String vailtime = dt.getHistoryDate(0);
            Long[] aid = createAArcile_id(vailtime, "9");
            Long activity_id = aid[1];
            Long arcile_id = aid[0];
            crm.appletLogin(code);
            Integer registered_num = crm.articleDetial(arcile_id).getInteger("registered_num");
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
            // crm.appletlogin(code);
            Integer registered_numA = crm.articleDetial(arcile_id).getInteger("registered_num");  //文章详情
            Preconditions.checkArgument((registered_numA - registered_num) == Integer.valueOf(customer_num), "报名活动人数写2，报名活动页报名人数未加2");
            crm.login(adminname, adminpassword);
            crm.articleStatusChange(arcile_id);
            crm.articleDelete(arcile_id);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
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
            Long[] aid = createAArcile_id(vailtime, "9");
            Long activity_id = aid[1];
            Long arcile_id = aid[0];
            String other_brand = "奥迪";
            String customer_num = "2";
            crm.appletLogin(code);

            JSONObject data = crm.joinActivity(Long.toString(activity_id), customer_name, customer_phone_number, appointment_date, car_type, other_brand, customer_num);
            String appointment_id = data.getString("appointment_id");
            Integer registered_num = crm.articleDetial(arcile_id).getInteger("registered_num");  //小程序活动报名人数

            crm.login(adminname, adminpassword);
            JSONObject pcdata = crm.activityList(1, 10, activity_id);
            String totalB = pcdata.getString("total");  //pc报名总数
            //客户id
            String customer_id = pcdata.getJSONArray("list").getJSONObject(0).getString("customer_id");
            //crm.chackActivity(1,appointment_id);  //pc 审核通过

            crm.blackadd(customer_id);                //加入黑名单
            //移除黑名单
            crm.blackRemove(customer_id);
            Integer registered_numA = crm.articleDetial(arcile_id).getInteger("registered_num");
            Preconditions.checkArgument((registered_num - registered_numA) == 2, "pc把审核通过的报名活动加入黑名单，小程序总报名人数没--");

            JSONObject pcdataA = crm.activityList(1, 10, activity_id);
            String totalA = pcdataA.getString("total");
            Preconditions.checkArgument(totalA.equals(totalB), "pc把审核通过的报名活动加入黑名单,报名活动列表总数不变");
            crm.articleStatusChange(arcile_id);
            crm.articleDelete(arcile_id);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc把审核通过的报名活动加入黑名单，小程序总报名人数--，报名活动列表总数不变");
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
            crm.appletLogin(code);
            //applet
            JSONArray list = crm.appletwatchCarList().getJSONArray("list");
            //pc
            crm.login(adminname, adminpassword);
            JSONArray listpc = crm.carList().getJSONArray("list");
            Preconditions.checkArgument(list.size() == listpc.size(), "applet看车和pc商品管理车辆列表数量一致");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("applet看车和pc商品管理车辆列表数量一致");
        }
    }

    //预约保养
    public String[] maintainP(String date, Long carid, String ifreception) throws Exception {
        String a[] = new String[3]; //0销售登陆账号 1预约记录id 2 接待记录id
        //小程序登陆
        crm.appletLogin(code);

        String appointment_time = "09:00";
        JSONObject obj = crm.appointmentMaintain(carid, customer_name, customer_phone_number, date, appointment_time);
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
        crm.login(baoyangr, adminpassword);
        if (ifreception.equals("yes")) {
            Long after_record_id = crm.reception_customer(maintain_id).getLong("after_record_id");
            a[2] = Long.toString(after_record_id);
        } else {
            a[2] = "未点击接待按钮";
        }
        return a;

    }


    /**
     * @description :人员管理，app完成接待接待次数+1  TODO:
     * @date :2020/8/2 10:43
     **/
//     @Test
     public void finnalRecept(){
         logger.logCaseStart(caseResult.getCaseName());
         try{
             //完成接待前，接待次数
             //预约接待
             //完成接待后，接待次数

         }catch (AssertionError e){
             appendFailreason(e.toString());
         }catch (Exception e){
             appendFailreason(e.toString());
         }finally {
             saveData("完成接待，pc预约记录接待次数+1");
         }
     }
    /**
     * @description :保养评价  ok,预约早上9点，完成接待，3.0时，此case,只运行一次; && 完成接待接待次数+1
     * @date :2020/8/2 10:29
     **/
    @Test(priority = 12)
    public void appointEvaluate(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //接待前baoyangr，接待次数
            crm.login(adminname,adminpassword);
            JSONArray list= crm.ManageList(16).getJSONArray("list");
            if(list==null||list.size()==0){
                return;
            }
            int  num=0;
            for(int i=0;i<list.size();i++){
                String name=list.getJSONObject(i).getString("name");
                if(name.equals("baoyangr")){
                    num=list.getJSONObject(i).getInteger("num");
                }else{
                    continue;
                }
            }
            String [] aa= maintainP(dt.getHistoryDate(0),mycarID,"yes");
            String appoint_id=aa[1];

             int num2=0;
            JSONArray list2= crm.ManageList(16).getJSONArray("list");
            for(int j=0;j<list2.size();j++){
                String name2=list2.getJSONObject(j).getString("name");
                if(name2.equals("baoyangr")){
                    num2=list2.getJSONObject(j).getInteger("num");
                }else{
                    continue;
                }
            }
            Preconditions.checkArgument((num2-num)==1,"接待完成，接待次数没+1");
            crm.appletLogin("123456");   //评价
            crm.appointmentEvaluate(Long.parseLong(appoint_id),"4","保养满意");
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("保养评价");
        }
    }

    /**
     * @description :小程序取消预约，pc预约记录，接待状态预约中变更已取消 ok
     * @date :2020/7/30 12:41
     **/
    @Test
    public void appointmentStstus(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            crm.appletLogin(code);
//            String plate_number = "蒙GBBA10";
//            crm.myCarAdd(car_type, plate_number);
//            JSONArray list = crm.myCarList().getJSONArray("list");
//            if (list == null) {
//                throw new Exception("暂无车辆");
//            }
//            String my_car_id = list.getJSONObject(0).getString("my_car_id");
            String appointment_time = "10:00";
            JSONObject data = crm.appointmentMaintain((mycarID), customer_name, customer_phone_number, appointment_date, appointment_time);
            Long appoint_id=data.getLong("appointment_id");
            //预约试驾后：  pc销售总监总经理权限登录
            crm.login(adminname, adminpassword);
            //预约记录，接待状态
            String service_status_name=crm.mainAppointmentpage(1,10).getJSONArray("list").getJSONObject(0).getString("service_status_name");
            //取消预约
            crm.cancle(appoint_id);
            String service_status_nameD=crm.mainAppointmentpage(1,10).getJSONArray("list").getJSONObject(0).getString("service_status_name");
            Preconditions.checkArgument(service_status_name.equals("预约中"),"预约记录接待状态错误");

            Preconditions.checkArgument(service_status_nameD.equals("已取消"),"预约记录接待状态错误");
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("小程序取消预约，pc预约记录，接待状态预约中变更已取消");
        }
    }



    /**
     * @description :删除历史数据
     * @date :2020/7/19 20:27
     **/
    //@Test
    public void deletaDate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(adminname, adminpassword);
            JSONArray list = crm.articlePage(2, 10).getJSONArray("list");
            for (int i = 0; i < 10; i++) {
                Long id = list.getJSONObject(i).getLong("id");
                crm.articleStatusChange(id);
                crm.articleDelete(id);
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("删除文章活动");
        }
    }

    /**
     * @description :
     * @date :2020/7/21 10:48
     **/
    //@Test
    public void create99() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (int i = 0; i < 40; i++) {
                createAArcile_id("2020-07-21", "10");
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("删除文章活动");
        }
    }


    /**
     * @description :我的车辆页==添加车辆后我的预约维修、保养  好像是一个接口。。。。。。
     * @date :2020/7/10 22:29
     **/
    //@Test
    public void mycarAndRepair() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("数据一致性：我的车辆页==添加车辆后我的预约维修、保养");
        }
    }


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
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
//        crm.login(adminname, adminpassword);
        crm.appletLogin(code);


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

}
