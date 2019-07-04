package com.haisheng.framework.testng.appCloud;

import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.ServiceChecklistRun;
import org.testng.annotations.Test;

public class AppCloudTest {

    ServiceChecklistRun serviceChecklistRun = new ServiceChecklistRun();
    int APP_PLATFORM_ID = ChecklistDbInfo.DB_APP_ID_OPEN_PLATFORM_SERVICE;
    int APP_SCREEN_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    int APP_SHELF_ID = ChecklistDbInfo.DB_APP_ID_SHELF_SERVICE;


    @Test
    public void runTest() {
        String hostPort = "http://192.168.50.2:7777";
        serviceChecklistRun.setHostPort(hostPort);
        sendQAPush();
    }

    private void sendQAPush() {

        String[] platformBugInfo = serviceChecklistRun.getBugInfo(APP_PLATFORM_ID);
        String[] screenBugInfo = serviceChecklistRun.getBugInfo(APP_SCREEN_ID);
        String[] shelfBugInfo = serviceChecklistRun.getBugInfo(APP_SHELF_ID);

        AlarmPush alarmPush = new AlarmPush();
        alarmPush.setDingWebhook(DingWebhook.APP_CLOUD_ALARM_GRP);
//        alarmPush.setDingWebhook(DingWebhook.AD_GRP);
        alarmPush.appCloudAlarm(platformBugInfo, screenBugInfo, shelfBugInfo);

    }
}
