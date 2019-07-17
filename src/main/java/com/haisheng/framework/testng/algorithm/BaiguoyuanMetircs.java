package com.haisheng.framework.testng.algorithm;

import com.haisheng.framework.model.bean.BaiguoyuanBindMetrics;
import com.haisheng.framework.model.bean.BaiguoyuanBindUser;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BaiguoyuanMetircs {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private QADbUtil qaDbUtil = new QADbUtil();
    private DateTimeUtil dt = new DateTimeUtil();
    private FileUtil fileUtil = new FileUtil();

    private String TRANS_REPORT_FILE = System.getProperty("TRANS_REPORT_FILE");;
    private String EDGE_LOG = System.getProperty("EDGE_LOG");
    private String IS_PUSH_MSG = System.getProperty("IS_PUSH_MSG");
    private String VIDEO_START_KEY = System.getProperty("VIDEO_START_KEY");
    private String IS_SAVE_TO_DB = System.getProperty("IS_SAVE_TO_DB");
    private String KEY_GENDER = "gender";
    private String KEY_START_TIME = "startTime";
    private String KEY_END_TIME = "endTime";
    private String KEY_USER_ID = "userId";
    private String KEY_TOTAL_USER = "userTotal";

    private String METRICS_BIND_ACCURACY = "bind_accuracy";
    private String METRICS_BIND_SUCCESS_ACCURACY = "bind_success_accuracy";

    private boolean IS_DEBUG = false;
    private String currentDate = dt.getHistoryDate(0);
    private int expectBindUserNum = 0;

    private String URL = "http://39.106.233.43/bind/receive";



    @Test
    private void uploadTransData() throws Exception {
        if (IS_DEBUG) {
            EDGE_LOG = "src/main/resources/csv/yuhaisheng/demo2.csv";
            TRANS_REPORT_FILE = "src/main/resources/test-res-repo/baiguoyuan-metircs/debug.csv";
            VIDEO_START_KEY = "start to play video";
            IS_PUSH_MSG = "false";
            IS_SAVE_TO_DB = "false";
        }


        //get video playing time
        String beginTime = getVideoStartTime();
        Assert.assertNotNull(beginTime, "NOT found actually video start playing time");

        //upload transaction data to cloud
        boolean result = postTransData(beginTime);
        Assert.assertTrue(result, "no expect transaction data");

        //get bind-accuracy and bind-success-accuracy
        logger.info("sleep 2m, to let cloud service work enough");
        Thread.sleep(2*60*1000);
        result = getAndPrintMetrics();
        Assert.assertTrue(result, "NO bind user found");

        pushMsg();
    }

    private void pushMsg() {

        if (null == IS_PUSH_MSG || IS_PUSH_MSG.trim().toLowerCase().equals("false")) {
            return;
        }

        List<BaiguoyuanBindMetrics> accuracyList = qaDbUtil.getBaiguoyuanMetrics(currentDate);
        AlarmPush alarmPush = new AlarmPush();
        alarmPush.setDingWebhook(DingWebhook.APP_BAIGUOYUAN_ALARM_GRP);
//        alarmPush.setDingWebhook(DingWebhook.AD_GRP);
        alarmPush.baiguoyuanAlarm(accuracyList);
    }

    private boolean getAndPrintMetrics() {
        boolean result = true;
        List<BaiguoyuanBindUser> bindUserList = qaDbUtil.getBaiguoyuanBindAccuracy(currentDate);
        if (null == bindUserList || bindUserList.size() < 1) {
            logger.info("NO bind user found");
            return false;
        }

        BaiguoyuanBindMetrics bindMetrics = new BaiguoyuanBindMetrics();
        bindMetrics.setDate(currentDate);
        bindMetrics.setMetrics(METRICS_BIND_ACCURACY);

        int actualBindUserNum = bindUserList.size();
        float accuracy = (float) actualBindUserNum/expectBindUserNum;
        bindMetrics.setAccuracy(accuracy);

        DecimalFormat df = new DecimalFormat("#.00");
        String percent = df.format(accuracy*100) + "%";

        logger.info("");
        logger.info("");
        logger.info("\n=========================================================="
                + "\n\tactual bind users' num: " + bindUserList.size()
                + "\n\texpect bind users' num: " + expectBindUserNum
                + "\n\tbind accuracy: " + percent
                + "\n==========================================================");
        logger.info("");
        logger.info("");

        if (null == IS_SAVE_TO_DB || IS_SAVE_TO_DB.trim().toLowerCase().equals("true")) {
            qaDbUtil.saveBaiguoyuanMetrics(bindMetrics);
        }

        return result;
    }

    private String sendRequestOnly(String URL, String json) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJson(URL, json);
        return executor.getResponse();
    }

    private boolean postTransData(String beginTime) throws Exception {
        //交易记录上传 http://{{host}}/bind/receive


        boolean isDataExist = getTransValueAndUploadData(beginTime);
        if (! isDataExist) {
            return false;
        }

        return true;
    }

    String syncTime(String beginTime, String lenTime) throws Exception {
        String result = null;
        String baseTime = currentDate + " " + beginTime;
        String pattern = "yyyy-MM-dd hh:mm:ss";
        result = dt.getHistoryDate(pattern, baseTime, lenTime);
        if (null == result) {
            throw new Exception("video playing time and transaction time NOT sync");
        }
        result = dt.dateToTimestamp(pattern, result);
        return result;
    }

    boolean getTransValueAndUploadData(String beginTime) throws Exception {
        //beginTime: 10:31:55
        //get file content
        List<String> fileContent = fileUtil.getFileContent(TRANS_REPORT_FILE);
        if (null == fileContent || fileContent.size() == 0) {
            logger.info("no expect user data, return");
            return false;
        }
        ConcurrentHashMap<String, String> hm = new ConcurrentHashMap<>();

        for (String line : fileContent) {
            if (line.trim().startsWith("#")) {
                continue;
            } else {
                expectBindUserNum++;
                String[] items = line.split(",");
                //gaiguoyuan_1,00:00:00-00:01:26,女
                String[] lenShift = items[1].split("-");
                String startTime = syncTime(beginTime, lenShift[0]);
                String endTime = syncTime(beginTime, lenShift[1]);
                hm.put(KEY_USER_ID, items[0]);
                hm.put(KEY_START_TIME, startTime);
                hm.put(KEY_END_TIME, endTime);
                hm.put(KEY_GENDER, items[2]);
            }
            String json = generateTransValue(hm);
            sendRequestOnly(URL, json);

        }
        hm = null;
        fileContent = null;
        return true;
    }

    String generateTransValue(ConcurrentHashMap<String, String> hm) {
        //gender: 0-male, 1-female
        //shopid: 134
        //userid: customer id
        //{"age":35,"endTime":1563244347527,"gender":0,"matchTimes":0,"shopId":"134","startTime":1563244327527,"userId":"baiguoyuancustomer1","requestId":"abctest"}

        String json = "{\"age\":\"30\"," +
                "\"endTime\":\"" + hm.get(KEY_END_TIME) + "\"," +
                "\"gender\":\"" + hm.get(KEY_GENDER) + "\"," +
                "\"matchTimes\":\"0\"," +
                "\"shopId\":\"baiguoyuan\"," +
                "\"startTime\":\"" + hm.get(KEY_START_TIME) + "\"," +
                "\"userId\":\"" + hm.get(KEY_USER_ID)+ "\"," +
                "\"requestId\":\"" + UUID.randomUUID().toString() + "\"}";

        logger.info("post transaction data: " + json);
        return json;
    }

    private String getVideoStartTime() {
        String beginTime = "";
        String line = fileUtil.findLineByKey(EDGE_LOG, VIDEO_START_KEY);
        //[36moffice-150    |[0m W0716 10:31:55.850082        start to play video
        beginTime = line.substring(line.indexOf(":")-2, line.indexOf("."));

        logger.info("get video playing begin time: " + beginTime);


        return beginTime;
    }

    @BeforeSuite
    private void initial() {
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }
}
