package com.haisheng.framework.testng.custemorGateTest.online;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.OnlinePVUV;
import com.haisheng.framework.model.bean.OnlinePvuvCheck;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.testng.cronJob.OnlineRequestMonitor;
import com.haisheng.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PVUVMonitor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    QADbUtil qaDbUtil = new QADbUtil();
    DateTimeUtil dt = new DateTimeUtil();


    final String LOCAL_DEBUG_HOST  = "http://39.105.225.20";
    final String DAILY_LB          = "http://10.0.15.226";
    final String ONLINE_LB         = "http://10.0.16.17";

    final float HOUR_DIFF_RANGE_MAX = 1f;
    final float HOUR_DIFF_RANGE = 0.5f;
    final float HOUR_DIFF_RANGE_100 = 0.8f;
    final float HOUR_DIFF_RANGE_50 = 1.6f;
    final float HOUR_DIFF_RANGE_20 = 3.2f;
    final float HOUR_DIFF_RANGE_10 = 5f;
    final float DAY_DIFF_RANGE = 0.1f;

    final String RISK_MAX = "高危险报警";
    //18210113587 于
    //15898182672 华成裕
    //18810332354 刘峤
    //18600514081 段
    final String AT_USERS = "请 @18210113587 @15898182672 @18810332354 @18600514081 关注";
    final String[] USER_LIST = {"18210113587", "15898182672", "18810332354", "18600514081"};

    String HOUR   = "all";
    boolean FAIL  = false;

    boolean DEBUG = false;

    ConcurrentHashMap<String, ArrayList<String>> ALARM_STACK = new ConcurrentHashMap<String, ArrayList<String>>();

    //@Test
    public void getRealTimeDataYingshiDaily() {
        String shopId = "8";
        String appId  = "097332a388c2";
        String com    = "赢识日常";
        String router = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1";
        String url    = DAILY_LB + router;

        if (DEBUG) {
            url = LOCAL_DEBUG_HOST + router;
        }

        realTimeMonitor(url, shopId, appId, com);

    }

    //@Test
    public void getRealTimeDataYingshiOnline() {
        String shopId = "97";
        String appId  = "111112a388c2";
        String com    = "赢识线上";
        String router = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1";
        String url    = ONLINE_LB + router;

        realTimeMonitor(url, shopId, appId, com);
        logger.info("PASS getRealTimeDataYingshiOnline");

    }

    @Test
    public void getHistoryDataYingshiDaily() {
        String shopId = "8";
        String appId  = "097332a388c2";
        String com    = "赢识日常";

        if (DEBUG) {
            getHistoryDataByShop(LOCAL_DEBUG_HOST, shopId, appId, com);
        } else {
            getHistoryDataByShop(DAILY_LB, shopId, appId, com);
        }

        logger.info("PASS getHistoryDataYingshiDaily");
    }

    @Test
    public void getHistoryDataYingshiOnline() {
        String shopId = "97";
        String appId  = "111112a388c2";
        String com    = "赢识线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

        logger.info("PASS getHistoryDataYingshiOnline");
    }

    @Test
    public void getHistoryDataBaihuaOnline() {
        String shopId = "149";
        String appId  = "77d07cf38e8e";
        String com    = "百花时代广场(东莞)线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

        logger.info("PASS getHistoryDataBaihuaOnline");

    }

    @Test
    public void getHistoryDataHumenOnline() {
        String shopId = "251";
        String appId  = "77d07cf38e8e";
        String com    = "虎门国际购物中心线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

        logger.info("PASS getHistoryDataHumenOnline");

    }

    @Test
    public void getHistoryDataLiyingOnline() {
        String shopId = "373";
        String appId  = "77d07cf38e8e";
        String com    = "丽影广场线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

        logger.info("PASS getHistoryDataLiyingOnline");

    }

    @Test
    public void getHistoryDataFengkeOnline() {
        String shopId = "242";
        String appId  = "5f20ed10b9cb";
        String com    = "万达广场丰科店线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

        logger.info("PASS getHistoryDataFengkeOnline");

    }

    /**
     * 锦华停电中，暂停监控
     * */
    //@Test
    public void getHistoryDataJinhuaOnline() {
        String shopId = "4283";
        String appId  = "5f20ed10b9cb";
        String com    = "万达成都锦华线上";
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

        logger.info("PASS getHistoryDataJinhuaOnline");

    }

    /**
     * 百果园
     * */
    @Test(dataProvider = "baiguoyuan")
    public void getHistoryDataBaiguoyuanOnline(String shopId) {
        //String shopId = "246";
        String appId  = "2cf019f4c443";
        String com    = "百果园-"+shopId;
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

        logger.info("PASS getHistoryDataBaiguoyuanOnline-"+shopId);

    }

    /**
     * 小天才
     * */
    @Test(dataProvider = "xiaotiancai")
    public void getHistoryDataXiaotiancaiOnline(String shopId) {
        //String shopId = "15617";
        String appId  = "4f4dc399f4ed";
        String com    = "小天才-"+shopId;
        getHistoryDataByShop(ONLINE_LB, shopId, appId, com);

        logger.info("PASS getHistoryDataXiaotiancaiOnline-"+shopId);

    }

    /**
    * 1958、1922 调试AI摄像头，故暂不监控
    * */
    @DataProvider(name = "baiguoyuan")
    public static Object[] baiguoyuan() {

        return new String[] {
                "246",
//                "1958",
                "1956",
                "1954",
                "1952",
                "1950",
                "1946",
                "1944",
                "1942",
                "1940",
                "1938",
                "1936",
                "1934",
                "1932",
                "1930",
                "1928",
                "1926",
                "1924",
//                "1922",
                "1920",
                "1918",
                "1916",
                "1914",
                "1912",
                "1910"
        };
    }

    @DataProvider(name = "xiaotiancai")
    public static Object[] xiaotiancai() {

        return new String[] {
                "15615",
                "15617"
        };
    }


    private void realTimeMonitor(String url, String shopId, String appId, String com) {
        try {
            String requestId = UUID.randomUUID().toString();
            String date = dt.getHistoryDate(0);
            HOUR = "all";

            String json = "{" +
                    "\"data\":{\"shop_id\":" + shopId + "}," +
                    "\"request_id\":\"" + requestId + "\"," +
                    "\"system\":{\"app_id\":\"" + appId + "\",\"source\":\"BIZ\"}" +
                    "}";

            String response = sendRequest(com, url, json);
            checkCode(response, StatusCode.SUCCESS, com + "环境监测-实时统计查询-返回值异常");

            saveData(response, com, date);

        } catch (Exception e) {
            dingPush(com + "-巡检代码异常: " + e.toString());
        }
    }

    private void getHistoryDataByShop(String lb, String shopId, String appId, String com) {
        String router = "/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1";
        String url    = lb + router;
        String startTime = null, endTime = null;

        try {
            startTime = dt.getHourBegin(-1);
            endTime = dt.getHourBegin(0);
            HOUR = dt.getCurrentHour();
        } catch (Exception e) {
            dingPush(com + "-巡检代码获取当前小时整点时间戳异常: " + e.toString());
        }

        if (HOUR.equals("0")) {
            HOUR = "24";
        }
        historyMonitor(url, startTime, endTime, shopId, appId, com);

        //get one day pvuv data
        if (HOUR.equals("24")) {
            HOUR = "all";
            endTime   = String.valueOf(dt.getHistoryDateTimestamp(0));
            startTime = String.valueOf(dt.getHistoryDateTimestamp(-1));
            historyMonitor(url, startTime, endTime, shopId, appId, com);
        }

    }

    private void historyMonitor(String url, String startTime, String endTime, String shopId, String appId, String com) {
        String requestId = UUID.randomUUID().toString();
        String date = dt.getHistoryDate(0);
        String dayWeek = dt.getHistoryDate(-7);

        if (HOUR.equals("24")) {
            //0点后获取昨天24点的数据，数据日期为昨天
            date = dt.getHistoryDate(-1);
            dayWeek = dt.getHistoryDate(-8);
        }
        String json = "{" +
                "\"data\":{" +
                "\"shop_id\":" + shopId + "," +
                "\"start_time\":" + startTime + "," +
                "\"end_time\":" + endTime +
                "}," +
                "\"request_id\":\"" + requestId + "\"," +
                "\"system\":{\"app_id\":\"" + appId + "\",\"source\":\"BIZ\"}" +
                "}";

        if (HOUR.equals("all")) {
            //全天历史数据统计的是昨天的流量
            date = dt.getHistoryDate(-1);
            dayWeek = dt.getHistoryDate(-8);
            json = "{" +
                    "\"data\":{" +
                    "\"shop_id\":" + shopId + "," +
                    "\"start_time\":" + startTime + "," +
                    "\"end_time\":" + endTime + "," +
                    "\"time_statistics_type\":" + "\"DAY\"" +
                    "}," +
                    "\"request_id\":\"" + requestId + "\"," +
                    "\"system\":{\"app_id\":\"" + appId + "\",\"source\":\"BIZ\"}" +
                    "}";
        }

        String response = sendRequest(com, url, json);
        checkCode(response, StatusCode.SUCCESS, com + " 环境监测-历史统计查询-返回值异常");
        OnlinePVUV onlinePVUV = saveData(response, com, date);
        checkResult(onlinePVUV, com, dayWeek, HOUR);

        onlinePVUV = null;
        response   = null;
    }


    private OnlinePVUV saveData(String response, String com, String date) {
        OnlinePVUV onlinePVUV = new OnlinePVUV();
        try {
            JSONObject responseJo = JSON.parseObject(response);
            JSONArray statistics  = responseJo.getJSONArray("statistics");

            String gender = null, age = null;
            int pvEnter = 0, pvLeave = 0, uvEnter = 0, uvLeave = 0;

            if (statistics != null) {
                int size = statistics.size();
                if (size > 0) {
                    JSONObject statisticsSingle = statistics.getJSONObject(0);

                    gender = statisticsSingle.getString("gender");
                    age = statisticsSingle.getString("age");

                    String uvLeaveStr = statisticsSingle.getJSONObject("person_number").getString("entrance_leave_total");
                    uvLeave = Integer.parseInt(uvLeaveStr);

                    String uvEnterStr = statisticsSingle.getJSONObject("person_number").getString("entrance_enter_total");
                    uvEnter = Integer.parseInt(uvEnterStr);

                    String pvLeaveStr = statisticsSingle.getJSONObject("passing_times").getString("entrance_leave_total");
                    pvLeave = Integer.parseInt(pvLeaveStr);

                    String pvEnterStr = statisticsSingle.getJSONObject("passing_times").getString("entrance_enter_total");
                    pvEnter = Integer.parseInt(pvEnterStr);
                }
            }

            onlinePVUV.setCom(com);
            onlinePVUV.setDate(date);
            onlinePVUV.setHour(HOUR);
            onlinePVUV.setGender(gender);
            onlinePVUV.setAge(age);
            onlinePVUV.setPvEnter(pvEnter);
            onlinePVUV.setPvLeave(pvLeave);
            onlinePVUV.setUvEnter(uvEnter);
            onlinePVUV.setUvLeave(uvLeave);
            onlinePVUV.setUpdateTime(dt.currentDateToTimestamp());

            qaDbUtil.saveOnlinePvUv(onlinePVUV);
        } catch (Exception e) {
            dingPush(com + "-历史统计查询接口返回response数据异常: " + e.toString() + "\n" + "response: " + response);
        }

        return onlinePVUV;

    }

    private void checkResult(OnlinePVUV currentData, String com, String historyDate, String hour) {
        //get history result
        OnlinePvuvCheck historyPvuv = qaDbUtil.selectOnlinePvUv(com, historyDate, hour);

        OnlinePvuvCheck currentPvuv = new OnlinePvuvCheck();
        currentPvuv.setPvEnter(currentData.getPvEnter());
        currentPvuv.setPvLeave(currentData.getPvLeave());
        currentPvuv.setUvEnter(currentData.getUvEnter());
        currentPvuv.setUvLeave(currentData.getUvLeave());
        currentPvuv.setDate(currentData.getDate());

        //check current data with history
        checkPvuv(com, hour, currentPvuv,historyPvuv);

    }

    private void checkPvuv(String com, String hour, OnlinePvuvCheck current, OnlinePvuvCheck history) {
        //当前pv uv 与历史数据差值，差值/当前数据 > 门限值时，发送钉钉推送

        DiffData diffData = new DiffData();

        if (null == history ) {
            //数据库中无数据，则初始化历史数据
            history = new OnlinePvuvCheck();
        }
        String dingMsg = checkDiff(com, hour, "uv_enter", current.getUvEnter(), history.getUvEnter(), diffData.uvEnterDiff);
        dingMsg += checkDiff(com, hour, "pv_enter", current.getPvEnter(), history.getPvEnter(), diffData.pvEnterDiff);

        //checkDiff(com, hour, "uv_leave", current.getPvEnter(), history.getPvEnter(), diffData.uvLeaveDiff);
        //checkDiff(com, hour, "pv_leave", current.getPvEnter(), history.getPvEnter(), diffData.pvLeaveDiff);

        //save diff data
        saveDiffData(com, current.getDate(), hour, diffData);

        //push dingding msg
        pushDiffMsg(dingMsg);

    }

    private String checkDiff(String com, String hour, String type, int current, int history, DiffDataUnit diffDataUnit) {
        int diff       = current - history;
        String dingMsg = "";
        String hourRange = "";
        diffDataUnit.currentValue = current;
        diffDataUnit.historyValue = history;
        diffDataUnit.diffValue    = diff;

        if (hour.equals("all")) {
            hourRange = "00:00 ~ 23:59";
        } else {
            int lastHour = Integer.parseInt(hour) - 1;
            hourRange = lastHour + ":00 ~ " + hour + ":00";
        }

        //数据为0，直接报警
        if (0 == current) {
            dingMsg = com + "-数据异常: " + type + "过去【" + hourRange + "】数据量为 0, "
                    + RISK_MAX
                    + ", " + AT_USERS;
            //数据缩水100%
            diffDataUnit.diffRange = -1;
        } else {
            if (0 == history) {
                //历史数据为0时不报警
                diffDataUnit.diffRange = 0;
            } else {
                diffDataUnit.diffRange = (float) diff / (float) history;
            }
            DecimalFormat df = new DecimalFormat("#.00");
            if (diff > 0) {
                float enlarge  = diffDataUnit.diffRange;
                String percent = df.format(enlarge*100) + "%";

                if (hour.equals("all")) {
                    if (enlarge > DAY_DIFF_RANGE) {
                        dingMsg = com + "-数据异常: " + type + "昨日较上周同日【全天数据量】扩大 " + percent;
                    }
                } else {
                    if (diffDataUnit.historyValue <= 10) {
                        if (enlarge > HOUR_DIFF_RANGE_10) {
                            dingMsg = com + "-数据异常: " + type + "较【上周今日同时段】【" + hourRange + "】数据量扩大 " + percent;
                        }
                    }else if (diffDataUnit.historyValue <= 20) {
                        if (enlarge > HOUR_DIFF_RANGE_20) {
                            dingMsg = com + "-数据异常: " + type + "较【上周今日同时段】【" + hourRange + "】数据量扩大 " + percent;
                        }
                    } else if (diffDataUnit.historyValue <= 50) {
                        if (enlarge > HOUR_DIFF_RANGE_50) {
                            dingMsg = com + "-数据异常: " + type + "较【上周今日同时段】【" + hourRange + "】数据量扩大 " + percent;
                        }
                    } else if (diffDataUnit.historyValue <= 100) {
                        if (enlarge > HOUR_DIFF_RANGE_100) {
                            dingMsg = com + "-数据异常: " + type + "较【上周今日同时段】【" + hourRange + "】数据量扩大 " + percent;
                        }
                    } else {
                        if (enlarge >= HOUR_DIFF_RANGE_MAX && diffDataUnit.historyValue > 300) {
                            dingMsg = com + "-数据异常: " + type + "较【上周今日同时段】【" + hourRange + "】数据量扩大 " + percent
                                    + ", " + RISK_MAX
                                    + ", " + AT_USERS;
                        } else if (enlarge > HOUR_DIFF_RANGE) {
                            dingMsg = com + "-数据异常: " + type + "较【上周今日同时段】【" + hourRange + "】数据量扩大 " + percent;
                        }
                    }
                }
            } else if (diff < 0) {
                float shrink = diffDataUnit.diffRange * (-1);
                String percent = df.format(shrink*100) + "%";

                if (hour.equals("all")) {
                    if (shrink > DAY_DIFF_RANGE) {
                        dingMsg = com + "-数据异常: " + type + "昨日较上周同日【全天数据量】缩小 " + percent;
                    }
                } else {
                    if (diffDataUnit.historyValue <= 10) {
                        dingMsg = "";
                    } else if (diffDataUnit.historyValue <= 50) {
                        if (shrink > HOUR_DIFF_RANGE_50) {
                            dingMsg = com + "-数据异常: " + type + "较【上周今日同时段】【" + hourRange + "】数据量缩小 " + percent;
                        }
                    } else if (diffDataUnit.historyValue <= 100) {
                        if (shrink > HOUR_DIFF_RANGE_100) {
                            dingMsg = com + "-数据异常: " + type + "较【上周今日同时段】【" + hourRange + "】数据量缩小 " + percent;
                        }
                    } else {
                        if (shrink > HOUR_DIFF_RANGE) {
                            dingMsg = com + "-数据异常: " + type + "较【上周今日同时段】【" + hourRange + "】数据量缩小 " + percent;
                        }
                    }
                }
            }

        }

        //实验室环境不报警，调式时报警
        if (com.contains("赢识") && !DEBUG) {
            return "";
        }
        //过滤掉8点前的数据，商场8点前人少，波动较剧烈
        if (!hour.equals("all")) {
            int intHour = Integer.parseInt(hour);
            if (intHour < 9 || intHour == 24) {
                //凌晨时段人数变动小于100人，则变动服务削峰为0，放置该时段干扰指标大盘其他时段数据展示
                if (diffDataUnit.diffValue < 100) {
                    diffDataUnit.diffRange = 0f;
                }
                return "";
            }
        }

        if (! StringUtils.isEmpty(dingMsg)) {
            dingMsg += "\n\n"
                    + "current: " + current + "\n"
                    + "history: " + history + "\n"
                    + "diff: " + diff + "\n\n\n\n";
            diffDataUnit.alarm = 1;
        }
        return dingMsg;
    }

    private void saveDiffData(String com, String date, String hour, DiffData diffData) {
        //save diff to db

        OnlinePVUV onlinePVUV = new OnlinePVUV();
        onlinePVUV.setCom(com);
        onlinePVUV.setDate(date);
        onlinePVUV.setHour(hour);
        onlinePVUV.setDiffUvEnterHourDay(diffData.uvEnterDiff.diffValue);
        onlinePVUV.setDiffUvEnterRangeHourDay(diffData.uvEnterDiff.diffRange);
        onlinePVUV.setDiffPvEnterHourDay(diffData.pvEnterDiff.diffValue);
        onlinePVUV.setDiffPvEnterRangeHourDay(diffData.pvEnterDiff.diffRange);

        if (0 != diffData.pvEnterDiff.alarm || 0 != diffData.uvEnterDiff.alarm) {
            onlinePVUV.setAlarm(1);
        }
        qaDbUtil.updateOnlinePvUvDiff(onlinePVUV);

    }

    private void pushDiffMsg(String dingMsg) {
        if (! StringUtils.isEmpty(dingMsg)) {
            //dingPush(dingMsg);
            //分类存储dingding推送消息，最终一起推送
            String com = dingMsg.substring(0, dingMsg.indexOf("-"));
            ArrayList<String> list = null;
            if (ALARM_STACK.containsKey(com)) {
                list = ALARM_STACK.get(com);
            } else {
                list = new ArrayList();
                ALARM_STACK.put(com, list);
            }
            list.add(dingMsg);
            this.FAIL = true;
        }
    }

    private String sendRequest(String com, String url, String json) {
        try {
            HttpExecutorUtil executorUtil = new HttpExecutorUtil();
            executorUtil.doPostJson(url, json);

            return executorUtil.getResponse();
        } catch (Exception e) {
            dingPush(com + "-历史统计查询接口请求异常: " + e.toString());
        }

        return "";

    }

    private void checkCode(String response, int expect, String message) {
        int code = JSON.parseObject(response).getInteger("code");

        if (expect != code) {
            dingPush(message + " expect code: " + expect + ", actual: " + code);
        }
    }

    private void dingPush(String msg) {
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
        this.FAIL = true;
        Assert.assertTrue(false);

    }

    private void multipleShopAlarm(AlarmPush alarmPush, String key) {
        ArrayList<String> recordList = ALARM_STACK.get(key);
        String summary = key + "有" + recordList.size() + "个店铺报警";
        int zeroSize = 0;
        String zeroComDetail = "";
        int diffSize = 0;
        String diffComDetail = "";
        int indexBegin = (key+"-").length();
        for (int i=0; i<recordList.size(); i++) {
            String record = recordList.get(i);
            if (record.contains("数据量为 0")) {
                zeroSize++;
                zeroComDetail += record.substring(record.indexOf(key+"-")+indexBegin, record.indexOf("-数据异常")) + "  ";
            } else {
                diffSize++;
                diffComDetail += record.substring(record.indexOf(key+"-")+indexBegin, record.indexOf("-数据异常")) + "  ";
            }
        }

        if (zeroSize > 0) {
            summary += "\n以下" + zeroSize + "个店铺数据量为0\n\n" + zeroComDetail;

            if (key.contains("百果园")) {
                //百果园数据为0且只在10点时段，单独发到【百果园盒子准备】群
                DateTimeUtil dt = new DateTimeUtil();
                if (dt.getCurrentHour().contains("10")) {
                    alarmPush.baiguoyuanZeroAlarm(summary);
                }
            }
        }
        if (diffSize > 0) {
            summary += "\n以下" + diffSize + "个店铺数据量波动过大\n\n" + diffComDetail;
        }

        alarmPush.onlineMonitorPvuvAlarm(summary);
    }

    private void oneShopAlarm(AlarmPush alarmPush, String key) {
        ArrayList<String> recordList = ALARM_STACK.get(key);
        String record = "";
        for (int i=0; i<recordList.size(); i++) {
            record = recordList.get(i) + "\n";
        }
        alarmPush.onlineMonitorPvuvAlarm(record);
    }

    private void dingPushFinal() {
        if (!DEBUG && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.PV_UV_ACCURACY_GRP);

            if (ALARM_STACK.size() > 0) {
                for(String key : ALARM_STACK.keySet()) {

                    if (key.contains("百果园") || key.contains("小天才")) {
                        multipleShopAlarm(alarmPush, key);
                    } else {
                        oneShopAlarm(alarmPush, key);
                    }

                }

            }

            //15011479599 谢志东
            //13436941018 吕雪晴
            //15084928847 黄青青
            //13581630214 马琨
            //18513118484 杨航
            //15898182672 华成裕
            //18810332354 刘峤
            //17319042091 甘甜甜
            //18611684769 杨立新
            //15037286013 夏明凤
            String[] rd = {"13436941018", "15084928847", "13581630214", "15898182672", "18513118484", "17319042091", "18611684769", "15037286013"};
            alarmPush.alarmToRd(rd);
        }
    }


    @BeforeSuite
    public void initial(){
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();

        if (this.FAIL) {
            logger.info("trigger device request monitor");
            OnlineRequestMonitor onlineRequestMonitor = new OnlineRequestMonitor();
            onlineRequestMonitor.initial();
            onlineRequestMonitor.requestNumberMonitor();
            onlineRequestMonitor.clean();
            dingPushFinal();
        } else {
            logger.info("do NOT trigger device request monitor");
        }
    }
}

class DiffDataUnit {
    int currentValue = 0;
    int historyValue = 0;
    int diffValue    = 0;
    float diffRange  = 0f;
    int alarm = 0;
}

class DiffData {
    DiffDataUnit pvEnterDiff = new DiffDataUnit();
    DiffDataUnit uvEnterDiff = new DiffDataUnit();
    DiffDataUnit pvLeaveDiff = new DiffDataUnit();
    DiffDataUnit uvLeaveDiff = new DiffDataUnit();

}
