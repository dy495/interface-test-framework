package com.haisheng.framework.testng.bigScreen.crm.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PackFunction;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicParm;
<<<<<<< Updated upstream
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletCode;
=======
import com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo.deliverCar;
>>>>>>> Stashed changes
import com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo.finishReceive;
import com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo.orderCar;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
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
    Random random = new Random();

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
            Preconditions.checkArgument((num2 - num) == 1, "接待完成，接待次数没+1");

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
     * @description :创建新客交车,今日交车次数+1,总计+1 列表数+1,数据校验
     * @date :2020/8/10 16:45
     **/
    @Test(priority = 12)
    public void testdeliver() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //销售总监今日试驾总数
            crm.login(pp.xiaoshouZongjian, pp.adminpassword);

            int num[] = pf.deliverSum();

            JSONObject json = pf.creatCust();
            finishReceive fr = new finishReceive();
            fr.name = "新客交车数据校验";
            fr.reception_id = json.getString("reception_id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "BB";

            String phone = json.getString("phone");

            pf.creatDeliver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, dt.getHistoryDate(0), true);
            crm.finishReception3(fr);

            crm.login(pp.xiaoshouZongjian, pp.adminpassword);
            JSONObject dataTotal2 = crm.jiaocheTotal();
            int today_number2 = dataTotal2.getInteger("today_deliver_car_total");
            int totalNum2 = dataTotal2.getInteger("deliver_car_total");

            JSONObject data = crm.deliverSelect(1, 10);
            int listtotal2 = data.getInteger("total");
            JSONObject list = data.getJSONArray("list").getJSONObject(0);
            String deliver_car_time = list.getString("deliver_car_time");
            String customer_name = list.getString("customer_name");
            String customer_phone_number = list.getString("customer_phone_number");


            Preconditions.checkArgument(today_number2 - num[0] == 1, "新建交车，今日交车+1，交车后：{}，交车前：{}", today_number2, num[0]);
            Preconditions.checkArgument(totalNum2 - num[1] == 1, "新建交车，总计交车+1，交车后：{}，交车前：{}", totalNum2, num[1]);
            Preconditions.checkArgument(listtotal2 - num[3] == 1, "新建交车，列表数+1，交车后：{}，交车前：{}", listtotal2, num[3]);
            Preconditions.checkArgument(deliver_car_time.equals(dt.getHistoryDate(0)), "新建交车，列表中交车时间错误");
            Preconditions.checkArgument(customer_name.equals(fr.name), "新建交车，列表中交客户名错误");
            Preconditions.checkArgument(customer_phone_number.equals(phone), "新建交车，列表中交客户电话错误");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("创建新客交车,今日交车次数+1,总计+1，列表数+1");
        }
    }


    /**
     * @description :创建新客交车,数据统计 ;重复手机号创建交车-全部交车+1，实际交车+0，今日交车+0
     * @date :2020/8/10 16:45
     **/
    @Test()
    public void testdeliverstatis() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(pp.qiantai, pp.qtpassword);
            String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
            String userLoginName1 = pf.username(sale_id);
            crm.login(userLoginName1, pp.adminpassword);

            int num[] = pf.deliverSum();

            JSONObject json = pf.creatCust();
            finishReceive fr = new finishReceive();
            fr.name = "新客交车数据校验";
            fr.reception_id = json.getString("reception_id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "FU";

            String userLoginName = json.getString("userLoginName");
            //-------第一次交车
            pf.creatDeliver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, dt.getHistoryDate(0), false);
            int afternum2[] = pf.deliverSum();

            //--------第二次交车
            pf.creatDeliver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, dt.getHistoryDate(0), false);

            int afternum3[] = pf.deliverSum();


            crm.login(userLoginName, pp.adminpassword);
            crm.finishReception3(fr);
            Preconditions.checkArgument(afternum2[2] - num[2] == 1, "购车全部交车+0");
            Preconditions.checkArgument(afternum2[1] - num[1] == 1, "购车实际交车+0");
            Preconditions.checkArgument(afternum2[0] - num[0] == 1, "购车今日交车+0");

            Preconditions.checkArgument(afternum3[2] - num[2] == 2, "交车全部交车+2");
            Preconditions.checkArgument(afternum3[1] - num[1] == 1, "购车实际交车+1");
            Preconditions.checkArgument(afternum3[0] - num[0] == 1, "购车今日交车+1");

            Preconditions.checkArgument(afternum3[3] - num[3] == 2, "新建交车，列表数+2", afternum3[3], num[3]);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建新客交车数据统计");
        }
    }

    /**
     * @description :创建新客交车,购车 全部交车+1，实际交车+0，今日交车+0；交车 全部交车+0，实际交车+1，今日交车+1；bug*,需求于实现不符
     * @date :2020/10/19 14:11
     **/
//        @Test(priority = 2)
    public void StestdeliverstatisB() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject json = pf.creatCust();
            finishReceive fr = new finishReceive();
            fr.name = json.getString("name");
            fr.reception_id = json.getString("reception_id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "FU";

            String userLoginName = json.getString("userLoginName");
            crm.login(userLoginName, pp.adminpassword);

            int num[] = pf.deliverSum();

            String vehicle_chassis_code = "ASD123456" + (random.nextInt(89999999) + 10000000);
            Long car_id = crm.addOrderCar(fr.customer_id, fr.reception_id, vehicle_chassis_code).getLong("car_id");

            int beforedeliver[] = pf.deliverSum();

            Long model = crm.customerOrderCar(fr.customer_id).getJSONArray("list").getJSONObject(0).getLong("car_model_id");
            String path = file.texFile(pp.filePath);
            crm.deliverAdd(car_id, Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, dt.getHistoryDate(0), model, path, true, path, vehicle_chassis_code);

            int afterdeliver[] = pf.deliverSum();


            crm.login(userLoginName, pp.adminpassword);
            crm.finishReception3(fr);
            Preconditions.checkArgument(beforedeliver[2] - num[2] == 1, "购车全部交车+1");  //TODO:此处有bug
            Preconditions.checkArgument(beforedeliver[1] - num[1] == 0, "购车实际交车+0");
            Preconditions.checkArgument(beforedeliver[0] - num[0] == 0, "购车今日交车+0");

            Preconditions.checkArgument(afterdeliver[2] - num[2] == 0, "交车全部交车+0");
            Preconditions.checkArgument(afterdeliver[1] - num[1] == 1, "购车实际交车+1");
            Preconditions.checkArgument(afterdeliver[0] - num[0] == 1, "购车今日交车+1");

            Preconditions.checkArgument(afterdeliver[3] - num[3] == 1, "新建交车，列表数+1");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("重复手机号创建新客交车数据统计");
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
//    @Test(dataProvider = "SELECT_DATE", dataProviderClass = CrmScenarioUtil.class)
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
//    @Test()
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
            JSONObject object = pf.creatCustOld(pp.customer_phone_numberO);
            finishReceive fr = new finishReceive();
            fr.customer_id = object.getString("customerId");
            fr.reception_id = object.getString("reception_id");
            fr.name = "编辑";
            String phone = object.getString("phone");
            fr.phoneList = object.getJSONArray("phoneList");
            fr.belongs_sale_id = object.getString("sale_id");
            fr.reception_type = "BB";
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
            crm.deleteCustomer(String.valueOf(activityTaskId), String.valueOf(customerId));

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


    @Test
    public void shijiacheSame() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(pp.xiaoshouZongjian, pp.adminpassword);
            Random r = new Random();
            Long start = dt.getHistoryDateTimestamp(1);
            long end = dt.getHistoryDateTimestamp(3);
            String carName = "试驾车" + r.nextInt(10) + dt.getHHmm(0);
            String plate_number = "浙Z12Q1" + r.nextInt(100);
            String vehicle_chassis_code = "ASD145656" + (r.nextInt(89999999) + 10000000);

            JSONArray data = crm.driverCarList().getJSONArray("list");
            carName = data.getJSONObject(0).getString("car_name");
            int code = crm.carManagementAddNotChk(carName, 1L, 37L, plate_number, vehicle_chassis_code, start, end).getInteger("code");
            carName = "试驾车" + r.nextInt(10) + dt.getHHmm(0);

            plate_number = pp.samePlate;
            int code2 = crm.carManagementAddNotChk(carName, 1L, 37L, plate_number, vehicle_chassis_code, start, end).getInteger("code");
            plate_number = "浙Z12Q1" + r.nextInt(100);

            vehicle_chassis_code = data.getJSONObject(0).getString("vehicle_chassis_code");
            int code3 = crm.carManagementAddNotChk(carName, 1L, 37L, plate_number, vehicle_chassis_code, start, end).getInteger("code");

            Preconditions.checkArgument(code == 1001, "创建试驾车填写重复信息仍成功");
            Preconditions.checkArgument(code2 == 1001, "创建试驾车填写重复信息仍成功");
            Preconditions.checkArgument(code3 == 1001, "创建试驾车填写重复信息仍成功");


        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.xspassword);
            saveData("新建车名、底盘号重复验证");
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
            fr.plate_number_one_id = crm.platepic().getJSONArray("plate_number_list").getJSONObject(0).getString("id");
            String[] plateabn = {pp.samePlate};
            for (int z = 0; z < plateabn.length; z++) {
                fr.plate_number_one = plateabn[z];
                int code = crm.editCustomer(fr).getInteger("code");
                Preconditions.checkArgument(code == 1001, "编辑客户重复车牌号1仍成功" + plateabn[z]);
            }
            fr.plate_number_one_id = "";
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

    @Test(description = "编辑新客户，于接待列表一致")
    public void editCustomerReceiptList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject object = pf.creatCust();
            finishReceive fr = new finishReceive();
            fr.customer_id = object.getString("customerId");
            fr.reception_id = object.getString("reception_id");
            fr.phoneList = object.getJSONArray("phoneList");
            fr.belongs_sale_id = object.getString("sale_id");
            fr.reception_type = "FU";
            String userLoginName = object.getString("userLoginName");
            crm.login(userLoginName, pp.adminpassword);
            fr.name = object.getString("name");
            crm.editCustomer(fr);
            JSONObject data = crm.customerMyReceptionList("", "", "", 10, 1).getJSONArray("list").getJSONObject(0);
            String service_status_name = data.getString("service_status_name");
            String reception_date = data.getString("reception_date");
            String intention_car_style_name = data.getString("intention_car_style_name");
            String customer_name = data.getString("customer_name");

            crm.finishReception3(fr);
            Preconditions.checkArgument(customer_name.equals(fr.name), "我的接待-编辑客户列表接待状态显示错误");
            Preconditions.checkArgument(service_status_name.equals("接待中"), "我的接待-编辑客户列表接待状态显示错误");
            Preconditions.checkArgument(reception_date.equals(dt.getHistoryDate(0)), "我的接待-编辑客户列表接待日期显示错误");
            Preconditions.checkArgument(intention_car_style_name.equals(pp.car_type_name), "我的接待-编辑客户列表意向车型显示错误");

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("编辑客户，于接待列表一致");
        }
    }

    //老客创建购车，购车档案+1
    @Test
    public void buyCarList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject json = pf.creatCustOld(pp.customer_phone_numberO);
            finishReceive fr = new finishReceive();
            fr.name = "编辑";
            fr.reception_id = json.getString("reception_id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "BB";
            int buyCatTotal = crm.buyCarList(fr.customer_id).getJSONArray("list").size();

            orderCar oc = new orderCar();
            oc.customerName = "交车编辑";
            oc.customer_id = fr.customer_id;
            oc.receptionId = fr.reception_id;
            Long car_id = crm.addOrderCar1(oc).getLong("car_id");
            oc.vehicle_chassis_code = "ASD123456" + (random.nextInt(89999999) + 10000000);
            Long model = crm.customerOrderCar(fr.customer_id).getJSONArray("list").getJSONObject(0).getLong("car_model_id");
            String path = file.texFile(pp.filePath);
            crm.deliverAddcode(car_id, Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, dt.getHistoryDate(0), model, path, false, path, oc.vehicle_chassis_code);

            //接待列表-是否订车变为 是
            JSONObject tempList = crm.customerMyReceptionList("", "", "", 10, 1).getJSONArray("list").getJSONObject(0);
            String isorderCar = tempList.getString("book_car_name");
            String customer_level_name = tempList.getString("customer_level_name");

            JSONArray buyCarL = crm.buyCarList(fr.customer_id).getJSONArray("list");
            int buyCatTotal2 = buyCarL.size();

            String buy_time = buyCarL.getJSONObject(0).getString("buy_time");
            String deliver_time = buyCarL.getJSONObject(0).getString("deliver_time");
            String vehicle_chassis_code = buyCarL.getJSONObject(0).getString("vehicle_chassis_code");
            String car_style_name = buyCarL.getJSONObject(0).getString("car_style_name");
            String plate_type_name = buyCarL.getJSONObject(0).getString("plate_type_name");
            String pay_type_name = buyCarL.getJSONObject(0).getString("pay_type_name");
            String defray_type_name = buyCarL.getJSONObject(0).getString("defray_type_name");
            crm.finishReception3(fr);
            Preconditions.checkArgument(buyCatTotal2 - buyCatTotal == 1, "新增购车-购车档按+1");
            Preconditions.checkArgument(buy_time.equals(dt.getHistoryDate(0)), "新增购车-购车档购车时间显示错误");
            Preconditions.checkArgument(deliver_time.equals(dt.getHistoryDate(0)), "新增购车-购车档交车时间显示错误");
            Preconditions.checkArgument(vehicle_chassis_code.equals(oc.vehicle_chassis_code), "新增购车-购车档底盘号显示错误");
            Preconditions.checkArgument(car_style_name.equals(pp.car_type_name), "新增购车-购车档购买车系名显示错误");
            Preconditions.checkArgument(plate_type_name.equals("蓝牌"), "新增购车-购车牌照显示错误");
            Preconditions.checkArgument(pay_type_name.equals("分期"), "新增购车-购车档支付方式显示错误");
            Preconditions.checkArgument(defray_type_name.equals("全款"), "新增购车-购车档本次支付显示错误");
            Preconditions.checkArgument(isorderCar.equals("是"), "新增购车-接待列表变成是");
            Preconditions.checkArgument(customer_level_name.equals("D"), "新增购车-接待列表等级 D");


        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建购车，购车档案+1&信息校验");
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
                    "DJFKQWERT1209876", "DJFKQWERT120987635",    //长度16.长度18
                    "WKSKSKKSKSKSKK123", "ZDHZDHZDH61018911", // 其他客户底盘号、试驾车管理中底盘号
            };

            orderCar oc = new orderCar();
            oc.customerName = "交车编辑";
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
    //完成接待接口校验必填项
    @Test()
    public void jiedaiCheckList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //必填项数组集合
            String mast[]={"customer_id",
                    "belongs_sale_id",
                    "name",
                    "subjectType",
                    "call",
                    "intention_car_model",
                    "expected_buy_day",
                    "pay_type",
                    "visit_count_type",
                    "is_offer",
                    "buy_car_type",
                    "power_type",
                    "is_assessed",};

            JSONObject json = pf.creatCustOld(pp.customer_phone_numberE);
            finishReceive fr = new finishReceive();
            fr.name = "必填参数空";
            fr.reception_id = json.getString("reception_id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "BB";
            fr.checkCode=false;
            for(int i=0;i<mast.length;i++) {
                String temp = mast[i];
                fr.Empty = temp;
                int code = crm.finishReception3(fr).getInteger("code");
                Preconditions.checkArgument(code == 1001, "参数{}不填" + mast[i] + "，code{}" + code);
            }
            fr.Empty="";
            fr.remark = new JSONArray();
            crm.finishReception3(fr);

        } catch (AssertionError |Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("完成接待接口必填项不填参数校验");
        }
    }

    //购车交车校验必填项
    @Test
    public void testOrderCarE() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject json = pf.creatCustOld(pp.customer_phone_numberE);
            finishReceive fr = new finishReceive();
            fr.name = "校验必填项";
            fr.reception_id = json.getString("reception_id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "BB";

            orderCar oc = new orderCar();
            oc.customerName = "交车编辑";
            oc.customer_id = fr.customer_id;
            oc.receptionId = fr.reception_id;
            oc.checkCode=false;
            String must[]={"receptionId",
                    "customer_id",
                    "car_model_id",
                    "car_style_id",
                    "defray_type",
                    "pay_type",
                    "plate_type",};
            for(int i=0;i<must.length;i++){
                oc.Empty=must[i];
                int code=crm.addOrderCar1(oc).getInteger("code");
                Preconditions.checkArgument(code==1001,"不参必填参数"+must[i]+"返回"+code);
            }
            fr.remark = new JSONArray();
            crm.finishReception3(fr);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("购车必填项校验");
        }
    }
    //交车校验必填项
//    @Test
    public void testdeliverE() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject json = pf.creatCustOld(pp.customer_phone_numberE);
            finishReceive fr = new finishReceive();
            fr.name = "校验必填项";
            fr.reception_id = json.getString("reception_id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "BB";

            orderCar oc = new orderCar();
            oc.customerName = "交车编辑";
            oc.customer_id = fr.customer_id;
            oc.receptionId = fr.reception_id;

//            Long car_id = crm.addOrderCar1(oc).getLong("car_id");
            Long car_id = crm.customerOrderCar(fr.customer_id).getJSONArray("list").getJSONObject(0).getLong("id");

            deliverCar dc=new deliverCar();
            dc.customer_name="交车编辑";
            dc.reception_id=fr.reception_id;
            dc.customer_id=fr.customer_id;
            dc.car_id=car_id.toString();
            dc.checkCode=false;
            //必填项合集
            String must[]={
//                    "car_id",
//                    "customer_id",
//                    "customer_name",
//                    "deliver_car_time",
//                    "img_file",   //1001
//                    "accept_show",
                    "Sign_name_url",
//                    "vehicle_chassis_code",
                    "likes",
                    "works",
                    "greeting",
                    "reception_id",
            };
            FileWriter fileWriter = new FileWriter(pp.textPath,true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            for(int i=0;i<must.length;i++){
                String temp=must[i];
                dc.Empty=temp;
                JSONObject data=crm.deliverAddDX(dc);
                int code=data.getInteger("code");
                String  message=data.getString("message");
                String content="参数：{}"+must[i]+"!!!!!!code:{}"+code+"返回"+message+"\n";
                bw.append(content);
//            Preconditions.checkArgument(code==1001,"交车必填项校验"+must[i]+code);
            }
            bw.close();
            fr.remark = new JSONArray();
            crm.finishReception3(fr);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("交车车必填项校验");
        }
    }

    //交车不授权，原授权必填项不填
    @Test
    public void testdeliverEN() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject json = pf.creatCustOld(pp.customer_phone_numberE);
            finishReceive fr = new finishReceive();
            fr.name = "校验必填项";
            fr.reception_id = json.getString("reception_id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "BB";

            orderCar oc = new orderCar();
            oc.customerName = "交车编辑";
            oc.customer_id = fr.customer_id;
            oc.receptionId = fr.reception_id;

            Long car_id = crm.addOrderCar1(oc).getLong("car_id");
            deliverCar dc=new deliverCar();
            dc.customer_name="必填";
            dc.reception_id=fr.reception_id;
            dc.customer_id=fr.customer_id;
            dc.car_id=car_id.toString();
            dc.accept_show=false;
            dc.likes=new JSONArray();
            dc.works=new JSONArray();
            dc.sign_name_url="";
            crm.deliverAddDX(dc);

            fr.remark = new JSONArray();
            crm.finishReception3(fr);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("交车不授权，原授权必填项不填");
        }
    }

    @Test
    public void deliverCar() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject json = pf.creatCust();
            finishReceive fr = new finishReceive();
            fr.name =json.getString("name");
            fr.reception_id = json.getString("reception_id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "FU";

            orderCar oc = new orderCar();
            oc.customerName = fr.name ;
            oc.customer_id = fr.customer_id;
            oc.receptionId = fr.reception_id;

            Long car_id = crm.addOrderCar1(oc).getLong("car_id");  //购车
//            Long car_id = crm.customerOrderCar(fr.customer_id).getJSONArray("list").getJSONObject(0).getLong("id");
            String customer_level_name=crm.customerMyReceptionList("","","",10,1).getJSONArray("list").getJSONObject(0).getString("customer_level_name");

            deliverCar dc=new deliverCar();
            dc.customer_name= fr.name;
            dc.reception_id=fr.reception_id;
            dc.customer_id=fr.customer_id;
            dc.car_id=car_id.toString();

            crm.deliverAddDX(dc);    //交车
            String customer_level_name2=crm.customerMyReceptionList("","","",10,1).getJSONArray("list").getJSONObject(0).getString("customer_level_name");

            Preconditions.checkArgument(customer_level_name.equals("O"),"新建客户购车，客户等级变化");
            Preconditions.checkArgument(customer_level_name2.equals("D"),"新建客户购车，客户等级变化");
            crm.finishReception3(fr);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("交车车必填项校验");
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
