package com.haisheng.framework.testng.bigScreen.yuntong.lxq;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.customermanage.*;
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
public class SystemCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.JC_DAILY;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_AUTHORITY_DAILY_LXQ;
    private static final EnumAccount ALL_AUTHORITY_DAILY = EnumAccount.ALL_AUTHORITY_DAILY;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_LXQ_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    YunTongInfo info = new YunTongInfo();


    CommonConfig commonConfig = new CommonConfig();


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");

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

    @Test(dataProvider = "CSTMINFO")
    public void newPotentialCustomer(String name, String phone, String type, String sex, String mess, String chk) {

        logger.logCaseStart(caseResult.getCaseName());
        try {


            int bef = PreSaleCustomerPageScene.builder().page(1).size(1).build().invoke(visitor).getInteger("total");
            Long shop_id = info.oneshopid;

            Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(shop_id).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = PreSaleCustomerSalesListScene.builder().shopId(shop_id).type("PRE").build().invoke(visitor).getJSONArray("list").getJSONObject(0).getString("sales_id");

            int code = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).salesId(salesId).build().invoke(visitor, false).getInteger("code");
            int after = PreSaleCustomerPageScene.builder().page(1).size(1).build().invoke(visitor).getInteger("total");
            if (chk.equals("false")) {
                Preconditions.checkArgument(code == 1001, mess + "期待失败，实际" + code);
            } else {
                int sum = after - bef;
                Preconditions.checkArgument(code == 1000, mess + "期待创建成功，实际" + code);
                Preconditions.checkArgument(sum == 1, mess + "期待创建成功列表+1，实际增加" + sum);
                int code2 = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).salesId(salesId).build().invoke(visitor, false).getInteger("code");
                Preconditions.checkArgument(code2 == 1001, "使用列表中存在的手机号期待创建失败，实际" + code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建潜客");
        }
    }

    @DataProvider(name = "CSTMINFO")
    public Object[] customerInfo() {
        return new String[][]{ // 姓名 手机号 类型 性别  提示语 正常/异常

                {"我", "1382172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "PERSON", "0", "姓名一个字", "true"},
                {info.stringfifty, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "CORPORATION", "1", "姓名50个字", "true"},
                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 100)), "CORPORATION", "1", "手机号10位", "false"},
                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), "CORPORATION", "1", "手机号12位", "false"},
                {info.stringfifty + "1", "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "CORPORATION", "1", "姓名51位", "false"},

        };
    }

    @Test
    public void newPotentialCustomerErr() {

        logger.logCaseStart(caseResult.getCaseName());
        try {


            Long shop_id = info.oneshopid;
            Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(shop_id).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = PreSaleCustomerSalesListScene.builder().shopId(shop_id).type("PRE").build().invoke(visitor).getJSONArray("list").getJSONObject(0).getString("sales_id");

            String name = "name" + System.currentTimeMillis();
            String phone = "1391172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));
            String type = "PERSON";
            String sex = "0";
            //不填写姓名
            int code = PreSaleCustomerCreatePotentialCustomerScene.builder().customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).salesId(salesId).build().invoke(visitor, false).getInteger("code");

            Preconditions.checkArgument(code == 1001, "不填写姓名期待失败，实际" + code);

            //不填写手机号
            int code1 = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).salesId(salesId).build().invoke(visitor, false).getInteger("code");

            Preconditions.checkArgument(code1 == 1001, "不填写手机号期待失败，实际" + code);

            //不填写类型
            int code2 = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).salesId(salesId).build().invoke(visitor, false).getInteger("code");

            Preconditions.checkArgument(code2 == 1001, "不填写车主类型期待失败，实际" + code);

            //不填写性别
            int code3 = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).salesId(salesId).build().invoke(visitor, false).getInteger("code");

            Preconditions.checkArgument(code3 == 1001, "不填写性别期待失败，实际" + code);

            //不填写意向车型
            int code4 = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).shopId(shop_id).salesId(salesId).build().invoke(visitor, false).getInteger("code");

            Preconditions.checkArgument(code4 == 1001, "不填写意向车型期待失败，实际" + code);

            //不填写所属门店
            int code5 = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).build().invoke(visitor, false).getInteger("code");

            Preconditions.checkArgument(code5 == 1001, "不填写所属门店期待失败，实际" + code);

            //不填写所属销售
            int code6 = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).build().invoke(visitor, false).getInteger("code");

            Preconditions.checkArgument(code6 == 1001, "不填写所属销售期待失败，实际" + code6);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建潜客");
        }
    }

    @Test(dataProvider = "CSTMINFO")
    public void newCstmRecord(String name, String phone, String type, String sex, String mess, String chk) {

        logger.logCaseStart(caseResult.getCaseName());
        try {


            Long shop_id = info.oneshopid;
            Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(shop_id).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = PreSaleCustomerSalesListScene.builder().shopId(shop_id).type("PRE").build().invoke(visitor).getJSONArray("list").getJSONObject(0).getString("sales_id");

            if (chk.equals("false")) {
                int code = PreSaleCustomerCreateCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).shopId(shop_id).vehicleChassisCode("ASDFUGGDSF12" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000))).purchaseCarDate(dt.getHistoryDate(-1)).build().invoke(visitor, false).getInteger("code");
                Preconditions.checkArgument(code == 1001, mess + "期待失败，实际" + code);
            } else {
                int code1 = PreSaleCustomerCreateCustomerScene.builder().customerName(name).customerPhone(info.donephone).customerType(type).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).shopId(shop_id).vehicleChassisCode("ASDFUGGDSF02" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000))).purchaseCarDate(dt.getHistoryDate(-1)).build().invoke(visitor, false).getInteger("code");
                Preconditions.checkArgument(code1 == 1000, mess + "期待创建成功，实际" + code1);

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建成交记录");
        }
    }

    @Test
    public void newCstmRecordErr() {

        logger.logCaseStart(caseResult.getCaseName());
        try {


            Long shop_id = info.oneshopid;
            Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(shop_id).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = PreSaleCustomerSalesListScene.builder().shopId(shop_id).type("PRE").build().invoke(visitor).getJSONArray("list").getJSONObject(0).getString("sales_id");

            String name = "name" + System.currentTimeMillis();
            String phone = info.donephone;
            String type = "PERSON";
            String sex = "0";


//            int code = jc.createCstm(name,phone,type,sex,car_model_id,shop_id,salesId,dt.getHistoryDate(1),"ASDFUGGDSF12"+Integer.toString((int)((Math.random()*9+1)*10000)),false).getInteger("code");
//            Preconditions.checkArgument(code==1001,"购车日期大于当前时间期待失败，实际"+code);

            int code1 = PreSaleCustomerCreateCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).shopId(shop_id).vehicleChassisCode("ASDFUGGDSF1" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000))).purchaseCarDate(dt.getHistoryDate(-1)).build().invoke(visitor, false).getInteger("code");

            Preconditions.checkArgument(code1 == 1001, "底盘号16位期待失败，实际" + code1);

            int code2 = PreSaleCustomerCreateCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).shopId(shop_id).vehicleChassisCode("ASDFUGGDSF111" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000))).purchaseCarDate(dt.getHistoryDate(-1)).build().invoke(visitor, false).getInteger("code");

            Preconditions.checkArgument(code2 == 1001, "底盘号18位期待失败，实际" + code2);

            int code3 = PreSaleCustomerCreateCustomerScene.builder().customerName(name).customerPhone(info.phone).customerType(type).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).shopId(shop_id).vehicleChassisCode("ASDFUGGDSF11" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000))).purchaseCarDate(dt.getHistoryDate(-1)).build().invoke(visitor, false).getInteger("code");

            Preconditions.checkArgument(code3 == 1001, "手机号未注册小程序期待失败，实际" + code3);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建成交记录异常条件");
        }
    }


}
