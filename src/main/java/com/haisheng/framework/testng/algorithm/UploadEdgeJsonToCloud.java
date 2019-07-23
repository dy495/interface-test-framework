package com.haisheng.framework.testng.algorithm;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadEdgeJsonToCloud {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private DateTimeUtil dt = new DateTimeUtil();
    private String TODAY = dt.getHistoryDate(0);
    private FileUtil fileUtil = new FileUtil();

    private String JSON_DIR = System.getProperty("JSON_DIR");
    private String JSON_DIR_CORRECT = JSON_DIR + "/" + "correct";
    private String JSON_DIR_SHIFT = JSON_DIR + "/" + "timeshift";
    private String EDGE_LOG = System.getProperty("EDGE_LOG");
    private String JSON_UPLOAD_LOG = EDGE_LOG + ".upload";
    private String VIDEO_START_KEY = System.getProperty("VIDEO_START_KEY");
    private String VIDEO_CREATE_LOG_KEY = System.getProperty("VIDEO_CREATE_LOG_KEY");

    private String START_TIME = "start_time";
    private String END_TIME = "end_time";
    private String FRAME_TIME = "frame_time";
    private String TIME = "time";
    private String DATA = "data";
    private String BIZ_DATA = "biz_data";
    private String TRACE = "trace";
    private String FACE_DATA = "face_data";

    private String PATTERN = "yyyy-MM-dd HH:mm:ss"; //HH: 24h, hh:12h
    private String VIDEO_PLAY_DATE = null;
    private String VIDEO_PLAY_DATE_MYPATTERN = null;
    private String VIDEO_BEGIN_TIME = null;
    private long REQUEST_UPLOAD_BASE_TIME = 0;

    private boolean IS_DEBUG = false;


    @Test
    private void uploadEdgeJsonToCloud() throws Exception {

        if(IS_DEBUG) {
            JSON_DIR = "/Users/yuhaisheng/jason/document/work/项目/百果园/request/6611113056961536/test";
            JSON_DIR_CORRECT = JSON_DIR + "/" + "correct";
            JSON_DIR_SHIFT = JSON_DIR + "/" + "timeshift";
            EDGE_LOG = "/Users/yuhaisheng/jason/document/work/项目/百果园/logs/6605924443128832/edge-service.INFO";
            JSON_UPLOAD_LOG = EDGE_LOG + ".upload";
            VIDEO_START_KEY = "start to play video";
            VIDEO_CREATE_LOG_KEY = "file created";
        }

        printPropertyParam();


        //get the json files which include time shift
        List<File> fileList = getTimeshiftFileList();


        //get correct timestamp json files
        List<File> fileCorrectList = getCorrectTimestampFileList(fileList);

        //send json to cloud
        sendJsonFileDataToCloud(fileCorrectList);


    }

    private void printPropertyParam() {
        logger.info("JSON_DIR: " + JSON_DIR);
        logger.info("EDGE_LOG: " + EDGE_LOG);
    }

    private List<File> getCorrectTimestampFileList(List<File> fileList) throws Exception {
        // correct timestamp in original json file, and save file to correct dir
        logger.info("correct timestamp in original json file, and save file to correct dir");

        String currentTime = TODAY + " " + dt.getCurrentHourMinutesSec();
        String nextminuteStamp = dt.getHistoryDate(PATTERN,currentTime, "00:01:00");
        REQUEST_UPLOAD_BASE_TIME = Long.valueOf(dt.dateToTimestamp(PATTERN, nextminuteStamp));

        for (int i=0; i<fileList.size(); i++) {
            correctFileTimestampAndSaveToNewFile(fileList.get(i), REQUEST_UPLOAD_BASE_TIME);
        }

        List<File> correctJsonList = fileUtil.getCurrentDirFilesWithoutDeepTraverse(JSON_DIR_CORRECT, ".json");
        long sleepTimeSec = REQUEST_UPLOAD_BASE_TIME - System.currentTimeMillis();
        saveVideoStartTime();
        Thread.sleep(sleepTimeSec);

        return correctJsonList;
    }

    private void correctFileTimestampAndSaveToNewFile(File timeShiftFile, long baseTime) throws Exception {

        logger.info("start to correct file: " + timeShiftFile.getName());
        String content = FileUtils.readFileToString(timeShiftFile,"UTF-8");
        JSONObject jsonRoot = JSON.parseObject(content);
        JSONObject data = jsonRoot.getJSONObject(DATA);
        JSONObject bizData = data.getJSONObject(BIZ_DATA);

        //start_time
        long time = bizData.getLongValue(START_TIME);
        long currentTime = baseTime + time;
        bizData.put(START_TIME, currentTime);

        //end_time
        time = bizData.getLongValue(END_TIME);
        currentTime = baseTime + time;
        bizData.put(END_TIME, currentTime);

        //face_data -> frame_time
        JSONArray faceDataArray = bizData.getJSONArray(FACE_DATA);
        for (int i=0; i<faceDataArray.size(); i++) {
            time = faceDataArray.getJSONObject(i).getLongValue(FRAME_TIME);
            currentTime = baseTime + time;
            faceDataArray.getJSONObject(i).put(FRAME_TIME, currentTime);
        }

        //trace -> time
        JSONArray traceArray = bizData.getJSONArray(TRACE);
        for (int i=0; i<traceArray.size(); i++) {
            time = traceArray.getJSONObject(i).getLongValue(TIME);
            currentTime = baseTime + time;
            traceArray.getJSONObject(i).put(TIME, currentTime);
        }


        bizData.put(TRACE, traceArray);
        bizData.put(FACE_DATA, faceDataArray);
        data.put(BIZ_DATA, bizData);
        jsonRoot.put(DATA, data);
        String latestJson = JSON.toJSONString(jsonRoot);
        List<String> latestContent = new ArrayList<>();
        latestContent.add(latestJson);

        //save to correct file dir
        String correctFile = JSON_DIR_CORRECT + "/" + timeShiftFile.getName();
        fileUtil.writeContentToFile(correctFile, latestContent);
    }

    private void sendJsonFileDataToCloud(List<File> fileList) throws Exception {
        for (int i=0; i<fileList.size(); i++) {
            apiSdkPostToCloud(fileList.get(i));
        }
    }

    private List<String> transferTimestampToshift(File oriFile, String baseTime) throws Exception {

        logger.info("start to update shifttime in file: " + oriFile.getName());
        String content = FileUtils.readFileToString(oriFile,"UTF-8");
        JSONObject jsonRoot = JSON.parseObject(content);
        JSONObject data = jsonRoot.getJSONObject(DATA);
        JSONObject bizData = data.getJSONObject(BIZ_DATA);

        long shiftValue = 0;
        long beginTimestamp = Long.valueOf(dt.dateToTimestamp(PATTERN, VIDEO_PLAY_DATE_MYPATTERN + " " + baseTime));

        //start_time
        long time = bizData.getLongValue(START_TIME);
        shiftValue = dt.getTimestampDiff(beginTimestamp, time);
        bizData.put(START_TIME, shiftValue);

        //end_time
        time = bizData.getLongValue(END_TIME);
        shiftValue = dt.getTimestampDiff(beginTimestamp, time);
        bizData.put(END_TIME, shiftValue);

        //face_data -> frame_time
        JSONArray faceDataArray = bizData.getJSONArray(FACE_DATA);
        for (int i=0; i<faceDataArray.size(); i++) {
            time = faceDataArray.getJSONObject(i).getLongValue(FRAME_TIME);
            shiftValue = dt.getTimestampDiff(beginTimestamp, time);
            faceDataArray.getJSONObject(i).put(FRAME_TIME, shiftValue);
        }

        //trace -> time
        JSONArray traceArray = bizData.getJSONArray(TRACE);
        for (int i=0; i<traceArray.size(); i++) {
            time = traceArray.getJSONObject(i).getLongValue(TIME);
            shiftValue = dt.getTimestampDiff(beginTimestamp, time);
            traceArray.getJSONObject(i).put(TIME, shiftValue);
        }


        bizData.put(TRACE, traceArray);
        bizData.put(FACE_DATA, faceDataArray);
        data.put(BIZ_DATA, bizData);
        jsonRoot.put(DATA, data);
        String latestJson = JSON.toJSONString(jsonRoot);
        List<String> latestContent = new ArrayList<>();
        latestContent.add(latestJson);
        return latestContent;
    }

    private List<File> generateTimeshiftFileList() throws Exception {
        logger.info("update timestamp to time-shift and save to new file");
        List<File> oriFileList = fileUtil.getCurrentDirFilesWithoutDeepTraverse(JSON_DIR, ".json");
        String baseTime = getVideoStartTime();

        for (int i=0; i<oriFileList.size(); i++) {

            List<String> content = transferTimestampToshift(oriFileList.get(i), baseTime);
            fileUtil.writeContentToFile(JSON_DIR_SHIFT+"/"+oriFileList.get(i).getName(), content);
        }


        return fileUtil.getCurrentDirFilesWithoutDeepTraverse(JSON_DIR_SHIFT, ".json");
    }

    private List<File> getTimeshiftFileList() throws Exception {
        List<File> fileList = fileUtil.getCurrentDirFilesWithoutDeepTraverse(JSON_DIR_SHIFT, ".json");
        List<File> fileListOri = fileUtil.getCurrentDirFilesWithoutDeepTraverse(JSON_DIR, ".json");
        if (null == fileList || fileList.size() < fileListOri.size()) {
            //generate time shift json files
            return generateTimeshiftFileList();
        }

        return fileList;

    }

    private boolean apiSdkPostToCloud(File file) throws Exception {
        // 传入签名参数
        Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
        String jsonString = FileUtils.readFileToString(file,"UTF-8");
        com.haisheng.framework.testng.service.ApiRequest haisheng
                = JSON.parseObject(jsonString, com.haisheng.framework.testng.service.ApiRequest.class);

        ApiRequest json = new ApiRequest.Builder().build();
        BeanUtils.copyProperties(haisheng, json);

        // client 请求
        String gateway = "http://dev.api.winsenseos.com/retail/api/data/device";
        ApiClient apiClient = new ApiClient(gateway, credential);
        ApiResponse apiResponse = apiClient.doRequest(json);
        String response = JSON.toJSONString(apiResponse);
        logger.info(response);
        if(apiResponse.isSuccess()) {
            return true;
        } else {
            logger.error("resoponse return error, request json: " + file.getName());
        }

        return false;
    }


    private String getVideoStartTime() {
        String line = fileUtil.findLineByKey(EDGE_LOG, VIDEO_START_KEY);
        //[36moffice-150    |[0m W0716 10:31:55.850082        start to play video
        VIDEO_BEGIN_TIME = line.substring(line.indexOf(":")-2, line.indexOf("."));

        line = fileUtil.findLineByKey(EDGE_LOG, VIDEO_CREATE_LOG_KEY);
        VIDEO_PLAY_DATE = line.substring(line.indexOf("/")-4, line.lastIndexOf(" "));

        VIDEO_PLAY_DATE_MYPATTERN = VIDEO_PLAY_DATE.replace("/", "-");

        logger.info("get video playing begin date: " + VIDEO_PLAY_DATE_MYPATTERN + " time: " + VIDEO_BEGIN_TIME);


        return VIDEO_BEGIN_TIME;
    }

    private void saveVideoStartTime() throws Exception {
        //String line = fileUtil.findLineByKey(EDGE_LOG, VIDEO_CREATE_LOG_KEY);
        //String date = line.substring(line.indexOf("/")-4, line.lastIndexOf(" "));
        //String lineDate = VIDEO_CREATE_LOG_KEY + " " + date + " " + "date";

        String time = dt.getHourMinutesSec(REQUEST_UPLOAD_BASE_TIME);

        String lineTime = time + " " + VIDEO_START_KEY;

        List<String> content = new ArrayList<>();
        //content.add(lineDate);
        content.add(lineTime);

        fileUtil.writeContentToFile(JSON_UPLOAD_LOG, content);

    }

}
