package com.haisheng.framework.testng.bigScreen.crm.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppointmentType;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCustomerInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.exception.DataException;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.EditAfterSaleCustomerScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 关闭的case后端无校验，前端有校验
 *
 * @author : guoliya
 * @date :  2020/10/15
 */

public class Crm_AfterSale extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    PublicMethod method = new PublicMethod();
    EnumAccount zjl = EnumAccount.ZJL_DAILY;
    EnumAppletToken applet = EnumAppletToken.BSJ_GLY_DAILY;
    private static final double travelMileage = 1000;
    String[] customerNameArr = {"1111111111111111", "^%&%$##@#$$%^&&*8"};
    String[] customerPhoneNumberArr = {"11111111111", "1337316600", "133731668066", "哈哈哈哈哈哈哈哈哈哈就", "GHBNBVHHkjl", "%^%$#@#$%&^&"};
    String[] plateNumberArr = {"哈TY12345", "京hj67877", "京U1230090", "京F8989", "京A1239456"};
    double[] travelMileageArr = {1999.99, 1666.88};
    double[] days = {0, 366, 12.555};

    @BeforeClass()
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.product = EnumTestProduce.PORSCHE_DAILY.getAbbreviation();
        commonConfig.referer = EnumTestProduce.PORSCHE_DAILY.getReferer();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.PORSCHE_DAILY.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumTestProduce.PORSCHE_DAILY.getShopId();
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
    public void createFreshCase(Method method) {
        UserUtil.login(zjl);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户姓异常")
    public void afterSaleReceptionException1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
                String appointmentPhoneNumber = response.getString("appointment_phone_number");
                String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
                String customerPhoneNumber = response.getString("customer_phone_number");
                int customerSource = response.getInteger("customer_source");
                String firstRepairCarType = response.getString("first_repair_car_type");
                String maintainSaleId = response.getString("maintain_sale_id");
                int maintainType = response.getInteger("maintain_type");
                String plateNumber = response.getString("plate_number");
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
                    String message = crm.invokeApi(scene, false).getString("message");
                    String err = "车主名称长度在1-15字之间";
                    CommonUtil.checkResult("车主姓名", s, err, message);
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户姓异常");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话--异常")
    public void afterSaleReceptionException2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
                String appointmentPhoneNumber = response.getString("appointment_phone_number");
                String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
                String customerName = response.getString("customer_name");
                int customerSource = response.getInteger("customer_source");
                String firstRepairCarType = response.getString("first_repair_car_type");
                String maintainSaleId = response.getString("maintain_sale_id");
                int maintainType = response.getInteger("maintain_type");
                String plateNumber = response.getString("plate_number");
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
                    String message = crm.invokeApi(scene, false).getString("message");
                    String err = "联系方式1必须为11位手机号";
                    CommonUtil.checkResult("车主电话", s, err, message);
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话--异常");
        }
    }


    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-备201个字")
    public void afterSaleReceptionException3() {
        logger.logCaseStart(caseResult.getCaseName());
        String remark = EnumCustomerInfo.CUSTOMER_2.getRemark();
        JSONArray remarks = new JSONArray();
        remarks.add(remark);
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
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
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .remarks(remarks)
                        .build();
                String message = crm.invokeApi(scene, false).getString("message");
                String err = "备注在10-200字之间";
                CommonUtil.checkResult("备注为", remark, err, message);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-备201个字");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-行驶里程", enabled = false)
    public void afterSaleReceptionException4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
                String appointmentPhoneNumber = response.getString("appointment_phone_number");
                String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
                String customerName = response.getString("customer_name");
                String customerPhoneNumber = response.getString("customer_phone_number");
                int customerSource = response.getInteger("customer_source");
                String firstRepairCarType = response.getString("first_repair_car_type");
                String maintainSaleId = response.getString("maintain_sale_id");
                int maintainType = response.getInteger("maintain_type");
                String plateNumber = response.getString("plate_number");
                for (double s : travelMileageArr) {
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
                            .travelMileage(s)
                            .build();
                    String message = crm.invokeApi(scene, false).getString("message");
                    String err = "行驶里程数字错误";
                    CommonUtil.checkResult("行驶里程为", s, err, message);
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-行驶里程");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话2异常")
    public void afterSaleReceptionException5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
                String appointmentPhoneNumber = response.getString("appointment_phone_number");
                String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
                String customerName = response.getString("customer_name");
                String customerPhoneNumber = response.getString("customer_phone_number");
                int customerSource = response.getInteger("customer_source");
                String firstRepairCarType = response.getString("first_repair_car_type");
                String maintainSaleId = response.getString("maintain_sale_id");
                int maintainType = response.getInteger("maintain_type");
                String plateNumber = response.getString("plate_number");
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
                    String message = crm.invokeApi(scene, false).getString("message");
                    String err = "联系方式2必须为11位手机号";
                    CommonUtil.checkResult("车主电话2", s, err, message);
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话2异常");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约电话2异常", enabled = false)
    public void afterSaleReceptionException6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
                String appointmentPhoneNumber = response.getString("appointment_phone_number");
                String customerName = response.getString("customer_name");
                String customerPhoneNumber = response.getString("customer_phone_number");
                int customerSource = response.getInteger("customer_source");
                String firstRepairCarType = response.getString("first_repair_car_type");
                String maintainSaleId = response.getString("maintain_sale_id");
                int maintainType = response.getInteger("maintain_type");
                String plateNumber = response.getString("plate_number");
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
                    String message = crm.invokeApi(scene, false).getString("message");
                    String err = "预约电话2必须为11位手机号";
                    CommonUtil.checkResult("预约电话2", s, err, message);
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约电话2异常");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-联系人1姓名异常")
    public void afterSaleReceptionException7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
                String appointmentPhoneNumber = response.getString("appointment_phone_number");
                String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
                String customerName = response.getString("customer_name");
                String customerPhoneNumber = response.getString("customer_phone_number");
                int customerSource = response.getInteger("customer_source");
                String firstRepairCarType = response.getString("first_repair_car_type");
                String maintainSaleId = response.getString("maintain_sale_id");
                int maintainType = response.getInteger("maintain_type");
                String plateNumber = response.getString("plate_number");
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
                            .firstContactPhone("15623456666")
                            .firstContactRelation(0)
                            .build();
                    String message = crm.invokeApi(scene, false).getString("message");
                    String err = "联系人1姓名长度在1-10字之间";
                    CommonUtil.checkResult("联系人1姓名", s, err, message);
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-联系人1姓名异常");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-联系人2姓名异常--提示语有问题，填写手机号，提示手机号不能为空")
    public void afterSaleReceptionException8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
                String appointmentPhoneNumber = response.getString("appointment_phone_number");
                String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
                String customerName = response.getString("customer_name");
                String customerPhoneNumber = response.getString("customer_phone_number");
                int customerSource = response.getInteger("customer_source");
                String firstRepairCarType = response.getString("first_repair_car_type");
                String maintainSaleId = response.getString("maintain_sale_id");
                int maintainType = response.getInteger("maintain_type");
                String plateNumber = response.getString("plate_number");
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
                            .secondContactPhone("15623456666")
                            .secondContactRelation(0)
                            .build();
                    String message = crm.invokeApi(scene, false).getString("message");
                    String err = "联系人2姓名长度在1-10字之间";
                    CommonUtil.checkResult("联系人2姓名", s, err, message);
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-联系人2姓名异常--提示语有问题，填写手机号，提示手机号不能为空");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-陪同车牌2异常")
    public void afterSaleReceptionException9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
                String appointmentPhoneNumber = response.getString("appointment_phone_number");
                String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
                String customerName = response.getString("customer_name");
                String customerPhoneNumber = response.getString("customer_phone_number");
                int customerSource = response.getInteger("customer_source");
                String firstRepairCarType = response.getString("first_repair_car_type");
                String maintainSaleId = response.getString("maintain_sale_id");
                int maintainType = response.getInteger("maintain_type");
                String plateNumber = response.getString("plate_number");
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
                    String message = crm.invokeApi(scene, false).getString("message");
                    String err = (s.length() == 7 || s.length() == 8) ? "车牌号码只允许文字+数字+大写字母" : "请输入7-8位车牌号码位数";
                    CommonUtil.checkResult("陪同车牌2", s, err, message);
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-陪同车牌2异常");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-维修天数异常", enabled = false)
    public void afterSaleReceptionException10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
                String appointmentPhoneNumber = response.getString("appointment_phone_number");
                String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
                String customerName = response.getString("customer_name");
                String customerPhoneNumber = response.getString("customer_phone_number");
                int customerSource = response.getInteger("customer_source");
                String firstRepairCarType = response.getString("first_repair_car_type");
                String maintainSaleId = response.getString("maintain_sale_id");
                int maintainType = response.getInteger("maintain_type");
                String plateNumber = response.getString("plate_number");
                for (double s : days) {
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
                            .estimateRepairDays(s)
                            .build();
                    String message = crm.invokeApi(scene, false).getString("message");
                    String err = "维修天数必须在1～365天之间";
                    CommonUtil.checkResult("维修天数", s, err, message);
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-维修天数异常");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户名称不填写")
    public void afterSaleReceptionException11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
                String appointmentPhoneNumber = response.getString("appointment_phone_number");
                String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
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
                        .customerPhoneNumber(customerPhoneNumber)
                        .customerSource(customerSource)
                        .firstRepairCarType(firstRepairCarType)
                        .maintainSaleId(maintainSaleId)
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .build();
                String message = crm.invokeApi(scene, false).getString("message");
                String err = "客户姓名不允许为空";
                CommonUtil.checkResult("客户名称", null, err, message);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户名称不填写");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话1不填写", enabled = false)
    public void afterSaleReceptionException12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
                String appointmentPhoneNumber = response.getString("appointment_phone_number");
                String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
                String customerName = response.getString("customer_name");
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
                        .customerSource(customerSource)
                        .firstRepairCarType(firstRepairCarType)
                        .maintainSaleId(maintainSaleId)
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .build();
                String message = crm.invokeApi(scene, false).getString("message");
                String err = "车主电话1不允许为空";
                CommonUtil.checkResult("车主电话1", null, err, message);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车主电话1不填写");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约名称不填写", enabled = false)
    public void afterSaleReceptionException13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                Integer appointmentId = response.getInteger("appointment_id");
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
                String message = crm.invokeApi(scene, false).getString("message");
                String err = "预约名称不允许为空";
                CommonUtil.checkResult("预约名称", null, err, message);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约名称不填写");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约电话不填写", enabled = false)
    public void afterSaleReceptionException14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
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
                String message = crm.invokeApi(scene, false).getString("message");
                String err = "预约电话不允许为空";
                CommonUtil.checkResult("预约电话", null, err, message);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-预约电话不填写");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车牌号1不填写")
    public void afterSaleReceptionException15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
                String appointmentPhoneNumber = response.getString("appointment_phone_number");
                String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
                String customerName = response.getString("customer_name");
                String customerPhoneNumber = response.getString("customer_phone_number");
                int customerSource = response.getInteger("customer_source");
                String firstRepairCarType = response.getString("first_repair_car_type");
                String maintainSaleId = response.getString("maintain_sale_id");
                int maintainType = response.getInteger("maintain_type");
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
                String message = crm.invokeApi(scene, false).getString("message");
                String err = "车牌号1不允许为空";
                CommonUtil.checkResult("车牌号1", null, err, message);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-车牌号1不填写");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-行驶里程不填写", enabled = false)
    public void afterSaleReceptionException16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
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
                String message = crm.invokeApi(scene, false).getString("message");
                String err = "行驶里程不允许为空";
                CommonUtil.checkResult("行驶里程", null, err, message);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-行驶里程不填写");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-所属保养顾问不填写---保养顾问不是必填项？？", enabled = false)
    public void afterSaleReceptionException17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
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
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .build();
                JSONObject object = crm.invokeApi(scene, false);
                Preconditions.checkArgument(object.getString("message").equals(""), " ");
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-所属保养顾问不填写");
        }
    }

    @Test(description = "APP-售后客户-我的接待-编辑维修中新客（创建客户）--异常情况-客户来源不填写")
    public void afterSaleReceptionException19() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appointmentRepair();
            int id = method.getAfterRecordId(false, 30);
            String afterRecordId = String.valueOf(id);
            if (!afterRecordId.equals("0")) {
                JSONObject response = crm.detailAfterSaleCustomer(afterRecordId);
                String appointmentCustomerName = response.getString("appointment_customer_name");
                Integer appointmentId = response.getInteger("appointment_id");
                String appointmentPhoneNumber = response.getString("appointment_phone_number");
                String appointmentSecondaryPhone = response.getString("appointment_secondary_phone");
                String customerName = response.getString("customer_name");
                String customerPhoneNumber = response.getString("customer_phone_number");
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
                        .firstRepairCarType(firstRepairCarType)
                        .maintainSaleId(maintainSaleId)
                        .maintainType(maintainType)
                        .plateNumber(plateNumber)
                        .travelMileage(travelMileage)
                        .build();
                String message = crm.invokeApi(scene, false).getString("message");
                String err = "客户来源不允许为空";
                CommonUtil.checkResult("客户来源", null, err, message);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
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

    private void appointmentRepair() throws Exception {
        String date = DateTimeUtil.getFormat(new Date());
        boolean haveTask = false;
        JSONArray list = crm.receptionAfterCustomerList("", "", "", 1, 100).getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            if (list.getJSONObject(i).getString("reception_status_name").equals("维修中")
                    && !list.getJSONObject(i).getBoolean("service_complete")) {
                CommonUtil.warning("存在未完成接待的维修任务");
                haveTask = true;
                break;
            }
        }
        if (!haveTask) {
            UserUtil.loginApplet(applet);
            int id = getTimeId(date);
            crm.appointmentRepairCode((long) getCarId(), "Max", "13373166806", "测试测试", (long) id, "");
            UserUtil.login(zjl);
            //预约中状态查询
            JSONArray array = crm.mainAppointmentList(1, 10).getJSONArray("list");
            for (int j = 0; j < array.size(); j++) {
                if (array.getJSONObject(j).getString("service_status_name").equals("预约中")) {
                    int appointmentId = array.getJSONObject(j).getInteger("appointment_id");
                    //接待
                    crm.reception_customer((long) appointmentId);
                    break;
                }
            }
        }
    }
}