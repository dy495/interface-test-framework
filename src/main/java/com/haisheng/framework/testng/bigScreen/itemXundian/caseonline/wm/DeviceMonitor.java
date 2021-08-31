package com.haisheng.framework.testng.bigScreen.itemXundian.caseonline.wm;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.Response;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.bean.DeviceMessage;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.equipmentmanagement.device.DevicePageScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.mapp.AppPatrolDeviceLiveScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.*;
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
import java.util.ArrayList;
import java.util.List;


/**
 * 直播流监控
 *
 * @author wangmin
 * @date 2021/08/31
 */
public class DeviceMonitor extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCT = EnumTestProduct.XD_ONLINE;
    private static final AccountEnum ACCOUNT = AccountEnum.SALES_DEMO_ONLINE;
    private static final List<DeviceMessage> list = new ArrayList<>();
    private final VisitorProxy visitor = new VisitorProxy(PRODUCT);
    private final SceneUtil util = new SceneUtil(visitor);
    private final CommonConfig commonConfig = new CommonConfig();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCT.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_VEDIO;
        beforeClassInit(commonConfig);
        util.loginPc(ACCOUNT);
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
        logger.logCaseStart(caseResult.getCaseName());
    }

    @Test(dataProvider = "DEVICE_ID", dataProviderClass = XdPackageDataOnline.class)
    public void check_video_salesDemo(String deviceId, String deviceName) {
        try {
            Response response = AppPatrolDeviceLiveScene.builder().deviceId(deviceId).build().visitor(visitor).getResponse();
            Integer code = response.getCode();
            IScene scene = DevicePageScene.builder().deviceId(deviceId).type("CAMERA").build();
            String statusName = util.toFirstJavaObject(scene, JSONObject.class).getString("status_name");
            if (code != 1000 && !statusName.equals("运行中")) {
                DeviceMessage deviceMessage = new DeviceMessage();
                deviceMessage.setDeviceId(deviceId);
                deviceMessage.setDeviceName(deviceName);
                deviceMessage.setDeviceStatus(statusName);
                list.add(deviceMessage);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    @Test(dependsOnMethods = "check_video_salesDemo")
    public void sendDing() {
        if (list.size() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n").append("##### ").append(ACCOUNT.getSubjectName()).append("  ").append("门店共有").append(list.size()).append("个设备直播异常").append("\n");
            list.forEach(deviceMessage -> sb.append("###### ").append("设备名称：").append(deviceMessage.getDeviceName()).append(" ").append("设备ID：").append(deviceMessage.getDeviceId()).append(" ").append("设备状态：").append(deviceMessage.getDeviceStatus()).append("\n"));
            DingPushUtil ding = new DingPushUtil();
            ding.changeWeHook(commonConfig.dingHook);
            ding.send(sb.toString());
        }
    }
}
