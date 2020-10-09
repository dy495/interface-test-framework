package com.haisheng.framework.testng.bigScreen.crm.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.experiment.enumerator.EnumAppletCode;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PackFunction;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicParm;
import com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo.finishReceive;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
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
    @Test(priority = 12)
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
            fr.remark=new JSONArray();
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
     * @description :试驾   ok
     * @date :2020/8/10 16:45
     **/
    @Test(priority = 12)
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
     * @description :创建新客交车,今日交车次数+1,总计+1  ok
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

            JSONObject json = pf.creatCust();
            finishReceive fr = new finishReceive();
            fr.name = pp.customer_name;
            fr.reception_id = json.getString("id");
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

            crm.login(userLoginName, pp.adminpassword);
            crm.finishReception3(fr);
            Preconditions.checkArgument(today_number2 - today_number == 1, "新建交车，今日交车+1，交车后：{}，交车前：{}", today_number2, today_number);
            Preconditions.checkArgument(totalNum2 - totalNum == 1, "新建交车，总计交车+1，交车后：{}，交车前：{}", totalNum2, totalNum);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建新客交车,今日交车次数+1,总计+1");
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
                Preconditions.checkArgument(nameSelect.equals(customer_name), "我的接待按客户名称查询，结果错误");
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
                }
            }
            JSONArray list = crm.deliverSelect(1, 10, customer_name).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String nameSelect = list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument(nameSelect.equals(customer_name), "交车按客户名称查询，结果错误");
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
                }
            }
            JSONArray list = crm.driverSelect(1, 10, customer_name).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String nameSelect = list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument(nameSelect.equals(customer_name), "交车按客户名称查询，结果错误");
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
                String phoneTemp = data.getJSONObject(0).getString("customer_phone");
                if (phoneTemp != null) {
                    customer_phone = phoneTemp;
                }
            }
            JSONArray list = crm.customerSelect(1, 10, customer_name).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String nameSelect = list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument(nameSelect.equals(customer_name), "客户按客户名称查询，结果错误");
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
    @Test
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
    @Test
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
     * @description :新建、注销试驾车，app预约试驾页试驾车列表+-1
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


    @Test(description = "新增注销试驾车，新建试驾下拉列表+-1")
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

            Preconditions.checkArgument(totalAfterAdd - total == 1, "新增试驾车型，试驾车列表没+1");
            Preconditions.checkArgument(totalAfterAdd - totalAfterlogout == 1, "注销试驾车型，试驾车列表没-1");

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("新建、注销试驾车，新建试驾页：试驾车列表+-1");
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
