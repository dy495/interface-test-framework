package com.haisheng.framework.testng.bigScreen.xundian.caseonline.wm;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.xundian.casedaily.hqq.sence.pc.PassengerFlow;
import com.haisheng.framework.testng.bigScreen.xundian.casedaily.hqq.sence.pc.RealTimeShopPvUv;
import com.haisheng.framework.testng.bigScreen.xundian.bean.PassengerFlowBean;
import com.haisheng.framework.testng.bigScreen.xundian.bean.RealTimeShopPvUvBean;
import com.haisheng.framework.testng.bigScreen.xundian.bean.ShopInfo;
import com.haisheng.framework.testng.bigScreen.xundian.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.xundian.util.DingPushUtil;
import com.haisheng.framework.testng.bigScreen.xundian.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.xundian.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 门店管理中心
 *
 * @author wangmin
 * @date 2020/11/24
 */

public class ShopManagerCase extends TestCaseCommon implements TestCaseStd {
    private final static EnumTestProduce PRODUCE = EnumTestProduce.MD_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.referer = PRODUCE.getReferer();
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
    }

    @Test
    public void test() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            user.loginPc(AccountEnum.ZD);
            List<ShopInfo> shopInfos = new LinkedList<>();
            List<PassengerFlowBean> passengerFlowBeanList = util.toJavaObjectList(PassengerFlow.builder().build(), PassengerFlowBean.class, "list");
            passengerFlowBeanList.forEach(e -> {
                ShopInfo shopInfo = new ShopInfo();
                List<RealTimeShopPvUvBean> realTimeShopPvUvBeanList = util.toJavaObjectList(RealTimeShopPvUv.builder().shopId(String.valueOf(e.getId())).build(), RealTimeShopPvUvBean.class, "list")
                        .stream().filter(b -> b.getTime().compareTo("08:00") >= 0 && b.getTime().compareTo("22:00") <= 0).collect(Collectors.toList());
                shopInfo.setShopName(e.getName());
                shopInfo.setRealTimeShopPvUvBeanList(realTimeShopPvUvBeanList);
                shopInfos.add(shopInfo);
            });
            DingPushUtil.sendMessage(shopInfos);
        } catch (Exception e) {
            collectMessage(e);
        }
    }

    @Test
    public void test2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            user.loginPc(AccountEnum.JKQS);
            List<ShopInfo> shopInfos = new LinkedList<>();
            List<PassengerFlowBean> passengerFlowBeanList = util.toJavaObjectList(PassengerFlow.builder().build(), PassengerFlowBean.class, "list");
            passengerFlowBeanList.forEach(e -> {
                ShopInfo shopInfo = new ShopInfo();
                List<RealTimeShopPvUvBean> realTimeShopPvUvBeanList = util.toJavaObjectList(RealTimeShopPvUv.builder().shopId(String.valueOf(e.getId())).build(), RealTimeShopPvUvBean.class, "list")
                        .stream().filter(b -> b.getTime().compareTo("08:00") >= 0 && b.getTime().compareTo("22:00") <= 0).collect(Collectors.toList());
                shopInfo.setShopName(e.getName());
                shopInfo.setRealTimeShopPvUvBeanList(realTimeShopPvUvBeanList);
                shopInfos.add(shopInfo);
            });
            DingPushUtil.sendMessage(shopInfos);
        } catch (Exception e) {
            collectMessage(e);
        }
    }


}
