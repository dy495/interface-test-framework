package com.haisheng.framework.testng.alarm;

import com.haisheng.framework.util.AlarmPush;
import org.testng.annotations.Test;

public class AlarmJenkinsIpChange {

    private final String DEBUG = System.getProperty("DEBUG", "true");
    private final String IP = System.getProperty("IP", "");


    @Test
    private void alarmIpChange() {
        if (DEBUG.contains("false")) {
            AlarmPush alarmPush = new AlarmPush();
            alarmPush.ipChangeAlarm(IP);
        }
    }

}
