package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.experiment.enumerator.*;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * @author wangmin
 * @date 2020/7/23 16:45
 */
public class CrmApp2_0 extends TestCaseCommon implements TestCaseStd {
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

    @Test(description = "获取门店小程序二维码")
    public void getShopQrCode() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.porscheAppShopGrCode();
            String url = response.getJSONObject("data").getString("qrcode_url");
            Preconditions.checkArgument(url != null, "二维码为空");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("获取门店小程序二维码");
        }
    }

    @Test(description = "创建接待")
    public void createReception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.createReception();
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建接待");
        }
    }

    @Test(description = "分配销售")
    public void allocationSale() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.allocationSale("uid_41786c76", 1L);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("分配销售");
        }
    }

    @Test(description = "空闲销售列表")
    public void freeSaleList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String url = "/porsche/app/sale-reception/freeSaleList";
            String json = "{}";
            String res = httpPostWithCheckCode(url, json, "http://dev.porsche.dealer-ydauto.winsenseos.cn");
            String message = JSONObject.parseObject(res).getString("message");
            Preconditions.checkArgument(message.equals("成功"), "接口返回不成功");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("空闲销售列表");
        }
    }

    @Test(description = "全部预约人数>=今日预约人数")
    public void appointTotalAndTodayNumber() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //预约试驾
            JSONObject response = crm.appointmentDriverNum();
            Integer testDriverTotal = response.getInteger("appointment_total_number");
            Integer testDriverToday = response.getInteger("appointment_today_number");
            Preconditions.checkArgument(testDriverToday <= testDriverTotal, "试驾-全部预约人数<今日预约人数");
            //预约保养
            JSONObject response1 = crm.mainAppointmentDriverNum();
            Integer maintainTodayNumber = response1.getInteger("appointment_today_number");
            Integer maintainTotalNumber = response1.getInteger("appointment_total_number");
            Preconditions.checkArgument(maintainTodayNumber <= maintainTotalNumber, "保养-全部预约人数<今日预约人数");
            //预约维修
            JSONObject response2 = crm.repairAppointmentDriverNum();
            Integer repairTodayNumber = response2.getInteger("appointment_today_number");
            Integer repairTotalNumber = response2.getInteger("appointment_total_number");
            Preconditions.checkArgument(repairTodayNumber <= repairTotalNumber, "维修-全部预约人数<今日预约人数");
            Preconditions.checkArgument((testDriverToday + maintainTodayNumber + repairTodayNumber) <=
                    (testDriverTotal + maintainTotalNumber + repairTotalNumber), "全部预约人数<今日预约人数");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("全部预约人数>=今日预约人数");
        }
    }

    @Test(description = "全部预约人数<=列表数")
    public void appointTotalAndListNumber() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //预约试驾
            Integer testDriverTotalNumber = crm.appointmentDriverNum().getInteger("appointment_total_number");
            //预约试驾列表
            Integer testDriveTotal = Integer.parseInt(crm.appointmentlist().getString("total"));
            //预约保养
            Integer maintainTotalNumber = crm.mainAppointmentDriverNum().getInteger("appointment_total_number");
            //预约保养列表
            Integer maintainTotal = Integer.parseInt(crm.mainAppointmentlist().getString("total"));
            //预约维修
            Integer repairTotalNumber = crm.repairAppointmentDriverNum().getInteger("appointment_total_number");
            //预约维修列表
            Integer repairTotal = Integer.parseInt(crm.repairAppointmentlist().getString("total"));
            Preconditions.checkArgument((testDriverTotalNumber + maintainTotalNumber + repairTotalNumber) <=
                    (testDriveTotal + maintainTotal + repairTotal), "全部预约人数>列表数");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("全部预约人数<=列表数");
        }
    }

    @Test
    public void registeredCustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        int activityTaskId = 0;
        try {
            JSONObject response = crm.activityTaskPage();
            JSONObject json = response.getJSONObject("data").getJSONArray("list").getJSONObject(0);
            activityTaskId = json.getInteger("activity_task_id");
            int activityId = json.getInteger("activity_id");
            JSONObject startResponse = crm.customerTaskPage(10, 1, (long) activityId);
            int size = startResponse.getJSONObject("data").getJSONArray("list").size();
            //添加报名信息
            crm.registeredCustomer((long) activityTaskId, "张三", "13454678912");
            //pc任务客户数量+1
            JSONObject endResponse = crm.customerTaskPage(10, 1, (long) activityId);
            int size1 = endResponse.getJSONObject("data").getJSONArray("list").size();
            Preconditions.checkArgument(size1 == size + 1, "添加报名人信息后，pc端任务活动未+1");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            String customerId = null;
            JSONObject response2 = crm.activityTaskPage();
            JSONArray list = response2.getJSONObject("data").getJSONArray("list").getJSONObject(0).getJSONArray("customer_list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("customer_phone_number").equals("13454678912")) {
                    customerId = list.getJSONObject(i).getString("customer_id");
                }
            }
            //删除报名人
            crm.deleteCustomer(String.valueOf(activityTaskId), customerId);
            saveData("添加报名人信息");
        }
    }
}
