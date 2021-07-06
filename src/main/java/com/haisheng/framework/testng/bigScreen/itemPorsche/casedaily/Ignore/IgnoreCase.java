package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.Ignore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs.CustomerInfo;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;


/**
 * @author : lxq
 * @date :  2020/05/30
 */

public class IgnoreCase extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    CustomerInfo cstm = new CustomerInfo();

    /**
     * 上传车牌
     * 接口说明：https://winsense.yuque.com/staff-qt5ptf/umvi00/mhinpu
     */
    @Test
    public void uploadLeaveShopCarPlate() {
        String carNum = "京KD1001";
        String router = "/business/porsche/PLATE_UPLOAD/v1.0";
        //设备与日常环境的设置一致，不要修改
        String deviceId = "7724082825888768";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        ImageUtil imageUtil = new ImageUtil();
        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
        String json = "{\"plate_num\":\"" + carNum + "\"," +
                "\"plate_pic\":\"@0\"," +
                "\"time\":\"" + System.currentTimeMillis() + "\"" +
                "}";
        try {
            crm.carUploadToDaily(router, deviceId, resource, json);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("离场车牌号上传");
        }
    }

    /**
     * ====================PC工作安排  V3.0取消页面======================
     */

    @Ignore
    @Test
    public void addScheduledesc10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1, 1, scheduledate, "").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc = "交车服务描述交车服务";

            int startM = 60;
            String starttime = dt.getHHmm(startM);//当前时间
            String endtime = dt.getHHmm(10 + startM);//1分钟之后
            Long scheduleid = crm.scheduleAdd_PC(schedulename, scheduledesc, scheduledate, starttime, endtime).getLong("id");

            //删除工作安排
            crm.scheduleDel_PC(scheduleid);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC端添加工作安排，工作描述字数=10");

        }


    }

    @Ignore
    @Test
    public void addScheduledesc200() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1, 1, scheduledate, "").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc = "";
            for (int i = 0; i < 200; i++) {
                scheduledesc = scheduledesc + "啊";
            }
            int startM = 60;
            String starttime = dt.getHHmm(startM);//当前时间
            String endtime = dt.getHHmm(10 + startM);//1分钟之后
            Long scheduleid = crm.scheduleAdd_PC(schedulename, scheduledesc, scheduledate, starttime, endtime).getLong("id");

            //删除工作安排
            crm.scheduleDel_PC(scheduleid);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC端添加工作安排，工作描述字数=200");

        }

    }

    @Ignore
    @Test
    public void addScheduledesc9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1, 1, scheduledate, "").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc = "交车服务描述交车服";

            int startM = 60;
            String starttime = dt.getHHmm(startM);//当前时间
            String endtime = dt.getHHmm(10 + startM);//1分钟之后
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename, scheduledesc, scheduledate, starttime, endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待1001，实际" + code + "，提示" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC端添加工作安排，工作描述字数=9");

        }


    }

    //@Test //前端校验
    public void addScheduledesc201() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1, 1, scheduledate, "").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc = "";
            for (int i = 0; i < 200; i++) {
                scheduledesc = scheduledesc + "啊";
            }

            int startM = 60;
            String starttime = dt.getHHmm(startM);//当前时间
            String endtime = dt.getHHmm(10 + startM);//1分钟之后
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename, scheduledesc, scheduledate, starttime, endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待1001，实际" + code + "，提示" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC端添加工作安排，工作描述字数=201");

        }


    }

    @Ignore
    @Test
    public void addSchedule_startGTend() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1, 1, scheduledate, "").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc = "添加工作安排，开始时间>结束时间";

            int startM = 60;
            String starttime = dt.getHHmm(10 + startM);//当前时间
            String endtime = dt.getHHmm(startM);//1分钟之后
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename, scheduledesc, scheduledate, starttime, endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待1001，实际" + code + "，提示" + message);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC端添加工作安排，开始时间>结束时间");

        }


    }

    @Ignore
    @Test
    public void addScheduleRetime() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1, 1, scheduledate, "").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc = "交车服务描述交车服务";

            int startM = 60;
            String starttime = dt.getHHmm(startM);//当前时间
            String endtime = dt.getHHmm(10 + startM);//1分钟之后
            Long scheduleid = crm.scheduleAdd_PC(schedulename, scheduledesc, scheduledate, starttime, endtime).getLong("id");

            //创建时间重叠的工作安排
            String starttime1 = dt.getHHmm(5 + startM);
            String endtime1 = dt.getHHmm(15 + startM);
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename, scheduledesc, scheduledate, starttime1, endtime1);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待1001，实际" + code + "，提示" + message);
            //删除工作安排
            crm.scheduleDel_PC(scheduleid);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC端添加工作安排，工作时间与已存在的安排时间重叠");

        }


    }

    @Ignore
    @Test
    public void addScheduleEndLTNow() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1, 1, scheduledate, "").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc = "交车服务描述交车服务";


            String starttime = dt.getHHmm(-15);//15分钟前
            String endtime = dt.getHHmm(-5);//5分钟前
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename, scheduledesc, scheduledate, starttime, endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待1001，实际" + code + "，提示" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC端添加工作安排，开始时间<结束时间<当前时间");

        }


    }

    @Ignore
    @Test
    public void addSchedulePeriodTL10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1, 1, scheduledate, "").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc = "交车服务描述交车服务";

            int startM = 60;
            String starttime = dt.getHHmm(startM);//当前时间
            String endtime = dt.getHHmm(5 + startM);//1分钟之后
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename, scheduledesc, scheduledate, starttime, endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待1001，实际" + code + "，提示" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC端添加工作安排，结束时间与开始时间间隔为5分钟");

        }


    }

    @Ignore
    @Test
    public void addScheduleEndLTStartLTNow() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1, 1, scheduledate, "").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc = "交车服务描述交车服务";


            String starttime = dt.getHHmm(-5);//5分钟前
            String endtime = dt.getHHmm(-15);//15分钟前
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename, scheduledesc, scheduledate, starttime, endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待1001，实际" + code + "，提示" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC端添加工作安排，结束时间<开始时间<当前时间");

        }


    }

    @Ignore
    @Test
    public void addScheduleEndEQStart() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1, 1, scheduledate, "").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc = "交车服务描述交车服务";


            String starttime = dt.getHHmm(5);//5分钟前
            String endtime = starttime;//5分钟前
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename, scheduledesc, scheduledate, starttime, endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待1001，实际" + code + "，提示" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC端添加工作安排，结束时间=开始时间");

        }


    }

    @Ignore
    @Test(dataProvider = "ERR_FORMAT", dataProviderClass = CrmScenarioUtil.class)
    public void addScheduleFormatErr1(String errformat) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1, 1, scheduledate, "").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc = "交车服务描述交车服务";


            String starttime = errformat;//格式异常
            String endtime = starttime;
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename, scheduledesc, scheduledate, starttime, endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待1001，实际" + code + "，提示" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC端添加工作安排，时间格式异常");

        }


    }


    /**
     * ====================PC我的回访 V3。0取消页面====================== 修改数据库
     */

    //--------------------------查询--------------------
    @Ignore
    @Test
    public void taskListSearchAll() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            String name = "" + System.currentTimeMillis();
            String phone = "zdh" + (int) ((Math.random() * 9 + 1) * 100000);
            String desc = "创建H级客户自动化------------------------------------";

            customerid = creatCust(name, phone);


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //直接点击查询
            int total = crm.taskList_PC("", -1, 1, 1, "").getInteger("total");
            Preconditions.checkArgument(total >= 1, "我的回访数量期待>=1，实际=" + total);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的回访页面直接点击查询按钮");
        }

    }

    @Ignore
    @Test
    public void taskListSearchName() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //姓名查询
            JSONObject obj = crm.customerListPC("", -1, name, "", 0, 0, 1, 1).getJSONArray("list").getJSONObject(0);
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_name.equals(name), "查询结果与查询条件不一致");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("我的回访页面根据姓名查询");
        }

    }

    @Ignore
    @Test
    public void taskListSearchPhone() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            String desc = "创建H级客户自动化------------------------------------";

            customerid = creatCust(name, phone);

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.customerListPC("", -1, "", phone1, 0, 0, 1, 1).getJSONArray("list").getJSONObject(0);
            String search_phone = obj.getString("customer_phone");
            Preconditions.checkArgument(search_phone.equals(phone1), "查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的回访页面根据手机号查询");
        }

    }

    @Ignore
    @Test
    public void taskListSearchLevel() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            customerid = creatCust(name, phone);

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONArray list = crm.customerListPC("", 7, "", "", 0, 0, 1, 1).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                Preconditions.checkArgument(single.getString("customer_level").equals("7"), "查询结果与查询条件不一致");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的回访页面根据客户等级查询");
        }

    }

    @Ignore
    @Test
    public void taskListSearchNameYPhoneY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            String desc = "创建H级客户自动化------------------------------------";

            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.customerListPC("", -1, name, phone1, 0, 0, 1, 1).getJSONArray("list").getJSONObject(0);
            String search_phone = obj.getString("customer_phone");
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_phone.equals(phone1) && search_name.equals(name), "查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的回访页面根据姓名+手机号查询");
        }

    }

    @Ignore
    @Test
    public void taskListSearchNameYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";

            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.customerListPC("", 7, name, "", 0, 0, 1, 1).getJSONArray("list").getJSONObject(0);
            String search_level = obj.getString("customer_level_name");
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_level.equals("H") && search_name.equals(name), "查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的回访页面根据姓名+级别查询");
        }

    }

    @Ignore
    @Test
    public void taskListSearchPhoneYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);

            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.customerListPC("", 7, "", phone1, 0, 0, 1, 1).getJSONArray("list").getJSONObject(0);
            String search_level = obj.getString("customer_level_name");
            String search_phone = obj.getString("customer_phone");
            Preconditions.checkArgument(search_level.equals("H") && search_phone.equals(phone1), "查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的回访页面根据手机号+级别查询");
        }

    }

    @Ignore
    @Test
    public void taskListSearchNameYPhoneYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);

            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.customerListPC("", 7, "", phone1, 0, 0, 1, 1).getJSONArray("list").getJSONObject(0);
            String search_level = obj.getString("customer_level_name");
            String search_phone = obj.getString("customer_phone");
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_level.equals("H") && search_phone.equals(phone1) && search_name.equals(name), "查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的回访页面根据姓名+手机号+级别查询");
        }

    }

    @Ignore
    @Test
    public void taskListSearchNameYPhoneN() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            String phone = "" + System.currentTimeMillis();
            String name = phone;

            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            int total = crm.customerListPC("", -1, name, phone + "1", 0, 0, 1, 1).getInteger("total");

            Preconditions.checkArgument(total == 0, "有查询结果");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的回访页面根据存在姓名+不匹配手机号查询");
        }

    }

    @Ignore
    @Test
    public void taskListSearchNameYLevelN() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            String phone = "" + System.currentTimeMillis();
            String name = phone;

            customerid = creatCust(name, phone);

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            int total = crm.customerListPC("", 3, name, "", 0, 0, 1, 1).getInteger("total");

            Preconditions.checkArgument(total == 0, "不应有查询结果");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的回访页面根据存在姓名+不匹配级别查询");
        }

    }

    @Ignore
    @Test
    public void taskListSearchPhoneYLevelN() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            customerid = creatCust(name, phone);

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            int total = crm.customerListPC("", 3, "", phone, 0, 0, 1, 1).getInteger("total");

            Preconditions.checkArgument(total == 0, "不应有查询结果");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的回访页面根据存在手机号+不匹配级别查询");
        }

    }

    @Ignore
    @Test
    public void taskListSearchNameNPhoneYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            int total = crm.customerListPC("", 7, name + "1", phone, 0, 0, 1, 1).getInteger("total");

            Preconditions.checkArgument(total == 0, "不应有查询结果");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的回访页面根据不存在的姓名+存在手机号+匹配的级别查询");
        }

    }


    //----------------------添加回访--------------------

    @Ignore
    @Test
    public void addVisitComment10() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = "" + System.currentTimeMillis();
            String phone1 = phone.substring(3);
            customerid = creatCust(name, phone);

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 10; i++) {
                comment = comment + "回";
            }
            String date = dt.getHistoryDate(1);
            visit.put("comment", comment);
            visit.put("next_return_visit_date", date);
            crm.customerEditVisitPC(customerid, name, phone1, level_id, visit);

            //查看顾客详情，回访记录条数
            int list = crm.customerDetailPC(customerid).getJSONArray("return_visit").size();
            Preconditions.checkArgument(list == 1, "添加失败");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加回访，回访记录字数=10");
        }

    }

    @Ignore
    @Test
    public void addVisitComment200() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 200; i++) {
                comment = comment + "回";
            }
            String date = dt.getHistoryDate(1);
            visit.put("comment", comment);
            visit.put("next_return_visit_date", date);
            crm.customerEditVisitPC(customerid, name, phone1, level_id, visit);

            //查看顾客详情，回访记录条数
            int list = crm.customerDetailPC(customerid).getJSONArray("return_visit").size();
            Preconditions.checkArgument(list == 1, "添加失败");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加回访，回访记录字数=200");
        }

    }

    @Ignore
    @Test
    public void addVisitnum50() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            customerid = creatCust(name, phone);

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 20; i++) {
                comment = comment + "回";
            }
            String date = dt.getHistoryDate(1);
            visit.put("comment", comment);
            visit.put("next_return_visit_date", date);
            for (int i = 0; i < 50; i++) {
                crm.customerEditVisitPC(customerid, name, phone1, level_id, visit);
            }

            //查看顾客详情，回访记录条数
            int list = crm.customerDetailPC(customerid).getJSONArray("return_visit").size();
            Preconditions.checkArgument(list == 50, "添加50条失败");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加回访，回访记录条数=50");
        }

    }

    //@Test //前端校验
    public void addVisitnum51() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;

            customerid = creatCust(name, phone);

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 20; i++) {
                comment = comment + "回";
            }
            String date = dt.getHistoryDate(1);
            visit.put("comment", comment);
            visit.put("next_return_visit_date", date);
            for (int i = 0; i < 50; i++) {
                crm.customerEditVisitPC(customerid, name, phone, level_id, visit);
            }
            JSONObject obj = crm.customerEditVisitPCNotChk(customerid, name, phone, level_id, visit);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + "，提示语：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加回访，回访记录条数=51");
        }

    }

    @Ignore
    @Test
    public void addVisitComment9() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 9; i++) {
                comment = comment + "回";
            }
            String date = dt.getHistoryDate(1);
            visit.put("comment", comment);
            visit.put("next_return_visit_date", date);
            JSONObject obj = crm.customerEditVisitPCNotChk(customerid, name, phone, level_id, visit);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + "，提示语：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加回访，回访记录字数=9");
        }

    }

    @Ignore
    @Test
    public void addVisitComment201() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 201; i++) {
                comment = comment + "回";
            }
            String date = dt.getHistoryDate(1);
            visit.put("comment", comment);
            visit.put("next_return_visit_date", date);
            JSONObject obj = crm.customerEditVisitPCNotChk(customerid, name, phone, level_id, visit);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + "，提示语：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加回访，回访记录字数=201");
        }

    }

    @Ignore
    @Test
    public void addVisitComment0() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            String date = dt.getHistoryDate(1);
            visit.put("comment", comment);
            visit.put("next_return_visit_date", date);
            JSONObject obj = crm.customerEditVisitPCNotChk(customerid, name, phone, level_id, visit);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + "，提示语：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加回访，回访记录字数=0");
        }

    }

    //@Test //前端做了校验
    public void addVisitYesterday() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 10; i++) {
                comment = comment + "回";
            }
            String date = dt.getHistoryDate(-1);
            visit.put("comment", comment);
            visit.put("next_return_visit_date", date);
            JSONObject obj = crm.customerEditVisitPCNotChk(customerid, name, phone, level_id, visit);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + "，提示语：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加回访，回访日期是昨天");
        }

    }

    @Ignore
    @Test
    public void addVisitButtonToContactED() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            String phone1 = phone.substring(3);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //获取回访记录id
            String date = dt.getHistoryDate(0);
            String tomorrow = dt.getHistoryDate(1);

            Long taskid = crm.taskList_PC(date, 0, 1, 1, phone).getJSONArray("list").getJSONObject(0).getLong("id");

            //添加前，不在已联系中
            int totalbefore = crm.taskList_PC(date, 0, 1, 1, phone).getInteger("total");
            //添加回访记录
            crm.customerEditVisitPC_button(customerid, taskid, tomorrow, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            //查询有结果
            int totalafter = crm.taskList_PC(date, 1, 1, 1, phone).getInteger("total");
            Preconditions.checkArgument(totalafter == 1, "记录未出现在已联系中");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("点击列表中回访按钮添加回访，app端由未联系转为已联系");
        }

    }

    @Ignore
    @Test
    public void addVisitDetialNotToContactED() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加前，在未联系
            String date = dt.getHistoryDate(0);
            int totalbefore = crm.taskList_PC(date, 0, 1, 1, phone).getInteger("total");

            //详情页添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 10; i++) {
                comment = comment + "回";
            }
            String date2 = dt.getHistoryDate(1);
            visit.put("comment", comment);
            visit.put("next_return_visit_date", date2);
            crm.customerEditVisitPCNotChk(customerid, name, phone, level_id, visit);

            //添加后，在未联系
            int totalafter = crm.taskList_PC(date, 0, 1, 1, phone).getInteger("total");
            Preconditions.checkArgument(totalafter == 1, "记录不在未联系中");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("顾客详情页添加回访，app端该记录仍为未联系");
        }

    }


    //----------------------添加备注--------------------
    @Test
    public void addVisitRemark20() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 20; i++) {
                comment = comment + "备";
            }
            crm.customerEditRemarkPC(customerid, name, phone, level_id, comment);

            //查看顾客详情，备注条数
            int list = crm.customerDetailPC(customerid).getJSONArray("remark").size();
            Preconditions.checkArgument(list == 2, "添加失败");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加备注，备注字数=20");
        }

    }

    @Test
    public void addVisitRemark200() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 200; i++) {
                comment = comment + "备";
            }
            crm.customerEditRemarkPC(customerid, name, phone1, level_id, comment);

            //查看顾客详情，备注条数
            int list = crm.customerDetailPC(customerid).getJSONArray("remark").size();
            Preconditions.checkArgument(list == 2, "添加失败");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加备注，备注字数=200");
        }

    }

    @Test
    public void addVisitRemarkNum50() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 20; i++) {
                comment = comment + "备";
            }
            for (int i = 0; i < 49; i++) {
                crm.customerEditRemarkPC(customerid, name, phone1, level_id, comment);
            }


            //查看顾客详情，备注条数
            int list = crm.customerDetailPC(customerid).getJSONArray("remark").size();
            Preconditions.checkArgument(list == 50, "添加失败");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加备注，备注条数=50");
        }

    }

    //@Test //前端校验
    public void addVisitRemark19() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 19; i++) {
                comment = comment + "备";
            }
            JSONObject obj = crm.customerEditRemarkPCNotChk(customerid, name, phone, level_id, comment);
            int code = obj.getInteger("code");
            String message = obj.getString("messge");
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + "，提示语：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加备注，备注字数=19");
        }

    }

    //@Test //前端校验
    public void addVisitRemark201() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 200; i++) {
                comment = comment + "备";
            }
            JSONObject obj = crm.customerEditRemarkPCNotChk(customerid, name, phone, level_id, comment);
            int code = obj.getInteger("code");
            String message = obj.getString("messge");
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + "，提示语：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加备注，备注字数=19");
        }

    }

    @Test
    public void addVisitRemarkNum51() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 20; i++) {
                comment = comment + "备";
            }
            for (int i = 0; i < 49; i++) {
                crm.customerEditRemarkPC(customerid, name, phone, level_id, comment);
            }

            JSONObject obj = crm.customerEditRemarkPCNotChk(customerid, name, phone, level_id, comment);
            int code = obj.getInteger("code");
            String message = obj.getString("messge");
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code + "，提示语：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加备注，备注条数=51");
        }

    }


    /**
     * ====================今日来访======================
     */
    //----------------------查询--------------------
    @Ignore
    @Test
    public void todayListSearchAll() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            customerid = creatCust(name, phone);
            //完成接待


            //直接点击查询
            int total = crm.todayListPC(-1, "", "", "", 0, 0, 1, 1).getInteger("total");
            Preconditions.checkArgument(total >= 1, "今日来访数量期待>=1，实际=" + total);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日来访页面直接点击查询按钮");
        }

    }

    @Ignore
    @Test
    public void todayListSearchName() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //姓名查询
            JSONObject obj = crm.todayListPC(-1, name, "", "", 0, 0, 1, 1).getJSONArray("list").getJSONObject(0);
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_name.equals(name), "查询结果与查询条件不一致");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("今日来访页面根据姓名查询");
        }

    }

    @Ignore
    @Test
    public void todayListSearchPhone() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.todayListPC(-1, "", phone1, "", 0, 0, 1, 1).getJSONArray("list").getJSONObject(0);
            String search_phone = obj.getString("customer_phone");
            Preconditions.checkArgument(search_phone.equals(phone1), "查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日来访页面根据手机号查询");
        }

    }

    @Ignore
    @Test
    public void todayListSearchLevel() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONArray list = crm.todayListPC(7, "", "", "", 0, 0, 1, 1).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                Preconditions.checkArgument(single.getString("customer_level_name").equals("H"), "查询结果与查询条件不一致");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日来访页面根据客户等级查询");
        }

    }

    @Ignore
    @Test
    public void todayListSearchNameYPhoneY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.todayListPC(-1, name, phone1, "", 0, 0, 1, 1).getJSONArray("list").getJSONObject(0);
            String search_phone = obj.getString("customer_phone");
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_phone.equals(phone1) && search_name.equals(name), "查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日来访页面根据姓名+手机号查询");
        }

    }

    @Ignore
    @Test
    public void todayListSearchNameYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.todayListPC(7, name, "", "", 0, 0, 1, 1).getJSONArray("list").getJSONObject(0);
            String search_level = obj.getString("customer_level_name");
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_level.equals("H") && search_name.equals(name), "查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日来访页面根据姓名+级别查询");
        }

    }

    @Ignore
    @Test
    public void todayListSearchPhoneYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.todayListPC(7, "", phone1, "", 0, 0, 1, 1).getJSONArray("list").getJSONObject(0);
            String search_level = obj.getString("customer_level_name");
            String search_phone = obj.getString("customer_phone");
            Preconditions.checkArgument(search_level.equals("H") && search_phone.equals(phone1), "查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日来访页面根据手机号+级别查询");
        }

    }

    @Ignore
    @Test
    public void todayListSearchNameYPhoneYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            String phone1 = phone.substring(3);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.todayListPC(7, name, phone1, "", 0, 0, 1, 1).getJSONArray("list").getJSONObject(0);
            String search_level = obj.getString("customer_level_name");
            String search_phone = obj.getString("customer_phone");
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_level.equals("H") && search_phone.equals(phone1) && search_name.equals(name), "查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日来访页面根据姓名+手机号+级别查询");
        }

    }

    @Ignore
    @Test
    public void todayListSearchNameYPhoneN() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            int total = crm.todayListPC(-1, name, phone + "1", "", 0, 0, 1, 1).getInteger("total");

            Preconditions.checkArgument(total == 0, "有查询结果");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日来访页面根据存在姓名+不匹配手机号查询");
        }

    }

    @Ignore
    @Test
    public void todayListSearchNameYLevelN() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            int total = crm.todayListPC(5, name, "", "", 0, 0, 1, 1).getInteger("total");

            Preconditions.checkArgument(total == 0, "不应有查询结果");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日来访页面根据存在姓名+不匹配级别查询");
        }

    }

    @Ignore
    @Test
    public void todayListSearchPhoneYLevelN() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            int total = crm.todayListPC(3, "", phone, "", 0, 0, 1, 1).getInteger("total");

            Preconditions.checkArgument(total == 0, "不应有查询结果");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日来访页面根据存在手机号+不匹配级别查询");
        }

    }

    @Ignore
    @Test
    public void todayListSearchNameNPhoneYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            int total = crm.todayListPC(7, name + "1", phone, "", 0, 0, 1, 1).getInteger("total");

            Preconditions.checkArgument(total == 0, "不应有查询结果");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日来访页面根据不存在的姓名+存在手机号+匹配的级别查询");
        }

    }

    @Ignore
    @Test
    public void customerListSearchLevel() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            //完成接待


            //查询
            JSONArray list = crm.todayListPC(7, "", "", "", 0, 0, 1, 1).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                Preconditions.checkArgument(single.getString("customer_level_name").equals("H"), "查询结果与查询条件不一致");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日来访页面根据客户等级查询");
        }

    }


    /**
     *
     * ====================我的试驾 V3。0 取消页面======================
     * */
    //----------------------查询--------------------

//    @Test
//    public void driverListSearchAll() {
//        logger.logCaseStart(caseResult.getCaseName());
//        Long customerid=-1L;
//        int driverid = -1;
//        try {
//            long level_id=7L;
//            String phone = "12312341234";
//            String name = dt.getHistoryDate(0);
//            String desc = "创建H级客户自动化------------------------------------";
//            //创建试驾
//            String idCard = "110226198210260078";
//            String gender = "男";
//            String signTime = dt.getHistoryDate(0);
//            String model = "718";
//            String country = "中国";
//            String city = "图们";
//            String email = dt.getHistoryDate(0)+"@qq.com";
//            String address = "北京市昌平区";
//            String ward_name = "小小";
//            String driverLicensePhoto1Url = picurl;
//            String driverLicensePhoto2Url = picurl;
//            String electronicContractUrl = picurl;
//            driverid = crm.driveradd(name,idCard,gender,phone,signTime,"试乘试驾",model,country,city,email,address,ward_name,driverLicensePhoto1Url,driverLicensePhoto2Url,electronicContractUrl).getInteger("id");
//
//            //直接点击查询
//            int total = crm.driveList(signTime,"","",1,1).getInteger("total");
//            Preconditions.checkArgument(total>=1,"我的试驾数量期待>=1，实际="+total);
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("我的试驾页面直接点击查询按钮");
//        }
//
//    }
//
//    @Test
//    public void driverListSearchName() {
//        logger.logCaseStart(caseResult.getCaseName());
//        Long customerid=-1L;
//        int driverid = -1;
//        try {
//            long level_id=7L;
//            String phone = "1";
//            for (int i = 0; i < 10;i++){
//                String a = Integer.toString((int)(Math.random()*10));
//                phone = phone + a;
//            }
//            String name = dt.getHistoryDate(0);
//            String desc = "创建H级客户自动化------------------------------------";
//            //创建试驾
//            String idCard = "110226198210260078";
//            String gender = "男";
//            String signTime = dt.getHistoryDate(0);
//            String model = "718";
//            String country = "中国";
//            String city = "图们";
//            String email = dt.getHistoryDate(0)+"@qq.com";
//            String address = "北京市昌平区";
//            String ward_name = "小小";
//            String driverLicensePhoto1Url = picurl;
//            String driverLicensePhoto2Url = picurl;
//            String electronicContractUrl = picurl;
//            driverid = crm.driveradd(name,idCard,gender,phone,signTime,"试乘试驾",model,country,city,email,address,ward_name,driverLicensePhoto1Url,driverLicensePhoto2Url,electronicContractUrl).getInteger("id");
//
//            //直接点击查询
//            JSONArray obj = crm.driveList(signTime,name,"",1,20).getJSONArray("list");
//            for (int i = 0; i < obj.size();i++){
//                JSONObject single = obj.getJSONObject(i);
//                String search_name = single.getString("customer_name");
//                Preconditions.checkArgument(search_name.equals(name),"查询结果与查询条件不一致");
//            }
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("我的试驾页面根据客户姓名搜索");
//        }
//
//    }
//
//    @Test
//    public void driverListSearchPhone() {
//        logger.logCaseStart(caseResult.getCaseName());
//        Long customerid=-1L;
//        int driverid = -1;
//        try {
//            long level_id=7L;
//            String phone = "1";
//            for (int i = 0; i < 10;i++){
//                String a = Integer.toString((int)(Math.random()*10));
//                phone = phone + a;
//            }
//            String name = dt.getHistoryDate(0);
//            String desc = "创建H级客户自动化------------------------------------";
//            //创建试驾
//            String idCard = "110226198210260078";
//            String gender = "男";
//            String signTime = dt.getHistoryDate(0);
//            String model = "718";
//            String country = "中国";
//            String city = "图们";
//            String email = dt.getHistoryDate(0)+"@qq.com";
//            String address = "北京市昌平区";
//            String ward_name = "小小";
//            String driverLicensePhoto1Url = picurl;
//            String driverLicensePhoto2Url = picurl;
//            String electronicContractUrl = picurl;
//            driverid = crm.driveradd(name,idCard,gender,phone,signTime,"试乘试驾",model,country,city,email,address,ward_name,driverLicensePhoto1Url,driverLicensePhoto2Url,electronicContractUrl).getInteger("id");
//
//            //直接点击查询
//            JSONArray obj = crm.driveList(signTime,"",phone,1,20).getJSONArray("list");
//            for (int i = 0; i < obj.size();i++){
//                JSONObject single = obj.getJSONObject(i);
//                String search_phone = single.getString("customer_phone_number");
//                Preconditions.checkArgument(search_phone.equals(phone),"查询结果与查询条件不一致");
//            }
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("我的试驾页面根据客户手机号搜索");
//        }
//
//    }
//
//    @Test
//    public void driverListSearchNameYPhoneY() {
//        logger.logCaseStart(caseResult.getCaseName());
//        Long customerid=-1L;
//        int driverid = -1;
//        try {
//            long level_id=7L;
//            String phone = "1";
//            for (int i = 0; i < 10;i++){
//                String a = Integer.toString((int)(Math.random()*10));
//                phone = phone + a;
//            }
//            String name = dt.getHistoryDate(0);
//            String desc = "创建H级客户自动化------------------------------------";
//            //创建试驾
//            String idCard = "110226198210260078";
//            String gender = "男";
//            String signTime = dt.getHistoryDate(0);
//            String model = "718";
//            String country = "中国";
//            String city = "图们";
//            String email = dt.getHistoryDate(0)+"@qq.com";
//            String address = "北京市昌平区";
//            String ward_name = "小小";
//            String driverLicensePhoto1Url = picurl;
//            String driverLicensePhoto2Url = picurl;
//            String electronicContractUrl = picurl;
//            driverid = crm.driveradd(name,idCard,gender,phone,signTime,"试乘试驾",model,country,city,email,address,ward_name,driverLicensePhoto1Url,driverLicensePhoto2Url,electronicContractUrl).getInteger("id");
//
//            //直接点击查询
//            JSONArray obj = crm.driveList(signTime,name,phone,1,20).getJSONArray("list");
//            for (int i = 0; i < obj.size();i++){
//                JSONObject single = obj.getJSONObject(i);
//                String search_phone = single.getString("customer_phone_number");
//                String search_name = single.getString("customer_name");
//                Preconditions.checkArgument(search_phone.equals(phone)&&search_name.equals(name),"查询结果与查询条件不一致");
//            }
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("我的试驾页面根据姓名+手机号搜索");
//        }
//
//    }
//
//    @Test
//    public void driverListSearchNameNPhoneY() {
//        logger.logCaseStart(caseResult.getCaseName());
//        Long customerid=-1L;
//        int driverid = -1;
//        try {
//            long level_id=7L;
//            String phone = "1";
//            for (int i = 0; i < 10;i++){
//                String a = Integer.toString((int)(Math.random()*10));
//                phone = phone + a;
//            }
//            String name = dt.getHistoryDate(0);
//            String desc = "创建H级客户自动化------------------------------------";
//            //创建试驾
//            String idCard = "110226198210260078";
//            String gender = "男";
//            String signTime = dt.getHistoryDate(0);
//            String model = "718";
//            String country = "中国";
//            String city = "图们";
//            String email = dt.getHistoryDate(0)+"@qq.com";
//            String address = "北京市昌平区";
//            String ward_name = "小小";
//            String driverLicensePhoto1Url = picurl;
//            String driverLicensePhoto2Url = picurl;
//            String electronicContractUrl = picurl;
//            driverid = crm.driveradd(name,idCard,gender,phone,signTime,"试乘试驾",model,country,city,email,address,ward_name,driverLicensePhoto1Url,driverLicensePhoto2Url,electronicContractUrl).getInteger("id");
//
//            //查询
//            int total = crm.driveList(signTime,name+"1",phone,1,20).getInteger("total");
//            Preconditions.checkArgument(total==0,"不应有查询结果");
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("我的试驾页面根据不存在姓名+手机号搜索");
//        }
//
//    }
//
//
//
//    /**
//     *
//     * ====================我的交车======================
//     * */
//    //----------------------查询--------------------
//    @Test
//    public void deliverListSearchAll() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //新建交车
//
//            String name = dt.getHHmm(0);
//            String gender = "男";
//            String signTime = dt.getHistoryDate(0);
//            String model = "718";
//            String customer_phone = "1";
//            for (int i = 0; i < 10;i++){
//                String a = Integer.toString((int)(Math.random()*10));
//                customer_phone = customer_phone + a;
//            }
//            int id = crm.deliverAdd(name, gender, customer_phone, signTime, model, picurl).getInteger("id");
//            int total = crm.deliverList(signTime,1,1,"","").getInteger("total");
//            Preconditions.checkArgument(total>=1,"我的交车数量期待>=1，实际="+total);
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("我的交车页面直接点击查询按钮");
//        }
//
//    }
//
//    @Test
//    public void deliverListSearchName() {
//        logger.logCaseStart(caseResult.getCaseName());
//
//        try {
//            //新建交车
//            String name = dt.getHHmm(0);
//            String gender = "男";
//            String signTime = dt.getHistoryDate(0);
//            String model = "718";
//            String customer_phone = "1";
//            for (int i = 0; i < 10;i++){
//                String a = Integer.toString((int)(Math.random()*10));
//                customer_phone = customer_phone + a;
//            }
//            int id = crm.deliverAdd(name, gender, customer_phone, signTime, model, picurl).getInteger("id");
//            JSONArray obj = crm.deliverList(signTime,1,1,name,"").getJSONArray("list");
//            for (int i = 0; i < obj.size();i++){
//                JSONObject single = obj.getJSONObject(i);
//                String search_name = single.getString("customer_name");
//                Preconditions.checkArgument(search_name.equals(name),"查询结果与查询条件不一致");
//            }
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("我的交车页面根据姓名查询");
//        }
//
//    }


    /**
     *
     * ====================展厅接待======================
     * */

//    @Test
//    public void inToWait() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //销售自动化转为空闲
//            crm.updateStatus("RECEPTIVE");
//            //销售自动化2创建客户
//            crm.login(salename2,salepwd2);
//            String phone = "1";
//            for (int i = 0; i < 10;i++){
//                String a = Integer.toString((int)(Math.random()*10));
//                phone = phone + a;
//            }
//            String name = phone;
//            //获取顾客id
//            Long customerid = crm.getCustomerId();
//            //创建某级客户
//            JSONObject customer = crm.customerEdit_onlyNec(customerid,7,name,phone,"H级客户-taskListChkNum-修改时间为昨天");
//            //完成接待之前为接待中,总经理登陆 接待中转等待
//            crm.login(cstm.xszj,cstm.pwd);
//            //展厅接待列表获取该记录id
//            int orderid = crm.customerTodayList().getJSONArray("list").getJSONObject(0).getInteger("id");
//            //修改客户状态 0:接待中, 1:离店, 2:等待中
//            int code = crm.reception(orderid,sale_id,2).getInteger("code");
//            //完成接待
//            crm.login(salename2,salepwd2);
//
//
//            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+ code);
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//           crm.login(cstm.lxqgw,cstm.pwd);
//            saveData("展厅接待接待中客户转等待");
//        }
//    }
//
//    @Test
//    public void inToLeave() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //销售自动化转为空闲
//            crm.updateStatus("RECEPTIVE");
//            //销售自动化2创建客户
//            crm.login(salename2,salepwd2);
//            crm.updateStatus("RECEPTIVE");
//            String phone = "1";
//            for (int i = 0; i < 10;i++){
//                String a = Integer.toString((int)(Math.random()*10));
//                phone = phone + a;
//            }
//            String name = phone;
//            //获取顾客id
//            Long customerid = crm.getCustomerId();
//            //创建某级客户
//            JSONObject customer = crm.customerEdit_onlyNec(customerid,7,name,phone,"H级客户-taskListChkNum-修改时间为昨天");
//            //完成接待之前为接待中,总经理登陆 接待中转等待
//            crm.login(cstm.xszj,cstm.pwd);
//            //展厅接待列表获取该记录id
//            int orderid = crm.customerTodayList().getJSONArray("list").getJSONObject(0).getInteger("id");
//            //修改客户状态 0:接待中, 1:离店, 2:等待中
//            String leavetime = dt.getHHmm(0);
//            int code = crm.reception(orderid,sale_id,1).getInteger("code");
//            //完成接待
////            crm.login(salename2,salepwd2);
////
//
//            Preconditions.checkArgument(code==1000,"状态码期待1000，实际"+ code);
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//           crm.login(cstm.lxqgw,cstm.pwd);
//            saveData("展厅接待接待中客户转离店");
//        }
//    }


    /**
     * ====================状态流转======================
     */
    //@Test
//    public void  RecToRec(){
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //完成接待
//
//            //转为空闲，保证当前状态=空闲
//            crm.updateStatus("RECEPTIVE");
//            //空闲转空闲
//            crm.updateStatus("RECEPTIVE");
//            //查询当前状态=空闲
//            String now = crm.userStatus().getString("user_status");
//            Preconditions.checkArgument(now.equals("RECEPTIVE"),"转换后状态="+now);
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("空闲转空闲");
//        }
//    }
    @Test //服务端没做校验
    public void RecToIn() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
//            //完成接待
//
//            //转为空闲，保证当前状态=空闲
//            crm.updateStatus("RECEPTIVE");
//            //空闲转接待中
//            crm.updateStatus("IN_RECEPTION");
//            //查询当前状态=空闲
//            String now = crm.userStatus().getString("user_status");
//            Preconditions.checkArgument(now.equals("RECEPTIVE"),"转换后状态="+now);

            //删除账号
//            JSONArray list = crm.userPage(1,100).getJSONArray("list");
//            for (int j = 0; j < list.size(); j++) {
//                JSONObject single = list.getJSONObject(j);
//                if (single.getString("user_login_name").contains("159")){
//                    String userid = single.getString("user_id"); //获取用户id
//                    crm.userDel(userid);
//                }
//            }
            Long cid = creatCust("aa", "aa"); //898


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("XXXXXX");
        }
    }


    //创建顾客
//    public Long creatCust(String name, String phone) throws Exception {
//        //获取顾客id
//        Long customerid = crm.getCustomerId();
//        //创建某级客户
//        JSONObject customer = crm.customerEdit_onlyNec(customerid,7,name,phone,"H级客户-taskListChkNum-修改时间为昨天");
//        return  customerid;
//
//    }


    //前台点击创建接待按钮创建顾客
    public Long creatCust(String name, String phone) throws Exception {
        //前台登陆
        crm.login(cstm.qt, cstm.pwd);
        Long customerid = -1L;
        //获取当前空闲第一位销售id

        String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
        //
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
        crm.login(userLoginName, cstm.pwd);
        customerid = crm.userInfService().getLong("customer_id");
        //创建某级客户
        if (name.equals("")) {
            String name1 = "zdh";
            String phone1 = "zdh" + (int) ((Math.random() * 9 + 1) * 100000);
            JSONObject customer = crm.finishReception(customerid, 7, name1, phone1, "自动化---------创建----------H级客户");

        } else {
            JSONObject customer = crm.finishReception(customerid, 7, name, phone.substring(3), "自动化---------创建----------H级客户");

        }

        return customerid;
    }

    @Test(description = "小程序“我的”预约试驾数=列表数", enabled = false)
    public void appointmentTestDriver1() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            CommonUtil.loginApplet(EnumAppletCode.WM);
//            JSONObject response = crm.appointmentList(0L, "TEST_DRIVE", 100);
//            int total = CommonUtil.getIntField(response, "total");
//            CommonUtil.login(EnumAccount.XSGWTEMP);
//            JSONObject response1 = crm.appointmentTestDriverList("", "", "", 1, 2 << 20);
//            JSONArray list = response1.getJSONArray("list");
//            int num = 0;
//            for (int i = 0; i < list.size(); i++) {
//                if (list.getJSONObject(i).getString("customer_name").equals("【自动化】王")) {
//                    num++;
//                }
//            }
//            CommonUtil.valueView(total, num);
//            Preconditions.checkArgument(total == num, "小程序我的试驾列表数量！=app我的预约该顾客预约的次数");
//        } catch (Exception | AssertionError e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("小程序“我的”预约试驾数=列表数");
//        }
    }


    @Test
    public void addVisitRemarkChkNum() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            customerid = creatCust(name, phone);

            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查看顾客详情，备注条数
            int listbefore = crm.customerDetailPC(customerid).getJSONArray("remark").size();

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 20; i++) {
                comment = comment + "备";
            }
            crm.customerEditRemarkPC(customerid, name, phone1, level_id, comment);

            //查看顾客详情，备注条数
            int listafter = crm.customerDetailPC(customerid).getJSONArray("remark").size();
            int change = listafter - listbefore;
            Preconditions.checkArgument(change == 1, "备注数增加" + change);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加备注，备注条数+1");
        }

    }

    @Test
    public void addVisitCommentChkNum() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String phone1 = phone.substring(3);
            String name = phone;
            customerid = creatCust(name, phone);

            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id


            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 10; i++) {
                comment = comment + "回";
            }
            String date = dt.getHistoryDate(1);
            visit.put("comment", comment);
            visit.put("next_return_visit_date", date);
            crm.customerEditVisitPC(customerid, name, phone1, level_id, visit);

            //查看顾客详情，回访记录条数
            int listbefore = crm.customerDetailPC(customerid).getJSONArray("return_visit").size();

            crm.customerEditVisitPC(customerid, name, phone1, level_id, visit);
            //查看顾客详情，回访记录条数
            int listafter = crm.customerDetailPC(customerid).getJSONArray("return_visit").size();
            int change = listafter - listbefore;
            Preconditions.checkArgument(change == 1, "回访记录数量增加了" + change);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加回访，回访记录数量+1");
        }

    }

    /**
     * ====================展厅接待======================
     */

//    @Test
//    public void inToWait() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //销售自动化转为空闲
//            crm.updateStatus("RECEPTIVE");
//            //销售自动化2创建客户
//            crm.login(salename2,salepwd2);
//            String phone = "1";
//            for (int i = 0; i < 10;i++){
//                String a = Integer.toString((int)(Math.random()*10));
//                phone = phone + a;
//            }
//            String name = phone;
//            //获取顾客id
//            Long customerid = crm.getCustomerId();
//            //创建某级客户
//            JSONObject customer = crm.customerEdit_onlyNec(customerid,7,name,phone,"H级客户-taskListChkNum-修改时间为昨天");
//            //完成接待之前为接待中,总经理登陆 接待中转等待
//            crm.login(cstm.xszj,cstm.pwd);
//            //展厅接待列表获取该记录id
//            int orderid = crm.customerTodayList().getJSONArray("list").getJSONObject(0).getInteger("id");
//            //修改客户状态 0:接待中, 1:离店, 2:等待中
//            int code = crm.reception(orderid,sale_id,2).getInteger("code");
//            //完成接待
//            crm.login(salename2,salepwd2);
//
//
//            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+ code);
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//           crm.login(cstm.lxqgw,cstm.pwd);
//            saveData("展厅接待接待中客户转等待");
//        }
//    }
//
//    @Test
//    public void inToLeave() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //销售自动化转为空闲
//            crm.updateStatus("RECEPTIVE");
//            //销售自动化2创建客户
//            crm.login(salename2,salepwd2);
//            crm.updateStatus("RECEPTIVE");
//            String phone = "1";
//            for (int i = 0; i < 10;i++){
//                String a = Integer.toString((int)(Math.random()*10));
//                phone = phone + a;
//            }
//            String name = phone;
//            //获取顾客id
//            Long customerid = crm.getCustomerId();
//            //创建某级客户
//            JSONObject customer = crm.customerEdit_onlyNec(customerid,7,name,phone,"H级客户-taskListChkNum-修改时间为昨天");
//            //完成接待之前为接待中,总经理登陆 接待中转等待
//            crm.login(cstm.xszj,cstm.pwd);
//            //展厅接待列表获取该记录id
//            int orderid = crm.customerTodayList().getJSONArray("list").getJSONObject(0).getInteger("id");
//            //修改客户状态 0:接待中, 1:离店, 2:等待中
//            String leavetime = dt.getHHmm(0);
//            int code = crm.reception(orderid,sale_id,1).getInteger("code");
//            //完成接待
////            crm.login(salename2,salepwd2);
////
//
//            Preconditions.checkArgument(code==1000,"状态码期待1000，实际"+ code);
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//           crm.login(cstm.lxqgw,cstm.pwd);
//            saveData("展厅接待接待中客户转离店");
//        }
//    }


    //---------------------销售状态-------------

    //@Test
    //app点击创建顾客按钮时，会调用修改销售状态的接口，状态不是自动转的，用例作废
    public void customerListsaleStatus() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);

            //查看销售状态
            String status1 = crm.userStatus().getString("user_status");
            Preconditions.checkArgument(status1.equals("BUSY"), "销售创建客户后，状态期待为BUSY，实际为" + status1);


            //完成接待


            //查看销售状态
            String status2 = crm.userStatus().getString("user_status");
            Preconditions.checkArgument(status2.equals("RECEPTIVE"), "销售完成接待后，状态期待为RECEPTIVE，实际为" + status1);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售顾问状态");
        }

    }


    //----------------------展示--------------------
    @Test
    public void customerListShowAll() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(cstm.lxqgw, cstm.pwd);
            //查询
            JSONArray list = crm.customerListPC("", -1, "", "", "", "", 1, 50).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                Preconditions.checkArgument(single.getString("belongs_sale_name").equals(cstm.lxqgw), "展示信息不正确");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的客户页面所属销售的顾客信息");
        }

    }

    //----------------------删除--------------------
    @Test
    public void customerListDel() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            customerid = creatCust(name, phone);
            //完成接待

            //姓名+手机号查询
            int total = crm.customerListPC("", -1, name, phone1, 0, 0, 1, 1).getInteger("total");
            Preconditions.checkArgument(total == 1, "删除前查询，期待有一条记录，实际" + total);

            //总经理登陆
            crm.login(cstm.xszj, cstm.pwd);
            //删除顾客
            crm.customerDeletePC(customerid);

            //销售顾问登陆
            crm.login(cstm.lxqgw, cstm.pwd);
            //再次查询应无结果
            int total2 = crm.customerListPC("", -1, name, phone1, 0, 0, 1, 1).getInteger("total");
            Preconditions.checkArgument(total2 == 0, "删除后查询，期待无结果，实际" + total);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(cstm.lxqgw, cstm.pwd);
            saveData("我的客户页面删除后再查询");
        }

    }

    //@Test
    public void customerListDelInService() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            //总经理登陆
            crm.login(cstm.xszj, cstm.pwd);
            //删除顾客
            int code = crm.customerDeletePCNotChk(customerid).getInteger("code");
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);
            crm.login(cstm.lxqgw, cstm.pwd);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(cstm.lxqgw, cstm.pwd);
            saveData("删除接待中客户");
        }

    }

    @Test
    public void customerListDelServiced() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);

            //总经理登陆
            crm.login(cstm.xszj, cstm.pwd);
            //删除顾客
            int code = crm.customerDeletePCNotChk(customerid).getInteger("code");
            Preconditions.checkArgument(code == 1000, "删除失败");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(cstm.lxqgw, cstm.pwd);
            saveData("删除已完成接待客户");
        }

    }


    //---------------------编辑顾客信息-------------
    @Test
    public void customerListsaleEditPhone() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {


            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String phone1 = phone.substring(3);
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";


            customerid = creatCust(name, phone);
            //完成接待


            crm.customerEditPC(customerid, name, "12312341234", 2);

            //再次查询，手机号应不变
            JSONObject obj = crm.customerListPC("", -1, name, "", "", "", 1, 1).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(obj.getString("customer_phone").equals(phone1), "手机号改变");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售顾问修改顾客手机号");
        }

    }

    @Test
    public void customerListzjlEditPhone() {
        logger.logCaseStart(caseResult.getCaseName());

        try {

            //销售总监登陆
            crm.login(cstm.xszj, cstm.pwd);
            //查询B级客户
            JSONObject obj = crm.customerListPC("", 2, "", "", "", "", 1, 1).getJSONArray("list").getJSONObject(0);
            Long customerid = obj.getLong("customer_id");
            String name = obj.getString("customer_name");
            String phone = obj.getString("customer_phone");
            String phone1 = "zdh" + (int) ((Math.random() * 9 + 1) * 100000);
            //销售总监修改客户手机号
            crm.customerEditRemarkPC(customerid, name, phone1, 2L, "自动化销售总监修改手机号---------------");
            //再次查询，手机号应改变
            JSONObject obj2 = crm.customerListPC("", 2, name, "", "", "", 1, 1).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(obj2.getString("customer_phone").equals(phone1), "手机号未改变");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售总监修改顾客手机号");
        }

    }

    //@Test
    public void customerListsaleEditsale() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            customerid = creatCust(name, phone);

            //销售总监登陆
            crm.login(cstm.xszj, cstm.pwd);
            crm.customerEditsale(customerid, name, phone.substring(3), "uid_9c2b914d");

            //再次查询，手机号应不变
            JSONObject obj = crm.customerListPC("", -1, name, "", "", "", 1, 1).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(obj.getString("belongs_sale_id").equals("uid_9c2b914d"), "所属顾问改变");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售总监修改顾客所属顾问");
        }

    }

    @Test
    public void customerListzjlEditsale() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {
            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";


            customerid = creatCust(name, phone);
            //完成接待


            //总经理登陆
            crm.login(cstm.xszj, cstm.pwd);
            crm.customerEditsale(customerid, name, phone.substring(3), "uid_8861b7fd");

            //再次查询，手机号应不变
            JSONObject obj = crm.customerListPC("", -1, name, "", "", "", 1, 1).getJSONArray("list").getJSONObject(0);
            crm.login(cstm.lxqgw, cstm.pwd);
            Preconditions.checkArgument(obj.getString("belongs_sale_id").equals("uid_8861b7fd"), "所属顾问未改变");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("总经理修改顾客所属顾问");
        }

    }

    //@Test
    public void customerListsaleEditSeveral() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            String phone = "" + System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            String desc = "创建H级客户自动化------------------------------------";

            String time = dt.getHistoryDate(0);

            customerid = creatCust(name, phone);
            //完成接待


            crm.customerEditPC(customerid, name, phone1, 2, 2, 0, time, 1, 1, 0);

            //再次查询
            JSONObject obj = crm.customerListPC("", -1, name, phone1, "", "", 1, 1).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(obj.getInteger("like_car") == 2, "like_car修改失败");
            Preconditions.checkArgument(obj.getInteger("pay_type") == 0, "pay_type修改失败");
            Preconditions.checkArgument(obj.getInteger("show_price") == 1, "show_price修改失败");
            Preconditions.checkArgument(obj.getInteger("test_drive_car") == 1, "test_drive_car修改失败");
            Preconditions.checkArgument(obj.getInteger("visit_count") == 0, "visit_count修改失败");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售顾问修改顾客多项信息");
        }

    }


    @Test(dataProvider = "car", dataProviderClass = CrmScenarioUtil.class)
    public void uploadEnterShopCarPlate(String car) {

        String carNum = car;

        String router = "/business/porsche/PLATE_UPLOAD/v1.0";
        //设备与日常环境的设置一致，不要修改
        String deviceId = "7709867521115136";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        ImageUtil imageUtil = new ImageUtil();
        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
        String json = "{\"plate_num\":\"" + carNum + "\"," +
                "\"plate_pic\":\"@0\"," +
                "\"time\":\"" + System.currentTimeMillis() + "\"" +
                "}";
        try {
            crm.carUploadToDaily(router, deviceId, resource, json);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("日常入场车牌号上传");
        }
    }


    @Override
    public void initial() {

    }

    @Override
    public void clean() {

    }

    @Override
    public void createFreshCase(Method method) {

    }
}
