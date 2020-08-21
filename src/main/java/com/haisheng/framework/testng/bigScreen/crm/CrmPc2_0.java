package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.experiment.checker.ApiChecker;
import com.haisheng.framework.model.experiment.enumerator.*;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.jooq.util.derby.sys.Sys;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

public class CrmPc2_0 extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getName());
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.QA_TEST_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_SHOP.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("crm: " + crm);
        CommonUtil.login(EnumAccount.ZJL);
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

    @Test(description = "共计人数=列表总条数")
    public void salesCustomerManagement_1() {
        logger.logCaseStart(caseResult.getCaseName());
        JSONObject object = crm.customerList("", "", "", "", "", 1, 100);
        //客户总数
        int total = CommonUtil.getIntField(object, "total");
        int pageSize = CommonUtil.pageTurning(total, 100);
        int listSizeTotal = 0;
        for (int i = 1; i <= pageSize; i++) {
            int listSize = crm.customerList("", "", "", "", "", i, 100).getJSONArray("list").size();
            listSizeTotal += listSize;
        }
        CommonUtil.resultLog(total, pageSize, listSizeTotal);
        new ApiChecker.Builder().scenario("pc端我的客户总数=列表的总数").check(listSizeTotal == total, "pc端我的客户总数!=列表的总数").build().check();
    }

    @Test
    public void salesCustomerManagement_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        JSONObject object = crm.customerList("", "", "", date, date, 1, 10);
        int total = CommonUtil.getIntField(object, "total");
        int pageSize = CommonUtil.pageTurning(total, 100);
        int listSizeTotal = 0;
        for (int i = 1; i <= pageSize; i++) {
            int listSize = crm.customerList("", "", "", date, date, i, 100).getJSONArray("list").size();
            listSizeTotal += listSize;
        }
        CommonUtil.resultLog(total, pageSize, listSizeTotal);
        new ApiChecker.Builder().scenario("今日人数=按今日搜索展示列表条数").check(listSizeTotal == total, "pc端今日客戶人数!=按今日搜索展示列表条数").build().check();
    }

    @Test(enabled = false)
    public void salesCustomerManagement_3() {
        logger.logCaseStart(caseResult.getCaseName());
        JSONObject response = crm.customerList("", "", "", "", "", 1, 10);
        JSONObject list = response.getJSONArray("list").getJSONObject(0);
        System.err.println(list);
        List<String> params = CommonUtil.getMoreParam(list, "already_car", "belongs_area", "belongs_sale_id", "buy_car", "buy_car_attribute",
                "buy_car_type", "car_assess", "compare_car", "customer_id", "customer_name", "customer_phone", "customer_select_type",
                "like_car", "pay_type", "pre_buy_time", "failure_cause_remark", "sehand_assess", "show_price", "test_drive_car", "visit_count");
        System.err.println(params);
    }

    @Test
    public void salesCustomerManagement_4() {
        logger.logCaseStart(caseResult.getCaseName());

    }
}
