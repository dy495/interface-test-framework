package com.haisheng.framework.testng.bigScreen.itemMall.caseonline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemMall.common.bean.FullCourtDataBean;
import com.haisheng.framework.testng.bigScreen.itemMall.common.bean.FullCourtTrendBean;
import com.haisheng.framework.testng.bigScreen.itemMall.common.bean.RegionDataBean;
import com.haisheng.framework.testng.bigScreen.itemMall.common.bean.RegionTrendBean;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.TimeTableEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.realtime.RegionRealTimeTrendScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.util.SceneUntil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.*;

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

    @Test(dataProvider = "ACCOUNT", description = "【09:00~22:00】全场监控")
    public void fullCourtMonitor(AccountEnum account) {
        try {
            commonConfig.setMallId(account.getMallId());
            util.loginPc(account);
            String nowTime = DateTimeUtil.getFormat(new Date(), "HH");
            FullCourtTrendBean pvData = util.findCurrentTimeData("PV", nowTime);
            FullCourtTrendBean uvData = util.findCurrentTimeData("UV", nowTime);
            FullCourtDataBean fullCourtData = new FullCourtDataBean();
            fullCourtData.setPv(pvData.getToday());
            fullCourtData.setUv(uvData.getToday());
            if (fullCourtData.getPv() == null || fullCourtData.getPv().equals(0) || fullCourtData.getUv() == null || fullCourtData.getUv().equals(0)) {
                String timeSection = TimeTableEnum.findSectionByHour(nowTime).getSection();
                util.sendMessage(account.getName(), timeSection, fullCourtData);
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
    public void regionMonitor(AccountEnum account, String[] regions) {
        try {
            commonConfig.setMallId(account.getMallId());
            util.loginPc(account);
            List<RegionDataBean> list = new LinkedList<>();
            String nowTime = DateTimeUtil.getFormat(new Date(), "HH");
            for (String region : regions) {
                RegionDataBean regionDataBean = new RegionDataBean();
                JSONArray seriesList = RegionRealTimeTrendScene.builder().region(region).type("UV").build().visitor(visitor).execute().getJSONArray("series_List");
                JSONObject uvObj = util.findCurrentTimeData("UV", region, nowTime);
                JSONObject pvObj = util.findCurrentTimeData("PV", region, nowTime);
                List<RegionTrendBean> regionTrendBeanList = util.findCurrentTimeValueIsZeroData(seriesList, uvObj, pvObj);
                if (regionTrendBeanList.size() != 0) {
                    regionDataBean.setRegionTrendBeanList(regionTrendBeanList);
                    regionDataBean.setRegion(region);
                    list.add(regionDataBean);
                }
            }
            if (list.size() != 0) {
                String timeSection = TimeTableEnum.findSectionByHour(nowTime).getSection();
                util.sendMessage(account.getName(), timeSection, list);
            }
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
