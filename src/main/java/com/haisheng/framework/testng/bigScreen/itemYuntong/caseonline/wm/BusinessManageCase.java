package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.wm;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.dataprovider.DataClass;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerCreateCustomerScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerCreatePotentialCustomerScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 业务管理测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class BusinessManageCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.YT_ONLINE_JD;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.YT_ALL_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //放入shopId
        commonConfig.setShopId(PRODUCE.getShopId()).setRoleId(ALL_AUTHORITY.getRoleId()).setProduct(PRODUCE.getAbbreviation());
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
        util.loginPc(ALL_AUTHORITY);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        logger.logCaseStart(caseResult.getCaseName());
    }

    @Test(description = "创建一个潜客，销售客户列表中手机号不存在，销售客户列表+1&客户类型为【潜客】")
    public void saleCustomerManager_data_1() {
        try {
            IScene scene = PreSaleCustomerPageScene.builder().build();
            int total = scene.execute(visitor).getInteger("total");
            String phone = util.getNotExistPhone();
            PreSaleCustomerCreatePotentialCustomerScene.builder().customerType("PERSON").customerName("燕小六")
                    .customerPhone(phone).sex("0").salesId(util.getSaleId()).shopId(Long.parseLong(util.getReceptionShopId()))
                    .carStyleId(Long.parseLong(util.getCarStyleId())).carModelId(Long.parseLong(util.getCarModelId())).build().execute(visitor);
            JSONObject response = scene.execute(visitor);
            int newTotal = response.getInteger("total");
            String customerTypeName = response.getJSONArray("list").getJSONObject(0).getString("customer_type_name");
            Preconditions.checkArgument(newTotal == total + 1, "创建潜客之前为：" + total + "创建潜客之后：" + newTotal);
            Preconditions.checkArgument(customerTypeName.equals("潜在客户"), "客户类型为：" + customerTypeName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("创建一个潜客，销售客户列表中手机号不存在，销售客户列表+1&客户类型为【潜客】");
        }
    }

    @Test(description = "创建一个潜客，销售客户列表中手机号存在，销售客户列表+0")
    public void saleCustomerManager_data_2() {
        try {
            IScene scene = PreSaleCustomerPageScene.builder().build();
            JSONObject response = scene.execute(visitor);
            int total = response.getInteger("total");
            String phone = response.getJSONArray("list").getJSONObject(0).getString("customer_phone");
            PreSaleCustomerCreatePotentialCustomerScene.builder().customerType("PERSON").customerName("燕小六")
                    .customerPhone(phone).sex("0").salesId(util.getSaleId()).shopId(Long.parseLong(util.getReceptionShopId()))
                    .carStyleId(Long.parseLong(util.getCarStyleId())).carModelId(Long.parseLong(util.getCarModelId())).build().execute(visitor, false);
            JSONObject response1 = scene.execute(visitor);
            int newTotal = response1.getInteger("total");
            Preconditions.checkArgument(newTotal == total, "创建潜客之前为：" + total + "创建潜客之后：" + newTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("创建一个潜客，销售客户列表中手机号存在，销售客户列表+0");
        }
    }

    @Test(dataProvider = "createCustomerAbnormalParam", dataProviderClass = DataClass.class, description = "创建潜客异常情况")
    public void saleCustomerManager_system_1(String field, Object value, String err) {
        try {
            IScene scene = PreSaleCustomerCreatePotentialCustomerScene.builder().customerType("PERSON").customerName("燕小六")
                    .customerPhone(util.getNotExistPhone()).sex("0").salesId(util.getSaleId()).shopId(Long.parseLong(util.getReceptionShopId()))
                    .carStyleId(Long.parseLong(util.getCarStyleId())).carModelId(Long.parseLong(util.getCarModelId())).build().modify(field, value);
            String message = scene.visitor(visitor).getResponse().getMessage();
            CommonUtil.checkResult("创建潜客：" + field, value, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("创建潜客异常情况");
        }
    }

    @Test(dataProvider = "createCustomerOrderAbnormalParam", dataProviderClass = DataClass.class, description = "创建成交记录异常情况")
    public void saleCustomerManager_system_2(String field, Object value, String err) {
        try {
            String vin = util.getExistVin();
            IScene scene = PreSaleCustomerCreateCustomerScene.builder().customerPhone(util.getNotExistPhone()).customerName("燕小六")
                    .sex("1").customerType("PERSON").shopId(Long.parseLong(util.getReceptionShopId()))
                    .carStyleId(Long.parseLong(util.getCarStyleId())).carModelId(Long.parseLong(util.getCarModelId())).salesId(util.getSaleId())
                    .purchaseCarDate(DateTimeUtil.addDayFormat(new Date(), -10)).vehicleChassisCode(vin).build().modify(field, value);
            String message = scene.visitor(visitor).getResponse().getMessage();
            CommonUtil.checkResult("创建成交记录：" + field, value, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("创建成交记录异常情况");
        }
    }
}
