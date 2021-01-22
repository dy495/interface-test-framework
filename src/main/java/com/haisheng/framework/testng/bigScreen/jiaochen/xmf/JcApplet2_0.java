package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PackFunction;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.LoginUtil;
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

import static com.google.common.base.Preconditions.checkArgument;

public class JcApplet2_0 extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = new ScenarioUtil();
    PublicParm pp = new PublicParm();
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
        commonConfig.product = EnumProduce.JC.name();

        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.referer = "https://servicewechat.com/wx4071a91527930b48/";

        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = "45973";
        beforeClassInit(commonConfig);
        jc.appletLoginToken(pp.appletTocken);
        logger.debug("jc: " + jc);


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

    @Test(description = "申请道路救援，pc救援列表+1")
    public void recuse() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            int total = jc.pcrescuePage(1, 10).getInteger("total");

            jc.appletLoginToken(pp.appletTocken);
            jc.rescueApply(pp.shopIdZ, pp.coordinate);

            jc.pcLogin(pp.gwphone, pp.gwpassword);
            int totalAfter = jc.pcrescuePage(1, 10).getInteger("total");
            Preconditions.checkArgument(totalAfter - total == 1, "申请道路救援，pc救援列表+1");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("申请道路救援，pc救援列表+1");
        }
    }
    //获取某种状态的门店数量
    public Integer getshopNumber(String status){
        JSONObject data=jc.shopPage(1,10,null);
        int count=0;
        int pages=data.getInteger("pages");
        for(int i=0;i<pages;i++){
            JSONArray list=jc.shopPage(i,10,null).getJSONArray("list");
            for(int j=0;i<list.size();j++){
                String  status1=list.getJSONObject(i).getString(status);
                if(status1.equals("ENABLE")){
                    count++;
                }
            }

        }
        return count;
    }

    @Test(description = "道路救援门店数=门店管理开启的门店")
    public void recuseShopList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.pcLogin(pp.gwphone,pp.gwpassword);
            int openShoptotal=getshopNumber("status");
            jc.appletLoginToken(pp.appletTocken);
            int total=jc.rescueShopList(null,null).getJSONArray("list").size();  //TODO：接口没有返回门店列表总数，暂取list的size
            Preconditions.checkArgument(openShoptotal==total,"道路救援门店数!=门店管理开启的门店");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("道路救援");
        }
    }


    @Test(description = "免费洗车门店数=门店管理开启的门店")
    public void washShopList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.pcLogin(pp.gwphone,pp.gwpassword);
            int openShoptotal=getshopNumber("washing_status");
            jc.appletLoginToken(pp.appletTocken);
            int total=jc.carWashShopList(pp.coordinate).getJSONArray("list").size();    //TODO：接口没有返回门店列表总数，暂取list的size
            Preconditions.checkArgument(openShoptotal==total,"免费洗车门店数!=门店管理洗车开启的门店");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("免费洗车门店数=门店管理开启的门店");
        }
    }
    @Test(description = "洗车，剩余次数-1，pc洗车管理+1")
    public void washCar() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.pcLogin(pp.jdgw, pp.gwpassword);
            int total = jc.washCarManagerPage(pp.shopIdZ, "1", "10").getInteger("total");  //洗车管理数

            jc.appletLoginToken(pp.appletTocken);
            int washCarTimes = jc.washTimes().getInteger("remainNumber");  //洗车次数

            jc.carWsah(pp.shopIdZ);
            int washCarTimesAfter = jc.washTimes().getInteger("remainNumber");

            jc.pcLogin(pp.jdgw, pp.gwpassword);
            int totalAfter = jc.washCarManagerPage(pp.shopIdZ, "1", "10").getInteger("total");

            Preconditions.checkArgument(totalAfter - total == 1, "洗车，洗车管理+1");
            Preconditions.checkArgument(washCarTimes - washCarTimesAfter == 1, "洗车，洗车次数-1");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.appletLoginToken(pp.appletTocken);
            saveData("洗车，洗车管理+1");
        }
    }
}
