package com.haisheng.framework.testng.bigScreen.itemXundian.caseonline.testShop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.hqq.fucPackage.XdPackageData;
import com.haisheng.framework.testng.bigScreen.itemXundian.util.StoreScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.itemXundian.util.XdPackageDataOnline;
import com.haisheng.framework.testng.bigScreen.itemXundian.util.XundianScenarioUtilOnline;
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
public class SalesVideo extends TestCaseCommon implements TestCaseStd {

    XundianScenarioUtilOnline xd = XundianScenarioUtilOnline.getInstance();
    StoreScenarioUtilOnline md = StoreScenarioUtilOnline.getInstance();
    XdPackageData xds = XdPackageData.getInstance();
    Long shop_id = 13260l;
    String comment = "自动化在进行处理，闲人走开";
    public String filepath = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/卡券图.jpg"; //巡店不合格图片base64



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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "青青";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "mendian-online-test");
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店(巡店) 线上");
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_VEDIO;
        commonConfig.pushRd = new String[]{"13581630214", "13604609869","13373166806"};
        commonConfig.shopId = getXunDianShopOnline(); //要改！！！
        beforeClassInit(commonConfig);
        logger.debug("xundian " + xd);
        xd.login("salesdemo@winsense.ai","c216d5045fbeb18bcca830c235e7f3c8");


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


    //(description = "salesdemo门店得两个设备回放流监控")
    @Test(dataProvider = "DEVICE_ID",dataProviderClass = XdPackageDataOnline.class)
    public void check_replay(String device_id,String device_name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date =dt.getHistoryDate(-1);
            String time = dt.getHHmm(0)+":00";
            JSONObject res = xd.device_replay(device_id,shop_id,date,time);
            Integer code = res.getInteger("code");
            JSONArray list = md.device_page("","",device_id,"","CAMERA",1,10).getJSONArray("list");
            String status_name = list.getJSONObject(0).getString("status_name");
            Preconditions.checkArgument(code == 1000, "salesdemo门店的回放视频播放报错了,设备名称:"+device_name+"  设备ID:"+device_id + "code :"+code);
            Preconditions.checkArgument(status_name.equals("运行中") , "salesdemo门店的回放视频播放报错了,设备名称:"+device_name+"  设备ID:"+device_id + "摄像头状态 :"+status_name);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            String failure = e.toString();
            if (failure.contains("java.lang.NullPointerException")) {
                failure = failure.replaceFirst("java.lang.NullPointerException", "接口网络连接失败");
                failure = failure.replaceAll("java.lang.NullPointerException", "");
            }
            appendFailReason(failure);
        } finally {
            saveData("salesdemo门店的回放情况");
        }
    }


   // @Test(dataProvider = "DEVICE_ID1",dataProviderClass = XdPackageDataOnline.class)//小天才宝龙店
    public void check_vedio1(String device_id1,String device_name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            md.login("xiaotiancai@xiaotiancai.com","de01cbdb4e06e9bbd91ccef41450b7dc");
            JSONObject res = xd.device_live(device_id1,15617l);
            Integer code = res.getInteger("code");
//            JSONArray list = md.device_page("","",device_id1,"","AI_CAMERA",1,10).getJSONArray("list");
//            String status_name = list.getJSONObject(0).getString("status_name");
            Preconditions.checkArgument(code == 1000, "小天才宝龙店门店的直播报错了,设备名称:"+device_name+"  设备ID:"+device_id1 + "code :"+code);
           // Preconditions.checkArgument(status_name.equals("运行中") , "salesdemo门店的直播报错了,设备ID:"+device_id1 + "摄像头状态 :"+status_name);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小天才宝龙店门店的直播情况");
        }
    }

   // @Test(dataProvider = "DEVICE_ID2",dataProviderClass = XdPackageDataOnline.class)//小天才西溪
    public void check_vedio2(String device_id2,String device_name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            md.login("xiaotiancai@xiaotiancai.com","de01cbdb4e06e9bbd91ccef41450b7dc");
            JSONObject res = xd.device_live(device_id2,15617l);
            Integer code = res.getInteger("code");
//            JSONArray list = md.device_page("","",device_id2,"","AI_CAMERA",1,10).getJSONArray("list");
//            String status_name = list.getJSONObject(0).getString("status_name");
            Preconditions.checkArgument(code == 1000, "小天才西溪门店的直播报错了,设备名称:"+device_name+"  设备ID:"+device_id2 + "code :"+code);
            //Preconditions.checkArgument(status_name.equals("运行中") , "salesdemo门店的直播报错了,设备ID:"+device_id2 + "摄像头状态 :"+status_name);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小天才西溪门店的直播情况");
        }
    }

    @Test(dataProvider = "DEVICE_ID3",dataProviderClass = XdPackageDataOnline.class)//德众赢
    public void check_vedio3(String device_id3,String device_name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            md.login("dezhongying@dezhongying.com","e369d98765f98e1690609b544f4bc230");
            JSONObject res = xd.device_live(device_id3,15694l);
            Integer code = res.getInteger("code");
            Preconditions.checkArgument(code == 1000, "德众赢门店的直播报错了,设备名称:"+device_name+"  设备ID:"+device_id3 + "code :"+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("德众赢门店的直播情况");
        }
    }

    @Test(dataProvider = "DEVICE_ID4",dataProviderClass = XdPackageDataOnline.class)//雷诺表
    public void check_vedio4(String device_id4,String device_name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            md.login("leinuobiao@leinuo.com","1843eee082b2956fac4920669b0dfc51");
            JSONObject res = xd.device_live(device_id4,18176l);
            Integer code = res.getInteger("code");
            Preconditions.checkArgument(code == 1000, "雷诺表门店的直播报错了,设备名称:"+device_name+"  设备ID:"+device_id4 + "code :"+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("雷诺表门店的直播情况");
        }
    }

}
