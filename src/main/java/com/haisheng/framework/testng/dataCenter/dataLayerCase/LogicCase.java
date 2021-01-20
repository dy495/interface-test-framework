package com.haisheng.framework.testng.dataCenter.dataLayerCase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.testng.dataCenter.interfaceUtil.DataLayerUtil;
import com.haisheng.framework.testng.dataCenter.interfaceUtil.LogicLayerUtil;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
;import static com.google.common.base.Preconditions.checkArgument;
/**
 * @author : qingqing
 * @date :  2020/07/06
 */
public class LogicCase extends TestCaseCommon implements TestCaseStd {
    LogicLayerUtil logic = LogicLayerUtil.getInstance();
    String request_id = "8b21f20d-6af6-43ff-8fd3-4251e9";

    String face_url = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=381876729,1649964117&fm=26&gp=0.jpg";


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CUSTOMER_DATA_SERVICE;
        commonConfig.checklistQaOwner = "青青";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "data_center");
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "逻辑层接口 日常");
        // commonConfig.dingHook = DingWebhook.APP_DATA_LAYER_ALARM_GRP;
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"18810332354", "15084928847"};
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);
        logger.debug("dataCenter " + logic);
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

    /**
     * ====================会员注册(正确入参&格式),根据注册接口的参数，在查询接口进行查询是否新建成功======================
     */
    @Test
    public void member_register() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray array = new JSONArray();
            array.add(22728);
            JSONObject obj = new JSONObject();
            obj.put("22728","7cfa3fe9-96c4-4e65-960b-44edc3f18b68");
            JSONObject shop_user = new JSONObject();
            shop_user.put("array",obj);
           // logic.special_register("autotester","1112223333",shop_user,);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员注册(正确入参&格式)");
        }
    }



}

