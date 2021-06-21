package com.haisheng.framework.testng.bigScreen.yuntong.lxq;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.customermanage.*;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.util.BusinessUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * @author lxq
 * @date 2021/1/29 11:17
 */
public class SystemCase extends TestCaseCommon implements TestCaseStd {
    EnumTestProduce PRODUCE = EnumTestProduce.YT_DAILY_ZT;
    EnumAccount ALL_AUTHORITY = EnumAccount.ALL_YT_DAILY;
    VisitorProxy visitor = new VisitorProxy(PRODUCE);
    UserUtil user = new UserUtil(visitor);
    SupporterUtil util = new SupporterUtil(visitor);

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

        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);

    }

    @Test(dataProvider = "CSTMINFO")
    public void newPotentialCustomer(String name, String phone, String type, String sex, String mess, String chk) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            EnumTestProduce PRODUCE = EnumTestProduce.YT_DAILY_ZH;
            EnumAccount ALL_AUTHORITY = EnumAccount.ALL_YT_DAILY;
            VisitorProxy visitor = new VisitorProxy(PRODUCE);
            BusinessUtil businessUtil = new BusinessUtil(visitor);
            businessUtil.loginPc(ALL_AUTHORITY);

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
//                {info.stringfifty, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "CORPORATION", "1", "姓名50个字", "true"},
//                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 100)), "CORPORATION", "1", "手机号10位", "false"},
//                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), "CORPORATION", "1", "手机号12位", "false"},
//                {info.stringfifty + "1", "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "CORPORATION", "1", "姓名51位", "false"},

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

    /**
     * -------------------------------系统配置------------------------------------
     */
    /**
     * PC 品牌管理-系统测试
     */


    //品牌--正常
//    @Test
//    public void addBrand_name1() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            int code = AddScene.builder().name(info.stringone).logoPath(info.getLogo()).build().invoke(visitor,false).getInteger("code");
//
//            Preconditions.checkArgument(code == 1000, "状态码期待1000，实际" + code);
//
//            //删除品牌
//            Long id = PageScene.builder().page(1).size(10).build().invoke(visitor).JSONArray("list").getJSONObject(0).getLong("id");
//            DeleteScene.builder().id(id).build().invoke(visitor);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
////            saveData("PC【品牌管理】，创建品牌，名称1个字");
//        }
//
//    }
//
//    @Test
//    public void addBrand_name10() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            int code = jc.addBrandNotChk(info.stringten, info.getLogo()).getInteger("code");
//            Preconditions.checkArgument(code == 1000, "状态码期待1000，实际" + code);
//
//            //删除品牌
//            Long id = jc.brandPage(1, 10, "", "").getJSONArray("list").getJSONObject(0).getLong("id");
//            jc.delBrand(id);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，创建品牌，名称10个字");
//        }
//
//    }
//
//    @Test
//    public void editBrand_name() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //创建一个品牌
//            String name1 = info.stringsix;
//            String name2 = info.stringsix + "aaa";
//            jc.addBrand(name1, info.getLogo());
//            //获取创建的品牌id
//            Long id = jc.brandPage(1, 10, "", "").getJSONArray("list").getJSONObject(0).getLong("id");
//            //修改这个品牌的名字
//            jc.editBrand(id, name2, info.getLogo());
//            //根据id查询，名字为name2
//            JSONArray arr = jc.brandPage(1, 100, "", "").getJSONArray("list");
//            for (int i = 0; i < arr.size(); i++) {
//                JSONObject obj = arr.getJSONObject(i);
//                if (obj.getLong("id") == id) {
//                    Preconditions.checkArgument(obj.getString("name").equals(name2), "修改前名字是" + name1 + "，期望修改为" + name2 + "，实际修改后为" + obj.getString("name"));
//                }
//            }
//
//            //删除品牌
//            jc.delBrand(id);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，创建品牌后进行修改");
//        }
//
//    }
//
//    //品牌--异常
//    @Test
//    public void addBrand_nameerr() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String name = "12345aA啊！@1";
//            int code = jc.addBrandNotChk(name, info.getLogo()).getInteger("code");
//            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，创建品牌，名称11个字");
//        }
//
//    }
//
//    //品牌车系--正常
//
//    @Test(dataProvider = "CAR_STYLE")
//    public void addCarStyle(String manufacturer, String name, String online_time) {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            int code = jc.addCarStyleNotChk(info.BrandID, manufacturer, name, online_time).getInteger("code");
//            Preconditions.checkArgument(code == 1000, "创建车系：生产商 " + manufacturer + ", 车系 " + name + ", 上线日期" + online_time + "状态码" + code);
//
//            //删除品牌车系
//            Long id = jc.carStylePage(1, 1, info.BrandID, "").getJSONArray("list").getJSONObject(0).getLong("id");
//            jc.delCarStyle(id);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，创建车系");
//        }
//    }
//
//    @Test
//    public void editCarStyle() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            //创建车系
//            String manufacturer = "旧生产商";
//            String name = "旧车系";
//            String online_time = dt.getHistoryDate(0);
//            jc.addCarStyle(info.BrandID, manufacturer, name, online_time);
//            //获取车系id
//            Long id = jc.carStylePage(1, 1, info.BrandID, name).getJSONArray("list").getJSONObject(0).getLong("id");
//            //修改车系
//            String manufacturer1 = "新生产商";
//            String name1 = "新车系";
//            String online_time1 = dt.getHistoryDate(-2);
//            jc.editCarStyle(id, info.BrandID, manufacturer1, name1, online_time1);
//            //查看修改结果
//            JSONObject obj = jc.carStylePage(1, 30, info.BrandID, "").getJSONArray("list").getJSONObject(0);
//            String search_manufacturer1 = obj.getString("manufacturer");
//            String search_name1 = obj.getString("name");
//            String search_online_time1 = obj.getString("online_time");
//
//            Preconditions.checkArgument(search_manufacturer1.equals(manufacturer1), "修改前生产商=" + manufacturer + "，期望修改为" + manufacturer1 + "，实际修改后为" + search_manufacturer1);
//            Preconditions.checkArgument(search_name1.equals(name1), "修改前车系=" + name + "，期望修改为" + name1 + "，实际修改后为" + search_name1);
//            Preconditions.checkArgument(search_online_time1.equals(online_time1), "修改前上线时间=" + online_time + "，期望修改为" + online_time1 + "，实际修改后为" + search_online_time1);
//
//
//            //删除品牌车系
//            jc.delCarStyle(id);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，修改车系");
//        }
//    }
//
//    @Test
//    public void addOneCarStyleinTwoModel() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            //创建品牌1
//            Long brandid1 = info.getBrandID(5);
//            //创建品牌2
//            Long brandid2 = info.getBrandID(6);
//
//            //创建车系
//            String manufacturer = "生产商重复";
//            String name = "车系重复";
//            String online_time = dt.getHistoryDate(0);
//            jc.addCarStyle(brandid1, manufacturer, name, online_time);
//
//            int code = jc.addCarStyleNotChk(brandid2, manufacturer, name, online_time).getInteger("code");
//            Preconditions.checkArgument(code == 1000, "期待状态码1000，实际" + code);
//
//            //删除品牌
//            jc.delBrand(brandid1);
//            jc.delBrand(brandid2);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，不同品牌下创建相同车系，期待成功");
//        }
//    }
//
//
//    @DataProvider(name = "CAR_STYLE")
//    public Object[] carStyle() {
//        return new String[][]{
//                {info.stringone, info.stringone, dt.getHistoryDate(0)},
//                {info.stringfifty, info.stringfifty, dt.getHistoryDate(-1)},
//                {info.stringsix, info.stringsix, dt.getHistoryDate(1)},
//        };
//    }
//
//    //品牌车系--异常
//    @Test(dataProvider = "CAR_STYLEERR")
//    public void addCarStyleErr(String manufacturer, String name, String online_time, String yz) {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONObject obj = jc.addCarStyleNotChk(info.BrandID, manufacturer, name, online_time);
//            int code = obj.getInteger("code");
//            String message = obj.getString("message");
//            Preconditions.checkArgument(code == 1001, yz + "期待状态码1001，实际" + code + ",提示语：" + message);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，创建车系，生产商/车系51字");
//        }
//    }
//
//    @DataProvider(name = "CAR_STYLEERR")
//    public Object[] carStyle_err() {
//        return new String[][]{
//                {info.stringfifty1, info.stringone, dt.getHistoryDate(0), "生产商51个字"},
//                {info.stringfifty, info.stringfifty1, dt.getHistoryDate(-1), "车系51个字"},
//
//        };
//    }
//
//    @Test
//    public void addTwoCarStyleinOneModel() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            //创建品牌1
//            Long brandid1 = info.getBrandID(5);
//
//            //创建车系
//            String manufacturer = "生产商重复";
//            String name = "车系重复";
//            String online_time = dt.getHistoryDate(0);
//
//            jc.addCarStyle(brandid1, manufacturer, name, online_time);
//
//            JSONObject obj = jc.addCarStyleNotChk(brandid1, manufacturer, name, online_time);
//            int code = obj.getInteger("code");
//            String message = obj.getString("message");
//            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + ",提示语：" + message);
//
//
//            //删除品牌
//            jc.delBrand(brandid1);
//
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，相同品牌下创建相同车系，期待失败");
//        }
//    }
//
//    @Test
//    public void addCarStyleinNotExistModel() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            //创建品牌1
//            Long brandid1 = info.getBrandID(5);
//
//            //删除品牌
//            jc.delBrand(brandid1);
//
//            //添加车系
//            String manufacturer = "自动化" + System.currentTimeMillis();
//            String name = "自动化" + System.currentTimeMillis();
//            String online_time = dt.getHistoryDate(0);
//            JSONObject obj = jc.addCarStyleNotChk(brandid1, manufacturer, name, online_time);
//            int code = obj.getInteger("code");
//            String message = obj.getString("message");
//            Preconditions.checkArgument(code == 1001, "实际状态码" + code + ", 提示语为：" + message);
//
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，在某品牌下创建车系时，品牌被删除，期待失败");
//        }
//    }
//
//
//    //品牌车系车型 --正常
//    @Test(dataProvider = "CAR_MODEL")
//    public void addCarModel(String name, String year, String status) {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            int code = jc.addCarModelNotChk(info.BrandID, info.CarStyleID, name, year, status).getInteger("code");
//            Preconditions.checkArgument(code == 1000, "创建车系：车型名称 " + name + ", 年款 " + year + ", 预约状态" + status + "状态码" + code);
//
//            //删除品牌车系
//            Long id = jc.carModelPage(1, 1, info.BrandID, info.CarStyleID, name, "", "").getJSONArray("list").getJSONObject(0).getLong("id");
//            jc.delCarModel(id);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，创建车型");
//        }
//    }
//
//    @DataProvider(name = "CAR_MODEL")
//    public Object[] carModel() {
//        return new String[][]{
//                {"x", "1", "ENABLE"},
//                {info.stringfifty, "2000年", "ENABLE"},
//                {info.stringsix, "12345啊啊啊啊啊！@#qweQWER", "DISABLE"},
//        };
//    }
//
//    @Test
//    public void editCarModel() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            //创建车型
//            String name1 = "旧车型名称" + System.currentTimeMillis();
//            String year1 = "2000年";
//            String status1 = "ENABLE";
//            jc.addCarModel(info.BrandID, info.CarStyleID, name1, year1, status1);
//            //获取车系id
//            int size = jc.carModelPage(1, 1, info.BrandID, info.CarStyleID, name1, "", "").getInteger("total");
//            Long id = jc.carModelPage(1, size, info.BrandID, info.CarStyleID, name1, "", "").getJSONArray("list").getJSONObject(size - 1).getLong("id");
//            System.out.println(id + "---------");
//            //修改车型
//            String name2 = "新车型名称" + System.currentTimeMillis();
//            String year2 = "2020年";
//            String status2 = "DISABLE";
//            jc.editCarModel(id, info.BrandID, info.CarStyleID, name2, year2, status2);
//            //查看修改结果
//            String search_name2 = "";
//            String search_year2 = "";
//            String search_status2 = "";
//            int size1 = jc.carModelPage(1, 1, info.BrandID, info.CarStyleID, "", "", "").getInteger("total");
//            JSONArray arr = jc.carModelPage(1, size1, info.BrandID, info.CarStyleID, "", "", "").getJSONArray("list");
//            for (int i = size1 - 1; i > 0; i--) {
//                JSONObject obj = arr.getJSONObject(i);
//                System.out.println(obj + "-----------------");
//                if (obj.getLong("id").longValue() == id.longValue()) {
//
//                    search_name2 = obj.getString("name");
//                    search_year2 = obj.getString("year");
//                    search_status2 = obj.getString("status");
//                }
//            }
//
//
//            Preconditions.checkArgument(search_name2.equals(name2), "修改前车型名称=" + name1 + "，期望修改为" + name2 + "，实际修改后为" + search_name2);
//            Preconditions.checkArgument(search_year2.equals(year2), "修改前年款=" + year1 + "，期望修改为" + year2 + "，实际修改后为" + search_year2);
//            Preconditions.checkArgument(search_status2.equals(status2), "修改前状态=" + status1 + "，期望修改为" + status2 + "，实际修改后为" + search_status2);
//
//            //删除品牌车系车型
//            jc.delCarModel(id);
//
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，修改车型");
//        }
//    }
//
//    //品牌车系车型 --异常
//    @Test
//    public void addCarModel_err() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            String year = "1998年";
//            String status = "ENABLE";
//            JSONObject obj = jc.addCarModelNotChk(info.BrandID, info.CarStyleID, info.stringfifty1, year, status);
//            int code = obj.getInteger("code");
//            String message = obj.getString("message");
//            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + "， 提示语：" + message);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，创建车型， 名称51字");
//        }
//    }
//
//    //品牌车系车型 --异常
//    @Test
//    public void addCarModel_err3() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            String year = "12345啊啊啊啊啊！@#qweQWERQ";
//            String status = "ENABLE";
//            JSONObject obj = jc.addCarModelNotChk(info.BrandID, info.CarStyleID, info.stringsix, year, status);
//            int code = obj.getInteger("code");
//            String message = obj.getString("message");
//            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + "， 提示语：" + message);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，创建车型， 年款21个字");
//        }
//    }
//
//    @Test
//    public void addCarModel_err1() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            //新建品牌
//            Long brandid = info.getBrandID(7);
//            //新建车系
//            Long carStyleId = info.getCarStyleID(brandid, 5);
//
//            //删除车系
//            jc.delCarStyle(carStyleId);
//
//            //新建车型
//            String name1 = "自动化" + System.currentTimeMillis();
//            String year1 = "2019年";
//            String status1 = "ENABLE";
//            JSONObject obj = jc.addCarModelNotChk(brandid, carStyleId, name1, year1, status1);
//
//            //删除品牌
//            jc.delBrand(brandid);
//
//            int code = obj.getInteger("code");
//            String message = obj.getString("message");
//            Preconditions.checkArgument(code == 1001, "状态码为" + code + ", 提示语" + message);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，创建车型时车系被删除， 期待失败");
//        }
//    }
//
//    @Test
//    public void addCarModel_err2() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            //新建品牌
//            Long brandid = info.getBrandID(7);
//            //新建车系
//            Long carStyleId = info.getCarStyleID(brandid, 5);
//
//            //删除品牌
//            jc.delBrand(brandid);
//            //新建车型
//            String name1 = "自动化" + System.currentTimeMillis();
//            String year1 = "1009年";
//            String status1 = "ENABLE";
//            JSONObject obj = jc.addCarModelNotChk(brandid, carStyleId, name1, year1, status1);
//
//            int code = obj.getInteger("code");
//            String message = obj.getString("message");
//            Preconditions.checkArgument(code == 1001, "状态码为" + code + ", 提示语" + message);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【品牌管理】，创建车型时品牌被删除， 期待失败");
//        }
//    }


}
