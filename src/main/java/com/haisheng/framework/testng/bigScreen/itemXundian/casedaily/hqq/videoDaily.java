package com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.hqq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemXundian.util.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.util.XundianScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.hqq.fucPackage.XdPackageData;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * @description :app 相关case
 * @date :2020/12/22 16:14
 **/
public class videoDaily extends TestCaseCommon implements TestCaseStd {

    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    XdPackageData xds = XdPackageData.getInstance();
    StoreScenarioUtil md = StoreScenarioUtil.getInstance();
    Long shop_id = 43072l;
    String comment = "自动化在进行处理，闲人走开";
    public String filepath = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/multimedia/picture/卡券图.jpg"; //巡店不合格图片base64


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_XUNDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";

        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "巡店 日常");
        commonConfig.pushRd = new String[]{ "13604609869","13373166806"};

        commonConfig.shopId = getXundianShop(); //要改！！！
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

    //(description = "43072门店得两个设备直播流监控")
    @Test(dataProvider = "DEVICE_ID", dataProviderClass = XdPackageData.class)
    public void check_vedio_aiTest(String device_id, String device_name,String divice_type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = xd.device_live(device_id, shop_id);
            Integer code = res.getInteger("code");
            JSONArray list = md.device_page("", "", device_id, "", "CAMERA", 1, 10).getJSONArray("list");
            String status_name = list.getJSONObject(0).getString("status_name");
            Preconditions.checkArgument(code == 1000, "43072门店的直播报错了,设备名称:" + device_name + "  设备ID:" + device_id + "code :" + code);
           Preconditions.checkArgument(status_name.equals("运行中"), "43072门店的直播报错了,设备名称:" + device_name + "  设备ID:" + device_id + "摄像头状态 :" + status_name);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("43072门店的直播情况");
        }
    }

    //(description = "43072门店得两个设备回放流监控")
    @Test(dataProvider = "DEVICE_ID",dataProviderClass = XdPackageData.class)
    public void check_replay_aiTest(String device_id,String device_name,String divice_type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date =dt.getHistoryDate(-1);
            String time = dt.getHHmm(0)+":00";
            JSONObject res = xd.device_replay(device_id,shop_id,date,time);
            Integer code = res.getInteger("code");
            JSONArray list = md.device_page("","",device_id,"","CAMERA",1,10).getJSONArray("list");
            String status_name = list.getJSONObject(0).getString("status_name");
            Preconditions.checkArgument(code == 1000, "43072门店的回放视频播放报错了,设备名称:"+device_name+"  设备ID:"+device_id + "code :"+code);
            Preconditions.checkArgument(status_name.equals("运行中") , "43072门店的回放视频播放报错了,设备名称:"+device_name+"  设备ID:"+device_id + "摄像头状态 :"+status_name);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("43072门店的回放情况");
        }
    }
}