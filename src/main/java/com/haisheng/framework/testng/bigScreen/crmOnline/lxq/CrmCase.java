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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;


/**
 * @author : yu
 * @date :  2020/05/30
 */

public class CrmCase extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    CustomerInfoOnline cstm = new CustomerInfoOnline();

    Long customerid = cstm.lxqid;
    long level_id=cstm.lxqlevel;
    String phone = cstm.lxqphone;
    String name = cstm.lxqname;

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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "CRM 线上 lxq");

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.ONLINE_OPEN_MANAGEMENT_PLATFORM_GRP;
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

    //@Test
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


            //获取手机号
            JSONObject obj = crm.customerListPC("",7,"","",0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_phone = obj.getString("customer_phone");
            System.out.println(search_phone);
            //查询
            JSONObject obj1 = crm.customerListPC("",-1,"",search_phone,0,0,1,1).getJSONArray("list").getJSONObject(0);
            String search_phone1 = obj1.getString("customer_phone");
            System.out.println(search_phone1);
            Preconditions.checkArgument(search_phone.equals(search_phone1),"查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的客户页面根据手机号查询");
        }

    }


    @Test
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

            int code = crm.tryLogin(cstm.lxqsale,cstm.pwd123456).getInteger("code");
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





}
