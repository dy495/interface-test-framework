package com.haisheng.framework.testng.algorithm;

import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.ServiceChecklistRun;
import org.testng.annotations.Test;

public class AlgorithomCloudAlarmPush {
    ServiceChecklistRun checklistRun = new ServiceChecklistRun();
    int APP_ID = ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE;
    int BODY_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_BODY_SERVICE;
    int FACE_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_FACE_SERVICE;
    int CUST_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_CUSTOMER_DATA_SERVICE;
    int MENJIN_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_MENJIN_ALGORITHM_DAILY_SERVICE;




    @Test
    public void sendQAPush() {
        String hostPort = "http://192.168.50.2:7777";
        checklistRun.setHostPort(hostPort);

        String[] bodyPassRate = checklistRun.getPassRate(APP_ID, BODY_CONF_ID);
        String[] facePassRate = checklistRun.getPassRate(APP_ID, FACE_CONF_ID);
        String[] customerApiPassRate = checklistRun.getPassRate(APP_ID, CUST_CONF_ID);
        String[] menjinPassRate = checklistRun.getPassRate(APP_ID, MENJIN_CONF_ID);
        String[] cloudBugInfo = checklistRun.getBugInfo(APP_ID);

        AlarmPush alarmPush = new AlarmPush();
        alarmPush.setDingWebhook(DingWebhook.CLOUD_ALARM_GRP);
        alarmPush.cloudAlarm(bodyPassRate,
                facePassRate,
                customerApiPassRate,
                menjinPassRate,
                cloudBugInfo);

    }
}
