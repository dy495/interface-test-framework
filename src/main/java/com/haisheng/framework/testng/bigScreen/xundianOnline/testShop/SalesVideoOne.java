package com.haisheng.framework.testng.bigScreen.xundianOnline.testShop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage.XdPackageData;
import com.haisheng.framework.testng.bigScreen.xundianOnline.StoreScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.xundianOnline.XdPackageDataOnline;
import com.haisheng.framework.testng.bigScreen.xundianOnline.XundianScenarioUtilOnline;
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
public class SalesVideoOne extends TestCaseCommon implements TestCaseStd {

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
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"13581630214", "15084928847"};
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
//(description = "salesdemo门店得两个设备直播流监控")
    @Test(dataProvider = "DEVICE_ID",dataProviderClass = XdPackageDataOnline.class)
    public void check_vedio(String device_id) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = xd.device_live(device_id,shop_id);
            Integer code = res.getInteger("code");
            JSONArray list = md.device_page("","",device_id,"","CAMERA",1,10).getJSONArray("list");
            String status_name = list.getJSONObject(0).getString("status_name");
            Preconditions.checkArgument(code == 1000, "salesdemo门店的直播报错了,设备ID:"+device_id + "code :"+code);
            Preconditions.checkArgument(status_name.equals("运行中") , "salesdemo门店的直播报错了,设备ID:"+device_id + "摄像头状态 :"+status_name);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("salesdemo门店的直播情况");
        }
    }


}
