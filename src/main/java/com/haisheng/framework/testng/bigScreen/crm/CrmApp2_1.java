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
import java.util.HashSet;
import java.util.Set;

/**
 * crmApp2.1自动化用例
 *
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
            CommonUtil.valueView(crm.returnVisitTaskInfo(taskId));
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
            CommonUtil.valueView(crm.returnVisitTaskExecute(taskId));
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        }
    }

    @Test(enabled = false)
    public void afterSaleCustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.publicFaceList();
            String analysisCustomerId = CommonUtil.getStrFieldByData(response, 0, "analysis_customer_id");
            crm.afterSalelCustomer(analysisCustomerId);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后客户标记");
        }
    }

    @Test(description = "接待状态为接待中数量<=1")
    public void myReceptionList() {
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
            Preconditions.checkArgument(a <= 1, "接待状态为接待中数量>1");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("接待状态为接待中数量<=1");
        }
    }

    @Test(description = "共计接待=列表总数-等待中数量-去重手机号数量")
    public void totalReception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            int totalReception = CommonUtil.getIntFieldByData(response, "total_reception");
            //列表总数
            JSONObject response1 = crm.customerMyReceptionList("", "", "", totalReception + 1000, 1);
            JSONArray list = response1.getJSONArray("list");
            int listTotal = list.size();
            int waitTotal = 0;
            //等待中数量
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("user_status_name").equals("等待中")) {
                    waitTotal++;
                }
            }
            int s = CommonUtil.phoneDuplicates(response1);
            Preconditions.checkArgument(totalReception == listTotal - waitTotal - s, "共计接待!=列表总数-等待中数量-去重手机号数量");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("共计接待==列表总数-等待中数量-去重手机号数量");
        }
    }

    @Test(description = "今日新客接待=接待日期为今天 客户类型为新客的手机号去重数量")
    public void todayNewCustomer() {
        logger.info(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            //今日新客接待
            int todayNewCustomer = CommonUtil.getIntFieldByData(response, "today_new_customer");
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
    public void totalOldCustomer() {
        logger.info(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            //今日老客接待
            int todayOldCustomer = CommonUtil.getIntFieldByData(response, "total_old_customer");
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
    public void todayOrder() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            int todayOrder = CommonUtil.getIntFieldByData(response, "today_order");
            int todayNewCustomer = CommonUtil.getIntFieldByData(response, "today_new_customer");
            int totalOldCustomer = CommonUtil.getIntFieldByData(response, "total_old_customer");
            Preconditions.checkArgument(todayOrder <= todayNewCustomer + totalOldCustomer, "今日订单数>今日新客接待+今日老客接待");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日订单数<=今日新客接待+今日老客接待");
        }
    }

    @Test(description = "【我的接待】共计接待 >= 【我的客户】全部客户")
    public void saleReceptionReception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            int totalReception = CommonUtil.getIntFieldByData(response, "total_reception");
            JSONObject response1 = crm.customerPage(10, 1, "", "", "");
            int total = CommonUtil.getIntFieldByData(response1, "total");
            Preconditions.checkArgument(totalReception >= total, "【我的接待】共计接待 < 【我的客户】全部客户");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("【我的接待】共计接待 >= 【我的客户】全部客户");
        }
    }

    @Test(description = "今日新客接待+今日老客接待=【PC端销售排班】该销售今日接待次数")
    public void todayReception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            int todayOldCustomer = CommonUtil.getIntFieldByData(response, "total_old_customer");
            int todayNewCustomer = CommonUtil.getIntFieldByData(response, "today_new_customer");
            int todayCustomerNum = 0;
            JSONArray list = crm.receptionOrder().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("sale_name").equals("销售顾问temp")) {
                    todayCustomerNum = list.getJSONObject(i).getInteger("today_customer_num");
                }
            }
            Preconditions.checkArgument(todayCustomerNum == todayNewCustomer + todayOldCustomer, "今日新客接待+今日老客接待!=【PC端销售排班】该销售今日接待次数");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日新客接待+今日老客接待=【PC端销售排班】该销售今日接待次数");
        }
    }

    @Test(description = "今日新客接待+今日老客接待=【PC端展厅接待】分配销售为该销售条数")
    public void todayReception1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerReceptionTotalInfo();
            int todayOldCustomer = CommonUtil.getIntFieldByData(response, "total_old_customer");
            int todayNewCustomer = CommonUtil.getIntFieldByData(response, "today_new_customer");
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

    @Test(description = "今日交车数=今日交车列表手机号去重后列数和")
    public void todayDeliverCar() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //今日交车数
            JSONObject response = crm.deliverCarTotal();
            int todayDeliverCarTotal = CommonUtil.getIntFieldByData(response, "today_deliver_car_total");
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
    public void deliverCarTotal() {
        logger.info(caseResult.getCaseName());
        try {
            JSONObject response = crm.deliverCarTotal();
            int deliverCarTotal = CommonUtil.getIntFieldByData(response, "deliver_car_total");
            int todayDeliverCarTotal = CommonUtil.getIntFieldByData(response, "today_deliver_car_total");
            CommonUtil.valueView(deliverCarTotal, todayDeliverCarTotal);
            Preconditions.checkArgument(deliverCarTotal >= todayDeliverCarTotal, "实际交车<今日交车");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("实际交车>=今日交车");
        }
    }

    @Test(description = "全部交车>=实际交车")
    public void totalOrder() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.deliverCarTotal();
            int totalOrder = CommonUtil.getIntFieldByData(response, "total_order");
            int deliverCarTotal = CommonUtil.getIntFieldByData(response, "deliver_car_total");
            Preconditions.checkArgument(totalOrder >= deliverCarTotal, "全部交车<实际交车");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("全部交车>=实际交车");
        }
    }

    @Test(description = "全部客户=列表数")
    public void totalCustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerPage(100, 1, "", "", "");
            int total = CommonUtil.getIntFieldByData(response, "total");
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
    public void customerCreate() {
        String phone = "13333333333";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        int total;
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(EnumAccount.XSZJ.getUsername(), EnumAccount.XSZJ.getPassword());
            total = CommonUtil.getIntFieldByData(crm.customerPage(1, 10, "", "", ""), "total");
            //创建线索
            JSONObject response = crm.createLine("【自动化】王先生", 6, phone, 2, remark);
            if (response.getString("message").equals("联系方式系统中已存在~")) {
                //删除客户
                deleteCustomer(phone);
                total = CommonUtil.getIntFieldByData(crm.customerPage(1, 10, "", "", ""), "total");
                crm.createLine("【自动化】王先生", 6, phone, 2, remark);
            }
            int total1 = CommonUtil.getIntFieldByData(crm.customerPage(1, 10, "", "", ""), "total");
            CommonUtil.valueView(total, total1);
            Preconditions.checkArgument(total1 == total + 1, "创建线索,全部客未+1");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索,全部客户+1");
        }
    }

    @Test(description = "已订车=列表中是否订车为是的数量")
    public void buyCarNum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerPage(100, 1, "", "", "");
            int buyCarNum = CommonUtil.getIntFieldByData(response, "buy_car_num");
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
    public void customerTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerPage(100, 1, "", "", "");
            int total = CommonUtil.getIntFieldByData(response, "total");
            int buyCarNum = CommonUtil.getIntFieldByData(response, "buy_car_num");
            int deliverCarNum = CommonUtil.getIntFieldByData(response, "deliver_car_num");
            CommonUtil.valueView(total, buyCarNum, deliverCarNum);
            Preconditions.checkArgument(total >= buyCarNum, "全部客户<已定车数");
            Preconditions.checkArgument(buyCarNum >= deliverCarNum, "已定车数<已交车数");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("全部客户>=已订车>=已交车");
        }
    }

    @Test(description = "非订单客户1=<剩余天数<90")
    public void remainDays() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerPage(100, 1, "", "", "");
            JSONArray list = response.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("customer_level_name") == null
                        || !list.getJSONObject(i).getString("customer_level_name").equals("D级")) {
                    int remainDays = list.getJSONObject(i).getInteger("remain_days");
                    CommonUtil.valueView(remainDays);
                    Preconditions.checkArgument(remainDays < 90, "我的客户-剩余天数>90");
                    Preconditions.checkArgument(remainDays >= 1, "我的客户-剩余天数<1");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("非订单客户1=<剩余天数<90");
        }
    }

    @Test(description = "app我的客户页列表数=PC我的客户页列表数")
    public void myCustomerTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.customerPage(100, 1, "", "", "");
            int total = CommonUtil.getIntFieldByData(response, "total");
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

    @Test(description = "今日试驾数=今日试驾列表手机号去重后列表数和")
    public void testDrive() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.driverTotal();
            int todayTestDriveTotal = CommonUtil.getIntFieldByData(response, "today_test_drive_total");
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
    public void totalTestDrive() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.driverTotal();
            int todayTestDriveTotal = CommonUtil.getIntFieldByData(response, "today_test_drive_total");
            int testDriveTotal = CommonUtil.getIntFieldByData(response, "test_drive_total");
            CommonUtil.valueView(testDriveTotal, testDriveTotal);
            Preconditions.checkArgument(todayTestDriveTotal == testDriveTotal, "全部试驾<今日试驾");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("全部试驾>=今日试驾");
        }
    }

    @Test(description = "删除销售顾问，【PC公海】共计数量=原数量+【我的客户】全部客户数", enabled = false)
    public void deleteUser() {
        String phone = "13333333333";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        logger.logCaseStart(caseResult.getCaseName());
        //查询公海数量
        crm.login(EnumAccount.XSZJ.getUsername(), EnumAccount.XSZJ.getPassword());
        int total = CommonUtil.getIntFieldByData(crm.publicCustomerList("", "", 2 << 10, 1), "total");
        try {
            //添加销售
            crm.login(EnumAccount.BAOSHIJIE.getUsername(), EnumAccount.BAOSHIJIE.getPassword());
            crm.addUser("zdhcs", EnumAccount.ZDHCS.getUsername(), "18989898989", EnumAccount.ZDHCS.getPassword(), 13);
            //登录
            crm.login(EnumAccount.ZDHCS.getUsername(), EnumAccount.ZDHCS.getPassword());
            //创建线索
            JSONObject response = crm.createLine("【自动化】王先生", 6, phone, 2, remark);
            if (response.getString("message").equals("联系方式系统中已存在~")) {
                //删除客户
                deleteCustomer(phone);
                //再创建线索
                crm.createLine("【自动化】王先生", 6, phone, 2, remark);
            }
            //获取新增顾问id
            String userId = null;
            JSONArray userList = crm.userPage(1, 100).getJSONArray("list");
            for (int i = 0; i < userList.size(); i++) {
                if (userList.getJSONObject(i).getString("user_phone").equals("18989898989")) {
                    userId = userList.getJSONObject(i).getString("user_id");
                }
            }
            //删除销售
            crm.login(EnumAccount.BAOSHIJIE.getUsername(), EnumAccount.BAOSHIJIE.getPassword());
            crm.userDel(userId);
            //公海数量+1
            crm.login(EnumAccount.XSZJ.getUsername(), EnumAccount.XSZJ.getPassword());
            int total1 = CommonUtil.getIntFieldByData(crm.publicCustomerList("", "", 2 << 10, 1), "total");
            CommonUtil.valueView(total, total1);
            Preconditions.checkArgument(total1 == total + 1, "删除所属销售，公海数量未增加");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("删除销售顾问，【PC公海】共计数量=原数量+【我的客户】全部客户数");
        }
    }

    @Test(description = "页面内容与pc我的回访一致")
    public void returnVisit() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //app端内容
            String time = DateTimeUtil.getFormat(new Date());
            JSONObject response = crm.returnVisitTaskPage(1, 100, time, time);
            String customerPhone = CommonUtil.getStrFieldByData(response, 0, "customer_phone");
            String belongsSaleName = CommonUtil.getStrFieldByData(response, 0, "belongs_sale_name");
            String customerLevelName = CommonUtil.getStrFieldByData(response, 0, "customer_level_name");
            String customerName = CommonUtil.getStrFieldByData(response, 0, "customer_name");
            String likeCarName = CommonUtil.getStrFieldByData(response, 0, "like_car_name");
            //pc端内容
            crm.login(EnumAccount.XSZJ.getUsername(), EnumAccount.XSZJ.getPassword());
            JSONObject response1 = crm.withFilterAndCustomerDetail("", 0, 1, 100, "", customerPhone, "");
            String saleName = CommonUtil.getStrFieldByData(response1, 0, "sale_name");
            String customerLevel = CommonUtil.getStrFieldByData(response1, 0, "customer_level");
            String pcCustomerName = CommonUtil.getStrFieldByData(response1, 0, "customer_name");
            String customerPhoneNumber = CommonUtil.getStrFieldByData(response1, 0, "customer_phone_number");
            String interestedCarModel = CommonUtil.getStrFieldByData(response1, 0, "interested_car_model");
            Preconditions.checkArgument(belongsSaleName.equals(saleName), "app与pc回访所属销售不同");
            Preconditions.checkArgument(customerLevelName.equals(customerLevel + "级"), "app与pc客户等级不同");
            Preconditions.checkArgument(customerName.equals(pcCustomerName), "app与pc客户名称不同");
            Preconditions.checkArgument(likeCarName.equals(interestedCarModel), "app与pc客户意向车型不同");
            Preconditions.checkArgument(customerPhone.equals(customerPhoneNumber), "app与pc客户电话不同");
            CommonUtil.valueView(customerPhone, belongsSaleName, customerLevelName, customerName, likeCarName, saleName, customerLevel, pcCustomerName, customerPhoneNumber, interestedCarModel);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("页面内容与pc我的回访一致");
        }
    }

    @Test(description = "【pc我的回访】条数=回访任务")
    public void returnVisitNum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String time = DateTimeUtil.getFormat(new Date());
            JSONObject response = crm.returnVisitTaskPage(1, 100, time, time);
            int appReturnVisitNum = response.getJSONArray("list").size();
            int pcReturnVisitNum = 0;
            crm.login(EnumAccount.XSZJ.getUsername(), EnumAccount.XSZJ.getPassword());
            JSONObject response1 = crm.withFilterAndCustomerDetail("", 0, 1, 100, "", "", "");
            JSONArray list = response1.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("sale_name").equals("销售顾问temp")) {
                    pcReturnVisitNum++;
                }
            }
            CommonUtil.valueView(appReturnVisitNum, pcReturnVisitNum);
            Preconditions.checkArgument(appReturnVisitNum == pcReturnVisitNum, "app端我的回访!=pc端我的回访数量");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("【pc我的回访】条数=回访任务");
        }
    }

    @Test(description = "前台分配新客，创建时手机号不存在，全部客户+1", enabled = false)
    public void create() {
        String phone = "13333333332";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        Long customerId = 13997L;
        //获取客户列表
        int total = CommonUtil.getIntFieldByData(getCustomerList(20), "total");
        try {
//            Long customerId = getCustomerId();
            //完成接待
            crm.finishReception(customerId, 7, "【自动化】王敏先生", phone, remark);
            //获取客户列表
            int total1 = CommonUtil.getIntFieldByData(getCustomerList(10), "total");
            CommonUtil.valueView(total, total1);
            Preconditions.checkArgument(total1 == total + 1, "前台分配新客，创建时手机号不存在，全部客户未+1");
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            //删除客户
            deleteCustomer(phone);
            saveData("前台分配新客，创建时手机号不存在，全部客户+1");
        }
    }

    @Test(enabled = false)
    public void test() {
        Long customerId = 13997L;
        String phone = "13333333332";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        try {
            crm.login(EnumAccount.XSGWTEMP.getUsername(), EnumAccount.XSGWTEMP.getPassword());
            crm.finishReception(customerId, 6, "【自动化】王先生", phone, remark);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        }
    }

    /**
     * 获取顾客id
     * 前台分配销售，顾问接待后
     *
     * @return customerId
     */
    private Long getCustomerId() throws Exception {
        //前台登陆
        crm.login(EnumAccount.QT.getUsername(), EnumAccount.QT.getPassword());
        Long customerId;
        //获取当前空闲第一位销售id
        String saleId = CommonUtil.getStrFieldByData(crm.freeSaleList(), 0, "sale_id");
        //获取销售账号名
        String userLoginName = "";
        JSONArray userList = crm.userPage(1, 100).getJSONArray("list");
        for (int i = 0; i < userList.size(); i++) {
            JSONObject obj = userList.getJSONObject(i);
            if (obj.getString("user_id").equals(saleId)) {
                userLoginName = obj.getString("user_login_name");
            }
        }
        //创建接待
        crm.saleReception(EnumReceptionType.FIRST_VISIT.getType());
        //销售登陆，获取当前接待id
        crm.login(userLoginName, EnumAccount.QT.getPassword());
        customerId = crm.userInfService().getLong("customer_id");
        return customerId;
    }

    /**
     * 删除客户
     *
     * @param phone 电话号
     */
    private void deleteCustomer(String phone) {
        crm.login(EnumAccount.XSZJ.getUsername(), EnumAccount.XSZJ.getPassword());
        JSONObject response = crm.customerList("", phone, "", "", "", 1, 10);
        int customerId = CommonUtil.getIntFieldByData(response, 0, "customer_id");
        crm.customerDelete(customerId);
    }

    /**
     * 获取客户列表
     *
     * @return 客户列表
     */
    private JSONObject getCustomerList(Integer size) {
        crm.login(EnumAccount.XSZJ.getUsername(), EnumAccount.XSZJ.getPassword());
        return crm.customerList("", "", "", "", "", 1, size);
    }
}