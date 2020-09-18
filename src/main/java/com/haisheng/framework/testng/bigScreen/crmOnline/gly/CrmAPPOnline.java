package com.haisheng.framework.testng.bigScreen.crmOnline.gly;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.experiment.enumerator.EnumAppletCode;
import com.haisheng.framework.model.experiment.enumerator.EnumShopId;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.crmOnline.commonDsOnline.CustomerInfoOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
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
 * @date :  2020/09/14
 */

public class CrmAPPOnline extends TestCaseCommon implements TestCaseStd {
CrmScenarioUtilOnline crm=CrmScenarioUtilOnline.getInstance();
    CustomerInfoOnline cstm = new CustomerInfoOnline();

    String bsj_name = cstm.gly;//超级管理员--demo
    String xs_name =cstm.lxqsale ;//销售顾问--11
    String by_name = cstm.bygw;//保养顾问--baoyang
    String fwzj_name=cstm.fwzj;//服务总监--fwzj
    String wx_name = cstm.wxgw;//维修顾问--66
    String zjl_name = cstm.zjl;//总经理账号--zjl
    String pwd = cstm.pwd;//密码全部一致
    String demoPassword = cstm.demoPassword;//超级管理员demo账号的密码
    String qt_name = cstm.qt;//前台账号
    //我的车辆信息
    Long my_car_id = 61L;
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


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "gly";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-online-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "CRM 线上 gly");

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.ONLINE_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = EnumShopId.PORSCHE_SHOP_ONLINE.getShopId();
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
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    /**
     * ====================销售顾问======================
     */

//    PC端添加工作安排，工作描述字数=10
    @Test
    public void addScheduledesc10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端添加工作安排，工作描述字数=10");

        }
    }

    /**
     * ====================售后======================
     */

//    APP-售后客户管理：全部车辆<=列表数
    @Ignore
    @Test //ok
    public void custChkallEQList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            //列表总数
            int listtotal = crm.afterSale_custList("", "", "", 1, 1).getInteger("total");
            //统计数据-全部车辆数
            int allcar = crm.afterSale_custTotal().getInteger("all_reception_total");
            Preconditions.checkArgument(allcar <= listtotal, "全部车辆" + listtotal + "!=列表数" + allcar);
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
    public void custChkTotalGTNew() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            JSONObject list = crm.afterSale_custTotal();
            //统计数据-今日接待售后数量
            int todayTotal = list.getInteger("today_reception_total");
            //统计数据-今日新增售后数量
            int todayNew = list.getInteger("today_new_after_sale_customer");

            Preconditions.checkArgument(todayTotal >= todayNew, "今日接待售后数量" + todayTotal + "<今日新增售后数量" + todayNew);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户管理：今日接待售后车辆>=今日新增售后车辆");

        }
    }

    //    APP-售后客户管理：全部车辆>=今日接待售后车辆
    @Test
    public void custChkAllGTTodatTotal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            JSONObject list = crm.afterSale_custTotal();
            //统计数据-今日接待售后数量
            int todayTotal = list.getInteger("today_reception_total");
            //统计数据-全部车辆
            int all = list.getInteger("all_reception_total");

            Preconditions.checkArgument(all >= todayTotal, "全部车辆" + all + "<今日接待售后数量" + todayTotal);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-售后客户管理：全部车辆>=今日接待售后车辆");

        }
    }

    //    APP-售后客户管理：添加21条备注，展示最新20条---小程序鉴权失败
    @Test(enabled = false)
    public void afterSaleAddremarks21() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //小程序预约，售后点击接待按钮，获取接待记录ID和售后登陆账号
            String a[] = repair(dt.getHistoryDate(1), my_car_id, "yes");
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
            crm.afterSale_custList(after_record_id, customer_name, customer_phone_number, customer_phone_number, plate_number, travel_mileage,
                    car_type, maintain_type, maintain_type, service_complete, customer_source, remarks);
            //查看客户信息的备注
            JSONArray remarkList = crm.afterSale_custDetail(after_record_id).getJSONArray("remarks");
            int size = remarkList.size();//备注条数
            Preconditions.checkArgument(size == 20, "备注有{}条" + size);
            for (int i = 0; i < remarkList.size(); i++) {
                JSONObject obj = remarkList.getJSONObject(i);
                String search_remark = obj.getString("remark");
                Preconditions.checkArgument(!search_remark.equals(dt.getHistoryDate(0) + "1Z！@#自动化备注1"), "包含第一条备注");
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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
            String a[] = repair(dt.getHistoryDate(1), my_car_id, "yes");
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
            JSONArray visit_list = crm.afterSale_custDetail(after_record_id).getJSONArray("visit");
            int size = visit_list.size();
            Preconditions.checkArgument(size == 20, "回访记录条数=" + size);
            //查看回访内容
            for (int i = 0; i < size; i++) {
                JSONObject obj = visit_list.getJSONObject(i);
                String comment = obj.getString("comment");
                Preconditions.checkArgument(!comment.equals(dt.getHistoryDate(0) + "1Z！@#自动化回访1"), "包含第一条回访记录");
            }


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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
            int total = obj.getInteger("appointment_total_number");
            int today = obj.getInteger("appointment_today_number");
            Preconditions.checkArgument(total >= today, "全部预约保养" + today + "<今日预约保养" + today);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("全部预约保养>=今日预约保养");

        }
    }

    //APP-全部预约保养<=列表数
    @Test
    public void afterSaleMaintainAllLEList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            JSONObject obj = crm.mainAppointmentDriverNum();
            int total = obj.getInteger("appointment_total_number");

            int list = crm.mainAppointmentlist().getInteger("total");
            Preconditions.checkArgument(total <= list, "全部预约保养" + total + ">列表数" + list);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-全部预约保养<=列表数");

        }
    }

    //  APP-新客的所属顾问=当前登陆账号销售名字
    @Test
    public void afterSaleNewChkRecpname() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(by_name, pwd);
            JSONArray maintain = crm.mainAppointmentlist().getJSONArray("list");
            int size = maintain.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    JSONObject obj = maintain.getJSONObject(i);
                    if (obj.getString("customer_type_name").equals("新客")) {
                        String userName = obj.getString("user_name");
                        Preconditions.checkArgument(userName.equals(by_name), "所属顾问为" + userName);
                    }
                }
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-新客的所属顾问=当前登陆账号销售名字");

        }
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
            int total = crm.mainAppointmentlist().getInteger("total");//列表数
            //小程序预约
            Long id = Long.parseLong(maintain(dt.getHistoryDate(1), my_car_id, "no")[1]);
            //销售登陆查看统计数据
            crm.login(by_name, pwd);
            JSONObject obj1 = crm.mainAppointmentDriverNum();
            int total_after = obj1.getInteger("appointment_total_number");
            int today_after = obj1.getInteger("appointment_today_number");
            int total1 = crm.mainAppointmentlist().getInteger("total");//列表数
            //预约不取消，今日/全部+1
            int changetotal1 = total_after - total_bef;
            int changetoday1 = today_after - today_bef;
            int changelist = total1 - total;
            //小程序取消预约

            crm.appletLoginToken(EnumAppletCode.GLY.getCode());
            crm.cancle(id);
            //销售登陆查看统计数据
            crm.login(by_name, pwd);
            JSONObject obj2 = crm.mainAppointmentDriverNum();
            int total_after1 = obj2.getInteger("appointment_total_number");
            int today_after1 = obj2.getInteger("appointment_today_number");
            int total2 = crm.mainAppointmentlist().getInteger("total");//列表数
            int changetotal2 = total_after - total_after1;
            int changetoday2 = today_after - today_after1;

            Preconditions.checkArgument(changetoday1 == 1, "小程序预约后，今日预约人数期待+1，实际+" + changetoday1);
            Preconditions.checkArgument(changetotal1 == 1, "小程序预约后，全部预约人数期待+1，实际+" + changetotal1);
            Preconditions.checkArgument(changelist == 1, "小程序预约后，列表数期待+1，实际+" + changelist);

            Preconditions.checkArgument(changetotal2 == 1, "小程序取消预约后，今日预约人数期待-1，实际+" + changetoday2);
            Preconditions.checkArgument(changetoday2 == 1, "小程序取消预约后，全部预约人数期待-1，实际+" + changetotal2);
            Preconditions.checkArgument(total2 == total1, "小程序取消预约后，列表数期待不变，实际+" + changetotal2);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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
            int total = crm.mainAppointmentlist().getInteger("total");//列表数
            //小程序预约

            Long id1 = Long.parseLong(maintain(dt.getHistoryDate(1), my_car_id, "no")[1]);
            Long id2 = Long.parseLong(maintain(dt.getHistoryDate(1), my_car_id, "no")[1]);

            JSONObject obj1 = crm.mainAppointmentDriverNum();
            int total_after = obj1.getInteger("appointment_total_number");
            int today_after = obj1.getInteger("appointment_today_number");
            int total1 = crm.mainAppointmentlist().getInteger("total");//列表数
            //预约不取消，今日/全部+1
            int changetotal1 = total_after - total_bef;
            int changetoday1 = today_after - today_bef;
            int changelist = total1 - total;
            //小程序取消1个预约
            crm.cancle(id1);
            JSONObject obj2 = crm.mainAppointmentDriverNum();
            int total_after1 = obj2.getInteger("appointment_total_number");
            int today_after1 = obj2.getInteger("appointment_today_number");
            int total2 = crm.mainAppointmentlist().getInteger("total");//列表数
            int changetotal2 = total_after - total_after1;
            int changetoday2 = today_after - today_after1;

            //小程序取消全部预约
            crm.cancle(id2);
            JSONObject obj3 = crm.mainAppointmentDriverNum();
            int total_after2 = obj3.getInteger("appointment_total_number");
            int today_after2 = obj3.getInteger("appointment_today_number");
            int total3 = crm.mainAppointmentlist().getInteger("total");//列表数
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

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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
            int total = crm.mainAppointmentlist().getInteger("total");//列表数
            //小程序预约
            Long id = Long.parseLong(maintain(dt.getHistoryDate(1), my_car_id, "no")[1]);
            //再加
            // Long id2 = Long.parseLong(maintain(dt.getHistoryDate(1),carid2,"no")[1]);
            JSONObject obj1 = crm.mainAppointmentDriverNum();
            int total_after = obj1.getInteger("appointment_total_number");
            int today_after = obj1.getInteger("appointment_today_number");
            int total1 = crm.mainAppointmentlist().getInteger("total");//列表数
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
            int total2 = crm.mainAppointmentlist().getInteger("total");//列表数
            int changetotal2 = total_after - total_after1;
            int changetoday2 = today_after - today_after1;

            Preconditions.checkArgument(changetoday1 == 2, "小程序预约后，今日预约人数期待+2，实际+" + changetoday1);
            Preconditions.checkArgument(changetotal1 == 2, "小程序预约后，全部预约人数期待+2，实际+" + changetotal1);
            Preconditions.checkArgument(changelist == 1, "小程序预约后，列表数期待+2，实际+" + changelist);

            Preconditions.checkArgument(changetotal2 == 2, "小程序取消预约后，今日预约人数期待-2，实际+" + changetoday2);
            Preconditions.checkArgument(changetoday2 == 2, "小程序取消预约后，全部预约人数期待-2，实际+" + changetotal2);
            Preconditions.checkArgument(total2 == total1, "小程序取消预约后，列表数期待不变，实际+" + changetotal2);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-全部预约维修>=今日预约维修");
        }
    }

    //    APP-全部预约维修<=列表数
    @Test
    public void afterSaleRepairAllLEList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(wx_name, pwd);
            JSONObject obj = crm.repairAppointmentDriverNum();
            int total = obj.getInteger("appointment_total_number");

            int list = crm.repairAppointmentlist().getInteger("total");
            Preconditions.checkArgument(total <= list, "全部预约维修" + total + ">列表数" + list);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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
            JSONObject response=crm.repairAppointmentlist();
            JSONArray maintain = response.getJSONArray("list");
            int pages=response.getInteger("pages");
            int size = maintain.size();
            for(int page=1;page<=pages;page++){
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        JSONObject obj = maintain.getJSONObject(i);
                        if (obj.getString("customer_type_name").equals("新客")&&obj.getString("service_status_name").equals("预约中")) {
                            String userName = obj.getString("user_name");
                            Preconditions.checkArgument(userName.equals(wx_name), "所属顾问为" + userName);
                        }
                    }
                }
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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
            System.out.println("listTotal------"+listTotal);
            Preconditions.checkArgument(total == listTotal, "售后工作管理中我的回访-售后回访中的全部回访" + total + "不等于后工作管理中我的回访-售后回访中的列表条数" + listTotal);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP-全部回访=列表条数");

        }
    }

    //     APP-今日回访=任务日期为今天的条数
    @Test
    public void afterSalesReturnVisit1() {
        logger.logCaseStart(caseResult.getCaseName());
        try{
            crm.login(by_name, pwd);
            JSONObject obj1 = crm.afterSale_VisitRecordList(1, 10, "", dt.getHistoryDate(0), dt.getHistoryDate(0));
            Integer total = obj1.getInteger("total");
            int pages1 = obj1.getInteger("pages");
            Integer todayViNum = obj1.getInteger("today_return_visit_number");//获取今日回访条数
            int todayListTotal = 0;
            for(int i=1;i<=pages1;i++){
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
            Preconditions.checkArgument(flag == true, "全部回访" + total + "大于今日回访" + todayListTotal);
            Preconditions.checkArgument(todayViNum == todayListTotal, "售后工作管理中我的回访-售后回访中的今日回访" + todayViNum + "不等于后工作管理中我的回访-售后回访中任务日期为今天的条数" + todayListTotal);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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
            JSONArray list=obj.getJSONArray("list");
            int pages = obj.getInteger("pages");
            int listTotal = 0;//列表的条数
            for(int page=1;page<=pages;page++){
                JSONArray list1= crm.afterSale_firstmMintainRecordList(page, 50, "", "", "").getJSONArray("list");
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
            System.out.println("todayViNum-------"+todayViNum);
            int pages1 = obj.getInteger("pages");
            int todayListTotal = 0;
            for(int i=1;i<=pages1;i++){
                JSONArray todayList= crm.afterSale_firstmMintainRecordList(i, 50, "", dt.getHistoryDate(0), dt.getHistoryDate(0)).getJSONArray("list");
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
            Preconditions.checkArgument(flag == true, "全部回访" + total + "大于今日回访" + todayListTotal);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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
            JSONArray list = obj.getJSONArray("list");
            int listTotal = 0;//列表的条数
            for(int page=1;page<=pages;page++){
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
            for(int i=1;i<=pages;i++){
                JSONArray  todayList= crm.afterSale_customerChurnWarningList(1, 50, "", dt.getHistoryDate(0), dt.getHistoryDate(0)).getJSONArray("list");
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
            Preconditions.checkArgument(flag == true, "全部回访" + total + "大于今日回访" + todayListTotal);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
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
            if (ifreception.equals("yes")) {
                Long after_record_id = crm.reception_customer(repair_id).getLong("after_record_id");
                a[2] = Long.toString(after_record_id);
            } else {
                a[2] = "未点击接待按钮";
            }
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
        return new String(Base64.encodeBase64(data));
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
        try {
            crm.login(qt_name, pwd);
            JSONObject response = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
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
        String CustomerPhone = "13373166000";
        try {
            crm.login(qt_name, pwd);
            JSONObject response = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
            int allCustomerNum = response.getInteger("all_customer_num");
            crm.login(xs_name, pwd);
            crm.createLine("Max(自动化)", 2, CustomerPhone, 1, "啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦");
        crm.login(qt_name, pwd);
            JSONObject response1 = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
            int allCustomerNum1 = response1.getInteger("all_customer_num");
            Preconditions.checkArgument(allCustomerNum1 == allCustomerNum + 1, "没有创建线索之前数据为：" + allCustomerNum + "  创建线索之后数据为：" + allCustomerNum1);
            crm.login(zjl_name, pwd);
            JSONObject responZjl = crm.customerListPC(CustomerPhone, 1, 10);
            long customerId = responZjl.getJSONArray("list").getJSONObject(0).getInteger("customer_id");
            crm.customerDeletePC(customerId);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今天销售创建线索->今日线索+1");
        }

    }

    //销售接待--今天销售创建客户->今日线索+1
    @Test
    public void xsCreateCustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //前台登陆
            crm.login(qt_name, pwd);
            JSONObject response = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
            int allCustomerNum = response.getInteger("all_customer_num");
            Long customerid = -1L;
            //获取当前空闲第一位销售id
            String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
            String userLoginName = "";
            JSONArray userlist = crm.userPage(1, 100).getJSONArray("list");
            for (int i = 0; i < userlist.size(); i++) {
                JSONObject obj = userlist.getJSONObject(i);
                if (obj.getString("user_id").equals(sale_id)) {
                    userLoginName = obj.getString("user_login_name");
                }
            }
            //创建接待
            crm.creatReception("FIRST_VISIT");
            //销售登陆，获取当前接待id
            crm.login(userLoginName, pwd);
            customerid = crm.userInfService().getLong("customer_id");
            //创建某级客户
            String name = "翠花";
            String phone = "13388115577";
            crm.finishReception(customerid, 3, name, phone, "自动化---------创建----------客户");
            crm.login(qt_name, pwd);
            JSONObject response1 = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
            int allCustomerNum1 = response1.getInteger("all_customer_num");
            Preconditions.checkArgument(allCustomerNum1 == allCustomerNum + 1, "没有创建线索之前数据为：" + allCustomerNum1 + "  创建线索之后数据为：" + allCustomerNum);

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(zjl_name, pwd);
            JSONObject responZjl = crm.customerListPC("13388115577", 1, 2<<100);
            long customerId = responZjl.getJSONArray("list").getJSONObject(0).getInteger("customer_id");
            crm.customerDeletePC(customerId);
            saveData("今天销售创建客户->今日线索+1");
        }


    }


    //销售接待--今日试驾=所有销售 【客户管理-我的试驾】今日试驾之和
    @Test(enabled = true)
    public void TodayDriveSum() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(qt_name, pwd);
            JSONObject response = crm.receptionPage(1, 10, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
            int todayTestDrive = response.getInteger("today_test_drive_num");
            System.out.println("今日试驾的数量" + todayTestDrive);
            crm.login(bsj_name, demoPassword);
            int pages = crm.userPage(1, 10).getInteger("pages");
            int max = 0;
            if (pages != 0) {
                for (int page = 1; page <= pages; page++) {
                    JSONObject responseBsj = crm.userPage(page, 10);
                    JSONArray list = responseBsj.getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        if (list.getJSONObject(i).getString("role_name").equals("销售顾问") || list.getJSONObject(i).getString("role_name").equals("DCC销售顾问")) {
                            String userLoginName = list.getJSONObject(i).getString("user_login_name");
                            crm.login(userLoginName, pwd);
                            int todayTestDriveTotal = crm.driverTotal().getInteger("today_test_drive_total");
                            max += todayTestDriveTotal;

                        }
                    }
                }
            }
            logger.info("max:{}=todaysum:{}", max, todayTestDrive);
            Preconditions.checkArgument(max == todayTestDrive, "今日试驾的数量为：" + todayTestDrive + "  " + "：各销售试驾累计为" + max);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日试驾=所有销售 【客户管理-我的试驾】今日试驾之和");
        }
    }


    //销售接待--今日订车=所有销售【客户管理-我的接待】今日订单之和

    @Test(enabled = true)
    public void TodayDuySum(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(qt_name, pwd);
            JSONObject response = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
            int todayBuyCarNum = response.getInteger("today_buy_car_num");
            crm.login(bsj_name, demoPassword);
            int pages = crm.userPage(1, 10).getInteger("pages");
            int max = 0;
            for (int page = 1; page <= pages; page++) {
                JSONArray list = crm.userPage(page, 10).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("role_name").equals("销售顾问") || list.getJSONObject(i).getString("role_name").equals("DCC销售顾问")) {
                        String userLoginName = list.getJSONObject(i).getString("user_login_name");
                        crm.login(userLoginName, pwd);
                        int todayOrder = crm.customerReceptionTotalInfo().getInteger("today_order");
                        max += todayOrder;
                    }
                }
            }
            logger.info("max:{}=todaysum:{}", max, todayBuyCarNum);
            Preconditions.checkArgument(max == todayBuyCarNum, "今日订车的数量为：" + todayBuyCarNum + "  " + "：各销售订车累计为" + max);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日试驾=所有销售 【客户管理-我的试驾】今日试驾之和");
        }
    }

    //销售接待--今日交车=所有销售【客户管理-我的交车】今日交车 之和

    @Test(enabled = true)
    public void DeliverCarSum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(qt_name, pwd);
            JSONObject response = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
            int todayDeliverCarNum = response.getInteger("today_deliver_car_num");
            crm.login(bsj_name, demoPassword);
            int pages = crm.userPage(1, 10).getInteger("pages");
            int max = 0;
            for (int page = 1; page <= pages; page++) {
                JSONArray list = crm.userPage(page, 10).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("role_name").equals("销售顾问") || list.getJSONObject(i).getString("role_name").equals("DCC销售顾问")) {
                        String userLoginName = list.getJSONObject(i).getString("user_login_name");
                        crm.login(userLoginName, pwd);
                        int todayDeliverCarTotal = crm.deliverCarTotal().getInteger("today_deliver_car_total");
                        max += todayDeliverCarTotal;
                    }
                }
            }
            Preconditions.checkArgument(max == todayDeliverCarNum, "今日交车的数量为：" + todayDeliverCarNum + "  " + "：各销售交车累计为" + max);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售接待--今日交车=所有销售【客户管理-我的交车】今日交车 之和");
        }

    }

    //接待列表导出
    @Test
    public void receptionExport() {
        try {
            String flag = crm.receptionExport();
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("接待列表导出");
        }
    }

    //APP销售接待列表数==PC销售接待列表数
    @Test
    public void receptionNumber() {

        try {
            crm.login(qt_name, pwd);
            JSONObject response = crm.receptionPage(1, 2);
            int allCustomerNum = response.getInteger("total");
            JSONObject response1 = crm.receptionPage1(1, "PRE_SALES", 10);
            int todayDeliverCarNum = response1.getInteger("total");
            Preconditions.checkArgument(allCustomerNum == todayDeliverCarNum, "APP列表数：" + allCustomerNum + "  " + "PC列表数" + todayDeliverCarNum);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("APP销售接待列表数==PC销售接待列表数");
        }
    }

    //V2.1活动报名-是否完成，未完成高亮，已完成不高亮
    @Test(enabled = true)
    public void activityTaskPage() {
        try {
            crm.login(wx_name, pwd);
            JSONObject response = crm.activityTaskPage(1, 10);
            JSONArray list = response.getJSONArray("list");
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                if (list.size() > 1) {
                    for (int i = 0; i < list.size(); i++) {
                        String taskStatusName = list.getJSONObject(i).getString("activity_task_status_name");
                        Boolean isHighlight = list.getJSONObject(i).getBoolean("is_highlight");
                        if (taskStatusName.equals("未完成")) {
                            Preconditions.checkArgument(isHighlight == true, "报名活动未完成的不高亮");
                        } else {
                            Preconditions.checkArgument(isHighlight == false, "报名活动已完成的高亮");
                        }
                    }
                } else {
                    Preconditions.checkArgument(list == null, "活动报名没有数据，列表长度不为空");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("V2.1活动报名-是否完成，未完成高亮，已完成不高亮--gly");
        }
    }

    //活动报名-当前日期>=开始日期，填写报名置灰，当前日期<开始日期，，填写报名，高亮可点击
    @Test(enabled = true)
    public void activityIsEdit() {
        try {
            crm.login(wx_name, pwd);
            JSONObject response = crm.activityTaskPage(1, 10);
            JSONArray list = response.getJSONArray("list");
            String nowTime = DateTimeUtil.getFormat(new Date());
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                if (list.size() > 1) {
                    for (int i = 0; i < list.size(); i++) {
                        String date = list.getJSONObject(i).getString("activity_start");
                        Boolean isEdit = list.getJSONObject(i).getBoolean("is_edit");
                        if (date.compareTo(nowTime) > 0) {
                            Preconditions.checkArgument(isEdit == true, "活动报名时间大于当前时间，活动报名不可以");
                        } else {
                            Preconditions.checkArgument(isEdit == false, "报名活动小于当前时间，活动可以报名");
                        }
                    }
                } else {
                    Preconditions.checkArgument(list == null, "活动报名没有数据，列表长度不为空");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("活动报名-当前日期>=开始日期，填写报名置灰，当前日期<开始日期，，填写报名，高亮可点击");
        }
    }

    //活动报名-添加报名人信息并删除---可优化，不把活动id写死，写一个判断：活动是否可以报名，可以则获取该活动id(包括下边的case一起改)----郭丽雅
    @Test(enabled = true)
    public void registeredCustomer() {
        try {
            crm.login(wx_name, pwd);
            String phone = "17737771883";

            crm.registeredCustomer(228L, "自动化啦", phone);
            JSONObject response = crm.TaskInfo(228L, phone);
            JSONArray list = response.getJSONArray("customer_list");
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    String phone1 = list.getJSONObject(i).getString("customer_phone_number");
                    if (phone1.equals(phone)) {
                        String cumId = list.getJSONObject(i).getString("customer_id");
                        JSONObject la=crm.deleteCustomerX("228", cumId);
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("活动报名-添加报名人信息并删除");
        }
    }

    //活动报名-添加报名人信息--异常--手机号12位
    @Test(enabled = true)
    public void registeredCustomer2() {
        try {
            crm.login(wx_name, pwd);
            String phone = "177377711311";
            String message = crm.registeredCustomer1(228L, "哈哈哈", phone).getString("message");
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
    @Test(enabled = true)
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
    @Test(enabled = true)
    public void activityTaskInfo() {
        try {
            crm.login(wx_name, pwd);
            JSONObject response = crm.TaskInfo(228L, "17737370002");
            JSONArray list = response.getJSONArray("customer_list");
            Preconditions.checkArgument(list.size() <= 50 && list.size() >= 0, "活动报名超过50人或少于0人");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("报名活动-添加版名人数在0到50之间");
        }
    }

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
        } catch (Exception|AssertionError e) {
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
            JSONObject response = crm.addUser(userName, userLoginName, phone, passwd, roleId);
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
            JSONObject response = crm.addUser(userName, userLoginName, phone, passwd, roleId);
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

    //我的接待-本月接待售后车辆>=今日接待售后车辆&&本月完成维修车辆>=今日完成维修车辆
    @Test
    public void receptionAfterCustomer() {
        try {
            crm.login(by_name, pwd);
            JSONObject response = crm.afterSale_custList("", "", "", 1, 10);
            int monthReceptionCar = response.getInteger("month_reception_car");
            int monthRepairedCar = response.getInteger("month_repaired_car");
            int todayReceptionCar = response.getInteger("today_reception_car");
            int todayRepairedCar = response.getInteger("today_repaired_car");
            Preconditions.checkArgument(monthReceptionCar >= todayReceptionCar && monthRepairedCar >= todayRepairedCar, "本月接待售后车辆>=今日接待售后车辆&&本月完成维修车辆>=今日完成维修车辆异常");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的接待-本月接待售后车辆>=今日接待售后车辆&&本月完成维修车辆>=今日完成维修车辆");
        }
    }

    //今日接待售后车辆=今天筛选，列表数车牌去重
    @Test
    public void todayReception() {
        try {
            crm.login(by_name, pwd);
            JSONObject response = crm.afterSale_custList("", DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()), 1, 10);
            int pages = response.getInteger("pages");
            int sum = 0;
            for (int page = 1; page <= pages; page++) {
                JSONObject response1 = crm.afterSale_custList("", DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()), page, 10);
                JSONArray list = response1.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String phone = list.getJSONObject(i).getString("customer_phone_number");
                    sum++;
                    for (int j = 0; j < i; j++) {
                        String phone1 = list.getJSONObject(j).getString("customer_phone_number");
                        if (phone.equals(phone1)) {
                            sum = sum - 1;
                        }
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的接待-本月接待售后车辆>=今日接待售后车辆&&本月完成维修车辆>=今日完成维修车辆");
        }

    }

    //总监本月接待售后车辆=各个顾问本月接待售后车辆之和----现在数据存在问题----郭丽雅
    @Test(enabled = true)
    public void monthReceptionCarCompeare() {
        try {
            crm.login(fwzj_name, pwd);
            JSONObject response = crm.afterSale_custList("", "", "", 1, 10);
            //接待数量
            int monthReceptionCar = response.getInteger("month_reception_car");
            crm.login(bsj_name, demoPassword);
            JSONObject response1 = crm.userPage(1, 10);
            int pages = response1.getInteger("pages");
            int max=0;
            for (int page = 1; page <= pages; page++) {
                JSONObject responseBsj = crm.userPage(page, 10);
                JSONArray list = responseBsj.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("role_name").equals("保养顾问") || list.getJSONObject(i).getString("role_name").equals("维修顾问")) {
                        String userLoginName = list.getJSONObject(i).getString("user_login_name");
                        System.out.println(userLoginName);
                        crm.login(userLoginName, pwd);
                        int monthReceptionCar1 = crm.afterSale_custList("","","",1,10).getInteger("month_reception_car");
                        max += monthReceptionCar1;
                    }
                }
            }
            Preconditions.checkArgument(max == monthReceptionCar, "总监本月接待售后车辆为   " + monthReceptionCar + "各个顾问本月接待售后车辆之和  " + max);
        } catch (Exception e) {
            appendFailreason(e.toString());
        }finally{
            saveData("总监本月接待售后车辆=各个顾问本月接待售后车辆之和");
        }
    }

    //总监本月完成维修车辆=各个顾问本月完成维修车辆之和----现在数据存在问题----郭丽雅
    @Test(enabled = true)
    public void monthRepairedCarCompeare() {
        try {
            crm.login(fwzj_name, pwd);
            JSONObject response = crm.afterSale_custList("", "", "", 1, 10);
            //接待数量
            int monthRepairedCar = response.getInteger("month_repaired_car");
            crm.login(bsj_name, demoPassword);
            JSONObject response1 = crm.userPage(1, 10);
            int pages = response1.getInteger("pages");
            int max=0;
            for (int page = 1; page <= pages; page++) {
                JSONObject responseBsj = crm.userPage(page, 10);
                JSONArray list = responseBsj.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("role_name").equals("维修顾问")||list.getJSONObject(i).getString("role_name").equals("保养顾问")) {
                        String userLoginName = list.getJSONObject(i).getString("user_login_name");
                        System.out.println(userLoginName);
                        crm.login(userLoginName, pwd);
                        int monthReceptionCar1 = crm.afterSale_custList("","","",1,10).getInteger("month_repaired_car");
                        System.out.println("----销售顾问接待数量----"+monthReceptionCar1);
                        max += monthReceptionCar1;
                    }
                }
            }
            System.out.println("总监本月完成维修车辆为   " + monthRepairedCar + "各个顾问本月完成维修车辆之和  " + max);
            Preconditions.checkArgument(max == monthRepairedCar, "总监本月完成维修车辆为   " + monthRepairedCar + "各个顾问本月完成维修车辆之和  " + max);
        } catch (Exception |AssertionError e) {
            appendFailreason(e.toString());
        }finally{
            saveData("总监本月完成维修车辆=各个顾问本月完成维修车辆之和");
        }
    }

    //总监今日接待售后车辆=各个顾问今日接待售后车辆之和
    @Test(enabled = true)
    public void todayReceptionCarCompeare() {
        try {
            crm.login(by_name, pwd);
            JSONObject response = crm.afterSale_custList("", "", "", 1, 10);
            //接待数量
            int todayReceptionCar = response.getInteger("today_reception_car");
            crm.login(bsj_name, demoPassword);
            JSONObject response1 = crm.userPage(1, 10);
            int pages = response1.getInteger("pages");
            int max=0;
            if (pages != 0) {
                for (int page = 1; page <= pages; page++) {
                    JSONObject responseBsj = crm.userPage(page, 10);
                    JSONArray list = responseBsj.getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        if (list.getJSONObject(i).getString("role_name").equals("保养顾问") || list.getJSONObject(i).getString("role_name").equals("维修顾问")) {
                            String userLoginName = list.getJSONObject(i).getString("user_login_name");
                            System.out.println(userLoginName);
                            crm.login(userLoginName, pwd);
                            int monthReceptionCar1 = crm.afterSale_custList("","","",1,10).getInteger("today_reception_car");
                            max += monthReceptionCar1;
                        }
                    }
                }
            }
            Preconditions.checkArgument(max == todayReceptionCar, "总监本月接待售后车辆为   " + todayReceptionCar + "各个顾问本月接待售后车辆之和  " + max);
        } catch (Exception |AssertionError e) {
            appendFailreason(e.toString());
        }finally{
            saveData("总监今日接待售后车辆=各个顾问今日接待售后车辆之和");
        }
    }

    //总监今日完成维修车辆=各个顾问今日完成维修车辆之和
    @Test(enabled = true)
    public void todayRepairedCarCompeare() {
        try {
            crm.login(by_name, pwd);
            JSONObject response = crm.afterSale_custList("", "", "", 1, 10);
            //接待数量
            int todayRepairedCar = response.getInteger("today_repaired_car");
            crm.login(bsj_name, demoPassword);
            JSONObject response1 = crm.userPage(1, 10);
            int pages = response1.getInteger("pages");
            int max=0;
            for (int page = 1; page <= pages; page++) {
                JSONObject responseBsj = crm.userPage(page, 10);
                JSONArray list = responseBsj.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if ( list.getJSONObject(i).getString("role_name").equals("维修顾问")) {
                        String userLoginName = list.getJSONObject(i).getString("user_login_name");
                        System.out.println(userLoginName);
                        crm.login(userLoginName, pwd);
                        int monthReceptionCar1 = crm.afterSale_custList("","","",1,10).getInteger("today_repaired_car");
                        max += monthReceptionCar1;
                    }
                }
            }
            Preconditions.checkArgument(max == todayRepairedCar, "总监今日接待维修车辆为   " + todayRepairedCar + "各个顾问今日接待维修车辆之和  " + max);
        } catch (Exception |AssertionError e) {
            appendFailreason(e.toString());
        }finally{
            saveData("总监今日完成维修车辆=各个顾问今日完成维修车辆之和");
        }
    }

    //我的客户--本月新增车辆<=【我的接待】本月接待售后车辆&&本月新增车辆>=今日新增车辆
    @Test
    public void afterSaleustomerCar(){
        crm.login(by_name, pwd);
        try {
            JSONObject response=crm.afterSaleCustomerList("","","",1,10);
            int totalReceptionarCar=response.getInteger("total_reception_car");
            int monthReceptionCar=response.getInteger("month_reception_car");
            int todayNewCar=response.getInteger("today_new_car");
            Preconditions.checkArgument(totalReceptionarCar >= monthReceptionCar&&monthReceptionCar>=todayNewCar, "\n" + " 我的客户--本月新增车辆<=【我的接待】本月接待售后车辆&&本月新增车辆>=今日新增车辆异常");
        } catch (Exception e) {
            appendFailreason(e.toString());
        }finally{
            saveData("我的客户--本月新增车辆<=【我的接待】本月接待售后车辆&&本月新增车辆>=今日新增车辆");
        }
    }

    //我的客户--全部车辆=列表数车牌号去重
    @Test(enabled = true)
    public void todayNewCarCarCompare(){
        crm.login(by_name, pwd);
        try {
            int sum=0;
            JSONObject response=crm.afterSaleCustomerList("","","",1,10);
            int totalReceptionCar=response.getInteger("total_reception_car");
            int pages=response.getInteger("pages");
            for(int page =1;page<=pages;page++){
                JSONObject response1=crm.afterSaleCustomerList("","","",page,2<<10);
                JSONArray list=response1.getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    String plateNumber=list.getJSONObject(i).getString("plate_number");
                    sum++;
                    for(int j=i+1;j<list.size();j++){
                        String plateNumber1 =list.getJSONObject(j).getString("plate_number");
                        if(plateNumber.equals(plateNumber1)){
                            sum--;
                        }
                    }
                }
            }
            Preconditions.checkArgument(totalReceptionCar == sum , "\n" + " 我的客户--全部车辆为 "+totalReceptionCar+"列表数车牌号去重 "+sum);
        } catch (Exception |AssertionError e) {
            appendFailreason(e.toString());
        }finally{
            saveData("APP-我的客户--全部车辆=列表数车牌号去重");
        }
    }

    //今日新增车辆=今日筛选，车牌号去重
    @Test(enabled = true)
    public void todayNewCarCompare(){
        crm.login(by_name, pwd);
        try {
            int sum=0;
            JSONObject response=crm.afterSaleCustomerList("", DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()),1,10);
            int todayNewCar=response.getInteger("today_new_car");
            int pages=response.getInteger("pages");
            for(int page =1;page<=pages;page++){
                JSONObject response1=crm.afterSaleCustomerList("","","",page,2<<10);
                JSONArray list=response1.getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    String plateNumber=list.getJSONObject(i).getString("plate_number");
                    sum++;
                    for(int j=i+1;j<list.size();j++){
                        String plateNumber1 =list.getJSONObject(j).getString("plate_number");
                        if(plateNumber.equals(plateNumber1)){
                            sum--;
                        }
                    }
                }
            }
            Preconditions.checkArgument(todayNewCar == sum , "\n" + " 我的客户--今日新增车辆为 "+todayNewCar+"列表数车牌号去重 "+sum);
        } catch (Exception |AssertionError e) {
            appendFailreason(e.toString());
        }finally{
            saveData("APP-今日新增车辆=今日筛选，车牌号去重");
        }
    }

    //APP-我的客户-本月新增车辆<=【我的接待】本月接待售后车辆
    @Test
    public void monthReceptionCompare(){
        crm.login(by_name, pwd);
        try {
            //我的客户-本月新增车辆
            JSONObject  response = crm.afterSaleCustomerList("", DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()),1,10);
            int monthReceptionCar=response.getInteger("month_reception_car");
            //【我的接待】本月接待售后车辆
            JSONObject response1 = crm.afterSale_custList("", "", "", 1, 10);
            int monthReceptionCar1 = response1.getInteger("month_reception_car");
            Preconditions.checkArgument(monthReceptionCar <= monthReceptionCar1 , "\n" + "【我的接待】本月接待售后车辆"+monthReceptionCar1+"我的客户-本月新增车辆 "+monthReceptionCar);
        } catch (Exception|AssertionError e) {
            appendFailreason(e.toString());
        }finally{
            saveData("APP-我的客户-本月新增车辆<=【我的接待】本月接待售后车辆");
        }
    }

    //APP-我的客户-今日新增车辆<=【我的接待】今日接待售后车辆
    @Test
    public void todayReceptionCompare(){
        crm.login(by_name, pwd);
        try {
            //我的客户-本月新增车辆
            JSONObject  response = crm.afterSaleCustomerList("", DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()),1,10);
            int todayNewCar=response.getInteger("today_new_car");
            //【我的接待】本月接待售后车辆
            JSONObject response1 = crm.afterSale_custList("", "", "", 1, 10);
            int todaycarReceptionCar1 = response1.getInteger("today_reception_car");
            Preconditions.checkArgument(todayNewCar <= todaycarReceptionCar1 , "\n" + "【我的接待】今日接待售后车辆"+todaycarReceptionCar1+"我的客户-今日新增车辆 "+todayNewCar);
        } catch (Exception|AssertionError e) {
            appendFailreason(e.toString());
        }finally{
            saveData("APP-我的客户-今日新增车辆<=【我的接待】今日接待售后车辆");
        }
    }

    //APP-我的客户-总监的全部车辆=各个顾问的全部车辆之和-----现在计算结果有问题，已提bug--郭丽雅
    @Test(enabled = true)
    public void comparezXTotal(){
        crm.login(fwzj_name,pwd);
        JSONObject  response = null;
        try {
            response = crm.afterSaleCustomerList("", DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()),1,10);
            int totalReceptionCar=response.getInteger("total_reception_car");
            crm.login(bsj_name, demoPassword);
            int pages = crm.userPage(1, 10).getInteger("pages");
            int max = 0;
            for (int page = 1; page <= pages; page++) {
                JSONObject responseBsj = crm.userPage(page, 10);
                JSONArray list = responseBsj.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("role_name").equals("维修顾问") || list.getJSONObject(i).getString("role_name").equals("保养顾问")) {
                        String userLoginName = list.getJSONObject(i).getString("user_login_name");
                        crm.login(userLoginName, pwd);
                        int totalReceptionCarGw =  crm.afterSaleCustomerList("", DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()),1,10).getInteger("total_reception_car");
                        max += totalReceptionCarGw;
                    }
                }
            }
            System.out.println("总监的全部车辆"+totalReceptionCar+"各个顾问的全部车辆之和"+max);
            Preconditions.checkArgument(totalReceptionCar==max,"总监的全部车辆"+totalReceptionCar+"各个顾问的全部车辆之和"+max);
        } catch (Exception |AssertionError e) {
            appendFailreason(e.toString());
        }finally{
            saveData("APP-我的客户-总监的全部车辆=各个顾问的全部车辆之和");
        }
    }

    //APP-我的客户-总监的本月新增=各个顾问的本月新增之和-----现在计算结果有问题，不过结果都是0，现在case稳定运行--郭丽雅
    @Test(enabled = true)
    public void comparezXMouth(){
        crm.login(fwzj_name,pwd);
        JSONObject  response = null;
        try {
            response = crm.afterSaleCustomerList("", DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()),1,10);
            int monthReceptionCar=response.getInteger("month_reception_car");
            crm.login(bsj_name, demoPassword);
            int pages = crm.userPage(1, 10).getInteger("pages");
            int max = 0;
            for (int page = 1; page <= pages; page++) {
                JSONObject responseBsj = crm.userPage(page, 10);
                JSONArray list = responseBsj.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("role_name").equals("维修顾问") || list.getJSONObject(i).getString("role_name").equals("保养顾问")) {
                        String userLoginName = list.getJSONObject(i).getString("user_login_name");
                        crm.login(userLoginName, pwd);
                        int totalReceptionCarGw =  crm.afterSaleCustomerList("", DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()),1,10).getInteger("month_reception_car");
                        max += totalReceptionCarGw;
                    }
                }
            }
            Preconditions.checkArgument(monthReceptionCar==max,"总监的本月新增车辆"+monthReceptionCar+"各个顾问的全部本月新增车辆之和"+max);
        } catch (Exception |AssertionError e) {
            appendFailreason(e.toString());
        }finally{
            saveData("APP-我的客户-总监的本月新增=各个顾问的本月新增之和");
        }
    }

    //APP-我的客户-总监的今日新增车辆=各个顾问的今日新增之和------现在计算结果有问题，不过结果都是0，现在case稳定运行--郭丽雅
    @Test(enabled = true)
    public void comparezXToday(){
        crm.login(fwzj_name,pwd);
        JSONObject  response = null;
        try {
            response = crm.afterSaleCustomerList("", DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()),1,10);
            int todayNewCar=response.getInteger("today_new_car");
            crm.login(bsj_name, demoPassword);
            int pages = crm.userPage(1, 10).getInteger("pages");
            int max = 0;
            for (int page = 1; page <= pages; page++) {
                JSONObject responseBsj = crm.userPage(page, 10);
                JSONArray list = responseBsj.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("role_name").equals("维修顾问") || list.getJSONObject(i).getString("role_name").equals("保养顾问")) {
                        String userLoginName = list.getJSONObject(i).getString("user_login_name");
                        crm.login(userLoginName, pwd);
                        int totalReceptionCarGw =  crm.afterSaleCustomerList("", DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()),1,10).getInteger("today_new_car");
                        max += totalReceptionCarGw;
                    }
                }
            }
            System.out.println("总监的本日新增车辆"+todayNewCar+"各个顾问的本日新增车辆之和"+max);
            Preconditions.checkArgument(todayNewCar==max,"总监的本日新增车辆"+todayNewCar+"各个顾问的本日新增车辆之和"+max);
        } catch (Exception |AssertionError e) {
            appendFailreason(e.toString());
        }finally{
            saveData("APP-我的客户-总监的今日新增车辆=各个顾问的今日新增之和");
        }
    }
}
