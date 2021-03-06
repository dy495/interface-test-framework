package com.haisheng.framework.testng.alarm;

import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.ServiceChecklistRun;
import org.testng.annotations.Test;

public class OpenPlatformAlarm {

    ServiceChecklistRun checklistRun = new ServiceChecklistRun();
    int APP_ID = ChecklistDbInfo.DB_APP_ID_OPEN_PLATFORM_SERVICE;
    int AD_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_AD_SERVICE;
    int CONSOLE_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_CONTROL_CENTER_SERVICE;
    int OPERATION_CENTER = ChecklistDbInfo.DB_SERVICE_ID_OPERATION_CENTER;


    @Test
    private void sendQAPush() {
        String hostPort = "http://192.168.50.2:7777";
        checklistRun.setHostPort(hostPort);


        String[] adPassRate = checklistRun.getPassRate(APP_ID, AD_CONF_ID);
        String[] consolePassRate = checklistRun.getPassRate(APP_ID, CONSOLE_CONF_ID);
        String[] dmpPassRate = checklistRun.getPassRate(APP_ID, OPERATION_CENTER);
        String[] bugInfo = checklistRun.getBugInfo(APP_ID);

        AlarmPush alarmPush = new AlarmPush();
        alarmPush.setDingWebhook(DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP);
        alarmPush.openPlatformAlarm(adPassRate, consolePassRate, dmpPassRate, bugInfo);

    }
}
