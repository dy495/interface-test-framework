package com.haisheng.framework.testng.bigScreen.xundianDaily.hqq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage.StoreFuncPackage;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage.XdPackageData;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.ImageUtil;
import org.springframework.util.StringUtils;
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description :app 相关case
 * @date :2020/12/22 16:14
 **/
public class XundianAppData extends TestCaseCommon implements TestCaseStd {

    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    XdPackageData xds = XdPackageData.getInstance();
    int page = 1;
    int size = 50;
    String comment = "自动化在进行处理，闲人走开";
    public String filepath = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/卡券图.jpg";  //巡店不合格图片base64

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
        commonConfig.pushRd = new String[]{"13581630214", "15084928847"};
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

    @Override
    public void createFreshCase(Method method) {

    }

    @Test(description = "【待办事项】中[未完成]列表的数量==【个人中心】中[未完成]的待办事项的的展示项")
    public void un_finished_total() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待办事项列表中【待处理】的数量
            int total = xd.task_list(page, size, 0, null).getInteger("total");
            //获取个人中心中【未完成待办事项】的数量
            int un_finished_total = xd.user_center().getInteger("un_finished_total");
            Preconditions.checkArgument(
                    total == un_finished_total,
                    "【待办事项】中[未完成]列表的数量" + total + "！==【个人中心】中[未完成]的待办事项的的展示项" + un_finished_total
            );
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【待办事项】中[未完成]列表的数量==【个人中心】中[未完成]的待办事项的的展示项");
        }
    }

    @Test(description = "将【待办事项】中[未完成]列表的待处理进行处理为合格或不合格==[未完成]列表-1&&【已完成】列表+1")
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
            xd.MstepSumit(shop_id, id, comment, null, 1);
            Integer finished_total2 = (Integer) xds.getTab_total(page, size, 1, null).get("total");
            Integer un_finished_total2 = (Integer) xds.getTab_total(page, size, 0, null).get("total");
            Preconditions.checkArgument(
                    finished_total2-finished_total1 == 1,
                    "【待办事项】中待处理进行处理为合格或不合格,【已完成】列表没有+1"
            );
            Preconditions.checkArgument(
                    un_finished_total1-un_finished_total2 == 1,
                    "【待办事项】中待处理进行处理为合格或不合格,[未完成]列表没有-1"
            );
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
         saveData("将【待办事项】中[未完成]列表的待处理进行处理为合格或不合格==[未完成]列表-1&&【已完成】列表+1");
        }
    }

    @Test(description = "将【待办事项】中[未完成]中的定检任务进行处理==PC【巡店中心】巡店次数+1 && pc【巡店报告中心】的报告记录数据+1 && APP【巡店中心】累计报告数量+1")
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
            //通过base64接口上传留痕图片
            JSONArray pic_list= xds.getPicPath();
            Map<String, Object> map = xds.Scheduled(shop_id, 1, id);
            long patrol_id = (long) map.get("patrol_id");
            long list_id = (long) map.get("list_id");
            long item_id = (long) map.get("item_id");
            xd.checks_item_submit(shop_id,patrol_id,list_id,item_id,2,"自动化处理为不合格",pic_list);
            xd.checks_submit(shop_id,patrol_id,"自动化处理全部不合格");

            int total2 = xd.xd_report_list("","","","",null,page,size).getInteger("total");
            JSONObject data2 = xd.ShopPage(page,size);
            Integer patrol_num2 = xds.patrol_num(data2,shop_id);
            Preconditions.checkArgument(
                    total2-total1 == 1,
                    "将【待办事项】中[未完成]中的定检任务进行处理,pc【巡店报告中心】的报告记录数据没有+1 "
            );
            Preconditions.checkArgument(
                    patrol_num2-patrol_num1 == 1,
                    "将【待办事项】中[未完成]中的定检任务进行处理,PC【巡店中心】巡店次数没有+1"
            );
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("将【待办事项】中[未完成]中的定检任务进行处理==PC【巡店中心】巡店次数+1 && pc【巡店报告中心】的报告记录数据+1 && APP【巡店中心】累计报告数量+1");
        }
    }


}
