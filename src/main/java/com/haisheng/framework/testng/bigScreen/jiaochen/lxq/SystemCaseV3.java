package com.haisheng.framework.testng.bigScreen.jiaochen.lxq;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.jiaoChenInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletConsultAfterServiceSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletConsultOnlineExpertsSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletConsultPreServiceSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletMessageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.followup.AppPageV3Scene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.consultmanagement.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * 内容运营
 *
 * @author lxq
 * @date 2021/1/29 11:17
 */
public class SystemCaseV3 extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.JC_DAILY;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_AUTHORITY_DAILY;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_LXQ_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    jiaoChenInfo info = new  jiaoChenInfo();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = "吕雪晴";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = PRODUCE.getShopId();
        commonConfig.roleId = ALL_AUTHORITY.getRoleId();
        beforeClassInit(commonConfig);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        user.loginApplet(APPLET_USER_ONE);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);

    }



    /**
     *-----------------------------在线专家咨询------------------------------------
     */

    @Test(dataProvider = "ONLINEEXPERTINFO")
    public void onlineExpert1(String customerName,String customerPhone,String content,String mess,String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long brandId = 1L;
            Long modelId = 1L;
            Long shopId  = 1L;
            JSONObject obj = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .brandId(brandId).modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code = obj.getInteger("code");
            if (status.equals("false")){
                String message = obj.getString("message");
                Preconditions.checkArgument(code==1001,mess+", 状态码为:"+code+", 提示语为:"+message);
            }
            if (status.equals("true")){
                Preconditions.checkArgument(code==100,mess+", 状态码为:"+code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交在线专家咨询，校验必填项内容");
        }
    }

    @Test
    public void onlineExpert2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long brandId = 1L;
            Long modelId = 1L;
            Long shopId  = 1L;
            String customerName="奶糖";
            String customerPhone="13811110000";
            String content="12345678901234567890";

            JSONObject obj1 = AppletConsultOnlineExpertsSubmitScene.builder().customerPhone(customerPhone).content(content)
                    .brandId(brandId).modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code1 = obj1.getInteger("code");
            String message = obj1.getString("message");
            Preconditions.checkArgument(code1==1001,"不填写姓名，状态码为:"+code1+", 提示语为:"+message);

            JSONObject obj2 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).content(content)
                    .brandId(brandId).modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code2 = obj2.getInteger("code");
            String message2 = obj2.getString("message");
            Preconditions.checkArgument(code2==1001,"不填写手机号，状态码为:"+code2+", 提示语为:"+message2);

            JSONObject obj3 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone)
                    .brandId(brandId).modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code3 = obj3.getInteger("code");
            String message3 = obj3.getString("message");
            Preconditions.checkArgument(code3==1001,"不填写咨询内容，状态码为:"+code3+", 提示语为:"+message3);

            JSONObject obj4 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code4 = obj4.getInteger("code");
            String message4 = obj4.getString("message");
            Preconditions.checkArgument(code4==1001,"不填写品牌，状态码为:"+code4+", 提示语为:"+message4);

            JSONObject obj5 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .brandId(brandId).shopId(shopId).build().invoke(visitor, false);
            int code5 = obj5.getInteger("code");
            String message5 = obj5.getString("message");
            Preconditions.checkArgument(code5==1001,"不填写车型，状态码为:"+code5+", 提示语为:"+message5);

            JSONObject obj6 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .brandId(brandId).modelId(modelId).build().invoke(visitor, false);
            int code6 = obj6.getInteger("code");
            String message6 = obj6.getString("message");
            Preconditions.checkArgument(code6==1001,"不填写咨询门店，状态码为:"+code6+", 提示语为:"+message6);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交在线专家咨询，不填写必填项");
        }
    }

    @Test
    public void onlineExpert3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取小程序消息列表数量
            user.loginApplet(APPLET_USER_ONE);
            int befApplet = AppletMessageListScene.builder().size(10).build().invoke(visitor).getInteger("total");
            //获取app跟进列表数量
            user.loginApp(ALL_AUTHORITY);
            int befApp = AppPageV3Scene.builder().size(10).build().invoke(visitor).getInteger("total");
            //PC【在线专家列表】
            user.loginPc(ALL_AUTHORITY);
            int befPC = OnlineExpertsPageListScene.builder().page(1).size(10).build().invoke(visitor).getInteger("total");

            //小程序提交在线专家
            JSONObject submitobj = info.submitonlineExpert();
            String name = submitobj.getString("customerName");
            String phone = submitobj.getString("customerPhone");
            String brandId = submitobj.getString("brandId");
            String modelId = submitobj.getString("modelId");
            String shopId = submitobj.getString("shopId");

            //获取小程序消息列表数量
            int afterApplet = AppletMessageListScene.builder().size(10).build().invoke(visitor).getInteger("total");
            //获取app跟进列表数量
            user.loginApp(ALL_AUTHORITY);
            int afterApp = AppPageV3Scene.builder().size(10).build().invoke(visitor).getInteger("total");
            //PC【在线专家列表】
            user.loginPc(ALL_AUTHORITY);
            JSONObject obj = OnlineExpertsPageListScene.builder().page(1).size(10).build().invoke(visitor).getJSONArray("list").getJSONObject(0);
            int afterPC = OnlineExpertsPageListScene.builder().page(1).size(10).build().invoke(visitor).getInteger("total");


            int applet = afterApplet - befApplet;
            int app = afterApp - befApp;
            int pc = afterPC - befPC;
            Preconditions.checkArgument(applet==1,"小程序消息列表未+1");
            Preconditions.checkArgument(app==1,"app跟进列表未+1");
            Preconditions.checkArgument(pc==1,"PC【在线专家】列表未+1");
            if(pc==1){
                String customer_name = obj.getString("customer_name");
                String customer_phone = obj.getString("customer_phone");
                String shop_name = obj.getString("shop_name");
                String brand_name = obj.getString("brand_name");
                String model_name = obj.getString("model_name");
                Preconditions.checkArgument(customer_name.equals(name),"提交时姓名"+name+", PC展示"+customer_name);
                Preconditions.checkArgument(customer_phone.equals(phone),"提交时手机号"+phone+", PC展示"+customer_phone);
                Preconditions.checkArgument(shop_name.equals(shopId),"提交时选择的门店"+shopId+", PC展示"+shop_name);
                Preconditions.checkArgument(brand_name.equals(brandId),"提交时选择的品牌"+brandId+", PC展示"+brand_name);
                Preconditions.checkArgument(model_name.equals(modelId),"提交时选择的品牌"+modelId+", PC展示"+model_name);

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交在线专家咨询，结果校验：app产生跟进&小程序收到自动回复消息&PC【在线专家】列表展示");
        }
    }




    /**
     *-----------------------------专属服务咨询------------------------------------
     */

    @Test(dataProvider = "ONLINEEXPERTINFO")
    public void preServiceSubmit1(String customerName,String customerPhone,String content,String mess,String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long modelId = 1L;
            Long shopId  = 1L;
            String salesId  = "";
            JSONObject obj = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code = obj.getInteger("code");
            if (status.equals("false")){
                String message = obj.getString("message");
                Preconditions.checkArgument(code==1001,mess+", 状态码为:"+code+", 提示语为:"+message);
            }
            if (status.equals("true")){
                Preconditions.checkArgument(code==100,mess+", 状态码为:"+code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交专属销售顾问咨询，校验必填项内容");
        }
    }

    @Test
    public void preServiceSubmit2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String salesId  = "";
            Long modelId = 1L;
            Long shopId  = 1L;
            String customerName="奶糖";
            String customerPhone="13811110000";
            String content="12345678901234567890";

            JSONObject obj1 = AppletConsultPreServiceSubmitScene.builder().customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code1 = obj1.getInteger("code");
            String message = obj1.getString("message");
            Preconditions.checkArgument(code1==1001,"不填写姓名，状态码为:"+code1+", 提示语为:"+message);

            JSONObject obj2 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code2 = obj2.getInteger("code");
            String message2 = obj2.getString("message");
            Preconditions.checkArgument(code2==1001,"不填写手机号，状态码为:"+code2+", 提示语为:"+message2);

            JSONObject obj3 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code3 = obj3.getInteger("code");
            String message3 = obj3.getString("message");
            Preconditions.checkArgument(code3==1001,"不填写留言，状态码为:"+code3+", 提示语为:"+message3);

            JSONObject obj4 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code4 = obj4.getInteger("code");
            String message4 = obj4.getString("message");
            Preconditions.checkArgument(code4==1001,"不选择销售，状态码为:"+code4+", 提示语为:"+message4);

            JSONObject obj5 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).shopId(shopId).build().invoke(visitor, false);
            int code5 = obj5.getInteger("code");
            String message5 = obj5.getString("message");
            Preconditions.checkArgument(code5==1001,"不填写车型，状态码为:"+code5+", 提示语为:"+message5);

            JSONObject obj6 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).build().invoke(visitor, false);
            int code6 = obj6.getInteger("code");
            String message6 = obj6.getString("message");
            Preconditions.checkArgument(code6==1001,"不填写咨询门店，状态码为:"+code6+", 提示语为:"+message6);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交专属销售顾问咨询，不填写必填项");
        }
    }

    @Test
    public void preServiceSubmit3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取小程序消息列表数量
            user.loginApplet(APPLET_USER_ONE);
            int befApplet = AppletMessageListScene.builder().size(10).build().invoke(visitor).getInteger("total");
            //获取app跟进列表数量
            user.loginApp(ALL_AUTHORITY);
            int befApp = AppPageV3Scene.builder().size(10).build().invoke(visitor).getInteger("total");
            //PC【专属服务列表】
            user.loginPc(ALL_AUTHORITY);
            int befPC = DedicatedServicePageListScene.builder().page(1).size(10).build().invoke(visitor).getInteger("total");

            //小程序提交销售咨询
            JSONObject submitobj = info.submitPreService();
            String name = submitobj.getString("customerName");
            String phone = submitobj.getString("customerPhone");
            String brandId = submitobj.getString("brandId");
            String modelId = submitobj.getString("modelId");
            String shopId = submitobj.getString("shopId");

            //获取小程序消息列表数量
            int afterApplet = AppletMessageListScene.builder().size(10).build().invoke(visitor).getInteger("total");
            //获取app跟进列表数量
            user.loginApp(ALL_AUTHORITY);
            int afterApp = AppPageV3Scene.builder().size(10).build().invoke(visitor).getInteger("total");
            //PC【专属服务列表】
            user.loginPc(ALL_AUTHORITY);
            JSONObject obj = DedicatedServicePageListScene.builder().page(1).size(10).build().invoke(visitor).getJSONArray("list").getJSONObject(0);
            int afterPC = DedicatedServicePageListScene.builder().page(1).size(10).build().invoke(visitor).getInteger("total");


            int applet = afterApplet - befApplet;
            int app = afterApp - befApp;
            int pc = afterPC - befPC;
            Preconditions.checkArgument(applet==1,"小程序消息列表未+1");
            Preconditions.checkArgument(app==1,"app跟进列表未+1");
            Preconditions.checkArgument(pc==1,"PC【专属服务】列表未+1");
            if(pc==1){
                String customer_name = obj.getString("customer_name");
                String customer_phone = obj.getString("customer_phone");
                String shop_name = obj.getString("shop_name");
                String brand_name = obj.getString("brand_name");
                String model_name = obj.getString("model_name");
                Preconditions.checkArgument(customer_name.equals(name),"提交时姓名"+name+", PC展示"+customer_name);
                Preconditions.checkArgument(customer_phone.equals(phone),"提交时手机号"+phone+", PC展示"+customer_phone);
                Preconditions.checkArgument(shop_name.equals(shopId),"提交时选择的门店"+shopId+", PC展示"+shop_name);
                Preconditions.checkArgument(brand_name.equals(brandId),"提交时选择的品牌"+brandId+", PC展示"+brand_name);
                Preconditions.checkArgument(model_name.equals(modelId),"提交时选择的品牌"+modelId+", PC展示"+model_name);

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交专属销售咨询，结果校验：app产生跟进&小程序收到自动回复消息&PC【专属销售】列表展示");
        }
    }


    @Test(dataProvider = "ONLINEEXPERTINFO")
    public void afterServiceSubmit1(String customerName,String customerPhone,String content,String mess,String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long modelId = 1L;
            Long shopId  = 1L;
            String salesId  = "";
            JSONObject obj = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code = obj.getInteger("code");
            if (status.equals("false")){
                String message = obj.getString("message");
                Preconditions.checkArgument(code==1001,mess+", 状态码为:"+code+", 提示语为:"+message);
            }
            if (status.equals("true")){
                Preconditions.checkArgument(code==100,mess+", 状态码为:"+code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交专属售后顾问咨询，校验必填项内容");
        }
    }

    @Test
    public void afterServiceSubmit2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String salesId  = "";
            Long modelId = 1L;
            Long shopId  = 1L;
            String customerName="奶糖";
            String customerPhone="13811110000";
            String content="12345678901234567890";

            JSONObject obj1 = AppletConsultAfterServiceSubmitScene.builder().customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code1 = obj1.getInteger("code");
            String message = obj1.getString("message");
            Preconditions.checkArgument(code1==1001,"不填写姓名，状态码为:"+code1+", 提示语为:"+message);

            JSONObject obj2 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code2 = obj2.getInteger("code");
            String message2 = obj2.getString("message");
            Preconditions.checkArgument(code2==1001,"不填写手机号，状态码为:"+code2+", 提示语为:"+message2);

            JSONObject obj3 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code3 = obj3.getInteger("code");
            String message3 = obj3.getString("message");
            Preconditions.checkArgument(code3==1001,"不填写留言，状态码为:"+code3+", 提示语为:"+message3);

            JSONObject obj4 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .modelId(modelId).shopId(shopId).build().invoke(visitor, false);
            int code4 = obj4.getInteger("code");
            String message4 = obj4.getString("message");
            Preconditions.checkArgument(code4==1001,"不选择顾问，状态码为:"+code4+", 提示语为:"+message4);

            JSONObject obj5 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).shopId(shopId).build().invoke(visitor, false);
            int code5 = obj5.getInteger("code");
            String message5 = obj5.getString("message");
            Preconditions.checkArgument(code5==1001,"不填写车型，状态码为:"+code5+", 提示语为:"+message5);

            JSONObject obj6 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).build().invoke(visitor, false);
            int code6 = obj6.getInteger("code");
            String message6 = obj6.getString("message");
            Preconditions.checkArgument(code6==1001,"不填写咨询门店，状态码为:"+code6+", 提示语为:"+message6);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交专属售后顾问咨询，不填写必填项");
        }
    }

    //todo 提交的结果验证






    /**
     *
     */
    @Test
    public void a1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            AppletConsultOnlineExpertsSubmitScene.builder().brandId(12L).build().invoke(visitor, false).getString("message");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }

    }


    /**
     * -----------------------------------dataProvider来这里排排站---------------------------------------------------
     */
    @DataProvider(name = "ONLINEEXPERTINFO")
    public Object[] onlineExpertrInfo(){
        return new String[][]{ // 姓名 手机号 咨询内容(10-200)  提示语 正常/异常

                {"吕","1382172"+Integer.toString((int)((Math.random()*9+1)*1000)),"这是10geZI！@","姓名1个字&咨询内容10个字(期待成功)","true"},
                {info.stringfifty,"1381172"+Integer.toString((int)((Math.random()*9+1)*1000)),info.string200,"姓名50个字&咨询内容200个字(期待成功)","true"},
                {info.stringsix,"1381172"+Integer.toString((int)((Math.random()*9+1)*100)),info.stringfifty,"手机号10位(期待失败)","false"},
                {info.stringsix,"1381172"+Integer.toString((int)((Math.random()*9+1)*10000)),info.stringfifty,"手机号12位(期待失败)","false"},
                {info.stringfifty+"1","1381172"+Integer.toString((int)((Math.random()*9+1)*1000)),info.stringfifty,"姓名51位(期待失败)","false"},
                {info.stringsix,"1381172"+Integer.toString((int)((Math.random()*9+1)*1000)),"这是9geZI！@","咨询内容9个字(期待失败)","false"},
                {info.stringsix,"1381172"+Integer.toString((int)((Math.random()*9+1)*1000)),info.string200+"1","咨询内容201个字(期待失败)","false"},

        };
    }


}
