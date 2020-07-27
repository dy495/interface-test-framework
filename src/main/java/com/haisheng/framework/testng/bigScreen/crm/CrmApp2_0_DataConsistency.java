package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * @author : yu
 * @date :  2020/05/30
 */

public class CrmApp2_0_DataConsistency extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    String sale_id = "uid_562be6aa"; //销售顾问-自动化 id

    String xh_name = "lxqgw";//销售顾问
    String by_name = "lxqby";//保养顾问姓名
    String wx_name = "lxqwx";//维修顾问姓名

    String by_name_chinese = "吕保养";
    String pwd = "e10adc3949ba59abbe56e057f20f883e";//密码全部一致
    String qt_name = "qt";//前台账号

    //我的车辆信息
    String car_type_name = "718";
    Long my_car_id = 61L;
    String plate_number = "吉A66ZDH";
    int car_type = 4;
    //预约信息
    String customer_name = "lxq自动化";
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

            crm.login(by_name,pwd);
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

            crm.login(by_name,pwd);
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
            crm.login(wx_name,pwd);
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
            crm.login(wx_name,pwd);
            JSONObject obj = crm.repairAppointmentDriverNum();
            int total = obj.getInteger("appointment_total_number");

            int list = crm.repairAppointmentlist().getInteger("total");
            Preconditions.checkArgument(total <= list, "全部预约维修"+total+">列表数"+list);

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
            crm.login(wx_name,pwd);
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

            crm.login(wx_name,pwd);
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

            crm.login(wx_name,pwd);
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
    public void afterSaleChk2Repair1Num() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(wx_name,pwd);
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
        JSONObject obj = crm.appointmentRepair(carid, customer_name, customer_phone_number, date, appointment_time, description);
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
            JSONObject obj = crm.appointmentMaintain(carid, customer_name, customer_phone_number, date, appointment_time);
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
        public static String getImgStr(String imgFile){ //图片转base64
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



    }

