package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.pc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.customermanage.AfterSaleCustomerPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.customermanage.PreSaleCustomerPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.manage.AppointmentMaintainConfigDetailBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.shop.ShopPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.shopstylemodel.ManageModelPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage.AfterSaleCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionManagerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop.ShopPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop.ShopStatusChangeScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shopstylemodel.ManageModelEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shopstylemodel.ManageModelPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

public class AppointmentManagerCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCT = EnumTestProduct.JC_ONLINE_JD;
    private static final EnumAccount account = EnumAccount.JC_ONLINE_LXQ;
    private static final EnumAppletToken APPLET_USER = EnumAppletToken.JC_LXQ_ONLINE;
    private final VisitorProxy visitor = new VisitorProxy(PRODUCT);
    private final SceneUtil util = new SceneUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.XMF.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCT.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(account.getReceptionShopId()).setRoleId(PRODUCT.getRoleId()).setReferer(PRODUCT.getReferer()).setProduct(PRODUCT.getAbbreviation());
        beforeClassInit(commonConfig);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        util.loginPc(account);
        logger.logCaseStart(caseResult.getCaseName());
    }

    @Test(description = "??????????????????")
    public void receptionSelect() {
        try {
            IScene scene = AfterSaleCustomerPageScene.builder().shopId(Long.parseLong(account.getReceptionShopId())).build();
            String plateNumber = util.toFirstJavaObject(scene, AfterSaleCustomerPageBean.class).getPlateNumber();
            scene = ReceptionManagerPageScene.builder().plateNumber(plateNumber).build();
            util.toJavaObjectList(scene, JSONObject.class).forEach(e -> Preconditions.checkArgument(e.getString("plate_number").contains(plateNumber), "???????????????" + plateNumber + "?????????????????????" + e.getString("plate_number")));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("?????????????????????????????????");
        }
    }

    @Test()
    public void preSleCustomerManageOneFilter() {
        try {
            IScene scene = PreSaleCustomerPageScene.builder().build();
            String customerName = util.toFirstJavaObject(scene, PreSaleCustomerPageBean.class).getCustomerName();
            scene = PreSaleCustomerPageScene.builder().customerName(customerName).build();
            util.toJavaObjectList(scene, PreSaleCustomerPageBean.class).forEach(e -> Preconditions.checkArgument(e.getCustomerName().contains(customerName), "???????????????" + customerName + "????????????????????????" + e.getCustomerName()));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("?????????????????????????????????????????????");
        }
    }

    /**
     * @description :???????????????????????????????????????
     * @date :2020/12/15 17:47
     **/
    @Test(description = "pc?????????????????????", dataProvider = "PLATE", dataProviderClass = ScenarioUtil.class)
    public void pcReceiptAb(String plate) {
        try {
            int code = ReceptionScene.builder().plateNumber(plate).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(code == 1001, "??????????????????" + code + "????????????code???1001 ??????code??????" + code);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc?????????????????????");
        }
    }

    @Test(description = "pc???????????????????????????????????????")
    public void pcReceipt() {
        try {
            JSONObject response = ReceptionScene.builder().plateNumber(util.getPlatNumber(EnumAppletToken.JC_WM_DAILY.getPhone())).build().visitor(visitor).execute();
            String jsonpath = "$.arrive_times&&$.customers[*].voucher_list[*]&&$.er_code_url&&$.last_reception_sale_name&&$.last_arrive_time&&$.plate_number";
            JsonPathUtil.spiltString(response.toJSONString(), jsonpath);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc???????????????????????????????????????");
        }
    }

    //***************pc??????????????????********************

    @DataProvider(name = "TYPE")
    public static Object[] type() {
        return new String[]{
                "MAINTAIN",
                "REPAIR"
        };
    }

    @Test(description = "pc?????????????????????????????????????????????")
    public void pcMaintainPriceEdit() {
        try {
            String type = AppointmentTypeEnum.MAINTAIN.name();
            //???????????????????????????????????????????????????????????????
            Double price = 300.00;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 0);
            String dataType = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ? "WEEKEND" : "WEEKDAY";
            //??????????????????
            IScene scene = ManageModelPageScene.builder().type(type).build();
            Long id = util.toFirstJavaObject(scene, ManageModelPageBean.class).getId();
            ManageModelEditScene.builder().id(id).price(price).type(type).status("ENABLE").build().visitor(visitor).execute();
            //????????????????????????
            JSONObject timeRangeDetail = AppointmentTimeRangeDetailScene.builder().type(type).dateType(dataType).build().visitor(visitor).execute();
            JSONArray morning = timeRangeDetail.getJSONObject("morning").getJSONArray("list");
            JSONArray afternoon = timeRangeDetail.getJSONObject("afternoon").getJSONArray("list");
            Double[] discount = new Double[morning.size() + afternoon.size()];
            for (int i = 0; i < morning.size(); i++) {
                discount[i] = morning.getJSONObject(i).getDouble("discount");
            }
            for (int j = 0; j < afternoon.size(); j++) {
                discount[morning.size() + j] = afternoon.getJSONObject(j).getDouble("discount");
            }
            //?????????????????????????????????
            util.loginApplet(APPLET_USER);
            JSONArray appletTime = AppletAppointmentTimeListScene.builder().shopId(Long.parseLong(account.getReceptionShopId()))
                    .carId(util.getCarId()).day(DateTimeUtil.getFormat(new Date())).type(type).build().visitor(visitor).execute().getJSONArray("list");
            Preconditions.checkArgument(discount.length == appletTime.size(), "pc???????????????????????????????????????????????????");
            for (int z = 0; z < appletTime.size(); z++) {
                String priceApplet = appletTime.getJSONObject(z).getString("price");
                Double result = price * discount[z];
                String pp = priceApplet.substring(1);
                Double pa = Double.valueOf(pp);
                Preconditions.checkArgument(result.equals(pa), "??????????????????" + result + ":" + pa);
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc????????????????????????");
        }
    }

    @Test(dataProvider = "APPOINTMENT_TYPE", description = "pc????????????????????????????????????????????????", enabled = false)
    public void pcMaintainPriceEditAb(String type) {
        try {
            Double[] price = {null, 100000000.011};
            IScene scene = ManageModelPageScene.builder().type(type).build();
            Long id = util.toFirstJavaObject(scene, ManageModelPageBean.class).getId();
            //??????????????????
            for (Double s : price) {
                int code = ManageModelEditScene.builder().id(id).price(s).type(type).status("ENABLE").build().visitor(visitor).getResponse().getCode();
                Preconditions.checkArgument(code == 1001, "????????????????????????code???1001 ????????????" + code);
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc????????????????????????????????????");
        }
    }

    @Test(description = "pc????????????????????????????????????????????????????????????")
    public void pcMaintainPriceEdit2() {
        try {
            String type = "MAINTAIN";
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 0);
            Double price = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ? 200.0 : 300.0;
            IScene scene = ManageModelPageScene.builder().type(type).build();
            ManageModelPageBean manageModelPageBean = util.toFirstJavaObject(scene, ManageModelPageBean.class);
            Long id = manageModelPageBean.getId();
            String carModel = manageModelPageBean.getModel();
            ManageModelEditScene.builder().id(id).price(price).type(type).build().visitor(visitor).getResponse();
            scene = ManageModelPageScene.builder().type(type).carModel(carModel).build();
            Double newPrice = util.toJavaObject(scene, ManageModelPageBean.class, "model", carModel).getPrice();
            Preconditions.checkArgument(newPrice.equals(price), "???????????????????????????" + price + " ???????????????????????????" + newPrice);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc???????????????????????????????????????");
        }
    }

    @Test(dataProvider = "APPOINTMENT_TYPE", description = "pc????????????????????????-????????????????????????????????????????????????")
    public void pcAppointmentButton2(String type) {
        try {
            IScene manageModelPageScene = ManageModelPageScene.builder().type(type).build();
            ManageModelPageBean manageModelPageBean = util.toFirstJavaObject(manageModelPageScene, ManageModelPageBean.class);
            String status = manageModelPageBean.getStatus();
            Long id = manageModelPageBean.getId();
            if (!status.equals("ENABLE")) {
                ManageModelEditScene.builder().id(id).status("DISABLE").type(type).build().visitor(visitor).execute();
            }
            int total = manageModelPageScene.visitor(visitor).execute().getInteger("total");
            ManageModelEditScene.builder().id(id).status("DISABLE").type(type).build().visitor(visitor).execute();
            int disableTotal = manageModelPageScene.visitor(visitor).execute().getInteger("total");
            ManageModelEditScene.builder().id(id).status("ENABLE").type(type).build().visitor(visitor).execute();
            int enableTotal = manageModelPageScene.visitor(visitor).execute().getInteger("total");
            Preconditions.checkArgument(total == disableTotal, "???????????????????????????");
            Preconditions.checkArgument(disableTotal == enableTotal, "???????????????????????????????????????");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc???????????????????????????????????????????????????????????????");
        }
    }
    //***********pc????????????************************

    @Test(dataProvider = "APPOINTMENT_TYPE", description = "pc???????????????????????????????????????????????????")
    public void pcMaintainTableEdit(String type) {
        try {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            //????????????????????????????????????
            IScene weekdayScene = AppointmentTimeRangeDetailScene.builder().type(type).dateType("WEEKDAY").build();
            JSONObject timeDetailWeekday = weekdayScene.visitor(visitor).execute();
            JSONObject afternoonBefore = timeDetailWeekday.getJSONObject("afternoon");
            JSONObject morningBefore = timeDetailWeekday.getJSONObject("morning");
            //????????????????????????
            IScene weekendScene = AppointmentTimeRangeDetailScene.builder().type(type).dateType("WEEKEND").build();
            JSONObject timeDetailWeekend = weekendScene.visitor(visitor).execute();
            JSONObject morning = timeDetailWeekend.getJSONObject("morning");
            JSONObject afternoon = timeDetailWeekend.getJSONObject("afternoon");
            if (day % 2 == 1) {
                morning.put("reply_start", "09:00");  //????????? ??????9???00
            } else {
                morning.put("reply_start", "08:00");
            }
            afternoon.put("title", "????????????");
            //???????????????????????????
            AppointmentTimeRangeEditScene.builder().type(type).dateType("WEEKEND").morning(morning).afternoon(afternoon).build().visitor(visitor).execute();
            //?????????????????????,?????????
            JSONObject timeDetailWeekdayAfter = weekdayScene.visitor(visitor).execute();
            JSONObject afternoonAfter = timeDetailWeekdayAfter.getJSONObject("afternoon");
            JSONObject morningAfter = timeDetailWeekdayAfter.getJSONObject("morning");
            //?????????
//            JSONObject timeDetailWeekendAfter = weekendScene.visitor(visitor).execute();
//            JSONObject afternoonAfterEnd = timeDetailWeekendAfter.getJSONObject("afternoon");
//            JSONObject morningAfterEnd = timeDetailWeekendAfter.getJSONObject("morning");
            //?????????
            Preconditions.checkArgument(afternoonAfter.equals(afternoonBefore), "??????????????????????????????????????????????????????????????????");
            Preconditions.checkArgument(morningAfter.equals(morningBefore), "??????????????????????????????????????????????????????????????????");
            //?????????
//            Preconditions.checkArgument(afternoonAfterEnd.equals(afternoon),"??????????????????????????????????????????????????????????????????");
//            Preconditions.checkArgument(morningAfterEnd.equals(morning),"????????????????????????????????????????????????????????????????????????");   //false
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc???????????????????????????????????????????????????");
        }
    }

    @Test(dataProvider = "APPOINTMENT_TYPE", description = "pc???????????????????????????")
    public void pcMaintainTableEdit2(String type) {
        try {
            AppointmentConfigScene.builder().type(type).remindTime(20).replayTimeLimit(30).appointmentInterval(1).build().visitor(visitor).execute();
            IScene scene = AppointmentMaintainConfigDetailScene.builder().type(type).build();
            AppointmentMaintainConfigDetailBean as = util.toJavaObject(scene, AppointmentMaintainConfigDetailBean.class);
            Preconditions.checkArgument(as.getAppointmentInterval() == 1, "????????????????????????");
            Preconditions.checkArgument(as.getReplayTimeLimit() == 30, "????????????????????????");
            Preconditions.checkArgument(as.getRemindTime() == 20, "????????????????????????");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc???????????????????????????");
        }
    }

    //***********pc??????????????????************
    @Test(dataProvider = "APPOINTMENT_TYPE", description = "pc??????????????????????????????????????????")
    public void pcShopAppointmentButton(String type) {
        try {
            Long shopId = Long.parseLong(account.getReceptionShopId());
            IScene scene = ShopPageScene.builder().name(account.getReceptionShopName()).build();
            String appointmentStatus = util.toFirstJavaObject(scene, ShopPageBean.class).getAppointmentStatus();
            if (!appointmentStatus.equals("ENABLE")) {
                ShopStatusChangeScene.builder().status("ENABLE").type("APPOINTMENT").id(shopId).build().visitor(visitor).execute();
            }
            //?????????????????????????????????
            util.loginApplet(APPLET_USER);
            Long carId = util.getCarId();
            int total = AppletAppointmentShopListScene.builder().carId(carId).coordinate(util.getCoordinate()).type(type).build().visitor(visitor).execute().getJSONArray("list").size();
            //????????????????????????
            util.loginPc(account);
            ShopStatusChangeScene.builder().status("DISABLE").type("APPOINTMENT").id(shopId).build().visitor(visitor).execute();
            //???????????????????????????
            util.loginApplet(APPLET_USER);
            int closeTotal = AppletAppointmentShopListScene.builder().carId(carId).coordinate(util.getCoordinate()).type(type).build().visitor(visitor).execute().getJSONArray("list").size();
            util.loginPc(account);
            ShopStatusChangeScene.builder().status("ENABLE").type("APPOINTMENT").id(shopId).build().visitor(visitor).execute();
            util.loginApplet(APPLET_USER);
            int openTotal = AppletAppointmentShopListScene.builder().carId(carId).coordinate(util.getCoordinate()).type(type).build().visitor(visitor).execute().getJSONArray("list").size();
            Preconditions.checkArgument(total - closeTotal == 1, "??????????????????????????????????????????-1");
            Preconditions.checkArgument(openTotal - closeTotal == 1, "??????????????????????????????????????????+1");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc????????????????????????");
        }
    }

    @DataProvider(name = "APPOINTMENT_TYPE")
    public static Object[] appointment_type() {
        return new String[]{
                "MAINTAIN",
                "REPAIR",
        };
    }

    @Test(description = "pc??????????????????????????????????????????????????????")
    public void pcShopButton() {
        try {
            Long shopId = Long.parseLong(account.getReceptionShopId());
            String statusAll = "DISABLE";
            String open = "ENABLE";
            IScene scene = ShopPageScene.builder().build();
            ShopPageBean shopPageBean = util.toJavaObject(scene, ShopPageBean.class, "id", shopId);
            String status = shopPageBean.getStatus();
            if (!status.equals("ENABLE")) {
                statusAll = "ENABLE";
                open = "DISABLE";
            }
            //????????????????????????
            ShopStatusChangeScene.builder().status(statusAll).type("SHOP").id(shopId).build().visitor(visitor).execute();
            shopPageBean = util.toJavaObject(scene, ShopPageBean.class, "id", shopId);
            status = shopPageBean.getStatus();
            String appointmentStatus = shopPageBean.getAppointmentStatus();
            String washingStatus = shopPageBean.getWashingStatus();
            Preconditions.checkArgument(appointmentStatus.equals(statusAll) && washingStatus.equals(statusAll) && status.equals(statusAll), "?????????????????????");
            //????????????????????????
            ShopStatusChangeScene.builder().status(open).type("SHOP").id(shopId).build().visitor(visitor).execute();
            shopPageBean = util.toJavaObject(scene, ShopPageBean.class, "id", shopId);
            status = shopPageBean.getStatus();
            appointmentStatus = shopPageBean.getAppointmentStatus();
            washingStatus = shopPageBean.getWashingStatus();
            Preconditions.checkArgument(appointmentStatus.equals(statusAll) && washingStatus.equals(statusAll) && status.equals(open), "?????????????????????2");
            ShopStatusChangeScene.builder().status(open).type("WASHING").id(shopId).build().visitor(visitor).execute();
            ShopStatusChangeScene.builder().status(open).type("APPOINTMENT").id(shopId).build().visitor(visitor).execute();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc??????????????????????????????");
        }
    }
}
