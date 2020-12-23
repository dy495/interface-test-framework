package com.haisheng.framework.testng.managePlatform.manageToOutDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.testng.managePlatform.manageToOutDaily.util.ToOutUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

    //获取摄像头状态枚举
    public String getDevice_status() throws Exception{
        JSONArray list = dw.device_status().getJSONArray("list");
        String type ="";
        for(int i=0;i<list.size();i++){
            type =list.getJSONObject(i).getString("type");
        }
        return type;
    }

    //获取服务器状态枚举
    public String getCluster_status() throws Exception{
        JSONArray list = dw.cluster_status().getJSONArray("list");
        String type ="";
        for(int i=0;i<list.size();i++){
            type =list.getJSONObject(i).getString("type");
        }
        return type;
    }

    //获取服务器型号枚举
    public String getCluster_model() throws Exception{
        JSONArray list = dw.cluster_model().getJSONArray("list");
        String model ="";
        for(int i=0;i<list.size();i++){
            model =list.getJSONObject(i).getString("model");
        }
        return model;
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
    @Test(description = "[管理后台对外接口]查询摄像头设备列表,根据门店43072和150的设备ID调取设备列表")
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
            saveData("[管理后台对外接口]查询摄像头设备列表,根据门店43072和150的设备ID调取设备列表");
        }
    }
    @Test(description = "[管理后台对外接口]查询摄像头设备列表,根据门店43072和摄像头状态-调取设备列表")
    public void device_list2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Long> shop_ids = new ArrayList();
            shop_ids.add(43072L);
            String device_status = this.getDevice_status();
            JSONObject data = dw.device_seach(page, size, shop_ids, "",null, device_status);
            JSONArray list = data.getJSONArray("list");
            Integer total = data.getInteger("total");
            Preconditions.checkArgument(
                    total != null,
                    "根据门店43072的摄像头状态"+device_status+"调取设备列表,列表数量返回为"+total
            );
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("[管理后台对外接口]查询摄像头设备列表,根据门店43072和设备类型-调取设备列表");
        }
    }
    @Test(description = "[管理后台对外接口]查询摄像头设备列表,根据门店43072、和设备类型调取设备列表")
    public void device_list3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Long> shop_ids = new ArrayList();
            shop_ids.add(43072L);
            JSONObject data = dw.device_seach(page, size, shop_ids, "AI_CAMERA",null, null);
            JSONArray list = data.getJSONArray("list");
            int total = data.getInteger("total");
            Preconditions.checkArgument(
                    total != 0 ,
                    "根据门店43072和设备类型为AI摄像头取设备列表,列表数据返回为空"
            );
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("[管理后台对外接口]查询摄像头设备列表,根据门店43072和设备类型-调取设备列表");
        }
    }

    @Test(description = "[管理后台对外接口]查询服务器列表")
    public void cluster_list1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Long> shop_ids = new ArrayList();
            shop_ids.add(43072L);
            JSONObject data = dw.cluster_seach(page, size, shop_ids,"",null,"", "","");
            JSONArray list = data.getJSONArray("list");
            Integer total = data.getInteger("total");
            Preconditions.checkArgument(
                    total != null ,
                    "根据门店43072单个条件调取服务器列表,列表返回为空"
            );
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("[管理后台对外接口]查询服务器列表");
        }
    }

    @Test(description = "[管理后台对外接口]查询服务器列表,根据【门店43072】【设备状态】调取服务器列表")
    public void cluster_list2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Long> shop_ids = new ArrayList();
            shop_ids.add(43072L);
            String device_status = this.getCluster_status();
            JSONObject data = dw.cluster_seach(page, size, shop_ids,"",null,device_status, "","");
            JSONArray list = data.getJSONArray("list");
            Integer total = data.getInteger("total");
            Preconditions.checkArgument(
                    total != null ,
                    "根据【门店43072】【设备状态】调取服务器列表,列表返回为空"
            );
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("[管理后台对外接口]查询服务器列表,根据【门店43072】【设备状态】调取服务器列表");
        }
    }

    @Test(description = "[管理后台对外接口]查询服务器列表,根据【门店43072】【服务器型号】调取服务器列表")
    public void cluster_list3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Long> shop_ids = new ArrayList();
            shop_ids.add(43072L);
            String device_model = this.getCluster_model();
            JSONObject data = dw.cluster_seach(page, size, shop_ids,"",null,"", "",device_model);
            JSONArray list = data.getJSONArray("list");
            Integer total = data.getInteger("total");
            Preconditions.checkArgument(
                    total != null || !data.isEmpty(),
                    "根据【门店43072】【服务器型号】调取服务器列表,列表返回为空"
            );
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("[管理后台对外接口]查询服务器列表,根据【门店43072】【服务器型号】调取服务器列表");
        }
    }

    @Test(description = "[管理后台对外接口]查询门店信息列表,根据[应用ID]调取门店信息列表")
    public void subjec_list1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = dw.storeInfo_seach(page, size,"");
            JSONArray list = data.getJSONArray("list");
            Integer total = data.getInteger("total");
            Preconditions.checkArgument(
                    total != 0 || !data.isEmpty() || list.size() !=0,
                    "[管理后台对外接口]查询所有门店信息列表,列表返回为空"
            );
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("[管理后台对外接口]查询门店信息列表,根据[应用ID]调取门店信息列表");
        }
    }
    @Test(description = "[管理后台对外接口]查询门店信息列表,根据[应用ID]调取门店信息列表")
    public void subjec_list2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = dw.storeInfo_seach(page, size,"88590052b177");
            JSONArray list = data.getJSONArray("list");
            Integer total = data.getInteger("total");
            Preconditions.checkArgument(
                    total != 0 || !data.isEmpty() || list.size() !=0,
                    "[管理后台对外接口]查询门店信息列表,根据[保时捷门店应用ID]调取门店信息列表,列表返回为空"
            );
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("[管理后台对外接口]查询门店信息列表,根据[应用ID]调取门店信息列表");
        }
    }
    @Test(description = "[管理后台对外接口]查询门店信息列表,根据[应用ID]调取门店信息列表")
    public void subjec_list3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = dw.storeInfo_seach(page, size,"49998b971ea0");
            JSONArray list = data.getJSONArray("list");
            Integer total = data.getInteger("total");
            Preconditions.checkArgument(
                    total != 0 || !data.isEmpty() || list.size() !=0,
                    "[管理后台对外接口]查询门店信息列表,根据[门店应用ID]调取门店信息列表,列表返回为空"
            );
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("[管理后台对外接口]查询门店信息列表,根据[应用ID]调取门店信息列表");
        }
    }
    @Test(description = "[管理后台对外接口]查询门店信息列表,根据[应用ID]调取门店信息列表")
    public void subjec_list4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = dw.storeInfo_seach(page, size,"913076db53d7");
            JSONArray list = data.getJSONArray("list");
            Integer total = data.getInteger("total");
            Preconditions.checkArgument(
                    total != 0 || !data.isEmpty() || list.size() !=0,
                    "[管理后台对外接口]查询门店信息列表,根据[轿辰应用ID]调取门店信息列表,列表返回为空"
            );
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("[管理后台对外接口]查询门店信息列表,根据[应用ID]调取门店信息列表");
        }
    }

}
