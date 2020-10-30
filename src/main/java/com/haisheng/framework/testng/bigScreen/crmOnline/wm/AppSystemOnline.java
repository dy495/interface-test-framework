package com.haisheng.framework.testng.bigScreen.crmOnline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCarModel;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCustomerInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCustomerLevel;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCustomerType;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumFailureCause;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumReturnVisitResult;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.CustomerDccListScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.ReturnVisitTaskExecuteScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
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

public class AppSystemOnline<returnVisitTask> extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    private static final EnumAccount xs = EnumAccount.XSGW_ONLINE;
    private static final EnumAccount zjl = EnumAccount.ZJL_ONLINE;
    private static final EnumAccount qt = EnumAccount.QT_ONLINE;
    private static final String carModule = "82";

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_ONLINE.getName());
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.WIN_SENSE_SHOP_ONLINE.getShopId();
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

//    ---------------------------------------------------2.0------------------------------------------------------------

    @Test(enabled = false)
    public void registeredCustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        int activityTaskId = 0;
        int activityId = 0;
        try {
            UserUtil.login(xs);
            JSONArray list = crm.activityTaskPage(1, 10).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getBoolean("is_edit")) {
                    activityTaskId = list.getJSONObject(i).getInteger("activity_task_id");
                    activityId = list.getJSONObject(i).getInteger("activity_id");
                    break;
                }
            }
            UserUtil.login(zjl);
            int activityCustomer = crm.customerTaskPage(10, 1, activityId).getJSONArray("list").size();
            UserUtil.login(xs);
            //添加报名信息
            crm.registeredCustomer((long) activityTaskId, "张三", "13454678912");
            //pc任务客户数量+1
            UserUtil.login(zjl);
            int activityCustomer1 = crm.customerTaskPage(10, 1, (long) activityId).getJSONArray("list").size();
            Preconditions.checkArgument(activityCustomer1 == activityCustomer + 1, "添加报名人信息后，pc端任务活动未+1");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            UserUtil.login(xs);
            int customerId = 0;
            JSONArray list = crm.customerTaskPage(10, 1, (long) activityId).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("customer_phone_number").equals("13454678912")) {
                    customerId = list.getJSONObject(i).getInteger("id");
                }
            }
            //删除报名人
            crm.deleteCustomer(String.valueOf(activityTaskId), String.valueOf(customerId));
            saveData("添加报名人信息");
        }
    }

    @Test(description = "创建接待", enabled = false)
    public void createReception() {
        logger.logCaseStart(caseResult.getCaseName());
        //分配销售
        try {
            //登录前台账号
            UserUtil.login(qt);
            //创建接待
            JSONObject response = crm.saleReceptionCreatReception();
            if (response.getString("message").equals("当前没有空闲销售~")) {
                //登录销售账号
                UserUtil.login(xs);
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

    //    ---------------------------------------------------2.1------------------------------------------------------------
    @Test(description = "销售--客户管理--DCC客户--正常搜索")
    public void customer_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String endDate = DateTimeUtil.getFormat(new Date());
        String startDate = DateTimeUtil.addDayFormat(new Date(), -180);
        try {
            UserUtil.login(zjl);
            IScene scene = CustomerDccListScene.builder().build();
            JSONArray list = crm.invokeApi(scene).getJSONArray("list");
            String customerName = list.getJSONObject(0).getString("customer_name");
            String customerPhone = list.getJSONObject(0).getString("customer_phone");
            CommonUtil.valueView(customerName, customerPhone);
            int total = crm.invokeApi(CustomerDccListScene.builder().searchCondition(customerName.substring(0, 1)).build()).getInteger("total");
            int total1 = crm.invokeApi(CustomerDccListScene.builder().searchCondition(customerPhone.substring(0, 3)).build()).getInteger("total");
            int total2 = crm.invokeApi(CustomerDccListScene.builder().startTime(startDate).endTime(endDate).build()).getInteger("total");
            int total3 = crm.invokeApi(CustomerDccListScene.builder().searchCondition(customerName.substring(0, 3)).startTime(startDate).endTime(endDate).build()).getInteger("total");
            int total4 = crm.invokeApi(CustomerDccListScene.builder().searchCondition(customerPhone.substring(0, 3)).startTime(startDate).endTime(endDate).build()).getInteger("total");
            int total5 = crm.invokeApi(CustomerDccListScene.builder().build()).getInteger("total");
            int total6 = crm.invokeApi(CustomerDccListScene.builder().customerLevel(String.valueOf(EnumCustomerLevel.C.getId())).build()).getInteger("total");
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
            saveData("销售--客户管理--DCC客户--正常搜索");
        }
    }

    @Test(description = "销售--客户管理--DCC客户--异常搜索")
    public void customer_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String endDate = DateTimeUtil.getFormat(new Date());
        String startDate = DateTimeUtil.addDayFormat(new Date(), -180);
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            UserUtil.login(zjl);
            int total = crm.invokeApi(CustomerDccListScene.builder().searchCondition("$qqwqsss").build()).getInteger("total");
            int total1 = crm.invokeApi(CustomerDccListScene.builder().searchCondition("11158585858").build()).getInteger("total");
            int total2 = crm.invokeApi(CustomerDccListScene.builder().customerLevel(String.valueOf(EnumCustomerLevel.O.getId())).build()).getInteger("total");
            int total3 = crm.invokeApi(CustomerDccListScene.builder().startTime(endDate).endTime(startDate).build()).getInteger("total");
            int total4 = crm.invokeApi(CustomerDccListScene.builder().searchCondition("139").startTime(date).build()).getInteger("total");
            int total5 = crm.invokeApi(CustomerDccListScene.builder().searchCondition("111").startTime(date).build()).getInteger("total");
            int total6 = crm.invokeApi(CustomerDccListScene.builder().searchCondition("啦").startTime(date).build()).getInteger("total");
            int total7 = crm.invokeApi(CustomerDccListScene.builder().searchCondition("$").startTime(date).build()).getInteger("total");
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
            saveData("销售--客户管理--DCC客户--异常搜索");
        }
    }

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
            JSONObject response = crm.customerCreate(name, customerLevel, getDistinctPhone(), "82", carModel.getStyleId(), remark);
            Preconditions.checkArgument(response.getString("message").equals("成功"), "客户姓名为汉字，长度1-10个字内创建线索失败");
            //汉字，1字
            JSONObject response1 = crm.customerCreate("王", customerLevel, getDistinctPhone(), "82", carModel.getStyleId(), remark);
            Preconditions.checkArgument(response1.getString("message").equals("成功"), "客户姓名为汉字，长度1个字创建线索失败");
            //汉字，10个字
            JSONObject response2 = crm.customerCreate("我的名字十个字不信你数", customerLevel, getDistinctPhone(), "82", carModel.getStyleId(), remark);
            Preconditions.checkArgument(response2.getString("message").equals("成功"), "客户姓名为汉字，长度1个字创建线索失败");
            //备注包含中英文、汉字、符号、数字
            JSONObject response3 = crm.customerCreate(name, customerLevel, getDistinctPhone(), "82", carModel.getStyleId(), str);
            Preconditions.checkArgument(response3.getString("message").equals("成功"), "客户姓名为汉字，长度1个字创建线索失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索,客户姓名为汉字，长度1-10，备注20-200字之内");
        }
    }

    @Test(description = "创建线索,意向车型与商品管理中车型一致", enabled = false)
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
            JSONObject response = crm.customerCreate("", customerLevel, phone, carModule, car.getStyleId(), remark);
            Preconditions.checkArgument(response.getString("message").equals("顾客姓名不能为空"), "顾客姓名为空也可创建成功");
            //不填意向车型
            JSONObject response1 = crm.customerCreate("望京", customerLevel, phone, "", "", remark);
            Preconditions.checkArgument(response1.getString("message").equals("意向车型不能为空"), "意向车型不存在也可创建成功");
            //不填电话
            JSONObject response2 = crm.customerCreate("望京", customerLevel, "", carModule, car.getStyleId(), remark);
            Preconditions.checkArgument(response2.getString("message").equals("顾客手机号不能为空"), "顾客手机号为空也可创建成功");
            //不填客户级别
            JSONObject response3 = crm.customerCreate("望京", "", phone, carModule, car.getStyleId(), remark);
            Preconditions.checkArgument(response3.getString("message").equals("顾客等级不能为空"), "顾客等级为空也可创建成功");
            //不填备注
            JSONObject response4 = crm.customerCreate("望京", customerLevel, phone, carModule, car.getStyleId(), "");
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
            //获取一个存在的电话号码
            String customerPhone = null;
            JSONArray list = crm.customerPage(10, 1, "", "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                customerPhone = list.getJSONObject(i).getString("customer_phone");
                if (customerPhone == null) {
                    continue;
                }
                customerPhone = list.getJSONObject(i).getString("customer_phone");
                break;
            }
            //创建存在的电话号
            JSONObject response = crm.customerCreate(name, customerLevel, customerPhone, carModule, car.getStyleId(), remark);
            Preconditions.checkArgument(response.getString("message").equals("当前手机号已被使用！请重新输入"), "手机号码重复也可创建成功");
            //备注长度201
            String phone = getDistinctPhone();
            JSONObject response1 = crm.customerCreate(name, customerLevel, phone, carModule, car.getStyleId(), remarks);
            Preconditions.checkArgument(response1.getString("message").equals("备注信息20-200字之间"), "备注信息超过200字也可创建成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索，不填写必填项");
        }
    }

    /**
     * @description: 可能逻辑有改动，暂时关闭修改
     */
    @Test(description = "销售--回访任务--show_pic字段为true的回访任务均有截图")
    public void returnVisit_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = crm.returnVisitTaskPage(1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("task_status_name").equals("已完成")) {
                        boolean showPic = list.getJSONObject(j).getBoolean("show_pic");
                        int taskId = list.getJSONObject(j).getInteger("task_id");
                        CommonUtil.valueView("任务" + taskId + "是否有图片：" + showPic);
                        JSONArray data = crm.showPicResult(taskId).getJSONArray("data");
                        for (int u = 0; u < data.size(); u++) {
                            JSONArray picList = data.getJSONObject(u).getJSONArray("pic_list");
                            Preconditions.checkArgument(picList.size() > 0, "已完成的回访任务无截图");
                        }
                        CommonUtil.logger(String.valueOf(taskId));
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--show_pic字段为true的回访任务均有截图");
        }
    }

    @Test(description = "销售--回访任务--show_pic字段为false的回访任务均无截图且状态为未完成")
    public void returnVisit_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = crm.returnVisitTaskPage(1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (!list.getJSONObject(j).getBoolean("show_pic")) {
                        int taskId = list.getJSONObject(j).getInteger("task_id");
                        String taskStatusName = list.getJSONObject(j).getString("task_status_name");
                        CommonUtil.valueView("任务" + taskId + "是否完成：" + taskStatusName);
                        Preconditions.checkArgument(taskStatusName.equals("未完成"), "无截图的回访任务taskStatusName不为未完成");
                        CommonUtil.logger(String.valueOf(taskId));
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--show_pic字段为false的回访任务均无截图且状态为未完成");
        }
    }

    @Test(description = "销售--回访任务--回访截图2张-3张成功")
    public void returnVisit_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
            String common = "举酒属客，诵明月之诗，歌窈窕之章";
            String picPath = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/wm/multimedia/goodsmanager/";
            String picture1 = new ImageUtil().getImageBinary(picPath + "车辆照片.jpg");
            String picture2 = new ImageUtil().getImageBinary(picPath + "外观照片.jpg");
            String picture3 = new ImageUtil().getImageBinary(picPath + "大图照片.jpg");
            JSONObject response = crm.returnVisitTaskPage(1, 10);
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
            saveData("销售--回访任务--回访截图2张-3张成功");
        }
    }

    @Test(description = "销售--回访任务--回访结果为接听,是否完成=已完成")
    public void returnVisit_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            returnVisitTask(EnumReturnVisitResult.ANSWER.getType(), "已完成");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--回访结果为接听,是否完成=已完成");
        }
    }

    @Test(description = "销售--回访任务--回访结果为其他,是否完成=已完成")
    public void returnVisit_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            returnVisitTask(EnumReturnVisitResult.OTHER.getType(), "已完成");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--回访结果为其他,是否完成=已完成");
        }
    }

    @Test(description = "销售--回访任务--回访结果为无人接听,是否完成=未完成")
    public void returnVisit_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            returnVisitTask(EnumReturnVisitResult.NO_ONE_ANSWER.getType(), "未完成");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--回访结果为无人接听,是否完成=未完成");
        }
    }

    @Test(description = "销售--回访任务--回访结果为挂断,是否完成=未完成")
    public void returnVisit_system_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            returnVisitTask(EnumReturnVisitResult.HANG_UP.getType(), "未完成");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--回访结果为挂断,是否完成=未完成");
        }
    }

    @Test(description = "销售--回访任务--回访结果为稍后联系,是否完成=未完成")
    public void returnVisit_system_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            returnVisitTask(EnumReturnVisitResult.CONTACT_LATER.getType(), "未完成");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--回访结果为稍后联系,是否完成=未完成");
        }
    }

    @Test(description = "销售--回访任务--回访结果为错号,是否完成=已完成")
    public void returnVisit_system_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            returnVisitTask(EnumReturnVisitResult.WRONG_NUMBER.getType(), "已完成");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--回访结果为错号,是否完成=已完成");
        }
    }

    /**
     * 非战败回访
     *
     * @param result     回访结果
     * @param isComplete 是否完成
     */
    private void returnVisitTask(String result, String isComplete) {
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String comment = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("return_visit_pic", picture);
        array.add(object);
        JSONArray list = crm.returnVisitTaskPage(1, 10).getJSONArray("list");
        if (list.getJSONObject(0).getString("task_status_name").equals("未完成")) {
            int taskId = list.getJSONObject(0).getInteger("task_id");
            IScene scene = ReturnVisitTaskExecuteScene.builder().returnVisitPic(array).comment(comment)
                    .nextReturnVisitDate(date).preBuyCarTime(preBuyCarTime).returnVisitResult(result)
                    .taskId(String.valueOf(taskId)).build();
            crm.invokeApi(scene, true);
            String taskStatusName = null;
            int total = crm.returnVisitTaskPage(1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int j = 1; j < s; j++) {
                JSONArray list1 = crm.returnVisitTaskPage(j, 100).getJSONArray("list");
                for (int x = 0; x < list1.size(); x++) {
                    if (list1.getJSONObject(x).getInteger("task_id") == taskId) {
                        taskStatusName = list1.getJSONObject(x).getString("task_status_name");
                    }
                }
            }
            CommonUtil.valueView(taskStatusName);
            Preconditions.checkArgument(taskStatusName != null && taskStatusName.equals(isComplete), "完成回访后,是否完成状态为" + taskStatusName);
        } else {
            CommonUtil.warning("没有回访任务");
        }
    }

    @Test(description = "销售--回访任务--回访结果为战败，战败原因为他店购车，是否完成=已完成")
    public void returnVisit_system_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            returnVisitTask(EnumFailureCause.OTHER_STORE_PURCHASE_CAR, "", "82");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--回访结果为战败，战败原因为他店购车，是否完成=已完成");
        }
    }

    @Test(description = "销售--回访任务--回访结果为战败，战败原因为购买竞品，是否完成=已完成")
    public void returnVisit_system_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            returnVisitTask(EnumFailureCause.BUY_COMPETING_PRODUCTS, "凯迪拉克CT6", "");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--回访结果为战败，战败原因为购买竞品，是否完成=已完成");
        }
    }

    @Test(description = "销售--回访任务--回访结果为战败，战败原因为放弃购车，是否完成=已完成")
    public void returnVisit_system_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            returnVisitTask(EnumFailureCause.GIVE_UP_TO_BUY, "没钱不买了", "");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--回访结果为战败，战败原因为放弃购车，是否完成=已完成");
        }
    }

    /**
     * 战败回访
     *
     * @param failureCause       战败原因
     * @param failureCauseRemark 放弃原因/竞品品牌
     * @param otherStoreCarType  购买车型
     */
    private void returnVisitTask(EnumFailureCause failureCause, String failureCauseRemark, String otherStoreCarType) {
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String comment = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("return_visit_pic", picture);
        array.add(object);
        JSONArray list = crm.returnVisitTaskPage(1, 10).getJSONArray("list");
        if (list.getJSONObject(0).getString("task_status_name").equals("未完成")) {
            int taskId = list.getJSONObject(0).getInteger("task_id");
            IScene scene = ReturnVisitTaskExecuteScene.builder().returnVisitPic(array).comment(comment)
                    .nextReturnVisitDate(date).preBuyCarTime(preBuyCarTime).returnVisitResult(EnumReturnVisitResult.FAILURE.getType())
                    .taskId(String.valueOf(taskId)).failureCause(failureCause.getCause())
                    .failureCauseRemark(failureCauseRemark).otherStoreCarType(otherStoreCarType).build();
            crm.invokeApi(scene, true);
            String taskStatusName = null;
            int total = crm.returnVisitTaskPage(1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int j = 1; j < s; j++) {
                JSONArray list1 = crm.returnVisitTaskPage(j, 100).getJSONArray("list");
                for (int x = 0; x < list1.size(); x++) {
                    if (list1.getJSONObject(x).getInteger("task_id") == taskId) {
                        taskStatusName = list1.getJSONObject(x).getString("task_status_name");
                    }
                }
            }
            CommonUtil.valueView(taskStatusName);
            Preconditions.checkArgument(taskStatusName != null && taskStatusName.equals("已完成"), "完成回访后,是否完成状态为" + taskStatusName);
        } else {
            CommonUtil.warning("没有回访任务");
        }
    }

    @Test(description = "销售--回访任务--回访记录内容包含汉字、英文、数字、符号、10-200字，回访成功")
    public void returnVisit_system_13() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String common = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890[]@-+~！#$^&()={}|;:'\"<>.?/·！￥" +
                "……（）——【】、；：”‘《》。？、,%* 浩浩乎如冯虚御风，而不知其所止；飘飘乎如遗世独立，羽化而登仙。\n" +
                "于是饮酒乐甚，扣舷而歌之。歌曰：“桂棹兮兰桨，击空明兮溯流光。渺渺兮予怀，望美人兮天一方。“";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10);
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(common, "", "", false,
                    date, "", preBuyCarTime, EnumReturnVisitResult.CONTACT_LATER.getType(), String.valueOf(taskId), picture);
            Preconditions.checkArgument(result.getString("message").equals("成功"), "回访失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--回访记录内容包含汉字、英文、数字、符号、10-200字，回访成功");
        }
    }

    @Test(description = "销售--回访任务--回访记录内容200字以上，回访失败")
    public void returnVisit_system_14() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String common = EnumCustomerInfo.CUSTOMER_2.getRemark();
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10);
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(common, "", "", false,
                    date, "", preBuyCarTime, EnumReturnVisitResult.CONTACT_LATER.getType(), String.valueOf(taskId), picture);
            Preconditions.checkArgument(result.getString("message").equals("下次回访内容长度必须在10和200之间"), "下次回访内容长度在10和200之外也可以回访成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--回访记录内容200字以上，回访失败");
        }
    }

    @Test(description = "销售--回访任务--下次回访日期在今日之前，回访失败")
    public void returnVisit_system_15() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        String preBuyCarTime = DateTimeUtil.dateToStamp(DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String common = "壬戌之秋，七月既望，苏子与客泛舟游于赤壁之下。";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10);
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(common, "", "", false,
                    date, "", preBuyCarTime, EnumReturnVisitResult.CONTACT_LATER.getType(), String.valueOf(taskId), picture);
            Preconditions.checkArgument(result.getString("message").equals("下次回访日期不允许在今日之前"), "下次回访日期在今日之前也可以回访成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--下次回访日期为今日之前，回访失败");
        }
    }

    @Test(description = "销售--回访任务排序，一级：未完成在上，已完成在下")
    public void returnVisit_system_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = crm.returnVisitTaskPage(1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            int x = 0;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("task_status_name").equals("未完成")) {
                        x++;
                    }
                }
            }
            CommonUtil.valueView(x);
            JSONArray list = crm.returnVisitTaskPage(1, x).getJSONArray("list");
            CommonUtil.valueView(list.size());
            for (int j = 0; j < list.size(); j++) {
                String taskStatusName = list.getJSONObject(j).getString("task_status_name");
                int taskId = list.getJSONObject(j).getInteger("task_id");
                CommonUtil.valueView("任务：" + "[" + j + "]" + taskId + "的完成状态为：" + taskStatusName);
                Preconditions.checkArgument(taskStatusName.equals("未完成"), "未完成中包含" + taskStatusName + "任务id为：" + taskId);
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务排序，一级：未完成在上，已完成在下");
        }
    }

    @Test(description = "销售--回访任务，联系电话为11位数字、非空")
    public void returnVisit_system_18() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10);
            int s = CommonUtil.getTurningPage(response.getInteger("total"), 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    String customerPhone = list.getJSONObject(j).getString("customer_phone");
                    Preconditions.checkArgument(!CommonUtil.isContainChinese(customerPhone), "我的回访存在非电话号" + customerPhone);
                    Preconditions.checkArgument(!StringUtils.isEmpty(customerPhone), "我的回访存在空电话号码");
                    Preconditions.checkArgument(customerPhone.length() == 11, "我的回访存在非11位电话号，电话号为：" + customerPhone);
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务，联系电话为11位数字、非空");
        }
    }

    @Test(description = "销售--回访任务--列表客户的所属销售都是当前登陆账号")
    public void returnVisit_system_19() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(xs);
            int total = crm.returnVisitTaskPage(1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    String belongsSaleName = list.getJSONObject(j).getString("belongs_sale_name");
                    CommonUtil.valueView(belongsSaleName);
                    Preconditions.checkArgument(belongsSaleName.equals(xs.getUsername()), "我的回访列表，存在非当前登录账号的回访任务。所属销售为：" + belongsSaleName);
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--列表客户的所属销售都是当前登陆账号");
        }
    }

    @Test(description = "销售--回访任务--列表项包括:所属销售、客户等级、客户名称、联系电话、意向车系、回访类型、是否完成")
    public void returnVisit_system_20() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.returnVisitTaskPage(1, 10).getJSONArray("list");
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
            saveData("销售--回访任务--列表项包括:所属销售、客户等级、客户名称、联系电话、意向车系、回访类型、是否完成");
        }
    }

    @Test(description = "销售--回访任务--创建接待时“订车”标记为否的客户回访类型为潜客，展厅客户回访任务类型为潜客")
    public void returnVisit_system_21() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = crm.returnVisitTaskPage(1, 10).getInteger("total");
            for (int j = 1; j < CommonUtil.getTurningPage(total, 100); j++) {
                JSONArray list = crm.returnVisitTaskPage(j, 100).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("customer_type_name").equals(EnumCustomerType.PROSPECTIVE_CUSTOMER.getName())) {
                        String customerPhone = list.getJSONObject(i).getString("customer_phone");
                        if (StringUtils.isEmpty(customerPhone)) {
                            continue;
                        }
                        CommonUtil.valueView("电话号是：" + customerPhone);
                        JSONArray list1 = crm.customerList("", customerPhone, "", "", "", 1, 10).getJSONArray("list");
                        if (list1.size() == 0) {
                            JSONArray list2 = crm.dccList("", customerPhone, "", "", 1, 10).getJSONArray("list");
                            Preconditions.checkArgument(list2.size() != 0, "潜客手机号：" + customerPhone + "在展厅客户/dcc客户列表都查询不到");
                            CommonUtil.logger("dcc客户");
                        } else {
                            boolean isOrder = list1.getJSONObject(0).getBoolean("is_order");
                            CommonUtil.valueView(isOrder);
                            Preconditions.checkArgument(!isOrder, "回访类型:潜客，创建接待时不是“订车”标记为否的客户，电话号为：" + customerPhone);
                            CommonUtil.logger("展厅客户");
                        }
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--创建接待时“订车”标记为否的客户回访类型为潜客，展厅客户回访任务类型为潜客");
        }
    }

    @Test(description = "销售--回访任务--创建接待时“订车”标记为是的客户回访类型为成交")
    public void returnVisit_system_22() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(zjl);
            int total = crm.returnVisitTaskPage(1, 10).getInteger("total");
            for (int j = 1; j < CommonUtil.getTurningPage(total, 100); j++) {
                JSONArray list = crm.returnVisitTaskPage(j, 100).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("customer_type_name").equals(EnumCustomerType.CHANGE_HANDS.getName())) {
                        String customerPhone = list.getJSONObject(i).getString("customer_phone");
                        if (StringUtils.isEmpty(customerPhone)) {
                            continue;
                        }
                        CommonUtil.valueView("电话号是：" + customerPhone);
                        JSONObject result = crm.customerList("", customerPhone, "", "", "", 1, 10);
                        boolean isOrder = result.getJSONArray("list").getJSONObject(0).getBoolean("is_order");
                        CommonUtil.valueView(isOrder);
                        Preconditions.checkArgument(isOrder, "回访类型:成交，创建接待时不是“订车”标记为是的客户，电话号为：" + customerPhone);
                        CommonUtil.log("分割线");
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售--回访任务--创建接待时“订车”标记为是的客户回访类型为成交");
        }
    }

    @Test(description = "销售--回访任务--预约记录中存在的客户回访类型为预约")
    public void returnVisit_system_23() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.returnVisitTaskPage(1, 100).getJSONArray("list");
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
            saveData("销售--回访任务--预约记录中存在的客户回访类型为预约");
        }
    }

    /**
     * @description: 前台销售排班
     */
    @Test(description = "销售排班")
    public void frontDesk_system_1() {
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

    /**
     * 获取非重复电话号
     *
     * @return phone
     */
    private String getDistinctPhone() {
        UserUtil.login(zjl);
        String phone = "153" + CommonUtil.getRandom(8);
        int a = crm.customerList("", phone, "", "", "", 1, 10).getInteger("total");
        int b = crm.dccList("", phone, "", "", 1, 10).getInteger("total");
        return a == 0 && b == 0 ? phone : getDistinctPhone();
    }
}
