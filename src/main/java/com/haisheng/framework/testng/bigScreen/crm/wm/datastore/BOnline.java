package com.haisheng.framework.testng.bigScreen.crm.wm.datastore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumShopId;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.pojo.TPorscheDeliverCarDO;
import com.haisheng.framework.testng.bigScreen.crm.wm.pojo.TPorscheOrderCarDO;
import com.haisheng.framework.testng.bigScreen.crm.wm.pojo.TPorscheReceptionDataDO;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerBuyCarListScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerFaceLabelScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerInfoScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerMyReceptionListScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.sql.Sql;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.Address;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.crmOnline.commonDsOnline.PublicMethodOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BOnline extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    PublicMethodOnline method = new PublicMethodOnline();
    private static final EnumAccount zjl = EnumAccount.ZJL_ONLINE;
    String shopId = EnumShopId.PORSCHE_SHOP_ONLINE.getShopId();

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
        CommonUtil.login(zjl);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test(description = "每日接待记录")
    public void receptionData() throws ParseException {
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
                        .value(po.getShopId(), po.getReceptionSaleId(), po.getReceptionSale(), po.getReceptionStartTime(), po.getReceptionEndTime(),
                                po.getReceptionDuration(), po.getCustomerId(), po.getCustomerName(),
                                po.getCustomerTypeName(), po.getCustomerPhone(), po.getReceptionDate())
                        .end().getSql();
                new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
            }
        }
    }

    @Test(description = "每日交车记录")
    public void deliverCarData() {
        StringBuilder stringBuilder = new StringBuilder();
        TPorscheDeliverCarDO po = new TPorscheDeliverCarDO();
        String shopId = this.shopId;
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        int total = crm.invokeApi(CustomerMyReceptionListScene.builder().searchDateStart(date).searchDateEnd(date).build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, 100);
        for (int i = 1; i < s; i++) {
            IScene scene = CustomerMyReceptionListScene.builder().page(i).size(100).searchDateStart(date).searchDateEnd(date).build();
            JSONArray list = crm.invokeApi(scene).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                String id = list.getJSONObject(j).getString("customer_id");
                if (stringBuilder.toString().contains(id)) {
                    continue;
                }
                stringBuilder.append(id).append(",");
                IScene buyCarListScene = CustomerBuyCarListScene.builder().customerId(id).build();
                JSONArray buyCarList = crm.invokeApi(buyCarListScene).getJSONArray("list");
                for (int x = 0; x < buyCarList.size(); x++) {
                    if (buyCarList.getJSONObject(x).getString("deliver_time") != null
                            && buyCarList.getJSONObject(x).getString("deliver_time").equals(date)) {
                        po.setSaleName(list.getJSONObject(j).getString("sale_name"));
                        po.setSaleId(getSaleId(list.getJSONObject(j).getString("sale_name")));
                        po.setCarStyle(buyCarList.getJSONObject(x).getString("car_style_id"));
                        po.setCarModel(buyCarList.getJSONObject(x).getString("car_model_name"));
                        po.setDeliverTime(buyCarList.getJSONObject(x).getString("deliver_time"));
                        po.setCustomerId(Integer.parseInt(id));
                        po.setShopId(shopId);
                        JSONObject response = crm.invokeApi(CustomerInfoScene.builder().customerId(id).build());
                        po.setCustomerName(response.getString("name"));
                        po.setCustomerType(response.getString("subject_type"));
                        JSONArray phoneList = response.getJSONArray("phone_list");
                        if (phoneList == null) {
                            po.setCustomerPhone(null);
                        } else {
                            for (int u = 0; u < phoneList.size(); u++) {
                                if (phoneList.getJSONObject(u).getInteger("phone_order") == 0) {
                                    po.setCustomerPhone(phoneList.getJSONObject(u).getString("phone"));
                                }
                            }
                        }
                        po.setCustomerRegion(getAddress(id));
                        String sql = Sql.instance().insert().from("t_porsche_deliver_car")
                                .field("car_style", "car_model", "deliver_time", "customer_id", "customer_name", "customer_region", "customer_phone", "shop_id", "customer_type", "sale_id", "sale_name")
                                .value(po.getCarStyle(), po.getCarModel(), po.getDeliverTime(), po.getCustomerId(),
                                        po.getCustomerName(), po.getCustomerRegion(), po.getCustomerPhone(),
                                        po.getShopId(), po.getCustomerType(), po.getSaleId(), po.getSaleName())
                                .end().getSql();
                        new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
                    }
                }
            }
        }
    }

    @Test(description = "每日订车记录")
    public void orderCarData() {
        StringBuilder stringBuilder = new StringBuilder();
        TPorscheOrderCarDO po = new TPorscheOrderCarDO();
        String shopId = this.shopId;
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        IScene receptionScene = CustomerMyReceptionListScene.builder().searchDateStart(date).searchDateEnd(date).build();
        int total = crm.invokeApi(receptionScene).getInteger("total");
        int s = CommonUtil.getTurningPage(total, 100);
        for (int i = 1; i < s; i++) {
            IScene scene = CustomerMyReceptionListScene.builder().page(i).size(100).searchDateStart(date).searchDateEnd(date).build();
            JSONArray list = crm.invokeApi(scene).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                String id = list.getJSONObject(j).getString("customer_id");
                if (stringBuilder.toString().contains(id)) {
                    continue;
                }
                stringBuilder.append(id).append(",");
                IScene buyCarListScene = CustomerBuyCarListScene.builder().customerId(id).build();
                JSONArray buyCarList = crm.invokeApi(buyCarListScene).getJSONArray("list");
                for (int x = 0; x < buyCarList.size(); x++) {
                    if (buyCarList.getJSONObject(x).getString("buy_time") != null
                            && buyCarList.getJSONObject(x).getString("buy_time").equals(date)) {
                        po.setSaleName(list.getJSONObject(j).getString("sale_name"));
                        po.setSaleId(getSaleId(list.getJSONObject(j).getString("sale_name")));
                        po.setCarStyle(buyCarList.getJSONObject(x).getString("car_style_id"));
                        po.setCarModel(buyCarList.getJSONObject(x).getString("car_model_name"));
                        po.setOrderTime(buyCarList.getJSONObject(x).getString("buy_time"));
                        po.setCustomerId(Integer.parseInt(id));
                        po.setShopId(shopId);
                        JSONObject response = crm.invokeApi(CustomerInfoScene.builder().customerId(id).build());
                        po.setCustomerName(response.getString("name"));
                        po.setCustomerType(response.getString("subject_type"));
                        JSONArray phoneList = response.getJSONArray("phone_list");
                        if (phoneList == null) {
                            po.setCustomerPhone(null);
                        } else {
                            for (int u = 0; u < phoneList.size(); u++) {
                                if (phoneList.getJSONObject(u).getInteger("phone_order") == 0) {
                                    po.setCustomerPhone(phoneList.getJSONObject(u).getString("phone"));
                                }
                            }
                        }
                        po.setCustomerRegion(getAddress(id));
                        String sql = Sql.instance().insert().from("t_porsche_order_car")
                                .field("car_style", "car_model", "order_time", "customer_id", "customer_name", "customer_region", "customer_phone", "shop_id", "customer_type", "sale_id", "sale_name")
                                .value(po.getCarStyle(), po.getCarModel(), po.getOrderTime(), po.getCustomerId(),
                                        po.getCustomerName(), po.getCustomerRegion(), po.getCustomerPhone(),
                                        po.getShopId(), po.getCustomerType(), po.getSaleId(), po.getSaleName())
                                .end().getSql();
                        new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
                    }
                }
            }
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
     * 获取顾客年龄
     *
     * @param birthday   生日
     * @param customerId 顾客id
     * @return age
     */
    private String getAge(String birthday, String customerId) {
        if (StringUtils.isEmpty(birthday)) {
            return getProperty(customerId, "age_group_name");
        }
        return CommonUtil.getAge(birthday);
    }

    /**
     * 获取顾客性别
     *
     * @param ID_number  省份证号
     * @param customerId 顾客id
     * @return gender
     */
    private String getGender(String ID_number, String customerId) {
        if (StringUtils.isEmpty(ID_number)) {
            String s = getProperty(customerId, "sex_name");
            if (s != null) {
                return s.equals("先生") ? "男性" : "女性";
            }
            return null;
        }
        return CommonUtil.getGender(ID_number);
    }

    /**
     * 获取顾客属性
     *
     * @param customerId 顾客id
     * @param property   属性
     * @return property
     */
    private String getProperty(String customerId, String property) {
        IScene scene = CustomerFaceLabelScene.builder().customerId(customerId).build();
        JSONArray faceList = crm.invokeApi(scene).getJSONArray("face_list");
        for (int i = 0; i < faceList.size(); i++) {
            if (faceList.getJSONObject(i).getBoolean("is_decision")) {
                return faceList.getJSONObject(i).getString(property);
            }
        }
        return null;
    }

    /**
     * 获取顾客地址
     *
     * @param customerId 顾客id
     * @return address
     */
    private String getAddress(String customerId) {
        IScene customerInfoScene = CustomerInfoScene.builder().customerId(customerId).build();
        JSONObject response = crm.invokeApi(customerInfoScene);
        String address = response.getString("district_name");
        if (StringUtils.isEmpty(address)) {
            String idNumber = response.getString("id_number");
            if (StringUtils.isEmpty(idNumber)) {
                return null;
            }
            return Address.getNativePlace(Integer.parseInt(customerId));
        }
        return address;
    }
}
