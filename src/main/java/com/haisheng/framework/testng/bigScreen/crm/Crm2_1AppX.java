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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description :2.1交车、试驾、客户relate----xia
 * @date :2020/8/3 12:47
 **/


public class Crm2_1AppX extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    public String adminname="xx";    //服务总监账号
    public String adminpassword="e10adc3949ba59abbe56e057f20f883e";

    public String adminnameapp="销售顾问xia";      //销售账号
    public String filePath="src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic";
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
        commonConfig.checklistQaOwner = "xmf";


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
        crm.login(adminnameapp, adminpassword);


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

    //读取文件内容
    public String texFile(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str = in.readLine();
        return str;
    }

    //前台点击创建接待按钮创建顾客
    public Long creatCust(String name, String phone) throws Exception {
        //前台登陆
        crm.login("qt", adminpassword);
        Long customerid = -1L;
        //获取当前空闲第一位销售id

        String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");

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
        crm.login(userLoginName, adminpassword);
        customerid = crm.userInfService().getLong("customer_id");
        //创建某级客户
//        JSONObject customer = crm.finishReception(customerid, 7, name, phone, "自动化---------创建----------H级客户");
        JSONObject customer = crm.customerEdit_onlyNec(customerid, 7, name, phone, "自动化---------创建----------H级客户");

        return customerid;
    }

    //新建试驾+审核封装 TODO：
    public void creatDriver(Long customer_id,String name,String phone,String sign_date,String sign_time, int audit_status) throws Exception {  //1-通过，2-拒绝
        String idCard = "110226198210260078";
        String gender = "男";
        String signTime = dt.getHistoryDate(0);
        Long model = 1L;
        String country = "中国";
        String city = "图们";
        String email = dt.getHistoryDate(0)+"@qq.com";
        String address = "北京市昌平区";
        String ward_name = "小小";
        String driverLicensePhoto1Url = texFile(filePath);
        String driverLicensePhoto2Url = texFile(filePath);
        String electronicContractUrl = texFile(filePath);

        String call="MEN";
        int driverid = crm.driveradd(customer_id,name,idCard,gender,phone,signTime,"试乘试驾",model,country,city,email,address,ward_name,driverLicensePhoto1Url,driverLicensePhoto2Url,electronicContractUrl,sign_date,sign_time,call).getInteger("id");
        //销售总监登陆
        crm.login("xszj",adminpassword);
        crm.driverAudit(driverid,audit_status);
        //最后销售要再登陆一次

    }

    //订车+交车封装  copy lxq debug ok
    public void creatDeliver(Long customer_id,String customer_name,String deliver_car_time, Boolean accept_show) throws Exception {
        //订车
        crm.orderCar(customer_id);
        //创建交车
        String model = "911";
        String path = texFile(filePath);
        crm.deliverAdd(customer_id,customer_name,deliver_car_time,model,path,accept_show,path);
    }


    //pc新建活动方法，返回文章id和文章id
    public Long[] createAArcile_id(String valid_start, String simulation_num){
        Long article_id=0L;
        Long [] aid=new Long[2];
        try {
            crm.login(adminname,adminpassword);
            String[] customer_types = {"PRE_SALES", "AFTER_SALES"};
            int[] customer_level = {};           //TODO:客户等级
            String[] customer_property = {};
            String[] positions = {"CAR_ACTIVITY"}; //投放位置车型推荐 单选
            // String [] positions={"MODEL_RECOMMENDATION","PURCHASE_GUIDE","BRAND_CULTURE","CAR_ACTIVITY"};
//            String valid_start = dt.getHistoryDate(0);
            String valid_end = dt.getHistoryDate(4);
            int[] car_types = {1};
            String article_title = "app任务报名品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
            String article_bg_pic = texFile(filePath);  //base 64
            String article_content = "品牌上新，优惠多多，限时4天,活动内容";
            String article_remarks = "品牌上新，优惠多多，限时4天,备注";

            boolean is_online_activity = true;  //是否线上报名活动
//            String reception_name = manage(13)[0];  //接待人员名
//            String reception_phone = manage(13)[1]; //接待人员电话
            String reception_name = "xx";  //接待人员名
            String reception_phone = "15037286013"; //接待人员电话
            String customer_max = "50";                    //人数上限

            String activity_start = dt.getHistoryDate(0);
            String activity_end = dt.getHistoryDate(4);
            Integer role_id = 13;
            Boolean is_create_poster = true;//是否生成海报
            Integer task_customer_num=5;
            //新建文章并返回文章/活动id
            article_id = crm.createArticle(positions, valid_start, valid_end, customer_types, car_types, customer_level, customer_property, article_title, article_bg_pic, article_content, article_remarks, is_online_activity, reception_name, reception_phone, customer_max, simulation_num, activity_start, activity_end, role_id, Integer.toString(task_customer_num), is_create_poster).getLong("id");
            Long activity_id=crm.appartilceDetail(article_id).getLong("activity_id");
            aid[0]=article_id;  //文章id
            aid[1]=activity_id;  //活动id
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            logger.info("create activity");
        }
        return aid;
    }
    /**
     * @description :交车按名字/电话查询，结果校验  ok
     * @date :2020/8/3 12:48
     **/
    @Test
    public void jiaocheSelect(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.deliverSelect(1,10);
            String customer_name=data.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String customer_phone_number=data.getJSONArray("list").getJSONObject(0).getString("customer_phone_number");
            JSONArray list=crm.deliverSelect(1,10,customer_name).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String nameSelect=list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument(nameSelect.equals(customer_name),"交车按客户名称查询，结果错误");
            }
            JSONArray listPhone=crm.deliverSelect(1,10,customer_phone_number).getJSONArray("list");
            for(int i=0;i<listPhone.size();i++){
                String PhoneSelect=listPhone.getJSONObject(i).getString("customer_phone_number");
                Preconditions.checkArgument(PhoneSelect.equals(customer_phone_number),"交车按客户电话查询，结果错误");
            }

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("交车按名字/电话查询，结果校验");
        }
    }

    /**
     * @description :交车按时间查询 ok
     * @date :2020/8/3 12:48
     **/
    @Test(dataProvider = "SELECT_DATE",dataProviderClass = CrmScenarioUtil.class)
    public void jiaocheSelectTime(String select_date){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list=crm.deliverSelect(10,1,select_date,select_date).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String timeSelect=list.getJSONObject(i).getString("deliver_car_time");
                Preconditions.checkArgument(timeSelect.equals(select_date),"交车按交车时间{}查询，结果{}错误",select_date,timeSelect);
            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("交车按交车日期查询，结果校验");
        }
    }
    /**
     * @description :交车查询
     * @date :2020/8/3 12:48
     **/
    @Test()
    public void jiaocheSelectTimeAndname(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.deliverSelect(1,10);
            String customer_name=data.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String select_date=dt.getHistoryDate(0);
            JSONArray list=crm.deliverSelect(1,10,customer_name,select_date,select_date).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String timeSelect=list.getJSONObject(i).getString("deliver_car_time");
                String nameSelect=list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument((timeSelect.equals(select_date))&&(customer_name.equals(nameSelect)),"交车按交车时间{}查询，结果{}错误",select_date,timeSelect);
            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("交车组合查询，结果校验");
        }
    }

    /**
     * @description :试驾按名字/电话查询，结果校验
     * @date :2020/8/3 12:48
     **/
    @Test
    public void driverSelect(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.driverSelect(1,10);
            String customer_name=data.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String customer_phone_number=data.getJSONArray("list").getJSONObject(0).getString("customer_phone_number");
            JSONArray list=crm.driverSelect(1,10,customer_name).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String nameSelect=list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument(nameSelect.equals(customer_name),"交车按客户名称查询，结果错误");
            }
            JSONArray listPhone=crm.driverSelect(1,10,customer_phone_number).getJSONArray("list");
            for(int i=0;i<listPhone.size();i++){
                String PhoneSelect=listPhone.getJSONObject(i).getString("customer_phone_number");
                Preconditions.checkArgument(PhoneSelect.equals(customer_phone_number),"交车按客户电话查询，结果错误");
            }

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("试驾按名字/电话查询，结果校验");
        }
    }

    /**
     * @description :试驾查询 ok
     * @date :2020/8/3 12:48
     **/
    @Test(dataProvider = "SELECT_DATE",dataProviderClass = CrmScenarioUtil.class)
    public void driverSelectTime(String select_date){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list=crm.driverSelect(1,10,select_date,select_date).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String timeSelect=list.getJSONObject(i).getString("sign_time");
                Preconditions.checkArgument(timeSelect.equals(select_date),"交车按交车时间{}查询，结果{}错误",select_date,timeSelect);
            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("试驾按试驾日期查询，结果校验");
        }
    }
    /**
     * @description :试驾查询 ok
     * @date :2020/8/3 12:48
     **/
    @Test()
    public void driverSelectTimeAndname(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.driverSelect(1,10);
            String customer_name=data.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String select_date=dt.getHistoryDate(0);
            JSONArray list=crm.driverSelect(1,10,customer_name,select_date,select_date).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String timeSelect=list.getJSONObject(i).getString("sign_time");
                String nameSelect=list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument((timeSelect.equals(select_date))&&(customer_name.equals(nameSelect)),"交车按交车时间{}查询，结果{}错误",select_date,timeSelect);
            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("试驾组合查询，结果校验");
        }
    }

    /**
     * @description :客户按名字/电话查询，结果校验 ok
     * @date :2020/8/3 12:48
     **/
    @Test
    public void customerSelect(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.customerSelect(1,10);
            String customer_name=data.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String customer_phone=data.getJSONArray("list").getJSONObject(0).getString("customer_phone");
            JSONArray list=crm.customerSelect(1,10,customer_name).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String nameSelect=list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument(nameSelect.equals(customer_name),"客户按客户名称查询，结果错误");
            }
            JSONArray listPhone=crm.customerSelect(1,10,customer_phone).getJSONArray("list");
            for(int i=0;i<listPhone.size();i++){
                String PhoneSelect=listPhone.getJSONObject(i).getString("customer_phone");
                Preconditions.checkArgument(PhoneSelect.equals(customer_phone),"客户按客户电话查询，结果错误");
            }

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("客户按名字/电话查询，结果校验");
        }
    }

    /**
     * @description :客户查询，列表展示无创建时间字段，可点击编辑查看客户详细信息中，创建时间
     * @date :2020/8/3 12:48
     **/
//    @Test(dataProvider = "SELECT_DATE",dataProviderClass = CrmScenarioUtil.class)
    public void customerSelectTime(String select_date){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list=crm.customerSelect(1,10,select_date,select_date).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String timeSelect=list.getJSONObject(i).getString("sign_time");
                Preconditions.checkArgument(timeSelect.equals(select_date),"客户按交车时间{}查询，结果{}错误",select_date,timeSelect);
            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("客户按试驾日期查询，结果校验");
        }
    }
    /**
     * @description :客户查询，列表展示无创建时间字段，可点击编辑查看客户详细信息中，创建时间
     * @date :2020/8/3 12:48
     **/
    //@Test()
    public void customerSelectTimeAndname(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.driverSelect(1,10);
            String customer_name=data.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String select_date=dt.getHistoryDate(0);
            JSONArray list=crm.driverSelect(1,10,customer_name,select_date,select_date).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String timeSelect=list.getJSONObject(i).getString("sign_time");
                String nameSelect=list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument((timeSelect.equals(select_date))&&(customer_name.equals(nameSelect)),"客户按交车时间{}查询，结果{}错误",select_date,timeSelect);
            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("客户组合查询，结果校验");
        }
    }

    /**
     * @description :创建线索 ok
     * @date :2020/8/3 14:58
     **/
    @Test
    public void createLine(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
//            String customer_phone="15037286012";
            String customer_phone = "1";
            for (int i = 0; i < 10;i++){
                String a = Integer.toString((int)(Math.random()*10));
                customer_phone = customer_phone + a;
            }
            String remark="自动化创建线索自动化创建线索自动化创建线索";
            Long code=crm.createLine("诸葛自动",1,customer_phone,1,remark).getLong("code");
            Preconditions.checkArgument(code==1000,"使用不存在的手机号创建线索应该成功");

            Long code2=crm.createLine("诸葛自动",1,customer_phone,1,remark).getLong("code");
            Preconditions.checkArgument(code2==1001,"使用存在的手机号创建线索不应该成功");

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("创建线索");
        }
    }


    /**
     * @description :新建交车授权，是小程序最新车主风采 ok
     * @date :2020/8/3 16:46
     **/
    @Test
    public void carOwerNewst(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //app 销售登录，交车授权信息
            Long customer_id=13979L;  //TODO:
            String customer_name="auto";
//            String phone = "1";
//            for (int i = 0; i < 10; i++) {
//                String a = Integer.toString((int) (Math.random() * 10));
//                phone = phone + a;
//            }
//            Long customer_id=creatCust(customer_name,phone);

            creatDeliver(customer_id,customer_name,dt.getHistoryDate(0),true);
            //完成接待
//            crm.finishReception(customer_id, 7, customer_name, phone, "自动化---------创建----------H级客户");
            //小程序登录，查看最新交车
            crm.appletLogin("123456");
            JSONObject data=crm.carOwnernew();
            String customer_nameN=data.getString("customer_name");
            String car_model=data.getString("car_model");
            String work=data.getString("work");
            String hobby=data.getString("hobby");
            crm.login(adminnameapp,adminpassword);
            Preconditions.checkArgument(car_model.equals("911"),"最新交车信息校验失败");
            Preconditions.checkArgument(work.equals("金融 "),"最新交车信息校验失败");
            Preconditions.checkArgument(hobby.equals("宠物 "),"最新交车信息校验失败");


        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("新建交车授权，是小程序最新车主风采");
        }
    }
    /**
     * @description :新建交车授权，applet车主风采列表+1 ok
     * @date :2020/8/3 18:25
     **/
    @Test
    public void carOwerListUp(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //applet登录，记录原始列表数
            crm.appletLogin("123456");
            JSONArray list=crm.carOwner().getJSONArray("list");
            int total;
            if(list==null||list.size()==0){
                total=0;
            }else{
                total=list.size();
            }
            //app 销售登录，交车授权信息
//            crm.login(adminnameapp,adminpassword);
//            Long customer_id=13979L;            //TODO:
            String customer_name="autolist";
            String phone = "1";
            for (int i = 0; i < 10; i++) {
                String a = Integer.toString((int) (Math.random() * 10));
                phone = phone + a;
            }
            Long customer_id=creatCust(customer_name,phone);
            creatDeliver(customer_id,customer_name,dt.getHistoryDate(0),true);
            //小程序登录，查看交车
            crm.appletLogin("123456");
            JSONArray listA=crm.carOwner().getJSONArray("list");
            int totalA;
            if(listA==null||listA.size()==0){
                totalA=0;
            }else{
                totalA=listA.size();
            }
            crm.login(adminnameapp,adminpassword);
            Preconditions.checkArgument(totalA-total==1,"建交车授权，applet车主风采列表没+1");
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("建交车授权，applet车主风采列表+1");
        }
    }

   /**
    * @description :app活动报名,任务人数于创建时相同，增加报名，任务人数+1，  删除报名-1 0k
    * @date :2020/8/3 19:13
    **/
   @Test
   public void taskactivity(){
       logger.logCaseStart(caseResult.getCaseName());
       try{
           //创建活动，获取活动id
           Long [] aid=createAArcile_id(dt.getHistoryDate(0),"8");
           Long activity_id=aid[1];
           Long id=aid[0];
           //app销售登录报名
           crm.login(adminnameapp,adminpassword);
           JSONObject response = crm.activityTaskPageX();
           JSONObject json = response.getJSONObject("data").getJSONArray("list").getJSONObject(0);   //新建的活动在pad端的位置需要确认 TODO:
           int activityTaskId = json.getInteger("activity_task_id");
           int task_customer_num = json.getInteger("task_customer_num");
           JSONArray list=json.getJSONArray("customer_list");
           int total;
           if(list==null||list.size()==0){
               total=0;
               }else {
               total = list.size();
               }
           String phone = "1";
            for (int i = 0; i < 10; i++) {
                String a = Integer.toString((int) (Math.random() * 10));
                phone = phone + a;
            }
            crm.registeredCustomer((long) activityTaskId, "夏", phone);

            JSONObject responseA = crm.activityTaskPageX();
            JSONObject jsonA = responseA.getJSONObject("data").getJSONArray("list").getJSONObject(0);   //新建的活动在pad端的位置需要确认 TODO:
           int totalA = jsonA.getJSONArray("customer_list").size();
           //获取报名字段，校验
           Preconditions.checkArgument(task_customer_num == 5, "app报名活动，任务人数与活动创建时不一致");
           Preconditions.checkArgument(totalA-total == 1, "app报名活动，报名列表+1");

           crm.articleStatusChange(id);
           crm.articleDelete(id);
           crm.login(adminnameapp, adminpassword);

       }catch (AssertionError e){
           appendFailreason(e.toString());
       }catch (Exception e){
           appendFailreason(e.toString());
       }finally {
           saveData("app活动报名,任务人数于创建时相同，增加报名，任务人数+1");
       }
   }


    /**
     * @description :交车 今日数=列表电话去重数   ok
     * @date :2020/7/31 13:55
     **/
    @Test
    public void jiaocheRecodeApp(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.deliverSelect(1,100,dt.getHistoryDate(0),dt.getHistoryDate(0));
            JSONObject dataTotal=crm.jiaocheTotal();
            int today_number=dataTotal.getInteger("today_deliver_car_total");
            JSONArray list=data.getJSONArray("list");
            List<String> numList = new ArrayList<String>();
            if(list==null||list.size()==0){
                logger.info("今日无交车记录");
                return;
            }
            for(int i=0;i<list.size();i++){
                String phone=list.getJSONObject(i).getString("customer_phone_number");
                numList.add(phone);
            }
            Set<String> numSet = new HashSet<String>();
            numSet.addAll(numList);
            int similar=numSet.size();
            Preconditions.checkArgument(similar==today_number,"今日交车数！=今日列表电话号码去重数");

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("交车 今日数=列表电话去重数");
        }
    }

    /**
     * @description :试驾 今日数=列表电话去重数  TODO
     * @date :2020/7/31 13:55
     **/
    @Test
    public void shijiaRecodeApp(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.driverSelect(1,100,dt.getHistoryDate(0),dt.getHistoryDate(0));
            JSONObject dataTotal=crm.driverTotal();
            int today_number=dataTotal.getInteger("today_test_drive_total");
            JSONArray list=data.getJSONArray("list");
            List<String> numList = new ArrayList<String>();
            if(list==null||list.size()==0){
                logger.info("今日无试驾记录");
                return;
            }
            for(int i=0;i<list.size();i++){
                String phone=list.getJSONObject(i).getString("customer_phone_number");
                String audit_status_name=list.getJSONObject(i).getString("audit_status_name");
                //TODO:审核通过装态待确认
                if(audit_status_name.equals("已通过")) {
                    numList.add(phone);
                }
            }
            Set<String> numSet = new HashSet<String>();
            numSet.addAll(numList);
            int similar=numSet.size();
            Preconditions.checkArgument(similar==today_number,"试驾 今日数=列表电话去重数");

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("试驾 今日数=列表电话去重数");
        }
    }

      /**
       * @description :试驾
       * @date :2020/8/10 16:45
       **/
      @Test
    public void testderver(){
          logger.logCaseStart(caseResult.getCaseName());
          try{
              Long customer_id=13979L;  //TODO:
              String customer_name="auto";
            String phone = "1";
//            for (int i = 0; i < 10; i++) {
//                String a = Integer.toString((int) (Math.random() * 10));
//                phone = phone + a;
//            }
//            Long customer_id=creatCust(customer_name,phone);
              String time=dt.getHHmm(0);
              creatDriver(customer_id,customer_name,phone,dt.getHistoryDate(0),dt.getHHmm(0),1);


          }catch (AssertionError e){
              appendFailreason(e.toString());
          }catch (Exception e){
             appendFailreason(e.toString());
          }finally {
              saveData("创建试驾");
          }
      }






}
