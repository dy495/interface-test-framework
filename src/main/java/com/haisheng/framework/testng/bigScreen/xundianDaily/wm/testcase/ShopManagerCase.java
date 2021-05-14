package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.testcase;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.IEntity;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.table.CsvTable;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.util.ContainerEnum;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.util.FileUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.sence.pc.PassengerFlow;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.sence.pc.RealTimeShopPvUv;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean.PassengerFlowBean;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean.RealTimeShopPvUvBean;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;

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
            IScene passengerFlow = PassengerFlow.builder().build();
            List<PassengerFlowBean> passengerFlowBeanList = util.toJavaObjectList(passengerFlow, PassengerFlowBean.class, "list");
            passengerFlowBeanList.forEach(e -> {
                Long shopId = e.getId();
                IScene realTimeShopPvUv = RealTimeShopPvUv.builder().shopId(String.valueOf(shopId)).build();
                List<RealTimeShopPvUvBean> realTimeShopPvUvBeanList = util.toJavaObjectList(realTimeShopPvUv, RealTimeShopPvUvBean.class, "list");
                int todayPvSum = realTimeShopPvUvBeanList.stream().mapToInt(RealTimeShopPvUvBean::getTodayPv).sum();
                int todayUvSum = realTimeShopPvUvBeanList.stream().mapToInt(RealTimeShopPvUvBean::getTodayUv).sum();
                int yesterdayPvSum = realTimeShopPvUvBeanList.stream().mapToInt(RealTimeShopPvUvBean::getYesterdayPv).sum();
                int yesterdayUvSum = realTimeShopPvUvBeanList.stream().mapToInt(RealTimeShopPvUvBean::getYesterdayUv).sum();
                Preconditions.checkArgument(todayPvSum != 0);


            });
        } catch (Exception e) {
            collectMessage(e);
        }
    }
}
