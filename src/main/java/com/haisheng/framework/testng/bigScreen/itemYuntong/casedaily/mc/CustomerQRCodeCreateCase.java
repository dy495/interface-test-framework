package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.H5.GetQRCode;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.loginuser.LoginPc;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongInfo;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import org.testng.annotations.*;

import java.lang.reflect.Method;


public class CustomerQRCodeCreateCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.YT_DAILY_ZH; // 管理页—-首页
    private static final EnumAccount YT_RECEPTION_DAILY = EnumAccount.YT_RECEPTION_DAILY; // 全部权限账号 【运通】
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);   // 产品类放到代理类中（通过代理类发请求）
    // public SceneUtil util = new SceneUtil(visitor);
    public YunTongInfo info = new YunTongInfo();
    CommonConfig commonConfig = new CommonConfig();    // 配置类初始化
    public Long newId; // 本次创建的接待id
    public Long newShopId; // 本次接待门店的shopId
    public Long newCustomerId;

    @BeforeClass
    @Override
    public void initial() {
//        logger.debug("before class initial");
//        //替换checklist的相关信息
//        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
//        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
//        commonConfig.checklistQaOwner = "孟辰";
//        //替换jenkins-job的相关信息
//        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_DAILY_TEST.getJobName());
//        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
//        //替换钉钉推送
//        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
//        commonConfig.product = PRODUCE.getAbbreviation(); // 产品代号 -- YT
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = "57279";  //请求头放入shopId
        commonConfig.roleId = "10322"; //请求头放入roleId
        beforeClassInit(commonConfig);  // 配置请求头
        //util.loginPc(YT_RECEPTION_DAILY);   //登录
        LoginPc loginScene = LoginPc.builder().phone("13402050043").verificationCode("000000").build();
        httpPost(PRODUCE.getIp(), loginScene.getPath(), loginScene.getBody());
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

    @Test
    public void customerInfo() {
        visitor.setProduct(EnumTestProduct.YT_DAILY_JD);
        String id = GetQRCode.builder().build().invoke(visitor, true).getString("qr_code_url");
//        GetBeforeQRCode.builder().build().invoke(visitor);
//        InputInfoBySelf.builder().name("111").build().invoke(visitor);
    }
}
