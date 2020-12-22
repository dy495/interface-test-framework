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
 * @description :app 相关case --xia
 * @date :2020/6/29 20:14
 **/


public class XundianAppData extends TestCaseCommon implements TestCaseStd {

    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    XdPackageData xds = XdPackageData.getInstance();
    int page = 1;
    int size = 50;
    String comment = "自动化在进行处理，闲人走开";
    public String filepath = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/卡券图.jpg";  //巡店不合格图片base64

    //读取文件内容
    public String texFile(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str = in.readLine();
        return str;
    }

    public String getPicList(String filename) throws Exception {
        String pic_data0 = texFile(filepath);
        JSONObject pic = xd.picUpload(1, pic_data0);
        String pic_list0 = pic.getString("pic_path");
        return pic_list0;
    }


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
            Integer un_finished_total = (Integer) xds.getTab_total(page, size, 0, null).get("total");
            Long shop_id = xds.getId_ShopId(list,"HANDLE_RESULT").get("shop_id");
            Long id =  xds.getId_ShopId(list,"HANDLE_RESULT").get("id");
            //通过base64接口上传留痕图片
            JSONArray pic_list= xds.getPicPath();
            //该次是处理复检事项，所以不需要上传留痕图片
            xd.MstepSumit(shop_id, id, comment, null, 1);
            //获取处理完结以后的已处理事项
            Integer finished_total = (Integer) xds.getTab_total(page, size, 1, null).get("total");


//            Preconditions.checkArgument(
//                    total == un_finished_total,
//                    "【待办事项】中[未完成]列表的数量"+total+"！==【个人中心】中[未完成]的待办事项的的展示项" + un_finished_total
//            );
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
//            saveData("【待办事项】中[未完成]列表的数量==【个人中心】中[未完成]的待办事项的的展示项");
        }
    }
}
