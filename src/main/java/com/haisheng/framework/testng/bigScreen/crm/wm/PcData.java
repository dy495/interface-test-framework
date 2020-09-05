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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;

public class PcData extends TestCaseCommon implements TestCaseStd {
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
        CommonUtil.login(EnumAccount.ZJL);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

//    ---------------------------------------------------2.0------------------------------------------------------------

    /**
     * @description: 销售客户管理-我的客户
     */
    @Test(description = "全部-pc端我的客户总数=列表的总数")
    public void myCustomer_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject object = crm.customerList("", "", "", "", "", 1, 100);
            //客户总数
            int total = CommonUtil.getIntField(object, "total");
            int pageSize = CommonUtil.pageTurning(total, 100);
            int listSizeTotal = 0;
            for (int i = 1; i < pageSize; i++) {
                int listSize = crm.customerList("", "", "", "", "", i, 100).getJSONArray("list").size();
                listSizeTotal += listSize;
            }
            CommonUtil.valueView(total, pageSize, listSizeTotal);
            Preconditions.checkArgument(listSizeTotal == total, "pc端我的客户总数!=列表的总数");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端我的客户总数=列表的总数");
        }
    }

    @Test(description = "公海-共计人数=列表总条数")
    public void myCustomer_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.publicCustomerList("", "", 2 << 10, 1);
            int total = CommonUtil.getIntField(response, "total");
            int listSize = response.getJSONArray("list").size();
            CommonUtil.valueView(total, listSize);
            Preconditions.checkArgument(total == listSize, "pc销售客户管理公海共计人数=列表总数");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc销售客户管理公海共计人数=列表总条数");
        }
    }

    @Test(description = "公海-今日人数=按今日搜索展示列表条数")
    public void myCustomer_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            JSONObject object = crm.customerList("", "", "", date, date, 1, 10);
            int total = CommonUtil.getIntField(object, "total");
            int pageSize = CommonUtil.pageTurning(total, 100);
            int listSizeTotal = 0;
            for (int i = 1; i < pageSize; i++) {
                int listSize = crm.customerList("", "", "", date, date, i, 100).getJSONArray("list").size();
                listSizeTotal += listSize;
            }
            CommonUtil.valueView(total, pageSize, listSizeTotal);
            Preconditions.checkArgument(listSizeTotal == total, "pc端今日客戶人数!=按今日搜索展示列表条数");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端-公海今日人数=按今日搜索展示列表条数");
        }
    }

    @Test(description = "公海-勾选两个客户分配给销售A,销售A客户名下客户数量+2,列表数-2,减2改为减1操作")
    public void myCustomer_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(EnumAccount.XSGWTEMP);
            int customerTotal = crm.customerPage(100, 1, "", "", "").getInteger("total");
            //公海人数总量
            CommonUtil.login(EnumAccount.ZJL);
            JSONObject publicCustomerList = crm.publicCustomerList("", "", 10, 1);
            String phone = CommonUtil.getStrField(publicCustomerList, 0, "customer_phone");
            int customerId = CommonUtil.getIntField(publicCustomerList, 0, "customer_id");
            int publicTotal = publicCustomerList.getInteger("total");
            //把公海分配销售
            crm.customerAllot(EnumAccount.XSGWTEMP.getUid(), customerId);
            //分配之后公海总数
            JSONObject publicCustomerList1 = crm.publicCustomerList("", "", 10, 1);
            int publicTotal1 = publicCustomerList1.getInteger("total");
            //等级变为c
            JSONObject result = crm.customerList("", phone, "", "", "", 1, 10);
            String customerLevelName = CommonUtil.getStrField(result, 0, "customer_level_name");
            //分配之后后销售名下客户数量
            CommonUtil.login(EnumAccount.XSGWTEMP);
            int customerTotal1 = crm.customerPage(100, 1, "", "", "").getInteger("total");
            CommonUtil.valueView(customerTotal, customerTotal1, publicTotal, publicTotal1);
            Preconditions.checkArgument(customerTotal1 == customerTotal + 1, "公海客户分配给xsgwtemp后，该销售名下客户数量未+1");
            Preconditions.checkArgument(publicTotal1 == publicTotal - 1, "公海客户分配给xsgwtemp后，公海客户数量未-1");
            Preconditions.checkArgument(customerLevelName.equals("C"), "客户从公海分配销售后，等级未变为C");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端勾选公海客户分配给销售A，销售A客户名下客户数量+1，列表数-1");
        }
    }

    @Test(description = "公海-将一个已存在客户的客户等级设置为G,公海列表中数量+1,客户信息一致,该销售全部客户数量-1")
    public void myCustomer_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //变换所属销售前公海数量
            CommonUtil.login(EnumAccount.ZJL);
            JSONObject publicCustomerList = crm.publicCustomerList("", "", 10, 1);
            int publicTotal = publicCustomerList.getInteger("total");
            //获取一名客户信息
            JSONObject customerList = crm.customerList("", "", "", "", "", 1, 10);
            int customerId = 0;
            String customerName = null;
            String customerPhone = null;
            JSONArray list = customerList.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (!list.getJSONObject(i).getString("customer_level_name").equals("G")
                        && !list.getJSONObject(i).getString("belongs_sale_id").equals(EnumAccount.XSGWTEMP.getUid())) {
                    customerId = list.getJSONObject(i).getInteger("customer_id");
                    customerName = list.getJSONObject(i).getString("customer_name");
                    customerPhone = list.getJSONObject(i).getString("customer_phone");
                }
            }
            //变换所属销售
            crm.customerEdit((long) customerId, customerName, customerPhone, EnumCustomerLevel.G.getCustomerLevel(), EnumAccount.XSGWTEMP.getUid());
            //变换所属销售后公海列表数
            JSONObject publicCustomerList1 = crm.publicCustomerList("", "", 10, 1);
            int publicTotal1 = publicCustomerList1.getInteger("total");
            CommonUtil.valueView(publicTotal, publicTotal1);
            Preconditions.checkArgument(publicTotal1 == publicTotal + 1, "原公海数量为" + publicTotal + "把一个客户等级改为公海后，公海数量为" + publicTotal1);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端将一个已存在客户的客户等级设置为G,公海列表数+1");
        }
    }

    @Test(description = "公海-新建一个G级客户，公海列表数+1")
    public void myCustomer_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        String phone = "13333333333";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        try {
            //公海客户数量
            CommonUtil.login(EnumAccount.ZJL);
            int publicTotal = crm.publicCustomerList("", "", 10, 1).getInteger("total");
            JSONObject response = crm.createLine("【自动化】王先生", 6, phone, 8, remark);
            if (response.getString("message").equals("手机号码重复")) {
                //删除客户
                deleteCustomer(phone);
                //再创建线索
                crm.createLine("【自动化】王先生", 6, phone, 8, remark);
            }
            CommonUtil.login(EnumAccount.ZJL);
            int publicTotal1 = crm.publicCustomerList("", "", 10, 1).getInteger("total");
            CommonUtil.valueView(publicTotal, publicTotal1);
            Preconditions.checkArgument(publicTotal1 == publicTotal + 1, "新建一个G级客户，公海数未+1");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            deleteCustomer(phone);
            saveData("新建一个G级客户,公海列表+1");
        }
    }

    @Test(description = "战败-共计人数=列表总条数")
    public void myCustomer_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject failureCustomerList = crm.failureCustomerList("", "", 1, 2 << 10);
            int listSize = failureCustomerList.getJSONArray("list").size();
            int total = failureCustomerList.getInteger("total");
            CommonUtil.valueView(listSize, total);
            Preconditions.checkArgument(listSize == total, "pc销售客户管理战败共计人数为" + total + "列表总条数为" + listSize);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc销售客户管理战败共计人数=列表总条数");
        }
    }

    @Test(description = "战败-今日人数=按今日搜索展示列表条数")
    public void myCustomer_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            JSONObject failureCustomerList = crm.failureCustomerList(date, date, 1, 2 << 10);
            int listSize = failureCustomerList.getJSONArray("list").size();
            int today = failureCustomerList.getInteger("today");
            Preconditions.checkArgument(listSize == today, "pc销售客户管理战败今日人数为" + today + "按今日搜索列表总条数为" + listSize);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc战败客户，今日人数=按今日搜索展示列表条数");
        }
    }

    @Test(description = "战败-将一个客户的等级修改为F,战败客户列表数量+1,该销售名下客户数量-1")
    public void myCustomer_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //变换所属销售前战败数量
            int publicTotal = crm.failureCustomerList("", "", 1, 10).getInteger("total");
            CommonUtil.login(EnumAccount.XSGWTEMP);
            int customerNum = crm.customerPage(10, 1, "", "", "").getInteger("total");
            CommonUtil.login(EnumAccount.ZJL);
            //获取一名客户信
            JSONObject customerList = crm.customerList("", "", "", "", "", 1, 10);
            int customerId = 0;
            String customerName = null;
            String customerPhone = null;
            JSONArray list = customerList.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (!list.getJSONObject(i).getString("customer_level_name").equals("G")
                        && list.getJSONObject(i).getString("belongs_sale_id").equals(EnumAccount.XSGWTEMP.getUid())
                        && !list.getJSONObject(i).getString("customer_phone").equals("15321527989")) {
                    customerId = list.getJSONObject(i).getInteger("customer_id");
                    customerName = list.getJSONObject(i).getString("customer_name");
                    customerPhone = list.getJSONObject(i).getString("customer_phone");
                }
            }
            //变换所属销售
            crm.customerEdit((long) customerId, customerName, customerPhone, EnumCustomerLevel.F.getCustomerLevel(), EnumAccount.XSGWTEMP.getUid());
            //变换所属销售后战败列表数
            int publicTotal1 = crm.failureCustomerList("", "", 1, 10).getInteger("total");
            CommonUtil.login(EnumAccount.XSGWTEMP);
            int customerNum1 = crm.customerPage(10, 1, "", "", "").getInteger("total");
            CommonUtil.valueView(publicTotal, publicTotal1, customerNum, customerNum1);
            Preconditions.checkArgument(publicTotal1 == publicTotal + 1, "原战败数量为" + publicTotal + "把一个客户等级改为战败后，战败数量为" + publicTotal1);
            Preconditions.checkArgument(customerNum == customerNum1 + 1, "该销售名下原有客户：" + customerNum + "把一个客户等级改为战败后，客户数量为" + customerNum1);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端将一个已存在客户的客户等级设置为F,战败列表数+1,该销售名下客户数量-1");
        }
    }

    @Test(description = "将一个战败客户划入公海,战败客户列表数量-1,公海客户列表数量+1")
    public void myCustomer_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //战败列表数
            JSONObject failureCustomerList = crm.failureCustomerList("", "", 1, 10);
            int customerId = CommonUtil.getIntField(failureCustomerList, 0, "customer_id");
            int failureTotal = failureCustomerList.getInteger("total");
            //公海列表数
            int publicTotal = crm.publicCustomerList("", "", 10, 1).getInteger("total");
            //战败转公海
            crm.failureCustomerToPublic(customerId);
            int failureTotal1 = crm.failureCustomerList("", "", 1, 10).getInteger("total");
            int publicTotal1 = crm.publicCustomerList("", "", 10, 1).getInteger("total");
            CommonUtil.valueView(publicTotal, publicTotal1, failureTotal, failureTotal1, customerId);
            Preconditions.checkArgument(failureTotal == failureTotal1 + 1, "战败转移公海前战败数量为" + failureTotal + "战败转公海后战败数量为" + failureTotal1);
            Preconditions.checkArgument(publicTotal == publicTotal1 - 1, "战败转移公海前公海数量为" + failureTotal + "战败转公海后公海数量为" + failureTotal1);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("将一个战败客户划入公海,战败客户数量-1，公海客户数量+1");
        }
    }

    @Test(description = "小程序-共计人数=列表总条数")
    public void myCustomer_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject weChatCustomerList = crm.wechatCustomerList("", "", 1, 2 << 10);
            int listSize = weChatCustomerList.getJSONArray("list").size();
            int total = weChatCustomerList.getInteger("total");
            CommonUtil.valueView(listSize, total);
            Preconditions.checkArgument(listSize == total, "小程序客户总数为：" + total + "列表总数为：" + listSize);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc销售客户管理小程序共计人数=列表总数");
        }
    }

    @Test(description = "小程序-今日人数=按今日搜索展示列表条数")
    public void myCustomer_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            JSONObject weChatCustomerList = crm.wechatCustomerList(date, date, 1, 2 << 10);
            int listSize = weChatCustomerList.getJSONArray("list").size();
            int today = weChatCustomerList.getInteger("today");
            CommonUtil.valueView(listSize, today);
            Preconditions.checkArgument(listSize == today, "小程序今日客户总数为：" + today + "按今日搜索列表总数为：" + listSize);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc销售客户管理小程序今日人数=按今日搜索展示列表条数");
        }
    }

    @Test(description = "小程序-预约试驾=小程序“我的”预约试驾条数,预约保养=小程序“我的”预约保养条数,预约维修=小程序“我的”预约维修条数")
    public void myCustomer_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.loginApplet(EnumAppletCode.WM);
            JSONObject response = crm.appointmentList(0L, EnumAppointmentType.TEST_DRIVE.getType(), 100);
            int id = CommonUtil.getIntField(response, 0, "id");
            String phone = crm.appointmentInfo((long) id).getString("phone");
            int testDriverTotal = response.getInteger("total");
            int maintainTotal = crm.appointmentList(0L, EnumAppointmentType.MAINTAIN.getType(), 100).getInteger("total");
            int repairTotal = crm.appointmentList(0L, EnumAppointmentType.REPAIR.getType(), 100).getInteger("total");
            CommonUtil.login(EnumAccount.ZJL);
            JSONArray list = crm.wechatCustomerList("", "", 1, 2 << 10).getJSONArray("list");
            int appointmentTestDriver = 0;
            int appointmentMend = 0;
            int appointmentMaintain = 0;
            String appointment = null;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("wechat_id").equals(EnumAppletCode.WM.getWeChatId())) {
                    appointmentTestDriver = list.getJSONObject(i).getInteger("appointment_test_driver");
                    appointmentMaintain = list.getJSONObject(i).getInteger("appointment_maintain");
                    appointmentMend = list.getJSONObject(i).getInteger("appointment_mend");
                    appointment = list.getJSONObject(i).getString("appointment");
                }
            }
            CommonUtil.valueView(testDriverTotal, maintainTotal, repairTotal, phone, appointmentTestDriver, appointmentMaintain, appointmentMend, appointment);
            Preconditions.checkArgument(testDriverTotal == appointmentTestDriver, "pc端预约试驾次数为：" + testDriverTotal + "小程序我的试驾预约总数：" + appointmentTestDriver);
            Preconditions.checkArgument(maintainTotal == appointmentMaintain, "pc端预约保养次数为：" + maintainTotal + "小程序我的保养预约总数：" + appointmentMaintain);
            Preconditions.checkArgument(repairTotal == appointmentMend, "pc端预约维修次数为：" + repairTotal + "小程序我的维修预约总数：" + appointmentMend);
            Preconditions.checkArgument(phone.equals(appointment), "最新预约手机号与pc端小程序预约电话不一致");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端小程序后面的预约试驾=小程序“我的”预约试驾条数,预约保养=小程序“我的”预约保养条数,预约维修=小程序“我的”预约维修条数");
        }
    }

//    ---------------------------------------------------私有方法区-------------------------------------------------------

    /**
     * 删除客户
     *
     * @param phone 电话号
     */
    private void deleteCustomer(String phone) {
        CommonUtil.login(EnumAccount.ZJL);
        JSONObject response = crm.customerList("", phone, "", "", "", 1, 10);
        int customerId = CommonUtil.getIntField(response, 0, "customer_id");
        crm.customerDelete(customerId);
    }
}
