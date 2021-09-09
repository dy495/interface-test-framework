package com.haisheng.framework.testng.bigScreen.jiaochen.xmf.pc;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppTodayDataDto;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppTodayTaskDto;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.presalesreception.AppPreSalesReceptionPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.pojo.DataStore;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppPreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.JcFunction;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * @description :销售创建接待
 * @date :2020/12/18 16:45
 **/

public class JcPcAffirmReception extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.JC_DAILY_JD;
    private static final EnumAccount account = EnumAccount.JC_DAILY_LXQ;
    private final VisitorProxy visitor = new VisitorProxy(product);
    public JcFunction util = new JcFunction(visitor);
    public AppPreSalesReceptionPageBean preSalesReceptionPage;
    public DataStore dataStore = new DataStore();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.XMF.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(account.getReceptionShopId()).setReferer(product.getReferer()).setRoleId(account.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
        initDataStore();
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
        util.loginPc(account);
    }

    /**
     * 获取数据信息
     */
    public void initDataStore() {
        logger.info("查询数据:{}", "---start");
        //pc接待管理数
        util.loginPc(account);
        Integer pcReceptionTotal = ReceptionPageScene.builder().build().visitor(visitor).execute().getInteger("total");
        dataStore.setPcReceptionTotal(pcReceptionTotal);
        //app任务-接待数
        util.loginApp(account);
        Integer appReceptionTotal = AppPreSalesReceptionPageScene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
        dataStore.setAppPreReceptionTotal(appReceptionTotal);
        //今日数据
        AppTodayDataDto appTodayData = util.getAppTodayData();
        AppTodayTaskDto appTodayTask = util.getAppTodayTask();
        dataStore.setAppTodayDataDto(appTodayData);
        dataStore.setAppTodayTaskDto(appTodayTask);
        util.loginPc(account);
        logger.info("查询数据:{}", "---end");
    }

    @Test(description = "接待后pc接待列表+1")
    public void pcAppointmentTotal() {
        try {
            Integer pcReceptionTotal = dataStore.getPcReceptionTotal();
            Integer pcReceptionTotal1 = ReceptionPageScene.builder().build().visitor(visitor).execute().getInteger("total");
            Preconditions.checkArgument(pcReceptionTotal1 == (pcReceptionTotal + 1), "接待前pc接待列表数据为：" + pcReceptionTotal + " 接待后pc接待列表数据为：" + pcReceptionTotal1);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("接待后pc接待列表+1");
        }
    }

    @Test(description = "接待后，app接待任务列数+1")
    public void appAppointmentTotal() {
        try {
            util.loginApp(account);
            Integer appPreReceptionTotal = dataStore.getAppPreReceptionTotal();
            Integer appPreReceptionTotal1 = AppPreSalesReceptionPageScene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            Preconditions.checkArgument(appPreReceptionTotal1 == (appPreReceptionTotal + 1), "接待前app接待列表数据为：" + appPreReceptionTotal + " 接待后pc接待列表数据为：" + appPreReceptionTotal1);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("接待后，app接待任务列数+1");
        }
    }

    @Test(description = "接待后，app今日任务分子分母+1")
    public void appTodayData() {
        try {
            Integer[] integers = util.parseStrToInt(dataStore.getAppTodayDataDto().getPrePendingReception());
            Integer[] integers1 = util.parseStrToInt(util.getAppTodayData().getPrePendingReception());
            CommonUtil.valueView(integers, integers1);
            Preconditions.checkArgument(integers[0].equals(integers1[0] + 1), "接待前今日任务销售接待分子为：" + integers[0] + " 接待后今日任务销售接待分子为：" + (integers1[0] + 1));
            Preconditions.checkArgument(integers[1].equals(integers1[1] + 1), "接待前今日任务销售接待分母为：" + integers[1] + " 接待后今日任务销售接待分母为：" + (integers1[1] + 1));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("接待后，app今日任务分子分母+1");
        }
    }
}
