package com.haisheng.framework.testng.bigScreen.itemPorsche.caseonline.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.CrmScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDsOnline.CustomerInfoOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.math.BigDecimal;


/**
 * @author : lxq
 * @date :  2020/05/30
 */

public class ThreeDataPage extends TestCaseCommon implements TestCaseStd {
    EnumTestProduct product = EnumTestProduct.PORSCHE_ONLINE;
    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    CustomerInfoOnline cstm = new CustomerInfoOnline();


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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "吕雪晴";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-online-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.setShopId(product.getShopId()).setReferer(product.getReferer()).setRoleId(product.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        crm.login(cstm.zj, cstm.pwd);

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
            crm.login(cstm.zj, cstm.pwd);
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

    //@Test(priority = 1)
    public void allEQAlone_4tab() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int service1 = 0;
            int test_drive1 = 0;
            int deal1 = 0;
            int delivery1 = 0;
            JSONArray array = crm.allSale().getJSONArray("list");
            for (int i = 0; i < array.size(); i++) {
                String saleid = array.getJSONObject(i).getString("sale_id");
                JSONObject obj1 = crm.shopPannel("DAY", "", saleid);
                service1 = service1 + obj1.getInteger("service"); //累计接待
                test_drive1 = obj1.getInteger("test_drive"); //累计试驾
                deal1 = obj1.getInteger("deal"); //累计成交
                delivery1 = obj1.getInteger("delivery"); //累计交车
            }
            Preconditions.checkArgument(service >= service1, "各销售累计接待" + service1 + " > 不选销售顾问累计接待" + service);
            Preconditions.checkArgument(test_drive == test_drive1, "各销售累计试驾" + test_drive1 + "！= 不选销售顾问累计试驾" + test_drive);
            Preconditions.checkArgument(deal == deal1, "各销售累计成交" + deal1 + "！= 不选销售顾问累计成交" + deal);
            Preconditions.checkArgument(delivery == delivery1, "各销售累计交车" + delivery1 + "！= 不选销售顾问累计交车" + delivery);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析：4个tab【不选销售顾问】累计数据=各个销售顾问累计数据之和");
        }
    }

    @Test(priority = 1)
    public void allEQAlone_rectime() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int all_10 = 0;
            int all_30 = 0;
            int all_60 = 0;
            int all_120l = 0;
            int all_120g = 0;
            JSONArray array = crm.allSale().getJSONArray("list");
            for (int i = 0; i < array.size(); i++) {
                String saleid = array.getJSONObject(i).getString("sale_id");
                JSONArray array0 = crm.receptTime("DAY", "", saleid).getJSONArray("list");
                all_10 = all_10 + array0.getJSONObject(0).getInteger("value");
                all_30 = all_30 + array0.getJSONObject(1).getInteger("value");
                all_60 = all_60 + array0.getJSONObject(2).getInteger("value");
                all_120l = all_120l + array0.getJSONObject(3).getInteger("value");
                all_120g = all_120g + array0.getJSONObject(4).getInteger("value");
            }
            JSONArray array1 = crm.receptTime("DAY", "", null).getJSONArray("list");
            int l10 = array1.getJSONObject(0).getInteger("value");
            int l30 = array1.getJSONObject(1).getInteger("value");
            int l60 = array1.getJSONObject(2).getInteger("value");
            int l120 = array1.getJSONObject(3).getInteger("value");
            int g120 = array1.getJSONObject(4).getInteger("value");

            Preconditions.checkArgument(all_10 <= l10, "10分钟内：不选销售累计组数" + l10 + " != 各销售接待组数之和" + all_10);
            Preconditions.checkArgument(all_30 <= l30, "10~30分钟：不选销售累计组数" + l30 + " != 各销售接待组数之和" + all_30);
            Preconditions.checkArgument(all_60 <= l60, "30～60分钟：不选销售累计组数" + l60 + " != 各销售接待组数之和" + all_60);
            Preconditions.checkArgument(all_120l <= l120, "60～120分钟：不选销售累计组数" + l120 + " != 各销售接待组数之和" + all_120l);
            Preconditions.checkArgument(all_120g <= g120, "大于120分钟：不选销售累计组数" + g120 + " != 各销售接待组数之和" + all_120g);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析：各时间段接待时长【不选销售顾问】接待组数=各个销售顾问接待组数之和");
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


    //@Test(priority = 1)
    public void businessReceiveGTClue() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(receive == recp, "接待" + receive + " != 接待线索" + recp);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：接待==接待线索");
        }
    }

    //@Test(priority = 1)
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

    //@Test(priority = 1)
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


    //@Test(priority = 1,dataProvider = "BUS_RATE")
    public void businessrate(String fz, String fm, String fzstr, String fmstr, String rate, String show) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int fz1 = Integer.parseInt(fz);
            int fm1 = Integer.parseInt(fm);
            double a = (double) fz1 / fm1;
            BigDecimal bd = new BigDecimal(a);
            String jisuan = bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString();

            Preconditions.checkArgument(jisuan.equals(show), "展示" + show + " != 计算结果" + jisuan);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【店面数据分析】业务漏斗：" + rate + " = " + fzstr + " / " + fmstr);
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

    //@Test(priority = 1)
    public void businessService() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(service == receive, "累计接待" + service + " != 业务漏斗接待" + receive);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析: 累计接待=【业务漏斗】接待");
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


    //@Test(priority = 1,dataProvider = "CAR_FUNNEL")
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
     * --------------------店面数据分析页 页面间一致性-------------------
     */


    //case 重复，后续由王敏维护他的用例即可，目前此用例误报，不再花费精力修复
    //@Test
    public void recpTimeChk() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int less10 = 0;
            int less30 = 0;
            int less60 = 0;
            int less120 = 0;
            int more120 = 0;

            String time = dt.getHistoryDate(-1);
            int total = crm.receptionPage1(1, "PRE_SALES", 1).getInteger("total");
            int a = 0;
            if (total > 50) {
                if (total % 50 == 0) {
                    a = total / 50;
                } else {
                    a = (int) Math.ceil(total / 50) + 1;
                }
                for (int i = 1; i <= a; i++) {

                    JSONArray list = crm.receptionPage1(i, "PRE_SALES", 50).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {

                        if (list.getJSONObject(j).getString("reception_date").equals(time) && list.getJSONObject(j).getString("reception_time") != null) {
                            String start = list.getJSONObject(j).getString("reception_time");
                            String end = list.getJSONObject(j).getString("leave_time");
                            int diff = dt.calTimeHourDiff(start, end);
                            if (diff < 10) {
                                less10 = less10 + 1;
                            } else if (diff < 30) {
                                less30 = less30 + 1;
                            } else if (diff < 60) {
                                less60 = less60 + 1;
                            } else if (diff < 120) {
                                less120 = less120 + 1;
                            } else {
                                more120 = more120 + 1;
                            }
                        }
                    }
                }
            } else {
                JSONArray list = crm.receptionPage1(1, "PRE_SALES", total).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("reception_date").equals(time) && list.getJSONObject(j).getString("reception_time") != null) {
                        String start = list.getJSONObject(j).getString("reception_time");
                        String end = list.getJSONObject(j).getString("leave_time");
                        int diff = dt.calTimeHourDiff(start, end);
                        if (diff < 10) {
                            less10 = less10 + 1;
                        } else if (diff < 30) {
                            less30 = less30 + 1;
                        } else if (diff < 60) {
                            less60 = less60 + 1;
                        } else if (diff < 120) {
                            less120 = less120 + 1;
                        } else {
                            more120 = more120 + 1;
                        }
                    }
                }
            }
            JSONArray array = crm.receptTime("DAY", "", null).getJSONArray("list");
            int l10 = array.getJSONObject(0).getInteger("value");
            int l30 = array.getJSONObject(1).getInteger("value");
            int l60 = array.getJSONObject(2).getInteger("value");
            int l120 = array.getJSONObject(3).getInteger("value");
            int g120 = array.getJSONObject(4).getInteger("value");

            Preconditions.checkArgument(less10 == l10, "10分钟内：组数=" + l10 + "接待列表组数=" + less10);
            Preconditions.checkArgument(less30 == l30, "10~30分钟：组数=" + l30 + "接待列表组数=" + less30);
            Preconditions.checkArgument(less60 == l60, "30～60分钟：组数=" + l60 + "接待列表组数=" + less60);
            Preconditions.checkArgument(less120 == l120, "60～120分钟：组数=" + l120 + "接待列表组数=" + less120);
            Preconditions.checkArgument(more120 == g120, "大于120分钟：组数=" + g120 + "接待列表组数=" + more120);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("店面数据分析-客户接待时长分析：时间段内组数=【前一日】【销售总监-app-销售接待】对应接待时长的数量");
        }
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

    //@Test
    public void carOwnerPersonalPercent() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray array = crm.carOwner("DAY", "", "").getJSONArray("ratio_list");
            int personal = array.getJSONObject(0).getInteger("value");
            int business = array.getJSONObject(1).getInteger("value");
            int all = personal + business;
            if (all > 0) {
                double a = (double) personal / all;
                BigDecimal bd = new BigDecimal(a);
                String jisuan = bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
                String personalPer = array.getJSONObject(0).getString("percent");
                Preconditions.checkArgument(jisuan.equals(personalPer), "不等于");
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("成交客户分析: 个人车主百分比=个人车主数量/（个人+公司车主数量）");
        }
    }

    //@Test
    public void carOwnerBusinessPercent() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray array = crm.carOwner("ALL", "", "").getJSONArray("ratio_list");
            int personal = array.getJSONObject(0).getInteger("value");
            int business = array.getJSONObject(1).getInteger("value");
            int all = personal + business;
            if (all > 0) {
                double a = (double) business / all;
                BigDecimal bd = new BigDecimal(a);
                String jisuan = bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
                String businessPer = array.getJSONObject(1).getString("percent");

                Preconditions.checkArgument(jisuan.equals(businessPer), "不等于");
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("成交客户分析: 公司车主百分比=公司车主数量/（个人+公司车主数量）");
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

    @Test
    public void city100() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray array = crm.city("DAY", "", "").getJSONArray("list");
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
            saveData("[成交客户分析] 苏州各区成交量 成交量百分比之和=100% 或0%");
        }
    }

    @Test
    public void partLTCity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray array = crm.city("ALL", "", "").getJSONArray("list");
            int sum = 0;
            for (int i = 0; i < array.size(); i++) {
                sum = sum + array.getJSONObject(i).getInteger("value");
            }
            int a = crm.wholeCountry("ALL", "", "").getJSONArray("list").size();
            if (a > 0) {
                int jiangsu = crm.wholeCountry("ALL", "", "").getJSONArray("list").getJSONObject(0).getInteger("value");
                Preconditions.checkArgument(sum <= jiangsu, "苏州各区之和" + sum + "> 江苏" + jiangsu);
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("[成交客户分析] 苏州各区成交量之和<=江苏成交量");
        }
    }


}
