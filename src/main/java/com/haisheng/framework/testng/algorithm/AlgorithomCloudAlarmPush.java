package com.haisheng.framework.testng.algorithm;

import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.ServiceChecklistRun;
import org.testng.annotations.Test;

public class AlgorithomCloudAlarmPush {
    ServiceChecklistRun checklistRun = new ServiceChecklistRun();
    int APP_ID = ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE;
    int BODY_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_BODY_SERVICE;
    int FACE_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_FACE_SERVICE;
    int CUST_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_CUSTOMER_DATA_SERVICE;




    @Test
    public void sendQAPush() {
        String[] bodyPassRate = checklistRun.getPassRate(APP_ID, BODY_CONF_ID);
        String[] facePassRate = checklistRun.getPassRate(APP_ID, FACE_CONF_ID);
        String[] customerApiPassRate = checklistRun.getPassRate(APP_ID, CUST_CONF_ID);
        String[] cloudBugInfo = checklistRun.getBugInfo(APP_ID);

        AlarmPush alarmPush = new AlarmPush();
        alarmPush.setDingWebhook(DingWebhook.CLOUD_ALARM_GRP);
        alarmPush.cloudAlarm(bodyPassRate,
                facePassRate,
                customerApiPassRate,
                cloudBugInfo);

    }
}
