package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.mc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.AppFlowUp.AppFlowUpPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.AppFlowUp.AppFlowUpRemarkScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppFinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionCreateScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.EvaluateDetailV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.EvaluatePageV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesRecpEvaluateOpt;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesRecpEvaluateSubmit;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongInfo;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.tool.DataCenter;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ReceivingSystemCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.YT_ONLINE_JD; // 管理页—-首页
    private static final EnumAccount YT_RECEPTION_ACCOUNT = EnumAccount.YT_ONLINE_MC; // 全部权限账号 【运通】
    public VisitorProxy visitor = new VisitorProxy(product);   // 产品类放到代理类中（通过代理类发请求）
    public SceneUtil util = new SceneUtil(visitor);
    CommonConfig commonConfig = new CommonConfig();    // 配置类初始化
    public YunTongInfo info = new YunTongInfo();

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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(YT_RECEPTION_ACCOUNT.getReceptionShopId()).setRoleId(YT_RECEPTION_ACCOUNT.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);  // 配置请求头
        util.loginPc(YT_RECEPTION_ACCOUNT);   //登录
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


    // 创建一个接待，
    //return：接待id 和 shop_id
    private Map<String, String> createCustomerCommon(String name, String sex, String phone, String carId, String buyTime) {
        Map<String, String> customer = new HashMap<>();
        AppPreSalesReceptionCreateScene.builder().customerName(name).customerPhone(phone).sexId(sex).intentionCarModelId(carId).estimateBuyCarTime(buyTime).build().execute(visitor);//创建销售接待
        JSONObject pageInfo = PreSalesReceptionPageScene.builder().build().execute(visitor, true);
        List<JSONObject> newCustomer = pageInfo.getJSONArray("list").stream().map(ele -> (JSONObject) ele).filter(obj -> Objects.equals(phone, obj.getString("customer_phone"))).collect(Collectors.toList());
        String id = newCustomer.get(0).getString("id");
        String shopId = pageInfo.getJSONArray("list").getJSONObject(0).getString("shop_id");
        String customerId = newCustomer.get(0).getString("customer_id");
        customer.put("id", id);
        customer.put("shopId", shopId);
        customer.put("customerId", customerId);
        return customer;
    }

    @Test(dataProvider = "createErrorInfo", dataProviderClass = DataCenter.class)
    public void test01createCustomer_system_err(String description, String point, String content, String expect) {
        try {
            IScene scene = AppPreSalesReceptionCreateScene.builder().customerName("正常名字").customerPhone("18" + CommonUtil.getRandom(9)).sexId("1").intentionCarModelId("20895").estimateBuyCarTime("2035-12-20").build().modify(point, content);
            String code = scene.execute(visitor, false).getString("code");
            Preconditions.checkArgument(Objects.equals(expect, code), description + ",预期结果code=" + expect + "，实际结果code=" + code);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手动创建接待,所有异常情况");
        }

    }

    @Test
    public void test02samePhone() {
        try {
            String phone = "15" + CommonUtil.getRandom(9);
            Map<String, String> first = createCustomerCommon("一次接待", "1", phone, util.mcCarId(), "2066-12-21");
            AppFinishReceptionScene.builder().id(first.get("id")).shopId(first.get("shopId")).build().execute(visitor);
            Map<String, String> second = createCustomerCommon("二次接待", "1", phone, util.mcCarId(), "2066-12-21");
            AppFinishReceptionScene.builder().id(second.get("id")).shopId(second.get("shopId")).build().execute(visitor);
            JSONObject third = AppPreSalesReceptionCreateScene.builder().customerName("三次接待").customerPhone(phone).sexId("1").intentionCarModelId(util.mcCarId()).estimateBuyCarTime("2035-12-20").build().execute(visitor, false);
            String message = third.getString("message");
            String code = third.getString("code");
            //Boolean isFinish = PreSalesReceptionPageScene.builder().build().invoke(visitor, true).getJSONArray("list").getJSONObject(0).getBoolean("is_finish");
            Preconditions.checkArgument(!Objects.equals(code, "1000") || Objects.equals("该客户当天已接待2次！不能再进行接待！", message), "同一个手机号当天接待三次message=" + message);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手机号接待次数：同一个手机号当天接待最多两次");
        }
    }

    @Test(dataProvider = "evaluateRemark", dataProviderClass = DataCenter.class)
    public void flowUpContent(String description, String expect, String remark) {
        try {
            Map<String, String> customer = util.createCustomerCommon("自动创建差评跟进", "1", "150" + CommonUtil.getRandom(8), util.mcCarId(), "2033-12-20");
            AppFinishReceptionScene.builder().id(customer.get("id")).shopId(customer.get("shopId")).build().execute(visitor);
            commonConfig.setShopId(null);
            commonConfig.setRoleId(null);
            JSONArray evaluateInfoList = new JSONArray();
            PreSalesRecpEvaluateOpt.builder().reception_id(Long.parseLong(customer.get("id"))).build().execute(visitor, true).getJSONArray("list").stream().map(j -> (JSONObject) j).map(json -> json.getInteger("id")).forEach(e -> evaluateInfoList.add(lowEvaluate(e)));
            String message = PreSalesRecpEvaluateSubmit.builder().evaluate_info_list(evaluateInfoList).reception_id(Long.parseLong(customer.get("id"))).build().execute(visitor, false).getString("message");
            commonConfig.setShopId(YT_RECEPTION_ACCOUNT.getReceptionShopId());
            commonConfig.setRoleId(YT_RECEPTION_ACCOUNT.getRoleId());
            JSONObject firstFlow = AppFlowUpPageScene.builder().size(10).build().execute(visitor, true).getJSONArray("list").getJSONObject(0);
            Integer flowUpId = firstFlow.getInteger("id");
            String phone = firstFlow.getJSONObject("pre_reception_offline_evaluate").getString("customer_phone");
            String code = AppFlowUpRemarkScene.builder().followId(flowUpId).remark(remark).build().execute(visitor, false).getString("code");
            Integer id = EvaluatePageV4Scene.builder().page(1).size(10).evaluateType(4).customerPhone(phone).build().execute(visitor).getJSONArray("list").getJSONObject(0).getInteger("id");
            String remarkContent = EvaluateDetailV4Scene.builder().id(id).build().execute(visitor, true).getString("remark_content");
            Preconditions.checkArgument(Objects.equals(expect, code), description + ",预期结果code=" + expect + ",实际结果code=" + code);
            if (Objects.equals(expect, "1000")) {
                Preconditions.checkArgument(Objects.equals(remark, remarkContent), "跟进内容不一致。APP填写跟进内容:" + remark + ";PC跟进内容:" + remarkContent);
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售接待差评,跟进内容校验");
        }
    }

    private JSONObject lowEvaluate(int id) {
        JSONObject o = new JSONObject();
        o.put("id", id);
        o.put("score", 1);
        return o;
    }

}
