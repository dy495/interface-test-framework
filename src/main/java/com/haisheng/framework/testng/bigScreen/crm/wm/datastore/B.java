package com.haisheng.framework.testng.bigScreen.crm.wm.datastore;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumShopId;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCarStyle;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.pojo.TPorscheDeliverInfoDO;
import com.haisheng.framework.testng.bigScreen.crm.wm.pojo.TPorscheOrderInfoDO;
import com.haisheng.framework.testng.bigScreen.crm.wm.pojo.TPorscheReceptionDataDO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class B extends TestCaseCommon implements TestCaseStd {
    private static final Map<String, String> map = new HashMap<>();
    private static final EnumAccount zjl = EnumAccount.ZJL_DAILY;
    private static final String shopId = EnumShopId.PORSCHE_SHOP.getShopId();
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
            TPorscheReceptionDataDO po = new TPorscheReceptionDataDO();
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            IScene scene = CustomerMyReceptionListScene.builder().page(1).size(10).searchDateStart(date).searchDateEnd(date).build();
            int total = crm.invokeApi(scene).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                IScene scene1 = CustomerMyReceptionListScene.builder().page(i).size(100).searchDateStart(date).searchDateEnd(date).build();
                JSONArray list = crm.invokeApi(scene1).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    po.setShopId(shopId);
                    po.setReceptionSale(list.getJSONObject(j).getString("sale_name"));
                    po.setReceptionSaleId(getSaleId(list.getJSONObject(j).getString("sale_name")));
                    po.setReceptionStartTime(list.getJSONObject(j).getString("reception_time_str"));
                    po.setReceptionEndTime(list.getJSONObject(j).getString("leave_time_str"));
                    String start = po.getReceptionStartTime() == null ? "00:00" : po.getReceptionStartTime();
                    String end = po.getReceptionEndTime() == null ? "00:00" : po.getReceptionEndTime();
                    po.setReceptionDuration(new DateTimeUtil().calTimeHourDiff(start, end));
                    po.setCustomerId(list.getJSONObject(j).getInteger("customer_id"));
                    po.setCustomerName(list.getJSONObject(j).getString("customer_name"));
                    po.setCustomerTypeName(list.getJSONObject(j).getString("customer_type_name"));
                    po.setCustomerPhone(list.getJSONObject(j).getString("customer_phone"));
                    po.setReceptionDate(list.getJSONObject(j).getString("day_date"));
                    String sql = Sql.instance().insert()
                            .from("t_porsche_reception_data")
                            .field("shop_id", "reception_sale_id", "reception_sale", "reception_start_time", "reception_end_time", "reception_duration", "customer_id", "customer_name", "customer_type_name", "customer_phone", "reception_date")
                            .value(po.getShopId(), po.getReceptionSaleId(), po.getReceptionSale(),
                                    po.getReceptionStartTime(), po.getReceptionEndTime(),
                                    po.getReceptionDuration(), po.getCustomerId(), po.getCustomerName(),
                                    po.getCustomerTypeName(), po.getCustomerPhone(), po.getReceptionDate())
                            .end().getSql();
                    new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendText(e.toString());
        } finally {
            DingPushUtil.sendTxt(map);
        }
    }

    @Test(description = "每日交车记录")
    public void deliverCarData() {
        try {
            TPorscheDeliverInfoDO DO = new TPorscheDeliverInfoDO();
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            IScene scene = OrderInfoPageScene.builder().build();
            int total = crm.invokeApi(scene).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                IScene scene1 = OrderInfoPageScene.builder().page(i).size(100).build();
                JSONArray list = crm.invokeApi(scene1).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("deliver_date") != null
                            && list.getJSONObject(j).getString("deliver_date").equals(date)) {
                        DO.setShopId(shopId);
                        DO.setCustomerName(list.getJSONObject(j).getString("customer_name"));
                        DO.setIdNumber(list.getJSONObject(j).getString("id_number"));
                        DO.setBirthday(list.getJSONObject(j).getString("birthday"));
                        DO.setAddress(list.getJSONObject(j).getString("district_name"));
                        DO.setGender(list.getJSONObject(j).getString("gender"));
                        DO.setAge(list.getJSONObject(j).getString("age"));
                        JSONArray phones = list.getJSONObject(j).getJSONArray("phones");
                        if (phones.size() > 0) {
                            DO.setPhones(phones.getString(0));
                        }
                        DO.setSubjectTypeName(list.getJSONObject(j).getString("subject_type_name"));
                        DO.setSaleName(list.getJSONObject(j).getString("belongs_sale_name"));
                        DO.setSaleId(getSaleId(DO.getSaleName()));
                        DO.setCarStyle(getCarStyleId(list.getJSONObject(j).getString("car_style_name")));
                        DO.setCarModel(list.getJSONObject(j).getString("car_model_name"));
                        DO.setDeliverDate(list.getJSONObject(j).getString("deliver_date"));
                        DO.setPlateTypeName(list.getJSONObject(j).getString("plate_type_name"));
                        DO.setDefrayTypeName(list.getJSONObject(j).getString("defray_type_name"));
                        DO.setSourceChannelName(list.getJSONObject(j).getString("source_channel_name"));
                        DO.setPayTypeName(list.getJSONObject(j).getString("pay_type_name"));
                        DO.setPlateNumber(list.getJSONObject(j).getString("plate_number"));
                        DO.setVehicleChassisCode(list.getJSONObject(j).getString("vehicle_chassis_code"));
                        String sql = Sql.instance().insert()
                                .from("t_porsche_deliver_info")
                                .field("shop_id", "customer_id", "customer_name", "id_number", "birthday", "address", "gender", "age", "phones",
                                        "subject_type_name", "sale_name", "sale_id", "car_style", "car_model", "deliver_date", "plate_type_name",
                                        "defray_type_name", "source_channel_name", "pay_type_name", "plate_number", "vehicle_chassis_code")
                                .value(DO.getShopId(), DO.getCustomerId(), DO.getCustomerName(), DO.getIdNumber(),
                                        DO.getBirthday(), DO.getAddress(), DO.getGender(), DO.getAge(), DO.getPhones(),
                                        DO.getSubjectTypeName(), DO.getSaleName(), DO.getSaleId(), DO.getCarStyle(),
                                        DO.getCarModel(), DO.getDeliverDate(), DO.getPlateTypeName(), DO.getDefrayTypeName(),
                                        DO.getSourceChannelName(), DO.getPayTypeName(), DO.getPlateNumber(), DO.getVehicleChassisCode())
                                .end().getSql();
                        new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendText(e.toString());
        } finally {
            DingPushUtil.sendTxt(map);
        }

    }

    @Test(description = "每日订车记录")
    public void orderCarData() {
        try {
            TPorscheOrderInfoDO orderDO = new TPorscheOrderInfoDO();
            String date = DateTimeUtil.addDayFormat(new Date(), -1);
            IScene scene = OrderInfoPageScene.builder().build();
            int total = crm.invokeApi(scene).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                IScene scene1 = OrderInfoPageScene.builder().page(i).size(100).build();
                JSONArray list = crm.invokeApi(scene1).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("order_date") != null
                            && list.getJSONObject(j).getString("order_date").equals(date)) {
                        orderDO.setShopId(shopId);
                        orderDO.setCustomerName(list.getJSONObject(j).getString("customer_name"));
                        orderDO.setIdNumber(list.getJSONObject(j).getString("id_number"));
                        orderDO.setBirthday(list.getJSONObject(j).getString("birthday"));
                        orderDO.setAddress(list.getJSONObject(j).getString("district_name"));
                        orderDO.setGender(list.getJSONObject(j).getString("gender"));
                        orderDO.setAge(list.getJSONObject(j).getString("age"));
                        JSONArray phones = list.getJSONObject(j).getJSONArray("phones");
                        if (phones.size() > 0) {
                            orderDO.setPhones(phones.getString(0));
                        }
                        orderDO.setSubjectTypeName(list.getJSONObject(j).getString("subject_type_name"));
                        orderDO.setSaleName(list.getJSONObject(j).getString("belongs_sale_name"));
                        orderDO.setSaleId(getSaleId(orderDO.getSaleName()));
                        orderDO.setCarStyle(getCarStyleId(list.getJSONObject(j).getString("car_style_name")));
                        orderDO.setCarModel(list.getJSONObject(j).getString("car_model_name"));
                        orderDO.setOrderDate(list.getJSONObject(j).getString("order_date"));
                        orderDO.setPlateTypeName(list.getJSONObject(j).getString("plate_type_name"));
                        orderDO.setDefrayTypeName(list.getJSONObject(j).getString("defray_type_name"));
                        orderDO.setSourceChannelName(list.getJSONObject(j).getString("source_channel_name"));
                        orderDO.setPayTypeName(list.getJSONObject(j).getString("pay_type_name"));
                        orderDO.setPlateNumber(list.getJSONObject(j).getString("plate_number"));
                        orderDO.setVehicleChassisCode(list.getJSONObject(j).getString("vehicle_chassis_code"));
                        String sql = Sql.instance().insert()
                                .from("t_porsche_order_info")
                                .field("shop_id", "customer_id", "customer_name", "id_number", "birthday", "address", "gender", "age", "phones",
                                        "subject_type_name", "sale_name", "sale_id", "car_style", "car_model", "order_date", "plate_type_name",
                                        "defray_type_name", "source_channel_name", "pay_type_name", "plate_number", "vehicle_chassis_code")
                                .value(orderDO.getShopId(), orderDO.getCustomerId(), orderDO.getCustomerName(), orderDO.getIdNumber(),
                                        orderDO.getBirthday(), orderDO.getAddress(), orderDO.getGender(), orderDO.getAge(), orderDO.getPhones(),
                                        orderDO.getSubjectTypeName(), orderDO.getSaleName(), orderDO.getSaleId(), orderDO.getCarStyle(),
                                        orderDO.getCarModel(), orderDO.getOrderDate(), orderDO.getPlateTypeName(), orderDO.getDefrayTypeName(),
                                        orderDO.getSourceChannelName(), orderDO.getPayTypeName(), orderDO.getPlateNumber(), orderDO.getVehicleChassisCode())
                                .end().getSql();
                        new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendText(e.toString());
        } finally {
            DingPushUtil.sendTxt(map);
        }
    }

    private void sendText(final String e) {
        map.put("title", "balabala");
        map.put("text", "### " + "**" + "Ding，有空看一下" + "**" + "\n"
                + "\n" + DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss") + "\n"
                + "\n" + "错误：" + "\n");
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
        return "不知道什么车系";
    }

    private long getBatchId(long customerId) {
        String sql = Sql.instance().select()
                .from("pre_sales_reception")
                .where("customer_id", "=", customerId)
                .end().getSql();
        List<Map<String, Object>> list = new Factory.Builder().container(EnumContainer.BUSINESS_PORSCHE.getContainer()).build().create(sql);
        return (long) list.get(0).get("batch_id");
    }
}
