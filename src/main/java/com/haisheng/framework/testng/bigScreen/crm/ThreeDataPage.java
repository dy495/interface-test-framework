package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.CustomerInfo;
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
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    CustomerInfo cstm = new CustomerInfo();
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


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     *
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "lxq";


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
       crm.login(cstm.lxqgw,cstm.pwd);


    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     *
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
            JSONObject obj1 = crm.shopPannel("DAY","","");
            service = obj1.getInteger("service"); //累计接待
            test_drive = obj1.getInteger("test_drive"); //累计试驾
            deal = obj1.getInteger("deal"); //累计成交
            delivery = obj1.getInteger("delivery"); //累计交车

            //4个tab数据 --周
            JSONObject objweek = crm.shopPannel("WEEK","","");
            week_service = objweek.getInteger("service"); //累计接待
            week_test_drive = objweek.getInteger("test_drive"); //累计试驾
            week_deal = objweek.getInteger("deal"); //累计成交
            week_delivery = objweek.getInteger("delivery"); //累计交车

            //4个tab数据 --月
            JSONObject objmonth = crm.shopPannel("MONTH","","");
            month_service = objmonth.getInteger("service"); //累计接待
            month_test_drive = objmonth.getInteger("test_drive"); //累计试驾
            month_deal = objmonth.getInteger("deal"); //累计成交
            month_delivery = objmonth.getInteger("delivery"); //累计交车

            //4个tab数据 --季
            JSONObject objquarter = crm.shopPannel("QUARTER","","");
            quarter_service = objquarter.getInteger("service"); //累计接待
            quarter_test_drive = objquarter.getInteger("test_drive"); //累计试驾
            quarter_deal = objquarter.getInteger("deal"); //累计成交
            quarter_delivery = objquarter.getInteger("delivery"); //累计交车

            //4个tab数据 --年
            JSONObject objyear = crm.shopPannel("YEAR","","");
            year_service = objyear.getInteger("service"); //累计接待
            year_test_drive = objyear.getInteger("test_drive"); //累计试驾
            year_deal = objyear.getInteger("deal"); //累计成交
            year_delivery = objyear.getInteger("delivery"); //累计交车


            //[业务漏斗]
            JSONArray array = crm.saleFunnel("DAY","","").getJSONObject("business").getJSONArray("list");

            JSONObject obj2= array.getJSONObject(0);
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


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
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
            Preconditions.checkArgument(service>=test_drive,"累计接待" + service +" < " + "累计试驾" + test_drive);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析：累计接待>=累计试驾");
        }
    }

    @Test(priority = 1)
    public void serviceETdeal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(service>=deal,"累计接待" + service +" < " + "累计成交" + deal);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析：累计接待>=累计成交");
        }
    }

    @Test(priority = 1)
    public void serviceETdelivery() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(service>=delivery,"累计接待" + service +" < " + "累计交车" + delivery);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析：累计接待>=累计交车");
        }
    }

    @Test(priority = 1)
    public void businessClue() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            int all = creat + recp;
            Preconditions.checkArgument(clue==all ,"线索" + clue +" != " + "创建线索" + creat +" + 接待线索" + recp);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：线索=创建线索+接待线索");
        }
    }

    @Test(priority = 1)
    public void businessRECEIVE() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int all = receive_first + receive_visit + receive_second;
            Preconditions.checkArgument(receive==all ,"接待" + receive +" != " + "首次" + receive_first +" + 邀约" + receive_visit+" + 再次" + receive_second);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：接待=首次+邀约+再次");
        }
    }

    @Test(priority = 1)
    public void businessTestDriver() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int all = testDriver_first + testDriver_visit + testDriver_second;
            Preconditions.checkArgument(testDriver==all ,"试驾" + testDriver +" != " + "首次" + testDriver_first +" + 邀约" + testDriver_visit+" + 再次" + testDriver_second);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：试驾=首次+邀约+再次");
        }
    }

    @Test(priority = 1)
    public void businessOrder() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            int all = order_first + order_visit + order_second;
            Preconditions.checkArgument(order==all ,"订单" + order +" != " + "首次" + order_first +" + 邀约" + order_visit+" + 再次" + order_second);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：订单=首次+邀约+再次");
        }
    }

    @Test(priority = 1)
    public void businessClueGTReceive() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(clue>=receive ,"线索" + clue +" < 接待" + receive );

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：线索>=接待");
        }
    }

    @Test(priority = 1)
    public void businessReceiveGTDriver() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(receive>=testDriver ,"接待" + receive +" < 试驾" + testDriver );

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：接待>=试驾");
        }
    }

    @Test(priority = 1)
    public void businessReceiveGTOrder() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(receive>=order ,"接待" + receive +" < 订单" + order );

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：接待>=订单");
        }
    }

    @Test(priority = 1)
    public void businessOrderGTDeal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(order>=deal ,"订单" + order +" < 交车" + deal );

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("【店面数据分析】业务漏斗：订单>=交车");
        }
    }

    @Test(priority = 1)
    public void businessServiceEQRecSum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int sum = 0;
            JSONArray array = crm.receptTime("DAY","","").getJSONArray("list");
            for (int i = 0; i < 5; i++){
                sum = sum + array.getJSONObject(i).getInteger("value");
            }
            Preconditions.checkArgument(service==sum ,"累计接待" + service +" != 各组时长之和" + sum );

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("[店面数据分析] 业务漏斗：累计接待=【客户接待时长分析】各时间段组数之和");
        }
    }

    @Test(priority = 1)
    public void businessService() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(service==receive ,"累计接待" + service +" != 业务漏斗接待" + receive );

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析: 累计接待=【业务漏斗】接待");
        }
    }

    @Test(priority = 1)
    public void businessTestDriverEQFunnel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(test_drive==testDriver ,"累计试驾" + test_drive +" != 业务漏斗试驾" + testDriver );

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析: 累计试驾=【业务漏斗】试驾");
        }
    }

    @Test(priority = 1)
    public void businessDealEQOrder() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(deal==order ,"累计成交" + deal +" != 业务漏斗订单" + order );

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析: 累计成交=【业务漏斗】订单");
        }
    }

    @Test(priority = 1)
    public void businessDeliveryEQDeal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Preconditions.checkArgument(delivery==funnel_deal ,"累计交车" + delivery +" != 业务漏斗交车" + funnel_deal );

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析: 累计交车=【业务漏斗】交车");
        }
    }

    @Ignore
    @Test(priority = 1)
    public void serviceDayWeek() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Preconditions.checkArgument(service<=week_service,"日累计接待" + service +" > " + "周累计接待" + week_service);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析：【日】累计接待<=【周】累计接待");
        }
    }

    @Ignore
    @Test(priority = 1)
    public void serviceWeekMonth() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Preconditions.checkArgument(week_service<=month_service,"周累计接待" + week_service +" > " + "月累计接待" + month_service);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析：【周】累计接待<=【月】累计接待");
        }
    }

    @Ignore
    @Test(priority = 1)
    public void serviceMonthQuarter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Preconditions.checkArgument(month_service<=quarter_service,"月累计接待" + month_service +" > " + "季累计接待" + quarter_service);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析：【月】累计接待<=【季】累计接待");
        }
    }

    @Ignore
    @Test(priority = 1)
    public void serviceQuarterYear() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Preconditions.checkArgument(quarter_service<=year_service,"季累计接待" + quarter_service +" > " + "年累计接待" + year_service);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析：【季】累计接待<=【年】累计接待");
        }
    }

    @Ignore
    @Test(priority = 1)
    public void driverDayWeek() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Preconditions.checkArgument(test_drive<=week_test_drive,"日累计试驾" + test_drive +" > " + "周累计试驾" + week_test_drive);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析：【日】累计试驾<=【周】累计试驾");
        }
    }

    @Ignore
    @Test(priority = 1)
    public void driverWeekMonth() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Preconditions.checkArgument(week_test_drive<=month_test_drive,"周累计试驾" + week_test_drive +" > " + "月累计试驾" + month_test_drive);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析：【周】累计试驾<=【月】累计试驾");
        }
    }

    @Ignore
    @Test(priority = 1)
    public void driverMonthQuarter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Preconditions.checkArgument(month_test_drive<=quarter_test_drive,"月累计试驾" + month_test_drive +" > " + "季累计试驾" + quarter_test_drive);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析：【月】累计试驾<=【季】累计试驾");
        }
    }

    @Ignore
    @Test(priority = 1)
    public void driverQuarterYear() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Preconditions.checkArgument(quarter_test_drive<=year_test_drive,"季累计试驾" + quarter_test_drive +" > " + "年累计试驾" + year_test_drive);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析：【季】累计试驾<=【年】累计试驾");
        }
    }









    /**
     * --------------------成交客户分析页 页面内一致性-------------------
     */

    @Test
    public void carOwner100() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray array = crm.carOwner("DAY","","").getJSONArray("ratio_list");
            double personal = array.getJSONObject(0).getDouble("percent");
            double business = array.getJSONObject(1).getDouble("percent");
            double all = personal + business;
            Preconditions.checkArgument(all==1 ,"个人车主" + personal +" + 公司车主" + business +" != 100%" );

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("成交客户分析: 个人车主百分比+公司车主百分比=100%");
        }
    }

    @Test
    public void carOwnerPersonalPercent() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray array = crm.carOwner("DAY","","").getJSONArray("ratio_list");
            int personal = array.getJSONObject(0).getInteger("value");
            int business = array.getJSONObject(1).getInteger("value");
            int all = personal + business;
            double a = (double) personal / all;
            BigDecimal bd   =   new   BigDecimal(a);
            String jisuan   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
            String personalPer = array.getJSONObject(0).getString("percent");

            Preconditions.checkArgument(jisuan.equals(personalPer) ,"不等于");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("成交客户分析: 个人车主百分比=个人车主数量/（个人+公司车主数量）");
        }
    }

    @Test
    public void carOwnerBusinessPercent() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray array = crm.carOwner("DAY","","").getJSONArray("ratio_list");
            int personal = array.getJSONObject(0).getInteger("value");
            int business = array.getJSONObject(1).getInteger("value");
            int all = personal + business;
            double a = (double) business / all;
            BigDecimal bd   =   new   BigDecimal(a);
            String jisuan   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
            String businessPer = array.getJSONObject(1).getString("percent");

            Preconditions.checkArgument(jisuan.equals(businessPer) ,"不等于");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("成交客户分析: 公司车主百分比=公司车主数量/（个人+公司车主数量）");
        }
    }

    @Test
    public void carOwnerAge100() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray array = crm.genderAge("DAY","","").getJSONObject("age").getJSONArray("list");
            double sum = 0;
            for (int i = 0 ; i < array.size();i++){
                sum = sum + array.getJSONObject(i).getDouble("percentage");
            }
            Preconditions.checkArgument(sum==1,"总和为"+ sum);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("[成交客户分析] 车主年龄分析 各年龄段之和=100%");
        }
    }

    @Test
    public void carOwnerGender100() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray array = crm.genderAge("DAY","","").getJSONObject("gender").getJSONArray("list");
            double sum = 0;
            for (int i = 0 ; i < array.size();i++){
                sum = sum + array.getJSONObject(i).getDouble("percentage");
            }
            Preconditions.checkArgument(sum==1,"总和为"+ sum);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("[成交客户分析] 车主性别分析 性别之和=100%");
        }
    }







}
