package com.haisheng.framework.testng.bigScreen.crm.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.experiment.enumerator.*;
import com.haisheng.framework.model.experiment.enumerator.customer.EnumReceptionType;
import com.haisheng.framework.model.experiment.excep.DataExcept;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CRM-App-Data 自动化用例
 *
 * @author wangmin
 */
public class AppData extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();

    @BeforeClass
    @Override
    public void initial() {
        CommonUtil.addConfig();
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

    /**
     * @description: 售后-工作管理-我的回访
     */
    @Test(description = "全部回访=列表条数")
    public void afterSaleMyReturnVisit_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(EnumAccount.FWZJ);
            int total = crm.afterSale_VisitRecordList(1, 100, "", "", "").getInteger("total");
            int listSize = 0;
            int s = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.afterSale_VisitRecordList(i, 100, "", "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    listSize++;
                }
            }
            CommonUtil.valueView(total, listSize);
            Preconditions.checkArgument(total == listSize, "售后-我的回访-全部回访=列表条数");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("服务--全部回访=列表条数");
        }
    }

    @Test(description = "今日回访=任务日期为今天的条数")
    public void afterSaleMyReturnVisit_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            JSONObject response = crm.afterSale_VisitRecordList(1, 100, "", date, date);
            int todayReturnVisitNumber = response.getInteger("today_return_visit_number");
            int listSize = response.getJSONArray("list").size();
            Preconditions.checkArgument(todayReturnVisitNumber == listSize, "服务-今日回访!=任务日期为今天的条数");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("服务--今日回访=任务日期为今天的条数");
        }
    }

    @Test(description = "全部回访>=今日回访")
    public void afterSaleMyReturnVisit_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.afterSale_VisitRecordList(1, 10, "", "", "");
            int todayReturnVisitNumber = response.getInteger("today_return_visit_number");
            int total = response.getInteger("total");
            Preconditions.checkArgument(total >= todayReturnVisitNumber, "服务-全部回访<今日回访");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("服务--全部回访>=今日回访");
        }
    }

    @Test(description = "回访任务日期为今天的回访任务，是否完成=已完成")
    public void afterSaleMyReturnVisit_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String comment = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            CommonUtil.login(EnumAccount.FWZJ);
            int id = 0;
            JSONArray list = crm.afterSale_VisitRecordList(1, 100, "", date, date).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("return_visit_status_name").equals("未完成")) {
                    id = list.getJSONObject(i).getInteger("id");
                    break;
                }
            }
            crm.afterSale_addVisitRecord((long) id, picture, comment, date);
            JSONArray list1 = crm.afterSale_VisitRecordList(1, 100, "", date, date).getJSONArray("list");
            for (int k = 0; k < list1.size(); k++) {
                if (list1.getJSONObject(k).getInteger("id") == id) {
                    String returnVisitStatusName = list1.getJSONObject(k).getString("return_visit_status_name");
                    Preconditions.checkArgument(returnVisitStatusName.equals("已完成"), "回访任务日期为今天的回访任务，是否完成!=已完成");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("服务--回访任务日期为今天的回访任务，是否完成=已完成");
        }
    }

    @Test(description = "回访任务日期为今天的回访任务，是否完成=已完成")
    public void afterSaleMyReturnVisit_5() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        String date1 = DateTimeUtil.getFormat(new Date());
        String comment = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            CommonUtil.login(EnumAccount.FWZJ);
            int id = 0;
            JSONArray list = crm.afterSale_VisitRecordList(1, 100, "", date, date).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("return_visit_status_name").equals("未完成")) {
                    id = list.getJSONObject(i).getInteger("id");
                    break;
                }
            }
            crm.afterSale_addVisitRecord((long) id, picture, comment, date1);
            JSONArray list1 = crm.afterSale_VisitRecordList(1, 100, "", date, date).getJSONArray("list");
            for (int k = 0; k < list1.size(); k++) {
                if (list1.getJSONObject(k).getInteger("id") == id) {
                    String returnVisitStatusName = list1.getJSONObject(k).getString("return_visit_status_name");
                    CommonUtil.valueView(returnVisitStatusName);
                    Preconditions.checkArgument(returnVisitStatusName.equals("未完成"), "回访任务日期为今天的回访任务，是否完成!=未完成");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("服务--回访任务日期为今天的回访任务，是否完成=未完成");
        }
    }

    @Test(description = "小程序预约今日的保养/维修（不取消）", priority = 1)
    public void afterSaleMyReturnVisit_6() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String customerName = "@@@";
        String customerPhoneNumber = "15037286014";
        try {
            CommonUtil.login(EnumAccount.FWZJ);
            JSONObject response = crm.afterSale_VisitRecordList(1, 10, "", "", "");
            int total = response.getInteger("total");
            int todayReturnVisitNumber = response.getInteger("today_return_visit_number");
            int s = CommonUtil.pageTurning(total, 100);
            int listSize = 0;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.afterSale_VisitRecordList(i, 100, "", "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    listSize++;
                }
            }
            CommonUtil.valueView(total, todayReturnVisitNumber, listSize);
            //预约保养
            int id = getTimeId(date);
            crm.appointmentMaintain((long) getCarId(), customerName, customerPhoneNumber, date, "", (long) id);
            CommonUtil.login(EnumAccount.FWZJ);
            JSONObject response1 = crm.afterSale_VisitRecordList(1, 10, "", "", "");
            int total1 = response1.getInteger("total");
            int todayReturnVisitNumber1 = response1.getInteger("today_return_visit_number");
            int s1 = CommonUtil.pageTurning(total, 100);
            int listSize1 = 0;
            for (int i = 1; i < s1; i++) {
                JSONArray list = crm.afterSale_VisitRecordList(i, 100, "", "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    listSize1++;
                }
            }
            CommonUtil.valueView(total1, todayReturnVisitNumber1, listSize1);
            Preconditions.checkArgument(total + 1 == total1, "售后回访页 全部回访未+1");
            Preconditions.checkArgument(todayReturnVisitNumber + 1 == todayReturnVisitNumber1, "今日回访未+1");
            Preconditions.checkArgument(listSize + 1 == listSize1, "售后回访页条数未+1");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("小程序预约今日的保养/维修（不取消）,售后回访页条数+1,售后回访页 全部回访+1,今日回访+1");
        }
    }

    @Test(description = "小程序预约今日的保养/维修（取消）", priority = 2)
    public void afterSaleMyReturnVisit_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(EnumAccount.FWZJ);
            JSONObject response = crm.afterSale_VisitRecordList(1, 10, "", "", "");
            int total = response.getInteger("total");
            int todayReturnVisitNumber = response.getInteger("today_return_visit_number");
            int s = CommonUtil.pageTurning(total, 100);
            int listSize = 0;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.afterSale_VisitRecordList(i, 100, "", "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    listSize++;
                }
            }
            CommonUtil.valueView(total, todayReturnVisitNumber, listSize);
            //取消试驾
            CommonUtil.loginApplet(EnumAppletCode.XMF);
            int id = crm.appointmentList(0L, EnumAppointmentType.MAINTAIN.getType(), 20).getJSONArray("list").getJSONObject(0).getInteger("id");
            crm.appointmentCancel(id);
            CommonUtil.login(EnumAccount.FWZJ);
            JSONObject response1 = crm.afterSale_VisitRecordList(1, 10, "", "", "");
            int total1 = response1.getInteger("total");
            int todayReturnVisitNumber1 = response1.getInteger("today_return_visit_number");
            int s1 = CommonUtil.pageTurning(total, 100);
            int listSize1 = 0;
            for (int i = 1; i < s1; i++) {
                JSONArray list = crm.afterSale_VisitRecordList(i, 100, "", "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    listSize1++;
                }
            }
            CommonUtil.valueView(total1, todayReturnVisitNumber1, listSize1);
            Preconditions.checkArgument(total == total1 + 1, "售后回访页 全部回访未-1");
            Preconditions.checkArgument(todayReturnVisitNumber == todayReturnVisitNumber1 + 1, "今日回访未-1");
            Preconditions.checkArgument(listSize == listSize1 + 1, "售后回访页条数未-1");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("小程序预约今日的保养/维修（取消）,售后回访页条数-1,售后回访页 全部回访-1,今日回访-1");
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
        logger.logCaseStart(caseResult.getCaseName());
        String phone = "13333333333";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        try {
            CommonUtil.login(EnumAccount.ZJL);
            deleteCustomer(phone);
            int total = CommonUtil.getIntField(crm.customerPage(1, 10, "", "", ""), "total");
            //创建线索
            crm.createLine("【自动化】王先生", 6, phone, 2, remark);
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

    /**
     * 获取预约时间id
     *
     * @param date 预约日期
     * @return 时间id
     */
    private Integer getTimeId(String date) throws Exception {
        CommonUtil.loginApplet(EnumAppletCode.XMF);
        JSONArray list = crm.timeList(EnumAppointmentType.MAINTAIN.getType(), date).getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            if (!(list.getJSONObject(i).getInteger("left_num") == 0)) {
                return list.getJSONObject(i).getInteger("id");
            }
        }
        throw new DataExcept("当前时间段可预约次数为0");
    }

    /**
     * 获取车辆id
     */
    private Integer getCarId() throws Exception {
        CommonUtil.loginApplet(EnumAppletCode.XMF);
        JSONArray list = crm.myCarList().getJSONArray("list");
        if (!list.isEmpty()) {
            return list.getJSONObject(0).getInteger("my_car_id");
        }
        throw new DataExcept("该用户小程序没有绑定车");
    }
}