package com.haisheng.framework.testng.bigScreen.crm.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.experiment.enumerator.*;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CRM-App 自动化用例
 *
 * @author wangmin
 */
public class CrmApp extends TestCaseCommon implements TestCaseStd {
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
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        CommonUtil.login(EnumAccount.XSGWTEMP);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

//    ---------------------------------------------------2.0------------------------------------------------------------

    /**
     * @description: 工作管理-我的预约
     */
    @Test(description = "全部预约人数>=今日预约人数")
    public void myAppointment_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //预约试驾
            JSONObject response = crm.appointmentDriverNumber();
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
    public void myAppointment_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //预约试驾
            Integer testDriverTotalNumber = crm.appointmentDriverNumber().getInteger("appointment_total_number");
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

    @Test(description = "销售完成回访任务,电话预约=已完成+1、电话预约=未完成-1、列表总数不变", enabled = false)
    public void myAppointment_data_3() {
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
            crm.returnVisitTaskExecute(taskId, common, "", data, "ANSWER", false, picture);
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

    @Test(description = "客户小程序取消预约试驾，客户状态=已取消的数量+1")
    public void myAppointment_data_4() {
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
            saveData("客户小程序取消预约试驾,客户状态=已取消的数量+1");
        }
    }

    @Test(description = "客户小程序取消预约试驾，列表数不变")
    public void myAppointment_data_5() {
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

    @Test(description = "客户小程序预约试驾，列表条数+1、客户状态=预约中数量+1，列表信息与小程序新建预约时信息一致、预约日期、联系人、试驾车型、联系电话")
    public void myAppointment_data_6() {
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

    @Test(description = "同一客户，一天内小程序预约试驾2次,列表条数+2、今日预约人数+1、全部预约+1")
    public void myAppointment_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject object = crm.appointmentDriverNumber();
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
            crm.appointmentTestDrive("MALE", "【自动化】王", "15321527989", data, 4);
            //连续访问接口会失败，延迟3s
            sleep(3);
            crm.appointmentTestDrive("MALE", "【自动化】王", "15321527989", data, 4);
            CommonUtil.login(EnumAccount.XSGWTEMP);
            JSONObject object1 = crm.appointmentDriverNumber();
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

    @Test(description = "两个客户，预约今天/明天明天试驾,列表条数+2、今日预约人数+2、全部预约+2")
    public void myAppointment_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
            CommonUtil.login(EnumAccount.ZJL);
            JSONObject response = crm.appointmentDriverNumber();
            int todayNumber = response.getInteger("appointment_today_number");
            int totalNumber = response.getInteger("appointment_total_number");
            int listSize = crm.appointmentTestDriverList("", "", "", 1, 2 << 10).getJSONArray("list").size();
            //两个人预约试驾-今明两天
            CommonUtil.loginApplet(EnumAppletCode.WM);
            crm.appointmentTestDrive("MALE", "【自动化】王", "15321527989", date, 4);
            CommonUtil.loginApplet(EnumAppletCode.XMF);
            crm.appointmentTestDrive("MALE", "【自动化】李", "18888888888", date1, 4);
            CommonUtil.valueView(todayNumber, totalNumber, listSize);
            CommonUtil.login(EnumAccount.ZJL);
            JSONObject response1 = crm.appointmentDriverNumber();
            int todayNumber1 = response1.getInteger("appointment_today_number");
            int totalNumber1 = response1.getInteger("appointment_total_number");
            int listSize1 = crm.appointmentTestDriverList("", "", "", 1, 2 << 10).getJSONArray("list").size();
            Preconditions.checkArgument(todayNumber + 2 == todayNumber1, "两个客户，预约今天/明天明天试驾,今日预约人数没有+2");
            Preconditions.checkArgument(totalNumber + 2 == totalNumber1, "两个客户，预约今天/明天明天试驾,全部预约没有+2");
            Preconditions.checkArgument(listSize + 2 == listSize1, "两个客户，预约今天/明天明天试驾,列表条数没有+2");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("两个客户，预约今天/明天明天试驾,列表条数+2、今日预约人数+2、全部预约+2");
        }
    }

    @Test(description = "PC预约试驾，与当前登陆app接待销售一致的数量=列表数")
    public void myAppointment_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //pc端试驾列表数
            CommonUtil.login(EnumAccount.ZJL);
            JSONObject response = crm.testDriverPage("", "", 1, 2 << 10);
            JSONArray list = response.getJSONArray("list");
            int pcTotal = 0;
            for (int i = 0; i < list.size(); i++) {
                if (CommonUtil.getStrField(response, i, "sale_name").equals("销售顾问temp")) {
                    pcTotal++;
                }
            }
            //app端预约数量
            CommonUtil.login(EnumAccount.XSGWTEMP);
            JSONObject response1 = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
            int appTotal = CommonUtil.getIntField(response1, "total");
            int listSize = response1.getJSONArray("list").size();
            CommonUtil.valueView(pcTotal, appTotal, listSize);
            Preconditions.checkArgument(pcTotal == appTotal, "PC某个销售顾问预约试驾数！=该销售在【app-我的预约】记录总数量");
            Preconditions.checkArgument(pcTotal == listSize, "PC某个销售顾问预约试驾数！=该销售在【app-我的预约】记录列表总数");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC预约试驾按照销售顾问分类，与每个销售顾问app-我的预约列表数一致");
        }
    }

    @Test(enabled = false)
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
                CommonUtil.login(EnumAccount.XSGWTEMP);
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

    /**
     * @description: 售后-工作管理-我的回访
     */
    @Test(description = "开始时间<=结束时间,成功")
    public void afterSaleMyReturnVisit_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            crm.afterSale_VisitRecordList(1, 1, "", date, date1);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，开始时间<=结束时间");
        }
    }

    @Test(description = "仅输入开始时间")
    public void afterSaleMyReturnVisit_function_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            crm.afterSale_VisitRecordList(1, 10, "", date, "");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，进输入开始时间");
        }
    }

    @Test(description = "仅输入结束时间")
    public void afterSaleMyReturnVisit_function_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            crm.afterSale_VisitRecordList(1, 10, "", "", date);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，进输入开始时间");
        }
    }

    @Test(description = "结束时间>开始时间")
    public void afterSaleMyReturnVisit_function_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            boolean flag = false;
            JSONArray list = crm.afterSale_VisitRecordList(1, 10, "", date1, date).getJSONArray("list");
            if (list.size() == 0) {
                flag = true;
            }
            Preconditions.checkArgument(flag, "结束时间>开始时间,查询成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，结束时间>开始时间");
        }
    }

//    ---------------------------------------------------2.1------------------------------------------------------------

    /**
     * @description: 客户管理-我的接待
     */
    @Test(description = "接待状态为接待中数量<=1")
    public void myReception_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取我的接待数量
            JSONObject response = crm.customerMyReceptionList("", "", "", 2 << 10, 1);
            JSONArray list = response.getJSONArray("list");
            int a = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("user_status_name").equals("接待中")) {
                    a++;
                }
            }
            CommonUtil.valueView(a);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("接待状态为接待中数量<=1");
        }
    }

    @Test(description = "接待按钮高亮数量可>1", enabled = false)
    public void myReception_data_2() {

    }

    @Test(description = "24点，所有接待状态=完成接待", enabled = false)
    public void myReception_data_3() {

    }

    @Test(description = "前台把等待中老客分配给其他销售，列表数-1", enabled = false)
    public void myReception_data_4() {

    }

    @Test(description = "description = 共计接待=列表总数-等待中数量-去重手机号数量", enabled = false)
    public void myReception_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            int totalReception = CommonUtil.getIntField(response, "total_reception");
            //列表总数
            JSONArray list = crm.customerMyReceptionList("", "", "", totalReception + 1000, 1).getJSONArray("list");
            int listTotal = list.size();
            int waitTotal = 0;
            //等待中数量
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("user_status_name").equals("等待中")) {
                    waitTotal++;
                }
            }
            int j = 0;
            Set<String> arr = new HashSet<>();
            for (int i = 0; i < list.size(); i++) {
                if (!list.getJSONObject(i).getString("user_status_name").equals("等待中")) {
                    String customerPhone = list.getJSONObject(i).getString("customer_phone");
                    if (customerPhone == null || customerPhone.trim().isEmpty()) {
                        arr.add(String.valueOf(j));
                        j++;
                    } else {
                        arr.add(customerPhone);
                    }
                }
            }
            System.err.println(arr);
            int s = listTotal - arr.size();
            CommonUtil.valueView(totalReception, listTotal, waitTotal, arr.size(), s);
            Preconditions.checkArgument(totalReception == listTotal - waitTotal - s, "共计接待!=列表总数-等待中数量-去重手机号数量");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("共计接待==列表总数-等待中数量-去重手机号数量");
        }
    }

    @Test(description = "今日新客接待=接待日期为今天 客户类型为新客的手机号去重数量")
    public void myReception_data_6() {
        logger.info(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            //今日新客接待
            int todayNewCustomer = CommonUtil.getIntField(response, "today_new_customer");
            JSONObject response1 = crm.customerMyReceptionList("", "", "", 2 << 10, 1);
            JSONArray list = response1.getJSONArray("list");
            Set<String> set = new HashSet<>();
            int ss = 0;
            for (int i = 0; i < list.size(); i++) {
                String today = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd");
                if (list.getJSONObject(i).getString("day_date").equals(today)
                        && list.getJSONObject(i).getString("customer_type_name").equals("新客")
                        && !list.getJSONObject(i).getString("user_status_name").equals("等待中")) {
                    String customerPhone = list.getJSONObject(i).getString("customer_phone");
                    set.add(customerPhone);
                    ss++;
                }
            }
            int s = ss - set.size();
            Preconditions.checkArgument(todayNewCustomer == ss - s, "今日新客接待!=接待日期为今天 客户类型为新客的手机号去重数量");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日新客接待==接待日期为今天 客户类型为新客的手机号去重数量");
        }
    }

    @Test(description = "今日老客接待=接待日期为今天 客户类型为老客的手机号去重数量")
    public void myReception_data_7() {
        logger.info(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            //今日老客接待
            int todayOldCustomer = CommonUtil.getIntField(response, "total_old_customer");
            JSONObject response1 = crm.customerMyReceptionList("", "", "", 2 << 10, 1);
            JSONArray list = response1.getJSONArray("list");
            Set<String> set = new HashSet<>();
            int ss = 0;
            for (int i = 0; i < list.size(); i++) {
                String today = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd");
                if (list.getJSONObject(i).getString("day_date").equals(today)
                        && list.getJSONObject(i).getString("customer_type_name").equals("老客")
                        && !list.getJSONObject(i).getString("user_status_name").equals("等待中")) {
                    String customerPhone = list.getJSONObject(i).getString("customer_phone");
                    set.add(customerPhone);
                    ss++;
                }
            }
            int s = ss - set.size();
            Preconditions.checkArgument(todayOldCustomer == ss - s, "今日老客接待!=接待日期为今天 客户类型为老客的手机号去重数量");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日新客接待==接待日期为今天 客户类型为新客的手机号去重数量");
        }
    }

    @Test(description = "今日订单数<=今日新客接待+今日老客接待")
    public void myReception_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            int todayOrder = CommonUtil.getIntField(response, "today_order");
            int todayNewCustomer = CommonUtil.getIntField(response, "today_new_customer");
            int totalOldCustomer = CommonUtil.getIntField(response, "total_old_customer");
            Preconditions.checkArgument(todayOrder <= todayNewCustomer + totalOldCustomer, "今日订单数>今日新客接待+今日老客接待");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日订单数<=今日新客接待+今日老客接待");
        }
    }

    @Test(description = "【我的接待】共计接待 >= 【我的客户】全部客户")
    public void myReception_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            int totalReception = CommonUtil.getIntField(response, "total_reception");
            JSONObject response1 = crm.customerPage(10, 1, "", "", "");
            int total = CommonUtil.getIntField(response1, "total");
            Preconditions.checkArgument(totalReception >= total, "【我的接待】共计接待 < 【我的客户】全部客户");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("【我的接待】共计接待 >= 【我的客户】全部客户");
        }
    }

    @Test(description = "今日新客接待+今日老客接待=【PC端销售排班】该销售今日接待次数")
    public void myReception_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            int todayOldCustomer = CommonUtil.getIntField(response, "total_old_customer");
            int todayNewCustomer = CommonUtil.getIntField(response, "today_new_customer");
            int todayCustomerNum = 0;
            JSONArray list = crm.receptionOrder().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("sale_name").equals("销售顾问temp")) {
                    todayCustomerNum = list.getJSONObject(i).getInteger("today_customer_num");
                }
            }
            CommonUtil.valueView(todayCustomerNum, todayNewCustomer, todayOldCustomer);
            Preconditions.checkArgument(todayCustomerNum >= todayNewCustomer + todayOldCustomer, "今日新客接待+今日老客接待!=【PC端销售排班】该销售今日接待次数");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日新客接待+今日老客接待=【PC端销售排班】该销售今日接待次数");
        }
    }

    @Test(description = "今日新客接待+今日老客接待=【PC端展厅接待】分配销售为该销售条数")
    public void myReception_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            int todayOldCustomer = CommonUtil.getIntField(response, "total_old_customer");
            int todayNewCustomer = CommonUtil.getIntField(response, "today_new_customer");
            int customerTodayList = 0;
            JSONArray list = crm.customerTodayList().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("sale_name") != null && list.getJSONObject(i).getString("sale_name").equals("销售顾问temp")) {
                    customerTodayList++;
                }
            }
            CommonUtil.valueView(todayNewCustomer, todayOldCustomer, customerTodayList);
            Preconditions.checkArgument(customerTodayList == (todayNewCustomer + todayOldCustomer), "今日新客接待+今日老客接待!=【PC端展厅接待】分配销售为该销售条数");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日新客接待+今日老客接待=【PC端展厅接待】分配销售为该销售条数");
        }
    }

    /**
     * @description: 客户管理-我的交车
     */
    @Test(description = "今日交车数=今日交车列表手机号去重后列数和")
    public void myDeliverCar_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //今日交车数
            JSONObject response = crm.deliverCarTotal();
            int todayDeliverCarTotal = CommonUtil.getIntField(response, "today_deliver_car_total");
            String startDay = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd");
            JSONArray array = crm.deliverCarAppList("", 1, 2 << 10, startDay, startDay).getJSONArray("list");
            //电话号去重
            Set<String> set = new HashSet<>();
            for (int i = 0; i < array.size(); i++) {
                String customerPhoneNumber = array.getJSONObject(i).getString("customer_phone_number");
                set.add(customerPhoneNumber);
            }
            CommonUtil.valueView(todayDeliverCarTotal, array.size(), set.size());
            Preconditions.checkArgument(todayDeliverCarTotal == set.size(), "今日交车数!=今日交车列表手机号去重后列数和");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日交车数=今日交车列表手机号去重后列数和");
        }
    }

    @Test(description = "实际交车>=今日交车")
    public void myDeliverCar_data_2() {
        logger.info(caseResult.getCaseName());
        try {
            JSONObject response = crm.deliverCarTotal();
            int deliverCarTotal = CommonUtil.getIntField(response, "deliver_car_total");
            int todayDeliverCarTotal = CommonUtil.getIntField(response, "today_deliver_car_total");
            CommonUtil.valueView(deliverCarTotal, todayDeliverCarTotal);
            Preconditions.checkArgument(deliverCarTotal >= todayDeliverCarTotal, "实际交车<今日交车");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("实际交车>=今日交车");
        }
    }

    @Test(description = "全部交车>=实际交车")
    public void myDeliverCar_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.deliverCarTotal();
            int totalOrder = CommonUtil.getIntField(response, "total_order");
            int deliverCarTotal = CommonUtil.getIntField(response, "deliver_car_total");
            Preconditions.checkArgument(totalOrder >= deliverCarTotal, "全部交车<实际交车");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("全部交车>=实际交车");
        }
    }

    /**
     * @description: 客户管理-我的试驾
     */
    @Test(description = "今日试驾数=今日试驾列表手机号去重后列表数和")
    public void myTestDriver_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.driverTotal();
            int todayTestDriveTotal = CommonUtil.getIntField(response, "today_test_drive_total");
            JSONArray list = crm.testDriverAppList("", "", "", 1, 2 << 20).getJSONArray("list");
            Set<String> set = new HashSet<>();
            for (int i = 0; i < list.size(); i++) {
                String phoneNumber = list.getJSONObject(i).getString("customer_phone_number");
                set.add(phoneNumber);
            }
            CommonUtil.valueView(todayTestDriveTotal, set.size());
            Preconditions.checkArgument(todayTestDriveTotal >= set.size(), "今日试驾数!=今日试驾列表手机号去重后列表数和");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日试驾数=今日试驾列表手机号去重后列表数和");
        }
    }

    @Test(description = "全部试驾>=今日试驾")
    public void myTestDriver_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.driverTotal();
            int todayTestDriveTotal = CommonUtil.getIntField(response, "today_test_drive_total");
            int testDriveTotal = CommonUtil.getIntField(response, "test_drive_total");
            CommonUtil.valueView(testDriveTotal, testDriveTotal);
            Preconditions.checkArgument(testDriveTotal >= todayTestDriveTotal, "全部试驾<今日试驾");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("全部试驾>=今日试驾");
        }
    }

    /**
     * @description: 工作管理-我的客户
     */
    @Test(description = "全部客户=列表数")
    public void myCustomer_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerPage(100, 1, "", "", "");
            int total = CommonUtil.getIntField(response, "total");
            JSONArray list = response.getJSONArray("list");
            CommonUtil.valueView(total, list.size());
            Preconditions.checkArgument(total == list.size(), "全部客户!=列表数");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("全部客户=列表数");
        }
    }

    @Test(description = "创建线索,全部客户+1")
    public void myCustomer_data_2() {
        String phone = "13333333333";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        int total;
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(EnumAccount.XSZJ);
            total = CommonUtil.getIntField(crm.customerPage(1, 10, "", "", ""), "total");
            //创建线索
            JSONObject response = crm.createLine("【自动化】王先生", 6, phone, 2, remark);
            if (response.getString("message").equals("联系方式系统中已存在~")) {
                //删除客户
                deleteCustomer(phone);
                total = CommonUtil.getIntField(crm.customerPage(1, 10, "", "", ""), "total");
                crm.createLine("【自动化】王先生", 6, phone, 2, remark);
            }
            int total1 = CommonUtil.getIntField(crm.customerPage(1, 10, "", "", ""), "total");
            CommonUtil.valueView(total, total1);
            Preconditions.checkArgument(total1 == total + 1, "创建线索,全部客未+1");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            deleteCustomer(phone);
            saveData("创建线索,全部客户+1");
        }
    }

    @Test(description = "前台分配新客，创建时手机号不存在,全部客户+1")
    public void myCustomer_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String phone = "13333333333";
        String remark = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        try {
            CommonUtil.login(EnumAccount.ZJL);
            JSONObject response = crm.customerList("", phone, "", "", "", 1, 10);
            if (!response.getJSONArray("list").isEmpty()) {
                deleteCustomer(phone);
            }
            //客户总数
            int total = crm.customerPage(10, 1, "", "", "").getInteger("total");
            //分配客户-获取客户id
            long customerId = getCustomerId();
            CommonUtil.login(EnumAccount.ZJL);
            //完成接待
            crm.customerFinishReception(EnumAccount.ZJL.getUid(), customerId, 3, "【自动化】刘", phone, remark);
            int total1 = crm.customerPage(10, 1, "", "", "").getInteger("total");
            CommonUtil.valueView(total, total1);
            Preconditions.checkArgument(total1 == total + 1, "前台分配新客，创建时手机号不存在,全部客户没有+1");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            deleteCustomer(phone);
            saveData("前台分配新客，创建时手机号不存在,全部客户+1");
        }
    }

    @Test(description = "已订车=列表中是否订车为是的数量")
    public void myCustomer_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerPage(100, 1, "", "", "");
            int buyCarNum = CommonUtil.getIntField(response, "buy_car_num");
            int s = 0;
            JSONArray list = response.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("if_buy_car_name").equals("是")) {
                    s++;
                }
            }
            CommonUtil.valueView(buyCarNum, s);
            Preconditions.checkArgument(buyCarNum == s, "已订车!=列表中是否订车为是的数量");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("已订车=列表中是否订车为是的数量");
        }
    }

    @Test(description = "全部客户>=已订车>=已交车")
    public void myCustomer_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerPage(100, 1, "", "", "");
            int total = CommonUtil.getIntField(response, "total");
            int buyCarNum = CommonUtil.getIntField(response, "buy_car_num");
            int deliverCarNum = CommonUtil.getIntField(response, "deliver_car_num");
            CommonUtil.valueView(total, buyCarNum, deliverCarNum);
            Preconditions.checkArgument(total >= buyCarNum, "全部客户<已定车数");
            Preconditions.checkArgument(buyCarNum >= deliverCarNum, "已定车数<已交车数");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("全部客户>=已订车>=已交车");
        }
    }

    @Test(description = "非订单客户,1=<剩余天数<90")
    public void myCustomer_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(EnumAccount.XSGWTEMP);
            JSONObject response = crm.customerPage(100, 1, "", "", "");
            JSONArray list = response.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (!list.getJSONObject(i).getString("customer_level_name").equals("D级")) {
                    int remainDays = list.getJSONObject(i).getInteger("remain_days");
                    CommonUtil.valueView(remainDays);
                    Preconditions.checkArgument(remainDays < 90, "app，我的客户列表中存在剩余天数>90的记录");
                    Preconditions.checkArgument(remainDays >= 1, "app，我的客户列表中存在剩余天数<1的记录");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("非订单客户,1=<剩余天数<90");
        }
    }

    @Test(description = "app我的客户页列表数=PC我的客户页列表数")
    public void myCustomer_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerPage(100, 1, "", "", "");
            int total = CommonUtil.getIntField(response, "total");
            JSONObject response1 = crm.customerList("", "", "", "", "", 1, 2 << 20);
            JSONArray list = response1.getJSONArray("list");
            CommonUtil.valueView(total, list.size());
            Preconditions.checkArgument(total == list.size(), "app我的客户页列表数!=PC我的客户页列表数");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("app我的客户页列表数=PC我的客户页列表数");
        }
    }

    @Test(description = "删除销售顾问，【PC公海】共计数量=原数量+【我的客户】全部客户数")
    public void myCustomer_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        String salePhone = "15321527989";
        String phone = "13333333333";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        //查询公海数量
        try {
            //先删除15321527989顾问的账号
            deleteSaleUser(salePhone);
            //公海客户数量
            int total = CommonUtil.getIntField(crm.publicCustomerList("", "", 10, 1), "total");
            //添加销售
            crm.addUser(EnumAccount.ZDHCS.getUsername(), EnumAccount.ZDHCS.getUsername(), salePhone, EnumAccount.ZDHCS.getPassword(), 13);
            CommonUtil.login(EnumAccount.ZDHCS);
            //创建线索
            JSONObject response = crm.createLine("【自动化】王", 6, phone, 2, remark);
            if (response.getString("message").equals("联系方式系统中已存在~")) {
                //删除客户
                deleteCustomer(phone);
                //再创建线索
                crm.createLine("【自动化】王先生", 6, phone, 2, remark);
            }
            CommonUtil.login(EnumAccount.ZJL);
            //删除此新增的顾问
            deleteSaleUser(salePhone);
            //公海数量+1
            int total1 = CommonUtil.getIntField(crm.publicCustomerList("", "", 10, 1), "total");
            CommonUtil.valueView(total, total1);
            Preconditions.checkArgument(total1 == total + 1, "删除所属销售，公海数量未增加");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            //删除新增的客户
            deleteCustomer(phone);
            saveData("删除销售顾问，【PC公海】共计数量=原数量+【我的客户】全部客户数");
        }
    }

    @Test(description = "创建线索,客户姓名为汉字-长度1-10")
    public void myCustomer_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String phone = "13333333333";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        String str = "!@#$%^&*()12345678历史记录计算机asdfghj";
        try {
            deleteCustomer(phone);
            //汉字，10字之内
            JSONObject response = crm.createLine("【自动化】王", 6, phone, 2, remark);
            Preconditions.checkArgument(response.getString("message").equals("成功"), "客户姓名为汉字，长度1-10个字内创建线索失败");
            deleteCustomer(phone);
            //汉字，1字
            JSONObject response1 = crm.createLine("王", 6, phone, 2, remark);
            Preconditions.checkArgument(response1.getString("message").equals("成功"), "客户姓名为汉字，长度1个字创建线索失败");
            deleteCustomer(phone);
            //汉字，10个字
            JSONObject response2 = crm.createLine("语言是一种通用的面向", 6, phone, 2, remark);
            Preconditions.checkArgument(response2.getString("message").equals("成功"), "客户姓名为汉字，长度1个字创建线索失败");
            deleteCustomer(phone);
            //备注包含中英文、汉字、符号、数字
            JSONObject response3 = crm.createLine("望京", 6, phone, 2, str);
            Preconditions.checkArgument(response3.getString("message").equals("成功"), "客户姓名为汉字，长度1个字创建线索失败");
            deleteCustomer(phone);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            deleteCustomer(phone);
            saveData("创建线索,客户姓名为汉字，长度1-10，备注20-200字之内");
        }
    }

    @Test(description = "创建线索,意向车型与商品管理中车型一致")
    public void myCustomer_function_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.afterSaleEnumInfo();
            JSONArray carTypes = response.getJSONArray("car_types");
            for (int i = 0; i < carTypes.size(); i++) {
                String carTypeName = carTypes.getJSONObject(i).getString("car_type_name");
                int carType = carTypes.getJSONObject(i).getInteger("car_type");
                JSONArray carList = crm.carList().getJSONArray("list");
                for (int j = 0; j < carList.size(); j++) {
                    if (carList.getJSONObject(j).getInteger("id") == carType) {
                        String carTypeName1 = carList.getJSONObject(j).getString("car_type_name");
                        Preconditions.checkArgument(carTypeName.equals(carTypeName1), "创建线索中意向车型与商品管理中车辆不一致");
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索,意向车型与商品管理中车型一致");
        }
    }

    @Test(description = "客户级别,下拉，HABCFG")
    public void myCustomer_function_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String strings = "G,F,C,B,A,H";
        try {
            JSONArray list = crm.appCustomerLevelList().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String level = list.getJSONObject(i).getString("level");
                CommonUtil.valueView(level);
                Preconditions.checkArgument(strings.contains(level), "客户级别不包含" + strings + "之一");
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索，客户级别,下拉，包含HABCFG");
        }
    }

    @Test(description = "创建线索，不填写必填项")
    public void myCustomer_function_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String phone = "13333333333";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        try {
            deleteCustomer(phone);
            //不填客户名称
            JSONObject response = crm.createLine("", 6, phone, 2, remark);
            Preconditions.checkArgument(response.getString("message").equals("顾客姓名不能为空"), "顾客姓名为空也可创建成功");
            //不填意向车型
            JSONObject response1 = crm.createLine("望京", 0, phone, 2, remark);
            Preconditions.checkArgument(response1.getString("message").equals("意向车型不能为空"), "意向车型不存在也可创建成功");
            //不填电话
            JSONObject response2 = crm.createLine("望京", 1, "", 2, remark);
            Preconditions.checkArgument(response2.getString("message").equals("顾客手机号不能为空"), "顾客手机号为空也可创建成功");
            //不填客户级别
            JSONObject response3 = crm.createLine("望京", 1, phone, 0, remark);
            Preconditions.checkArgument(response3.getString("message").equals("顾客等级不能为空"), "顾客等级为空也可创建成功");
            //不填备注
            JSONObject response4 = crm.createLine("望京", 1, phone, 1, "");
            Preconditions.checkArgument(response4.getString("message").equals("备注信息20-200字之间"), "备注信息为空也可创建成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            deleteCustomer(phone);
            saveData("创建线索，不填写必填项");
        }
    }

    @Test(description = "创建线索，填写全部必填项,备注长度201，联系方式：系统存在")
    public void myCustomer_function_5() {
        logger.logCaseStart(caseResult.getCaseName());
        String phone = "15321527989";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        String remarks = "汉皇重色思倾国，御宇多年求不得。\n" +
                "杨家有女初长成，养在深闺人未识。\n" +
                "天生丽质难自弃，一朝选在君王侧。\n" +
                "回眸一笑百媚生，六宫粉黛无颜色。\n" +
                "春寒赐浴华清池，温泉水滑洗凝脂。\n" +
                "侍儿扶起娇无力，始是新承恩泽时。\n" +
                "云鬓花颜金步摇，芙蓉帐暖度春宵。\n" +
                "春宵苦短日高起，从此君王不早朝。\n" +
                "承欢侍宴无闲暇，春从春游夜专夜。\n" +
                "后宫佳丽三千人，三千宠爱在一身。\n" +
                "金屋妆成娇侍夜，玉楼宴罢醉和春。\n" +
                "姊妹弟兄皆列土，可怜光彩生门户。\n" +
                "遂令天下父母心，不重生男重生女。\n" +
                "骊宫高处入青云，仙乐风飘处处闻。\n" +
                "缓歌慢舞凝丝竹，尽日君王看不足。\n" +
                "渔阳鼙鼓动地来，惊破霓裳羽衣曲。\n" +
                "九重城阙烟尘生，千乘万骑西南行。\n" +
                "翠华摇摇行复止，西出都门百余里。\n" +
                "六军不发无奈何，宛转蛾眉马前死。\n" +
                "花钿委地无人收，翠翘金雀玉搔头。\n" +
                "君王掩面救不得，回看血泪相和流。\n" +
                "黄埃散漫风萧索，云栈萦纡登剑阁。\n" +
                "峨嵋山下少人行，旌旗无光日色薄。\n" +
                "蜀江水碧蜀山青，圣主朝朝暮暮情。\n" +
                "行宫见月伤心色，夜雨闻铃肠断声。\n" +
                "天旋地转回龙驭，到此踌躇不能去。\n" +
                "马嵬坡下泥土中，不见玉颜空死处。\n" +
                "君臣相顾尽沾衣，东望都门信马归。\n";
        try {
            //存在的电话号
            JSONObject response = crm.createLine("王", 6, phone, 2, remark);
            Preconditions.checkArgument(response.getString("message").equals("手机号码重复"), "手机号码重复也可创建成功");
            //备注长度201
            JSONObject response4 = crm.createLine("望京", 1, phone, 1, remarks);
            Preconditions.checkArgument(response4.getString("message").equals("备注信息20-200字之间"), "备注信息超过200字也可创建成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索，不填写必填项");
        }
    }

    /**
     * 工作管理-我的回访
     */
    @Test(description = "开始时间<=结束时间,成功")
    public void myReturnVisit_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            int listSize = crm.returnVisitTaskPage(1, 10, date, date1).getJSONArray("list").size();
            Preconditions.checkArgument(listSize != 0, "回访查询失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，开始时间<=结束时间");
        }
    }

    @Test(description = "仅输入开始时间")
    public void myReturnVisit_function_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            int listSize = crm.returnVisitTaskPage(1, 10, date, "").getJSONArray("list").size();
            Preconditions.checkArgument(listSize != 0, "回访查询失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，进输入开始时间");
        }
    }

    @Test(description = "仅输入结束时间")
    public void myReturnVisit_function_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            int listSize = crm.returnVisitTaskPage(1, 10, "", date).getJSONArray("list").size();
            Preconditions.checkArgument(listSize != 0, "回访查询失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，进输入开始时间");
        }
    }

    @Test(description = "结束时间>开始时间")
    public void myReturnVisit_function_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            boolean flag = false;
            String list = crm.returnVisitTaskPage(1, 10, date1, date).getString("list");
            if (StringUtils.isEmpty(list)) {
                flag = true;
            }
            Preconditions.checkArgument(flag, "结束时间>开始时间,查询成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，结束时间>开始时间");
        }
    }

    @Test(description = "排序,一级：未联系在上，已联系在下", enabled = false)
    public void myReturnVisit_function_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(EnumAccount.XSGWTEMP);
            int s = 0;
            int total = crm.returnVisitTaskPage(1, 1, "", "").getInteger("total");
            int page1 = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < page1; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("task_status_name").equals("未完成")) {
                        s++;
                    }
                }
            }
            CommonUtil.valueView(s);
            int page2 = CommonUtil.pageTurning(s, 100);
            for (int i = 1; i < page2; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    Preconditions.checkArgument(list.getJSONObject(j).getString("task_status_name").equals("未完成"));

                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        }
    }

    @Test(description = "排序,二级：相同状态时间倒序排列", enabled = false)
    public void myReturnVisit_function_6() {

    }

    @Test(description = "手机号为11位手机号")
    public void myReturnVisit_function_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int s = CommonUtil.pageTurning(response.getInteger("total"), 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    String customerPhone = list.getJSONObject(j).getString("customer_phone");
                    Preconditions.checkArgument(customerPhone.length() == 11, "我的回访存在非11位电话号，电话号为：" + customerPhone);
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访，手机号为11位手机号码");
        }
    }

    @Test(description = "作为所属销售的客户")
    public void myReturnVisit_function_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(EnumAccount.XSGWTEMP);
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int s = CommonUtil.pageTurning(response.getInteger("total"), 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    String belongsSaleName = list.getJSONObject(i).getString("belongs_sale_name");
                    Preconditions.checkArgument(belongsSaleName.equals("销售顾问temp"), "我的回访列表，存在非当前登录账号的回访任务。所属销售为：" + belongsSaleName);
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访任务里 我看不到别人的客户 那些客户的所属销售 都是当前登陆账号");
        }
    }

    @Test(description = "列表项包括:所属销售、客户等级、客户名称、联系电话、意向车型、回访类型、是否完成")
    public void myReturnVisit_function_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.returnVisitTaskPage(1, 10, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("belongs_sale_name"), "接口返回参数中不包含字段：belongs_sale_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("customer_level_name"), "接口返回参数中不包含字段：customer_level_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("customer_name"), "接口返回参数中不包含字段：customer_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("customer_phone"), "接口返回参数中不包含字段：customer_phone");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("like_car_name"), "接口返回参数中不包含字段：like_car_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("customer_type_name"), "接口返回参数中不包含字段：customer_type_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("task_status_name"), "接口返回参数中不包含字段：task_status_name");
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访列表包括:所属销售、客户等级、客户名称、联系电话、意向车型、回访类型、是否完成");
        }
    }

    @Test(description = "回访类型:潜客，创建接待时“订车”标记为否的客户")
    public void myReturnVisit_function_10() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            CommonUtil.login(EnumAccount.ZJL);
            JSONArray list = crm.returnVisitTaskPage(1, 100, date, date1).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("customer_type_name").equals("潜客")) {
                    String customerPhone;
                    customerPhone = list.getJSONObject(i).getString("customer_phone");
                    if (StringUtils.isEmpty(customerPhone)) {
                        customerPhone = list.getJSONObject(i - 2).getString("customer_phone");
                        CommonUtil.valueView(customerPhone);
                    }
                    CommonUtil.valueView("电话号是" + customerPhone);
                    JSONObject result = crm.customerList("", customerPhone, "", "", "", 1, 10);
                    String ifByCarName = result.getJSONArray("list").getJSONObject(0).getString("buy_car_name");
                    CommonUtil.valueView(ifByCarName);
                    Preconditions.checkArgument(ifByCarName.equals("否"), "回访类型:潜客，创建接待时不是“订车”标记为否的客户");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访类型:潜客，创建接待时“订车”标记为否的客户");
        }
    }

    @Test(description = "回访类型:成交，创建接待时“订车”标记为是的客户")
    public void myReturnVisit_function_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("customer_type_name").equals("成交")) {
                        String customerPhone = list.getJSONObject(j).getString("customer_phone");
                        CommonUtil.valueView(customerPhone);
                        CommonUtil.login(EnumAccount.ZJL);
                        JSONObject result = crm.customerList("", customerPhone, "", "", "", 1, 10);
                        String ifByCarName = result.getJSONArray("list").getJSONObject(0).getString("buy_car_name");
                        CommonUtil.valueView(j, ifByCarName);
                        Preconditions.checkArgument(ifByCarName.equals("是"), "回访类型:成交，创建接待时不是“订车”标记为是的客户");
                        CommonUtil.login(EnumAccount.XSGWTEMP);
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访类型:成交，创建接待时“订车”标记为是的客户");
        }
    }

    @Test(description = "回访类型:预约，预约记录中存在")
    public void myReturnVisit_function_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.returnVisitTaskPage(1, 100, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("customer_type_name").equals("预约")) {
                    String customerPhone;
                    customerPhone = list.getJSONObject(i).getString("customer_phone");
                    if (StringUtils.isEmpty(customerPhone)) {
                        customerPhone = list.getJSONObject(i - 2).getString("customer_phone");
                        CommonUtil.valueView(customerPhone);
                    }
                    CommonUtil.valueView("电话号是" + customerPhone);
                    JSONObject result = crm.appointmentTestDriverList(customerPhone, "", "", 1, 10);
                    JSONArray list1 = result.getJSONArray("list");
                    CommonUtil.valueView(list1);
                    Preconditions.checkArgument(list1.size() >= 1, "回访类型:预约，预约记录中不存在此电话号");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访类型:预约，工作管理-我的预约中存该手机号");
        }
    }

    @Test(description = "查看,有截图,show_pic字段为true")
    public void myReturnVisit_function_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("task_status_name").equals("已完成")) {
                        boolean showPic = list.getJSONObject(j).getBoolean("show_pic");
                        int taskId = list.getJSONObject(j).getInteger("task_id");
                        JSONArray picList = crm.showPicResult(taskId).getJSONArray("data").getJSONObject(0).getJSONArray("pic_list");
                        Preconditions.checkArgument(showPic, "已完成的回访任务无法查看");
                        Preconditions.checkArgument(picList.size() > 0, "已完成的回访任务无截图");
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访,查看已完成任务,有图片,show_pic字段为true");
        }
    }

    @Test(description = "查看,无截图置灰,show_pic字段为false")
    public void myReturnVisit_function_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (!list.getJSONObject(j).getBoolean("show_pic")) {
                        String taskStatusName = list.getJSONObject(j).getString("task_status_name");
                        Preconditions.checkArgument(taskStatusName.equals("未完成"));
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访,未完成任务无图片,按钮置灰,show_pic字段为false");
        }
    }

    @Test(description = "回访客户,接听,是否完成=已完成")
    public void myReturnVisit_function_15() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            //获取回访列表
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 1, "task_id");
            //回访
            crm.returnVisitTaskExecute(taskId, common, "", date, "ANSWER", false, picture);
            CommonUtil.valueView(taskId);
            String taskStatusName = null;
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int j = 1; j < s; j++) {
                JSONArray list = crm.returnVisitTaskPage(j, 100, "", "").getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                        taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    }
                }
            }
            assert taskStatusName != null;
            CommonUtil.valueView(taskStatusName);
            Preconditions.checkArgument(taskStatusName.equals("已完成"), "完成回访后,是否完成状态未变为已完成");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访客户");
        }
    }

    @Test(description = "回访客户,其他,是否完成=已完成")
    public void myReturnVisit_function_16() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            //获取回访列表
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 1, "task_id");
            //回访
            crm.returnVisitTaskExecute(taskId, common, "", date, "OTHER", false, picture);
            CommonUtil.valueView(taskId);
            String taskStatusName = null;
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int j = 1; j < s; j++) {
                JSONArray list = crm.returnVisitTaskPage(j, 100, "", "").getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                        taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    }
                }
            }
            assert taskStatusName != null;
            CommonUtil.valueView(taskStatusName);
            Preconditions.checkArgument(taskStatusName.equals("已完成"), "完成回访后,是否完成状态未变为已完成");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访客户");
        }
    }

    @Test(description = "回访客户,无人接听,是否完成=未完成")
    public void myReturnVisit_function_17() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        int taskId = 0;
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "", date, "NO_ONE_ANSWER", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("成功"));
            //验证是否完成为未完成
            JSONArray list = crm.returnVisitTaskPage(1, 10, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                    String taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    CommonUtil.valueView(taskStatusName);
                    Preconditions.checkArgument(taskStatusName.equals("未完成"), "第一次回访为无人接听时,回访任务状态不为未完成");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            crm.returnVisitTaskExecute(taskId, common, "", date, "ANSWER", false, picPath);
            saveData("回访选择第一次无人接听时,是否完成=未完成");
        }
    }

    @Test(description = "回访客户,挂断,是否完成=未完成")
    public void myReturnVisit_function_18() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "", date, "HANG_UP", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("成功"));
            //验证是否完成为未完成
            JSONArray list = crm.returnVisitTaskPage(1, 10, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                    String taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    CommonUtil.valueView(taskStatusName);
                    Preconditions.checkArgument(taskStatusName.equals("未完成"), "第一次回访为挂断时,回访任务状态不为未完成");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访选择第一次挂断时,是否完成=未完成");
        }
    }

    @Test(description = "回访客户,稍后联系,是否完成=未完成")
    public void myReturnVisit_function_19() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "", date, "CONTACT_LATER", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("成功"));
            //验证是否完成为未完成
            JSONArray list = crm.returnVisitTaskPage(1, 10, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                    String taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    CommonUtil.valueView(taskStatusName);
                    Preconditions.checkArgument(taskStatusName.equals("未完成"), "第一次回访为稍后联系时,回访任务状态不为未完成");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访选择第一次稍后联系时,是否完成=未完成");
        }
    }

    @Test(description = "回访客户,错号,是否完成=已完成,所有等级产生的回访任务取消", enabled = false)
    public void myReturnVisit_function_20() {
        logger.logCaseStart(caseResult.getCaseName());

    }

    @Test(description = "回访记录,内容,汉字英文数字符号,10-200字")
    public void myReturnVisit_function_21() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String common = "(壬戌之秋)7w既望%苏子与客泛舟游于赤壁之下@清风徐来水波不兴。举酒属客，诵明月之诗，歌窈窕之章。少焉，月出于东山之上，徘徊于斗牛之间。白露横江，水光接天。纵一苇之所如，凌万顷之茫然。浩浩乎如冯虚御风，而不知其所止；飘飘乎如遗世独立，羽化而登仙。\n" +
                "于是饮酒乐甚，扣舷而歌之。歌曰：“桂棹兮兰桨，击空明兮溯流光。渺渺兮予怀，望美人兮天一方。”客有吹洞箫者，倚歌而和之。其声呜呜然，如怨如慕，如泣如诉";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "", date, "ANSWER", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("成功"), "回访记录200字,回访失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访记录,内容,汉字英文数字符号,10-200字");
        }
    }

    @Test(description = "回访截图,2张-3张")
    public void myReturnVisit_function_22() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String common = "举酒属客，诵明月之诗，歌窈窕之章";
        String picPath = "src/main/java/com/haisheng/framework/model/experiment/multimedia/goodsmanager/";
        String picture1 = new ImageUtil().getImageBinary(picPath + "车辆照片.jpg");
        String picture2 = new ImageUtil().getImageBinary(picPath + "外观照片.jpg");
        String picture3 = new ImageUtil().getImageBinary(picPath + "大图照片.jpg");
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访2张图片
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "", date, "CONTACT_LATER", false, picture1, picture2);
            Preconditions.checkArgument(result.getString("message").equals("成功"), "回访截图2张,回访失败");
            //回访3张图片
            JSONObject result1 = crm.returnVisitTaskExecute(taskId, common, "", date, "CONTACT_LATER", false, picture1, picture2, picture3);
            Preconditions.checkArgument(result1.getString("message").equals("成功"), "回访截图3张,回访失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访截图,2张-3张");
        }
    }

    @Test(description = "回访记录,200字以上")
    public void myReturnVisit_function_23() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String common = "壬戌之秋，七月既望，苏子与客泛舟游于赤壁之下。清风徐来，水波不兴。举酒属客，诵明月之诗，歌窈窕之章。少焉，月出于东山之上，徘徊于斗牛之间。白露横江，水光接天。纵一苇之所如，凌万顷之茫然。浩浩乎如冯虚御风，而不知其所止；飘飘乎如遗世独立，羽化而登仙。\n" +
                "于是饮酒乐甚，扣舷而歌之。歌曰：“桂棹兮兰桨，击空明兮溯流光。渺渺兮予怀，望美人兮天一方。”客有吹洞箫者，倚歌而和之。其声呜呜然，如怨如慕，如泣如诉，余音袅袅，不绝如缕。舞幽壑之潜蛟，泣孤舟之嫠妇。\n" +
                "苏子愀然，正襟危坐而问客曰：“何为其然也？”客曰：“月明星稀，乌鹊南飞，此非曹孟德之诗乎？西望夏口，东望武昌，山川相缪，郁乎苍苍，此非孟德之困于周郎者乎？方其破荆州，下江陵，顺流而东也，舳舻千里，旌旗蔽空，酾酒临江，横槊赋诗，固一世之雄也，而今安在哉？况吾与子渔樵于江渚之上，侣鱼虾而友麋鹿，驾一叶之扁舟，举匏樽以相属。寄蜉蝣于天地，渺沧海之一粟。哀吾生之须臾，羡长江之无穷。挟飞仙以遨游，抱明月而长终。知不可乎骤得，托遗响于悲风。”";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "", date, "ANSWER", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("下次回访内容长度必须在10和200之间"), "下次回访内容长度必须在10和200之外也可以回访成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访记录,内容,汉字英文数字符号,200字以上");
        }
    }

    @Test(description = "下次回访日期,昨天及之前")
    public void myReturnVisit_function_24() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        String common = "壬戌之秋，七月既望，苏子与客泛舟游于赤壁之下。";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "", date, "ANSWER", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("回访日期不允许在今日之前"), "下次回访日期在今日之前也可以回访成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("昨天及之前不可做为下次回访日期");
        }
    }

    @Test(description = "我的回访,不可以修改为订单客户")
    public void myReturnVisit_function_25() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String common = "壬戌之秋，七月既望，苏子与客泛舟游于赤壁之下。";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "4", date, "ANSWER", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("不可以修改为订单客户"), "修改为订单客户也可以回访成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访,不可以修改为订单客户");
        }
    }

    @Test(description = "我的回访,不可以修改为订单客户")
    public void myReturnVisit_function_26() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String common = "壬戌之秋，七月既望，苏子与客泛舟游于赤壁之下。";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "4", date, "ANSWER", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("不可以修改为订单客户"), "修改为订单客户也可以回访成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访,不可以修改为订单客户");
        }
    }

    /**
     * @description: 前台销售排班
     */
    @Test(description = "销售排班")
    public void saleOrder_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取销售排班
            JSONObject response = crm.saleOrderList();
            String saleId = CommonUtil.getStrField(response, 0, "sale_id");
            //销售排班
            crm.saleOrder(saleId, 2);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售排班");
        }
    }

    @Test(enabled = false)
    public void afterSaleCustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.publicFaceList();
            String analysisCustomerId = CommonUtil.getStrField(response, 0, "analysis_customer_id");
            crm.afterSalelCustomer(analysisCustomerId);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后客户标记");
        }
    }

//    ---------------------------------------------------私有方法区-------------------------------------------------------

    /**
     * 获取状态数量
     *
     * @param serviceStatusName 服务状态名称：已取消、预约中、已接待
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

    /**
     * 前台分配销售
     * 顾问接待后获取顾客id
     *
     * @return customerId
     */
    private long getCustomerId() {
        CommonUtil.login(EnumAccount.ZJL);
        long customerId;
        //获取当前空闲第一位销售id
        String saleId = CommonUtil.getStrField(crm.freeSaleList(), 0, "sale_id");
        //获取销售账号名
        String userLoginName = "";
        JSONArray userList = crm.userUserPage(1, 100).getJSONArray("list");
        for (int i = 0; i < userList.size(); i++) {
            JSONObject obj = userList.getJSONObject(i);
            if (obj.getString("user_id").equals(saleId)) {
                userLoginName = obj.getString("user_login_name");
            }
        }
        //创建接待
        crm.saleReception(EnumReceptionType.FIRST_VISIT.getType());
        //销售登陆，获取当前接待id
        crm.login(userLoginName, EnumAccount.XSGWTEMP.getPassword());
        customerId = crm.userInfService().getLong("customer_id");
        return customerId;
    }

    /**
     * 删除客户
     *
     * @param phone 客户电话号
     */
    private void deleteCustomer(String phone) {
        CommonUtil.login(EnumAccount.ZJL);
        JSONObject response = crm.customerList("", phone, "", "", "", 1, 10);
        if (!response.getJSONArray("list").isEmpty()) {
            int customerId = CommonUtil.getIntField(response, 0, "customer_id");
            crm.customerDelete(customerId);
        } else {
            System.out.println(response.getString("message"));
        }
    }

    /**
     * 删除
     *
     * @param phone 角色电话
     */
    private void deleteSaleUser(String phone) throws Exception {
        CommonUtil.login(EnumAccount.ZJL);
        JSONArray userList = crm.userUserPage(1, 100).getJSONArray("list");
        for (int i = 0; i < userList.size(); i++) {
            if (userList.getJSONObject(i).getString("user_phone").equals(phone)) {
                String userId = userList.getJSONObject(i).getString("user_id");
                //删除销售
                crm.userDel(userId);
            }
        }
    }
}
