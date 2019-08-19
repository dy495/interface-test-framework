package com.haisheng.framework.testng.algorithm;

import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class BaiguoyuanTransTimeCorrect {


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    FileUtil fileUtil = new FileUtil();
    DateTimeUtil dt   = new DateTimeUtil();

    String TRANS_FILE = System.getProperty("TRANS_REPORT_FILE");
    String TRANS_FILE_CORRECT = TRANS_FILE + ".correct";
    String TRANS_FILE_UPLOAD = TRANS_FILE + ".upload";
    String USER_ID_KEY = System.getProperty("USER_ID_KEY");
    String CORRECT_SHIF = System.getProperty("CORRECT_SHIF"); //+00:00:03 OR -00:00:03
    String VIDEO_RECORD_BEGIN_TIME = System.getProperty("VIDEO_RECORD_BEGIN_TIME"); //10:14:41

    String PATTERN = "yyyy-MM-dd HH:mm:ss"; //HH: 24h, hh:12h

    boolean IS_DEBUG = false;

    @Test
    private void correctTransTime() throws Exception {

        if (IS_DEBUG) {
            TRANS_FILE = "src/main/resources/test-res-repo/baiguoyuan-metircs/timeshift.csv";
            TRANS_FILE_CORRECT = TRANS_FILE + ".correct";
            TRANS_FILE_UPLOAD = TRANS_FILE + ".upload";
            USER_ID_KEY = "baiguoyuan_2019_07_17_12H";
            CORRECT_SHIF = "+00:03:00";
            VIDEO_RECORD_BEGIN_TIME = "10:14:41";
        }

        List<String> lines = fileUtil.getFileContent(TRANS_FILE);
        List<String> correctLines = transferLinesToCorrect(lines);
        saveToCorrectFile(correctLines);
        saveToUploadFile(correctLines);



    }

    private List<String> transferLinesToCorrect(List<String> lines) throws Exception {
        List<String> correctLines = new ArrayList<>();
        for (String line : lines ) {
            if (line.contains("线下")) {
                correctLine(correctLines, line);
            }
        }
        return correctLines;
    }

    private boolean saveToCorrectFile(List<String> lines) {
        //write lines to trans file
        boolean result = fileUtil.writeContentToFile(TRANS_FILE_CORRECT, lines);

        Assert.assertTrue(result, "ERROR: fail to write correct line to file");
        logger.info("generate file " + TRANS_FILE_CORRECT + " done");
        return result;
    }

    private boolean saveToUploadFile(List<String> lines) {

        List<String> uploadList = new ArrayList<>();
        String sep = ",";
        //#交易编号0，日期1，时间2，纠正时间3，视频时间偏移4，phone5，交易类型
        for (String line: lines) {
            String[] items = line.split(",");
            //baiguoyuan_1,00:01:26-00:01:26,18200003457,1
            String reorgLine = items[0] + sep + items[4] + "-" + items[4] + sep + items[5] + sep + "1";
            uploadList.add(reorgLine);
        }
        //write lines to upload file
        boolean result = fileUtil.writeContentToFile(TRANS_FILE_UPLOAD, uploadList);
        Assert.assertTrue(result, "ERROR: fail to write line to upload file");
        logger.info("generate file " + TRANS_FILE_UPLOAD + " done");

        return result;
    }

    private void correctLine(List<String> correctLines, String line) throws Exception {

        if (null == line || line.trim().length() < 6 || line.trim().startsWith("#")) {
            return;
        }
        //交易编号，日期，时间，纠正时间，会员卡号，交易类型
        String reorgLine = "";
        String[] itemArray = line.split(",");
        String sep = ",";
        int num = correctLines.size() + 1;
        //set user id
        reorgLine += USER_ID_KEY + "_" + num + sep;

        //set date
        String date = itemArray[1].trim().replace("/", "-");
        reorgLine += date + sep;

        //set time
        String beginTime = itemArray[2].trim();
        reorgLine += beginTime + sep;

        //set correct time
        String correctTime = getCorrectTime(itemArray[3], date, beginTime);
        reorgLine += correctTime + sep;

        //set time shift, new column 视频时间偏移
        reorgLine += getTimeShift(date, correctTime) + sep;

        //set phone number
        reorgLine += itemArray[4] + sep;

        //set trans type
        reorgLine += itemArray[5];

        logger.info(reorgLine);
        correctLines.add(reorgLine);

    }


    private String getTimeShift(String date, String correctTime) throws Exception {
        //get video record begin time timestamp
        long videoBeginTimestamp = Long.valueOf(dt.dateToTimestamp(PATTERN, date + " " + VIDEO_RECORD_BEGIN_TIME));

        //get current line correct time timestamp
        long corretTimestamp = Long.valueOf(dt.dateToTimestamp(PATTERN, date + " " + correctTime.trim()));

        //get shift timestamp and transfer to HH:mm:ss format
        String shifTime = dt.getTimestampDistance(videoBeginTimestamp, corretTimestamp);

        return shifTime;
    }

    private String getCorrectTime(String currentItemTime, String date, String beginTime) {
        String correctTime = null;

        if (null == currentItemTime || currentItemTime.trim().length() < 1) {
            //no manual correct time, add it automatically
            String baseTime = date + " " + beginTime;
            formatCorrecShit();
            String corretFullTime = dt.getHistoryDate(PATTERN, baseTime, CORRECT_SHIF);
            correctTime = corretFullTime.substring(corretFullTime.indexOf(":")-2);
        } else {
            correctTime = currentItemTime.trim();
        }

        return  correctTime;
    }

    private void formatCorrecShit() {
        if (CORRECT_SHIF.indexOf("-") > -1) {
            String[] correctHMS = CORRECT_SHIF.split(":");
            correctHMS[1] = "-"+correctHMS[1];
            correctHMS[2] = "-"+correctHMS[2];
            CORRECT_SHIF = String.join(":", correctHMS);
        }
    }


}
