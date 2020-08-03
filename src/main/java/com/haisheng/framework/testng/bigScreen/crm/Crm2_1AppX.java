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
    public String adminname="xx";    //pc登录密码，最好销售总监或总经理权限
    public String adminpassword="e10adc3949ba59abbe56e057f20f883e";
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
        crm.login(adminname, adminpassword);


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
     * @description :交车按名字/电话查询，结果校验
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
                String PhoneSelect=listPhone.getJSONObject(i).getString("customer_name");
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
     * @description :交车查询
     * @date :2020/8/3 12:48
     **/
    @Test(dataProvider = "SELECT_DATE",dataProviderClass = CrmScenarioUtil.class)
    public void jiaocheSelectTime(String select_date){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray list=crm.deliverSelect(1,10,select_date,select_date).getJSONArray("list");
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
                String PhoneSelect=listPhone.getJSONObject(i).getString("customer_name");
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
     * @description :试驾查询
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
     * @description :试驾查询
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
     * @description :客户按名字/电话查询，结果校验
     * @date :2020/8/3 12:48
     **/
    @Test
    public void customerSelect(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.driverSelect(1,10);
            String customer_name=data.getJSONArray("list").getJSONObject(0).getString("customer_name");
            String customer_phone=data.getJSONArray("list").getJSONObject(0).getString("customer_phone");
            JSONArray list=crm.driverSelect(1,10,customer_name).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String nameSelect=list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument(nameSelect.equals(customer_name),"客户按客户名称查询，结果错误");
            }
            JSONArray listPhone=crm.driverSelect(1,10,customer_phone).getJSONArray("list");
            for(int i=0;i<listPhone.size();i++){
                String PhoneSelect=listPhone.getJSONObject(i).getString("customer_name");
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
     * @description :创建线索
     * @date :2020/8/3 14:58
     **/
    @Test
    public void createLine(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("创建线索");
        }
    }





    /**
     * @description :预约记录查询验证预约记录查询验证，今日数=列表去重数,数字统计按创建日期，页面无创建日期，故此case不通 TODO:可用于app试驾、交车
     * @date :2020/7/31 13:55
     **/
    @Test
    public void appointmentRecodeApp(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject data=crm.appointmentpage(dt.getHistoryDate(0),dt.getHistoryDate(0),1,100);
            int today_number=data.getInteger("today_number");
            JSONArray list=data.getJSONArray("list");
            List<String> numList = new ArrayList<String>();
            if(list==null||list.size()==0){
                logger.info("今日无交车记录");
                return;
            }
            for(int i=0;i<list.size();i++){
                String phone=list.getJSONObject(i).getString("customer_phone_number");
                String service_status_name=list.getJSONObject(i).getString("service_status_name");
                if(!service_status_name.equals("已取消")){
                    numList.add(phone);
                }
            }
            Set<String> numSet = new HashSet<String>();
            numSet.addAll(numList);
            int similar=numSet.size();
            Preconditions.checkArgument(similar==today_number,"今日预约数！=今日列表电话号码去重数");

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("预约记录查询验证预约记录查询验证，今日数=列表去重数");
        }
    }





}
