package com.haisheng.framework.testng.bigScreen.crm.wm.datastore;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.SaleInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.TPorscheTodayData;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.sql.Sql;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.DingPushUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
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

public class AOnline extends TestCaseCommon implements TestCaseStd {
    PublicMethodOnline method = new PublicMethodOnline();
    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    private static final EnumAccount zjl = EnumAccount.ZJL_ONLINE;
    private static final String shopId = EnumTestProduce.CRM_ONLINE.getShopId();

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
    public void everydayData() {
        try {
            TPorscheTodayData db = new TPorscheTodayData();
            List<SaleInfo> saleInfos = method.getSaleList("销售顾问");
            saleInfos.forEach(arr -> {
                CommonUtil.valueView(arr.getUserName());
                if (arr.getUserName().contains("总经理")) {
                    UserUtil.login(zjl);
                    JSONObject response = crm.receptionPage(1, 10, "", "");
                    db.setTodayReceptionNum(response.getInteger("today_reception_num"));
                    db.setTodayClueNum(response.getInteger("all_customer_num"));
                } else {
                    crm.login(arr.getAccount(), zjl.getPassword());
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
                db.setSaleName(arr.getUserName());
                db.setSaleId(arr.getUserId());
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
            DingPushUtil.sendText(e.toString());
        }
    }

    @Test(dependsOnMethods = "everydayData")
    public void dataCheck() {
        String date = DateTimeUtil.getFormat(new Date());
        Sql sql = Sql.instance().select().from(TPorscheTodayData.class)
                .where("today_date", "=", date)
                .and("shop_id", "=", shopId)
                .end();
        int count = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
        if (!(count > 0)) {
            DingPushUtil.sendText(CommonUtil.humpToLine(TPorscheTodayData.class.getSimpleName()) + "表记录数据失败");
        }
    }
}
