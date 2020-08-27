package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.CustomerInfo;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.Driver;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;


/**
 * @author : lxq
 * @date :  2020/05/30
 */

public class CrmSystemCase extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    CustomerInfo cstm = new CustomerInfo();



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
     *
     * 上传日常进场车牌
     * 接口说明：https://winsense.yuque.com/staff-qt5ptf/umvi00/mhinpu
     *
     */
    @Test
    public void uploadEnterShopCarPlate() {
//        String carNum = "黑ABC1357";     //售前新，售后老（维修+保养）
//        String carNum = "鲁ABB1711";    //全新
//        String carNum = "浙ABC1711";    //售前老客，售后新客
        String carNum = "京ASD1235";    //售前老客，售后新客
//        String carNum = "京A081800";    //售前新客，售后新客

        String router = "/business/porsche/PLATE_UPLOAD/v1.0";
        //设备与日常环境的设置一致，不要修改
        String deviceId = "7709867521115136";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        ImageUtil imageUtil = new ImageUtil();
        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
        String json = "{\"plate_num\":\"" + carNum +"\"," +
                "\"plate_pic\":\"@0\"," +
                "\"time\":\""+System.currentTimeMillis()+"\"" +
                "}";
        try {
            crm.carUploadToDaily(router, deviceId, resource, json);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("日常入场车牌号上传");
        }
    }

    /**
     *
     * 上传日常离场车牌
     * 接口说明：https://winsense.yuque.com/staff-qt5ptf/umvi00/mhinpu
     *
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
        String json = "{\"plate_num\":\"" + carNum +"\"," +
                "\"plate_pic\":\"@0\"," +
                "\"time\":\""+System.currentTimeMillis()+"\"" +
                "}";
        try {
            crm.carUploadToDaily(router, deviceId, resource, json);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("日常离场车牌号上传");
        }
    }


    /**
     *
     * 上传赢识线上进场车牌
     * 接口说明：https://winsense.yuque.com/staff-qt5ptf/umvi00/mhinpu
     *
     */
    //@Test
    public void uploadOnlineEnterShopCarPlate() {
//        String carNum = "黑ABC1357";     //售前新，售后老（维修+保养）
//        String carNum = "鲁ABB1711";    //全新
//        String carNum = "浙ABC1711";    //售前老客，售后新客
        String carNum = "京ASD1235";    //售前老客，售后新客
//        String carNum = "京A081800";    //售前新客，售后新客

        String router = "/business/porsche/PLATE_UPLOAD/v1.0";
        //设备与线上环境的设置一致，不要修改
        String deviceId = "7736789438301184";//线上设备
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        ImageUtil imageUtil = new ImageUtil();
        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
        String json = "{\"plate_num\":\"" + carNum +"\"," +
                "\"plate_pic\":\"@0\"," +
                "\"time\":\""+System.currentTimeMillis()+"\"" +
                "}";
        try {
            crm.carUploadToDaily(router, deviceId, resource, json);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("线上入场车牌号上传");
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
            String phone1 = phone.substring(3);
            String name = phone;
            customerid = creatCust(name,phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 20 ; i++){
                comment = comment + "备";
            }
            crm.customerEditRemarkPC(customerid,name,phone1,level_id,comment);

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
            String phone1 = phone.substring(3);
            customerid = creatCust(name,phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 200 ; i++){
                comment = comment + "备";
            }
            crm.customerEditRemarkPC(customerid,name,phone1,level_id,comment);

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
            String phone1 = phone.substring(3);
            customerid = creatCust(name,phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 20 ; i++){
                comment = comment + "备";
            }
            for (int i = 0; i < 49;i++){
                crm.customerEditRemarkPC(customerid,name,phone1,level_id,comment);
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


            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name,phone);
            //完成接待


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

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name,phone);
            //完成接待


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
            String phone1 = phone.substring(3);
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            customerid = creatCust(name,phone);
            //完成接待


            //修改创建时间为昨天
            qaDbUtil.updateRetrunVisitTimeToToday(customerid); //顾客id

            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 20 ; i++){
                comment = comment + "备";
            }
            for (int i = 0; i < 49;i++){
                crm.customerEditRemarkPC(customerid,name,phone1,level_id,comment);
            }

            JSONObject obj = crm.customerEditRemarkPCNotChk(customerid,name,phone1,level_id,comment);
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
     * ====================我的客户======================
     * */
    //----------------------查询--------------------
    @Test
    public void customerListSearchAll() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {


            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name,phone);
            //完成接待

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

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name,phone);
            //完成接待


            //姓名查询
            JSONObject obj = crm.customerListPC("",-1,name,"",0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_name = obj.getString("customer_name");
            Preconditions.checkArgument(search_name.equals(name),"查询结果与查询条件不一致");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("我的客户页面根据姓名查询");
        }

    }

    @Test
    public void customerListSearchPhone() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {


            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            customerid = creatCust(name,phone);
            //完成接待



            //查询
            JSONObject obj = crm.customerListPC("",-1,"",phone1,0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_phone = obj.getString("customer_phone");
            Preconditions.checkArgument(search_phone.equals(phone1),"查询结果与查询条件不一致");
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


            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name,phone);
            //完成接待


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

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name,phone);
            //完成接待


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

            crm.login(cstm.lxqgw,cstm.pwd);
            //查询
            JSONArray list = crm.customerListPC("",-1,"","","","",1,50).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                Preconditions.checkArgument(single.getString("belongs_sale_name").equals(cstm.lxqgw),"展示信息不正确");
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


            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            customerid = creatCust(name,phone);
            //完成接待

            //姓名+手机号查询
            int total = crm.customerListPC("",-1,name,phone1,0,0,1,1).getInteger("total");
            Preconditions.checkArgument(total==1,"删除前查询，期待有一条记录，实际"+total);

            //总经理登陆
            crm.login(cstm.xszj,cstm.pwd);
            //删除顾客
            crm.customerDeletePC(customerid);

            //销售顾问登陆
            crm.login(cstm.lxqgw,cstm.pwd);
            //再次查询应无结果
            int total2 = crm.customerListPC("",-1,name,phone1,0,0,1,1).getInteger("total");
            Preconditions.checkArgument(total2==0,"删除后查询，期待无结果，实际"+total);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("我的客户页面删除后再查询");
        }

    }

    //@Test
    public void customerListDelInService() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name,phone);
            //总经理登陆
            crm.login(cstm.xszj,cstm.pwd);
            //删除顾客
            int code = crm.customerDeletePCNotChk(customerid).getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+ code);
            crm.login(cstm.lxqgw,cstm.pwd);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("删除接待中客户");
        }

    }

    @Test
    public void customerListDelServiced() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name,phone);

            //总经理登陆
            crm.login(cstm.xszj,cstm.pwd);
            //删除顾客
            int code = crm.customerDeletePCNotChk(customerid).getInteger("code");
            Preconditions.checkArgument(code==1000,"删除失败");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("删除已完成接待客户");
        }

    }



    //---------------------编辑顾客信息-------------
    @Test
    public void customerListsaleEditPhone() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {


            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String phone1 = phone.substring(3);
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";


            customerid = creatCust(name,phone);
            //完成接待


            crm.customerEditPC(customerid,name,"12312341234",2);

            //再次查询，手机号应不变
            JSONObject obj = crm.customerListPC("",-1,name,"","","",1,1).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(obj.getString("customer_phone").equals(phone1),"手机号改变");


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


            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String phone1 = phone.substring(4);
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            customerid = creatCust(name,phone);
            //完成接待


            //总经理登陆
            crm.login(cstm.xszj,cstm.pwd);
            crm.customerEditPC(customerid,name,phone1,2);

            //
            crm.login(cstm.lxqgw,cstm.pwd);
            //再次查询，手机号应改变
            JSONObject obj = crm.customerListPC("",-1,name,"","","",1,1).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(obj.getString("customer_phone").equals(phone1),"手机号未改变");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售总监修改顾客手机号");
        }

    }

    @Test
    public void customerListsaleEditsale() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";
            customerid = creatCust(name,phone);

            //销售总监登陆
            crm.login(cstm.xszj,cstm.pwd);
            crm.customerEditsale(customerid,name,phone.substring(3),"uid_9c2b914d");

            //再次查询，手机号应不变
            JSONObject obj = crm.customerListPC("",-1,name,"","","",1,1).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(obj.getString("belongs_sale_id").equals("uid_9c2b914d"),"所属顾问改变");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售总监修改顾客所属顾问");
        }

    }

    @Test
    public void customerListzjlEditsale() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid=-1L;
        try {
            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String desc = "创建H级客户自动化------------------------------------";


            customerid = creatCust(name,phone);
            //完成接待


            //总经理登陆
            crm.login(cstm.xszj,cstm.pwd);
            crm.customerEditsale(customerid,name,phone.substring(3),"uid_8861b7fd");

            //再次查询，手机号应不变
            JSONObject obj = crm.customerListPC("",-1,name,"","","",1,1).getJSONArray("list").getJSONObject(0);
            crm.login(cstm.lxqgw,cstm.pwd);
            Preconditions.checkArgument(obj.getString("belongs_sale_id").equals("uid_8861b7fd"),"所属顾问未改变");


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

            String phone = ""+System.currentTimeMillis();
            String name = phone;
            String phone1 = phone.substring(3);
            String desc = "创建H级客户自动化------------------------------------";

            String time = dt.getHistoryDate(0);

            customerid = creatCust(name,phone);
            //完成接待


            crm.customerEditPC(customerid,name,phone1,2,2,0,time,1,1,0);

            //再次查询
            JSONObject obj = crm.customerListPC("",-1,name,phone1,"","",1,1).getJSONArray("list").getJSONObject(0);
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

            long level_id=7L;
            String phone = ""+System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name,phone);

            //查看销售状态
            String status1 = crm.userStatus().getString("user_status");
            Preconditions.checkArgument(status1.equals("BUSY"),"销售创建客户后，状态期待为BUSY，实际为"+ status1);


            //完成接待


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
            crm.login(cstm.baoshijie,cstm.pwd);
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
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("创建已存在账号");
        }
    }

    @Test
    public void  addUserREphone(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie,cstm.pwd);
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
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("使用已存在手机号创建账号");
        }
    }


    @Test(dataProvider = "ERR_PHONE",dataProviderClass = CrmScenarioUtil.class)
    public void  addUserPhoneErr1(String errphone){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie,cstm.pwd);
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
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("创建账号时手机号格式不正确");
        }
    }

    @Test
    public void  addUse200(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie,cstm.pwd);
            String userName = ""+ System.currentTimeMillis();
            String userLoginName=userName;

            String passwd=cstm.pwd;
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
            for (int i = 0 ; i < 7;i++){
                JSONArray list = crm.userPage(1,100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    if (single.getString("user_name").contains("159")){
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
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("创建200个账号");
        }
    }

    //@Test
    public void  addUse201(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie,cstm.pwd);
            String userName = ""+ System.currentTimeMillis();
            String userLoginName=userName;

            String passwd=cstm.pwd;
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
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("创建201个账号");
        }
    }

    @Test(dataProvider = "ROLE_ID",dataProviderClass = CrmScenarioUtil.class)
    public void  delUserDiffRole(String role){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie,cstm.pwd);
            String userName = ""+ System.currentTimeMillis();
            String userLoginName=userName;
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }

            String passwd=cstm.pwd;
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
                    JSONArray list = crm.userPage(i,50).getJSONArray("list");
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
            String message = crm.tryLogin(userLoginName,cstm.pwd).getString("message");
            Preconditions.checkArgument(message.equals("用户名或密码错误"),"提示语为："+message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("删除不同身份账号");
        }
    }

    @Test(dataProvider = "ROLE_ID",dataProviderClass = CrmScenarioUtil.class)
    public void  delUserDiffRoleANDlogin(String role){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie,cstm.pwd);
            String userName = ""+ System.currentTimeMillis();
            String userLoginName=userName;
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }

            String passwd=cstm.pwd;
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
                    JSONArray list = crm.userPage(i,50).getJSONArray("list");
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
            String message = crm.tryLogin(userLoginName,cstm.pwd).getString("message");
            Preconditions.checkArgument(message.equals("用户名或密码错误"),"提示语为："+message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("删除不同身份账号后，再次登陆");
        }
    }


    /**
     *
     * ====================登陆======================
     * */
    @Test
    public void  loginExist(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie,cstm.pwd);
            String userName = ""+ System.currentTimeMillis();
            String userLoginName=userName;
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }

            String passwd=cstm.pwd;
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
            int code = crm.tryLogin(userLoginName,passwd).getInteger("code");
            Preconditions.checkArgument(code==1000,"登陆失败");


            //删除账号
            crm.userDel(userid);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("使用存在的销售账号登陆");
        }
    }

    @Test
    public void  loginExistWrongPwd(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie,cstm.pwd);
            String userName = ""+ System.currentTimeMillis();
            String userLoginName=userName;
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }

            String passwd=cstm.pwd;
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
            int code = crm.tryLogin(userLoginName,passwd+"1").getInteger("code");
            Preconditions.checkArgument(code!=1000,"登陆成功");


            //删除账号
            crm.userDel(userid);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("使用存在的销售账号，错误的密码登陆");
        }
    }

    @Test
    public void  loginExistWrongAcc(){
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = crm.tryLogin("1@q啊～","1").getInteger("code");
            Preconditions.checkArgument(code!=1000,"登陆成功");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(cstm.lxqgw,cstm.pwd);
            saveData("使用不存在的销售账号登陆");
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

            crm.login(cstm.baoshijie,cstm.pwd);
            //上传图片
            int code = crm.faceOutUpload(path).getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
           crm.login(cstm.lxqgw,cstm.pwd);
            saveData("人脸排除上传识别不出人脸的图片");
        }
    }


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
     *
     * ====================状态流转======================
     * */
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

    //@Test //服务端没做校验
    public void  RecToIn(){
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
            Long cid = creatCust("aa","aa"); //898



        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
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
        crm.login(userLoginName, cstm.pwd);
        customerid = crm.userInfService().getLong("customer_id");
        //创建某级客户
        if (name.equals("")){
            String name1 = "zdh";
            String phone1 = "zdh"+(int)((Math.random()*9+1)*100000);
            JSONObject customer = crm.finishReception(customerid, 7, name1, phone1, "自动化---------创建----------H级客户");

        }
        else {
            JSONObject customer = crm.finishReception(customerid, 7, name, phone.substring(3), "自动化---------创建----------H级客户");

        }

        return customerid;
    }

    //新建试驾+审核封装
    public void creatDriver(Driver driver) throws Exception {  //1-通过，2-拒绝
        String idCard = "110226198210260078";
        Long receptionId=1L;    //接待记录id
        String gender = "男";
        String signTime = dt.getHistoryDate(0);
        Long model = 1L;
        String country = "中国";
        String city = "图们";
        String email = dt.getHistoryDate(0)+"@qq.com";
        String address = "北京市昌平区";
        String ward_name = "小小";
        String driverLicensePhoto1Url = cstm.picurl;
        String driverLicensePhoto2Url =  cstm.picurl;
        String electronicContractUrl =  cstm.picurl;

        String call="先生";
        int driverid = crm.driveradd(receptionId,driver.customerId,driver.name,idCard,driver.phone,signTime,"试乘试驾",model,country,city,email,address,ward_name,driverLicensePhoto1Url,driverLicensePhoto2Url,electronicContractUrl,driver.signDate,driver.signTime,call).getInteger("id");
        //销售总监登陆
        crm.login(cstm.xszj,cstm.pwd);
        crm.driverAudit(driverid,driver.auditStatus);
        //最后销售要再登陆一次

    }




    /**
     *
     * ====================2.1case   销售======================
     * */

    //app-销售-工作管理-我的预约
    @Test
    public void  Search_testDriver_name(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.lxqgw,cstm.pwd);
            JSONArray list = crm.appointmentlist().getJSONArray("list");

            if (list.size()>0){
                String name = list.getJSONObject(0).getString("customer_name").substring(1);
                String phone = list.getJSONObject(0).getString("customer_phone_number").substring(1);
                int size1 = crm.appointmentlist(name).getJSONArray("list").size();
                int size2 =  crm.appointmentlist(phone).getJSONArray("list").size();
                Preconditions.checkArgument(size1>=1,"根据已存在姓名模糊搜索无结果");
                Preconditions.checkArgument(size2>=1,"根据已存在手机号模糊搜索无结果");
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("app-销售-工作管理-我的预约,根据姓名/手机号模糊搜索");
        }
    }

    @Test
    public void  Search_testDriver_time(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.lxqgw,cstm.pwd);
            String yesterday = dt.getHistoryDate(-1);
            String tomorrow = dt.getHistoryDate(1);
            String dayaftertom = dt.getHistoryDate(2);
            int code1 = crm.appointmentlist(yesterday,tomorrow).getInteger("code");
            int code2 = crm.appointmentlist(tomorrow,dayaftertom).getInteger("code");
            int code3 = crm.appointmentlist(tomorrow,yesterday).getInteger("code");
            Preconditions.checkArgument(code1==1000,"开始时间<=当前时间<=结束时间状态码为"+code1);
            Preconditions.checkArgument(code2==1000,"当前时间<=开始时间<=结束时间"+code2);
            Preconditions.checkArgument(code3==1001,"结束时间<开始时间"+code3);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("app-销售-工作管理-我的预约,根据时间搜索");
        }
    }

    /**
     *
     * ====================2.1case   售后======================
     * */


    @Test
    public void  del(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie,cstm.pwd);
            JSONArray array = crm.userPage(1,100).getJSONArray("list");
            for (int i = 0 ; i< array.size();i++){
                JSONObject obj = array.getJSONObject(i);
                if (obj.getString("user_name").contains("159")){
                    crm.userDel(obj.getString("user_id"));
                }
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("删除账号");
        }
    }


}
