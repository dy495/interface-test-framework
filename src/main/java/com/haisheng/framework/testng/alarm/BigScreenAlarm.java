package com.haisheng.framework.testng.alarm;

import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.ServiceChecklistRun;
import org.testng.annotations.Test;

public class BigScreenAlarm {

    ServiceChecklistRun checklistRun = new ServiceChecklistRun();
    int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    int YUEXIU_DAILY_CONF_ID  = ChecklistDbInfo.DB_SERVICE_ID_YUEXIU_SALES_OFFICE_DAILY_SERVICE;
    int YUEXIU_ONLINE_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_YUEXIU_SALES_OFFICE_ONLINE_SERVICE;


    @Test
    private void sendQAPush() {
        String hostPort = "http://192.168.50.2:7777";
        checklistRun.setHostPort(hostPort);


        String[] yuexiuDailyPassRate  = checklistRun.getPassRate(APP_ID, YUEXIU_DAILY_CONF_ID);
        String[] yuexiuOnlinePassRate = checklistRun.getPassRate(APP_ID, YUEXIU_ONLINE_CONF_ID);
        String[] bugInfo = checklistRun.getBugInfo(APP_ID);

        AlarmPush alarmPush = new AlarmPush();
        alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
        alarmPush.bigScreenAlarm(yuexiuOnlinePassRate, yuexiuDailyPassRate, bugInfo);

    }
}