package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.experiment.enumerator.EnumAccount;
import com.haisheng.framework.model.experiment.enumerator.EnumAppletCode;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author : yu
 * @date :  2020/05/30
 */

public class CrmApp2_0_DataConsistency extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    String sale_id = "uid_562be6aa"; //销售顾问-自动化 id

    String xs_name = "0805xsgw";//销售顾问
    String by_name = "lxqby";//保养顾问姓名

    String by_name2 = "baoyang";
    String wx_name = "lxqwx";//维修顾问姓名
    String zjl_name = "zjl";


    String by_name_chinese = "吕保养";
    String pwd = "e10adc3949ba59abbe56e057f20f883e";//密码全部一致
    String qt_name = "qt";//前台账号
    String bsj_name = "baoshijie";

    //我的车辆信息
    String car_type_name = "718";
    Long my_car_id = 61L;
    String plate_number = "吉A66ZDH";
    int car_type = 4;
    //预约信息
    String customer_name = "lxq自动化";
    String customer_phone_number = "13400000000";
    private String IpPort;


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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "lxq";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "CRM 日常");

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
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    /**
     * ====================销售顾问======================
     */

    @Ignore
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

    //客户管理
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
            saveData("售后客户管理：全部车辆<=列表数");
        }

    }

    @Test//ok
    public void custChkTotalGTNew() {
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
            saveData("售后客户管理：今日接待售后车辆>=今日新增售后车辆");

        }
    }

    @Test//ok
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
            saveData("售后客户管理：全部车辆>=今日接待售后车辆");

        }
    }

    @Test
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
            saveData("售后客户管理：添加21条备注，展示最新20条");

        }
    }

    //@Test //bug2695
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


    //我的预约-预约保养

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
            saveData("全部预约保养<=列表数");

        }
    }

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
                        String reception_sale_name = obj.getString("reception_sale_name");
                        Preconditions.checkArgument(reception_sale_name.equals(by_name_chinese), "所属顾问为" + reception_sale_name);
                    }
                }
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新客的所属顾问=当前登陆账号销售名字");

        }
    }

    @Test
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
            crm.appletLoginLxq("");
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

    @Test
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

    @Test
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


    //我的预约-预约维修

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
            saveData("全部预约维修>=今日预约维修");

        }
    }

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
            saveData("全部预约维修<=列表数");

        }
    }

    @Test
    public void afterSaleRepairNewChkRecpname() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(wx_name, pwd);
            JSONArray maintain = crm.repairAppointmentlist().getJSONArray("list");
            int size = maintain.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    JSONObject obj = maintain.getJSONObject(i);
                    if (obj.getString("customer_type_name").equals("新客")) {
                        String reception_sale_name = obj.getString("reception_sale_name");
                        Preconditions.checkArgument(reception_sale_name.equals(wx_name), "所属顾问为" + reception_sale_name);
                    }
                }
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新客的所属顾问=当前登陆账号销售名字");

        }
    }

    @Test
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

    @Test
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

    @Test
    public void afterSalesReturnVisit() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(by_name2, pwd);

            //1.全部回访=列表条数
            JSONObject obj = crm.afterSale_VisitRecordList(1, 50, "", "", "");
            Integer total = obj.getInteger("total");//全部回访条数
            JSONArray list = obj.getJSONArray("list");
            int listTotal = 0;//列表的条数
            for (int j = 0; j < list.size(); j++) {
                Integer id = list.getJSONObject(j).getInteger("id");
                if (id != null) {
                    listTotal++;
                }
            }


            //2.今日回访=任务日期为今天的条数
            JSONObject obj1 = crm.afterSale_VisitRecordList(1, 50, "", dt.getHistoryDate(0), dt.getHistoryDate(0));
            Integer todayViNum = obj1.getInteger("today_return_visit_number");//获取今日回访条数
            JSONArray todayList = obj1.getJSONArray("list");
            int todayListTotal = 0;
            for (int k = 0; k < todayList.size(); k++) {
                Integer id = todayList.getJSONObject(k).getInteger("id");
                if (id != null) {
                    todayListTotal++;
                }
            }

            //3.全部回访>=今日回访
            boolean flag = false;
            if (total >= todayListTotal) {
                flag = true;
            }

            //4.回访任务日期为今天的回访任务，是否完成=已完成   //5.回访任务日期为昨天的回访任务，是否完成=已完成
            boolean isTrueOrF1 = false;
            boolean isTrueOrF2 = false;
            for (int i = 0; i < list.size(); i++) {
                String taskTime = list.getJSONObject(i).getString("return_visit_date");
                String today_time = dt.getHistoryDate(0);
                String yester_time = dt.getHistoryDate(-1);
                if (taskTime.equals(today_time)) {
                    String isTrue = list.getJSONObject(i).getString("return_visit_status_name");
                    if (isTrue.equals("已完成")) {
                        isTrueOrF1 = true;

                    }
                }
                if (taskTime.equals(yester_time)) {
                    String isTrue = list.getJSONObject(i).getString("return_visit_status_name");
                    if (isTrue.equals("已完成")) {
                        isTrueOrF2 = true;

                    }
                }
            }


            Preconditions.checkArgument(todayViNum == todayListTotal, "售后工作管理中我的回访-售后回访中的今日回访" + todayViNum + "不等于工作管理中我的回访-售后回访中任务日期为今天的条数" + todayListTotal);
            Preconditions.checkArgument(flag == true, "全部回访" + total + "大于今日回访" + todayListTotal);
            Preconditions.checkArgument(isTrueOrF1 == true, "回访任务日期为今天的回访任务是否完成=未完成");
            Preconditions.checkArgument(isTrueOrF2 == true, "回访任务日期为昨天的回访任务是否完成=未完成");
            Preconditions.checkArgument(total == listTotal, "售后工作管理中我的回访-售后回访中的全部回访" + total + "不等于后工作管理中我的回访-售后回访中的列表条数" + listTotal);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后工作管理中我的回访-售后回访的数据一致性");

        }
    }

    @Test
    public void afterSalesfirstmMintain() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(by_name2, pwd);

            //1.全部回访=列表条数
            JSONObject obj = crm.afterSale_firstmMintainRecordList(1, 50, "", "", "");
            Integer total = obj.getInteger("total");//全部回访条数
            JSONArray list = obj.getJSONArray("list");
            int listTotal = 0;//列表的条数
            for (int j = 0; j < list.size(); j++) {
                Integer id = list.getJSONObject(j).getInteger("id");
                if (id != null) {
                    listTotal++;
                }
            }


            //2.今日回访=任务日期为今天的条数
            JSONObject obj1 = crm.afterSale_firstmMintainRecordList(1, 50, "", dt.getHistoryDate(0), dt.getHistoryDate(0));
            Integer todayViNum = obj1.getInteger("today_return_visit_number");//获取今日回访条数
            JSONArray todayList = obj1.getJSONArray("list");
            int todayListTotal = 0;
            for (int k = 0; k < todayList.size(); k++) {
                Integer id = todayList.getJSONObject(k).getInteger("id");
                if (id != null) {
                    todayListTotal++;
                }
            }

            //3.全部回访>=今日回访
            boolean flag = false;
            if (total >= todayListTotal) {
                flag = true;
            }

            //4.回访任务日期为今天的回访任务，是否完成=已完成   //5.回访任务日期为昨天的回访任务，是否完成=已完成
            boolean isTrueOrF1 = false;
            boolean isTrueOrF2 = false;
            for (int i = 0; i < list.size(); i++) {
                String taskTime = list.getJSONObject(i).getString("return_visit_date");
                String today_time = dt.getHistoryDate(0);
                String yester_time = dt.getHistoryDate(-1);

                if (taskTime.equals(today_time)) {
                    String isTrue = list.getJSONObject(i).getString("return_visit_status_name");
                    if (isTrue.equals("已完成")) {
                        isTrueOrF1 = true;

                    }
                } else {
                    isTrueOrF1 = false;
                }
                if (taskTime.equals(yester_time)) {
                    String isTrue = list.getJSONObject(i).getString("return_visit_status_name");
                    if (isTrue.equals("已完成")) {
                        isTrueOrF2 = true;

                    }
                } else {
                    isTrueOrF2 = false;
                }

            }


            Preconditions.checkArgument(total == listTotal, "售后工作管理中我的回访-首保提醒中的全部回访" + total + "不等于售后后工作管理中我的回访-首保提醒中的列表条数" + listTotal);
            Preconditions.checkArgument(todayViNum == todayListTotal, "售后工作管理中我的回访-售后回访中的今日回访" + todayViNum + "不等于后工作管理中我的回访-售后回访中任务日期为今天的条数" + todayListTotal);
            Preconditions.checkArgument(flag == true, "全部回访" + total + "大于今日回访" + todayListTotal);
            Preconditions.checkArgument(isTrueOrF1 == true, "回访任务日期为今天的回访任务是否完成=未完成");
            Preconditions.checkArgument(isTrueOrF2 == true, "回访任务日期为昨天的回访任务是否完成=未完成");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后工作管理中我的回访-首保提醒的数据一致性");

        }
    }

    @Test
    public void afterSalescustomerChurnWarn() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(by_name2, pwd);

            //1.全部回访=列表条数
            JSONObject obj = crm.afterSale_customerChurnWarningList(1, 50, "", "", "");
            Integer total = obj.getInteger("total");//全部回访条数
            JSONArray list = obj.getJSONArray("list");
            int listTotal = 0;//列表的条数
            for (int j = 0; j < list.size(); j++) {
                Integer id = list.getJSONObject(j).getInteger("id");
                if (id != null) {
                    listTotal++;
                }
            }


            //2.今日回访=任务日期为今天的条数
            JSONObject obj1 = crm.afterSale_customerChurnWarningList(1, 50, "", dt.getHistoryDate(0), dt.getHistoryDate(0));
            Integer todayViNum = obj1.getInteger("today_return_visit_number");//获取今日回访条数
            JSONArray todayList = obj1.getJSONArray("list");
            int todayListTotal = 0;
            for (int k = 0; k < todayList.size(); k++) {
                Integer id = todayList.getJSONObject(k).getInteger("id");
                if (id != null) {
                    todayListTotal++;
                }
            }

            //3.全部回访>=今日回访
            boolean flag = false;
            if (total >= todayListTotal) {
                flag = true;
            }

            //4.回访任务日期为今天的回访任务，是否完成=已完成   //5.回访任务日期为昨天的回访任务，是否完成=已完成
            boolean isTrueOrF1 = false;
            boolean isTrueOrF2 = false;
            for (int i = 0; i < list.size(); i++) {
                String taskTime = list.getJSONObject(i).getString("return_visit_date");
                String today_time = dt.getHistoryDate(0);
                String yester_time = dt.getHistoryDate(-1);

                if (taskTime.equals(today_time)) {
                    String isTrue = list.getJSONObject(i).getString("return_visit_status_name");
                    if (isTrue.equals("已完成")) {
                        isTrueOrF1 = true;

                    }
                } else {
                    isTrueOrF1 = false;
                }
                if (taskTime.equals(yester_time)) {
                    String isTrue = list.getJSONObject(i).getString("return_visit_status_name");
                    if (isTrue.equals("已完成")) {
                        isTrueOrF2 = true;

                    }
                } else {
                    isTrueOrF2 = false;
                }

            }


            Preconditions.checkArgument(total == listTotal, "售后工作管理中我的回访-流失预警中的全部回访" + total + "不等于售后工作管理中我的回访-流失预警中的列表条数" + listTotal);
            Preconditions.checkArgument(todayViNum == todayListTotal, "售后工作管理中我的回访-流失预警中的今日回访" + todayViNum + "不等于售后工作管理中我的回访-流失预警中任务日期为今天的条数" + todayListTotal);
            Preconditions.checkArgument(flag == true, "全部回访" + total + "大于今日回访" + todayListTotal);
            Preconditions.checkArgument(isTrueOrF1 == true, "回访任务日期为今天的回访任务是否完成=未完成");
            Preconditions.checkArgument(isTrueOrF2 == true, "回访任务日期为昨天的回访任务是否完成=未完成");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后工作管理中我的回访-流失预警的数据一致性");

        }
    }

    @Test
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
    //我的回访-售后回访


    //我的回访-流失预警

    //活动报名

    //---------------------------


//    //小程序添加车辆信息
//    public Long addcar () throws Exception {
//        int cartype = 1;
//        String plate_number = "吉A";
//        for (int i = 0; i < 5;i++){
//            String a = Integer.toString((int)(Math.random()*10));
//            plate_number = plate_number + a;
//        }
//        Long carid = crm.myCarAdd(cartype,plate_number).getLong("my_car_id");
//        return carid;
//    }


    // 小程序预约维修，售后顾问点击接待按钮yes/不点击接待no
    public String[] repair(String date, Long carid, String ifreception) throws Exception {

        String a[] = new String[3]; //0销售登陆账号 1预约记录id 2 接待记录id
        //小程序登陆
        crm.appletLoginLxq("");
        //预约使用参数

        String appointment_time = "09:30";
        String description = "自动化故障说明" + System.currentTimeMillis();
        long timelist = crm.timeList("REPAIR", date).getJSONArray("list").getJSONObject(0).getLong("id");

        JSONObject obj = crm.appointmentRepair(carid, customer_name, customer_phone_number, date, appointment_time, description, timelist);
        Long repair_id = obj.getLong("appointment_id");
        a[1] = Long.toString(repair_id);

        String salephone = obj.getString("sale_phone");
//        //前台登陆 //使用吕雪晴微信号进行预约 每次分配给吕保养
//        crm.login(qt_name,pwd);
//        String userLoginName = "";
//        JSONArray userlist = crm.userPage(1,100).getJSONArray("list");
//        for (int i = 0 ; i <userlist.size();i++){
//            JSONObject obj1 = userlist.getJSONObject(i);
//            if (obj1.getString("user_phone").equals(salephone)){
//                userLoginName = obj1.getString("user_login_name");
//            }
//        }
//        a[0] = userLoginName;

        //维修顾问登陆，点击接待按钮

        crm.login(by_name, pwd);
        if (ifreception.equals("yes")) {
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
        crm.appletLoginLxq("");

        String appointment_time = "09:00";
        long timelist = crm.timeList("REPAIR", date).getJSONArray("list").getJSONObject(0).getLong("id");
        JSONObject obj = crm.appointmentMaintain(carid, customer_name, customer_phone_number, date, appointment_time, timelist);
        Long maintain_id = obj.getLong("appointment_id");
        a[1] = Long.toString(maintain_id);

//        String salephone = obj.getString("sale_phone");
//        //前台登陆
//
//        String userLoginName = "";
//        JSONArray userlist = crm.userPage(1,100).getJSONArray("list");
//        for (int i = 0 ; i <userlist.size();i++){
//            JSONObject obj1 = userlist.getJSONObject(i);
//            if (obj1.getString("user_phone").equals(salephone)){
//                userLoginName = obj1.getString("user_login_name");
//            }
//        }
//        a[0] = userLoginName;
        //维修顾问登陆，点击接待按钮
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


//    //销售接待--今日接待=列表总数手机号去重

    @Test
    public void TodayReception() {
        logger.logCaseStart(caseResult.getCaseName());
        crm.login(qt_name, pwd);
        JSONObject response = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
        System.out.println("response   " + response);
        int todayReptionNum = response.getInteger("today_reception_num");
        System.out.println("todayReptionNum   " + todayReptionNum);

    }

//    //销售接待--接待率=今日接待/今日线索*100
//   有问题 ，没有调试完   文字需要修改

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
            if (todayReptionNum == 0 && allCustomerNum != 0) {
                result = "100";
                Preconditions.checkArgument(todayReceptionRatio.equals(result + "%"), "接待率：" + todayReceptionRatio + "  " + "今日接待/今日线索*100：" + result);
            } else if (todayReptionNum == 0 && allCustomerNum == 0 || todayReptionNum != 0 && allCustomerNum == 0) {
                result = "0";
                Preconditions.checkArgument(todayReceptionRatio.equals(result), "接待率：" + todayReceptionRatio + "  " + "今日接待/今日线索*100：" + result);
            } else {
                result = new DecimalFormat("#").format(Math.round(todayReptionNum / allCustomerNum * 100));
                Preconditions.checkArgument(todayReceptionRatio.equals(result), "接待率：" + todayReceptionRatio + "  " + "今日接待/今日线索*100：" + result);
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            //saveData("销售接待--接待率=今日接待/今日线索*100");
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
            System.out.println("订车率" + todayBuyCaratio + "   订单/试驾" + new DecimalFormat("#").format(Math.round(todayBuyCarNum / todayestDriveNum * 100)));
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
            e.printStackTrace();
        } finally {
            saveData("交车率=交车/订单*100");
        }
    }

    //    //销售接待--今天销售创建线索->今日线索+1
    @Test
    public void xsCreateLine() {
        logger.logCaseStart(caseResult.getCaseName());
        String CustomerPhone = "13373166806";
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

        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(zjl_name, pwd);
            JSONObject responZjl = crm.customerListPC(CustomerPhone, 1, 10);
            long customerId = responZjl.getJSONArray("list").getJSONObject(0).getInteger("customer_id");
            crm.customerDeletePC(customerId);
            saveData("今天销售创建线索->今日线索+1");
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
        crm.login(bsj_name, pwd);
        int pages = crm.userPage(1, 10).getInteger("pages");
        int max = 0;
        if (pages != 0) {
            for (int page = 1; page <= pages; page++) {
                JSONObject responseBsj = crm.userPage(page, 10);
                JSONArray list = responseBsj.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("role_name").equals("销售顾问") || list.getJSONObject(i).getString("role_name").equals("DCC销售顾问")) {
                        String userLoginName = list.getJSONObject(i).getString("user_login_name");
                        crm.login(userLoginName, "e10adc3949ba59abbe56e057f20f883e");
                        int todayTestDriveTotal = crm.driverTotal().getInteger("today_test_drive_total");
                        max += todayTestDriveTotal;
                    }
                }
            }
        }
            logger.info("max:{}=todaysum:{}",max,todayTestDrive);
            Preconditions.checkArgument(max==todayTestDrive, "今日试驾的数量为：" + todayTestDrive + "  " + "：各销售试驾累计为" + max);
        }catch(Exception|AssertionError e){
            appendFailreason(e.toString());
        }finally{
            saveData("今日试驾=所有销售 【客户管理-我的试驾】今日试驾之和");
        }
    }


    //销售接待--今日订车=所有销售【客户管理-我的接待】今日订单之和

    @Test(enabled = true)
    public void TodayDuySum() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
        crm.login(qt_name, pwd);
        JSONObject response = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
        int todayBuyCarNum = response.getInteger("today_buy_car_num");
        crm.login(bsj_name, pwd);
        int pages = crm.userPage(1, 10).getInteger("pages");
        int max = 0;
        for (int page = 1; page <= pages; page++) {
            JSONArray list = crm.userPage(page, 10).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("role_name").equals("销售顾问") || list.getJSONObject(i).getString("role_name").equals("DCC销售顾问")) {
                    String userLoginName = list.getJSONObject(i).getString("user_login_name");
                    crm.login(userLoginName, EnumAccount.XSGWTEMP.getPassword());
                    int todayOrder = crm.customerReceptionTotalInfo().getInteger("today_order");
                    max += todayOrder;
                }
            }
        }
            logger.info("max:{}=todaysum:{}",max,todayBuyCarNum);
            Preconditions.checkArgument(max == todayBuyCarNum, "今日订车的数量为：" + todayBuyCarNum + "  " + "：各销售订车累计为" + max);
        }catch(AssertionError|Exception e){
            appendFailreason(e.toString());
        }finally{
            saveData("今日试驾=所有销售 【客户管理-我的试驾】今日试驾之和");
        }
    }

    //销售接待--今日交车=所有销售【客户管理-我的交车】今日交车 之和

    @Test(enabled = true)
    public void DeliverCarSum() {
        logger.logCaseStart(caseResult.getCaseName());
        try{
            crm.login(qt_name, pwd);
            JSONObject response = crm.receptionPage(1, 2, DateTimeUtil.getFormat(new Date()), DateTimeUtil.getFormat(new Date()));
            int todayDeliverCarNum = response.getInteger("today_deliver_car_num");
            crm.login(bsj_name, pwd);
            int pages = crm.userPage(1, 10).getInteger("pages");
            int max = 0;
            for (int page = 1; page <= pages; page++) {
                JSONArray list =  crm.userPage(page, 10).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("role_name").equals("销售顾问") || list.getJSONObject(i).getString("role_name").equals("DCC销售顾问")) {
                        String userLoginName = list.getJSONObject(i).getString("user_login_name");
                        crm.login(userLoginName, EnumAccount.XSGWTEMP.getPassword());
                        int todayDeliverCarTotal = crm.deliverCarTotal().getInteger("today_deliver_car_total");
                        max += todayDeliverCarTotal;
                    }
                }
            }
            Preconditions.checkArgument(max == todayDeliverCarNum, "今日交车的数量为：" + todayDeliverCarNum + "  " + "：各销售交车累计为" + max);
    }catch(Exception|AssertionError e){
            appendFailreason(e.toString());
        }finally {
            saveData("销售接待--今日交车=所有销售【客户管理-我的交车】今日交车 之和");
        }

    }

    //接待列表导出
    @Test
    public void receptionExport() {
        try {
            String flag=crm.receptionExport();
        } catch (Exception |AssertionError e) {
            e.printStackTrace();
        } finally{

        }
    }

}

