package com.haisheng.framework.testng.bigScreen.crm.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletCode;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PackFunction;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicParm;
import com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo.destDriver;
import com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo.finishReceive;
import com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo.orderCar;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @description :2.1交车、试驾、客户relate----xia
 * @date :2020/8/3 12:47
 **/


public class Crm2_1AppX extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp = new PublicParm();
    PackFunction pf = new PackFunction();
    FileUtil file = new FileUtil();
    Random random=new Random();

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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "CRM 日常X");

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        crm.login(pp.xiaoshouGuwen, pp.adminpassword);


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
     * @description :试驾评价 && 完成接待接待次数+1 ；评价完成pc评价列表+1
     * @date :2020/8/2 10:29
     **/
//    @Test(priority = 12)
    public void driverEvaluate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //接待前Guwen，接待次数 13 销售顾问，16保养 ，15维修
            int num = pf.jiedaiTimes(13, pp.xiaoshouGuwen);
            //pc评价页总数
            int total = crm.evaluateList(1, 10, "", "", "").getInteger("total");
            //预约接待完成
            Long appointmentId = pf.driverEva();

            int num2 = pf.jiedaiTimes(13, pp.xiaoshouGuwen);
            Preconditions.checkArgument((num2 - num) == 1, "接待完成，接待次数没+1");
            //小程序评价
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
//            SERVICE_QUALITY|PROCESS|PROFESSIONAL|EXPERIENCE
            int score = 4;
            JSONObject ll = new JSONObject();
            ll.put("score", score);
            ll.put("type_comment", "销售接待服务质量");
            ll.put("type", "SERVICE_QUALITY");

            JSONObject ll2 = new JSONObject();
            ll2.put("score", score);
            ll2.put("type_comment", "销售接待服务流程");
            ll2.put("type", "PROCESS");

            JSONObject ll3 = new JSONObject();
            ll3.put("score", score);
            ll3.put("type_comment", "试乘试驾体验评价");
            ll3.put("type", "EXPERIENCE");

            JSONArray array1 = new JSONArray();
            array1.add(0, ll);
            array1.add(1, ll2);
            array1.add(2, ll3);

            crm.appointmentEvaluate(appointmentId, "保养满意", array1);  //评价
            crm.login(pp.zongjingli, pp.adminpassword);
            int totalB = crm.evaluateList(1, 10, "", "", "").getInteger("total");
            Preconditions.checkArgument((totalB - total) == 1, "评价后，pc评价列表没+1");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("试驾评价 && 完成接待接待次数+1 ；评价完成pc评价列表+1");
        }
    }

    /**
     * @description :直接接待老客，为小程序接待评价提供消息
     * @date :2020/8/22 14:05
     **/
    @Test(priority = 12)
    public void acceptEvaluate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //pc评价页总数
            String type = "MSG";
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            int total = crm.messageList(20, type).getInteger("total");
            finishReceive fr = new finishReceive();
            //预约接待完成
            JSONObject json = pf.creatCustOld(pp.customer_phone_number);
            fr.name = pp.customer_name;
            fr.reception_id = json.getString("id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            //完成接待
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "BB";
            fr.remark = new JSONArray();
            crm.finishReception3(fr);

            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONArray messagePage = crm.messageList(10, type).getJSONArray("list");
            Long id = messagePage.getJSONObject(0).getLong("id");

            int totalB = crm.messageList(100, type).getInteger("total");
            //小程序评价
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
//            SERVICE_QUALITY|PROCESS|PROFESSIONAL|EXPERIENCE
            int score = 4;
            JSONObject ll = new JSONObject();
            ll.put("score", score);
            ll.put("type_comment", "销售接待服务质量");
            ll.put("type", "SERVICE_QUALITY");

            JSONObject ll2 = new JSONObject();
            ll2.put("score", score);
            ll2.put("type_comment", "销售接待服务流程");
            ll2.put("type", "PROCESS");
//
            JSONObject ll3 = new JSONObject();
            ll3.put("score", score);
            ll3.put("type_comment", "销售接待专业评价");
            ll3.put("type", "PROFESSIONAL");

            JSONObject ll4 = new JSONObject();
            ll4.put("score", score);
            ll4.put("type", "EXPERIENCE");
            ll4.put("type_comment", "试乘试驾体验评价");


            JSONArray array1 = new JSONArray();
            array1.add(0, ll);
            array1.add(1, ll2);
            array1.add(2, ll3);
            array1.add(3, ll4);

            crm.messageEvaluate(id, "我的消息-保养满意", array1);  //评价

            Preconditions.checkArgument((totalB - total) == 1, "接待小程序客户，发送评价消息，我的消息数量没+1");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("接待小程序客户，发送评价消息，我的消息数量+1");
        }
    }


    /**
     * @description :创建新客试驾,今日试驾次数+1,总计+1   ok
     * @date :2020/8/10 16:45
     **/
//    @Test(priority = 12)
    public void testderver() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //销售总监今日试驾总数
            crm.login(pp.xiaoshouZongjian, pp.adminpassword);
            JSONObject dataTotal = crm.driverTotal();
            int today_number = dataTotal.getInteger("today_test_drive_total");
            int totalNum = dataTotal.getInteger("test_drive_total");

            JSONObject object = pf.creatCust();  //创建新客
            finishReceive fr = new finishReceive();
            fr.customer_id = object.getString("customerId");
            fr.reception_id = object.getString("reception_id");
            fr.name = object.getString("name");
            String phone = object.getString("phone");
            fr.phoneList = object.getJSONArray("phoneList");
            fr.belongs_sale_id = object.getString("sale_id");
            fr.reception_type = "FU";
            String userLoginName = object.getString("userLoginName");
//            crm.login(pp.xiaoshouGuwen,pp.adminpassword);
//            String customer_name="@@@2";
//            String phone=pp.customer_phone_number;
//            Long reception_id=3968L;
//            Long customer_id=14508L;
//            String userLoginName="销售顾问xia";
            pf.creatDriver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, phone, 1);  //新客试驾
            JSONObject dataTotal2 = crm.driverTotal();
            int today_number2 = dataTotal2.getInteger("today_test_drive_total");
            int totalNum2 = dataTotal2.getInteger("test_drive_total");
            crm.login(userLoginName, pp.adminpassword);

            crm.finishReception3(fr);
            Preconditions.checkArgument(today_number2 - today_number == 1, "新建试驾，今日试驾+1，试驾后:" + today_number2 + "试驾前：" + today_number);
            Preconditions.checkArgument(totalNum2 - totalNum == 1, "新建试驾，总计试驾+1，试驾后：" + totalNum2 + "，试驾前：{}" + totalNum);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建新客试驾,今日试驾次数+1,总计+1");
        }
    }

    /**
     * @description :创建新客交车,今日交车次数+1,总计+1 列表数+1 ok
     * @date :2020/8/10 16:45
     **/
    @Test(priority = 12)
    public void testdeliver() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //销售总监今日试驾总数
            crm.login(pp.xiaoshouZongjian, pp.adminpassword);

            JSONObject dataTotal = crm.jiaocheTotal();
            int today_number = dataTotal.getInteger("today_deliver_car_total");
            int totalNum = dataTotal.getInteger("deliver_car_total");
            int listtotal=crm.deliverSelect(1,10).getInteger("total");

            JSONObject json = pf.creatCust();
            finishReceive fr = new finishReceive();
            fr.name = "新客交车数据校验";
            fr.reception_id = json.getString("reception_id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "BB";

            String userLoginName = json.getString("userLoginName");
            String phone = json.getString("phone");

            pf.creatDeliver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, dt.getHistoryDate(0), true);

            crm.login(pp.xiaoshouZongjian, pp.adminpassword);
            JSONObject dataTotal2 = crm.jiaocheTotal();
            int today_number2 = dataTotal2.getInteger("today_deliver_car_total");
            int totalNum2 = dataTotal2.getInteger("deliver_car_total");
            JSONObject data=crm.deliverSelect(1,10);
            int listtotal2=data.getInteger("total");
            JSONObject list=data.getJSONArray("list").getJSONObject(0);
            String deliver_car_time=list.getString("deliver_car_time");
            String customer_name=list.getString("customer_name");
            String customer_phone_number=list.getString("customer_phone_number");


            crm.login(userLoginName, pp.adminpassword);
            crm.finishReception3(fr);
            Preconditions.checkArgument(today_number2 - today_number == 1, "新建交车，今日交车+1，交车后：{}，交车前：{}", today_number2, today_number);
            Preconditions.checkArgument(totalNum2 - totalNum == 1, "新建交车，总计交车+1，交车后：{}，交车前：{}", totalNum2, totalNum);
            Preconditions.checkArgument(listtotal2 - listtotal == 1, "新建交车，列表数+1，交车后：{}，交车前：{}", listtotal2, listtotal);
            Preconditions.checkArgument(deliver_car_time.equals(dt.getHistoryDate(0)), "新建交车，列表中交车时间错误");
            Preconditions.checkArgument(customer_name.equals(fr.name), "新建交车，列表中交客户名错误");
            Preconditions.checkArgument(customer_phone_number.equals(phone), "新建交车，列表中交客户电话错误");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建新客交车,今日交车次数+1,总计+1，列表数+1");
        }
    }
    /**
     * @description :创建新客交车,数据统计 TODO:
     * @date :2020/8/10 16:45
     **/
    @Test()
    public void testdeliverstatis() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(pp.qiantai,pp.qtpassword);
            String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
            String userLoginName1 = pf.username(sale_id);
            crm.login(userLoginName1, pp.adminpassword);

            JSONObject dataTotal = crm.jiaocheTotal();
            int today_number = dataTotal.getInteger("today_deliver_car_total");
            int totalNum = dataTotal.getInteger("deliver_car_total");
            int total_order = dataTotal.getInteger("total_order");
            int listtotal=crm.deliverSelect(1,10).getInteger("total");

            JSONObject json = pf.creatCust();
            finishReceive fr = new finishReceive();
            fr.name = "新客交车数据校验";
            fr.reception_id = json.getString("reception_id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "BB";

            String userLoginName = json.getString("userLoginName");

            String vehicle_chassis_code = "ASD123456" + (random.nextInt(89999999) + 10000000);
            Long car_id = crm.addOrderCar(fr.customer_id, fr.reception_id, vehicle_chassis_code).getLong("car_id");

            JSONObject dataTotal2 = crm.jiaocheTotal();
            int today_number2 = dataTotal2.getInteger("today_deliver_car_total");
            int totalNum2 = dataTotal2.getInteger("deliver_car_total");
            int total_order2 = dataTotal2.getInteger("total_order");

            Long model = crm.customerOrderCar(fr.customer_id).getJSONArray("list").getJSONObject(0).getLong("car_model_id");
            String path = file.texFile(pp.filePath);
            crm.deliverAdd(car_id, Long.parseLong(fr.reception_id),  Long.parseLong(fr.customer_id), fr.name, dt.getHistoryDate(0), model, path, true, path, vehicle_chassis_code);

            JSONObject dataTotal3 = crm.jiaocheTotal();
            int today_number3 = dataTotal3.getInteger("today_deliver_car_total");
            int totalNum3 = dataTotal3.getInteger("deliver_car_total");
            int total_order3 = dataTotal3.getInteger("total_order");
            JSONObject data=crm.deliverSelect(1,10);
            int listtotal3=data.getInteger("total");

            JSONObject list=data.getJSONArray("list").getJSONObject(0);
            String deliver_car_time=list.getString("deliver_car_time");
            String customer_name=list.getString("customer_name");
            String customer_phone_number=list.getString("customer_phone_number");


            crm.login(userLoginName, pp.adminpassword);
            crm.finishReception3(fr);
//            Preconditions.checkArgument(total_order2 - total_order == 1, "购车全部交车+1");  //TODO:此处有bug
            Preconditions.checkArgument(totalNum2  == totalNum, "购车实际交车+0");
            Preconditions.checkArgument(today_number2  == today_number, "购车今日交车+0");

//            Preconditions.checkArgument(total_order3 ==total_order , "交车全部交车+0");
            Preconditions.checkArgument(totalNum3 - totalNum==1, "购车实际交车+1");
            Preconditions.checkArgument(today_number3 - today_number==1, "购车今日交车+1");

            Preconditions.checkArgument(listtotal3 - listtotal == 1, "新建交车，列表数+1", listtotal3, listtotal);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建新客交车数据统计");
        }
    }

    /**
     * @description :我的接待按时间查询 ok
     * @date :2020/8/3 12:48
     **/
    @Test(dataProvider = "SELECT_DATE", dataProviderClass = CrmScenarioUtil.class)
    public void MyReceptionSelectTime(String select_date) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.customerMyReceptionList("", select_date, select_date, 10, 1).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String service_date = list.getJSONObject(i).getString("service_date");
                Preconditions.checkArgument(service_date.equals(select_date), "我的接待按接待时间{}查询，结果{}错误", select_date, service_date);
            }
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的接待按接待日期查询，结果校验");
        }
    }

    /**
     * @description :我的接待按时间查仅输入开始结束时间 ok
     * @date :2020/8/3 12:48
     **/
    @Test()
    public void MyReceptionSelect2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String select_date = dt.getHistoryDate(0);
            crm.customerMyReceptionList("", select_date, select_date, 10, 1);
            crm.customerMyReceptionList("", select_date, select_date, 10, 1);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的接待查询");
        }
    }

    /**
     * @description :我的接待组合查询
     * @date :2020/8/3 12:48
     **/
    @Test()
    public void MyReceptionSelectTimeAndname() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = crm.customerMyReceptionList("", "", "", 10, 1);
            String customer_name = data.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String select_date = dt.getHistoryDate(0);
            JSONArray list = crm.deliverSelect(1, 10, customer_name, select_date, select_date).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String timeSelect = list.getJSONObject(i).getString("service_date");
                String nameSelect = list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument((timeSelect.equals(select_date)) && (customer_name.equals(nameSelect)), "我的接待按交车时间{}查询，结果{}错误", select_date, timeSelect);
            }
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的接待组合查询，结果校验");
        }
    }


    /**
     * @description :我的接待按名字/电话查询，结果校验  ok
     * @date :2020/8/3 12:48
     **/
    @Test
    public void MyReceptionSelect() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = crm.customerMyReceptionList("", "", "", 10, 1);
            String customer_name = data.getJSONArray("list").getJSONObject(0).getString("customer_name");
//            String customer_phone_number=data.getJSONArray("list").getJSONObject(0).getString("customer_phone");
            String customer_phone_number = pp.customer_phone_number;

            JSONArray list = crm.customerMyReceptionList(customer_name, "", "", 1, 10).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String nameSelect = list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument(nameSelect.contains(customer_name), "我的接待按客户名称查询，结果错误");
            }
            JSONArray listPhone = crm.customerMyReceptionList(customer_phone_number, "", "", 1, 10).getJSONArray("list");
            for (int i = 0; i < listPhone.size(); i++) {
                String PhoneSelect = listPhone.getJSONObject(i).getString("customer_phone");
                Preconditions.checkArgument(PhoneSelect.equals(customer_phone_number), "我的接待按客户电话查询，结果错误");
            }

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的接待按名字/电话查询，结果校验");
        }
    }

    /**
     * @description :交车按名字/电话查询，结果校验  ok
     * @date :2020/8/3 12:48
     **/
    @Test
    public void jiaocheSelect() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray data = crm.deliverSelect(1, 10).getJSONArray("list");
            String customer_name = data.getJSONObject(0).getString("customer_name");
            String customer_phone_number = "";
            for (int i = 0; i < data.size(); i++) {
                String phoneTemp = data.getJSONObject(0).getString("customer_phone_number");
                if (phoneTemp != null) {
                    customer_phone_number = phoneTemp;
                    break;
                }
            }
            JSONArray list = crm.deliverSelect(1, 10, customer_name).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String nameSelect = list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument(nameSelect.contains(customer_name), "交车按客户名称查询，结果错误");
            }
            JSONArray listPhone = crm.deliverSelect(1, 10, customer_phone_number).getJSONArray("list");
            for (int i = 0; i < listPhone.size(); i++) {
                String PhoneSelect = listPhone.getJSONObject(i).getString("customer_phone_number");
                Preconditions.checkArgument(PhoneSelect.equals(customer_phone_number), "交车按客户电话查询，结果错误");
            }

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("交车按名字/电话查询，结果校验");
        }
    }

    /**
     * @description :交车按时间查询 ok
     * @date :2020/8/3 12:48
     **/
    @Test(dataProvider = "SELECT_DATE", dataProviderClass = CrmScenarioUtil.class)
    public void jiaocheSelectTime(String select_date) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.deliverSelect(1, 10, select_date, select_date).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String timeSelect = list.getJSONObject(i).getString("deliver_car_time");
                Preconditions.checkArgument(timeSelect.equals(select_date), "交车按交车时间{}查询，结果{}错误", select_date, timeSelect);
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("交车按交车日期查询，结果校验");
        }
    }

    /**
     * @description :交车按时间查仅输入开始结束时间 ok
     * @date :2020/8/3 12:48
     **/
    @Test()
    public void jiaocheSelect2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.deliverSelect(1, 10, "", dt.getHistoryDate(0));
            crm.deliverSelect(1, 10, dt.getHistoryDate(0), "");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("交车查询");
        }
    }

    /**
     * @description :交车查询
     * @date :2020/8/3 12:48
     **/
    @Test()
    public void jiaocheSelectTimeAndname() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = crm.deliverSelect(1, 10);
            String customer_name = data.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String select_date = dt.getHistoryDate(0);
            JSONArray list = crm.deliverSelect(1, 10, customer_name, select_date, select_date).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String timeSelect = list.getJSONObject(i).getString("deliver_car_time");
                String nameSelect = list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument((timeSelect.equals(select_date)) && (customer_name.equals(nameSelect)), "交车按交车时间{}查询，结果{}错误", select_date, timeSelect);
            }
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("交车组合查询，结果校验");
        }
    }


    /**
     * @description :交车按名字/电话查询，结果校验
     * @date :2020/8/3 12:48
     **/
    @Test
    public void driverSelect() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray data = crm.driverSelect(1, 10).getJSONArray("list");
            String customer_name = data.getJSONObject(0).getString("customer_name");
            String customer_phone_number = "";
            for (int i = 0; i < data.size(); i++) {
                String phoneTemp = data.getJSONObject(i).getString("customer_phone_number");
                if (phoneTemp != null) {
                    customer_phone_number = phoneTemp;
                    break;
                }
            }
            JSONArray list = crm.driverSelect(1, 10, customer_name).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String nameSelect = list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument(nameSelect.contains(customer_name), "交车按客户名称查询，结果错误");
            }
            JSONArray listPhone = crm.driverSelect(1, 10, customer_phone_number).getJSONArray("list");
            for (int i = 0; i < listPhone.size(); i++) {
                String PhoneSelect = listPhone.getJSONObject(i).getString("customer_phone_number");
                Preconditions.checkArgument(PhoneSelect.equals(customer_phone_number), "交车按客户电话查询，结果错误");
            }

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("交车按名字/电话查询，结果校验");
        }
    }

    /**
     * @description :试驾查询 ok
     * @date :2020/8/3 12:48
     **/
    @Test(dataProvider = "SELECT_DATE", dataProviderClass = CrmScenarioUtil.class)
    public void driverSelectTime(String select_date) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.driverSelect(1, 10, select_date, select_date).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String timeSelect = list.getJSONObject(i).getString("sign_date");
                Preconditions.checkArgument(timeSelect.equals(select_date), "交车按交车时间{}查询，结果{}错误", select_date, timeSelect);
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("试驾按试驾日期查询，结果校验");
        }
    }

    /**
     * @description :试驾查询 ok
     * @date :2020/8/3 12:48
     **/
    @Test()
    public void driverSelectTimeAndname() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = crm.driverSelect(1, 10);
            String customer_name = data.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String select_date = dt.getHistoryDate(0);
            JSONArray list = crm.driverSelect(1, 10, customer_name, select_date, select_date).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String timeSelect = list.getJSONObject(i).getString("sign_time");
                String nameSelect = list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument((timeSelect.equals(select_date)) && (customer_name.equals(nameSelect)), "交车按交车时间{}查询，结果{}错误", select_date, timeSelect);
            }
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("试驾组合查询，结果校验");
        }
    }

    /**
     * @description :客户按名字/电话查询，结果校验 ok
     * @date :2020/8/3 12:48
     **/
    @Test
    public void customerSelect() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray data = crm.customerSelect(1, 10).getJSONArray("list");
            String customer_name = data.getJSONObject(0).getString("customer_name");
            String customer_phone = "";
            for (int i = 0; i < data.size(); i++) {
                String phoneTemp = data.getJSONObject(i).getString("customer_phone");
                if (phoneTemp != null) {
                    customer_phone = phoneTemp;
                    break;
                }
            }
            JSONArray list = crm.customerSelect(1, 10, customer_name).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String nameSelect = list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument(nameSelect.contains(customer_name), "客户按客户名称查询，结果错误");
            }
            JSONArray listPhone = crm.customerSelect(1, 10, customer_phone).getJSONArray("list");
            for (int i = 0; i < listPhone.size(); i++) {
                String PhoneSelect = listPhone.getJSONObject(i).getString("customer_phone");
                Preconditions.checkArgument(PhoneSelect.equals(customer_phone), "客户按客户电话查询，结果错误");
            }

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("客户按名字/电话查询，结果校验");
        }
    }

    /**
     * @description :客户查询按创建时间
     * @date :2020/8/3 12:48
     **/
    @Test(dataProvider = "SELECT_DATE", dataProviderClass = CrmScenarioUtil.class)
    public void customerSelectTime(String select_date) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.customerSelect(1, 10, select_date, select_date).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String timeSelect = list.getJSONObject(i).getString("create_date");
                if (timeSelect != null) {
                    Preconditions.checkArgument(timeSelect.equals(select_date), "客户按创建时间" + select_date + "查询到" + timeSelect + "日期数据");
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("客户按创建日期查询，结果校验");
        }
    }

    /**
     * @description :客户查询组合查询
     * @date :2020/8/3 12:48
     **/
    @Test()
    public void customerSelectTimeAndname() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = crm.customerSelect(1, 10);
            String customer_name = data.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String select_date = dt.getHistoryDate(0);
            JSONArray list = crm.customerSelect(1, 10, customer_name, select_date, select_date).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String timeSelect = list.getJSONObject(i).getString("create_date");
                String nameSelect = list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument((timeSelect.equals(select_date)) && (customer_name.equals(nameSelect)), "客户按交车时间{}查询，结果{}错误", select_date, timeSelect);
            }
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("客户组合查询，结果校验");
        }
    }

    /**
     * @description :创建线索 ok
     * @date :2020/8/3 14:58
     **/
    @Test
    public void createLine() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String customer_phone = pf.genPhoneNum();
            String remark = "自动化创建线索自动化创建线索自动化创建线索";
            Long code = crm.createLine("诸葛自动线索", 1, customer_phone, 1, remark).getLong("code");
            Preconditions.checkArgument(code == 1000, "使用不存在的手机号创建线索应该成功");

            Long code2 = crm.createLine("诸葛自动", 1, customer_phone, 1, remark).getLong("code");
            Preconditions.checkArgument(code2 == 1001, "使用存在的手机号创建线索不应该成功");

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索");
        }
    }


    /**
     * @description :新建交车授权，是小程序最新车主风采 ok
     * @date :2020/8/3 16:46
     **/
    @Test(priority = 1)
    public void carOwerNewst() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject object = pf.creatCust();
            finishReceive fr = new finishReceive();
            fr.customer_id = object.getString("customerId");
            fr.reception_id = object.getString("reception_id");
            fr.name = object.getString("name");
            String phone = object.getString("phone");
            fr.phoneList = object.getJSONArray("phoneList");
            fr.belongs_sale_id = object.getString("sale_id");
            fr.reception_type = "FU";
            pf.creatDeliver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), "新车授权", dt.getHistoryDate(0), true);
            crm.finishReception3(fr);
            //小程序登录，查看最新交车
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONObject data = crm.carOwnernew();
            String customer_nameN = data.getString("customer_name");
            String car_model = data.getString("car_model_name");
            String car_style = data.getString("car_style_name");
            String work = data.getString("work");
            String hobby = data.getString("hobby");
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            Preconditions.checkArgument(car_style.equals("Panamera"), "最新交车信息校验失败");
            Preconditions.checkArgument(work.equals("金融"), "最新交车信息校验工作显示错误");
            Preconditions.checkArgument(hobby.equals("宠物"), "最新交车信息校验爱好显示错误");
            Preconditions.checkArgument(customer_nameN.equals("新车授权"), "最新交车信息校验车主名显示错误");


        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建交车授权，是小程序最新车主风采");
        }
    }

    /**
     * @description :新建交车授权，applet车主风采列表+1 ok
     * @date :2020/8/3 18:25
     **/
    @Test(priority = 12)
    public void carOwerListUp() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //applet登录，记录原始列表数
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONArray list = crm.carOwner().getJSONArray("list");
            int total;
            if (list == null || list.size() == 0) {
                total = 0;
            } else {
                total = list.size();
            }
            JSONObject object = pf.creatCust();
            finishReceive fr = new finishReceive();
            fr.customer_id = object.getString("customerId");
            fr.reception_id = object.getString("reception_id");
            fr.name = object.getString("name");
            fr.phoneList = object.getJSONArray("phoneList");
            fr.belongs_sale_id = object.getString("sale_id");
            fr.reception_type = "FU";
            pf.creatDeliver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, dt.getHistoryDate(0), true);
            crm.finishReception3(fr);

            //小程序登录，查看交车
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONArray listA = crm.carOwner().getJSONArray("list");
            int totalA;
            if (listA == null || listA.size() == 0) {
                totalA = 0;
            } else {
                totalA = listA.size();
            }
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            Preconditions.checkArgument(totalA - total == 1, "建交车授权，applet车主风采列表没加1；交车前" + totalA + "+交车后" + total);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建交车授权，applet车主风采列表+1");
        }
    }

    /**
     * @description :交车不授权  ok
     * @date :2020/8/10 16:45
     **/
    @Test()
    public void testdeliverNotShow() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //applet登录，记录原始列表数
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONArray list = crm.carOwner().getJSONArray("list");
            int total;
            if (list == null || list.size() == 0) {
                total = 0;
            } else {
                total = list.size();
            }
            JSONObject object = pf.creatCust();
            finishReceive fr = new finishReceive();
            fr.customer_id = object.getString("customerId");
            fr.reception_id = object.getString("reception_id");
            fr.name = object.getString("name");
            fr.phoneList = object.getJSONArray("phoneList");
            fr.belongs_sale_id = object.getString("sale_id");
            fr.reception_type = "FU";
            pf.creatDeliver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, dt.getHistoryDate(0), false);
            crm.finishReception3(fr);

            //小程序登录，查看交车
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONArray listA = crm.carOwner().getJSONArray("list");
            int totalA;
            if (listA == null || listA.size() == 0) {
                totalA = 0;
            } else {
                totalA = listA.size();
            }
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            logger.info("交车前total{},交车后totalA{}", totalA, total);
            Preconditions.checkArgument(totalA == total, "新建交车不授权，applet车主风采列表+1了");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建交车不授权，applet车主风采列表不+1");
        }
    }

    /**
     * @description :app活动报名,任务人数于创建时相同，增加报名，报名条数+1，  删除报名-1 0k
     * @date :2020/8/3 19:13
     **/
    @Test
    public void taskactivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建活动，获取活动id
            Long[] aid = pf.createAArcile_id(dt.getHistoryDate(0), "8");
            Long id = aid[0];
            //app销售登录报名
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            JSONObject response = crm.activityTaskPageX();
            JSONObject json = response.getJSONObject("data").getJSONArray("list").getJSONObject(0);   //新建的活动在pad端的位置需要确认 TODO:
            int activityTaskId = json.getInteger("activity_task_id");
            int task_customer_num = json.getInteger("task_customer_num");
            JSONArray list = json.getJSONArray("customer_list");
            int total;
            if (list == null || list.size() == 0) {
                total = 0;
            } else {
                total = list.size();
            }
            String phone = "1";
            for (int i = 0; i < 10; i++) {
                String a = Integer.toString((int) (Math.random() * 10));
                phone = phone + a;
            }
            crm.registeredCustomer((long) activityTaskId, "夏", phone);

            JSONObject responseA = crm.activityTaskPageX();
            JSONObject jsonA = responseA.getJSONObject("data").getJSONArray("list").getJSONObject(0);   //新建的活动在pad端的位置需要确认 TODO:
            int totalA = jsonA.getJSONArray("customer_list").size();

            int customerId = crm.activityTaskInfo(Integer.toString(activityTaskId)).getJSONArray("customer_list").getJSONObject(0).getInteger("customer_id");
            crm.deleteCustomer(String.valueOf(activityTaskId), customerId);

            int totalAfterDelet = crm.activityTaskPageX().getJSONObject("data").getJSONArray("list").getJSONObject(0).getJSONArray("customer_list").size();
            //获取报名字段，校验
            Preconditions.checkArgument(task_customer_num == 5, "app报名活动，任务人数与活动创建时不一致");
            Preconditions.checkArgument(totalA - total == 1, "app报名活动，报名列表+1");
            Preconditions.checkArgument(totalA - totalAfterDelet == 1, "app删除报名人，报名列表没-1");
            crm.login(pp.zongjingli, pp.adminpassword);
            crm.articleStatusChange(id);
            crm.articleDelete(id);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("app活动报名,任务人数于创建时相同，增加报名，任务人数+1");
        }
    }

    /**
     * @description :交车 今日数=列表电话去重数   ok
     * @date :2020/7/31 13:55
     **/
    @Test
    public void jiaocheRecodeApp() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = crm.deliverSelect(1, 100, dt.getHistoryDate(0), dt.getHistoryDate(0));
            JSONObject dataTotal = crm.jiaocheTotal();
            int today_number = dataTotal.getInteger("today_deliver_car_total");
            JSONArray list = data.getJSONArray("list");
            List<String> numList = new ArrayList<>();
            if (list == null || list.size() == 0) {
                logger.info("今日无交车记录");
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                String phone = list.getJSONObject(i).getString("customer_phone_number");
                numList.add(phone);
            }
            Set<String> numSet = new HashSet<>();
            numSet.addAll(numList);
            int similar = numSet.size();
            Preconditions.checkArgument(similar == today_number, "今日交车数！=今日列表电话号码去重数");

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("交车 今日数=列表电话去重数");
        }
    }

    /**
     * @description :试驾 今日数=列表电话去重数  TODO：
     * @date :2020/7/31 13:55
     **/
    @Test
    public void shijiaRecodeApp() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = crm.driverSelect(1, 100, dt.getHistoryDate(0), dt.getHistoryDate(0));
            JSONObject dataTotal = crm.driverTotal();
            int today_number = dataTotal.getInteger("today_test_drive_total");
            JSONArray list = data.getJSONArray("list");
            List<String> numList = new ArrayList<>();
            if (list == null || list.size() == 0) {
                logger.info("今日无试驾记录");
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                String phone = list.getJSONObject(i).getString("customer_phone_number");
                String audit_status_name = list.getJSONObject(i).getString("audit_status_name");
                //TODO:审核通过装态待确认
                if (audit_status_name.equals("已通过")) {
                    numList.add(phone);
                }
            }
            Set<String> numSet = new HashSet<String>();
            numSet.addAll(numList);
            int similar = numSet.size();
            Preconditions.checkArgument(similar == today_number, "试驾 今日数=列表电话去重数");

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("试驾 今日数=列表电话去重数");
        }
    }

    /**
     * @description :新建Dcc线索，Dcc列表总数+1
     * @date :2020/9/11 21:17
     **/

    @Test
    public void CreateDcc() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(pp.dcc, pp.adminpassword);
            int total = crm.dcclist(1, 100).getInteger("total");
            String name = "dcc" + dt.getHHmm(0);
            Random random = new Random();
            String phone = "177" + (random.nextInt(89999999) + 10000000);
//            String plateNum="京K8S123";
            crm.dccCreate(name, phone, "");
            int total2 = crm.dcclist(1, 100).getInteger("total");
            Preconditions.checkArgument(total2 - total == 1, "新建Dcc线索，Dcc列表总数+1");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.zongjingli, pp.adminpassword);
            saveData("新建Dcc线索，Dcc列表总数+1");
        }
    }


    /**
     * @description :新建、注销试驾车，试驾车列表+-1
     * @date :2020/9/10 19:36
     **/

//    @Test
    public void shijiacheNum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = crm.driverCarList();
            int total = data.getInteger("total");
            long id = pf.newCarDriver();    //新建试驾车，获取试驾车id
            JSONObject data2 = crm.driverCarList();
            int total2 = data2.getInteger("total");
            crm.carLogout(id);    //注销试驾车
            JSONObject data3 = crm.driverCarList();
            int total3 = data3.getInteger("total");
            Preconditions.checkArgument(total2 - total == 1, "新增试驾车型，试驾车列表没+1");
            Preconditions.checkArgument(total2 - total3 == 0, "注销试驾车型，试驾车列表不变");

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建、注销试驾车，试驾车列表+-1");
        }
    }

        @Test
    public void shijiacheSame() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(pp.xiaoshouZongjian,pp.adminpassword);
            Random r =new Random();
            Long start = dt.getHistoryDateTimestamp(1);
            long end = dt.getHistoryDateTimestamp(3);
            String carName = "试驾车"+r.nextInt(10) + dt.getHHmm(0);
            String plate_number = "浙Z12Q1" + r.nextInt(100);
            String vehicle_chassis_code = "ASD145656" + (r.nextInt(89999999) + 10000000);

            JSONArray data = crm.driverCarList().getJSONArray("list");
            carName = data.getJSONObject(0).getString("car_name");
            int code = crm.carManagementAddNotChk(carName, 1L, 37L, plate_number, vehicle_chassis_code, start, end).getInteger("code");
            carName = "试驾车"+r.nextInt(10) + dt.getHHmm(0);

            plate_number = pp.samePlate;
            int code2 = crm.carManagementAddNotChk(carName, 1L, 37L, plate_number, vehicle_chassis_code, start, end).getInteger("code");
            plate_number = "浙Z12Q1" + r.nextInt(100);

            vehicle_chassis_code = data.getJSONObject(0).getString("vehicle_chassis_code");
            int code3 = crm.carManagementAddNotChk(carName, 1L, 37L, plate_number, vehicle_chassis_code, start, end).getInteger("code");

            Preconditions.checkArgument(code==1001,"创建试驾车填写重复信息仍成功");
            Preconditions.checkArgument(code2==1001,"创建试驾车填写重复信息仍成功");
            Preconditions.checkArgument(code3==1001,"创建试驾车填写重复信息仍成功");



        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen,pp.xspassword);
            saveData("新建车名、底盘号重复验证");
        }
    }
    //    @Test(description = "新增注销试驾车，新建试驾下拉列表+-1")
    public void shijiacheNum2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.testDriverList().getJSONArray("list");
            int total = list.size();
            long id2 = list.getJSONObject(0).getLong("test_car_id");

            crm.login(pp.xiaoshouZongjian, pp.adminpassword);
            long id = pf.newCarDriver();
            int totalAfterAdd = crm.testDriverList().getJSONArray("list").size();

            crm.carLogout(id);    //注销试驾车
            int totalAfterlogout = crm.testDriverList().getJSONArray("list").size();

            Preconditions.checkArgument(totalAfterAdd - total == 1, "新增试驾车型，试驾车列表没+1" + totalAfterAdd + ";" + total);
            Preconditions.checkArgument(totalAfterAdd - totalAfterlogout == 1, "注销试驾车型，试驾车列表没-1");

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("新建、注销试驾车，新建试驾页：试驾车列表+-1");
        }
    }

   //手机号、现有车型、评估车型、对比车型，身份证长度均由前端控制
    @Test(description = "编辑客户，填写信息长度异常验证")
    public void editCustomerAbnomal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject object = pf.creatCustOld(pp.customer_phone_numberO);
            finishReceive fr = new finishReceive();
            fr.customer_id = object.getString("customerId");
            fr.reception_id = object.getString("reception_id");
            fr.phoneList = object.getJSONArray("phoneList");
            fr.belongs_sale_id = object.getString("sale_id");
            fr.reception_type = "BB";
            fr.checkCode = false;
            String userLoginName = object.getString("userLoginName");
            //姓名51个字
            crm.login(userLoginName, pp.adminpassword);
            fr.name = "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十12";
            int code = crm.editCustomer(fr).getInteger("code");
            fr.name = "编辑客户名@#￥……&*！";
            //备注201个字
            JSONArray remark = new JSONArray();
            JSONObject re = new JSONObject();
            re.put("remark", "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十" +
                    "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十" +
                    "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十" +
                    "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十！@#￥%……&*（）12");
            remark.add(re);
            fr.remark = remark;
            int code2 = crm.editCustomer(fr).getInteger("code");
            fr.remark = new JSONArray();

            crm.finishReception3(fr);
            Preconditions.checkArgument(code == 1001, "编辑客户姓名超过50个字，仍成功");
            Preconditions.checkArgument(code2 == 1001, "编辑备注201个字，仍成功");

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("编辑客户姓名/备注51/201，异常验证");
        }
    }

    //车牌号，异常验证
    @Test(description = "编辑客户，车牌号2，异常验证")
    public void editCarPlateAbnomal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject object = pf.creatCustOld(pp.customer_phone_numberO);
            finishReceive fr = new finishReceive();
            fr.customer_id = object.getString("customerId");
            fr.reception_id = object.getString("reception_id");
            fr.name = "编辑客户";
            fr.phoneList = object.getJSONArray("phoneList");
            fr.belongs_sale_id = object.getString("sale_id");
            fr.reception_type = "BB";
            fr.checkCode = false;
            fr.remark = new JSONArray();
            String userLoginName = object.getString("userLoginName");
            crm.login(userLoginName, pp.adminpassword);

            String[] plateabn = {"苏BJ123", "苏BJ123456", "BJ12345", "京1234567", "京bj12345"}; //6位/9位/无汉字/无大写字母/小写字母
            for (int z = 0; z < plateabn.length; z++) {
                fr.plate_number_two = plateabn[z];
                int code = crm.editCustomer(fr).getInteger("code");
                Preconditions.checkArgument(code == 1001, "编辑客户错误车牌号仍成功" + plateabn[z]);
            }
            //重复车牌集合  TODO:展厅客户 车牌号2没去重
//            String[] plateabn1 = {"冀DFGY12" }; //展厅客户车牌号2
//            for (int z = 0; z < plateabn1.length; z++) {
//                fr.plate_number_two = plateabn1[z];
//                int code = crm.editCustomer(fr).getInteger("code");
//                Preconditions.checkArgument(code == 1001, "编辑客户重复车牌号仍成功" + plateabn1[z]);
//            }
            fr.plate_number_two = "苏BJBJBJ";

            crm.finishReception3(fr);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("编辑客户车牌号，异常验证");
        }
    }

    //车牌号1，重复验证
    @Test(description = "编辑客户，车牌号1，重复验证")
    public void editCarPlateAbnomalsame() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject object = pf.creatCustOld(pp.customer_phone_numberO);
            finishReceive fr = new finishReceive();
            fr.customer_id = object.getString("customerId");
            fr.reception_id = object.getString("reception_id");
            fr.name = "编辑客户";
            fr.phoneList = object.getJSONArray("phoneList");
            fr.belongs_sale_id = object.getString("sale_id");
            fr.reception_type = "BB";
            fr.checkCode = false;
            fr.remark = new JSONArray();
            String userLoginName = object.getString("userLoginName");
            crm.login(userLoginName, pp.adminpassword);
            fr.plate_number_one_id=crm.platepic().getJSONArray("plate_number_list").getJSONObject(0).getString("id");
            String[] plateabn = {pp.samePlate};
            for (int z = 0; z < plateabn.length; z++) {
                fr.plate_number_one = plateabn[z];
                int code = crm.editCustomer(fr).getInteger("code");
                Preconditions.checkArgument(code == 1001, "编辑客户重复车牌号1仍成功" + plateabn[z]);
            }
            fr.plate_number_one_id="";
            fr.plate_number_one = "";

            crm.finishReception3(fr);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("编辑客户，车牌号1，重复验证");
        }
    }
    //车牌号，异常验证
    @Test(description = "编辑客户，重复手机号、异常验证")
    public void editCustomerSamephone() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject object = pf.creatCustOld(pp.customer_phone_numberO);
            finishReceive fr = new finishReceive();
            fr.customer_id = object.getString("customerId");
            fr.reception_id = object.getString("reception_id");
            fr.name = "编辑客户";
            fr.belongs_sale_id = object.getString("sale_id");
            fr.reception_type = "BB";
            fr.checkCode = false;
            fr.remark = new JSONArray();
            String userLoginName = object.getString("userLoginName");
            crm.login(userLoginName, pp.adminpassword);

            String[] phonelist = {pp.customer_phone_number, pp.samephone, pp.linephone, pp.linephone2}; //其他客户联系方式1 /9位/无汉字/无大写字母/小写字母
            for (int z = 0; z < phonelist.length; z++) {
                JSONArray PhoneList = new JSONArray();
                JSONObject phone1 = new JSONObject();
                phone1.put("phone", pp.customer_phone_numberO);
                phone1.put("phone_order", 0);
                JSONObject phone2 = new JSONObject();
                phone2.put("phone", phonelist[z]);
                phone2.put("phone_order", 1);
                PhoneList.add(0, phone1);
                PhoneList.add(1, phone2);
                fr.phoneList = PhoneList;
                int code = crm.editCustomer(fr).getInteger("code");
                Preconditions.checkArgument(code == 1001, "编辑客户重复手机号仍成功" + phonelist[z]);
            }
            //重复车牌集合
            fr.phoneList = object.getJSONArray("phoneList");
            crm.finishReception3(fr);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("编辑客户重复手机号异常验证");
        }
    }
    /**
     * @description :试驾异常验证
     * @date :2020/10/13 19:02
     **/
    @Test
    public void shijiaAb() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject json;
            json = pf.creatCustOld(pp.customer_phone_numberO);
            finishReceive fr = new finishReceive();
            fr.name = "试驾编辑";
            fr.reception_id = json.getString("id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "BB";
            fr.remark = new JSONArray();

            JSONArray list = crm.testDriverList().getJSONArray("list");

            destDriver dd = new destDriver();
            Long test_drive_car = dt.getHistoryDateTimestamp(1);
            for (int i = 0; i < list.size(); i++) {
                dd.test_drive_car = list.getJSONObject(i).getLong("test_car_id");
                JSONArray timelist = crm.driverTimelist(test_drive_car).getJSONArray("list");
                if (timelist.size() != 0) {
                    dd.apply_time = timelist.getString(0);
                    break;
                }
            }
            dd.receptionId = fr.reception_id;
            dd.customer_id = fr.customer_id;
            dd.phone = pp.customer_phone_numberO;
            dd.customerName = pp.abString;   //名字超过50
            dd.checkCode = false;
            int code = crm.driveradd6(dd).getInteger("code");
            dd.customerName = "编辑试驾";

            dd.phone = "159039404921";
            int code2 = crm.driveradd6(dd).getInteger("code");
            dd.phone = pp.customer_phone_numberO;

            dd.address = pp.abString;
            int code3 = crm.driveradd6(dd).getInteger("code");
            dd.address = "东城";
//           //邮箱
//           String email[]={"12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789011@163.com",
//                   "1@qq.com",
//                   "12345Lxsd67890@gmail.com",
//                   "1234567890@baiyahoo.com",
//                   "wertyui@baimsn.com",
//                   "WERTY@hotmail.com",
//                   "_____@aol.com",
//                   "KJHGFYTU@ask.com",
//                   "12KKJ567890@live.com",
//                   "123OOO000@0355.net",
//                   "1234567890@163.net",
//                   "1234567890@263.net",
//                   "1234567890@3721.net",
//                   "2842726905@qq.com",};
//           for(int i=0;i<email.length;i++){
//               dd.email=email[0];
//               int code4 =crm.driveradd6(dd).getInteger("code");
//               Preconditions.checkArgument(code4==1001,"试驾客户地址超过50个字");
//           }
            crm.finishReception3(fr);
            Preconditions.checkArgument(code == 1001, "试驾客户名超过50个字");
            Preconditions.checkArgument(code2 == 1001, "试驾客户手机号超过50个字");
            Preconditions.checkArgument(code3 == 1001, "试驾客户地址超过50个字");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建试驾异常判断");
        }
    }

    //购车-交车底盘号验证
    @Test
    public void jiaocheAb() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Random random = new Random();
            JSONObject json;
            json = pf.creatCustOld(pp.customer_phone_numberO);
            finishReceive fr = new finishReceive();
            fr.name = "交车编辑";
            fr.reception_id = json.getString("id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "BB";
            fr.remark = new JSONArray();
            String vehicle_chassis_code[] = {"12345678901234567", "ASDFGHJKLMNBVCXZA", "ASDFGHJKLmnb12345", "哈SDFGHJKL!@#12345",//纯数字 、纯英文、小写英文、汉字字符
                    "DJFKQWERT1209876","DJFKQWERT120987635",    //长度16.长度18
                    "WKSKSKKSKSKSKK123","ZDHZDHZDH61018911", // 其他客户底盘号、试驾车管理中底盘号
            };

            orderCar oc = new orderCar();
            oc.customer_id = fr.customer_id;
            oc.receptionId = fr.reception_id;
            oc.checkCode = false;
            for (int i = 0; i < vehicle_chassis_code.length; i++) {
                oc.vehicle_chassis_code = vehicle_chassis_code[i];
                int code = crm.addOrderCar1(oc).getInteger("code");
                Preconditions.checkArgument(code == 1001, "底盘号错误");
            }
            oc.vehicle_chassis_code = "ASD123456" + (random.nextInt(89999999) + 10000000);
            oc.checkCode = true;
            Long car_id = crm.addOrderCar1(oc).getLong("car_id");

            Long model = crm.customerOrderCar(fr.customer_id).getJSONArray("list").getJSONObject(0).getLong("car_model_id");
            String path = file.texFile(pp.filePath);
            for (int i = 0; i < vehicle_chassis_code.length; i++) {
                int code = crm.deliverAddcode(car_id, Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, dt.getHistoryDate(0), model, path, false, path, vehicle_chassis_code[i]).getInteger("code");
                Preconditions.checkArgument(code == 1001, "底盘号错误");
            }
            crm.finishReception3(fr);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("购车交车底盘号异常验证");
        }
    }



    /**
     * @description :新建工作计划，列表+1
     * @date :2020/9/10 19:59
     **/
//    @Test
    public void createPlan() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String working_date = dt.getHistoryDate(1);
            String working_time = dt.currentTimeB("HH:mm", 240);
            JSONObject data = crm.workPlanList();
            Long total = data.getLong("total");
            Long customer_id = crm.searchByPhone(pp.customer_phone_number).getLong("customer_id");
            String taskType = crm.taskEnum().getJSONArray("task_enum").getJSONObject(0).getString("task_enum_type");

            String plate_number = "";
            String remark = "工作计划一二三四五六七八九十";
            //新建工作计划
            Long planId = crm.addplan(taskType, customer_id, working_date, working_time, plate_number, remark).getLong("work_plan_id");
            JSONObject data2 = crm.workPlanList();
            Long total2 = data2.getLong("total");
            crm.planCancle(planId);     //取消计划
            Preconditions.checkArgument(total2 - total == 1, "新建工作计划，列表+1");

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建工作计划，列表+1");
        }
    }

}
