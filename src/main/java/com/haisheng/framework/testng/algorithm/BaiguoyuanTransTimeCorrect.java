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
    String USER_ID_KEY = System.getProperty("USER_ID_KEY");
    String CORRECT_SHIF = System.getProperty("CORRECT_SHIF"); //+00:00:03 OR -00:00:03
    String VIDEO_RECORD_BEGIN_TIME = System.getProperty("VIDEO_RECORD_BEGIN_TIME"); //10:14:41

    String PATTERN = "yyyy-MM-dd HH:mm:ss"; //HH: 24h, hh:12h

    boolean IS_DEBUG = true;

    @Test
    private void correctTransTime() throws Exception {

        if (IS_DEBUG) {
            TRANS_FILE = "src/main/resources/test-res-repo/baiguoyuan-metircs/timeshift.csv";
            USER_ID_KEY = "7_17_12h";
            CORRECT_SHIF = "+00:03:00";
            VIDEO_RECORD_BEGIN_TIME = "10:14:41";
        }

        List<String> lines = fileUtil.getFileContent(TRANS_FILE);
        List<String> correctLines = new ArrayList<>();
        for (String line : lines ) {
            correctLine(correctLines, line);
        }
        lines = null;
        //write lines to trans file
        boolean result = fileUtil.writeContentToFile(TRANS_FILE, correctLines);
        correctLines = null;
        Assert.assertTrue(result, "ERROR: fail to write correct line to file");


    }

    private void correctLine(List<String> correctLines, String line) throws Exception {

        if (null == line || line.trim().length() < 5 || line.trim().startsWith("#")) {
            return;
        }
        //会员编号，日期，时间，纠正时间，交易类型
        String reorgLine = "";
        String[] itemArray = line.split(",");
        String sep = ",";
        int num = correctLines.size() + 1;
        //set user id
        reorgLine += USER_ID_KEY + "_" + correctLines.size() + sep;

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

        //set trans type
        reorgLine += itemArray[4];

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
