package com.haisheng.framework.testng.bigScreen.crm.wm;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;

public class Q extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    PublicMethod method = new PublicMethod();
    private static final EnumAccount zjl = EnumAccount.ZJL_DAILY;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.produce="porsche";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getName());
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_SHOP.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("crm: " + crm);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        UserUtil.login(zjl);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    /**
     * 销售客户管理-所有顾客
     */
    @Test(description = "销售客户管理--公海--开始时间<=结束时间,筛选出公海日期在此区间内的客户")
    public void testGlobalVariable() {
        logger.logCaseStart(caseResult.getCaseName());
        String startDate = DateTimeUtil.addDayFormat(new Date(), -30);
        String endDate = DateTimeUtil.getFormat(new Date());
        String unixStart = DateTimeUtil.dateToStamp(startDate, "yyyy-MM-dd");
        String unixEnd = DateTimeUtil.dateToStamp(DateTimeUtil.addDayFormat(new Date(), 1), "yyyy-MM-dd");
        try {
            int total = crm.publicCustomerList(startDate, endDate, 10, 1).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.publicCustomerList(startDate, endDate, 100, i).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    String distributeTime = list.getJSONObject(j).getString("distribute_time");
                    CommonUtil.valueView(unixStart, distributeTime, unixEnd);
                    Preconditions.checkArgument(distributeTime.compareTo(unixEnd) <= 0, "开始时间>=结束时间，结果包含不在此时间区间内的客户");
                    Preconditions.checkArgument(distributeTime.compareTo(unixStart) >= 0, "开始时间>=结束时间，结果包含不在此时间区间内的客户");
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户管理--公海--开始时间<=结束时间,筛选出公海日期在此区间内的客户");
        }
    }
}
