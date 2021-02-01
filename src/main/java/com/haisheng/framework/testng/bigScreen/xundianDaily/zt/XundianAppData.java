package com.haisheng.framework.testng.bigScreen.xundianDaily.zt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
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
        commonConfig.checklistQaOwner = EnumChecklistUser.QQ.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.MENDIAN_DAILY.getDesc());
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
        commonConfig.shopId = EnumTestProduce.MENDIAN_DAILY.getShopId();
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
    public void getShopHandleStatusList() {

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
    public void getResultTypeList() {
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
    public void getShopChecksDetail() {
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
    public void wwcSum() {
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
    public void mdNum() {
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
    public void mdNum1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer appMdNum = md.app_shopNum().getInteger("shop_count");
            Integer pcCustomerNum = md.customerFlowList("", "", "", "", "", null, 1, 10, "").getInteger("total");
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
    public void todayNum() {
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
                    ;
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



    //图片中心总数==展示的数量
    @Test
    public void picNum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int pictotal = md.picturePage("","","","",null,1,8).getInteger("total");
            int pages = md.picturePage("","","","",null,1,8).getInteger("pages");
            int pagesize = (pages-1)*8;
            int pages_size = md.picturePage("","","","",null,pages,8).getInteger("page_size");

            CommonUtil.valueView(pictotal, pagesize+pages_size);
            checkArgument(pictotal == pagesize+pages_size, "图片中心总数" + pictotal + "!=搜索出图片的数量" + pagesize+pages_size);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("图片中心总数==展示的数量");
        }
    }


    //定检巡查展示图片个数==返回的数量
    @Test
    public void picScheduled() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int pictotal = md.picturePage("SCHEDULED","","","",null,1,8).getInteger("total");
            int pages = md.picturePage("SCHEDULED","","","",null,1,8).getInteger("pages");
            int pagesize = (pages-1)*8;
            int pages_size = md.picturePage("SCHEDULED","","","",null,pages,8).getInteger("page_size");

            CommonUtil.valueView(pictotal, pagesize+pages_size);
            checkArgument(pictotal == pagesize+pages_size, "图片中心总数" + pictotal + "!=搜索出图片的数量" + pagesize+pages_size);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("定检巡查展示图片个数==返回的数量");
        }
    }


    //定检巡查展示图片类型==返回的图片类型
    @Test
    public void picScheduled1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int pictotal = md.picturePage("SCHEDULED","","","",null,1,8).getInteger("total");
            int pages = md.picturePage("SCHEDULED","","","",null,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SCHEDULED","","","",null,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
//                    int a = tips.indexOf("定检巡店");
                    checkArgument(tips.contains("定检巡店"), "定检巡查" + "!=图片返回的类型" + tips);
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("定检巡查展示图片类型==返回的图片类型");
        }
    }

    //定检巡查展示图片类型+日期==返回的图片类型+日期
    @Test
    public void picScheduled2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String start_time = "2021-01-19";
            String end_time = "2021-01-22";
            int pictotal = md.picturePage("SCHEDULED",start_time,end_time,"",null,1,8).getInteger("total");
            int pages = md.picturePage("SCHEDULED",start_time,end_time,"",null,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SCHEDULED",start_time,end_time,"",null,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
                    String date_time = list.getJSONObject(j).getString("date_time");
                    String s = date_time.substring(0,10);
                    int a = s.compareTo(start_time);
                    int b = s.compareTo(end_time);
                    checkArgument(tips.contains("定检巡店"), "定检巡查" + "!=图片返回的类型" + tips);
                    checkArgument(a>=0&&b<=0, "定检巡查+日期" + "!=图片返回日期" + date_time);
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("定检巡查展示图片类型+日期==返回的图片类型+日期");
        }
    }


    //定检巡查展示图片类型+日期+门店名称+异常==返回的图片类型+日期+门店名称+异常
    @Test
    public void picScheduled3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String start_time = "2021-01-19";
            String end_time = "2021-01-22";
            int pictotal = md.picturePage("SCHEDULED",start_time,end_time,"",1,1,8).getInteger("total");
            int pages = md.picturePage("SCHEDULED",start_time,end_time,info.shop_id_01_chin,1,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SCHEDULED",start_time,end_time,info.shop_id_01_chin,1,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
                    String date_time = list.getJSONObject(j).getString("date_time");
                    String title = list.getJSONObject(j).getString("title");
                    int isAbnormal = list.getJSONObject(j).getInteger("is_abnormal");
                    String s = date_time.substring(0,10);
                    int a = s.compareTo(start_time);
                    int b = s.compareTo(end_time);
                    checkArgument(tips.contains("定检巡店"), "定检巡查" + "!=图片返回的类型" + tips);
                    checkArgument(a>=0&&b<=0, "定检巡查+日期" + "!=图片返回日期" + date_time);
                    checkArgument(title.contains(info.shop_id_01_chin), "输入的门店名称" + "!=返回的门店名称" + title);
                    checkArgument(isAbnormal==1, "异常图片" + "!=图片返回的状态" + isAbnormal);
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("定检巡查展示图片类型+日期+门店名称+异常==返回的图片类型+日期+门店名称+异常");
        }
    }



    //定检巡查展示图片类型+日期+门店名称+非异常==返回的图片类型+日期+门店名称+非异常
    @Test
    public void picScheduled4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String start_time = "2021-01-19";
            String end_time = "2021-01-22";
            int pictotal = md.picturePage("SCHEDULED",start_time,end_time,"",1,1,8).getInteger("total");
            int pages = md.picturePage("SCHEDULED",start_time,end_time,info.shop_id_01_chin,0,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SCHEDULED",start_time,end_time,info.shop_id_01_chin,0,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
                    String date_time = list.getJSONObject(j).getString("date_time");
                    String title = list.getJSONObject(j).getString("title");
                    int isAbnormal = list.getJSONObject(j).getInteger("is_abnormal");
                    String s = date_time.substring(0,10);
                    int a = s.compareTo(start_time);
                    int b = s.compareTo(end_time);
                    checkArgument(tips.contains("定检巡店"), "定检巡查" + "!=图片返回的类型" + tips);
                    checkArgument(a>=0&&b<=0, "定检巡查+日期" + "!=图片返回日期" + date_time);
                    checkArgument(title.contains(info.shop_id_01_chin), "输入的门店名称" + "!=返回的门店名称" + title);
                    checkArgument(isAbnormal==0, "异常图片" + "!=图片返回的状态" + isAbnormal);
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("定检巡查展示图片类型+日期+门店名称+异常==返回的图片类型+日期+门店名称+异常");
        }
    }


    //手动留痕展示图片个数==返回的数量
    @Test
    public void picSpot() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int pictotal = md.picturePage("SPOT","","","",null,1,8).getInteger("total");
            int pages = md.picturePage("SPOT","","","",null,1,8).getInteger("pages");
            int pagesize = (pages-1)*8;
            int pages_size = md.picturePage("SPOT","","","",null,pages,8).getInteger("page_size");

            CommonUtil.valueView(pictotal, pagesize+pages_size);
            checkArgument(pictotal == pagesize+pages_size, "图片中心总数" + pictotal + "!=搜索出图片的数量" + pagesize+pages_size);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手动留痕展示图片个数==返回的数量");
        }
    }


    //手动留痕展示图片类型==返回的图片类型
    @Test
    public void picSpot1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int pictotal = md.picturePage("SPOT","","","",null,1,8).getInteger("total");
            int pages = md.picturePage("SPOT","","","",null,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SPOT","","","",null,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
//                    int a = tips.indexOf("定检巡店");
                    checkArgument(tips.contains("现场巡店")||tips.contains("远程巡店"), "手动巡查" + "!=图片返回的类型" + tips);
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手动留痕展示图片类型==返回的图片类型");
        }
    }



    //手动展示图片类型+日期+门店名称+非异常==返回的图片类型+日期+门店名称+非异常
    @Test
    public void picSpot4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String start_time = "2021-01-19";
            String end_time = "2021-01-22";

            int pages = md.picturePage("SPOT",start_time,end_time,info.shop_id_01_chin,0,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SPOT",start_time,end_time,info.shop_id_01_chin,0,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
                    String date_time = list.getJSONObject(j).getString("date_time");
                    String title = list.getJSONObject(j).getString("title");
                    int isAbnormal = list.getJSONObject(j).getInteger("is_abnormal");
                    String s = date_time.substring(0,10);
                    int a = s.compareTo(start_time);
                    int b = s.compareTo(end_time);
                    checkArgument(tips.contains("现场巡店")||tips.contains("远程巡店"), "定检巡查" + "!=图片返回的类型" + tips);
                    checkArgument(a>=0&&b<=0, "手动留痕+日期" + "!=图片返回日期" + date_time);
                    checkArgument(title.contains(info.shop_id_01_chin), "输入的门店名称" + "!=返回的门店名称" + title);
                    checkArgument(isAbnormal==0, "异常图片" + "!=图片返回的状态" + isAbnormal);
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手动展示图片类型+日期+门店名称+异常==返回的图片类型+日期+门店名称+异常");
        }
    }

    @DataProvider(name = "CHKRESULT")
    public Object[] chkResult() {

        return new String[][]{
                {"REMOTE", "远程巡店", "1", "合格"},
                {"REMOTE", "远程巡店", "2", "不合格"},
                {"REMOTE", "远程巡店", "3", "不适用"},
                {"SPOT", "现场巡店", "1", "合格"},
                {"SPOT", "现场巡店", "2", "不合格"},
                {"SPOT", "现场巡店", "3", "不适用"},

        };
    }

    @DataProvider(name = "DJTYPE")
    public Object[] DJTYPE() {
        return new String[][]{
                {"SCHEDULE_TASK", "定检任务", "1", "合格"},
                {"SCHEDULE_TASK", "定检任务", "2", "不合格"},
                {"SCHEDULE_TASK", "定检任务", "3", "不适用"},
        };
    }


    @DataProvider(name = "XDTYPE")
    public Object[] xdtype() {

        return new String[][]{
                {"REMOTE", "远程巡店"},
                {"SPOT", "现场巡店"},

        };
    }

    @DataProvider(name = "BLACKTYPE")
    public Object[] blackType() {

        return new String[][]{
                {"0", "待办"},
                {"1", "已办"},
                {"2", "已过期"}
        };
    }


}




