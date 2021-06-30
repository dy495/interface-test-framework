package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.util.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemPorsche.util.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.sql.Sql;
import com.haisheng.framework.testng.bigScreen.itemPorsche.bean.SaleInfo;
import com.haisheng.framework.testng.bigScreen.itemPorsche.bean.TPorscheDeliverInfo;
import com.haisheng.framework.testng.bigScreen.itemPorsche.bean.TPorscheOrderInfo;
import com.haisheng.framework.testng.bigScreen.itemPorsche.bean.TPorscheReceptionData;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.customer.EnumCarStyle;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemPorsche.scene.app.CustomerMyReceptionListScene;
import com.haisheng.framework.testng.bigScreen.itemPorsche.scene.pc.OrderInfoPageScene;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessData extends TestCaseCommon implements TestCaseStd {
    private static final EnumAccount zjl = EnumAccount.ZJL_DAILY;
    private static final String shopId = EnumTestProduce.PORSCHE_DAILY.getShopId();
    private static final int day = -1;
    private static final int size = 100;
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
            String date = DateTimeUtil.addDayFormat(new Date(), day);
            IScene scene = CustomerMyReceptionListScene.builder().searchDateStart(date).searchDateEnd(date).build();
            List<TPorscheReceptionData> receptionData = collectBean(scene, TPorscheReceptionData.class);
            for (TPorscheReceptionData e : receptionData) {
                e.setReceptionSaleId(getSaleId(e.getReceptionSale()));
                e.setShopId(shopId);
                e.setReceptionDuration(getReceptionDuration(e));
                Sql sql = Sql.instance()
                        .insert(TPorscheReceptionData.class)
                        .set("shop_id", e.getShopId())
                        .set("reception_sale_id", e.getReceptionSaleId())
                        .set("reception_sale", e.getReceptionSale())
                        .set("reception_start_time", e.getReceptionStartTime())
                        .set("reception_end_time", e.getReceptionEndTime())
                        .set("reception_duration", e.getReceptionDuration())
                        .set("customer_id", e.getCustomerId())
                        .set("customer_name", e.getCustomerName())
                        .set("customer_type_name", e.getCustomerTypeName())
                        .set("customer_phone", e.getCustomerPhone())
                        .set("reception_date", e.getReceptionDate())
                        .end();
                new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql.getSql());
            }
        } catch (Exception e) {
            e.printStackTrace();
            DingPushUtil.sendText(e.toString());
        }
    }

    @Test(description = "每日交车记录")
    public void deliverCarData() {
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), day);
            IScene scene = OrderInfoPageScene.builder().build();
            List<TPorscheDeliverInfo> deliverInfos = collectBean(scene, TPorscheDeliverInfo.class);
            deliverInfos.forEach(e -> {
                if (e.getDeliverDate() != null && e.getDeliverDate().equals(date)) {
                    String phone = e.getPhones().size() > 0 ? e.getPhones().getString(0) : null;
                    e.setPhone(phone);
                    e.setCarStyle(getCarStyleId(e.getCarStyle()));
                    e.setSaleId(getSaleId(e.getSaleName()));
                    e.setShopId(shopId);
                    Sql sql = Sql.instance().insert(TPorscheReceptionData.class)
                            .set("shop_id", e.getShopId())
                            .set("customer_id", e.getCustomerId())
                            .set("customer_name", e.getCustomerName())
                            .set("id_number", e.getIdNumber())
                            .set("birthday", e.getBirthday())
                            .set("address", e.getAddress())
                            .set("gender", e.getGender())
                            .set("age", e.getAge())
                            .set("phone", e.getPhone())
                            .set("subject_type_name", e.getSubjectTypeName())
                            .set("sale_name", e.getSaleName())
                            .set("sale_id", e.getSaleId())
                            .set("car_style", e.getCarStyle())
                            .set("car_model", e.getCarModel())
                            .set("deliver_date", e.getDeliverDate())
                            .set("plate_type_name", e.getPlateTypeName())
                            .set("defray_type_name", e.getDefrayTypeName())
                            .set("source_channel_name", e.getSourceChannelName())
                            .set("pay_type_name", e.getPayTypeName())
                            .set("plate_number", e.getPlateNumber())
                            .set("vehicle_chassis_code", e.getVehicleChassisCode()).end();
                    new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql.getSql());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            DingPushUtil.sendText(e.toString());
        }
    }

    @Test(description = "每日订车记录")
    public void orderCarData() {
        try {
            String date = DateTimeUtil.addDayFormat(new Date(), day);
            IScene scene = OrderInfoPageScene.builder().build();
            List<TPorscheOrderInfo> orderInfos = collectBean(scene, TPorscheOrderInfo.class);
            orderInfos.forEach(e -> {
                if (e.getOrderDate() != null && e.getOrderDate().equals(date)) {
                    String phone = e.getPhones().size() > 0 ? e.getPhones().getString(0) : null;
                    e.setPhone(phone);
                    e.setSaleId(getSaleId(e.getSaleName()));
                    e.setCarStyle(getCarStyleId(e.getCarStyle()));
                    e.setShopId(shopId);
                    Sql sql = Sql.instance().insert(TPorscheOrderInfo.class)
                            .set("shop_id", e.getShopId())
                            .set("customer_id", e.getCustomerId())
                            .set("customer_name", e.getCustomerName())
                            .set("id_number", e.getIdNumber())
                            .set("birthday", e.getBirthday())
                            .set("address", e.getAddress())
                            .set("gender", e.getGender())
                            .set("age", e.getAge())
                            .set("phone", e.getPhone())
                            .set("subject_type_name", e.getSubjectTypeName())
                            .set("sale_name", e.getSaleName())
                            .set("sale_id", e.getSaleId())
                            .set("car_style", e.getCarStyle())
                            .set("car_model", e.getCarModel())
                            .set("order_date", e.getOrderDate())
                            .set("plate_type_name", e.getPlateTypeName())
                            .set("defray_type_name", e.getDefrayTypeName())
                            .set("source_channel_name", e.getSourceChannelName())
                            .set("pay_type_name", e.getPayTypeName())
                            .set("plate_number", e.getPlateNumber())
                            .set("vehicle_chassis_code", e.getVehicleChassisCode()).end();
                    new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql.getSql());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            DingPushUtil.sendText(e.toString());
        }
    }

    @Test(priority = 1)
    public void dataCheck() {
        String date = DateTimeUtil.addDayFormat(new Date(), day);
        Sql sql = Sql.instance().select().from(TPorscheDeliverInfo.class)
                .where("deliver_date", "=", date)
                .and("shop_id", "=", shopId)
                .end();
        int count = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql.getSql()).length;
        if (count <= 0) {
            DingPushUtil.sendText(CommonUtil.humpToLineReplaceFirst(TPorscheDeliverInfo.class.getSimpleName()) + "表记录数据失败");
        }
        Sql sql1 = Sql.instance().select().from(TPorscheOrderInfo.class)
                .where("order_date", "=", date)
                .and("shop_id", "=", shopId)
                .end();
        int count1 = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql1.getSql()).length;
        if (count1 <= 0) {
            DingPushUtil.sendText(CommonUtil.humpToLineReplaceFirst(TPorscheOrderInfo.class.getSimpleName()) + "表记录数据失败");
        }
        Sql sql2 = Sql.instance().select().from(TPorscheReceptionData.class)
                .where("reception_date", "=", date)
                .and("shop_id", "=", shopId)
                .end();
        int count2 = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql2.getSql()).length;
        if (count2 <= 0) {
            DingPushUtil.sendText(CommonUtil.humpToLineReplaceFirst(TPorscheReceptionData.class.getSimpleName()) + "表记录数据失败");
        }
    }

    /**
     * 计算接待时间
     *
     * @param receptionData 接待数据
     * @return 接待时长
     */
    private int getReceptionDuration(TPorscheReceptionData receptionData) throws ParseException {
        String start = receptionData.getReceptionStartTime() == null ? "00:00" : receptionData.getReceptionStartTime();
        String end = receptionData.getReceptionEndTime() == null ? "00:00" : receptionData.getReceptionEndTime();
        return new DateTimeUtil().calTimeHourDiff(start, end);
    }

    /**
     * 获取销售id
     *
     * @param saleName 销售名字
     * @return saleId
     */
    private String getSaleId(String saleName) {
        List<SaleInfo> saleInfos = method.getSaleList("销售顾问");
        return saleInfos.stream().filter(e -> e.getUserName().equals(saleName)).map(SaleInfo::getUserId).findFirst().orElse(null);
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

    /**
     * @param scene 接口场景
     * @param bean  bean类
     * @param <T>   T
     * @return bean的集合
     */
    public <T> List<T> collectBean(IScene scene, Class<T> bean) {
        List<T> list = new ArrayList<>();
        int total = crm.invokeApi(scene).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(size);
            JSONArray array = crm.invokeApi(scene).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, bean)).collect(Collectors.toList()));
        }
        return list;
    }
}
