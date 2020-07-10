package com.haisheng.framework.testng.bigScreen.ExperienceofstoreDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.FileUtil;
import com.haisheng.framework.util.JsonpathUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author : qingqing
 * @date :  2020/07/06
 */

public class storeCase extends TestCaseCommon implements TestCaseStd {
    storeScenarioUtil Md = storeScenarioUtil.getInstance();
    String xjy4="uid_663ad653";




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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";

//
//        //replace backend gateway url
//        //commonConfig.gateway = "";
//
//        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "mendian-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 日常");

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("store " + Md);

//        xd.login("yuexiu@test.com","f5b3e737510f31b88eb2d4b5d0cd2fb4");


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
     *
     * ====================添加事件(结束时间为开始时间&结束时间大于开始时间)======================
     * */
    @Test(dataProvider = "END_TIME_TYPE", dataProviderClass = storeScenarioUtil.class)
    public void thingAddT1(String endTimeType) {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //新增一个正常进行的添加事项
            String activity_description = "店庆店庆店庆店庆店庆";
            String activity_type = "a店庆";
            String start_date = "2020-07-10" ;
            String end_date = endTimeType;
            long shop_id = 23760;

            int code = Md.StoreActivityAdd(activity_description, activity_type, start_date, end_date, shop_id).getInteger("code");


            Preconditions.checkArgument(code == 1000,"添加事项不成功");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("添加事件(结束时间为开始时间&结束时间大于开始时间)");
        }

    }
    /**
     *
     * ====================添加事件（说明小于20个子&等于20个字）======================
     * */
    @Test(dataProvider = "DESCRIPTION", dataProviderClass = storeScenarioUtil.class)
    public void thingAddT2(String description) {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //新增一个正常进行的添加事项
            String activity_description = description;
            String activity_type = "a店庆";
            String start_date = "2020-07-09";
            String end_date = "2020-08-09";
            long shop_id = 23760;

            int code = Md.StoreActivityAdd(activity_description, activity_type, start_date, end_date, shop_id).getInteger("code");


            Preconditions.checkArgument(code == 1000,"添加事项不成功");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("添加事件（说明小于20个子&等于20个字）");
        }

    }
    /**
     *
     * ====================添加事件======================
     * */
    @Test
    public void thingAddF() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //新增一个正常进行的添加事项
            String activity_description = "店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆";//24字的说明
            String activity_type = "aa";
            String start_date = "2020-07-09";
            String end_date = "2020-08-09";
            long shop_id = 23760;

            int code = Md.StoreActivityAdd(activity_description, activity_type, start_date, end_date, shop_id).getInteger("code");


            Preconditions.checkArgument(code == 1000,"添加事项不成功");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("日均客流==所选时间段内的日均客流pv");
        }

    }

}
