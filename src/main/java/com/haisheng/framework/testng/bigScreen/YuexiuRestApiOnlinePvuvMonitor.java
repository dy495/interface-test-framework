package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.model.bean.OnlinePVUV;
import com.haisheng.framework.model.bean.OnlinePvuvCheck;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.QADbUtil;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final float HOUR_DIFF_RANGE = 0.3f;
    private final float DAY_DIFF_RANGE = 0.1f;

    private String HOUR = "all";



    @Test
    public void getRealTimePvuv() {
        String shopId = "889";
        String com    = "越秀-售楼处线上";

        realTimeMonitor(com, shopId);

    }

    private void realTimeMonitor(String com, String shopId) {
        //get pv uv data
        String path = REAL_TIME_PREFIX + "shop";
        CheckUnit checkUnit = realTimeShop(path);

        String date = dt.getHistoryDate(0);
        String history = dt.getHistoryDate(-7);
        HOUR = dt.getCurrentHour();

        OnlinePVUV onlinePVUV = saveData(checkUnit, com, date);
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
            data.stayUv = jsonData.getInteger("stay_num");

            if (data.pv < 0 || data.uv < 0 || data.stayUv < 0) {
                throw new Exception("pv uv stay_num 为负数");
            }
        } catch (Exception e) {
            String msg = path + "接口获取pv uv 数据异常: \nrequest id: " + data.requestId + "\n" + e.toString() + "\nresponse: " + resStr;
            dingPush(msg);
        }


        return data;
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
        diffDataUnit.currentValue = current;
        diffDataUnit.historyValue = history;
        diffDataUnit.diffValue    = diff;

        //数据为0，直接报警
        if (0 == current) {
            dingMsg = com + "-数据异常: " + type + "截止当前时间数据量为 0";
            //数据缩水100%
            diffDataUnit.diffRange = -1;
        } else {
            if (0 == history) {
                //数据增长100%
                diffDataUnit.diffRange = 1;
            } else {
                diffDataUnit.diffRange = (float) diff / (float) history;
            }
            DecimalFormat df = new DecimalFormat("#.00");
            if (diff > 0) {
                float enlarge  = diffDataUnit.diffRange;
                String percent = df.format(enlarge*100) + "%";

                if (hour.equals("23")) {
                    if (enlarge > DAY_DIFF_RANGE) {
                        dingMsg = com + "-数据异常: " + type + "今日较上周今日【全天数据量】扩大 " + percent;
                    }
                } else {
                    if (enlarge > HOUR_DIFF_RANGE) {
                        dingMsg = com + "-数据异常: " + type + "较【上周今日同时段】数据量扩大 " + percent;
                    }
                }
            } else if (diff < 0) {
                float shrink = diffDataUnit.diffRange * (-1);
                String percent = df.format(shrink*100) + "%";

                if (hour.equals("23")) {
                    if (shrink > DAY_DIFF_RANGE) {
                        dingMsg = com + "-数据异常: " + type + "今日较上周今日【全天数据量】缩小 " + percent;
                    }
                } else {
                    if (shrink > HOUR_DIFF_RANGE) {
                        dingMsg = com + "-数据异常: " + type + "较【上周今日同时段】数据量缩小 " + percent;
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
            alarmPush.setDingWebhook(DingWebhook.AD_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.PV_UV_ACCURACY_GRP);
        }

        alarmPush.onlineMonitorPvuvAlarm(msg);
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


