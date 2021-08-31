package com.haisheng.framework.testng.bigScreen.itemXundian.caseonline.wm;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.bean.RealTimeShopPassPvUvBean;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.bean.ShopData;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator.TimeTableEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.realtime.shop.PassPvUvScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.shop.page.PassengerFlowScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.bean.PassengerFlowBean;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.bean.RealTimeShopPvUvBean;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.realtime.shop.PvUvScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 门店管理中pvuv监控
 *
 * @author wangmin
 * @date 2020/11/24
 */

public class DataMonitor extends TestCaseCommon implements TestCaseStd {
    private final static EnumTestProduct PRODUCE = EnumTestProduct.XD_ONLINE;
    private final VisitorProxy visitor = new VisitorProxy(PRODUCE);
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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setProduct(PRODUCE.getAbbreviation());
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

    @Test(dataProvider = "ACCOUNT")
    public void enterMonitoring(AccountEnum account) {
        try {
            util.loginPc(account);
            String subjectName = account.getSubjectName();
            List<PassengerFlowBean> passengerFlowBeanList = util.toJavaObjectList(PassengerFlowScene.builder().build(), PassengerFlowBean.class, "list");
            List<ShopData> shopDataList = new LinkedList<>();
            for (PassengerFlowBean flowBean : passengerFlowBeanList) {
                List<RealTimeShopPvUvBean> realTimeShopPvUvBeanList = util.toJavaObjectList(PvUvScene.builder().shopId(flowBean.getId()).build(), RealTimeShopPvUvBean.class, "list");
                shopDataList.addAll(realTimeShopPvUvBeanList.stream()
                        .filter(pvUvBean -> pvUvBean.getTime().compareTo("09:00") >= 0)
                        .filter(pvUvBean -> pvUvBean.getTime().compareTo("22:00") <= 0)
                        .map(pvUvBean -> util.setShopData(flowBean, pvUvBean, null))
                        .collect(Collectors.toList()));
            }
            String nowTime = DateTimeUtil.getFormat(new Date(), "HH");
            logger.info("当前时间：{}", nowTime);
            Stream<ShopData> stream = shopDataList.stream()
                    .filter(shopData -> shopData.getRealTimeShopPvUvBean().getTime().substring(0, 2).equals(nowTime));
            shopDataList = account.equals(AccountEnum.DDC) ? util.getDdcShopData(stream) : account.equals(AccountEnum.BGY) ? util.getBgyShopData(stream) : util.getLzShopData(stream);
            if (shopDataList.size() != 0) {
                String timeSection = TimeTableEnum.findSectionByHour(nowTime).getSection();
                util.enterShopData(subjectName, timeSection, shopDataList);
            }
        } catch (Exception e) {
            collectMessage(e);
        }
    }

    @Test(dataProvider = "PASS_ACCOUNT")
    public void passMonitoring(AccountEnum account) {
        try {
            util.loginPc(account);
            String subjectName = account.getSubjectName();
            List<PassengerFlowBean> passengerFlowBeanList = util.toJavaObjectList(PassengerFlowScene.builder().build(), PassengerFlowBean.class, "list");
            List<ShopData> shopDataList = new LinkedList<>();
            for (PassengerFlowBean flowBean : passengerFlowBeanList) {
                List<RealTimeShopPassPvUvBean> realTimeShopPassPvUvBeanList = util.toJavaObjectList(PassPvUvScene.builder().shopId(flowBean.getId()).build(), RealTimeShopPassPvUvBean.class, "list");
                shopDataList.addAll(realTimeShopPassPvUvBeanList.stream()
                        .filter(pvUvBean -> pvUvBean.getTime().compareTo("09:00") >= 0)
                        .filter(pvUvBean -> pvUvBean.getTime().compareTo("22:00") <= 0)
                        .map(pvUvBean -> util.setShopData(flowBean, null, pvUvBean))
                        .collect(Collectors.toList()));
            }
            String nowTime = DateTimeUtil.getFormat(new Date(), "HH");
            logger.info("当前时间：{}", nowTime);
            Stream<ShopData> stream = shopDataList.stream().filter(shopData -> shopData.getRealTimeShopPassPvUvBean().getHour().equals(nowTime));
            shopDataList = util.getPassLzShopData(stream);
            if (shopDataList.size() != 0) {
                String timeSection = TimeTableEnum.findSectionByHour(nowTime).getSection();
                util.passShopData(subjectName, timeSection, shopDataList);
            }
        } catch (Exception e) {
            collectMessage(e);
        }
    }

    @DataProvider(name = "ACCOUNT")
    public Object[] getAccount() {
        return new Object[]{
                AccountEnum.BGY,
                AccountEnum.DDC,
                AccountEnum.LZ
        };
    }

    @DataProvider(name = "PASS_ACCOUNT")
    public Object[] getPassAccount() {
        return new Object[]{
                AccountEnum.LZ
        };
    }
}
