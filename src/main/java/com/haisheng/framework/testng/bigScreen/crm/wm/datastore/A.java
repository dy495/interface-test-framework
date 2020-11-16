package com.haisheng.framework.testng.bigScreen.crm.wm.datastore;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumShopId;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.dao.TPorscheTodayData;
import com.haisheng.framework.testng.bigScreen.crm.wm.sql.Sql;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.DingPushUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
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

public class A extends TestCaseCommon implements TestCaseStd {
    PublicMethod method = new PublicMethod();
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    private static final EnumAccount zjl = EnumAccount.ZJL_DAILY;
    private static final String shopId = EnumShopId.PORSCHE_SHOP.getShopId();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.shopId = shopId;
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

    @Test
    public void everydayDat() {
        try {
            TPorscheTodayData db = new TPorscheTodayData();
            List<Map<String, String>> list = method.getSaleList("销售顾问");
            list.forEach(arr -> {
                CommonUtil.valueView(arr.get("userName"));
                if (arr.get("userName").contains("总经理")) {
                    UserUtil.login(zjl);
                    JSONObject response = crm.receptionPage(1, 10, "", "");
                    db.setTodayReceptionNum(response.getInteger("today_reception_num"));
                    db.setTodayClueNum(response.getInteger("all_customer_num"));
                } else {
                    crm.login(arr.get("account"), zjl.getPassword());
                }
                JSONObject responseA = crm.customerReceptionTotalInfo();
                JSONObject responseB = crm.deliverCarTotal();
                JSONObject responseC = crm.driverTotal();
                JSONObject responseD = crm.appointmentTestDriverNumber();
                db.setTodayAppointmentNum(responseD.getInteger("appointment_today_number"));
                db.setTodayNewCustomerReceptionNum(responseA.getInteger("today_new_customer"));
                db.setTodayOldCustomerReceptionNum(responseA.getInteger("total_old_customer"));
                db.setTodayOrderNum(responseA.getInteger("today_order"));
                db.setTodayDealNum(responseB.getInteger("today_deliver_car_total"));
                db.setTodayTestDriverNum(responseC.getInteger("today_test_drive_total"));
                db.setTodayDate(DateTimeUtil.getFormat(new Date()));
                db.setShopId(shopId);
                db.setSaleName(arr.get("userName"));
                db.setSaleId(arr.get("userId"));
                String sql = Sql.instance().insert()
                        .from(TPorscheTodayData.class)
                        .field("today_test_driver_num", "today_order_num", "today_deal_num", "today_clue_num",
                                "today_reception_num", "today_appointment_num", "today_date", "shop_id", "sale_name",
                                "today_new_customer_reception_num", "today_old_customer_reception_num", "sale_id")
                        .setValue(db.getTodayTestDriverNum(), db.getTodayOrderNum(), db.getTodayDealNum(), db.getTodayClueNum(),
                                db.getTodayReceptionNum(), db.getTodayAppointmentNum(), db.getTodayDate(), db.getShopId(),
                                db.getSaleName(), db.getTodayNewCustomerReceptionNum(), db.getTodayOldCustomerReceptionNum(), db.getSaleId())
                        .end().getSql();
                new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
            });
        } catch (Exception e) {
            e.printStackTrace();
            DingPushUtil.sendText(e.toString());
        }
    }
}
