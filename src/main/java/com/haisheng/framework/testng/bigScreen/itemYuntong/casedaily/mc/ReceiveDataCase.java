package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc;

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
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppPreSalesReceptionCreateV7;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppPreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesReceptionPageScene;
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


public class ReceiveDataCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.YT_DAILY_JD; // 管理页—-首页
    private static final EnumAccount YT_RECEPTION_DAILY = EnumAccount.YT_DAILY_MC; // 全部权限账号 【运通】
    public VisitorProxy visitor = new VisitorProxy(product);   // 产品类放到代理类中（通过代理类发请求）
    public SceneUtil util = new SceneUtil(visitor);
    public YunTongInfo info = new YunTongInfo();
    CommonConfig commonConfig = new CommonConfig();    // 配置类初始化

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = "孟辰";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(YT_RECEPTION_DAILY.getReceptionShopId()).setRoleId(YT_RECEPTION_DAILY.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);  // 配置请求头
        util.loginPc(YT_RECEPTION_DAILY);   //登录
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

    @Test(description = "新建虚拟卡片，app接待列表+1，销售客户管理+0，接待记录+0")
    public void creatCard(){
        try {
            Integer totalBefore = AppPreSalesReceptionPageScene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            Integer customerTotalBefore = PreSaleCustomerPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");
            Integer recepTotalBefore = PreSalesReceptionPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");
            String message = AppPreSalesReceptionCreateV7.builder().build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(Objects.equals("success", message),"创建虚拟卡片失败message："+message);
            Integer totalAfter = AppPreSalesReceptionPageScene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            Preconditions.checkArgument(totalAfter == totalBefore + 1, "接待列表总数:"+totalBefore+"，创建虚拟卡片成功后app销售接待列表总数:"+totalAfter);
            Integer customerTotalAfter = PreSaleCustomerPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");
            Preconditions.checkArgument(customerTotalAfter+1 == customerTotalBefore+1, "销售客户列表总共"+customerTotalBefore+"，创建虚拟卡片后pc销售客户列表"+customerTotalAfter);
            Integer recepTotalAfter = PreSalesReceptionPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");
            Preconditions.checkArgument(recepTotalBefore+1 == recepTotalAfter+1, "销售接待记录总共"+recepTotalBefore+"条，创建虚拟卡片后pc销售接待记录列表"+recepTotalAfter);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建虚拟卡片，app接待列表+1，销售客户管理+0，接待记录+0");
        }
    }

    @Test
    public void flowUp1() {
        try {
            Integer total1 = AppFlowUpPageScene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            Map<String, String> customer = util.createCustomerCommon("自动差评者", "1", "150" + CommonUtil.getRandom(8), util.mcCarId(), "2033-12-20");
            AppFinishReceptionScene.builder().id(customer.get("id")).shopId(customer.get("shopId")).build().visitor(visitor).execute();
            commonConfig.setShopId(null);
            commonConfig.setRoleId(null);
            JSONArray evaluateInfoList = new JSONArray();
            PreSalesRecpEvaluateOpt.builder().reception_id(Long.parseLong(customer.get("id"))).build().visitor(visitor).execute().getJSONArray("list").stream().map(j -> (JSONObject) j).map(json -> json.getInteger("id")).forEach(e -> evaluateInfoList.add(lowEvaluate(e)));
            String message = PreSalesRecpEvaluateSubmit.builder().evaluate_info_list(evaluateInfoList).reception_id(Long.parseLong(customer.get("id"))).build().visitor(visitor).getResponse().getMessage();
            if (Objects.equals(message, "success")) {
                commonConfig.setShopId(YT_RECEPTION_DAILY.getReceptionShopId());
                commonConfig.setRoleId(YT_RECEPTION_DAILY.getRoleId());
                Integer total2 = AppFlowUpPageScene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
                Preconditions.checkArgument(total2 == total1 + 1, "app跟进列表总数：" + total1 + "销售接待差评，app跟进列表总数：" + total2);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
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
            String[] follows1 = AppTodayTaskScene.builder().build().visitor(visitor).execute().getString("pre_follow").split("/");
            Integer flowUpId = AppFlowUpPageScene.builder().size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getInteger("id");
            AppFlowUpRemarkScene.builder().followId(flowUpId).remark("自动完成跟进备注。。。1azCVB").build().visitor(visitor).execute();
            String[] follows2 = AppTodayTaskScene.builder().build().visitor(visitor).execute().getString("pre_follow").split("/");
            int followZi1 = Integer.parseInt(follows1[0]);
            int followZi2 = Integer.parseInt(follows2[0]);
            int followMu1 = Integer.parseInt(follows1[1]);
            int followMu2 = Integer.parseInt(follows2[1]);
            Preconditions.checkArgument(followZi2 == followZi1 - 1 && followMu1 == followMu2, "跟进前：" + followZi1 + "/" + followMu1 + ",跟进后：" + followZi2 + "/" + followMu2);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售接待差评跟进,【首页-今日任务】分子-1,分母-0");
        }
    }

}
