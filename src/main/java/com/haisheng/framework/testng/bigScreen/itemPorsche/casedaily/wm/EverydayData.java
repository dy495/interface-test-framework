package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.util.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemPorsche.util.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.sql.Sql;
import com.haisheng.framework.testng.bigScreen.itemPorsche.bean.SaleInfo;
import com.haisheng.framework.testng.bigScreen.itemPorsche.bean.TPorscheTodayData;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemPorsche.util.DingPushUtil;
import com.haisheng.framework.testng.bigScreen.itemPorsche.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.enumerator.EnumContainer;
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

public class EverydayData extends TestCaseCommon implements TestCaseStd {
    PublicMethod method = new PublicMethod();
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    private static final EnumAccount zjl = EnumAccount.ZJL_DAILY;
    private static final String shopId = EnumTestProduce.PORSCHE_DAILY.getShopId();

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

    @Test()
    public void everydayData() {
        try {
            TPorscheTodayData db = new TPorscheTodayData();
            List<SaleInfo> saleInfos = method.getSaleList("销售顾问");
            saleInfos.forEach(arr -> {
                CommonUtil.valueView(arr.getUserName());
                if (arr.getUserName().contains("总经理")) {
                    crm.login(arr.getAccount(), zjl.getPassword());
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
                Sql sql = Sql.instance().insert(TPorscheTodayData.class)
                        .set("today_test_driver_num", db.getTodayTestDriverNum())
                        .set("today_order_num", db.getTodayOrderNum())
                        .set("today_deal_num", db.getTodayDealNum())
                        .set("today_clue_num", db.getTodayClueNum())
                        .set("today_reception_num", db.getTodayReceptionNum())
                        .set("today_appointment_num", db.getTodayAppointmentNum())
                        .set("today_date", db.getTodayDate())
                        .set("shop_id", db.getShopId())
                        .set("sale_name", db.getSaleName())
                        .set("today_new_customer_reception_num", db.getTodayNewCustomerReceptionNum())
                        .set("today_old_customer_reception_num", db.getTodayOldCustomerReceptionNum())
                        .set("sale_id", db.getSaleId())
                        .end();
                new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql.getSql());
            });
        } catch (Exception e) {
            e.printStackTrace();
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
        int count = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql).length;
        if (!(count > 0)) {
            DingPushUtil.sendText(CommonUtil.humpToLineReplaceFirst(TPorscheTodayData.class.getSimpleName()) + "表记录数据失败");
        }
    }
}
