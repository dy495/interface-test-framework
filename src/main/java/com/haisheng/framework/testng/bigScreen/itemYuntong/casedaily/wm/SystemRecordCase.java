package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.wm;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.record.ImportPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 系统记录case
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class SystemRecordCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.YT_DAILY_JD;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.YT_DAILY_YS;
    public VisitorProxy visitor = new VisitorProxy(product);
    public SceneUtil util = new SceneUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //放入shopId
        commonConfig.setShopId(product.getShopId()).setRoleId(ALL_AUTHORITY.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
        util.loginPc(ALL_AUTHORITY);
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

    @Test(description = "导入成功条数=导入条数-失败条数")
    public void importRecord_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ImportPageScene.builder().build();
            List<JSONObject> list = util.toJavaObjectList(scene, JSONObject.class);
            list.forEach(e -> Preconditions.checkArgument(e.getInteger("import_num") == e.getInteger("success_num") + e.getInteger("failure_num"), "导入条数：" + e.getInteger("import_num") + " 成功+失败条数：" + e.getInteger("success_num") + e.getInteger("failure_num")));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("导入成功条数=导入条数-失败条数");
        }
    }

    @Test(description = "导入成功条数<=导入条数")
    public void importRecord_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ImportPageScene.builder().build();
            List<JSONObject> list = util.toJavaObjectList(scene, JSONObject.class);
            list.forEach(e -> Preconditions.checkArgument(e.getInteger("success_num") <= e.getInteger("import_num"), "导入成功条数：" + e.getInteger("success_num") + " 一共导入条数：" + e.getInteger("import_num")));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("导入成功条数<=导入条数");
        }
    }

    @Test(description = "导入失败条数<=导入条数")
    public void importRecord_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ImportPageScene.builder().build();
            List<JSONObject> list = util.toJavaObjectList(scene, JSONObject.class);
            list.forEach(e -> Preconditions.checkArgument(e.getInteger("failure_num") <= e.getInteger("import_num"), "导入失败条数：" + e.getInteger("failure_num") + " 一共导入条数：" + e.getInteger("import_num")));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("导入失败条数<=导入条数");
        }
    }
}
