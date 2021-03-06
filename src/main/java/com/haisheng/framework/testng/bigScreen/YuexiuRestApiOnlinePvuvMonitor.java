package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.OnlinePVUV;
import com.haisheng.framework.model.bean.OnlinePvuvCheck;
import com.haisheng.framework.model.bean.OnlineYuexiuUvGap;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.QADbUtil;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;
import java.text.DecimalFormat;


public class YuexiuRestApiOnlinePvuvMonitor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private QADbUtil qaDbUtil = new QADbUtil();
    private DateTimeUtil dt   = new DateTimeUtil();
    private final String ONLINE_LB  = "http://123.57.114.205";

    private String authorization = "";
    private HttpConfig config;
    private final String REAL_TIME_PREFIX = "/yuexiu/data/statistics/real-time/";

    /**
     * 环境   线上为 ONLINE 测试为 DAILY
     */
    private boolean DEBUG = false;

    private long SHOP_ID_ENV = 889;

    final float HOUR_DIFF_RANGE_OVERLAP = 2f; //干扰波动阈值，高于改阈值不监控波动
    final float HOUR_DIFF_RANGE_MAX = 1f; //小时级波动阈值
    final float DIFF_FILTER_OUT = 0.5f; //波动阈值,低于该阈值不监控波动; 天级波动阈值与此保持一致
    final float PV_FILTER_OUT = 200f; //pv 波动监控阈值, 低于该阈值不监控波动, 只监控数据为0

    private String HOUR  = dt.getCurrentHour();
    private int SHOP_UV  = 0;

    final String RISK_MAX = "高危险报警";
    //18210113587 于
    //15898182672 华成裕
    //18810332354 刘峤
    //18600514081 段
    final String AT_USERS = "请 @18210113587 @15898182672 @18810332354 @18600514081 关注";
    final String[] USER_LIST = {"18210113587", "15898182672", "18810332354", "18600514081"};



    @Test
    public void getRealTimePvuv() {
        String shopId = "889";
        String com    = "越秀-售楼处线上";

        realTimeMonitor(com, shopId);

    }

    @Test(dependsOnMethods = {"getRealTimePvuv"}, alwaysRun = true)
    public void getShopUvGap() {

        String path = REAL_TIME_PREFIX + "region";
        CheckUnit checkUnitRegion = realTimeRegion(path);

        String date = dt.getHistoryDate(0);
        saveRegionData(SHOP_UV, checkUnitRegion.uv, date);
    }

    private void realTimeMonitor(String com, String shopId) {
        //get pv uv data
        String path = REAL_TIME_PREFIX + "shop";
        CheckUnit checkUnit = realTimeShop(path);

        String date = dt.getHistoryDate(0);
        String history = dt.getHistoryDate(-7);
        HOUR = dt.getCurrentHour();

        OnlinePVUV onlinePVUV = saveData(checkUnit, com, date);

        //check current hour data not zero
        checkCurrentHourDataNotZerro(onlinePVUV, com, dt.getHistoryDate(0), dt.getCurrentHour(-1));
        checkResult(onlinePVUV, com, history, HOUR);

    }

    private String getRealTimeParamJson() {

        return "{\"shop_id\":" + SHOP_ID_ENV + "}";
    }



    private void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
            String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
            Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                    .userAgent(userAgent)
                    .authorization(authorization)
                    .build();

            config = HttpConfig.custom()
                    .headers(headers)
                    .client(client);
        } catch (HttpProcessException e) {
            logger.error(e.toString());
            dingPush("初始化http配置异常" + "\n" + e);
        }

    }

    private String httpPost(String path, String json, String msg) {
        initHttpConfig();
        String queryUrl = ONLINE_LB + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        String response = "";

        try {
            response = HttpClientUtil.post(config);
        } catch (HttpProcessException e) {
            String failReason = "http post 调用异常，url = " + queryUrl + "\n" + e;
            logger.error(failReason);
            dingPush(failReason);
            
        }

        logger.info("result = {}", response);
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        checkCode(response, 1000, msg);

        return response;
    }





    public CheckUnit realTimeShop(String path) {
        CheckUnit data = new CheckUnit();
        String resStr = "";
        try {
            String json = getRealTimeParamJson();
            resStr = httpPost(path, json, path+"接口异常\n");
            JSONObject jsonRoot = JSON.parseObject(resStr);
            data.requestId = jsonRoot.getString("request_id");
            JSONObject jsonData = jsonRoot.getJSONObject("data");
            data.pv = jsonData.getInteger("pv");
            data.uv = jsonData.getInteger("uv");
            //data.stayUv = jsonData.getInteger("stay_num");
            SHOP_UV = data.uv;
            if (data.pv < 0 || data.uv < 0) {
                throw new Exception("pv uv 为负数");
            }
        } catch (Exception e) {
            String msg = path + "接口获取pv uv 数据异常: \nrequest id: " + data.requestId + "\n" + e.toString() + "\nresponse: " + resStr;
            dingPush(msg);
        }


        return data;
    }

    public CheckUnit realTimeRegion(String path) {
        CheckUnit data = new CheckUnit();
        String resStr = "";
        try {
            String json = getRealTimeParamJson();
            resStr = httpPost(path, json, path+"接口异常\n");
            JSONObject jsonRoot = JSON.parseObject(resStr);
            data.requestId = jsonRoot.getString("request_id");
            JSONObject jsonData = jsonRoot.getJSONObject("data");
            JSONArray regionArray = jsonData.getJSONArray("regions");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(regionArray), "data.regions为空");
            for (int i=0; i<regionArray.size(); i++) {
                JSONObject region = regionArray.getJSONObject(i);
                String regionName = region.getString("region_name");
                Preconditions.checkArgument(!StringUtils.isEmpty(regionName), "data.regions[" + i + "].region_name为空");
                if (regionName.trim().equals("大堂区")) {
                    data.uv = (int) JSONPath.eval(region, "$.statistics.uv");
                    data.pv = (int) JSONPath.eval(region, "$.statistics.pv");
                }
            }

            if (data.pv < 0 || data.uv < 0) {
                throw new Exception("pv uv 为负数");
            }
        } catch (Exception e) {
            String msg = path + "接口获取pv uv 数据异常: \nrequest id: " + data.requestId + "\n" + e.toString() + "\nresponse: " + resStr;
            dingPush(msg);
        }


        return data;
    }

    private void checkCurrentHourDataNotZerro(OnlinePVUV onlinePVUV, String com, String historyDate, String hour) {
        String hourRange = "";
        if (hour.equals("all")) {
            return;
        } else {
            int currentHour = Integer.parseInt(hour);
            int lastHour = currentHour - 1;

            if (currentHour <= 8 || currentHour >= 22) {
                //not check result
                return;
            }
            hourRange = lastHour + ":00 ~ " + hour + ":00";
        }


        OnlinePvuvCheck historyPvuv = qaDbUtil.selectOnlinePvUv(com, historyDate, hour);
        int uvEnterDiff = onlinePVUV.getUvEnter() - historyPvuv.getUvEnter();
        int pvEnterDiff = onlinePVUV.getPvEnter() - historyPvuv.getPvEnter();

        if (0 == uvEnterDiff && 0 == pvEnterDiff) {
            String dingMsg = com + "-数据异常: 过去1小时【" + hourRange + "】数据量为 0,  "
                    + RISK_MAX
                    + ", " + AT_USERS;
            dingPush(dingMsg);
        }
    }

    private void saveRegionData(int shopUv, int regionUv, String date) {
        OnlineYuexiuUvGap onlineYuexiuUvGap = new OnlineYuexiuUvGap();
        onlineYuexiuUvGap.setDate(date);
        onlineYuexiuUvGap.setHour(HOUR);
        onlineYuexiuUvGap.setDatangUvEnter(regionUv);
        onlineYuexiuUvGap.setShopUvEnter(shopUv);
        onlineYuexiuUvGap.setDiffUvEnterHourDay(shopUv - regionUv);
        onlineYuexiuUvGap.setDiffUvEnterRangeHourDay((float)regionUv/(float)shopUv);

        try {
            onlineYuexiuUvGap.setUpdateTime(dt.currentDateToTimestamp());
        } catch (Exception e) {
            logger.error("save data set update_time exception");
        }

        qaDbUtil.saveYuexiuOnlineUvGap(onlineYuexiuUvGap);


    }

    private OnlinePVUV saveData(CheckUnit checkUnit, String com, String date) {
        OnlinePVUV onlinePVUV = new OnlinePVUV();
        onlinePVUV.setCom(com);
        onlinePVUV.setDate(date);
        onlinePVUV.setHour(HOUR);
        onlinePVUV.setPvEnter(checkUnit.pv);
        onlinePVUV.setUvEnter(checkUnit.uv);
        try {
            onlinePVUV.setUpdateTime(dt.currentDateToTimestamp());
        } catch (Exception e) {
            logger.error("save data set update_time exception");
        }

        qaDbUtil.saveOnlinePvUv(onlinePVUV);

        return onlinePVUV;

    }

    private void checkResult(OnlinePVUV currentData, String com, String historyDate, String hour) {
        //get history result
        OnlinePvuvCheck historyPvuv = qaDbUtil.selectOnlinePvUv(com, historyDate, hour);
        if (null == historyPvuv) {
            logger.error(com + "-" + historyDate + " " + hour + "history NO data");
            return;
        }

        OnlinePvuvCheck currentPvuv = new OnlinePvuvCheck();
        currentPvuv.setPvEnter(currentData.getPvEnter());
        currentPvuv.setUvEnter(currentData.getUvEnter());
        currentPvuv.setDate(currentData.getDate());

        //check current data with history
        checkPvuv(com, hour, currentPvuv,historyPvuv);

    }

    private void checkPvuv(String com, String hour, OnlinePvuvCheck current, OnlinePvuvCheck history) {
        //当前pv uv 与历史数据差值，差值/当前数据 > 门限值时，发送钉钉推送

        DiffPvUv diffData = new DiffPvUv();

        String dingMsg = checkDiff(com, hour, "uv", current.getUvEnter(), history.getUvEnter(), diffData.uv);
        dingMsg += checkDiff(com, hour, "pv", current.getPvEnter(), history.getPvEnter(), diffData.pv);

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
            hourRange = "00:00 ~ " + hour + ":00";
        }

        //数据为0，直接报警
        if (0 == current) {
            dingMsg = com + "-数据异常: " + type + "截止当前时间【" + hourRange + "】【累计】数据量为 0";
            //数据缩水100%
            diffDataUnit.diffRange = -1;
        } else {
            if (0 == history) {
                //数据增长100%
                diffDataUnit.diffRange = 1;
            } else {
                diffDataUnit.diffRange = (float) diff / (float) history;
            }
            float diffRate  = Math.abs(diffDataUnit.diffRange);
            if (diffRate >= HOUR_DIFF_RANGE_OVERLAP ||
                    diffRate <= DIFF_FILTER_OUT ||
                    diffDataUnit.historyValue <= PV_FILTER_OUT ||
                    diffDataUnit.currentValue <= PV_FILTER_OUT
            ) {
                //排波动范围过大、过小
                //PV阈值以下的波动不予报警
                return "";
            }

            DecimalFormat df = new DecimalFormat("#.00");
            String percent = df.format(diffRate*100) + "%";
            if (diff > 0) {
                if (hour.equals("23")) {
                    dingMsg = com + "-数据异常: " + type + "今日较上周今日【全天数据量】扩大 " + percent;
                } else {
                    if (diffRate > HOUR_DIFF_RANGE_MAX) {
                        dingMsg = com + "-数据异常: " + type + "较【上周今日】【" + hourRange + "时段】【累计】数据量扩大 " + percent;
                    }
                }
            } else if (diff < 0) {
                if (hour.equals("23")) {
                    dingMsg = com + "-数据异常: " + type + "今日较上周今日【全天数据量】缩小 " + percent;
                } else {
                    if (diffRate > HOUR_DIFF_RANGE_MAX) {
                        dingMsg = com + "-数据异常: " + type + "较【上周今日】【" + hourRange + "时段】【累计】数据量缩小 " + percent;
                    }
                }
            }

        }


        //过滤掉8点前的数据，商场8点前人少，波动较剧烈
        if (!hour.equals("all")) {
            int intHour = Integer.parseInt(hour);
            if (intHour < 9) {
                //放弃该时段干扰指标大盘其他时段数据展示
                diffDataUnit.diffRange = 0f;
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

    private void saveDiffData(String com, String date, String hour, DiffPvUv diffData) {
        //save diff to db

        OnlinePVUV onlinePVUV = new OnlinePVUV();
        onlinePVUV.setCom(com);
        onlinePVUV.setDate(date);
        onlinePVUV.setHour(hour);
        onlinePVUV.setDiffUvEnterHourDay(diffData.uv.diffValue);
        onlinePVUV.setDiffUvEnterRangeHourDay(diffData.uv.diffRange);
        onlinePVUV.setDiffPvEnterHourDay(diffData.pv.diffValue);
        onlinePVUV.setDiffPvEnterRangeHourDay(diffData.pv.diffRange);

        if (0 != diffData.uv.alarm || 0 != diffData.pv.alarm) {
            onlinePVUV.setAlarm(1);
        }
        qaDbUtil.updateOnlinePvUvDiff(onlinePVUV);

    }

    private void pushDiffMsg(String dingMsg) {
        if (! StringUtils.isEmpty(dingMsg)) {
            dingPush(dingMsg);
        }
    }


    private void checkCode(String response, int expect, String message) {
        int code = JSON.parseObject(response).getInteger("code");

        if (expect != code) {
            dingPush(message + " expect code: " + expect + ", actual: " + code);
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


    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeSuite
    public void login() {

        String json = "{\"username\":\"yuexiu@yuexiu.com\",\"passwd\":\"f2c7219953b54583ea11065215f22a8b\"}";
        String path = "/yuexiu-login";
        qaDbUtil.openConnection();

        initHttpConfig();
        String loginUrl = ONLINE_LB + path;
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            String result = HttpClientUtil.post(config);
            this.authorization = JSONObject.parseObject(result).getJSONObject("data").getString("token");
            logger.info("authorization: {}", authorization);
        } catch (Exception e) {
            dingPush("http post 调用异常，url = " + loginUrl + "\n" + e);
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }

    class DiffDataUnit {
        int currentValue = 0;
        int historyValue = 0;
        int diffValue    = 0;
        float diffRange  = 0f;
        int alarm = 0;
    }

    class DiffPvUv {
        DiffDataUnit pv = new DiffDataUnit();
        DiffDataUnit uv = new DiffDataUnit();
    }

    class CheckUnit {
        int pv = 0;
        int uv = 0;
        int stayUv = 0;
        String requestId = "";
    }


}


