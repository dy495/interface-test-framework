package com.haisheng.framework.testng.bigScreen.crmOnline.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crmOnline.PublicParmOnline;
import com.haisheng.framework.testng.bigScreen.crmOnline.xmf.interfaceOnline.finishReceiveOnline;
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

public class CrmQtOnlineWarm extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtilOnlineX crm = CrmScenarioUtilOnlineX.getInstance();
    DateTimeUtil dt = new DateTimeUtil();



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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "夏明凤";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-online-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_ONLINE.getName()+"门店：12732" + commonConfig.checklistQaOwner);

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_GRP;
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = "12732";
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        crm.login("hellenan","e10adc3949ba59abbe56e057f20f883e");


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

    @Test
    public void qtztSelect() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String data=dt.getHistoryDate(0);
            JSONArray data2 = crm.qtreceptionPage("", data, data, "1", "10").getJSONArray("list");
           if(data2.size()==0){
               throw new Exception("warm:接待管理今日接待数据为空");
           }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("展厅接待数据监控");
        }
    }

        /**
     * @description :到访记录按时间查询 ok
     * @date :2020/8/3 12:48
     **/
    @Test()
    public void visitRecodeSelectTime() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String select_date=dt.getHistoryDate(0);
            JSONArray list = crm.visitList(select_date, select_date, "1", "10").getJSONArray("list");
            if(select_date.equals("")&&list.size()==0){
                throw new Exception("到访记录为空");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("到访记录监控");
        }
    }

    @Test(description = "线上人脸为空警告")
    public void faceListNontull() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.markcustomerList().getJSONArray("list");
            if(list.size()==0){
                throw new Exception("警告：线上人脸列表为空");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("检查前台人脸列表是否为空");
        }
    }



}
