package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.wm;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.presalesreception.AppPreSalesReceptionPageBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppCustomerRemarkV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionCreateScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerBuyCarPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerCreateCustomerScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanagev4.PreSaleCustomerInfoBuyCarRecordScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanagev4.PreSaleCustomerInfoRemarkRecordScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanagev4.PreSaleCustomerInfoScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.BuyCarScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.FinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * app接待测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class AppReceptionCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.YT_ONLINE_JD;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.YT_ALL_ONLINE;
    private static AppPreSalesReceptionPageBean preSalesReceptionPage;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //放入shopId
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = ALL_AUTHORITY.getReceptionShopId();
        commonConfig.roleId = ALL_AUTHORITY.getRoleId();
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
    }

    private void initAppPreSalesReceptionPageBean() {
        util.loginApp(ALL_AUTHORITY);
        preSalesReceptionPage = util.getAppPreSalesReceptionPageList().stream().filter(e -> e.getCustomerName().equals("自动化创建的接待人")).findFirst().orElse(null);
        if (preSalesReceptionPage == null) {
            logger.info("不存在接待人，需要创建");
            AppPreSalesReceptionCreateScene.builder().customerName("自动化创建的接待人").customerPhone(EnumAppletToken.JC_GLY_ONLINE.getPhone()).sexId("1").intentionCarModelId(util.getCarModelId()).estimateBuyCarTime("2100-07-12").build().invoke(visitor);
            preSalesReceptionPage = util.getAppPreSalesReceptionPageList().stream().filter(e -> e.getCustomerName().equals("自动化创建的接待人")).findFirst().orElse(null);
        }
        util.loginPc(ALL_AUTHORITY);
    }

    @Test(description = "APP接待时产生新的节点，节点名称为销售创建")
    public void saleCustomerManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            initAppPreSalesReceptionPageBean();
            IScene preSalesReceptionPageScene = PreSalesReceptionPageScene.builder().customerId(String.valueOf(preSalesReceptionPage.getCustomerId())).build();
            JSONObject response = util.toFirstJavaObject(preSalesReceptionPageScene, JSONObject.class);
            String receptionTypeName = response.getString("reception_type_name");
            Preconditions.checkArgument(receptionTypeName.equals("销售创建"), "APP接待时产生新的节点，节点名称为：" + receptionTypeName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP接待时产生新的节点，节点名称为销售创建");
        }
    }

    @Test(description = "APP接待时填写备注，备注记录+1")
    public void saleCustomerManager_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            initAppPreSalesReceptionPageBean();
            IScene scene = PreSaleCustomerInfoRemarkRecordScene.builder().customerId(String.valueOf(preSalesReceptionPage.getCustomerId())).build();
            int total = scene.invoke(visitor).getInteger("total");
            util.loginApp(ALL_AUTHORITY);
            AppCustomerRemarkV4Scene.builder().id(String.valueOf(preSalesReceptionPage.getId())).customerId(String.valueOf(preSalesReceptionPage.getCustomerId())).remark(EnumDesc.DESC_BETWEEN_200_300.getDesc())
                    .shopId(util.getReceptionShopId()).build().invoke(visitor);
            util.loginPc(ALL_AUTHORITY);
            int newTotal = scene.invoke(visitor).getInteger("total");
            String remarkContent = util.toFirstJavaObject(scene, JSONObject.class).getString("remark_content");
            Preconditions.checkArgument(newTotal == total + 1, "APP接待时填写备注前pc备注条数：" + total + " 填写备注后pc备注条数：" + newTotal);
            Preconditions.checkArgument(EnumDesc.DESC_BETWEEN_200_300.getDesc().equals(remarkContent), "APP接待时填写备注后pc备注内容预期为：" + EnumDesc.DESC_BETWEEN_200_300.getDesc() + " 实际为：" + remarkContent);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP接待时填写备注，备注记录+1");
        }
    }

    @Test(description = "APP接待时购买车辆，购车记录+1")
    public void saleCustomerManager_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            initAppPreSalesReceptionPageBean();
            IScene preSaleCustomerBuyCarPageScene = PreSaleCustomerBuyCarPageScene.builder().build();
            int buyCarTotal = preSaleCustomerBuyCarPageScene.invoke(visitor).getInteger("total");
            IScene preSaleCustomerInfoBuyCarRecordScene = PreSaleCustomerInfoBuyCarRecordScene.builder().customerId(preSalesReceptionPage.getCustomerId()).build();
            int total = preSaleCustomerInfoBuyCarRecordScene.invoke(visitor).getInteger("total");
            String vin = util.getNotExistVin();
            //买车
            BuyCarScene.builder().carModel(Long.parseLong(util.getCarModelId())).carStyle(Long.parseLong(util.getCarStyleId())).vin(vin).id(preSalesReceptionPage.getId()).shopId(Long.parseLong(util.getReceptionShopId())).build().invoke(visitor);
            JSONObject response = preSaleCustomerInfoBuyCarRecordScene.invoke(visitor);
            int newTotal = response.getInteger("total");
            int newBuyCarTotal = preSaleCustomerBuyCarPageScene.invoke(visitor).getInteger("total");
            String vehicleChassisCode = response.getJSONArray("list").getJSONObject(0).getString("vehicle_chassis_code");
            Preconditions.checkArgument(newTotal == total + 1, "APP接待时购买车辆后，购车记录预期为：" + (total + 1) + " 实际为：" + newTotal);
            Preconditions.checkArgument(vehicleChassisCode.equals(vin), "APP接待时购买车辆后，底盘号预期为：" + vin + " 实际为：" + vehicleChassisCode);
            Preconditions.checkArgument(newBuyCarTotal == buyCarTotal + 1, "APP接待时购买车辆后，成交记录页预期为：" + (buyCarTotal + 1) + " 实际为：" + newBuyCarTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP接待时购买车辆，购车记录+1");
        }
    }

    @Test(description = "PC新建成交记录，购车记录+1")
    public void saleCustomerManager_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            initAppPreSalesReceptionPageBean();
            IScene scene = PreSaleCustomerInfoBuyCarRecordScene.builder().customerId(preSalesReceptionPage.getCustomerId()).build();
            int total = scene.invoke(visitor).getInteger("total");
            String vin = util.getNotExistVin();
            //新建成交记录
            PreSaleCustomerCreateCustomerScene.builder().customerPhone(preSalesReceptionPage.getCustomerPhone()).customerName(preSalesReceptionPage.getCustomerName())
                    .sex("1").customerType("PERSON").shopId(Long.parseLong(util.getReceptionShopId()))
                    .carStyleId(Long.parseLong(util.getCarStyleId())).carModelId(Long.parseLong(util.getCarModelId())).salesId(util.getSaleId())
                    .purchaseCarDate(DateTimeUtil.addDayFormat(new Date(), -10)).vehicleChassisCode(vin).build().invoke(visitor);
            JSONObject response = scene.invoke(visitor);
            int newTotal = response.getInteger("total");
            String vehicleChassisCode = response.getJSONArray("list").getJSONObject(0).getString("vehicle_chassis_code");
            Preconditions.checkArgument(newTotal == total + 1, "新建成交记录后，购车记录预期为：" + (total + 1) + " 实际为：" + newTotal);
            Preconditions.checkArgument(vehicleChassisCode.equals(vin), "新建成交记录后，底盘号预期为：" + vin + " 实际为：" + vehicleChassisCode);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("PC新建成交记录，购车记录+1");
        }
    }

    @Test(description = "接待客户一次，更新最近到店时间为当前接待时间")
    public void saleCustomerManager_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            initAppPreSalesReceptionPageBean();
            IScene scene = PreSaleCustomerInfoScene.builder().customerId(preSalesReceptionPage.getCustomerId()).shopId(Long.parseLong(util.getReceptionShopId())).build();
            String lastToShopDate = scene.invoke(visitor).getString("last_to_shop_date");
            String date = DateTimeUtil.getFormat(new Date());
            Preconditions.checkArgument(lastToShopDate.equals(date), "更新最近到店时间预期为：" + date + " 实际为：" + lastToShopDate);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待客户一次，更新最近到店时间为当前接待时间");
        }
    }

    @AfterClass(description = "完成此条接待记录")
    @Test(description = "完成接待，更新接待时间")
    public void saleCustomerManager_data_6() {
        logger.info(caseResult.getCaseName());
        try {
            initAppPreSalesReceptionPageBean();
            String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
            FinishReceptionScene.builder().id(preSalesReceptionPage.getId()).shopId(Long.parseLong(util.getReceptionShopId())).build().invoke(visitor);
            IScene scene = PreSalesReceptionPageScene.builder().phone(preSalesReceptionPage.getCustomerPhone()).build();
            JSONObject response = util.toFirstJavaObject(scene, JSONObject.class);
            Preconditions.checkArgument(response.getString("reception_end_time").contains(date), "预期完成接待时间：" + date + " 实际完成接待时间：" + response.getString("reception_end_time"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("完成接待，更新接待时间");
        }
    }
}
