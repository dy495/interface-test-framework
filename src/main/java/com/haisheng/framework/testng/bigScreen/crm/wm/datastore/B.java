package com.haisheng.framework.testng.bigScreen.crm.wm.datastore;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumShopId;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCarStyle;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.TPorscheDeliverInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.TPorscheOrderInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.TPorscheReceptionData;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerMyReceptionListScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc.OrderInfoPageScene;
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

public class B extends TestCaseCommon implements TestCaseStd {
    private static final EnumAccount zjl = EnumAccount.ZJL_DAILY;
    private static final String shopId = EnumShopId.PORSCHE_SHOP.getShopId();
    private static final int day = -1;
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    PublicMethod method = new PublicMethod();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //放入shopId
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

    @Test(description = "每日接待记录")
    public void receptionData() {
        try {
            TPorscheReceptionData db = new TPorscheReceptionData();
            String date = DateTimeUtil.addDayFormat(new Date(), day);
            IScene scene = CustomerMyReceptionListScene.builder().page(1).size(10).searchDateStart(date).searchDateEnd(date).build();
            int total = crm.invokeApi(scene).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                IScene scene1 = CustomerMyReceptionListScene.builder().page(i).size(100).searchDateStart(date).searchDateEnd(date).build();
                JSONArray list = crm.invokeApi(scene1).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    db.setShopId(shopId);
                    db.setReceptionSale(list.getJSONObject(j).getString("sale_name"));
                    db.setReceptionSaleId(getSaleId(list.getJSONObject(j).getString("sale_name")));
                    db.setReceptionStartTime(list.getJSONObject(j).getString("reception_time_str"));
                    db.setReceptionEndTime(list.getJSONObject(j).getString("leave_time_str"));
                    String start = db.getReceptionStartTime() == null ? "00:00" : db.getReceptionStartTime();
                    String end = db.getReceptionEndTime() == null ? "00:00" : db.getReceptionEndTime();
                    db.setReceptionDuration(new DateTimeUtil().calTimeHourDiff(start, end));
                    db.setCustomerId(list.getJSONObject(j).getInteger("customer_id"));
                    db.setCustomerName(list.getJSONObject(j).getString("customer_name"));
                    db.setCustomerTypeName(list.getJSONObject(j).getString("customer_type_name"));
                    db.setCustomerPhone(list.getJSONObject(j).getString("customer_phone"));
                    db.setReceptionDate(list.getJSONObject(j).getString("day_date"));
                    String sql = Sql.instance().insert()
                            .from("t_porsche_reception_data")
                            .field("shop_id", "reception_sale_id", "reception_sale", "reception_start_time",
                                    "reception_end_time", "reception_duration", "customer_id", "customer_name",
                                    "customer_type_name", "customer_phone", "reception_date")
                            .setValue(db.getShopId(), db.getReceptionSaleId(), db.getReceptionSale(),
                                    db.getReceptionStartTime(), db.getReceptionEndTime(),
                                    db.getReceptionDuration(), db.getCustomerId(), db.getCustomerName(),
                                    db.getCustomerTypeName(), db.getCustomerPhone(), db.getReceptionDate())
                            .end().getSql();
                    new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DingPushUtil.sendText(e.toString());
        }
    }

    @Test(description = "每日交车记录")
    public void deliverCarData() {
        try {
            TPorscheDeliverInfo db = new TPorscheDeliverInfo();
            String date = DateTimeUtil.addDayFormat(new Date(), day);
            IScene scene = OrderInfoPageScene.builder().build();
            int total = crm.invokeApi(scene).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                IScene scene1 = OrderInfoPageScene.builder().page(i).size(100).build();
                JSONArray list = crm.invokeApi(scene1).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("deliver_date") != null
                            && list.getJSONObject(j).getString("deliver_date").equals(date)) {
                        db.setShopId(shopId);
                        db.setCustomerName(list.getJSONObject(j).getString("customer_name"));
                        db.setIdNumber(list.getJSONObject(j).getString("id_number"));
                        db.setBirthday(list.getJSONObject(j).getString("birthday"));
                        db.setAddress(list.getJSONObject(j).getString("district_name"));
                        db.setGender(list.getJSONObject(j).getString("gender"));
                        db.setAge(list.getJSONObject(j).getString("age"));
                        JSONArray phones = list.getJSONObject(j).getJSONArray("phones");
                        if (phones.size() > 0) {
                            db.setPhones(phones.getString(0));
                        }
                        db.setSubjectTypeName(list.getJSONObject(j).getString("subject_type_name"));
                        db.setSaleName(list.getJSONObject(j).getString("belongs_sale_name"));
                        db.setSaleId(getSaleId(db.getSaleName()));
                        db.setCarStyle(getCarStyleId(list.getJSONObject(j).getString("car_style_name")));
                        db.setCarModel(list.getJSONObject(j).getString("car_model_name"));
                        db.setDeliverDate(list.getJSONObject(j).getString("deliver_date"));
                        db.setPlateTypeName(list.getJSONObject(j).getString("plate_type_name"));
                        db.setDefrayTypeName(list.getJSONObject(j).getString("defray_type_name"));
                        db.setSourceChannelName(list.getJSONObject(j).getString("source_channel_name"));
                        db.setPayTypeName(list.getJSONObject(j).getString("pay_type_name"));
                        db.setPlateNumber(list.getJSONObject(j).getString("plate_number"));
                        db.setVehicleChassisCode(list.getJSONObject(j).getString("vehicle_chassis_code"));
                        String sql = Sql.instance().insert()
                                .from(TPorscheDeliverInfo.class)
                                .field("shop_id", "customer_id", "customer_name", "id_number", "birthday", "address",
                                        "gender", "age", "phones", "subject_type_name", "sale_name", "sale_id",
                                        "car_style", "car_model", "deliver_date", "plate_type_name", "defray_type_name",
                                        "source_channel_name", "pay_type_name", "plate_number", "vehicle_chassis_code")
                                .setValue(db.getShopId(), db.getCustomerId(), db.getCustomerName(), db.getIdNumber(),
                                        db.getBirthday(), db.getAddress(), db.getGender(), db.getAge(), db.getPhones(),
                                        db.getSubjectTypeName(), db.getSaleName(), db.getSaleId(), db.getCarStyle(),
                                        db.getCarModel(), db.getDeliverDate(), db.getPlateTypeName(), db.getDefrayTypeName(),
                                        db.getSourceChannelName(), db.getPayTypeName(), db.getPlateNumber(), db.getVehicleChassisCode())
                                .end().getSql();
                        new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DingPushUtil.sendText(e.toString());
        }
    }

    @Test(description = "每日订车记录")
    public void orderCarData() {
        try {
            TPorscheOrderInfo db = new TPorscheOrderInfo();
            String date = DateTimeUtil.addDayFormat(new Date(), day);
            IScene scene = OrderInfoPageScene.builder().build();
            int total = crm.invokeApi(scene).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                IScene scene1 = OrderInfoPageScene.builder().page(i).size(100).build();
                JSONArray list = crm.invokeApi(scene1).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("order_date") != null
                            && list.getJSONObject(j).getString("order_date").equals(date)) {
                        db.setShopId(shopId);
                        db.setCustomerName(list.getJSONObject(j).getString("customer_name"));
                        db.setIdNumber(list.getJSONObject(j).getString("id_number"));
                        db.setBirthday(list.getJSONObject(j).getString("birthday"));
                        db.setAddress(list.getJSONObject(j).getString("district_name"));
                        db.setGender(list.getJSONObject(j).getString("gender"));
                        db.setAge(list.getJSONObject(j).getString("age"));
                        JSONArray phones = list.getJSONObject(j).getJSONArray("phones");
                        if (phones.size() > 0) {
                            db.setPhones(phones.getString(0));
                        }
                        db.setSubjectTypeName(list.getJSONObject(j).getString("subject_type_name"));
                        db.setSaleName(list.getJSONObject(j).getString("belongs_sale_name"));
                        db.setSaleId(getSaleId(db.getSaleName()));
                        db.setCarStyle(getCarStyleId(list.getJSONObject(j).getString("car_style_name")));
                        db.setCarModel(list.getJSONObject(j).getString("car_model_name"));
                        db.setOrderDate(list.getJSONObject(j).getString("order_date"));
                        db.setPlateTypeName(list.getJSONObject(j).getString("plate_type_name"));
                        db.setDefrayTypeName(list.getJSONObject(j).getString("defray_type_name"));
                        db.setSourceChannelName(list.getJSONObject(j).getString("source_channel_name"));
                        db.setPayTypeName(list.getJSONObject(j).getString("pay_type_name"));
                        db.setPlateNumber(list.getJSONObject(j).getString("plate_number"));
                        db.setVehicleChassisCode(list.getJSONObject(j).getString("vehicle_chassis_code"));
                        String sql = Sql.instance().insert()
                                .from(TPorscheOrderInfo.class)
                                .field("shop_id", "customer_id", "customer_name", "id_number", "birthday", "address",
                                        "gender", "age", "phones", "subject_type_name", "sale_name", "sale_id",
                                        "car_style", "car_model", "order_date", "plate_type_name", "defray_type_name",
                                        "source_channel_name", "pay_type_name", "plate_number", "vehicle_chassis_code")
                                .setValue(db.getShopId(), db.getCustomerId(), db.getCustomerName(), db.getIdNumber(),
                                        db.getBirthday(), db.getAddress(), db.getGender(), db.getAge(), db.getPhones(),
                                        db.getSubjectTypeName(), db.getSaleName(), db.getSaleId(), db.getCarStyle(),
                                        db.getCarModel(), db.getOrderDate(), db.getPlateTypeName(), db.getDefrayTypeName(),
                                        db.getSourceChannelName(), db.getPayTypeName(), db.getPlateNumber(), db.getVehicleChassisCode())
                                .end().getSql();
                        new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DingPushUtil.sendText(e.toString());
        }
    }


    /**
     * 获取销售id
     *
     * @param saleName 销售名字
     * @return saleId
     */
    private String getSaleId(String saleName) {
        List<Map<String, String>> list = method.getSaleList("销售顾问");
        for (Map<String, String> stringStringMap : list) {
            if (stringStringMap.get("userName").equals(saleName)) {
                return stringStringMap.get("userId");
            }
        }
        return null;
    }

    /**
     * 获取车系id
     *
     * @param carStyleName 车系名称
     * @return 车系id
     */
    private String getCarStyleId(String carStyleName) {
        for (EnumCarStyle s : EnumCarStyle.values()) {
            if (s.getName().equals(carStyleName)) {
                return s.getStyleId();
            }
        }
        return null;
    }
}
