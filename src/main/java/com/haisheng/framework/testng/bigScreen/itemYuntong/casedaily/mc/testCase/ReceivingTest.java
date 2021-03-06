package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.testCase;

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
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.bean.PreReceptionBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppFinishReceptionScene;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.*;
import java.lang.reflect.Method;
import java.util.*;

public class ReceivingTest extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.YT_DAILY_JD; // ????????????-??????
    private static final EnumAccount YT_RECEPTION_DAILY = EnumAccount.YT_DAILY_MC; // ?????????????????? ????????????
    public VisitorProxy visitor = new VisitorProxy(product);   // ?????????????????????????????????????????????????????????
    public SceneUtil util = new SceneUtil(visitor);
    public YunTongInfo info = new YunTongInfo();
    CommonConfig commonConfig = new CommonConfig();    // ??????????????????

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //??????checklist???????????????
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = "??????";
        //??????jenkins-job???????????????
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //??????????????????
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(YT_RECEPTION_DAILY.getReceptionShopId()).setRoleId(YT_RECEPTION_DAILY.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);  // ???????????????
        util.loginPc(YT_RECEPTION_DAILY);   //??????
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


    public PreReceptionBean createCustomerCommon(String name, Integer sex, String phone, Long carId, String buyTime) {
        AppPreSalesReceptionCreateV7.builder().build().visitor(visitor).getResponse();
        PreReceptionBean beforeReception = AppPreSalesReceptionPageScene.builder().size(10).lastValue(null).build()
                .visitor(visitor).execute().getJSONArray("list").getJSONObject(0).toJavaObject(PreReceptionBean.class);
        String message = AppCustomerEditV4Scene.builder().id(beforeReception.getId()).customerId(beforeReception.getCustomerId())
                .shopId(beforeReception.getShopId()).customerName(name).customerPhone(phone).customerSource("NATURE_VISIT")
                .sexId(sex).intentionCarModelId(carId).estimateBuyCarDate(buyTime).build().visitor(visitor).getResponse().getMessage();
        Preconditions.checkArgument(Objects.equals("success", message),"??????????????????:"+message);
        return AppPreSalesReceptionPageScene.builder().size(10).lastValue(null).build().visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e)
                .filter(e ->Objects.equals(e.getString("id"), beforeReception.getId().toString())).findFirst().get().toJavaObject(PreReceptionBean.class);
    }

    //@Test
    public void test02samePhone() {
        try {
            String phone = "15" + CommonUtil.getRandom(9);
            PreReceptionBean first = createCustomerCommon("????????????", 1, phone, Long.parseLong(util.mcCarId()), "2066-12-21");
            AppFinishReceptionScene.builder().id(first.getId()).shopId(first.getShopId()).build().visitor(visitor).execute();
            PreReceptionBean second = createCustomerCommon("????????????", 1, phone, Long.parseLong(util.mcCarId()), "2066-12-21");
            AppFinishReceptionScene.builder().id(second.getId()).shopId(second.getShopId()).build().visitor(visitor).execute();
            String message = AppPreSalesReceptionCreateScene.builder().customerName("????????????").customerPhone(phone).sexId(1).intentionCarModelId(Long.parseLong(util.mcCarId())).estimateBuyCarTime("2035-12-20").build().visitor(visitor).getResponse().getMessage();
            //Boolean isFinish = PreSalesReceptionPageScene.builder().build().invoke(visitor, true).getJSONArray("list").getJSONObject(0).getBoolean("is_finish");
            Preconditions.checkArgument(Objects.equals("????????????????????????2??????????????????????????????", message), "????????????????????????????????????,message:" + message);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????????????????????????????????????????????????????");
        }
    }









}
