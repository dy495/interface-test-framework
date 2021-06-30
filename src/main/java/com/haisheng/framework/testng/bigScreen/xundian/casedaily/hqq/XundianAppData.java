package com.haisheng.framework.testng.bigScreen.xundian.casedaily.hqq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundian.casedaily.hqq.fucPackage.XdPackageData;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * @description :app 相关case
 * @date :2020/12/22 16:14
 **/
public class XundianAppData extends TestCaseCommon implements TestCaseStd {

    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
   // MendianInfo info = new MendianInfo();
    XdPackageData xds = XdPackageData.getInstance();
    int page = 1;
    int size = 50;
    String comment = "自动化在进行处理，闲人走开";
    public String filepath = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/卡券图.jpg"; //巡店不合格图片base64

//    //读取文件内容
//    public String texFile(String fileName) throws IOException {
//        BufferedReader in = new BufferedReader(new FileReader(fileName));
//        String str = in.readLine();
//        return str;
//    }
//

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 日常");
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"13604609869","13373166806"};
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);
        logger.debug("store " + xd);
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

    @Test(description = "【待办事项】中[未完成]列表的数量==【个人中心】中[未完成]的待办事项的的展示项")
    public void un_finished_total() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待办事项列表中【待处理】的数量
            int total = xd.task_list(page, size, 0, null).getInteger("total");
            //获取个人中心中【未完成待办事项】的数量
            int un_finished_total = xd.user_center().getInteger("un_finished_total");
            Preconditions.checkArgument(total == un_finished_total, "【待办事项】中[未完成]列表的数量" + total + "！==【个人中心】中[未完成]的待办事项的的展示项" + un_finished_total);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【待办事项】中[未完成]列表的数量==【个人中心】中[未完成]的待办事项的的展示项");
        }
    }

   // @Test(description = "将【待办事项】中[未完成]列表的待处理进行处理为合格或不合格==[未完成]列表-1&&【已完成】列表+1")
    public void dealAfterData() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取【待办事项】待处理的个数、进行处理需要用到的参数
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            Integer finished_total1 = (Integer) xds.getTab_total(page, size, 1, null).get("total");
            Integer un_finished_total1 = (Integer) xds.getTab_total(page, size, 0, null).get("total");
            Long shop_id = xds.getId_ShopId(list,"HANDLE_RESULT").get("shop_id");
            Long id =  xds.getId_ShopId(list,"HANDLE_RESULT").get("id");
            //该次是处理复检事项，所以不需要上传留痕图片
            xd.task_step_submit(shop_id, id, null, 1,comment);
            Integer finished_total2 = (Integer) xds.getTab_total(page, size, 1, null).get("total");
            Integer un_finished_total2 = (Integer) xds.getTab_total(page, size, 0, null).get("total");
            Preconditions.checkArgument(finished_total2-finished_total1 == 1, "【待办事项】中待处理进行处理为合格或不合格,【已完成】列表没有+1");
            Preconditions.checkArgument(un_finished_total1-un_finished_total2 == 1, "【待办事项】中待处理进行处理为合格或不合格,[未完成]列表没有-1");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
         saveData("将【待办事项】中[未完成]列表的待处理进行处理为合格或不合格==[未完成]列表-1&&【已完成】列表+1");
        }
    }

    //@Test(description = "将【待办事项】中[未完成]中的定检任务进行处理==PC【巡店中心】巡店次数+1 && pc【巡店报告中心】的报告记录数据+1 && APP【巡店中心】累计报告数量+1")
    public void dealAfterData_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取【待办事项】待处理的个数、进行处理需要用到的参数
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            Long shop_id = xds.getId_ShopId(list,"SCHEDULE_TASK").get("shop_id");
            Long id =  xds.getId_ShopId(list,"SCHEDULE_TASK").get("id");

            int total1 = xd.xd_report_list("","","","",null,page,size).getInteger("total");
            JSONObject data1 = xd.ShopPage(page,size);
            Integer patrol_num1 = xds.patrol_num(data1,shop_id);
            Long patrol_id = xds.Scheduled(shop_id, 1, id,"SCHEDULED",2,0);
            int code = xd.checks_submit(shop_id,patrol_id,"自动化处理全部不合格").getInteger("code");
            Preconditions.checkArgument(code ==1000, "定检巡店提交失败"+code);
            int total2 = xd.xd_report_list("","","","",null,page,size).getInteger("total");
            JSONObject data2 = xd.ShopPage(page,size);
            Integer patrol_num2 = xds.patrol_num(data2,shop_id);
            Preconditions.checkArgument(total2-total1 == 1, "将【待办事项】中[未完成]中的定检任务进行处理,pc【巡店报告中心】的报告记录数据没有+1 ");
            Preconditions.checkArgument(patrol_num2-patrol_num1 == 1, "将【待办事项】中[未完成]中的定检任务进行处理,PC【巡店中心】巡店次数没有+1");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("将【待办事项】中[未完成]中的定检任务进行处理==PC【巡店中心】巡店次数+1 && pc【巡店报告中心】的报告记录数据+1 && APP【巡店中心】累计报告数量+1");
        }
    }

    @Test(description = "累计报告数量==该品牌下所有门店从上线至今提交的所有巡店报告数量")
    public void checkInfoData() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = xd.xd_report_list("","","","",null,page,size).getInteger("total");
            JSONObject object = xd.patrol_center().getJSONObject("total_patrol_result");
            int total_patrol_number = object.getInteger("total_patrol_number");
            Preconditions.checkArgument(total_patrol_number ==total , "PC【巡店报告中心】的报告数量="+total+"。APP【巡店中心】中的累计提交报告="+total_patrol_number);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("累计报告数量==该品牌下所有门店从上线至今提交的所有巡店报告数量");
        }
    }
    @Test(description = "APP【巡店中心】今日巡店数==PC【巡店分析】中的今日巡店数")
    public void checkInfoData1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int today_patrol_shop_number = xd.xd_analysis_data().getInteger("today_patrol_shop_number");
            JSONObject object = xd.patrol_center().getJSONObject("today_patrol_result");
            int patrol_shop_number = object.getInteger("patrol_shop_number");//APP中今日巡店数
            Preconditions.checkArgument(patrol_shop_number ==today_patrol_shop_number , "APP【巡店中心】今日巡店数="+patrol_shop_number+"PC【巡店分析】中的今日巡店数="+today_patrol_shop_number);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("APP【巡店中心】今日巡店数==PC【巡店分析】中的今日巡店数");
        }
    }
    @Test(description = "APP【巡店中心】今日覆盖率==PC【巡店分析】中的今日覆盖率")
    public void checkInfoData2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String today_patrol_coverage_rate = xd.xd_analysis_data().getString("today_patrol_coverage_rate");
            JSONObject object = xd.patrol_center().getJSONObject("today_patrol_result");
            String patrol_coverage_rate = object.getString("patrol_coverage_rate");//APP中今日覆盖率
            Preconditions.checkArgument(patrol_coverage_rate.equals(today_patrol_coverage_rate) , "APP【巡店中心】今日覆盖率="+patrol_coverage_rate+"PC【巡店分析】中的今日覆盖率="+today_patrol_coverage_rate);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("APP【巡店中心】今日覆盖率==PC【巡店分析】中的今日覆盖率");
        }
    }

    @Test(description = "APP【巡店中心】今日合格率==PC【巡店分析】中的今日合格率")
    public void checkInfoData3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String today_patrol_pass_rate = xd.xd_analysis_data().getString("today_patrol_pass_rate");
            JSONObject object = xd.patrol_center().getJSONObject("today_patrol_result");
            String patrol_pass_rate = object.getString("patrol_pass_rate");//APP中今日合格率
            Preconditions.checkArgument(patrol_pass_rate.equals(today_patrol_pass_rate) , "APP【巡店中心】今日合格率="+patrol_pass_rate+"PC【巡店分析】中的今日合格率="+today_patrol_pass_rate);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("APP【巡店中心】今日合格率==PC【巡店分析】中的今日合格率");
        }
    }

    @Test(description = "APP【巡店中心】累计合格率==提交时巡店结果为合格的巡店报告数量/所有门店从上线至今提交的所有巡店报告数量")
    public void checkInfoData4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total1 = xd.xd_report_list("","","","",null,page,size).getInteger("total");
            int total2 = xd.xd_report_list("","","QUALIFIED","",null,page,size).getInteger("total");
            String ss=  CommonUtil.getPercent(total2,total1,4);
            String pass_rate =  ss.replace("%","");
            JSONObject object = xd.patrol_center().getJSONObject("total_patrol_result");
            String total_patrol_pass_rate = object.getString("total_patrol_pass_rate");//APP累计合格率
            Preconditions.checkArgument(total_patrol_pass_rate.equals(pass_rate) , "APP【巡店中心】累计合格率"+total_patrol_pass_rate+"根据公式计算出来的累计合格率="+pass_rate);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("APP【巡店中心】今日合格率==PC【巡店分析】中的今日合格率");
        }
    }
}
