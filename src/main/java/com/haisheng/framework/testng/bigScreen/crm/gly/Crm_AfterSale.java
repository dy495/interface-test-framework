package com.haisheng.framework.testng.bigScreen.crm.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletCode;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppointmentType;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCustomerInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.exception.DataException;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.EditAfterSaleCustomerScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author : guoliya
 * @date :  2020/10/15
 */

public class Crm_AfterSale extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    PublicMethod method = new PublicMethod();
    EnumAccount zjl = EnumAccount.ZJL_DAILY;
    EnumAppletCode applet = EnumAppletCode.GLY;
    String[] customerNameArr = {"1111111111111111", "^%&%$##@#$$%^&&*8"};
    String[] customerPhoneNumberArr = {"11111111111", "1337316600", "133731668066", "哈哈哈哈哈哈哈哈哈哈就", "GHBNBVHHkjl", "%^%$#@#$%&^&"};
    String[] plateNumberArr = {"哈TY12345", "京hj67877", "京U1230090", "京F8989", "京A1239456"};
    double[] travelMileageArr = {1999.99, 1666.88};
    double[] days = {0, 366, 12.555};

    @BeforeClass()
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //checklist相关配置
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "郭丽雅";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");
        //钉钉推送消息验证
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "汽车 日常-郭丽雅");
        //钉钉推送消息选择群
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);
        logger.debug("crm: " + crm);
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            UserUtil.loginApplet(applet);
            int id = getTimeId(date);
            crm.appointmentRepair((long) getCarId(), "Max", "13373166806", date, "测试测试", "", (long) id);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("预约失败");
        }
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        UserUtil.login(zjl);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户姓异常")
    public void AfterSaleReceptionException1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage = response.getDouble("travel_mileage");
            for (String s : customerNameArr) {
                IScene scene = EditAfterSaleCustomerScene.builder()
                        .afterRecordId(afterRecordId)
                        .appointmentCustomerName(appointmentCustomerName)
                        .appointmentId(appointmentId)
                        .appointmentPhoneNumber(appointmentPhoneNumber)
                        .appointmentSecondaryPhone(appointmentSecondaryPhone)
                        .customerName(s)
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

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话--异常")
    public void AfterSaleReceptionException2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage = response.getDouble("travel_mileage");
            for (String s : customerPhoneNumberArr) {
                IScene scene = EditAfterSaleCustomerScene.builder()
                        .afterRecordId(afterRecordId)
                        .appointmentCustomerName(appointmentCustomerName)
                        .appointmentId(appointmentId)
                        .appointmentPhoneNumber(appointmentPhoneNumber)
                        .appointmentSecondaryPhone(appointmentSecondaryPhone)
                        .customerName(customerName)
                        .customerPhoneNumber(s)
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
        String remark = EnumCustomerInfo.CUSTOMER_2.getRemark();
        JSONArray remarks = new JSONArray();
        remarks.add(remark);
        try {
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
            double travelMileage = response.getDouble("travel_mileage");
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

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-行驶里程")
    public void AfterSaleReceptionException4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            for (double v : travelMileageArr) {
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
                        .travelMileage(v)
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

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话2异常")
    public void AfterSaleReceptionException5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            double travelMileage = response.getDouble("travel_mileage");
            for (String s : customerNameArr) {
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
                        .customerSecondaryPhone(s)
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("联系方式2必须为11位手机号"), "联系人手机号为" + s);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话2异常");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约电话2异常")
    public void AfterSaleReceptionException6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            double travelMileage = response.getDouble("travel_mileage");
            for (String s : customerNameArr) {
                IScene scene = EditAfterSaleCustomerScene.builder()
                        .afterRecordId(afterRecordId)
                        .appointmentCustomerName(appointmentCustomerName)
                        .appointmentId(appointmentId)
                        .appointmentPhoneNumber(appointmentPhoneNumber)
                        .appointmentSecondaryPhone(s)
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
                Preconditions.checkArgument(object.getString("message").equals("预约电话2必须为11位手机号"), "联系人手机号为" + s);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约电话2异常");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-联系人1姓名异常")
    public void AfterSaleReceptionException7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            double travelMileage = response.getDouble("travel_mileage");
            for (String s : customerNameArr) {
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
                        .firstContactName(s)
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("预约电话1必须为11位手机号"), "联系人手机号为" + s);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-联系人1姓名异常");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-联系人2姓名异常--提示语有问题，填写手机号，提示手机号不能为空")
    public void AfterSaleReceptionException8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            double travelMileage = response.getDouble("travel_mileage");
            for (String s : customerNameArr) {
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
                        .secondContactName(s)
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("联系人2必须为11位手机号"), "联系人手机号为" + s);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-联系人2姓名异常--提示语有问题，填写手机号，提示手机号不能为空");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车牌号2异常")
    public void AfterSaleReceptionException9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            double travelMileage = response.getDouble("travel_mileage");
            for (String s : plateNumberArr) {
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
                        .secondPlateNumber(s)
                        .build();
                JSONObject object = crm.invokeApi(scene.getPath(), scene.getJSONObject(), false);
                Preconditions.checkArgument(object.getString("message").equals("车牌号码只允许文字+数字+大写字母") || object.getString("message").equals("请输入7-8位车牌号码位数"), " 异常车牌号：" + s);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车牌号2异常");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-维修天数异常--维修天数没有加整数限制")
    public void AfterSaleReceptionException10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            double travelMileage = response.getDouble("travel_mileage");
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
                Preconditions.checkArgument(object.getString("message").equals("维修天数必须在1～365天之间"), "维修时间天数为：" + days[i]);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-维修天数异常--维修天数没有加整数限制");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户名称不填写")
    public void AfterSaleReceptionException11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage = response.getDouble("travel_mileage");
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

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话1不填写")
    public void AfterSaleReceptionException12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage = response.getDouble("travel_mileage");
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

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约名称不填写-没有校验")
    public void AfterSaleReceptionException13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
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
            double travelMileage = response.getDouble("travel_mileage");
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

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约电话不填写--没有限制")
    public void AfterSaleReceptionException14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
            String appointmentCustomerName = response.getString("appointment_customer_name");
            Integer appointmentId = response.getInteger("appointment_id");
            if (appointmentId == null) {
                appointmentId = 0;
            }
            String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
            String customerName = response.getString("customer_name");
            String customerPhoneNumber = response.getString("customer_phone_number");
            int customerSource = response.getInteger("customer_source");
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage = response.getDouble("travel_mileage");
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

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车牌号1不填写")
    public void AfterSaleReceptionException15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            double travelMileage = response.getDouble("travel_mileage");
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

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-行驶里程不填写---必填项可以为空，没有限制")
    public void AfterSaleReceptionException16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-所属保养顾问不填写---保养顾问不是必填项？？", enabled = false)
    public void AfterSaleReceptionException17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            double travelMileage = response.getDouble("travel_mileage");
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
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-所属保养顾问不填写");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户来源不填写")
    public void AfterSaleReceptionException19() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
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
            String firstRepairCarType = response.getString("first_repair_car_type");
            String maintainSaleId = response.getString("maintain_sale_id");
            int maintainType = response.getInteger("maintain_type");
            String plateNumber = response.getString("plate_number");
            double travelMileage = response.getDouble("travel_mileage");
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

    /**
     * 获取预约时间id
     *
     * @param date 预约日期
     * @return 时间id
     */
    private Integer getTimeId(String date) {
        JSONArray list = crm.timeList(EnumAppointmentType.MAINTAIN.getType(), date).getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            if (!(list.getJSONObject(i).getInteger("left_num") == 0)) {
                return list.getJSONObject(i).getInteger("id");
            }
        }
        throw new RuntimeException("当前时间段可预约次数为0");
    }

    /**
     * 获取车辆id
     */
    private Integer getCarId() {
        JSONArray list = crm.myCarList().getJSONArray("list");
        if (!list.isEmpty()) {
            return list.getJSONObject(0).getInteger("my_car_id");
        }
        throw new DataException("该用户小程序没有绑定车");
    }
}