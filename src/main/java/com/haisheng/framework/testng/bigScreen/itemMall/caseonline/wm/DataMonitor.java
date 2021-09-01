package com.haisheng.framework.testng.bigScreen.itemMall.caseonline.wm;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemMall.common.bean.FullCourtTrendBean;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.realtime.FullCourtTrendScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.util.SceneUntil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator.TimeTableEnum;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import lombok.Data;
import org.testng.annotations.*;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class DataMonitor extends TestCaseCommon implements TestCaseStd {
    private final EnumTestProduct product = EnumTestProduct.MALL_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(product);
    public SceneUntil util = new SceneUntil(visitor);
    public CommonConfig commonConfig = new CommonConfig();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_SHOPMALL_Online_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.MALL_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setProduct(product.getAbbreviation());
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
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        logger.logCaseStart(caseResult.getCaseName());
    }

    @Test(dataProvider = "ACCOUNT", dependsOnMethods = "【09:00~22:00】全场监控")
    public void fullCourtMonitor(AccountEnum account) {
        try {
            commonConfig.setMallId(account.getMallId());
            util.loginPc(account);
            String subjectName = account.getName();
            Map<String, Integer> map = new HashMap<>();
            IScene scene = FullCourtTrendScene.builder().type("pv").build();
            List<FullCourtTrendBean> fullCourtTrendBeanList_PV = util.toJavaObjectList(scene, FullCourtTrendBean.class, "list");
            fullCourtTrendBeanList_PV = fullCourtTrendBeanList_PV.stream().filter(e -> e.getTime().compareTo("09:00") >= 0 && e.getTime().compareTo("22:00") <= 0).collect(Collectors.toList());
            scene = FullCourtTrendScene.builder().type("uv").build();
            List<FullCourtTrendBean> fullCourtTrendBeanList_UV = util.toJavaObjectList(scene, FullCourtTrendBean.class, "list");
            fullCourtTrendBeanList_UV = fullCourtTrendBeanList_UV.stream().filter(e -> e.getTime().compareTo("09:00") >= 0 && e.getTime().compareTo("22:00") <= 0).collect(Collectors.toList());
            String nowTime = DateTimeUtil.getFormat(new Date(), "HH");
            fullCourtTrendBeanList_PV.stream().filter(e -> e.getTime().substring(0, 2).equals(nowTime)).map(FullCourtTrendBean::getToday).forEach(pv -> map.put("pv", pv));
            fullCourtTrendBeanList_UV.stream().filter(e -> e.getTime().substring(0, 2).equals(nowTime)).map(FullCourtTrendBean::getToday).forEach(uv -> map.put("uv", uv));
            if (map.get("pv") == null || map.get("pv").equals(0) || map.get("uv") == null || map.get("uv").equals(0)) {
                String timeSection = TimeTableEnum.findSectionByHour(nowTime).getSection();
                util.sendMessage(subjectName, timeSection, map);
            }
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("【09:00~22:00】全场监控");
        }
    }

    @DataProvider(name = "ACCOUNT")
    public Object[] account() {
        return new Object[]{
                AccountEnum.MALL_ONLINE_LY,
                AccountEnum.MALL_ONLINE_ZD,
        };
    }

    @Test(dataProvider = "REGION_TYPE", description = "【09:00~22:00】区域监控")
    public void regionMonitor(AccountEnum account, String[] regionType) {
        try {
            System.err.println(Arrays.toString(regionType));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("【09:00~22:00】区域监控");
        }
    }

    @DataProvider(name = "REGION_TYPE")
    public Object[] regionType() {
        String[] regionType = {"EXIT", "PARKING", "FLOOR"};
        return new Object[][]{
                {AccountEnum.MALL_ONLINE_LY, regionType},
                {AccountEnum.MALL_ONLINE_ZD, regionType},
        };
    }
}
