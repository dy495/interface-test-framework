package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.testng.yu.ScenarioUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.testng.annotations.*;
import com.haisheng.framework.util.DateTimeUtil;

import java.awt.*;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.haisheng.framework.util.QADbUtil.*;


/**
 * @author : yu
 * @date :  2020/05/30
 */

public class CrmCase extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    QADbUtil qaDbUtil = new QADbUtil();
    String sale_id = ""; //销售顾问id
    String salename1 = "baoshijie";
    String salepwd1 = "e10adc3949ba59abbe56e057f20f883e";


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     *
     */
    @BeforeClass
    @Override
    public void initial() {
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
//        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
//        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENJIN_BE_DAILY_SERVICE;
//        commonConfig.checklistQaOwner = "lxq";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xxxx");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门禁日常");

        //replace ding push conf
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

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
        logger.debug("case: " + caseResult.getCaseName());
    }


    //----------------------工作安排---------------------------
    @Test
    public void inScheduleChkStatus() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //创建工作安排
            String schedulename = "";
            String scheduledesc="";
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String scheduledate = df.format(date);
            String starttime = DateTimeUtil.getHHmm(0);//当前时间
            String endtime = DateTimeUtil.getHHmm(1);//1分钟之后
            Long scheduleid = crm.scheduleAdd_PC(schedulename,scheduledesc,scheduledate,starttime,endtime).getLong("id");
            //销售排班页面-查询改销售状态
            String status = crm.userStatus().getString("user_status_name");
            Preconditions.checkArgument(status.equals("忙碌中"),"实际状态为"+status);
            //删除工作安排
            crm.scheduleDel_PC(scheduleid);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端工作安排时间内（不手动修改状态）,顾问状态=忙碌");
        }


    }

    @Test
    public void addScheduleChkNum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String scheduledate = df.format(date); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");
            //创建工作安排
            String schedulename = "交车服务";
            String scheduledesc="交车服务描述交车服务描述交车服务描述";

            String starttime = DateTimeUtil.getHHmm(1);//1分钟之后
            String endtime = DateTimeUtil.getHHmm(2);//2分钟之后
            Long scheduleid = crm.scheduleAdd_PC(schedulename,scheduledesc,scheduledate,starttime,endtime).getLong("id");

            //查看数量
            int total2 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");

            //删除工作安排
            crm.scheduleDel_PC(scheduleid);

            //查看数量
            int total3 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");
            int checkadd = total2 - total1;
            int checkdel = total2 - total3;

            Preconditions.checkArgument(checkadd == 1, "添加一条安排后，增加了" + checkadd + "条记录");
            Preconditions.checkArgument(checkdel == 1, "删除一条安排后，减少了" + checkdel + "条记录");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("PC端添加/删除工作安排，数量准确性");
        }


    }

    @Test
    public void addScheduleChkContent() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String scheduledate = df.format(date); //今天日期
            //创建工作安排
            String schedulename = "交车服务";
            String scheduledesc="交车服务描述交车服务描述交车服务描述";

            String starttime = DateTimeUtil.getHHmm(1);//1分钟之后
            String endtime = DateTimeUtil.getHHmm(2);//2分钟之后
            Long scheduleid = crm.scheduleAdd_PC(schedulename,scheduledesc,scheduledate,starttime,endtime).getLong("id");


            String search_name ="";
            String search_desc ="";
            String search_date ="";
            String search_start ="";
            String search_end ="";
            //查看列表
            JSONArray list_object = crm.scheduleList_PC(1,21,scheduledate,"").getJSONArray("list");
            for (int i = 0; i < list_object.size();i++){
                JSONObject single = list_object.getJSONObject(i);
                if (single.getLong("id")==scheduleid){
                    search_name = single.getString("name");
                    search_desc = single.getString("description");
                    search_date = single.getString("date");
                    search_start = single.getString("start_time");
                    search_end = single.getString("end_time");
                }
            }
            Preconditions.checkArgument(search_name.equals(schedulename),"工作安排类型不一致");
            Preconditions.checkArgument(search_desc.equals(scheduledesc),"工作描述不一致");
            Preconditions.checkArgument(search_date.equals(scheduledate),"日期不一致");
            Preconditions.checkArgument(search_start.equals(search_start),"开始时间不一致");
            Preconditions.checkArgument(search_end.equals(endtime),"结束时间不一致");

            //删除工作安排
            crm.scheduleDel_PC(scheduleid);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("工作安排列表与新建一致");
        }

    }


    //----------------------我的回访---------------------------

    @Test
    public void taskListChkNum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //顾问登陆
            crm.login(salename1,salepwd1);

            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today = df.format(date); //今天日期


            //创建H级客户
            JSONObject customer = crm.decisionCstmer_onlyNec(0,"H级客户-taskListChkNum-修改时间为昨天");
            List list = new List();
            crm.customerAdd(customer, list);
            //修改创建时间为昨天
            //qaDbUtil.updateRetrunVisitTimeToToday(1); //顾客id

            //PC端今日工作-我的回访数量
            int pctotal = crm.taskList_PC(today,-1,1,1,-1L).getInteger("total");
            //app端 已联系+未联系数量
            int apptotal = crm.taskList_APP(today,1,1).getInteger("total");

            Preconditions.checkArgument(pctotal==apptotal,"PC"+pctotal+"条，app"+ apptotal+"条");



        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("（app）未联系+已联系数量 == （PC）我的回访数量");
        }

    }

    @Test
    public void taskListChkNum_buycar() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today = df.format(date); //今天日期


            //创建H级客户
            JSONObject customer = crm.decisionCstmer_onlyNec(0,"H级客户-taskListChkNum_buycar-修改时间为昨天");
            List list = new List();
            Long customerid = crm.customerAdd(customer, list).getLong("customer_id"); //顾客id
            //修改创建时间为昨天


            //PC端今日工作-我的回访数量
            int pctotal_before = crm.taskList_PC(today,-1,1,1,-1L).getInteger("total");

            //顾客订车，修改顾客信息为已订车
            crm.customerEditNec(customerid,"",0);

            //PC端今日工作-我的回访数量
            int pctotal_after = crm.taskList_PC(today,-1,1,1,-1L).getInteger("total");

            int change = pctotal_before - pctotal_after;
            Preconditions.checkArgument(change==1,"订车顾客信息未在我的回访中消失");



        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("顾客订车，当天我的回访数量-1");
        }

    }

    @Test
    public void taskListChkNum_delcustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today = df.format(date); //今天日期


            //创建H级客户
            JSONObject customer = crm.decisionCstmer_onlyNec(0,"H级客户-taskListChkNum_buycar-修改时间为昨天");
            List list = new List();
            Long customerid = crm.customerAdd(customer, list).getLong("customer_id"); //顾客id
            //修改创建时间为昨天


            //PC端今日工作-我的回访数量
            int pctotal_before = crm.taskList_PC(today,-1,1,1,-1L).getInteger("total");

            //删除顾客
            crm.customerDeletePC(customerid);

            //PC端今日工作-我的回访数量
            int pctotal_after = crm.taskList_PC(today,-1,1,1,-1L).getInteger("total");

            int change = pctotal_before - pctotal_after;
            Preconditions.checkArgument(change==1,"订车顾客信息未在我的回访中消失");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("删除顾客，当天我的回访数量-1");
        }

    }

    //----------------------今日来访---------------------------

    @Test
    public void addCustChkTodayListnum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today = df.format(date); //今天日期

            Long starttime = DateTimeUtil.get0OclockStamp(0);
            Long endtime = DateTimeUtil.get0OclockStamp(1);
            //PC端今日工作-今日来访数量
            int todaylist_before = crm.todayListPC(-1,"","","",starttime,endtime,1,1).getInteger("total");

            //创建H级客户
            JSONObject customer = crm.decisionCstmer_onlyNec(0,"H级客户-addCustChkTOdayListnum-创建时间为今天");
            List list = new List();
            Long customerid = crm.customerAdd(customer, list).getLong("customer_id"); //顾客id


            //PC端今日工作-今日来访数量
            int todaylist_after = crm.taskList_PC(today,-1,1,1,-1L).getInteger("total");

            int change = todaylist_before - todaylist_after;
            Preconditions.checkArgument(change==1,"增加"+change);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("app创建客户，今日来访+1");
        }

    }

    @Test
    public void addCustChkcontent() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today = df.format(date); //今天日期

            Long starttime = DateTimeUtil.get0OclockStamp(0);
            Long endtime = DateTimeUtil.get0OclockStamp(1);

            String customer_name = "顾客姓名";
            String customer_phone = "13436941111";
            String customer_level = "0";

            String like_car = "0";
            String compare_car = "宾利";
            String buy_car_attribute = "3";
            String buy_car = "1";
            String pre_buy_time = today;

            //创建H级客户
            JSONObject customer = crm.decisionCstmer_All(Integer.parseInt(customer_level),"创建顾客填写全部信息addCustChkcontent","",customer_name,customer_phone,"4","","","","","","","","","","",pre_buy_time,like_car,compare_car,"","",buy_car,buy_car_attribute,"");
            List list = new List();
            Long customerid = crm.customerAdd(customer, list).getLong("customer_id"); //顾客id

            //查询顾客信息
            JSONArray search = crm.todayListPC(-1,customer_name,customer_phone,Long.toString(customerid),0,0,1,1).getJSONArray("list");
            JSONObject single = search.getJSONObject(0);
            String search_name = single.getString("customer_name");
            String search_phone = single.getString("customer_phone");
            String search_level = single.getString("customer_level");
            String search_like = single.getString("like_car");
            String search_compare = single.getString("compare_car");
            String search_attribute = single.getString("buy_car_attribute");
            String search_buy = single.getString("buy_car");
            String search_pre = single.getString("pre_buy_time");

            Preconditions.checkArgument(search_name.equals(customer_name),"姓名不一致");
            Preconditions.checkArgument(search_phone.equals(customer_phone),"手机号不一致");
            Preconditions.checkArgument(search_level.equals(customer_level),"客户级别不一致");
            Preconditions.checkArgument(search_like.equals(like_car),"意向车型不一致");
            Preconditions.checkArgument(search_compare.equals(compare_car),"对比车型不一致");
            Preconditions.checkArgument(search_attribute.equals(buy_car_attribute),"购车属性不一致");
            Preconditions.checkArgument(search_buy.equals(buy_car),"是否订车不一致");
            Preconditions.checkArgument(search_pre.equals(pre_buy_time),"预计购车时间不一致");




        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("app创建客户，今日来访+1");
        }

    }

    //----------------------我的客户---------------------------
    @Test
    public void customerListChkNum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today = df.format(date); //今天日期

            //我的客户条数
            int before = crm.customerListPC("",-1,"","",0L,0L,1,1).getInteger("total");

            //创建H级客户
            JSONObject customer = crm.decisionCstmer_onlyNec(0,"H级客户-customerListChkNum");
            List list = new List();
            long customerid = crm.customerAdd(customer, list).getLong("customer_id");

            //我的客户条数
            int after = crm.customerListPC("",-1,"","",0L,0L,1,1).getInteger("total");

            int change = after - before;
            Preconditions.checkArgument(change==1,"创建后增加了" + change + "条");

            //删除客户
            crm.customerDeletePC(customerid);

            int afterdel = crm.customerListPC("",-1,"","",0L,0L,1,1).getInteger("total");

            int change2 = after - afterdel;
            Preconditions.checkArgument(change2==1,"删除后减少了" + change + "条");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("app创建/删除客户，我的客户+1/-1");
        }

    }

    @Test
    public void customerListChkcontent() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today = df.format(date); //今天日期


            String customer_name = "顾客姓名";
            String customer_phone = "13436941111";
            String customer_level = "0";

            String like_car = "0";
            String compare_car = "宾利";
            String buy_car_attribute = "3";
            String buy_car = "1";
            String pre_buy_time = today;

            //创建H级客户
            JSONObject customer = crm.decisionCstmer_All(Integer.parseInt(customer_level),"创建顾客填写全部信息addCustChkcontent","",customer_name,customer_phone,"4","","","","","","","","","","",pre_buy_time,like_car,compare_car,"","",buy_car,buy_car_attribute,"");
            List list = new List();
            Long customerid = crm.customerAdd(customer, list).getLong("customer_id"); //顾客id

            String search_name ="";
            String search_phone ="";
            String search_level ="";
            String search_like ="";
            String search_compare ="";
            String search_attribute ="";
            String search_buy = "";
            String search_pre ="";
            //查询顾客信息
            JSONArray search = crm.customerListPC("",-1,customer_name,customer_phone,0,0,1,1).getJSONArray("list");
            for (int i = 0; i<search.size();i++){
                JSONObject single = search.getJSONObject(i);
                if (single.getLong("customer_id")==customerid){
                    search_name = single.getString("customer_name");
                    search_phone = single.getString("customer_phone");
                    search_level = single.getString("customer_level");
                    search_like = single.getString("like_car");
                    search_compare = single.getString("compare_car");
                    search_attribute = single.getString("buy_car_attribute");
                    search_buy = single.getString("buy_car");
                    search_pre = single.getString("pre_buy_time");
                }
            }

            Preconditions.checkArgument(search_name.equals(customer_name),"姓名不一致");
            Preconditions.checkArgument(search_phone.equals(customer_phone),"手机号不一致");
            Preconditions.checkArgument(search_level.equals(customer_level),"客户级别不一致");
            Preconditions.checkArgument(search_like.equals(like_car),"意向车型不一致");
            Preconditions.checkArgument(search_compare.equals(compare_car),"对比车型不一致");
            Preconditions.checkArgument(search_attribute.equals(buy_car_attribute),"购车属性不一致");
            Preconditions.checkArgument(search_buy.equals(buy_car),"是否订车不一致");
            Preconditions.checkArgument(search_pre.equals(pre_buy_time),"预计购车时间不一致");




        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("app创建客户，PC我的客户页面列表与新建时信息一致");
        }

    }

    @Test
    public void customerListDelChkOrderNum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today = df.format(date); //今天日期
            int before = 0;
            int after = 0;
            int afterdel = 0;

            JSONArray list1 = crm.receptionOrder(sale_id,0,0).getJSONArray("list");
            for (int i = 0; i < list1.size();i++){
                JSONObject single = list1.getJSONObject(i);
                if (single.getString("user_id").equals(sale_id)){
                    before = single.getInteger("order");
                }
            }

            //创建H级客户
            JSONObject customer = crm.decisionCstmer_onlyNec(0,"H级客户-customerListChkNum");
            List list = new List();
            long customerid = crm.customerAdd(customer, list).getLong("customer_id");
            //查看今日接待数量
            JSONArray list2 = crm.receptionOrder(sale_id,0,0).getJSONArray("list");
            for (int i = 0; i < list2.size();i++){
                JSONObject single = list2.getJSONObject(i);
                if (single.getString("user_id").equals(sale_id)){
                    after = single.getInteger("order");
                }
            }
            int change1 = after - before;
            Preconditions.checkArgument(change1==1,"创建客户后，今日接待人数增加了" + change1);

            //删除客户
            crm.customerDeletePC(customerid);
            //查看今日接待数量
            JSONArray list3 = crm.receptionOrder(sale_id,0,0).getJSONArray("list");
            for (int i = 0; i < list3.size();i++){
                JSONObject single = list3.getJSONObject(i);
                if (single.getString("user_id").equals(sale_id)){
                    afterdel = single.getInteger("order");
                }
            }
            int change2 = afterdel - after;
            Preconditions.checkArgument(change2==0,"删除客户后，今日接待人数减少了" + change2);



        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的客户删除一条，销售排班中的今日接待人数不变");
        }

    }

    @Test
    public void customerListDelChkTodayList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today = df.format(date); //今天日期
            int before = 0;
            int after = 0;
            int afterdel = 0;

            JSONArray list1 = crm.receptionOrder(sale_id,0,0).getJSONArray("list");
            for (int i = 0; i < list1.size();i++){
                JSONObject single = list1.getJSONObject(i);
                if (single.getString("user_id").equals(sale_id)){
                    before = single.getInteger("order");
                }
            }

            //创建H级客户
            JSONObject customer = crm.decisionCstmer_onlyNec(0,"H级客户-customerListChkNum");
            List list = new List();
            long customerid = crm.customerAdd(customer, list).getLong("customer_id");
            //查看今日接待数量
            JSONArray list2 = crm.receptionOrder(sale_id,0,0).getJSONArray("list");
            for (int i = 0; i < list2.size();i++){
                JSONObject single = list2.getJSONObject(i);
                if (single.getString("user_id").equals(sale_id)){
                    after = single.getInteger("order");
                }
            }
            int change1 = after - before;
            Preconditions.checkArgument(change1==1,"创建客户后，今日接待人数增加了" + change1);

            //删除客户
            crm.customerDeletePC(customerid);
            //查看今日接待数量
            JSONArray list3 = crm.receptionOrder(sale_id,0,0).getJSONArray("list");
            for (int i = 0; i < list3.size();i++){
                JSONObject single = list3.getJSONObject(i);
                if (single.getString("user_id").equals(sale_id)){
                    afterdel = single.getInteger("order");
                }
            }
            int change2 = afterdel - after;
            Preconditions.checkArgument(change2==0,"删除客户后，今日接待人数减少了" + change2);



        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的客户删除一条，销售排班中的今日接待人数不变");
        }

    }





}
