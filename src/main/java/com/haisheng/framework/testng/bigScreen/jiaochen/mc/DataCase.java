package com.haisheng.framework.testng.bigScreen.jiaochen.mc;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.saleschedule.AppSaleScheduleDayListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.saleschedule.AppSaleScheduleGroupListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.saleschedule.AppSaleScheduleMonthListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
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
import java.util.Objects;

public class DataCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.JC_DAILY_JD;
    private static final EnumAccount ACCOUNT = EnumAccount.JC_DAILY_MC;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);
    CommonConfig commonConfig = new CommonConfig();
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");

        //替换checklist的相关信息
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "孟辰";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //放入shopId
        commonConfig.setProduct(PRODUCE.getAbbreviation()).setReferer(PRODUCE.getReferer()).setShopId(ACCOUNT.getReceptionShopId()).setRoleId(ACCOUNT.getRoleId());
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

    @Test(description = "按组排班，排班列表数=组别对应的人数")
    public void groupMode(){
        try {
            JSONObject pre = AppSaleScheduleDayListScene.builder().type("PRE").build().visitor(visitor).execute();
            String groupName = pre.getString("group_name");
            int saleNum = pre.getJSONArray("sales_info_list").size();
            Long groupId = pre.getLong("group_id");
            String today = dt.getHistoryDate(0);
            String month = today.substring(0,7);
            String setGroupName = AppSaleScheduleMonthListScene.builder().type("PRE").date(month).size(20).build().visitor(visitor).execute().getJSONArray("list")
                    .stream().map(e -> (JSONObject) e).filter(e -> Objects.equals(today, e.getString("schedule_time"))).findFirst().get().getString("group_name");
            Preconditions.checkArgument(Objects.equals(groupName,setGroupName),"排班与设置不一致，排班设置组："+setGroupName+"实际显示组别："+groupName);
            int groupSaleNum = AppSaleScheduleGroupListScene.builder().type("PRE").groupId(groupId).build().visitor(visitor).execute().getJSONArray("group_infos")
                    .stream().map(e -> (JSONObject) e).filter(e->e.getLong("group_id")==groupId).map(e->e.getJSONArray("sales_info_list")).findFirst().get().size();
            Preconditions.checkArgument(saleNum==groupSaleNum,"排班列表数!=组内的人员数，组内人员数："+groupName+"排班列表数"+saleNum);
        }catch (AssertionError | Exception e){
            collectMessage(e);
        }finally {
            saveData("按组排班，排班列表数=组别对应的人数");
        }
    }

    @Test(description = "小程序差评：跟进任务分子+1 分母+1,跟进后分子-1")
    public void todayTaskFlowUp(){
        
    }


    @Test(description = "今日任务销售接待：分子 = 今日未完成的主客接待记录；分母 = 今日所有的接待数（主客+陪客）")
    public void todayTaskPreReception(){

    }



}
