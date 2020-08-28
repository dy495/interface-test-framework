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
import com.haisheng.framework.util.ImageUtil;
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
        CommonUtil.login(EnumAccount.XSZJ);
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
        CommonUtil.login(EnumAccount.XSGWTEMP);
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

    @Test()
    public void registeredCustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        int activityTaskId = 0;
        int activityId = 0;
        try {
            CommonUtil.login(EnumAccount.XSGWTEMP);
            JSONArray list = crm.activityTaskPage(1, 10).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getBoolean("is_edit")) {
                    activityTaskId = list.getJSONObject(i).getInteger("activity_task_id");
                    activityId = list.getJSONObject(i).getInteger("activity_id");
                    break;
                }
            }
            CommonUtil.login(EnumAccount.ZJL);
            int activityCustomer = crm.customerTaskPage(10, 1, (long) activityId).getJSONArray("list").size();
            CommonUtil.login(EnumAccount.XSGWTEMP);
            //添加报名信息
            crm.registeredCustomer((long) activityTaskId, "张三", "13454678912");
            //pc任务客户数量+1
            CommonUtil.login(EnumAccount.ZJL);
            int activityCustomer1 = crm.customerTaskPage(10, 1, (long) activityId).getJSONArray("list").size();
            Preconditions.checkArgument(activityCustomer1 == activityCustomer + 1, "添加报名人信息后，pc端任务活动未+1");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            CommonUtil.login(EnumAccount.XSGWTEMP);
            int customerId = 0;
            JSONArray list = crm.customerTaskPage(10, 1, (long) activityId).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("customer_phone_number").equals("13454678912")) {
                    customerId = list.getJSONObject(i).getInteger("id");
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
            CommonUtil.login(EnumAccount.QT);
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

    @Test(description = "同一客户，一天内小程序预约试驾2次,列表条数+2,今日预约人数+1,全部预约+1")
    public void appointmentTestDrive() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject object = crm.appointmentDriverNum();
            //全部预约
            int appointmentTotalNumber = object.getInteger("appointment_total_number");
            //今日预约
            int appointmentTodayNumber = object.getInteger("appointment_today_number");
            JSONObject response = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
            //列表数
            int listSize = response.getJSONArray("list").size();
            //预约试驾
            CommonUtil.loginApplet(EnumAppletCode.WM);
            String data = DateTimeUtil.getFormat(new Date());
            for (int i = 0; i < 2; i++) {
                crm.appointmentTestDrive("MALE", "【自动化】王", "15321527989", data, 4);
            }
            CommonUtil.login(EnumAccount.XSGWTEMP);
            JSONObject object1 = crm.appointmentDriverNum();
            //全部预约
            int appointmentTotalNumber1 = object1.getInteger("appointment_total_number");
            //今日预约
            int appointmentTodayNumber1 = object1.getInteger("appointment_today_number");
            JSONObject response1 = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
            //列表数
            int listSize1 = response1.getJSONArray("list").size();
            Preconditions.checkArgument(listSize1 == listSize + 2, "列表条数未+2");
            Preconditions.checkArgument(appointmentTotalNumber == appointmentTotalNumber1 + 1, "全部预约未+1");
            Preconditions.checkArgument(appointmentTodayNumber1 == appointmentTodayNumber + 1, "今日预约未+1");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("同一客户，一天内小程序预约试驾2次,列表条数+2,今日预约人数+1,全部预约+1");
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
            int appTotal = CommonUtil.getIntField(response1, "total");
            Preconditions.checkArgument(pcTotal == appTotal, "PC预约试驾，与当前登陆app接待销售不一致");
            Preconditions.checkArgument(pcTotal == response1.getJSONArray("list").size(), "PC预约试驾与app预约列表数不一致");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC预约试驾，与当前登陆app接待销售一致的数量=列表数");
        }
    }


    @Test(description = "客户小程序取消预约试驾,客户状态=已取消的数量+1")
    public void returnVisitRecordExecute1() {
        logger.logCaseStart(caseResult.getCaseName());
        //更新小程序token
        CommonUtil.loginApplet(EnumAppletCode.WM);
        String data = DateTimeUtil.getFormat(new Date());
        try {
            //预约试驾
            JSONObject response = crm.appointmentTestDrive("MALE", "【自动化】王", "15321527989", data, 4);
            int appointmentId = CommonUtil.getIntField(response, "appointment_id");
            //已取消数量
            int cancelNum1 = getCancelNum("已取消");
            //取消试驾
            CommonUtil.loginApplet(EnumAppletCode.WM);
            crm.appointmentCancel(appointmentId);
            //已取消数量
            int cancelNum2 = getCancelNum("已取消");
            CommonUtil.valueView(cancelNum1, cancelNum2);
            Preconditions.checkArgument(cancelNum2 == cancelNum1 + 1, "小程序预约试驾后，再取消，已取消状态未+1");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            CommonUtil.login(EnumAccount.XSGWTEMP);
            saveData("客户小程序取消预约试驾,客户状态=已取消的数量+1");
        }
    }

    @Test(description = "客户小程序取消预约试驾,列表总数不变")
    public void returnVisitRecordExecute2() {
        logger.logCaseStart(caseResult.getCaseName());
        //更新小程序token
        CommonUtil.loginApplet(EnumAppletCode.WM);
        String data = DateTimeUtil.getFormat(new Date());
        try {
            JSONObject response = crm.appointmentTestDrive("MALE", "【自动化】王", "15321527989", data, 4);
            int appointmentId = CommonUtil.getIntField(response, "appointment_id");
            //获取列表总数
            CommonUtil.login(EnumAccount.XSGWTEMP);
            JSONObject response1 = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
            Integer total = CommonUtil.getIntField(response1, "total");
            //取消试驾
            CommonUtil.loginApplet(EnumAppletCode.WM);
            crm.appointmentCancel(appointmentId);
            //获取列表总数
            CommonUtil.login(EnumAccount.XSGWTEMP);
            JSONObject response2 = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
            Integer total1 = CommonUtil.getIntField(response2, "total");
            CommonUtil.valueView(total, total1);
            Preconditions.checkArgument(total.equals(total1), "小程序取消预约试驾后，app我的预约列表数量变化了，应该不变");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            CommonUtil.login(EnumAccount.XSGWTEMP);
            saveData("客户小程序取消预约试驾,列表总数不变");
        }
    }

    @Test(description = "预约试驾后额各种验证")
    public void appointmentTestDriver() {
        logger.logCaseStart(caseResult.getCaseName());
        String data = DateTimeUtil.getFormat(new Date());
        try {
            //列表条数
            CommonUtil.login(EnumAccount.XSGWTEMP);
            JSONObject response = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
            Integer total1 = CommonUtil.getIntField(response, "total");
            //预约中数量
            int appointmentNum = getCancelNum("预约中");
            //预约试驾
            CommonUtil.loginApplet(EnumAppletCode.WM);
            JSONObject result = crm.appointmentTestDrive("MALE", "【自动化】王", "15321527989", data, 4);
            int appointmentId = CommonUtil.getIntField(result, "appointment_id");
            //列表条数
            CommonUtil.login(EnumAccount.XSGWTEMP);
            JSONObject response2 = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
            JSONArray list = response2.getJSONArray("list");
            String appointmentDate = null;
            String customerName = null;
            String customerPhoneNumber = null;
            int carType = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getInteger("appointment_id") == appointmentId) {
                    appointmentDate = list.getJSONObject(i).getString("appointment_date");
                    customerName = list.getJSONObject(i).getString("customer_name");
                    carType = list.getJSONObject(i).getInteger("car_type");
                    customerPhoneNumber = list.getJSONObject(i).getString("customer_phone_number");
                }
            }
            assert appointmentDate != null;
            int appointmentNum1 = getCancelNum("预约中");
            Integer total2 = CommonUtil.getIntField(response2, "total");
            CommonUtil.valueView(total1, total2, appointmentNum, appointmentNum1, appointmentDate, carType, customerName, customerPhoneNumber);
            Preconditions.checkArgument(appointmentDate.equals(data), "app我的预约记录中预约日期与小程序不一致");
            Preconditions.checkArgument(customerName.equals("【自动化】王"), "app我的预约记录中客户名称与小程序不一致");
            Preconditions.checkArgument(carType == 4, "app我的预约记录中试驾车型与小程序不一致");
            Preconditions.checkArgument(customerPhoneNumber.equals("15321527989"), "app我的预约记录中联系电话与小程序不一致");
            Preconditions.checkArgument(total2 == total1 + 1, "预约试驾后，app我的预约列表条数未+1");
            Preconditions.checkArgument(appointmentNum1 == appointmentNum + 1, "预约试驾后，app我的预约列表预约中状态未+1");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("预约试驾后，验证小程序和app数据一致性");
        }
    }

    @Test(description = "小程序“我的”预约试驾数=列表数", enabled = false)
    public void appointmentTestDriver1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.loginApplet(EnumAppletCode.WM);
            JSONObject response = crm.appointmentList(0L, "TEST_DRIVE", 100);
            int total = CommonUtil.getIntField(response, "total");
            CommonUtil.login(EnumAccount.XSGWTEMP);
            JSONObject response1 = crm.appointmentTestDriverList("", "", "", 1, 2 << 20);
            JSONArray list = response1.getJSONArray("list");
            int num = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("customer_name").equals("【自动化】王")) {
                    num++;
                }
            }
            CommonUtil.valueView(total, num);
            Preconditions.checkArgument(total == num, "小程序我的试驾列表数量！=app我的预约该顾客预约的次数");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("小程序“我的”预约试驾数=列表数");
        }
    }

    @Test(description = "小程序预约成功，app的列表数+1")
    public void appointmentTestDriver2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String data = DateTimeUtil.getFormat(new Date());
            int appointmentId = 0;
            //获取列表数
            CommonUtil.login(EnumAccount.XSZJ);
            JSONObject response = crm.appointmentTestDriverList("", "", "", 1, 10);
            int total = CommonUtil.getIntField(response, "total");
            JSONObject response1 = crm.appointmentDriverNum();
            int appointmentTodayNumber = response1.getInteger("appointment_today_number");
            int appointmentTotalNumber = response1.getInteger("appointment_total_number");
            //两个人预约试驾
            CommonUtil.loginApplet(EnumAppletCode.WM);
            JSONObject result = crm.appointmentTestDrive("MALE", "【自动化】王", "15321527989", data, 4);
            appointmentId = CommonUtil.getIntField(result, "appointment_id");
            CommonUtil.login(EnumAccount.XSZJ);
            crm.appointmentTestDriverList("", "", "", 1, 100);
            JSONObject response2 = crm.appointmentTestDriverList("", "", "", 1, 10);
            int total1 = CommonUtil.getIntField(response2, "total");
            JSONObject response3 = crm.appointmentDriverNum();
            int appointmentTodayNumber1 = response3.getInteger("appointment_today_number");
            int appointmentTotalNumber1 = response3.getInteger("appointment_total_number");
            CommonUtil.valueView(total, appointmentTodayNumber, appointmentTotalNumber, total1, appointmentTodayNumber1, appointmentTotalNumber1);
            Preconditions.checkArgument(total1 == total + 1, "小程序预约，app我的预约列表数未+1");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("小程序预约成功，app的列表数+1");
        }
    }

    @Test(description = "销售回访任务", enabled = false)
    public void returnVisitTask() {
        logger.logCaseStart(caseResult.getCaseName());
        String data = DateTimeUtil.getFormat(new Date());
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        try {
            //登录小程序
            CommonUtil.loginApplet(EnumAppletCode.WM);
            //预约试驾
            CommonUtil.loginApplet(EnumAppletCode.WM);
            crm.appointmentTestDrive("MALE", "【自动化】王", "15321527989", data, 4);
            //电话预约已完成数量
            int phoneAppointmentNum = 0;
            CommonUtil.login(EnumAccount.XSGWTEMP);
            JSONArray list = crm.appointmentTestDriverList("", "", "", 1, 100).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (!list.getJSONObject(i).getBoolean("phone_appointment")) {
                    phoneAppointmentNum++;
                }
            }
            //获取taskId
            int taskId = 0;
            JSONArray list1 = crm.returnVisitTaskPage(1, 100, "", "").getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                if (list1.getJSONObject(i).getString("customer_name").equals("【自动化】王")
                        && list1.getJSONObject(i).getString("customer_phone").equals("15321527989")
                        && list1.getJSONObject(i).getString("task_status_name").equals("未完成")) {
                    taskId = list1.getJSONObject(i).getInteger("task_id");
                    break;
                }
            }
            //回访
            String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
            String picture = new ImageUtil().getImageBinary(picPath);
            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("return_visit_pic", picture);
            array.add(object);
            crm.returnVisitTaskExecute(taskId, common, data, "ANSWER", array, false);
            //获取
            int x = 0;
            JSONArray s = crm.appointmentTestDriverList("", "", "", 1, 100).getJSONArray("list");
            for (int i = 0; i < s.size(); i++) {
                if (!s.getJSONObject(i).getBoolean("phone_appointment")) {
                    x++;
                }
            }
            CommonUtil.valueView(phoneAppointmentNum, x);
            Preconditions.checkArgument(x == phoneAppointmentNum + 1, "完成回访任务，我的预约列表电话预约没有变为已完成，还是未完成状态");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("完成回访任务，我的预约列表电话预约变为已完成");
        }
    }

    /**
     * 获取状态数量
     */
    private Integer getCancelNum(String serviceStatusName) {
        CommonUtil.login(EnumAccount.XSGWTEMP);
        JSONObject response = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
        JSONArray list = response.getJSONArray("list");
        int cancelNum = 0;
        //获取已取消数量
        for (int i = 0; i < list.size(); i++) {
            if (list.getJSONObject(i).getString("service_status_name").equals(serviceStatusName)) {
                cancelNum++;
            }
        }
        return cancelNum;
    }
}
