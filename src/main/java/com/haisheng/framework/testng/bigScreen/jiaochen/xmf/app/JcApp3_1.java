package com.haisheng.framework.testng.bigScreen.jiaochen.xmf.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletConsultAfterServiceSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletConsultOnlineExpertsSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletConsultPreServiceSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.consultmanagement.ResponseRuleEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.JcFunction;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.followType;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.roleList;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.QADbProxy;
import com.haisheng.framework.util.QADbUtil;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JcApp3_1 extends TestCaseCommon implements TestCaseStd {

    private static final EnumTestProduce product = EnumTestProduce.JC_DAILY;
    public VisitorProxy visitor=new VisitorProxy(product);

    ScenarioUtil jc = new ScenarioUtil();
    PublicParm pp = new PublicParm();
    JcFunction pf = new JcFunction(visitor,pp);
    CommonConfig commonConfig = new CommonConfig();

    private QADbProxy qaDbProxy = QADbProxy.getInstance();
    public QADbUtil qaDbUtil = qaDbProxy.getQaUtil();



    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "夏明凤";
        commonConfig.referer = EnumTestProduce.JC_DAILY.getReferer();
        commonConfig.product=EnumTestProduce.JC_DAILY.getAbbreviation();

        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JC_DAILY.getDesc() + commonConfig.checklistQaOwner);

        //replace ding f
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = "49195";
        commonConfig.roleId = "2945";
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);
        appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);

    }

    //app登录
    public void appLogin(String username, String password, String roleId) {
        String path = "/jiaochen/login-m-app";
        JSONObject object = new JSONObject();
        object.put("phone", username);
        object.put("verification_code", password);
        commonConfig.roleId = roleId;
        httpPost(path, object, EnumTestProduce.JC_DAILY.getAddress());
    }

    //pc登录
    public void pcLogin(String phone, String verificationCode, String roleId) {
        String path = "/jiaochen/login-pc";
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        commonConfig.roleId = roleId;
        httpPost(path, object, EnumTestProduce.JC_DAILY.getAddress());
    }

    public void startReception(){
        int receptionId=Integer.valueOf(pf.salereception(pp.customerPhone)[0]);
        qaDbUtil.updateDataNum("app_sale_receptionId",receptionId);
    }
    public void finishReception(){
        Integer receptionId=  qaDbUtil.selsetDataTempOne("pcAppointmentRecordNum","app_sale_receptionId");
        IScene appcustomerEdit = AppCustomerEditScene.builder()
                .id(String.valueOf(receptionId))
                .shopId(pp.shopIdZ)
                .customerName("夏明凤")
                .estimatedBuyTime(dt.getHistoryDate(0))
                .build();
        JSONObject data1 = jc.invokeApi(appcustomerEdit, true);


        IScene appfinishReception = AppFinishReceptionScene.builder()
                .shopId(Long.valueOf(pp.shopIdZ))
                .id((long)receptionId).build();

        jc.invokeApi(appfinishReception);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }
    @Test()
    public void Astart() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            startReception();

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("开始接待");
        }
    }

    @Test()
    public void zfinish() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
           finishReception();

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("完成接待");
        }
    }


    @Test(description = "编辑用户名称51个字异常")
    public void editCustomer1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
           int id= qaDbUtil.selsetDataTempOne("pcAppointmentRecordNum","app_sale_receptionId");
            String[] reception = {String.valueOf(id),null};
            //编辑客户--名称超过50字
            IScene appcustomerEdit = AppCustomerEditScene.builder()
                    .id(reception[0])
                    .shopId(pp.shopIdZ)
                    .customerName(pp.String_50 + "字")
                    .estimatedBuyTime(dt.getHistoryDate(0))
                    .build();
            JSONObject data1 = jc.invokeApi(appcustomerEdit, false);

            Preconditions.checkArgument(data1.getLong("code") == 1001, "异常名称返回值错误");
            Preconditions.checkArgument(data1.getString("message").contains("50"), "异常名称提示错误");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("编辑客户名称异常验证");
        }
    }

    @Test(description = "编辑用户手机号异常")
    public void editCustomer2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id= qaDbUtil.selsetDataTempOne("pcAppointmentRecordNum","app_sale_receptionId");
            String[] reception = {String.valueOf(id),null};
            String[] errphone = {"1590293829","178273766554","新人%￥#"};
            for (int i = 0; i < errphone.length; i++) {
                IScene appcustomerEdit2 = AppCustomerEditScene.builder()
                        .id(reception[0])
                        .shopId(pp.shopIdZ)
                        .customerName("一个字")
                        .customerPhone(errphone[i])
                        .estimatedBuyTime(dt.getHistoryDate(0))
                        .build();
                JSONObject data1 = jc.invokeApi(appcustomerEdit2, false);

                Preconditions.checkArgument(data1.getLong("code") == 1001, "异常手机号返回值错误");
                Preconditions.checkArgument(data1.getString("message").contains("手机号"), "异常手机号提示错误");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("编辑客户手机号异常验证");
        }
    }

   /**
    * @description :     为避免在小程序端产生较多脏数据，仅测试时运行
    * @date :2021/5/27 11:51
    **/
//    @Test(description = "买车购车记录+1")
    public void buyCar() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id= qaDbUtil.selsetDataTempOne("pcAppointmentRecordNum","app_sale_receptionId");
            String[] reception = {String.valueOf(id),null};
            //编辑客户信息
            IScene appcustomerEdit2 = AppCustomerEditScene.builder()
                    .id(reception[0])
                    .shopId(pp.shopIdZ)
                    .customerName("一个字")
                    .customerPhone(pp.customerPhone)
                    .estimatedBuyTime(dt.getHistoryDate(0))
                    .carModel(Long.valueOf(pp.carModelId))
                    .build();
            JSONObject data1 = jc.invokeApi(appcustomerEdit2);

            //购车记录数
            IScene customerDetail=AppCustomerDetailScene.builder().id(reception[0]).shopId(pp.shopIdZ).build();
            JSONObject orderList=jc.invokeApi(customerDetail);
            JSONArray list=orderList.getJSONArray("order_list");

            //购车
            IScene appbuycar= AppBuyCarScene.builder().id(Long.valueOf(reception[0]))
                    .carModel(Long.valueOf(pp.carModelId))
                    .shopId(Long.valueOf(pp.shopIdZ))
                    .vin("AAAAAAASDS1829837")   //底盘号
                    .build();
            jc.invokeApi(appbuycar);
            //购车记录数
            IScene customerDetail2=AppCustomerDetailScene.builder().id(reception[0]).shopId(pp.shopIdZ).build();
            JSONObject orderList2=jc.invokeApi(customerDetail2);
            JSONArray list2=orderList2.getJSONArray("order_list");

            Preconditions.checkArgument(list2.size()-list.size()==1,"购车后记录+1");





        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("买车购车记录+1");
        }
    }

    @Test(description = "销售变更接待")
    public void changeReception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id= qaDbUtil.selsetDataTempOne("pcAppointmentRecordNum","app_sale_receptionId");
            String[] reception = {String.valueOf(id),null};
            String uid="";

            JSONArray saleList=jc.AppReceptorListScene(Long.parseLong(pp.shopIdZ)).getJSONArray("list");
            for(int i=0;i<saleList.size();i++){
                 uid=saleList.getJSONObject(i).getString("uid");
                if(!uid.equals(pp.useridxs)){
                   break;
                }
            }
            int totalBefore=pf.appSaleReceptionPage();
            //变更接待
            IScene appreceptionChange=AppReceptorChangeScene.builder().receptorId(uid)
                    .shopId(Long.valueOf(pp.shopIdZ))
                    .id(Long.valueOf(reception[0]))
                    .build();
            jc.invokeApi(appreceptionChange);
            int total=pf.appSaleReceptionPage();

            pcLogin(pp.dzphone,pp.dzcode,pp.dzroleId);
            //店长登录 变更接待
            appreceptionChange=AppReceptorChangeScene.builder().receptorId(pp.useridxs)
                    .shopId(Long.valueOf(pp.shopIdZ))
                    .id(Long.valueOf(reception[0]))
                    .build();
            jc.invokeApi(appreceptionChange);
            appLogin(pp.jdgw,pp.gwpassword,pp.roleidJdgw);
            int totalAfter=pf.appSaleReceptionPage();

            Preconditions.checkArgument(total-totalBefore==-1,"变更接待列表-1");
            Preconditions.checkArgument(total-totalAfter==-1,"再次变更列表+1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            pcLogin(pp.jdgw,pp.jdgwpassword,pp.roleidJdgw);
            saveData("变更接待+1");
        }
    }

    @Test(description = "备注")
    public void remark() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id= qaDbUtil.selsetDataTempOne("pcAppointmentRecordNum","app_sale_receptionId");
            String[] reception = {String.valueOf(id),null};

            IScene customerDetail=AppCustomerDetailScene.builder().id(reception[0]).shopId(pp.shopIdZ).build();
            JSONObject orderList=jc.invokeApi(customerDetail);
            JSONArray list=orderList.getJSONArray("remarks");

            String remark=pp.String_50;
            jc.AppCustomerRemarkScene(Long.valueOf(reception[0]),Long.valueOf(pp.shopIdZ),remark);

            IScene customerDetail2=AppCustomerDetailScene.builder().id(reception[0]).shopId(pp.shopIdZ).build();
            JSONObject orderList2=jc.invokeApi(customerDetail2);
            JSONArray list2=orderList2.getJSONArray("remarks");

//            Preconditions.checkArgument(list2.size()-list.size()==1,"备注失败");




        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("备注 查看详情");
        }
    }

//    @Test(description = "在线专家回复")
    public void follow_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int totalBefore=pf.followPageNumber();
            jc.appletLoginToken(pp.appletTocken);
            IScene onlineExpert = AppletConsultOnlineExpertsSubmitScene.builder()
                    .shopId(Long.valueOf(pp.shopIdZ))
                    .content("咨询轮胎保养")
                    .customerName("小明")
                    .customerPhone("15037286013")
                    .modelId(Long.valueOf(pp.carModelId))
                    .brandId(pp.brandId)
                    .build();
            JSONObject data=jc.invokeApi(onlineExpert);

            //跟进列表 获取id
            JSONObject followList=jc.AppPageV3Scene(10,null, followType.ONLINE_EXPERTS.getName());
            int total=followList.getInteger("total");
            Long id=followList.getJSONArray("list").getJSONObject(0).getLong("id");

            Preconditions.checkArgument(total-totalBefore==1,"在线咨询，跟进列表+1");

            //回复
            jc.AppReplyV3Scene(id,"轮胎保养参考用户手册");

            //备注
            jc.AppRemarkV3Scene(id,"我是备注");

            JSONObject lastvalue=null;
            int size=10;
            while(size<10){
                JSONObject followPageAfter=jc.AppPageV3Scene(10,lastvalue, followType.ONLINE_EXPERTS.getName());
                JSONArray followListAfter=followPageAfter.getJSONArray("list");
                size=followListAfter.size();
                lastvalue=followPageAfter.getJSONObject("last_value");

                for(int i=0;i<followListAfter.size();i++){
                    Long idAfter = followListAfter.getJSONObject(i).getLong("id");
                    if(idAfter.equals(id)){
                        //返回跟进和 备注的内容
                        break;
                    }

                }

            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("在线专家回复");
        }
    }


//    @Test(description = "专属售后咨询")   //TODO: 销售顾问咨询 copy 即可
    public void follow_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject followListBefore=jc.AppPageV3Scene(10,null, followType.ONLINE_EXPERTS.getName());
            int totalBefore=followListBefore.getInteger("total");
            IScene onlineExpert = AppletConsultAfterServiceSubmitScene.builder()
                    .shopId(Long.valueOf(pp.shopIdZ))
                    .content("专属售后咨询")
                    .customerName("小明")
                    .customerPhone("15037286013")
                    .modelId(Long.valueOf(pp.carModelId))
                    .salesId(pp.userid)
                    .build();
            JSONObject data=jc.invokeApi(onlineExpert);

            //跟进列表 获取id
            JSONObject followList=jc.AppPageV3Scene(10,null, followType.ONLINE_EXPERTS.getName());
            int total=followList.getInteger("total");
            Long id=followList.getJSONArray("list").getJSONObject(0).getLong("id");

            Preconditions.checkArgument(total-totalBefore==1,"在线咨询，跟进列表+1");

            //回复
            jc.AppReplyV3Scene(id,"轮胎保养参考用户手册");

            //备注
            jc.AppRemarkV3Scene(id,"我是备注");

            //循环查找 列表中需要的跟进任务
            JSONObject lastvalue=null;
            int size=10;
            while(size<10){
                JSONObject followPageAfter=jc.AppPageV3Scene(10,lastvalue, followType.ONLINE_EXPERTS.getName());
                JSONArray followListAfter=followPageAfter.getJSONArray("list");
                size=followListAfter.size();
                lastvalue=followPageAfter.getJSONObject("last_value");

                for(int i=0;i<followListAfter.size();i++){
                    Long idAfter = followListAfter.getJSONObject(i).getLong("id");
                    if(idAfter.equals(id)){
                        //返回跟进和 备注的内容
                        break;
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("在线专家回复");
        }
    }

    @Test(description = "权限相关")  //修改预约应答人权限，小程序接待人列表对应变更 ok
    public void permissions() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcLogin(pp.gwphone,pp.gwpassword,pp.roleId);
//            Long i=pf.getAccessId2("个人");

            //账户登录  修改角色的权限
            JSONArray roleList1 = new JSONArray();
            roleList1.add( roleList.findByLable("个人").getValue());
            roleList1.add(roleList.findByLable("门店").getValue());
            //预约保养分配
            jc.organizationRoleEdit(Long.parseLong(pp.userroleId),"临时用户","随时修改用户权限",roleList1);
            jc.appletLoginToken(pp.appletTocken);
            int staffTotalBefore =  jc.AppletAppointmentStaffListScene("MAINTAIN",Long.valueOf(pp.shopIdZ)).getJSONArray("list").size();

            roleList1.add(roleList.findByLable("预约保养分配").getValue());
            //            appLogin(pp.user,pp.userpassword,pp.userroleId);
            pcLogin(pp.gwphone,pp.gwpassword,pp.roleId);
            jc.organizationRoleEdit(Long.parseLong(pp.userroleId),"临时用户","随时修改用户权限",roleList1);

            //小程序 门店下接待人员总数
            jc.appletLoginToken(pp.appletTocken);
            int staffTotal=  jc.AppletAppointmentStaffListScene("MAINTAIN",Long.valueOf(pp.shopIdZ)).getJSONArray("list").size();

            roleList1.remove(2);
            pcLogin(pp.gwphone,pp.gwpassword,pp.roleId);
            jc.organizationRoleEdit(Long.parseLong(pp.userroleId),"临时用户","随时修改用户权限",roleList1);

            jc.appletLoginToken(pp.appletTocken);
            int staffTotalAfter=  jc.AppletAppointmentStaffListScene("MAINTAIN",Long.valueOf(pp.shopIdZ)).getJSONArray("list").size();
            Preconditions.checkArgument(staffTotalAfter-staffTotal==-1,"去掉权限后："+staffTotalAfter+"去掉权限前:"+staffTotal);
            Preconditions.checkArgument(staffTotalBefore-staffTotal==-1,"增加权限前："+staffTotalBefore+"增加之后:"+staffTotal);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约应答人权限");
        }
    }

    @Test(description = "在线专家配置")
    public void expertsConfig() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type="ONLINE_EXPERTS";
            Integer remind=1;
            pcLogin(pp.gwphone,pp.gwpassword,pp.roleId);
            String workday = "{\n" +
                    "            \"afternoon_date_start\":\"13:00\",\n" +
                    "            \"forenoon_date_end\":\"12:00\",\n" +
                    "            \"forenoon_date_start\":\"04:00\",\n" +
                    "            \"afternoon_date_end\":\"23:59\"\n" +
                    "        }";
            String weekDay = "{\n" +
                    "            \"afternoon_date_start\":\"13:00\",\n" +
                    "            \"forenoon_date_end\":\"12:00\",\n" +
                    "            \"forenoon_date_start\":\"04:00\",\n" +
                    "            \"afternoon_date_end\":\"17:00\"\n" +
                    "        }";
            JSONObject work_day = JSONObject.parseObject(workday);
            JSONObject week_day = JSONObject.parseObject(weekDay);
           IScene responseRuleEditScene = ResponseRuleEditScene.builder().businessType(type).remindTime(remind).overTime(remind)
                    .workDay(work_day).weekDay(week_day).build();
            jc.invokeApi(responseRuleEditScene);


            jc.appletLoginToken(pp.appletTocken);
            //发起专属服务
            IScene appletCustomer= AppletConsultOnlineExpertsSubmitScene.builder().customerName("夏明凤").customerPhone(pp.customerPhone)
                    .content("自动询问有权限者收1234567890")
                    .modelId(Long.parseLong(pp.carModelId)).shopId(Long.parseLong(pp.shopIdZ)).build();
            jc.invokeApi(appletCustomer);

            appLogin(pp.jdgw,pp.jdgwpassword,pp.roleidJdgw);
            //消息数
            int gwtotal=jc.appmessageList("20",null).getInteger("total");
            appLogin(pp.dzphone,pp.dzcode,pp.dzroleId);
            int dztotal=jc.appmessageList("20",null).getInteger("total");

            sleep(remind*60);
            int dztotalAfter=jc.appmessageList("20",null).getInteger("total");

            appLogin(pp.jdgw,pp.jdgwpassword,pp.roleidJdgw);
            int gwtotalAfter=jc.appmessageList("20",null).getInteger("total");

            Preconditions.checkArgument(gwtotalAfter==gwtotal,"销售顾问没有提醒接受权限,确收到提醒");
            Preconditions.checkArgument(dztotalAfter-dztotal==1,"店长有提醒接受权限，没有收到提醒");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("配置在线专家，提醒接收人在超时事件后收到提醒");
        }
    }

    @Test(description = "pc配置专属服务")
    public void serverConfig() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type="SALES";
            Integer remind=1;
            pcLogin(pp.gwphone,pp.gwpassword,pp.roleId);
            String workday = "{\n" +
                    "            \"afternoon_date_start\":\"13:00\",\n" +
                    "            \"forenoon_date_end\":\"12:00\",\n" +
                    "            \"forenoon_date_start\":\"04:00\",\n" +
                    "            \"afternoon_date_end\":\"23:59\"\n" +
                    "        }";
            String weekDay = "{\n" +
                    "            \"afternoon_date_start\":\"13:00\",\n" +
                    "            \"forenoon_date_end\":\"12:00\",\n" +
                    "            \"forenoon_date_start\":\"04:00\",\n" +
                    "            \"afternoon_date_end\":\"17:00\"\n" +
                    "        }";
            JSONObject work_day = JSONObject.parseObject(workday);
            JSONObject week_day = JSONObject.parseObject(weekDay);
            IScene responseRuleEditScene = ResponseRuleEditScene.builder().businessType(type).remindTime(remind).overTime(remind)
                    .workDay(work_day).weekDay(week_day).build();
            jc.invokeApi(responseRuleEditScene);


            jc.appletLoginToken(pp.appletTocken);
            //发起专属服务
            IScene appletCustomer= AppletConsultPreServiceSubmitScene.builder().customerName("夏明凤").customerPhone(pp.customerPhone)
                    .content("自动发起专属服务，有提醒权限者收1111111")
                    .salesId(pp.userid).modelId(Long.parseLong(pp.carModelId)).shopId(Long.parseLong(pp.shopIdZ)).build();
            jc.invokeApi(appletCustomer);

            appLogin(pp.jdgw,pp.jdgwpassword,pp.roleidJdgw);
            //消息数
            int gwtotal=jc.appmessageList("20",null).getInteger("total");
            appLogin(pp.dzphone,pp.dzcode,pp.dzroleId);
            int dztotal=jc.appmessageList("20",null).getInteger("total");

            sleep(remind*60);
            int dztotalAfter=jc.appmessageList("20",null).getInteger("total");

            appLogin(pp.jdgw,pp.jdgwpassword,pp.roleidJdgw);
            int gwtotalAfter=jc.appmessageList("20",null).getInteger("total");

            Preconditions.checkArgument(gwtotalAfter==gwtotal,"销售顾问没有提醒接受权限,确收到提醒");
            Preconditions.checkArgument(dztotalAfter-dztotal==1,"店长有提醒接受权限，没有收到提醒");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("配置在线专家，提醒接收人在超时事件后收到提醒");
        }
    }

    @DataProvider(name = "onlineExpertInfo")
    public static Object[] onlineExpertInfo(){
        return new String[]{
                followType.ONLINE_EXPERTS.getType(),
                followType.SALES.getType(),
        };
    }

    @Test(dataProvider ="onlineExpertInfo", description = "跟进回复在线专家和专属服务")   //三次
    public void zfollowRemark(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appLogin(pp.jdgw, pp.jdgwpassword,pp.roleidJdgw);
            JSONObject data=jc.AppPageV3Scene(10,null,type);
            JSONArray list=data.getJSONArray("list");
            Long followId=list.getJSONObject(0).getLong("id");
//            jc.AppRemarkV3Scene(followId,"自动回复在线专家专属服务您可满意！！！");AppReplyV3Scene
            jc.AppReplyV3Scene(followId,"自动回复在线专家专属服务您可满意！！！");

//            JSONObject dataAfter=jc.AppPageV3Scene(10,null,type);
//            JSONArray listAfter=dataAfter.getJSONArray("list");
//            Long followIdAfter=listAfter.getJSONObject(0).getLong("id");
//            String isreply=listAfter.getJSONObject(0).getString("is_reply");
//            Preconditions.checkArgument(isreply.equals("true"),"");




        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("跟进回复在线专家和专属服务");
        }
    }

    @DataProvider(name = "repairreply")
    public static Object[] repairreply(){
        return new String[]{
                followType.MAINTAIN_EVALUATE.getType(),
                followType.REPAIR_EVALUATE.getType(),
        };
    }

//    @Test(dataProvider ="repairreply", description = "跟进回复差评")   //三次
    public void zfollowRemark2(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appLogin(pp.dzphone, pp.dzcode,pp.dzroleId);
            JSONObject data=jc.AppPageV3Scene(10,null,type);
            JSONArray list=data.getJSONArray("list");
            Long followId=list.getJSONObject(0).getLong("id");
            jc.AppRemarkV3Scene(followId,"自动回复维修保养差评您可满意！！！");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            appLogin(pp.jdgw, pp.jdgwpassword,pp.roleidJdgw);
            saveData("轿辰-app接待,今日数据待处理接待+1,完成接待，待处理接待-1");
        }
    }
    @Test
    public void collectsort(){
        Pattern p=Pattern.compile("\\d");
        Matcher m=p.matcher("QWe");
        System.out.println(m.find());

        List a=new ArrayList();
        Collections.sort(a);
    }










}
