package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppCustomerEditV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppPreSalesReceptionCreateScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppPreSalesReceptionCreateV7;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppPreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongInfo;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.AppReceptionBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppFinishReceptionScene;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.*;
import java.lang.reflect.Method;
import java.util.*;

public class AppReceivingDataCase extends TestCaseCommon implements TestCaseStd {
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
//        LoginPc loginScene = LoginPc.builder().phone("13402050043").verificationCode("000000").build();
//        httpPost(loginScene.getPath(),loginScene.getBody(),PRODUCE.getPort());
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


    public AppReceptionBean createCustomerCommon(String name, Integer sex, String phone, Long carId, String buyTime) {
        AppPreSalesReceptionCreateV7.builder().build().visitor(visitor).getResponse();
        AppReceptionBean beforeReception = AppPreSalesReceptionPageScene.builder().size(10).lastValue(null).build()
                .visitor(visitor).execute().getJSONArray("list").getJSONObject(0).toJavaObject(AppReceptionBean.class);
        String message = AppCustomerEditV4Scene.builder().id(beforeReception.getId()).customerId(beforeReception.getCustomerId())
                .shopId(beforeReception.getShopId()).customerName(name).customerPhone(phone).customerSource("NATURE_VISIT")
                .sexId(sex).intentionCarModelId(carId).estimateBuyCarDate(buyTime).build().visitor(visitor).getResponse().getMessage();
        Preconditions.checkArgument(Objects.equals("success", message),"修改资料失败:"+message);
        return AppPreSalesReceptionPageScene.builder().size(10).lastValue(null).build().visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e)
                .filter(e ->Objects.equals(e.getString("id"), beforeReception.getId().toString())).findFirst().get().toJavaObject(AppReceptionBean.class);
    }

    //@Test
    public void test02samePhone() {
        try {
            String phone = "15" + CommonUtil.getRandom(9);
            AppReceptionBean first = createCustomerCommon("一次接待", 1, phone, Long.parseLong(util.mcCarId()), "2066-12-21");
            AppFinishReceptionScene.builder().id(first.getId()).shopId(first.getShopId()).build().visitor(visitor).execute();
            AppReceptionBean second = createCustomerCommon("二次接待", 1, phone, Long.parseLong(util.mcCarId()), "2066-12-21");
            AppFinishReceptionScene.builder().id(second.getId()).shopId(second.getShopId()).build().visitor(visitor).execute();
            String message = AppPreSalesReceptionCreateScene.builder().customerName("三次接待").customerPhone(phone).sexId(1).intentionCarModelId(Long.parseLong(util.mcCarId())).estimateBuyCarTime("2035-12-20").build().visitor(visitor).getResponse().getMessage();
            //Boolean isFinish = PreSalesReceptionPageScene.builder().build().invoke(visitor, true).getJSONArray("list").getJSONObject(0).getBoolean("is_finish");
            Preconditions.checkArgument(Objects.equals("该客户当天已接待2次！不能再进行接待！", message), "同一个手机号当天接待三次,message:" + message);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手机号接待次数：同一个手机号当天接待最多两次");
        }
    }









}
