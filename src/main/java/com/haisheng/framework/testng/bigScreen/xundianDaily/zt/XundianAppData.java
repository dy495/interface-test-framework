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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.MENDIAN_DAILY.getName());
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
            // 获取巡检员id
            JSONObject shop_id = xd.authShopInspectors(info.shop_id);
            JSONArray xjyId = shop_id.getJSONArray("id");
            String xjyId1 = String.valueOf(xjyId);
            // 获取门店巡店记录列表total总数
            JSONObject shopCHeckStatus = xd.getShopChecksPage(info.shop_id, null, null, "越秀测试账号", xjyId1, "DESC", 10, null);
            Integer checks_list = shopCHeckStatus.getInteger("total");
            // 巡店记录处理下拉框
            JSONArray list_Type = xd.handleStatusList().getJSONArray("list");
            int Unwanted_TypeNum = 0;//无需处理数量
            int deal_TypeNum = 0;//已处理数量
            int pending_TypeNum = 0;//待处理数量
            for (int i = 0; i < list_Type.size(); i++) {
                int typeNum = list_Type.getJSONObject(i).getInteger("type");
                if (typeNum == 0) {
                    Unwanted_TypeNum = Unwanted_TypeNum + 1;
                }
                if (typeNum == 1) {
                    deal_TypeNum = deal_TypeNum + 1;
                }
                if (typeNum == 2) {
                    pending_TypeNum = pending_TypeNum + 1;
                }

            }
            int listNum = Unwanted_TypeNum + deal_TypeNum + pending_TypeNum;//三项之和
            Preconditions.checkArgument(checks_list == listNum, "巡店记录列表数量" + checks_list + "不等于待处理+已处理+无需处理的数量=" + listNum);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app现场、远程巡店 处理结果全部合格，不产生待办事项");
        }
    }


    //不合格报告+合格报告 == 列表下全部报告
    @Test
    public void getResultTypeList() {
        try {
            // 获取巡检员id
            JSONObject shop_id = xd.authShopInspectors(info.shop_id);
            String xjyid = shop_id.getString("id");
            // 获取门店巡店记录列表total总数
            JSONObject shopCHeckStatus = xd.getShopChecksPage(info.shop_id, null, null, "越秀测试账号", xjyid, "DESC", null, null);
            Integer checks_list = shopCHeckStatus.getInteger("total");
            // 巡店记录处理下拉框
            JSONArray list_Num = xd.resultTypeList().getJSONArray("list");
            int qualified_Num = 0;
            int unqualified_Num = 0;
            for (int i = 0; i < list_Num.size(); i++) {
                int type_Num = list_Num.getJSONObject(i).getInteger("type");
                if (type_Num == 0) {
                    qualified_Num = qualified_Num + 1;
                }
                if (type_Num == 1) {
                    unqualified_Num = unqualified_Num + 1;
                }
            }
            int result_Type = qualified_Num + unqualified_Num;
            Preconditions.checkArgument(checks_list == result_Type, "巡店记录列表数量" + checks_list + "不等于合格+不合格的数量=" + result_Type);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app现场、远程巡店 处理结果全部合格，不产生待办事项");
        }
    }


    //巡店记录详情内容==PC【巡店报告详情】中的巡店记录详情内容
    @Test
    public void getShopChecksDetail() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取shop_id
            int page = 1;
            JSONArray check_list = xd.ShopPage(page, size).getJSONArray("list");
            int patrol_num = check_list.getJSONObject(0).getInteger("patrol_num");
            int shop_id = check_list.getJSONObject(0).getInteger("id");

            //获取pc报告id
            JSONArray detailList = xd.shopChecksPage(page, size, shop_id).getJSONArray("list");
            int id = detailList.getJSONObject(0).getInteger("id");
            // 获取报告中不合格不合格项数，合格项数，不适用项数,巡店者，提交说明
            int inappropriate_num = xd.shopChecksDetail(id, shop_id).getInteger("inappropriate_num");
            int qualified_num = xd.shopChecksDetail(id, shop_id).getInteger("qualified_num");
            int unqualified_num = xd.shopChecksDetail(id, shop_id).getInteger("unqualified_num");
            String inspector_names = xd.shopChecksDetail(id, shop_id).getString("inspector_name");
            String submit_comment_list = xd.shopChecksDetail(id, shop_id).getString("submit_comment");
            String check_type0 = xd.shopChecksDetail(id, shop_id).getString("check_type");
//            app端报告
            JSONObject id1 = xd.authShopInspectors(info.shop_id);
            String xjyid = id1.getString("id");
            JSONArray shopCHeckStatus = xd.getShopChecksPage(info.shop_id, null, null, inspector_names, xjyid, "DESC", null, null).getJSONArray("list");
            Long id2 = shopCHeckStatus.getJSONObject(0).getLong("id");

            JSONObject shopCheck = xd.getShopChecksDetail(id2, info.shop_id, null, null);
            int inappropriate_num1 = shopCheck.getInteger("inappropriate_num");
            int qualified_num1 = shopCheck.getInteger("qualified_num");
            int unqualified_num1 = shopCheck.getInteger("unqualified_num");
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
            saveData("app现场、远程巡店 处理结果全部合格，不产生待办事项");
        }
    }


    //未完成的定检任务进行处理，pc巡店中心巡店次数+1，巡店报告中心数据+1，app报告中心+1
    @Test(dataProvider = "DJTYPE")
    public void djXunDian(String type, String chinesetype, String result, String mes) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取门店巡店次数
            int bef1 = xd.xunDianCenterselect(1, 1, "赢识办公室(测试越秀/飞单)").getJSONArray("list").getJSONObject(0).getInteger("patrol_num");
            //获取巡店报告中心报告总数
            int bef2 = xd.xd_report_list(null, "赢识办公室(测试越秀/飞单)", null, null, null, 1, 1).getInteger("total");

            int bef3 = xd.getShopChecksPage(info.shop_id, null, null, "", "", "DESC", null, null).getInteger("total");
            //定检任务列表
            JSONArray dj = xd.scheduleCheckPage(1, 10).getJSONArray("list");
            //获取定检任务id
            Long djid = dj.getLong(0);
            //开始一次定检巡店
            info.djXdOperate(info.shop_id, type, 1, Integer.parseInt(result), djid);

            int after1 = xd.xunDianCenterselect(1, 1, "赢识办公室(测试越秀/飞单)").getJSONArray("list").getJSONObject(0).getInteger("patrol_num");
            int after2 = xd.xd_report_list(null, "赢识办公室(测试越秀/飞单)", null, null, null, 1, 1).getInteger("total");
            int after3 = xd.getShopChecksPage(info.shop_id, null, null, "", "", "DESC", null, null).getInteger("total");


            int add1 = after1 - bef1;
            int add2 = after2 - bef2;
            int add3 = after3 - bef3;
            Preconditions.checkArgument(add1 == 1, "PC【巡店中心】巡店次数 期待增加1，实际增加" + add1);
            Preconditions.checkArgument(add2 == 1, "PC【巡店报告中心】巡店报告 期待增加1，实际增加" + add2);
            Preconditions.checkArgument(add3 == 1, "app【巡店记录】巡店报告 期待增加1，实际增加" + add3);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【" + chinesetype + "】提交一个" + mes + "报告，PC【巡店中心】巡店次数+1、PC【巡店报告中心】巡店报告+1、app【巡店记录】巡店报告+1");
        }
    }

    //[未完成]列表的数量==未完成的待办事项的的展示项
    @Test
    public void wwcSum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待办列表事项总数totalsum
            Long totalNum = xd.task_list(null, null, null, null).getLong("total");
            JSONArray list_size = xd.task_list(null, null, null, null).getJSONArray("list");
            checkArgument(totalNum == list_size.size(), "未完成列表数量" + totalNum + "!=未完成的待办事项的展示项" + list_size.size());
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            //        saveData("【" + chinesetype + "】提交一个" + mes + "报告，PC【巡店中心】巡店次数+1、PC【巡店报告中心】巡店报告+1、app【巡店记录】巡店报告+1");
        }
    }


//    app账号下当前门店数量==pc该账号下巡店中心列表的数量

    @Test
    public void mdNum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer appMdNum = md.app_shopNum().getInteger("shop_count");
            Integer pcMdNum = md.patrolShopPageV3("", 1, 10).getInteger("total");
            checkArgument(appMdNum == pcMdNum, "app账号下当前门店数量" + appMdNum + "pc该账号下巡店中心列表的数量" + pcMdNum);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            //        saveData("【" + chinesetype + "】提交一个" + mes + "报告，PC【巡店中心】巡店次数+1、PC【巡店报告中心】巡店报告+1、app【巡店记录】巡店报告+1");
        }

    }


//
//    //个人中心【待办事项】未完成、已完成、已过期列表下所对应的事项
//    @Test
//    public void backlogNum(String type,String mes){
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray list = xd.task_list(100,100,Integer.parseInt(type),null).getJSONArray("list");
//            for(int i=0;i<list.size();i++){
//                JSONObject obj = list.getJSONObject(i);
//
//            }
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            //        saveData("【" + chinesetype + "】提交一个" + mes + "报告，PC【巡店中心】巡店次数+1、PC【巡店报告中心】巡店报告+1、app【巡店记录】巡店报告+1");
//        }
//
//    }


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




