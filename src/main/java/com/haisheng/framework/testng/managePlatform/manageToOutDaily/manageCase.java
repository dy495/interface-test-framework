package com.haisheng.framework.testng.managePlatform.manageToOutDaily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.google.inject.internal.cglib.core.$LocalVariablesSorter;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.testng.managePlatform.manageToOutDaily.util.ToOutUtil;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 有问题处请@wangmin
 */
public class manageCase extends TestCaseCommon implements TestCaseStd {
    Integer page = 1;
    Integer size = 50;
    Long device_id = 8049875851805696l;//43072门店的150设备

    ToOutUtil dw = ToOutUtil.getInstance();


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_MANAGE_PORTAL_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MANAGEMENT_PLATFORM_SERVICE;
        commonConfig.checklistQaOwner = "QQ";
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "管理后台对外接口 日常");
        commonConfig.pushRd = new String[]{"15084928847"};
        commonConfig.referer = "http://39.106.253.190/cms/login";
        beforeClassInit(commonConfig);

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


    @Test(description = "[管理后台对外接口]查询摄像头设备列表,根据门店43072单个条件调取设备列表")
    public void device_list() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Long> shop_ids = new ArrayList();
            shop_ids.add(43072L);
            JSONObject data = dw.device_seach(page, size, shop_ids, "", null, null);
            JSONArray list = data.getJSONArray("list");
            int total = data.getInteger("total");
            Preconditions.checkArgument(
                    total != 0 || list.size() != 0,
                    "根据门店43072单个条件调取设备列表,列表返回为空"
            );
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("管理后台对外接口]查询摄像头设备列表,根据门店43072单个条件调取设备列表");
        }
    }
    @Test(description = "[管理后台对外接口]查询摄像头设备列表,根据门店43072、和150的设备ID调取设备列表")
    public void device_list1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Long> shop_ids = new ArrayList();
            shop_ids.add(43072L);
            JSONObject data = dw.device_seach(page, size, shop_ids, "", device_id, null);
            JSONArray list = data.getJSONArray("list");
            int total = data.getInteger("total");
            Preconditions.checkArgument(
                    total != 0 || list.size() != 0,
                    "根据门店43072单个条件调取设备列表,列表返回为空"
            );
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("[管理后台对外接口]查询摄像头设备列表,根据门店43072、和150的设备ID调取设备列表");
        }
    }
    @Test(description = "[管理后台对外接口]查询摄像头设备列表,根据门店43072、和150的设备ID调取设备列表")
    public void device_list2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Long> shop_ids = new ArrayList();
            shop_ids.add(43072L);
            JSONObject data = dw.device_seach(page, size, shop_ids, "", device_id, null);
            JSONArray list = data.getJSONArray("list");
            int total = data.getInteger("total");
            Preconditions.checkArgument(
                    total != 0 || list.size() != 0,
                    "根据门店43072单个条件调取设备列表,列表返回为空"
            );
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("[管理后台对外接口]查询摄像头设备列表,根据门店43072、和150的设备ID调取设备列表");
        }
    }


}
