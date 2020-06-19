package com.haisheng.framework.testng.cronJob;

import com.haisheng.framework.model.bean.OnlineReqNum;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.QADbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
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
    private final float HOUR_DIFF_RANGE = 0.5f;
    private final float DAY_DIFF_RANGE  = 0.1f;

    private boolean DEBUG = false;
    final String RISK_MAX = "高危险报警";
    //18210113587 于
    //15898182672 华成裕
    //18810332354 刘峤
    //18600514081 段
    final String AT_USERS = "请 @18210113587 @15898182672 @18810332354 @18600514081 关注";
    final String[] USER_LIST = {"18210113587", "15898182672", "18810332354", "18600514081"};


    @Test
    public void requestNumberMonitor() {
        if (HOUR.length() < 2) {
            HOUR = "0" + HOUR;
        }

        if (HOUR.equals("23")) {
            requestNumberCheck();
            HOUR = "ALL";
        }
        requestNumberCheck();
    }

    @BeforeSuite
    public void initial() {
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }

    private void requestNumberCheck() {

        String yesterday = dt.getHistoryDate(-1);
        List<DataUnit> dataList = new LinkedList<>();
        List<DataUnit> zeroReqdataList = new LinkedList<>();

        //get today and yesterday data from db
        getDataFromDb(TODAY, yesterday, dataList, zeroReqdataList);

        //check today data
        checkData(dataList, zeroReqdataList);
    }

    private void getDataFromDb(String today, String history, List<DataUnit> dataList, List<DataUnit> zeroReqdataList) {
        List<String> deviceList = qaDbUtil.selectOnlineReqDeviceList(history);

        List<OnlineReqNum> todayData = qaDbUtil.selectOnlineReqNumByDate(today, HOUR);
        List<OnlineReqNum> historyData = qaDbUtil.selectOnlineReqNumByDate(history, HOUR);

        if (CollectionUtils.isEmpty(todayData)) {
            Assert.fail(today + " " + HOUR + ", 数据库中没有数据");
        }

        ConcurrentHashMap<String, Integer> deviceHm = new ConcurrentHashMap<>();
        for (String device : deviceList) {
            deviceHm.put(device, 1);
        }

        for (OnlineReqNum onlineReqNumToday : todayData) {
            Integer reqNumToday = onlineReqNumToday.getReqNum();
            if (null != reqNumToday && reqNumToday.intValue() > 0) {
                DataUnit dataUnit = new DataUnit();
                dataUnit.deviceId = onlineReqNumToday.getDeviceId();
                dataUnit.deviceName = onlineReqNumToday.getDeviceName();
                dataUnit.shopName = onlineReqNumToday.getShopName();
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
            } else {
                //今日为0的设备添加到zeroReqdataList
                DataUnit dataUnit = new DataUnit();
                dataUnit.deviceName = onlineReqNumToday.getDeviceName();
                dataUnit.deviceId = onlineReqNumToday.getDeviceId();
                dataUnit.shopName = onlineReqNumToday.getShopName();
                zeroReqdataList.add(dataUnit);
            }
        }

        for (String zeroReqDevice : deviceHm.keySet()) {
            for (OnlineReqNum onlineReqNum : historyData) {
                //历史有device但今日没有的device设备添加到zeroReqdataList
                if (zeroReqDevice.equals(onlineReqNum.getDeviceId())) {
                    DataUnit dataUnit = new DataUnit();
                    dataUnit.deviceName = onlineReqNum.getDeviceName();
                    dataUnit.deviceId = onlineReqNum.getDeviceId();
                    dataUnit.shopName = onlineReqNum.getShopName();
                    zeroReqdataList.add(dataUnit);
                }
            }
        }

    }

    private ConcurrentHashMap<String, List<DataUnit>> sortDataByShop(List<DataUnit> dataList) {
        ConcurrentHashMap<String, List<DataUnit>> hm = new ConcurrentHashMap<>();

        for (DataUnit dataUnit : dataList) {
            String shop = dataUnit.shopName;
            if (StringUtils.isEmpty(shop)) {
                shop = "历史数据缺省shop";
            }
            if (hm.containsKey(shop)) {
                List<DataUnit> list = hm.get(shop);
                list.add(dataUnit);
            } else {
                List<DataUnit> list = new LinkedList<>();
                list.add(dataUnit);
                hm.put(shop, list);
            }
        }

        return hm;
    }


    private void checkData(List<DataUnit> dataList, List<DataUnit> zeroReqDataList) {

        String hourRange = "";
        if (HOUR.toLowerCase().equals("all")) {
            hourRange = "00:00 ~ 23:59";
        } else {
            int lastHour = Integer.parseInt(HOUR) - 1;
            hourRange = lastHour + ":00 ~ " + HOUR + ":00";
        }

        ConcurrentHashMap<String, List<DataUnit>> diffDataHm = sortDataByShop(dataList);
        ConcurrentHashMap<String, List<DataUnit>> zeroDataHm = sortDataByShop(zeroReqDataList);


        String dingDiffMsg = "";
        String dingZeroMsg = "";
        boolean isDiff = false;
        boolean isZero = false;

        for (String shop : diffDataHm.keySet()) {
            String shopInfo = "\n#### " + shop + " \n";
            dingDiffMsg += shopInfo;
            boolean isShopDiff = false;
            for (DataUnit dataUnit : diffDataHm.get(shop)) {
                String deviceId = dataUnit.deviceId;
                //calc diff
                String result = checkDiff(dataUnit);
                if (! StringUtils.isEmpty(result)) {
                    dingDiffMsg += "###### " + result;
                    isShopDiff = true;
                    isDiff = true;
                }
                //save diff data
                saveDiffData(deviceId, TODAY, HOUR, dataUnit);
            }
            if (! isShopDiff) {
                //current data no diff, remove the shop info
                dingDiffMsg = dingDiffMsg.replace(shopInfo, "");
            }
        }

        for (String shop : zeroDataHm.keySet()) {
            dingZeroMsg += "\n" + shop + " 【" + hourRange + "】该时段，以下" + zeroDataHm.get(shop).size() + "个设备请求数为0 \n###### ";;
            for (DataUnit dataUnit : zeroDataHm.get(shop)) {
                String result = checkZero(dataUnit);
                if (! StringUtils.isEmpty(result)) {
                    dingZeroMsg += result;
                    isZero = true;
                }
            }
        }

        //push dingding msg
        if (isZero) {
            dingPushWithoutAssert(dingZeroMsg);
        }
        //push diff msg
        if (isDiff) {
            pushDiffMsg(dingDiffMsg);
        }
    }

    private String checkDiff(DataUnit dataUnit) {

        String hourRange = "";
        if (HOUR.toLowerCase().equals("all")) {
            hourRange = "00:00 ~ 23:59";
        } else {
            int lastHour = Integer.parseInt(HOUR) - 1;
            hourRange = lastHour + ":00 ~ " + HOUR + ":00";
        }

        String dingMsg = "";
        dataUnit.diffValue = dataUnit.today - dataUnit.history;

        //数据为0，直接报警
        if (0 == dataUnit.today) {
            dingMsg = dataUnit.deviceId + "(" + dataUnit.deviceName + ")" + "-请求数据异常: 【" + hourRange + "】该时段数据量为 0，"
                    + RISK_MAX
                    + ", " + AT_USERS;;
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

                if (HOUR.equals("ALL")) {
                    if (enlarge > DAY_DIFF_RANGE) {
                        dingMsg = dataUnit.deviceId + "(" + dataUnit.deviceName + ")" + "-请求数据异常: 今日较昨日【全天数据量】扩大 " + percent;
                    }
                } else {
                    if (enlarge > HOUR_DIFF_RANGE) {
                        dingMsg = dataUnit.deviceId + "(" + dataUnit.deviceName + ")" + "-请求数据异常: 较【昨日同时段】【" + hourRange + "】数据量扩大 " + percent;
                    }
                }
            } else if (dataUnit.diffValue < 0) {
                float shrink = dataUnit.diffRange * (-1);
                String percent = df.format(shrink*100) + "%";

                if (HOUR.equals("ALL")) {
                    if (shrink > DAY_DIFF_RANGE) {
                        dingMsg = dataUnit.deviceId + "(" + dataUnit.deviceName + ")" + "-请求数据异常: 今日较昨日【全天数据量】缩小 " + percent;
                    }
                } else {
                    if (shrink > HOUR_DIFF_RANGE) {
                        dingMsg = dataUnit.deviceId + "(" + dataUnit.deviceName + ")" + "-请求数据异常: 较【昨日同时段】【" + hourRange + "】数据量缩小 " + percent;
                    }
                }
            }

        }



        if (! HOUR.equals("ALL")) {

            //过滤掉8点前的数据，商场8点前人少，波动较剧烈
            int intHour = Integer.parseInt(HOUR);
            if (intHour <= 8 || intHour >= 22) {
                //放弃该时段干扰指标大盘其他时段数据展示
                dataUnit.diffRange = 0f;
                return "";
            }

            //100请求量以下的只做天级报警
            if (dataUnit.today < 100) {
                logger.info(dataUnit.today + "< 100, " + HOUR + "时数据 do NOT check");
                return "";
            }

            //昨天为0的，过滤掉
            if (0 == dataUnit.history) {
                return "";
            }
        }

        if (! StringUtils.isEmpty(dingMsg)) {
            dingMsg += "\n"
                    + "current: " + dataUnit.today + " "
                    + "history: " + dataUnit.history + " "
                    + "diff: " + dataUnit.diffValue + "\n";
            dataUnit.alarm = 1;
        }
        return dingMsg;
    }

    private String checkZero(DataUnit dataUnit) {

        String dingMsg = "";

        if (StringUtils.isEmpty(dataUnit.deviceName)) {
            dingMsg = dataUnit.deviceId + " ";
        } else {

            dingMsg = dataUnit.deviceId + "(" + dataUnit.deviceName + ") ";
        }

        if (! HOUR.equals("ALL")) {

            //过滤掉8点前的数据，商场8点前人少，波动较剧烈
            int intHour = Integer.parseInt(HOUR);
            if (intHour < 9 || intHour > 21) {
                //放弃该时段干扰指标大盘其他时段数据展示
                return "";
            }
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
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.PV_UV_ACCURACY_GRP);
        }

        if (msg.contains(RISK_MAX) && !DEBUG) {
            alarmPush.onlineMonitorPvuvAlarm(msg, USER_LIST);
        } else {
            alarmPush.onlineMonitorPvuvAlarm(msg);
        }

        Assert.assertTrue(false);

    }

    private void dingPushWithoutAssert(String dingMsg) {
        logger.error(dingMsg);
        AlarmPush alarmPush = new AlarmPush();

        if (DEBUG) {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.PV_UV_ACCURACY_GRP);
        }

        alarmPush.onlineMonitorPvuvAlarm(dingMsg);
    }

    class DataUnit {
        String deviceId;
        String deviceName;
        String shopName;
        int history = 0;
        int today = 0;
        int diffValue    = 0;
        float diffRange  = 0f;
        int alarm = 0;
    }

}
