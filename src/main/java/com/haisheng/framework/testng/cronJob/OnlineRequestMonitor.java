package com.haisheng.framework.testng.cronJob;

import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.QADbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

public class OnlineRequestMonitor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private QADbUtil qaDbUtil = new QADbUtil();
    private DateTimeUtil dt   = new DateTimeUtil();
    private String HOUR   = dt.getCurrentHour();
    private boolean DEBUG = true;

    @Test
    private void requestNumberMonitor() {

        if (HOUR.equals("23")) {
            requestNumberCheck();
            HOUR = "all";
        }
        requestNumberCheck();
    }

    @BeforeSuite
    public void login() {
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }

    private void requestNumberCheck() {
        String today = dt.getHistoryDate(0);
        String yesterday = dt.getHistoryDate(-1);
        List<DataUnit> dataList = new LinkedList<>();

        //get today data from db
        getDataFromDb(today, dataList);

        //get yesterday data from db
        getDataFromDb(yesterday, dataList);

        //check today data
        checkData(dataList);
    }
    private void getDataFromDb(String date, List<DataUnit> dataList) {
        List<String> deviceList = qaDbUtil.selectOnlineReqDeviceList(date);
        for (String device : deviceList) {
            DataUnit dataUnit = new DataUnit();
            dataUnit.deviceId = device;
            int reqNum = qaDbUtil.selectOnlineReqNum(device, date, HOUR);
            String today = dt.getHistoryDate(0);
            if (reqNum > 0) {
                if (date.equals(today)) {
                    dataUnit.today = reqNum;
                } else {
                    dataUnit.history = reqNum;
                }
            }
            dataList.add(dataUnit);
        }
    }

    private void checkData(List<DataUnit> dataList) {

    }

    private void dingPush(String msg) {
        logger.error(msg);
        AlarmPush alarmPush = new AlarmPush();

        if (DEBUG) {
            alarmPush.setDingWebhook(DingWebhook.AD_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.PV_UV_ACCURACY_GRP);
        }

        alarmPush.onlineMonitorPvuvAlarm(msg);
        Assert.assertTrue(false);

    }

    class DataUnit {
        String deviceId;
        int history = 0;
        int today = 0;
    }

}
