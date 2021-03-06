package com.haisheng.framework.testng.managePlatform.manageOnline;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.haisheng.framework.model.bean.OnlineScopeDevice;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.testng.managePlatform.manageOnline.utilOnline.ManageUtilOnline;
import com.haisheng.framework.util.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StoreDeviceData extends TestCaseCommon implements TestCaseStd {
    Integer page = 1;
    Integer size = 50;

    ManageUtilOnline store = ManageUtilOnline.getInstance();


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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "管理后台-统计线上店铺和设备掉线监控");
        commonConfig.pushRd = new String[]{"15084928847"};
        commonConfig.setReferer("http://39.106.253.190/cms/login");
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


    @Test(description = "管理后台线上，统计在用的产品")
    public void deviceList() {
        logger.logCaseStart(caseResult.getCaseName());
        List<OnlineScopeDevice> onlineScopeDeviceList = new LinkedList<>();
        try {
            int total = store.store_manage(page, size, null, null, null, null, null).getInteger("total");
            int t = CommonUtil.getTurningPage(total, 50);
            for (int l = 1; l < t; l++) {
                JSONObject res = store.store_manage(l, size, null, null, null, null, null);
                JSONArray list = res.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    Long subject_id = list.getJSONObject(i).getLong("subject_id");
                    OnlineScopeDevice onlineScopeDevice = new OnlineScopeDevice();
                    onlineScopeDeviceList.add(onlineScopeDevice);
                    onlineScopeDevice.setScopeId(subject_id);
//                    List<OnlineScopeDevice> deviceMonitorUnitList = new ArrayList<OnlineScopeDevice>();
//                    List<OnlineScopeDevice> deviceMonitorUnitErrorList = new ArrayList<OnlineScopeDevice>();
                    if (subject_id != null) {
                        JSONObject response = store.decice_manage(subject_id);
                        JSONArray device_list = response.getJSONArray("list");
                        String deviceId;
                        String deviceStatus;
                        String scopeName;
                        String deviceName;
                        String deviceType;
                        Timestamp deviceCreateTime;
                        if (device_list.size() > 0) {
                            for (int j = 0; j < device_list.size(); j++) {
                                deviceId = device_list.getJSONObject(j).getString("device_id");
                                deviceStatus = device_list.getJSONObject(j).getString("status_name");
                                scopeName = device_list.getJSONObject(j).getString("subject_name");
                                deviceName = device_list.getJSONObject(j).getString("name");
                                deviceType = device_list.getJSONObject(j).getString("type_name");
                                deviceCreateTime = device_list.getJSONObject(j).getTimestamp("gmt_create");

                                onlineScopeDevice.setDeviceId(deviceId);
                                onlineScopeDevice.setDeviceStatus(deviceStatus);
                                onlineScopeDevice.setScopeName(scopeName);
                                onlineScopeDevice.setDeviceName(deviceName);
                                onlineScopeDevice.setDeviceType(deviceType);
                                onlineScopeDevice.setDeviceCreateTime(deviceCreateTime);
//                                if(status_name.equals("掉线")){
//                                    String finalDevice_id = device_id;
//                                    deviceMonitorUnitErrorHm.compute(String.valueOf(subject_id), (k, v)->{
//                                        OnlineScopeDevice monitorUnit = new OnlineScopeDevice();
//                                        monitorUnit.setDeviceId(finalDevice_id);
//                                        if (CollectionUtils.isNotEmpty(v)){
//                                            //如果list集合不为空加入新的id
//                                            v.add(monitorUnit);
//                                        }else {
//                                            //如果为空，那么传一个新的list加入
//                                            v = Lists.newArrayList(monitorUnit);
//                                        }
//                                        return v;
//                                    });
//                                }
//                                OnlineScopeDevice deviceMonitorUnit = new OnlineScopeDevice();
//                                deviceMonitorUnit.setDeviceId(device_id);
//                                deviceMonitorUnitList.add(deviceMonitorUnit);
//                                deviceMonitorUnitHm.put(String.valueOf(subject_id), deviceMonitorUnitList);
                            }
//                            logger.info(JSONObject.toJSONString(deviceMonitorUnitErrorHm));
                        }
                    }
                }
            }
            //Preconditions.checkArgument(status_name.equals("运行中") , "线上共计有："+total+"个门店,"+"门店:"+subject_name+"的设备ID为："+device_id+"的设备运行状态为："+status_name);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("线上一共有多少个门店，门店下的设备运行情况");
            qaDbUtil.saveDeviceInfo(onlineScopeDeviceList);
        }
    }

}
