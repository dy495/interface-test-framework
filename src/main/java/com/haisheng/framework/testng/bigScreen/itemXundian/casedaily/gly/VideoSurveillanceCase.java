package com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.gly;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.gly.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.equipmentmanagement.auth.AllDeviceListScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.equipmentmanagement.device.DevicePageScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.UserUtil;
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

public class VideoSurveillanceCase extends TestCaseCommon implements TestCaseStd {
    private final EnumTestProduce product = EnumTestProduce.XD_DAILY;
    public VisitorProxy visitor = new VisitorProxy(product);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    BusinessUtil businessUtil = new BusinessUtil(visitor);
    StoreScenarioUtil su = StoreScenarioUtil.getInstance();
    public Long shopId = 28758L;
    public String shopName = "巡店测试门店1";
    CommonConfig commonConfig = new CommonConfig();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_XUNDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.GLY.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.product = product.getAbbreviation();
        commonConfig.shopId = product.getShopId();
        commonConfig.referer = product.getReferer();
        beforeClassInit(commonConfig);
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
//        user.loginPc(AccountEnum.YUE_XIU_DAILY);
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        su.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
    }


    /**
     * [视频监控]摄像头数量=【设备管理】所有状态设备和
     */
    @Test(description = "[视频监控]摄像头数量=【设备管理】所有状态设备和")
    public void videoSurveillanceData1(){
       try{
           //获取视频监控中门店的列表
           IScene scene= AllDeviceListScene.builder().build();
           JSONObject response=visitor.invokeApi(scene,true).getJSONObject("total_status");
           System.err.println(response);
           //获取视频监控中的摄像头总数量
           String[] deviceArray=response.getString("device").split("/");
           int deviceSum= Integer.parseInt(deviceArray[1]);
           System.out.println("deviceSum:"+deviceSum);
           //设备管理中的列表条数
           IScene scene1= DevicePageScene.builder().page(1).size(10).type("CAMERA").build();
           JSONObject response1=visitor.invokeApi(scene1,true);
           //摄像头的总数量
           int cameraSum=response1.getInteger("total");

           System.out.println("视频监控中的摄像头总数量:"+deviceSum+"  设备管理中的摄像头的总数量:"+cameraSum);
           Preconditions.checkArgument(deviceSum==cameraSum,"视频监控中的摄像头总数量:"+deviceSum+"  设备管理中的摄像头的总数量:"+cameraSum);

       }catch(AssertionError|Exception e){
           appendFailReason(e.toString());
       }finally{
           saveData("[视频监控]摄像头数量=【设备管理】所有状态设备和");
       }
    }


}
