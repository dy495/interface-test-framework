package com.haisheng.framework.testng.bigScreen.crm.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCarModel;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCustomerInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCustomerLevel;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCustomerType;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumReturnVisitResult;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * CRM-App-System 自动化用例
 *
 * @author wangmin
 */
public class AppSystem extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    private static final EnumAccount xs = EnumAccount.XSGW_DAILY;
    private static final EnumAccount zjl = EnumAccount.ZJL_DAILY;
    private static final EnumAccount qt = EnumAccount.QT_DAILY;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getName());
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_SHOP.getShopId();
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
        CommonUtil.login(xs);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

//    ---------------------------------------------------2.0------------------------------------------------------------

    @Test(enabled = false)
    public void registeredCustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        int activityTaskId = 0;
        int activityId = 0;
        try {
            CommonUtil.login(xs);
            JSONArray list = crm.activityTaskPage(1, 10).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getBoolean("is_edit")) {
                    activityTaskId = list.getJSONObject(i).getInteger("activity_task_id");
                    activityId = list.getJSONObject(i).getInteger("activity_id");
                    break;
                }
            }
            CommonUtil.login(zjl);
            int activityCustomer = crm.customerTaskPage(10, 1, (long) activityId).getJSONArray("list").size();
            CommonUtil.login(xs);
            //添加报名信息
            crm.registeredCustomer((long) activityTaskId, "张三", "13454678912");
            //pc任务客户数量+1
            CommonUtil.login(zjl);
            int activityCustomer1 = crm.customerTaskPage(10, 1, (long) activityId).getJSONArray("list").size();
            Preconditions.checkArgument(activityCustomer1 == activityCustomer + 1, "添加报名人信息后，pc端任务活动未+1");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            CommonUtil.login(xs);
            int customerId = 0;
            JSONArray list = crm.customerTaskPage(10, 1, (long) activityId).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("customer_phone_number").equals("13454678912")) {
                    customerId = list.getJSONObject(i).getInteger("id");
                }
            }
            //删除报名人
            crm.deleteCustomer(String.valueOf(activityTaskId), customerId);
            saveData("添加报名人信息");
        }
    }

    @Test(description = "创建接待", enabled = false)
    public void createReception() {
        logger.logCaseStart(caseResult.getCaseName());
        //分配销售
        try {
            //登录前台账号
            CommonUtil.login(qt);
            //创建接待
            JSONObject response = crm.saleReceptionCreatReception();
            if (response.getString("message").equals("当前没有空闲销售~")) {
                //登录销售账号
                CommonUtil.login(xs);
                long customerId = crm.userInfService().getLong("customer_id");
                //完成接待
                crm.finishReception(customerId, 7, "测试顾客1", "", "H级客户-taskListChkNum-修改时间为昨天");
                //再次分配
                crm.saleReceptionCreatReception();
            }
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建接待");
        }
    }

    /**
     * @description: 售后-工作管理-我的回访
     */
    @Test(description = "开始时间<=结束时间,成功")
    public void afterSaleMyReturnVisit_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            crm.afterSale_VisitRecordList(1, 1, "", date, date1);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，开始时间<=结束时间");
        }
    }

    @Test(description = "仅输入开始时间")
    public void afterSaleMyReturnVisit_function_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            crm.afterSale_VisitRecordList(1, 10, "", date, "");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，进输入开始时间");
        }
    }

    @Test(description = "仅输入结束时间")
    public void afterSaleMyReturnVisit_function_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            crm.afterSale_VisitRecordList(1, 10, "", "", date);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，进输入开始时间");
        }
    }

    @Test(description = "结束时间>开始时间")
    public void afterSaleMyReturnVisit_function_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            boolean flag;
            JSONArray list = crm.afterSale_VisitRecordList(1, 10, "", date1, date).getJSONArray("list");
            flag = list.size() == 0;
            Preconditions.checkArgument(flag, "结束时间>开始时间,查询成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，结束时间>开始时间");
        }
    }

//    ---------------------------------------------------2.1------------------------------------------------------------

    @Test(description = "创建线索,客户姓名为汉字-长度1-10")
    public void myCustomer_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        EnumCustomerInfo customerInfo = EnumCustomerInfo.CUSTOMER_1;
        EnumCarModel carModel = EnumCarModel.PANAMERA_TURBO_S_E_HYBRID_SPORT_TURISMO;
        String name = customerInfo.getName();
        String remark = customerInfo.getRemark();
        String customerLevel = String.valueOf(EnumCustomerLevel.B.getId());
        String str = "!@#$%^&*()12345678历史记录计算机asdfghj";
        try {
            //汉字，10字之内
            JSONObject response = crm.customerCreate(name, customerLevel, getDistinctPhone(), carModel.getModelId(), carModel.getStyleId(), remark);
            Preconditions.checkArgument(response.getString("message").equals("成功"), "客户姓名为汉字，长度1-10个字内创建线索失败");
            //汉字，1字
            JSONObject response1 = crm.customerCreate("王", customerLevel, getDistinctPhone(), carModel.getModelId(), carModel.getStyleId(), remark);
            Preconditions.checkArgument(response1.getString("message").equals("成功"), "客户姓名为汉字，长度1个字创建线索失败");
            //汉字，10个字
            JSONObject response2 = crm.customerCreate("我的名字十个字不信你数", customerLevel, getDistinctPhone(), carModel.getModelId(), carModel.getStyleId(), remark);
            Preconditions.checkArgument(response2.getString("message").equals("成功"), "客户姓名为汉字，长度1个字创建线索失败");
            //备注包含中英文、汉字、符号、数字
            JSONObject response3 = crm.customerCreate(name, customerLevel, getDistinctPhone(), carModel.getModelId(), carModel.getStyleId(), str);
            Preconditions.checkArgument(response3.getString("message").equals("成功"), "客户姓名为汉字，长度1个字创建线索失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索,客户姓名为汉字，长度1-10，备注20-200字之内");
        }
    }

    @Test(description = "创建线索,意向车型与商品管理中车型一致")
    public void myCustomer_function_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.afterSaleEnumInfo();
            JSONArray carTypes = response.getJSONArray("car_types");
            for (int i = 0; i < carTypes.size(); i++) {
                String carModelName = carTypes.getJSONObject(i).getString("car_model_name");
                int carModel = carTypes.getJSONObject(i).getInteger("car_model");
                JSONArray carList = crm.carList().getJSONArray("list");
                for (int j = 0; j < carList.size(); j++) {
                    if (carList.getJSONObject(j).getInteger("id") == carModel) {
                        String carTypeName1 = carList.getJSONObject(j).getString("car_type_name");
                        Preconditions.checkArgument(carModelName.equals(carTypeName1), "创建线索中意向车型与商品管理中车辆不一致");
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索,意向车型与商品管理中车型一致");
        }
    }

    @Test(description = "客户级别,下拉，HABCFG")
    public void myCustomer_function_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String strings = "H,A,B,C,F,G";
        try {
            JSONArray list = crm.appCustomerLevelList().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String level = list.getJSONObject(i).getString("level");
                CommonUtil.valueView(level);
                Preconditions.checkArgument(strings.contains(level), "客户级别不包含" + strings + "之一");
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索，客户级别,下拉，包含HABCFG");
        }
    }

    @Test(description = "创建线索，不填写必填项")
    public void myCustomer_function_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String remark = EnumCustomerInfo.CUSTOMER_1.getRemark();
        EnumCarModel car = EnumCarModel.PANAMERA_TURBO_S_E_HYBRID_SPORT_TURISMO;
        String customerLevel = String.valueOf(EnumCustomerLevel.B.getId());
        try {
            String phone = getDistinctPhone();
            //不填客户名称
            JSONObject response = crm.customerCreate("", customerLevel, phone, car.getModelId(), car.getStyleId(), remark);
            Preconditions.checkArgument(response.getString("message").equals("顾客姓名不能为空"), "顾客姓名为空也可创建成功");
            //不填意向车型
//            JSONObject response1 = crm.customerCreate("望京", customerLevel, phone, "", "", remark);
//            Preconditions.checkArgument(response1.getString("message").equals("意向车型不能为空"), "意向车型不存在也可创建成功");
            //不填电话
            JSONObject response2 = crm.customerCreate("望京", customerLevel, "", car.getModelId(), car.getStyleId(), remark);
            Preconditions.checkArgument(response2.getString("message").equals("顾客手机号不能为空"), "顾客手机号为空也可创建成功");
            //不填客户级别
            JSONObject response3 = crm.customerCreate("望京", "", phone, car.getModelId(), car.getStyleId(), remark);
            Preconditions.checkArgument(response3.getString("message").equals("顾客等级不能为空"), "顾客等级为空也可创建成功");
            //不填备注
            JSONObject response4 = crm.customerCreate("望京", customerLevel, phone, car.getModelId(), car.getStyleId(), "");
            Preconditions.checkArgument(response4.getString("message").equals("备注信息不能为空"), "备注信息为空也可创建成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索，不填写必填项");
        }
    }

    @Test(description = "创建线索，填写全部必填项,备注长度201，联系方式：系统存在")
    public void myCustomer_function_5() {
        logger.logCaseStart(caseResult.getCaseName());
        String name = EnumCustomerInfo.CUSTOMER_1.getName();
        EnumCarModel car = EnumCarModel.PANAMERA_TURBO_S_E_HYBRID_SPORT_TURISMO;
        String remark = EnumCustomerInfo.CUSTOMER_1.getRemark();
        String remarks = EnumCustomerInfo.CUSTOMER_2.getRemark();
        String customerLevel = String.valueOf(EnumCustomerLevel.B.getId());
        try {
            String phone = getDistinctPhone();
            String customerPhone = null;
            //获取一个存在的电话号码
            JSONArray list = crm.customerPage(10, 1, "", "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                customerPhone = list.getJSONObject(i).getString("customer_phone");
                if (customerPhone == null) {
                    customerPhone = list.getJSONObject(i + 1).getString("customer_phone");
                }
            }
            //创建存在的电话号
            JSONObject response = crm.customerCreate(name, customerLevel, customerPhone, car.getModelId(), car.getStyleId(), remark);
            Preconditions.checkArgument(response.getString("message").equals("当前手机号已被使用！请重新输入"), "手机号码重复也可创建成功");
            //备注长度201
            JSONObject response1 = crm.customerCreate(name, customerLevel, phone, car.getModelId(), car.getStyleId(), remarks);
            Preconditions.checkArgument(response1.getString("message").equals("备注信息20-200字之间"), "备注信息超过200字也可创建成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索，不填写必填项");
        }
    }

    /**
     * 工作管理-我的回访
     */
    @Test(description = "开始时间<=结束时间,成功")
    public void myReturnVisit_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            crm.returnVisitTaskPage(1, 10, date, date1).getJSONArray("list");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，开始时间<=结束时间");
        }
    }

    @Test(description = "仅输入开始时间")
    public void myReturnVisit_function_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            crm.returnVisitTaskPage(1, 10, date, "").getJSONArray("list");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，进输入开始时间");
        }
    }

    @Test(description = "仅输入结束时间")
    public void myReturnVisit_function_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            crm.returnVisitTaskPage(1, 10, "", date).getJSONArray("list");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，进输入开始时间");
        }
    }

    @Test(description = "结束时间>开始时间")
    public void myReturnVisit_function_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            JSONArray list = crm.returnVisitTaskPage(1, 10, date1, date).getJSONArray("list");
            Preconditions.checkArgument(list.size() == 0, "结束时间>开始时间,查询成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，结束时间>开始时间");
        }
    }

    @Test(description = "排序,一级：未联系在上，已联系在下", enabled = false)
    public void myReturnVisit_function_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(xs);
            int s = 0;
            int total = crm.returnVisitTaskPage(1, 1, "", "").getInteger("total");
            int page1 = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < page1; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("task_status_name").equals("未完成")) {
                        s++;
                    }
                }
            }
            CommonUtil.valueView(s);
            int page2 = CommonUtil.pageTurning(s, 100);
            for (int i = 1; i < page2; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    Preconditions.checkArgument(list.getJSONObject(j).getString("task_status_name").equals("未完成"));

                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        }
    }

    @Test(description = "排序,二级：相同状态时间倒序排列", enabled = false)
    public void myReturnVisit_function_6() {

    }

    /**
     * @description: 回访数据中存在空电话号，垃圾数据
     */
    @Test(description = "手机号为11位手机号")
    public void myReturnVisit_function_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int s = CommonUtil.pageTurning(response.getInteger("total"), 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    String customerPhone = list.getJSONObject(j).getString("customer_phone");
                    Preconditions.checkArgument(!StringUtils.isEmpty(customerPhone), "我的回访存在空电话号码");
                    Preconditions.checkArgument(customerPhone.length() == 11, "我的回访存在非11位电话号，电话号为：" + customerPhone);
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访，手机号为11位手机号码");
        }
    }

    @Test(description = "作为所属销售的客户")
    public void myReturnVisit_function_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(xs);
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int s = CommonUtil.pageTurning(response.getInteger("total"), 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    String belongsSaleName = list.getJSONObject(i).getString("belongs_sale_name");
                    Preconditions.checkArgument(belongsSaleName.equals(xs.getUsername()), "我的回访列表，存在非当前登录账号的回访任务。所属销售为：" + belongsSaleName);
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访任务里 我看不到别人的客户 那些客户的所属销售 都是当前登陆账号");
        }
    }

    @Test(description = "列表项包括:所属销售、客户等级、客户名称、联系电话、意向车系、回访类型、是否完成")
    public void myReturnVisit_function_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.returnVisitTaskPage(1, 10, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("belongs_sale_name"), "接口返回参数中不包含字段：belongs_sale_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("customer_level_name"), "接口返回参数中不包含字段：customer_level_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("customer_name"), "接口返回参数中不包含字段：customer_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("customer_phone"), "接口返回参数中不包含字段：customer_phone");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("intention_car_style_name"), "接口返回参数中不包含字段：intention_car_style_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("customer_type_name"), "接口返回参数中不包含字段：customer_type_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("task_status_name"), "接口返回参数中不包含字段：task_status_name");
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访列表包括:所属销售、客户等级、客户名称、联系电话、意向车型、回访类型、是否完成");
        }
    }

    @Test(description = "回访类型:潜客，创建接待时“订车”标记为否的客户")
    public void myReturnVisit_function_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(xs);
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            for (int j = 1; j < CommonUtil.pageTurning(total, 100); j++) {
                JSONArray list = crm.returnVisitTaskPage(j, 100, "", "").getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("customer_type_name").equals(EnumCustomerType.PROSPECTIVE_CUSTOMER.getName())) {
                        String customerPhone = list.getJSONObject(i).getString("customer_phone");
                        if (StringUtils.isEmpty(customerPhone)) {
                            continue;
                        }
                        CommonUtil.valueView("电话号是" + customerPhone);
                        JSONObject result = crm.customerList("", customerPhone, "", "", "", 1, 10);
                        String ifByCarName = result.getJSONArray("list").getJSONObject(0).getString("buy_car_name");
                        CommonUtil.valueView(ifByCarName);
                        Preconditions.checkArgument(ifByCarName.equals("否"), "回访类型:潜客，创建接待时不是“订车”标记为否的客户");
                        CommonUtil.log("分割线");
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访类型:潜客，创建接待时“订车”标记为否的客户");
        }
    }

    /**
     * @description: 有垃圾数据-回访电话号非电话格式
     */
    @Test(description = "回访类型:成交，创建接待时“订车”标记为是的客户")
    public void myReturnVisit_function_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(xs);
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            for (int j = 1; j < CommonUtil.pageTurning(total, 100); j++) {
                JSONArray list = crm.returnVisitTaskPage(j, 100, "", "").getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("customer_type_name").equals(EnumCustomerType.CHANGE_HANDS.getName())) {
                        String customerPhone = list.getJSONObject(i).getString("customer_phone");
                        if (StringUtils.isEmpty(customerPhone)) {
                            continue;
                        }
                        CommonUtil.valueView("电话号是" + customerPhone);
                        JSONObject result = crm.customerList("", customerPhone, "", "", "", 1, 10);
                        String ifByCarName = result.getJSONArray("list").getJSONObject(0).getString("buy_car_name");
                        CommonUtil.valueView(ifByCarName);
                        Preconditions.checkArgument(ifByCarName.equals("是"), "回访类型:成交，创建接待时不是“订车”标记为是的客户");
                        CommonUtil.log("分割线");
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访类型:成交，创建接待时“订车”标记为是的客户");
        }
    }

    @Test(description = "回访类型:预约，预约记录中存在")
    public void myReturnVisit_function_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.returnVisitTaskPage(1, 100, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("customer_type_name").equals(EnumCustomerType.APPOINTMENT.getName())) {
                    String customerPhone;
                    customerPhone = list.getJSONObject(i).getString("customer_phone");
                    if (StringUtils.isEmpty(customerPhone)) {
                        continue;
                    }
                    CommonUtil.valueView("电话号是:" + customerPhone);
                    JSONObject result = crm.appointmentTestDriverList(customerPhone, "", "", 1, 10);
                    JSONArray list1 = result.getJSONArray("list");
                    CommonUtil.valueView(list1);
                    Preconditions.checkArgument(list1.size() >= 1, "回访类型:预约，预约记录中不存在此电话号" + "电话号是：" + customerPhone);
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访类型:预约，工作管理-我的预约中存该手机号");
        }
    }

    /**
     * @description: 可能逻辑有改动，暂时关闭修改
     */
    @Test(description = "查看,有截图,show_pic字段为true", enabled = false)
    public void myReturnVisit_function_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("task_status_name").equals("已完成")) {
                        boolean showPic = list.getJSONObject(j).getBoolean("show_pic");
                        int taskId = list.getJSONObject(j).getInteger("task_id");
                        CommonUtil.valueView(showPic, taskId);
                        JSONArray picList = crm.showPicResult(taskId).getJSONArray("data").getJSONObject(0).getJSONArray("pic_list");
                        Preconditions.checkArgument(showPic, "已完成的回访任务无法查看");
                        Preconditions.checkArgument(picList.size() > 0, "已完成的回访任务无截图");
                        CommonUtil.log("分割线");
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访,查看已完成任务,有图片,show_pic字段为true");
        }
    }

    @Test(description = "查看,无截图置灰,show_pic字段为false")
    public void myReturnVisit_function_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (!list.getJSONObject(j).getBoolean("show_pic")) {
                        String taskStatusName = list.getJSONObject(j).getString("task_status_name");
                        CommonUtil.valueView(taskStatusName);
                        Preconditions.checkArgument(taskStatusName.equals("未完成"), "无截图的回访任务taskStatusName不为未完成");
                        CommonUtil.log("分割线");
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访,未完成任务无图片,按钮置灰,show_pic字段为false");
        }
    }

    @Test(description = "回访客户,接听,是否完成=已完成")
    public void myReturnVisit_function_15() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            CommonUtil.login(zjl);
            //获取回访列表
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 1, "task_id");
            //回访
            crm.returnVisitTaskExecute(common, "", "", false, date, "", preBuyCarTime, EnumReturnVisitResult.ANSWER.getType(), String.valueOf(taskId), picture);
            CommonUtil.valueView(taskId);
            String taskStatusName = null;
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int j = 1; j < s; j++) {
                JSONArray list = crm.returnVisitTaskPage(j, 100, "", "").getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                        taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    }
                }
            }
            CommonUtil.valueView(taskStatusName);
            Preconditions.checkArgument(taskStatusName != null && taskStatusName.equals("已完成"), "完成回访后,是否完成状态未变为已完成");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访客户");
        }
    }

    @Test(description = "回访客户,其他,是否完成=已完成")
    public void myReturnVisit_function_16() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            CommonUtil.login(zjl);
            //获取回访列表
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 1, "task_id");
            //回访
            crm.returnVisitTaskExecute(common, "", "", false, date, "", preBuyCarTime, EnumReturnVisitResult.OTHER.getType(), String.valueOf(taskId), picture);
            CommonUtil.valueView(taskId);
            String taskStatusName = null;
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int j = 1; j < s; j++) {
                JSONArray list = crm.returnVisitTaskPage(j, 100, "", "").getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                        taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    }
                }
            }
            CommonUtil.valueView(taskStatusName);
            Preconditions.checkArgument(taskStatusName != null && taskStatusName.equals("已完成"), "完成回访后,是否完成状态未变为已完成");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访客户");
        }
    }

    @Test(description = "回访客户,无人接听,是否完成=未完成")
    public void myReturnVisit_function_17() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            CommonUtil.login(zjl);
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            crm.returnVisitTaskExecute(common, "", "", false, date, "", preBuyCarTime, EnumReturnVisitResult.NO_ONE_ANSWER.getType(), String.valueOf(taskId), picture);
            CommonUtil.valueView(taskId);
            //验证是否完成为未完成
            JSONArray list = crm.returnVisitTaskPage(1, 10, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                    String taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    CommonUtil.valueView(taskStatusName);
                    Preconditions.checkArgument(taskStatusName.equals("未完成"), "第一次回访为无人接听时,回访任务状态不为未完成");
                }
            }
            crm.returnVisitTaskExecute(common, "", "", false, date, "", preBuyCarTime, EnumReturnVisitResult.ANSWER.getType(), String.valueOf(taskId), picture);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访选择第一次无人接听时,是否完成=未完成");
        }
    }

    @Test(description = "回访客户,挂断,是否完成=未完成")
    public void myReturnVisit_function_18() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            CommonUtil.login(zjl);
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            crm.returnVisitTaskExecute(common, "", "", false, date, "", preBuyCarTime, EnumReturnVisitResult.HANG_UP.getType(), String.valueOf(taskId), picture);
            CommonUtil.valueView(taskId);
            //验证是否完成为未完成
            JSONArray list = crm.returnVisitTaskPage(1, 10, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                    String taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    CommonUtil.valueView(taskStatusName);
                    Preconditions.checkArgument(taskStatusName.equals("未完成"), "第一次回访为挂断时,回访任务状态不为未完成");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访选择第一次挂断时,是否完成=未完成");
        }
    }

    @Test(description = "回访客户,稍后联系,是否完成=未完成")
    public void myReturnVisit_function_19() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            CommonUtil.login(zjl);
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            crm.returnVisitTaskExecute(common, "", "", false, date, "", preBuyCarTime, EnumReturnVisitResult.CONTACT_LATER.getType(), String.valueOf(taskId), picture);
            CommonUtil.valueView(taskId);
            //验证是否完成为未完成
            JSONArray list = crm.returnVisitTaskPage(1, 10, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                    String taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    CommonUtil.valueView(taskStatusName);
                    Preconditions.checkArgument(taskStatusName.equals("未完成"), "第一次回访为稍后联系时,回访任务状态不为未完成");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访选择第一次稍后联系时,是否完成=未完成");
        }
    }

    @Test(description = "回访客户,错号,是否完成=已完成,所有等级产生的回访任务取消", enabled = false)
    public void myReturnVisit_function_20() {
        logger.logCaseStart(caseResult.getCaseName());

    }

    @Test(description = "回访记录,内容,汉字英文数字符号,10-200字")
    public void myReturnVisit_function_21() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String common = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890[]@-+~！#$^&()={}|;:'\"<>.?/·！￥" +
                "……（）——【】、；：”‘《》。？、,%* 浩浩乎如冯虚御风，而不知其所止；飘飘乎如遗世独立，羽化而登仙。\n" +
                "于是饮酒乐甚，扣舷而歌之。歌曰：“桂棹兮兰桨，击空明兮溯流光。渺渺兮予怀，望美人兮天一方。“";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            CommonUtil.login(zjl);
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(common, "", "", false, date, "", preBuyCarTime, EnumReturnVisitResult.ANSWER.getType(), String.valueOf(taskId), picture);
            Preconditions.checkArgument(result.getString("message").equals("成功"), "回访记录200字,回访失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访记录,内容,汉字英文数字符号,10-200字");
        }
    }

    @Test(description = "回访截图,2张-3张")
    public void myReturnVisit_function_22() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String common = "举酒属客，诵明月之诗，歌窈窕之章";
        String picPath = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/wm/multimedia/goodsmanager/";
        String picture1 = new ImageUtil().getImageBinary(picPath + "车辆照片.jpg");
        String picture2 = new ImageUtil().getImageBinary(picPath + "外观照片.jpg");
        String picture3 = new ImageUtil().getImageBinary(picPath + "大图照片.jpg");
        try {
            CommonUtil.login(zjl);
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访2张图片
            JSONObject result = crm.returnVisitTaskExecute(common, "", "", false, date, "", preBuyCarTime, EnumReturnVisitResult.CONTACT_LATER.getType(), String.valueOf(taskId), picture1, picture2);
            Preconditions.checkArgument(result.getString("message").equals("成功"), "回访截图2张,回访失败");
            //回访3张图片
            JSONObject result1 = crm.returnVisitTaskExecute(common, "", "", false, date, "", preBuyCarTime, EnumReturnVisitResult.CONTACT_LATER.getType(), String.valueOf(taskId), picture1, picture2, picture3);
            Preconditions.checkArgument(result1.getString("message").equals("成功"), "回访截图3张,回访失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访截图,2张-3张");
        }
    }

    @Test(description = "回访记录,200字以上")
    public void myReturnVisit_function_23() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String common = EnumCustomerInfo.CUSTOMER_2.getRemark();
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(common, "", "", false, date, "", preBuyCarTime, EnumReturnVisitResult.ANSWER.getType(), String.valueOf(taskId), picture);
            Preconditions.checkArgument(result.getString("message").equals("下次回访内容长度必须在10和200之间"), "下次回访内容长度必须在10和200之外也可以回访成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访记录,内容,汉字英文数字符号,200字以上");
        }
    }

    @Test(description = "下次回访日期,昨天及之前")
    public void myReturnVisit_function_24() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String common = "壬戌之秋，七月既望，苏子与客泛舟游于赤壁之下。";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(common, "", "", false, date, "", preBuyCarTime, EnumReturnVisitResult.ANSWER.getType(), String.valueOf(taskId), picture);
            Preconditions.checkArgument(result.getString("message").equals("下次回访日期不允许在今日之前"), "下次回访日期在今日之前也可以回访成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("昨天及之前不可做为下次回访日期");
        }
    }

    /**
     * @description: 前台销售排班
     */
    @Test(description = "销售排班")
    public void saleOrder_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取销售排班
            JSONObject response = crm.saleOrderList();
            String saleId = CommonUtil.getStrField(response, 0, "sale_id");
            //销售排班
            crm.saleOrder(saleId, 2);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售排班");
        }
    }

    @Test(enabled = false)
    public void afterSaleCustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.publicFaceList();
            String analysisCustomerId = CommonUtil.getStrField(response, 0, "analysis_customer_id");
            crm.afterSalelCustomer(analysisCustomerId);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后客户标记");
        }
    }

//    -----------------------------------------------------V4.0---------------------------------------------------------

    @Test(description = "正常搜索")
    public void myCustomer_function_6() {
        logger.logCaseStart(caseResult.getCaseName());
        String endDate = DateTimeUtil.getFormat(new Date());
        String startDate = DateTimeUtil.addDayFormat(new Date(), -180);
        try {
            CommonUtil.login(zjl);
            int total = crm.dccList("啦", "", "", "", "1", "10").getInteger("total");
            int total1 = crm.dccList("139", "", "", "", "1", "10").getInteger("total");
            int total2 = crm.dccList("", "", startDate, endDate, "1", "10").getInteger("total");
            int total3 = crm.dccList("啦", "", startDate, endDate, "1", "10").getInteger("total");
            int total4 = crm.dccList("139", "", startDate, endDate, "1", "10").getInteger("total");
            int total5 = crm.dccList("", "", "", "", "1", "10").getInteger("total");
            int total6 = crm.dccList("", String.valueOf(EnumCustomerLevel.C.getId()), "", "", "1", "10").getInteger("total");
            Preconditions.checkArgument(total > 0, "dcc客户按照客户名称模糊搜索失败");
            Preconditions.checkArgument(total1 > 0, "dcc客户按照电话模糊搜索失败");
            Preconditions.checkArgument(total2 > 0, "按照日期筛选，开始时间<=结束时间失败");
            Preconditions.checkArgument(total3 > 0, "按照客户名称+日期筛选失败");
            Preconditions.checkArgument(total4 > 0, "按照联系电话+日期筛选失败");
            Preconditions.checkArgument(total5 > 0, "搜索全部客户失败");
            Preconditions.checkArgument(total6 > 0, "按照等级筛选失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("dcc客户筛选，按照客户名称/电话模糊搜索");
        }
    }

    @Test(description = "异常搜索")
    public void myCustomer_function_7() {
        logger.logCaseStart(caseResult.getCaseName());
        String endDate = DateTimeUtil.getFormat(new Date());
        String startDate = DateTimeUtil.addDayFormat(new Date(), -180);
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            int total = crm.dccList("$", "", "", "", "1", "10").getInteger("total");
            int total1 = crm.dccList("111", "", "", "", "1", "10").getInteger("total");
            int total2 = crm.dccList("", String.valueOf(EnumCustomerLevel.O.getId()), "", "", "1", "10").getInteger("total");
            int total3 = crm.dccList("", "", endDate, startDate, "1", "10").getInteger("total");
            int total4 = crm.dccList("139", "", date, "", "1", "10").getInteger("total");
            int total5 = crm.dccList("111", "", date, "", "1", "10").getInteger("total");
            int total6 = crm.dccList("啦", "", date, "", "1", "10").getInteger("total");
            int total7 = crm.dccList("$", "", date, "", "1", "10").getInteger("total");
            Preconditions.checkArgument(total == 0, "查询不存在的用户查出了结果");
            Preconditions.checkArgument(total1 == 0, "查询不存在的联系电话查出了结果");
            Preconditions.checkArgument(total2 == 0, "查询不存在的等级查出了结果");
            Preconditions.checkArgument(total3 == 0, "开始时间>结束时间查出了结果");
            Preconditions.checkArgument(total4 == 0, "手机号存在，开始日期>当前日期，查出了结果");
            Preconditions.checkArgument(total5 == 0, "手机号不存在，开始日期>当前日期，查出了结果");
            Preconditions.checkArgument(total6 == 0, "姓名存在，开始日期>当前日期，查出了结果");
            Preconditions.checkArgument(total7 == 0, "姓名不存在，开始日期>当前日期，查出了结果");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("dcc客户筛选，筛选不存在的客户信息，数据为空");
        }
    }


//    ---------------------------------------------------私有方法区-------------------------------------------------------

    /**
     * 获取非重复电话号
     *
     * @return phone
     */
    private String getDistinctPhone() {
        CommonUtil.login(zjl);
        String phone = "153" + CommonUtil.getRandom(8);
        int a = crm.customerList("", phone, "", "", "", 1, 10).getInteger("total");
        int b = crm.dccList("", phone, "", "", 1, 10).getInteger("total");
        return a == 0 && b == 0 ? phone : getDistinctPhone();
    }
}
