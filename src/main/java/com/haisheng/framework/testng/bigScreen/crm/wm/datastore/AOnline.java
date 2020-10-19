package com.haisheng.framework.testng.bigScreen.crm.wm.datastore;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumShopId;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.sql.Sql;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.crmOnline.commonDsOnline.PublicMethodOnline;
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
import java.util.List;
import java.util.Map;

public class AOnline extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    private static final EnumAccount zjl = EnumAccount.ZJL_ONLINE;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
//        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
//        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
//        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
//        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAY_DATA_STORE.getJobName());
//        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_ONLINE.getName());
        //替换钉钉推送
//        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_SHOP_ONLINE.getShopId();
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
        CommonUtil.login(zjl);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test
    public void testA() {
        List<Map<String, String>> list = new PublicMethodOnline().getSaleList("销售顾问");
        list.forEach(arr -> {
            CommonUtil.valueView(arr.get("userName"));
            String shop_id = EnumShopId.PORSCHE_SHOP_ONLINE.getShopId();
            String date = DateTimeUtil.getFormat(new Date());
            //今日预约
            int today_appointment_number;
            //今日新客接待
            int today_new_customer;
            //今日老客接待
            int total_old_customer;
            //今日订单
            int today_order;
            //今日交车
            int today_deliver_car_total;
            //今日试驾
            int today_test_drive_total;
            //今日接待
            int today_reception_num = 0;
            //今日线索
            int all_customer_num = 0;
            String sale_id = arr.get("userId");
            if (arr.get("userName").equals("总经理")) {
                CommonUtil.login(zjl);
                JSONObject responseA = crm.customerReceptionTotalInfo();
                JSONObject responseB = crm.deliverCarTotal();
                JSONObject responseC = crm.driverTotal();
                JSONObject responseD = crm.appointmentTestDriverNumber();
                today_appointment_number = responseD.getInteger("appointment_today_number");
                today_new_customer = responseA.getInteger("today_new_customer");
                total_old_customer = responseA.getInteger("total_old_customer");
                today_order = responseA.getInteger("today_order");
                today_deliver_car_total = responseB.getInteger("today_deliver_car_total");
                today_test_drive_total = responseC.getInteger("today_test_drive_total");
                JSONObject response = crm.receptionPage(1, 10, "", "");
                today_reception_num = response.getInteger("today_reception_num");
                all_customer_num = response.getInteger("all_customer_num");
            } else {
                crm.login(arr.get("account"), zjl.getPassword());
                JSONObject response1 = crm.customerReceptionTotalInfo();
                JSONObject response2 = crm.deliverCarTotal();
                JSONObject response3 = crm.driverTotal();
                JSONObject response4 = crm.appointmentTestDriverNumber();
                today_new_customer = response1.getInteger("today_new_customer");
                total_old_customer = response1.getInteger("total_old_customer");
                today_order = response1.getInteger("today_order");
                today_deliver_car_total = response2.getInteger("today_deliver_car_total");
                today_test_drive_total = response3.getInteger("today_test_drive_total");
                today_appointment_number = response4.getInteger("appointment_today_number");
            }
            String sql = Sql.instance().insert()
                    .from("t_porsche_today_data")
                    .field("today_test_driver_num", "today_order_num", "today_deal_num", "today_clue_num", "today_reception_num", "today_appointment_num", "today_date", "shop_id", "sale_name", "today_new_customer_reception_num", "today_old_customer_reception_num", "sale_id")
                    .value(today_test_drive_total, today_order, today_deliver_car_total, all_customer_num, today_reception_num, today_appointment_number, date, shop_id, arr.get("userName"), today_new_customer, total_old_customer, sale_id)
                    .end().getSql();
            new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
        });
    }
}
