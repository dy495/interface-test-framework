package com.haisheng.framework.testng.algorithm;


import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;

import com.haisheng.framework.util.ServiceChecklistRun;
import org.testng.annotations.Test;


public class ChecklistRun {

    ServiceChecklistRun checklistRun = new ServiceChecklistRun();
    int APP_ID_AG_CLOUD   = ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE;
    int BODY_CONF_ID      = ChecklistDbInfo.DB_SERVICE_ID_BODY_SERVICE;
    int FACE_CONF_ID      = ChecklistDbInfo.DB_SERVICE_ID_FACE_SERVICE;
    int CUST_CONF_ID      = ChecklistDbInfo.DB_SERVICE_ID_CUSTOMER_DATA_SERVICE;
    int MENJIN_AG_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_MENJIN_ALGORITHM_DAILY_SERVICE;
    int AI_LIVING_AREA_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_AI_LIVING_AREA_DAILY_SERVICE;

    int APP_ID_BIG_SCREEN = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    int MENJIN_BE_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_MENJIN_BE_DAILY_SERVICE;
    int CRM_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
    int MENDIAN_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;


    @Test
    public void runTest() {
        String hostPort = "http://192.168.50.2:7777";
        checklistRun.setHostPort(hostPort);

        checklistRun.runChecklist(APP_ID_AG_CLOUD, BODY_CONF_ID);
        checklistRun.runChecklist(APP_ID_AG_CLOUD, FACE_CONF_ID);
        checklistRun.runChecklist(APP_ID_AG_CLOUD, CUST_CONF_ID);
        checklistRun.runChecklist(APP_ID_AG_CLOUD, MENJIN_AG_CONF_ID);
        checklistRun.runChecklist(APP_ID_BIG_SCREEN, MENJIN_BE_CONF_ID);
        checklistRun.runChecklist(APP_ID_BIG_SCREEN, AI_LIVING_AREA_CONF_ID);
        checklistRun.runChecklist(APP_ID_BIG_SCREEN, CRM_CONF_ID);
        checklistRun.runChecklist(APP_ID_BIG_SCREEN, MENDIAN_CONF_ID);
    }

}
