package com.haisheng.framework.testng.alarm;

import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.ServiceChecklistRun;
import org.testng.annotations.Test;

public class CommodityShelfAlarm {
    ServiceChecklistRun checklistRun = new ServiceChecklistRun();
    int APP_ID = ChecklistDbInfo.DB_APP_ID_SHELF_SERVICE;
    int SHELF_CONF_ID = ChecklistDbInfo.DB_SERVICE_ID_SHELF_SERVICE;


    @Test
    private void sendQAPush() {
        String hostPort = "http://192.168.50.2:7777";
        checklistRun.setHostPort(hostPort);


        String[] shelfPassRate = checklistRun.getPassRate(APP_ID, SHELF_CONF_ID);
        String[] bugInfo = checklistRun.getBugInfo(APP_ID);

        AlarmPush alarmPush = new AlarmPush();
        alarmPush.setDingWebhook(DingWebhook.COMMODITY_SHELF_GRP);
        alarmPush.shelfAlarm(shelfPassRate, bugInfo);

    }
}
