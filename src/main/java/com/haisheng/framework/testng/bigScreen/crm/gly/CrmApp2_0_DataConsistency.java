package com.haisheng.framework.testng.bigScreen.crm.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.crm.wm.PcData;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletCode;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppointmentType;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCarModel;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCustomerInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc.OrderMaintainPageScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc.OrderRepairPageScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;


/**
 * @author : guoliya
 * @date :  2020/08/20
 */

public class CrmApp2_0_DataConsistency extends TestCaseCommon implements TestCaseStd {
    PublicMethod method = new PublicMethod();
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    EnumAccount zjl = EnumAccount.ZJL_DAILY;
    private static final int size = 50;
    int zjl_num = 0;
    int gw_num = 0;
    String xs_name = "0805xsgw";//销售顾问
    String by_name = "lxqby";//保养顾问姓名
    String by_name2 = "baoyang";
    String wx_name = "lxqwx";//维修顾问姓名
    String zjl_name = "zjl";
    String fwzj_name = "baoyang";//服务总监
    String pwd = "e10adc3949ba59abbe56e057f20f883e";//密码全部一致
    String qt_name = "qt";//前台账号
    String bsj_name = "baoshijie";
    //我的车辆信息
    long my_car_id = 61L;
    String plate_number = "吉A66ZDH";
    int car_type = 4;
    //预约信息
    String customer_name = "自动化哈";
    String customer_phone_number = "13400000000";


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //checklist相关配置
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "gly";
        //replace backend gateway url
        //commonConfig.gateway = "";
        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "CRM 日常-gly");
        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        //crm.login(sh_name1, sh_pwd1);

    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        UserUtil.login(zjl);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    //    APP-售后客户管理：全部车辆<=列表数
    @Ignore
    @Test //ok
    public void custChkallEQList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            //列表总数
            int listTotal = crm.afterSaleCustList("", "", "", 1, 1).getInteger("total");
            //统计数据-全部车辆数
            int allCar = crm.afterSale_custTotal().getInteger("all_reception_total");
            Preconditions.checkArgument(allCar <= listTotal, "全部车辆" + listTotal + "!=列表数" + allCar);
        } catch (AssertionError e) {
            logger.info(e.toString());
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户管理：全部车辆<=列表数");
        }

    }

    //    APP-售后客户管理：今日接待售后车辆>=今日新增售后车辆
    @Test
    public void customerChkTotalGTNew() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            JSONObject list = crm.afterSale_custTotal();
            //统计数据-今日接待售后数量
            int todayTotal = list.getInteger("today_reception_total");
            //统计数据-今日新增售后数量
            int todayNew = list.getInteger("today_new_after_sale_customer");

            Preconditions.checkArgument(todayTotal >= todayNew, "今日接待售后数量" + todayTotal + "<今日新增售后数量" + todayNew);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户管理：今日接待售后车辆>=今日新增售后车辆");
        }
    }

    //    APP-售后客户管理：全部车辆>=今日接待售后车辆
    @Test
    public void customerChkAllTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            JSONObject list = crm.afterSale_custTotal();
            //统计数据-今日接待售后数量
            int todayTotal = list.getInteger("today_reception_total");
            //统计数据-全部车辆
            int all = list.getInteger("all_reception_total");

            Preconditions.checkArgument(all >= todayTotal, "全部车辆" + all + "<今日接待售后数量" + todayTotal);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户管理：全部车辆>=今日接待售后车辆");

        }
    }

    //    APP-售后客户管理：添加21条备注，展示最新20条---小程序鉴权失败
    @Test(enabled = false)
    public void afterSaleAddRemarks21() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //小程序预约，售后点击接待按钮，获取接待记录ID和售后登陆账号
            String[] a = repair(dt.getHistoryDate(1), my_car_id, "yes");
            Long after_record_id = Long.parseLong(a[2]);

            List<String> remarks = new ArrayList<String>();
            for (int i = 1; i < 30; i++) {
                remarks.add(dt.getHistoryDate(0) + "1Z！@#自动化备注" + i);
            }
            int travel_mileage = 1;
            int maintain_type = 1;
            boolean service_complete = false;
            int customer_source = 0;
            //添加备注
            crm.afterSaleCustList(after_record_id, customer_name, customer_phone_number, customer_phone_number, plate_number, travel_mileage,
                    car_type, maintain_type, maintain_type, service_complete, customer_source, remarks);
            //查看客户信息的备注
            JSONArray remarkList = crm.detailAfterSaleCustomer(String.valueOf(after_record_id)).getJSONArray("remarks");
            int size = remarkList.size();//备注条数
            Preconditions.checkArgument(size == 20, "备注有{}条" + size);
            for (int i = 0; i < remarkList.size(); i++) {
                JSONObject obj = remarkList.getJSONObject(i);
                String search_remark = obj.getString("remark");
                Preconditions.checkArgument(!search_remark.equals(dt.getHistoryDate(0) + "1Z！@#自动化备注1"), "包含第一条备注");
            }
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户管理：添加21条备注，展示最新20条");

        }
    }

    //    售后客户管理：添加21条回访，展示最新20条--小程序鉴权失败
    @Test(enabled = false) //bug2695
    public void afterSaleAddVisitRecord21() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //小程序预约，售后点击接待按钮，获取接待记录ID和售后登陆账号
            String[] a = repair(dt.getHistoryDate(1), my_car_id, "yes");
            Long after_record_id = Long.parseLong(a[2]);


            //销售查看回访记录获取id
            Long recordid = crm.afterSale_VisitRecordList(1, 1, "", "", "").getJSONArray("list").getJSONObject(0).getLong("id");

            //添加回访
            String return_visit_pic = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/分辨率较低.png");
            String next_return_visit_time = dt.getHistoryDate(1);
            for (int i = 1; i < 25; i++) {
                String comment = dt.getHistoryDate(0) + "1Z！@#自动化回访" + i;
                crm.afterSale_addVisitRecord(recordid, return_visit_pic, comment, next_return_visit_time);
                Thread.sleep(100);
            }

            //查看顾客信息
            JSONArray visit_list = crm.detailAfterSaleCustomer(String.valueOf(after_record_id)).getJSONArray("visit");
            int size = visit_list.size();
            Preconditions.checkArgument(size == 20, "回访记录条数=" + size);
            //查看回访内容
            for (int i = 0; i < size; i++) {
                JSONObject obj = visit_list.getJSONObject(i);
                String comment = obj.getString("comment");
                Preconditions.checkArgument(!comment.equals(dt.getHistoryDate(0) + "1Z！@#自动化回访1"), "包含第一条回访记录");
            }


        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后客户管理：添加21条回访，展示最新20条");

        }
    }


    //APP-全部预约保养>=今日预约保养
    @Test
    public void afterSaleMaintainAllGEToday() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            JSONObject obj = crm.mainAppointmentDriverNum();
            System.out.println("     ---" + obj);
            int total = obj.getInteger("appointment_total_number");
            int today = obj.getInteger("appointment_today_number");
            Preconditions.checkArgument(total >= today, "全部预约保养" + total + "<今日预约保养" + today);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("全部预约保养>=今日预约保养");
        }
    }

    //  APP-新客的所属顾问=当前登陆账号销售名字---有问题
    @Test(enabled = false)
    public void afterSaleNewChkRecpname() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            int total = crm.mainAppointmentList(1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.mainAppointmentList(i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("customer_type_name").equals("新客")) {
                        String saleName = list.getJSONObject(j).getString("reception_sale_name");
                        String name = getSaleName(by_name);
                        CommonUtil.valueView(saleName, name);
                        Preconditions.checkArgument(saleName.equals(name), "所属顾问为" + saleName);
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-新客的所属顾问=当前登陆账号销售名字");
        }
    }

    private String getSaleName(String userLoginName) {
        crm.login(zjl_name, pwd);
        int total = crm.userUserPage(1, 10).getInteger("total");
        int x = CommonUtil.getTurningPage(total, 100);
        String userName = null;
        for (int i = 1; i < x; i++) {
            JSONArray list = crm.userUserPage(i, 100).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("user_login_name").equals(userLoginName)) {
                    userName = list.getJSONObject(j).getString("user_name");
                }
            }
        }
        return userName;
    }

    //    小程序1个人1个车预约1次，校验预约保养页面统计数据--小程序鉴权失败
    @Test(enabled = false)
    public void afterSaleChk1Maintain1Num() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //销售登陆查看统计数据
            crm.login(by_name, pwd);
            JSONObject obj = crm.mainAppointmentDriverNum();
            int total_bef = obj.getInteger("appointment_total_number");
            int today_bef = obj.getInteger("appointment_today_number");
            int total = crm.mainAppointmentList().getInteger("total");//列表数
            //小程序预约
            Long id = Long.parseLong(maintain(dt.getHistoryDate(1), my_car_id, "no")[1]);
            //销售登陆查看统计数据
            crm.login(by_name, pwd);
            JSONObject obj1 = crm.mainAppointmentDriverNum();
            int total_after = obj1.getInteger("appointment_total_number");
            int today_after = obj1.getInteger("appointment_today_number");
            int total1 = crm.mainAppointmentList().getInteger("total");//列表数
            //预约不取消，今日/全部+1
            int changetotal1 = total_after - total_bef;
            int changetoday1 = today_after - today_bef;
            int changelist = total1 - total;
            //小程序取消预约
            crm.cancle(id);
            //销售登陆查看统计数据
            crm.login(by_name, pwd);
            JSONObject obj2 = crm.mainAppointmentDriverNum();
            int total_after1 = obj2.getInteger("appointment_total_number");
            int today_after1 = obj2.getInteger("appointment_today_number");
            int total2 = crm.mainAppointmentList().getInteger("total");//列表数
            int changetotal2 = total_after - total_after1;
            int changetoday2 = today_after - today_after1;

            Preconditions.checkArgument(changetoday1 == 1, "小程序预约后，今日预约人数期待+1，实际+" + changetoday1);
            Preconditions.checkArgument(changetotal1 == 1, "小程序预约后，全部预约人数期待+1，实际+" + changetotal1);
            Preconditions.checkArgument(changelist == 1, "小程序预约后，列表数期待+1，实际+" + changelist);

            Preconditions.checkArgument(changetotal2 == 1, "小程序取消预约后，今日预约人数期待-1，实际+" + changetoday2);
            Preconditions.checkArgument(changetoday2 == 1, "小程序取消预约后，全部预约人数期待-1，实际+" + changetotal2);
            Preconditions.checkArgument(total2 == total1, "小程序取消预约后，列表数期待不变，实际+" + changetotal2);


        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("小程序1个人1个车预约1次，校验预约保养页面统计数据");

        }
    }

    //    小程序1个人1个车预约2次，校验预约保养页面统计数据--小程序鉴权失败
    @Test(enabled = false)
    public void afterSaleChk1Maintain2Num() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(by_name, pwd);
            //销售登陆查看统计数据
            JSONObject obj = crm.mainAppointmentDriverNum();
            int total_bef = obj.getInteger("appointment_total_number");
            int today_bef = obj.getInteger("appointment_today_number");
            int total = crm.mainAppointmentList().getInteger("total");//列表数
            //小程序预约

            Long id1 = Long.parseLong(maintain(dt.getHistoryDate(1), my_car_id, "no")[1]);
            Long id2 = Long.parseLong(maintain(dt.getHistoryDate(1), my_car_id, "no")[1]);

            JSONObject obj1 = crm.mainAppointmentDriverNum();
            int total_after = obj1.getInteger("appointment_total_number");
            int today_after = obj1.getInteger("appointment_today_number");
            int total1 = crm.mainAppointmentList().getInteger("total");//列表数
            //预约不取消，今日/全部+1
            int changetotal1 = total_after - total_bef;
            int changetoday1 = today_after - today_bef;
            int changelist = total1 - total;
            //小程序取消1个预约
            crm.cancle(id1);
            JSONObject obj2 = crm.mainAppointmentDriverNum();
            int total_after1 = obj2.getInteger("appointment_total_number");
            int today_after1 = obj2.getInteger("appointment_today_number");
            int total2 = crm.mainAppointmentList().getInteger("total");//列表数
            int changetotal2 = total_after - total_after1;
            int changetoday2 = today_after - today_after1;

            //小程序取消全部预约
            crm.cancle(id2);
            JSONObject obj3 = crm.mainAppointmentDriverNum();
            int total_after2 = obj3.getInteger("appointment_total_number");
            int today_after2 = obj3.getInteger("appointment_today_number");
            int total3 = crm.mainAppointmentList().getInteger("total");//列表数
            int changetotal3 = total_after - total_after2;
            int changetoday3 = today_after - today_after2;

            Preconditions.checkArgument(changetoday1 == 1, "预约后，今日预约人数期待+1，实际+" + changetoday1);
            Preconditions.checkArgument(changetotal1 == 1, "预约后，全部预约人数期待+1，实际+" + changetotal1);
            Preconditions.checkArgument(changelist == 2, "预约后，列表数期待+2，实际+" + changelist);

            Preconditions.checkArgument(changetotal2 == 0, "取消1个预约后，今日预约人数期待-0，实际+" + changetoday2);
            Preconditions.checkArgument(changetoday2 == 0, "取消1个预约后，全部预约人数期待-0，实际+" + changetotal2);
            Preconditions.checkArgument(total2 == total1, "取消1个预约后，列表数期待不变，实际+" + changetotal2);

            Preconditions.checkArgument(changetotal3 == 1, "取消全部预约后，今日预约人数期待-1，实际+" + changetoday2);
            Preconditions.checkArgument(changetoday3 == 1, "取消全部预约后，全部预约人数期待-1，实际+" + changetotal2);
            Preconditions.checkArgument(total2 == total3, "取消全部预约后，列表数期待不变，实际+" + changetotal2);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("小程序1个人1个车预约2次，校验预约保养页面统计数据");

        }
    }

    //    小程序1个人2个车各预约1次，校验预约保养页面统计数据--小程序鉴权失败
    @Test(enabled = false)
    public void afterSaleChk2Maintain1Num() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(by_name, pwd);
            //销售登陆查看统计数据
            JSONObject obj = crm.mainAppointmentDriverNum();
            int total_bef = obj.getInteger("appointment_total_number");
            int today_bef = obj.getInteger("appointment_today_number");
            int total = crm.mainAppointmentList().getInteger("total");//列表数
            //小程序预约
            Long id = Long.parseLong(maintain(dt.getHistoryDate(1), my_car_id, "no")[1]);
            //再加
            // Long id2 = Long.parseLong(maintain(dt.getHistoryDate(1),carid2,"no")[1]);
            JSONObject obj1 = crm.mainAppointmentDriverNum();
            int total_after = obj1.getInteger("appointment_total_number");
            int today_after = obj1.getInteger("appointment_today_number");
            int total1 = crm.mainAppointmentList().getInteger("total");//列表数
            //预约不取消，今日/全部+2
            int changetotal1 = total_after - total_bef;
            int changetoday1 = today_after - today_bef;
            int changelist = total1 - total;
            //小程序取消预约
            crm.cancle(id);
//            crm.cancle(id2);
            JSONObject obj2 = crm.mainAppointmentDriverNum();
            int total_after1 = obj2.getInteger("appointment_total_number");
            int today_after1 = obj2.getInteger("appointment_today_number");
            int total2 = crm.mainAppointmentList().getInteger("total");//列表数
            int changetotal2 = total_after - total_after1;
            int changetoday2 = today_after - today_after1;

            Preconditions.checkArgument(changetoday1 == 2, "小程序预约后，今日预约人数期待+2，实际+" + changetoday1);
            Preconditions.checkArgument(changetotal1 == 2, "小程序预约后，全部预约人数期待+2，实际+" + changetotal1);
            Preconditions.checkArgument(changelist == 1, "小程序预约后，列表数期待+2，实际+" + changelist);

            Preconditions.checkArgument(changetotal2 == 2, "小程序取消预约后，今日预约人数期待-2，实际+" + changetoday2);
            Preconditions.checkArgument(changetoday2 == 2, "小程序取消预约后，全部预约人数期待-2，实际+" + changetotal2);
            Preconditions.checkArgument(total2 == total1, "小程序取消预约后，列表数期待不变，实际+" + changetotal2);


        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("小程序1个人2个车各预约1次，校验预约保养页面统计数据");

        }
    }


    //APP-全部预约维修>=今日预约维修
    @Test
    public void afterSaleRepairAllGEToday() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(wx_name, pwd);
            JSONObject obj = crm.repairAppointmentDriverNum();
            int total = obj.getInteger("appointment_total_number");
            int today = obj.getInteger("appointment_today_number");
            Preconditions.checkArgument(total >= today, "全部预约维修{}<今日预约维修{}", total, today);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-全部预约维修>=今日预约维修");
        }
    }

    // APP-全部预约维修<=列表数
    @Test(enabled = false)
    public void afterSaleRepairAllLEList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(wx_name, pwd);
            JSONObject obj = crm.repairAppointmentDriverNum();
            int total = obj.getInteger("appointment_total_number");

            int list = crm.repairAppointmentlist().getInteger("total");
            Preconditions.checkArgument(total <= list, "全部预约维修" + total + ">列表数" + list);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-全部预约维修<=列表数");

        }
    }

    //    APP-新客的所属顾问=当前登陆账号销售名字
    @Test
    public void afterSaleRepairNewChkRecpname() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(wx_name, pwd);
            JSONObject response = crm.repairAppointmentlist();
            JSONArray maintain = response.getJSONArray("list");
            int pages = response.getInteger("pages");
            int size = maintain.size();
            for (int page = 1; page <= pages; page++) {
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        JSONObject obj = maintain.getJSONObject(i);
                        if (obj.getString("customer_type_name").equals("新客") && obj.getString("service_status_name").equals("预约中")) {
                            String userName = obj.getString("user_name");
                            Preconditions.checkArgument(userName.equals(wx_name), "所属顾问为" + userName);
                        }
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-新客的所属顾问=当前登陆账号销售名字");

        }
    }

    //    小程序1个人1个车预约1次，校验预约维修页面统计数据--小程序鉴权失败
    @Test(enabled = false)
    public void afterSaleChk1Repair1Num() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(wx_name, pwd);
            //销售登陆查看统计数据
            JSONObject obj = crm.repairAppointmentDriverNum();
            int total_bef = obj.getInteger("appointment_total_number");
            int today_bef = obj.getInteger("appointment_today_number");
            int total = crm.repairAppointmentlist().getInteger("total");//列表数
            //小程序预约
            Long id = Long.parseLong(repair(dt.getHistoryDate(1), my_car_id, "no")[1]);
            JSONObject obj1 = crm.repairAppointmentDriverNum();
            int total_after = obj1.getInteger("appointment_total_number");
            int today_after = obj1.getInteger("appointment_today_number");
            int total1 = crm.repairAppointmentlist().getInteger("total");//列表数
            //预约不取消，今日/全部+1
            int changetotal1 = total_after - total_bef;
            int changetoday1 = today_after - today_bef;
            int changelist = total1 - total;
            //小程序取消预约
            crm.cancle(id);
            JSONObject obj2 = crm.repairAppointmentDriverNum();
            int total_after1 = obj2.getInteger("appointment_total_number");
            int today_after1 = obj2.getInteger("appointment_today_number");
            int total2 = crm.repairAppointmentlist().getInteger("total");//列表数
            int changetotal2 = total_after - total_after1;
            int changetoday2 = today_after - today_after1;

            Preconditions.checkArgument(changetoday1 == 1, "小程序预约后，今日预约人数期待+1，实际+" + changetoday1);
            Preconditions.checkArgument(changetotal1 == 1, "小程序预约后，全部预约人数期待+1，实际+" + changetotal1);
            Preconditions.checkArgument(changelist == 1, "小程序预约后，列表数期待+1，实际+" + changelist);

            Preconditions.checkArgument(changetotal2 == 1, "小程序取消预约后，今日预约人数期待-1，实际+" + changetoday2);
            Preconditions.checkArgument(changetoday2 == 1, "小程序取消预约后，全部预约人数期待-1，实际+" + changetotal2);
            Preconditions.checkArgument(total2 == total1, "小程序取消预约后，列表数期待不变，实际+" + changetotal2);


        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("小程序1个人1个车预约1次，校验预约维修页面统计数据");

        }
    }

    //    小程序1个人1个车预约2次，校验预约维修页面统计数据--小程序鉴权失败
    @Test(enabled = false)
    public void afterSaleChk1Repair2Num() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(wx_name, pwd);
            //销售登陆查看统计数据
            JSONObject obj = crm.repairAppointmentDriverNum();
            int total_bef = obj.getInteger("appointment_total_number");
            int today_bef = obj.getInteger("appointment_today_number");
            int total = crm.repairAppointmentlist().getInteger("total");//列表数
            //小程序预约

            Long id1 = Long.parseLong(repair(dt.getHistoryDate(1), my_car_id, "no")[1]);
            Long id2 = Long.parseLong(repair(dt.getHistoryDate(1), my_car_id, "no")[1]);

            JSONObject obj1 = crm.repairAppointmentDriverNum();
            int total_after = obj1.getInteger("appointment_total_number");
            int today_after = obj1.getInteger("appointment_today_number");
            int total1 = crm.repairAppointmentlist().getInteger("total");//列表数
            //预约不取消，今日/全部+1
            int changetotal1 = total_after - total_bef;
            int changetoday1 = today_after - today_bef;
            int changelist = total1 - total;
            //小程序取消1个预约
            crm.cancle(id1);
            JSONObject obj2 = crm.repairAppointmentDriverNum();
            int total_after1 = obj2.getInteger("appointment_total_number");
            int today_after1 = obj2.getInteger("appointment_today_number");
            int total2 = crm.repairAppointmentlist().getInteger("total");//列表数
            int changetotal2 = total_after - total_after1;
            int changetoday2 = today_after - today_after1;

            //小程序取消全部预约
            crm.cancle(id2);
            JSONObject obj3 = crm.repairAppointmentDriverNum();
            int total_after2 = obj3.getInteger("appointment_total_number");
            int today_after2 = obj3.getInteger("appointment_today_number");
            int total3 = crm.repairAppointmentlist().getInteger("total");//列表数
            int changetotal3 = total_after - total_after2;
            int changetoday3 = today_after - today_after2;

            Preconditions.checkArgument(changetoday1 == 1, "预约后，今日预约人数期待+1，实际+" + changetoday1);
            Preconditions.checkArgument(changetotal1 == 1, "预约后，全部预约人数期待+1，实际+" + changetotal1);
            Preconditions.checkArgument(changelist == 2, "预约后，列表数期待+2，实际+" + changelist);

            Preconditions.checkArgument(changetotal2 == 0, "取消1个预约后，今日预约人数期待-0，实际+" + changetoday2);
            Preconditions.checkArgument(changetoday2 == 0, "取消1个预约后，全部预约人数期待-0，实际+" + changetotal2);
            Preconditions.checkArgument(total2 == total1, "取消1个预约后，列表数期待不变，实际+" + changetotal2);

            Preconditions.checkArgument(changetotal3 == 1, "取消全部预约后，今日预约人数期待-1，实际+" + changetoday2);
            Preconditions.checkArgument(changetoday3 == 1, "取消全部预约后，全部预约人数期待-1，实际+" + changetotal2);
            Preconditions.checkArgument(total2 == total3, "取消全部预约后，列表数期待不变，实际+" + changetotal2);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("小程序1个人1个车预约2次，校验预约维修页面统计数据");

        }
    }

    //1.APP-全部回访=列表条数
    @Test
    public void afterSalesReturnVisit() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            //1.全部回访=列表条数
            JSONObject obj = crm.afterSale_VisitRecordList(1, 10, "", "", "");
            int pages = obj.getInteger("pages");
            Integer total = obj.getInteger("total");//全部回访条数
            int listTotal = 0;//列表的条数
            for (int page = 1; page <= pages; page++) {
                JSONArray list1 = crm.afterSale_VisitRecordList(page, 10, "", "", "").getJSONArray("list");
                for (int j = 0; j < list1.size(); j++) {
                    Integer id = list1.getJSONObject(j).getInteger("id");
                    if (id != null) {
                        listTotal++;
                    }
                }
            }
            System.out.println("listTotal------" + listTotal);
            Preconditions.checkArgument(total == listTotal, "售后工作管理中我的回访-售后回访中的全部回访" + total + "不等于后工作管理中我的回访-售后回访中的列表条数" + listTotal);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-全部回访=列表条数");

        }
    }

    //     APP-今日回访=任务日期为今天的条数
    @Test
    public void afterSalesReturnVisit1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            JSONObject obj1 = crm.afterSale_VisitRecordList(1, 10, "", dt.getHistoryDate(0), dt.getHistoryDate(0));
            Integer total = obj1.getInteger("total");
            int pages1 = obj1.getInteger("pages");
            Integer todayViNum = obj1.getInteger("today_return_visit_number");//获取今日回访条数
            int todayListTotal = 0;
            for (int i = 1; i <= pages1; i++) {
                JSONArray todayList1 = crm.afterSale_VisitRecordList(i, 10, "", dt.getHistoryDate(0), dt.getHistoryDate(0)).getJSONArray("list");
                for (int k = 0; k < todayList1.size(); k++) {
                    Integer id = todayList1.getJSONObject(k).getInteger("id");
                    if (id != null) {
                        todayListTotal++;
                    }
                }
            }
            boolean flag = false;
            if (total >= todayListTotal) {
                flag = true;
            }
            Preconditions.checkArgument(flag, "全部回访" + total + "大于今日回访" + todayListTotal);
            Preconditions.checkArgument(todayViNum == todayListTotal, "售后工作管理中我的回访-售后回访中的今日回访" + todayViNum + "不等于后工作管理中我的回访-售后回访中任务日期为今天的条数" + todayListTotal);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-今日回访=任务日期为今天的条数");

        }
    }

    //  APP-售后工作-我的回访-首保提醒-数据一致性验证
    @Test
    public void afterSalesfirstmMintain() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            //1.全部回访=列表条数
            JSONObject obj = crm.afterSale_firstmMintainRecordList(1, 50, "", "", "");
            Integer total = obj.getInteger("total");//全部回访条数
            int pages = obj.getInteger("pages");
            int listTotal = 0;//列表的条数
            for (int page = 1; page <= pages; page++) {
                JSONArray list1 = crm.afterSale_firstmMintainRecordList(page, 50, "", "", "").getJSONArray("list");
                for (int j = 0; j < list1.size(); j++) {
                    Integer id = list1.getJSONObject(j).getInteger("id");
                    if (id != null) {
                        listTotal++;
                    }
                }
            }

            //2.今日回访=任务日期为今天的条数
            JSONObject obj1 = crm.afterSale_firstmMintainRecordList(1, 50, "", dt.getHistoryDate(0), dt.getHistoryDate(0));
            Integer todayViNum = obj1.getInteger("today_return_visit_number");//获取今日回访条数
            System.out.println("todayViNum-------" + todayViNum);
            int pages1 = obj.getInteger("pages");
            int todayListTotal = 0;
            for (int i = 1; i <= pages1; i++) {
                JSONArray todayList = crm.afterSale_firstmMintainRecordList(i, 50, "", dt.getHistoryDate(0), dt.getHistoryDate(0)).getJSONArray("list");
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
            Preconditions.checkArgument(total == listTotal, "售后工作管理中我的回访-首保提醒中的全部回访" + total + "不等于售后后工作管理中我的回访-首保提醒中的列表条数" + listTotal);
            Preconditions.checkArgument(todayViNum == todayListTotal, "售后工作管理中我的回访-售后回访中的今日回访" + todayViNum + "不等于后工作管理中我的回访-售后回访中任务日期为今天的条数" + todayListTotal);
            Preconditions.checkArgument(flag, "全部回访" + total + "大于今日回访" + todayListTotal);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后工作-我的回访-首保提醒-数据一致性验证");
        }
    }

    //    APP-售后工作-我的回访-流失预警-数据一致性验证
    @Test
    public void afterSalescustomerChurnWarn() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            //1.全部回访=列表条数
            JSONObject obj = crm.afterSale_customerChurnWarningList(1, 50, "", "", "");
            Integer total = obj.getInteger("total");//全部回访条数
            int pages = obj.getInteger("pages");
            int listTotal = 0;//列表的条数
            for (int page = 1; page <= pages; page++) {
                JSONArray list1 = crm.afterSale_customerChurnWarningList(1, 50, "", "", "").getJSONArray("list");
                for (int j = 0; j < list1.size(); j++) {
                    Integer id = list1.getJSONObject(j).getInteger("id");
                    if (id != null) {
                        listTotal++;
                    }
                }
            }

            //2.今日回访=任务日期为今天的条数
            JSONObject obj1 = crm.afterSale_customerChurnWarningList(1, 50, "", dt.getHistoryDate(0), dt.getHistoryDate(0));
            Integer todayViNum = obj1.getInteger("today_return_visit_number");//获取今日回访条数
            int pages1 = obj.getInteger("pages");
            int todayListTotal = 0;
            for (int i = 1; i <= pages1; i++) {
                JSONArray todayList = crm.afterSale_customerChurnWarningList(1, 50, "", dt.getHistoryDate(0), dt.getHistoryDate(0)).getJSONArray("list");
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

            Preconditions.checkArgument(total == listTotal, "售后工作管理中我的回访-流失预警中的全部回访" + total + "不等于售后工作管理中我的回访-流失预警中的列表条数" + listTotal);
            Preconditions.checkArgument(todayViNum == todayListTotal, "售后工作管理中我的回访-流失预警中的今日回访" + todayViNum + "不等于售后工作管理中我的回访-流失预警中任务日期为今天的条数" + todayListTotal);
            Preconditions.checkArgument(flag, "全部回访" + total + "大于今日回访" + todayListTotal);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后工作-我的回访-流失预警-数据一致性验证");

        }
    }

    //    小程序1个人2个车各预约1次，--小程序鉴权失败
    @Test(enabled = false)
    public void afterSaleChk2Repair1Num() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(wx_name, pwd);
            //销售登陆查看统计数据
            JSONObject obj = crm.repairAppointmentDriverNum();
            int total_bef = obj.getInteger("appointment_total_number");
            int today_bef = obj.getInteger("appointment_today_number");
            int total = crm.repairAppointmentlist().getInteger("total");//列表数
            //小程序预约
            Long id = Long.parseLong(repair(dt.getHistoryDate(1), my_car_id, "no")[1]);
            //mycarid2
            // Long id2 = Long.parseLong(repair(dt.getHistoryDate(1),carid2,"no")[1]);
            JSONObject obj1 = crm.repairAppointmentDriverNum();
            int total_after = obj1.getInteger("appointment_total_number");
            int today_after = obj1.getInteger("appointment_today_number");
            int total1 = crm.repairAppointmentlist().getInteger("total");//列表数
            //预约不取消，今日/全部+2
            int changetotal1 = total_after - total_bef;
            int changetoday1 = today_after - today_bef;
            int changelist = total1 - total;
            //小程序取消预约
            crm.cancle(id);
            //crm.cancle(id2);
            JSONObject obj2 = crm.repairAppointmentDriverNum();
            int total_after1 = obj2.getInteger("appointment_total_number");
            int today_after1 = obj2.getInteger("appointment_today_number");
            int total2 = crm.repairAppointmentlist().getInteger("total");//列表数
            int changetotal2 = total_after - total_after1;
            int changetoday2 = today_after - today_after1;

            Preconditions.checkArgument(changetoday1 == 2, "小程序预约后，今日预约人数期待+2，实际+" + changetoday1);
            Preconditions.checkArgument(changetotal1 == 2, "小程序预约后，全部预约人数期待+2，实际+" + changetotal1);
            Preconditions.checkArgument(changelist == 1, "小程序预约后，列表数期待+2，实际+" + changelist);

            Preconditions.checkArgument(changetotal2 == 2, "小程序取消预约后，今日预约人数期待-2，实际+" + changetoday2);
            Preconditions.checkArgument(changetoday2 == 2, "小程序取消预约后，全部预约人数期待-2，实际+" + changetotal2);
            Preconditions.checkArgument(total2 == total1, "小程序取消预约后，列表数期待不变，实际+" + changetotal2);


        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("小程序1个人2个车各预约1次，校验预约维修页面统计数据");

        }
    }

    // 小程序预约维修，售后顾问点击接待按钮yes/不点击接待no
    public String[] repair(String date, Long carid, String ifreception) throws Exception {
        String a[] = new String[3]; //0销售登陆账号 1预约记录id 2 接待记录id
        //小程序登陆
        crm.appletLoginToken(EnumAppletCode.GLY.getCode());
        //预约使用参数
        String appointment_time = "09:30";
        String description = "自动化故障说明" + System.currentTimeMillis();
        long timelist = crm.timeList("REPAIR", date).getJSONArray("list").getJSONObject(0).getLong("id");
        JSONObject obj = crm.appointmentRepair(carid, customer_name, customer_phone_number, date, appointment_time, description, timelist);
        Long repair_id = obj.getLong("appointment_id");
        a[1] = Long.toString(repair_id);
        String salephone = obj.getString("sale_phone");
        crm.login(by_name, pwd);
        if (ifreception.equals("yes")) {
            //维修顾问登陆，点击接待按钮
            crm.login(wx_name, pwd);
            a[2] = "未点击接待按钮";
        }
        return a;
    }


    //预约保养
    public String[] maintain(String date, Long carid, String ifreception) throws Exception {
        String a[] = new String[3]; //0销售登陆账号 1预约记录id 2 接待记录id
        //小程序登陆
        crm.appletLoginToken(EnumAppletCode.GLY.getCode());
        String appointment_time = "09:00";
        long timelist = crm.timeList("REPAIR", date).getJSONArray("list").getJSONObject(0).getLong("id");
        JSONObject obj = crm.appointmentMaintain(carid, customer_name, customer_phone_number, date, appointment_time, timelist);
        Long maintain_id = obj.getLong("appointment_id");
        a[1] = Long.toString(maintain_id);
        //保养顾问登陆，点击接待按钮
        crm.login(by_name, pwd);
        if (ifreception.equals("yes")) {
            Long after_record_id = crm.reception_customer(maintain_id).getLong("after_record_id");
            a[2] = Long.toString(after_record_id);
        } else {
            a[2] = "未点击接待按钮";
        }
        return a;

    }

    //获取图片base64
    public static String getImgStr(String imgFile) { //图片转base64
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Objects.requireNonNull(Base64.encodeBase64(data)));
    }


    /**
     * @author : guoliya
     * @date :  2020/8/24
     */


    //销售接待--接待率=今日接待/今日线索*100
    @Test
    public void receptionRatio() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(qt_name, pwd);
            JSONObject response = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
            //今日接待数量
            double todayReptionNum = response.getInteger("today_reception_num");
            //今日线索数量
            double allCustomerNum = response.getInteger("all_customer_num");
            //接待率
            String todayReceptionRatio = response.getString("today_reception_ratio");
            String result = null;
            if (allCustomerNum == 0 && todayReptionNum != 0) {
                result = "100";
                Preconditions.checkArgument(todayReceptionRatio.equals(result), "接待率：" + todayReceptionRatio + "  " + "今日接待/今日线索*100：" + result);
            } else if (todayReptionNum == 0 && allCustomerNum == 0 || allCustomerNum != 0 && todayReptionNum == 0) {
                result = "0";
                Preconditions.checkArgument(todayReceptionRatio.equals(result), "接待率：" + todayReceptionRatio + "  " + "今日接待/今日线索*100：" + result);
            } else {
                result = new DecimalFormat("#").format(Math.round(todayReptionNum / allCustomerNum * 100));
                Preconditions.checkArgument(todayReceptionRatio.equals(result), "接待率：" + todayReceptionRatio + "  " + "今日接待/今日线索*100：" + result);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售接待--接待率=今日接待/今日线索*100");
        }
    }

    //    //销售接待--试驾率=试驾/接待*100
//
    @Test
    public void driveatio() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(qt_name, pwd);
            JSONObject response = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
            //今日接待数量
            double todayReptionNum = response.getInteger("today_reception_num");
            //今日试驾数量R
            double todayestDriveNum = response.getInteger("today_test_drive_num");
            //试驾率
            String todayRestDriveatio = response.getString("today_test_drive_ratio");
            String result = null;
            if (todayReptionNum == 0 && todayestDriveNum != 0) {
                result = "100";
                Preconditions.checkArgument(todayRestDriveatio.equals(result), "试驾率：" + todayRestDriveatio + "  " + "试驾/接待：" + result);
            } else if (todayReptionNum == 0 && todayestDriveNum == 0 || todayReptionNum != 0 && todayestDriveNum == 0) {
                result = "0";
                Preconditions.checkArgument(todayRestDriveatio.equals(result), "试驾率：" + todayRestDriveatio + "  " + "试驾/接待：" + result);
            } else {
                result = new DecimalFormat("#").format(Math.round(todayestDriveNum / todayReptionNum * 100));
                Preconditions.checkArgument(todayRestDriveatio.equals(result), "试驾率：" + todayRestDriveatio + "  " + "试驾/接待*100：" + result);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售接待--试驾率=试驾/接待*100");
        }
    }

    //    //销售接待--订单率=订单/试驾*100
    @Test
    public void buyCaratio() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(qt_name, pwd);
            JSONObject response = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
            //今日订车数量
            double todayBuyCarNum = response.getInteger("today_buy_car_num");
            //今日试驾数量
            double todayestDriveNum = response.getInteger("today_test_drive_num");
            //订车率
            String todayBuyCaratio = response.getString("today_buy_car_ratio");
            String result = null;
            if (todayestDriveNum == 0 && todayBuyCarNum != 0) {
                result = "100";
                Preconditions.checkArgument(todayBuyCaratio.equals(result), "订单率：" + todayBuyCaratio + "  " + "订单/试驾*100：" + result);
            } else if (todayestDriveNum == 0 && todayBuyCarNum == 0 || todayestDriveNum != 0 && todayBuyCarNum == 0) {
                result = "0";
                Preconditions.checkArgument(todayBuyCaratio.equals(result), "订单率：" + todayBuyCaratio + "  " + "订单/试驾：" + result);
            } else {
                result = new DecimalFormat("#").format(Math.round(todayBuyCarNum / todayestDriveNum * 100));
                Preconditions.checkArgument(todayBuyCaratio.equals(result), "订单率：" + todayBuyCaratio + "  " + "订单/试驾*100：" + result);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售接待--订单率=订单/试驾*100");
        }
    }

    //    //销售接待--交车率=交车/订单*100
    @Test
    public void deliverCarRatio() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            crm.login(qt_name, pwd);
            JSONObject response = crm.receptionPage(1, 2, date, date);
            //今日订车数量
            double todayBuyCarNum = response.getInteger("today_buy_car_num");
            //今日交车数量
            double todayeliverCarNum = response.getInteger("today_deliver_car_num");
            //交车率
            String todayDeliverCarRatio = response.getString("today_deliver_car_ratio");
            String result = null;
            if (todayBuyCarNum == 0 && todayeliverCarNum != 0) {
                result = "100";
                Preconditions.checkArgument(todayDeliverCarRatio.equals(result), "交车率：" + todayDeliverCarRatio + "  " + "交车/订单*100：" + result);
            } else if (todayBuyCarNum == 0 && todayeliverCarNum == 0 || todayBuyCarNum != 0 && todayeliverCarNum == 0) {
                result = "0";
                Preconditions.checkArgument(todayDeliverCarRatio.equals(result), "交车率：" + todayDeliverCarRatio + "  " + "交车/订单*100：" + result);
            } else {
                result = new DecimalFormat("#").format(Math.round(todayeliverCarNum / todayBuyCarNum * 100));
                Preconditions.checkArgument(todayDeliverCarRatio.equals(result), "交车率：" + todayDeliverCarRatio + "  " + "交车/订单*100：" + result);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("交车率=交车/订单*100");
        }
    }

    //销售接待--今天销售创建线索->今日线索+1
    @Test
    public void xsCreateLine() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        EnumCarModel car = EnumCarModel.PANAMERA_TURBO_S_E_HYBRID_SPORT_TURISMO;
        EnumCustomerInfo customerInfo = EnumCustomerInfo.CUSTOMER_5;
        try {
            crm.login(zjl_name, pwd);
            int customerNum = crm.receptionPage("", date, date, "1", "10").getInteger("all_customer_num");
            //创建线索
            String phone = new PcData().getDistinctPhone();
            crm.customerCreate(customerInfo.getName(), "2", phone, car.getModelId(), car.getStyleId(), customerInfo.getRemark());
            int customerNum1 = crm.receptionPage("", date, date, "1", "10").getInteger("all_customer_num");
            Preconditions.checkArgument(customerNum1 == customerNum + 1, "没有创建线索之前数据为：" + customerNum + "  创建线索之后数据为：" + customerNum1);
            crm.login(zjl_name, pwd);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今天销售创建线索->今日线索+1");
        }
    }

    //销售接待--今天销售创建客户->今日线索+1
//    @Test
//    public void xsCreateCustomer() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //前台登陆
//            crm.login(qt_name, pwd);
//            JSONObject response = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
//            int allCustomerNum = response.getInteger("all_customer_num");
//            Long customerid = -1L;
//            //获取当前空闲第一位销售id
//            String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
//            String userLoginName = "";
//            JSONArray userlist = crm.userPage(1, 100).getJSONArray("list");
//            for (int i = 0; i < userlist.size(); i++) {
//                JSONObject obj = userlist.getJSONObject(i);
//                if (obj.getString("user_id").equals(sale_id)) {
//                    userLoginName = obj.getString("user_login_name");
//                }
//            }
//            //创建接待
//            crm.creatReception("FIRST_VISIT");
//            //销售登陆，获取当前接待id
//            crm.login(userLoginName, pwd);
//            customerid = crm.userInfService().getLong("customer_id");
//            //创建某级客户
//            String name = "翠花";
//            String phone = "13388115577";
//            crm.finishReception(customerid, 3, name, phone, "自动化---------创建----------客户");
//            crm.login(qt_name, pwd);
//            JSONObject response1 = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
//            int allCustomerNum1 = response1.getInteger("all_customer_num");
//            Preconditions.checkArgument(allCustomerNum1 == allCustomerNum + 1, "没有创建线索之前数据为：" + allCustomerNum1 + "  创建线索之后数据为：" + allCustomerNum);
//        } catch (AssertionError | Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            crm.login(zjl_name, pwd);
//            JSONObject responZjl = crm.customerListPC("13388115577", 1, 2 << 100);
//            long customerId = responZjl.getJSONArray("list").getJSONObject(0).getInteger("customer_id");
//            crm.customerDeletePC(customerId);
//            saveData("今天销售创建客户->今日线索+1");
//        }
//    }


    //销售接待--今日试驾>=所有销售 【客户管理-我的试驾】今日试驾之和
    @Test()
    public void TodayDriveSum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            JSONObject response = crm.receptionPage(1, 10, "", "");
            int todayTestDrive = response.getInteger("today_test_drive_num");
            int total = crm.userUserPage(1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            int max = 0;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.userUserPage(i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("role_name").equals("销售顾问")
                            || list.getJSONObject(j).getString("role_name").equals("DCC销售顾问")) {
                        String userLoginName = list.getJSONObject(j).getString("user_login_name");
                        crm.login(userLoginName, pwd);
                        int todayDeliverCarTotal = crm.driverTotal().getInteger("today_test_drive_total");
                        max += todayDeliverCarTotal;
                        CommonUtil.valueView(max);
                        CommonUtil.log("分割线");
                    }
                }
            }
            CommonUtil.valueView(todayTestDrive, max);
            Preconditions.checkArgument(todayTestDrive >= max, "今日试驾的数量为：" + todayTestDrive + "  " + "：各销售试驾累计为" + max);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日试驾>=所有销售 【客户管理-我的试驾】今日试驾之和");
        }
    }


    //销售接待--今日订车=所有销售【客户管理-我的接待】今日订单之和
    @Test()
    public void TodayDuySum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            JSONObject response = crm.receptionPage(1, 2, "", "");
            int todayBuyCarNum = response.getInteger("today_buy_car_num");
            int total = crm.userUserPage(1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            int max = 0;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.userUserPage(i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("role_name").equals("销售顾问")
                            || list.getJSONObject(j).getString("role_name").equals("DCC销售顾问")) {
                        String userLoginName = list.getJSONObject(j).getString("user_login_name");
                        crm.login(userLoginName, pwd);
                        int todayDeliverCarTotal = crm.customerReceptionTotalInfo().getInteger("today_order");
                        max += todayDeliverCarTotal;
                        CommonUtil.valueView(max);
                        CommonUtil.log("分割线");
                    }
                }
            }
            CommonUtil.valueView(todayBuyCarNum, max);
            Preconditions.checkArgument(todayBuyCarNum >= max, "今日订车的数量为：" + todayBuyCarNum + "  " + "：各销售订车累计为" + max);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日试驾>=所有销售 【客户管理-我的试驾】今日试驾之和");
        }
    }

    //销售接待--今日交车>=所有销售【客户管理-我的交车】今日交车 之和
    @Test()
    public void DeliverCarSum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            JSONObject response = crm.receptionPage(1, 10, "", "");
            int todayDeliverCarNum = response.getInteger("today_deliver_car_num");
            int total = crm.userUserPage(1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            int max = 0;
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.userUserPage(i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("role_name").equals("销售顾问")
                            || list.getJSONObject(j).getString("role_name").equals("DCC销售顾问")) {
                        String userLoginName = list.getJSONObject(j).getString("user_login_name");
                        crm.login(userLoginName, pwd);
                        int todayDeliverCarTotal = crm.deliverCarTotal().getInteger("today_deliver_car_total");
                        max += todayDeliverCarTotal;
                        CommonUtil.valueView(max);
                        CommonUtil.log("分割线");
                    }
                }
            }
            CommonUtil.valueView(todayDeliverCarNum, max);
            Preconditions.checkArgument(todayDeliverCarNum >= max, "今日交车的数量为：" + todayDeliverCarNum + "  " + "：各销售交车累计为" + max);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售接待--今日交车>=所有销售【客户管理-我的交车】今日交车 之和");
        }
    }

    //接待列表导出
    @Test
    public void receptionExport() {
        try {
            crm.receptionExport();
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("接待列表导出");
        }
    }

    //APP销售接待列表数==PC销售接待列表数
    @Test
    public void receptionNumber() {
        String date = DateTimeUtil.getFormat(new Date());
        try {
            crm.login(qt_name, pwd);
            int total = crm.receptionPage("", date, date, "1", "10").getInteger("total");
            int total1 = crm.receptionPage("", "", "PRE_SALES", 1, 10).getInteger("total");
            int pcList = 0;
            int s = CommonUtil.getTurningPage(total1, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.receptionPage("", "", "", String.valueOf(i), "10").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("reception_date").equals(date)) {
                        pcList++;
                    }
                }
            }
            Preconditions.checkArgument(total == pcList, "APP列表数：" + total + "  " + "PC列表数" + pcList);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP销售接待列表数==PC销售接待列表数");
        }
    }

    //V2.1活动报名-是否完成，未完成高亮，已完成不高亮
    @Test()
    public void activityTaskPage() {
        try {
            crm.login(wx_name, pwd);
            int total = crm.activityTaskPage(1, 10).getInteger("total");
            if (total > 0) {
                for (int i = 1; i < total; i++) {
                    JSONArray list = crm.activityTaskPage(i, 100).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        if (list.getJSONObject(j).getString("activity_task_status_name").equals("未完成")) {
                            boolean isHighLight = list.getJSONObject(j).getBoolean("is_highlight");
                            CommonUtil.valueView(isHighLight);
                            Preconditions.checkArgument(isHighLight, "报名活动未完成的不高亮");
                        } else if (list.getJSONObject(j).getString("activity_task_status_name").equals("已完成")) {
                            boolean isHighLight = list.getJSONObject(j).getBoolean("is_highlight");
                            CommonUtil.valueView(isHighLight);
                            Preconditions.checkArgument(!isHighLight, "报名活动未完成的不高亮");
                        }
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("V2.1活动报名-是否完成，未完成高亮，已完成不高亮");
        }
    }

    //活动报名-当前日期>=开始日期，填写报名置灰，当前日期<开始日期，，填写报名，高亮可点击
    @Test()
    public void activityIsEdit() {
        String date = DateTimeUtil.getFormat(new Date());
        try {
            crm.login(wx_name, pwd);
            int total = crm.activityTaskPage(1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.activityTaskPage(i, 10).getJSONArray("list");
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
            appendFailreason(e.toString());
        } finally {
            saveData("app活动开始日期>当前日期，活动可以报名，填写报名，高亮可点击");
        }
    }

    //活动报名-添加报名人信息并删除---可优化，不把活动id写死，写一个判断：活动是否可以报名，可以则获取该活动id(包括下边的case一起改)----郭丽雅
//    @Test(enabled = true)
//    public void registeredCustomer() {
//        try {
//            crm.login(wx_name, pwd);
//            String phone = "17737771883";
//
//            crm.registeredCustomer(228L, "自动化啦", phone);
//            JSONObject response = crm.TaskInfo(228L, phone);
//            JSONArray list = response.getJSONArray("customer_list");
//            if (list != null) {
//                for (int i = 0; i < list.size(); i++) {
//                    String phone1 = list.getJSONObject(i).getString("customer_phone_number");
//                    if (phone1.equals(phone)) {
//                        String cumId = list.getJSONObject(i).getString("customer_id");
//                        JSONObject la = crm.deleteCustomerX("228", cumId);
//                    }
//                }
//            }
//        } catch (Exception | AssertionError e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("活动报名-添加报名人信息并删除");
//        }
//    }

    //活动报名-添加报名人信息--异常--手机号12位
    @Test()
    public void registeredCustomer2() {
        try {
            crm.login(wx_name, pwd);
            JSONArray list = crm.activityTaskPage(1, 100).getJSONArray("list");
            int activityTaskId = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getBoolean("is_edit")) {
                    activityTaskId = list.getJSONObject(i).getInteger("activity_task_id");
                }
            }
            String phone = "177377711311";
            String message = crm.registeredCustomer1((long) activityTaskId, "哈哈哈", phone).getString("message");
            Preconditions.checkArgument(message.equals("请输入有效手机号码"), "12位手机号码验证");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("活动报名-添加报名人信息--异常--手机号12位");
        }
    }

    //活动报名-添加报名人信息--异常--手机号11位+数字+标点+中文
    @Test(enabled = true)
    public void registeredCustomer3() {
        try {
            crm.login(wx_name, pwd);
            String phone = "1773@@拉拉**";
            String message = crm.registeredCustomer1(228L, "哈哈哈", phone).getString("message");
            Preconditions.checkArgument(message.equals("请输入有效手机号码"), "12位手机号码验证");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("活动报名-添加报名人信息--异常--手机号11位+数字+标点+中文");
        }
    }

    //活动报名-添加报名人信息--异常--姓名12位中文
    @Test(enabled = true)
    public void registeredCustomer4() {
        try {
            crm.login(wx_name, pwd);
            String phone = "17737771311";
            String message1 = crm.registeredCustomer1(228L, "哈哈哈哈哈哈哈哈哈哈哈哈", phone).getString("message");
            Preconditions.checkArgument(message1.equals("客户名称长度不能超过十个字"), "12位姓名验证");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("活动报名-添加报名人信息--异常--姓名12位");
        }
    }

    //活动报名-添加报名人信息--异常--姓名12位英文+数字+标点符号
    @Test()
    public void registeredCustomer5() {
        try {
            crm.login(wx_name, pwd);
            String phone = "17737771311";
            String message1 = crm.registeredCustomer1(228L, "**%*****", phone).getString("message");
            System.out.println("message1---- " + message1);
            Preconditions.checkArgument(message1.equals("您只能输入长度不超过10的汉字"), "12位姓名验证");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("活动报名-添加报名人信息--异常--姓名12位英文+数字+标点符号");
        }
    }

    //报名活动-添加版名人数在0到50之间
//    @Test()
//    public void activityTaskInfo() {
//        try {
//            crm.login(wx_name, pwd);
//            JSONArray list = crm.activityTaskPage(1, 10).getJSONArray("list");
//            int activityId = 0;
//            for (int i = 0; i < list.size(); i++) {
//                if (list.getJSONObject(i).getBoolean("is_edit")) {
//                    activityId = list.getJSONObject(i).getInteger("activity_task_id");
//                }
//            }
//            if (activityId != 0) {
//                JSONArray list1 = crm.activityTaskInfo(String.valueOf(activityId)).getJSONArray("customer_list");
//                Preconditions.checkArgument(list1.size() <= 50, "活动报名超过50人或少于0人");
//            }
//        } catch (Exception | AssertionError e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("报名活动-添加版名人数在0到50之间");
//        }
//    }

    //删除销售顾问
    public void delectXS(String phone) {
        try {
            crm.login("baoshijie", pwd);
            JSONObject response1 = crm.userPage(1, 10);
            int pages = response1.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONObject response2 = crm.userPage(page, 10);
                JSONArray list = response2.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String userPhone = list.getJSONObject(i).getString("user_phone");
                    if (userPhone.equals(phone)) {
                        String userId = response2.getJSONArray("list").getJSONObject(i).getString("user_id");
                        System.out.println("-----userId----" + userId);
                        crm.userDel(userId);
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        }
    }

    //修改密码-填写全部必填项：原密码、新密码、新密码确认----现在逻辑不通，需要完善逻辑，需要重新修改--郭丽雅
    @Test(enabled = false)
    public void modifyPassword() {
        try {
            String phone = "13378909876";
            //确认销售顾问账号是否存在，如果存在-删除
            delectXS(phone);
            //创建销售顾问账号
            crm.login("baoshijie", pwd);
            String userName = "Max哈";
            String userLoginName = "xsgwmax";
            String passwd = "e10adc3949ba59abbe56e057f20f883e";
            int roleId = 13;
            JSONObject response = crm.addUser(userName, userLoginName, phone, passwd, roleId, "", "");
            //修改密码
            crm.login("xsgwmax", pwd);
            String oldPassword = "e10adc3949ba59abbe56e057f20f883e";
            String newPassword = "b7769b49af4953a5093a54e3d313c1c5";
            crm.modifyPasswordJk(oldPassword, newPassword);
            //删除销售顾问账号
            delectXS(phone);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("修改密码-填写全部必填项：原密码、新密码、新密码确认");
        }
    }

    //修改密码-填写全部必填项：新密码输入长度21位--1234!@#$我们111111111111111qq----现在逻辑不通，需要完善逻辑，需要重新修改--郭丽雅
    @Test(enabled = false)
    public void modifyPassword1() {
        try {
            String phone = "13378909876";
            //确认销售顾问账号是否存在，如果存在-删除
            delectXS(phone);
            //创建销售顾问账号
            crm.login("baoshijie", pwd);
            String userName = "Max哈";
            String userLoginName = "xsgwmax";
            String passwd = "e10adc3949ba59abbe56e057f20f883e";
            int roleId = 13;
            JSONObject response = crm.addUser(userName, userLoginName, phone, passwd, roleId, "", "");
            //修改密码
            crm.login("xsgwmax", pwd);
            String oldPassword = "e10adc3949ba59abbe56e057f20f883e";
            String newPassword = "860591a9-d852-4c93-8f34-0232d559bc04";
            String message = crm.modifyPasswordJk(oldPassword, newPassword).getString("message");
            Preconditions.checkArgument(message.equals("系统异常"), "密码21位且存在中文+符号");
            //删除销售顾问账号
            delectXS(phone);

        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("修改密码-填写全部必填项：新密码输入长度21位--1234!@#$我们111111111111111qq");
        }
    }

    //-----------------------------------------------------售后----------------------------------------------------------

    @Test(description = "售后--我的接待--本月接待售后车辆>=今日接待售后车辆&&本月完成维修车辆>=今日完成维修车辆")
    public void afterSale_reception_data_1() {
        try {
            crm.login(by_name, pwd);
            JSONObject response = crm.afterSaleCustList("", "", "", 1, 10);
            int monthReceptionCar = response.getInteger("month_reception_car");
            int monthRepairedCar = response.getInteger("month_repaired_car");
            int todayReceptionCar = response.getInteger("today_reception_car");
            int todayRepairedCar = response.getInteger("today_repaired_car");
            CommonUtil.valueView(monthReceptionCar, monthRepairedCar, todayReceptionCar, todayRepairedCar);
            Preconditions.checkArgument(monthReceptionCar >= todayReceptionCar && monthRepairedCar >= todayRepairedCar, "本月接待售后车辆>=今日接待售后车辆&&本月完成维修车辆>=今日完成维修车辆异常");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后--我的接待--本月接待售后车辆>=今日接待售后车辆&&本月完成维修车辆>=今日完成维修车辆");
        }
    }

    @Test(description = "售后--我的接待--今日接待售后车辆=今天筛选，列表数车牌去重")
    public void afterSale_reception_data_2() {
        try {
            crm.login(by_name, pwd);
            String date = DateTimeUtil.getFormat(new Date());
            IScene scene = ReceptionAfterCustomerListScene.builder().searchDateStart(date).searchDateEnd(date).build();
            JSONObject response = crm.invokeApi(scene);
            int total = response.getInteger("total");
            int todayReceptionCar = response.getInteger("today_reception_car");
            Set<String> set = new HashSet<>();
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                IScene scene1 = ReceptionAfterCustomerListScene.builder().page(i).size(100).searchDateStart(date).searchDateEnd(date).build();
                JSONArray list = crm.invokeApi(scene1).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("reception_status_name").equals("已完成")) {
                        set.add(crm.invokeApi(scene1).getString("plate_number"));
                    }
                }
            }
            CommonUtil.valueView(todayReceptionCar, set.size());
            Preconditions.checkArgument(todayReceptionCar == set.size(), by_name + "今日接待售后车辆数：" + todayReceptionCar + "列表数：" + set.size());
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的接待-本月接待售后车辆>=今日接待售后车辆&&本月完成维修车辆>=今日完成维修车辆");
        }
    }

    @Test(description = "接待日期为今天的记录，确认交车，今日完成维修车辆+1&&本月完成维修车辆+1", enabled = false)
    public void afterSale_reception_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(zjl);
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
            appendFailreason(e.toString());
        }
    }

    @Test(description = "接待日期为今天的记录，确认交车，列表总条数不变，接待状态=已完成+1&&接待状态=维修中-1")
    public void afterSale_reception_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(zjl);
            int maintainNum = method.getStatusNum("维修中");
            int completeNum = method.getStatusNum("已完成");
            int listSize = maintainNum + completeNum;
            CommonUtil.valueView(maintainNum, completeNum);
            int afterRecordId = method.getAfterRecordId(false, "维修中", 30);
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
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("接待日期为今天的记录，确认交车，列表总条数不变，接待状态=已完成+1&&接待状态=维修中-1");
        }
    }

    @Test(description = "接待日期为昨天的记录，确认交车，今日完成维修车辆+1&&本月完成维修车辆+1", enabled = false)
    public void afterSale_reception_data_5() {
        logger.logCaseStart(caseResult.getCaseName());

    }

    @Test(description = "app展示信息与小程序预约时信息保持一致", enabled = false)
    public void afterSale_reception_data_6() {
        logger.logCaseStart(caseResult.getCaseName());

    }

    @Test(description = "app预约保养列表数与pc预约保养列表数一致，app预约维修列表数与pc预约维修列表数一致")
    public void afterSale_reception_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(zjl_name, pwd);
            IScene scene = OrderMaintainPageScene.builder().build();
            int pcMaintainTotal = crm.invokeApi(scene).getInteger("total");
            IScene scene1 = OrderRepairPageScene.builder().build();
            int pcRepairTotal = crm.invokeApi(scene1).getInteger("total");
            int appMaintainTotal = crm.mainAppointmentList(1, 10).getInteger("total");
            int appRepairTotal = crm.repairAppointmentlist().getInteger("total");
            CommonUtil.valueView(pcMaintainTotal, appMaintainTotal, pcRepairTotal, appRepairTotal);
            Preconditions.checkArgument(pcMaintainTotal == appMaintainTotal, "app预约保养总数为：" + appMaintainTotal + "pc预约保养总数为：" + pcMaintainTotal);
            Preconditions.checkArgument(pcRepairTotal == appRepairTotal, "app预约维修总数为" + appRepairTotal + "pc预约维修总数为：" + pcRepairTotal);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("app预约保养列表数与pc预约保养列表数一致，app预约维修列表数与pc预约维修列表数一致");
        }
    }

    @Test(description = "预约保养/维修页点击接待按钮（客户类型=新客）--接待页列表+1")
    public void afterSale_reception_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(zjl);
            IScene scene = ReceptionAfterCustomerListScene.builder().build();
            int total = crm.invokeApi(scene).getInteger("total");
            String date = DateTimeUtil.addDayFormat(new Date(), +1);
            method.appointment(EnumAppointmentType.MAINTAIN, date);
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
            appendFailreason(e.toString());
        } finally {
            saveData("预约保养/维修页点击接待按钮（客户类型=新客）--接待页列表+1");
        }
    }

    @Test(description = "总经理本月接待售后车辆>=各个顾问本月接待售后车辆之和")
    public void afterSale_reception_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyReception("month_reception_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理本月接待售后车辆为   " + zjl_name + "各个顾问本月接待售后车辆之和  " + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("总经理本月接待售后车辆>=各个顾问本月接待售后车辆之和");
        }
    }

    @Test(description = "总经理本月完成维修车辆>=各个顾问本月完成维修车辆之和")
    public void afterSale_reception_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyReception("month_repaired_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理本月完成维修车辆为   " + zjl_name + "各个顾问本月完成维修车辆之和  " + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("总经理本月完成维修车辆>=各个顾问本月完成维修车辆之和");
        }
    }

    @Test(description = "总经理今日接待售后车辆>=各个顾问今日接待售后车辆之和")
    public void afterSale_reception_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyReception("today_reception_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理今日接待售后车辆   " + zjl_name + "各个顾问今日接待售后车辆之和  " + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("总经理今日接待售后车辆>=各个顾问今日接待售后车辆之和");
        }
    }

    @Test(description = "总经理今日完成维修车辆>=各个顾问今日完成维修车辆之和")
    public void afterSale_reception_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyReception("today_repaired_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理今日完成维修车辆   " + zjl_name + "各个顾问今日完成维修车辆之和  " + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("总经理今日完成维修车辆>=各个顾问今日完成维修车辆之和");
        }
    }

    private void compareAfterSaleMyReception(String type) {
        crm.login(zjl_name, pwd);
        List<Map<String, String>> list = method.getSaleList("服务顾问");
        for (Map<String, String> stringStringMap : list) {
            CommonUtil.valueView(stringStringMap.get("userName"));
            if (stringStringMap.get("userName").contains("总经理")) {
                crm.login(stringStringMap.get("account"), pwd);
                IScene scene = ReceptionAfterCustomerListScene.builder().build();
                zjl_num = crm.invokeApi(scene).getInteger(type);
            }
            if (!stringStringMap.get("userName").contains("总经理")) {
                crm.login(stringStringMap.get("account"), pwd);
                IScene scene = ReceptionAfterCustomerListScene.builder().build();
                int num = crm.invokeApi(scene).getInteger(type);
                CommonUtil.valueView(num);
                gw_num += num;
            }
            CommonUtil.log("分割线");
        }
    }

    @Test(description = "售后--我的接待--增加20条备注，展示20条")
    public void afterSale_reception_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            UserUtil.login(zjl);
            JSONArray remarks = new JSONArray();
            String str = EnumCustomerInfo.CUSTOMER_1.getRemark();
            for (int i = 0; i < 20; i++) {
                remarks.add(str);
            }
            int afterRecordId = method.getAfterRecordId(false, 30);
            method.saveReception(String.valueOf(afterRecordId), remarks);
            IScene scene = DetailAfterSaleCustomerScene.builder().afterRecordId(String.valueOf(afterRecordId)).build();
            JSONObject response = crm.invokeApi(scene);
            JSONArray array = response.getJSONArray("remarks");
            String platNumber = response.getString("plate_number");
            Preconditions.checkArgument(array.size() == 20, "车牌号为：" + platNumber + "的客户，备注列表展示条数为：" + array.size());
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
        } finally {
            saveData("售后--我的接待--增加20条备注，展示20条");
        }
    }

    @Test(description = "售后--客户管理--全部车辆>=本月新增车辆")
    public void afterSale_customerManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AfterSaleCustomerListScene.builder().build();
            JSONObject response = crm.invokeApi(scene);
            int monthReceptionCar = response.getInteger("month_reception_car");
            int totalReceptionCar = response.getInteger("total_reception_car");
            CommonUtil.valueView(monthReceptionCar, totalReceptionCar);
            Preconditions.checkArgument(totalReceptionCar >= monthReceptionCar, "全部车辆为：" + totalReceptionCar + "本月新增车辆为：" + monthReceptionCar);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后--客户管理--全部车辆>=本月新增车辆");
        }
    }

    @Test(description = "售后--客户管理--本月新增车辆>=今日新增车辆")
    public void afterSale_customerManager_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = AfterSaleCustomerListScene.builder().build();
            JSONObject response = crm.invokeApi(scene);
            int monthReceptionCar = response.getInteger("month_reception_car");
            int todayNewCar = response.getInteger("today_new_car");
            CommonUtil.valueView(monthReceptionCar, todayNewCar);
            Preconditions.checkArgument(todayNewCar <= monthReceptionCar, "今日新增车辆为：" + todayNewCar + "本月新增车辆为：" + monthReceptionCar);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后--客户管理--本月新增车辆>=今日新增车辆");
        }
    }

    @Test(description = "售后--客户管理--今日新增车辆=今日筛选，车牌号去重")
    public void afterSale_customerManager_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            JSONObject response = crm.afterSaleCustomerList("", date, date, 1, 10);
            int todayNewCar = response.getInteger("today_new_car");
            Set<String> set = new HashSet<>();
            JSONArray list = response.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String plateNumber = list.getJSONObject(i).getString("plate_number");
                CommonUtil.valueView(plateNumber);
                set.add(plateNumber);
            }
            CommonUtil.valueView(todayNewCar, set.size());
            Preconditions.checkArgument(todayNewCar == set.size(), "今日新增车辆为 " + todayNewCar + "列表数车牌号去重 " + set.size());
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-今日新增车辆=今日筛选，车牌号去重");
        }
    }

    @Test(description = "售后-客户管理-本月新增车辆<=【我的接待】本月接待售后车辆")
    public void afterSale_customerManager_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ReceptionAfterCustomerListScene.builder().build();
            int monthReceptionCar = crm.invokeApi(scene).getInteger("month_reception_car");
            IScene scene1 = AfterSaleCustomerListScene.builder().build();
            int monthReceptionCar1 = crm.invokeApi(scene1).getInteger("month_reception_car");
            CommonUtil.valueView(monthReceptionCar, monthReceptionCar1);
            Preconditions.checkArgument(monthReceptionCar >= monthReceptionCar1, "本月新增车辆：" + monthReceptionCar1 + "本月接待售后车辆：" + monthReceptionCar);
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后-客户管理-本月新增车辆<=【我的接待】本月接待售后车辆");
        }
    }

    @Test(description = "售后-客户管理-今日新增车辆<=【我的接待】今日接待售后车辆")
    public void afterSale_customerManager_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ReceptionAfterCustomerListScene.builder().build();
            int todayReceptionCar = crm.invokeApi(scene).getInteger("today_reception_car");
            IScene scene1 = AfterSaleCustomerListScene.builder().build();
            int todayNewCar = crm.invokeApi(scene1).getInteger("today_new_car");
            CommonUtil.valueView(todayNewCar, todayReceptionCar);
            Preconditions.checkArgument(todayReceptionCar >= todayNewCar, "今日新增车辆：" + todayNewCar + "今日接待售后车辆：" + todayReceptionCar);
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后-客户管理-今日新增车辆<=【我的接待】今日接待售后车辆");
        }
    }

    @Test(description = "售后--客户管理--总经理的全部车辆>=各个顾问的全部车辆之和")
    public void afterSale_customerManager_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyCustomer("total_reception_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理的全部车辆为：" + zjl_num + "各顾问数量和为：" + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后--客户管理--总经理的全部车辆=各个顾问的全部车辆之和");
        }
    }

    @Test(description = "售后--客户管理--总经理的全部车辆=各个顾问的全部车辆之和")
    public void afterSale_customerManager_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyCustomer("total_reception_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理的全部车辆为：" + zjl_num + "各顾问数量和为：" + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后--客户管理--总经理的全部车辆=各个顾问的全部车辆之和");
        }
    }

    @Test(description = "售后--客户管理--总经理的本月新增>=各个顾问的本月新增之和")
    public void afterSale_customerManager_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyCustomer("month_reception_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理的本月新增车辆为：" + zjl_num + "各顾问数量和为：" + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后--客户管理--总经理的本月新增>=各个顾问的本月新增之和");
        }
    }

    @Test(description = "售后--客户管理--总经理的今日新增车辆>=各个顾问的今日新增之和")
    public void afterSale_customerManager_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            compareAfterSaleMyCustomer("today_new_car");
            CommonUtil.valueView(zjl_num, gw_num);
            Preconditions.checkArgument(zjl_num >= gw_num, "总经理的今日新增车辆为：" + zjl_num + "各顾问数量和为：" + gw_num);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后--客户管理--总经理的今日新增车辆>=各个顾问的今日新增之和");
        }
    }

    private void compareAfterSaleMyCustomer(String type) {
        List<Map<String, String>> list = method.getSaleList("服务顾问");
        for (Map<String, String> map : list) {
            CommonUtil.valueView(map.get("userName"));
            if (map.get("userName").contains("总经理")) {
                crm.login(map.get("account"), pwd);
                IScene scene = AfterSaleCustomerListScene.builder().build();
                zjl_num = crm.invokeApi(scene).getInteger(type);
            }
            if (!map.get("userName").contains("总经理")) {
                crm.login(map.get("account"), pwd);
                IScene scene = AfterSaleCustomerListScene.builder().build();
                int x = crm.invokeApi(scene).getInteger(type);
                CommonUtil.valueView(x);
                gw_num += x;
            }
            CommonUtil.log("分割线");
        }
    }

    @Test(description = "售后--任务管理-全部预约保养>=今日预约保养")
    public void afterSale_taskManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            JSONObject obj = crm.mainAppointmentDriverNum();
            int total = obj.getInteger("appointment_total_number");
            int today = obj.getInteger("appointment_today_number");
            CommonUtil.valueView(total, today);
            Preconditions.checkArgument(total >= today, "全部预约保养：" + total + "今日预约保养：" + today);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后--任务管理-全部预约保养>=今日预约保养");
        }
    }

    @Test(description = "售后--任务管理-全部预约保养<=列表数")
    public void afterSale_taskManager_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = crm.mainAppointmentDriverNum();
            int total = obj.getInteger("appointment_total_number");
            int list = crm.mainAppointmentList().getInteger("total");
            Preconditions.checkArgument(total <= list, "全部预约保养" + total + ">列表数" + list);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后--任务管理-全部预约保养<=列表数");
        }
    }
}



