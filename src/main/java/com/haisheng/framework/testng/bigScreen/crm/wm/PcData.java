package com.haisheng.framework.testng.bigScreen.crm.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppointmentType;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCarModel;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCustomerInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerPageScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * pc数据测试用例
 */
public class PcData extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    PublicMethod method = new PublicMethod();
    private static final EnumAccount zjl = EnumAccount.ZJL_DAILY;
    private static final EnumAccount xs = EnumAccount.XS_TEMP_DAILY;
    private static final EnumCarModel car = EnumCarModel.PANAMERA_TEN_YEARS_EDITION;
    private static final int size = 100;

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

    @Test(description = "销售客户管理--我的客户--展厅客户总数=列表的总数")
    public void customer_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject object = crm.customerList("", "", "", "", "", 1, 100);
            //客户总数
            int total = object.getInteger("total");
            int pageSize = CommonUtil.getTurningPage(total, 100);
            int listSizeTotal = 0;
            for (int i = 1; i < pageSize; i++) {
                JSONArray array = crm.customerList("", "", "", "", "", i, 100).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    listSizeTotal++;
                }
            }
            CommonUtil.valueView(total, listSizeTotal);
            Preconditions.checkArgument(listSizeTotal == total, "pc端我的客户总数为：" + total + "列表总数为：" + listSizeTotal);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售客户管理--我的客户--展厅客户总数=列表的总数");
        }
    }

    @Test(description = "销售客户管理--我的客户--DCC客户总数=列表的总数")
    public void customer_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //客户总数
            int total = crm.dccList("", "", "", "", 1, size).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            int listSizeTotal = 0;
            for (int i = 1; i < s; i++) {
                int listSize = crm.dccList("", "", "", "", i, size).getJSONArray("list").size();
                listSizeTotal += listSize;
            }
            CommonUtil.valueView(total, listSizeTotal);
            Preconditions.checkArgument(listSizeTotal == total, "pc端DCC客户总数为：" + total + "列表总数为：" + listSizeTotal);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售客户管理--我的客户--DCC客户总数=列表的总数");
        }
    }

    @Test(description = "销售客户管理--我的客户--成交记录总数=列表的总数")
    public void customer_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //客户总数
            int total = crm.orderInfoPage(1, size).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            int listSizeTotal = 0;
            for (int i = 1; i < s; i++) {
                int listSize = crm.orderInfoPage(i, size).getJSONArray("list").size();
                listSizeTotal += listSize;
            }
            CommonUtil.valueView(total, listSizeTotal);
            Preconditions.checkArgument(listSizeTotal == total, "pc端成交记录总数为：" + total + "列表总数为：" + listSizeTotal);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售客户管理--我的客户--成交记录总数=列表的总数");
        }
    }

    @Test(description = "pc--我的客户--成交记录总数=列表的总数")
    public void customer_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //客户总数
            int total = crm.orderInfoPage(1, size).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            int listSizeTotal = 0;
            for (int i = 1; i < s; i++) {
                int listSize = crm.orderInfoPage(i, size).getJSONArray("list").size();
                listSizeTotal += listSize;
            }
            CommonUtil.valueView(total, listSizeTotal);
            Preconditions.checkArgument(listSizeTotal == total, "pc端成交记录总数为：" + total + "列表总数为：" + listSizeTotal);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc--我的客户--成交记录总数=列表的总数");
        }
    }

    @Test(description = "销售客户管理--我的客户--交车记录--相同底盘号 校验app购车档案和PC成交记录对应数据一致")
    public void customer_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            //客户总数
            int total = crm.orderInfoPage(1, size).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.orderInfoPage(i, size).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    //如果电话号为空，跳过||车牌号为空跳过||不是昨天交车跳过
                    JSONArray phones = list.getJSONObject(j).getJSONArray("phones");
                    String orderDate = CommonUtil.getStrField(list, j, "order_date");
                    String vehicleChassisCode = list.getJSONObject(j).getString("vehicle_chassis_code");
                    if (phones.size() == 0 || !orderDate.equals(date) || StringUtils.isEmpty(vehicleChassisCode)) {
                        continue;
                    }
                    //pc购车信息
                    String deliverDate = CommonUtil.getStrField(list, j, "deliver_date");
                    String carStyleName = CommonUtil.getStrField(list, j, "car_style_name");
                    String carModelName = CommonUtil.getStrField(list, j, "car_model_name");
                    String phone = phones.getString(0);
                    IScene scene = CustomerPageScene.builder().customerPhone(phone).build();
                    int customerId = crm.invokeApi(scene).getJSONArray("list").getJSONObject(0).getInteger("customer_id");
                    JSONArray carList = crm.buyCarList(String.valueOf(customerId)).getJSONArray("list");
                    for (int u = 0; u < carList.size(); u++) {
                        String code = carList.getJSONObject(u).getString("vehicle_chassis_code");
                        if (code != null && code.equals(vehicleChassisCode)) {
                            CommonUtil.valueView(phone, code);
                            //app购车信息
                            String buyTime = CommonUtil.getStrField(carList, u, "buy_time");
                            String deliverTime = CommonUtil.getStrField(carList, u, "deliver_time");
                            String styleName = CommonUtil.getStrField(carList, u, "car_style_name");
                            String modelName = CommonUtil.getStrField(carList, u, "car_model_name");
                            //比较
                            Preconditions.checkArgument(orderDate.equals(buyTime), phone + " 购车时间不一致，pc端购车时间：" + orderDate + " app购车时间：" + buyTime);
                            Preconditions.checkArgument(deliverDate.equals(deliverTime), phone + " 交车时间不一致，pc端交车时间：" + deliverDate + " app交车时间：" + deliverTime);
                            Preconditions.checkArgument(carStyleName.equals(styleName), phone + " 购买车系不一致，pc端购买车系：" + carStyleName + " app购买车系：" + styleName);
                            Preconditions.checkArgument(carModelName.equals(modelName), phone + " 购买车型不一致，pc端购买车系：" + carModelName + " app购买车型：" + modelName);
                            CommonUtil.logger(code);
                        }
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailreason(e.toString());
        } finally {
            saveData("销售客户管理--我的客户--交车记录--相同底盘号 校验app购车档案和PC成交记录对应数据一致");
        }
    }

    @Test(description = "销售客户管理--公海--共计人数=列表总条数")
    public void myCustomer_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = crm.publicCustomerList("", "", 10, 1).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            CommonUtil.valueView(s);
            int listSizeTotal = 0;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.publicCustomerList("", "", 100, i).getJSONArray("list");
                listSizeTotal += list.size();
            }
            CommonUtil.valueView(total, listSizeTotal);
            Preconditions.checkArgument(total == listSizeTotal, "pc销售客户管理公海共计人数为：" + total + "列表总数为：" + listSizeTotal + "两者不相等");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售客户管理--公海--共计人数=列表总条数");
        }
    }

    @Test(description = "销售客户管理--公海--今日人数=按今日搜索展示列表条数")
    public void myCustomer_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            JSONObject object = crm.customerList("", "", "", date, date, 1, 10);
            int total = object.getInteger("total");
            int pageSize = CommonUtil.getTurningPage(total, 100);
            int listSizeTotal = 0;
            for (int i = 1; i < pageSize; i++) {
                int listSize = crm.customerList("", "", "", date, date, i, 100).getJSONArray("list").size();
                listSizeTotal += listSize;
            }
            CommonUtil.valueView(total, listSizeTotal);
            Preconditions.checkArgument(listSizeTotal == total, "pc端今日客戶人数：" + total + "不等于" + "按今日搜索展示列表条数：" + listSizeTotal);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售客户管理--公海--今日人数=按今日搜索展示列表条数");
        }
    }

    @Test(description = "销售客户管理--公海--勾选公海客户分配给销售A，销售A客户名下客户数量+1，列表数-1")
    public void myCustomer_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(xs);
            int customerTotal = crm.customerPage(100, 1, "", "", "").getInteger("total");
            //公海人数总量
            UserUtil.login(zjl);
            JSONObject publicCustomerList = crm.publicCustomerList("", "", 10, 1);
            String name = CommonUtil.getStrField(publicCustomerList, 0, "customer_name");
            int customerId = CommonUtil.getIntField(publicCustomerList, 0, "customer_id");
            int publicTotal = publicCustomerList.getInteger("total");
            //把公海分配销售
            crm.customerAllot(xs.getUid(), customerId);
            //分配之后公海总数
            JSONObject publicCustomerList1 = crm.publicCustomerList("", "", 10, 1);
            int publicTotal1 = publicCustomerList1.getInteger("total");
            //等级变为c
            String customerLevelName = null;
            int total = crm.customerList(name, "", "", "", "", 1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.customerList(name, "", "", "", "", i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getInteger("customer_id") == customerId) {
                        customerLevelName = list.getJSONObject(j).getString("customer_level_name");
                    }
                }
            }
            //分配之后后销售名下客户数量
            UserUtil.login(xs);
            int customerTotal1 = crm.customerPage(100, 1, "", "", "").getInteger("total");
            CommonUtil.valueView(customerTotal, customerTotal1, publicTotal, publicTotal1, customerLevelName);
            Preconditions.checkArgument(customerTotal1 == customerTotal + 1, "公海客户分配给销售顾问后，该销售名下客户数量未+1");
            Preconditions.checkArgument(publicTotal1 == publicTotal - 1, "公海客户分配给销售顾问后，公海客户数量未-1");
            Preconditions.checkArgument(customerLevelName != null && customerLevelName.equals("C"), "客户从公海分配销售后，等级未变为C");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售客户管理--公海--勾选公海客户分配给销售A，销售A客户名下客户数量+1，列表数-1");
        }
    }

    @Test(description = "销售客户管理--公海--新建一个G级客户，公海列表数+1")
    public void myCustomer_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        String name = EnumCustomerInfo.CUSTOMER_1.getName();
        String remark = EnumCustomerInfo.CUSTOMER_1.getRemark();
        try {
            String phone = method.getDistinctPhone();
            int publicTotal = crm.publicCustomerList("", "", 10, 1).getInteger("total");
            //创建线索
            crm.customerCreate(name, "8", phone, car.getModelId(), car.getStyleId(), remark);
            int publicTotal1 = crm.publicCustomerList("", "", 10, 1).getInteger("total");
            CommonUtil.valueView(publicTotal, publicTotal1);
            Preconditions.checkArgument(publicTotal1 == publicTotal + 1, "新建一个G级客户，公海数未+1");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售客户管理--公海--新建一个G级客户，公海列表数+1");
        }
    }

    @Test(description = "销售客户管理--战败--共计人数=列表总条数")
    public void myCustomer_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject failureCustomerList = crm.failureCustomerList("", "", 1, 100);
            int listSize = failureCustomerList.getJSONArray("list").size();
            int total = failureCustomerList.getInteger("total");
            CommonUtil.valueView(listSize, total);
            Preconditions.checkArgument(listSize == total, "pc销售客户管理战败共计人数为" + total + "列表总条数为" + listSize);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售客户管理--战败--共计人数=列表总条数");
        }
    }

    @Test(description = "销售客户管理--战败--今日人数=按今日搜索展示列表条数")
    public void myCustomer_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            JSONObject failureCustomerList = crm.failureCustomerList(date, date, 1, 100);
            int listSize = failureCustomerList.getJSONArray("list").size();
            int today = failureCustomerList.getInteger("today");
            Preconditions.checkArgument(listSize == today, "pc销售客户管理战败今日人数为" + today + "按今日搜索列表总条数为" + listSize);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售客户管理--战败--今日人数=按今日搜索展示列表条数");
        }
    }

    @Test(description = "销售客户管理--战败--将一个战败客户划入公海,战败客户列表数量-1,公海客户列表数量+1")
    public void myCustomer_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //战败列表数
            JSONObject failureCustomerList = crm.failureCustomerList("", "", 1, 10);
            int failureTotal = failureCustomerList.getInteger("total");
            if (failureTotal > 0) {
                int customerId = CommonUtil.getIntField(failureCustomerList, 0, "customer_id");
                //公海列表数
                int publicTotal = crm.publicCustomerList("", "", 10, 1).getInteger("total");
                //战败转公海
                crm.failureCustomerToPublic(customerId);
                int failureTotal1 = crm.failureCustomerList("", "", 1, 10).getInteger("total");
                int publicTotal1 = crm.publicCustomerList("", "", 10, 1).getInteger("total");
                CommonUtil.valueView(publicTotal, publicTotal1, failureTotal, failureTotal1, customerId);
                Preconditions.checkArgument(failureTotal == failureTotal1 + 1, "战败转移公海前战败数量为" + failureTotal + "战败转公海后战败数量为" + failureTotal1);
                Preconditions.checkArgument(publicTotal == publicTotal1 - 1, "战败转移公海前公海数量为" + failureTotal + "战败转公海后公海数量为" + failureTotal1);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售客户管理--战败--将一个战败客户划入公海,战败客户列表数量-1,公海客户列表数量+1");
        }
    }

    @Test(description = "销售客户管理--小程序--共计人数=列表总条数")
    public void myCustomer_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject weChatCustomerList = crm.wechatCustomerList("", "", 1, 100);
            int listSize = weChatCustomerList.getJSONArray("list").size();
            int total = weChatCustomerList.getInteger("total");
            CommonUtil.valueView(listSize, total);
            Preconditions.checkArgument(listSize == total, "小程序客户总数为：" + total + "列表总数为：" + listSize);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售客户管理--小程序--共计人数=列表总条数");
        }
    }

    @Test(description = "销售客户管理--小程序--今日人数=按今日搜索展示列表条数")
    public void myCustomer_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            JSONObject weChatCustomerList = crm.wechatCustomerList(date, date, 1, 100);
            int listSize = weChatCustomerList.getJSONArray("list").size();
            int today = weChatCustomerList.getInteger("today");
            CommonUtil.valueView(listSize, today);
            Preconditions.checkArgument(listSize == today, "小程序今日客户总数为：" + today + "按今日搜索列表总数为：" + listSize);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售客户管理--小程序--今日人数=按今日搜索展示列表条数");
        }
    }

    @Test(description = "销售客户管理--小程序--预约试驾=小程序“我的”预约试驾条数,预约保养=小程序“我的”预约保养条数,预约维修=小程序“我的”预约维修条数", enabled = false)
    public void myCustomer_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.loginApplet(EnumAppletCode.WM);
            int testDriverTotal = crm.appointmentList(0L, EnumAppointmentType.TEST_DRIVE.getType(), 100).getInteger("total");
            int maintainTotal = crm.appointmentList(0L, EnumAppointmentType.MAINTAIN.getType(), 100).getInteger("total");
            int repairTotal = crm.appointmentList(0L, EnumAppointmentType.REPAIR.getType(), 100).getInteger("total");
            UserUtil.login(zjl);
            JSONArray list = crm.wechatCustomerList("", "", 1, 2 << 10).getJSONArray("list");
            int appointmentTestDriver = 0;
            int appointmentMend = 0;
            int appointmentMaintain = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("wechat_id").equals(EnumAppletCode.WM.getWeChatId())) {
                    appointmentTestDriver = list.getJSONObject(i).getInteger("appointment_test_driver");
                    appointmentMaintain = list.getJSONObject(i).getInteger("appointment_maintain");
                    appointmentMend = list.getJSONObject(i).getInteger("appointment_mend");
                }
            }
            CommonUtil.valueView(testDriverTotal, maintainTotal, repairTotal, appointmentTestDriver, appointmentMaintain, appointmentMend);
            Preconditions.checkArgument(testDriverTotal == appointmentTestDriver, "pc端预约试驾次数为：" + testDriverTotal + "小程序我的试驾预约总数：" + appointmentTestDriver);
            Preconditions.checkArgument(maintainTotal == appointmentMaintain, "pc端预约保养次数为：" + maintainTotal + "小程序我的保养预约总数：" + appointmentMaintain);
            Preconditions.checkArgument(repairTotal == appointmentMend, "pc端预约维修次数为：" + repairTotal + "小程序我的维修预约总数：" + appointmentMend);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售客户管理--小程序--预约试驾=小程序“我的”预约试驾条数,预约保养=小程序“我的”预约保养条数,预约维修=小程序“我的”预约维修条数");
        }
    }
}
