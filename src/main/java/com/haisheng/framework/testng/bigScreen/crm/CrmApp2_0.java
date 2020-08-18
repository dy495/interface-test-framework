package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.experiment.enumerator.*;
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

/**
 * crmApp2.0自动化用例
 *
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
        crm.login(EnumAccount.XSGWTEMP.getUsername(), EnumAccount.XSGWTEMP.getPassword());
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
            JSONObject response = crm.activityTaskPage(1, 10);
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
            JSONObject response2 = crm.activityTaskPage(1, 10);
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

    @Test(description = "创建接待", enabled = false)
    public void createReception() {
        logger.logCaseStart(caseResult.getCaseName());
        //分配销售
        try {
            //登录前台账号
            crm.login(EnumAccount.QT.getUsername(), EnumAccount.QT.getPassword());
            //创建接待
            JSONObject response = crm.saleReceptionCreatReception();
            if (response.getString("message").equals("当前没有空闲销售~")) {
                //登录销售账号
                crm.login(EnumAccount.XSGWTEMP.getUsername(), EnumAccount.XSGWTEMP.getPassword());
                long customerId = crm.userInfService().getLong("customer_id");
                //完成接待
                crm.finishReception(customerId, 7, "测试顾客1", "", "H级客户-taskListChkNum-修改时间为昨天");
                //再次分配
                crm.saleReceptionCreatReception();
            }
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建接待");
        }
    }

    @Test(description = "同一客户，一天内小程序预约试驾2次", enabled = false)
    public void appointmentTestDrive() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.appointmentDriverNum();
            JSONObject testDriverList = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
            int listSize = testDriverList.getJSONArray("list").size();
            int appointmentTotalNumber = CommonUtil.getIntFieldByData(response, "appointment_total_number");
            int appointmentTodayNumber = CommonUtil.getIntFieldByData(response, "appointment_today_number");
            //登录小程序
            crm.appletLogin("qa_need_not_delete");
            String data = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 1));
            for (int i = 0; i < 2; i++) {
                crm.appointmentDrive("【自动化】王先生", "15321527989", data, 718);
            }
            JSONObject response1 = crm.appointmentDriverNum();
            JSONObject testDriverList1 = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
            int listSize1 = testDriverList1.getJSONArray("list").size();
            int appointmentTotalNumber1 = CommonUtil.getIntFieldByData(response1, "appointment_total_number");
            int appointmentTodayNumber1 = CommonUtil.getIntFieldByData(response1, "appointment_today_number");
            Preconditions.checkArgument(listSize1 == listSize + 2, "列表条数未+2");
            Preconditions.checkArgument(appointmentTotalNumber == appointmentTotalNumber1 + 1, "全部预约未+1");
            Preconditions.checkArgument(appointmentTodayNumber1 == appointmentTodayNumber + 1, "今日预约未+1");
        } catch (Exception | AssertionError e) {
            System.err.println("sss");
            appendFailreason(e.toString());
        } finally {
            saveData("同一客户，一天内小程序预约试驾2次");
        }
    }

    @Test(description = "PC预约试驾，与当前登陆app接待销售一致的数量=列表数")
    public void appointmentTestDriverNumber() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //pc端试驾列表数
            crm.login(EnumAccount.XSZJ.getUsername(), EnumAccount.XSZJ.getPassword());
            JSONObject response = crm.testDriverPage("", "", 1, 10);
            int pcTotal = 0;
            JSONArray list = response.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("sale_name").equals("销售顾问temp")) {
                    pcTotal++;
                }
            }
            //app端预约数量
            JSONObject response1 = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
            int appTotal = CommonUtil.getIntFieldByData(response1, "total");
            Preconditions.checkArgument(pcTotal == appTotal, "PC预约试驾，与当前登陆app接待销售不一致");
            Preconditions.checkArgument(pcTotal == response1.getJSONArray("list").size(), "PC预约试驾与app预约列表数不一致");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC预约试驾，与当前登陆app接待销售一致的数量=列表数");
        }
    }


//    @Test
    public void returnVisitRecordExecute() {
        logger.logCaseStart(caseResult.getCaseName());
        //更新小程序token
        crm.appletLoginToken(EnumAppletCode.WM.getCode());
        try {
            //预约试驾
            JSONObject response = crm.appointmentDrive("王", "15321527989", DateTimeUtil.getFormat(new Date()), 1);
            int appointmentId = response.getInteger("appointment_id");
            //登录销售总监账号
            crm.login(EnumAccount.XSZJ.getUsername(), EnumAccount.XSZJ.getPassword());
            crm.appointmentTestDriverList("", "", "", 1, 100);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        }
//            //我的预约电话预约已完成数量
//            JSONObject response = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
//            JSONArray list = response.getJSONArray("list");
//            int phoneAppointmentTrue = 0;
//            int phoneAppointmentFalse = 0;
//            for (int i = 0; i < list.size(); i++) {
//                if (list.getJSONObject(i).getBoolean("phone_appointment").equals(true)) {
//                    phoneAppointmentTrue++;
//                }
//                if (list.getJSONObject(i).getBoolean("phone_appointment").equals(false)) {
//                    phoneAppointmentFalse++;
//                }
//            }
//            //我的回访列表
//            crm.returnVisitTaskPage(1, 100, "", "");
//            CommonUtil.valueView(phoneAppointmentTrue, phoneAppointmentFalse);
//        } catch (Exception | AssertionError e) {
//            appendFailreason(e.toString());
//        }
    }
}
