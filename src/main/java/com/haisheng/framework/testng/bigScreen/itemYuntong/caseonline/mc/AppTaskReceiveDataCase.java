package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.mc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.AppFlowUp.AppFlowUpPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.AppFlowUp.AppFlowUpRemarkScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.homepagev4.AppTodayTaskScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppFinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesRecpEvaluateOpt;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesRecpEvaluateSubmit;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongInfo;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;


public class AppTaskReceiveDataCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.YT_ONLINE_JD; // 管理页—-首页
    private static final EnumAccount YT_RECEPTION = EnumAccount.YT_RECEPTION_ONLINE_MC; // 全部权限账号 【运通】
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);   // 产品类放到代理类中（通过代理类发请求）
    public SceneUtil util = new SceneUtil(visitor);
    public YunTongInfo info = new YunTongInfo();
    CommonConfig commonConfig = new CommonConfig();    // 配置类初始化

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = "孟辰";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(YT_RECEPTION.getReceptionShopId()).setRoleId(YT_RECEPTION.getRoleId()).setProduct(PRODUCE.getAbbreviation());
        beforeClassInit(commonConfig);  // 配置请求头
        util.loginPc(YT_RECEPTION);   //登录
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

    @Test
    public void flowUp1() {
        try {
            Integer total1 = AppFlowUpPageScene.builder().size(10).build().invoke(visitor).getInteger("total");
            Map<String, String> customer = util.createCustomerCommon("自动差评者", "1", "150" + CommonUtil.getRandom(8), util.mcCarId(), "2033-12-20");
            AppFinishReceptionScene.builder().id(customer.get("id")).shopId(customer.get("shopId")).build().invoke(visitor);
            commonConfig.setShopId(null);
            commonConfig.setRoleId(null);
            JSONArray evaluateInfoList = new JSONArray();
            PreSalesRecpEvaluateOpt.builder().reception_id(Long.parseLong(customer.get("id"))).build().invoke(visitor).getJSONArray("list").stream().map(j -> (JSONObject) j).map(json -> json.getInteger("id")).forEach(e -> evaluateInfoList.add(lowEvaluate(e)));
            String message = PreSalesRecpEvaluateSubmit.builder().evaluate_info_list(evaluateInfoList).reception_id(Long.parseLong(customer.get("id"))).build().invoke(visitor, false).getString("message");
            if (Objects.equals(message, "success")) {
                commonConfig.setShopId(YT_RECEPTION.getReceptionShopId());
                commonConfig.setRoleId(YT_RECEPTION.getRoleId());
                Integer total2 = AppFlowUpPageScene.builder().size(10).build().invoke(visitor).getInteger("total");
                Preconditions.checkArgument(total2 == total1 + 1, "app跟进列表总数：" + total1 + "销售接待差评，app跟进列表总数：" + total2);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售接待差评,跟进+1");
        }

    }

    private JSONObject lowEvaluate(int id) {
        JSONObject o = new JSONObject();
        o.put("id", id);
        o.put("score", 1);
        return o;
    }

    @Test
    public void flowUp2() {
        try {
            String[] follows1 = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_follow").split("/");
            Integer flowUpId = AppFlowUpPageScene.builder().size(10).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getInteger("id");
            AppFlowUpRemarkScene.builder().followId(flowUpId).remark("自动完成跟进备注。。。1azCVB").build().invoke(visitor);
            String[] follows2 = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_follow").split("/");
            int followZi1 = Integer.parseInt(follows1[0]);
            int followZi2 = Integer.parseInt(follows2[0]);
            int followMu1 = Integer.parseInt(follows1[1]);
            int followMu2 = Integer.parseInt(follows2[1]);
            Preconditions.checkArgument(followZi2 == followZi1 - 1 && followMu1 == followMu2, "跟进前：" + followZi1 + "/" + followMu1 + ",跟进后：" + followZi2 + "/" + followMu2);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售接待差评跟进,【首页-今日任务】分子-1,分母-0");
        }

    }

}
