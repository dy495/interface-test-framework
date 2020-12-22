package com.haisheng.framework.testng.bigScreen.crmOnline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumReturnVisitResult;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.crmOnline.commonDsOnline.PublicMethodOnline;
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
import java.util.HashSet;
import java.util.Set;

public class AppDataOnline extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    PublicMethodOnline method = new PublicMethodOnline();
    private static final EnumAccount zjl = EnumAccount.ZJL_ONLINE;
    private static final EnumAccount xs = EnumAccount.XS_22_ONLINE;
    private static final EnumAccount newXs = EnumAccount.XS_CS_ONLINE;
    private static final EnumCarModel car = EnumCarModel.PANAMERA_ONLINE;
    private static final int size = 100;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.produce = EnumProduce.BSJ.name();
        commonConfig.referer = EnumRefer.PORSCHE_REFERER_ONLINE.getReferer();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_ONLINE.getName() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.WINSENSE_PORSCHE_ONLINE.getShopId();
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
        UserUtil.login(xs);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test(description = "description = 共计接待=列表总数-等待中数量-去重手机号数量", enabled = false)
    public void myReception_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            int totalReception = response.getInteger("total_reception");
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
            appendFailReason(e.toString());
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
            int todayNewCustomer = response.getInteger("today_new_customer");
            int total = crm.customerMyReceptionList("", "", "", size, 1).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            int ss = 0;
            Set<String> set = new HashSet<>();
            for (int j = 1; j < s; j++) {
                JSONArray list = crm.customerMyReceptionList("", "", "", size, j).getJSONArray("list");
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
            }
            int x = ss - set.size();
            CommonUtil.valueView(todayNewCustomer, ss, x);
            Preconditions.checkArgument(todayNewCustomer == ss - x, "今日新客接待" + todayNewCustomer + "接待日期为今天 客户类型为新客的手机号去重数量" + (ss - x));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
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
            int todayOldCustomer = response.getInteger("total_old_customer");
            int total = crm.customerMyReceptionList("", "", "", size, 1).getInteger("total");
            int x = CommonUtil.getTurningPage(total, size);
            Set<String> set = new HashSet<>();
            int ss = 0;
            for (int j = 0; j < x; j++) {
                JSONArray list = crm.customerMyReceptionList("", "", "", size, 1).getJSONArray("list");
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
            }
            int s = ss - set.size();
            CommonUtil.valueView(todayOldCustomer, ss, s);
            Preconditions.checkArgument(todayOldCustomer == ss - s, "今日老客接待!=接待日期为今天 客户类型为老客的手机号去重数量");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日新客接待==接待日期为今天 客户类型为新客的手机号去重数量");
        }
    }

    @Test(description = "今日新客接待+今日老客接待=【PC端销售排班】该销售今日接待次数")
    public void myReception_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(xs);
            JSONObject response = crm.customerReceptionTotalInfo();
            int todayOldCustomer = response.getInteger("total_old_customer");
            int todayNewCustomer = response.getInteger("today_new_customer");
            int todayCustomerNum = 0;
            JSONArray list = crm.receptionOrder().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("sale_name").equals(xs.getUsername())) {
                    todayCustomerNum = list.getJSONObject(i).getInteger("today_customer_num");
                }
            }
            CommonUtil.valueView(todayCustomerNum, todayNewCustomer, todayOldCustomer);
            Preconditions.checkArgument(todayCustomerNum >= todayNewCustomer + todayOldCustomer, "今日新客接待+今日老客接待：" + todayNewCustomer + todayOldCustomer + "【PC端销售排班】该销售今日接待次数：" + todayCustomerNum);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日新客接待+今日老客接待=【PC端销售排班】该销售今日接待次数");
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
            int todayDeliverCarTotal = response.getInteger("today_deliver_car_total");
            String startDay = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd");
            int todayTotal = crm.deliverCarAppList("", 1, size, startDay, startDay).getInteger("total");
            int s = CommonUtil.getTurningPage(todayTotal, size);
            int listSize = 0;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.deliverCarAppList("", i, size, startDay, startDay).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    listSize++;
                }
            }
            CommonUtil.valueView(todayDeliverCarTotal, listSize);
            Preconditions.checkArgument(todayDeliverCarTotal == listSize, "今日交车数" + todayDeliverCarTotal + "今日交车列表手机号去重后列数和" + listSize);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日交车数=今日交车列表手机号去重后列数和");
        }
    }

    @Test(description = "实际交车>=今日交车")
    public void myDeliverCar_data_2() {
        logger.info(caseResult.getCaseName());
        try {
            JSONObject response = crm.deliverCarTotal();
            int deliverCarTotal = response.getInteger("deliver_car_total");
            int todayDeliverCarTotal = response.getInteger("today_deliver_car_total");
            CommonUtil.valueView(deliverCarTotal, todayDeliverCarTotal);
            Preconditions.checkArgument(deliverCarTotal >= todayDeliverCarTotal, "实际交车<今日交车");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实际交车>=今日交车");
        }
    }

    @Test(description = "全部交车>=实际交车")
    public void myDeliverCar_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.deliverCarTotal();
            int totalOrder = response.getInteger("total_order");
            int deliverCarTotal = response.getInteger("deliver_car_total");
            Preconditions.checkArgument(totalOrder >= deliverCarTotal, "全部交车<实际交车");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
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
            UserUtil.login(zjl);
            String date = DateTimeUtil.getFormat(new Date());
            JSONObject response = crm.driverTotal();
            int todayTestDriveTotal = response.getInteger("today_test_drive_total");
            int total = crm.testDriverAppList("", date, date, size, 1).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            int x = 0;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.testDriverAppList("", date, date, size, i).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("audit_status_name").equals("已通过")) {
                        x++;
                    }
                }
            }
            CommonUtil.valueView(todayTestDriveTotal, x);
            Preconditions.checkArgument(todayTestDriveTotal == x, "今日试驾数：" + todayTestDriveTotal + " 今日试驾列表手机号去重后列表数和：" + x);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日试驾数=今日试驾列表手机号去重后列表数和");
        }
    }

    @Test(description = "全部试驾>=今日试驾")
    public void myTestDriver_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.driverTotal();
            int todayTestDriveTotal = response.getInteger("today_test_drive_total");
            int testDriveTotal = response.getInteger("test_drive_total");
            CommonUtil.valueView(testDriveTotal, testDriveTotal);
            Preconditions.checkArgument(testDriveTotal >= todayTestDriveTotal, "全部试驾<今日试驾");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
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
            UserUtil.login(xs);
            int total = crm.customerPage(size, 1, "", "", "").getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            int listSize = 0;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.customerPage(size, i, "", "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    listSize++;
                }
            }
            CommonUtil.valueView(total, listSize);
            Preconditions.checkArgument(total == listSize, "全部客户数量:" + total + "列表数:" + listSize + "不相等");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("全部客户=列表数");
        }
    }

    @Test(description = "创建线索,全部客户+1")
    public void myCustomer_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        EnumCustomerInfo customerInfo = EnumCustomerInfo.CUSTOMER_1;
        try {
            String phone = method.getDistinctPhone();
            int total = crm.customerPage(1, size, "", "", "").getInteger("total");
            //创建线索
            crm.customerCreate(customerInfo.getName(), "2", phone, car.getModelId(), car.getStyleId(), customerInfo.getRemark());
            int total1 = crm.customerPage(1, size, "", "", "").getInteger("total");
            CommonUtil.valueView(total, total1);
            Preconditions.checkArgument(total1 == total + 1, "创建线索,全部客未+1");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建线索,全部客户+1");
        }
    }

    @Test(description = "前台分配新客，创建时手机号不存在,全部客户+1", enabled = false)
    public void myCustomer_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        EnumCustomerInfo customerInfo = EnumCustomerInfo.CUSTOMER_1;
        try {
            String phone = method.getDistinctPhone();
            //客户总数
            int total = crm.customerPage(size, 1, "", "", "").getInteger("total");
            //分配客户-获取客户id
            long customerId = getCustomerId();
            UserUtil.login(zjl);
            //完成接待
            crm.customerFinishReception(zjl.getUid(), customerId, 3, customerInfo.getName(), phone, customerInfo.getRemark());
            int total1 = crm.customerPage(size, 1, "", "", "").getInteger("total");
            CommonUtil.valueView(total, total1);
            Preconditions.checkArgument(total1 == total + 1, "前台分配新客，创建时手机号不存在,全部客户没有+1");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("前台分配新客，创建时手机号不存在,全部客户+1");
        }
    }

    @Test(description = "已订车=列表中是否订车为是的数量")
    public void myCustomer_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerPage(size, 1, "", "", "");
            int total = response.getInteger("total");
            int buyCarNum = response.getInteger("buy_car_num");
            int listSize = 0;
            for (int i = 1; i < CommonUtil.getTurningPage(total, size); i++) {
                JSONArray list = crm.customerPage(size, i, "", "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("if_buy_car_name").equals("是")) {
                        listSize++;
                    }
                }
            }
            CommonUtil.valueView(buyCarNum, listSize);
            Preconditions.checkArgument(buyCarNum == listSize, "已订车数量!=列表中是否订车为是的数量");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("已订车=列表中是否订车为是的数量");
        }
    }

    @Test(description = "全部客户>=已订车>=已交车")
    public void myCustomer_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerPage(size, 1, "", "", "");
            int total = response.getInteger("total");
            int buyCarNum = response.getInteger("buy_car_num");
            int deliverCarNum = response.getInteger("deliver_car_num");
            CommonUtil.valueView(total, buyCarNum, deliverCarNum);
            Preconditions.checkArgument(total >= buyCarNum, "全部客户<已定车数");
            Preconditions.checkArgument(buyCarNum >= deliverCarNum, "已定车数<已交车数");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("全部客户>=已订车>=已交车");
        }
    }

    @Test(description = "非订单客户,1=<剩余天数<90")
    public void myCustomer_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(xs);
            JSONObject response = crm.customerPage(size, 1, "", "", "");
            JSONArray list = response.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (!list.getJSONObject(i).getString("customer_level_name").equals(EnumCustomerLevel.D.getName())
                        && !list.getJSONObject(i).getString("customer_level_name").equals(EnumCustomerLevel.O.getName())
                        && !list.getJSONObject(i).getString("customer_level_name").equals(EnumCustomerLevel.G.getName())
                        && !list.getJSONObject(i).getString("customer_level_name").equals(EnumCustomerLevel.F.getName())) {
                    int remainDays = list.getJSONObject(i).getInteger("remain_days");
                    CommonUtil.valueView(remainDays);
                    Preconditions.checkArgument(remainDays < 90, "app，我的客户列表中存在剩余天数>90的记录，" + "天数为：" + remainDays);
                    Preconditions.checkArgument(remainDays >= 1, "app，我的客户列表中存在剩余天数<1的记录，" + "天数为：" + remainDays);
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("非订单客户,1=<剩余天数<90");
        }
    }

    @Test(description = "app我的客户页列表数=PC我的客户页列表数")
    public void myCustomer_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerPage(size, 1, "", "", "");
            int total = response.getInteger("total");
            int listSize = 0;
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.customerList("", "", "", "", "", i, size).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    listSize++;
                }
            }
            CommonUtil.valueView(total, listSize);
            Preconditions.checkArgument(total == listSize, "app我的客户页列表数!=PC我的客户页列表数");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app我的客户页列表数=PC我的客户页列表数");
        }
    }

    @Test(description = "删除销售顾问，【PC公海】共计数量=原数量+【我的客户】全部客户数")
    public void myCustomer_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        EnumCustomerInfo customerInfo = EnumCustomerInfo.CUSTOMER_1;
        String salePhone = "13654973499";
        //查询公海数量
        try {
            //先删除15321527989顾问的账号
            deleteSaleUser(salePhone);
            //公海客户数量
            int total = crm.publicCustomerList("", "", size, 1).getInteger("total");
            //添加销售
            crm.addUser(newXs.getAccount(), newXs.getAccount(), salePhone, newXs.getPassword(), 407, "", "");
            //创建
            String customerPhone = method.getDistinctPhone();
            UserUtil.login(newXs);
            crm.customerCreate(customerInfo.getName(), String.valueOf(EnumCustomerLevel.B.getId()), customerPhone, car.getModelId(), car.getStyleId(), customerInfo.getRemark());
            //删除此新增的顾问
            deleteSaleUser(salePhone);
            //公海数量+1
            int total1 = crm.publicCustomerList("", "", size, 1).getInteger("total");
            CommonUtil.valueView(total, total1);
            Preconditions.checkArgument(total1 == total + 1, "删除所属销售，公海数量未增加");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("删除销售顾问，【PC公海】共计数量=原数量+【我的客户】全部客户数");
        }
    }

    /**
     * @description: 工作管理-我的预约
     */
    @Test(description = "全部预约人数>=今日预约人数")
    public void myAppointment_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //预约试驾
            JSONObject response = crm.appointmentTestDriverNumber();
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
            appendFailReason(e.toString());
        } finally {
            saveData("全部预约人数>=今日预约人数");
        }
    }

    @Test(description = "全部预约人数<=列表数")
    public void myAppointment_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(xs);
            //预约试驾
            Integer testDriverTotalNumber = crm.appointmentTestDriverNumber().getInteger("appointment_total_number");
            //预约试驾列表
            Integer testDriveTotal = crm.appointmentTestDriverList("", "", "", 1, size).getInteger("total");
            //预约保养
            Integer maintainTotalNumber = crm.mainAppointmentDriverNum().getInteger("appointment_total_number");
            //预约保养列表
            Integer maintainTotal = crm.mainAppointmentList().getInteger("total");
            //预约维修
            Integer repairTotalNumber = crm.repairAppointmentDriverNum().getInteger("appointment_total_number");
            //预约维修列表
            Integer repairTotal = Integer.parseInt(crm.repairAppointmentlist().getString("total"));
            CommonUtil.valueView(testDriverTotalNumber, testDriveTotal, maintainTotalNumber, maintainTotal);
            Preconditions.checkArgument(testDriverTotalNumber <= testDriveTotal, "全部预约试驾数>预约试驾任务列表数");
            Preconditions.checkArgument(maintainTotalNumber <= maintainTotal, "售后保养--全部预约车辆>售后预约列表数");
            Preconditions.checkArgument(repairTotalNumber <= repairTotal, "售后维修--全部预约车辆>售后预约列表数");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("全部预约人数<=列表数");
        }
    }

    @Test(description = "销售完成回访任务,电话预约=已完成+1、电话预约=未完成-1、列表总数不变", enabled = false)
    public void myAppointment_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String preBuyCarTime = DateTimeUtil.dateToStamp(date);
        EnumCustomerInfo customerInfo = EnumCustomerInfo.CUSTOMER_3;
        try {
            //登录小程序
            UserUtil.loginApplet(EnumAppletToken.BSJ_WM_ONLINE);
            //预约试驾
            UserUtil.loginApplet(EnumAppletToken.BSJ_WM_ONLINE);
            crm.appointmentTestDrive(customerInfo.getGender(), customerInfo.getName(), customerInfo.getPhone(), date, 1, 36);

            //电话预约已完成数量
            int phoneAppointmentNum = 0;
            UserUtil.login(xs);
            JSONArray list = crm.appointmentTestDriverList("", "", "", 1, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (!list.getJSONObject(i).getBoolean("phone_appointment")) {
                    phoneAppointmentNum++;
                }
            }
            //获取taskId
            int taskId = 0;
            JSONArray list1 = crm.returnVisitTaskPage(1, size).getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                if (list1.getJSONObject(i).getString("customer_name").equals(customerInfo.getName())
                        && list1.getJSONObject(i).getString("customer_phone").equals(customerInfo.getPhone())
                        && list1.getJSONObject(i).getString("task_status_name").equals("未完成")) {
                    taskId = list1.getJSONObject(i).getInteger("task_id");
                    break;
                }
            }
            //回访
            String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
            String picture = new ImageUtil().getImageBinary(picPath);
            crm.returnVisitTaskExecute(customerInfo.getRemark(), "", "", false, date, "", preBuyCarTime, EnumReturnVisitResult.ANSWER.getName(), String.valueOf(taskId), picture);
            //获取
            int x = 0;
            JSONArray s = crm.appointmentTestDriverList("", "", "", 1, size).getJSONArray("list");
            for (int i = 0; i < s.size(); i++) {
                if (!s.getJSONObject(i).getBoolean("phone_appointment")) {
                    x++;
                }
            }
            CommonUtil.valueView(phoneAppointmentNum, x);
            Preconditions.checkArgument(x == phoneAppointmentNum + 1, "完成回访任务，我的预约列表电话预约没有变为已完成，还是未完成状态");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("完成回访任务，我的预约列表电话预约变为已完成");
        }
    }

    @Test(description = "客户小程序取消预约试驾，客户状态=已取消的数量+1", enabled = false)
    public void myAppointment_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String data = DateTimeUtil.getFormat(new Date());
        EnumCustomerInfo customerInfo = EnumCustomerInfo.CUSTOMER_3;
        try {
            //更新小程序token
            UserUtil.loginApplet(EnumAppletToken.BSJ_WM_ONLINE);
            //预约试驾
            JSONObject response = crm.appointmentTestDrive(customerInfo.getGender(), customerInfo.getName(), customerInfo.getPhone(), data, 1, 36);
            int appointmentId = response.getInteger("appointment_id");
            //已取消数量
            int cancelNum1 = getCancelNum("已取消");
            //取消试驾
            UserUtil.loginApplet(EnumAppletToken.BSJ_WM_ONLINE);
            crm.appointmentCancel(appointmentId);
            //已取消数量
            int cancelNum2 = getCancelNum("已取消");
            CommonUtil.valueView(cancelNum1, cancelNum2);
            Preconditions.checkArgument(cancelNum2 == cancelNum1 + 1, "小程序预约试驾后，再取消，已取消状态未+1");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户小程序取消预约试驾,客户状态=已取消的数量+1");
        }
    }

    @Test(description = "客户小程序取消预约试驾，列表数不变", enabled = false)
    public void myAppointment_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        String data = DateTimeUtil.getFormat(new Date());
        EnumCustomerInfo customerInfo = EnumCustomerInfo.CUSTOMER_3;
        try {
            //更新小程序token
            UserUtil.loginApplet(EnumAppletToken.BSJ_WM_ONLINE);
            JSONObject response = crm.appointmentTestDrive(customerInfo.getGender(), customerInfo.getName(), customerInfo.getPhone(), data, 1, 36);
            int appointmentId = response.getInteger("appointment_id");
            //获取列表总数
            UserUtil.login(xs);
            JSONObject response1 = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
            Integer total = response1.getInteger("total");
            //取消试驾
            UserUtil.loginApplet(EnumAppletToken.BSJ_WM_ONLINE);
            crm.appointmentCancel(appointmentId);
            //获取列表总数
            UserUtil.login(xs);
            JSONObject response2 = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
            Integer total1 = response2.getInteger("total");
            CommonUtil.valueView(total, total1);
            Preconditions.checkArgument(total.equals(total1), "小程序取消预约试驾后，app我的预约列表数量变化了，应该不变");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户小程序取消预约试驾,列表总数不变");
        }
    }

    @Test(description = "客户小程序预约试驾，列表条数+1、客户状态=预约中数量+1，列表信息与小程序新建预约时信息一致、预约日期、联系人、试驾车型、联系电话", enabled = false)
    public void myAppointment_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        String data = DateTimeUtil.getFormat(new Date());
        EnumCustomerInfo customerInfo = EnumCustomerInfo.CUSTOMER_3;
        try {
            //列表条数
            UserUtil.login(xs);
            JSONObject response = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
            Integer total1 = response.getInteger("total");
            //预约中数量
            int appointmentNum = getCancelNum("预约中");
            //预约试驾
            UserUtil.loginApplet(EnumAppletToken.BSJ_WM_ONLINE);
            JSONObject result = crm.appointmentTestDrive(customerInfo.getGender(), customerInfo.getName(), customerInfo.getPhone(), data, 1, 36);
            int appointmentId = result.getInteger("appointment_id");
            //列表条数
            UserUtil.login(xs);
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
            Integer total2 = response2.getInteger("total");
            CommonUtil.valueView(total1, total2, appointmentNum, appointmentNum1, appointmentDate, carType, customerName, customerPhoneNumber);
            Preconditions.checkArgument(appointmentDate.equals(data), "app我的预约记录中预约日期与小程序不一致");
            Preconditions.checkArgument(customerName.equals(customerInfo.getName()), "app我的预约记录中客户名称与小程序不一致");
            Preconditions.checkArgument(carType == 4, "app我的预约记录中试驾车型与小程序不一致");
            Preconditions.checkArgument(customerPhoneNumber.equals(customerInfo.getPhone()), "app我的预约记录中联系电话与小程序不一致");
            Preconditions.checkArgument(total2 == total1 + 1, "预约试驾后，app我的预约列表条数未+1");
            Preconditions.checkArgument(appointmentNum1 == appointmentNum + 1, "预约试驾后，app我的预约列表预约中状态未+1");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约试驾后，验证小程序和app数据一致性");
        }
    }

    @Test(description = "同一客户，一天内小程序预约试驾2次,列表条数+2、今日预约人数+1、全部预约+1", enabled = false)
    public void myAppointment_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        EnumCustomerInfo customerInfo = EnumCustomerInfo.CUSTOMER_3;
        EnumCustomerInfo customerInfo1 = EnumCustomerInfo.CUSTOMER_4;
        try {
            JSONObject object = crm.appointmentTestDriverNumber();
            //全部预约
            int appointmentTotalNumber = object.getInteger("appointment_total_number");
            //今日预约
            int appointmentTodayNumber = object.getInteger("appointment_today_number");
            JSONObject response = crm.appointmentTestDriverList("", "", "", 1, 2 << 10);
            //列表数
            int listSize = response.getJSONArray("list").size();
            //预约试驾
            UserUtil.loginApplet(EnumAppletToken.BSJ_WM_ONLINE);
            String data = DateTimeUtil.getFormat(new Date());

            crm.appointmentTestDrive(customerInfo.getGender(), customerInfo.getName(), customerInfo.getPhone(), data, 1, 36);
            //连续访问接口会失败，延迟3s
            sleep(3);
            crm.appointmentTestDrive(customerInfo1.getGender(), customerInfo1.getName(), customerInfo1.getPhone(), data, 1, 36);
            UserUtil.login(xs);

            JSONObject object1 = crm.appointmentTestDriverNumber();
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
            appendFailReason(e.toString());
        } finally {
            saveData("同一客户，一天内小程序预约试驾2次,列表条数+2,今日预约人数+1,全部预约+1");
        }
    }

    @Test(description = "两个客户，预约今天/明天明天试驾,列表条数+2、今日预约人数+2、全部预约+2", enabled = false)
    public void myAppointment_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        EnumCustomerInfo customerInfo = EnumCustomerInfo.CUSTOMER_3;
        EnumCustomerInfo customerInfo1 = EnumCustomerInfo.CUSTOMER_4;
        try {
            String date = DateTimeUtil.getFormat(new Date());
            String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
            UserUtil.login(zjl);
            JSONObject response = crm.appointmentTestDriverNumber();
            int todayNumber = response.getInteger("appointment_today_number");
            int totalNumber = response.getInteger("appointment_total_number");
            int listSize = crm.appointmentTestDriverList("", "", "", 1, 2 << 10).getJSONArray("list").size();
            //两个人预约试驾-今明两天
            UserUtil.loginApplet(EnumAppletToken.BSJ_WM_ONLINE);
            crm.appointmentTestDrive(customerInfo.getGender(), customerInfo.getName(), customerInfo.getPhone(), date, 1, 36);
            UserUtil.loginApplet(EnumAppletToken.BSJ_WM_ONLINE);
            crm.appointmentTestDrive(customerInfo1.getGender(), customerInfo1.getName(), customerInfo1.getPhone(), date1, 1, 36);
            CommonUtil.valueView(todayNumber, totalNumber, listSize);
            UserUtil.login(zjl);
            JSONObject response1 = crm.appointmentTestDriverNumber();
            int todayNumber1 = response1.getInteger("appointment_today_number");
            int totalNumber1 = response1.getInteger("appointment_total_number");
            int listSize1 = crm.appointmentTestDriverList("", "", "", 1, 2 << 10).getJSONArray("list").size();
            Preconditions.checkArgument(todayNumber + 2 == todayNumber1, "两个客户，预约今天/明天明天试驾,今日预约人数没有+2");
            Preconditions.checkArgument(totalNumber + 2 == totalNumber1, "两个客户，预约今天/明天明天试驾,全部预约没有+2");
            Preconditions.checkArgument(listSize + 2 == listSize1, "两个客户，预约今天/明天明天试驾,列表条数没有+2");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("两个客户，预约今天/明天明天试驾,列表条数+2、今日预约人数+2、全部预约+2");
        }
    }

    @Test(description = "PC预约试驾，与当前登陆app接待销售一致的数量=列表数", enabled = false)
    public void myAppointment_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //pc端试驾列表数
            UserUtil.login(zjl);
            int pcTotal = crm.testDriverPage("", "", 1, size).getInteger("total");
            //app端预约数量
            UserUtil.login(xs);
            int appTotal = crm.appointmentTestDriverList("", "", "", 1, size).getInteger("total");
            int listSize = 0;
            int s = CommonUtil.getTurningPage(appTotal, size);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.appointmentTestDriverList("", "", "", 1, size).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    listSize++;
                }
            }
            CommonUtil.valueView(pcTotal, appTotal, listSize);
            Preconditions.checkArgument(pcTotal == appTotal, "PC某个销售顾问预约试驾数：" + pcTotal + " ！=该销售在【app-我的预约】记录总数量：" + appTotal);
            Preconditions.checkArgument(pcTotal == listSize, "PC某个销售顾问预约试驾数：" + pcTotal + " ！=该销售在【app-我的预约】记录列表总数：" + listSize);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC预约试驾按照销售顾问分类，与每个销售顾问app-我的预约列表数一致");
        }
    }

    /**
     * 获取状态数量
     *
     * @param serviceStatusName 服务状态名称：已取消、预约中、已接待
     */
    private Integer getCancelNum(String serviceStatusName) {
        UserUtil.login(xs);
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
        UserUtil.login(zjl);
        long customerId;
        //获取当前空闲第一位销售id
        String saleId = CommonUtil.getStrField(crm.freeSaleList(), 0, "sale_id");
        //获取销售账号名
        String userLoginName = "";
        JSONArray userList = crm.userUserPage(1, size).getJSONArray("list");
        for (int i = 0; i < userList.size(); i++) {
            JSONObject obj = userList.getJSONObject(i);
            if (obj.getString("user_id").equals(saleId)) {
                userLoginName = obj.getString("user_login_name");
            }
        }
        //创建接待
        JSONArray customerList = new JSONArray();
        JSONArray newCustomerList = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("customer_name", "哒哒");
        object.put("is_decision", true);
        newCustomerList.add(object);
        crm.saleReception(EnumReceptionType.FIRST_VISIT.getType(), newCustomerList, customerList);
        //销售登陆，获取当前接待id
        crm.login(userLoginName, xs.getPassword());
        customerId = crm.userInfService().getLong("customer_id");
        return customerId;
    }

    /**
     * 删除
     *
     * @param phone 角色电话
     */
    private void deleteSaleUser(String phone) throws Exception {
        UserUtil.login(zjl);
        int total = crm.userUserPage(1, size).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int j = 1; j < s; j++) {
            JSONArray userList = crm.userUserPage(j, size).getJSONArray("list");
            for (int i = 0; i < userList.size(); i++) {
                if (userList.getJSONObject(i).getString("user_phone").equals(phone)) {
                    String userId = userList.getJSONObject(i).getString("user_id");
                    //删除销售
                    crm.userDel(userId);
                }
            }
        }
    }
}
