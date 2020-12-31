package com.haisheng.framework.testng.bigScreen.crm.lxq;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.CustomerInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.FileUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;


/**
 * @author : lxq
 * @date :  2020/05/30
 */

public class GetData extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    CustomerInfo cstm = new CustomerInfo();
    FileUtil fileUtil = new FileUtil();
    public String data = "data" + dt.getHistoryDate(0) + ".txt";
    public String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/lxq/" + data;


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
        commonConfig.checklistQaOwner = "吕雪晴";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");
        commonConfig.produce = EnumProduce.BSJ.name();
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getName() + commonConfig.checklistQaOwner);

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = EnumTestProduce.CRM_DAILY.getShopId();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        crm.login(cstm.xszj, cstm.pwd);

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
    public void savedata() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            filePath = filePath.replace("/", File.separator);

            //今日新客接待+今日老客接待
            JSONObject obj = crm.customerReceptionTotalInfo();
            int todaynew = obj.getInteger("today_new_customer");
            int todayold = obj.getInteger("total_old_customer");
            int all = todaynew + todayold;
            //存
            fileUtil.appendContentToFile(filePath, "今日新客接待+今日老客接待/" + all);
            fileUtil.appendContentToFile(filePath, "今日新客接待1/" + todaynew);
            fileUtil.appendContentToFile(filePath, "今日老客接待1/" + todayold);

            //今日交车
            JSONObject obj1 = crm.deliverCarTotal();
            int todaydeliver = obj1.getInteger("today_deliver_car_total");
            //存
            fileUtil.appendContentToFile(filePath, "今日交车/" + todaydeliver);

            //今日试驾
            JSONObject obj2 = crm.driverTotal();
            int todaydriver = obj2.getInteger("today_test_drive_total");
            //存
            fileUtil.appendContentToFile(filePath, "今日试驾/" + todaydriver);

            //今日线索
            JSONObject obj3 = crm.receptionPage(1, 1, dt.getHistoryDate(0), dt.getHistoryDate(0));
            int todaycust = obj3.getInteger("all_customer_num");
            //存
            fileUtil.appendContentToFile(filePath, "今日线索/" + todaycust);


            //取
            System.out.println(fileUtil.findLineByKey(filePath, "service"));

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("存数据");
        }
    }

    @Test
    public void test() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            dt.calTimeHourDiff("15:00", "16:01");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("存数据");
        }
    }


}
