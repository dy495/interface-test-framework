package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.FileUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;


/**
 * @author : yu
 * @date :  2020/05/30
 */

public class CrmSystemCase extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
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
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP;
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

    //@Test //前端校验
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

    @Test
    public void addScheduleEndLTStartLTNow() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc="交车服务描述交车服务";


            String starttime = dt.getHHmm(-5);//5分钟前
            String endtime = dt.getHHmm(-15);//15分钟前
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename,scheduledesc,scheduledate,starttime,endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待1001，实际"+code+"，提示"+message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端添加工作安排，结束时间<开始时间<当前时间");

        }


    }

    @Test
    public void addScheduleEndEQStart() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc="交车服务描述交车服务";


            String starttime = dt.getHHmm(5);//5分钟前
            String endtime = starttime;//5分钟前
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename,scheduledesc,scheduledate,starttime,endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待1001，实际"+code+"，提示"+message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端添加工作安排，结束时间=开始时间");

        }


    }

    @Test(dataProvider = "ERR_FORMAT",dataProviderClass = CrmScenarioUtil.class)
    public void addScheduleFormatErr1(String errformat) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");
            //创建工作安排
            String schedulename = "回访工作";
            String scheduledesc="交车服务描述交车服务";


            String starttime = errformat;//格式异常
            String endtime = starttime;
            JSONObject obj = crm.scheduleAdd_PCNotChk(schedulename,scheduledesc,scheduledate,starttime,endtime);
            int code = obj.getInteger("code");
            String message = obj.getString("message");
            Preconditions.checkArgument(code==1001,"期待1001，实际"+code+"，提示"+message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端添加工作安排，时间格式异常");

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
            //完成接待
            crm.finishReception();

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
            //完成接待
            crm.finishReception();

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
            //完成接待
            crm.finishReception();

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
            //完成接待
            crm.finishReception();

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
                Preconditions.checkArgument(single.getString("customer_level").equals("7"),"查询结果与查询条件不一致");
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
            //完成接待
            crm.finishReception();

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
            //完成接待
            crm.finishReception();

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
            //完成接待
            crm.finishReception();

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
            //完成接待
            crm.finishReception();

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
            //完成接待
            crm.finishReception();

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
            //完成接待
            crm.finishReception();

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
            //完成接待
            crm.finishReception();

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
            saveData("我的回访页面根据存在手机号+不匹配级别查询");
        }

    }

    @Test
    public void taskListSearchNameNPhoneYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            //完成接待
            crm.finishReception();

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
            String date = dt.getHistoryDate(1);
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
            //完成接待
            crm.finishReception();

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
            String date = dt.getHistoryDate(1);
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
            //完成接待
            crm.finishReception();

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
            String date = dt.getHistoryDate(1);
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

    //@Test //前端校验
    public void addVisitnum51() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            String date = dt.getHistoryDate(1);
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
            //完成接待
            crm.finishReception();

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
            String date = dt.getHistoryDate(1);
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
            //完成接待
            crm.finishReception();

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
            String date = dt.getHistoryDate(1);
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
            //完成接待
            crm.finishReception();

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
            String date = dt.getHistoryDate(1);
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

    //@Test //前端做了校验
    public void addVisitYesterday() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            String date = dt.getHistoryDate(-1);
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
            //完成接待
            crm.finishReception();

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
            //完成接待
            crm.finishReception();

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
            //完成接待
            crm.finishReception();

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

    //@Test //前端校验
    public void addVisitRemark19() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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

    //@Test //前端校验
    public void addVisitRemark201() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            //完成接待
            crm.finishReception();

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



    /**
     *
     * ====================今日来访======================
     * */
    //----------------------查询--------------------
    @Test
    public void todayListSearchAll() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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

            //直接点击查询
            int total = crm.todayListPC(-1,"","","",0,0,1,1).getInteger("total");
            Preconditions.checkArgument(total>=1,"今日来访数量期待>=1，实际="+total);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日来访页面直接点击查询按钮");
        }

    }

    @Test
    public void todayListSearchName() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            JSONObject obj = crm.todayListPC(-1,name,"","",0,0,1,1).getJSONArray("list").getJSONObject(0);
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
            saveData("今日来访页面根据姓名查询");
        }

    }

    @Test
    public void todayListSearchPhone() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            JSONObject obj = crm.todayListPC(-1,"",phone,"",0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_phone = obj.getString("customer_phone");
            Preconditions.checkArgument(search_phone.equals(phone),"查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日来访页面根据手机号查询");
        }

    }

    @Test
    public void todayListSearchLevel() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            JSONArray list = crm.todayListPC(7,"","","",0,0,1,1).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                Preconditions.checkArgument(single.getString("customer_level_name").equals("H"),"查询结果与查询条件不一致");
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日来访页面根据客户等级查询");
        }

    }

    @Test
    public void todayListSearchNameYPhoneY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            JSONObject obj = crm.todayListPC(-1,name,phone,"",0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_phone = obj.getString("customer_phone");
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_phone.equals(phone)&&search_name.equals(name),"查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日来访页面根据姓名+手机号查询");
        }

    }

    @Test
    public void todayListSearchNameYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            JSONObject obj = crm.todayListPC(7,name,"","",0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_level = obj.getString("customer_level_name");
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_level.equals("H")&&search_name.equals(name),"查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日来访页面根据姓名+级别查询");
        }

    }

    @Test
    public void todayListSearchPhoneYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            JSONObject obj = crm.todayListPC(7,"",phone,"",0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_level = obj.getString("customer_level_name");
            String search_phone = obj.getString("customer_phone");
            Preconditions.checkArgument(search_level.equals("H")&&search_phone.equals(phone),"查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日来访页面根据手机号+级别查询");
        }

    }

    @Test
    public void todayListSearchNameYPhoneYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            JSONObject obj = crm.todayListPC(7,name,phone,"",0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_level = obj.getString("customer_level_name");
            String search_phone = obj.getString("customer_phone");
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_level.equals("H")&&search_phone.equals(phone) && search_name.equals(name),"查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日来访页面根据姓名+手机号+级别查询");
        }

    }

    @Test
    public void todayListSearchNameYPhoneN() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            int total = crm.todayListPC(-1,name,phone+"1","",0,0,1,1).getInteger("total");

            Preconditions.checkArgument(total==0,"有查询结果");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日来访页面根据存在姓名+不匹配手机号查询");
        }

    }

    @Test
    public void todayListSearchNameYLevelN() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            int total = crm.todayListPC(5,name,"","",0,0,1,1).getInteger("total");

            Preconditions.checkArgument(total==0,"不应有查询结果");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日来访页面根据存在姓名+不匹配级别查询");
        }

    }

    @Test
    public void todayListSearchPhoneYLevelN() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            int total = crm.todayListPC(3,"",phone,"",0,0,1,1).getInteger("total");

            Preconditions.checkArgument(total==0,"不应有查询结果");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日来访页面根据存在手机号+不匹配级别查询");
        }

    }

    @Test
    public void todayListSearchNameNPhoneYLevelY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            int total = crm.todayListPC(7,name+"1",phone,"",0,0,1,1).getInteger("total");

            Preconditions.checkArgument(total==0,"不应有查询结果");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日来访页面根据不存在的姓名+存在手机号+匹配的级别查询");
        }

    }


    /**
     *
     * ====================我的试驾======================
     * */
    //----------------------查询--------------------

    @Test
    public void driverListSearchAll() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        int driverid = -1;
        try {
            long level_id=7L;
            String phone = "12312341234";
            String name = dt.getHistoryDate(0);
            String desc = "创建H级客户自动化------------------------------------";
            //创建试驾
            String idCard = "110226198210260078";
            String gender = "男";
            String signTime = dt.getHistoryDate(0);
            String model = "911";
            String country = "中国";
            String city = "图们";
            String email = dt.getHistoryDate(0)+"@qq.com";
            String address = "北京市昌平区";
            String ward_name = "小小";
            String driverLicensePhoto1Url = picurl;
            String driverLicensePhoto2Url = picurl;
            String electronicContractUrl = picurl;
            driverid = crm.driveradd(name,idCard,gender,phone,signTime,"试乘试驾",model,country,city,email,address,ward_name,driverLicensePhoto1Url,driverLicensePhoto2Url,electronicContractUrl).getInteger("id");

            //直接点击查询
            int total = crm.driveList(signTime,"","",1,1).getInteger("total");
            Preconditions.checkArgument(total>=1,"我的试驾数量期待>=1，实际="+total);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的试驾页面直接点击查询按钮");
        }

    }

    @Test
    public void driverListSearchName() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        int driverid = -1;
        try {
            long level_id=7L;
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            String name = dt.getHistoryDate(0);
            String desc = "创建H级客户自动化------------------------------------";
            //创建试驾
            String idCard = "110226198210260078";
            String gender = "男";
            String signTime = dt.getHistoryDate(0);
            String model = "911";
            String country = "中国";
            String city = "图们";
            String email = dt.getHistoryDate(0)+"@qq.com";
            String address = "北京市昌平区";
            String ward_name = "小小";
            String driverLicensePhoto1Url = picurl;
            String driverLicensePhoto2Url = picurl;
            String electronicContractUrl = picurl;
            driverid = crm.driveradd(name,idCard,gender,phone,signTime,"试乘试驾",model,country,city,email,address,ward_name,driverLicensePhoto1Url,driverLicensePhoto2Url,electronicContractUrl).getInteger("id");

            //直接点击查询
            JSONArray obj = crm.driveList(signTime,name,"",1,20).getJSONArray("list");
            for (int i = 0; i < obj.size();i++){
                JSONObject single = obj.getJSONObject(i);
                String search_name = single.getString("customer_name");
                Preconditions.checkArgument(search_name.equals(name),"查询结果与查询条件不一致");
            }

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的试驾页面根据客户姓名搜索");
        }

    }

    @Test
    public void driverListSearchPhone() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        int driverid = -1;
        try {
            long level_id=7L;
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            String name = dt.getHistoryDate(0);
            String desc = "创建H级客户自动化------------------------------------";
            //创建试驾
            String idCard = "110226198210260078";
            String gender = "男";
            String signTime = dt.getHistoryDate(0);
            String model = "911";
            String country = "中国";
            String city = "图们";
            String email = dt.getHistoryDate(0)+"@qq.com";
            String address = "北京市昌平区";
            String ward_name = "小小";
            String driverLicensePhoto1Url = picurl;
            String driverLicensePhoto2Url = picurl;
            String electronicContractUrl = picurl;
            driverid = crm.driveradd(name,idCard,gender,phone,signTime,"试乘试驾",model,country,city,email,address,ward_name,driverLicensePhoto1Url,driverLicensePhoto2Url,electronicContractUrl).getInteger("id");

            //直接点击查询
            JSONArray obj = crm.driveList(signTime,"",phone,1,20).getJSONArray("list");
            for (int i = 0; i < obj.size();i++){
                JSONObject single = obj.getJSONObject(i);
                String search_phone = single.getString("customer_phone_number");
                Preconditions.checkArgument(search_phone.equals(phone),"查询结果与查询条件不一致");
            }

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的试驾页面根据客户手机号搜索");
        }

    }

    @Test
    public void driverListSearchNameYPhoneY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        int driverid = -1;
        try {
            long level_id=7L;
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            String name = dt.getHistoryDate(0);
            String desc = "创建H级客户自动化------------------------------------";
            //创建试驾
            String idCard = "110226198210260078";
            String gender = "男";
            String signTime = dt.getHistoryDate(0);
            String model = "911";
            String country = "中国";
            String city = "图们";
            String email = dt.getHistoryDate(0)+"@qq.com";
            String address = "北京市昌平区";
            String ward_name = "小小";
            String driverLicensePhoto1Url = picurl;
            String driverLicensePhoto2Url = picurl;
            String electronicContractUrl = picurl;
            driverid = crm.driveradd(name,idCard,gender,phone,signTime,"试乘试驾",model,country,city,email,address,ward_name,driverLicensePhoto1Url,driverLicensePhoto2Url,electronicContractUrl).getInteger("id");

            //直接点击查询
            JSONArray obj = crm.driveList(signTime,name,phone,1,20).getJSONArray("list");
            for (int i = 0; i < obj.size();i++){
                JSONObject single = obj.getJSONObject(i);
                String search_phone = single.getString("customer_phone_number");
                String search_name = single.getString("customer_name");
                Preconditions.checkArgument(search_phone.equals(phone)&&search_name.equals(name),"查询结果与查询条件不一致");
            }

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的试驾页面根据姓名+手机号搜索");
        }

    }

    @Test
    public void driverListSearchNameNPhoneY() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        int driverid = -1;
        try {
            long level_id=7L;
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            String name = dt.getHistoryDate(0);
            String desc = "创建H级客户自动化------------------------------------";
            //创建试驾
            String idCard = "110226198210260078";
            String gender = "男";
            String signTime = dt.getHistoryDate(0);
            String model = "911";
            String country = "中国";
            String city = "图们";
            String email = dt.getHistoryDate(0)+"@qq.com";
            String address = "北京市昌平区";
            String ward_name = "小小";
            String driverLicensePhoto1Url = picurl;
            String driverLicensePhoto2Url = picurl;
            String electronicContractUrl = picurl;
            driverid = crm.driveradd(name,idCard,gender,phone,signTime,"试乘试驾",model,country,city,email,address,ward_name,driverLicensePhoto1Url,driverLicensePhoto2Url,electronicContractUrl).getInteger("id");

            //查询
            int total = crm.driveList(signTime,name+"1",phone,1,20).getInteger("total");
            Preconditions.checkArgument(total==0,"不应有查询结果");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的试驾页面根据不存在姓名+手机号搜索");
        }

    }



    /**
     *
     * ====================我的交车======================
     * */
    //----------------------查询--------------------
    @Test
    public void deliverListSearchAll() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建交车

            String name = dt.getHHmm(0);
            String gender = "男";
            String signTime = dt.getHistoryDate(0);
            String model = "911";
            int id = crm.deliverAdd(name, gender, phone, signTime, model, picurl).getInteger("id");
            int total = crm.deliverList(signTime,1,1,"","").getInteger("total");
            Preconditions.checkArgument(total>=1,"我的交车数量期待>=1，实际="+total);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的交车页面直接点击查询按钮");
        }

    }

    @Test
    public void deliverListSearchName() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            //新建交车
            String name = dt.getHHmm(0);
            String gender = "男";
            String signTime = dt.getHistoryDate(0);
            String model = "911";
            int id = crm.deliverAdd(name, gender, phone, signTime, model, picurl).getInteger("id");
            JSONArray obj = crm.deliverList(signTime,1,1,name,"").getJSONArray("list");
            for (int i = 0; i < obj.size();i++){
                JSONObject single = obj.getJSONObject(i);
                String search_name = single.getString("customer_name");
                Preconditions.checkArgument(search_name.equals(name),"查询结果与查询条件不一致");
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的交车页面根据姓名查询");
        }

    }

    /**
     *
     * ====================我的客户======================
     * */
    //----------------------查询--------------------
    @Test
    public void customerListSearchAll() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            //直接点击查询
            int total = crm.customerListPC("",-1,"","",0,0,1,1).getInteger("total");
            Preconditions.checkArgument(total>=1,"我的客户数量期待>=1，实际="+total);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的客户页面直接点击查询按钮");
        }

    }

    @Test
    public void customerListSearchName() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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
            saveData("我的客户页面根据姓名查询");
        }

    }

    @Test
    public void customerListSearchPhone() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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


            //查询
            JSONObject obj = crm.customerListPC("",-1,"",phone,0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_phone = obj.getString("customer_phone");
            Preconditions.checkArgument(search_phone.equals(phone),"查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的客户页面根据手机号查询");
        }

    }

    @Test
    public void customerListSearchLevel() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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

            //查询
            JSONArray list = crm.todayListPC(7,"","","",0,0,1,1).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                Preconditions.checkArgument(single.getString("customer_level_name").equals("H"),"查询结果与查询条件不一致");
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("今日来访页面根据客户等级查询");
        }

    }

    @Test
    public void customerListSearchDate() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

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

            String starttime = dt.getHistoryDate(0);
            String endtime = starttime;
            //查询
            JSONArray list = crm.customerListPC("",-1,"","",starttime,endtime,1,1).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                Preconditions.checkArgument(single.getString("service_date").equals(starttime),"查询结果与查询条件不一致");
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的客户页面根据日期查询");
        }

    }

    //----------------------展示--------------------
    @Test
    public void customerListShowAll() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //查询
            JSONArray list = crm.customerListPC("",-1,"","","","",1,50).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                Preconditions.checkArgument(single.getString("belongs_sale_name").equals("销售顾问-自动化"),"展示信息不正确");
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的客户页面所属销售的顾客信息");
        }

    }
    //----------------------删除--------------------
    @Test
    public void customerListDel() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {

            //完成接待
            crm.finishReception();

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
            //姓名+手机号查询
            int total = crm.customerListPC("",-1,name,phone,0,0,1,1).getInteger("total");
            Preconditions.checkArgument(total==1,"删除前查询，期待有一条记录，实际"+total);

            //总经理登陆
            crm.login(zjlname,zjlpwd);
            //删除顾客
            crm.customerDeletePC(customerid);

            //销售顾问登陆
            crm.login(salename1,salepwd1);
            //再次查询应无结果
            int total2 = crm.customerListPC("",-1,name,phone,0,0,1,1).getInteger("total");
            Preconditions.checkArgument(total2==0,"删除后查询，期待无结果，实际"+total);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(salename1,salepwd1);
            saveData("我的客户页面删除后再查询");
        }

    }

    @Test
    public void customerListDelInService() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {

            //完成接待
            crm.finishReception();

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //总经理登陆
            crm.login(zjlname,zjlpwd);
            //删除顾客
            int code = crm.customerDeletePCNotChk(customerid).getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+ code);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(salename1,salepwd1);
            saveData("删除接待中客户");
        }

    }

    @Test
    public void customerListDelServiced() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {

            //完成接待
            crm.finishReception();

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

            //总经理登陆
            crm.login(zjlname,zjlpwd);
            //删除顾客
            crm.customerDeletePC(customerid);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(salename1,salepwd1);
            saveData("删除已完成接待客户");
        }

    }



    //---------------------编辑顾客信息-------------
    @Test
    public void customerListsaleEditPhone() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";

            //完成接待
            crm.finishReception();
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);


            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            crm.customerEditPC(customerid,name,"12312341234",2);

            //再次查询，手机号应不变
            JSONObject obj = crm.customerListPC("",-1,name,"","","",1,1).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(obj.getString("customer_phone").equals(phone),"手机号改变");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问修改顾客手机号");
        }

    }

    @Test
    public void customerListzjlEditPhone() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";

            //完成接待
            crm.finishReception();
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);


            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //总经理登陆
            crm.login(zjlname,zjlpwd);
            crm.customerEditPC(customerid,name,"12312341234",2);

            //
            crm.login(salename1,salepwd1);
            //再次查询，手机号应改变
            JSONObject obj = crm.customerListPC("",-1,name,"","","",1,1).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(obj.getString("customer_phone").equals("12312341234"),"手机号未改变");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("总经理修改顾客手机号");
        }

    }

    @Test
    public void customerListsaleEditsale() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";

            //完成接待
            crm.finishReception();
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);


            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            crm.customerEditsale(customerid,name,phone,"uid_e40c1119");

            //再次查询，手机号应不变
            JSONObject obj = crm.customerListPC("",-1,name,"","","",1,1).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(obj.getString("belongs_sale_id").equals("uid_562be6aa"),"所属顾问改变");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问修改顾客所属顾问");
        }

    }

    @Test
    public void customerListzjlEditsale() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";

            //完成接待
            crm.finishReception();
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);


            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            //总经理登陆
            crm.login(zjlname,zjlpwd);
            crm.customerEditsale(customerid,name,phone,"uid_e40c1119");

            //再次查询，手机号应不变
            JSONObject obj = crm.customerListPC("",-1,name,"","","",1,1).getJSONArray("list").getJSONObject(0);
            crm.login(salename1,salepwd1);
            Preconditions.checkArgument(obj.getString("belongs_sale_id").equals("uid_e40c1119"),"所属顾问未改变");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("总经理修改顾客所属顾问");
        }

    }

    @Test
    public void customerListsaleEditSeveral() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";

            String time = dt.getHistoryDate(0);
            //完成接待
            crm.finishReception();
            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);


            //获取顾客id
            customerid = Long.parseLong(crm.userInfService().getString("customer_id"));

            //完成接待
            crm.finishReception();

            crm.customerEditPC(customerid,name,phone,2,2,0,time,1,1,0);

            //再次查询
            JSONObject obj = crm.customerListPC("",-1,name,phone,"","",1,1).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(obj.getInteger("like_car")==2,"like_car修改失败");
            Preconditions.checkArgument(obj.getInteger("pay_type")==0,"pay_type修改失败");
            Preconditions.checkArgument(obj.getInteger("show_price")==1,"show_price修改失败");
            Preconditions.checkArgument(obj.getInteger("test_drive_car")==1,"test_drive_car修改失败");
            Preconditions.checkArgument(obj.getInteger("visit_count")==0,"visit_count修改失败");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问修改顾客多项信息");
        }

    }

    //---------------------销售状态-------------

    //@Test
    //app点击创建顾客按钮时，会调用修改销售状态的接口，状态不是自动转的，用例作废
    public void customerListsaleStatus() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //完成接待
            crm.finishReception();

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";


            //创建某级客户
            JSONObject customer = crm.decisionCstmer_NamePhone(level_id,desc,name,phone);
            crm.customerAdd(customer);

            //查看销售状态
            String status1 = crm.userStatus().getString("user_status");
            Preconditions.checkArgument(status1.equals("BUSY"),"销售创建客户后，状态期待为BUSY，实际为"+ status1);


            //完成接待
            crm.finishReception();

            //查看销售状态
            String status2 = crm.userStatus().getString("user_status");
            Preconditions.checkArgument(status2.equals("RECEPTIVE"),"销售完成接待后，状态期待为RECEPTIVE，实际为"+ status1);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问状态");
        }

    }


    /**
     *
     * ====================创建账号======================
     * */
    @Test
    public void  addUserREname(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(baoshijie,bpwd);
            String userName = ""+ System.currentTimeMillis();
            String userLoginName=userName;
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            String phone2 = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone2 = phone2 + a;
            }
            String passwd=userLoginName;
            int roleId=13; //销售顾问
            //添加账号
            crm.addUser(userName,userLoginName,phone,passwd,roleId);
            int a = 0;
            int total = crm.userPage(1,1).getInteger("total");
            String userid = "";
            if (total > 50) {
                if (total % 50 == 0) {
                    a = total / 50;
                } else {
                    a = (int) Math.ceil(total / 50) + 1;
                }
                for (int i = 1; i <= a; i++) {
                    JSONArray list = crm.userPage(1,50).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        if (single.getString("user_login_name").equals(userLoginName)){
                            userid = single.getString("user_id"); //获取用户id
                        }
                    }
                }
            } else {
                JSONArray list = crm.userPage(1,50).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    if (single.getString("user_login_name").equals(userLoginName)){
                        userid = single.getString("user_id"); //获取用户id
                    }
                }
            }

            //重复添加
            int code = crm.addUserNotChk(userName,userLoginName,phone2,passwd,roleId).getInteger("code");
            //删除账号
            crm.userDel(userid);
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(salename1,salepwd1);
            saveData("创建已存在账号");
        }
    }

    @Test
    public void  addUserREphone(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(baoshijie,bpwd);
            String userName = ""+ System.currentTimeMillis();
            String userLoginName=userName;
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            String phone2 = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone2 = phone2 + a;
            }
            String passwd=userLoginName;
            int roleId=13; //销售顾问
            //添加账号
            crm.addUser(userName,userLoginName,phone,passwd,roleId);
            //查询userid
            int a = 0;
            int total = crm.userPage(1,1).getInteger("total");
            String userid = "";
            if (total > 50) {
                if (total % 50 == 0) {
                    a = total / 50;
                } else {
                    a = (int) Math.ceil(total / 50) + 1;
                }
                for (int i = 1; i <= a; i++) {
                    JSONArray list = crm.userPage(1,50).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        if (single.getString("user_login_name").equals(userLoginName)){
                            userid = single.getString("user_id"); //获取用户id
                        }
                    }
                }
            } else {
                JSONArray list = crm.userPage(1,50).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    if (single.getString("user_login_name").equals(userLoginName)){
                        userid = single.getString("user_id"); //获取用户id
                    }
                }
            }

            //重复添加
            int code = crm.addUserNotChk(userName+"1",userLoginName+"1",phone,passwd+"1",roleId).getInteger("code");
            //删除账号
            crm.userDel(userid);
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(salename1,salepwd1);
            saveData("使用已存在手机号创建账号");
        }
    }


    @Test(dataProvider = "ERR_PHONE",dataProviderClass = CrmScenarioUtil.class)
    public void  addUserPhoneErr1(String errphone){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(baoshijie,bpwd);
            String userName = ""+ System.currentTimeMillis();
            String userLoginName=userName;
            String phone = errphone;

            String passwd=userLoginName;
            int roleId=13; //销售顾问
            //添加账号

            int code = crm.addUserNotChk(userName,userLoginName,phone,passwd,roleId).getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(salename1,salepwd1);
            saveData("创建账号时手机号格式不正确");
        }
    }

    @Test
    public void  addUse200(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(baoshijie,bpwd);
            String userName = ""+ System.currentTimeMillis();
            String userLoginName=userName;

            String passwd=userLoginName;
            int roleId=13; //销售顾问
            int before_total = crm.userPage(1,1).getInteger("total");
            while (before_total<200){
                String newloginname = userLoginName+before_total;
                String phone = "1";
                for (int i = 0; i < 10;i++){
                    String a = Integer.toString((int)(Math.random()*10));
                    phone = phone + a;
                }
                //添加账号
                crm.addUser(userName,newloginname,phone,passwd,roleId);
                before_total = before_total +1;
            }
            String userid = "";
            for (int i = 0 ; i < 3;i++){
                JSONArray list = crm.userPage(1,100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    if (single.getString("user_name").equals(userName)){
                        userid = single.getString("user_id"); //获取用户id
                        //删除账号
                        crm.userDel(userid);
                    }
                }
            }

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(salename1,salepwd1);
            saveData("创建200个账号");
        }
    }

    @Test
    public void  addUse201(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(baoshijie,bpwd);
            String userName = ""+ System.currentTimeMillis();
            String userLoginName=userName;

            String passwd=userLoginName;
            int roleId=13; //销售顾问
            int before_total = crm.userPage(1,1).getInteger("total");
            while (before_total<200){
                String newloginname = userLoginName+before_total;
                String phone = "1";
                for (int i = 0; i < 10;i++){
                    String a = Integer.toString((int)(Math.random()*10));
                    phone = phone + a;
                }
                //添加账号
                crm.addUser(userName,newloginname,phone,passwd,roleId);
                before_total = before_total +1;
            }
            int code = crm.addUserNotChk(userName,userLoginName,"19900000000",passwd,roleId).getInteger("code");
            String userid = "";
            for (int i = 0 ; i < 3;i++){
                JSONArray list = crm.userPage(1,100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    if (single.getString("user_name").equals(userName)){
                        userid = single.getString("user_id"); //获取用户id
                        //删除账号
                        crm.userDel(userid);
                    }
                }
            }
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(salename1,salepwd1);
            saveData("创建201个账号");
        }
    }

    @Test(dataProvider = "ROLE_ID",dataProviderClass = CrmScenarioUtil.class)
    public void  delUserDiffRole(String role){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(baoshijie,bpwd);
            String userName = ""+ System.currentTimeMillis();
            String userLoginName=userName;
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }

            String passwd="lvxueqing";
            int roleId=Integer.parseInt(role);
            //添加账号
            crm.addUser(userName,userLoginName,phone,passwd,roleId);
            int a = 0;
            int total = crm.userPage(1,1).getInteger("total");
            String userid = "";
            if (total > 50) {
                if (total % 50 == 0) {
                    a = total / 50;
                } else {
                    a = (int) Math.ceil(total / 50) + 1;
                }
                for (int i = 1; i <= a; i++) {
                    JSONArray list = crm.userPage(1,50).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        if (single.getString("user_login_name").equals(userLoginName)){
                            userid = single.getString("user_id"); //获取用户id
                        }
                    }
                }
            } else {
                JSONArray list = crm.userPage(1,50).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    if (single.getString("user_login_name").equals(userLoginName)){
                        userid = single.getString("user_id"); //获取用户id
                    }
                }
            }

            //删除账号
            int code = crm.userDelNotChk(userid).getInteger("code");
            Preconditions.checkArgument(code==1000,"删除失败，状态码"+code);
            //总经理登陆
            String message = crm.tryLogin(userLoginName,salepwd1).getString("message");
            Preconditions.checkArgument(message.equals("用户名或密码错误"),"提示语为："+message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(salename1,salepwd1);
            saveData("删除不同身份账号");
        }
    }

    @Test(dataProvider = "ROLE_ID",dataProviderClass = CrmScenarioUtil.class)
    public void  delUserDiffRoleANDlogin(String role){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(baoshijie,bpwd);
            String userName = ""+ System.currentTimeMillis();
            String userLoginName=userName;
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }

            String passwd="lvxueqing";
            int roleId=Integer.parseInt(role);
            //添加账号
            crm.addUser(userName,userLoginName,phone,passwd,roleId);
            int a = 0;
            int total = crm.userPage(1,1).getInteger("total");
            String userid = "";
            if (total > 50) {
                if (total % 50 == 0) {
                    a = total / 50;
                } else {
                    a = (int) Math.ceil(total / 50) + 1;
                }
                for (int i = 1; i <= a; i++) {
                    JSONArray list = crm.userPage(1,50).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        if (single.getString("user_login_name").equals(userLoginName)){
                            userid = single.getString("user_id"); //获取用户id
                        }
                    }
                }
            } else {
                JSONArray list = crm.userPage(1,50).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    if (single.getString("user_login_name").equals(userLoginName)){
                        userid = single.getString("user_id"); //获取用户id
                    }
                }
            }

            //删除账号
            crm.userDel(userid);

            //登陆
            String message = crm.tryLogin(userLoginName,salepwd1).getString("message");
            Preconditions.checkArgument(message.equals("用户名或密码错误"),"提示语为："+message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(salename1,salepwd1);
            saveData("删除不同身份账号后，再次登陆");
        }
    }


    /**
     *
     * ====================人脸排除======================
     * */
    @Test(dataProvider = "NO_FACE",dataProviderClass = CrmScenarioUtil.class)
    public void faceOutNoFace(String path) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(baoshijie, bpwd);
            //上传图片
            int code = crm.faceOutUpload(path).getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(salename1, salepwd1);
            saveData("人脸排除上传识别不出人脸的图片");
        }
    }




    /**
     *
     * ====================状态流转======================
     * */
    @Test
    public void  RecToRec(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //完成接待
            crm.finishReception();
            //转为空闲，保证当前状态=空闲
            crm.updateStatus("RECEPTIVE");
            //空闲转空闲
            crm.updateStatus("RECEPTIVE");
            //查询当前状态=空闲
            String now = crm.userStatus().getString("user_status");
            Preconditions.checkArgument(now.equals("RECEPTIVE"),"转换后状态="+now);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("空闲转空闲");
        }
    }

    //@Test //服务端没做校验
    public void  RecToIn(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
//            //完成接待
//            crm.finishReception();
//            //转为空闲，保证当前状态=空闲
//            crm.updateStatus("RECEPTIVE");
//            //空闲转接待中
//            crm.updateStatus("IN_RECEPTION");
//            //查询当前状态=空闲
//            String now = crm.userStatus().getString("user_status");
//            Preconditions.checkArgument(now.equals("RECEPTIVE"),"转换后状态="+now);

                //删账号
                JSONArray list = crm.userPage(1,100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    if (single.getString("user_name").equals("1592380526394")){
                        String userid = single.getString("user_id"); //获取用户id
                        //删除账号
                        crm.userDel(userid);
                    }
                }

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("XXXXXX");
        }
    }



}
