package com.haisheng.framework.testng.bigScreen.crm.wm.datastore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumShopId;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerBuyCarListScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerFaceLabelScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerInfoScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerMyReceptionListScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.sql.Address;
import com.haisheng.framework.testng.bigScreen.crm.wm.sql.Sql;
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

public class B extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    private static final EnumAccount zjl = EnumAccount.ZJL_DAILY;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
//        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
//        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
//        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
//        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
//        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getName());
        //替换钉钉推送
//        commonConfig.dingHook = EnumDingTalkWebHook.OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_SHOP.getShopId();
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
    public void testB() {
        String reception_sale;
        String reception_start_time;
        String reception_end_time;
        String reception_date;
        String customer_name;
        String customer_active_type;
        String shop_id;
        String customer_phone1;
        String customer_phone2;
        int reception_duration = 0;
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        int total = crm.receptionPage("", "", "PRE_SALES", 1, 10).getInteger("total");
        int s = CommonUtil.getTurningPage(total, 100);
        for (int i = 1; i < s; i++) {
            JSONArray list = crm.receptionPage("", "", "PRE_SALES", i, 100).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("reception_date").equals(date)) {
                    String saleName = list.getJSONObject(j).getString("reception_sale");
                    reception_sale = saleName.substring(0, saleName.indexOf("（"));
                    reception_start_time = list.getJSONObject(j).getString("reception_time");
                    reception_end_time = list.getJSONObject(j).getString("leave_time");
                    reception_date = list.getJSONObject(j).getString("reception_date");
                    customer_name = list.getJSONObject(j).getString("customer_name");
                    customer_active_type = list.getJSONObject(j).getString("customer_active_type");
                    shop_id = list.getJSONObject(j).getString("shopId");
                    customer_phone1 = list.getJSONObject(j).getString("phone1");
                    customer_phone2 = list.getJSONObject(j).getString("phone2");
                    try {
                        reception_duration = new DateTimeUtil().calTimeHourDiff(list.getJSONObject(j).getString("reception_time"), list.getJSONObject(j).getString("leave_time"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String sql = Sql.instance().insert().from("t_porsche_reception_data")
                            .field("shop_id", "reception_sale", "reception_start_time", "reception_end_time", "reception_date", "customer_name", "customer_active_type", "customer_phone_1", "customer_phone_2", "reception_duration")
                            .value(shop_id, reception_sale, reception_start_time, reception_end_time, reception_date, customer_name, customer_active_type, customer_phone1, customer_phone2, reception_duration)
                            .end().getSql();
                    new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
                }
            }
        }
        int appTotal = crm.receptionPage(1, 10, date, date).getInteger("total");
        int y = CommonUtil.getTurningPage(appTotal, 100);
        for (int i = 1; i < y; i++) {
            JSONArray list = crm.receptionPage(i, 100, date, date).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("reception_sale_name") == null) {
                    reception_date = list.getJSONObject(j).getString("reception_date");
                    customer_name = list.getJSONObject(j).getString("customer_name");
                    shop_id = EnumShopId.PORSCHE_SHOP.getShopId();
                    reception_sale = list.getJSONObject(j).getString("reception_sale_name");
                    reception_start_time = list.getJSONObject(j).getString("reception_time");
                    reception_end_time = list.getJSONObject(j).getString("leave_time_str");
                    try {
                        String q = list.getJSONObject(j).getString("reception_time") == null ? "00:00" : list.getJSONObject(j).getString("reception_time");
                        String p = list.getJSONObject(j).getString("leave_time_str") == null ? "00:00" : list.getJSONObject(j).getString("leave_time_str");
                        reception_duration = new DateTimeUtil().calTimeHourDiff(q, p);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String sql = Sql.instance().insert().from("t_porsche_reception_data")
                            .field("shop_id", "reception_sale", "reception_start_time", "reception_end_time", "reception_date", "customer_name", "reception_duration")
                            .value(shop_id, reception_sale, reception_start_time, reception_end_time, reception_date, customer_name, reception_duration)
                            .end().getSql();
                    new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
                }
            }
        }
    }

    @Test(description = "每日交车记录")
    public void testC() {
        StringBuilder stringBuilder = new StringBuilder();
        String carStyle;
        String carModel;
        String deliverTime;
        int customerId;
        String customerName;
        String customerPhone = null;
        String customerRegion;
        String customerType;
        String shopId = EnumShopId.PORSCHE_SHOP.getShopId();
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
                        carStyle = buyCarList.getJSONObject(x).getString("car_style_id");
                        carModel = buyCarList.getJSONObject(x).getString("car_model_name");
                        deliverTime = buyCarList.getJSONObject(x).getString("deliver_time");
                        customerId = Integer.parseInt(id);
                        IScene customerInfoScene = CustomerInfoScene.builder().customerId(id).build();
                        JSONObject response = crm.invokeApi(customerInfoScene);
                        customerName = response.getString("name");

                        customerType = response.getString("subject_type");
                        JSONArray phoneList = response.getJSONArray("phone_list");
                        if (phoneList == null) {
                            customerPhone = null;
                        } else {
                            for (int u = 0; u < phoneList.size(); u++) {
                                if (phoneList.getJSONObject(u).getInteger("phone_order") == 0) {
                                    customerPhone = phoneList.getJSONObject(u).getString("phone");
                                }
                            }
                        }
                        customerRegion = getAddress(id);
                        String sql = Sql.instance().insert().from("t_porsche_deliver_car")
                                .field("car_style", "car_model", "deliver_time", "customer_id", "customer_name", "customer_region", "customer_phone", "shop_id", "customer_type")
                                .value(carStyle, carModel, deliverTime, customerId, customerName, customerRegion, customerPhone, shopId, customerType)
                                .end().getSql();
                        new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
                    }
                }
            }
        }
    }

    private String getAge(String birthday, String customerId) {
        if (StringUtils.isEmpty(birthday)) {
            return getProperty(customerId, "age_group_name");
        }
        return CommonUtil.getAge(birthday);
    }

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

    private String getAddress(String customerId) {
        IScene customerInfoScene = CustomerInfoScene.builder().customerId(customerId).build();
        JSONObject response = crm.invokeApi(customerInfoScene);
        String address = response.getString("address");
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
