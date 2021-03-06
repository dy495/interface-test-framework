package com.haisheng.framework.testng.algorithm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.model.bean.BaiguoyuanBindMetrics;
import com.haisheng.framework.model.bean.BaiguoyuanBindUser;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BaiguoyuanMetircs {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private QADbUtil qaDbUtil = new QADbUtil();
    private DateTimeUtil dt = new DateTimeUtil();
    private FileUtil fileUtil = new FileUtil();

    private String TRANS_REPORT_FILE = System.getProperty("TRANS_REPORT_FILE");
    private String EDGE_LOG = System.getProperty("EDGE_LOG");
    private String PIC_PATH = System.getProperty("PIC_PATH");
    private String IS_PUSH_MSG = System.getProperty("IS_PUSH_MSG");
    private String IS_SAVE_TO_DB = System.getProperty("IS_SAVE_TO_DB");
    private String VIDEO_START_KEY = System.getProperty("VIDEO_START_KEY");
    private String VIDEO_SAMPLE = System.getProperty("VIDEO_SAMPLE");
    private String SHOP_ID = System.getProperty("SHOP_ID");
    private String SKIP_CLEAN_DB = System.getProperty("SKIP_CLEAN_DB");
    private String WAIT_TIME_SEC = System.getProperty("WAIT_TIME_SEC");
    private String RD_TRACE_ERROR_LOG = System.getProperty("RD_TRACE_ERROR_LOG");
    private String SKIP_GET_RESULT = System.getProperty("SKIP_GET_RESULT");
    private String CONTINUE_RUN_GET_RESULT = System.getProperty("CONTINUE_RUN_GET_RESULT");
    private String KEY_GENDER = "gender";
    private String KEY_START_TIME = "startTime";
    private String KEY_END_TIME = "endTime";
    private String KEY_USER_ID = "userId";
    private String KEY_SHITF_START_TIME = "shiftStartTime";
    private String KEY_SHITF_END_TIME = "shiftEndTime";
    private String KEY_BASE_TIME = "baseTime";

    private String METRICS_BIND_ACCURACY = "bind_accuracy";
    private String METRICS_BIND_SUCCESS_ACCURACY = "bind_success_accuracy";

    private boolean IS_DEBUG = false;
    private String currentDate = dt.getHistoryDate(0);
    private int EXPECT_BIND_NUM = 0;
    private String VIDEO_BEGIN_TIME = "";
    private float IS_SAME_VALUE = (float) 0.5;

    private String URL = "http://39.106.233.43/bind/receive";
    //    private String FACE_COMPARE_URL = "http://39.97.5.67/lab/DAILY/comp/FACE/file/";
    private String FACE_COMPARE_URL = "http://39.97.5.67/lab/DAILY/comp/FACE";

    private List<String> FACE_WRONG_LIST = new ArrayList<>();
    private String EXPECT_BIND_USER_NUM_LOG = "";


    @Test
    private void uploadTransData() throws Exception {
        if (IS_DEBUG) {
            PIC_PATH = "src/main/resources/trans/yuhaisheng";
            EDGE_LOG = "/Users/yuhaisheng/logs/baiguoyuan/logs/6611113056961536/baiguoyuan_2019_07_17_12H_10/edge-service.INFO.upload";
            TRANS_REPORT_FILE = "/Users/yuhaisheng/logs/baiguoyuan/trans/baiguoyuan_2019_07_17_12H_10.trans";
            VIDEO_START_KEY = "start to play video";
            RD_TRACE_ERROR_LOG = "src/main/resources/test-res-repo/baiguoyuan-metircs/error.log";
            IS_PUSH_MSG = "true";
            IS_SAVE_TO_DB = "false";
            VIDEO_SAMPLE = "baiguoyuan_2019_07_17_12H_10.mp4";
            SHOP_ID = "1459";
            SKIP_GET_RESULT = "false";
            CONTINUE_RUN_GET_RESULT = "false";
        }

        printProps();

        //get video playing time
        String beginTime = getVideoStartTime();
        Assert.assertNotNull(beginTime, "NOT found actually video start playing time");

        //upload transaction data to cloud
        boolean result = postTransData(beginTime);
        Assert.assertTrue(result, "no expect transaction data");

        //let algorithm work enough
        waitTime();

        //print result
        getAndPrintMetrics();

        //push msg
        pushMsg();
    }

    private void printProps() {
        logger.info("TRANS_REPORT_FILE: " + TRANS_REPORT_FILE);
        logger.info("EDGE_LOG: " + EDGE_LOG);
        logger.info("PIC_PATH: " + PIC_PATH);
        logger.info("IS_PUSH_MSG: " + IS_PUSH_MSG);
        logger.info("IS_SAVE_TO_DB: " + IS_SAVE_TO_DB);
        logger.info("VIDEO_START_KEY: " + VIDEO_START_KEY);
        logger.info("VIDEO_SAMPLE: " + VIDEO_SAMPLE);
        logger.info("SHOP_ID: " + SHOP_ID);
        logger.info("SKIP_CLEAN_DB: " + SKIP_CLEAN_DB);
        logger.info("WAIT_TIME_SEC: " + WAIT_TIME_SEC);
    }

    private void waitTime() throws InterruptedException {
        //get bind-accuracy and bind-success-accuracy
        if (null == WAIT_TIME_SEC) {
            WAIT_TIME_SEC = "120";
        }
        logger.info("");
        logger.info("");
        logger.info("sleep " + WAIT_TIME_SEC + "s, to let cloud service work enough");
        logger.info("");
        logger.info("");
        Thread.sleep(Integer.parseInt(WAIT_TIME_SEC)*1000);
    }

    private void pushMsg() {
        if (null == IS_PUSH_MSG || IS_PUSH_MSG.trim().toLowerCase().equals("false")) {
            return;
        }

        List<BaiguoyuanBindMetrics> accuracyList = qaDbUtil.getBaiguoyuanMetrics(currentDate, SHOP_ID);
        List<BaiguoyuanBindMetrics> pushList = accuracyList;
        if (! IS_PUSH_MSG.trim().toLowerCase().equals("all")) {
            //filter current video to push
            pushList = new ArrayList<>();
            for (BaiguoyuanBindMetrics item : accuracyList) {
                if (item.getVideo().equals(VIDEO_SAMPLE)) {
                    pushList.add(item);
                }
            }
        }


        AlarmPush alarmPush = new AlarmPush();
        alarmPush.setDingWebhook(DingWebhook.APP_BAIGUOYUAN_ALARM_GRP);
        if (IS_DEBUG) {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }

        if (null != CONTINUE_RUN_GET_RESULT && CONTINUE_RUN_GET_RESULT.trim().toLowerCase().equals("true")) {
            alarmPush.baiguoyuanAlarm(pushList, true);
        } else {
            alarmPush.baiguoyuanAlarm(pushList, false);
        }
    }

    private boolean getAndPrintMetrics() {

        if (null != SKIP_GET_RESULT && SKIP_GET_RESULT.trim().toLowerCase().equals("true")) {
            //skip this method by manually
            return true;
        }

        boolean result = true;
        List<BaiguoyuanBindUser> bindUserList = qaDbUtil.getBaiguoyuanBindAccuracy(currentDate, SHOP_ID);
        if (null == bindUserList || bindUserList.size() < 1) {
            logger.info("");
            logger.info("");
            logger.info("\n=========================================================="
                    + "\n\tNO bind user found"
                    + "\n==========================================================");
            logger.info("");
            logger.info("");
            bindUserList = new ArrayList<>();
        }

        if (null != CONTINUE_RUN_GET_RESULT && CONTINUE_RUN_GET_RESULT.trim().toLowerCase().equals("true")) {
            EXPECT_BIND_NUM += getTotalExpectBindUserNum();
            VIDEO_SAMPLE = VIDEO_SAMPLE.substring(0, VIDEO_SAMPLE.lastIndexOf("_"));
        }
        BaiguoyuanBindMetrics bindAccuracy = new BaiguoyuanBindMetrics();
        String actualBindUserNum = calBindAccuracy(bindUserList, bindAccuracy);

        BaiguoyuanBindMetrics bindSucAccuracy = new BaiguoyuanBindMetrics();
        String actualBindSucUserNum = calBindSucAccuracy(bindUserList, bindSucAccuracy);

        DecimalFormat df = new DecimalFormat("#.00");
        String bindAccuracyPercent = df.format(bindAccuracy.getAccuracy()*100) + "%";
        String bindSucAccuracyPercent = df.format(bindSucAccuracy.getAccuracy()*100) + "%";


        logger.info("");
        logger.info("");
        logger.info("\n=========================================================="
                + "\n\texpect bind users' num: " + EXPECT_BIND_NUM
                + "\n\tactual bind users' num: " + actualBindUserNum
                + "\n\tactual bind success users' num: " + actualBindSucUserNum
                + "\n\tbind accuracy ratio: " + bindAccuracyPercent
                + "\n\tbind success accuracy ratio: " + bindSucAccuracyPercent
                + "\n\tshop id: " + SHOP_ID
                + "\n\tvideo: " + VIDEO_SAMPLE
                + "\n==========================================================");
        logger.info("");
        logger.info("");

        if (null != IS_SAVE_TO_DB && IS_SAVE_TO_DB.trim().toLowerCase().equals("true")) {
            List<BaiguoyuanBindMetrics> bindMetricsList = new ArrayList<>();
            bindMetricsList.add(bindAccuracy);
            bindMetricsList.add(bindSucAccuracy);
            qaDbUtil.saveBaiguoyuanMetrics(bindMetricsList);
        }

        return result;
    }

    private String calBindAccuracy(List<BaiguoyuanBindUser> bindUserList, BaiguoyuanBindMetrics bindAccuracy) {
        bindAccuracy.setDate(currentDate);
        bindAccuracy.setMetrics(METRICS_BIND_ACCURACY);
        bindAccuracy.setVideo(VIDEO_SAMPLE);
        bindAccuracy.setShopId(SHOP_ID);
        bindAccuracy.setExpectNum(EXPECT_BIND_NUM);
        bindAccuracy.setActualNum(bindUserList.size());

        int actualBindUserNum = bindUserList.size();
        if ( EXPECT_BIND_NUM > 0 ) {
            float accuracy = (float) actualBindUserNum/EXPECT_BIND_NUM;
            bindAccuracy.setAccuracy(accuracy);
        } else {
            bindAccuracy.setAccuracy(0);
        }


        return String.valueOf(actualBindUserNum);
    }

    private String calBindSucAccuracy(List<BaiguoyuanBindUser> bindUserList, BaiguoyuanBindMetrics bindSucAccuracy) {
        bindSucAccuracy.setDate(currentDate);
        bindSucAccuracy.setMetrics(METRICS_BIND_SUCCESS_ACCURACY);
        bindSucAccuracy.setVideo(VIDEO_SAMPLE);
        bindSucAccuracy.setShopId(SHOP_ID);
        bindSucAccuracy.setExpectNum(bindUserList.size());


        int actualBindSucUserNum = 0;
        for (BaiguoyuanBindUser bindUser : bindUserList) {
//            List<File> expectFaceUrl = getSampleUserFaceUrl(bindUser.getCustUserId()); //local saved pics
            String expectFaceUrl = getSampleUserFaceUrlFromOss(bindUser.getCustUserId()); //oss saved pics
            JSONArray personListJson = JSON.parseArray(bindUser.getPersonList());
            boolean isSame = false;
            for (int i=0; i<personListJson.size(); i++) {
                String faceUrl = personListJson.getJSONObject(i).getString("faceUrl");
                isSame = isPictureSame(faceUrl, expectFaceUrl);
            }
            if (isSame) {
                actualBindSucUserNum++;
            } else {
                logger.error(bindUser.getCustUserId() + ", faceUrl NOT same as expect\n");
            }

        }
        bindSucAccuracy.setActualNum(actualBindSucUserNum);
        if (bindUserList.size() > 0) {
            float accuracy = (float) actualBindSucUserNum/bindUserList.size();
            bindSucAccuracy.setAccuracy(accuracy);
        } else {
            bindSucAccuracy.setAccuracy(0);
        }

        return String.valueOf(actualBindSucUserNum);
    }
    private List<File> getSampleUserFaceUrl(String userId) {
        List<File> faceUrlList = fileUtil.getFiles(PIC_PATH, userId+".");
        if (null == faceUrlList || faceUrlList.size() == 0) {
            //??????????????????????????????baiguoyuan_100_1.png baiguoyuan_100_2.png
            faceUrlList = fileUtil.getFiles(PIC_PATH, userId+"_");
        }

        return faceUrlList;
    }

    private String getSampleUserFaceUrlFromOss(String userId) {
        String ossRoot = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/Test/baiguoyuan/baiguoyuan/REPLACEUSERID.png";
        String user = userId.replaceAll(" ", "").replaceAll(" ", "").replaceAll("\\uFEFF", ""); //???????????????+BOM
        String png = ossRoot.replaceAll("REPLACEUSERID", user);

        return png;
    }

    private boolean isPictureSame(String picA, String picB) {
        boolean result = false;

        try {
            HttpExecutorUtil executorUtil = new HttpExecutorUtil();
            String json = "{\"pictureA\":\"" + picA + "\"," +
                    "\"pictureB\":\"" + picB + "\"," +
                    "\"isImageA\":\"true\",\"isImageB\":\"false\"}";
            Map<String, Object> headers = new ConcurrentHashMap<>();
            headers.put("session_token", "123456");
            executorUtil.doPostJsonWithHeaders(FACE_COMPARE_URL, json, headers);
            String userId = picB.substring(picB.lastIndexOf("/"), picB.lastIndexOf(".png"));
            if (executorUtil.getStatusCode() == 200) {
                JSONArray response = JSON.parseArray(executorUtil.getResponse());
                if (null == response) {
                    logger.info("faceUrl: " + picA);
                    logger.info("expect pic: " + picB);
                    logger.error("response is NULL, compare face failure");
                    return false;
                }
                float similary = response.getJSONObject(0).getFloat("similarity");
                if (similary > IS_SAME_VALUE) {
                    result = true;
                } else {
                    logger.info("try isImageB: true ");
                    json = "{\"pictureA\":\"" + picA + "\"," +
                            "\"pictureB\":\"" + picB + "\"," +
                            "\"isImageA\":\"true\",\"isImageB\":\"true\"}";
                    executorUtil.doPostJsonWithHeaders(FACE_COMPARE_URL, json, headers);
                    if (executorUtil.getStatusCode() == 200) {
                        response = JSON.parseArray(executorUtil.getResponse());
                        if (null == response) {
                            logger.info("faceUrl: " + picA);
                            logger.info("expect pic: " + picB);
                            logger.error("response is NULL, compare face failure");
                            return false;
                        }
                        similary = response.getJSONObject(0).getFloat("similarity");
                        if (similary > IS_SAME_VALUE) {
                            result = true;
                        } else {
                            logger.info("faceUrl: " + picA);
                            logger.info("expect pic: " + picB);
                            FACE_WRONG_LIST.add("video: " + VIDEO_SAMPLE + ", "
                                    + "userId: " + userId + ", "
                                    + "video start time: " + VIDEO_BEGIN_TIME + ", "
                                    + "expect pic: " + picB + ", "
                                    + "actual pic: " + picA);
                        }
                    }


                    logger.info("faceUrl: " + picA);
                    logger.info("expect pic: " + picB);
                    FACE_WRONG_LIST.add("video: " + VIDEO_SAMPLE + ", "
                            + "userId: " + userId + ", "
                            + "video start time: " + VIDEO_BEGIN_TIME + ", "
                            + "expect pic: " + picB + ", "
                            + "actual pic: " + picA);

                }
            } else {
                logger.info("try isImageB: true ");
                json = "{\"pictureA\":\"" + picA + "\"," +
                        "\"pictureB\":\"" + picB + "\"," +
                        "\"isImageA\":\"true\",\"isImageB\":\"true\"}";
                executorUtil.doPostJsonWithHeaders(FACE_COMPARE_URL, json, headers);
                if (executorUtil.getStatusCode() == 200) {
                    JSONArray response = JSON.parseArray(executorUtil.getResponse());
                    if (null == response) {
                        logger.info("faceUrl: " + picA);
                        logger.info("expect pic: " + picB);
                        logger.error("response is NULL, compare face failure");
                        return false;
                    }
                    float similary = response.getJSONObject(0).getFloat("similarity");
                    if (similary > IS_SAME_VALUE) {
                        result = true;
                    } else {
                        logger.info("faceUrl: " + picA);
                        logger.info("expect pic: " + picB);
                        FACE_WRONG_LIST.add("video: " + VIDEO_SAMPLE + ", "
                                + "userId: " + userId + ", "
                                + "video start time: " + VIDEO_BEGIN_TIME + ", "
                                + "expect pic: " + picB + ", "
                                + "actual pic: " + picA);
                    }
                }

            }

        } catch (Exception e) {
            logger.info(e.toString());
        }

        return result;
    }

    private boolean isPictureSame(String picA, List<File> expectFaceUrl) {
        boolean result = false;

        for (File faceFile : expectFaceUrl) {
            try {
                HttpExecutorUtil executorUtil = new HttpExecutorUtil();
                String downloadImagePath = faceFile.getParent() + "/" + "faceUrl.png";
                downloadImage(picA, downloadImagePath);
                boolean isSuccess = executorUtil.compareImageRequest(FACE_COMPARE_URL, downloadImagePath, faceFile.getAbsolutePath());
                if (isSuccess) {
                    JSONArray response = JSON.parseArray(executorUtil.getResponse());
                    float similary = response.getJSONObject(0).getFloat("similarity");
                    if (similary > IS_SAME_VALUE) {
                        result = true;
                    } else {
                        logger.info("faceUrl: " + picA);
                        logger.info("expect pic: " + faceFile.getName());
                    }
                }

            } catch (Exception e) {
                logger.info(e.toString());
            }
        }

        return result;
    }

    private void downloadImage(String urlOrPath, String newFilePath){
        InputStream in = null;
        try {
            byte[] b ;
            if(urlOrPath.toLowerCase().startsWith("http")){
                //??????http???????????????
                java.net.URL url = new URL(urlOrPath);
                in = url.openStream();
            }else{ //???????????????????????????
                File file = new File(urlOrPath);
                if(!file.isFile() || !file.exists() || !file.canRead()){
                    logger.info("??????????????????????????????");
                    return;
                }
                in = new FileInputStream(file);
            }
            b = getByte(in); //?????????????????????????????????????????????
            FileUtils.writeByteArrayToFile(new File(newFilePath), b);

        } catch (Exception e) {
            logger.error("????????????????????????:"+ e);
            return;
        }
    }

    private byte[] getByte(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buf=new byte[1024]; //????????????
            while(in.read(buf)!=-1){ //???????????????????????????????????????????????????????????????????????????false;
                out.write(buf); //?????????????????????????????????out?????????????????????????????????????????????????????????????????????
            }
            out.flush();
            return out.toByteArray();	//?????????????????????????????????????????????????????????	????????????finally?????????return	???
        } finally{
            if(in!=null){
                in.close();
            }
            if(out!=null){
                out.close();
            }
        }
    }

    private String sendRequestOnly(String URL, String json) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJson(URL, json);
        return executor.getResponse();
    }

    private boolean postTransData(String beginTime) throws Exception {

        //clean today data in db
        if (null != SKIP_CLEAN_DB && SKIP_CLEAN_DB.trim().toLowerCase().equals("false")) {
            qaDbUtil.removeBaiguoyuanBindUser(currentDate, SHOP_ID);
        }

        boolean isDataExist = getTransValueAndUploadData(beginTime);
        if (! isDataExist) {
            return false;
        }

        return true;
    }

    String syncTime(String beginTime, String lenTime) throws Exception {
        String result = null;
        String baseTime = currentDate + " " + beginTime;
        String pattern = "yyyy-MM-dd HH:mm:ss"; //HH: 24h, hh:12h

        String[] lenTimeArray = lenTime.split(":");
        if (lenTimeArray.length < 2) {
            logger.error("shift time len == 0, expexct hh:mm:ss");
            throw new Exception("video playing time and transaction time NOT sync");
        } else if (lenTimeArray.length == 2) {
            lenTime = "00:" + lenTime;
        }

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
        EXPECT_BIND_NUM = 0;
        ConcurrentHashMap<String, String> hm = new ConcurrentHashMap<>();
        HashSet<String> expectUserSet = new HashSet<>();

        for (String line : fileContent) {
            if (line.trim().startsWith("#") || line.trim().indexOf(",")<0 || line.trim().length()<5) {
                continue;
            } else {
                EXPECT_BIND_NUM++;
                String[] items = line.split(",");
                if (items.length < 3) {
                    String error = "trans trans file NOT correct, please check file: " + TRANS_REPORT_FILE;
                    throw new Exception(error);
                }
                //18210111234,00:00:00-00:01:26,???
                String[] lenShift = items[1].split("-");
                String startTime = null;
                String endTime = null;
                String shiftBegin = null;
                String shiftEnd = null;
                if (lenShift.length < 2) {
                    endTime = syncTime(beginTime, lenShift[0]);
                    startTime = endTime;
                    shiftBegin = lenShift[0];
                    shiftEnd = shiftBegin;
                } else {
                    startTime = syncTime(beginTime, lenShift[0]);
                    endTime = syncTime(beginTime, lenShift[1]);
                    shiftBegin = lenShift[0];
                    shiftEnd = lenShift[1];
                }

                hm.put(KEY_USER_ID, items[0].trim());
                hm.put(KEY_START_TIME, startTime);
                hm.put(KEY_END_TIME, endTime);
                hm.put(KEY_GENDER, items[2]);
                hm.put(KEY_SHITF_START_TIME, shiftBegin);
                hm.put(KEY_SHITF_END_TIME, shiftEnd);
                hm.put(KEY_BASE_TIME, beginTime);

                expectUserSet.add(items[0].trim());
                EXPECT_BIND_NUM = expectUserSet.size();

                logger.info("transaction begin time: " + beginTime + ", shift time range: " + shiftBegin + "-" + shiftEnd);
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
                "\"shopId\":\"" + SHOP_ID + "\"," +
                "\"startTime\":\"" + hm.get(KEY_START_TIME) + "\"," +
                "\"userId\":\"" + hm.get(KEY_USER_ID)+ "\"," +
                "\"requestId\":\"" + UUID.randomUUID().toString()
                + "-baseTime-" + hm.get(KEY_BASE_TIME)
                + "-startshift-" + hm.get(KEY_SHITF_START_TIME)
                + "-endshift-" + hm.get(KEY_SHITF_END_TIME) + "\"}";

        logger.info("post transaction data: " + json);
        return json;
    }

    private String getVideoStartTime() {
        String beginTime = "";
        logger.info("get video start time line from log: " + EDGE_LOG);
        String line = fileUtil.findLineByKey(EDGE_LOG, VIDEO_START_KEY);
        //[36moffice-150    |[0m W0716 10:31:55.850082        start to play video
        logger.info("video start time line: " + line);
        int charBegin = line.indexOf(":");
        if (charBegin < 2 ) {
            //handle 9:9:9
            charBegin -= 1;
        } else {
            charBegin -= 2;
        }
        beginTime = line.substring(charBegin, line.indexOf("."));

        logger.info("get video playing begin time: " + beginTime);

        VIDEO_BEGIN_TIME = beginTime;
        return beginTime;
    }

    private int getTotalExpectBindUserNum() {
        int num = 0;

        if (null == EXPECT_BIND_USER_NUM_LOG) {
            return num;
        }

        List<String> content = fileUtil.getFileContent(EXPECT_BIND_USER_NUM_LOG);
        for (String line : content) {
            num += Integer.parseInt(line.trim());
        }
        return num;
    }

    @BeforeSuite
    private void initial() {
        qaDbUtil.openConnection();

        logger.info("init");
        //clean today data in db
        if (null != SKIP_CLEAN_DB && SKIP_CLEAN_DB.trim().toLowerCase().equals("false")) {
            qaDbUtil.removeBaiguoyuanBindUser(currentDate, SHOP_ID);
        }

        if (null != TRANS_REPORT_FILE && TRANS_REPORT_FILE.trim().length() > 2) {
            EXPECT_BIND_USER_NUM_LOG = TRANS_REPORT_FILE.substring(0, TRANS_REPORT_FILE.lastIndexOf("/")) +
                    File.separator + "expectBindUserNum.log";
            logger.info("EXPECT_BIND_USER_NUM_LOG: " + EXPECT_BIND_USER_NUM_LOG);
        }

    }

    @AfterSuite
    public void clean() {
        logger.info("clean");
        qaDbUtil.closeConnection();
        if (null != FACE_WRONG_LIST && FACE_WRONG_LIST.size() > 0 && null != RD_TRACE_ERROR_LOG) {
            fileUtil.writeContentToFile(RD_TRACE_ERROR_LOG, FACE_WRONG_LIST);
        }

        //set expect users' num to local file
        List<String> content = fileUtil.getFileContent(EXPECT_BIND_USER_NUM_LOG);
        if (null == content) {
            content = new ArrayList<>();
        }
        content.add(String.valueOf(EXPECT_BIND_NUM));
        fileUtil.writeContentToFile(EXPECT_BIND_USER_NUM_LOG, content);

    }
}
