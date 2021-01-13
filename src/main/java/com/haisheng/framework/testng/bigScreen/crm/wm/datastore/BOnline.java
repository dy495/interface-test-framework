package com.haisheng.framework.testng.bigScreen.crm.wm.datastore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.SaleInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.TPorscheDeliverInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.TPorscheOrderInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.TPorscheReceptionData;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCarStyle;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerMyReceptionListScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc.OrderInfoPageScene;
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
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BOnline extends TestCaseCommon implements TestCaseStd {
    private static final String shopId = EnumTestProduce.CRM_ONLINE.getShopId();
    private static final EnumAccount zjl = EnumAccount.ZJL_ONLINE;
    private static final int day = -1;
    private static final int size = 100;
    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    PublicMethodOnline method = new PublicMethodOnline();

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
                String sql = Sql.instance().insert().from(TPorscheReceptionData.class)
                        .field("shop_id", "reception_sale_id", "reception_sale", "reception_start_time", "reception_end_time", "reception_duration", "customer_id", "customer_name", "customer_type_name", "customer_phone", "reception_date")
                        .setValue(e.getShopId(), e.getReceptionSaleId(), e.getReceptionSale(), e.getReceptionStartTime(), e.getReceptionEndTime(), e.getReceptionDuration(), e.getCustomerId(), e.getCustomerName(), e.getCustomerTypeName(), e.getCustomerPhone(), e.getReceptionDate())
                        .end().getSql();
                new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
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
                    String sql = Sql.instance().insert().from(TPorscheDeliverInfo.class)
                            .field("shop_id", "customer_id", "customer_name", "id_number", "birthday", "address", "gender", "age", "phone", "subject_type_name", "sale_name", "sale_id", "car_style", "car_model", "deliver_date", "plate_type_name", "defray_type_name", "source_channel_name", "pay_type_name", "plate_number", "vehicle_chassis_code")
                            .setValue(e.getShopId(), e.getCustomerId(), e.getCustomerName(), e.getIdNumber(), e.getBirthday(), e.getAddress(), e.getGender(), e.getAge(), e.getPhone(), e.getSubjectTypeName(), e.getSaleName(), e.getSaleId(), e.getCarStyle(), e.getCarModel(), e.getDeliverDate(), e.getPlateTypeName(), e.getDefrayTypeName(), e.getSourceChannelName(), e.getPayTypeName(), e.getPlateNumber(), e.getVehicleChassisCode())
                            .end().getSql();
                    new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
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
                    String sql = Sql.instance().insert().from(TPorscheOrderInfo.class)
                            .field("shop_id", "customer_id", "customer_name", "id_number", "birthday", "address", "gender", "age", "phone", "subject_type_name", "sale_name", "sale_id", "car_style", "car_model", "order_date", "plate_type_name", "defray_type_name", "source_channel_name", "pay_type_name", "plate_number", "vehicle_chassis_code")
                            .setValue(e.getShopId(), e.getCustomerId(), e.getCustomerName(), e.getIdNumber(), e.getBirthday(), e.getAddress(), e.getGender(), e.getAge(), e.getPhone(), e.getSubjectTypeName(), e.getSaleName(), e.getSaleId(), e.getCarStyle(), e.getCarModel(), e.getOrderDate(), e.getPlateTypeName(), e.getDefrayTypeName(), e.getSourceChannelName(), e.getPayTypeName(), e.getPlateNumber(), e.getVehicleChassisCode())
                            .end().getSql();
                    new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            DingPushUtil.sendText(e.toString());
        }
    }

    @AfterClass
    @Test()
    public void dataCheck() {
        String date = DateTimeUtil.addDayFormat(new Date(), day);
        Sql sql = Sql.instance().select().from(TPorscheDeliverInfo.class)
                .where("deliver_date", "=", date)
                .and("shop_id", "=", shopId)
                .end();
        int count = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql).size();
        if (count <= 0) {
            DingPushUtil.sendText(CommonUtil.humpToLine(TPorscheDeliverInfo.class.getSimpleName()) + "表记录数据失败");
        }
        Sql sql1 = Sql.instance().select().from(TPorscheOrderInfo.class)
                .where("order_date", "=", date)
                .and("shop_id", "=", shopId)
                .end();
        int count1 = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql1).size();
        if (count1 <= 0) {
            DingPushUtil.sendText(CommonUtil.humpToLine(TPorscheOrderInfo.class.getSimpleName()) + "表记录数据失败");
        }
        Sql sql2 = Sql.instance().select().from(TPorscheReceptionData.class)
                .where("reception_date", "=", date)
                .and("shop_id", "=", shopId)
                .end();
        int count2 = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql2).size();
        if (count2 <= 0) {
            DingPushUtil.sendText(CommonUtil.humpToLine(TPorscheReceptionData.class.getSimpleName()) + "表记录数据失败");
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
