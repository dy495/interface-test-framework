package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.Response;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletCarInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.AppletPlateNumberProvinceListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletCarCreateScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletCarDeleteScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletCarEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
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

import static com.google.common.base.Preconditions.checkArgument;

public class AppletOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCT = EnumTestProduct.JC_ONLINE_JD;
    private static final EnumAppletToken APPLET_USER = EnumAppletToken.JC_LXQ_ONLINE;
    private final VisitorProxy visitor = new VisitorProxy(PRODUCT);
    SceneUtil util = new SceneUtil(visitor);
    PublicParamOnline pp = new PublicParamOnline();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCT.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId("45973").setReferer(PRODUCT.getReferer()).setRoleId(PRODUCT.getRoleId()).setProduct(PRODUCT.getAbbreviation());
        beforeClassInit(commonConfig);
        util.loginApplet(APPLET_USER);
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

    /**
     * @description :?????????????????????8????????????+1 ok
     * @date :2020/7/10 18:03
     **/
    @Test()
    public void myCar_system_1() {
        try {
            String plateNumber = "???JKIO123";
            int count = util.getAppletCarList().size();
            Long carId = util.createCar(plateNumber, Long.parseLong(pp.carModelId)).getId();
            int addCount = util.getAppletCarList().size();
            AppletCarDeleteScene.builder().id(carId).build().visitor(visitor).execute();
            int deleteCount = util.getAppletCarList().size();
            checkArgument((addCount - count) == 1, "???????????????????????????????????????1");
            checkArgument((addCount - deleteCount) == 1, "????????????????????????????????????-1");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("???????????????applet?????????????????????1");
        }
    }

    /**
     * @description :?????????????????????7??? ok
     * @date :2020/7/10 18:03
     **/
    @Test()
    public void myCar_system_2() {
        try {
            String plateNumber = "???JKIO12";
            int count = util.getAppletCarList().size();
            Long carId = util.createCar(plateNumber, Long.parseLong(pp.carModelId)).getId();
            int addCount = util.getAppletCarList().size();
            AppletCarDeleteScene.builder().id(carId).build().visitor(visitor).execute();
            int deleteCount = util.getAppletCarList().size();
            checkArgument((addCount - count) == 1, "???????????????????????????????????????1");
            checkArgument((addCount - deleteCount) == 1, "????????????????????????????????????-1");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("????????????7????????????applet?????????????????????1");
        }
    }

    /**
     * @description :????????????????????????ok
     * @date :2020/7/10 18:03
     **/
    @Test()
    public void myCar_system_3() {
        try {
            List<AppletCarInfo> carInfoList = util.getAppletCarList();
            int carCount = carInfoList.size();
            AppletCarInfo appletCarInfo = carInfoList.stream().filter(e -> e.getModelId() != null).filter(e -> e.getPlateNumber() != null).findFirst().orElse(null);
            Long modelId = appletCarInfo == null ? Long.parseLong(pp.carModelId) : appletCarInfo.getModelId();
            String plateNumber = appletCarInfo == null ? pp.carPlate : appletCarInfo.getPlateNumber();
            Response response = AppletCarCreateScene.builder().modelId(modelId).plateNumber(plateNumber).build().visitor(visitor).getResponse();
            int code = response.getCode();
            String message = response.getMessage();
            String err = "????????????????????????";
            Preconditions.checkArgument(code == 1001, "??????????????????????????????code????????????1001" + " ????????????" + code);
            Preconditions.checkArgument(message.equals(err), "??????????????????????????????message????????????" + err + " ????????????" + message);
            int afterCount = util.getAppletCarList().size();
            checkArgument(afterCount == carCount, "????????????????????????????????????????????????" + carCount + " ????????????????????????????????????????????????" + afterCount);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("????????????????????????");
        }
    }

    /**
     * @description :???????????????????????????10?????????
     * @date :2020/7/27 19:43
     **/
    @Test()
    public void myCar_system_4() {
        List<Long> ids = new ArrayList<>();
        try {
            int count = util.getAppletCarList().size();
            for (int i = 0; i < 5 - count; i++) {
                String plateNumber = "???GBBA3" + i;
                Long id = util.createCar(plateNumber, Long.parseLong(pp.carModelId)).getId();
                ids.add(id);
            }
            String plateNumber = "???GBBA11";
            Response response = AppletCarCreateScene.builder().plateNumber(plateNumber).modelId(Long.parseLong(pp.carModelId)).build().visitor(visitor).getResponse();
            int code = response.getCode();
            String message = response.getMessage();
            checkArgument(code == 1001, "??????????????????5??????");
            checkArgument(message.equals("??????????????????????????????"), "??????????????????5??????message??????" + message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            ids.forEach(id -> AppletCarDeleteScene.builder().id(id).build().visitor(visitor).execute());
            saveData("??????????????????????????????6???");
        }
    }

    /**
     * @description :??????????????? ok
     * @date :2020/8/24 19:54
     **/
    @Test
    public void myCar_system_5() {
        try {
            JSONArray list = AppletPlateNumberProvinceListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
            checkArgument(list.size() == 31, "?????????????????????31");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("???????????????");
        }
    }

    /**
     * @description :?????????????????????????????????
     * @date :2020/10/10 16:00
     **/
    @Test(dataProvider = "PLATE", dataProviderClass = ScenarioUtilOnline.class)
    public void myCar_system_6(String plate) {
        try {
            AppletCarInfo appletCarInfo = util.getAppletCarList().get(0);
            Long carId = appletCarInfo.getId();
            int code = AppletCarEditScene.builder().id(carId).plateNumber(plate).modelId(Long.parseLong(pp.carModelId)).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(code == 1001, "?????????????????????" + plate + " ??????code??????1001" + " ??????code??????" + code);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("??????????????????????????????");
        }
    }

    /**
     * @description :????????????
     * @date :2020/10/10 16:00
     **/
    @Test()
    public void myCar_system_7() {
        String finalPlatNumber = "???S53200";
        Long id = null;
        try {
            String platNumber = "???A12345";
            id = util.getAppletCarList().get(0).getId();
            int code = AppletCarEditScene.builder().id(id).plateNumber(platNumber).modelId(Long.parseLong(pp.carModelId)).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(code == 1000, "????????????????????????code???1000" + " ??????code???" + 1001);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            AppletCarEditScene.builder().id(id).plateNumber(finalPlatNumber).modelId(Long.parseLong(pp.carModelId)).build().visitor(visitor).execute();
            saveData("????????????");
        }
    }

    /**
     * @description :?????????????????????????????????
     * @date :2020/10/10 16:00
     **/
    @Test(dataProvider = "PLATE", dataProviderClass = ScenarioUtilOnline.class)
    public void myCar_system_8(String plate) {
        try {
            int code = AppletCarCreateScene.builder().plateNumber(plate).modelId(Long.valueOf(pp.carModelId)).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(code == 1001, "?????????????????????" + plate + " ??????code??????1001" + " ??????code??????" + code);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("????????????????????????????????????");
        }
    }
}
