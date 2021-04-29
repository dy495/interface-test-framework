package com.haisheng.framework.testng.bigScreen.jiaochen.xmf.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletConsultAfterServiceSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletConsultOnlineExpertsSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.JcFunction;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.followType;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.roleList;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class JcApp3_1 extends TestCaseCommon implements TestCaseStd {

    ScenarioUtil jc = new ScenarioUtil();

    PublicParm pp = new PublicParm();
    JcFunction pf = new JcFunction();
    JsonPathUtil jp = new JsonPathUtil();
    CommonConfig commonConfig = new CommonConfig();


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

    public String[] salereception(String phone) {
        //注册过的手机号接待
        IScene appAdmitScene = AppAdmitScene.builder().phone(phone).build();
        JSONObject data = jc.invokeApi(appAdmitScene);
        Long customerId = data.getLong("customer_id");
        //开始接待
        IScene appstartReception = AppStartReceptionScene.builder()
                .customerId(customerId)
                .customerPhone(phone)
                .build();
        String[] receptionId = new String[2];
        receptionId[0] = jc.invokeApi(appstartReception).getString("id");  //接待ID
        return receptionId;
    }

    //销售接待
//    @Test(dataProvider = "ERR_PHONE", dataProviderClass = DataAbnormal.class)
    public void saleReception(String phone) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //搜索手机号异常验证
            IScene appAdmitScene = AppAdmitScene.builder().phone(phone).build();
            int code = jc.invokeApi(appAdmitScene, false).getInteger("code");
            Preconditions.checkArgument(code == 1001, "异常格式手机号：" + phone);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("搜索手机号异常验证");
        }
    }

    @Test(description = "编辑用户名称51个字异常")
    public void editCustomer1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String phone="15037286013";
            String[] reception = salereception(phone);
            System.out.println(reception[0]+":"+reception[1]);
            //编辑客户--名称超过50字
            IScene appcustomerEdit = AppCustomerEditScene.builder()
                    .id(String.valueOf(reception[0]))
                    .customerName(pp.String_50 + "字")
                    .estimatedBuyTime(dt.getHistoryDate(0))
                    .build();
            JSONObject data1 = jc.invokeApi(appcustomerEdit, false);

            Preconditions.checkArgument(data1.getLong("code") == 1001, "异常名称返回值错误");
            Preconditions.checkArgument(data1.getString("message").contains("50"), "异常名称提示错误");

            //完成接待
            IScene appfinishReception = AppFinishReceptionScene.builder()
                    .shopId(Long.valueOf(pp.shopIdZ))
                    .id(Long.valueOf(reception[0])).build();

            jc.invokeApi(appfinishReception);


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
            String[] reception = salereception(pp.customerPhone);
            String[] errphone = {};
            for (int i = 0; i < errphone.length; i++) {
                IScene appcustomerEdit2 = AppCustomerEditScene.builder()
                        .id(String.valueOf(reception[0]))
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

    //购车
    @Test(description = "买车购车记录+1")
    public void buyCar() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String phone = pf.genPhoneNum();
            //接待新客
            String[] reception = salereception(phone);
            //编辑客户信息
            IScene appcustomerEdit2 = AppCustomerEditScene.builder()
                    .id(String.valueOf(reception[0]))
                    .customerName("一个字")
                    .customerPhone(phone)
                    .estimatedBuyTime(dt.getHistoryDate(0))
                    .carModel(Long.valueOf(pp.carModelId))
                    .build();
            JSONObject data1 = jc.invokeApi(appcustomerEdit2);
            //购车
            IScene appbuycar= AppBuyCarScene.builder().id(Long.valueOf(reception[0]))
                    .carModel(Long.valueOf(pp.carModelId))
                    .shopId(Long.valueOf(pp.shopIdZ))
                    .vin("ASDFGGGHH")   //底盘号
                    .build();
            jc.invokeApi(appbuycar);
            //购车记录数
            IScene customerDetail=AppCustomerDetailScene.builder().id(String.valueOf(reception[0])).build();
            JSONObject orderList=jc.invokeApi(customerDetail);
            ReadContext context = JsonPath.parse(orderList);
            JSONArray ll=context.read("$data.order_list");  //array 大小即为记录条数

            //备注
            IScene appCustomerRemark=AppCustomerRemarkScene.builder()
                    .remark("备注记录你来过")
                    .id(Long.valueOf(reception[0]))
                    .shopId(Long.valueOf(pp.shopIdZ)).build();
            jc.invokeApi(appCustomerRemark);



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
            String phone = pf.genPhoneNum();
            //接待新客
            String[] reception = salereception(phone);
            //编辑客户信息
            IScene appcustomerEdit2 = AppCustomerEditScene.builder()
                    .id(String.valueOf(reception[0]))
                    .customerName("一个字")
                    .customerPhone(phone)
                    .estimatedBuyTime(dt.getHistoryDate(0))
                    .carModel(Long.valueOf(pp.carModelId))
                    .build();
            JSONObject data1 = jc.invokeApi(appcustomerEdit2);



            //变更接待
            IScene appreceptionChange=AppReceptorChangeScene.builder().receptorId(pp.useridxs)
                    .shopId(Long.valueOf(pp.shopIdZ))
                    .id(Long.valueOf(reception[0]))
                    .build();

            //店长登录 变更接待

            appreceptionChange=AppReceptorChangeScene.builder().receptorId(pp.useridxs)
                    .shopId(Long.valueOf(pp.shopIdZ))
                    .id(Long.valueOf(reception[0]))
                    .build();


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("变更接待+1");
        }
    }


    @Test(description = "备注")
    public void remark() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String []reception=new String[3];
            String remark="";
            jc.AppCustomerRemarkScene(Long.valueOf(reception[0]),Long.valueOf(pp.shopIdZ),remark);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("备注 查看详情");
        }
    }

    @Test(description = "在线专家回复")
    public void follow_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject followListBefore=jc.AppPageV3Scene(10,null, followType.ONLINE_EXPERTS.getName());
            int totalBefore=followListBefore.getInteger("total");
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


    @Test(description = "专属售后咨询")   //TODO: 销售顾问咨询 copy 即可
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

    //权限相关

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











}
