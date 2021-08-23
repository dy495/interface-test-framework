package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.brand.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongInfo;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
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

    private final static EnumTestProduct PRODUCT = EnumTestProduct.YT_DAILY_JD;
    private final static EnumAccount ACCOUNT = EnumAccount.YT_DAILY_YS;
    VisitorProxy visitor = new VisitorProxy(PRODUCT);
    SceneUtil util = new SceneUtil(visitor);
    YunTongInfo info = new YunTongInfo();

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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCT.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.setShopId(PRODUCT.getShopId()).setRoleId(PRODUCT.getRoleId()).setProduct(PRODUCT.getAbbreviation());
        beforeClassInit(commonConfig);
        util.loginPc(ACCOUNT);
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

    /**
     * -------------------------------销售业务管理 - 销售客户管理 - 展厅客户 ------------------------------------
     */

    /**
     * 展厅客户-创建 正常&异常
     */

    @Test(dataProvider = "CSTMINFO") //bug 9608
    public void newPotentialCustomer(String name, String phone, String type, String sex, String mess, String chk) {

        logger.logCaseStart(caseResult.getCaseName());
        try {


            int bef = PreSaleCustomerPageScene.builder().page(1).size(1).build().execute(visitor).getInteger("total");

            Long shop_id = info.oneshopid;

            Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(shop_id).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = PreSaleCustomerSalesListScene.builder().shopId(shop_id).type("PRE").build().execute(visitor).getJSONArray("list").getJSONObject(0).getString("sales_id");

            int code = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).salesId(salesId).build().execute(visitor, false).getInteger("code");
            int after = PreSaleCustomerPageScene.builder().page(1).size(1).build().execute(visitor).getInteger("total");
            if (chk.equals("false")) {
                Preconditions.checkArgument(code == 1001, mess + "期待失败，实际" + code);
            } else {
                int sum = after - bef;
                Preconditions.checkArgument(code == 1000, mess + "期待创建成功，实际" + code);
                Preconditions.checkArgument(sum == 1, mess + "期待创建成功列表+1，实际增加" + sum);
                int code2 = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).salesId(salesId).build().execute(visitor, false).getInteger("code");
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
                {"我", getPhone(), "PERSON", "0", "姓名一个字", "true"},
                {info.stringfifty, getPhone(), "CORPORATION", "1", "姓名50个字", "true"},
                {info.stringsix, "1381172" + (int) ((Math.random() * 9 + 1) * 100), "CORPORATION", "1", "手机号10位", "false"},
                {info.stringsix, "1381172" + (int) ((Math.random() * 9 + 1) * 10000), "CORPORATION", "1", "手机号12位", "false"},
                {info.stringfifty + "1", "1381172" + (int) ((Math.random() * 9 + 1) * 1000), "CORPORATION", "1", "姓名51位", "false"},

        };
    }

    public String getPhone() {
        return util.getNotExistPhone();
    }

    @Test
    public void newPotentialCustomerErr() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long shop_id = info.oneshopid;
            Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(shop_id).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = PreSaleCustomerSalesListScene.builder().shopId(shop_id).type("PRE").build().execute(visitor).getJSONArray("list").getJSONObject(0).getString("sales_id");

            String name = "name" + System.currentTimeMillis();
            String phone = "1391172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));
            String type = "PERSON";
            String sex = "0";
            //不填写姓名
            int code = PreSaleCustomerCreatePotentialCustomerScene.builder().customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).salesId(salesId).build().execute(visitor, false).getInteger("code");

            Preconditions.checkArgument(code == 1001, "不填写姓名期待失败，实际" + code);

            //不填写手机号
            int code1 = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).salesId(salesId).build().execute(visitor, false).getInteger("code");

            Preconditions.checkArgument(code1 == 1001, "不填写手机号期待失败，实际" + code);

            //不填写类型
            int code2 = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).salesId(salesId).build().execute(visitor, false).getInteger("code");

            Preconditions.checkArgument(code2 == 1001, "不填写车主类型期待失败，实际" + code);

            //不填写性别
            int code3 = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).salesId(salesId).build().execute(visitor, false).getInteger("code");

            Preconditions.checkArgument(code3 == 1001, "不填写性别期待失败，实际" + code);

            //不填写意向车型
            int code4 = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).shopId(shop_id).salesId(salesId).build().execute(visitor, false).getInteger("code");

            Preconditions.checkArgument(code4 == 1001, "不填写意向车型期待失败，实际" + code);

            //不填写所属门店
            int code5 = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).build().execute(visitor, false).getInteger("code");

            Preconditions.checkArgument(code5 == 1001, "不填写所属门店期待失败，实际" + code);

            //不填写所属销售 bug 9609
            int code6 = PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).build().execute(visitor, false).getInteger("code");

            Preconditions.checkArgument(code6 == 1001, "不填写所属销售期待失败，实际" + code6);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建潜客");
        }
    }

    /**
     * 展厅客户-筛选
     */
    @Test
    public void PotentialCustomerFilter1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONObject obj = PreSaleCustomerPageScene.builder().page(1).size(1).build().execute(visitor).getJSONArray("list").getJSONObject(0);
            String customer_name = obj.getString("customer_name"); //客户姓名
            String customer_phone = obj.getString("customer_phone"); //客户手机号
            String date = obj.getString("create_date").substring(0, 10); // 创建日期
            String sale_name = obj.getString("sale_name"); //销售顾问
            String subject_type = obj.getString("subject_type"); //客户类型

            //已有姓名
            JSONArray namelist = PreSaleCustomerPageScene.builder().page(1).size(30).customerName(customer_name).build().execute(visitor).getJSONArray("list");
            int nametotal = namelist.size();
            Preconditions.checkArgument(nametotal > 0, "搜索列表中存在的姓名" + customer_name + ",结果无数据");
            for (int i = 0; i < nametotal; i++) {
                JSONObject obj1 = namelist.getJSONObject(i);
                Preconditions.checkArgument(obj1.getString("customer_name").contains(customer_name), "搜索" + customer_name + " ,结果包含" + obj1.getString("customer_name"));
            }

            //已有手机号
            JSONArray phonelist = PreSaleCustomerPageScene.builder().page(1).size(30).customerPhone(customer_phone).build().execute(visitor).getJSONArray("list");
            int phonetotal = phonelist.size();
            Preconditions.checkArgument(phonetotal > 0, "搜索列表中存在的手机号" + customer_phone + ",结果无数据");
            for (int i = 0; i < phonetotal; i++) {
                JSONObject obj1 = phonelist.getJSONObject(i);
                Preconditions.checkArgument(obj1.getString("customer_phone").contains(customer_phone), "搜索" + customer_phone + " ,结果包含" + obj1.getString("customer_phone"));
            }

            //已有日期
            JSONArray datelist = PreSaleCustomerPageScene.builder().page(1).size(30).startTime(date).endTime(date).build().execute(visitor).getJSONArray("list");
            int datetotal = datelist.size();
            Preconditions.checkArgument(datetotal > 0, "搜索列表中存在的时间" + date + ",结果无数据");
            for (int i = 0; i < datetotal; i++) {
                JSONObject obj1 = datelist.getJSONObject(i);
                Preconditions.checkArgument(obj1.getString("create_date").contains(date), "搜索" + date + " ,结果包含" + obj1.getString("create_date"));
            }

            //已有销售顾问
            JSONArray salelist = PreSaleCustomerPageScene.builder().page(1).size(30).saleName(sale_name).build().execute(visitor).getJSONArray("list");
            int saletotal = salelist.size();
            Preconditions.checkArgument(saletotal > 0, "搜索列表中存在的销售顾问" + sale_name + ",结果无数据");
            for (int i = 0; i < saletotal; i++) {
                JSONObject obj1 = salelist.getJSONObject(i);
                Preconditions.checkArgument(obj1.getString("sale_name").contains(sale_name), "搜索" + sale_name + " ,结果包含" + obj1.getString("sale_name"));
            }

            //已有客户类型
            JSONArray subjectlist = PreSaleCustomerPageScene.builder().page(1).size(30).customerType(subject_type).build().execute(visitor).getJSONArray("list");
            int subjecttotal = subjectlist.size();
            Preconditions.checkArgument(subjecttotal > 0, "搜索列表中存在的客户类型" + subject_type + ",结果无数据");
            for (int i = 0; i < subjecttotal; i++) {
                JSONObject obj1 = subjectlist.getJSONObject(i);
                Preconditions.checkArgument(obj1.getString("subject_type").contains(subject_type), "搜索" + subject_type + " ,结果包含" + obj1.getString("subject_type"));
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("展厅客户根据已有信息筛选");
        }
    }

    @Test(dataProvider = "FILTER", dataProviderClass = YunTongInfo.class)
    public void PotentialCustomerFilter2(String search) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            //姓名
            JSONArray namelist = PreSaleCustomerPageScene.builder().page(1).size(30).customerName(search).build().execute(visitor).getJSONArray("list");
            if (namelist.size() > 0) {
                for (int i = 0; i < namelist.size(); i++) {
                    JSONObject obj1 = namelist.getJSONObject(i);
                    Preconditions.checkArgument(obj1.getString("customer_name").toUpperCase().contains(search), "搜索" + search + " ,结果包含" + obj1.getString("customer_name"));
                }
            }

            //手机号
            JSONArray phonelist = PreSaleCustomerPageScene.builder().page(1).size(30).customerPhone(search).build().execute(visitor).getJSONArray("list");
            if (phonelist.size() > 0) {
                for (int i = 0; i < phonelist.size(); i++) {
                    JSONObject obj1 = phonelist.getJSONObject(i);
                    Preconditions.checkArgument(obj1.getString("customer_phone").contains(search), "搜索" + search + " ,结果包含" + obj1.getString("customer_phone"));
                }
            }


            //销售顾问
            JSONArray salelist = PreSaleCustomerPageScene.builder().page(1).size(30).saleName(search).build().execute(visitor).getJSONArray("list");
            if (salelist.size() > 0) {
                for (int i = 0; i < salelist.size(); i++) {
                    JSONObject obj1 = salelist.getJSONObject(i);
                    Preconditions.checkArgument(obj1.getString("sale_name").toUpperCase().contains(search), "搜索" + search + " ,结果包含" + obj1.getString("sale_name"));
                }
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("展厅客户根据已有信息筛选");
        }
    }

    /**
     * 展厅客户-详情-编辑资料
     */
    @Test
    public void cstmEdit() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            //正常编辑
            Long custID = info.newPotentialCstm();

            String name = "修改后" + dt.getHistoryDate(0) + Integer.toString((int) ((Math.random() * 9 + 1) * 100));
            String phone = "138" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)) + "7788";
            String type = "PERSON";
            int sex = 1;
            Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(info.oneshopid).build().execute(visitor).getJSONArray("list").getJSONObject(1).getLong("style_id");
            Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("model_id");
            JSONObject obj = PreSaleCustomerEditScene.builder().customerId(custID).customerName(name).customerPhone(phone).subjectType(type).sex(sex).carStyleId(car_style_id).intentionCarModelId(car_model_id).shopId(info.oneshopid).build().execute(visitor, false);
            int code = obj.getInteger("code");
            if (code != 1000) {
                String message = obj.getString("message");
                Preconditions.checkArgument(code == 1000, "编辑客户信息提示" + message);
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("展厅客户编辑资料");
        }
    }

    @Test // bug 9686
    public void cstmEditErr() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

//            Long custID = info.newPotentialCstm();
            Long custID = 310318L;

            String name = "修改后" + dt.getHistoryDate(0) + Integer.toString((int) ((Math.random() * 9 + 1) * 100));
            String phone = "138" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)) + "7788";
            String type = "PERSON";
            int sex = 1;
            Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(info.oneshopid).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("model_id");


//            //姓名51字
//            JSONObject obj = PreSaleCustomerEditScene.builder().customerId(custID).customerName(info.stringfifty1).customerPhone(phone).subjectType(type).sex(sex).carStyleId(car_style_id).intentionCarModelId(car_model_id).shopId(info.oneshopid).build().invoke(visitor,false);
//            int code = obj.getInteger("code");
//            Preconditions.checkArgument(code==1001,"编辑客户姓名51字，期待失败，实际"+code);

//            //手机号12位
//            JSONObject obj2 = PreSaleCustomerEditScene.builder().customerId(custID).customerName(name).customerPhone(phone+"1").subjectType(type).sex(sex).carStyleId(car_style_id).intentionCarModelId(car_model_id).shopId(info.oneshopid).build().invoke(visitor,false);
//            int code1 = obj2.getInteger("code");
//            Preconditions.checkArgument(code1==1001,"编辑客户手机号12位，期待失败，实际"+code1);

//            //手机号10位
//            JSONObject obj3 = PreSaleCustomerEditScene.builder().customerId(custID).customerName(name).customerPhone("1340000000").subjectType(type).sex(sex).carStyleId(car_style_id).intentionCarModelId(car_model_id).shopId(info.oneshopid).build().invoke(visitor,false);
//            int code3 = obj3.getInteger("code");
//            Preconditions.checkArgument(code3==1001,"编辑客户手机号10位，期待失败，实际"+code3);

            //不填写必填项
            int code4 = PreSaleCustomerEditScene.builder().customerId(custID).customerPhone(phone).subjectType(type).sex(sex).carStyleId(car_style_id).intentionCarModelId(car_model_id).shopId(info.oneshopid).build().execute(visitor, false).getInteger("code");
            Preconditions.checkArgument(code4 == 1001, "编辑客户不填写 姓名，期待失败，实际" + code4);

            int code5 = PreSaleCustomerEditScene.builder().customerId(custID).customerName(name).subjectType(type).sex(sex).carStyleId(car_style_id).intentionCarModelId(car_model_id).shopId(info.oneshopid).build().execute(visitor, false).getInteger("code");
            Preconditions.checkArgument(code5 == 1001, "编辑客户不填写 手机号，期待失败，实际" + code5);

            int code6 = PreSaleCustomerEditScene.builder().customerId(custID).customerName(name).customerPhone(phone).subjectType(type).carStyleId(car_style_id).intentionCarModelId(car_model_id).shopId(info.oneshopid).build().execute(visitor, false).getInteger("code");
            Preconditions.checkArgument(code6 == 1001, "编辑客户不填写 性别，期待失败，实际" + code6);

            int code7 = PreSaleCustomerEditScene.builder().customerId(custID).customerName(name).customerPhone(phone).sex(sex).carStyleId(car_style_id).intentionCarModelId(car_model_id).shopId(info.oneshopid).build().execute(visitor, false).getInteger("code");
            Preconditions.checkArgument(code7 == 1001, "编辑客户不填写车主类型 ，期待失败，实际" + code7);

            int code8 = PreSaleCustomerEditScene.builder().customerId(custID).customerName(name).customerPhone(phone).subjectType(type).sex(sex).intentionCarModelId(car_model_id).shopId(info.oneshopid).build().execute(visitor, false).getInteger("code");
            Preconditions.checkArgument(code8 == 1001, "编辑客户不填写 意向车系，期待失败，实际" + code8);

            int code9 = PreSaleCustomerEditScene.builder().customerId(custID).customerName(name).customerPhone(phone).subjectType(type).sex(sex).carStyleId(car_style_id).shopId(info.oneshopid).build().execute(visitor, false).getInteger("code");
            Preconditions.checkArgument(code9 == 1001, "编辑客户不填写 意向车型，期待失败，实际" + code9);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("展厅客户编辑资料");
        }
    }


    /**
     *     展厅客户-详情-备注记录
     */

    /**
     *     展厅客户-详情-购车记录
     */

    /**
     *     展厅客户-详情-接待记录
     */

    /**
     *     展厅客户-变更所属销售
     */


    /**
     * -------------------------------销售业务管理 - 销售客户管理 - 成交记录 ------------------------------------
     */

    /**
     * 成交记录-创建 正常&异常
     */
    @Test(dataProvider = "CSTMINFO")
    public void newCstmRecord(String name, String phone, String type, String sex, String mess, String chk) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            visitor.setProduct(EnumTestProduct.YT_DAILY_JD);
            Long shop_id = info.oneshopid;
            Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(shop_id).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = PreSaleCustomerSalesListScene.builder().shopId(shop_id).type("PRE").build().execute(visitor).getJSONArray("list").getJSONObject(0).getString("sales_id");
            if (chk.equals("false")) {
                int code = PreSaleCustomerCreateCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).shopId(shop_id).vehicleChassisCode("ASDFUGGDSF12" + (int) ((Math.random() * 9 + 1) * 10000)).purchaseCarDate(dt.getHistoryDate(-1)).build().execute(visitor, false).getInteger("code");
                Preconditions.checkArgument(code == 1001, mess + "期待失败，实际" + code);
            } else {
                IScene preSaleCustomerPageScene = PreSaleCustomerPageScene.builder().build();
                int total = preSaleCustomerPageScene.execute(visitor).getInteger("total");

                JSONObject response = PreSaleCustomerCreateCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).shopId(shop_id).vehicleChassisCode("ASDFUGGDSF02" + (int) ((Math.random() * 9 + 1) * 10000)).purchaseCarDate(dt.getHistoryDate(-1)).build().execute(visitor, false);
                Preconditions.checkArgument(response.getInteger("code") == 1000, mess + "期待创建成功，实际" + response.getInteger("code"));

                int oneTotal = preSaleCustomerPageScene.execute(visitor).getInteger("total");
                Preconditions.checkArgument(oneTotal == total + 1, "创建成交记录之前客户列表总数：" + total + " 创建成交记录之后客户列表总数：" + oneTotal);

                JSONObject object = util.toJavaObject(preSaleCustomerPageScene, JSONObject.class, "customer_phone", phone);
                Preconditions.checkArgument(object.getString("customer_type_name").equals("成交客户"), "创建客户列表不存在的电话号的成交记录，客户类型变为：" + object.getString("customer_type_name"));

                //找一个潜客创建成交记录
                JSONObject object1 = util.toJavaObject(preSaleCustomerPageScene, JSONObject.class, "customer_type_name", "潜在客户");
                PreSaleCustomerCreateCustomerScene.builder().customerName(name).customerPhone(object1.getString("customer_phone")).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).shopId(Long.parseLong(object1.getString("shop_id"))).vehicleChassisCode("ASDFUGGDSF02" + (int) ((Math.random() * 9 + 1) * 10000)).purchaseCarDate(dt.getHistoryDate(-1)).build().execute(visitor);
                JSONObject object2 = util.toJavaObject(preSaleCustomerPageScene, JSONObject.class, "id", object1.getString("id"));
                Preconditions.checkArgument(object2.getString("customer_type_name").equals("成交客户"), "创建潜客户的成交记录，客户类型变为：" + object2.getString("customer_type_name"));
                Preconditions.checkArgument(object2.getString("subject_type").equals(type), "创建潜客户的成交记录，车主类型变为：" + object2.getString("subject_type"));
                Preconditions.checkArgument(object2.getString("customer_name").equals(name), "创建潜客户的成交记录，客户姓名变为：" + object2.getString("customer_name"));
//                Preconditions.checkArgument(object2.getString("customer_phone").equals(phone), "创建潜客户的成交记录，客户联系方式变为：" + object2.getString("customer_phone"));
                Preconditions.checkArgument(object2.getString("sex_id").equals(sex), "创建潜客户的成交记录，客户性别id变为：" + object2.getString("sex_id"));
                int secondTotal = preSaleCustomerPageScene.execute(visitor).getInteger("total");
                Preconditions.checkArgument(secondTotal == oneTotal, "创建潜客户的成交记前客户列表总数：" + oneTotal + " 创建潜客户的成交记后客户列表总数：" + secondTotal);
                //找一个成交客户创建成交记录
                JSONObject object3 = util.toJavaObject(preSaleCustomerPageScene, JSONObject.class, "customer_type_name", "成交客户");
                PreSaleCustomerCreateCustomerScene.builder().customerName(name).customerPhone(object3.getString("customer_phone")).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).shopId(Long.parseLong(object3.getString("shop_id"))).vehicleChassisCode("ASDFUGGDSF02" + (int) ((Math.random() * 9 + 1) * 10000)).purchaseCarDate(dt.getHistoryDate(-1)).build().execute(visitor);
                JSONObject object4 = util.toJavaObject(preSaleCustomerPageScene, JSONObject.class, "id", object3.getString("id"));
                Preconditions.checkArgument(object4.getString("customer_type_name").equals("成交客户"), "创建成交客户的成交记录，客户类型变为：" + object4.getString("customer_type_name"));
                Preconditions.checkArgument(object4.getString("customer_type_name").equals("成交客户"), "创建成交客户的成交记录，客户类型变为：" + object2.getString("customer_type_name"));
                Preconditions.checkArgument(object4.getString("subject_type").equals(type), "创建成交客户的成交记录，车主类型变为：" + object2.getString("subject_type"));
                Preconditions.checkArgument(object4.getString("customer_name").equals(name), "创建成交客户的成交记录，客户姓名变为：" + object2.getString("customer_name"));
//                Preconditions.checkArgument(object4.getString("customer_phone").equals(phone), "创建成交客户的成交记录，客户联系方式变为：" + object2.getString("customer_phone"));
                Preconditions.checkArgument(object4.getString("sex_id").equals(sex), "创建成交客户的成交记录，客户性别id变为：" + object2.getString("sex_id"));
                int thirdTotal = preSaleCustomerPageScene.execute(visitor).getInteger("total");
                Preconditions.checkArgument(thirdTotal == secondTotal, "创建成交客户的成交记前客户列表总数：" + secondTotal + " 创建成交客户的成交记后客户列表总数：" + thirdTotal);
            }

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("创建成交记录");
        }
    }

    @Test
    public void newCstmRecordErr() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            visitor.setProduct(EnumTestProduct.YT_DAILY_JD);
            Long shop_id = info.oneshopid;
            Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(shop_id).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = PreSaleCustomerSalesListScene.builder().shopId(shop_id).type("PRE").build().execute(visitor).getJSONArray("list").getJSONObject(0).getString("sales_id");

            String name = "name" + System.currentTimeMillis();
            String phone = info.phone;
            String type = "PERSON";
            String sex = "0";


            int code = PreSaleCustomerCreateCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).shopId(shop_id).vehicleChassisCode("ASDFUGGDSF12" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000))).purchaseCarDate(dt.getHistoryDate(+1)).build().execute(visitor, false).getInteger("code");

            Preconditions.checkArgument(code == 1001, "购车日期大于当前时间期待失败，实际" + code);

            int code1 = PreSaleCustomerCreateCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).shopId(shop_id).vehicleChassisCode("ASDFUGGDSF1" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000))).purchaseCarDate(dt.getHistoryDate(-1)).build().execute(visitor, false).getInteger("code");

            Preconditions.checkArgument(code1 == 1001, "底盘号16位期待失败，实际" + code1);

            int code2 = PreSaleCustomerCreateCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).shopId(shop_id).vehicleChassisCode("ASDFUGGDSF111" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000))).purchaseCarDate(dt.getHistoryDate(-1)).build().execute(visitor, false).getInteger("code");

            Preconditions.checkArgument(code2 == 1001, "底盘号18位期待失败，实际" + code2);

//            int code3 = PreSaleCustomerCreateCustomerScene.builder().customerName(name).customerPhone(info.phone).customerType(type).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).salesId(salesId).shopId(shop_id).vehicleChassisCode("ASDFUGGDSF11" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000))).purchaseCarDate(dt.getHistoryDate(-1)).build().invoke(visitor, false).getInteger("code");
//
//            Preconditions.checkArgument(code3 == 1001, "手机号未注册小程序期待失败，实际" + code3);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建成交记录异常条件");
        }
    }

    /**
     * 成交记录-筛选
     */

    @Test
    public void buyCarPage_Filter1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray pagelist = PreSaleCustomerBuyCarPageScene.builder().build().execute(visitor).getJSONArray("list");
            if (pagelist.size() > 0) {
                JSONObject one = pagelist.getJSONObject(0);
                String customer_name = one.getString("customer_name").toUpperCase();
                String customer_phone = one.getString("customer_phone").toUpperCase();
                String pre_sale_name = one.getString("pre_sale_name").toUpperCase();
                String pre_sale_account = one.getString("pre_sale_account").toUpperCase();
//                String vehicle_chassis_code = one.getString("vehicle_chassis_code").toUpperCase();
                String buy_car_date = one.getString("buy_car_date").toUpperCase();

                //姓名
                JSONArray searchlist = PreSaleCustomerBuyCarPageScene.builder().page(1).size(20).customerName(customer_name).build().execute(visitor).getJSONArray("list");
                Preconditions.checkArgument(searchlist.size() > 0, "搜索无结果");
                for (int i = 0; i < searchlist.size(); i++) {
                    String searchname = searchlist.getJSONObject(i).getString("customer_name").toUpperCase();
                    Preconditions.checkArgument(searchname.contains(customer_name), "搜索" + customer_name + " ，结果包含" + searchname);
                }

                //手机号
                JSONArray searchlist1 = PreSaleCustomerBuyCarPageScene.builder().page(1).size(20).phone(customer_phone).build().execute(visitor).getJSONArray("list");
                Preconditions.checkArgument(searchlist1.size() > 0, "搜索无结果");
                for (int i = 0; i < searchlist1.size(); i++) {
                    String searchphone = searchlist1.getJSONObject(i).getString("customer_phone").toUpperCase();
                    Preconditions.checkArgument(searchphone.contains(customer_phone), "搜索" + customer_phone + " ，结果包含" + searchphone);
                }
                //销售顾问
                JSONArray searchlist2 = PreSaleCustomerBuyCarPageScene.builder().page(1).size(20).preSaleName(pre_sale_name).build().execute(visitor).getJSONArray("list");
                Preconditions.checkArgument(searchlist2.size() > 0, "搜索无结果");
                for (int i = 0; i < searchlist2.size(); i++) {
                    String searchpre_sale_name = searchlist2.getJSONObject(i).getString("pre_sale_name").toUpperCase();
                    Preconditions.checkArgument(searchpre_sale_name.contains(pre_sale_name), "搜索" + customer_phone + " ，结果包含" + searchpre_sale_name);
                }

                //销售账号
                JSONArray searchlist3 = PreSaleCustomerBuyCarPageScene.builder().page(1).size(20).preSaleAccount(pre_sale_account).build().execute(visitor).getJSONArray("list");
                Preconditions.checkArgument(searchlist3.size() > 0, "搜索无结果");
                for (int i = 0; i < searchlist3.size(); i++) {
                    String searchpre_sale_account = searchlist3.getJSONObject(i).getString("pre_sale_account").toUpperCase();
                    Preconditions.checkArgument(searchpre_sale_account.contains(pre_sale_account), "搜索" + customer_phone + " ，结果包含" + searchpre_sale_account);
                }

                //购车日期
                JSONArray searchlist4 = PreSaleCustomerBuyCarPageScene.builder().page(1).size(20).buyCarTimeStart(buy_car_date).buyCarTimeEnd(buy_car_date).build().execute(visitor).getJSONArray("list");
                Preconditions.checkArgument(searchlist4.size() > 0, "搜索无结果");
                for (int i = 0; i < searchlist4.size(); i++) {
                    String searchdate = searchlist4.getJSONObject(i).getString("buy_car_date").toUpperCase();
                    Preconditions.checkArgument(searchdate.equals(buy_car_date), "搜索" + buy_car_date + " ，结果包含" + searchdate);
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【成交记录】，根据存在的信息进行筛选");
        }

    }

    @Test
    public void buyCarPage_Filter2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //所属门店
            String shopname = "自动化门店简称";
            JSONArray searchlist = PreSaleCustomerBuyCarPageScene.builder().page(1).size(20).shopId(info.oneshopid).build().execute(visitor).getJSONArray("list");
            Preconditions.checkArgument(searchlist.size() > 0, "搜索无结果");
            for (int i = 0; i < searchlist.size(); i++) {
                String search = searchlist.getJSONObject(i).getString("shop_name");
                Preconditions.checkArgument(search.equals(shopname), "搜索" + shopname + " ，结果包含" + search);
            }

            //购买车系
            Long car_style_id = 1470L;
            String intention_car_style_name = "Model";
            JSONArray searchlist1 = PreSaleCustomerBuyCarPageScene.builder().page(1).size(20).carStyleId(car_style_id).build().execute(visitor).getJSONArray("list");
            Preconditions.checkArgument(searchlist1.size() > 0, "搜索无结果");
            for (int i = 0; i < searchlist1.size(); i++) {
                String search = searchlist1.getJSONObject(i).getString("intention_car_style_name");
                Preconditions.checkArgument(search.contains(intention_car_style_name), "搜索" + intention_car_style_name + " ，结果包含" + search);
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【成交记录】，根据存在的信息进行筛选");
        }

    }

    @Test(dataProvider = "FILTER", dataProviderClass = YunTongInfo.class)
    public void buyCarPage_Filter3(String content) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String customer_name = content.toUpperCase();
            String customer_phone = content.toUpperCase();
            String pre_sale_name = content.toUpperCase();
            String pre_sale_account = content.toUpperCase();


            //姓名
            JSONArray searchlist = PreSaleCustomerBuyCarPageScene.builder().page(1).size(20).customerName(customer_name).build().execute(visitor).getJSONArray("list");
            for (int i = 0; i < searchlist.size(); i++) {
                String searchname = searchlist.getJSONObject(i).getString("customer_name").toUpperCase();
                Preconditions.checkArgument(searchname.contains(customer_name), "搜索姓名=" + customer_name + " ，结果包含" + searchname);
            }

            //手机号
            JSONArray searchlist1 = PreSaleCustomerBuyCarPageScene.builder().page(1).size(20).phone(customer_phone).build().execute(visitor).getJSONArray("list");
            for (int i = 0; i < searchlist1.size(); i++) {
                String searchphone = searchlist1.getJSONObject(i).getString("customer_phone").toUpperCase();
                Preconditions.checkArgument(searchphone.contains(customer_phone), "搜索手机号=" + customer_phone + " ，结果包含" + searchphone);
            }
            //销售顾问
            JSONArray searchlist2 = PreSaleCustomerBuyCarPageScene.builder().page(1).size(20).preSaleName(pre_sale_name).build().execute(visitor).getJSONArray("list");
            for (int i = 0; i < searchlist2.size(); i++) {
                String searchpre_sale_name = searchlist2.getJSONObject(i).getString("pre_sale_name").toUpperCase();
                Preconditions.checkArgument(searchpre_sale_name.contains(pre_sale_name), "搜索销售顾问=" + customer_phone + " ，结果包含" + searchpre_sale_name);
            }

            //销售账号
            JSONArray searchlist3 = PreSaleCustomerBuyCarPageScene.builder().page(1).size(20).preSaleAccount(pre_sale_account).build().execute(visitor).getJSONArray("list");
            for (int i = 0; i < searchlist3.size(); i++) {
                String searchpre_sale_account = searchlist3.getJSONObject(i).getString("pre_sale_account").toUpperCase();
                Preconditions.checkArgument(searchpre_sale_account.contains(pre_sale_account), "搜索销售账号=" + customer_phone + " ，结果包含" + searchpre_sale_account);
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【成交记录】，根据特殊字符进行筛选");
        }

    }


    /**
     * -------------------------------销售业务管理 - 销售接待记录 -----------------------------------------
     */

    /**
     *     销售接待记录-筛选
     */

    /**
     *     销售接待记录-列表操作 备注、确认购车、变更接待、完成接待
     */


    /**
     * -------------------------------系统配置  品牌管理------------------------------------
     */
    /**
     * PC 品牌管理-系统测试
     */


    //品牌--正常
    @Test
    public void addBrand_name1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            int code = BrandAddScene.builder().name(info.stringone).logoPath(info.getLogo()).build().execute(visitor, false).getInteger("code");

            Preconditions.checkArgument(code == 1000, "状态码期待1000，实际" + code);

            //删除品牌
            Long id = BrandPageScene.builder().page(1).size(10).name(info.stringone).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("id");

            BrandDeleteScene.builder().id(id).build().execute(visitor);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建品牌，名称1个字");
        }

    }

    @Test
    public void addBrand_name10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            int code = BrandAddScene.builder().name(info.stringten).logoPath(info.getLogo()).build().execute(visitor, false).getInteger("code");

            Preconditions.checkArgument(code == 1000, "状态码期待1000，实际" + code);

            //删除品牌
            Long id = BrandPageScene.builder().page(1).size(10).name(info.stringten).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("id");

            BrandDeleteScene.builder().id(id).build().execute(visitor);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建品牌，名称10个字");
        }

    }

    @Test
    public void editBrand_name() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            visitor.setProduct(EnumTestProduct.YT_DAILY_JD);

            //创建一个品牌
            String name1 = info.stringsix;
            String name2 = info.stringsix + "aaa";
            BrandAddScene.builder().name(name1).logoPath(info.getLogo()).build().execute(visitor);
            //获取创建的品牌id
            Long id = BrandPageScene.builder().page(1).size(10).name(name1).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("id");

            //修改这个品牌的名字
            BrandEditScene.builder().id(id).name(name2).logoPath(info.getLogo()).build().execute(visitor);
            //根据id查询，名字为name2
            JSONArray arr = BrandPageScene.builder().page(1).size(10).build().execute(visitor).getJSONArray("list");

            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getLong("id").equals(id)) {
                    Preconditions.checkArgument(obj.getString("name").equals(name2), "修改前名字是" + name1 + "，期望修改为" + name2 + "，实际修改后为" + obj.getString("name"));
                }
            }

            //删除品牌
            BrandDeleteScene.builder().id(id).build().execute(visitor);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建品牌后进行修改");
        }

    }

    //品牌--异常
    @Test
    public void addBrand_nameerr() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            String name = "12345aA啊！@1";
            int code = BrandAddScene.builder().name(name).logoPath(info.getLogo()).build().execute(visitor, false).getInteger("code");

            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建品牌，名称11个字");
        }

    }

    //品牌车系--正常

    @Test(dataProvider = "CAR_STYLE")
    public void addCarStyle(String manufacturer, String name, String online_time) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = CarStyleAddScene.builder().brandId(info.BrandID).manufacturer(manufacturer).name(name).onlineTime(online_time).build().execute(visitor, false).getInteger("code");
            Preconditions.checkArgument(code == 1000, "创建车系：生产商 " + manufacturer + ", 车系 " + name + ", 上线日期" + online_time + "状态码" + code);

            //删除品牌车系
            Long id = CarStylePageScene.builder().brandId(info.BrandID).page(1).size(1).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
            CarStyleDeleteScene.builder().id(id).build().execute(visitor);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车系");
        }
    }

    @DataProvider(name = "CAR_STYLE")
    public Object[] carStyle() {
        return new String[][]{
                {info.stringone, info.stringone, dt.getHistoryDate(0)},
                {info.stringfifty, info.stringfifty, dt.getHistoryDate(-1)},
                {info.stringsix, info.stringsix, dt.getHistoryDate(1)},
        };
    }

    @Test
    public void editCarStyle() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建车系
            String manufacturer = "旧生产商" + Integer.toString((int) ((Math.random() * 9 + 1) * 100));
            String name = "旧车系" + Integer.toString((int) ((Math.random() * 9 + 1) * 100));
            ;
            String online_time = dt.getHistoryDate(0);
            CarStyleAddScene.builder().brandId(info.BrandID).manufacturer(manufacturer).name(name).onlineTime(online_time).build().execute(visitor);

            //获取车系id
            Long id = CarStylePageScene.builder().brandId(info.BrandID).page(1).size(1).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
            //修改车系
            String manufacturer1 = "新生产商" + Integer.toString((int) ((Math.random() * 9 + 1) * 100));
            ;
            String name1 = "新车系" + Integer.toString((int) ((Math.random() * 9 + 1) * 100));
            ;
            String online_time1 = dt.getHistoryDate(-2);

            CarStyleEditScene.builder().brandId(info.BrandID).id(id).manufacturer(manufacturer1).name(name1).onlineTime(online_time1).build().execute(visitor);

            //查看修改结果
            JSONArray arr = CarStylePageScene.builder().brandId(info.BrandID).page(1).size(30).build().execute(visitor).getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getLong("id").longValue() == id) {
                    String search_manufacturer1 = obj.getString("manufacturer");
                    String search_name1 = obj.getString("name");
                    String search_online_time1 = obj.getString("online_time");

                    Preconditions.checkArgument(search_manufacturer1.equals(manufacturer1), "修改前生产商=" + manufacturer + "，期望修改为" + manufacturer1 + "，实际修改后为" + search_manufacturer1);
                    Preconditions.checkArgument(search_name1.equals(name1), "修改前车系=" + name + "，期望修改为" + name1 + "，实际修改后为" + search_name1);
                    Preconditions.checkArgument(search_online_time1.equals(online_time1), "修改前上线时间=" + online_time + "，期望修改为" + online_time1 + "，实际修改后为" + search_online_time1);

                }
            }


            //删除品牌车系
            CarStyleDeleteScene.builder().id(id).build().execute(visitor);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，修改车系");
        }
    }

    @Test
    public void addOneCarStyleinTwoModel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建品牌1
            Long brandid1 = info.getBrandID(5);
            //创建品牌2
            Long brandid2 = info.getBrandID(6);

            //创建车系
            String manufacturer = "生产商重复";
            String name = "车系重复";
            String online_time = dt.getHistoryDate(0);

            CarStyleAddScene.builder().brandId(brandid1).manufacturer(manufacturer).name(name).onlineTime(online_time).build().execute(visitor);

            int code = CarStyleAddScene.builder().brandId(brandid2).manufacturer(manufacturer).name(name).onlineTime(online_time).build().execute(visitor, false).getInteger("code");

            Preconditions.checkArgument(code == 1000, "期待状态码1000，实际" + code);

            //删除品牌
            BrandDeleteScene.builder().id(brandid1).build().execute(visitor);
            BrandDeleteScene.builder().id(brandid2).build().execute(visitor);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，不同品牌下创建相同车系，期待成功");
        }
    }


    //品牌车系--异常
    @Test(dataProvider = "CAR_STYLEERR")
    public void addCarStyleErr(String manufacturer, String name, String online_time, String yz) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = CarStyleAddScene.builder().brandId(info.BrandID).manufacturer(manufacturer).name(name).onlineTime(online_time).build().execute(visitor, false);

            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, yz + "期待状态码1001，实际" + code + ",提示语：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车系，生产商/车系51字");
        }
    }

    @DataProvider(name = "CAR_STYLEERR")
    public Object[] carStyle_err() {
        return new String[][]{
                {info.stringfifty1, info.stringone, dt.getHistoryDate(0), "生产商51个字"},
                {info.stringfifty, info.stringfifty1, dt.getHistoryDate(-1), "车系51个字"},

        };
    }

    @Test
    public void addTwoCarStyleinOneModel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建品牌1
            Long brandid1 = info.getBrandID(5);

            //创建车系
            String manufacturer = "生产商重复" + Integer.toString((int) ((Math.random() * 9 + 1) * 100));
            String name = "车系重复" + Integer.toString((int) ((Math.random() * 9 + 1) * 100));
            String online_time = dt.getHistoryDate(0);

            CarStyleAddScene.builder().brandId(brandid1).manufacturer(manufacturer).name(name).onlineTime(online_time).build().execute(visitor);

            JSONObject obj = CarStyleAddScene.builder().brandId(brandid1).manufacturer(manufacturer).name(name).onlineTime(online_time).build().execute(visitor, false);

            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + ",提示语：" + message);


            //删除品牌
            BrandDeleteScene.builder().id(brandid1).build().execute(visitor);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，相同品牌下创建相同车系，期待失败");
        }
    }

    @Test
    public void addCarStyleinNotExistModel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建品牌1
            Long brandid1 = info.getBrandID(5);

            //删除品牌
            BrandDeleteScene.builder().id(brandid1).build().execute(visitor);

            //添加车系
            String manufacturer = "自动化" + System.currentTimeMillis();
            String name = "自动化" + System.currentTimeMillis();
            String online_time = dt.getHistoryDate(0);
            JSONObject obj = CarStyleAddScene.builder().brandId(brandid1).manufacturer(manufacturer).name(name).onlineTime(online_time).build().execute(visitor, false);

            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "实际状态码" + code + ", 提示语为：" + message);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，在某品牌下创建车系时，品牌被删除，期待失败");
        }
    }


    //品牌车系车型 --正常
    @Test(dataProvider = "CAR_MODEL")
    public void addCarModel(String name, String year, String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = CarStyleCarModelAddScene.builder().brandId(info.BrandID).styleId(info.CarStyleID).name(name).year(year).status(status).build().execute(visitor, false).getInteger("code");
            Preconditions.checkArgument(code == 1000, "创建车系：车型名称 " + name + ", 年款 " + year + ", 预约状态" + status + "状态码" + code);

            //删除品牌车系
            Long id = CarStyleCarModelPageScene.builder().brandId(info.BrandID).styleId(info.CarStyleID).name(name).page(1).size(1).build().execute(visitor).getJSONArray("list").getJSONObject(0).getLong("id");

            CarStyleCarModelDeleteScene.builder().id(id).build().execute(visitor);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车型");
        }
    }

    @DataProvider(name = "CAR_MODEL")
    public Object[] carModel() {
        return new String[][]{
                {"x", "1", "ENABLE"},
                {info.stringfifty, "2000年", "ENABLE"},
                {info.stringsix, "12345啊啊啊啊啊！@#qweQWER", "DISABLE"},
        };
    }

    @Test
    public void editCarModel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建车型
            String name1 = "旧车型名称" + System.currentTimeMillis();
            String year1 = "2000年";
            String status1 = "ENABLE";
            CarStyleCarModelAddScene.builder().brandId(info.BrandID).styleId(info.CarStyleID).name(name1).year(year1).status(status1).build().execute(visitor);

            //获取车系id
            int size = CarStyleCarModelPageScene.builder().brandId(info.BrandID).styleId(info.CarStyleID).name(name1).page(1).size(1).build().execute(visitor).getInteger("total");

            Long id = CarStyleCarModelPageScene.builder().brandId(info.BrandID).styleId(info.CarStyleID).name(name1).page(1).size(size).build().execute(visitor).getJSONArray("list").getJSONObject(size - 1).getLong("id");

            //修改车型
            String name2 = "新车型名称" + System.currentTimeMillis();
            String year2 = "2020年";
            String status2 = "DISABLE";
            CarStyleCarModelEditScene.builder().brandId(info.BrandID).styleId(info.CarStyleID).id(id).name(name2).year(year2).status(status2).build().execute(visitor);

            //查看修改结果
            String search_name2 = "";
            String search_year2 = "";
            String search_status2 = "";
            int size1 = CarStyleCarModelPageScene.builder().brandId(info.BrandID).styleId(info.CarStyleID).page(1).size(1).build().execute(visitor).getInteger("total");
            JSONArray arr = CarStyleCarModelPageScene.builder().brandId(info.BrandID).styleId(info.CarStyleID).page(1).size(size1).build().execute(visitor).getJSONArray("list");

            for (int i = size1 - 1; i > 0; i--) {
                JSONObject obj = arr.getJSONObject(i);
                System.out.println(obj + "-----------------");
                if (obj.getLong("id").longValue() == id.longValue()) {

                    search_name2 = obj.getString("name");
                    search_year2 = obj.getString("year");
                    search_status2 = obj.getString("status");
                }
            }


            Preconditions.checkArgument(search_name2.equals(name2), "修改前车型名称=" + name1 + "，期望修改为" + name2 + "，实际修改后为" + search_name2);
            Preconditions.checkArgument(search_year2.equals(year2), "修改前年款=" + year1 + "，期望修改为" + year2 + "，实际修改后为" + search_year2);
            Preconditions.checkArgument(search_status2.equals(status2), "修改前状态=" + status1 + "，期望修改为" + status2 + "，实际修改后为" + search_status2);

            //删除品牌车系车型
            CarStyleCarModelDeleteScene.builder().id(id).build().execute(visitor);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，修改车型");
        }
    }

    //品牌车系车型 --异常
    @Test
    public void addCarModel_err() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String year = "1998年";
            String status = "ENABLE";
            JSONObject obj = CarStyleCarModelAddScene.builder().brandId(info.BrandID).styleId(info.CarStyleID).name(info.stringfifty1).year(year).status(status).build().execute(visitor, false);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + "， 提示语：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车型， 名称51字");
        }
    }

    //品牌车系车型 --异常
    @Test
    public void addCarModel_err3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String year = "12345啊啊啊啊啊！@#qweQWERQ";
            String status = "ENABLE";
            JSONObject obj = CarStyleCarModelAddScene.builder().brandId(info.BrandID).styleId(info.CarStyleID).name(info.stringsix).year(year).status(status).build().execute(visitor, false);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + "， 提示语：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车型， 年款21个字");
        }
    }

    @Test
    public void addCarModel_err1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //新建品牌
            Long brandid = info.getBrandID(7);
            //新建车系
            Long carStyleId = info.getCarStyleID(brandid, 5);

            //删除车系
            CarStyleDeleteScene.builder().id(carStyleId).build().execute(visitor);

            //新建车型
            String name1 = "自动化" + System.currentTimeMillis();
            String year1 = "2019年";
            String status1 = "ENABLE";
            JSONObject obj = CarStyleCarModelAddScene.builder().brandId(brandid).styleId(carStyleId).name(name1).year(year1).status(status1).build().execute(visitor, false);


            //删除品牌
            BrandDeleteScene.builder().id(brandid).build().execute(visitor);

            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "状态码为" + code + ", 提示语" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车型时车系被删除， 期待失败");
        }
    }

    @Test
    public void addCarModel_err2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //新建品牌
            Long brandid = info.getBrandID(7);
            //新建车系
            Long carStyleId = info.getCarStyleID(brandid, 5);

            //删除品牌
            BrandDeleteScene.builder().id(brandid).build().execute(visitor);
            //新建车型
            String name1 = "自动化" + System.currentTimeMillis();
            String year1 = "1009年";
            String status1 = "ENABLE";
            JSONObject obj = CarStyleCarModelAddScene.builder().brandId(brandid).styleId(carStyleId).name(name1).year(year1).status(status1).build().execute(visitor, false);


            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "状态码为" + code + ", 提示语" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【品牌管理】，创建车型时品牌被删除， 期待失败");
        }
    }

//    //评价跟进
//    @Test(dataProvider = "WORDNUM", dataProviderClass = YunTongInfo.class)
//    public void doEvaluate_num(String contnet) {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Long id =-1L;
//            JSONArray arr = EvaluatePageV4Scene.builder().page(1).size(20).isFollowUp(false).evaluateType(5).build().invoke(visitor).getJSONArray("list");
//            for (int i = 0 ; i< arr.size();i++){
//                JSONObject obj = arr.getJSONObject(i);
//                if (obj.getInteger("score")<5){
//                    id = obj.getLong("id");
//                }
//            }
//            EvaluateFollowUpScene.builder().id(id).evaluate_type(5).shopId(info.oneshopid).remark(contnet).build().invoke(visitor);
//
//
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【评价管理】，跟进字数校验");
//        }
//    }


}
