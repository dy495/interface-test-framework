package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.MyUtil.TopicUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppCustomerDetailV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionCreateScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanagev4.PreSaleCustomerInfoBuyCarRecordScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.BuyCarScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.CustomerRemarkScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.FinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class ReceivingRecordCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.YT_DAILY_SSO; // 管理页—-首页
    private static final EnumAccount YT_RECEPTION_DAILY = EnumAccount.YT_RECEPTION_DAILY; // 全部权限账号 【运通】
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);   // 产品类放到代理类中（通过代理类发请求）
    public SceneUtil util = new TopicUtil(visitor);    //场景工具类中放入代理类，类中封装接口方法直接调用
    CommonConfig commonConfig = new CommonConfig();    // 配置类初始化
    Long newId; // 本次创建的接待id
    Long newShopId; // 本次接待门店的shopId
    Long newCustomerId;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = "孟辰";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.product = PRODUCE.getAbbreviation(); // 产品代号 -- YT
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = PRODUCE.getShopId();  //请求头放入shopId
        commonConfig.roleId = YT_RECEPTION_DAILY.getRoleId(); //请求头放入roleId
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
    }
    // 随机n位数字
    private String numRandom(Integer n) {
        Random ran = new Random();
        String numStr = "";
        for (int i = 0; i < n; i++) {
            String num = ran.nextInt(9) + "";
            numStr += num;
        }
        return numStr;
    }

    // 创建一个接待，
    //return：接待id 和 shop_id
//    private void createCustomer() {
//        visitor.setProduct(EnumTestProduce.YT_DAILY_CAR);
//        HashMap<String,Long> customer = new HashMap<>();
//        String phone = "15" + phoneRandom(9);
//        AppPreSalesReceptionCreateScene.builder().customerName("自动化创建测试使用mc").customerPhone(phone).sexId("1").intentionCarModelId("676").estimateBuyCarTime("2035-07-12").build().invoke(visitor);//创建销售接待
//        JSONObject pageInfo = PreSalesReceptionPageScene.builder().build().invoke(visitor, true);
//        List<JSONObject> newCustomer = pageInfo.getJSONArray("list").stream().map(ele -> (JSONObject) ele).filter(obj -> phone.equals(obj.getString("customer_phone"))).collect(Collectors.toList());
//        Long id = newCustomer.get(0).getLong("id");
//        Long shopId = pageInfo.getJSONArray("list").getJSONObject(0).getLong("shop_id");
//        customer.put("id",id);
//        customer.put("shopId",shopId);
//        return customer;
//    }

    @Test
    public void testtest(){

    }

    @Test
    public void test01CustomerConfig() {
        visitor.setProduct(EnumTestProduce.YT_DAILY_CAR);
        try {
            String phone = "15" + numRandom(9);
            AppPreSalesReceptionCreateScene.builder().customerName("mc自动化创建使用").customerPhone(phone).sexId("1").intentionCarModelId("676").estimateBuyCarTime("2035-07-12").build().invoke(visitor);//创建销售接待
            JSONObject pageInfo = AppPreSalesReceptionPageScene.builder().build().invoke(visitor, true);
            List<JSONObject> newCustomer = pageInfo.getJSONArray("list").stream().map(ele -> (JSONObject) ele).filter(obj -> phone.equals(obj.getString("customer_phone"))).collect(Collectors.toList());
            Long id = newCustomer.get(0).getLong("id");
            Long shopId = newCustomer.get(0).getLong("shop_id");
            Long customerId = newCustomer.get(0).getLong("customer_id");
            this.newId = id;
            this.newShopId = shopId;
            this.newCustomerId = customerId;
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手动创建接待");
        }

    }

    @Test(dataProvider = "remarkContent")
    public void test02ReceiveRemark(String description, String expect, String remark) {
        try {
            AppCustomerDetailV4Scene detail = AppCustomerDetailV4Scene.builder().shopId(newShopId.toString()).customerId(newCustomerId.toString()).id(newId.toString()).build();
//            int beforeSum = detail.invoke(visitor, true).getJSONArray("remarks").size();
            String code = CustomerRemarkScene.builder().id(newId).shopId(newShopId).remark(remark).build().invoke(visitor, false).getString("code");
            JSONArray remarks = detail.invoke(visitor, true).getJSONArray("remarks");
//            int afterSum = remarks.size();
            String addedRemark = remarks.getJSONObject(0).getString("remark");
            Preconditions.checkArgument(Objects.equals(code,expect), description + ",预期:" + expect + ",实际结果:" + code);
            if(Objects.equals("1000",expect)) {
                Preconditions.checkArgument(remark.equals(addedRemark), "备注内容不一致，输入的备注内容" + remark + ",app接待详情中备注记录:" + addedRemark);
            }
        } catch (AssertionError e) {
        appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待中，备注");
        }
    }

    @DataProvider(name = "remarkContent")
    private Object[] remark() {
        return new Object[][]{
                {"备注长度10个字符", "1000", "@%^#*?><jh"},
                {"备注长度9个字符", "1001", "zsdfghyjh"},
                {"备注长度0个字符", "1001", ""},
                {"符合长度限制的字符组合", "1000", "!_=-][/?ASF你        我他slj,.  l;'\nffflllai"},
        };
    }

    @Test(dataProvider = "chassisCode")
    public void test03BuyCar(String description, String expect, String vin) {
        try {
            String code = BuyCarScene.builder().carModel(676L).carStyle(1398L).id(newId).shopId(newShopId).vin(vin).build().invoke(visitor, false).getString("code");
            Preconditions.checkArgument(Objects.equals(code,expect),description+",预期code:"+expect+"实际code="+code);
            if(expect.equals(code)){
                String chassisCode = PreSaleCustomerInfoBuyCarRecordScene.builder().customerId(newCustomerId).shopId(newShopId).build().invoke(visitor, true).getJSONArray("list").getJSONObject(0).getString("vehicle_chassis_code");
                Preconditions.checkArgument(Objects.equals(chassisCode,vin.toUpperCase()),"详情中底盘号不一致,输入:"+vin+"实际:"+chassisCode);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待中，确认购车");
        }
    }
    @DataProvider(name = "chassisCode")
    public Object[] chassisCode(){
        return new String[][]{
                {"底盘号17位英文+数字","1000","aaaabbbc"+numRandom(9)}
        };
    }

    @Test
    public void test04Complete(){
        try{
            FinishReceptionScene.builder().id(newId).shopId(newShopId).build().invoke(visitor);
            Boolean isFinish = PreSalesReceptionPageScene.builder().build().invoke(visitor, true).getJSONArray("list").getJSONObject(0).getBoolean("is_finish");
            Preconditions.checkArgument(isFinish,"完成接待操作失败，接待id="+newId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("完成接待");
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
