package com.haisheng.framework.testng.bigScreen.crm.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletCode;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCustomerInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.EditAfterSaleCustomerScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.ParseException;

/**
 * @author : guoliya
 * @date :  2020/10/15
 */

public class Crm_AfterSale extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    PublicMethod method = new PublicMethod();
    String[] customerNameArr = {"1111111111111111", "^%&%$##@#$$%^&&*8"};
    String[] customerPhoneNumberArr = {"11111111111", "1337316600", "133731668066", "哈哈哈哈哈哈哈哈哈哈就", "GHBNBVHHkjl","%^%$#@#$%&^&"};
    String[] plateNumberArr = {"哈TY12345", "京hj67877", "京U1230090", "京F8989", "京A1239456"};

    double travelMileageArr[]={1999.99,1666.88};
    double[] days={0,366,12.555};


    //各角色的 账号和密码
    EnumAccount zjl = EnumAccount.ZJL_DAILY;
    String xs_name = "0805xsgw";//销售顾问
    String by_name = "lxqby";//保养顾问姓名
    String by_name2 = "baoyang";
    String wx_name = "lxqwx";//维修顾问姓名
    String zjl_name = "zjl";
    String fwzj_name = "baoyang";//服务总监
    String pwd = "e10adc3949ba59abbe56e057f20f883e";//密码全部一致
    String qt_name = "qt";//前台账号
    String bsj_name = "baoshijie";


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //checklist相关配置
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "gly";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");
        //钉钉推送消息验证
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "CRM 日常-gly");
        //钉钉推送消息选择群
        commonConfig.dingHook = DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);
        logger.debug("crm: " + crm);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method)  {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户姓异常")
    public void AfterSaleReceptionException1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
//            UserUtil.loginApplet(EnumAppletCode.GLY);
//            crm.appointmentRepair(62518L,"Max","MALE","2020-11-25","09:30","车轱辘有问题",1377L);
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
            for (int i = 0; i < customerNameArr.length; i++) {
                IScene scene = EditAfterSaleCustomerScene.builder()
                        .afterRecordId(afterRecordId)
                        .appointmentCustomerName(appointmentCustomerName)
                        .appointmentId(appointmentId)
                        .appointmentPhoneNumber(appointmentPhoneNumber)
                        .appointmentSecondaryPhone(appointmentSecondaryPhone)
                        .customerName(customerNameArr[i])
                        .customerPhoneNumber(customerPhoneNumber)
                        .customerSource(customerSource)
                        .firstRepairCarType(firstRepairCarType)
                        .maintainSaleId(maintainSaleId)
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("车主名称长度在1-15字之间"), "车主姓名异常");
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("车主姓名异常");
        }
    }

    @Test(description="APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话--异常")
    public void AfterSaleReceptionException2() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
            for (int i = 0; i < customerPhoneNumberArr.length; i++) {
                IScene scene = EditAfterSaleCustomerScene.builder()
                        .afterRecordId(afterRecordId)
                        .appointmentCustomerName(appointmentCustomerName)
                        .appointmentId(appointmentId)
                        .appointmentPhoneNumber(appointmentPhoneNumber)
                        .appointmentSecondaryPhone(appointmentSecondaryPhone)
                        .customerName(customerName)
                        .customerPhoneNumber(customerPhoneNumberArr[i])
                        .customerSource(customerSource)
                        .firstRepairCarType(firstRepairCarType)
                        .maintainSaleId(maintainSaleId)
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("联系方式1必须为11位手机号"), "联系方式1异常 ");
            }

        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("联系方式1异常");
        }
    }


    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-备201个字")
    public void AfterSaleReceptionException3() {
        logger.logCaseStart(caseResult.getCaseName());
        UserUtil.loginApplet(EnumAppletCode.GLY);
        String remark=EnumCustomerInfo.CUSTOMER_2.getRemark();
        JSONArray remarks=new JSONArray();
        remarks.add(remark);
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
                IScene scene = EditAfterSaleCustomerScene.builder()
                        .afterRecordId(afterRecordId)
                        .appointmentCustomerName(appointmentCustomerName)
                        .appointmentId(appointmentId)
                        .appointmentPhoneNumber(appointmentPhoneNumber)
                        .appointmentSecondaryPhone(appointmentSecondaryPhone)
                        .customerName(customerName)
                        .customerPhoneNumber(customerPhoneNumber)
                        .customerSource(customerSource)
                        .firstRepairCarType(firstRepairCarType)
                        .maintainSaleId(maintainSaleId)
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .remarks(remarks)
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("备注在10-200字之间"), " ");

        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-备201个字");
        }
    }

//    APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-行驶里程--后端没有校验
    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-行驶里程")
    public void AfterSaleReceptionException4() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            for (int i = 0; i < travelMileageArr.length; i++) {
                IScene scene = EditAfterSaleCustomerScene.builder()
                        .afterRecordId(afterRecordId)
                        .appointmentCustomerName(appointmentCustomerName)
                        .appointmentId(appointmentId)
                        .appointmentPhoneNumber(appointmentPhoneNumber)
                        .appointmentSecondaryPhone(appointmentSecondaryPhone)
                        .customerName(customerName)
                        .customerPhoneNumber(customerPhoneNumber)
                        .customerSource(customerSource)
                        .firstRepairCarType(firstRepairCarType)
                        .maintainSaleId(maintainSaleId)
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileageArr[i])
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("行驶里程数字错误"), "行驶里程不能有小数点");
            }

        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-行驶里程不能有小数点");
        }
    }

    //APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话2异常
    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话2异常" )
    public void AfterSaleReceptionException5() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
            for (int i = 0; i < customerNameArr.length; i++) {
                IScene scene = EditAfterSaleCustomerScene.builder()
                        .afterRecordId(afterRecordId)
                        .appointmentCustomerName(appointmentCustomerName)
                        .appointmentId(appointmentId)
                        .appointmentPhoneNumber(appointmentPhoneNumber)
                        .appointmentSecondaryPhone(appointmentSecondaryPhone)
                        .customerName(customerName)
                        .customerPhoneNumber(customerPhoneNumber)
                        .customerSource(customerSource)
                        .firstRepairCarType(firstRepairCarType)
                        .maintainSaleId(maintainSaleId)
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .customerSecondaryPhone(customerNameArr[i])
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("联系方式2必须为11位手机号"), "联系人手机号为"+customerNameArr[i]);
            }

        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话2异常");
        }
    }



    //APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约电话2异常---后端没有校验
    @Test(description ="APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约电话2异常")
    public void AfterSaleReceptionException6() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
            for (int i = 0; i < customerNameArr.length; i++) {
                IScene scene = EditAfterSaleCustomerScene.builder()
                        .afterRecordId(afterRecordId)
                        .appointmentCustomerName(appointmentCustomerName)
                        .appointmentId(appointmentId)
                        .appointmentPhoneNumber(appointmentPhoneNumber)
                        .appointmentSecondaryPhone(customerNameArr[i])
                        .customerName(customerName)
                        .customerPhoneNumber(customerPhoneNumber)
                        .customerSource(customerSource)
                        .firstRepairCarType(firstRepairCarType)
                        .maintainSaleId(maintainSaleId)
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .customerSecondaryPhone(customerPhoneNumber)
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("预约电话2必须为11位手机号"), "联系人手机号为"+customerNameArr[i]);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约电话2异常");
        }
    }

    //APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-联系人1姓名异常--提示语有问题，填写手机号，提示手机号不能为空
    @Test(description ="APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-联系人1姓名异常")
    public void AfterSaleReceptionException7() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
            for (int i = 0; i < customerNameArr.length; i++) {
                IScene scene = EditAfterSaleCustomerScene.builder()
                        .afterRecordId(afterRecordId)
                        .appointmentCustomerName(appointmentCustomerName)
                        .appointmentId(appointmentId)
                        .appointmentPhoneNumber(appointmentPhoneNumber)
                        .appointmentSecondaryPhone(appointmentSecondaryPhone)
                        .customerName(customerName)
                        .customerPhoneNumber(customerPhoneNumber)
                        .customerSource(customerSource)
                        .firstRepairCarType(firstRepairCarType)
                        .maintainSaleId(maintainSaleId)
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .firstContactName(customerNameArr[i])
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("预约电话1必须为11位手机号"), "联系人手机号为"+customerNameArr[i]);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-联系人1姓名异常");
        }
    }

    //APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-联系人2姓名异常--提示语有问题，填写手机号，提示手机号不能为空
    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-联系人2姓名异常--提示语有问题，填写手机号，提示手机号不能为空")
    public void AfterSaleReceptionException8() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
            for (int i = 0; i <customerNameArr.length; i++) {
                IScene scene = EditAfterSaleCustomerScene.builder()
                        .afterRecordId(afterRecordId)
                        .appointmentCustomerName(appointmentCustomerName)
                        .appointmentId(appointmentId)
                        .appointmentPhoneNumber(appointmentPhoneNumber)
                        .appointmentSecondaryPhone(appointmentSecondaryPhone)
                        .customerName(customerName)
                        .customerPhoneNumber(customerPhoneNumber)
                        .customerSource(customerSource)
                        .firstRepairCarType(firstRepairCarType)
                        .maintainSaleId(maintainSaleId)
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .secondContactName(customerNameArr[i])
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("联系人2必须为11位手机号"), "联系人手机号为"+customerNameArr[i]);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            //saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-联系人2姓名异常--提示语有问题，填写手机号，提示手机号不能为空");
        }
    }

    //APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车牌号2异常
    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车牌号2异常")
    public void AfterSaleReceptionException9() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
            for (int i = 0; i < plateNumberArr.length; i++) {
                IScene scene = EditAfterSaleCustomerScene.builder()
                        .afterRecordId(afterRecordId)
                        .appointmentCustomerName(appointmentCustomerName)
                        .appointmentId(appointmentId)
                        .appointmentPhoneNumber(appointmentPhoneNumber)
                        .appointmentSecondaryPhone(appointmentSecondaryPhone)
                        .customerName(customerName)
                        .customerPhoneNumber(customerPhoneNumber)
                        .customerSource(customerSource)
                        .firstRepairCarType(firstRepairCarType)
                        .maintainSaleId(maintainSaleId)
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .secondPlateNumber(plateNumberArr[i])
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("车牌号码只允许文字+数字+大写字母")||object.getString("message").equals("请输入7-8位车牌号码位数"), " 异常车牌号："+plateNumberArr[i]);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车牌号2异常");
        }
    }
    //APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-维修天数异常--维修天数没有加整数限制
    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-维修天数异常--维修天数没有加整数限制")
    public void AfterSaleReceptionException10() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
            for (int i = 0; i <= days.length; i++) {
                IScene scene = EditAfterSaleCustomerScene.builder()
                        .afterRecordId(afterRecordId)
                        .appointmentCustomerName(appointmentCustomerName)
                        .appointmentId(appointmentId)
                        .appointmentPhoneNumber(appointmentPhoneNumber)
                        .appointmentSecondaryPhone(appointmentSecondaryPhone)
                        .customerName(customerName)
                        .customerPhoneNumber(customerPhoneNumber)
                        .customerSource(customerSource)
                        .firstRepairCarType(firstRepairCarType)
                        .maintainSaleId(maintainSaleId)
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .estimateRepairDays(days[i])
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("维修天数必须在1～365天之间"), "维修时间天数为："+days[i]);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-维修天数异常--维修天数没有加整数限制");
        }
    }

    //APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户名称不填写
    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户名称不填写")
    public void AfterSaleReceptionException11() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
                IScene scene = EditAfterSaleCustomerScene.builder()
                        .afterRecordId(afterRecordId)
                        .appointmentCustomerName(appointmentCustomerName)
                        .appointmentId(appointmentId)
                        .appointmentPhoneNumber(appointmentPhoneNumber)
                        .appointmentSecondaryPhone(appointmentSecondaryPhone)
                        .customerPhoneNumber(customerPhoneNumber)
                        .customerSource(customerSource)
                        .firstRepairCarType(firstRepairCarType)
                        .maintainSaleId(maintainSaleId)
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("客户姓名不允许为空"), "客户姓名不允许为空 ");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户名称不填写");
        }
    }

    //APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话1不填写
    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话1不填写")
    public void AfterSaleReceptionException12() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
            IScene scene = EditAfterSaleCustomerScene.builder()
                    .afterRecordId(afterRecordId)
                    .appointmentCustomerName(appointmentCustomerName)
                    .appointmentId(appointmentId)
                    .appointmentPhoneNumber(appointmentPhoneNumber)
                    .appointmentSecondaryPhone(appointmentSecondaryPhone)
                    .customerName(customerName)
                    .customerSource(customerSource)
                    .firstRepairCarType(firstRepairCarType)
                    .maintainSaleId(maintainSaleId)
                    .maintainType(maintainType)
                    .plateNumber(plateNumber)
                    .travelMileage(travelMileage)
                    .build();
            JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
            Preconditions.checkArgument(object.getString("message").equals("联系方式1不允许为空"), " 联系方式1不允许为空");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话1不填写");
        }
    }

    //APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约名称不填写
    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约名称不填写-没有校验")
    public void AfterSaleReceptionException13() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
            IScene scene = EditAfterSaleCustomerScene.builder()
                    .afterRecordId(afterRecordId)
                    .appointmentId(appointmentId)
                    .appointmentPhoneNumber(appointmentPhoneNumber)
                    .appointmentSecondaryPhone(appointmentSecondaryPhone)
                    .customerName(customerName)
                    .customerPhoneNumber(customerPhoneNumber)
                    .customerSource(customerSource)
                    .firstRepairCarType(firstRepairCarType)
                    .maintainSaleId(maintainSaleId)
                    .maintainType(maintainType)
                    .plateNumber(plateNumber)
                    .travelMileage(travelMileage)
                    .build();
            JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
            Preconditions.checkArgument(object.getString("message").equals("预约名称不允许为空"), " 预约名称不允许为空");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约名称不填写-没有校验");
        }
    }

    //APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约电话不填写
    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约电话不填写--没有限制")
    public void AfterSaleReceptionException14() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
            IScene scene = EditAfterSaleCustomerScene.builder()
                    .afterRecordId(afterRecordId)
                    .appointmentCustomerName(appointmentCustomerName)
                    .appointmentId(appointmentId)
                    .appointmentSecondaryPhone(appointmentSecondaryPhone)
                    .customerName(customerName)
                    .customerPhoneNumber(customerPhoneNumber)
                    .customerSource(customerSource)
                    .firstRepairCarType(firstRepairCarType)
                    .maintainSaleId(maintainSaleId)
                    .maintainType(maintainType)
                    .plateNumber(plateNumber)
                    .travelMileage(travelMileage)
                    .build();
            JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
            Preconditions.checkArgument(object.getString("message").equals("预约电话不允许为空"), " 预约电话不允许为空");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约电话不填写--没有限制");
        }
    }

    //APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车牌号1不填写
    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车牌号1不填写")
    public void AfterSaleReceptionException15() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
            IScene scene = EditAfterSaleCustomerScene.builder()
                    .afterRecordId(afterRecordId)
                    .appointmentCustomerName(appointmentCustomerName)
                    .appointmentId(appointmentId)
                    .appointmentPhoneNumber(appointmentPhoneNumber)
                    .appointmentSecondaryPhone(appointmentSecondaryPhone)
                    .customerName(customerName)
                    .customerPhoneNumber(customerPhoneNumber)
                    .customerSource(customerSource)
                    .firstRepairCarType(firstRepairCarType)
                    .maintainSaleId(maintainSaleId)
                    .maintainType(maintainType)
                    .travelMileage(travelMileage)
                    .build();
            JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
            Preconditions.checkArgument(object.getString("message").equals("车牌号1不允许为空"), " 车牌号1不允许为空");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车牌号1不填写");
        }
    }

    //APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-行驶里程不填写
    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-行驶里程不填写---必填项可以为空，没有限制")
    public void AfterSaleReceptionException16() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            double travelMileage=response.getDouble("travel_mileage");
            String plateNumber = response.getString("plate_number");
            IScene scene = EditAfterSaleCustomerScene.builder()
                    .afterRecordId(afterRecordId)
                    .appointmentCustomerName(appointmentCustomerName)
                    .appointmentId(appointmentId)
                    .appointmentPhoneNumber(appointmentPhoneNumber)
                    .appointmentSecondaryPhone(appointmentSecondaryPhone)
                    .customerName(customerName)
                    .customerPhoneNumber(customerPhoneNumber)
                    .customerSource(customerSource)
                    .firstRepairCarType(firstRepairCarType)
                    .maintainSaleId(maintainSaleId)
                    .plateNumber(plateNumber)
                    .maintainType(maintainType)
                    .build();
            JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
            Preconditions.checkArgument(object.getString("message").equals("行驶里程不允许为空"), "行驶里程为空 ");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-行驶里程不填写---必填项可以为空，没有限制");
        }
    }

    //APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-所属保养顾问不填写---保养顾问不是必填项？？--有疑问
    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-所属保养顾问不填写---保养顾问不是必填项？？")
    public void AfterSaleReceptionException17() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
            IScene scene = EditAfterSaleCustomerScene.builder()
                    .afterRecordId(afterRecordId)
                    .appointmentCustomerName(appointmentCustomerName)
                    .appointmentId(appointmentId)
                    .appointmentPhoneNumber(appointmentPhoneNumber)
                    .appointmentSecondaryPhone(appointmentSecondaryPhone)
                    .customerName(customerName)
                    .customerPhoneNumber(customerPhoneNumber)
                    .customerSource(customerSource)
                    .firstRepairCarType(firstRepairCarType)
                    .maintainSaleId(maintainSaleId)
                    .maintainType(maintainType)
                    .plateNumber(plateNumber)
                    .travelMileage(travelMileage)
                    .build();
            JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
            Preconditions.checkArgument(object.getString("message").equals(""), " ");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            //saveData("");
        }
    }

    //APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户来源不填写
    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户来源不填写")
    public void AfterSaleReceptionException19() {
        UserUtil.loginApplet(EnumAppletCode.GLY);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentPhoneNumber = response.getString("appointment_phone_number");
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage=response.getDouble("travel_mileage");
            IScene scene = EditAfterSaleCustomerScene.builder()
                    .afterRecordId(afterRecordId)
                    .appointmentCustomerName(appointmentCustomerName)
                    .appointmentId(appointmentId)
                    .appointmentPhoneNumber(appointmentPhoneNumber)
                    .appointmentSecondaryPhone(appointmentSecondaryPhone)
                    .customerName(customerName)
                    .customerPhoneNumber(customerPhoneNumber)
                    .firstRepairCarType(firstRepairCarType)
                    .maintainSaleId(maintainSaleId)
                    .maintainType(maintainType)
                    .plateNumber(plateNumber)
                    .travelMileage(travelMileage)
                    .build();
            JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
            Preconditions.checkArgument(object.getString("message").equals("客户来源不允许为空"), " 客户来源为空");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户来源不填写");
        }
    }

//    @Test
//    public void justTry() throws Exception {
//        new PublicMethod().uploadShopCarPlate("皖A123456",0);
//
//
//    }

}