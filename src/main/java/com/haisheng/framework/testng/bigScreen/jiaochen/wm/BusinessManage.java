package com.haisheng.framework.testng.bigScreen.jiaochen.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.jc.AfterSaleCustomerPageVO;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.jc.WechatCustomerPageVO;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumCarType;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumVP;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager.AfterSaleCustomerPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager.AfterSaleCustomerRepairPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager.WechatCustomerPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.BuyPackageRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.PackageFormPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.Page;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.PurchaseFixedPackage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.recordimport.ImportPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.LoginUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.JcFunction;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务管理测试用例
 */
public class BusinessManage extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = ScenarioUtil.getInstance();
    BusinessUtil util = new BusinessUtil();
    LoginUtil user = new LoginUtil();
    private static final Integer size = 100;
    private static final EnumAccount marketing = EnumAccount.MARKETING;
    private static final EnumAccount administrator = EnumAccount.ADMINISTRATOR;
    private static final EnumAppletToken appletUser = EnumAppletToken.JC_WM_DAILY;
    private static final EnumAppletToken applet = EnumAppletToken.JC_GLY_DAILY;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.produce = EnumProduce.JC.name();
        commonConfig.referer = EnumRefer.JIAOCHEN_REFERER_DAILY.getReferer();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.JIAOCHEN_DAILY.getShopId();
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
        user.login(administrator);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test(description = "接待管理--【套餐管理-套餐购买记录】列表数+1")
    public void receptionManage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //套餐购买记录列表数
            IScene buyPackageRecordScene = BuyPackageRecord.builder().build();
            int total = jc.invokeApi(buyPackageRecordScene).getInteger("total");
            util.receptionBuyFixedPackage(1);
            //购买后套餐购买记录页
            int newTotal = jc.invokeApi(buyPackageRecordScene).getInteger("total");
            CommonUtil.valueView(total, newTotal);
            Preconditions.checkArgument(newTotal == total + 1, "套餐购买列表" + CommonUtil.checkResult(total + 1, newTotal));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--【套餐管理-套餐购买记录】列表数+1");
        }
    }

    @Test(description = "接待管理--购买套餐，确认支付之后，该套餐购买数量+1")
    public void receptionManage_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //套餐购卖数量
            IScene packageFormPageScene = PackageFormPage.builder().packageName(EnumVP.ONE.getPackageName()).build();
            int num = CommonUtil.getIntField(jc.invokeApi(packageFormPageScene), 0, "sold_number");
            //购买套餐
            util.receptionBuyFixedPackage(1);
            //确认支付
            util.makeSureBuyPackage(EnumVP.ONE.getPackageName());
            //购买后套餐购卖数量
            int newNum = CommonUtil.getIntField(jc.invokeApi(packageFormPageScene), 0, "sold_number");
            CommonUtil.valueView(num, newNum);
            Preconditions.checkArgument(newNum == num + 1, "套餐购买列表" + CommonUtil.checkResult(num + 1, newNum));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--购买套餐，确认支付之后，该套餐购买数量+1");
        }
    }

    @Test(description = "接待管理--赠送套餐，确认支付之后，该套餐赠送数量+1")
    public void receptionManage_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //套餐购卖数量
            IScene packageFormPageScene = PackageFormPage.builder().packageName(EnumVP.ONE.getPackageName()).build();
            int num = CommonUtil.getIntField(jc.invokeApi(packageFormPageScene), 0, "give_number");
            //购买套餐
            util.receptionBuyFixedPackage(0);
            //确认支付
            util.makeSureBuyPackage(EnumVP.ONE.getPackageName());
            //购买后套餐购卖数量
            int newNum = CommonUtil.getIntField(jc.invokeApi(packageFormPageScene), 0, "give_number");
            CommonUtil.valueView(num, newNum);
            Preconditions.checkArgument(newNum == num + 1, "套餐购买列表" + CommonUtil.checkResult(num + 1, newNum));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--赠送套餐，确认支付之后，该套餐赠送数量+1");
        }
    }

    @Test(description = "接待管理--购买套餐，确认支付之后，【我的消息】+1")
    public void receptionManage_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //购买前消息列表数量
            user.loginApplet(appletUser);
            int listSize = util.getAppletMessageNum();
            user.login(administrator);
            //购买套餐
            util.receptionBuyFixedPackage(1);
            //确认支付
            util.makeSureBuyPackage(EnumVP.ONE.getPackageName());
            //购买后消息列表数量
            user.loginApplet(appletUser);
            int newListSize = util.getAppletMessageNum();
            CommonUtil.valueView(listSize, newListSize);
            Preconditions.checkArgument(newListSize == listSize + 1, "我的消息列表" + CommonUtil.checkResult(listSize + 1, newListSize));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--购买套餐，确认支付之后，【我的消息】+1");
        }
    }

    @Test(description = "接待管理--购买固定套餐，确认支付之后，【我的套餐】列表+1")
    public void receptionManage_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //购买前套餐列表数量
            user.loginApplet(appletUser);
            int listSize = util.getAppletPackageNum();
            user.login(administrator);
            //购买套餐
            util.receptionBuyFixedPackage(1);
            //确认支付
            util.makeSureBuyPackage(EnumVP.ONE.getPackageName());
            //购买后消息列表数量
            user.loginApplet(appletUser);
            int newListSize = util.getAppletPackageNum();
            CommonUtil.valueView(listSize, newListSize);
            Preconditions.checkArgument(newListSize == listSize + 1, "我的套餐列表" + CommonUtil.checkResult(listSize + 1, newListSize));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--购买固定套餐，确认支付之后，【我的套餐】列表+1");
        }
    }

    @Test(description = "接待管理--购买临时套餐，确认支付之后，【我的卡券】列表+1")
    public void receptionManage_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //购买前卡券列表数量
            user.loginApplet(appletUser);
            int listSize = util.getAppletVoucherNum();
            user.login(administrator);
            //购买套餐
            util.receptionBuyTemporaryPackage(1);
            //确认支付
            util.makeSureBuyPackage(EnumVP.TEMPORARY.getPackageName());
            //购买后消息列表数量
            user.loginApplet(appletUser);
            int newListSize = util.getAppletVoucherNum();
            CommonUtil.valueView(listSize, newListSize);
            Preconditions.checkArgument(newListSize == listSize + 1, "我的卡券列表" + CommonUtil.checkResult(listSize + 1, newListSize));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--购买临时套餐，确认支付之后，【我的卡券】列表+1");
        }
    }

    @Test(description = "接待管理--购买一个套餐，【小程序客户】消费频次+1&&【小程序客户】总消费+累计金额&&【售后客户】频次+1&&【售后客户】总消费+累计金额", enabled = false)
    public void receptionManage_data_10() {
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
                    .packageId(null).carType(EnumCarType.ALL_CAR.name()).selectNumber(1).packagePrice("100").expiryDate("30").remark("xxxxxxx")
                    .voucherType("CURRENT").type(1).subjectType("").subjectId(1L).build();
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

    @Test(description = "客户管理--【售后客户】列表中频次＝【接待管理】该客户已完成接待的次数", enabled = false)
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

    @Test(description = "客户管理--【售后客户】列表中 总消费＝维修记录中产值之和", enabled = false)
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

    @Test(description = "客户管理--【售后客户】的送修人姓名&&联系电话&&里程数=维修记录中最新及记录的姓名&&联系电话&&里程数", enabled = false)
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

    @Test(description = "客户管理--导入一次工单，【导入记录】+1")
    public void customerManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ImportPage.builder().build();
            int total = jc.invokeApi(scene).getInteger("total");
            String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/excel/服务单号已存在.xlsx";
            String message = jc.pcWorkOrder(filePath).getString("message");
            Assert.assertEquals(message, "success");
            int newTotal = jc.invokeApi(scene).getInteger("total");
            CommonUtil.valueView(total, newTotal);
            Preconditions.checkArgument(newTotal == total + 1, "导入前导入记录总数为：" + total + "导入后记录总数为：" + newTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("客户管理--导入一次工单，【导入记录】+1");
        }
    }

    @Test(description = "客户管理--小程序【我的爱车】新加一个爱车,售后客户+1")
    public void customerManager_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String carId = null;
        try {
            String platNumber = "皖H123456";
            user.login(marketing);
            List<Long> shopList = util.getShopIdList();
            user.login(administrator);
            AfterSaleCustomerPage.AfterSaleCustomerPageBuilder builder = AfterSaleCustomerPage.builder().customerPhone(marketing.getPhone());
            int customerNum = getCustomerNum(builder, shopList);
            //小程序添加爱车
            user.loginApplet(appletUser);
            carId = new JcFunction().appletAddCar(platNumber);
            //添加车辆后，客户列表数
            user.login(administrator);
            int newCustomerNum = getCustomerNum(builder, shopList);
            CommonUtil.valueView(customerNum, newCustomerNum);
            Preconditions.checkArgument(newCustomerNum == customerNum + 1, "添加爱车前售后客户列表数：" + customerNum + "添加爱车后售后客户列表数：" + newCustomerNum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            user.loginApplet(appletUser);
            jc.appletCarDelst(carId);
            saveData("客户管理--小程序【我的爱车】新加一个爱车,售后客户+1");
        }
    }

    private int getCustomerNum(AfterSaleCustomerPage.AfterSaleCustomerPageBuilder builder, List<Long> shopList) {
        List<JSONObject> list = new ArrayList<>();
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(1).size(size).build()).getJSONArray("list");
            list.addAll(array.stream().map(object -> (JSONObject) object).filter(object -> object.getLong("shop_id").equals(shopList.get(0)))
                    .collect(Collectors.toList()));
        }
        return list.size();
    }

    @Test(description = "客户管理--【小程序客户】对应的总金额=对应手机号的【售后客户】的维修记录的产值之和")
    public void customerManager_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = WechatCustomerPage.builder().size(size).build();
            JSONArray array = jc.invokeApi(scene).getJSONArray("list");
            List<WechatCustomerPageVO> customerInfo = array.stream().map(e -> (JSONObject) e).map(e -> JSON.parseObject(JSON.toJSONString(e), WechatCustomerPageVO.class)).collect(Collectors.toList());
            customerInfo.forEach(info -> {
                CommonUtil.valueView(info.getCustomerName());
                List<Double> priceList = new ArrayList<>();
                String phone = info.getCustomerPhone();
                Double price = info.getTotalPrice() == null ? 0 : info.getTotalPrice();
                AfterSaleCustomerPage.AfterSaleCustomerPageBuilder builder = AfterSaleCustomerPage.builder().customerPhone(phone);
                int total = jc.invokeApi(builder.build()).getInteger("total");
                for (int i = 1; i < total; i++) {
                    JSONArray list = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                    priceList.addAll(list.stream().map(e -> (JSONObject) e).map(e -> e.getDouble("total_price") == null ? 0 : e.getDouble("total_price")).collect(Collectors.toList()));
                }
                Double priceLitNum = priceList.stream().mapToDouble(e -> e).sum();
                CommonUtil.valueView(price, priceLitNum);
                Preconditions.checkArgument(price.equals(priceLitNum), info.getCustomerPhone() + "总金额：" + price + "产值之和：" + priceLitNum);
                CommonUtil.logger(info.getCustomerName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("客户管理--【小程序客户】对应的总金额=对应手机号的【售后客户】的维修记录的产值之和");
        }
    }

    @Test(description = "客户管理--【售后客列表户】中里程数＝【维修记录】中最新的一次里程数")
    public void customerManager_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AfterSaleCustomerPageVO> afterSaleInfo = new ArrayList<>();
            AfterSaleCustomerPage.AfterSaleCustomerPageBuilder builder = AfterSaleCustomerPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                afterSaleInfo.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSON.parseObject(JSON.toJSONString(e), AfterSaleCustomerPageVO.class)).collect(Collectors.toList()));
            }
            afterSaleInfo.forEach(e -> {
                CommonUtil.valueView(e.getRepairCustomerName(), e.getImportDate());
                int newMiles = e.getNewestMiles() == null ? 0 : e.getNewestMiles();
                int repairNewMiles = getMiles(e);
                CommonUtil.valueView(newMiles, repairNewMiles);
                Preconditions.checkArgument(newMiles == repairNewMiles, "售后客户列表最新里程数：" + newMiles + "维修记录最新里程数" + repairNewMiles);
                CommonUtil.logger(e.getRepairCustomerName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("客户管理--【售后客列表户】中里程数＝【维修记录】中最新的一次里程数");
        }
    }

    private Integer getMiles(AfterSaleCustomerPageVO customerInfo) {
        List<Integer> list = new ArrayList<>();
        AfterSaleCustomerRepairPage.AfterSaleCustomerRepairPageBuilder builder = AfterSaleCustomerRepairPage.builder().carId(String.valueOf(customerInfo.getCarId()))
                .shopId(String.valueOf(customerInfo.getShopId()));
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("repair_customer_phone").equals(customerInfo.getRepairCustomerPhone())).map(e -> e.getInteger("newest_miles"))
                    .collect(Collectors.toList()));
        }
        return list.size() == 0 ? 0 : list.get(0);
    }
}
