package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import com.haisheng.framework.util.JsonpathUtil;
import org.joda.time.DateTime;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author : yu
 * @date :  2020/05/30
 */

public class CrmSystemCase extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    String sale_id = ""; //销售顾问id
    //销售顾问
    String saleShowName = "销售顾问-自动化";
    String salename1 = "xiaoshouguwen";
    String salepwd1 = "ab6c2349e0bd4f3c886949c3b9cb1b7b";
    //前台
    String qiantaiShowName = "前台-自动化测试";
    String qiantainame = "lxq_test_qiantai";
    String qiantaipwd = "ab6c2349e0bd4f3c886949c3b9cb1b7b";
    //总经理
    String zjlShowName = "自动化勿动";
    String zjlname = "win";
    String zjlpwd = "0b08bd98d279b88859b628cd8c061ae0";
    //根账号
    String baoshijie = "baoshijie";
    String bpwd = "e10adc3949ba59abbe56e057f20f883e";


    FileUtil fileUtil = new FileUtil();
    String jpgPath = "src/main/java/com/haisheng/framework/testng/bigScreen/dailyImages/2019-10-22_1.jpg";
    String picurl = fileUtil.getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/dailyImages/2019-10-22_1.jpg");

    String phone = "一个假的手机号"+dt.getHistoryDate(0);


    public void clearCustomer(long customerid)  throws Exception{
        if( customerid!=-1L){
            try {
                crm.finishReception();
                //总经理登陆删除客户
                crm.login(zjlname,zjlpwd);
                //删除顾客
                crm.customerDeletePC(customerid);
            } catch (Exception e) {
                throw e;
            } finally {
                crm.login(salename1,salepwd1);
            }
        }
    }

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     *
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
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        crm.login(salename1, salepwd1);

    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     *
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    /**
     *
     * ====================工作安排======================
     * */

    @Test
    public void addScheduledesc10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc="交车服务描述交车服务";

            int startM = 60;
            String starttime = dt.getHHmm(startM);//当前时间
            String endtime = dt.getHHmm(10+startM);//1分钟之后
            Long scheduleid = crm.scheduleAdd_PC(schedulename,scheduledesc,scheduledate,starttime,endtime).getLong("id");

            //删除工作安排
            crm.scheduleDel_PC(scheduleid);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端添加工作安排，工作描述字数=10");

        }


    }

    @Test
    public void addScheduledesc200() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc="";
            for(int i = 0; i < 200;i++){
                scheduledesc = scheduledesc + "啊";
            }
            int startM = 60;
            String starttime = dt.getHHmm(startM);//当前时间
            String endtime = dt.getHHmm(10+startM);//1分钟之后
            Long scheduleid = crm.scheduleAdd_PC(schedulename,scheduledesc,scheduledate,starttime,endtime).getLong("id");

            //删除工作安排
            crm.scheduleDel_PC(scheduleid);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端添加工作安排，工作描述字数=200");

        }

    }

    @Test
    public void addScheduledesc9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc="交车服务描述交车服";

            int startM = 60;
            String starttime = dt.getHHmm(startM);//当前时间
            String endtime = dt.getHHmm(10+startM);//1分钟之后
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename,scheduledesc,scheduledate,starttime,endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待1001，实际"+code+"，提示"+message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端添加工作安排，工作描述字数=9");

        }


    }

    @Test
    public void addScheduledesc201() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc = "";
            for(int i = 0; i < 200;i++){
                scheduledesc = scheduledesc + "啊";
            }

            int startM = 60;
            String starttime = dt.getHHmm(startM);//当前时间
            String endtime = dt.getHHmm(10+startM);//1分钟之后
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename,scheduledesc,scheduledate,starttime,endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待1001，实际"+code+"，提示"+message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端添加工作安排，工作描述字数=201");

        }


    }

    @Test
    public void addSchedule_startGTend() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc="添加工作安排，开始时间>结束时间";

            int startM = 60;
            String starttime = dt.getHHmm(10+startM);//当前时间
            String endtime = dt.getHHmm(startM);//1分钟之后
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename,scheduledesc,scheduledate,starttime,endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待1001，实际"+code+"，提示"+message);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端添加工作安排，开始时间>结束时间");

        }


    }

    @Test
    public void addScheduleRetime() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc="交车服务描述交车服务";

            int startM = 60;
            String starttime = dt.getHHmm(startM);//当前时间
            String endtime = dt.getHHmm(10+startM);//1分钟之后
            Long scheduleid = crm.scheduleAdd_PC(schedulename,scheduledesc,scheduledate,starttime,endtime).getLong("id");

            //创建时间重叠的工作安排
            String starttime1 = dt.getHHmm(5+startM);
            String endtime1 = dt.getHHmm(15+startM);
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename,scheduledesc,scheduledate,starttime1,endtime1);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待1001，实际"+code+"，提示"+message);
            //删除工作安排
            crm.scheduleDel_PC(scheduleid);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端添加工作安排，工作时间与已存在的安排时间重叠");

        }


    }

    @Test
    public void addScheduleEndLTNow() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc="交车服务描述交车服务";


            String starttime = dt.getHHmm(-15);//15分钟前
            String endtime = dt.getHHmm(-5);//5分钟前
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename,scheduledesc,scheduledate,starttime,endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待1001，实际"+code+"，提示"+message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端添加工作安排，开始时间<结束时间<当前时间");

        }


    }

    @Test
    public void addSchedulePeriodTL10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc="交车服务描述交车服务";

            int startM = 60;
            String starttime = dt.getHHmm(startM);//当前时间
            String endtime = dt.getHHmm(5+startM);//1分钟之后
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename,scheduledesc,scheduledate,starttime,endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待1001，实际"+code+"，提示"+message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端添加工作安排，结束时间与开始时间间隔为5分钟");

        }


    }


    /**
     *
     * ====================我的回访====================== 修改数据库
     * */

    //--------------------------查询--------------------

    @Test
    public void taskListSearchAll() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //直接点击查询
            int total = crm.taskList_PC("",-1,1,1,"").getInteger("total");
            Preconditions.checkArgument(total>=1,"我的回访数量期待>=1，实际="+total);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访页面直接点击查询按钮");
        }

    }

    @Test
    public void taskListSearchName() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //姓名查询
            JSONObject obj = crm.customerListPC("",-1,name,"",0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_name.equals(name),"查询结果与查询条件不一致");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            try{
                clearCustomer(customerid);
            }catch(Exception e){
                e.printStackTrace();
            }
            saveData("我的回访页面根据姓名查询");
        }

    }

    @Test
    public void taskListSearchPhone() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.customerListPC("",-1,"",phone,0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_phone = obj.getString("customer_phone");
            Preconditions.checkArgument(search_phone.equals(phone),"查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访页面根据手机号查询");
        }

    }

    @Test
    public void taskListSearchLevel() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONArray list = crm.customerListPC("",7,"","",0,0,1,1).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                Preconditions.checkArgument(single.getString("customer_level").equals("H"),"查询结果与查询条件不一致");
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访页面根据客户等级查询");
        }

    }

    @Test
    public void taskListSearchNameYPhoneY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.customerListPC("",-1,name,phone,0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_phone = obj.getString("customer_phone");
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_phone.equals(phone)&&search_name.equals(name),"查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访页面根据姓名+手机号查询");
        }

    }

    @Test
    public void taskListSearchNameYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.customerListPC("",7,name,"",0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_level = obj.getString("customer_level_name");
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_level.equals("H")&&search_name.equals(name),"查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访页面根据姓名+级别查询");
        }

    }

    @Test
    public void taskListSearchPhoneYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.customerListPC("",7,"",phone,0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_level = obj.getString("customer_level_name");
            String search_phone = obj.getString("customer_phone");
            Preconditions.checkArgument(search_level.equals("H")&&search_phone.equals(phone),"查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访页面根据手机号+级别查询");
        }

    }

    @Test
    public void taskListSearchNameYPhoneYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            JSONObject obj = crm.customerListPC("",7,"",phone,0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_level = obj.getString("customer_level_name");
            String search_phone = obj.getString("customer_phone");
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_level.equals("H")&&search_phone.equals(phone) && search_name.equals(name),"查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访页面根据姓名+手机号+级别查询");
        }

    }

    @Test
    public void taskListSearchNameYPhoneN() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            int total = crm.customerListPC("",-1,name,phone+"1",0,0,1,1).getInteger("total");

            Preconditions.checkArgument(total==0,"有查询结果");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访页面根据存在姓名+不匹配手机号查询");
        }

    }

    @Test
    public void taskListSearchNameYLevelN() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            int total = crm.customerListPC("",3,name,"",0,0,1,1).getInteger("total");

            Preconditions.checkArgument(total==0,"不应有查询结果");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访页面根据存在姓名+不匹配级别查询");
        }

    }

    @Test
    public void taskListSearchPhoneYLevelN() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            int total = crm.customerListPC("",3,"",phone,0,0,1,1).getInteger("total");

            Preconditions.checkArgument(total==0,"不应有查询结果");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访页面根据存在姓名+不匹配级别查询");
        }

    }

    @Test
    public void taskListSearchNameNPhoneYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查询
            int total = crm.customerListPC("",7,name+"1",phone,0,0,1,1).getInteger("total");

            Preconditions.checkArgument(total==0,"不应有查询结果");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访页面根据不存在的姓名+存在手机号+匹配的级别查询");
        }

    }


    //----------------------添加回访--------------------

    @Test
    public void addVisitComment10() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 10 ; i++){
                comment = comment + "回";
            }
            String date = dateTimeUtil.getHistoryDate(1);
            visit.put("comment",comment);
            visit.put("next_return_visit_date",date);
            crm.customerEditVisitPC(customerid,name,phone,level_id,visit);

            //查看顾客详情，回访记录条数
            int list = crm.customerDetailPC(customerid).getJSONArray("return_visit").size();
            Preconditions.checkArgument(list==1,"添加失败");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加回访，回访记录字数=10");
        }

    }

    @Test
    public void addVisitComment200() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 200 ; i++){
                comment = comment + "回";
            }
            String date = dateTimeUtil.getHistoryDate(1);
            visit.put("comment",comment);
            visit.put("next_return_visit_date",date);
            crm.customerEditVisitPC(customerid,name,phone,level_id,visit);

            //查看顾客详情，回访记录条数
            int list = crm.customerDetailPC(customerid).getJSONArray("return_visit").size();
            Preconditions.checkArgument(list==1,"添加失败");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加回访，回访记录字数=200");
        }

    }

    @Test
    public void addVisitnum50() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 20 ; i++){
                comment = comment + "回";
            }
            String date = dateTimeUtil.getHistoryDate(1);
            visit.put("comment",comment);
            visit.put("next_return_visit_date",date);
            for (int i = 0 ;i < 50;i++){
                crm.customerEditVisitPC(customerid,name,phone,level_id,visit);
            }

            //查看顾客详情，回访记录条数
            int list = crm.customerDetailPC(customerid).getJSONArray("return_visit").size();
            Preconditions.checkArgument(list==50,"添加50条失败");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加回访，回访记录条数=50");
        }

    }

    @Test
    public void addVisitnum51() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 20 ; i++){
                comment = comment + "回";
            }
            String date = dateTimeUtil.getHistoryDate(1);
            visit.put("comment",comment);
            visit.put("next_return_visit_date",date);
            for (int i = 0 ;i < 50;i++){
                crm.customerEditVisitPC(customerid,name,phone,level_id,visit);
            }
            JSONObject obj = crm.customerEditVisitPCNotChk(customerid,name,phone,level_id,visit);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际"+code+"，提示语：" + message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加回访，回访记录条数=51");
        }

    }

    @Test
    public void addVisitComment9() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 9 ; i++){
                comment = comment + "回";
            }
            String date = dateTimeUtil.getHistoryDate(1);
            visit.put("comment",comment);
            visit.put("next_return_visit_date",date);
            JSONObject obj = crm.customerEditVisitPCNotChk(customerid,name,phone,level_id,visit);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际"+code+"，提示语：" + message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加回访，回访记录字数=9");
        }

    }

    @Test
    public void addVisitComment201() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 201 ; i++){
                comment = comment + "回";
            }
            String date = dateTimeUtil.getHistoryDate(1);
            visit.put("comment",comment);
            visit.put("next_return_visit_date",date);
            JSONObject obj = crm.customerEditVisitPCNotChk(customerid,name,phone,level_id,visit);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际"+code+"，提示语：" + message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加回访，回访记录字数=201");
        }

    }

    @Test
    public void addVisitComment0() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            String date = dateTimeUtil.getHistoryDate(1);
            visit.put("comment",comment);
            visit.put("next_return_visit_date",date);
            JSONObject obj = crm.customerEditVisitPCNotChk(customerid,name,phone,level_id,visit);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际"+code+"，提示语：" + message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加回访，回访记录字数=0");
        }

    }

    @Test //前端做了校验
    public void addVisitYesterday() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 10 ; i++){
                comment = comment + "回";
            }
            String date = dateTimeUtil.getHistoryDate(-1);
            visit.put("comment",comment);
            visit.put("next_return_visit_date",date);
            JSONObject obj = crm.customerEditVisitPCNotChk(customerid,name,phone,level_id,visit);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际"+code+"，提示语：" + message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加回访，回访日期是昨天");
        }

    }


    //----------------------添加备注--------------------
    @Test
    public void addVisitRemark20() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 20 ; i++){
                comment = comment + "备";
            }
            crm.customerEditRemarkPC(customerid,name,phone,level_id,comment);

            //查看顾客详情，备注条数
            int list = crm.customerDetailPC(customerid).getJSONArray("remark").size();
            Preconditions.checkArgument(list==2,"添加失败");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加备注，备注字数=20");
        }

    }

    @Test
    public void addVisitRemark200() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 200 ; i++){
                comment = comment + "备";
            }
            crm.customerEditRemarkPC(customerid,name,phone,level_id,comment);

            //查看顾客详情，备注条数
            int list = crm.customerDetailPC(customerid).getJSONArray("remark").size();
            Preconditions.checkArgument(list==2,"添加失败");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加备注，备注字数=200");
        }

    }

    @Test
    public void addVisitRemarkNum50() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 20 ; i++){
                comment = comment + "备";
            }
            for (int i = 0; i < 49;i++){
                crm.customerEditRemarkPC(customerid,name,phone,level_id,comment);
            }


            //查看顾客详情，备注条数
            int list = crm.customerDetailPC(customerid).getJSONArray("remark").size();
            Preconditions.checkArgument(list==50,"添加失败");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加备注，备注条数=50");
        }

    }

    @Test
    public void addVisitRemark19() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 19 ; i++){
                comment = comment + "备";
            }
            JSONObject obj = crm.customerEditRemarkPCNotChk(customerid,name,phone,level_id,comment);
            int code = obj.getInteger("code");
            String message = obj.getString("messge");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际"+code + "，提示语：" + message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加备注，备注字数=19");
        }

    }

    @Test
    public void addVisitRemark201() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 200 ; i++){
                comment = comment + "备";
            }
            JSONObject obj = crm.customerEditRemarkPCNotChk(customerid,name,phone,level_id,comment);
            int code = obj.getInteger("code");
            String message = obj.getString("messge");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际"+code + "，提示语：" + message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加备注，备注字数=19");
        }

    }

    @Test
    public void addVisitRemarkNum51() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 20 ; i++){
                comment = comment + "备";
            }
            for (int i = 0; i < 49;i++){
                crm.customerEditRemarkPC(customerid,name,phone,level_id,comment);
            }

            JSONObject obj = crm.customerEditRemarkPCNotChk(customerid,name,phone,level_id,comment);
            int code = obj.getInteger("code");
            String message = obj.getString("messge");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际"+code + "，提示语：" + message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加备注，备注条数=51");
        }

    }


}
