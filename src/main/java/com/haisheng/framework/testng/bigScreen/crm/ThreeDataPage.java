package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.CustomerInfo;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;


/**
 * @author : lxq
 * @date :  2020/05/30
 */

public class ThreeDataPage extends TestCaseCommon implements TestCaseStd {
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
     * 店面数据分析页 页面内一致性
     */
    @Test
    public void serviceETdriver() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONObject obj = crm.shopPannel("DAY","","");
            int service = obj.getInteger("service");
            int test_drive = obj.getInteger("test_drive");
            Preconditions.checkArgument(service>=test_drive,"累计接待" + service +" < " + "累计试驾" + test_drive);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析：累计接待>=累计试驾");
        }
    }

    @Test
    public void serviceETdeal() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONObject obj = crm.shopPannel("DAY","","");
            int service = obj.getInteger("service");
            int deal = obj.getInteger("deal");
            Preconditions.checkArgument(service>=deal,"累计接待" + service +" < " + "累计成交" + deal);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析：累计接待>=累计成交");
        }
    }

    @Test
    public void serviceETdelivery() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONObject obj = crm.shopPannel("DAY","","");
            int service = obj.getInteger("service");
            int delivery = obj.getInteger("delivery");
            Preconditions.checkArgument(service>=delivery,"累计接待" + service +" < " + "累计交车" + delivery);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析：累计接待>=累计交车");
        }
    }

    @Test
    public void businessClue() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONObject obj = crm.saleFunnel("DAY","","").getJSONObject("business").getJSONArray("list").getJSONObject(0);
            int clue = obj.getInteger("value");
            int creat = obj.getJSONArray("detail").getJSONObject(0).getInteger("value"); //创建线索
            int recp = obj.getJSONArray("detail").getJSONObject(1).getInteger("value"); //接待线索
            int all = creat + recp;
            Preconditions.checkArgument(clue==all ,"线索" + clue +" != " + "创建线索" + creat +" + 接待线索" + recp);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("店面数据分析-业务漏斗：线索=创建线索+接待线索");
        }
    }




}
