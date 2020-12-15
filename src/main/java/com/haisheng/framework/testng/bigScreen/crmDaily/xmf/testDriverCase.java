package com.haisheng.framework.testng.bigScreen.crmDaily.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crmDaily.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crmDaily.commonDs.PackFunction;
import com.haisheng.framework.testng.bigScreen.crmDaily.commonDs.PublicParm;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.enumerator.config.EnumProduce;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.enumerator.customer.EnumAppletCode;
import com.haisheng.framework.testng.bigScreen.crmDaily.xmf.interfaceDemo.destDriver;
import com.haisheng.framework.testng.bigScreen.crmDaily.xmf.interfaceDemo.finishReceive;
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

public class testDriverCase extends TestCaseCommon implements TestCaseStd {
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
        commonConfig.checklistQaOwner = "夏明凤";
        commonConfig.produce = EnumProduce.BSJ.name();

        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getName() + commonConfig.checklistQaOwner);

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
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
    @Test()
    public void driverEvaluate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(pp.xiaoshouZongjian, pp.adminpassword);
            //接待前Guwen，接待次数 13 销售顾问，16保养 ，15维修
            int num = pf.jiedaiTimes(13, pp.xiaoshouGuwen);
            //pc评价页总数
            int total = crm.evaluateList(1, 10, "", "", "").getInteger("total");
            //预约接待完成
            Long appointmentId = pf.driverEva();


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
            int num2 = pf.jiedaiTimes(13, pp.xiaoshouGuwen);
            Preconditions.checkArgument((totalB - total) == 1, "评价后，pc评价列表没+1");
//            Preconditions.checkArgument((num2 - num) == 1, "接待完成，接待次数没+1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("试驾评价 && 完成接待接待次数+1 ；评价完成pc评价列表+1");
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

            Preconditions.checkArgument(totalAfterAdd - total == 1, "新增试驾车型，试驾车列表没+1" + totalAfterAdd + ";" + total);
            Preconditions.checkArgument(totalAfterAdd - totalAfterlogout == 1, "注销试驾车型，试驾车列表没-1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("新建、注销试驾车，新建试驾页：试驾车列表+-1");
        }
    }

    /**
     * @description :
     * @date :2020/9/10 19:36
     **/

    @Test(description = "新建、注销试驾车，试驾车列表+-1")
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
            appendFailReason(e.toString());
        } finally {
            saveData("新建、注销试驾车，试驾车列表+-1");
        }
    }

    /**
     * @description :创建新客试驾审核通过,今日试驾次数+1,总计+1   ok
     * @date :2020/8/10 16:45
     **/
    @Test()
    public void testderver() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //销售总监今日试驾总数
            crm.login(pp.xiaoshouZongjian, pp.adminpassword);
            int driverNum[] = pf.driverSum();         //计数函数

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

            String testcarStyleName = pf.creatDriver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, phone, 1);  //新客试驾
            String isdriver = crm.customerMyReceptionList("", "", "", 10, 1).getJSONArray("list").getJSONObject(0).getString("test_drive_name");

            JSONObject dataTotal2 = crm.driverTotal();
            int today_number2 = dataTotal2.getInteger("today_test_drive_total");
            int totalNum2 = dataTotal2.getInteger("test_drive_total");
            JSONObject dataList2 = crm.driverSelect(1, 10);
            int total2 = dataList2.getInteger("total");

            JSONObject list = dataList2.getJSONArray("list").getJSONObject(0);
            String customer_name = list.getString("customer_name");
            String customer_phone_number = list.getString("customer_phone_number");
            String call = list.getString("call");
            String sign_date = list.getString("sign_date");
            String test_car_style_name = list.getString("test_car_style_name");
            String audit_status_name = list.getString("audit_status_name");

            crm.login(userLoginName, pp.adminpassword);

            crm.finishReception3(fr);
            Preconditions.checkArgument(today_number2 - driverNum[0] == 1, "新建试驾，今日试驾+1，试驾后:" + today_number2 + "试驾前：" + driverNum[0]);
            Preconditions.checkArgument(totalNum2 - driverNum[1] == 1, "新建试驾，总计试驾+1，试驾后：" + totalNum2 + "，试驾前：{}" + driverNum[1]);
            Preconditions.checkArgument(total2 - driverNum[2] == 1, "新建试驾，试驾列表+1，试驾后：" + totalNum2 + "，试驾前：{}" + driverNum[2]);
            Preconditions.checkArgument(sign_date.equals(dt.getHistoryDate(0)), "新建试驾，试驾列表签订日期错误");
            Preconditions.checkArgument(customer_name.equals(fr.name), "新建试驾，试驾列表客户名错误");
            Preconditions.checkArgument(customer_phone_number.equals(phone), "新建试驾，试驾列表电话错误");
            Preconditions.checkArgument(call.equals("先生"), "新建试驾，试驾列表客户称呼错误");
            Preconditions.checkArgument(test_car_style_name.equals(testcarStyleName), "新建试驾，试驾列表客户试驾车系错误");
            Preconditions.checkArgument(audit_status_name.equals("已通过"), "新建试驾，试驾列表审核状态错误");
            Preconditions.checkArgument(isdriver.equals("是"), "新建试驾，接待列表是否试驾不显示 是");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建新客试驾,今日试驾次数+1,总计+1，信息校验");
        }
    }

    @Test()
    public void listStatus() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            crm.editCustomer(fr);

            //pc客户信息
            crm.login(pp.xiaoshouZongjian, pp.adminpassword);
            JSONObject list = crm.customerListPC(phone, 1, 10).getJSONArray("list").getJSONObject(0);
            String is_test = list.getString("is_test");
            String is_order = list.getString("is_order");
            String is_deliver = list.getString("is_deliver");
            //试驾
            crm.login(userLoginName, pp.adminpassword);
            String testcarStyleName = pf.creatDriver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, phone, 1);  //新客试驾

            crm.finishReception3(fr);

            JSONObject listA = crm.customerListPC(phone, 1, 10).getJSONArray("list").getJSONObject(0);
            String is_testA = listA.getString("is_test");
            String is_orderA = listA.getString("is_order");
            String is_deliverA = listA.getString("is_deliver");


            Preconditions.checkArgument(is_testA.equals("true"), "新建试驾，pc客户信息是否试驾没显示 是");
            Preconditions.checkArgument(is_test.equals("false"), "新建试驾前，pc客户信息是否试驾没显示 否");

//            Preconditions.checkArgument(is_orderA.equals("true"), "新建试驾，pc客户信息是否订车没显示 是");
//            Preconditions.checkArgument(is_deliverA.equals("true"), "新建试驾，pc客户信息是否试驾没显示 是");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建新客试驾pc客户信息是否试驾变更是");
        }
    }

    /**
     * @description :创建新客试驾审核拒绝,今日试驾次数+0,总计+0   ok
     * @date :2020/8/10 16:45
     **/
    @Test()
    public void testderverJete() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //销售总监今日试驾总数
            crm.login(pp.xiaoshouZongjian, pp.adminpassword);
            int driverNum[] = pf.driverSum();

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
            pf.creatDriver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, phone, 2);  //新客试驾

            JSONObject dataTotal2 = crm.driverTotal();
            int today_number2 = dataTotal2.getInteger("today_test_drive_total");
            int totalNum2 = dataTotal2.getInteger("test_drive_total");
            JSONObject dataList2 = crm.driverSelect(1, 10);
            int total2 = dataList2.getInteger("total");

            JSONObject list = dataList2.getJSONArray("list").getJSONObject(0);
            String customer_name = list.getString("customer_name");
            String customer_phone_number = list.getString("customer_phone_number");
            String call = list.getString("call");
            String sign_date = list.getString("sign_date");
            String test_car_style_name = list.getString("test_car_style_name");
            String audit_status_name = list.getString("audit_status_name");

            crm.login(userLoginName, pp.adminpassword);

            crm.finishReception3(fr);
            Preconditions.checkArgument(today_number2 - driverNum[0] == 0, "新建试驾，今日试驾+1，试驾后:" + today_number2 + "试驾前：" + driverNum[0]);
            Preconditions.checkArgument(totalNum2 - driverNum[1] == 0, "新建试驾，总计试驾+1，试驾后：" + totalNum2 + "，试驾前：{}" + driverNum[1]);
            Preconditions.checkArgument(total2 - driverNum[2] == 1, "新建试驾，试驾列表+1，试驾后：" + totalNum2 + "，试驾前：{}" + driverNum[2]);
            Preconditions.checkArgument(sign_date.equals(dt.getHistoryDate(0)), "新建试驾，试驾列表签订日期错误");
            Preconditions.checkArgument(customer_name.equals(fr.name), "新建试驾，试驾列表客户名错误");
            Preconditions.checkArgument(customer_phone_number.equals(phone), "新建试驾，试驾列表电话错误");
            Preconditions.checkArgument(call.equals("先生"), "新建试驾，试驾列表客户称呼错误");
//            Preconditions.checkArgument(test_car_style_name.equals(pp.car_type_name), "新建试驾，试驾列表客户试驾车系错误");
            Preconditions.checkArgument(audit_status_name.equals("已拒绝"), "新建试驾，试驾列表审核状态错误");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建新客试驾审批拒绝,今日试驾次数+0,总计+0,列表+1");
        }
    }

    /**
     * @description :创建新客试驾两次审核通过,今日试驾次数+2,总计+2   ok
     * @date :2020/8/10 16:45
     **/
    @Test()
    public void testderverTwo() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //销售总监今日试驾总数
            crm.login(pp.xiaoshouZongjian, pp.adminpassword);
            int driverNum[] = pf.driverSum();

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

            pf.creatDriver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, phone, 1);  //新客试驾
            pf.creatDriver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, phone, 1);  //新客试驾

            int driverNum2[] = pf.driverSum();

            crm.login(userLoginName, pp.adminpassword);

            crm.finishReception3(fr);
            Preconditions.checkArgument(driverNum2[0] - driverNum[0] == 2, "新建试驾两次，今日试驾+1，试驾后:" + driverNum2[0] + "试驾前：" + driverNum[0]);
            Preconditions.checkArgument(driverNum2[1] - driverNum[1] == 2, "新建试驾，总计试驾+1，试驾后：" + driverNum2[1] + "，试驾前：{}" + driverNum[1]);
            Preconditions.checkArgument(driverNum2[2] - driverNum[2] == 2, "新建试驾，试驾列表+2，试驾后：" + driverNum2[2] + "，试驾前：{}" + driverNum[2]);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建新客两次试驾,今日试驾次数+1,总计+1");
        }
    }

    /**
     * @description :创建新客试驾两次审核拒绝,今日试驾次数+0,总计+0   ok
     * @date :2020/8/10 16:45
     **/
    @Test()
    public void testderverJeteTwo() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //销售总监今日试驾总数
            crm.login(pp.xiaoshouZongjian, pp.adminpassword);
            int driverNum[] = pf.driverSum();

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

            pf.creatDriver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, phone, 2);  //新客试驾
            pf.creatDriver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, phone, 2);  //新客试驾

            int driverNum2[] = pf.driverSum();


            crm.login(userLoginName, pp.adminpassword);

            crm.finishReception3(fr);
            Preconditions.checkArgument(driverNum2[0] - driverNum[0] == 0, "新建试驾审批拒绝，今日试驾+0，试驾后:" + driverNum2[0] + "试驾前：" + driverNum[0]);
            Preconditions.checkArgument(driverNum2[1] - driverNum[1] == 0, "新建试驾，总计试驾+0，试驾后：" + driverNum2[1] + "，试驾前：{}" + driverNum[1]);
            Preconditions.checkArgument(driverNum2[2] - driverNum[2] == 2, "新建试驾，试驾列表+2，试驾后：" + driverNum2[2] + "，试驾前：{}" + driverNum[2]);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建新客试驾两次审批拒绝,今日试驾次数+0,总计+0,列表+2");
        }
    }

    //试驾审核页状态验证合集

    /**
     * @description :创建新客试驾审核通过,审批状态变更
     * @date :2020/8/10 16:45
     **/
    @Test()
    public void testderverS() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject object = pf.creatCustOld(pp.customer_phone_numberO);
            finishReceive fr = new finishReceive();
            fr.customer_id = object.getString("customerId");
            fr.reception_id = object.getString("reception_id");
            fr.name = "编辑";
            String phone = pp.customer_phone_numberO;
            fr.phoneList = object.getJSONArray("phoneList");
            fr.belongs_sale_id = object.getString("sale_id");
            fr.reception_type = "BB";
            fr.remark = new JSONArray();
            String userLoginName = object.getString("userLoginName");
            //申请试驾
            pf.creatDriver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, phone, 3);  //新客试驾

            JSONObject dataList = crm.driverSelect(1, 10).getJSONArray("list").getJSONObject(0);
            String audit_status_name = dataList.getString("audit_status_name");
            Long id = dataList.getLong("id");
            //审批通过
            crm.driverAudit(id, 1);
            JSONObject dataList2 = crm.driverSelect(1, 10).getJSONArray("list").getJSONObject(0);
            String audit_status_name2 = dataList2.getString("audit_status_name");

            crm.login(userLoginName, pp.adminpassword);
            crm.finishReception3(fr);
            Preconditions.checkArgument(audit_status_name.equals("审核中"), "申请试驾，审核装态错误");
            Preconditions.checkArgument(audit_status_name2.equals("已通过"), "申请试驾，审核装态错误");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("申请试驾，审批，申请状态由审核中变为已通过");
        }
    }

    @Test()
    public void testderverS2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject object = pf.creatCustOld(pp.customer_phone_numberO);  //创建新客
            finishReceive fr = new finishReceive();
            fr.customer_id = object.getString("customerId");
            fr.reception_id = object.getString("reception_id");
            fr.name = "编辑";
            String phone = pp.customer_phone_numberO;
            fr.phoneList = object.getJSONArray("phoneList");
            fr.belongs_sale_id = object.getString("sale_id");
            fr.reception_type = "BB";
            fr.remark = new JSONArray();
            String userLoginName = object.getString("userLoginName");
            //申请试驾
            pf.creatDriver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, phone, 3);  //新客试驾

            JSONObject dataList = crm.driverSelect(1, 10).getJSONArray("list").getJSONObject(0);
            String audit_status_name = dataList.getString("audit_status_name");
            Long id = dataList.getLong("id");
            //审批通过
            crm.driverAudit(id, 2);
            JSONObject dataList2 = crm.driverSelect(1, 10).getJSONArray("list").getJSONObject(0);
            String audit_status_name2 = dataList2.getString("audit_status_name");

            crm.login(userLoginName, pp.adminpassword);
            crm.finishReception3(fr);
            Preconditions.checkArgument(audit_status_name.equals("审核中"), "申请试驾，审核装态错误");
            Preconditions.checkArgument(audit_status_name2.equals("已拒绝"), "申请试驾，审核装态错误");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("申请试驾，审批拒绝，申请状态由审核中变为已拒绝");
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

            destDriver dd = new destDriver();

            JSONArray list = crm.testDriverList().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                dd.test_drive_car = list.getJSONObject(i).getLong("test_car_id");
                JSONArray timelist = crm.driverTimelist(dd.test_drive_car).getJSONArray("list");
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
            dd.oss = crm.addressDiscernment(dd.driverLicensePhoto1Url).getString("license_face_path");
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
            appendFailReason(e.toString());
        } finally {
            saveData("新建试驾异常判断");
        }
    }

    //申请试驾后，试驾时间段-3，取消+3
    @Test
    public void shijiaTime() {
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
            destDriver dd = new destDriver();
            JSONArray list = crm.testDriverList().getJSONArray("list");
            int driverTimeList = 0;
            for (int i = 0; i < list.size(); i++) {
                dd.test_drive_car = list.getJSONObject(i).getLong("test_car_id");
                JSONArray timelist = crm.driverTimelist(dd.test_drive_car).getJSONArray("list");
                driverTimeList = timelist.size();
                if (driverTimeList != 0) {
                    dd.apply_time = timelist.getString(0);
                    break;
                }
            }
            dd.customerName = fr.name;
            dd.receptionId = fr.reception_id;
            dd.customer_id = fr.customer_id;
            dd.phone = pp.customer_phone_numberO;
            dd.oss = crm.addressDiscernment(dd.driverLicensePhoto1Url).getString("license_face_path");
            crm.driveradd6(dd);       //预约试驾
            Long driverId = crm.driverSelect(1, 10).getJSONArray("list").getJSONObject(0).getLong("id");
            //试驾后，该试驾车时间
            int afterDriver = crm.driverTimelist(dd.test_drive_car).getJSONArray("list").size();
            crm.appdriverCanle(driverId);   //取消试驾
            int afterDriver2 = crm.driverTimelist(dd.test_drive_car).getJSONArray("list").size();

            Preconditions.checkArgument(driverTimeList - afterDriver == 3, "试驾后该车型试驾时间段-3");
            Preconditions.checkArgument(driverTimeList - afterDriver2 == 0, "取消试驾预约后该车型试驾时间段+3");
            crm.finishReception3(fr);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("试驾取消，试驾时间段验证");
        }
    }

    //申请试驾后，审批拒绝-试驾时间段恢复
    @Test
    public void shijiaTime2() {
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
            destDriver dd = new destDriver();
            JSONArray list = crm.testDriverList().getJSONArray("list");
            int driverTimeList = 0;
            for (int i = 0; i < list.size(); i++) {
                dd.test_drive_car = list.getJSONObject(i).getLong("test_car_id");
                JSONArray timelist = crm.driverTimelist(dd.test_drive_car).getJSONArray("list");
                driverTimeList = timelist.size();
                if (driverTimeList != 0) {
                    dd.apply_time = timelist.getString(0);
                    break;
                }
            }
            dd.customerName = fr.name;
            dd.receptionId = fr.reception_id;
            dd.customer_id = fr.customer_id;
            dd.phone = pp.customer_phone_numberO;
            dd.oss = crm.addressDiscernment(dd.driverLicensePhoto1Url).getString("license_face_path");
            crm.driveradd6(dd);

            Long driverId = crm.driverSelect(1, 10).getJSONArray("list").getJSONObject(0).getLong("id");
            //试驾后，该试驾车时间
            int afterDriver = crm.driverTimelist(dd.test_drive_car).getJSONArray("list").size();
            crm.login(pp.xiaoshouZongjian, pp.adminpassword);
            crm.driverAudit(driverId, 2);   //审批  2  拒绝
            int afterDriver2 = crm.driverTimelist(dd.test_drive_car).getJSONArray("list").size();

            Preconditions.checkArgument(driverTimeList - afterDriver == 3, "试驾后该车型试驾时间段-3");
            Preconditions.checkArgument(driverTimeList - afterDriver2 == 0, "取消试驾预约后该车型试驾时间段+3");
            crm.finishReception3(fr);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("试驾审批拒绝，试驾时间段验证");
        }
    }

    //试驾接口校验必填项
    @Test
    public void testdriverE() {
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
            fr.remark = new JSONArray();
            String phone = pp.customer_phone_numberE;

            JSONObject driverCar = pf.cartime();
            destDriver dd = new destDriver();

            dd.test_drive_car = driverCar.getLong("test_car_id");
            dd.apply_time = driverCar.getString("apply_time");

            dd.receptionId = fr.reception_id;
            dd.customer_id = fr.customer_id;
            dd.customerName = "编辑试驾";
            dd.phone = phone;
            dd.checkCode = false;
            dd.oss = crm.addressDiscernment(dd.driverLicensePhoto1Url).getString("license_face_path");
            String must[] = {
                    "address",
                    "customer_id",
                    "customer_name",
                    "customer_phone_number",
                    "car_model",
                    "country",
                    "sign_date",
                    "sign_time",
                    "call",
                    "test_drive_time",
                    "is_fill",
//                    "activity",    //1005
                    "city",
                    "driver_license_photo_2_url",
                    "electronic_contract_url",
                    "driver_license_photo_1_url",
            };
//            FileWriter fileWriter = new FileWriter(pp.textPath,true);
//            BufferedWriter bw = new BufferedWriter(fileWriter);

            for (int i = 0; i < must.length; i++) {
                dd.Empty = must[i];
                JSONObject data = crm.driveradd6(dd);
                int code = data.getInteger("code");
                String message = data.getString("message");
                String content = "参数：{}" + must[i] + "!!!!!!code:{}" + code + "返回" + message + "\n";
                logger.info(content);
//                bw.append(content);
                Preconditions.checkArgument(code == 1001, "试驾必填项" + must[i] + "不填" + code);

            }
//            bw.close();
            crm.finishReception3(fr);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            saveData("试驾必填项校验");
        }
    }

    //申请试驾后，试驾时间段-3，取消+3
//    @Test
    public void shijiaTime99() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject object = pf.creatCustOld(pp.customer_phone_numberO);
            finishReceive fr = new finishReceive();
            fr.customer_id = object.getString("customerId");
            fr.reception_id = object.getString("reception_id");
            fr.name = "编辑";
            String phone = pp.customer_phone_numberO;
            fr.phoneList = object.getJSONArray("phoneList");
            fr.belongs_sale_id = object.getString("sale_id");
            fr.reception_type = "BB";
            String userLoginName = object.getString("userLoginName");
            //申请试驾
            for (int i = 0; i < 70; i++) {
                pf.creatDriver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, phone, 1);  //新客试驾
            }
            crm.finishReception3(fr);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("试驾取消，试驾时间段验证");
        }
    }
}
