package com.haisheng.framework.testng.cronJob;

import com.haisheng.framework.model.bean.OnlineReqNum;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.testng.service.ApiRequest;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.QADbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class OnlineRequestMonitor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private QADbUtil qaDbUtil = new QADbUtil();
    private DateTimeUtil dt   = new DateTimeUtil();
    private String TODAY  = dt.getHistoryDate(0);
    private String HOUR   = dt.getCurrentHour();
    private final float HOUR_DIFF_RANGE = 0.3f;
    private final float DAY_DIFF_RANGE  = 0.1f;

    private boolean DEBUG = false;



    @Test
    private void requestNumberMonitor() {
        if (HOUR.length() < 2) {
            HOUR = "0" + HOUR;
        }

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

        String yesterday = dt.getHistoryDate(-1);
        List<DataUnit> dataList = new LinkedList<>();
        List<String> zeroReqdataList = new LinkedList<>();

        //get today and yesterday data from db
        getDataFromDb(TODAY, yesterday, dataList, zeroReqdataList);

        //check today data
        checkData(dataList, zeroReqdataList);
    }

    private void getDataFromDb(String today, String history, List<DataUnit> dataList, List<String> zeroReqdataList) {
        List<String> deviceList = qaDbUtil.selectOnlineReqDeviceList(history);

        List<OnlineReqNum> todayData = qaDbUtil.selectOnlineReqNumByDate(today, HOUR);
        List<OnlineReqNum> historyData = qaDbUtil.selectOnlineReqNumByDate(history, HOUR);

        ConcurrentHashMap<String, Integer> deviceHm = new ConcurrentHashMap<>();
        for (String device : deviceList) {
            deviceHm.put(device, 1);
        }

        for (OnlineReqNum onlineReqNumToday : todayData) {
            Integer reqNumToday = onlineReqNumToday.getReqNum();
            if (null != reqNumToday && reqNumToday.intValue() > 0) {
                DataUnit dataUnit = new DataUnit();
                dataUnit.deviceId = onlineReqNumToday.getDeviceId();
                dataUnit.today = reqNumToday.intValue();

                //get history request num
                for (OnlineReqNum onlineReqNumHistory : historyData) {
                    if (onlineReqNumHistory.getDeviceId().equals(dataUnit.deviceId)) {
                        Integer reqNumHistory = onlineReqNumHistory.getReqNum();
                        if (null != reqNumHistory && reqNumHistory.intValue() > 0) {
                            dataUnit.history = reqNumHistory.intValue();
                        }
                    }
                }

                dataList.add(dataUnit);
                //remove not zero request device
                if (deviceHm.containsKey(dataUnit.deviceId)) {
                    deviceHm.remove(dataUnit.deviceId);
                }
            }
        }

        for (String zeroReqDevice : deviceHm.keySet()) {
            zeroReqdataList.add(zeroReqDevice);
        }

    }

    private void checkData(List<DataUnit> dataList, List<String> zeroReqDataList) {

        String dingMsg = "";
        for (DataUnit dataUnit : dataList) {
            String deviceId = dataUnit.deviceId;
            //calc diff
            String result = checkDiff(dataUnit);
            if (! StringUtils.isEmpty(result)) {
                dingMsg += result;
            }
            //save diff data
            saveDiffData(deviceId, TODAY, HOUR, dataUnit);
        }

        String zeroReqDingMsg = "";
        if (HOUR.equals("all")) {
            zeroReqDingMsg = "以下" + zeroReqDataList.size() + "个设备今日【全天】请求数为 0 \n";
        } else {
            zeroReqDingMsg = "以下" + zeroReqDataList.size() + "个设备今日【上个小时】请求数为 0 \n";
        }
        for (String deviceId : zeroReqDataList) {
            zeroReqDingMsg += deviceId + " \n";
        }

        //push dingding msg
        if (zeroReqDataList.size() > 0) {
            dingPushWithoutAssert(zeroReqDingMsg);
        }
        pushDiffMsg(dingMsg);
    }

    private String checkDiff(DataUnit dataUnit) {
        String dingMsg = "";
        dataUnit.diffValue = dataUnit.today - dataUnit.history;

        //数据为0，直接报警
        if (0 == dataUnit.today) {
            dingMsg = dataUnit.deviceId + "-请求数据异常: 截止当前时间数据量为 0";
            //数据缩水100%
            dataUnit.diffRange = -1;
        } else {
            if (0 == dataUnit.history) {
                //数据增长100%
                dataUnit.diffRange = 1;
            } else {
                dataUnit.diffRange = (float) dataUnit.diffValue / (float) dataUnit.history;
            }
            DecimalFormat df = new DecimalFormat("#.00");
            if (dataUnit.diffValue > 0) {
                float enlarge  = dataUnit.diffRange;
                String percent = df.format(enlarge*100) + "%";

                if (HOUR.equals("all")) {
                    if (enlarge > DAY_DIFF_RANGE) {
                        dingMsg = dataUnit.deviceId + "-请求数据异常: 今日较昨日【全天数据量】扩大 " + percent;
                    }
                } else {
                    if (enlarge > HOUR_DIFF_RANGE) {
                        dingMsg = dataUnit.deviceId + "-请求数据异常: 较【昨日同时段】数据量扩大 " + percent;
                    }
                }
            } else if (dataUnit.diffValue < 0) {
                float shrink = dataUnit.diffRange * (-1);
                String percent = df.format(shrink*100) + "%";

                if (HOUR.equals("all")) {
                    if (shrink > DAY_DIFF_RANGE) {
                        dingMsg = dataUnit.deviceId + "-请求数据异常: 今日较昨日【全天数据量】缩小 " + percent;
                    }
                } else {
                    if (shrink > HOUR_DIFF_RANGE) {
                        dingMsg = dataUnit.deviceId + "-请求数据异常: 较【昨日同时段】数据量缩小 " + percent;
                    }
                }
            }

        }


        //过滤掉8点前的数据，商场8点前人少，波动较剧烈
        if (!HOUR.equals("all")) {
            int intHour = Integer.parseInt(HOUR);
            if (intHour < 9) {
                //放弃该时段干扰指标大盘其他时段数据展示
                dataUnit.diffRange = 0f;
                return "";
            }
        }

        if (! StringUtils.isEmpty(dingMsg)) {
            dingMsg += "\n\n"
                    + "current: " + dataUnit.today + "\n"
                    + "history: " + dataUnit.history + "\n"
                    + "diff: " + dataUnit.diffValue + "\n\n\n\n";
            dataUnit.alarm = 1;
        }
        return dingMsg;
    }

    private void saveDiffData(String deviceId, String date, String hour, DataUnit dataUnit) {
        OnlineReqNum onlineReqNum = new OnlineReqNum();
        onlineReqNum.setDeviceId(deviceId);
        onlineReqNum.setDate(date);
        onlineReqNum.setHour(hour);
        onlineReqNum.setAlarm(dataUnit.alarm);
        onlineReqNum.setDiffReqNumHourDay(dataUnit.diffValue);
        onlineReqNum.setDiffReqNumRangeHourDay(dataUnit.diffRange);

        qaDbUtil.updateOnlineReqNumDiff(onlineReqNum);
    }

    private void pushDiffMsg(String dingMsg) {
        if (! StringUtils.isEmpty(dingMsg)) {
            dingPush(dingMsg);
        }
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

    private void dingPushWithoutAssert(String msg) {
        logger.error(msg);
        AlarmPush alarmPush = new AlarmPush();

        if (DEBUG) {
            alarmPush.setDingWebhook(DingWebhook.AD_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.PV_UV_ACCURACY_GRP);
        }

        alarmPush.onlineMonitorPvuvAlarm(msg);
    }

    class DataUnit {
        String deviceId;
        int history = 0;
        int today = 0;
        int diffValue    = 0;
        float diffRange  = 0f;
        int alarm = 0;
    }

}
