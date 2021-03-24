package com.haisheng.framework.testng.bigScreen.crm.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.CustomerInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.FileUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;


/**
 * @author : lxq
 * @date :  2020/05/30
 */

public class ThreeDataPage extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    CustomerInfo cstm = new CustomerInfo();
    FileUtil fileUtil = new FileUtil();
    public String data = "data" + dt.getHistoryDate(-1) + ".txt";
    public String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/lxq" + data;

    int service = 0; //累计接待-  日
    int test_drive = 0; //累计试驾
    int deal = 0; //累计成交
    int delivery = 0; //累计交车

    int week_service = 0; //累计接待 --周
    int week_test_drive = 0; //累计试驾
    int week_deal = 0; //累计成交
    int week_delivery = 0; //累计交车

    int month_service = 0; //累计接待-  月
    int month_test_drive = 0; //累计试驾
    int month_deal = 0; //累计成交
    int month_delivery = 0; //累计交车

    int quarter_service = 0; //累计接待-  季
    int quarter_test_drive = 0; //累计试驾
    int quarter_deal = 0; //累计成交
    int quarter_delivery = 0; //累计交车

    int year_service = 0; //累计接待-  年
    int year_test_drive = 0; //累计试驾
    int year_deal = 0; //累计成交
    int year_delivery = 0; //累计交车

    int all_service = 0; //累计接待-  全部
    int all_test_drive = 0; //累计试驾
    int all_deal = 0; //累计成交
    int all_delivery = 0; //累计交车

    //[业务漏斗]

    int clue = 0; //线索
    int creat = 0; //创建线索
    int recp = 0; //接待线索

    int receive = 0; //接待
    int receive_first = 0; //首次
    int receive_visit = 0; //邀约
    int receive_second = 0;//再次

    int testDriver = 0; //试驾
    int testDriver_first = 0; //首次
    int testDriver_visit = 0; //邀约
    int testDriver_second = 0;//再次

    int order = 0;//订单
    int order_first = 0; //首次
    int order_visit = 0; //邀约
    int order_second = 0;//再次

    int funnel_deal = 0; //交车

    String enter_rate = ""; //到店率
    String driver_rate = ""; //试驾率
    String deal_rate = ""; //成交率

    //[车型漏斗]
    int car_clue = 0; //线索
    int car_creat = 0; //创建线索
    int car_recp = 0; //接待线索
    int car_receive = 0; //接待
    int car_testDriver = 0; //试驾
    int car_order = 0; //订单
    int car_funnel_deal = 0;//交车


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
        commonConfig.checklistQaOwner = "吕雪晴";
        commonConfig.product = EnumProduce.BSJ.name();

        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.PORSCHE_DAILY.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = EnumTestProduce.PORSCHE_DAILY.getShopId();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        crm.login(cstm.lxqgw, cstm.pwd);


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


    @Test(priority = 0)
    public void getnum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //4个tab数据 --日
            JSONObject obj1 = crm.shopPannel("DAY", "", "");
            service = obj1.getInteger("service"); //累计接待
            test_drive = obj1.getInteger("test_drive"); //累计试驾
            deal = obj1.getInteger("deal"); //累计成交
            delivery = obj1.getInteger("delivery"); //累计交车

            //4个tab数据 --周
            JSONObject objweek = crm.shopPannel("WEEK", "", "");
            week_service = objweek.getInteger("service"); //累计接待
            week_test_drive = objweek.getInteger("test_drive"); //累计试驾
            week_deal = objweek.getInteger("deal"); //累计成交
            week_delivery = objweek.getInteger("delivery"); //累计交车

            //4个tab数据 --月
            JSONObject objmonth = crm.shopPannel("MONTH", "", "");
            month_service = objmonth.getInteger("service"); //累计接待
            month_test_drive = objmonth.getInteger("test_drive"); //累计试驾
            month_deal = objmonth.getInteger("deal"); //累计成交
            month_delivery = objmonth.getInteger("delivery"); //累计交车

            //4个tab数据 --季
            JSONObject objquarter = crm.shopPannel("QUARTER", "", "");
            quarter_service = objquarter.getInteger("service"); //累计接待
            quarter_test_drive = objquarter.getInteger("test_drive"); //累计试驾
            quarter_deal = objquarter.getInteger("deal"); //累计成交
            quarter_delivery = objquarter.getInteger("delivery"); //累计交车

            //4个tab数据 --年
            JSONObject objyear = crm.shopPannel("YEAR", "", "");
            year_service = objyear.getInteger("service"); //累计接待
            year_test_drive = objyear.getInteger("test_drive"); //累计试驾
            year_deal = objyear.getInteger("deal"); //累计成交
            year_delivery = objyear.getInteger("delivery"); //累计交车

            //4个tab数据 --全部 //？？？

            JSONObject objall = crm.shopPannel("ALL", "", "");
            all_service = objall.getInteger("service"); //累计接待
            all_test_drive = objall.getInteger("test_drive"); //累计试驾
            all_deal = objall.getInteger("deal"); //累计成交
            all_delivery = objall.getInteger("delivery"); //累计交车


            //[业务漏斗]
            JSONArray array = crm.saleFunnel("DAY", "", "").getJSONObject("business").getJSONArray("list");

            JSONObject obj2 = array.getJSONObject(0);
            clue = obj2.getInteger("value"); //线索
            creat = obj2.getJSONArray("detail").getJSONObject(0).getInteger("value"); //创建线索
            recp = obj2.getJSONArray("detail").getJSONObject(1).getInteger("value"); //接待线索

            JSONObject obj3 = array.getJSONObject(1);
            receive = obj3.getInteger("value"); //接待
            receive_first = obj3.getJSONArray("detail").getJSONObject(0).getInteger("value"); //首次
            receive_visit = obj3.getJSONArray("detail").getJSONObject(1).getInteger("value"); //邀约
            receive_second = obj3.getJSONArray("detail").getJSONObject(2).getInteger("value");//再次

            JSONObject obj4 = array.getJSONObject(2);
            testDriver = obj4.getInteger("value"); //试驾
            testDriver_first = obj4.getJSONArray("detail").getJSONObject(0).getInteger("value"); //首次
            testDriver_visit = obj4.getJSONArray("detail").getJSONObject(1).getInteger("value"); //邀约
            testDriver_second = obj4.getJSONArray("detail").getJSONObject(2).getInteger("value");//再次

            JSONObject obj5 = array.getJSONObject(3);
            order = obj5.getInteger("value");//订单
            order_first = obj5.getJSONArray("detail").getJSONObject(0).getInteger("value"); //首次
            order_visit = obj5.getJSONArray("detail").getJSONObject(1).getInteger("value"); //邀约
            order_second = obj5.getJSONArray("detail").getJSONObject(2).getInteger("value");//再次

            JSONObject obj6 = array.getJSONObject(4);
            funnel_deal = obj6.getInteger("value");//交车

            JSONObject busrate = crm.saleFunnel("DAY", "", "").getJSONObject("business");
            enter_rate = busrate.getString("enter_percentage"); //到店率
            driver_rate = busrate.getString("test_drive_percentage"); //试驾率
            deal_rate = busrate.getString("deal_percentage"); //成交率


            //[车型漏斗]
            JSONArray array1 = crm.saleFunnel("DAY", "", "").getJSONObject("car_type").getJSONArray("list");

            JSONObject car_obj2 = array1.getJSONObject(0);
            car_clue = car_obj2.getInteger("value"); //线索
            car_creat = obj2.getJSONArray("detail").getJSONObject(0).getInteger("value"); //创建线索
            car_recp = obj2.getJSONArray("detail").getJSONObject(1).getInteger("value"); //接待线索

            JSONObject car_obj3 = array1.getJSONObject(1);
            car_receive = car_obj3.getInteger("value"); //接待

            JSONObject car_obj4 = array1.getJSONObject(2);
            car_testDriver = car_obj4.getInteger("value"); //试驾

            JSONObject car_obj5 = array1.getJSONObject(3);
            car_order = car_obj5.getInteger("value");//订单

            JSONObject car_obj6 = array1.getJSONObject(4);
            car_funnel_deal = car_obj6.getInteger("value");//交车


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }


    /**
     * --------------------店面数据分析页 页面内一致性-------------------
     */


    @Test(priority = 1)
    public void serviceETdriver() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Preconditions.checkArgument(service >= test_drive, "累计接待" + service + " < " + "累计试驾" + test_drive);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析：累计接待>=累计试驾");
        }
    }

    @Test(priority = 1)
    public void serviceETdeal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(service >= deal, "累计接待" + service + " < " + "累计成交" + deal);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析：累计接待>=累计成交");
        }
    }

    @Test(priority = 1)
    public void dealETdelivery() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(deal >= delivery, "累计成交" + deal + " < " + "累计交车" + delivery);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析：累计成交>=累计交车");
        }
    }


    @Test(priority = 1)
    public void businessClue() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            int all = creat + recp;
            Preconditions.checkArgument(clue == all, "线索" + clue + " != " + "创建线索" + creat + " + 接待线索" + recp);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：线索=创建线索+接待线索");
        }
    }

    @Test(priority = 1)
    public void businessRECEIVE() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int all = receive_first + receive_visit;
            Preconditions.checkArgument(receive == all, "接待" + receive + " != " + "首次" + receive_first + " + 邀约" + receive_visit);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：接待=首次+邀约+再次");
        }
    }

    @Test(priority = 1)
    public void businessTestDriver() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int all = testDriver_first + testDriver_visit + testDriver_second;
            Preconditions.checkArgument(testDriver == all, "试驾" + testDriver + " != " + "首次" + testDriver_first + " + 邀约" + testDriver_visit + " + 再次" + testDriver_second);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：试驾=首次+邀约+再次");
        }
    }

    @Test(priority = 1)
    public void businessOrder() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            int all = order_first + order_visit + order_second;
            Preconditions.checkArgument(order == all, "订单" + order + " != " + "首次" + order_first + " + 邀约" + order_visit + " + 再次" + order_second);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：订单=首次+邀约+再次");
        }
    }


    @Test(priority = 1)
    public void businessReceiveGTDriver() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(receive >= testDriver, "接待" + receive + " < 试驾" + testDriver);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：接待>=试驾");
        }
    }


    @Test(priority = 1)
    public void businessReceiveGTOrder() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(receive >= order, "接待" + receive + " < 订单" + order);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：接待>=订单");
        }
    }

    @Test(priority = 1)
    public void businessOrderGTDeal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(order >= deal, "订单" + order + " < 交车" + deal);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【店面数据分析】业务漏斗：订单>=交车");
        }
    }


    @Test(priority = 1)
    public void businessServiceEQRecSum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int sum = 0;
            JSONArray array = crm.receptTime("DAY", "", null).getJSONArray("list");
            for (int i = 0; i < 5; i++) {
                sum = sum + array.getJSONObject(i).getInteger("value");
            }
            Preconditions.checkArgument(service == sum, "累计接待" + service + " != 各组时长之和" + sum);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("[店面数据分析] 业务漏斗：累计接待=【客户接待时长分析】各时间段组数之和");
        }
    }


    @Test(priority = 1)
    public void businessTestDriverEQFunnel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(test_drive == testDriver, "累计试驾" + test_drive + " != 业务漏斗试驾" + testDriver);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析: 累计试驾=【业务漏斗】试驾");
        }
    }

    @Test(priority = 1)
    public void businessDealEQOrder() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(deal == order, "累计成交" + deal + " != 业务漏斗订单" + order);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析: 累计成交=【业务漏斗】订单");
        }
    }

    @Test(priority = 1)
    public void businessDeliveryEQDeal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(delivery == funnel_deal, "累计交车" + delivery + " != 业务漏斗交车" + funnel_deal);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析: 累计交车=【业务漏斗】交车");
        }
    }


    @Test(priority = 1, dataProvider = "FOUR_TAB")
    public void fourtabChk(String small, String big, String small1, String big1) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int s = Integer.parseInt(small);
            int b = Integer.parseInt(big);
            Preconditions.checkArgument(s <= b, small1 + s + " > " + big1 + b);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析：" + small1 + "<=" + big1);
        }
    }


    @Test(priority = 1)
    public void busniessChkCarClue() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Preconditions.checkArgument(clue == car_clue, "【业务漏斗】线索" + clue + " != 【车型漏斗】线索" + car_clue);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析：【业务漏斗】线索=【车型漏斗】线索");
        }
    }

    @Test(priority = 1, dataProvider = "BUS_CAR")
    public void busniessChkCar(String bus, String car, String bus1, String car1) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int s = Integer.parseInt(bus);
            int b = Integer.parseInt(car);
            Preconditions.checkArgument(s >= b, bus1 + s + " < " + car1 + b);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析：" + bus1 + ">=" + car1);
        }
    }


    @Test(priority = 1, dataProvider = "CAR_FUNNEL")
    public void carFunnelChk(String bus, String car, String bus1, String car1) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int s = Integer.parseInt(bus);
            int b = Integer.parseInt(car);
            Preconditions.checkArgument(s >= b, bus1 + s + " < " + car1 + b);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析-车型漏斗：" + bus1 + ">=" + car1);
        }
    }

    @Test(priority = 1)
    public void carClue() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            int all = car_creat + car_recp;
            Preconditions.checkArgument(car_clue == all, "线索" + car_clue + " != " + "创建线索" + car_creat + " + 接待线索" + car_recp);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析-车型漏斗：线索=创建线索+接待线索");
        }
    }


    @DataProvider(name = "BUS_RATE")
    public Object[][] bus_rate() {
        return new String[][]{
                {Integer.toString(car_receive), Integer.toString(car_clue), "商机PU", "接待线索", "留资率", enter_rate},
                {Integer.toString(car_testDriver), Integer.toString(car_clue), "试驾", "线索", "试驾率", driver_rate},
                {Integer.toString(car_order), Integer.toString(car_clue), "订单", "线索", "成交率", deal_rate}

        };
    }


    @DataProvider(name = "FOUR_TAB")
    public Object[][] four_tab() {
        return new String[][]{
                {Integer.toString(service), Integer.toString(all_service), "【日】累计接待", "【全部】累计接待"},
                {Integer.toString(week_service), Integer.toString(all_service), "【周】累计接待", "【全部】累计接待"},
                {Integer.toString(month_service), Integer.toString(all_service), "【月】累计接待", "【全部】累计接待"},
                {Integer.toString(quarter_service), Integer.toString(all_service), "【季】累计接待", "【全部】累计接待"},

                {Integer.toString(test_drive), Integer.toString(all_test_drive), "【日】累计试驾", "【全部】累计试驾"},
                {Integer.toString(week_test_drive), Integer.toString(all_test_drive), "【周】累计试驾", "【全部】累计试驾"},
                {Integer.toString(month_test_drive), Integer.toString(all_test_drive), "【月】累计试驾", "【全部】累计试驾"},
                {Integer.toString(quarter_test_drive), Integer.toString(all_test_drive), "【季】累计试驾", "【全部】累计试驾"},

                {Integer.toString(deal), Integer.toString(all_deal), "【日】累计成交", "【全部】累计成交"},
                {Integer.toString(week_deal), Integer.toString(all_deal), "【周】累计成交", "【全部】累计成交"},
                {Integer.toString(month_deal), Integer.toString(all_deal), "【月】累计成交", "【全部】累计成交"},
                {Integer.toString(quarter_deal), Integer.toString(all_deal), "【季】累计成交", "【全部】累计成交"},

                {Integer.toString(delivery), Integer.toString(all_delivery), "【日】累计交车", "【全部】累计交车"},
                {Integer.toString(week_delivery), Integer.toString(all_delivery), "【周】累计交车", "【全部】累计交车"},
                {Integer.toString(month_delivery), Integer.toString(all_delivery), "【月】累计交车", "【全部】累计交车"},
                {Integer.toString(quarter_delivery), Integer.toString(all_delivery), "【季】累计交车", "【全部】累计交车"}

        };
    }

    @DataProvider(name = "BUS_CAR")
    public Object[][] Bus_car() {
        return new String[][]{
                {Integer.toString(receive), Integer.toString(car_receive), "【业务漏斗】接待", "【车型漏斗】接待"},
                {Integer.toString(testDriver), Integer.toString(car_testDriver), "【业务漏斗】试驾", "【车型漏斗】试驾"},
                {Integer.toString(order), Integer.toString(car_order), "【业务漏斗】订单", "【车型漏斗】订单"},
                {Integer.toString(funnel_deal), Integer.toString(car_funnel_deal), "【业务漏斗】交车", "【车型漏斗】交车"},
                {Integer.toString(creat), Integer.toString(car_creat), "【业务漏斗】创建线索", "【车型漏斗】创建线索"},
                {Integer.toString(recp), Integer.toString(car_recp), "【业务漏斗】接待线索", "【车型漏斗】接待线索"},

        };
    }

    @DataProvider(name = "CAR_FUNNEL")
    public Object[][] car_funnel() {
        return new String[][]{
                {Integer.toString(car_clue), Integer.toString(car_receive), "线索", "接待"},
                {Integer.toString(car_receive), Integer.toString(car_testDriver), "接待", "试驾"},
                {Integer.toString(car_receive), Integer.toString(car_order), "接待", "订单"},
                {Integer.toString(car_order), Integer.toString(car_funnel_deal), "订单", "交车"}

        };
    }


    /**
     * --------------------潜在客户分析页 页面内一致性-------------------
     */


    @Test(priority = 1)
    public void age100() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray array = crm.customerAge("DAY", "", "", "").getJSONArray("list");
            double a0 = array.getJSONObject(0).getDouble("percentage");
            double a1 = array.getJSONObject(1).getDouble("percentage");
            double a2 = array.getJSONObject(2).getDouble("percentage");
            double a3 = array.getJSONObject(3).getDouble("percentage");
            double a4 = array.getJSONObject(4).getDouble("percentage");
            double a5 = array.getJSONObject(5).getDouble("percentage");
            double all = a0 + a1 + a2 + a3 + a4 + a5;
            Preconditions.checkArgument(all == 1 || all == 0, "百分比之和为" + all);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("潜在客户分析-潜在年龄分布：各年龄段百分比之和=100%或0%");
        }
    }


    @Test(priority = 1)
    public void gender100() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray array = crm.customerGender("DAY", "", "", "").getJSONArray("list");
            double a0 = array.getJSONObject(0).getDouble("percentage");
            double a1 = array.getJSONObject(1).getDouble("percentage");
            double all = a0 + a1;
            Preconditions.checkArgument(all == 1 || all == 0, "百分比之和为" + all);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("潜在客户分析-潜在性别分布：男+女百分比之和=100% 或0%");
        }
    }


    /**
     * --------------------成交客户分析页 页面内一致性-------------------
     */

    @Test
    public void carOwner100() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray array = crm.carOwner("DAY", "", "").getJSONArray("ratio_list");
            double personal = array.getJSONObject(0).getDouble("percent");
            double business = array.getJSONObject(1).getDouble("percent");
            double all = personal + business;
            Preconditions.checkArgument(all == 1 || all == 0, "个人车主" + personal + " + 公司车主" + business + " != 100% 或0%");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("成交客户分析: 个人车主百分比+公司车主百分比=100%或0%");
        }
    }


    @Test
    public void carOwnerAge100() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray array = crm.genderAge("DAY", "", "").getJSONObject("age").getJSONArray("list");
            double sum = 0;
            for (int i = 0; i < array.size(); i++) {
                sum = sum + array.getJSONObject(i).getDouble("percentage");
            }
            Preconditions.checkArgument(sum == 1 || sum == 0, "总和为" + sum);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("[成交客户分析] 车主年龄分析 各年龄段之和=100% 或0%");
        }
    }

    @Test
    public void carOwnerGender100() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray array = crm.genderAge("DAY", "", "").getJSONObject("gender").getJSONArray("list");
            double sum = 0;
            for (int i = 0; i < array.size(); i++) {
                sum = sum + array.getJSONObject(i).getDouble("percentage");
            }
            Preconditions.checkArgument(sum == 1 || sum == 0, "总和为" + sum);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("[成交客户分析] 车主性别分析 性别之和=100% 或0%");
        }
    }

    @Test
    public void wholeCountry100() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray array = crm.wholeCountry("DAY", "", "").getJSONArray("list");
            double sum = 0;
            for (int i = 0; i < array.size(); i++) {
                sum = sum + array.getJSONObject(i).getDouble("percentage");
            }
            double abs = 1 - sum;
            Preconditions.checkArgument(Math.abs(abs) <= 1 || Math.abs(abs) == 0, "总和为" + sum);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("[成交客户分析] 全国各省成交量 成交量百分比之和=100% 或0%");
        }
    }


}
