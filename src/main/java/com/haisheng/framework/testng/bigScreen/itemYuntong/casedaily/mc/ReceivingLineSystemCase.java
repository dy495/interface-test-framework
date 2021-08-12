package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc;

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

public class ReceivingLineSystemCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.YT_DAILY_ZH; // 管理页—-首页
    private static final EnumAccount YT_RECEPTION_DAILY = EnumAccount.YT_RECEPTION_DAILY_M; // 全部权限账号 【运通】
    public VisitorProxy visitor = new VisitorProxy(product);   // 产品类放到代理类中（通过代理类发请求）
    public SceneUtil util = new SceneUtil(visitor);
    public YunTongInfo info = new YunTongInfo();
    CommonConfig commonConfig = new CommonConfig();    // 配置类初始化
    public Long newId; // 本次创建的接待id
    public String newShopId = YT_RECEPTION_DAILY.getReceptionShopId(); // 本次接待门店的shopId
    public Long newCustomerId;

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
        commonConfig.setShopId(YT_RECEPTION_DAILY.getShopId()).setReferer(product.getReferer()).setRoleId(YT_RECEPTION_DAILY.getRoleId()).setProduct(product.getAbbreviation());
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

    @Test
    public void test01CustomerConfig() {
        visitor.setProduct(EnumTestProduct.YT_DAILY_JD);
        try {
            String phone = "15" + numRandom(9);
            AppPreSalesReceptionCreateScene.builder().customerName("mc自动化创建使用").customerPhone(phone).sexId("1").intentionCarModelId(util.mcCarId()).estimateBuyCarTime("2035-07-12").build().invoke(visitor);//创建销售接待
            JSONObject pageInfo = AppPreSalesReceptionPageScene.builder().build().invoke(visitor, true);
            List<JSONObject> newCustomer = pageInfo.getJSONArray("list").stream().map(ele -> (JSONObject) ele).filter(obj -> phone.equals(obj.getString("customer_phone"))).collect(Collectors.toList());
            Long id = newCustomer.get(0).getLong("id");
//            Long shopId = newCustomer.get(0).getLong("shop_id");
            Long customerId = newCustomer.get(0).getLong("customer_id");
            this.newId = id;
//            this.newShopId = shopId;
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
    public void test02PcRemark(String description, String expect, String remark) {
        try {
            if (newId != null && newCustomerId != null) {
                AppCustomerDetailV4Scene detail = AppCustomerDetailV4Scene.builder().shopId(newShopId).customerId(newCustomerId.toString()).id(newId.toString()).build();
                //            int beforeSum = detail.invoke(visitor, true).getJSONArray("remarks").size();
                String code = CustomerRemarkScene.builder().id(newId).shopId(Long.parseLong(newShopId)).remark(remark).build().invoke(visitor, false).getString("code");
                JSONArray remarks = detail.invoke(visitor, true).getJSONArray("remarks");
                //            int afterSum = remarks.size();
                String addedRemark = remarks.getJSONObject(0).getString("remark");
                Preconditions.checkArgument(Objects.equals(code, expect), description + ",预期:" + expect + ",实际结果:" + code);
                if (Objects.equals("1000", code)) {
                    Preconditions.checkArgument(Objects.equals(addedRemark, remark), "备注内容不一致，pc输入的备注内容" + remark + ",app接待详情中备注记录:" + addedRemark);
                }
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待中，PC备注");
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
            if (newId != null && newCustomerId != null) {
                String code = BuyCarScene.builder().carModel(676L).carStyle(1398L).id(newId).shopId(Long.parseLong(newShopId)).vin(vin).build().invoke(visitor, false).getString("code");
                Preconditions.checkArgument(Objects.equals(code, expect), description + ",预期code:" + expect + "实际code=" + code);
                if (Objects.equals(expect, "1000") && vin.length() != 0) {
                    String chassisCode = PreSaleCustomerInfoBuyCarRecordScene.builder().customerId(newCustomerId).shopId(Long.parseLong(newShopId)).build().invoke(visitor, true).getJSONArray("list").getJSONObject(0).getString("vehicle_chassis_code");
                    Preconditions.checkArgument(Objects.equals(chassisCode, vin.toUpperCase()), "详情中底盘号不一致,输入:" + vin + "实际:" + chassisCode);
                }
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待中，PC确认购车");
        }
    }

    @DataProvider(name = "chassisCode")
    public Object[] chassisCode() {
        return new String[][]{
                {"底盘号17位英文+数字", "1000", "aaaabbbc" + numRandom(9)},
                {"底盘号16位英文+数字", "1001", "aaaabbbc" + numRandom(8)},
                {"底盘号18位英文+数字", "1001", "aaaabbbc" + numRandom(10)},
                {"底盘号为空", "1000", ""},
                {"系统存在的底盘号", "1001", "ABC12345678901234"}
        };
    }

    @Test(dataProvider = "remarkContent")
    public void test04AppRemark(String description, String expect, String remark) {
        try {
            if (newId != null && newCustomerId != null) {
                String code = AppCustomerRemarkV4Scene.builder().id(newId.toString()).shopId(newShopId.toString()).customerId(newCustomerId.toString()).remark(remark).build().invoke(visitor, false).getString("code");
                String addedRemark = AppCustomerDetailV4Scene.builder().shopId(newShopId.toString()).customerId(newCustomerId.toString()).id(newId.toString()).build().invoke(visitor, true).getJSONArray("remarks").getJSONObject(0).getString("remark");
                Preconditions.checkArgument(Objects.equals(code, expect), description + ",预期:" + expect + ",实际结果:" + code);
                if (Objects.equals("1000", code)) {
                    Preconditions.checkArgument(Objects.equals(addedRemark, remark), "备注内容不一致，app输入的备注内容" + remark + ",app接待详情中备注记录:" + addedRemark);
                }
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app接待中备注");
        }
    }
//     没有相关接口
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
    //     失败
//    @Test(dataProvider = "chassisCode")
//    public void test06ChangeChassisCode(String description, String expect, String vin){
//        try {
//            if(newId != null && newCustomerId != null) {
//                String code = VehicleEditScene.builder().carModel(676L).id(newId).vin(vin).build().invoke(visitor, false).getString("code");
//                Preconditions.checkArgument(Objects.equals(code, expect), description + ",预期code:" + expect + "实际code=" + code);
//                if (Objects.equals(expect, "1000") && vin.length() != 0) {
//                    String chassisCode = PreSaleCustomerInfoBuyCarRecordScene.builder().customerId(newCustomerId).shopId(newShopId).build().invoke(visitor, true).getJSONArray("list").getJSONObject(0).getString("vehicle_chassis_code");
//                    Preconditions.checkArgument(Objects.equals(chassisCode, vin.toUpperCase()), "详情中底盘号不一致,输入:" + vin + "实际:" + chassisCode);
//                }
//            }
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("app接待中修改底盘号");
//        }
//    }

    @Test(dataProvider = "userInfo")
    public void test07ChangeUserInfo(String description, String expect, String name, String phone, Integer sex) {
        try {
            if (newId != null && newCustomerId != null) {
                String code = AppCustomerEditV4Scene.builder().id(newId.toString()).customerId(newCustomerId.toString()).shopId(newShopId.toString()).customerName(name).customerPhone(phone).sexId(sex).intentionCarModelId("775").estimateBuyCarDate("2035-12-20").build().invoke(visitor, false).getString("code");
                Preconditions.checkArgument(Objects.equals(code, expect), description + "，期待结果code=" + expect + "实际结果code=" + code);
                sleep(3);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app接待中编辑资料,只填写必填项");
        }
    }

    @DataProvider(name = "userInfo")
    public Object[] userInfo() {
        return new Object[][]{
                {"用户姓名1位，不存在的正常手机号", "1000", "1", "18" + numRandom(9), 1},
                {"用户姓名50位，不存在的正常手机号", "1000", info.stringfifty, "18" + numRandom(9), 1},
                //{"用户姓名51位，不存在的正常手机号","1001", info.stringfifty1,"18"+numRandom(9),1}, //系统繁忙
                {"用户姓名正常，手机号10位", "1001", info.stringsix, "18" + numRandom(8), 1},
                {"用户姓名正常，手机号11位含非数字", "1001", info.stringsix, "ab" + numRandom(8), 1},
        };
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


    @Test
    public void test09PcComplete() {
        try {
            if (newId != null && newCustomerId != null) {
                FinishReceptionScene.builder().id(newId).shopId(Long.parseLong(newShopId)).build().invoke(visitor);
                Boolean isFinish = PreSalesReceptionPageScene.builder().build().invoke(visitor, true).getJSONArray("list").getJSONObject(0).getBoolean("is_finish");
                Preconditions.checkArgument(isFinish, "完成接待操作失败，接待id=" + newId);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
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
