package com.haisheng.framework.testng.bigScreen.itemXundian.caseonline.wm;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.bean.RealTimeShopPassPvUvBean;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.bean.ShopMessage;
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
 * pvuv监控
 *
 * @author wangmin
 * @date 2021/08/31
 */

public class PvUvMonitor extends TestCaseCommon implements TestCaseStd {
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

    @Test(dataProvider = "ENTER_ACCOUNT", description = "进店数据监控")
    public void enterMonitoring(AccountEnum account) {
        try {
            util.loginPc(account);
            List<ShopMessage> shopDataList = new LinkedList<>();
            List<PassengerFlowBean> passengerFlowBeanList = util.toJavaObjectList(PassengerFlowScene.builder().build(), PassengerFlowBean.class, "list");
            for (PassengerFlowBean flowBean : passengerFlowBeanList) {
                List<RealTimeShopPvUvBean> enterPvUvList = util.toJavaObjectList(PvUvScene.builder().shopId(flowBean.getId()).build(), RealTimeShopPvUvBean.class, "list");
                shopDataList.addAll(enterPvUvList.stream().filter(e -> util.filterTime(e.getTime())).map(pvUvBean -> util.setShopData(flowBean, pvUvBean, null)).collect(Collectors.toList()));
            }
            String nowTime = DateTimeUtil.getFormat(new Date(), "HH");
            Stream<ShopMessage> stream = shopDataList.stream().filter(shopData -> shopData.getRealTimeShopPvUvBean().getTime().substring(0, 2).equals(nowTime));
            shopDataList = account.equals(AccountEnum.DDC) ? util.getDdcShopData(stream) : account.equals(AccountEnum.BGY) ? util.getBgyShopData(stream) : util.getLzShopData(stream);
            if (shopDataList.size() != 0) {
                String timeSection = TimeTableEnum.findSectionByHour(nowTime).getSection();
                util.enterShopData(account.getSubjectName(), timeSection, shopDataList);
            }
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("进店数据监控");
        }
    }

    @Test(dataProvider = "PASS_ACCOUNT", description = "过店数据监控")
    public void passMonitoring(AccountEnum account) {
        try {
            util.loginPc(account);
            List<ShopMessage> shopDataList = new LinkedList<>();
            List<PassengerFlowBean> passengerFlowBeanList = util.toJavaObjectList(PassengerFlowScene.builder().build(), PassengerFlowBean.class, "list");
            for (PassengerFlowBean flowBean : passengerFlowBeanList) {
                List<RealTimeShopPassPvUvBean> passPvUvList = util.toJavaObjectList(PassPvUvScene.builder().shopId(flowBean.getId()).build(), RealTimeShopPassPvUvBean.class, "list");
                shopDataList.addAll(passPvUvList.stream().filter(e -> util.filterTime(e.getTime())).map(pvUvBean -> util.setShopData(flowBean, null, pvUvBean)).collect(Collectors.toList()));
            }
            String nowTime = DateTimeUtil.getFormat(new Date(), "HH");
            Stream<ShopMessage> stream = shopDataList.stream().filter(shopData -> shopData.getRealTimeShopPassPvUvBean().getHour().equals(nowTime));
            shopDataList = util.getPassLzShopData(stream);
            if (shopDataList.size() != 0) {
                String timeSection = TimeTableEnum.findSectionByHour(nowTime).getSection();
                util.passShopData(account.getSubjectName(), timeSection, shopDataList);
            }
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("过店数据监控");
        }
    }

    @DataProvider(name = "ENTER_ACCOUNT")
    public Object[] getEnterAccount() {
        return new Object[]{
                AccountEnum.BGY,
                AccountEnum.DDC,
                AccountEnum.LZ,
                AccountEnum.MSC,
        };
    }

    @DataProvider(name = "PASS_ACCOUNT")
    public Object[] getPassAccount() {
        return new Object[]{
                AccountEnum.LZ
        };
    }
}
