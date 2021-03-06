package com.haisheng.framework.testng.bigScreen.jiaochenonline.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.CommonPram;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class Jc_Pc_CustomerManageOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.JC_ONLINE_JD;
    private static final EnumAccount account = EnumAccount.JC_ONLINE_YS;
    public VisitorProxy visitor = new VisitorProxy(product);
    SceneUtil util = new SceneUtil(visitor);
    ScenarioUtil jc = new ScenarioUtil();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        jc.changeIpPort(product.getIp());
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = "郭丽雅";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "jc-onLine-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.setShopId(product.getShopId()).setReferer(product.getReferer()).setRoleId("395").setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/responsese
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        util.loginPc(account);
    }

    /**
     * @description :客户管理-销售客户-异常校验-客户名称51个字-先校验注册再校验客户名称51个字--研发没有校验，数据库长度越界
     * @date :2020/12/22
     **/
    @Test(enabled = false)
    public void customerManage_System1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] phone = CommonPram.phoneNumberFormat();
            Object[][] sex = CommonPram.sex();
            Object[][] name = CommonPram.name();
            Object[][] customerType = CommonPram.customerType();
            //姓名为空
            JSONObject response = jc.createCustomer("", name[0][1].toString(), CommonPram.phoneNumber, sex[1][1].toString(), customerType[1][1].toString());
            String message = response.getString("message");
            Preconditions.checkArgument(message.equals("客户名称不允许为空"), "姓名入参为空，返回的的message为：" + message);
            //客户姓名51个字
            JSONObject response1 = jc.createCustomer("", name[0][1].toString(), "13373166806", sex[1][1].toString(), customerType[1][1].toString());
            String message1 = response1.getString("message");
            Preconditions.checkArgument(message1.equals(" "), "姓名入参51个字，返回的的message为：" + message1);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-销售客户-异常校验-客户名称51个字");
        }
    }

    /**
     * @description :客户管理-销售客户-异常校验-手机号格式校验---现在都报异常，服务端没有校验
     * @date :2020/12/22
     **/
    @Test(enabled = false)
    public void customerManage_System2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] phone = CommonPram.phoneNumberFormat();
            Object[][] sex = CommonPram.sex();
            Object[][] name = CommonPram.name();
            Object[][] customerType = CommonPram.customerType();
            for (int i = 0; i < 7; i++) {
                JSONObject response = jc.createCustomer("", CommonPram.name, phone[i][1].toString(), sex[1][1].toString(), customerType[1][1].toString());
                String message = response.getString("message");
                Preconditions.checkArgument(message.equals("手机号格式不正确") || message.equals("手机号已存在") || message.equals("手机号不能为空"), phone[i][0].toString() + " : " + phone[i][1].toString());
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-销售客户-异常校验-手机号格式校验");
        }
    }

    /**
     * @description :客户管理-销售客户-异常校验-性别异常校验---现在都报异常，服务端没有校验，message信息现在没有填写
     * @date :2020/12/22
     **/
    @Test(enabled = false)
    public void customerManage_System3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] phone = CommonPram.phoneNumberFormat();
            Object[][] sex = CommonPram.sex();
            Object[][] name = CommonPram.name();
            Object[][] customerType = CommonPram.customerType();
            for (int i = 2; i <= 3; i++) {
                JSONObject response = jc.createCustomer("", CommonPram.name, CommonPram.phoneNumber, sex[i][1].toString(), customerType[1][1].toString());
                String message = response.getString("message");
                Preconditions.checkArgument(message.equals(""), sex[i][0].toString() + " : " + sex[i][1].toString());
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-销售客户-异常校验-性别异常校验");
        }
    }

    /**
     * @description :客户管理-销售客户-异常校验-车主类型异常校验---现在都报异常，服务端没有校验,message信息现在没有填写
     * @date :2020/12/22
     **/
    @Test(enabled = false)
    public void customerManage_System4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] phone = CommonPram.phoneNumberFormat();
            Object[][] sex = CommonPram.sex();
            Object[][] name = CommonPram.name();
            Object[][] customerType = CommonPram.customerType();
            for (int i = 2; i <= 3; i++) {
                JSONObject response = jc.createCustomer("", CommonPram.name, CommonPram.phoneNumber, sex[1][1].toString(), customerType[i][1].toString());
                String message = response.getString("message");
                Preconditions.checkArgument(message.equals(""), customerType[i][0].toString() + " : " + customerType[i][1].toString());
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-销售客户-异常校验-车主类型异常校验");
        }
    }

    /**
     * @description :客户管理-销售客户-列表项校验不为空
     * @date :2020/12/22
     **/
    @Test
    public void customerManage_System5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = jc.preSleCustomerManage("", "1", "10", "", "");
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.preSleCustomerManage("", String.valueOf(page), "10", "", "").getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String shopName = list.getJSONObject(i).getString("shop_name");
                    String brandName = list.getJSONObject(i).getString("brand_name");
                    String registrationStatusName = list.getJSONObject(i).getString("registration_status_name");
//                    String plateNumber = list.getJSONObject(i).getString("plate_number");
                    String sex = list.getJSONObject(i).getString("sex");
                    String customerName = list.getJSONObject(i).getString("customer_name");
                    String customerPhone = list.getJSONObject(i).getString("customer_phone");
                    String createDate = list.getJSONObject(i).getString("create_date");
                    System.out.println("shopName" + shopName);
                    System.out.println("brandName" + brandName);
                    System.out.println("registrationStatusName" + registrationStatusName);
                    System.out.println("sex" + sex);
                    System.out.println("customerName" + customerName);
                    System.out.println("customerPhone" + customerPhone);
                    System.out.println("createDate" + createDate);
                    Preconditions.checkArgument(shopName != null && registrationStatusName != null && customerPhone != null && customerName != null && sex != null && createDate != null, "销售客户列表中第 " + (i + 1) + "行，列表项为空");
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-销售客户-列表项校验不为空");
        }
    }

    /**
     * @description :客户管理-销售客户-列表排序按照创建时间倒序排列
     * @date :2020/12/22
     **/
    @Test
    public void customerManage_System6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = jc.preSleCustomerManage("", "1", "10", "", "");
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.preSleCustomerManage("", String.valueOf(page), "10", "", "").getJSONArray("list");
                for (int i = 0; i < list.size() - 1; i++) {
                    String createDate = list.getJSONObject(i).getString("create_date");
                    String createDateNext = list.getJSONObject(i + 1).getString("create_date");
                    System.out.println("表排序按照创建时间倒序排列，现在的创建时间是:" + createDate + "下一个创建时间为：" + createDateNext);
                    Preconditions.checkArgument(createDate.compareTo(createDateNext) > 0 || createDate.compareTo(createDateNext) == 0, "表排序按照创建时间倒序排列，现在的创建时间是:" + createDate + "下一个创建时间为：" + createDateNext);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-销售客户-列表排序按照创建时间倒序排列");
        }
    }

    /**
     * @description :客户管理-售后客户-列表排序按照导入时间倒序排列
     * @date :2020/12/23
     **/
    @Test
    public void customerManage_System7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = jc.afterSleCustomerManage("", "1", "10", "", "");
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.afterSleCustomerManage("", String.valueOf(page), "10", "", "").getJSONArray("list");
                for (int i = 0; i < list.size() - 1; i++) {
                    String createDate = list.getJSONObject(i).getString("import_date");
                    String createDateNext = list.getJSONObject(i + 1).getString("import_date");
                    System.out.println("表排序按照导入时间倒序排列，现在的创建时间是:" + createDate + "下一个创建时间为：" + createDateNext);
                    Preconditions.checkArgument(createDate.compareTo(createDateNext) > 0 || createDate.compareTo(createDateNext) == 0, "表排序按照导入时间倒序排列，现在的创建时间是:" + createDate + "下一个创建时间为：" + createDateNext);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-售后客户-列表排序按照导入时间倒序排列");
        }
    }

    /**
     * @description :客户管理-售后客户-列表项校验不为空
     * @date :2020/12/23
     **/
    @Test
    public void customerManage_System8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = jc.afterSleCustomerManage("", "1", "10", "", "");
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.afterSleCustomerManage("", String.valueOf(page), "10", "", "").getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String shopName = list.getJSONObject(i).getString("shop_name");
                    String brandName = list.getJSONObject(i).getString("brand_name");
                    String registrationStatusName = list.getJSONObject(i).getString("registration_status_name");
                    String plateNumber = list.getJSONObject(i).getString("plate_number");
                    String sex = list.getJSONObject(i).getString("sex");
                    String repairCustomerName = list.getJSONObject(i).getString("repair_customer_name");
                    String repairCustomerPhone = list.getJSONObject(i).getString("repair_customer_phone");
                    String importDate = list.getJSONObject(i).getString("import_date");
                    Preconditions.checkArgument(shopName != null && brandName != null && plateNumber != null && registrationStatusName != null && repairCustomerName != null && sex != null && repairCustomerPhone != null && importDate != null, "销售客户列表中第 " + (i + 1) + "行，列表项为空");
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-售后客户-列表项校验不为空");
        }
    }

    /**
     * @description :客户管理-小程序客户-列表排序按照注册时间倒序排列
     * @date :2020/12/23
     **/
    @Test
    public void customerManage_System9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = jc.weChatSleCustomerManage("", "1", "10", "", "");
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.weChatSleCustomerManage("", String.valueOf(page), "10", "", "").getJSONArray("list");
                for (int i = 0; i < list.size() - 1; i++) {
                    String createDate = list.getJSONObject(i).getString("create_date");
                    String createDateNext = list.getJSONObject(i + 1).getString("create_date");
                    System.out.println("表排序按照注册时间倒序排列，现在的创建时间是:" + createDate + "下一个创建时间为：" + createDateNext);
                    Preconditions.checkArgument(createDate.compareTo(createDateNext) > 0 || createDate.compareTo(createDateNext) == 0, "表排序按照注册时间倒序排列，现在的创建时间是:" + createDate + "下一个创建时间为：" + createDateNext);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-小程序客户-列表排序按照注册时间倒序排列");
        }
    }

    /**
     * @description :客户管理-小程序客户-列表项校验不为空
     * @date :2020/12/23
     **/
    @Test
    public void customerManage_System10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = jc.weChatSleCustomerManage("", "1", "10", "", "");
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.weChatSleCustomerManage("", String.valueOf(page), "10", "", "").getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String createDate = list.getJSONObject(i).getString("create_date");
                    String customerPhone = list.getJSONObject(i).getString("customer_phone");
                    String activeType = list.getJSONObject(i).getString("active_type");
                    String appointmentActivity = list.getJSONObject(i).getString("appointment_activity");
                    String appointmentMaintain = list.getJSONObject(i).getString("appointment_maintain");
                    String appointmentRepair = list.getJSONObject(i).getString("appointment_repair");
                    String appointmentTestDrive = list.getJSONObject(i).getString("appointment_test_drive");
                    String arriveTimes = list.getJSONObject(i).getString("arrive_times");
                    String consumeTimes = list.getJSONObject(i).getString("consume_times");
                    Preconditions.checkArgument(createDate != null && customerPhone != null && activeType != null && appointmentActivity != null && appointmentMaintain != null && appointmentRepair != null && appointmentTestDrive != null && arriveTimes != null && consumeTimes != null, "销售客户列表中第 " + (i + 1) + "行，列表项为空");
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户管理-小程序客户-列表项校验不为空");
        }
    }

}




