package com.haisheng.framework.testng.bigScreen.itemPorsche.caseonline.xmf;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.PublicParmOnline;
import com.haisheng.framework.testng.bigScreen.itemPorsche.caseonline.xmf.interfaceOnline.FinishReceptionOnline;
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

/**
 * @description :线上23点接待客户交车
 * @date :2020/11/2 16:14
 **/


public class Crm2_1AppXOnline23_receipt extends TestCaseCommon implements TestCaseStd {
    EnumTestProduct product = EnumTestProduct.PORSCHE_ONLINE;
    CrmScenarioUtilOnlineX crm = CrmScenarioUtilOnlineX.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParmOnline pp = new PublicParmOnline();
    PackFunctionOnline pf = new PackFunctionOnline();


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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.setShopId(product.getShopId()).setReferer(product.getReferer()).setRoleId(product.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        crm.login(pp.xiaoshouGuwen, pp.xsgwPassword);


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

    @Test(description = "23点接待客户，购车交车，有手机号完成接待")
    public void testdeliver() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject json = pf.creatCust();
            FinishReceptionOnline fr = new FinishReceptionOnline();
            fr.name = json.getString("name");
            fr.reception_id = json.getString("reception_id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.phoneList = json.getJSONArray("phoneList");
            fr.reception_type = "FU";
            pf.creatDeliver(Long.parseLong(fr.reception_id), Long.parseLong(fr.customer_id), fr.name, dt.getHistoryDate(0), true);
            crm.finishReception3(fr);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建新客完成接待");
        }
    }

    @Test(description = "23点接待客户无手机号")
    public void testdeliverNophone() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject json = pf.creatCust();
            FinishReceptionOnline fr = new FinishReceptionOnline();
            fr.name = json.getString("name");
            fr.reception_id = json.getString("reception_id");
            fr.customer_id = json.getString("customerId");
            fr.belongs_sale_id = json.getString("sale_id");
            fr.reception_type = "FU";
            crm.finishReception3(fr);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建新客完成接待");
        }
    }

}
