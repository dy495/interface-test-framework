package com.haisheng.framework.testng.bigScreen.sumaryPush;

import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.ServiceChecklistRun;
import org.testng.annotations.Test;

public class CarOnlinePush {

    ServiceChecklistRun checklistRun = new ServiceChecklistRun();
    int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    int STORE_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;


    @Test
    public void sendQAPush() {
        String hostPort = "http://192.168.50.2:7777";
        checklistRun.setHostPort(hostPort);

        String[] passRate = checklistRun.getPassRate(APP_ID, STORE_CONF_ID);

        AlarmPush alarmPush = new AlarmPush();
        alarmPush.setDingWebhook(DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP);
        alarmPush.carAlarm(true, passRate);

    }
}
