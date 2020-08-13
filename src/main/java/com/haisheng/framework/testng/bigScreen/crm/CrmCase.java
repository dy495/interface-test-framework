package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.CustomerInfo;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.FileUtil;
import com.haisheng.framework.util.JsonpathUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author : yu
 * @date :  2020/05/30
 */

public class CrmCase extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    CustomerInfo cstm = new CustomerInfo();
    String sale_id = "uid_562be6aa"; //销售顾问-自动化 id

    String pwd = "e10adc3949ba59abbe56e057f20f883e";//123456
    String phone = "一个假的手机号"+dt.getHistoryDate(0);
    String name = "自动化名字";


    public void clearCustomer(long customerid)  throws Exception{
        if( customerid!=-1L){
            try {

                //总经理登陆删除客户
                crm.login(cstm.xszj,cstm.pwd);
                //删除顾客
                crm.customerDeletePC(customerid);
            } catch (Exception e) {
                throw e;
            } finally {
                crm.login(cstm.lxqgw,cstm.pwd);
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
        crm.login(cstm.lxqgw,cstm.pwd);

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
     * ===============PC-我的客户=============
     */

    @Test
    public void addVisitRemarkChkNum() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {


            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            customerid = creatCust(name,phone);

            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //查看顾客详情，备注条数
            int listbefore = crm.customerDetailPC(customerid).getJSONArray("remark").size();

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 20 ; i++){
                comment = comment + "备";
            }
            crm.customerEditRemarkPC(customerid,name,phone1,level_id,comment);

            //查看顾客详情，备注条数
            int listafter = crm.customerDetailPC(customerid).getJSONArray("remark").size();
            int change = listafter - listbefore;
            Preconditions.checkArgument(change==1,"备注数增加"+change);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加备注，备注条数+1");
        }

    }

    @Test
    public void addVisitCommentChkNum() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String phone1 = phone.substring(3);
            String name = phone;
            customerid = creatCust(name,phone);

            //完成接待


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
            crm.customerEditVisitPC(customerid,name,phone1,level_id,visit);

            //查看顾客详情，回访记录条数
            int listbefore = crm.customerDetailPC(customerid).getJSONArray("return_visit").size();

            crm.customerEditVisitPC(customerid,name,phone1,level_id,visit);
            //查看顾客详情，回访记录条数
            int listafter = crm.customerDetailPC(customerid).getJSONArray("return_visit").size();
            int change = listafter - listbefore;
            Preconditions.checkArgument(change==1,"回访记录数量增加了"+change);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加回访，回访记录数量+1");
        }

    }

    @Test
    public void addVisitChkNum() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name,phone);
            int size = crm.customerDetailPC(customerid).getJSONArray("visit").size();

            //完成接待

            Preconditions.checkArgument(size==1,"来访记录条数="+ size);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("app创建顾客，来访记录数量+1");
        }

    }

    @Test
    public void customerChkListAndDetail() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {


            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            int likecar = 1;
            int buycar = 0;
            String pretime = dt.getHistoryDate(1);
            String compare_car = "宾利";
            int buy_car_attribute = 3;
            customerid = creatCust(name,phone);
            //完成接待

            //列表页
            JSONObject list = crm.customerListPC("",-1,name,phone1,"","",1,1).getJSONArray("list").getJSONObject(0);
            String list_name = list.getString("customer_name");
            Long list_level = list.getLong("customer_level");
            String list_phone = list.getString("customer_phone");
            String list_sale = list.getString("belongs_sale_id");
            int list_like_car = list.getInteger("like_car");
            int list_buycar = list.getInteger("buy_car");
            String list_time = list.getString("pre_buy_time");

            //详情页
            JSONObject detail = crm.customerDetailPC(customerid);
            String detail_name = detail.getString("customer_name");
            Long detail_level = detail.getLong("customer_level");
            String detail_phone = detail.getString("customer_phone");
            String detail_sale = detail.getString("belongs_sale_id");
            int detailt_like_car = detail.getInteger("like_car");
            int detail_buycar = detail.getInteger("buy_car");
            String detail_time = detail.getString("pre_buy_time");
            Preconditions.checkArgument(name.equals(list_name) && name.equals(detail_name),"姓名不一致");
            Preconditions.checkArgument(level_id==list_level && level_id==detail_level,"等级不一致");
            Preconditions.checkArgument(phone.equals(list_phone) && phone.equals(detail_phone),"手机号不一致");
            Preconditions.checkArgument(sale_id.equals(list_sale) && sale_id.equals(detail_sale),"所属销售不一致");
            Preconditions.checkArgument(likecar==list_like_car && likecar==detailt_like_car,"意向车型不一致");
            Preconditions.checkArgument(buycar==list_buycar && buycar==detail_buycar,"是否订车不一致");
            Preconditions.checkArgument(pretime.equals(list_time) && pretime.equals(detail_time),"预计购车时间不一致");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建客户，新建时信息、列表页信息、详情页信息一致");
        }

    }


    /**
     * ==============PC-人脸排除=================
     * */
    @Test
    public void faceOut() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(cstm.baoshijie,cstm.pwd);

            JSONObject data = crm.faceOutList(1, 200);
            int beforeAdd = data.getInteger("total");
            List<Integer> idList = JsonpathUtil.readIntListUsingJsonPath(data.toJSONString(), "$..id");
            //上传人脸
            crm.faceOutUpload(cstm.jpgPath);

            //人脸数量+1
            data = crm.faceOutList(1, 200);
            int afterAdd = data.getInteger("total");
            int diff = afterAdd - beforeAdd;
            Preconditions.checkArgument(diff==1,"人脸排除，新增上传1人，总数未+1");

            //获取人脸id
            List<Integer> idList2 = JsonpathUtil.readIntListUsingJsonPath(data.toJSONString(), "$..id");
            List<Integer> diffList = crm.getDiff(idList, idList2);

            //删除人脸
            if (diffList.size() == 1) {
                for (Integer id : diffList) {
                    crm.faceOutDel(id);
                }
            }

            //人脸数量-1
            int afterSub = crm.faceOutList(1, 200).getInteger("total");
            Preconditions.checkArgument(afterSub==beforeAdd,"人脸排除，删除1人，期待：" + beforeAdd + ", 实际：" + afterSub);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("人脸排除-新增、删除验证");
        }


    }

    /**
     * ==============app前台-销售排班=================  未调试
     * */

    //未调试
    @Test
    public void ReceptionOrderToBusy1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //销售登陆
            crm.login(cstm.lxqgw,cstm.pwd);
            //获取当前状态
            String status1 = crm.userStatus().getString("user_status");
            if (!status1.equals("RECEPTIVE")){
                crm.updateStatus("RECEPTIVE"); //当前不是空闲，则转为空闲
            }
            //前台登陆
            crm.login(cstm.qt,cstm.pwd);
            //当前销售排班中忙碌的数量
            int before = 0;
            JSONArray list = crm.saleOrderList().getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                if (single.getString("sale_status").equals("BUSY")){
                    before = before+1;
                }
            }
            //销售登陆
            crm.login(cstm.lxqgw,cstm.pwd);
            //空闲->忙碌
            crm.updateStatus("BUSY");

            //前台登陆
            crm.login(cstm.qt,cstm.pwd);
            //当前销售排班中忙碌的数量
            int after = 0;
            JSONArray list2 = crm.saleOrderList().getJSONArray("list");
            for (int i = 0; i < list2.size();i++){
                JSONObject single = list2.getJSONObject(i);
                if (single.getString("sale_status").equals("BUSY")){
                    after = after+1;
                }
            }
            int change = after - before;
            Preconditions.checkArgument(change==1,"增加了"+ change);

            String appstatus = crm.userStatus().getString("user_status");
            Preconditions.checkArgument(appstatus.equals("BUSY"),"app状态为"+appstatus);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售手动改变状态，空闲->忙碌，销售排班中忙碌+1，app状态=忙碌");
        }
    }


    @Test
    public void ReceptionOrderToBusy2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //销售登陆
            crm.login(cstm.lxqgw,cstm.pwd);

            //获取当前状态
            String status1 = crm.userStatus().getString("user_status");
            if (!status1.equals("DAY_OFF")){
                crm.updateStatus("DAY_OFF"); //当前不是休假，则转为休假
            }
            //前台登陆
            crm.login(cstm.qt,cstm.pwd);
            //当前销售排班中忙碌的数量
            int before = 0;
            JSONArray list = crm.saleOrderList().getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                if (single.getString("sale_status").equals("BUSY")){
                    before = before+1;
                }
            }
            //销售登陆
            crm.login(cstm.lxqgw,cstm.pwd);
            //休假->忙碌
            crm.updateStatus("BUSY");

            //前台登陆
            crm.login(cstm.qt,cstm.pwd);
            //当前销售排班中忙碌的数量
            int after = 0;
            JSONArray list2 = crm.saleOrderList().getJSONArray("list");
            for (int i = 0; i < list2.size();i++){
                JSONObject single = list2.getJSONObject(i);
                if (single.getString("sale_status").equals("BUSY")){
                    after = after+1;
                }
            }
            int change = after - before;
            Preconditions.checkArgument(change==1,"增加了"+ change);
            String appstatus = crm.userStatus().getString("user_status");
            Preconditions.checkArgument(appstatus.equals("BUSY"),"app状态为"+appstatus);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售手动改变状态，休假->忙碌，销售排班中忙碌+1，app状态=忙碌");
        }
    }


    @Test
    public void ReceptionOrderToDayoff1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //销售登陆
            crm.login(cstm.lxqgw,cstm.pwd);
            //获取当前状态
            String status1 = crm.userStatus().getString("user_status");
            if (!status1.equals("RECEPTIVE")){
                crm.updateStatus("RECEPTIVE"); //当前不是空闲，则转为空闲
            }

            //前台登陆
            crm.login(cstm.qt,cstm.pwd);
            //当前销售排班中休假的数量
            int before = 0;
            JSONArray list = crm.saleOrderList().getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                if (single.getString("sale_status").equals("DAY_OFF")){
                    before = before+1;
                }
            }

            //销售登陆
            crm.login(cstm.lxqgw,cstm.pwd);
            //空闲->忙碌
            crm.updateStatus("DAY_OFF");

            //前台登陆
            crm.login(cstm.qt,cstm.pwd);
            //当前销售排班中休假的数量
            int after = 0;
            JSONArray list2 = crm.saleOrderList().getJSONArray("list");
            for (int i = 0; i < list2.size();i++){
                JSONObject single = list2.getJSONObject(i);
                if (single.getString("sale_status").equals("DAY_OFF")){
                    after = after+1;
                }
            }
            int change = after - before;
            Preconditions.checkArgument(change==1,"增加了"+ change);
            String appstatus = crm.userStatus().getString("user_status");
            Preconditions.checkArgument(appstatus.equals("DAY_OFF"),"app状态为"+appstatus);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售手动改变状态，空闲->休假，销售排班中休假+1，，app状态=休假");
        }
    }


    @Test
    public void ReceptionOrderToDayoff2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //销售登陆
            crm.login(cstm.lxqgw,cstm.pwd);
            //获取当前状态
            String status1 = crm.userStatus().getString("user_status");
            if (!status1.equals("BUSY")){
                crm.updateStatus("BUSY"); //当前不是忙碌，则转为忙碌
            }

            //前台登陆
            crm.login(cstm.qt,cstm.pwd);
            //当前销售排班中休假的数量
            int before = 0;
            JSONArray list = crm.saleOrderList().getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                if (single.getString("sale_status").equals("DAY_OFF")){
                    before = before+1;
                }
            }

            //销售登陆
            crm.login(cstm.lxqgw,cstm.pwd);
            //忙碌->休假
            crm.updateStatus("DAY_OFF");
            //前台登陆
            crm.login(cstm.qt,cstm.pwd);
            //当前销售排班中休假的数量
            int after = 0;
            JSONArray list2 = crm.saleOrderList().getJSONArray("list");
            for (int i = 0; i < list2.size();i++){
                JSONObject single = list2.getJSONObject(i);
                if (single.getString("sale_status").equals("DAY_OFF")){
                    after = after+1;
                }
            }
            int change = after - before;
            Preconditions.checkArgument(change==1,"增加了"+ change);

            String appstatus = crm.userStatus().getString("user_status");
            Preconditions.checkArgument(appstatus.equals("DAY_OFF"),"app状态为"+appstatus);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售手动改变状态，忙碌->休假，销售排班中休假+1，app状态=休假");
        }
    }


    @Test
    public void ReceptionOrderToRECEPTIVE1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //销售登陆
            crm.login(cstm.lxqgw,cstm.pwd);
            //获取当前状态
            String status1 = crm.userStatus().getString("user_status");
            if (!status1.equals("DAY_OFF")){
                crm.updateStatus("DAY_OFF"); //当前不是休假，则转为休假
            }

            //前台登陆
            crm.login(cstm.qt,cstm.pwd);
            //当前销售排班中空闲的数量
            int before = 0;
            JSONArray list = crm.saleOrderList().getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                if (single.getString("sale_status").equals("RECEPTIVE")){
                    before = before+1;
                }
            }

            //销售登陆
            crm.login(cstm.lxqgw,cstm.pwd);
            //休假->空闲
            crm.updateStatus("RECEPTIVE");

            //前台登陆
            crm.login(cstm.qt,cstm.pwd);
            //当前销售排班中空闲的数量
            int after = 0;
            JSONArray list2 = crm.saleOrderList().getJSONArray("list");
            for (int i = 0; i < list2.size();i++){
                JSONObject single = list2.getJSONObject(i);
                if (single.getString("sale_status").equals("RECEPTIVE")){
                    after = after+1;
                }
            }
            int change = after - before;
            Preconditions.checkArgument(change==1,"增加了"+ change);

            String appstatus = crm.userStatus().getString("user_status");
            Preconditions.checkArgument(appstatus.equals("RECEPTIVE"),"app状态为"+appstatus);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售手动改变状态，休假->空闲，销售排班中空闲+1，app状态=空闲");
        }
    }


    @Test
    public void ReceptionOrderToRECEPTIVE2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //销售登陆
            crm.login(cstm.lxqgw,cstm.pwd);
            //获取当前状态
            String status1 = crm.userStatus().getString("user_status");
            if (!status1.equals("BUSY")){
                crm.updateStatus("BUSY"); //当前不是忙碌，则转为忙碌
            }
            //前台登陆
            crm.login(cstm.qt,cstm.pwd);
            //当前销售排班中空闲的数量
            int before = 0;
            JSONArray list = crm.saleOrderList().getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                if (single.getString("sale_status").equals("RECEPTIVE")){
                    before = before+1;
                }
            }

            //销售登陆
            crm.login(cstm.lxqgw,cstm.pwd);
            //忙碌->休假
            crm.updateStatus("RECEPTIVE");
            //前台登陆
            crm.login(cstm.qt,cstm.pwd);
            //当前销售排班中空闲的数量
            int after = 0;
            JSONArray list2 = crm.saleOrderList().getJSONArray("list");
            for (int i = 0; i < list2.size();i++){
                JSONObject single = list2.getJSONObject(i);
                if (single.getString("sale_status").equals("RECEPTIVE")){
                    after = after+1;
                }
            }
            int change = after - before;
            Preconditions.checkArgument(change==1,"增加了"+ change);

            String appstatus = crm.userStatus().getString("user_status");
            Preconditions.checkArgument(appstatus.equals("RECEPTIVE"),"app状态为"+appstatus);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售手动改变状态，忙碌->空闲，销售排班中空闲+1，app状态=空闲");
        }
    }











    /**
     *
     * ====================PC我的工作-工作安排======================
     * */
//    @Test(priority = 9) //建议最后执行，因为case步骤需要sleep
//    public void inScheduleChkStatus() {
//        logger.logCaseStart(caseResult.getCaseName());
//        boolean needLoginBack=false;
//        try {
//
//            //销售顾问登陆
//            crm.login(cstm.lxqgw,cstm.pwd);
//            //创建工作安排
//            String schedulename = "其他安排";
//            String scheduledesc="十个字十个字十个字十一";
//            Date date = new Date();
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//            String scheduledate = df.format(date);
//            int startM = 2;
//            String starttime = dt.getHHmm(startM);//当前时间
//            String endtime = dt.getHHmm(10+startM);//1分钟之后
//            Long scheduleid = crm.scheduleAdd_PC(schedulename,scheduledesc,scheduledate,starttime,endtime).getLong("id");
//
//            //等待N分钟
//            Thread.sleep(1000*60*startM);
//
//            //销售总监登陆
//            needLoginBack=true;
//            crm.login(qiantainame,qiantaipwd);
//            //销售排班页面-查询改销售状态
//            String status = crm.userStatus(saleShowName);
//            Preconditions.checkArgument(status.equals("忙碌"),"期待[忙碌]，实际状态为"+status);
//
//            //销售登陆
//            crm.login(cstm.lxqgw,cstm.pwd);
//            needLoginBack=false;
//            //删除工作安排，需要等10分钟，故不删除
//            //crm.scheduleDel_PC(scheduleid);
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            if(needLoginBack){
//                //销售登陆
//                crm.login(cstm.lxqgw,cstm.pwd);
//            }
//
//            saveData("PC端工作安排时间内（不手动修改状态）,顾问状态=忙碌");
//        }
//
//    }

    @Ignore //3.0取消
    @Test(dataProvider = "WORK_TYPE", dataProviderClass = CrmScenarioUtil.class)
    public void addScheduleChkNum(String workType) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //查看数量
            int total1 = crm.scheduleList_PC(1,1,scheduledate,"").getInteger("total");
            //创建工作安排
            String schedulename = workType;
            String scheduledesc="交车服务描述交车服务描述交车服务描述";

            int startM = 60;
            String starttime = dt.getHHmm(startM);//当前时间
            String endtime = dt.getHHmm(10+startM);//1分钟之后
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

    @Ignore //3.0取消
    @Test(dataProvider = "WORK_TYPE", dataProviderClass = CrmScenarioUtil.class)
    public void addScheduleChkContent(String workType) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String scheduledate = dt.getHistoryDate(0); //今天日期

            //创建工作安排
            String schedulename = workType;
            String scheduledesc="交车服务描述交车服务描述交车服务描述";

            int startM = 71;
            String starttime = dt.getHHmm(startM);//1分钟之后
            String endtime = dt.getHHmm(10+startM);//2分钟之后
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
                if (single.getLong("id").equals(scheduleid)) {
                    search_name = single.getString("name");
                    search_desc = single.getString("description");
                    search_date = single.getString("date");
                    search_start = single.getString("start_time");
                    search_end = single.getString("end_time");
                    break;
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


    /**
     *
     * ====================我的工作-我的回访======================
     * */
    @Ignore //3.0取消
    @Test
    public void taskListChkNum() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            crm.updateStatus("RECEPTIVE");
            String today = dt.getHistoryDate(0); //今天日期

            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            customerid = creatCust(name,phone);
            //完成接待

            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //PC端今日工作-我的回访数量
            int pctotal = crm.taskList_PC(today,-1,1,50, phone).getInteger("total");

            //app端 已联系+未联系数量
            int apptotal = crm.taskList_APP(today,1,50, phone).getInteger("total");


            Preconditions.checkArgument(pctotal==apptotal,"PC"+pctotal+"条，app"+ apptotal+"条");

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
            saveData("（app）未联系+已联系数量 == （PC）我的回访数量");
        }

    }

    //http://192.168.50.2:8081/bug-view-2192.html
    //@Ignore //3.0取消
    //@Test
    public void taskListChkNum_buycar() {
        logger.logCaseStart(caseResult.getCaseName());
        Long  customerid=-1L;
        try {

            String today = dt.getHistoryDate(0); //今天日期

            customerid = creatCust(name,phone);
            //完成接待

            //修改
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //PC端今日工作-我的回访数量
            int pctotal_before = crm.taskList_PC(today,-1,1,10, phone).getInteger("total");

            //顾客订车，修改顾客信息为已订车，0为订车
            crm.customerEditPC(customerid,0, phone);

            //PC今日工作-今日来访列表中订车为是
            JSONObject data = crm.todayListPC(-1,"",phone,"",0,0,1,100);
            JSONObject info = data.getJSONArray("list").getJSONObject(0);
            String buyCar = info.getString("buy_car_name");
            Preconditions.checkArgument(buyCar.contains("是"),"修改客户为已订车，实际得到客户订车信息：" + buyCar);

            //PC端今日工作-我的回访数量
            int pctotal_after = crm.taskList_PC(today,-1,1,10, phone).getInteger("total");
            int change = pctotal_before - pctotal_after;
            Preconditions.checkArgument(change==1,"修改客户信息未订车，顾客未在我的回访中消失");

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
            saveData("顾客订车，当天我的回访数量-1");
        }

    }

    @Ignore //3.0取消
    @Test
    public void taskListChkNum_delcustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {

            String today = dt.getHistoryDate(0); //今天日期

            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            customerid = creatCust(name,phone);
            //完成接待

            Thread.sleep(1000);
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //PC端今日工作-我的回访数量
            int pctotal_before = crm.taskList_PC(today,-1,1,10, phone).getInteger("total");

            clearCustomer(customerid);
            customerid = -1L;
            //PC端今日工作-我的回访数量
            int pctotal_after = crm.taskList_PC(today,-1,1,10, phone).getInteger("total");

            int change = pctotal_before - pctotal_after;
            Preconditions.checkArgument(change==1,"删除顾客，顾客未在我的回访中消失");


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
            saveData("删除顾客，当天我的回访数量-1");
        }

    }

    /**
     *
     * ====================我的工作-今日来访======================
     * */

    @Ignore //3.0取消
    @Test
    public void addCustChkTodayListnum() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {

            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }

            freeFirstLogin();

            //PC端今日工作-今日来访数量
            int todaylist_before = crm.todayListPC(-1,"","","",0,0,1,200).getInteger("total");

            creatCust(name,phone);


            //PC端今日工作-今日来访数量
            int todaylist_after = crm.todayListPC(-1,"","","",0,0,1,200).getInteger("total");

            int change = todaylist_after - todaylist_before;
            Preconditions.checkArgument(change==1,"增加一个顾客，来访量增加"+change);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("app创建客户，今日来访+1");
        }

    }

    //@Test //2.1取消手机号合并 这里不做校验
    public void addCustRePhoneChkTodayListnum() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            //前台登陆
            crm.login(cstm.qt,cstm.pwd);
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
            //销售登陆，获取当前接待id
            crm.login(userLoginName, pwd);
            //获取原销售今天之前创建过的客户手机号
            String starttime = "2020-02-01";
            String endtime = dt.getHistoryDate(-1);

            String phone = crm.customerListPC("",-1,"","",starttime,endtime,1,1).getJSONArray("list").getJSONObject(0).getString("customer_phone");
            //PC端今日工作-今日来访数量
            int todaylist_before = crm.todayListPC(-1,"","","",0,0,1,200).getInteger("total");

            customerid = creatCust(name,phone);

            crm.login(userLoginName, pwd);
            //PC端今日工作-今日来访数量
            int todaylist_after = crm.todayListPC(-1,"","","",0,0,1,200).getInteger("total");

            int change = todaylist_after - todaylist_before;
            Preconditions.checkArgument(change==1,"原销售来访量增加"+change);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("V1.1 app其他销售顾问创建已存在手机号客户，原销售顾问今日来访+1");
        }

    }

    //@Test //2.1取消手机号合并 这里不做校验
    public void addCustOnlyCreatChkTodayListnum() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {

            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }

            //PC端今日工作-今日来访数量
            int todaylist_before = crm.todayListPC(-1,"","","",0,0,1,200).getInteger("total");

            customerid = creatCust(name,phone);


            //PC端今日工作-今日来访数量
            int todaylist_after = crm.todayListPC(-1,"","","",0,0,1,200).getInteger("total");


            //完成接待

            //PC端今日工作-今日来访数量
            int todaylist_after2 = crm.todayListPC(-1,"","","",0,0,1,200).getInteger("total");


            int change = todaylist_after - todaylist_before;
            Preconditions.checkArgument(change==1,"点击创建，今日来访增加"+change);

            int change2 = todaylist_after2 - todaylist_after;
            Preconditions.checkArgument(change2==0,"手机号不存在，今日来访增加"+change);


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
            saveData("V1.1 app点击创建顾客按钮，不保存，今日来访+1；创建后手机号不存在，数量不变");
        }

    }

    //@Test //2.1取消手机号合并 这里不做校验
    public void addCustRePhoneNotBelongChkTodayListnum() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {

            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }

            customerid = creatCust(name,phone);

            //完成接待


            customerid = creatCust(name,phone);
            //PC端今日工作-今日来访数量
            int todaylist_before = crm.todayListPC(-1,"","","",0,0,1,200).getInteger("total");

            //PC端今日工作-今日来访数量
            int todaylist_after = crm.todayListPC(-1,"","","",0,0,1,200).getInteger("total");

            int change = todaylist_after - todaylist_before;
            Preconditions.checkArgument(change==-1,"新销售来访量增加"+change);


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
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("V1.1 app其他销售顾问创建已存在手机号客户，新销售顾问今日来访-1");
        }

    }

    //@Test //2.1取消手机号合并 这里不做校验
    public void addCustRePhoneBelongChkTodayListnum() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {

            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            customerid = creatCust(name,phone);
            //完成接待

            //PC端今日工作-今日来访数量
            int todaylist_before = crm.todayListPC(-1,"","","",0,0,1,200).getInteger("total");


            customerid = creatCust(name,phone);
            //PC端今日工作-今日来访数量
            int todaylist_after = crm.todayListPC(-1,"","","",0,0,1,200).getInteger("total");

            int change = todaylist_after - todaylist_before;
            Preconditions.checkArgument(change==0,"来访量增加"+change);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("V1.1 app销售顾问创建自己创建过的手机号客户，今日来访数量不变");
        }

    }



    /**
     *
     * ====================我的客户======================
     * */
//    @Test//工作量较大 有空再改
//    public void customerListChkNum() {
//        logger.logCaseStart(caseResult.getCaseName());
//
//        try {
//
//            String phone = "1";
//            for (int i = 0; i < 10;i++){
//                String a = Integer.toString((int)(Math.random()*10));
//                phone = phone + a;
//            }
//            freeFirstLogin();
//            //我的客户条数
//            int before = crm.customerListPC("",-1,"","",0L,0L,1,1).getInteger("total");
//
//            //获取顾客id
//            crm.login(cstm.qt,cstm.pwd);
//            String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
//            //
//            String userLoginName = "";
//            JSONArray userlist = crm.userPage(1, 100).getJSONArray("list");
//            for (int i = 0; i < userlist.size(); i++) {
//                JSONObject obj = userlist.getJSONObject(i);
//                if (obj.getString("user_id").equals(sale_id)) {
//                    userLoginName = obj.getString("user_login_name");
//                }
//            }
//            //创建接待
//            Long customerid = creatCust(name,phone);
//
//            //我的客户条数
//            int after2 = crm.customerListPC("",-1,"","",0L,0L,1,200).getInteger("total");
//
//            int change1 = after1 - before;
//            Preconditions.checkArgument(change1==1,"仅点击创建按钮增加了" + change1 + "条");
//
//            int change2 = after2 - after1;
//            Preconditions.checkArgument(change2==0,"手机号不存在增加了" + change2 + "条");
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("appV1.1 app点击创建顾客按钮，不保存，我的客户+1；创建不存在手机号，数量不变");
//        }
//
//    }

    //@Test //V2.1取消手机号合并
    public void customerListNotBelongChkNum() {
        logger.logCaseStart(caseResult.getCaseName());

        try {

            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }

            //所属销售创建
            //获取顾客id
            Long customerid = crm.getCustomerId();
            //创建某级客户
            JSONObject customer = crm.customerEdit_onlyNec(customerid,7,name,phone,"H级客户-----"+System.currentTimeMillis()+"自动化-----");
            //完成接待


            //新销售创建存在手机号
            //crm.login(salename2,salepwd2);
            //获取顾客id
            Long customerid2 = crm.getCustomerId();
            //我的客户条数
            int before = crm.customerListPC("",-1,"","",0L,0L,1,1).getInteger("total");

            //创建某级客户
            JSONObject customer2 = crm.customerEdit_onlyNec(customerid2,7,name,phone,"H级客户-----"+System.currentTimeMillis()+"自动化-----");

            //我的客户条数
            int after1 = crm.customerListPC("",-1,"","",0L,0L,1,200).getInteger("total");

            int change1 = after1 - before;
            Preconditions.checkArgument(change1==-1,"创建成功后增加了" + change1 + "条");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("V1.1 非所属销售app创建已存在手机号顾客后，我的客户-1");
        }

    }

    //@Test//V2.1取消手机号合并
    public void customerListBelongChkNum() {
        logger.logCaseStart(caseResult.getCaseName());

        try {

            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }

            //获取顾客id
            Long customerid = crm.getCustomerId();
            //创建某级客户
            JSONObject customer = crm.customerEdit_onlyNec(customerid,7,name,phone,"H级客户-----"+System.currentTimeMillis()+"自动化-----");
            //完成接待

            //我的客户条数
            int before = crm.customerListPC("",-1,"","",0L,0L,1,1).getInteger("total");


            //获取顾客id
            Long customerid2 = crm.getCustomerId();

            //创建某级客户
            JSONObject customer2 = crm.customerEdit_onlyNec(customerid2,7,name,phone,"H级客户-----"+System.currentTimeMillis()+"自动化-----");

            //我的客户条数
            int after1 = crm.customerListPC("",-1,"","",0L,0L,1,200).getInteger("total");

            int change1 = after1 - before;
            Preconditions.checkArgument(change1==0,"创建成功后增加了" + change1 + "条");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("V1.1 所属销售app创建自己已存在手机号顾客后，我的客户不变");
        }

    }

    @Test
    public void customerListChkcontent() {
        logger.logCaseStart(caseResult.getCaseName());


        try {

            String today = dt.getHistoryDate(0); //今天日期
            String phone1 = phone.substring(3);

            int like_car = 3;
            String compare_car = "宾利";
            int buy_car_attribute = 3;
            int buy_car = 1;
            String pre_buy_time = today;

            Long customerid = creatCust(name,phone);
            //完成接待
            String search_name ="";
            String search_phone ="";
            long search_level =-1L;
            int search_like =-1;
            String search_compare ="";
            int search_attribute =-1;
            int search_buy = -1;
            String search_pre ="";

            //查询顾客信息
            JSONArray search = crm.customerListPC("",-1,name,phone,0,0,1,200).getJSONArray("list");
            for (int i = 0; i<search.size();i++){
                JSONObject single = search.getJSONObject(i);
                if (single.getLong("customer_id").equals(customerid)){
                    search_name = single.getString("customer_name");
                    search_phone = single.getString("customer_phone");
                    search_like = single.getInteger("like_car");
                    search_compare = single.getString("compare_car");
                    search_attribute = single.getInteger("buy_car_attribute");
                    search_buy = single.getInteger("buy_car");
                    search_pre = single.getString("pre_buy_time");
                    break;
                }
            }

            Preconditions.checkArgument(search_name.equals(name),"姓名不一致");
            Preconditions.checkArgument(search_phone.equals(phone1),"手机号不一致");
            Preconditions.checkArgument(search_like==like_car,"意向车型不一致");
            Preconditions.checkArgument(search_compare.equals(compare_car),"对比车型不一致");
            Preconditions.checkArgument(search_attribute==buy_car_attribute,"购车属性不一致");
            Preconditions.checkArgument(search_buy==buy_car,"是否订车不一致");
            Preconditions.checkArgument(search_pre.equals(pre_buy_time),"预计购车时间不一致");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("app创建客户，PC我的客户页面列表与新建时信息一致");
        }

    }

    //@Test V2.1取消手机号合并
    public void customerListRePhoneChkcontent() {
        logger.logCaseStart(caseResult.getCaseName());


        try {

            String today = dt.getHistoryDate(0); //今天日期

            String customer_name = "顾客姓名";
            String customer_phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                customer_phone = customer_phone + a;
            }

            int like_car = 3;
            String compare_car = "宾利";
            int buy_car_attribute = 3;
            int buy_car = 1;
            String pre_buy_time1 = today;
            String pre_buy_time2 = dt.getHistoryDate(1); //更新

            //原销售顾问（自动化）获取顾客id
            Long customerid = crm.getCustomerId();
            //创建某级客户
            JSONObject customer = crm.customerEdit_car(customerid,7,customer_name,customer_phone,pre_buy_time1,compare_car,like_car,buy_car,4,3,buy_car_attribute,"H级客户-taskListChkNum-修改时间为昨天");

            //完成接待

            //新销售顾问（自动化2）创建顾客
            //crm.login(salename2,salepwd2);
            Long customerid2 = crm.getCustomerId();
            //创建某级客户
            JSONObject customer2 = crm.customerEdit_several(customerid2,7,customer_name,customer_phone,pre_buy_time2,compare_car,0,"H级客户-taskListChkNum-修改时间为昨天");

            crm.login(cstm.lxqgw,cstm.pwd);
            String search_name ="";
            String search_phone ="";
            long search_level =-1L;
            int search_like =-1;
            String search_compare ="";
            int search_attribute =-1;
            int search_buy = -1;
            String search_pre ="";
            int search_pay_type = -1;
            String search_belongs_sale_id = "";

            //查询顾客信息
            JSONArray search = crm.customerListPC("",-1,customer_name,customer_phone,0,0,1,200).getJSONArray("list");
            for (int i = 0; i<search.size();i++){
                JSONObject single = search.getJSONObject(i);
                if (single.getLong("customer_id").equals(customerid)){
                    search_belongs_sale_id = single.getString("belongs_sale_id");
                    search_name = single.getString("customer_name");
                    search_phone = single.getString("customer_phone");
                    search_like = single.getInteger("like_car");
                    search_compare = single.getString("compare_car");
                    search_attribute = single.getInteger("buy_car_attribute");
                    search_buy = single.getInteger("buy_car");
                    search_pre = single.getString("pre_buy_time");
                    search_pay_type = single.getInteger("pay_type");
                    break;
                }
            }

            Preconditions.checkArgument(search_name.equals(customer_name),"姓名不正确");
            Preconditions.checkArgument(search_belongs_sale_id.equals(sale_id),"所属销售不正确");
            Preconditions.checkArgument(search_phone.equals(customer_phone),"手机号正确");
            Preconditions.checkArgument(search_like==like_car,"意向车型不正确");
            Preconditions.checkArgument(search_compare.equals(compare_car),"对比车型不正确");
            Preconditions.checkArgument(search_attribute==buy_car_attribute,"购车属性不正确");
            Preconditions.checkArgument(search_buy==buy_car,"是否订车不正确");
            Preconditions.checkArgument(search_pre.equals(pre_buy_time2),"预计购车时间不正确");
            Preconditions.checkArgument(search_pay_type==0,"付款方式不正确");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("V1.1 app创建已存在手机号客户，PC客户详情根据变更内容更新，未变更内容不作处理");
        }

    }

//    @Test//要改
//    public void customerListDelChkOrderNum() {
//        logger.logCaseStart(caseResult.getCaseName());
//
//        try {
//
//            int before = 0;
//            int after = 0;
//            int afterdel = 0;
//
//            String userlogin = freeFirstLogin();
//
//            JSONArray list1 = crm.receptionOrder().getJSONArray("list");
//            for (int i = 0; i < list1.size();i++){
//                JSONObject single = list1.getJSONObject(i);
//                if (single.getString("sale_name").equals(saleShowName)){
//                    before = single.getInteger("today_customer_num");
//                    break;
//                }
//            }
//
//            String phone = "1";
//            for (int i = 0; i < 10;i++){
//                String a = Integer.toString((int)(Math.random()*10));
//                phone = phone + a;
//            }
//            Long customerid = creatCust(name,phone);
//            //完成接待
//
//            Thread.sleep(1000);
//            //查看今日接待数量
//            JSONArray list2 = crm.receptionOrder().getJSONArray("list");
//            for (int i = 0; i < list2.size();i++){
//                JSONObject single = list2.getJSONObject(i);
//                if (single.getString("sale_name").equals(saleShowName)){
//                    after = single.getInteger("today_customer_num");
//                    break;
//                }
//            }
//            int change1 = after - before;
//            Preconditions.checkArgument(change1==1,"创建客户后，今日接待人数增加了" + change1);
//            crm.login(zjlname,zjlpwd);
//            //删除客户
//            crm.customerDeletePC(customerid);
//            crm.login(userlogin,pwd);
//            //查看今日接待数量
//            JSONArray list3 = crm.receptionOrder().getJSONArray("list");
//            for (int i = 0; i < list3.size();i++){
//                JSONObject single = list3.getJSONObject(i);
//                if (single.getString("sale_name").equals(saleShowName)){
//                    afterdel = single.getInteger("today_customer_num");
//                    break;
//                }
//            }
//            int change2 = afterdel - after;
//            Preconditions.checkArgument(change2==0,"删除客户后，今日接待人数减少了" + change2);
//
//
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//           crm.login(cstm.lxqgw,cstm.pwd);
//            saveData("我的客户删除一条，销售排班中的今日接待人数不变");
//        }
//
//    }

    @Ignore //3.0取消
    @Test
    public void customerListDelChkTodayList() {
        logger.logCaseStart(caseResult.getCaseName());
        long customerid=-1L;
        try {

            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            customerid = creatCust(name,phone);
            //完成接待

            Thread.sleep(1000);

            //查看今日来访顾客信息存在
            boolean exist = false;
            JSONArray list2 = crm.todayListPC(-1,"","","",0,0,1,100).getJSONArray("list");
            for (int i=0; i<list2.size(); i++){
                JSONObject single = list2.getJSONObject(i);
                if (single.getString("customer_id").equals(String.valueOf(customerid))){
                    exist = true;
                    break;
                }
            }
            Preconditions.checkArgument(exist==true,"新增顾客后，今日来访中无顾客信息");

            //删除客户
            crm.login(cstm.xszj,cstm.pwd);
            crm.customerDeletePC(customerid);



            //查看今日接待数量
            exist = true;
            JSONArray list3 = crm.todayListPC(-1,"","","",0,0,1,100).getJSONArray("list");
            for (int i = 0; i < list3.size();i++){
                JSONObject single = list3.getJSONObject(i);
                if (single.getString("customer_id").equals(String.valueOf(customerid))){
                    exist = false;
                }
            }
            Preconditions.checkArgument(exist==true,"未删除");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("我的客户删除一条，今日来访信息删除");
        }

    }



    @Ignore //3.0取消 手机号不可重复
    @Test
    public void addVisitRePhoneChkNum() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            crm.updateStatus("RECEPTIVE");

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name,phone);

            int size1 = crm.customerDetailPC(customerid).getJSONArray("visit").size();

            creatCust(name,phone);

            int size2 = crm.customerDetailPC(customerid).getJSONArray("visit").size();

            int change = size2 - size1;

            Preconditions.checkArgument(change==1,"来访记录条数增加了"+ change);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("V1.1 app销售顾问创建已存在手机号客户，顾客来访记录+1");
        }

    }



//    @Test
//    public void customerAddDriver() {
//        logger.logCaseStart(caseResult.getCaseName());
//        long customerid=-1L;
//        try {
//
//            long level_id=7L;
//            String phone = "1";
//            for (int i = 0; i < 10;i++){
//                String a = Integer.toString((int)(Math.random()*10));
//                phone = phone + a;
//            }
//            String name = dt.getHistoryDate(0);
//            String desc = "创建H级客户自动化------------------------------------";
//
//
//            customerid = creatCust(name,phone);
//
//
//            //创建试驾信息
//
//            String idCard = "110226198210260078";
//            String gender = "男";
//            String signTime = dt.getHistoryDate(0);
//            String model = "911";
//            String country = "中国";
//            String city = "图们";
//            String email = dt.getHistoryDate(0)+"@qq.com";
//            String address = "北京市昌平区";
//            String ward_name = "小小";
//            String driverLicensePhoto1Url = picurl;
//            String driverLicensePhoto2Url = picurl;
//            String electronicContractUrl = picurl;
//            crm.driveradd(name,idCard,gender,phone,signTime,"试乘试驾",model,country,city,email,address,ward_name,driverLicensePhoto1Url,driverLicensePhoto2Url,electronicContractUrl).getInteger("id");
//
//            //查看客户详情中的是否试驾信息
//            JSONObject detail = crm.customerDetailPC(customerid);
//            String if_test_drive_name = detail.getString("if_test_drive_name");
//            Preconditions.checkArgument(if_test_drive_name.equals("否"),"是否试驾信息为"+if_test_drive_name);
//
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            try{
//                clearCustomer(customerid);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//            saveData("创建和顾客姓名一致的试驾信息，顾客详情中是否试驾=否");
//        }
//
//    }
//
//    @Test
//    public void customerAddDeliver() {
//        logger.logCaseStart(caseResult.getCaseName());
//        long customerid=-1L;
//        try {
//
//
//            String phone = "1";
//            for (int i = 0; i < 10;i++){
//                String a = Integer.toString((int)(Math.random()*10));
//                phone = phone + a;
//            }
//            String name = dt.getHistoryDate(0);
//
//
//            customerid = creatCust(name,phone);
//
//
//
//            //新建交车
//            String gender = "男";
//            String signTime = dt.getHistoryDate(0);
//            String model = "911";
//            int id = crm.deliverAdd(name, gender, phone, signTime, model, picurl).getInteger("id");
//
//            //查看客户详情中的是否交车信息
//            JSONObject detail = crm.customerDetailPC(customerid);
//            String if_confirm_car_name = detail.getString("if_confirm_car_name");
//            Preconditions.checkArgument(if_confirm_car_name.equals("否"),"是否交车信息为"+if_confirm_car_name);
//
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            try{
//                clearCustomer(customerid);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//            saveData("创建和顾客姓名一致的交车信息，顾客详情中是否交车=否");
//        }
//
//    }

    //@Test //2.1合并逻辑更改，case作废
    public void customerRePhoneChkVisitNum() {
        logger.logCaseStart(caseResult.getCaseName());

        try {


            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            Long customerid1 = creatCust(name, phone);
            int size_before = crm.customerDetailPC(customerid1).getJSONArray("visit").size();

            Long customerid2 = creatCust(name, phone);

            int size_after = crm.customerDetailPC(customerid1).getJSONArray("visit").size();

            int change = size_after - size_before;
            Preconditions.checkArgument(change==1,"来访记录条数增加了"+ change);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("app创建已存在手机号顾客，顾客来访记录数量+1");
        }

    }

    //@Test //2.1合并逻辑更改，case作废
    public void customerRePhoneChkRemarkNum() {
        logger.logCaseStart(caseResult.getCaseName());

        try {


            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);

            Long customerid1 = creatCust(name, phone);
            //查看顾客详情，备注条数
            int listbefore = crm.customerDetailPC(customerid1).getJSONArray("remark").size();


            Long customerid2 = creatCust(name, phone);


            //查看顾客详情，备注条数
            int listafter = crm.customerDetailPC(customerid1).getJSONArray("remark").size();
            int change = listafter - listbefore;
            Preconditions.checkArgument(change==1,"备注数期待增加1，实际增加"+change);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("备注记录条数 = 原备注条数+新建相同手机号客户的备注条数");
        }

    }



//    /**
//     * ==============交车服务=================
//     * */
//    @Test
//    public void checkDeliver() {
//        logger.logCaseStart(caseResult.getCaseName());
//        long customerid=-1;
//        try {
//            crm.login(cstm.lxqgw,cstm.pwd);
//            String name = dt.getHistoryDate(0);
//            String gender = "男";
//            String signTime = dt.getHistoryDate(0);
//            String model = "911";
//
//            int beforeAdd = crm.deliverList(name, 1, 50, name, "").getInteger("total");
//
//            //创建交车
//            int id = crm.deliverAdd(name, gender, phone, signTime, model, picurl).getInteger("id");
//
//            //查看交车列表，交车+1
//            JSONObject data = crm.deliverList(name, 1, 50, name, "");
//            int afterAdd = data.getInteger("total");;
//            int diff = afterAdd - beforeAdd;
//            Preconditions.checkArgument(diff==1,"新建交车，交车记录未增加");
//
//            //交车列表页信息，信息一致性校验
//            JSONArray list = data.getJSONArray("list");
//            for (int i=0; i<list.size(); i++) {
//                JSONObject item = list.getJSONObject(i);
//                String dCarTime = item.getString("deliver_car_time");
//                String dCustName = item.getString("customer_name");
//                String dSaleName = item.getString("sale_name");
//                String dCustGend = item.getString("customer_gender");
//
//                Preconditions.checkArgument(dCarTime.equals(name),"新建交车（交车日期：" + name + "），与我的交车列表中交车日期：" +  dCarTime + " 不一致");
//                Preconditions.checkArgument(dCustName.equals(name),"新建交车（客户名称：" + name + "），与我的交车列表中客户名称：" +  dCustName + " 不一致");
//                Preconditions.checkArgument(dSaleName.equals("lxqgw"),"新建交车（销售顾问：lxqgw），与我的交车列表中销售顾问：" +  dSaleName + " 不一致");
//                Preconditions.checkArgument(dCustGend.equals(gender),"新建交车（顾问性别：" + gender + "），与我的交车列表中顾问性别：" +  dCustGend + " 不一致");
//
//            }
//            //删除我的交车，交车-1
//            crm.deliverDelete(id);
//            int afterDel = crm.deliverList(name, 1, 50, name, "").getInteger("total");
//            Preconditions.checkArgument(beforeAdd==afterDel,"删除一个交车记录后记录列表未-1");
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("新建交车->我的工作-我的交车信息验证");
//        }
//    }
//
//    /**
//     * ==============我的试驾=================
//     * */
//
//    @Test
//    public void driverDelChkNum() {
//        logger.logCaseStart(caseResult.getCaseName());
//        int driverid = -1;
//        try {
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
//            String model = "911";
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
//            int totalbefore = crm.driveList(signTime,"","",1,1).getInteger("total");
//            //删除记录
//            crm.driverDel(driverid);
//            //查询
//            int totalafter = crm.driveList(signTime,"","",1,1).getInteger("total");
//            int change = totalbefore - totalafter;
//            Preconditions.checkArgument(change==1,"减少了"+change);
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("删除一条我的试驾，列表数量-1");
//        }
//
//    }


    /**
     * ==============展厅接待=================
     * */
    @Ignore //3.0取消
    @Test
    public void custTodayListChkToday() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String today = dt.getHistoryDate(0);
            JSONArray list = crm.customerTodayList().getJSONArray("list");
            for (int i = 0 ; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                String list_date = obj.getString("day_date");
                Preconditions.checkArgument(list_date.equals(today),"接待日期为"+ list_date);
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("展厅接待展示当天记录");
        }
    }

    @Ignore //3.0取消
    @Test
    public void custTodayListNewCUstChkNum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int before = crm.customerTodayList().getInteger("total");
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            phone = phone.substring(3);

            //获取顾客id
            Long customerid1 = crm.getCustomerId();
            int after = crm.customerTodayList().getInteger("total");
            //创建某级客户
            JSONObject customer = crm.customerEdit_onlyNec(customerid1,7,name,phone,"H级客户-----"+System.currentTimeMillis()+"自动化-----");
            //完成接待
            crm.finishReception(customerid1, 7, name, phone, "H级客户-taskListChkNum-修改时间为昨天");
            int after2 = crm.customerTodayList().getInteger("total");
            int change = after - before;
            Preconditions.checkArgument(change==1,"仅点击创建按钮，增加了"+ change);
            int change2 = after2 - after;
            Preconditions.checkArgument(change2==0,"保存后，增加了"+ change2);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("仅点击创建按钮，展厅接待记录+1；保存后，记录数不变");
        }
    }



























    //前台点击创建接待按钮创建顾客
    public Long creatCust(String name, String phone) throws Exception {
        //前台登陆
        crm.login(cstm.qt,cstm.pwd);
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
        crm.login(userLoginName, pwd);
        customerid = crm.userInfService().getLong("customer_id");
        //创建某级客户
        JSONObject customer = crm.finishReception(customerid, 7, name, phone.substring(3), "H级客户-taskListChkNum-修改时间为昨天");

        return customerid;

    }


    public String  freeFirstLogin() throws Exception{
        //前台登陆
        crm.login(cstm.qt,cstm.pwd);
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
        //销售登陆，获取当前接待id
        crm.login(userLoginName, pwd);
        return  userLoginName;
    }


}
