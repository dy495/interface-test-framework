package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.mc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanagev4.PreSaleCustomerInfoBuyCarRecordScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongInfo;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.tool.DataCenter;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.testng.service.ApiRequest;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class ReceivingLineSystemCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.YT_ONLINE_JD; // 管理页—-首页
    private static final EnumAccount YT_RECEPTION_ACCOUNT = EnumAccount.YT_ONLINE_MC; // 全部权限账号 【运通】
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);   // 产品类放到代理类中（通过代理类发请求）
    public SceneUtil util = new SceneUtil(visitor);
    public YunTongInfo info = new YunTongInfo();
    public Long newId; // 本次创建的接待id
    public Long newShopId; // 本次接待门店的shopId
    public Long newCustomerId;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();    // 配置类初始化
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = "孟辰";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(YT_RECEPTION_ACCOUNT.getReceptionShopId()).setRoleId(YT_RECEPTION_ACCOUNT.getRoleId()).setProduct(PRODUCE.getAbbreviation());
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


    public void customerConfig() {
        String phone = "15" + CommonUtil.getRandom(9);
        AppPreSalesReceptionCreateScene.builder().customerName("mc自动化创建使用").customerPhone(phone).sexId("1").intentionCarModelId(util.mcCarId()).estimateBuyCarTime("2035-07-12").build().visitor(visitor).execute();//创建销售接待
        JSONObject pageInfo = AppPreSalesReceptionPageScene.builder().build().visitor(visitor).execute();
        List<JSONObject> newCustomer = pageInfo.getJSONArray("list").stream().map(ele -> (JSONObject) ele).filter(obj -> phone.equals(obj.getString("customer_phone"))).collect(Collectors.toList());
        Long id = newCustomer.get(0).getLong("id");
        Long shopId = newCustomer.get(0).getLong("shop_id");
        Long customerId = newCustomer.get(0).getLong("customer_id");
        this.newId = id;
        this.newShopId = shopId;
        this.newCustomerId = customerId;

    }

    @Test(dataProvider = "remark", dataProviderClass = DataCenter.class)
    public void test02PcRemark(String description, String remark, String expect) {
        try {
            if (newId == null || newCustomerId == null) {
                customerConfig();
            }
            AppCustomerDetailV4Scene detail = AppCustomerDetailV4Scene.builder().shopId(newShopId.toString()).customerId(newCustomerId.toString()).id(newId.toString()).build();
            //            int beforeSum = detail.invoke(visitor, true).getJSONArray("remarks").size();
            Integer code = CustomerRemarkScene.builder().id(newId).shopId(newShopId).remark(remark).build().visitor(visitor).getResponse().getCode();
            JSONArray remarks = detail.visitor(visitor).execute().getJSONArray("remarks");
            //            int afterSum = remarks.size();
            String addedRemark = remarks.getJSONObject(0).getString("remark");
            Preconditions.checkArgument(Objects.equals(String.valueOf(code), expect), description + ",预期:" + expect + ",实际结果:" + code);
            if (Objects.equals("1000", String.valueOf(code))) {
                Preconditions.checkArgument(Objects.equals(addedRemark, remark), "备注内容不一致，pc输入的备注内容" + remark + ",app接待详情中备注记录:" + addedRemark);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待中，PC备注");
        }
    }

    @Test(dataProvider = "chassisCode")
    public void test03BuyCar(String description, String expect, String vin) {
        try {
            if (newId != null && newCustomerId != null) {
                Integer code = BuyCarScene.builder().carModel(20895L).carStyle(2527L).id(newId).shopId(newShopId).vin(vin).build().visitor(visitor).getResponse().getCode();
                Preconditions.checkArgument(Objects.equals(String.valueOf(code), expect), description + ",预期code:" + expect + "实际code=" + code);
                if (Objects.equals(expect, "1000") && vin.length() != 0) {
                    String chassisCode = PreSaleCustomerInfoBuyCarRecordScene.builder().customerId(newCustomerId).shopId(newShopId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getString("vehicle_chassis_code");
                    Preconditions.checkArgument(Objects.equals(chassisCode, vin.toUpperCase()), "详情中底盘号不一致,输入:" + vin + "实际:" + chassisCode);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待中，PC确认购车");
        }
    }

    @DataProvider(name = "chassisCode")
    public Object[] chassisCode() {
        return new String[][]{
                {"底盘号17位英文+数字", "1000", "aaaabbbc" + CommonUtil.getRandom(9)},
                {"底盘号16位英文+数字", "1001", "aaaabbbc" + CommonUtil.getRandom(8)},
                {"底盘号18位英文+数字", "1001", "aaaabbbc" + CommonUtil.getRandom(10)},
                {"底盘号为空", "1000", ""},
                {"系统存在的底盘号", "1001", "ABC12345678901234"}
        };
    }

    @Test(dataProvider = "remark", dataProviderClass = DataCenter.class)
    public void test04AppRemark(String description, String remark, String expect) {
        try {
            if (newId == null || newCustomerId == null) {
                customerConfig();
            }
            Integer code = AppCustomerRemarkV4Scene.builder().id(newId.toString()).shopId(newShopId.toString()).customerId(newCustomerId.toString()).remark(remark).build().visitor(visitor).getResponse().getCode();
            String addedRemark = AppCustomerDetailV4Scene.builder().shopId(newShopId.toString()).customerId(newCustomerId.toString()).id(newId.toString()).build().visitor(visitor).execute().getJSONArray("remarks").getJSONObject(0).getString("remark");
            Preconditions.checkArgument(Objects.equals(String.valueOf(code), expect), description + ",预期:" + expect + ",实际结果:" + code);
            if (Objects.equals("1000", String.valueOf(code))) {
                Preconditions.checkArgument(Objects.equals(addedRemark, remark), "备注内容不一致，app输入的备注内容" + remark + ",app接待详情中备注记录:" + addedRemark);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app接待中备注");
        }
    }
//    @Test
//    public void test05AddCar(){
//        try {
//
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("app接待中添加车牌号");
//        }
//    }

//    @Test
//    public void test06ChangeChassisCode(){
//        try {
//            VehicleEditScene.builder().carModel()
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("app接待中修改底盘号");
//        }
//    }

    @Test(dataProvider = "editErrorInfo", dataProviderClass = DataCenter.class)
    public void test07ChangeUserInfo(String description, String point, String content, String expect) {
        try {
            if (newId == null || newCustomerId == null) {
                customerConfig();
            }
            String message = AppCustomerEditV4Scene.builder().id(newId.toString()).customerId(newCustomerId.toString()).shopId(newShopId.toString()).customerName("自动名字" + dt.getHistoryDate(0)).customerPhone("13" + CommonUtil.getRandom(9)).sexId(1).intentionCarModelId("20895").estimateBuyCarDate("2035-12-20").build().modify(point, content).visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(Objects.equals(message, expect), description + "，期待结果=" + expect + "实际结果=" + message);
            sleep(3);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app接待中编辑资料,只填写必填项");
        }
    }

//    @Test(dataProvider = "customerLevel")
//    public void test08changeBuyDate_levelCheck(String buyDate,String expectLevel){
//        try{
//            String code = AppCustomerEditV4Scene.builder().id(newId.toString()).customerId(newCustomerId.toString()).shopId(newShopId.toString()).customerName("改购车时间").customerPhone("17" + numRandom(9)).sexId(1).intentionCarModelId("775").estimateBuyCarDate(buyDate).build().invoke(visitor, false).getString("code");
//            if (Objects.equals(code,"1000")){
//                String customerLevel = AppCustomerDetailV4Scene.builder().shopId(newShopId.toString()).customerId(newCustomerId.toString()).id(newId.toString()).build().invoke(visitor, true).getString("customer_level_name");
//                Preconditions.checkArgument(Objects.equals(expectLevel,customerLevel),"修改预计购车日期为"+buyDate+",预期等级应变更为"+expectLevel+",实际等级"+customerLevel);
//            }
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("修改购车时间，校验客户等级");
//        }
//    }
//    @DataProvider(name = "customerLevel")
//    public Object[] levelCheck(){
//        DateTimeUtil time = new DateTimeUtil();
//        return new String[][]{
//                {time.getHistoryDate(7),"H"},
//                {time.getHistoryDate(30),"A"},
//                {time.getHistoryDate(90),"B"},
//                {time.getHistoryDate(180),"C"},
//        };
//    }

    @AfterClass
    public void test09PcComplete() {
        try {
            if (newId != null && newCustomerId != null) {
                FinishReceptionScene.builder().id(newId).shopId(newShopId).build().visitor(visitor).execute();
                Boolean isFinish = PreSalesReceptionPageScene.builder().build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getBoolean("is_finish");
                Preconditions.checkArgument(isFinish, "完成接待操作失败，接待id=" + newId);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("Pc完成接待");
        }
    }
//    @Test
//    public void test05AfterCompleteMark(){
//       try{
//           String code = CustomerRemarkScene.builder().id(newId).shopId(newShopId).remark("完成接待之后填写备注12345678").build().invoke(visitor, false).getString("code");
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
