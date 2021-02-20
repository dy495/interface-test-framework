package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.AfterSaleCustomerPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.WechatCustomerPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.Import.WorkOrderScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager.AfterSaleCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager.RepairPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager.WechatCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.recordimport.ImportPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务管理测试用例
 */
public class BusinessManageCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce product = EnumTestProduce.JIAOCHEN_DAILY;
    private static final EnumAccount ADMINISTRATOR = EnumAccount.WINSENSE_LAB_DAILY;
    //访问者
    public Visitor visitor = new Visitor(product);
    //登录工具
    public UserUtil user = new UserUtil(visitor);
    //封装方法
    public SupporterUtil util = new SupporterUtil(visitor);


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.product = product.getAbbreviation();
        commonConfig.referer = product.getReferer();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = product.getShopId();
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
        user.loginPc(ADMINISTRATOR);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test(description = "客户管理--导入一次工单，【导入记录】+1")
    public void customerManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene importPageScene = ImportPageScene.builder().build();
            int total = visitor.invokeApi(importPageScene).getInteger("total");
            String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/excel/服务单号已存在.xlsx";
            IScene workOrderScene = WorkOrderScene.builder().filePath(filePath).build();
            String message = visitor.invokeApi(workOrderScene, false).getString("message");
            Assert.assertEquals(message, "success");
            int newTotal = visitor.invokeApi(importPageScene).getInteger("total");
            CommonUtil.checkResult("导入后导入记录总数", total + 1, newTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("客户管理--导入一次工单，【导入记录】+1");
        }
    }

    @Test(description = "客户管理--【小程序客户】对应的总金额=对应手机号的【售后客户】的维修记录的产值之和")
    public void customerManager_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene wechatCustomerPageScene = WechatCustomerPageScene.builder().build();
            List<WechatCustomerPage> wechatCustomerPageList = util.collectBean(wechatCustomerPageScene, WechatCustomerPage.class);
            wechatCustomerPageList.forEach(wechatCustomerPage -> {
                CommonUtil.valueView(wechatCustomerPage.getCustomerName());
                String phone = wechatCustomerPage.getCustomerPhone();
                Double price = wechatCustomerPage.getTotalPrice() == null ? 0 : wechatCustomerPage.getTotalPrice();
                IScene afterSaleCustomerPageScene = AfterSaleCustomerPageScene.builder().customerPhone(phone).build();
                List<JSONObject> jsonObjectList = util.collectBean(afterSaleCustomerPageScene, JSONObject.class);
                Double priceLitSum = jsonObjectList.stream().map(e -> e.getDouble("total_price") == null ? 0 : e.getDouble("total_price")).collect(Collectors.toList()).stream().mapToDouble(e -> e).sum();
                CommonUtil.checkResultPlus(wechatCustomerPage.getCustomerPhone() + " 总金额", price, "产值之和", priceLitSum);
                CommonUtil.logger(wechatCustomerPage.getCustomerName());
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
            IScene afterSaleCustomerPageScene = AfterSaleCustomerPageScene.builder().build();
            List<AfterSaleCustomerPage> afterSaleCustomerPageList = util.collectBean(afterSaleCustomerPageScene, AfterSaleCustomerPage.class);
            afterSaleCustomerPageList.forEach(afterSaleCustomerPage -> {
                int miles = afterSaleCustomerPage.getNewestMiles() == null ? 0 : afterSaleCustomerPage.getNewestMiles();
                int repairNewMiles = getMiles(afterSaleCustomerPage);
                CommonUtil.checkResultPlus("售后客户列表里程数", miles, "维修记录最新里程数", repairNewMiles);
                CommonUtil.logger(afterSaleCustomerPage.getRepairCustomerName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("客户管理--【售后客列表户】中里程数＝【维修记录】中最新的一次里程数");
        }
    }

    private Integer getMiles(AfterSaleCustomerPage customerInfo) {
        IScene repairPageScene = RepairPageScene.builder().carId(String.valueOf(customerInfo.getCarId())).shopId(String.valueOf(customerInfo.getShopId())).build();
        return util.collectBean(repairPageScene, JSONObject.class).stream().filter(jsonObject -> jsonObject.getString("repair_customer_phone").equals(customerInfo.getRepairCustomerPhone())).map(jsonObject -> jsonObject.getInteger("newest_miles")).findFirst().orElse(0);
    }
}
