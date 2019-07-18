package com.haisheng.framework.testng.algorithm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.model.bean.BaiguoyuanBindMetrics;
import com.haisheng.framework.model.bean.BaiguoyuanBindUser;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    private String KEY_GENDER = "gender";
    private String KEY_START_TIME = "startTime";
    private String KEY_END_TIME = "endTime";
    private String KEY_USER_ID = "userId";
    private String KEY_SHITF_START_TIME = "shiftStartTime";
    private String KEY_SHITF_END_TIME = "shiftEndTime";

    private String METRICS_BIND_ACCURACY = "bind_accuracy";
    private String METRICS_BIND_SUCCESS_ACCURACY = "bind_success_accuracy";

    private boolean IS_DEBUG = false;
    private String currentDate = dt.getHistoryDate(0);
    private int expectBindUserNum = 0;
    private float IS_SAME_VALUE = (float) 0.8;

    private String URL = "http://39.106.233.43/bind/receive";
    private String FACE_COMPARE_URL = "http://39.97.5.67/lab/DAILY/comp/FACE/file/";



    @Test
    private void uploadTransData() throws Exception {
        if (IS_DEBUG) {
            PIC_PATH = "src/main/resources/csv/yuhaisheng";
            EDGE_LOG = "src/main/resources/csv/yuhaisheng/demo2.csv";
            TRANS_REPORT_FILE = "src/main/resources/test-res-repo/baiguoyuan-metircs/debug.csv";
            VIDEO_START_KEY = "start to play video";
            IS_PUSH_MSG = "false";
            IS_SAVE_TO_DB = "false";
            VIDEO_SAMPLE = "baiguoyuan_2019_07_14_18_1.mp4";
            expectBindUserNum = 11;
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
        if (IS_DEBUG) {
            alarmPush.setDingWebhook(DingWebhook.AD_GRP);
        }
        alarmPush.baiguoyuanAlarm(accuracyList);
    }

    private boolean getAndPrintMetrics() {
        boolean result = true;
        List<BaiguoyuanBindUser> bindUserList = qaDbUtil.getBaiguoyuanBindAccuracy(currentDate);
        if (null == bindUserList || bindUserList.size() < 1) {
            logger.info("");
            logger.info("");
            logger.info("\n=========================================================="
                        + "\n\tNO bind user found"
                      + "\n==========================================================");
            logger.info("");
            logger.info("");
            return false;
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
                + "\n\texpect bind users' num: " + expectBindUserNum
                + "\n\tactual bind users' num: " + actualBindUserNum
                + "\n\tactual bind success users' num: " + actualBindSucUserNum
                + "\n\tbind accuracy ratio: " + bindAccuracyPercent
                + "\n\tbind success accuracy ratio: " + bindSucAccuracyPercent
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

        int actualBindUserNum = bindUserList.size();
        float accuracy = (float) actualBindUserNum/expectBindUserNum;
        bindAccuracy.setAccuracy(accuracy);
        bindAccuracy.setVideo(VIDEO_SAMPLE);

        return String.valueOf(actualBindUserNum);
    }

    private String calBindSucAccuracy(List<BaiguoyuanBindUser> bindUserList, BaiguoyuanBindMetrics bindSucAccuracy) {
        bindSucAccuracy.setDate(currentDate);
        bindSucAccuracy.setMetrics(METRICS_BIND_SUCCESS_ACCURACY);
        bindSucAccuracy.setVideo(VIDEO_SAMPLE);

        int actualBindSucUserNum = 0;
        for (BaiguoyuanBindUser bindUser : bindUserList) {
            List<File> expectFaceUrl = getSampleUserFaceUrl(bindUser.getCustUserId());
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
        float accuracy = (float) actualBindSucUserNum/bindUserList.size();
        bindSucAccuracy.setAccuracy(accuracy);

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

    private boolean postTransData(String beginTime) throws Exception {

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
                hm.put(KEY_SHITF_START_TIME, lenShift[0]);
                hm.put(KEY_SHITF_END_TIME, lenShift[1]);

                logger.info("transaction begin time: " + beginTime + ", shift time range: " + lenShift[0] + "-" + lenShift[1]);
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
                "\"shopId\":\"1411\"," +
                "\"startTime\":\"" + hm.get(KEY_START_TIME) + "\"," +
                "\"userId\":\"" + hm.get(KEY_USER_ID)+ "\"," +
                "\"requestId\":\"" + UUID.randomUUID().toString() + "-start-" + hm.get(KEY_SHITF_START_TIME) + "-end-" + hm.get(KEY_SHITF_END_TIME) + "\"}";

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
