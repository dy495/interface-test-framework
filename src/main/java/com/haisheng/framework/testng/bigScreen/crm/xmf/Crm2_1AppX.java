package com.haisheng.framework.testng.bigScreen.crm.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.experiment.enumerator.EnumAppletCode;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PackFunction;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicParm;
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
    PublicParm pp=new PublicParm();
    PackFunction pf=new PackFunction();

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
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
//        commonConfig.dingHook = DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        crm.login(pp.xiaoshouGuwen, pp.adminpassword);


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
     * @description :试驾评价 && 完成接待接待次数+1 ；评价完成pc评价列表+1
     * @date :2020/8/2 10:29
     **/
    @Test(priority = 12)
    public void driverEvaluate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //接待前Guwen，接待次数 13 销售顾问，16保养 ，15维修
            int num=pf.jiedaiTimes(13,pp.xiaoshouGuwen);
            //pc评价页总数
            JSONArray evaluateList = crm.evaluateList(1, 100, "", "", "").getJSONArray("list");
            int total = evaluateList.size();
            //预约接待完成
            Long appointmentId=pf.driverEva();

            int num2 = pf.jiedaiTimes(13,pp.xiaoshouGuwen);
            Preconditions.checkArgument((num2 - num) == 1, "接待完成，接待次数没+1");
            //小程序评价
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
//            SERVICE_QUALITY|PROCESS|PROFESSIONAL|EXPERIENCE
            int score = 4;
            JSONObject ll = new JSONObject();
            ll.put("score", score);
            ll.put("type_comment", "销售接待服务质量");
            ll.put("type", "SERVICE_QUALITY");

            JSONObject ll2 = new JSONObject();
            ll2.put("score", score);
            ll2.put("type_comment", "销售接待服务流程");
            ll2.put("type", "PROCESS");

            JSONObject ll3 = new JSONObject();
            ll3.put("score", score);
            ll3.put("type_comment", "试乘试驾体验评价");
            ll3.put("type", "EXPERIENCE");

            JSONArray array1 = new JSONArray();
            array1.add(0, ll);
            array1.add(1, ll2);
            array1.add(2, ll3);

            crm.appointmentEvaluate(appointmentId, "保养满意", array1);  //评价
            crm.login(pp.zongjingli,pp.adminpassword);
            JSONArray evaluateListB = crm.evaluateList(1, 10, "", "", "").getJSONArray("list");
            int totalB = evaluateListB.size();
            Preconditions.checkArgument((totalB - total) == 1, "评价后，pc评价列表没+1");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("试驾评价 && 完成接待接待次数+1 ；评价完成pc评价列表+1");
        }
    }

    /**
     * @description :直接接待老客，为小程序接待评价提供消息
     * @date :2020/8/22 14:05
     **/
    @Test(priority = 12)
    public void acceptEvaluate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //pc评价页总数
            String type="MSG";
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            int total=crm.messageList(20,type).getInteger("total");

            //预约接待完成
            JSONObject json=pf.creatCustOld(pp.customer_phone_number);
            Long customerId=json.getLong("customerId");
            crm.finishReception(customerId, 7, pp.customer_name, pp.customer_phone_number,pp.remark);  //完成接待
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONArray messagePage=crm.messageList(10,type).getJSONArray("list");
            Long id=messagePage.getJSONObject(0).getLong("id");

            int totalB=crm.messageList(100,type).getInteger("total");
            //小程序评价
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
//            SERVICE_QUALITY|PROCESS|PROFESSIONAL|EXPERIENCE
            int score = 4;
            JSONObject ll = new JSONObject();
            ll.put("score", score);
            ll.put("type_comment", "销售接待服务质量");
            ll.put("type", "SERVICE_QUALITY");

            JSONObject ll2 = new JSONObject();
            ll2.put("score", score);
            ll2.put("type_comment", "销售接待服务流程");
            ll2.put("type", "PROCESS");

            JSONObject ll3 = new JSONObject();
            ll3.put("score", score);
            ll3.put("type_comment", "销售接待专业评价");
            ll3.put("type", "PROFESSIONAL");
//            ll3.put("type", "EXPERIENCE");
//            ll3.put("type_comment", "试乘试驾体验评价");



            JSONArray array1 = new JSONArray();
            array1.add(0, ll);
            array1.add(1, ll2);
            array1.add(2, ll3);

            crm.messageEvaluate(id, "我的消息-保养满意", array1);  //评价

            Preconditions.checkArgument((totalB - total) == 1, "接待小程序客户，发送评价消息，我的消息数量没+1");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("接待小程序客户，发送评价消息，我的消息数量+1");
        }
    }


    /**
     * @description :试驾   ok
     * @date :2020/8/10 16:45
     **/
    @Test(priority = 12)
    public void testderver(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //销售总监今日试驾总数
            crm.login(pp.xiaoshouZongjian,pp.adminpassword);
            JSONObject dataTotal=crm.driverTotal();
            int today_number=dataTotal.getInteger("today_test_drive_total");
            int totalNum=dataTotal.getInteger("test_drive_total");

            JSONObject jsonObject=pf.creatCust();  //创建新客
            String customer_name=jsonObject.getString("name");
            String phone=jsonObject.getString("phone");
            Long receptionId=jsonObject.getLong("reception_id");
            Long customerId=jsonObject.getLong("customerId");
            String userLoginName=jsonObject.getString("userLoginName");
            pf.creatDriver(receptionId,customerId,customer_name,phone,1);  //新客试驾

            JSONObject dataTotal2=crm.driverTotal();
            int today_number2=dataTotal2.getInteger("today_test_drive_total");
            int totalNum2=dataTotal2.getInteger("test_drive_total");
            crm.login(userLoginName,pp.adminpassword);
            crm.finishReception(customerId, 7, customer_name, phone,pp.remark);  //完成接待
            Preconditions.checkArgument(today_number2-today_number==1,"新建试驾，今日试驾+1，试驾后：{}，试驾前：{}",today_number2,today_number);
            Preconditions.checkArgument(totalNum2-totalNum==1,"新建试驾，总计试驾+1，试驾后：{}，试驾前：{}",totalNum2,totalNum);


        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("创建新客试驾,今日试驾次数+1,总计+1");
        }
    }
    /**
     * @description :创建新客交车,今日交车次数+1,总计+1  ok
     * @date :2020/8/10 16:45
     **/
    @Test(priority = 12)
    public void testdeliver(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //销售总监今日试驾总数
            crm.login(pp.xiaoshouZongjian,pp.adminpassword);

            JSONObject dataTotal=crm.jiaocheTotal();
            int today_number=dataTotal.getInteger("today_deliver_car_total");
            int totalNum=dataTotal.getInteger("deliver_car_total");

            JSONObject object=pf.creatCust();
            Long customer_id=object.getLong("customerId");
            Long reception_id=object.getLong("reception_id");
            String userLoginName=object.getString("userLoginName");
            String customer_name=object.getString("name");
            String phone=object.getString("phone");
            pf.creatDeliver(reception_id,customer_id,customer_name,dt.getHistoryDate(0),true);

            crm.login(pp.xiaoshouZongjian,pp.adminpassword);
            JSONObject dataTotal2=crm.jiaocheTotal();
            int today_number2=dataTotal2.getInteger("today_deliver_car_total");
            int totalNum2=dataTotal2.getInteger("deliver_car_total");

            crm.login(userLoginName,pp.adminpassword);
            crm.finishReception(customer_id, 4, customer_name, phone, pp.remark);   //TODO:完成接待,交车完成用户等级 4
            Preconditions.checkArgument(today_number2-today_number==1,"新建交车，今日交车+1，交车后：{}，交车前：{}",today_number2,today_number);
            Preconditions.checkArgument(totalNum2-totalNum==1,"新建交车，总计交车+1，交车后：{}，交车前：{}",totalNum2,totalNum);


        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("创建新客交车,今日交车次数+1,总计+1");
        }
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
        }catch (AssertionError | Exception e){
            appendFailreason(e.toString());
        } finally {
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
                String timeSelect=list.getJSONObject(i).getString("sign_date");
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
        }catch (AssertionError | Exception e){
            appendFailreason(e.toString());
        } finally {
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
        }catch (AssertionError | Exception e){
            appendFailreason(e.toString());
        } finally {
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
        }catch (AssertionError | Exception e){
            appendFailreason(e.toString());
        } finally {
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

        }catch (AssertionError | Exception e){
            appendFailreason(e.toString());
        } finally {
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
            JSONObject object=pf.creatCust();
            Long customer_id=object.getLong("customerId");
            Long reception_id=object.getLong("reception_id");
            String customer_name=object.getString("name");
            String phone=object.getString("phone");
            pf.creatDeliver(reception_id,customer_id,"药不然",dt.getHistoryDate(0),true);
            //完成接待
            crm.finishReception(customer_id, 4, customer_name, phone, pp.remark);   //TODO:完成接待参数
            //小程序登录，查看最新交车
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONObject data=crm.carOwnernew();
            String customer_nameN=data.getString("customer_name");
            String car_model=data.getString("car_model");
            String work=data.getString("work");
            String hobby=data.getString("hobby");
            crm.login(pp.xiaoshouGuwen,pp.adminpassword);
            Preconditions.checkArgument(car_model.equals("Taycan"),"最新交车信息校验失败");
            Preconditions.checkArgument(work.equals("金融"),"最新交车信息校验工作显示错误");
            Preconditions.checkArgument(hobby.equals("宠物"),"最新交车信息校验爱好显示错误");
            Preconditions.checkArgument(customer_nameN.equals("药不然"),"最新交车信息校验车主名显示错误");


        }catch (AssertionError | Exception e){
            appendFailreason(e.toString());
        } finally {
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
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONArray list=crm.carOwner().getJSONArray("list");
            int total;
            if(list==null||list.size()==0){
                total=0;
            }else{
                total=list.size();
            }
            JSONObject object=pf.creatCust();
            Long customer_id=object.getLong("customerId");
            Long reception_id=object.getLong("reception_id");
            String customer_name=object.getString("name");
            String phone=object.getString("phone");
            pf.creatDeliver(reception_id,customer_id,customer_name,dt.getHistoryDate(0),true);
            //完成接待  `
            crm.finishReception(customer_id, 4, customer_name, phone, pp.remark);

            //小程序登录，查看交车
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONArray listA=crm.carOwner().getJSONArray("list");
            int totalA;
            if(listA==null||listA.size()==0){
                totalA=0;
            }else{
                totalA=listA.size();
            }
            crm.login(pp.xiaoshouGuwen,pp.adminpassword);
            Preconditions.checkArgument(totalA-total==1,"建交车授权，applet车主风采列表！=交车前"+totalA+"+交车后"+total);
        }catch (AssertionError | Exception e){
            appendFailreason(e.toString());
        } finally {
            saveData("新建交车授权，applet车主风采列表+1");
        }
    }

    /**
     * @description :交车不授权  ok
     * @date :2020/8/10 16:45
     **/
    @Test()
    public void testdeliverNotShow(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //applet登录，记录原始列表数
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONArray list=crm.carOwner().getJSONArray("list");
            int total;
            if(list==null||list.size()==0){
                total=0;
            }else{
                total=list.size();
            }
            JSONObject object=pf.creatCust();
            Long customer_id=object.getLong("customerId");
            Long reception_id=object.getLong("reception_id");
            String customer_name=object.getString("name");
            String phone=object.getString("phone");
            pf.creatDeliver(reception_id,customer_id,customer_name,dt.getHistoryDate(0),false);
            //完成接待  `
            crm.finishReception(customer_id, 4, customer_name, phone, pp.remark);

            //小程序登录，查看交车
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            JSONArray listA=crm.carOwner().getJSONArray("list");
            int totalA;
            if(listA==null||listA.size()==0){
                totalA=0;
            }else{
                totalA=listA.size();
            }
            crm.login(pp.xiaoshouGuwen,pp.adminpassword);
            logger.info("交车前total{},交车后totalA{}",totalA,total);
            Preconditions.checkArgument(totalA==total,"新建交车不授权，applet车主风采列表+1了");

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("新建交车不授权，applet车主风采列表不+1");
        }
    }

   /**
    * @description :app活动报名,任务人数于创建时相同，增加报名，报名条数+1，  删除报名-1 0k
    * @date :2020/8/3 19:13
    **/
   @Test
   public void taskactivity(){
       logger.logCaseStart(caseResult.getCaseName());
       try{
           //创建活动，获取活动id
           Long [] aid=pf.createAArcile_id(dt.getHistoryDate(0),"8");
           Long id=aid[0];
           //app销售登录报名
           crm.login(pp.xiaoshouGuwen,pp.adminpassword);
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

           int customerId = crm.activityTaskInfo( Integer.toString(activityTaskId)).getJSONArray("customer_list").getJSONObject(0).getInteger("customer_id");
           crm.deleteCustomer(String.valueOf(activityTaskId), customerId);

           int totalAfterDelet=crm.activityTaskPageX().getJSONObject("data").getJSONArray("list").getJSONObject(0).getJSONArray("customer_list").size();
           //获取报名字段，校验
           Preconditions.checkArgument(task_customer_num == 5, "app报名活动，任务人数与活动创建时不一致");
           Preconditions.checkArgument(totalA-total == 1, "app报名活动，报名列表+1");
           Preconditions.checkArgument(totalA-totalAfterDelet== 1, "app删除报名人，报名列表没-1");
           crm.login(pp.zongjingli,pp.adminpassword);
           crm.articleStatusChange(id);
           crm.articleDelete(id);

       }catch (AssertionError | Exception e){
           appendFailreason(e.toString());
       } finally {
           crm.login(pp.xiaoshouGuwen, pp.adminpassword);
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
            List<String> numList = new ArrayList<>();
            if(list==null||list.size()==0){
                logger.info("今日无交车记录");
                return;
            }
            for(int i=0;i<list.size();i++){
                String phone=list.getJSONObject(i).getString("customer_phone_number");
                numList.add(phone);
            }
            Set<String> numSet = new HashSet<>();
            numSet.addAll(numList);
            int similar=numSet.size();
            Preconditions.checkArgument(similar==today_number,"今日交车数！=今日列表电话号码去重数");

        }catch (AssertionError | Exception e){
            appendFailreason(e.toString());
        } finally {
            saveData("交车 今日数=列表电话去重数");
        }
    }

    /**
     * @description :试驾 今日数=列表电话去重数  TODO：
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
            List<String> numList = new ArrayList<>();
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

        }catch (AssertionError | Exception e){
            appendFailreason(e.toString());
        } finally {
            saveData("试驾 今日数=列表电话去重数");
        }
    }








}