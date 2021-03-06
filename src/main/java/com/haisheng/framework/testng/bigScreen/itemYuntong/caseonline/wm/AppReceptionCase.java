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
 * app??????????????????
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class AppReceptionCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.YT_ONLINE_JD;
    private static final EnumAccount ACCOUNT = EnumAccount.YT_ONLINE_LXQ;
    private static AppPreSalesReceptionPageBean preSalesReceptionPage;
    private final VisitorProxy visitor = new VisitorProxy(PRODUCE);
    private final SceneUtil util = new SceneUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //??????checklist???????????????
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //??????jenkins-job???????????????
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //??????shopId
        commonConfig.setShopId(ACCOUNT.getReceptionShopId()).setRoleId(ACCOUNT.getRoleId()).setProduct(PRODUCE.getAbbreviation());
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
        logger.logCaseStart(caseResult.getCaseName());
    }

    private void initAppPreSalesReceptionPageBean() {
        util.loginApp(ACCOUNT);
        preSalesReceptionPage = util.getAppPreSalesReceptionPageList().stream().filter(e -> e.getCustomerName().equals("???????????????????????????")).findFirst().orElse(null);
        if (preSalesReceptionPage == null) {
            logger.info("?????????????????????????????????");
            AppPreSalesReceptionCreateScene.builder().customerName("???????????????????????????").customerPhone(EnumAppletToken.JC_GLY_ONLINE.getPhone()).sexId("1").intentionCarModelId(util.getCarModelId()).estimateBuyCarTime("2100-07-12").build().visitor(visitor).execute();
            preSalesReceptionPage = util.getAppPreSalesReceptionPageList().stream().filter(e -> e.getCustomerName().equals("???????????????????????????")).findFirst().orElse(null);
        }
        util.loginPc(ACCOUNT);
    }

    @Test(description = "APP?????????????????????????????????????????????????????????")
    public void saleCustomerManager_data_1() {
        try {
            initAppPreSalesReceptionPageBean();
            IScene preSalesReceptionPageScene = PreSalesReceptionPageScene.builder().customerId(String.valueOf(preSalesReceptionPage.getCustomerId())).build();
            JSONObject response = util.toFirstJavaObject(preSalesReceptionPageScene, JSONObject.class);
            String receptionTypeName = response.getString("reception_type_name");
            Preconditions.checkArgument(receptionTypeName.equals("????????????"), "APP????????????????????????????????????????????????" + receptionTypeName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP?????????????????????????????????????????????????????????");
        }
    }

    @Test(description = "APP????????????????????????????????????+1")
    public void saleCustomerManager_data_2() {
        try {
            initAppPreSalesReceptionPageBean();
            String customerId = String.valueOf(preSalesReceptionPage.getCustomerId());
            String id = String.valueOf(preSalesReceptionPage.getId());
            IScene scene = PreSaleCustomerInfoRemarkRecordScene.builder().customerId(customerId).build();
            int total = scene.visitor(visitor).execute().getInteger("total");
            util.loginApp(ACCOUNT);
            AppCustomerRemarkV4Scene.builder().id(id).customerId(customerId).remark(EnumDesc.DESC_BETWEEN_200_300.getDesc()).shopId(util.getReceptionShopId()).build().visitor(visitor).execute();
            util.loginPc(ACCOUNT);
            int newTotal = scene.visitor(visitor).execute().getInteger("total");
            String remarkContent = util.toFirstJavaObject(scene, JSONObject.class).getString("remark_content");
            Preconditions.checkArgument(newTotal == total + 1, "APP????????????????????????pc???????????????" + total + " ???????????????pc???????????????" + newTotal);
            Preconditions.checkArgument(EnumDesc.DESC_BETWEEN_200_300.getDesc().equals(remarkContent), "APP????????????????????????pc????????????????????????" + EnumDesc.DESC_BETWEEN_200_300.getDesc() + " ????????????" + remarkContent);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP????????????????????????????????????+1");
        }
    }

    @Test(description = "APP????????????????????????????????????+1")
    public void saleCustomerManager_data_3() {
        try {
            initAppPreSalesReceptionPageBean();
            IScene preSaleCustomerBuyCarPageScene = PreSaleCustomerBuyCarPageScene.builder().build();
            int buyCarTotal = preSaleCustomerBuyCarPageScene.visitor(visitor).execute().getInteger("total");
            IScene preSaleCustomerInfoBuyCarRecordScene = PreSaleCustomerInfoBuyCarRecordScene.builder().customerId(preSalesReceptionPage.getCustomerId()).build();
            int total = preSaleCustomerInfoBuyCarRecordScene.visitor(visitor).execute().getInteger("total");
            String vin = util.getNotExistVin();
            //??????
            BuyCarScene.builder().carModel(Long.parseLong(util.getCarModelId())).carStyle(Long.parseLong(util.getCarStyleId())).vin(vin).id(preSalesReceptionPage.getId()).shopId(Long.parseLong(util.getReceptionShopId())).build().visitor(visitor).execute();
            JSONObject response = preSaleCustomerInfoBuyCarRecordScene.visitor(visitor).execute();
            int newTotal = response.getInteger("total");
            int newBuyCarTotal = preSaleCustomerBuyCarPageScene.visitor(visitor).execute().getInteger("total");
            String vehicleChassisCode = response.getJSONArray("list").getJSONObject(0).getString("vehicle_chassis_code");
            Preconditions.checkArgument(newTotal == total + 1, "APP???????????????????????????????????????????????????" + (total + 1) + " ????????????" + newTotal);
            Preconditions.checkArgument(vehicleChassisCode.equals(vin), "APP????????????????????????????????????????????????" + vin + " ????????????" + vehicleChassisCode);
            Preconditions.checkArgument(newBuyCarTotal == buyCarTotal + 1, "APP??????????????????????????????????????????????????????" + (buyCarTotal + 1) + " ????????????" + newBuyCarTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP????????????????????????????????????+1");
        }
    }

    @Test(description = "PC?????????????????????????????????+1")
    public void saleCustomerManager_data_4() {
        try {
            initAppPreSalesReceptionPageBean();
            IScene scene = PreSaleCustomerInfoBuyCarRecordScene.builder().customerId(preSalesReceptionPage.getCustomerId()).build();
            int total = scene.visitor(visitor).execute().getInteger("total");
            String vin = util.getNotExistVin();
            //??????????????????
            PreSaleCustomerCreateCustomerScene.builder().customerPhone(preSalesReceptionPage.getCustomerPhone()).customerName(preSalesReceptionPage.getCustomerName())
                    .sex("1").customerType("PERSON").shopId(Long.parseLong(util.getReceptionShopId()))
                    .carStyleId(Long.parseLong(util.getCarStyleId())).carModelId(Long.parseLong(util.getCarModelId())).salesId(util.getSaleId())
                    .purchaseCarDate(DateTimeUtil.addDayFormat(new Date(), -10)).vehicleChassisCode(vin).build().visitor(visitor).execute();
            JSONObject response = scene.visitor(visitor).execute();
            int newTotal = response.getInteger("total");
            String vehicleChassisCode = response.getJSONArray("list").getJSONObject(0).getString("vehicle_chassis_code");
            Preconditions.checkArgument(newTotal == total + 1, "????????????????????????????????????????????????" + (total + 1) + " ????????????" + newTotal);
            Preconditions.checkArgument(vehicleChassisCode.equals(vin), "?????????????????????????????????????????????" + vin + " ????????????" + vehicleChassisCode);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????+1");
        }
    }

    @Test(description = "??????????????????????????????????????????????????????????????????")
    public void saleCustomerManager_data_5() {
        try {
            initAppPreSalesReceptionPageBean();
            IScene scene = PreSaleCustomerInfoScene.builder().customerId(preSalesReceptionPage.getCustomerId()).shopId(Long.parseLong(util.getReceptionShopId())).build();
            String lastToShopDate = scene.visitor(visitor).execute().getString("last_to_shop_date");
            String date = DateTimeUtil.getFormat(new Date());
            Preconditions.checkArgument(lastToShopDate.equals(date), "????????????????????????????????????" + date + " ????????????" + lastToShopDate);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("??????????????????????????????????????????????????????????????????");
        }
    }

    @AfterClass(description = "????????????????????????")
    @Test(description = "?????????????????????????????????")
    public void saleCustomerManager_data_6() {
        try {
            initAppPreSalesReceptionPageBean();
            String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
            FinishReceptionScene.builder().id(preSalesReceptionPage.getId()).shopId(Long.parseLong(util.getReceptionShopId())).build().visitor(visitor).execute();
            IScene scene = PreSalesReceptionPageScene.builder().phone(preSalesReceptionPage.getCustomerPhone()).build();
            JSONObject response = util.toFirstJavaObject(scene, JSONObject.class);
            Preconditions.checkArgument(response.getString("reception_end_time").contains(date), "???????????????????????????" + date + " ???????????????????????????" + response.getString("reception_end_time"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("?????????????????????????????????");
        }
    }
}
