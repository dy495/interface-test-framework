package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.wm;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppCustomerRemarkV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionCreateScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanagev4.PreSaleCustomerInfoRemarkRecordScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * app接待测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class AppReceptionCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.YT_DAILY_SSO;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.YT_RECEPTION_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);
    private static final Map<String, String> map = new HashMap<>();
    private static String receptionId;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
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
        if (receptionId == null) {
            util.loginApp(ALL_AUTHORITY);
            visitor.setProduct(EnumTestProduce.YT_DAILY_CAR);
            AppPreSalesReceptionCreateScene.builder().customerName("自动化创建的接待人").customerPhone("15321527989")
                    .sexId("1").intentionCarModelId(util.getCarModelId()).estimateBuyCarTime("2100-07-12").build().invoke(visitor);
            IScene scene = AppPreSalesReceptionPageScene.builder().build();
            JSONObject response = util.toJavaObject(scene, JSONObject.class, "customer_name", "自动化创建的接待人");
            String customerId = response.getString("customer_id");
            receptionId = response.getString("id");
            map.put("customer_id", customerId);
            map.put("id", receptionId);
        }
        util.loginPc(ALL_AUTHORITY);
        visitor.setProduct(EnumTestProduce.YT_DAILY_CAR);
    }

    @Test(description = "app接待时产生新的节点，节点名称为销售创建")
    public void saleCustomerManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene preSalesReceptionPageScene = PreSalesReceptionPageScene.builder().customerId(map.get("customer_id")).build();
            JSONObject response = util.toFirstJavaObject(preSalesReceptionPageScene, JSONObject.class);
            String receptionTypeName = response.getString("reception_type_name");
            Preconditions.checkArgument(receptionTypeName.equals("销售创建"), "app接待时产生新的节点，节点名称为：" + receptionTypeName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待时产生新的节点，节点名称为销售创建");
        }
    }

    @Test(description = "app接待时产生新的节点，节点名称为销售创建")
    public void saleCustomerManager_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = PreSaleCustomerInfoRemarkRecordScene.builder().customerId(map.get("customer_id")).build();
            int total = scene.invoke(visitor).getInteger("total");
            util.loginApp(ALL_AUTHORITY);
            AppCustomerRemarkV4Scene.builder().id(map.get("id")).customerId(map.get("customer_id")).remark(EnumDesc.DESC_BETWEEN_200_300.getDesc())
                    .shopId(util.getReceptionShopId()).build().invoke(visitor);
            util.loginPc(ALL_AUTHORITY);
            int newTotal = scene.invoke(visitor).getInteger("total");
            String remarkContent = util.toFirstJavaObject(scene, JSONObject.class).getString("remark_content");
            Preconditions.checkArgument(newTotal == total + 1, "app接待时填写备注前pc备注条数：" + total + " 填写备注后pc备注条数：" + newTotal);
            Preconditions.checkArgument(EnumDesc.DESC_BETWEEN_200_300.getDesc().equals(remarkContent), "app接待时填写备注后pc备注内容预期为：" + EnumDesc.DESC_BETWEEN_200_300.getDesc() + " 实际为：" + remarkContent);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待时产生新的节点，节点名称为销售创建");
        }
    }


}
