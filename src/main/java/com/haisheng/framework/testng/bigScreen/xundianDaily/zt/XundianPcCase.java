package com.haisheng.framework.testng.bigScreen.xundianDaily.zt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class XundianPcCase extends TestCaseCommon implements TestCaseStd {
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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.XD_DAILY.getDesc());
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
        commonConfig.shopId = EnumTestProduce.XD_DAILY.getShopId();
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

    //pc特殊截屏（四张图片截取成功）+事件时间
    @Test
    public void problemMark() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //JSONObject list = xd.checkStart("\"REMOTE\"", 1); //进入远程巡店
            JSONObject data = xd.problemeItems();

            JSONArray check_lists = data.getJSONArray("list");  //执行清单列表
            long listId = check_lists.getJSONObject(0).getInteger("id"); //取一个执行清单
            //取执行清单的一个执行项
            long itemId = check_lists.getJSONObject(0).getJSONArray("check_items").getJSONObject(0).getLong("id");
            //截屏图片
            JSONArray pic_list = info.getpicFour(0);
            //获取整改处理人
            String responsorId = xd.problemesponsors().getJSONArray("list").getJSONObject(0).getString("id");
            String audit_comment = "pc 截屏留痕推送给门店负责人";
            xd.problemMarkTime("uid_91df0ddd", listId, itemId, pic_list,43072, audit_comment,20);
//            checkArgument(res.getInteger("code") == 1000, "截图四张失败message+"+res.getString("message"));
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc特有截屏留痕");
        }
    }


//    @Test
//    public void problemMark1() throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //JSONObject list = xd.checkStart("\"REMOTE\"", 1); //进入远程巡店
//            JSONObject data = xd.problemeItems();
//
//            //截屏图片
//            JSONArray pic_list = info.getpicFour(0);
//            //获取整改处理人
//            String responsorId = xd.problemesponsors().getJSONArray("list").getJSONObject(0).getString("id");
//            String audit_comment = "pc 截屏留痕推送给门店负责人";
//            JSONObject res =  xd.problemMarkTime(responsorId, null, null, pic_list,43072, audit_comment,20);
//            checkArgument(res.getString("source").equals("配置清单id不能为空"), "执行项为空特殊留痕也成功了message+"+res.getString("message"));
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("pc执行项为空特殊留痕也成功了");
//        }
//    }
//
//
//    @Test
//    public void problemMark2() throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //JSONObject list = xd.checkStart("\"REMOTE\"", 1); //进入远程巡店
//            JSONObject data = xd.problemeItems();
//
//            JSONArray check_lists = data.getJSONArray("list");  //执行清单列表
//            long listId = check_lists.getJSONObject(0).getInteger("id"); //取一个执行清单
////            取执行清单的一个执行项
//            long itemId = check_lists.getJSONObject(0).getJSONArray("check_items").getJSONObject(0).getLong("id");
//            //截屏图片
//            JSONArray pic_list = info.getpicFour(0);
//            //获取整改处理人
//            String responsorId = xd.problemesponsors().getJSONArray("list").getJSONObject(0).getString("id");
//            String audit_comment = "pc 截屏留痕推送给门店负责人";
//            JSONObject res =  xd.problemMarkTime("", listId, itemId, pic_list, 43072,audit_comment,20);
//            checkArgument(res.getInteger("code") != 1000, "处理人为空也成功了message+"+res.getString("message"));
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("pc特殊截屏处理人为空也成功了");
//        }
//    }
//
//
//    @Test
//    public void problemMark3() throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //JSONObject list = xd.checkStart("\"REMOTE\"", 1); //进入远程巡店
//            JSONObject data = xd.problemeItems();
//
//            JSONArray check_lists = data.getJSONArray("list");  //执行清单列表
//            long listId = check_lists.getJSONObject(0).getInteger("id"); //取一个执行清单
////            取执行清单的一个执行项
//            long itemId = check_lists.getJSONObject(0).getJSONArray("check_items").getJSONObject(0).getLong("id");
//            //截屏图片
//            JSONArray pic_list = info.getpicFour(0);
//            //获取整改处理人
//            String responsorId = xd.problemesponsors().getJSONArray("list").getJSONObject(0).getString("id");
////            String audit_comment = "pc 截屏留痕推送给门店负责人";
//            String audit_comment = "审核意见" + CommonUtil.getRandom(100);
//            JSONObject res =  xd.problemMarkTime(responsorId, listId, itemId, pic_list, 43072,audit_comment,20);
//            checkArgument(res.getInteger("code") != 1000, "处理意见大于100字也成功了message+"+res.getString("message"));
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("pc特殊截屏处理意见大于100字也成功了");
//        }
//    }
//
//    @Test
//    public void problemMark4() throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //JSONObject list = xd.checkStart("\"REMOTE\"", 1); //进入远程巡店
//            JSONObject data = xd.problemeItems();
//
//            JSONArray check_lists = data.getJSONArray("list");  //执行清单列表
//            long listId = check_lists.getJSONObject(0).getInteger("id"); //取一个执行清单
////            取执行清单的一个执行项
//            long itemId = check_lists.getJSONObject(0).getJSONArray("check_items").getJSONObject(0).getLong("id");
//            //截屏图片
//            JSONArray pic_list = info.getpicFour(0);
//            //获取整改处理人
//            String responsorId = xd.problemesponsors().getJSONArray("list").getJSONObject(0).getString("id");
////            String audit_comment = "pc 截屏留痕推送给门店负责人";
//            String audit_comment = "审核意见" + CommonUtil.getRandom(100);
//            JSONObject res =  xd.problemMarkTime(responsorId, listId, itemId, pic_list,43072,"" ,20);
//            checkArgument(res.getInteger("code") != 1000, "处理意见为空也成功了message+"+res.getString("message"));
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("pc特殊截屏处理意见为空");
//        }
//    }
//
//
//    @Test
//    public void problemMark5() throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //JSONObject list = xd.checkStart("\"REMOTE\"", 1); //进入远程巡店
//            JSONObject data = xd.problemeItems();
//
//            JSONArray check_lists = data.getJSONArray("list");  //执行清单列表
//            long listId = check_lists.getJSONObject(0).getInteger("id"); //取一个执行清单
////            取执行清单的一个执行项
//            long itemId = check_lists.getJSONObject(0).getJSONArray("check_items").getJSONObject(0).getLong("id");
//            //截屏图片
////            JSONArray pic_list = info.getpicFour(0);
//            JSONArray pic_list = new JSONArray();
//            //获取整改处理人
//            String responsorId = xd.problemesponsors().getJSONArray("list").getJSONObject(0).getString("id");
//          String audit_comment = "pc 截屏留痕推送给门店负责人";
//            JSONObject res =  xd.problemMarkTime(responsorId, listId, itemId, pic_list,43072, audit_comment,20);
//            checkArgument(res.getInteger("code") != 1000, "图片为空也成功了message+"+res.getString("message"));
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("pc特殊截屏没有图片");
//        }
//    }
}




