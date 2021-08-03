package com.haisheng.framework.testng.bigScreen.itemXundian.caseonline.wm;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.shop.page.PassengerFlowScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.bean.PassengerFlowBean;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.bean.RealTimeShopPvUvBean;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.bean.ShopInfo;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.realtime.shop.PvUvScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.DingPushUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.UserUtil;
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
    private final static EnumTestProduct PRODUCE = EnumTestProduct.MD_ONLINE;
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

    @Test(dataProvider = "account")
    public void test(AccountEnum account) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            user.loginPc(account);
            List<ShopInfo> shopInfos = new LinkedList<>();
            List<PassengerFlowBean> passengerFlowBeanList = util.toJavaObjectList(PassengerFlowScene.builder().build(), PassengerFlowBean.class, "list");
            passengerFlowBeanList.forEach(e -> {
                ShopInfo shopInfo = new ShopInfo();
                List<RealTimeShopPvUvBean> realTimeShopPvUvBeanList = util.toJavaObjectList(PvUvScene.builder().shopId(String.valueOf(e.getId())).build(), RealTimeShopPvUvBean.class, "list")
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

    @DataProvider(name = "account")
    public Object[] getAccount() {
        return new Object[]{
                AccountEnum.ZD,
                AccountEnum.JKQS
        };
    }
}
