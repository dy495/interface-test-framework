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
import com.haisheng.framework.util.FileUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Random;

public class CrmPc4_0X extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp=new PublicParm();
    PackFunction pf=new PackFunction();
    FileUtil file=new FileUtil();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "CRM 日常x");

        //replace ding push conf
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
//        commonConfig.dingHook = DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        crm.login(pp.zongjingli,pp.adminpassword);
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
     * @description :新建车型车系，pc对应列表+-1
     * @date :2020/9/10 18:10
     **/

    @Test
    public void addCarStyle(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            long total1=crm.carStyleList(1,10).getLong("total");
            String carStylename="车系";
            JSONObject data=crm.addCarStyle(carStylename);
            long id=data.getLong("id");
            JSONObject style=crm.carStyleList();
            long total2=style.getLong("total");
            //新建车型
            long modelTotal1=crm.carmodelList(id,1,10).getLong("total");
            crm.addCarmodel(id,"车型");
            long modelTotal2=crm.carmodelList(id,1,10).getLong("total");
            //禁用新建车系
            crm.carStyleEffect(id,false);
            Preconditions.checkArgument(total2-total1==1,"新建车系，车系列表+1");
            Preconditions.checkArgument(modelTotal2-modelTotal1==1,"新建车系，车系列表+1");
        }catch (AssertionError |Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("新建车系，车系列表数+1");
        }
    }

    /**
     * @description :启用禁用车系，小程序，车系下拉菜单数+—1 TODO:
     * @date :2020/9/10 18:17
     **/

    @Test
    public void CarStyleEffect(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //禁用某车型
            JSONObject data=crm.carStyleList(1,10);
            long id=data.getJSONArray("list").getJSONObject(0).getLong("car_style_id");
            crm.carStyleEffect(id,true);

            //小程序原车系数
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            int total=crm.carStyleList().getJSONArray("list").size();
            crm.login(pp.zongjingli,pp.adminpassword);
            crm.carStyleEffect(id,false);

            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            int total2=crm.carStyleList().getJSONArray("list").size();
            Preconditions.checkArgument(total-total2==1,"禁用车系，小程序车系列表没-1");

        }catch (AssertionError |Exception e){
            appendFailreason(e.toString());
        }finally {
            crm.login(pp.zongjingli,pp.adminpassword);
            saveData("禁用车系，小程序车系列表-1");
        }
    }

    /**
     * @description :启用禁用车型，小程序，车型车系下拉菜单数+—1 TODO:
     * @date :2020/9/10 18:17
     **/

    @Test
    public void CarModelEffect(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            long car_style=1L;  //TODO:车系id赋定值
            //禁用某车型
            JSONObject data=crm.carmodelList(car_style,1,10);
            long id=data.getJSONArray("list").getJSONObject(0).getLong("car_model_id");
            crm.carmodelEffect(id,true);

            //小程序原车系数
            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            int total=crm.carModelList(car_style).getJSONArray("list").size();
            crm.login(pp.zongjingli,pp.adminpassword);
            crm.carmodelEffect(id,false);

            crm.appletLoginToken(EnumAppletCode.XMF.getCode());
            int total2=crm.carModelList(car_style).getJSONArray("list").size();
            Preconditions.checkArgument(total-total2==1,"禁用车型，小程序车型列表没-1");

        }catch (AssertionError |Exception e){
            appendFailreason(e.toString());
        }finally {
            crm.login(pp.zongjingli,pp.adminpassword);
            saveData("禁用车型，小程序车型列表-1");
        }
    }
    /**
     * @description :新建Dcc线索，Dcc列表总数+1
     * @date :2020/9/11 21:17
     **/

    @Test
    public void CreateDcc(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
           int total=crm.dcclist(1,100).getInteger("total");
           String name="dcc"+dt.getHHmm(0);
            Random random = new Random();
            String phone = "177" + (random.nextInt(89999999) + 10000000);
           String plateNum="京K8S123";
           crm.dccCreate(name,phone,plateNum);
            int total2=crm.dcclist(1,100).getInteger("total");
            Preconditions.checkArgument(total2-total==1,"新建Dcc线索，Dcc列表总数+1");
        }catch (AssertionError |Exception e){
            appendFailreason(e.toString());
        }finally {
            crm.login(pp.zongjingli,pp.adminpassword);
            saveData("新建Dcc线索，Dcc列表总数+1");
        }
    }

}