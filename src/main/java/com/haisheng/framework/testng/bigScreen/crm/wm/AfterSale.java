package com.haisheng.framework.testng.bigScreen.crm.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletCode;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppointmentType;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCustomerInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.other.EnumOperation;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.exception.DataException;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc.OrderMaintainPageScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc.OrderRepairPageScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
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
import java.util.*;

/**
 * app售后
 */
public class AfterSale extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    PublicMethod method = new PublicMethod();
    private static final EnumAccount zjl = EnumAccount.ZJL_DAILY;
    private static final EnumAccount fw = EnumAccount.FW_BAOYANG_DAILY;
    private static final int size = 100;
    int zjl_num = 0;
    int gw_num = 0;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.produce = EnumProduce.BSJ.name();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getName() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_DAILY.getShopId();
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

    @Test(description = "售后--我的接待--本月接待售后车辆>=今日接待售后车辆&&本月完成维修车辆>=今日完成维修车辆")
    public void afterSale_reception_data_1() {
        try {
            JSONObject response = crm.receptionAfterCustomerList("", "", "", 1, size);
            int monthReceptionCar = response.getInteger("month_reception_car");
            int monthRepairedCar = response.getInteger("month_repaired_car");
            int todayReceptionCar = response.getInteger("today_reception_car");
            int todayRepairedCar = response.getInteger("today_repaired_car");
            CommonUtil.valueView(monthReceptionCar, monthRepairedCar, todayReceptionCar, todayRepairedCar);
            Preconditions.checkArgument(monthReceptionCar >= todayReceptionCar && monthRepairedCar >= todayRepairedCar, "本月接待售后车辆>=今日接待售后车辆&&本月完成维修车辆>=今日完成维修车辆异常");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--本月接待售后车辆>=今日接待售后车辆&&本月完成维修车辆>=今日完成维修车辆");
        }
    }

    @Test(description = "售后--我的接待--今日接待售后车辆=今天筛选，列表数车牌去重")
    public void afterSale_reception_data_2() {
        try {
            String date = DateTimeUtil.getFormat(new Date());
            IScene scene = ReceptionAfterCustomerListScene.builder().searchDateStart(date).searchDateEnd(date).build();
            JSONObject response = crm.invokeApi(scene);
            int total = response.getInteger("total");
            int todayReceptionCar = response.getInteger("today_reception_car");
            Set<String> set = new HashSet<>();
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                IScene scene1 = ReceptionAfterCustomerListScene.builder().page(i).size(size).searchDateStart(date).searchDateEnd(date).build();
                JSONArray list = crm.invokeApi(scene1).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    set.add(list.getJSONObject(j).getString("plate_number"));
                }
            }
            CommonUtil.valueView(todayReceptionCar, set.size());
            Preconditions.checkArgument(todayReceptionCar == set.size(), zjl.getUsername() + "今日接待售后车辆数：" + todayReceptionCar + "列表数：" + set.size());
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--今日接待售后车辆=今天筛选，列表数车牌去重");
        }
    }

    @Test(description = "售后--我的接待--接待日期为今天的记录，确认交车，今日完成维修车辆+1&&本月完成维修车辆+1", enabled = false)
    public void afterSale_reception_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ReceptionAfterCustomerListScene.builder().build();
            JSONObject response = crm.invokeApi(scene);
            int monthRepairedCar = response.getInteger("month_repaired_car");
            int todayRepairedCar = response.getInteger("today_repaired_car");
            int maintainNum = method.getStatusNum("维修中");
            int completeNum = method.getStatusNum("已完成");
            CommonUtil.valueView(monthRepairedCar, todayRepairedCar, maintainNum, completeNum);
            //获取记录id
            int recordId = method.getAfterRecordId(false, 19);
            //完成接待
            method.completeReception(String.valueOf(recordId));
            //提车
            crm.invokeApi(SendPickUpNewsScene.builder().afterRecordId(String.valueOf(recordId)).build());
            //交车
            crm.invokeApi(ConfirmCarScene.builder().afterRecordId(String.valueOf(recordId)).build());
            JSONObject response1 = crm.invokeApi(scene);
            int monthRepairedCar1 = response1.getInteger("month_repaired_car");
            int todayRepairedCar1 = response1.getInteger("today_repaired_car");
            int maintainNum1 = method.getStatusNum("维修中");
            int completeNum1 = method.getStatusNum("已完成");
            CommonUtil.valueView(monthRepairedCar, todayRepairedCar, maintainNum1, completeNum1);
            Preconditions.checkArgument(monthRepairedCar1 - monthRepairedCar == 1, "");
            Preconditions.checkArgument(todayRepairedCar1 - todayRepairedCar == 1, "");
            Preconditions.checkArgument(maintainNum - maintainNum1 == 1, "");
            Preconditions.checkArgument(completeNum1 - completeNum == 1, "");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--接待日期为今天的记录，确认交车，今日完成维修车辆+1&&本月完成维修车辆+1");
        }
    }

    @Test(description = "售后--我的接待--接待日期为今天的记录，确认交车，列表总条数不变，接待状态=已完成+1&&接待状态=维修中-1")
    public void afterSale_reception_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int maintainNum = method.getStatusNum("维修中");
            int completeNum = method.getStatusNum("已完成");
            int listSize = maintainNum + completeNum;
            CommonUtil.valueView(maintainNum, completeNum);
            int afterRecordId = method.getAfterRecordId(true, "维修中", 30);
            if (afterRecordId != 0) {
                //提车
                crm.invokeApi(SendPickUpNewsScene.builder().afterRecordId(String.valueOf(afterRecordId)).build());
                //交车
                crm.invokeApi(ConfirmCarScene.builder().afterRecordId(String.valueOf(afterRecordId)).build());
                int maintainNum1 = method.getStatusNum("维修中");
                int completeNum1 = method.getStatusNum("已完成");
                int listSize1 = maintainNum1 + completeNum1;
                CommonUtil.valueView(maintainNum1, completeNum1);
                Preconditions.checkArgument(maintainNum1 == maintainNum - 1, "交车前维修中数量为：" + maintainNum + "交车后维修中数量为：" + maintainNum1);
                Preconditions.checkArgument(completeNum1 == completeNum + 1, "交车前已完成数量为：" + completeNum + "交车后已完成数量为：" + completeNum1);
                Preconditions.checkArgument(listSize == listSize1, "交车前列表数量为：" + listSize + "交车后列表数数量为：" + listSize1);
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--接待日期为今天的记录，确认交车，列表总条数不变，接待状态=已完成+1&&接待状态=维修中-1");
        }
    }

    @Test(description = "售后--我的接待--接待日期为昨天的记录，确认交车，今日完成维修车辆+1&&本月完成维修车辆+1", enabled = false)
    public void afterSale_reception_data_5() {
        //todo
    }

    @Test(description = "售后--我的接待--app展示信息与小程序预约时信息保持一致", enabled = false)
    public void afterSale_reception_data_6() {
        //todo
    }

    @Test(description = "售后--我的接待--app预约保养列表数与pc预约保养列表数一致，app预约维修列表数与pc预约维修列表数一致")
    public void afterSale_reception_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = OrderMaintainPageScene.builder().build();
            int pcMaintainTotal = crm.invokeApi(scene).getInteger("total");
            IScene scene1 = OrderRepairPageScene.builder().build();
            int pcRepairTotal = crm.invokeApi(scene1).getInteger("total");
            int appMaintainTotal = crm.mainAppointmentList(1, size).getInteger("total");
            int appRepairTotal = crm.repairAppointmentlist().getInteger("total");
            CommonUtil.valueView(pcMaintainTotal, appMaintainTotal, pcRepairTotal, appRepairTotal);
            Preconditions.checkArgument(pcMaintainTotal == appMaintainTotal, "app预约保养总数为：" + appMaintainTotal + "pc预约保养总数为：" + pcMaintainTotal);
            Preconditions.checkArgument(pcRepairTotal == appRepairTotal, "app预约维修总数为" + appRepairTotal + "pc预约维修总数为：" + pcRepairTotal);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--app预约保养列表数与pc预约保养列表数一致，app预约维修列表数与pc预约维修列表数一致");
        }
    }

    @Test(description = "售后--我的接待--预约保养/维修页点击接待按钮（客户类型=新客）--接待页列表+1", enabled = false)
    public void afterSale_reception_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ReceptionAfterCustomerListScene.builder().build();
            int total = crm.invokeApi(scene).getInteger("total");
            String date = DateTimeUtil.addDayFormat(new Date(), +1);
            method.appointment(EnumAppointmentType.MAINTAIN, date);
            UserUtil.login(zjl);
            JSONArray list = crm.mainAppointmentList().getJSONArray("list");
            int appointmentId = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("service_status_name").equals("预约中")) {
                    appointmentId = list.getJSONObject(i).getInteger("appointment_id");
                }
            }
            crm.reception_customer((long) appointmentId);
            int total1 = crm.invokeApi(scene).getInteger("total");
            CommonUtil.valueView(total, total1);
            Preconditions.checkArgument(total1 == total + 1, "");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--预约保养/维修页点击接待按钮（客户类型=新客）--接待页列表+1");
        }
    }

    @Test(description = "售后--我的接待--总经理本月接待售后车辆>=各个顾问本月x接待售后车辆之和")
    public void afterSale_reception_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyReception("month_reception_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理本月接待售后车辆为   " + zjl_num + "各个顾问本月接待售后车辆之和  " + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--总经理本月接待售后车辆>=各个顾问本月接待售后车辆之和");
        }
    }

    @Test(description = "售后--我的接待--总经理本月完成维修车辆>=各个顾问本月完成维修车辆之和")
    public void afterSale_reception_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyReception("month_repaired_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理本月完成维修车辆为   " + zjl_num + "各个顾问本月完成维修车辆之和  " + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--总经理本月完成维修车辆>=各个顾问本月完成维修车辆之和");
        }
    }

    @Test(description = "售后-我的接待--总经理今日接待售后车辆>=各个顾问今日接待售后车辆之和")
    public void afterSale_reception_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyReception("today_reception_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理今日接待售后车辆   " + zjl_num + "各个顾问今日接待售后车辆之和  " + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后-我的接待--总经理今日接待售后车辆>=各个顾问今日接待售后车辆之和");
        }
    }

    @Test(description = "售后--我的接待--总经理今日完成维修车辆>=各个顾问今日完成维修车辆之和")
    public void afterSale_reception_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyReception("today_repaired_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理今日完成维修车辆   " + zjl_num + "各个顾问今日完成维修车辆之和  " + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--总经理今日完成维修车辆>=各个顾问今日完成维修车辆之和");
        }
    }

    private void compareAfterSaleMyReception(String type) {
        UserUtil.login(zjl);
        int x = 0;
        List<Map<String, String>> list = method.getSaleList("服务顾问");
        for (Map<String, String> stringStringMap : list) {
            CommonUtil.valueView(stringStringMap.get("userName"));
            if (stringStringMap.get("userName").contains("总经理")) {
                crm.login(stringStringMap.get("account"), zjl.getPassword());
                IScene scene = ReceptionAfterCustomerListScene.builder().build();
                zjl_num = crm.invokeApi(scene).getInteger(type);
            }
            if (!stringStringMap.get("userName").contains("总经理")) {
                crm.login(stringStringMap.get("account"), zjl.getPassword());
                IScene scene = ReceptionAfterCustomerListScene.builder().build();
                int num = crm.invokeApi(scene).getInteger(type);
                CommonUtil.valueView(num);
                x += num;
            }
            gw_num = x;
            CommonUtil.log("分割线");
        }
    }

    @Test(description = "售后--我的接待--增加20条备注，展示20条")
    public void afterSale_reception_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray remarks = new JSONArray();
            String str = EnumCustomerInfo.CUSTOMER_1.getRemark();
            for (int i = 0; i < 22; i++) {
                remarks.add(str);
            }
            int afterRecordId = method.getAfterRecordId(false, 30);
            if (afterRecordId != 0) {
                method.saveReception(String.valueOf(afterRecordId), remarks);
                IScene scene = DetailAfterSaleCustomerScene.builder().afterRecordId(String.valueOf(afterRecordId)).build();
                JSONObject response = crm.invokeApi(scene);
                JSONArray array = response.getJSONArray("remarks");
                String platNumber = response.getString("plate_number");
                Preconditions.checkArgument(array.size() == 20, "车牌号为：" + platNumber + "的客户，备注列表展示条数为：" + array.size());
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
        } finally {
            saveData("售后--我的接待--增加20条备注，展示20条");
        }
    }

    @Test(description = "售后--客户管理--全部车辆>=本月新增车辆")
    public void afterSale_customer_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AfterSaleCustomerListScene.builder().build();
            JSONObject response = crm.invokeApi(scene);
            int monthReceptionCar = response.getInteger("month_reception_car");
            int totalReceptionCar = response.getInteger("total_reception_car");
            CommonUtil.valueView(monthReceptionCar, totalReceptionCar);
            Preconditions.checkArgument(totalReceptionCar >= monthReceptionCar, "全部车辆为：" + totalReceptionCar + "本月新增车辆为：" + monthReceptionCar);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--客户管理--全部车辆>=本月新增车辆");
        }
    }

    @Test(description = "售后--客户管理--本月新增车辆>=今日新增车辆")
    public void afterSale_customer_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AfterSaleCustomerListScene.builder().build();
            JSONObject response = crm.invokeApi(scene);
            int monthReceptionCar = response.getInteger("month_reception_car");
            int todayNewCar = response.getInteger("today_new_car");
            CommonUtil.valueView(monthReceptionCar, todayNewCar);
            Preconditions.checkArgument(todayNewCar <= monthReceptionCar, "今日新增车辆为：" + todayNewCar + "本月新增车辆为：" + monthReceptionCar);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--客户管理--本月新增车辆>=今日新增车辆");
        }
    }

    @Test(description = "售后-客户管理-本月新增车辆<=【我的接待】本月接待售后车辆")
    public void afterSale_customer_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ReceptionAfterCustomerListScene.builder().build();
            int monthReceptionCar = crm.invokeApi(scene).getInteger("month_reception_car");
            IScene scene1 = AfterSaleCustomerListScene.builder().build();
            int monthReceptionCar1 = crm.invokeApi(scene1).getInteger("month_reception_car");
            CommonUtil.valueView(monthReceptionCar, monthReceptionCar1);
            Preconditions.checkArgument(monthReceptionCar >= monthReceptionCar1, "本月新增车辆：" + monthReceptionCar1 + "本月接待售后车辆：" + monthReceptionCar);
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后-客户管理-本月新增车辆<=【我的接待】本月接待售后车辆");
        }
    }

    @Test(description = "售后-客户管理-今日新增车辆<=【我的接待】今日接待售后车辆")
    public void afterSale_customer_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ReceptionAfterCustomerListScene.builder().build();
            int todayReceptionCar = crm.invokeApi(scene).getInteger("today_reception_car");
            IScene scene1 = AfterSaleCustomerListScene.builder().build();
            int todayNewCar = crm.invokeApi(scene1).getInteger("today_new_car");
            CommonUtil.valueView(todayNewCar, todayReceptionCar);
            Preconditions.checkArgument(todayReceptionCar >= todayNewCar, "今日新增车辆：" + todayNewCar + "今日接待售后车辆：" + todayReceptionCar);
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后-客户管理-今日新增车辆<=【我的接待】今日接待售后车辆");
        }
    }

    @Test(description = "售后--客户管理--总经理的全部车辆>=各个顾问的全部车辆之和", enabled = false)
    public void afterSale_customer_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyCustomer("total_reception_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理的全部车辆为：" + zjl_num + "各顾问数量和为：" + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--客户管理--总经理的全部车辆=各个顾问的全部车辆之和");
        }
    }

    @Test(description = "售后--客户管理--今日接待售后车辆>=今日新增售后车辆")
    public void afterSale_customer_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(fw);
            JSONObject list = crm.afterSale_custTotal();
            //统计数据-今日接待售后数量
            int todayTotal = list.getInteger("today_reception_total");
            //统计数据-今日新增售后数量
            int todayNew = list.getInteger("today_new_after_sale_customer");
            CommonUtil.valueView(todayTotal, todayNew);
            Preconditions.checkArgument(todayTotal >= todayNew, "今日接待售后数量" + todayTotal + "<今日新增售后数量" + todayNew);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--客户管理--今日接待售后车辆>=今日新增售后车辆");
        }
    }

    @Test(description = "售后--客户管理--总经理的本月新增>=各个顾问的本月新增之和", enabled = false)
    public void afterSale_customer_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyCustomer("month_reception_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理的本月新增车辆为：" + zjl_num + "各顾问数量和为：" + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--客户管理--总经理的本月新增>=各个顾问的本月新增之和");
        }
    }

    @Test(description = "售后--客户管理--总经理的今日新增车辆>=各个顾问的今日新增之和")
    public void afterSale_customer_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyCustomer("today_new_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理的今日新增车辆为：" + zjl_num + "各顾问数量和为：" + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--客户管理--总经理的今日新增车辆>=各个顾问的今日新增之和");
        }
    }

    private void compareAfterSaleMyCustomer(String type) {
        int x = 0;
        List<Map<String, String>> list = method.getSaleList("服务顾问");
        for (Map<String, String> map : list) {
            CommonUtil.valueView(map.get("userName"));
            if (map.get("userName").contains("总经理")) {
                crm.login(map.get("account"), zjl.getPassword());
                IScene scene = AfterSaleCustomerListScene.builder().build();
                zjl_num = crm.invokeApi(scene).getInteger(type);
            }
            if (!map.get("userName").contains("总经理")) {
                crm.login(map.get("account"), zjl.getPassword());
                IScene scene = AfterSaleCustomerListScene.builder().build();
                int num = crm.invokeApi(scene).getInteger(type);
                CommonUtil.valueView(x);
                x += num;
            }
            gw_num = x;
            CommonUtil.log("分割线");
        }
    }

    @Test(description = "售后--客户管理--全部车辆>=今日接待售后车辆")
    public void afterSale_customer_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(fw);
            JSONObject list = crm.receptionAfterCustomerList("", "", "", 1, size);
            //统计数据-今日接待售后数量
            int todayTotal = list.getInteger("today_reception_car");
            //统计数据-全部车辆
            JSONObject list1 = crm.afterSaleCustomerList("", "", "", 1, size);
            int all = list1.getInteger("total_reception_car");
            Preconditions.checkArgument(all >= todayTotal, "全部车辆" + all + "<今日接待售后数量" + todayTotal);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--客户管理--全部车辆>=今日接待售后车辆");

        }
    }

    @Test(description = "售后--回访任务--全部回访=列表条数")
    public void afterSale_returnVisit_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = crm.returnVisitRecordAfterSalePage(1, size, "").getInteger("total");
            int listSize = 0;
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitRecordAfterSalePage(i, size, "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    listSize++;
                }
            }
            CommonUtil.valueView(total, listSize);
            Preconditions.checkArgument(total == listSize, "售后--我的回访-全部回访=列表条数");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--回访任务--全部回访=列表条数");
        }
    }

    @Test(description = "售后--回访任务--今日回访=任务日期为今天的条数")
    public void afterSale_returnVisit_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.returnVisitRecordAfterSalePage(1, size, "");
            int todayReturnVisitNumber = response.getInteger("today_return_visit_number");
            int listSize = response.getJSONArray("list").size();
            CommonUtil.valueView(todayReturnVisitNumber, listSize);
            Preconditions.checkArgument(todayReturnVisitNumber == listSize, "服务--今日回访!=任务日期为今天的条数");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--回访任务--今日回访=任务日期为今天的条数");
        }
    }

    @Test(description = "售后--回访任务--全部回访>=今日回访")
    public void afterSale_returnVisit_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.returnVisitRecordAfterSalePage(1, size, "");
            int todayReturnVisitNumber = response.getInteger("today_return_visit_number");
            int total = response.getInteger("total");
            Preconditions.checkArgument(total >= todayReturnVisitNumber, "服务--全部回访<今日回访");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--回访任务--全部回访>=今日回访");
        }
    }

    @Test(description = "售后--回访任务--回访任务日期为今天的回访任务，是否完成=已完成")
    public void afterSale_returnVisit_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String returnVisitStatusName = null;
            //回访当天任务
            int id = createReturnVisitTask("售后回访");
            if (id != 0) {
                int total = crm.returnVisitRecordAfterSalePage(1, size, "").getInteger("total");
                int s = CommonUtil.getTurningPage(total, size);
                for (int i = 1; i < s; i++) {
                    JSONArray list = crm.returnVisitRecordAfterSalePage(i, size, "").getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        if (list.getJSONObject(j).getInteger("id") == id) {
                            returnVisitStatusName = list.getJSONObject(j).getString("return_visit_status_name");
                        }
                    }
                }
                CommonUtil.valueView(returnVisitStatusName);
                Preconditions.checkArgument(returnVisitStatusName != null && returnVisitStatusName.equals("已完成"), "回访任务日期为今天的回访任务，是否完成!=已完成");
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("服务--回访任务日期为今天的回访任务，是否完成=已完成");
        }
    }

    @Test(description = "售后--回访任务--小程序预约今日的保养/维修（不取消）,售后回访页条数+1,售后回访页 全部回访+1,今日回访+1", priority = 1, enabled = false)
    public void afterSale_returnVisit_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String customerName = "门徒";
        String customerPhoneNumber = "15321527989";
        try {
            JSONObject response = crm.returnVisitRecordAfterSalePage(1, size, "");
            int total = response.getInteger("total");
            int todayReturnVisitNumber = response.getInteger("today_return_visit_number");
            int s = CommonUtil.getTurningPage(total, size);
            int listSize = 0;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitRecordAfterSalePage(i, size, "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    listSize++;
                }
            }
            CommonUtil.valueView(total, todayReturnVisitNumber, listSize);
            //预约保养
            int id = getTimeId(date);
            crm.appointmentMaintain((long) getCarId(), customerName, customerPhoneNumber, date, "", (long) id);
            UserUtil.login(zjl);
            JSONObject response1 = crm.returnVisitRecordAfterSalePage(1, size, "");
            int total1 = response1.getInteger("total");
            int todayReturnVisitNumber1 = response1.getInteger("today_return_visit_number");
            int s1 = CommonUtil.getTurningPage(total, size);
            int listSize1 = 0;
            for (int i = 1; i < s1; i++) {
                JSONArray list = crm.returnVisitRecordAfterSalePage(i, size, "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    listSize1++;
                }
            }
            CommonUtil.valueView(total1, todayReturnVisitNumber1, listSize1);
            Preconditions.checkArgument(total + 1 == total1, "售后回访页 全部回访未+1");
            Preconditions.checkArgument(todayReturnVisitNumber + 1 == todayReturnVisitNumber1, "今日回访未+1");
            Preconditions.checkArgument(listSize + 1 == listSize1, "售后回访页条数未+1");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--回访任务--小程序预约今日的保养/维修（不取消）,售后回访页条数+1,售后回访页 全部回访+1,今日回访+1");
        }
    }

    @Test(description = "售后--回访任务--小程序预约今日的保养/维修（取消）,售后回访页条数-1,售后回访页 全部回访-1,今日回访-1", priority = 2, enabled = false)
    public void afterSale_returnVisit_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(zjl);
            JSONObject response = crm.returnVisitRecordAfterSalePage(1, size, "");
            int total = response.getInteger("total");
            int todayReturnVisitNumber = response.getInteger("today_return_visit_number");
            int s = CommonUtil.getTurningPage(total, size);
            int listSize = 0;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitRecordAfterSalePage(i, size, "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    listSize++;
                }
            }
            CommonUtil.valueView(total, todayReturnVisitNumber, listSize);
            //取消试驾
            UserUtil.loginApplet(EnumAppletCode.WM_SMALL);
            int id = crm.appointmentList(0L, EnumAppointmentType.MAINTAIN.getType(), 20).getJSONArray("list").getJSONObject(0).getInteger("id");
            crm.appointmentCancel(id);
            UserUtil.login(zjl);
            JSONObject response1 = crm.returnVisitRecordAfterSalePage(1, size, "");
            int total1 = response1.getInteger("total");
            int todayReturnVisitNumber1 = response1.getInteger("today_return_visit_number");
            int s1 = CommonUtil.getTurningPage(total, size);
            int listSize1 = 0;
            for (int i = 1; i < s1; i++) {
                JSONArray list = crm.returnVisitRecordAfterSalePage(i, size, "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    listSize1++;
                }
            }
            CommonUtil.valueView(total1, todayReturnVisitNumber1, listSize1);
            Preconditions.checkArgument(total == total1 + 1, "售后回访页 全部回访未-1");
            Preconditions.checkArgument(todayReturnVisitNumber == todayReturnVisitNumber1 + 1, "今日回访未-1");
            Preconditions.checkArgument(listSize == listSize1 + 1, "售后回访页条数未-1");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--回访任务--小程序预约今日的保养/维修（取消）,售后回访页条数-1,售后回访页 全部回访-1,今日回访-1");
        }
    }

    @Test(description = "售后--任务管理-全部预约保养>=今日预约保养")
    public void afterSale_appointment_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = crm.mainAppointmentDriverNum();
            int total = obj.getInteger("appointment_total_number");
            int today = obj.getInteger("appointment_today_number");
            CommonUtil.valueView(total, today);
            Preconditions.checkArgument(total >= today, "全部预约保养：" + total + "今日预约保养：" + today);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--任务管理-全部预约保养>=今日预约保养");
        }
    }

    @Test(description = "售后--任务管理-全部预约保养<=列表数")
    public void afterSale_appointment_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = crm.mainAppointmentDriverNum();
            int total = obj.getInteger("appointment_total_number");
            int list = crm.mainAppointmentList().getInteger("total");
            Preconditions.checkArgument(total <= list, "全部预约保养" + total + ">列表数" + list);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--任务管理-全部预约保养<=列表数");
        }
    }

    @Test(description = "售后--任务管理--首保提醒--全部回访=列表条数&&今日回访=任务日期为今天的条数&&全部回访>=今日回访")
    public void afterSale_appointment_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(zjl);
            //1.全部回访=列表条数
            JSONObject obj = crm.firstMaintainRecordPage(1, size, "");
            Integer total = obj.getInteger("total");//全部回访条数
            int pages = obj.getInteger("pages");
            int listTotal = 0;//列表的条数
            for (int page = 1; page <= pages; page++) {
                JSONArray list1 = crm.firstMaintainRecordPage(page, size, "").getJSONArray("list");
                for (int j = 0; j < list1.size(); j++) {
                    Integer id = list1.getJSONObject(j).getInteger("id");
                    if (id != null) {
                        listTotal++;
                    }
                }
            }
            //2.今日回访=任务日期为今天的条数
            JSONObject obj1 = crm.firstMaintainRecordPage(1, size, "");
            Integer todayViNum = obj1.getInteger("today_return_visit_number");//获取今日回访条数
            int pages1 = obj.getInteger("pages");
            int todayListTotal = 0;
            for (int i = 1; i <= pages1; i++) {
                JSONArray todayList = crm.firstMaintainRecordPage(i, size, "").getJSONArray("list");
                for (int k = 0; k < todayList.size(); k++) {
                    Integer id = todayList.getJSONObject(k).getInteger("id");
                    if (id != null) {
                        todayListTotal++;
                    }
                }
            }
            //3.全部回访>=今日回访
            boolean flag = false;
            if (total >= todayListTotal) {
                flag = true;
            }
            CommonUtil.valueView(total, listTotal, todayViNum, todayListTotal);
            Preconditions.checkArgument(total == listTotal, "售后工作管理中我的回访-首保提醒中的全部回访" + total + "不等于售后后工作管理中我的回访-首保提醒中的列表条数" + listTotal);
            Preconditions.checkArgument(todayViNum == todayListTotal, "售后工作管理中我的回访-售后回访中的今日回访" + todayViNum + "不等于后工作管理中我的回访-售后回访中任务日期为今天的条数" + todayListTotal);
            Preconditions.checkArgument(flag, "全部回访" + total + "大于今日回访" + todayListTotal);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--任务管理--首保提醒--全部回访=列表条数&&今日回访=任务日期为今天的条数&&全部回访>=今日回访");
        }
    }

    @Test(description = "售后--任务管理--首保提醒--按照今天日期进行搜索，结果条数=今日回访数")
    public void afterSale_appointment_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            JSONObject response = crm.firstMaintainRecordPage(1, size, "");
            int total = response.getInteger("total");
            int todayReturnVisitNumber = response.getInteger("today_return_visit_number");
            CommonUtil.valueView(total, todayReturnVisitNumber);
            Preconditions.checkArgument(total == todayReturnVisitNumber, "按" + date + "搜索回访条数为：" + total + "今日回访数为：" + todayReturnVisitNumber);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--任务管理--首保提醒--全部回访=列表条数&&今日回访=任务日期为今天的条数&&全部回访>=今日回访");
        }
    }

    @Test(description = "售后--任务管理--首保提醒--回访任务日期为今天的回访任务，是否完成=已完成")
    public void afterSale_appointment_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String returnVisitStatusName = null;
            //回访当天任务
            int id = createReturnVisitTask("首保提醒");
            if (id != 0) {
                int total = crm.firstMaintainRecordPage(1, size, "").getInteger("total");
                int s = CommonUtil.getTurningPage(total, size);
                for (int i = 1; i < s; i++) {
                    JSONArray list = crm.firstMaintainRecordPage(i, size, "").getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        if (list.getJSONObject(j).getInteger("id") == id) {
                            returnVisitStatusName = list.getJSONObject(j).getString("return_visit_status_name");
                        }
                    }
                }
                CommonUtil.valueView(returnVisitStatusName);
                Preconditions.checkArgument(returnVisitStatusName != null && returnVisitStatusName.equals("已完成"), "任务日期为今天的回访任务，是否完成!=已完成");
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--任务管理--首保提醒--回访任务日期为今天的回访任务，是否完成=已完成");
        }
    }

    @Test(description = "售后--任务管理--流失预警--全部回访=列表条数&&今日回访=任务日期为今天的条数&&全部回访>=今日回访")
    public void afterSale_appointment_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //1.全部回访=列表条数
            JSONObject obj = crm.customerChurnWarningPage(1, size, "");
            Integer total = obj.getInteger("total");//全部回访条数
            int pages = obj.getInteger("pages");
            int listTotal = 0;//列表的条数
            for (int page = 1; page <= pages; page++) {
                JSONArray list1 = crm.customerChurnWarningPage(1, size, "").getJSONArray("list");
                for (int j = 0; j < list1.size(); j++) {
                    Integer id = list1.getJSONObject(j).getInteger("id");
                    if (id != null) {
                        listTotal++;
                    }
                }
            }
            //2.今日回访=任务日期为今天的条数
            JSONObject obj1 = crm.customerChurnWarningPage(1, size, "");
            Integer todayViNum = obj1.getInteger("today_return_visit_number");//获取今日回访条数
            int pages1 = obj.getInteger("pages");
            int todayListTotal = 0;
            for (int i = 1; i <= pages1; i++) {
                JSONArray todayList = crm.customerChurnWarningPage(1, size, "").getJSONArray("list");
                for (int k = 0; k < todayList.size(); k++) {
                    Integer id = todayList.getJSONObject(k).getInteger("id");
                    if (id != null) {
                        todayListTotal++;
                    }
                }
            }
            //3.全部回访>=今日回访
            boolean flag = false;
            if (total >= todayListTotal) {
                flag = true;
            }
            CommonUtil.valueView(total, listTotal, todayViNum, todayListTotal);
            Preconditions.checkArgument(total == listTotal, "售后工作管理中我的回访-流失预警中的全部回访" + total + "不等于售后工作管理中我的回访-流失预警中的列表条数" + listTotal);
            Preconditions.checkArgument(todayViNum == todayListTotal, "售后工作管理中我的回访-流失预警中的今日回访" + todayViNum + "不等于售后工作管理中我的回访-流失预警中任务日期为今天的条数" + todayListTotal);
            Preconditions.checkArgument(flag, "全部回访" + total + "大于今日回访" + todayListTotal);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--任务管理--流失预警--全部回访=列表条数&&今日回访=任务日期为今天的条数&&全部回访>=今日回访");
        }
    }

    @Test(description = "售后--任务管理--流失预警--按照今天日期进行搜索，结果条数=今日回访数")
    public void afterSale_appointment_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            JSONObject response = crm.customerChurnWarningPage(1, size, "");
            int total = response.getInteger("total");
            int todayReturnVisitNumber = response.getInteger("today_return_visit_number");
            CommonUtil.valueView(total, todayReturnVisitNumber);
            Preconditions.checkArgument(total == todayReturnVisitNumber, "按" + date + "搜索回访条数为：" + total + "今日回访数为：" + todayReturnVisitNumber);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--任务管理--流失预警--按照今天日期进行搜索，结果条数=今日回访数");
        }
    }

    @Test(description = "售后--任务管理--流失预警--回访任务日期为今天的回访任务，是否完成=已完成")
    public void afterSale_appointment_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String returnVisitStatusName = null;
            //回访当天任务
            int id = createReturnVisitTask("流失预警");
            if (id != 0) {
                int total = crm.customerChurnWarningPage(1, size, "").getInteger("total");
                int s = CommonUtil.getTurningPage(total, size);
                for (int i = 1; i < s; i++) {
                    JSONArray list = crm.customerChurnWarningPage(i, size, "").getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        if (list.getJSONObject(j).getInteger("id") == id) {
                            returnVisitStatusName = list.getJSONObject(j).getString("return_visit_status_name");
                        }
                    }
                }
                CommonUtil.valueView(returnVisitStatusName);
                Preconditions.checkArgument(returnVisitStatusName != null && returnVisitStatusName.equals("已完成"), "任务日期为今天的回访任务，是否完成!=已完成");
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--任务管理--流失预警--回访任务日期为今天的回访任务，是否完成=已完成");
        }
    }

    @Test(description = "售后--活动任务--添加1个报名,报名条数+1&&删除1个报名，报名条数-1")
    public void afterSale_activity_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        int activityId;
        String phone = method.getDistinctPhone();
        try {
            activityId = getActivityId();
            int a = crm.activityTaskInfo(String.valueOf(activityId)).getJSONArray("customer_list").size();
            crm.registeredCustomer1(activityId, "哈哈哈", phone).getString("message");
            int b = crm.activityTaskInfo(String.valueOf(activityId)).getJSONArray("customer_list").size();
            deleteActivityCustomer(activityId, phone);
            int c = crm.activityTaskInfo(String.valueOf(activityId)).getJSONArray("customer_list").size();
            CommonUtil.valueView(a, b, c);
            Preconditions.checkArgument(b == a + 1, "报名活动成功，客户列表未+1");
            Preconditions.checkArgument(c == b - 1, "删除活动报名客户，客户列表未-1");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--活动任务--添加1个报名,报名条数+1");
        }
    }

    @Test(description = "售后--活动任务--可填写报名的活动当前时间<活动结束时间")
    public void afterSale_activity_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int activityId = getActivityId();
            String activityEnd = crm.activityTaskInfo(String.valueOf(activityId)).getString("activity_end");
            String date = DateTimeUtil.getFormat(new Date());
            int i = new DateTimeUtil().calTimeDayDiff(date, activityEnd);
            CommonUtil.valueView(i);
            Preconditions.checkArgument(i > 0, "可填写报名的活动当前时间>活动结束时间");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--活动任务--可填写报名的活动当前时间<活动结束时间");
        }
    }

    @Test(description = "售后--活动任务--活动信息与运营中心发布文章时信息一致")
    public void afterSale_activity_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int activityId = getActivityId();
            JSONObject response = crm.activityTaskInfo(String.valueOf(activityId));
            String articleTitle = response.getString("article_title");
            String articleContent = response.getString("article_content");
            int id = 0;
            for (EnumOperation e : EnumOperation.values()) {
                JSONArray list = crm.articlePage(1, size, e.name()).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("article_title").equals(articleTitle)) {
                        id = list.getJSONObject(i).getInteger("id");
                    }
                }
            }
            JSONObject object = crm.artilceView(id);
            Preconditions.checkArgument(articleContent.equals(object.getString("article_content")), articleTitle + " 活动内容不一致");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("售后--活动任务--活动信息与运营中心发布文章时信息一致");
        }
    }

    @Test(description = "售后--活动任务--添加1个报名,后台任务客户+1")
    public void afterSale_activity_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String phone = method.getDistinctPhone();
            int activityId = getActivityId();
            JSONObject response = crm.activityTaskInfo(String.valueOf(activityId));
            String activityTitle = response.getString("article_title");
            int id = 0;
            JSONArray list = crm.activityShowList().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("name").equals(activityTitle)) {
                    id = list.getJSONObject(i).getInteger("id");
                }
            }
            JSONArray a = crm.customerTaskPage(size, 1, id).getJSONArray("list");
            crm.registeredCustomer1(activityId, "哈哈哈", phone).getString("message");
            JSONArray b = crm.customerTaskPage(size, 1, id).getJSONArray("list");
            deleteActivityCustomer(activityId, phone);
            JSONArray c = crm.customerTaskPage(size, 1, id).getJSONArray("list");
            CommonUtil.valueView(a.size(), b.size(), c.size());
            Preconditions.checkArgument(b.size() == a.size() + 1, "添加1个报名,后台任务客户未+1");
            Preconditions.checkArgument(c.size() == b.size() - 1, "删除1个报名,后台任务客户未-1");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--活动任务--添加1个报名,后台任务客户+1");
        }

    }

    @Test(description = "售后--我的接待--车牌号模糊搜索")
    public void afterSale_reception_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(zjl);
            IScene scene = ReceptionAfterCustomerListScene.builder().build();
            JSONArray list = crm.invokeApi(scene).getJSONArray("list");
            String plateNumber = list.getJSONObject(0).getString("plate_number");
            CommonUtil.valueView(plateNumber);
            String findParam = plateNumber.substring(0, 3);
            CommonUtil.valueView(findParam);
            IScene scene1 = ReceptionAfterCustomerListScene.builder().searchCondition(findParam).build();
            int total = crm.invokeApi(scene1).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                IScene scene2 = ReceptionAfterCustomerListScene.builder().searchCondition(findParam).page(i).size(size).build();
                JSONArray list1 = crm.invokeApi(scene2).getJSONArray("list");
                for (int j = 0; j < list1.size(); j++) {
                    String resultPlateNumber = list1.getJSONObject(j).getString("plate_number");
                    Preconditions.checkArgument(resultPlateNumber.contains(findParam), "按照车牌号查询失败,搜索参数为：" + findParam);
                }
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--车牌号模糊搜索");
        }
    }

    @Test(description = "售后--我的接待--联系方式模糊搜索")
    public void afterSale_reception_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(zjl);
            ReceptionAfterCustomerListScene.ReceptionAfterCustomerListSceneBuilder builder = ReceptionAfterCustomerListScene.builder();
            int total = crm.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            String customerPhoneNumber = null;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (!StringUtils.isEmpty(list.getJSONObject(j).getString("customer_phone_number"))) {
                        customerPhoneNumber = list.getJSONObject(j).getString("customer_phone_number");
                        CommonUtil.valueView(customerPhoneNumber);
                        break;
                    }
                }
            }
            if (customerPhoneNumber != null) {
                String findParam = customerPhoneNumber.substring(0, 5);
                CommonUtil.valueView(findParam);
                builder.searchCondition(findParam).build();
                int x = CommonUtil.getTurningPage(crm.invokeApi(builder.build()).getInteger("total"), size);
                for (int i = 1; i < x; i++) {
                    builder.searchCondition(findParam).page(i).size(size);
                    JSONArray list1 = crm.invokeApi(builder.build()).getJSONArray("list");
                    for (int j = 0; j < list1.size(); j++) {
                        String resultPlateNumber = list1.getJSONObject(j).getString("customer_phone_number");
                        CommonUtil.valueView(resultPlateNumber);
                        Preconditions.checkArgument(resultPlateNumber.contains(findParam), "按电话号查询失败,搜索参数为：" + findParam);
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("售后--我的接待--联系方式模糊搜索");
        }
    }

    @Test(description = "售后--我的接待--按照接待日期查询")
    public void afterSale_reception_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String endDate = DateTimeUtil.getFormat(new Date());
        String startDate = DateTimeUtil.addDayFormat(new Date(), -30);
        try {
            UserUtil.login(zjl);
            IScene scene = ReceptionAfterCustomerListScene.builder().searchDateStart(startDate).searchDateEnd(endDate).build();
            int total = crm.invokeApi(scene).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                IScene scene1 = ReceptionAfterCustomerListScene.builder().searchDateStart(startDate).searchDateEnd(endDate).page(i).size(size).build();
                JSONArray list = crm.invokeApi(scene1).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    String serviceDate = list.getJSONObject(j).getString("service_date");
                    CommonUtil.valueView(startDate, serviceDate, endDate);
                    Preconditions.checkArgument(serviceDate.compareTo(endDate) <= 0, "");
                    Preconditions.checkArgument(serviceDate.compareTo(startDate) >= 0, "");
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--按照接待日期查询");
        }
    }

    @Test(description = "售后--我的接待--按照接待日期查询")
    public void afterSale_reception_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        String endDate = DateTimeUtil.getFormat(new Date());
        String startDate = DateTimeUtil.addDayFormat(new Date(), -30);
        try {
            UserUtil.login(zjl);
            IScene scene = ReceptionAfterCustomerListScene.builder().searchDateStart(startDate).searchDateEnd(endDate).build();
            int total = crm.invokeApi(scene).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                IScene scene1 = ReceptionAfterCustomerListScene.builder().searchDateStart(startDate).searchDateEnd(endDate).page(i).size(size).build();
                JSONArray list = crm.invokeApi(scene1).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    String serviceDate = list.getJSONObject(j).getString("service_date");
                    CommonUtil.valueView(startDate, serviceDate, endDate);
                    Preconditions.checkArgument(serviceDate.compareTo(endDate) <= 0, "");
                    Preconditions.checkArgument(serviceDate.compareTo(startDate) >= 0, "");
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--按照接待日期查询");
        }
    }

    @Test(description = "售后--我的接待--按照接待日期查询，开始时间>结束时间")
    public void afterSale_reception_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        String endDate = DateTimeUtil.getFormat(new Date());
        String startDate = DateTimeUtil.addDayFormat(new Date(), -30);
        try {
            UserUtil.login(zjl);
            IScene scene = ReceptionAfterCustomerListScene.builder().searchDateStart(endDate).searchDateEnd(startDate).build();
            JSONArray list = crm.invokeApi(scene).getJSONArray("list");
            Preconditions.checkArgument(list.size() == 0, "开始时间>结束时间,查询出了结果");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--按照接待日期查询，开始时间>结束时间");
        }
    }

    @Test(description = "售后--我的接待--获取接待二维码")
    public void afterSale_reception_system_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(zjl);
            IScene scene = ShopQrcodeScene.builder().build();
            String qrcodeUrl = crm.invokeApi(scene).getString("qrcode_url");
            Preconditions.checkArgument(qrcodeUrl != null, "接待二维码为空");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--获取接待二维码");
        }
    }

    @Test(description = "售后接待--我的接待列表，维度1: 维修中在上，已完成在下,维度2: 接待日期倒序")
    public void afterSale_reception_system_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(zjl);
            //查询未完成接待的记录
            IScene scene = ReceptionAfterCustomerListScene.builder().page(1).size(size).build();
            JSONArray list = crm.invokeApi(scene).getJSONArray("list");
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("reception_status_name").equals("维修中")) {
                    count++;
                }
            }
            CommonUtil.valueView(count);
            for (int i = 1; i <= count; i++) {
                IScene scene1 = ReceptionAfterCustomerListScene.builder().page(1).size(i).build();
                JSONArray list1 = crm.invokeApi(scene1).getJSONArray("list");
                for (int j = 0; j < list1.size(); j++) {
                    String receptionStatusName = list1.getJSONObject(j).getString("reception_status_name");
                    CommonUtil.valueView(receptionStatusName);
                    Preconditions.checkArgument(receptionStatusName.equals("维修中"), "没有按照维修中在上排序");
                    String serviceDate = list1.getJSONObject(j).getString("service_date");
                    if (j != list1.size() - 1) {
                        String serviceDate1 = list1.getJSONObject(j + 1).getString("service_date");
                        CommonUtil.valueView(serviceDate, serviceDate1);
                        Preconditions.checkArgument(serviceDate.compareTo(serviceDate1) >= 0, "没有按照日期倒序");
                    }
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后接待--我的接待列表，维度1: 维修中在上，已完成在下,维度2: 接待日期倒序");
        }
    }

    @Test(description = "售后--我的接待--发送提车消息，成功后，按钮置灰。发送确认交车，按钮置灰", enabled = false)
    public void afterSale_reception_system_9() {
        logger.logCaseStart(caseResult.getCaseName());
        String customerName = "大马猴";
        String customerPhoneNumber = "13717737462";
        String date = DateTimeUtil.getFormat(new Date());
        try {
            //预约保养
            int id = getTimeId(date);
            crm.appointmentMaintain((long) getCarId(), customerName, customerPhoneNumber, date, "", (long) id);
            //完成接待
            int afterRecordId = method.getAfterRecordId(false, 30);
            method.completeReception(String.valueOf(afterRecordId));
            //提车
            crm.invokeApi(SendPickUpNewsScene.builder().afterRecordId(String.valueOf(afterRecordId)).build());
            int i = getFlag(afterRecordId, "提车消息");
            CommonUtil.valueView(i);
            Preconditions.checkArgument(i == 0, "发送提车消息，提车消息按钮没置灰");
            //交车
            crm.invokeApi(ConfirmCarScene.builder().afterRecordId(String.valueOf(afterRecordId)).build());
            int x = getFlag(afterRecordId, "确定交车");
            CommonUtil.valueView(x);
            Preconditions.checkArgument(x == 0, "发送确定交车消息，确定交车按钮置灰");
            //出门条
            String platNumber = crm.detailAfterSaleCustomer(String.valueOf(afterRecordId)).getString("plate_number");
            crm.sendExitNote(platNumber, String.valueOf(afterRecordId));
            int y = getFlag(afterRecordId, "出门条");
            CommonUtil.valueView(y);
            Preconditions.checkArgument(y == 1, "当天开出门条后还无法继续开");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--发送提车消息，成功后，按钮置灰。发送确认交车，按钮置灰");
        }
    }

    @Test(description = "售后--售后客户--车牌号筛选")
    public void afterSale_customer_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(zjl);
            String plateNumber = null;
            JSONArray list = crm.afterSaleCustomerList("", "", "", 1, size).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("plate_number") != null) {
                    plateNumber = list.getJSONObject(j).getString("plate_number");
                }
            }
            CommonUtil.valueView(plateNumber);
            Preconditions.checkArgument(plateNumber != null, "售后客户存在空车牌号");
            String findParam = plateNumber.substring(0, 3);
            CommonUtil.valueView(findParam);
            int total = crm.afterSaleCustomerList(findParam, "", "", 1, size).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray list1 = crm.afterSaleCustomerList(findParam, "", "", i, size).getJSONArray("list");
                for (int j = 0; j < list1.size(); j++) {
                    String resultPlateNumber = list1.getJSONObject(j).getString("plate_number");
                    Preconditions.checkArgument(resultPlateNumber.contains(findParam), "按照车牌号查询失败,搜索参数为：" + findParam);
                }
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("售后--售后客户--车牌号/联系方式筛选");
        }
    }

    @Test(description = "售后--售后客户--联系方式筛选")
    public void afterSale_customer_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(zjl);
            String customerPhoneNumber = null;
            JSONArray list = crm.afterSaleCustomerList("", "", "", 1, size).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("plate_number") != null) {
                    customerPhoneNumber = list.getJSONObject(j).getString("customer_phone_number");
                }
            }
            CommonUtil.valueView(customerPhoneNumber);
            Preconditions.checkArgument(customerPhoneNumber != null, "存在空电话号");
            String findParam = customerPhoneNumber.substring(0, 3);
            CommonUtil.valueView(findParam);
            int total = crm.afterSaleCustomerList(findParam, "", "", 1, size).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray list1 = crm.afterSaleCustomerList(findParam, "", "", i, size).getJSONArray("list");
                for (int j = 0; j < list1.size(); j++) {
                    String resultPlateNumber = list1.getJSONObject(i).getString("customer_phone_number");
                    Preconditions.checkArgument(resultPlateNumber.contains(findParam), "按电话号查询失败,搜索参数为：" + findParam);
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--售后客户--车牌号/联系方式筛选");
        }
    }

    @Test(description = "售后--我的接待--按照接待日期查询")
    public void afterSale_customer_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String endDate = DateTimeUtil.getFormat(new Date());
        String startDate = DateTimeUtil.addDayFormat(new Date(), -30);
        try {
            UserUtil.login(zjl);
            IScene scene = AfterSaleCustomerListScene.builder().searchDateStart(startDate).searchDateEnd(endDate).build();
            int total = crm.invokeApi(scene).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                IScene scene1 = AfterSaleCustomerListScene.builder().searchDateStart(startDate).searchDateEnd(endDate).page(String.valueOf(i)).size(String.valueOf(size)).build();
                JSONArray list = crm.invokeApi(scene1).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    String serviceDate = list.getJSONObject(j).getString("service_date");
                    if (!StringUtils.isEmpty(serviceDate)) {
                        CommonUtil.valueView(startDate, serviceDate, endDate);
                        Preconditions.checkArgument(serviceDate.compareTo(endDate) <= 0, "");
                        Preconditions.checkArgument(serviceDate.compareTo(startDate) >= 0, "");
                        CommonUtil.log("分割线");
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--按照接待日期查询");
        }
    }

    @Test(description = "售后--我的接待--按照接待日期查询，开始时间>结束时间")
    public void afterSale_customer_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        String endDate = DateTimeUtil.getFormat(new Date());
        String startDate = DateTimeUtil.addDayFormat(new Date(), -30);
        try {
            UserUtil.login(zjl);
            JSONArray list = crm.afterSaleCustomerList("", endDate, startDate, 1, size).getJSONArray("list");
            Preconditions.checkArgument(list.size() == 0, "开始时间>结束时间,查询出了结果");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--我的接待--按照接待日期查询，开始时间>结束时间");
        }
    }

    @Test(description = "售后--售后回访--不填必填参数")
    public void afterSale_returnVisit_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String comment = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            String message = returnVisit("售后回访", "", comment, date);
            Preconditions.checkArgument(message.equals("回访截图不可为空"), "售后回访截图为空也可回访成功");
            String message1 = returnVisit("售后回访", picture, "", date);
            Preconditions.checkArgument(message1.equals("回访描述不能为空"), "售后回访描述为空也可回访成功");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--售后回访--不填必填参数");
        }
    }

    @Test(description = "售后--首保提醒回访--不填必填参数")
    public void afterSale_returnVisit_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String comment = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            String message = returnVisit("首保提醒", "", comment, date);
            Preconditions.checkArgument(message.equals("回访截图不可为空"), "首保提醒回访截图为空也可回访成功");
            String message1 = returnVisit("首保提醒", picture, "", date);
            Preconditions.checkArgument(message1.equals("回访描述不能为空"), "首保提醒回访描述为空也可回访成功");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--首保提醒回访--不填必填参数");
        }
    }

    @Test(description = "售后--流失预警回访--不填必填参数")
    public void afterSale_returnVisit_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String comment = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            String message = returnVisit("流失预警", "", comment, date);
            Preconditions.checkArgument(message.equals("回访截图不可为空"), "流失预警回访截图为空也可回访成功");
            String message1 = returnVisit("流失预警", picture, "", date);
            Preconditions.checkArgument(message1.equals("回访描述不能为空"), "流失预警回访描述为空也可回访成功");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--流失预警回访--不填必填参数");
        }
    }

    @Test(description = "售后--活动任务--当前日期>=开始日期，填写报名置灰。当前日期<开始日期，填写报名高亮可点击")
    public void afterSale_activity_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            UserUtil.login(fw);
            int total = crm.activityTaskPage(1, size).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.activityTaskPage(i, size).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("activity_start").compareTo(date) <= 0) {
                        boolean isEdit = list.getJSONObject(j).getBoolean("is_edit");
                        CommonUtil.valueView(String.valueOf(isEdit));
                        Preconditions.checkArgument(!isEdit, "活动开始日期<=当前日期，活动可以报名");
                    }
                    if (list.getJSONObject(j).getString("activity_start").compareTo(date) > 0) {
                        boolean isEdit = list.getJSONObject(j).getBoolean("is_edit");
                        CommonUtil.valueView(String.valueOf(isEdit));
                        Preconditions.checkArgument(isEdit, "活动开始日期>当前日期，活动不可以报名");
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--活动报名--当前日期>=开始日期，填写报名置灰。当前日期<开始日期，填写报名高亮可点击");
        }
    }

    @Test(description = "售后--活动任务--异常情况")
    public void afterSale_activity_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String phone = "17777777777";
            int activityId = getActivityId();
            String message = crm.registeredCustomer1(activityId, "哈哈哈", phone + "1").getString("message");
            String message1 = crm.registeredCustomer1(activityId, "1", phone).getString("message");
            String message2 = crm.registeredCustomer1(activityId, "Q", phone).getString("message");
            String message3 = crm.registeredCustomer1(activityId, "/", phone).getString("message");
            String message4 = crm.registeredCustomer1(activityId, "哈哈哈啊哈哈哈哈哈哈哈", phone).getString("message");
            String message5 = crm.registeredCustomer1(activityId, "哈哈哈", "哈哈哈哈").getString("message");
            String message6 = crm.registeredCustomer1(activityId, "哈哈哈", "yyyyy").getString("message");
            String message7 = crm.registeredCustomer1(activityId, "哈哈哈", ",,,,,").getString("message");
            String message8 = crm.registeredCustomer1(activityId, "哈哈哈", "王12").getString("message");
            String message9 = crm.registeredCustomer1(activityId, "哈哈哈", "y12").getString("message");
            String message10 = crm.registeredCustomer1(activityId, "哈哈哈", "123/").getString("message");
            String message11 = crm.registeredCustomer1(activityId, "哈哈哈", phone.substring(0, phone.length() - 1)).getString("message");
            Preconditions.checkArgument(message.equals("请输入有效手机号码"), phone + "1".length() + "位手机号码报名成功");
            Preconditions.checkArgument(message1.equals("您只能输入长度不超过10的汉字") && message2.equals("您只能输入长度不超过10的汉字") && message3.equals("您只能输入长度不超过10的汉字"), "客户名称包含1、Q、/报名成功");
            Preconditions.checkArgument(message4.equals("客户名称长度不能超过十个字"), "客户名称超过10个字报名成功");
            Preconditions.checkArgument(message5.equals("请输入有效手机号码"), "中文号码报名成功");
            Preconditions.checkArgument(message6.equals("请输入有效手机号码"), "英文号码报名成功");
            Preconditions.checkArgument(message7.equals("请输入有效手机号码"), "标点号码报名成功");
            Preconditions.checkArgument(message8.equals("请输入有效手机号码"), "数字+中文号码报名成功");
            Preconditions.checkArgument(message9.equals("请输入有效手机号码"), "数字+英文号码报名成功");
            Preconditions.checkArgument(message10.equals("请输入有效手机号码"), "数字+标点号码报名成功");
            Preconditions.checkArgument(message11.equals("请输入有效手机号码"), phone.substring(0, phone.length() - 1).length() + "位手机号码报名成功");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后--活动任务--异常情况");
        }
    }

    @Test(description = "售后--活动任务--全部任务50，报名51人")
    public void afterSale_activity_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        Set<String> set = new HashSet<>();
        int activityId = 0;
        try {
            activityId = getActivityId();
            for (int i = 0; i < size; i++) {
                String phone = "159" + CommonUtil.getRandom(8);
                CommonUtil.valueView(phone);
                if (set.contains(phone)) {
                    continue;
                }
                set.add(phone);
                crm.registeredCustomer1(activityId, "哈哈哈", phone).getString("message");
                CommonUtil.log("分割线");
            }
            String message = crm.registeredCustomer1(activityId, "哈哈哈", "13473166806").getString("message");
            Preconditions.checkArgument(message.equals("最多50条哦~"), "活动报名人数51人成功");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            for (String s : set) {
                deleteActivityCustomer(activityId, s);
            }
            saveData("售后--活动任务--全部任务50，报名51人");
        }
    }

    @Test(description = "售后--活动任务--添加销售添加过的联系方式,失败")
    public void afterSale_activity_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        int activityId = 0;
        String phone = "15321527989";
        try {
            activityId = getActivityId();
            crm.registeredCustomer1(activityId, "哈哈哈", phone).getString("message");
            String message = crm.registeredCustomer1(activityId, "哈哈哈", phone).getString("message");
            Preconditions.checkArgument(message.equals("当前手机号已经报过了哦~"), "添加销售添加过的联系方式，成功");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            deleteActivityCustomer(activityId, phone);
            saveData("售后--活动任务--添加销售添加过的联系方式,失败");
        }
    }

    private void deleteActivityCustomer(int activityId, String phone) {
        JSONArray list = crm.activityTaskInfo(String.valueOf(activityId)).getJSONArray("customer_list");
        for (int i = 0; i < list.size(); i++) {
            if (list.getJSONObject(i).getString("customer_phone_number").equals(phone)) {
                String customerId = list.getJSONObject(i).getString("customer_id");
                crm.deleteCustomer(String.valueOf(activityId), customerId);
            }
        }
    }

    /**
     * 获取活动id
     *
     * @return activityId
     */
    private int getActivityId() {
        UserUtil.login(fw);
        JSONArray list = crm.activityTaskPage(1, size).getJSONArray("list");
        int activityTaskId = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.getJSONObject(i).getBoolean("is_edit")
                    && list.getJSONObject(i).getString("activity_task_status_name").equals("未完成")) {
                activityTaskId = list.getJSONObject(i).getInteger("activity_task_id");
            }
        }
        return activityTaskId;
    }

    /**
     * 获取预约时间id
     *
     * @param date 预约日期
     * @return 时间id
     */
    private Integer getTimeId(String date) {
        UserUtil.loginApplet(EnumAppletCode.WM_SMALL);
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
        UserUtil.loginApplet(EnumAppletCode.XMF);
        JSONArray list = crm.myCarList().getJSONArray("list");
        if (!list.isEmpty()) {
            return list.getJSONObject(0).getInteger("my_car_id");
        }
        throw new DataException("该用户小程序没有绑定车");
    }

    /**
     * 回访
     */
    private int createReturnVisitTask(String type) {
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String comment = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        JSONObject response;
        switch (type) {
            case "首保提醒":
                response = crm.firstMaintainRecordPage(1, size, "");
                break;
            case "流失预警":
                response = crm.customerChurnWarningPage(1, size, "");
                break;
            default:
                response = crm.returnVisitRecordAfterSalePage(1, size, "");
        }
        JSONArray list = response.getJSONArray("list");
        int id = 0;
        if (list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("return_visit_status_name").equals("未完成")) {
                    id = list.getJSONObject(j).getInteger("id");
                    break;
                }
            }
        }
        if (id != 0) {
            crm.afterSale_addVisitRecord((long) id, picture, comment, date);
        }
        return id;
    }

    private String returnVisit(String type, String picture, String comment, String date) {
        JSONObject response;
        switch (type) {
            case "首保提醒":
                response = crm.firstMaintainRecordPage(1, size, "");
                break;
            case "流失预警":
                response = crm.customerChurnWarningPage(1, size, "");
                break;
            default:
                response = crm.returnVisitRecordAfterSalePage(1, size, "");
        }
        JSONArray list = response.getJSONArray("list");
        int id = 0;
        if (list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("return_visit_status_name").equals("未完成")) {
                    id = list.getJSONObject(j).getInteger("id");
                    break;
                }
            }
        }
        return crm.returnVisitRecordExecute((long) id, picture, comment, date).getString("message");
    }

    /**
     * 获取操作标签
     */
    private int getFlag(int afterRecordId, String flag) {
        int f = 0;
        int total = crm.receptionAfterCustomerList("", "", "", 1, size).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            JSONArray array = crm.receptionAfterCustomerList("", "", "", i, size).getJSONArray("list");
            for (int j = 0; j < array.size(); j++) {
                if (array.getJSONObject(j).getInteger("after_record_id") == afterRecordId) {
                    switch (flag) {
                        case "提车消息":
                            f = array.getJSONObject(j).getInteger("if_show_pick_up_news");
                            break;
                        case "确定交车":
                            f = array.getJSONObject(j).getInteger("if_deliver_car");
                            break;
                        default:
                            f = array.getJSONObject(j).getInteger("if_show_exit_note");
                    }
                }
            }
        }
        return f;
    }
}
