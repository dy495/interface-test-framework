package com.haisheng.framework.testng.bigScreen.jiaochen.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager.AfterSaleCustomerPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager.WechatCustomerPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.BuyPackageRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.PackageFormPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.Page;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.PurchaseFixedPackage;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BusinessManage extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = ScenarioUtil.getInstance();
    private static final Integer size = 100;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_JIAOCHEN_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.produce = EnumProduce.JC.name();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName());
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_DAILY.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
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
    }

    @Test(description = "接待管理--开始接待，列表数+1,【首页-今日任务-接待】分子，分母+1,【首页-今日任务-接待】分子，分母+1")
    public void receptionManage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //接待列表数量
            IScene scene = Page.builder().build();
            int total = jc.invokeApi(scene).getInteger("total");
            //app接待
            JSONObject response = jc.appTodayTask();
            int surplusReception = response.getInteger("surplus_reception");
            int allReception = response.getInteger("all_reception");
            int receptionTotal = jc.appReceptionPage(null, 100).getInteger("total");
            //获取卡券id集合
            List<Long> list = new ArrayList<>();
            JSONArray array = jc.appletVoucherList(null, null, size).getJSONArray("list");
            for (int i = 0; i < array.size(); i++) {
                list.add(array.getJSONObject(i).getLong("id"));
            }
            //开始接待
            jc.pcStartReception("", list, "", "");
            int newTotal = jc.invokeApi(scene).getInteger("total");
            Preconditions.checkArgument(newTotal == total + 1, "");
            JSONObject response1 = jc.appTodayTask();
            int newSurplusReception = response1.getInteger("surplus_reception");
            int newAllReception = response1.getInteger("all_reception");
            Preconditions.checkArgument(newAllReception == allReception + 1, "");
            Preconditions.checkArgument(newSurplusReception == surplusReception + 1, "");
            int newReceptionTotal = jc.appReceptionPage(null, 100).getInteger("total");
            Preconditions.checkArgument(newReceptionTotal == receptionTotal + 1, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--开始接待，列表数+1,【首页-今日任务-接待】分子，分母+1,【首页-今日任务-接待】分子，分母+1");
        }
    }

    @Test(description = "接待管理--完成接待，列表数+0&&小程序【我的消息】+1")
    public void receptionManage_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //接待列表数量
            Page.PageBuilder builder = Page.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            //小程序我的消息数量
            int appletTotal = jc.appletMessageList(null, size).getInteger("total");
            builder.receptionStatus(0);
            JSONObject data = jc.invokeApi(builder.build());
            Long receptionId = (long) CommonUtil.getIntField(data, 0, "reception_id");
            //完成接待
            jc.pcFinishReception(receptionId);
            IScene scene = Page.builder().build();
            int newTotal = jc.invokeApi(scene).getInteger("total");
            Preconditions.checkArgument(newTotal == total, "");
            int newAppletTotal = jc.appletMessageList(null, size).getInteger("total");
            Preconditions.checkArgument(newAppletTotal == appletTotal + 1, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--完成接待，列表数+0&&小程序【我的消息】+1");
        }
    }

    @Test(description = "接待管理--购买套餐，列表数+0")
    public void receptionManage_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //接待列表数量
            Page.PageBuilder builder = Page.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            //购买记录列表
            IScene recordScene = BuyPackageRecord.builder().build();
            int recordTotal = jc.invokeApi(recordScene).getInteger("total");
            //套餐表单售出数量
            IScene packageScene = PackageFormPage.builder().packageName("").build();
            int soldNumber = CommonUtil.getIntField(jc.invokeApi(packageScene), 0, "sold_number");
            builder.receptionStatus(0);
            JSONObject data = jc.invokeApi(builder.build());
            Long receptionId = (long) CommonUtil.getIntField(data, 0, "reception_id");
            Long customerId = (long) CommonUtil.getIntField(data, 0, "customer_id");
            //购买套餐
            IScene scene = PurchaseFixedPackage.builder().customerId(customerId).receptionId(receptionId)
                    .packageId(null).carType("ALL_CAR").selectNumber(1).price(100)
                    .expiryDate(30).remark("xxxxxxx").voucherType("CURRENT").type(1)
                    .subjectType("").subjectId(1).build();
            jc.invokeApi(scene);
            int newTotal = jc.invokeApi(builder.build()).getInteger("total");
            Preconditions.checkArgument(newTotal == total, "");
            int newRecordTotal = jc.invokeApi(recordScene).getInteger("total");
            Preconditions.checkArgument(newRecordTotal == recordTotal + 1, "");
            int newSoldNumber = CommonUtil.getIntField(jc.invokeApi(packageScene), 0, "sold_number");
            Preconditions.checkArgument(newSoldNumber == soldNumber + 1, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--购买套餐，列表数+0");
        }
    }

    @Test(description = "接待管理--取消，列表数+0")
    public void receptionManage_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //接待列表数量
            Page.PageBuilder builder = Page.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            builder.receptionStatus(0);
            JSONObject data = jc.invokeApi(builder.build());
            Long receptionId = (long) CommonUtil.getIntField(data, 0, "reception_id");
            int receptionTotal = jc.appReceptionPage(null, 100).getInteger("total");
            //取消接待
            jc.pcCancelReception(receptionId);
            IScene scene = Page.builder().build();
            int newTotal = jc.invokeApi(scene).getInteger("total");
            Preconditions.checkArgument(newTotal == total, "");
            int newReceptionTotal = jc.appReceptionPage(null, 100).getInteger("total");
            Preconditions.checkArgument(newReceptionTotal == receptionTotal - 1, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--取消，列表数+0");
        }
    }

    @Test(description = "`接待管理--购买一个套餐，【小程序客户】消费频次+1&&【小程序客户】总消费+累计金额&&【售后客户】频次+1&&【售后客户】总消费+累计金额`")
    public void receptionManage_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //接待页接待
            JSONObject pageData = jc.invokeApi(Page.builder().receptionStatus(0).build());
            Long receptionId = (long) CommonUtil.getIntField(pageData, 0, "reception_id");
            Long customerId = (long) CommonUtil.getIntField(pageData, 0, "customer_id");
            String plateNumber = CommonUtil.getStrField(pageData, 0, "plate_number");
            String customerPhone = CommonUtil.getStrField(pageData, 0, "customer_phone");
            //购买前，售后客户消费频次&总消费
            AfterSaleCustomerPage.AfterSaleCustomerPageBuilder afterSaleCustomerBuilder = AfterSaleCustomerPage.builder().customerPhone(customerPhone);
            JSONObject afterSaleCustomerData = jc.invokeApi(afterSaleCustomerBuilder.build());
            int repairTimes = CommonUtil.getIntField(afterSaleCustomerData, 0, "repair_times");
            long afterSaleTotalPrice = (long) CommonUtil.getIntField(afterSaleCustomerData, 0, "total_price");
            //购买前，小程序客户消费频次&总消费
            WechatCustomerPage.WechatCustomerPageBuilder wechatCustomerBuilder = WechatCustomerPage.builder().customerPhone(customerPhone);
            JSONObject wechatCustomerData = jc.invokeApi(wechatCustomerBuilder.build());
            int consumeTimes = CommonUtil.getIntField(wechatCustomerData, 0, "consume_times");
            long wechatTotalPrice = (long) CommonUtil.getIntField(wechatCustomerData, 0, "total_price");
            //购买套餐
            IScene scene = PurchaseFixedPackage.builder().customerId(customerId).receptionId(receptionId)
                    .packageId(null).carType("ALL_CAR").selectNumber(1).price(100).expiryDate(30).remark("xxxxxxx")
                    .voucherType("CURRENT").type(1).subjectType("").subjectId(1).build();
            jc.invokeApi(scene);
            //购买后，售后客户消费频次+1
            int customerTotal = jc.invokeApi(afterSaleCustomerBuilder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(customerTotal, size);
            for (int i = 1; i < s; i++) {
                afterSaleCustomerBuilder.page(i).size(size);
                JSONArray array = jc.invokeApi(afterSaleCustomerBuilder.build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getString("plate_number").equals(plateNumber)) {
                        int newRepairTimes = array.getJSONObject(j).getInteger("repair_times");
                        Preconditions.checkArgument(newRepairTimes == repairTimes + 1, "");
                    }
                }
            }
            //购买后，售后客户总消费=购买前总消费+新买套餐价格
            long newAfterSaleTotalPrice = (long) CommonUtil.getIntField(jc.invokeApi(afterSaleCustomerBuilder.build()), 0, "total_price");
            Preconditions.checkArgument(newAfterSaleTotalPrice == afterSaleTotalPrice + 100, "");
            //购买后，小程序客户消费频次+1
            int newConsumeTimes = CommonUtil.getIntField(jc.invokeApi(wechatCustomerBuilder.build()), 0, "consume_times");
            Preconditions.checkArgument(newConsumeTimes == consumeTimes + 1, "");
            //购买后，小程序客户总消费=购买前总消费+新买套餐价格
            long newWechatTotalPrice = (long) CommonUtil.getIntField(jc.invokeApi(wechatCustomerBuilder.build()), 0, "total_price");
            Preconditions.checkArgument(newWechatTotalPrice == wechatTotalPrice + 100, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--购买一个套餐，【小程序客户】消费频次+1&&【小程序客户】总消费+累计金额&&【售后客户】频次+1&&【售后客户】总消费+累计金额");
        }
    }

    @Test(description = "客户管理--【售后客户】列表中频次＝【接待管理】该客户已完成接待的次数")
    public void customerManage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            AfterSaleCustomerPage.AfterSaleCustomerPageBuilder builder = AfterSaleCustomerPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                builder.page(i).size(size);
                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    //车牌号
                    String platNumber = array.getJSONObject(j).getString("plate_number");
                    //频次
                    int repairTimes = array.getJSONObject(j).getInteger("repair_times");
                    //按车牌号搜索接待记录
                    IScene scene = Page.builder().plateNumber(platNumber).receptionStatus(1).build();
                    int receptionTotal = jc.invokeApi(scene).getInteger("total");
                    Preconditions.checkArgument(repairTimes == receptionTotal, "");
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("客户管理--【售后客户】列表中频次＝【接待管理】该客户已完成接待的次数");
        }
    }

    @Test(description = "客户管理--【售后客户】列表中 总消费＝维修记录中产值之和")
    public void customerManage_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            AfterSaleCustomerPage.AfterSaleCustomerPageBuilder builder = AfterSaleCustomerPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                builder.page(i).size(size);
                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    //总产值
                    int totalPrice = array.getJSONObject(j).getInteger("total_price");
                    int carId = array.getJSONObject(j).getInteger("car_id");
                    //维修记录中数据
                    int repairTotal = jc.pcAfterSaleCustomerRepairPage(1, size, carId).getInteger("total");
                    int x = CommonUtil.getTurningPage(repairTotal, size);
                    int priceSum = 0;
                    for (int u = 0; u < x; u++) {
                        JSONArray repairArray = jc.pcAfterSaleCustomerRepairPage(i, size, carId).getJSONArray("list");
                        for (int v = 0; v < repairArray.size(); v++) {
                            priceSum += array.getJSONObject(v).getInteger("output_value");
                        }
                    }
                    Preconditions.checkArgument(totalPrice == priceSum, "");
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("客户管理--【售后客户】列表中 总消费＝维修记录中产值之和");
        }
    }

    @Test(description = "客户管理--【售后客户】的送修人姓名&&联系电话&&里程数=维修记录中最新及记录的姓名&&联系电话&&里程数")
    public void customerManage_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            AfterSaleCustomerPage.AfterSaleCustomerPageBuilder builder = AfterSaleCustomerPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                builder.page(i).size(size);
                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    //总产值
                    String repairCustomerName = array.getJSONObject(j).getString("repair_customer_name");
                    String repairCustomerPhone = array.getJSONObject(j).getString("repair_customer_phone");
                    int newestMiles = array.getJSONObject(j).getInteger("newest_miles");
                    int carId = array.getJSONObject(j).getInteger("car_id");
                    //维修记录中数据
                    JSONObject object = jc.pcAfterSaleCustomerRepairPage(1, size, carId).getJSONArray("list").getJSONObject(0);
                    String repairCustomerName1 = object.getString("repair_customer_name");
                    String repairCustomerPhone1 = object.getString("repair_customer_phone");
                    int newestMiles1 = object.getInteger("newest_miles");
                    Preconditions.checkArgument(repairCustomerName.equals(repairCustomerName1), "");
                    Preconditions.checkArgument(repairCustomerPhone.equals(repairCustomerPhone1), "");
                    Preconditions.checkArgument(newestMiles == newestMiles1, "");
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("客户管理--【售后客户】的送修人姓名&&联系电话&&里程数=维修记录中最新及记录的姓名&&联系电话&&里程数");
        }
    }

    @Test(description = "客户管理--【小程序客户】总消费=【售后客户】按此电话号搜索")
    public void customerManage_data_4() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            int total = jc.pcWechatCustomerPage(null, null, null, 1, size).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.pcWechatCustomerPage(null, null, null, i, size).getJSONArray("list");
//                for (int j = 0; j < array.size(); j++) {
//                    int totalPrice = array.getJSONObject(j).getInteger("total_price");
//                    String customerPhone = array.getJSONObject(j).getString("customer_phone");
//                    AfterSaleCustomerPage.AfterSaleCustomerPageBuilder builder = AfterSaleCustomerPage.builder().customerPhone(customerPhone);
//                    int afterTotal = jc.invokeApi(builder.build()).getInteger("total");
//                    int x = CommonUtil.getTurningPage(afterTotal, size);
//                    for (int y = 1; y < x; y++) {
//                        JSONArray array1 = jc.invokeApi(builder.page(y).size(size).build()).getJSONArray("list");
//                    }
//                }
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("客户管理--【小程序客户】总消费=【售后客户】按此电话号搜索");
//        }
    }
}
