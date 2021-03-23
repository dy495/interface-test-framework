package com.haisheng.framework.testng.bigScreen.xundianDaily.zt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.xundianDaily.MendianInfo;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.StorePcAndAppData;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;


import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkArgument;

public class XundianAppData extends TestCaseCommon implements TestCaseStd {
    public static final Logger log = LoggerFactory.getLogger(StorePcAndAppData.class);
    public static final int size = 100;
    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    StoreScenarioUtil md = StoreScenarioUtil.getInstance();
    MendianInfo info = new MendianInfo();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //replace checklist app id and conf id


        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "周涛";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.XUNDIAN_DAILY.getDesc());
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
        commonConfig.shopId = EnumTestProduce.XUNDIAN_DAILY.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("xundian " + xd);
        xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    // 巡店记录列表报告数量 == 待处理+已处理+无需处理的数量
    @Test
    public void getShopHandleStatusList() throws Exception{

        logger.logCaseStart(caseResult.getCaseName());
        try {

            // 获取门店巡店记录列表total总数
            JSONObject shopCHeckStatus = xd.getShopChecksPage(info.shop_id, null, null, "", "", "", 100, null);
            Integer checks_list = shopCHeckStatus.getInteger("total");
            //无需处理
            JSONObject Unwanted = xd.getShopChecksPage(info.shop_id, null, 0, "", "", "", 10, null);
            Integer Unwanted_TypeNum = Unwanted.getInteger("total");
            //待处理
            JSONObject deal_Type = xd.getShopChecksPage(info.shop_id, null, 1, "", "", "", 10, null);
            Integer deal_TypeNum = deal_Type.getInteger("total");
            //已处理
            JSONObject pending_Type = xd.getShopChecksPage(info.shop_id, null, 2, "", "", "", 10, null);
            Integer pending_TypeNum = pending_Type.getInteger("total");

            int listNum = Unwanted_TypeNum + deal_TypeNum + pending_TypeNum;//三项之和
            Preconditions.checkArgument(checks_list == listNum, "巡店记录列表数量" + checks_list + "不等于待处理+已处理+无需处理的数量=" + listNum);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("巡店记录列表报告数量 == 待处理+已处理+无需处理的数量");
        }
    }


    //不合格报告+合格报告 == 列表下全部报告
    @Test
    public void getResultTypeList() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            // 获取门店巡店记录列表total总数
            JSONObject shopCHeckStatus = xd.getShopChecksPage(info.shop_id, null, null, "", "", "", 10, null);
            Integer checks_list = shopCHeckStatus.getInteger("total");
            // 巡店记录处理下拉框
            JSONObject qualified_Type = xd.getShopChecksPage(info.shop_id, 0, null, "", "", "", 10, null);
            Integer qualified_Num = qualified_Type.getInteger("total");
            JSONObject unqualified_Type = xd.getShopChecksPage(info.shop_id, 1, null, "", "", "", 100, null);
            Integer unqualified_Num = unqualified_Type.getInteger("total");
            int result_Type = qualified_Num + unqualified_Num;
            Preconditions.checkArgument(checks_list == result_Type, "巡店记录列表数量" + checks_list + "不等于合格+不合格的数量=" + result_Type);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app不合格报告+合格报告 == 列表下全部报告");
        }
    }


    //巡店记录详情内容==PC【巡店报告详情】中的巡店记录详情内容
    @Test
    public void getShopChecksDetail() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray bgList = xd.shopChecksPage(1, 10, info.shop_id_01).getJSONArray("list");
            Integer bgId = bgList.getJSONObject(0).getInteger("id");
            // 获取报告中不合格不合格项数，合格项数，不适用项数,巡店者，提交说明
            Integer inappropriate_num = xd.shopChecksDetail(bgId, info.shop_id_01).getInteger("inappropriate_num");
            Integer qualified_num = xd.shopChecksDetail(bgId, info.shop_id_01).getInteger("qualified_num");
            Integer unqualified_num = xd.shopChecksDetail(bgId, info.shop_id_01).getInteger("unqualified_num");
            String inspector_names = xd.shopChecksDetail(bgId, info.shop_id_01).getString("inspector_name");
            String submit_comment_list = xd.shopChecksDetail(bgId, info.shop_id_01).getString("submit_comment");
            String check_type0 = xd.shopChecksDetail(bgId, info.shop_id_01).getString("check_type");
//            app端报告
            JSONArray shopCHeckStatus = xd.getShopChecksPage(info.shop_id_01, null, null, "", "", "", 10, null).getJSONArray("list");
            Long id2 = shopCHeckStatus.getJSONObject(0).getLong("id");
//            Long bgId1 = bgId.longValue();
            JSONArray checkListId = xd.patrol_detail(info.shop_id_01, id2).getJSONArray("list");
            Long id3 = checkListId.getJSONObject(0).getLong("id");
            JSONObject shopCheck = xd.getShopChecksDetail(id2, info.shop_id_01, id3, null);
            Integer inappropriate_num1 = shopCheck.getInteger("inappropriate_num");
            Integer qualified_num1 = shopCheck.getInteger("qualified_num");
            Integer unqualified_num1 = shopCheck.getInteger("unqualified_num");
            String submit_comment1 = shopCheck.getString("submit_comment");
            String inspector_name1 = shopCheck.getString("inspector_name");
            String check_type1 = shopCheck.getString("check_type");

            checkArgument(inappropriate_num == inappropriate_num1, "【巡店详情】中报告不适用项数!=【app巡店信息】中报告不适用项数" + inappropriate_num + "!=" + inappropriate_num1);
            checkArgument(qualified_num == qualified_num1, "【巡店详情】中报告合格项数!=【app巡店信息】中报告合格项数" + qualified_num + "!=" + qualified_num1);
            checkArgument(unqualified_num == unqualified_num1, "【巡店详情】中报告不合格项数!=【app巡店信息】中报告不合格项数" + unqualified_num + "!=" + unqualified_num1);
            checkArgument(submit_comment_list.equals(submit_comment1), "【巡店详情】中报告提交说明!=【app巡店信息】中提交说明" + submit_comment_list + "!=" + submit_comment1);
            checkArgument(inspector_names.equals(inspector_name1), "【巡店详情】中报告巡店者!=【app巡店信息】中报告巡店者" + inspector_names + "!=" + inspector_name1);
            checkArgument(check_type0.equals(check_type1), "【巡店详情】中巡店方式!=【app巡店信息】中巡店方式" + check_type0 + "!=" + check_type1);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("巡店记录详情内容==PC【巡店报告详情】中的巡店记录详情内容");
        }
    }
 

    //[未完成]列表的数量==未完成的待办事项的的展示项
    @Test
    public void wwcSum() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long lastValue = null;
            JSONArray jsonArray;
            int count = 0;
            do {
                JSONObject response = xd.task_list(1, 10, 0, lastValue);
                lastValue = response.getLong("last_value");
                jsonArray = response.getJSONArray("list");
                count += jsonArray.size();
            } while (jsonArray.size() == 10);
            //获取待办列表事项总数totalnum
            int totalnum = xd.task_list(1,10,0,null).getInteger("total");
            CommonUtil.valueView(count,totalnum);
            checkArgument(totalnum == count, "未完成列表数量" + totalnum + "!=未完成的待办事项的展示项" + count);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app[未完成]列表的数量==未完成的待办事项的的展示项");
        }
    }


//    app账号下当前门店数量==pc该账号下巡店中心列表的数量

    @Test
    public void mdNum() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer appMdNum = md.app_shopNum().getInteger("shop_count");
            Integer pcMdNum = md.patrolShopPageV3("", 1, 10).getInteger("total");
            CommonUtil.valueView(appMdNum, pcMdNum);
            checkArgument(appMdNum == pcMdNum, "app账号下当前门店数量" + appMdNum + "pc该账号下巡店中心列表的数量" + pcMdNum);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app账号下当前门店数量==pc该账号下巡店中心列表的数量");
        }

    }

    // app账号下当前门店数量==pc该账号下客流分析列表的数量
    @Test
    public void mdNum1() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer appMdNum = md.app_shopNum().getInteger("shop_count");
            Integer pcCustomerNum = md.customerFlowList("", null, "", "", "", null, 1, 10, "").getInteger("total");
            checkArgument(appMdNum == pcCustomerNum, "app账号下当前门店数量" + appMdNum + "pc该账号下客流分析列表的数量" + pcCustomerNum);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app账号下当前门店数量==pc该账号下客流分析列表的数量");
        }

    }


    //app[首页实时客流分析] 今日到访人数<= [趋势图]今天各时段人数之和
    @Test
    public void todayNumUv() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray homeList = md.cardList("HOME_BELOW", null, 10).getJSONArray("list");
//            Integer todayUv = homeList.getJSONObject(0).getJSONObject("result").getInteger("today_uv");
            JSONObject resultList = homeList.getJSONObject(0).getJSONObject("result");
//            Integer todayUv = resultList.getJSONObject(0).getInteger("today_uv");
            Integer todayUv = resultList.getJSONObject("total_number").getInteger("today_uv");
            int todayUvCount = 0;
            JSONArray trendList = homeList.getJSONObject(0).getJSONObject("result").getJSONArray("trend_list");
            for (int i = 0; i < trendList.size(); i++) {
                Integer uv = trendList.getJSONObject(i).getInteger("today_uv");
                if (uv == null) {
                    uv = 0;
                }
                todayUvCount += uv;
            }
            CommonUtil.valueView(todayUv, todayUvCount);
            checkArgument(todayUv <= todayUvCount, "app首页实时客流分析中今日到访人数" + todayUv + "app趋势图中各时间段人数" + todayUvCount);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app首页实时客流分钟中今日到访人数 <= app趋势图中今天各时段人数之和");
        }

    }

    //app[首页实时客流分析] 今日到访人次== [趋势图]今天各时段人次之和
    @Test
    public void todayNumPv() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray homeList = md.cardList("HOME_BELOW", null, 10).getJSONArray("list");
//            Integer todayUv = homeList.getJSONObject(0).getJSONObject("result").getInteger("today_uv");
            JSONObject resultList = homeList.getJSONObject(0).getJSONObject("result");
//            Integer todayUv = resultList.getJSONObject(0).getInteger("today_uv");
            Integer todayPv = resultList.getJSONObject("total_number").getInteger("today_pv");
            int todayPvCount = 0;
            JSONArray trendList = homeList.getJSONObject(0).getJSONObject("result").getJSONArray("trend_list");
            for (int i = 0; i < trendList.size(); i++) {
                Integer pv = trendList.getJSONObject(i).getInteger("today_pv");
                if (pv == null) {
                    pv = 0;
                }
                todayPvCount += pv;
            }
            CommonUtil.valueView(todayPv, todayPvCount);
            checkArgument(todayPv == todayPvCount, "app首页实时客流分析中今日到访人数" + todayPv + "app趋势图中各时间段人数" + todayPvCount);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app首页实时客流分钟中今日到访人数 <= app趋势图中今天各时段人数之和");
        }

    }


    //APP巡店记录详情，打开展示报告信息
    @Test(dataProvider = "is_read",dataProviderClass = DataProviderMethod.class)
    public void ReportList(Boolean is_read) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
//            获取消息列表
            JSONArray message_center = xd.user_message_center(is_read,null,10).getJSONArray("list");
//          获取第一份报告的id
            for(int i=0;i<message_center.size();i++){
                int reportId = message_center.getJSONObject(i).getInteger("id");
//            获取recordID
                Long record_id = xd.user_message_center_detail(reportId).getLong("record_id");
                Long shopID = xd.user_message_center_detail(reportId).getLong("shop_id");
                JSONArray items_list = xd.patrol_detail(shopID,record_id).getJSONArray("list");
                for(int j=0;j<items_list.size();j++){
                    Long items_id = items_list.getJSONObject(j).getLong("id");
                    JSONObject check = xd.getShopChecksDetail(record_id,shopID,items_id,null).getJSONObject("check");
                    JSONArray check_items = check.getJSONArray("check_items");
                    checkArgument(check_items != null, "app巡店记录详情");
                }
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("巡店记录详情默认展示为空");
        }

    }


    // 今日进店客流==今日进店客流详情中今日进店客流
    @Test
    public void customerFlow() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取卡片list
            JSONArray list = md.cardList("HOME_TOP",null,10).getJSONArray("list");
            //获取result
            JSONObject result = list.getJSONObject(0).getJSONObject("result");
            //获取累计进店客流
            int total_enter_customer_uv = result.getInteger("total_enter_customer_uv");
            //获取进店客流详情中的进店客流
            JSONArray items = md.cardDetail("TOTAL_ENTER_CUSTOMER_FLOW").getJSONArray("items");
            int items_value = items.getJSONObject(0).getInteger("item_value");
            CommonUtil.valueView(items_value, total_enter_customer_uv);
            checkArgument(total_enter_customer_uv==items_value, "今日进店客流总数!=今日进店客流详情中今日进店客流总数");

            //获取今日进店客流列表
            JSONObject today_enter_customer_flow = result.getJSONObject("today_enter_customer_flow");
            //获取今日进店客流
            int today_enter_customer_uv = today_enter_customer_flow.getInteger("today_enter_customer_uv");
            int items_value1 = items.getJSONObject(2).getInteger("item_value");
            CommonUtil.valueView(today_enter_customer_uv, items_value1);
            checkArgument(today_enter_customer_uv == items_value1, "今日进店客流！=今日进店客流详情中今日进店客流");

            //获取今日进店客流得同比
            int yesterday_enter_customer_qoq = today_enter_customer_flow.getInteger("yesterday_enter_customer_qoq");
            //获取今日进店客流的环比
            int last_week_enter_customer_qoq = today_enter_customer_flow.getInteger("last_week_enter_customer_qoq");
            //获取详情中的环比和同比
            int before_day = items.getJSONObject(2).getInteger("before_day");
            int before_week = items.getJSONObject(2).getInteger("before_week");
            checkArgument(yesterday_enter_customer_qoq==before_day, "今日进店客流同比！=今日进店客流详情中今日进店客流同比");
            checkArgument(last_week_enter_customer_qoq==before_week, "今日进店客流环比比！=今日进店客流详情中今日进店客流环比");
//
//            获取今日进店新客
            JSONObject today_new_customer_flow = result.getJSONObject("today_new_customer_flow");
            int today_new_customer_uv = today_new_customer_flow.getInteger("today_new_customer_uv");
            int items_value2 = items.getJSONObject(1).getInteger("item_value");
            checkArgument(today_new_customer_uv==items_value2, "今日进店新客！=今日进店客流详情中今日进店新客");
            //获取今日进店新客的同比
            int last_week_new_customer_qoq = today_new_customer_flow.getInteger("last_week_new_customer_qoq");
            int yesterday_new_customer_qoq = today_new_customer_flow.getInteger("yesterday_new_customer_qoq");
            int before_day1 = items.getJSONObject(1).getInteger("before_day");
            int before_week1 = items.getJSONObject(1).getInteger("before_week");
            checkArgument(last_week_new_customer_qoq==before_week1, "今日进店新客同比！=今日进店新客中今日新客同比");
            checkArgument(yesterday_new_customer_qoq==before_day1, "今日进店新客环比比==今日进店新客中今日新客环比");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日进店客流数据==今日进店客流中的各分数据");
        }

    }

    //交易数据方面
//    @Test
//    public void  tradeCustomer(Boolean is_read) throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //获取卡片list
//            JSONArray list = md.cardList("HOME_TOP",null,10).getJSONArray("list");
//            //获取result
//            JSONObject result = list.getJSONObject(1).getJSONObject("result");
//            //获取累计交易客流
//            Integer total_enter_customer_uv = result.getInteger("total_enter_customer_uv");
//            //获取累计交易客流
//            JSONArray items = md.cardDetail("TOTAL_TRANS_CUSTOMER_FLOW").getJSONArray("items");
//            Integer items_value = items.getInteger(1);
//            checkArgument(total_enter_customer_uv==items_value, "今日进店客流总数==今日进店客流详情中今日进店客流总数");
//
//            //获取今日进店客流列表
//            JSONObject today_enter_customer_flow = result.getJSONObject("today_enter_customer_flow");
//            //获取今日进店客流
//            Integer today_enter_customer_uv = today_enter_customer_flow.getInteger("today_enter_customer_uv");
//            Integer items_value1 = items.getInteger(1);
//            checkArgument(today_enter_customer_uv==items_value1, "今日进店客流==今日进店客流详情中今日进店客流");
//
//            //获取今日进店客流得同比
//            Number yesterday_enter_customer_qoq = today_enter_customer_flow.getInteger("last_week_enter_customer_qoq");
//            //获取今日进店客流的环比
//            Number last_week_enter_customer_qoq = today_enter_customer_flow.getInteger("yesterday_enter_customer_qoq");
//            //获取详情中的环比和同比
//            Number before_day = items.getInteger(3);
//            Number before_week = items.getInteger(4);
//            checkArgument(yesterday_enter_customer_qoq==before_day, "今日进店客流同比==今日进店客流详情中今日进店客流同比");
//            checkArgument(last_week_enter_customer_qoq==before_week, "今日进店客流环比比==今日进店客流详情中今日进店客流环比");
//
//            //获取今日进店新客
//            JSONObject today_new_customer_flow = result.getJSONObject("today_new_customer_flow");
//            Integer today_new_customer_uv = today_new_customer_flow.getInteger("today_new_customer_uv");
//            Integer items_value2 = items.getInteger(1);
//            checkArgument(today_new_customer_uv==items_value2, "今日进店新客==今日进店客流详情中今日进店新客");
//            //获取今日进店新客的同比
//            Number last_week_new_customer_qoq = today_new_customer_flow.getInteger("last_week_new_customer_qoq");
//            Number yesterday_new_customer_qoq = today_new_customer_flow.getInteger("yesterday_new_customer_qoq");
//            Number before_day1 = items.getInteger(3);
//            Number before_week1 = items.getInteger(4);
//            checkArgument(last_week_new_customer_qoq==before_day1, "今日进店客流同比==今日进店客流详情中今日新客同比");
//            checkArgument(yesterday_new_customer_qoq==before_week1, "今日进店客流环比比==今日进店客流详情中今日新客环比");
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("今日进店客流数据==今日进店客流中的各分数据");
//        }
//
//    }
    // 今日进店客流==今日进店客流详情中今日进店客流
    @Test
    public void  orderForm() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取卡片list
            JSONArray list = md.cardList("HOME_TOP",null,10).getJSONArray("list");
            //获取result
            JSONObject result = list.getJSONObject(2).getJSONObject("result");
            //获取累计订单
            int total_order_number = result.getInteger("total_order_number");
            //获取订单详情中的累计订单数
            JSONArray items = md.cardDetail("TOTAL_ORDER").getJSONArray("items");
            int items_value = items.getJSONObject(0).getInteger("item_value");
            checkArgument(total_order_number==items_value, "累计订单数==累计订单详情中的累计订单数");

            //获取今日订单数
            JSONObject today_order = result.getJSONObject("today_order");
            int today_order_number = today_order.getInteger("today_order_number");
            int items_value1 = items.getJSONObject(1).getInteger("item_value");
            checkArgument(today_order_number==items_value1, "今日订单数==订单详情中的今日订单数");

            //今日订单的环比和同比
            int last_week_order_qoq = today_order.getInteger("last_week_order_qoq");
            int yesterday_order_qoq = today_order.getInteger("yesterday_order_qoq");
            int before_day =  items.getJSONObject(1).getInteger("before_day");
            int before_week =  items.getJSONObject(1).getInteger("before_day");
            checkArgument(last_week_order_qoq==before_week, "今日订单的环比==订单详情中的今日订单数环比");
            checkArgument(yesterday_order_qoq==before_day, "今日订单数的同比==订单详情中的今日订单数同比");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日进店客流==今日进店客流详情中今日进店客流");
        }
    }

    //营业额
    @Test
    public void  OrderTurnover() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取卡片list
            JSONArray list = md.cardList("HOME_TOP",null,10).getJSONArray("list");
            //获取result
            JSONObject result = list.getJSONObject(3).getJSONObject("result");
            //获取累计营业额
            int total_turnover = result.getInteger("total_turnover");
            //获取营业额详情中的累计营业额
            JSONArray items = md.cardDetail("TOTAL_TURNOVER").getJSONArray("items");
            int items_value = items.getJSONObject(0).getInteger("item_value");
            checkArgument(total_turnover==items_value, "累计营业额==营业额详情中的累计营业额");

            //获取今日营业额
            JSONObject today_customer_univalence = result.getJSONObject("today_customer_univalence");
            int today_customer_univalence1 = today_customer_univalence.getInteger("today_customer_univalence");
            int items_value1 = items.getJSONObject(1).getInteger("item_value");
            checkArgument(today_customer_univalence1==items_value1, "今日营业额==订单详情中的今日营业额");

            //今日订单的环比和同比
            int last_week_customer_univalence_qoq = today_customer_univalence.getInteger("last_week_customer_univalence_qoq");
            int yesterday_customer_univalence_qoq = today_customer_univalence.getInteger("yesterday_customer_univalence_qoq");
            int before_day = items.getJSONObject(1).getInteger("before_day");
            int before_week = items.getJSONObject(1).getInteger("before_week");
            checkArgument(last_week_customer_univalence_qoq==before_week, "今日营业额的环比==订单详情中的今日营业额环比");
            checkArgument(yesterday_customer_univalence_qoq==before_day, "今日营业额的同比==订单详情中的今日订单数同比");


            //获取今日客单价
            JSONObject today_turnover = result.getJSONObject("today_turnover");
            int today_turnover1 = today_turnover.getInteger("today_turnover");
            int items_value2 = items.getJSONObject(2).getInteger("item_value");
            checkArgument(today_turnover1==items_value2, "今日客单价==订单详情中的今日客单价");

            //今日客单价的环比和同比
            int last_week_turnover_qoq = today_turnover.getInteger("last_week_turnover_qoq");
            int yesterday_turnover_qoq = today_turnover.getInteger("yesterday_turnover_qoq");
            int before_day1 = items.getJSONObject(1).getInteger("before_day");
            int before_week1 = items.getJSONObject(1).getInteger("before_week");
            checkArgument(last_week_turnover_qoq==before_week1, "今日客单价的环比==订单详情中的今日客单价环比");
            checkArgument(yesterday_turnover_qoq==before_day1, "今日客单价的同比==订单详情中的今日客单价同比");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("累计营业额==营业额详情中的每个数据");
        }
    }


    //app实时客流趋势图中的uv==客流概览趋势图中的uv
    @Test
    public void allShoptodayNum() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //app趋势图中uv
            JSONArray homeList = md.cardList("HOME_BELOW", null, 10).getJSONArray("list");
            int appTodayUvCount = 0;
            JSONArray trendList = homeList.getJSONObject(0).getJSONObject("result").getJSONArray("trend_list");
            for (int i = 0; i < trendList.size(); i++) {
                Integer uv = trendList.getJSONObject(i).getInteger("today_uv");
                if (uv == null) {
                    uv = 0;
                }
                appTodayUvCount += uv;
            }
            //pc趋势图中的uv
            int pcTodayUvCount = 0;
            JSONArray list = md.real_shop_PUv().getJSONArray("list");
            for (int j=0;j<list.size();j++){
                Integer uv = list.getJSONObject(j).getInteger("today_uv");
                if (uv == null) {
                    uv = 0;
                }
                pcTodayUvCount += uv;
            }
            CommonUtil.valueView( pcTodayUvCount,appTodayUvCount);
            checkArgument(appTodayUvCount==pcTodayUvCount, "app实时客流趋势图uv" + appTodayUvCount + "pc实时趋势图中uv" + pcTodayUvCount);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app实时客流趋势图uv == pc实时趋势图中uv");
        }

    }


    //app实时客流趋势图中的pv==客流概览趋势图中的pv
    @Test
    public void allShoptodayNum1() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //app趋势图中Pv
            JSONArray homeList = md.cardList("HOME_BELOW", null, 10).getJSONArray("list");
            int appTodayPvCount = 0;
            JSONArray trendList = homeList.getJSONObject(0).getJSONObject("result").getJSONArray("trend_list");
            for (int i = 0; i < trendList.size(); i++) {
                Integer pv = trendList.getJSONObject(i).getInteger("today_pv");
                if (pv == null) {
                    pv = 0;
                }
                appTodayPvCount += pv;
            }
            //pc趋势图中的Pv
            int pcTodayPvCount = 0;
            JSONArray list = md.real_shop_PUv().getJSONArray("list");
            for (int j=0;j<list.size();j++){
                Integer pv = list.getJSONObject(j).getInteger("today_pv");
                if (pv == null) {
                    pv = 0;
                }
                pcTodayPvCount += pv;
            }
            CommonUtil.valueView( pcTodayPvCount,appTodayPvCount);
            checkArgument(appTodayPvCount==pcTodayPvCount, "app实时客流趋势图Pv" + appTodayPvCount + "pc实时趋势图中Pv" + pcTodayPvCount);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app实时客流趋势图Pv == pc实时趋势图中Pv");
        }

    }


    //app实时客流趋势图中的yesterdayuv==客流概览趋势图中的yesterdayuv
    @Test
    public void allShoptodayNum2() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //app趋势图中uv
            JSONArray homeList = md.cardList("HOME_BELOW", null, 10).getJSONArray("list");
            int appYesterdayUvCount = 0;
            JSONArray trendList = homeList.getJSONObject(0).getJSONObject("result").getJSONArray("trend_list");
            for (int i = 0; i < trendList.size(); i++) {
                Integer uv = trendList.getJSONObject(i).getInteger("yesterday_uv");
                if (uv == null) {
                    uv = 0;
                }
                appYesterdayUvCount += uv;
            }
            //pc趋势图中的uv
            int pcYesterdayUvCount = 0;
            JSONArray list = md.real_shop_PUv().getJSONArray("list");
            for (int j=0;j<list.size();j++){
                Integer uv = list.getJSONObject(j).getInteger("yesterday_uv");
                if (uv == null) {
                    uv = 0;
                }
                pcYesterdayUvCount += uv;
            }
            CommonUtil.valueView( pcYesterdayUvCount,appYesterdayUvCount);
            checkArgument(appYesterdayUvCount==pcYesterdayUvCount, "app实时客流趋势图昨日uv" + appYesterdayUvCount + "pc实时趋势图中昨日uv" + pcYesterdayUvCount);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app实时客流趋势图昨日uv == pc实时趋势图中昨日uv");
        }

    }


    //app实时客流趋势图中昨日pv==客流概览趋势图中昨日的pv
    @Test
    public void allShoptodayNum3() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //app趋势图中Pv
            JSONArray homeList = md.cardList("HOME_BELOW", null, 10).getJSONArray("list");
            int appYesterdayPvCount = 0;
            JSONArray trendList = homeList.getJSONObject(0).getJSONObject("result").getJSONArray("trend_list");
            for (int i = 0; i < trendList.size(); i++) {
                Integer pv = trendList.getJSONObject(i).getInteger("yesterday_pv");
                if (pv == null) {
                    pv = 0;
                }
                appYesterdayPvCount += pv;
            }
            //pc趋势图中的Pv
            int pcYesterdayPvCount = 0;
            JSONArray list = md.real_shop_PUv().getJSONArray("list");
            for (int j=0;j<list.size();j++){
                Integer pv = list.getJSONObject(j).getInteger("yesterday_pv");
                if (pv == null) {
                    pv = 0;
                }
                pcYesterdayPvCount += pv;
            }
            CommonUtil.valueView( pcYesterdayPvCount,appYesterdayPvCount);
            checkArgument(appYesterdayPvCount==pcYesterdayPvCount, "app实时客流趋势图昨日Pv" + appYesterdayPvCount + "pc实时趋势图中昨日Pv" + pcYesterdayPvCount);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app实时客流趋势图昨日Pv == pc实时趋势图中昨日Pv");
        }

    }

}




