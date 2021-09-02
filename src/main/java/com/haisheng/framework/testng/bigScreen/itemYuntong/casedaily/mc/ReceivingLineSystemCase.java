package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppCustomerDetailV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppCustomerEditV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongInfo;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.AppReceptionBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.tool.YtDataCenter;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.*;
import java.lang.reflect.Method;
import java.util.Objects;


public class ReceivingLineSystemCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.YT_DAILY_ZH; // 管理页—-首页
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
        visitor.setProduct(EnumTestProduct.YT_DAILY_JD);
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



    @Test(dataProvider = "remark", dataProviderClass = YtDataCenter.class)
    public void test02PcRemark(String description, String remark, String expect) {
        try {
            AppReceptionBean reception = util.getReception();
            Integer code = CustomerRemarkScene.builder().id(reception.getId()).shopId(reception.getShopId()).remark(remark).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(Objects.equals(String.valueOf(code), expect), description + ",预期:" + expect + ",实际结果:" + code);
            if (Objects.equals("1000", String.valueOf(code))) {
                JSONArray remarks = AppCustomerDetailV4Scene.builder().shopId(reception.getShopId().toString()).id(reception.getId().toString()).build().visitor(visitor).execute().getJSONArray("remarks");
                String addedRemark = remarks.getJSONObject(0).getString("remark");
                Preconditions.checkArgument(Objects.equals(addedRemark, remark), "备注内容不一致，pc输入的备注内容" + remark + ",app接待详情中备注记录:" + addedRemark);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待中，PC备注");
        }
    }


//    @Test(dataProvider = "remarkContent")
//    public void test04AppRemark(String description, String remark, String expect) {
//        try {
//            if (newId != null && newCustomerId != null) {
//                Integer code = AppCustomerRemarkV4Scene.builder().id(newId.toString()).shopId(newShopId.toString()).customerId(newCustomerId.toString()).remark(remark).build().visitor(visitor).getResponse().getCode();
//                String addedRemark = AppCustomerDetailV4Scene.builder().shopId(newShopId.toString()).customerId(newCustomerId.toString()).id(newId.toString()).build().visitor(visitor).execute().getJSONArray("remarks").getJSONObject(0).getString("remark");
//                Preconditions.checkArgument(Objects.equals(String.valueOf(code), expect), description + ",预期:" + expect + ",实际结果:" + code);
//                if (Objects.equals("1000", String.valueOf(code))) {
//                    Preconditions.checkArgument(Objects.equals(addedRemark, remark), "备注内容不一致，app输入的备注内容" + remark + ",app接待详情中备注记录:" + addedRemark);
//                }
//            }
//        } catch (AssertionError | Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("app接待中备注");
//        }
//    }

    @Test(dataProvider = "editErrorInfo", dataProviderClass = YtDataCenter.class)
    public void test07ChangeUserInfo(String description, String point, String content, String expect) {
        try {
            AppReceptionBean reception = util.getReception();
            Integer code = AppCustomerEditV4Scene.builder().id(reception.getId()).customerId(reception.getCustomerId()).shopId(reception.getShopId()).customerName("name").customerPhone("18"+CommonUtil.getRandom(9)).sexId(1).intentionCarModelId(Long.parseLong(util.mcCarId())).estimateBuyCarDate("2035-12-20").build().modify(point,content).visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(Objects.equals(code.toString(), expect), description + "，期待结果=" + expect + "实际结果code=" + code);
            sleep(3);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app接待中编辑资料,只填写必填项");
        }
    }

    @Test
    public void test09PcComplete() {
        try {
            AppReceptionBean reception = util.getReception();
            FinishReceptionScene.builder().id(reception.getId()).shopId(reception.getShopId()).build().visitor(visitor).execute();
            Boolean isFinish = PreSalesReceptionPageScene.builder().build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getBoolean("is_finish");
            Preconditions.checkArgument(isFinish, "完成接待操作失败，接待手机=" + reception.getCustomerPhone());
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("Pc完成接待");
        }
    }
//    @Test
//    public void test05AfterCompleteMark(){
//       try{
//           Integer code = CustomerRemarkScene.builder().id(newId).shopId(newShopId).remark("完成接待之后填写备注12345678").build().invoke(visitor, false).getCode();
//            Preconditions.checkArgument(Objects.equals("1001",code),"完成接待后填写备注，期待失败1001，结果："+code);
//    } catch (AssertionError e) {
//        appendFailReason(e.toString());
//    } catch (Exception e) {
//        appendFailReason(e.toString());
//    } finally {
//        saveData("接待完成，备注");
//    }
//    }

}
