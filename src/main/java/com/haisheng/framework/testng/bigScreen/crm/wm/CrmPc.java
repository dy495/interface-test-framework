package com.haisheng.framework.testng.bigScreen.crm.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.experiment.enumerator.*;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

public class CrmPc extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();

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
        commonConfig.dingHook = EnumDingTalkWebHook.QA_TEST_GRP.getWebHook();
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
        CommonUtil.login(EnumAccount.ZJL);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

//    ---------------------------------------------------2.0------------------------------------------------------------

    @Test(description = "pc端我的客户总数=列表的总数")
    public void salesCustomerManagement_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject object = crm.customerList("", "", "", "", "", 1, 100);
            //客户总数
            int total = CommonUtil.getIntField(object, "total");
            int pageSize = CommonUtil.pageTurning(total, 100);
            int listSizeTotal = 0;
            for (int i = 1; i < pageSize; i++) {
                int listSize = crm.customerList("", "", "", "", "", i, 100).getJSONArray("list").size();
                listSizeTotal += listSize;
            }
            CommonUtil.valueView(total, pageSize, listSizeTotal);
            Preconditions.checkArgument(listSizeTotal == total, "pc端我的客户总数!=列表的总数");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端我的客户总数=列表的总数");
        }
    }

    @Test(description = "今日人数=按今日搜索展示列表条数")
    public void salesCustomerManagement_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            JSONObject object = crm.customerList("", "", "", date, date, 1, 10);
            int total = CommonUtil.getIntField(object, "total");
            int pageSize = CommonUtil.pageTurning(total, 100);
            int listSizeTotal = 0;
            for (int i = 1; i < pageSize; i++) {
                int listSize = crm.customerList("", "", "", date, date, i, 100).getJSONArray("list").size();
                listSizeTotal += listSize;
            }
            CommonUtil.valueView(total, pageSize, listSizeTotal);
            Preconditions.checkArgument(listSizeTotal == total, "pc端今日客戶人数!=按今日搜索展示列表条数");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日人数=按今日搜索展示列表条数");
        }
    }

    @Test(enabled = false)
    public void salesCustomerManagement_3() {
        logger.logCaseStart(caseResult.getCaseName());
        JSONObject response = crm.customerList("", "", "", "", "", 1, 10);
        JSONObject list = response.getJSONArray("list").getJSONObject(0);
        CommonUtil.valueView(list);
        List<String> array = CommonUtil.getMoreParam(list, "customer_id", "customer_name", "customer_phone", "belongs_sale_id");
        System.out.println(array);
    }


    @Test(description = "pc销售客户管理公海共计人数=列表总条数")
    public void salesCustomerManagement_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.publicCustomerList("", "", 2 << 10, 1);
            int total = CommonUtil.getIntField(response, "total");
            int listSize = response.getJSONArray("list").size();
            CommonUtil.valueView(total, listSize);
            Preconditions.checkArgument(total == listSize, "pc销售客户管理公海共计人数=列表总数");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc销售客户管理公海共计人数=列表总条数");
        }
    }

    @Test
    public void salesCustomerManagement_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            JSONObject response = crm.publicCustomerList(date, date, 2 << 10, 1);
            int today = CommonUtil.getIntField(response, "today");
            int listSize = response.getJSONArray("list").size();
            CommonUtil.valueView(today, listSize);
            Preconditions.checkArgument(today == listSize, "pc销售客户管理公海共计人数=列表总数");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc销售客户管理公海共计人数=列表总条数");
        }
    }

    @Test()
    public void salesCustomerManagement_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(EnumAccount.XSGWTEMP);
            int customerTotal = crm.customerPage(100, 1, "", "", "").getInteger("total");
            //公海人数总量
            CommonUtil.login(EnumAccount.ZJL);
            JSONObject publicCustomerList = crm.publicCustomerList("", "", 10, 1);
            String phone = CommonUtil.getStrField(publicCustomerList, 0, "customer_phone");
            int customerId = CommonUtil.getIntField(publicCustomerList, 0, "customer_id");
            int publicTotal = publicCustomerList.getInteger("total");
            //把公海分配销售
            crm.customerAllot(EnumAccount.XSGWTEMP.getUid(), customerId);
            //分配之后公海总数
            JSONObject publicCustomerList1 = crm.publicCustomerList("", "", 10, 1);
            int publicTotal1 = publicCustomerList1.getInteger("total");
            //等级变为c
            JSONObject result = crm.customerList("", phone, "", "", "", 1, 10);
            String customerLevelName = CommonUtil.getStrField(result, 0, "customer_level_name");
            //分配之后后销售名下客户数量
            CommonUtil.login(EnumAccount.XSGWTEMP);
            int customerTotal1 = crm.customerPage(100, 1, "", "", "").getInteger("total");
            CommonUtil.valueView(customerTotal, customerTotal1, publicTotal, publicTotal1);
            Preconditions.checkArgument(customerTotal1 == customerTotal + 1, "公海客户分配给xsgwtemp后，该销售名下客户数量未+1");
            Preconditions.checkArgument(publicTotal1 == publicTotal - 1, "公海客户分配给xsgwtemp后，公海客户数量未-1");
            Preconditions.checkArgument(customerLevelName.equals("C"), "客户从公海分配销售后，等级未变为C");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端勾选公海客户分配给销售A，销售A客户名下客户数量+1，列表数-1");
        }
    }

    @Test
    public void salesCustomerManagement_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //变换所属销售前公海数量
            CommonUtil.login(EnumAccount.ZJL);
            JSONObject publicCustomerList = crm.publicCustomerList("", "", 10, 1);
            int publicTotal = publicCustomerList.getInteger("total");
            //获取一名客户信息
            JSONObject customerList = crm.customerList("", "", "", "", "", 1, 10);
            int customerId = 0;
            String customerName = null;
            String customerPhone = null;
            JSONArray list = customerList.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (!list.getJSONObject(i).getString("customer_level_name").equals("G")
                        && !list.getJSONObject(i).getString("belongs_sale_id").equals(EnumAccount.XSGWTEMP.getUid())) {
                    customerId = list.getJSONObject(i).getInteger("customer_id");
                    customerName = list.getJSONObject(i).getString("customer_name");
                    customerPhone = list.getJSONObject(i).getString("customer_phone");
                }
            }
            //变换所属销售
            crm.customerEdit((long) customerId, customerName, customerPhone, EnumCustomerLevel.G.getCustomerLevel(), EnumAccount.XSGWTEMP.getUid());
            //变换所属销售后公海列表数
            JSONObject publicCustomerList1 = crm.publicCustomerList("", "", 10, 1);
            int publicTotal1 = publicCustomerList1.getInteger("total");
            CommonUtil.valueView(publicTotal, publicTotal1);
            Preconditions.checkArgument(publicTotal1 == publicTotal + 1, "原公海数量为" + publicTotal + "把一个客户等级改为公海后，公海数量为" + publicTotal1);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端将一个已存在客户的客户等级设置为G,公海列表数+1");
        }
    }

    @Test
    public void salesCustomerManagement_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject failureCustomerList = crm.failureCustomerList("", "", 1, 2 << 10);
            int listSize = failureCustomerList.getJSONArray("list").size();
            int total = failureCustomerList.getInteger("total");
            CommonUtil.valueView(listSize, total);
            Preconditions.checkArgument(listSize == total, "pc销售客户管理战败共计人数为" + total + "列表总条数为" + listSize);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc销售客户管理战败共计人数=列表总条数");
        }
    }

    @Test
    public void salesCustomerManagement_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            JSONObject failureCustomerList = crm.failureCustomerList(date, date, 1, 2 << 10);
            int listSize = failureCustomerList.getJSONArray("list").size();
            int today = failureCustomerList.getInteger("today");
            Preconditions.checkArgument(listSize == today, "pc销售客户管理战败今日人数为" + today + "按今日搜索列表总条数为" + listSize);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc战败客户，今日人数=按今日搜索展示列表条数");
        }
    }

    @Test
    public void salesCustomerManagement_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //变换所属销售前战败数量
            int publicTotal = crm.failureCustomerList("", "", 1, 10).getInteger("total");
            //获取一名客户信息
            JSONObject customerList = crm.customerList("", "", "", "", "", 1, 10);
            int customerId = 0;
            String customerName = null;
            String customerPhone = null;
            JSONArray list = customerList.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (!list.getJSONObject(i).getString("customer_level_name").equals("G")
                        && !list.getJSONObject(i).getString("belongs_sale_id").equals(EnumAccount.XSGWTEMP.getUid())) {
                    customerId = list.getJSONObject(i).getInteger("customer_id");
                    customerName = list.getJSONObject(i).getString("customer_name");
                    customerPhone = list.getJSONObject(i).getString("customer_phone");
                }
            }
            //变换所属销售
            crm.customerEdit((long) customerId, customerName, customerPhone, EnumCustomerLevel.F.getCustomerLevel(), EnumAccount.XSGWTEMP.getUid());
            //变换所属销售后战败列表数
            int publicTotal1 = crm.failureCustomerList("", "", 1, 10).getInteger("total");
            CommonUtil.valueView(publicTotal, publicTotal1);
            Preconditions.checkArgument(publicTotal1 == publicTotal + 1, "原战败数量为" + publicTotal + "把一个客户等级改为战败后，公海数量为" + publicTotal1);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端将一个已存在客户的客户等级设置为F,战败列表数+1");
        }
    }

    @Test
    public void salesCustomerManagement_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //战败列表数
            JSONObject failureCustomerList = crm.failureCustomerList("", "", 1, 10);
            int customerId = CommonUtil.getIntField(failureCustomerList, 0, "customer_id");
            int failureTotal = failureCustomerList.getInteger("total");
            //公海列表数
            int publicTotal = crm.publicCustomerList("", "", 10, 1).getInteger("total");
            //战败转公海
            crm.failureCustomerToPublic(customerId);
            int failureTotal1 = crm.failureCustomerList("", "", 1, 10).getInteger("total");
            int publicTotal1 = crm.publicCustomerList("", "", 10, 1).getInteger("total");
            CommonUtil.valueView(publicTotal, publicTotal1, failureTotal, failureTotal1, customerId);
            Preconditions.checkArgument(failureTotal == failureTotal1 + 1, "战败转移公海前战败数量为" + failureTotal + "战败转公海后战败数量为" + failureTotal1);
            Preconditions.checkArgument(publicTotal == publicTotal1 - 1, "战败转移公海前公海数量为" + failureTotal + "战败转公海后公海数量为" + failureTotal1);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("将一个战败客户划入公海,战败客户数量-1，公海客户数量+1");
        }
    }

    @Test
    public void salesCustomerManagement_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject weChatCustomerList = crm.wechatCustomerList("", "", 1, 2 << 10);
            int listSize = weChatCustomerList.getJSONArray("list").size();
            int total = weChatCustomerList.getInteger("total");
            CommonUtil.valueView(listSize, total);
            Preconditions.checkArgument(listSize == total, "小程序客户总数为：" + total + "列表总数为：" + listSize);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc销售客户管理小程序共计人数=列表总数");
        }
    }

    @Test
    public void salesCustomerManagement_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            JSONObject weChatCustomerList = crm.wechatCustomerList(date, date, 1, 2 << 10);
            int listSize = weChatCustomerList.getJSONArray("list").size();
            int today = weChatCustomerList.getInteger("today");
            CommonUtil.valueView(listSize, today);
            Preconditions.checkArgument(listSize == today, "小程序今日客户总数为：" + today + "按今日搜索列表总数为：" + listSize);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc销售客户管理小程序今日人数=按今日搜索展示列表条数");
        }
    }

    @Test
    public void salesCustomerManagement_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.loginApplet(EnumAppletCode.WM);
            JSONObject response = crm.appointmentList(0L, EnumAppointmentType.TEST_DRIVE.getType(), 100);
            int id = CommonUtil.getIntField(response, 0, "id");
            String phone = crm.appointmentInfo((long) id).getString("phone");
            int testDriverTotal = response.getInteger("total");
            int maintainTotal = crm.appointmentList(0L, EnumAppointmentType.MAINTAIN.getType(), 100).getInteger("total");
            int repairTotal = crm.appointmentList(0L, EnumAppointmentType.REPAIR.getType(), 100).getInteger("total");
            CommonUtil.login(EnumAccount.ZJL);
            JSONArray list = crm.wechatCustomerList("", "", 1, 2 << 10).getJSONArray("list");
            int appointmentTestDriver = 0;
            int appointmentMend = 0;
            int appointmentMaintain = 0;
            String appointment = null;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("wechat_id").equals(EnumAppletCode.WM.getWeChatId())) {
                    appointmentTestDriver = list.getJSONObject(i).getInteger("appointment_test_driver");
                    appointmentMaintain = list.getJSONObject(i).getInteger("appointment_maintain");
                    appointmentMend = list.getJSONObject(i).getInteger("appointment_mend");
                    appointment = list.getJSONObject(i).getString("appointment");
                }
            }
            CommonUtil.valueView(testDriverTotal, maintainTotal, repairTotal, phone, appointmentTestDriver, appointmentMaintain, appointmentMend, appointment);
            Preconditions.checkArgument(testDriverTotal == appointmentTestDriver, "pc端预约试驾次数为：" + testDriverTotal + "小程序我的试驾预约总数：" + appointmentTestDriver);
            Preconditions.checkArgument(maintainTotal == appointmentMaintain, "pc端预约保养次数为：" + maintainTotal + "小程序我的保养预约总数：" + appointmentMaintain);
            Preconditions.checkArgument(repairTotal == appointmentMend, "pc端预约维修次数为：" + repairTotal + "小程序我的维修预约总数：" + appointmentMend);
            Preconditions.checkArgument(phone.equals(appointment), "最新预约手机号与pc端小程序预约电话不一致");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端小程序后面的预约试驾=小程序“我的”预约试驾条数,预约保养=小程序“我的”预约保养条数,预约维修=小程序“我的”预约维修条数");
        }
    }

    @Test
    public void salesCustomerManagement_15() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            crm.publicCustomerList(date, date1, 1, 100);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("开始时间<=结束时间,筛选出日期内划入公海得客户列表");
        }
    }

    @Test(description = "交车后，客户不回到公海")
    public void salesCustomerManagement_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int remainDays = 1;
            JSONArray list = crm.customerPage(1, 2 << 10, "", "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("customer_level_name").equals("D级")) {
                    Integer day = list.getJSONObject(i).getInteger("remain_days");
                    if (day == null) {
                        remainDays = 0;
                    }
                }
            }
            CommonUtil.valueView(remainDays);
            Preconditions.checkArgument(remainDays == 0, "交车后，客户回到公海天数不等于0");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("交车后，客户不回到公海");
        }
    }

    @Test(description = "小程序筛选")
    public void salesCustomerManagement_17() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            crm.wechatCustomerList(date, date1, 1, 10);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端小程序客户按照日期筛选-开始时间<=结束时间");
        }
    }

    @Test(description = "战败筛选")
    public void salesCustomerManagement_18() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            crm.failureCustomerList(date, date1, 1, 10);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端战败客户按照日期筛选-开始时间<=结束时间");
        }
    }

    @Test(description = "战败筛选")
    public void salesCustomerManagement_19() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            crm.failureCustomerList(date1, date, 1, 10);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端战败客户按照日期筛选-开始时间>结束时间,接口不限制，前端限制时间");
        }
    }

    @Test(description = "新建一个G级客户")
    public void customerRelations_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String phone = "13333333333";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        try {
            //公海客户数量
            CommonUtil.login(EnumAccount.ZJL);
            int publicTotal = crm.publicCustomerList("", "", 10, 1).getInteger("total");
            JSONObject response = crm.createLine("【自动化】王先生", 6, phone, 8, remark);
            if (response.getString("message").equals("手机号码重复")) {
                //删除客户
                deleteCustomer(phone);
                //再创建线索
                crm.createLine("【自动化】王先生", 6, phone, 8, remark);
            }
            CommonUtil.login(EnumAccount.ZJL);
            int publicTotal1 = crm.publicCustomerList("", "", 10, 1).getInteger("total");
            CommonUtil.valueView(publicTotal, publicTotal1);
            Preconditions.checkArgument(publicTotal1 == publicTotal + 1, "新建一个G级客户，公海数未+1");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            deleteCustomer(phone);
            saveData("新建一个G级客户,公海列表+1");
        }
    }

    @Test()
    public void marketingBoards_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String path = "src/main/java/com/haisheng/framework/model/experiment/multimedia/goodsmanager/";
        String bigPic = new ImageUtil().getImageBinary(path + "大图照片.jpg");
        String interiorPic = new ImageUtil().getImageBinary(path + "内饰照片.jpg");
        String spacePic = new ImageUtil().getImageBinary(path + "空间照片.jpg");
        String appearancePic = new ImageUtil().getImageBinary(path + "外观照片.jpg");
        String carPic = new ImageUtil().getImageBinary(path + "车辆照片.jpg");
        String carTypeName = "凯迪拉克CT6";
        String carIntroduce = "无介绍";
        try {
            String message = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", carIntroduce, "", carTypeName, 10, interiorPic, 20, spacePic).getString("message");
            String message1 = crm.goodsManagerAddCar("", bigPic, "无优惠", carIntroduce, carPic, carTypeName, 10, interiorPic, 20, spacePic).getString("message");
            String message2 = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", carIntroduce, carPic, carTypeName, 10, "", 20, spacePic).getString("message");
            String message3 = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", carIntroduce, carPic, carTypeName, 10, interiorPic, 20, "").getString("message");
            String message4 = crm.goodsManagerAddCar(appearancePic, "", "无优惠", carIntroduce, carPic, carTypeName, 10, interiorPic, 20, "").getString("message");
            String message5 = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", carIntroduce, carPic, carTypeName, -1, interiorPic, 20, spacePic).getString("message");
            String message6 = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", carIntroduce, carPic, carTypeName, 10, interiorPic, -1, spacePic).getString("message");
            String message7 = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", "", carPic, carTypeName, 20, interiorPic, 10, spacePic).getString("message");
            String message8 = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", carIntroduce, carPic, "", 20, interiorPic, 10, spacePic).getString("message");
            String message9 = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", carIntroduce, carPic, carTypeName, 10, interiorPic, 200, spacePic).getString("message");
            String message10 = crm.goodsManagerAddCar(appearancePic, bigPic, "", carIntroduce, carPic, carTypeName, 20, interiorPic, 10, spacePic).getString("message");
            Preconditions.checkArgument(message.equals("车辆图片不能为空"), "pc商品管理，车辆图片为空也可创建成功");
            Preconditions.checkArgument(message1.equals("车辆外观图片不能为空"), "pc商品管理，外观照片为空也可创建成功");
            Preconditions.checkArgument(message2.equals("车辆内饰图片不能为空"), "pc商品管理，内饰照片为空也可创建成功");
            Preconditions.checkArgument(message3.equals("车辆空间图片不能为空"), "pc商品管理，空间照片为空也可创建成功");
            Preconditions.checkArgument(message4.equals("车辆大图不能为空"), "pc商品管理，大图照片为空也可创建成功");
            Preconditions.checkArgument(message5.equals("车辆最高价格不能为空"), "pc商品管理，最高价格为空也可创建成功");
            Preconditions.checkArgument(message6.equals("车辆最低价格不能为空"), "pc商品管理，最低价格为空也可创建成功");
            Preconditions.checkArgument(message7.equals("车辆介绍不能为空"), "pc商品管理，车辆介绍为空也可创建成功");
            Preconditions.checkArgument(message8.equals("车辆类型名称不能为空"), "pc商品管理，车辆最低价格>最高价格也可创建成功");
            Preconditions.checkArgument(message9.equals("车辆最低价格不能高于车辆最高价格"), "所有必填项全正确填写，车型创建失败");
            Preconditions.checkArgument(message10.equals("成功"), "所有必填项全正确填写，车型创建失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            int id = 0;
            JSONObject result = crm.carList();
            int size = result.getJSONArray("list").size() - 1;
            String carName = CommonUtil.getStrField(result, size, "car_type_name");
            if (carName.equals(carTypeName)) {
                id = CommonUtil.getIntField(result, size, "id");
            }
            crm.carDelete(id);
            saveData("商品管理中，各必须参数不填写创建车型&&全部填写创建车型");
        }
    }

//    ---------------------------------------------------3.0------------------------------------------------------------

    @Test
    public void stationMessage_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            boolean flag = false;
            int total = crm.messagePage(1, 10).getInteger("total");
            for (int i = 1; i < CommonUtil.pageTurning(total, 100); i++) {
                JSONArray list = crm.messagePage(1, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("customer_types").contains("销售/售后")
                            || list.getJSONObject(j).getString("customer_types").contains("销售")
                            || list.getJSONObject(j).getString("customer_types").contains("售后")) {
                        flag = true;
                    }
                }
            }
            CommonUtil.valueView(flag);
            Preconditions.checkArgument(flag, "站内消息投放人群不正确");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息可投放人群为售前、售后、售前/售后");
        }
    }

    @Test
    public void stationMessage_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.messagePage(1, 10);
            String date = CommonUtil.getStrField(response, 0, "send_time");
            boolean result = CommonUtil.isLegalDate(date, "yyyy-MM-dd HH:mm");
            CommonUtil.valueView(date, result);
            Preconditions.checkArgument(result, "站内消息生效日期格式非yyyy-MM-dd hh:mm");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息生效日期格式：yyyy-MM-dd hh：mm");
        }
    }

    @Test
    public void stationMessage_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = 0;
            String title = null;
            int total = crm.messagePage(1, 10).getInteger("total");
            for (int i = 1; i < CommonUtil.pageTurning(total, 100); i++) {
                JSONArray list = crm.messagePage(1, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("status_name").equals("排期中")) {
                        id = list.getJSONObject(i).getInteger("id");
                        title = list.getJSONObject(i).getString("title");
                        break;
                    }
                }
            }
            JSONObject result = crm.messageDetail(id);
            String title1 = result.getString("title");
            assert title != null;
            CommonUtil.valueView(title, title1);
            Preconditions.checkArgument(title.equals(title1), "排期中的站内消息可以查看操作");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("排期中的消息可以查看操作，接口不做限制，前端限制");
        }
    }

    @Test()
    public void stationMessage_4() {
        logger.logCaseStart(caseResult.getCaseName());
        int messageId = 0;
        String title = "自动化站内消息-待删";
        String content = "自动化";
        Date date = DateTimeUtil.addDay(new Date(), 1);
        String sendDate = DateTimeUtil.getFormat(date, "yyyy-MM-dd HH:mm");
        try {
            crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES", "AFTER_SALES");
            messageId = crm.messagePage(1, 10).getJSONArray("list").getJSONObject(0).getInteger("id");
            String content1 = crm.messageDetail(messageId).getString("content");
            String title1 = crm.messageDetail(messageId).getString("title");
            CommonUtil.valueView(title, title1, content, content1);
            Preconditions.checkArgument(content.equals(content1), "新创建的文章未排列在第一位");
            Preconditions.checkArgument(title.equals(title1), "新创建的文章未排列在第一位");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            //删除消息
            crm.messageDelete(messageId);
            saveData("站内消息倒叙排列-最新的在最上方&&排期中的站内消息可以删除");
        }
    }

    @Test()
    public void stationMessage_5() {
        logger.logCaseStart(caseResult.getCaseName());
        int messageId = 0;
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        CommonUtil.valueView(sendDate);
        try {
            crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES", "AFTER_SALES");
            JSONArray list = crm.messagePage(1, 100).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("status_name").equals("发送成功")) {
                    messageId = list.getJSONObject(i).getInteger("id");
                }
            }
            //删除消息
            crm.messageDelete(messageId);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("发送成功的站内消息可以删除");
        }
    }

    @Test()
    public void stationMessage_6() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        try {
            boolean result1 = false;
            boolean result2 = false;
            crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES");
            sleep(70);
            //登陆小程序-售前可见消息
            CommonUtil.loginApplet(EnumAppletCode.WM);
            JSONArray list = crm.messageList(20, "MSG").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("title").equals(title)
                        && list.getJSONObject(i).getString("date").equals(sendDate)
                        && !list.getJSONObject(i).getBoolean("is_read")) {
                    result1 = true;
                    break;
                }
            }
            //登陆小程序-售后不可见消息
            CommonUtil.loginApplet(EnumAppletCode.XMF);
            JSONArray list1 = crm.messageList(20, "MSG").getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                if (list1.getJSONObject(i).getString("title").equals(title)
                        && list1.getJSONObject(i).getString("date").equals(sendDate)
                        && !list1.getJSONObject(i).getBoolean("is_read")) {
                    result2 = true;
                    break;
                }
            }
            CommonUtil.valueView(result1, result2);
            Preconditions.checkArgument(result1, "小程序销售客户看不见消息");
            Preconditions.checkArgument(!result2, "小程序售后客户能看见消息");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息配置人群为销售，小程序销售可见消息，售后不可见消息");
        }
    }

    @Test
    public void stationMessage_7() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        try {
            boolean result1 = false;
            boolean result2 = false;
            crm.messageAdd("", "", "", sendDate, title, content, "", "", "AFTER_SALES");
            sleep(70);
            //登陆小程序-售前可见消息
            CommonUtil.loginApplet(EnumAppletCode.WM);
            JSONArray list = crm.messageList(20, "MSG").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("title").equals(title)
                        && list.getJSONObject(i).getString("date").equals(sendDate)
                        && !list.getJSONObject(i).getBoolean("is_read")) {
                    result1 = true;
                    break;
                }
            }
            //登陆小程序-售后不可见消息
            CommonUtil.loginApplet(EnumAppletCode.XMF);
            JSONArray list1 = crm.messageList(20, "MSG").getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                if (list1.getJSONObject(i).getString("title").equals(title)
                        && list1.getJSONObject(i).getString("date").equals(sendDate)
                        && !list1.getJSONObject(i).getBoolean("is_read")) {
                    result2 = true;
                    break;
                }
            }
            CommonUtil.valueView(result1, result2);
            Preconditions.checkArgument(!result1, "小程序销售客户能看见消息");
            Preconditions.checkArgument(result2, "小程序售后客户不能看见消息");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息配置人群为销售，小程序销售不可见消息，售后可见消息");
        }
    }

    @Test
    public void stationMessage_8() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        try {
            boolean result1 = false;
            boolean result2 = false;
            crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES", "AFTER_SALES");
            sleep(70);
            //登陆小程序-售前可见消息
            CommonUtil.loginApplet(EnumAppletCode.WM);
            JSONArray list = crm.messageList(20, "MSG").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("title").equals(title)
                        && list.getJSONObject(i).getString("date").equals(sendDate)
                        && !list.getJSONObject(i).getBoolean("is_read")) {
                    result1 = true;
                    break;
                }
            }
            //登陆小程序-售后不可见消息
            CommonUtil.loginApplet(EnumAppletCode.XMF);
            JSONArray list1 = crm.messageList(20, "MSG").getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                if (list1.getJSONObject(i).getString("title").equals(title)
                        && list1.getJSONObject(i).getString("date").equals(sendDate)
                        && !list1.getJSONObject(i).getBoolean("is_read")) {
                    result2 = true;
                    break;
                }
            }
            CommonUtil.valueView(result1, result2);
            Preconditions.checkArgument(result1, "小程序销售客户能看见消息");
            Preconditions.checkArgument(result2, "小程序售后客户不能看见消息");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息配置人群为销售/售后，小程序销售可见消息，售后可见消息");
        }
    }

    @Test
    public void stationMessage_9() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "Chinese&&English is No.1 in use!";
        String content = "aba aba 123456!@#$%^&*,待删除";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        try {
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES", "AFTER_SALES");
            int id = Integer.parseInt(response.getString("id"));
            Preconditions.checkArgument(id != 0, "创建站内消息失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息内容可包括中英文，符号，数字，空格");
        }
    }

    @Test
    public void stationMessage_10() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        String appointmentType = EnumAppointmentType.TEST_DRIVE.getType();
        try {
            crm.messageAdd("", "", "", sendDate, title, content, appointmentType, "", "PRE_SALES", "AFTER_SALES");
            sleep(80);
            CommonUtil.loginApplet(EnumAppletCode.WM);
            JSONArray list = crm.messageList(20, "MSG").getJSONArray("list");
            int id = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("date").equals(sendDate)) {
                    id = list.getJSONObject(i).getInteger("id");
                }
            }

            String appletAppointmentType = crm.messageDetail((long) id).getString("appointment_type");
            CommonUtil.valueView(appletAppointmentType);
            Preconditions.checkArgument(appointmentType.equals(appletAppointmentType), "pc端发送的站内消息，小程序接收到以后没有预约试驾按钮");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc创建预约试驾的站内消息，小程序显示按钮,小程序可跳转填写试驾信息页");
        }
    }

    @Test
    public void stationMessage_11() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        String appointmentType = EnumAppointmentType.REPAIR.getType();
        try {
            crm.messageAdd("", "", "", sendDate, title, content, appointmentType, "", "PRE_SALES", "AFTER_SALES");
            sleep(80);
            CommonUtil.loginApplet(EnumAppletCode.WM);
            JSONArray list = crm.messageList(20, "MSG").getJSONArray("list");
            int id = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("date").equals(sendDate)) {
                    id = list.getJSONObject(i).getInteger("id");
                }
            }
            String appletAppointmentType = crm.messageDetail((long) id).getString("appointment_type");
            CommonUtil.valueView(appletAppointmentType);
            Preconditions.checkArgument(appointmentType.equals(appletAppointmentType), "pc端发送的站内消息，小程序接收到以后没有预约维修按钮");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc创建预约维修的站内消息，小程序显示按钮,小程序可跳转填写维修信息页");
        }
    }

    @Test
    public void stationMessage_12() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        String appointmentType = EnumAppointmentType.MAINTAIN.getType();
        try {
            crm.messageAdd("", "", "", sendDate, title, content, appointmentType, "", "PRE_SALES", "AFTER_SALES");
            sleep(80);
            CommonUtil.loginApplet(EnumAppletCode.WM);
            JSONArray list = crm.messageList(20, "MSG").getJSONArray("list");
            int id = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("date").equals(sendDate)) {
                    id = list.getJSONObject(i).getInteger("id");
                }
            }
            String appletAppointmentType = crm.messageDetail((long) id).getString("appointment_type");
            CommonUtil.valueView(appletAppointmentType);
            Preconditions.checkArgument(appointmentType.equals(appletAppointmentType), "pc端发送的站内消息，小程序接收到以后没有预约保养按钮");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc创建预约保养的站内消息，小程序显示按钮,小程序可跳转填写保养信息页");
        }
    }

//    ---------------------------------------------------私有方法区-------------------------------------------------------

    /**
     * 删除客户
     *
     * @param phone 电话号
     */
    private void deleteCustomer(String phone) {
        CommonUtil.login(EnumAccount.ZJL);
        JSONObject response = crm.customerList("", phone, "", "", "", 1, 10);
        int customerId = CommonUtil.getIntField(response, 0, "customer_id");
        crm.customerDelete(customerId);
    }
}
