package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.EvaluateV4ConfigDetailScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.EvaluateV4ConfigSubmitScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * 服务管理测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class SeverManageCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.YT_ONLINE_JD;
    private static final EnumAccount ACCOUNT = EnumAccount.YT_ONLINE_YS;
    private final VisitorProxy visitor = new VisitorProxy(PRODUCE);
    private final SceneUtil util = new SceneUtil(visitor);

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
        commonConfig.setShopId(util.getReceptionShopId()).setRoleId(ACCOUNT.getRoleId()).setProduct(PRODUCE.getAbbreviation());
        beforeClassInit(commonConfig);
        util.loginPc(ACCOUNT);
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

    @Test(description = "创建一个题目，大类下题目+1")
    public void evaluateManager_data_1() {
        try {
            JSONArray linksA = util.getSubmitLink(false);
            EvaluateV4ConfigSubmitScene.builder().links(linksA).build().visitor(visitor).execute();
            IScene scene = EvaluateV4ConfigDetailScene.builder().build();
            int itemCount = scene.visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e)
                    .mapToInt(e -> e.getJSONArray("items").size()).sum();
            JSONArray linksB = util.getSubmitLink(true);
            EvaluateV4ConfigSubmitScene.builder().links(linksB).build().visitor(visitor).execute();
            int newItemCount = scene.visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e)
                    .mapToInt(e -> e.getJSONArray("items").size()).sum();
            Preconditions.checkArgument(newItemCount == itemCount + 1, "增加题目之前题目数量：" + itemCount + " 增加题目之后题目数量：" + newItemCount);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("创建一个题目，大类下题目+1");
        }
    }

    @Test(description = "删除一个题目，大类下题目-1", dependsOnMethods = "evaluateManager_data_1")
    public void evaluateManager_data_2() {
        try {
            IScene scene = EvaluateV4ConfigDetailScene.builder().build();
            int itemCount = scene.visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e)
                    .mapToInt(e -> e.getJSONArray("items").size()).sum();
            JSONArray links = util.getSubmitLink(false);
            EvaluateV4ConfigSubmitScene.builder().links(links).build().visitor(visitor).execute();
            int newItemCount = scene.visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e)
                    .mapToInt(e -> e.getJSONArray("items").size()).sum();
            Preconditions.checkArgument(newItemCount == itemCount - 1, "删除题目之前题目数量：" + itemCount + " 删除题目之后题目数量：" + newItemCount);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("删除一个题目，大类下题目-1");
        }
    }
}
