package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.mc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.AppFlowUp.AppFlowUpPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.AppFlowUp.AppFlowUpRemarkScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.homepagev4.AppTodayTaskScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppFinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppPreSalesReceptionCreateV7;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppPreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesRecpEvaluateOpt;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesRecpEvaluateSubmit;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.bean.PreReceptionBean;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Objects;


public class AppTaskReceiveDataCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.YT_ONLINE_JD; // ????????????-??????
    private static final EnumAccount YT_RECEPTION = EnumAccount.YT_ONLINE_MC; // ?????????????????? ????????????
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);   // ?????????????????????????????????????????????????????????
    public SceneUtil util = new SceneUtil(visitor);
    public YunTongInfo info = new YunTongInfo();
    CommonConfig commonConfig = new CommonConfig();    // ??????????????????

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //??????checklist???????????????
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = "??????";
        //??????jenkins-job???????????????
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //??????????????????
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(YT_RECEPTION.getReceptionShopId()).setRoleId(YT_RECEPTION.getRoleId()).setProduct(PRODUCE.getAbbreviation());
        beforeClassInit(commonConfig);  // ???????????????
        util.loginPc(YT_RECEPTION);   //??????
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

    @Test(description = "?????????????????????app????????????+1?????????????????????+0???????????????+0")
    public void creatCard(){
        try {
            Integer totalBefore = AppPreSalesReceptionPageScene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            Integer customerTotalBefore = PreSaleCustomerPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");
            Integer recepTotalBefore = PreSalesReceptionPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");
            String message = AppPreSalesReceptionCreateV7.builder().build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(Objects.equals("success", message),"????????????????????????message???"+message);
            Integer totalAfter = AppPreSalesReceptionPageScene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            Preconditions.checkArgument(totalAfter == totalBefore + 1, "??????????????????:"+totalBefore+"??????????????????????????????app????????????????????????:"+totalAfter);
            Integer customerTotalAfter = PreSaleCustomerPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");
            Preconditions.checkArgument(customerTotalAfter+1 == customerTotalBefore+1, "????????????????????????"+customerTotalBefore+"????????????????????????pc??????????????????"+customerTotalAfter);
            Integer recepTotalAfter = PreSalesReceptionPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");
            Preconditions.checkArgument(recepTotalBefore+1 == recepTotalAfter+1, "????????????????????????"+recepTotalBefore+"???????????????????????????pc????????????????????????"+recepTotalAfter);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????????????????????app????????????+1?????????????????????+0???????????????+0");
        }
    }

    @Test
    public void flowUp1() {
        try {
            Integer total1 = AppFlowUpPageScene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            PreReceptionBean customer = util.getReception();
            AppFinishReceptionScene.builder().id(customer.getId().toString()).shopId(customer.getShopId().toString()).build().visitor(visitor).execute();
            commonConfig.setShopId(null);
            commonConfig.setRoleId(null);
            JSONArray evaluateInfoList = new JSONArray();
            PreSalesRecpEvaluateOpt.builder().reception_id(customer.getId()).build().visitor(visitor).execute().getJSONArray("list").stream().map(j -> (JSONObject) j).map(json -> json.getInteger("id")).forEach(e -> evaluateInfoList.add(lowEvaluate(e)));
            String message = PreSalesRecpEvaluateSubmit.builder().evaluate_info_list(evaluateInfoList).reception_id(customer.getId()).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(Objects.equals("success", message),"????????????:"+message);
            commonConfig.setShopId(YT_RECEPTION.getReceptionShopId());
            commonConfig.setRoleId(YT_RECEPTION.getRoleId());
            Integer total2 = AppFlowUpPageScene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            Preconditions.checkArgument(total2 == total1 + 1, "app?????????????????????" + total1 + "?????????????????????app?????????????????????" + total2);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????,??????+1");
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
            String[] follows1 = AppTodayTaskScene.builder().build().visitor(visitor).execute().getString("pre_follow").split("/");
            Integer flowUpId = AppFlowUpPageScene.builder().size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getInteger("id");
            AppFlowUpRemarkScene.builder().followId(flowUpId).remark("?????????????????????????????????1azCVB").build().visitor(visitor).execute();
            String[] follows2 = AppTodayTaskScene.builder().build().visitor(visitor).execute().getString("pre_follow").split("/");
            int followZi1 = Integer.parseInt(follows1[0]);
            int followZi2 = Integer.parseInt(follows2[0]);
            int followMu1 = Integer.parseInt(follows1[1]);
            int followMu2 = Integer.parseInt(follows2[1]);
            Preconditions.checkArgument(followZi2 == followZi1 - 1 && followMu1 == followMu2, "????????????" + followZi1 + "/" + followMu1 + ",????????????" + followZi2 + "/" + followMu2);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????????????????,?????????-?????????????????????-1,??????-0");
        }
    }

}
