package com.haisheng.framework.testng.bigScreen.xundianDaily.zt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumShopId;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.StorePcAndAppData;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class XundianAppData extends TestCaseCommon implements TestCaseStd {
    public static final Logger log = LoggerFactory.getLogger(StorePcAndAppData.class);
    public static final int size = 100;
    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.QQ.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.MENDIAN_DAILY.getName());
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
        commonConfig.shopId = EnumShopId.XUNDIAN_DAILY.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("xundian " + xd);
        xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    // 巡店记录列表报告数量 == 待处理+已处理+无需处理的数量
    @Test
    public void getShopHandleStatusList() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            // 获取巡检员id
            JSONObject shop_id = xd.authShopInspectors(43072l);
            JSONArray xjyid = shop_id.getJSONArray("id");
            String xjyid1 = String.valueOf(xjyid);
            // 获取门店巡店记录列表total总数
            JSONObject shopCHeckStatus = xd.getShopChecksPage(43072l,null,null,"越秀测试账号",xjyid1,"DESC",null,null);
            Integer checks_list = shopCHeckStatus.getInteger("total");
            // 巡店记录处理下拉框
            int listSize = xd.handleStatusList().getJSONArray("list").size();
            Preconditions.checkArgument(checks_list == listSize,"巡店记录列表数量" + checks_list + "不等于待处理+已处理+无需处理的数量=" + listSize);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app现场、远程巡店 处理结果全部合格，不产生待办事项");
        }
    }


    //不合格报告+合格报告 == 列表下全部报告
    @Test
    public void getResultTypeList(){
        try {
            // 获取巡检员id
            JSONObject shop_id = xd.authShopInspectors(43072l);
            JSONArray xjyid = shop_id.getJSONArray("id");
            String xjyid1 = String.valueOf(xjyid);
            // 获取门店巡店记录列表total总数
            JSONObject shopCHeckStatus = xd.getShopChecksPage(43072l,null,null,"越秀测试账号",xjyid1,"DESC",null,null);
            Integer checks_list = shopCHeckStatus.getInteger("total");
            // 巡店记录处理下拉框
            int listSize = xd.resultTypeList().getJSONArray("list").size();
            Preconditions.checkArgument(checks_list == listSize,"巡店记录列表数量" + checks_list + "不等于合格+不合格的数量=" + listSize);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app现场、远程巡店 处理结果全部合格，不产生待办事项");
        }
    }

}




