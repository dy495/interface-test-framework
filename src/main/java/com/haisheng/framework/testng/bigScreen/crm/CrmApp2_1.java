package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.experiment.enumerator.*;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * @author wangmin
 * @date 2020/7/30 18:52
 */
public class CrmApp2_1 extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_YUEXIU_SALES_OFFICE_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.gateway = EnumChecklistGateway.GATEWAY.getGateway();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getName());
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_SHOP.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("crm: " + crm);
        crm.login(EnumAccount.XSGW.getUsername(), EnumAccount.XSGW.getPassword());
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

    @Test(description = "销售排班")
    public void saleOrder() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取销售排班
            JSONObject response = crm.saleOrderList();
            String saleId = CommonUtil.getStrFieldByData(response, 0, "sale_id");
            //销售排班
            crm.saleOrder(saleId, 2);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售排班");
        }
    }

    @Test(description = "回访详情", enabled = false)
    public void returnVisitTaskInfo() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取回访列表
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntFieldByData(response, 0, "task_id");
            //获取回访详情
            crm.returnVisitTaskInfo(taskId);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("获取回访详情");
        }
    }

    @Test(description = "回访操作", enabled = false)
    public void returnVisitTaskExecute() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取回访列表
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntFieldByData(response, 1, "task_id");
            //回访
            crm.returnVisitTaskExecute(taskId);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        }
    }

    @Test(enabled = false)
    public void afterSaleCustomer() {
        logger.info(caseResult.getCaseName());
        try {
            JSONObject response = crm.publicFaceList();
            String analysisCustomerId = CommonUtil.getStrFieldByData(response, 0, "analysis_customer_id");
            System.err.println(analysisCustomerId);
            crm.afterSalelCustomer(analysisCustomerId);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后客户标记");
        }
    }

    @Test(description = "接待状态为接待中数量<=1")
    public void myReceptionList() {
        logger.info(caseResult.getCaseName());
        try {
            //获取我的接待数量
            JSONObject response = crm.myReceptionList("", "", "", 10, 1);
            JSONArray list = response.getJSONObject("data").getJSONArray("list");
            int a = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("service_status_name").equals("接待中")) {
                    a++;
                }
            }
            Preconditions.checkArgument(a <= 1, "接待状态为接待中数量>1");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("接待状态为接待中数量>1");
        }
    }


    @Test
    public void test() {

    }
}
