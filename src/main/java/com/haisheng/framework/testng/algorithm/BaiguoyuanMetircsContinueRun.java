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

public class BaiguoyuanMetircsContinueRun {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private QADbUtil qaDbUtil = new QADbUtil();
    private DateTimeUtil dt = new DateTimeUtil();
    private FileUtil fileUtil = new FileUtil();

    private String TRANS_REPORT_DIR = System.getProperty("TRANS_REPORT_DIR");
    private String TRANS_REPORT_FILE = "";
    private String EDGE_LOG_DIR = System.getProperty("EDGE_LOG_DIR");
    private String EDGE_LOG_NAME = System.getProperty("EDGE_LOG_NAME");
    private String EDGE_LOG = "";
    private String PIC_PATH = System.getProperty("PIC_PATH");
    private String IS_PUSH_MSG = System.getProperty("IS_PUSH_MSG");
    private String IS_SAVE_TO_DB = System.getProperty("IS_SAVE_TO_DB");
    private String VIDEO_START_KEY = System.getProperty("VIDEO_START_KEY");
    private String VIDEO_SAMPLE_LIST = System.getProperty("VIDEO_SAMPLE_LIST");
    private String VIDEO_SAMPLE = "";
    private String SHOP_ID = System.getProperty("SHOP_ID");
    private String WAIT_TIME_SEC = System.getProperty("WAIT_TIME_SEC");
    private String RD_TRACE_ERROR_LOG = System.getProperty("RD_TRACE_ERROR_LOG");
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

    private LinkedHashMap<String, ReportBind> REPORT = null;
    private boolean MULTIPLE_VIDEO_BOOLEAN = false;



    class ReportBind {
        public String videoBeginTime;
        public int expectBindUserNum = 0;
        public int actualBindUserNum = 0;
        public int actualSucBindUserNum = 0;
        public String expectBindUsers = "";
        public String notBindUsers = "";
        public String expectSucBindUsers = "";
        public String bindWrongUsers = "";
    }

    @Test
    private void uploadTransData() throws Exception {
        if (IS_DEBUG) {
            PIC_PATH = "src/main/resources/csv/yuhaisheng";
            EDGE_LOG = "src/main/resources/csv/yuhaisheng/demo2.csv";
            TRANS_REPORT_FILE = "src/main/resources/test-res-repo/baiguoyuan-metircs/debug.csv";
            TRANS_REPORT_FILE = "/var/lib/jenkins/workspace/testbaiguoyuan/docker/trans/baiguoyuan_2019_07_17_12H_12.csv";
            VIDEO_START_KEY = "start to play video";
            RD_TRACE_ERROR_LOG = "src/main/resources/test-res-repo/baiguoyuan-metircs/error.log";
            IS_PUSH_MSG = "true";
            IS_SAVE_TO_DB = "false";
            VIDEO_SAMPLE = "baiguoyuan_2019_07_17_12H_1.mp4";
            EXPECT_BIND_NUM = 11;
            SHOP_ID = "1459";
        }

        runVideos();
    }

    private void runVideos() throws Exception {

        String[] videoArr = VIDEO_SAMPLE_LIST.split(",");
        REPORT = new LinkedHashMap<>();

        for (int i=0; i<videoArr.length; i++) {

            VIDEO_SAMPLE = videoArr[i].trim();
            String videoNameNoSuffix = VIDEO_SAMPLE.substring(0, VIDEO_SAMPLE.lastIndexOf(".mp4"));
            TRANS_REPORT_FILE = fileUtil.getCurrentDirFileWithoutDeepTraverse(TRANS_REPORT_DIR, videoNameNoSuffix+".csv");
            EDGE_LOG = EDGE_LOG_DIR.trim() + File.separator + videoNameNoSuffix + File.separator + EDGE_LOG_NAME;

            REPORT.put(videoNameNoSuffix, new ReportBind());

            if (i == 0) {
                if (videoArr.length > 1) {
                    MULTIPLE_VIDEO_BOOLEAN = true;
                    runEachVideo("0", true, false, false);
                } else {
                    runEachVideo(WAIT_TIME_SEC, true, true, Boolean.valueOf(IS_PUSH_MSG));
                }
            } else if (i == videoArr.length -1 ) {
                runEachVideo(WAIT_TIME_SEC, false, true, Boolean.valueOf(IS_PUSH_MSG));
            } else {
                runEachVideo("0", true, false, false);
            }


        }

    }



    private void runEachVideo(String waitTimeSec, boolean cleanDB, boolean printResult, boolean pushMsg) throws Exception {
        printProps();

        //get video playing time
        String beginTime = getVideoStartTime();
        Assert.assertNotNull(beginTime, "NOT found actually video start playing time");

        //upload transaction data to cloud
        boolean result = postTransData(beginTime, cleanDB);
        Assert.assertTrue(result, "no expect transaction data");

        //let algorithm work enough
        waitTime(waitTimeSec);

        if (printResult) {
            //print result
            getAndPrintMetrics();
        }

        if (pushMsg) {
            //push msg
            pushMsg();
        }

    }

    private void printProps() {
        logger.info("MULTIPLE_VIDEO_BOOLEAN: " + MULTIPLE_VIDEO_BOOLEAN);
        logger.info("TRANS_REPORT_FILE: " + TRANS_REPORT_FILE);
        logger.info("EDGE_LOG: " + EDGE_LOG);
        logger.info("PIC_PATH: " + PIC_PATH);
        logger.info("IS_PUSH_MSG: " + IS_PUSH_MSG);
        logger.info("IS_SAVE_TO_DB: " + IS_SAVE_TO_DB);
        logger.info("VIDEO_START_KEY: " + VIDEO_START_KEY);
        logger.info("VIDEO_SAMPLE: " + VIDEO_SAMPLE);
        logger.info("SHOP_ID: " + SHOP_ID);
        logger.info("WAIT_TIME_SEC: " + WAIT_TIME_SEC);
    }

    private void waitTime(String waitTimeSec) throws InterruptedException {
        //get bind-accuracy and bind-success-accuracy
        if (null == waitTimeSec) {
            waitTimeSec = "120";
        }

        if (waitTimeSec.trim().equals("0")) {
            return;
        }
        logger.info("");
        logger.info("");
        logger.info("sleep " + waitTimeSec + "s, to let cloud service work enough");
        logger.info("");
        logger.info("");
        Thread.sleep(Integer.parseInt(waitTimeSec)*1000);
    }

    private void pushMsg() {
        List<BaiguoyuanBindMetrics> accuracyList = qaDbUtil.getBaiguoyuanMetrics(currentDate, SHOP_ID);
        List<BaiguoyuanBindMetrics> pushList = accuracyList;
        if (! MULTIPLE_VIDEO_BOOLEAN) {
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

        if (MULTIPLE_VIDEO_BOOLEAN) {
            alarmPush.baiguoyuanAlarm(pushList, true);
        } else {
            alarmPush.baiguoyuanAlarm(pushList, false);
        }
    }

    private void getAndPrintMetrics() {

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

        if (MULTIPLE_VIDEO_BOOLEAN) {
            //statistic each video bind accuracy and bind suc
            for (String videoName : REPORT.keySet()) {
                //filter reslut by video
                videoName = videoName.trim();
                List<BaiguoyuanBindUser> currentVideoBindUserList = new ArrayList<>();
                for (BaiguoyuanBindUser bindUser : bindUserList) {
                    if (bindUser.getCustUserId().trim().contains(videoName)) {
                        currentVideoBindUserList.add(bindUser);
                    }
                }
                printCurrentVideoAccuracyAndSaveToDb(currentVideoBindUserList, REPORT.get(videoName).expectBindUserNum, videoName+".mp4");
            }

        } else {
            printCurrentVideoAccuracyAndSaveToDb(bindUserList, EXPECT_BIND_NUM, VIDEO_SAMPLE);
        }

    }



    private void printCurrentVideoAccuracyAndSaveToDb(List<BaiguoyuanBindUser> bindUserList, int expectBindNum, String video) {
        VIDEO_SAMPLE = video;
        BaiguoyuanBindMetrics bindAccuracy = new BaiguoyuanBindMetrics();
        String actualBindUserNum = calBindAccuracy(video, bindUserList, bindAccuracy, expectBindNum);

        BaiguoyuanBindMetrics bindSucAccuracy = new BaiguoyuanBindMetrics();
        String actualBindSucUserNum = calBindSucAccuracy(video, bindUserList, bindSucAccuracy);

        DecimalFormat df = new DecimalFormat("#.00");
        String bindAccuracyPercent = df.format(bindAccuracy.getAccuracy()*100) + "%";
        String bindSucAccuracyPercent = df.format(bindSucAccuracy.getAccuracy()*100) + "%";


        logger.info("");
        logger.info("");
        logger.info("\n=========================================================="
                + "\n\texpect bind users' num: " + expectBindNum
                + "\n\tactual bind users' num: " + actualBindUserNum
                + "\n\tactual bind success users' num: " + actualBindSucUserNum
                + "\n\tbind accuracy ratio: " + bindAccuracyPercent
                + "\n\tbind success accuracy ratio: " + bindSucAccuracyPercent
                + "\n\tshop id: " + SHOP_ID
                + "\n\tvideo: " + video
                + "\n==========================================================");
        logger.info("");
        logger.info("");

        if (null != IS_SAVE_TO_DB && IS_SAVE_TO_DB.trim().toLowerCase().equals("true")) {
            List<BaiguoyuanBindMetrics> bindMetricsList = new ArrayList<>();
            bindMetricsList.add(bindAccuracy);
            bindMetricsList.add(bindSucAccuracy);
            qaDbUtil.saveBaiguoyuanMetrics(bindMetricsList);
        }
    }

    private String calBindAccuracy(String video, List<BaiguoyuanBindUser> bindUserList, BaiguoyuanBindMetrics bindAccuracy, int expectBindNum) {
        bindAccuracy.setDate(currentDate);
        bindAccuracy.setMetrics(METRICS_BIND_ACCURACY);
        bindAccuracy.setVideo(video);
        bindAccuracy.setShopId(SHOP_ID);
        bindAccuracy.setExpectNum(expectBindNum);
        bindAccuracy.setActualNum(bindUserList.size());

        int actualBindUserNum = bindUserList.size();
        if ( expectBindNum > 0 ) {
            float accuracy = (float) actualBindUserNum/expectBindNum;
            bindAccuracy.setAccuracy(accuracy);
        } else {
            bindAccuracy.setAccuracy(0);
        }

        String videoName = video.substring(0, video.lastIndexOf(".mp4"));
        ReportBind reportBind = REPORT.get(videoName);
        String expectUsers = reportBind.expectBindUsers;
        String expectSucBindUsers = "";
        String notBindUsers = "";
        for (BaiguoyuanBindUser bindUser : bindUserList) {
            if (expectUsers.contains(bindUser.getCustUserId())) {
                expectSucBindUsers += bindUser.getCustUserId() + ", ";
            } else {
                notBindUsers += bindUser.getCustUserId() + ", ";
            }
        }
        reportBind.expectSucBindUsers = expectSucBindUsers;
        reportBind.actualBindUserNum = actualBindUserNum;
        reportBind.notBindUsers = notBindUsers;
        REPORT.put(videoName, reportBind);


        return String.valueOf(actualBindUserNum);
    }

    private String calBindSucAccuracy(String video, List<BaiguoyuanBindUser> bindUserList, BaiguoyuanBindMetrics bindSucAccuracy) {
        bindSucAccuracy.setDate(currentDate);
        bindSucAccuracy.setMetrics(METRICS_BIND_SUCCESS_ACCURACY);
        bindSucAccuracy.setVideo(video);
        bindSucAccuracy.setShopId(SHOP_ID);
        bindSucAccuracy.setExpectNum(bindUserList.size());


        String videoName = video.substring(0, video.lastIndexOf(".mp4"));
        ReportBind reportBind = REPORT.get(videoName);
        int actualBindSucUserNum = 0;
        String wrongBindUsers = "";
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
                wrongBindUsers += bindUser.getCustUserId() + ", ";
            }


        }
        reportBind.actualSucBindUserNum = actualBindSucUserNum;
        reportBind.bindWrongUsers = wrongBindUsers;
        REPORT.put(videoName, reportBind);

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
            //对应多人的情况，如：baiguoyuan_100_1.png baiguoyuan_100_2.png
            faceUrlList = fileUtil.getFiles(PIC_PATH, userId+"_");
        }

        return faceUrlList;
    }

    private String getSampleUserFaceUrlFromOss(String userId) {
        String ossRoot = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/Test/baiguoyuan/baiguoyuan/";
        String png = ossRoot + userId.trim() + ".png";

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
                    logger.info("faceUrl: " + picA);
                    logger.info("expect pic: " + picB);
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
                //加载http途径的图片
                java.net.URL url = new URL(urlOrPath);
                in = url.openStream();
            }else{ //加载本地路径的图片
                File file = new File(urlOrPath);
                if(!file.isFile() || !file.exists() || !file.canRead()){
                    logger.info("图片不存在或文件错误");
                    return;
                }
                in = new FileInputStream(file);
            }
            b = getByte(in); //调用方法，得到输出流的字节数组
            FileUtils.writeByteArrayToFile(new File(newFilePath), b);

        } catch (Exception e) {
            logger.error("读取图片发生异常:"+ e);
            return;
        }
    }

    private byte[] getByte(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buf=new byte[1024]; //缓存数组
            while(in.read(buf)!=-1){ //读取输入流中的数据放入缓存，如果读取完则循环条件为false;
                out.write(buf); //将缓存数组中的数据写入out输出流，如果需要写到文件，使用输出流的其他方法
            }
            out.flush();
            return out.toByteArray();	//将输出流的结果转换为字节数组的形式返回	（先执行finally再执行return	）
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

    private boolean postTransData(String beginTime, boolean cleanDB) throws Exception {

        //clean today data in db
        if (cleanDB) {
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
                    String error = "trans csv file NOT correct, please check file: " + TRANS_REPORT_FILE;
                    throw new Exception(error);
                }
                //gaiguoyuan_1,00:00:00-00:01:26,女
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
                logger.info("transaction begin time: " + beginTime + ", shift time range: " + shiftBegin + "-" + shiftEnd);
            }
            String json = generateTransValue(hm);
            sendRequestOnly(URL, json);

        }

        EXPECT_BIND_NUM = expectUserSet.size();
        String videoName = VIDEO_SAMPLE.substring(0, VIDEO_SAMPLE.lastIndexOf(".mp4"));
        ReportBind reportBind = REPORT.get(videoName);
        reportBind.expectBindUsers = expectUserSet.toString();
        reportBind.expectBindUserNum = expectUserSet.size();
        REPORT.put(videoName, reportBind);

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

        String videoName = VIDEO_SAMPLE.substring(0, VIDEO_SAMPLE.lastIndexOf(".mp4"));
        ReportBind reportBind = REPORT.get(videoName);
        reportBind.videoBeginTime = beginTime;
        REPORT.put(videoName, reportBind);

        return beginTime;
    }

    @BeforeSuite
    private void initial() {
        qaDbUtil.openConnection();

        logger.info("init");
        //clean today data in db
        qaDbUtil.removeBaiguoyuanBindUser(currentDate, SHOP_ID);

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
        if (null != REPORT && REPORT.size() > 0 && null != RD_TRACE_ERROR_LOG) {
            List<String> content = new ArrayList<>();
            for (String videoName : REPORT.keySet()) {
                ReportBind reportBind = REPORT.get(videoName);
                String sep = "\n";
                String indent = "    ";
                String line = sep + "video name: " + videoName + sep
                            + indent + "videoBeginTime: " + reportBind.videoBeginTime + sep
                            + indent + "expectBindUserNum: " + reportBind.expectBindUserNum + sep
                            + indent + "actualBindUserNum: " + reportBind.actualBindUserNum + sep
                            + indent + "actualSucBindUserNum: " + reportBind.actualSucBindUserNum + sep
                            + indent + "expectBindUsers: " + reportBind.expectBindUsers + sep
                            + indent + "notBindUsers: " + reportBind.notBindUsers + sep
                            + indent + "expectSucBindUsers: " + reportBind.expectSucBindUsers + sep
                            + indent + "bindWrongUsers: " + reportBind.bindWrongUsers + sep;
                content.add(line);
            }
            fileUtil.writeContentToFile(RD_TRACE_ERROR_LOG, FACE_WRONG_LIST);
        }

    }
}
