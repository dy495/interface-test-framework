package com.haisheng.framework.testng.bigScreen.itemXundian.caseonline.wm;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
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
import org.testng.annotations.*;

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
    private final List<DeviceMessage> list = new ArrayList<>();
    private final VisitorProxy visitor = new VisitorProxy(PRODUCT);
    private final SceneUtil util = new SceneUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCT.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_VIDEO;
        commonConfig.setProduct(PRODUCT.getAbbreviation());
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

    @Test(dataProvider = "DEVICE", description = "门店设备监控")
    public void check_video_salesDemo(String deviceId, String deviceName) {
        try {
            Integer code = AppPatrolDeviceLiveScene.builder().deviceId(deviceId).build().visitor(visitor).getResponse().getCode();
            IScene scene = DevicePageScene.builder().deviceId(deviceId).type("CAMERA").build();
            String statusName = util.toFirstJavaObject(scene, JSONObject.class).getString("status_name");
            if (code != 1000) {
                if (!statusName.equals("运行中")) {
                    DeviceMessage deviceMessage = new DeviceMessage();
                    deviceMessage.setDeviceId(deviceId);
                    deviceMessage.setDeviceName(deviceName);
                    deviceMessage.setDeviceStatus(statusName);
                    list.add(deviceMessage);
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("门店设备监控");
        }
    }

    @Test(dependsOnMethods = "check_video_salesDemo", description = "门店设备监控")
    public void check_video_salesDemo_1() {
        try {
            String message = list.size() == 0 ? "无异常设备" : util.pushMessage(ACCOUNT.getSubjectName(), list);
            logger.info("message is：{}", message);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("门店设备监控");
        }
    }

    @DataProvider(name = "DEVICE")
    public static Object[] device() {
        return new String[][]{
                {"8720871407682560", "大门152"},
//                {"8134193718100992","云台全功能"},
                {"8446461860381696", "巡店1（ 不支持云台操作）"},
                {"8446463209866240", "巡店2（ 不支持云台操作）"},
                {"8530565666243584", "宇视-云台全功能"},
//                {"8166364248540160","AI摄像头【151】"},
//                {"8256597211415552","AI摄像头【180】"}
        };
    }
}
