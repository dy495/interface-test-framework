package com.haisheng.framework.testng.bigScreen.crmOnline.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.crmOnline.commonDsOnline.CustomerInfoOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.JsonpathUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;


/**
 * @author : yu
 * @date :  2020/05/30
 */

public class CrmCase extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    CustomerInfoOnline cstm = new CustomerInfoOnline();

    Long customerid = 838L;
    String phone = "15567898766";
    String name = "刘（自动化-别动）";
    Long level_id = 15L;

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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "lxq";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-online-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "汽车-线上-赢识 lxq");

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShopOline();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        crm.login(cstm.lxqsale,cstm.pwd);


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



    @Ignore //4.0作废
    @Test
    public void addVisitCommentChkNum() {
        logger.logCaseStart(caseResult.getCaseName());

        try {

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
            int listbefore = crm.customerDetailPC(customerid).getJSONArray("return_visit").size();

            crm.customerEditVisitPC(customerid,name,phone,level_id,visit);
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

//    @Test //要改
//    public void addVisitChkNum() {
//        logger.logCaseStart(caseResult.getCaseName());
//        Long customerid=-1L;
//        try {
//
//            long level_id=7L;
//            String phone = ""+System.currentTimeMillis();
//            String name = phone;
//            customerid = creatCust(name,phone);
//            int size = crm.customerDetailPC(customerid).getJSONArray("visit").size();
//
//            //完成接待
//
//            Preconditions.checkArgument(size==1,"来访记录条数="+ size);
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("app创建顾客，来访记录数量+1");
//        }
//
//    }
//
//
//
    /**
     * ==============PC-人脸排除=================
     * */
    @Test
    public void faceOut() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(cstm.zjl,cstm.pwd);

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

            saveData("人脸排除-新增、删除验证");
        }


    }


    /**
     *
     * ====================我的客户======================
     * */


    //----------------------添加备注--------------------


    @Ignore //4.0作废
    @Test
    public void addVisitRemark200() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            //添加备注
            JSONObject visit = new JSONObject();
            String comment = ""; //备注内容
            for (int i = 0; i < 200 ; i++){
                comment = comment + "备";
            }
            crm.customerEditRemarkPC(customerid,name,phone,level_id,comment);

            //查看顾客详情，备注条数
            int code = crm.customerDetailPCNotChk(customerid).getInteger("code");
            Preconditions.checkArgument(code==1000,"添加失败");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("添加备注，备注字数=200");
        }

    }

    @Ignore //4.0作废
    @Test
    public void addVisitRemarkNum50() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long customerid = crm.customerListPC("",7,"","","","",1,1).getJSONArray("list").getJSONObject(0).getLong("customer_id");
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

    @Ignore //4.0作废
    @Test
    public void addVisitRemarkNum51() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.lxqsale,cstm.pwd);
            JSONObject obj = crm.customerEditRemarkPCNotChk(3709L,"暴徒妹妹（自动化-别删）","19999999999",15L,"1234567890987653234567876543");
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


    //----------------------查询--------------------
    @Test
    public void customerListSearchAll() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            Long id = level_id;
            //直接点击查询
            int total = crm.customerListPC("",id.intValue(),"","",0,0,1,1).getInteger("total");
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
        try {
            Long id = level_id;
            //姓名查询
            int total = crm.customerListPC("",id.intValue(),name,"",0,0,1,50).getInteger("total");

            Preconditions.checkArgument(total >= 1, "无查询结果");
            for(int i = 0 ; i < total ; i++){
                JSONObject o = crm.customerListPC("",id.intValue(),name,"",0,0,1,total).getJSONArray("list").getJSONObject(i);
                String name = o.getString("customer_name");
                Preconditions.checkArgument(name.contains(name),"查询结果与查询条件不一致");
            }

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
        try {
            crm.login(cstm.xszj,cstm.pwd);
            //获取手机号
            JSONObject obj = crm.customerListPC("",-1,"","","2020-06-01","2020-06-30",1,1).getJSONArray("list").getJSONObject(0);
            String search_phone = obj.getJSONArray("phones").getString(0);
            //查询
            JSONObject obj1 = crm.customerListPC("",-1,"",search_phone,0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_phone1 = obj1.getJSONArray("phones").getString(0);
            Preconditions.checkArgument(search_phone.equals(search_phone1),"查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的客户页面根据手机号查询");
        }

    }


    //@Test
    public void customerListSearchDate() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            String starttime = dt.getHistoryDate(0);
            String endtime = starttime;
            //查询
            JSONArray list = crm.customerListPC("",1,"","",starttime,endtime,1,50).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                String date = single.getString("service_date");
                Preconditions.checkArgument(date.equals(starttime) || date==null ,"查询结果与查询条件不一致");
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
            crm.login(cstm.lxqsale,cstm.pwd);

            //查询
            JSONArray list = crm.customerListPC("",-1,"","","","",1,50).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                Preconditions.checkArgument(single.getString("belongs_sale_name").equals(cstm.lxqsale),"展示信息不正确");
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的客户页面所属销售的顾客信息");
        }

    }




    //---------------------编辑顾客信息-------------
    @Ignore //4.0作废
    @Test
    public void customerListsaleEditPhone() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //销售登陆
            crm.customerEditPC(customerid,name,"12312341234",level_id);

            //再次查询，手机号应不变
            JSONObject obj = crm.customerListPC("",-1,name,"","","",1,1).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(obj.getString("customer_phone").equals(phone),"手机号改变");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问修改顾客手机号,应失败");
        }

    }

    @Ignore //4.0作废
    @Test
    public void customerListzjlEditPhone() {
        logger.logCaseStart(caseResult.getCaseName());

        try {

            //销售总监登陆
            crm.login(cstm.zj,cstm.pwd);
            String newphone = "13599999999";
            //添加回访记录
            JSONObject visit = new JSONObject();
            String comment = ""; //回访内容
            for (int i = 0; i < 10 ; i++){
                comment = comment + "回";
            }
            String date = dt.getHistoryDate(1);
            visit.put("comment",comment);
            visit.put("next_return_visit_date",date);
            crm.customerEditVisitPC(customerid,name,newphone,level_id,visit);


            //销售总监修改客户手机号
            //crm.customerEditRemarkPC(customerid,name,newphone,level_id,"自动化销售总监修改手机号---------------");
            //再次查询，手机号应改变
            Long id = level_id;
            JSONObject obj2 = crm.customerListPC("",id.intValue(),name,"","","",1,1).getJSONArray("list").getJSONObject(0);

            crm.customerEditVisitPC(customerid,name,phone,level_id,visit);
            Preconditions.checkArgument(obj2.getString("customer_phone").equals(newphone),"手机号未改变");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(cstm.lxqsale,cstm.pwd);
            saveData("销售总监修改顾客手机号");
        }

    }

    @Ignore //4.0作废
    @Test
    public void customerListsaleEditsale() {
        logger.logCaseStart(caseResult.getCaseName());

        try {


            crm.customerEditsale(customerid,name,phone,cstm.saleid22);

            //再次查询，顾问应不变
            JSONObject obj = crm.customerListPC("",-1,name,"","","",1,1).getJSONArray("list").getJSONObject(0);
            Preconditions.checkArgument(obj.getString("belongs_sale_id").equals(cstm.saleid11),"所属顾问改变");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售顾问修改顾客所属顾问");
        }

    }

    @Ignore //4.0作废
    @Test
    public void customerListzjlEditsale() {
        logger.logCaseStart(caseResult.getCaseName());

        try {

            //总经理登陆
            crm.login(cstm.zj,cstm.pwd);
            crm.customerEditsale(customerid,name,phone,cstm.saleid22);

            //再次查询，所属顾问应改变
            JSONObject obj = crm.customerListPC("",-1,name,phone,"","",1,1).getJSONArray("list").getJSONObject(0);

            //修改回来
            crm.customerEditsale(customerid,name,phone,cstm.saleid11);
            Preconditions.checkArgument(obj.getString("belongs_sale_id").equals(cstm.saleid22),"所属顾问未改变");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("总经理修改顾客所属顾问应成功");
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
            crm.login(cstm.zjl,cstm.pwd);
            String userName = "11";
            String userLoginName=userName;
            String phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                phone = phone + a;
            }
            //重复添加
            int code = crm.addUserNotChk(userName,userLoginName,phone,cstm.pwd11,13).getInteger("code");

            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("创建已存在账号");
        }
    }

    @Test
    public void  addUserREphone(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.zjl,cstm.pwd);

            //重复添加
            int code = crm.addUserNotChk("aaaa","aaaa",cstm.phone11,cstm.pwd11,13).getInteger("code");

            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("使用已存在手机号创建账号");
        }
    }


    @Test(dataProvider = "ERR_PHONE",dataProviderClass = CrmScenarioUtil.class)
    public void  addUserPhoneErr1(String errphone){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.zjl,cstm.pwd);
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

            saveData("创建账号时手机号格式不正确");
        }
    }


    @Test(dataProvider = "ROLE_ID",dataProviderClass = CrmScenarioUtil.class)
    public void  delUserDiffRole(String role){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.zjl,cstm.pwd);
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

            String message = crm.tryLogin(userLoginName,passwd).getString("message");
            Preconditions.checkArgument(message.equals("用户名或密码错误"),"提示语为："+message);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("删除不同身份账号");
        }
    }



    /**
     *
     * ====================登陆======================
     * */


    @Test
    public void  loginExistWrongPwd(){
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = crm.tryLogin(cstm.lxqsale,"1").getInteger("code");
            Preconditions.checkArgument(code!=1000,"登陆成功");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

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

            crm.login(cstm.zjl,cstm.pwd);
            //上传图片
            int code = crm.faceOutUpload(path).getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            //crm.login(cstm.lxqsale,cstm.pwd);
            saveData("人脸排除上传识别不出人脸的图片");
        }
    }


    /**
     *  ===============  4.0新增   ====================
     */


    /**
     * 试驾车管理
     * */

    @Test(dataProvider = "ADD_CAR",dataProviderClass = CrmScenarioUtilOnline.class)
    public void  addtestcar(String name,String car,String carid){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.xszj,cstm.pwd);
            int total = crm.driverCarList().getInteger("total");
            Long starttime = dt.getHistoryDateTimestamp(1);
            Long endtime = dt.getHistoryDateTimestamp(31);
            crm.carManagementAdd(name,4L,64L,car,carid,starttime,endtime);
            JSONObject obj = crm.driverCarList();
            int total2 = obj.getInteger("total");
            int add = total2 - total;
            String car_name = obj.getJSONArray("list").getJSONObject(0).getString("car_name");
            String plate_number = obj.getJSONArray("list").getJSONObject(0).getString("plate_number");
            String vehicle_chassis_code = obj.getJSONArray("list").getJSONObject(0).getString("vehicle_chassis_code");

            Preconditions.checkArgument(add==1,"新建试驾车，列表数据增加了"+ add);
            Preconditions.checkArgument(car_name.equals(name),"新建试驾车，新建时名称为"+ name+", 列表展示为"+ car_name);
            Preconditions.checkArgument(plate_number.equals(car),"新建试驾车，新建时车牌号为"+ car+", 列表展示为"+ plate_number);
            Preconditions.checkArgument(vehicle_chassis_code.equals(carid),"新建试驾车，新建时车架号为"+ carid+", 列表展示为"+ vehicle_chassis_code);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建试驾车-名字20位+车牌7位/8位");
        }
    }

    @Test
    public void  addtestcarExist(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.xszj,cstm.pwd);

            Long starttime = dt.getHistoryDateTimestamp(1);
            Long endtime = dt.getHistoryDateTimestamp(2);
            String car = getPlateNum();
            String carid = "ZDHZDHZDH"+(long)((Math.random()*9+1)*10000000);
            //新增
            Long test_car_id = crm.carManagementAdd(getCarName(),4L,64L,car,carid,starttime,endtime).getLong("test_car_id");
            //注销
            crm.carLogout(test_car_id);

            int code1 = crm.carManagementAddNotChk(getCarName(),4L,64L,car,"ZDHZDHZDH"+(long)((Math.random()*9+1)*10000000),starttime,endtime).getInteger("code"); //车牌重复
            int code2 = crm.carManagementAddNotChk(getCarName(),4L,64L,getPlateNum(),carid,starttime,endtime).getInteger("code"); //车架重复
            Preconditions.checkArgument(code1==1000,"车牌重复时失败");
            Preconditions.checkArgument(code2!=1000,"车架号重复时成功");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建试驾车-车牌号和已注销车牌重复可成功，车架号和已注销的重复应失败");
        }
    }

    @Test
    public void  addtestcarErr(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.xszj,cstm.pwd);

            Long starttime = dt.getHistoryDateTimestamp(1);
            Long yesterday = dt.getHistoryDateTimestamp(-1);
            Long endtime = dt.getHistoryDateTimestamp(2);
            String car = getPlateNum();
            String carid = "ZDHZDHZDH"+(long)((Math.random()*9+1)*10000000);
            String name = getCarName();
            //新增
            String name21 = "123456789012345678901";
            String car6 = "苏ZDH"+(int)((Math.random()*9+1)*10);
            String car9 = "苏ZDH"+(int)((Math.random()*9+1)*10000);
            String carno = "11111111";
            String carid16 = "ZDHZDHZDH"+(long)((Math.random()*9+1)*1000000);
            String carid18 = "ZDHZDHZDH"+(long)((Math.random()*9+1)*100000000);
            String caridnum = "0000000000"+(long)((Math.random()*9+1)*10000000);
            String carideng = "ZDHZDHZDHZDHZDHZD";

            int code1 = crm.carManagementAddNotChk(name21,1L,37L,car,carid,starttime,endtime).getInteger("code"); //名字21位
            int code2 = crm.carManagementAddNotChk(name,1L,37L,car6,carid,starttime,endtime).getInteger("code"); //车牌号6位
            int code3 = crm.carManagementAddNotChk(name,1L,37L,car9,carid,starttime,endtime).getInteger("code"); //车牌号9位
            int code4 = crm.carManagementAddNotChk(name,1L,37L,car,carid16,starttime,endtime).getInteger("code"); //车架号16位
            int code5 = crm.carManagementAddNotChk(name,1L,37L,car,carid18,starttime,endtime).getInteger("code"); //车架号18位
            int code8 = crm.carManagementAddNotChk(name,1L,37L,carno,carid,starttime,endtime).getInteger("code"); //车牌号8位纯数字
            int code9 = crm.carManagementAddNotChk(name,1L,37L,car,carid,endtime,starttime).getInteger("code"); //服役结束时间<服役开始时间 前端限制
            //int code10 = crm.carManagementAddNotChk(name,1L,37L,car,carid,yesterday,starttime).getInteger("code"); //服役开始时间<当前时间
            //有bug 先注掉
            int code6 = crm.carManagementAddNotChk(name,1L,37L,car,caridnum,starttime,endtime).getInteger("code"); //车架号纯数字
            int code7 = crm.carManagementAddNotChk(name,1L,37L,car,carideng,starttime,endtime).getInteger("code"); //车架号纯字母



            Preconditions.checkArgument(code1==1001,"名字21位成功");
            Preconditions.checkArgument(code2==1001,"车牌号6位成功");
            Preconditions.checkArgument(code3==1001,"车牌号9位成功");
            Preconditions.checkArgument(code4==1001,"车架号16位成功");
            Preconditions.checkArgument(code5==1001,"车架号18位成功");
            Preconditions.checkArgument(code8==1001,"车牌号8位纯数字成功");
            Preconditions.checkArgument(code9==1001,"服役结束时间<服役开始时间成功");
            //Preconditions.checkArgument(code10==1001,"服役开始时间<当前时间成功");
            Preconditions.checkArgument(code6==1001,"车架号17位纯数字成功");
            Preconditions.checkArgument(code7==1001,"车架号17位纯字母成功");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建试驾车-名字车牌号车架号内容校验");
        }
    }

    @Test
    public void  addtestcarLogout(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.xszj,cstm.pwd);

            Long starttime = dt.getHistoryDateTimestamp(1);
            Long endtime = dt.getHistoryDateTimestamp(2);
            String car = getPlateNum();
            String carid = "ZDHZDHZDH"+(long)((Math.random()*9+1)*10000000);
            //新增
            Long test_car_id = crm.carManagementAdd(getCarName(),4L,64L,car,carid,starttime,endtime).getLong("test_car_id");
            //注销
            crm.carLogout(test_car_id);
            boolean exit = false;
            JSONArray array = crm.driverCarList().getJSONArray("list");
            for (int i = array.size()-1; i >=0; i--){
                System.out.println(i);
                JSONObject obj = array.getJSONObject(i);
                if (obj.getString("vehicle_chassis_code").equals(carid)){
                    exit = true;
                    break;
                }
            }
            Preconditions.checkArgument(exit==true,"注销后车辆不存在");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("注销试驾车，该车辆仍在列表中");
        }
    }

    @Test
    public void  addtestcarEdit(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.xszj,cstm.pwd);

            Long starttime = dt.getHistoryDateTimestamp(1);
            Long starttimenew = dt.getHistoryDateTimestamp(2);
            String startdate = dt.getHistoryDate(2);

            Long endtime = dt.getHistoryDateTimestamp(2);
            Long endtimenew = dt.getHistoryDateTimestamp(3);
            String enddate = dt.getHistoryDate(3);

            String car = getPlateNum();
            String carnew = getPlateNum();
            String carid = "ZDHZDHZDH"+(long)((Math.random()*9+1)*10000000);
            String caridnew = "ZDHZDHGGG"+(long)((Math.random()*9+1)*10000000);
            String name = getCarName();
            String namenew = getCarName();
            //新增
            int test_car_id = crm.carManagementAdd(name,4L,64L,car,carid,starttime,endtime).getInteger("test_car_id");

            //修改
            crm.carManagementEdit(test_car_id,namenew,4L,64L,carnew,caridnew,starttimenew,endtimenew);
            String startresult = "";
            String endtimeresult = "";
            String carresult = "";
            String caridresult = "";
            String nameresult = "";

            JSONArray array = crm.driverCarList().getJSONArray("list");
            for (int i = 0; i < array.size(); i++){
                JSONObject obj = array.getJSONObject(i);
                if (obj.getLong("test_car_id") == test_car_id){
                    startresult = obj.getString("service_time_start_date");
                    endtimeresult = obj.getString("service_time_end_date");
                    nameresult = obj.getString("car_name");
                    carresult = obj.getString("plate_number");
                    caridresult = obj.getString("vehicle_chassis_code");
                }
            }

            Preconditions.checkArgument(startresult.equals(startdate),"开始服役时间未更新");
            Preconditions.checkArgument(endtimeresult.equals(enddate),"结束服役时间未更新");
            Preconditions.checkArgument(nameresult.equals(namenew),"车辆名称未更新");
            Preconditions.checkArgument(carresult.equals(carnew),"车牌号未更新");
            Preconditions.checkArgument(caridresult.equals(caridnew),"车架号未更新");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("修改试驾车，判断信息是否更新");
        }
    }



    /**
     *
     *  创建DCC线索
     * */

    @Test(dataProvider = "DCCCREAT",dataProviderClass = CrmScenarioUtilOnline.class)
    public void  addDccCust(String name, String phone,String car){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.dcc,cstm.pwd);

            int total1 = crm.dcclist(1,1).getInteger("total");
            crm.dccCreate(name,phone,car);
            JSONObject obj = crm.dcclist(1,1);
            int total2 = obj.getInteger("total");
            int change = total2- total1;
            String phoneresult = obj.getJSONArray("list").getJSONObject(0).getString("customer_phone");
            String nameresult = obj.getJSONArray("list").getJSONObject(0).getString("customer_name");
            Preconditions.checkArgument(change==1,"新建dcc线索后，列表数增加了"+change);
            Preconditions.checkArgument(phoneresult.equals(phone),"列表中手机号为"+ phoneresult+", 新建时手机号为" + phone);
            Preconditions.checkArgument(nameresult.equals(name),"列表中客户名称为"+ nameresult+", 新建时客户名称为" + name);
            Thread.sleep(1000);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建dcc线索-校验姓名/手机号/车牌号格式");
        }
    }

    @Test
    public void  addDccCustErr(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.dcc,cstm.pwd);

            String name = "自动化";
            String nameno = "      ";
            String name51 = "姓名51位姓名51位姓名51位姓名51位姓名51位姓名51位姓名51位姓名51位姓名51位123451";
            String phone = "139000"+(int)((Math.random()*9+1)*10000);
            String phone12 = "1390001"+(int)((Math.random()*9+1)*10000); //手机号12位
            String phone10 = "13900"+(int)((Math.random()*9+1)*10000); //手机号10位
            String car7 =getPlateNum();
            String car8 = getPlateNum()+"1";
            String car6 = "苏ZDH"+(int)((Math.random()*9+1)*10);
            String car9 = "苏ZDH"+(int)((Math.random()*9+1)*10000);
            String carno = "AZDH"+(int)((Math.random()*9+1)*1000);

            int code = crm.dccCreateNotChk(name51,phone,car7).getInteger("code"); //姓名51位

            int code1 = crm.dccCreateNotChk(name,phone12,car8).getInteger("code"); //手机号12位
            int code2 = crm.dccCreateNotChk(name,phone10,"").getInteger("code"); //手机号10位
            int code3 = crm.dccCreateNotChk(name,phone,car6).getInteger("code"); //车牌号6位
            int code4 = crm.dccCreateNotChk(name,phone,car9).getInteger("code"); //车牌号9位
            int code5 = crm.dccCreateNotChk(name,phone,carno).getInteger("code"); //车牌号非
            int code6 = crm.dccCreateNotChk(nameno,phone,car7).getInteger("code"); //姓名为空

            Preconditions.checkArgument(code==1001,"姓名51位期待1001，实际"+ code);
            Preconditions.checkArgument(code1==1001,"手机号12位"+phone12+"期待1001，实际"+ code1);
            Preconditions.checkArgument(code2==1001,"手机号10位"+phone10+"期待1001，实际"+ code2);
            Preconditions.checkArgument(code3==1001,"车牌号6位"+car6+"期待1001，实际"+ code3);
            Preconditions.checkArgument(code4==1001,"车牌号9位"+car9+"期待1001，实际"+ code4);
            Preconditions.checkArgument(code5==1001,"非车牌号格式"+carno+"期待1001，实际"+ code5);
            Preconditions.checkArgument(code6==1001,"姓名为空格时期待1001，实际"+ code6);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建dcc线索-姓名/手机号/车牌号格式不正确");
        }
    }

    @Test
    public void  addDccCustRe(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.dcc,cstm.pwd);

            String name = getCarName();
            String phone = "139000"+(int)((Math.random()*9+1)*10000);
            String phone1 = "139001"+(int)((Math.random()*9+1)*10000);
            String car7 = getPlateNum();
            String car8 = getPlateNum()+"1";


            crm.dccCreate(name,phone,car7);
            int code = crm.dccCreateNotChk(name,phone,car8).getInteger("code"); //已存在手机号
            Preconditions.checkArgument(code==1001,"使用已存在手机号期待1001，实际"+ code);
            int code1 = crm.dccCreateNotChk(name,phone1,car7).getInteger("code"); //已存在客户的车牌号
            Preconditions.checkArgument(code1==1001,"使用已存在客户的车牌号期待1001，实际"+ code1);

            Long starttime = dt.getHistoryDateTimestamp(1);
            Long endtime = dt.getHistoryDateTimestamp(2);
            String car = getPlateNum();
            String carid = "ZDHZDHZDH"+(long)((Math.random()*9+1)*10000000);
            //新增
            Long test_car_id = crm.carManagementAdd("ZDH"+(int)((Math.random()*9+1)*100),4L,64L,car,carid,starttime,endtime).getLong("test_car_id");


            int code2 = crm.dccCreateNotChk(name,phone1,car).getInteger("code"); //试驾车列表未注销的车牌号
            Preconditions.checkArgument(code2==1001,"使用试驾车列表未注销的车牌号"+car+"期待1001，实际"+ code2);

            //注销
            crm.carLogout(test_car_id);
            int code3 = crm.dccCreateNotChk(name,phone1,car).getInteger("code"); //试驾车列表已注销的车牌号
            Preconditions.checkArgument(code3==1000,"使用试驾车列表已注销的车牌号"+car+"期待1000，实际"+ code3);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建dcc线索-使用存在手机号/车牌号期待失败");
        }
    }




    /**
     *
     * 收件箱
     * */

    @Test(dataProvider = "EMAIL",dataProviderClass = CrmScenarioUtilOnline.class)
    public void  emailConfig(String email){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.xszj,cstm.pwd);
            crm.mailConfig(email);
            String result = crm.mailDetail().getString("email");
            Preconditions.checkArgument(result.equals(email),"配置后，邮箱未改变");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("配置收件箱，邮箱格式，长度1-100");
        }
    }

    @Test(dataProvider = "EMAILERR",dataProviderClass = CrmScenarioUtilOnline.class)
    public void  emailConfigErr(String email){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.xszj,cstm.pwd);
            int code = crm.mailConfigNotChk(email).getInteger("code");
            Preconditions.checkArgument(code==1001,"邮箱为"+ email + "时修改成功");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("配置收件箱，邮箱格式/长度不正确");
        }
    }


    public String getPlateNum(){
        String qu = "CEFGHJKLMNPQY";
        int a = (int)(Math.random()*10);
        String plateNum = "京";
        plateNum = plateNum + qu.substring(a,a+1);
        for (int i = 0; i < 5;i++){
            String b = Integer.toString((int)(Math.random()*10));
            plateNum = plateNum + b;
        }
        System.out.println(plateNum);
        return plateNum;
    }

    public String getCarName(){

        String name = "Name"+ Integer.toString((int)(Math.random()*100000000));
        return name;
    }


}
