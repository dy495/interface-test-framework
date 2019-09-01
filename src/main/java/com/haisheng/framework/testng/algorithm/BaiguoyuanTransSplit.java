package com.haisheng.framework.testng.algorithm;

import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BaiguoyuanTransSplit {


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    FileUtil fileUtil = new FileUtil();
    DateTimeUtil dt   = new DateTimeUtil();

    String TRANS_FILE = System.getProperty("TRANS_REPORT_FILE");
    String CSV_FILE_BASE = System.getProperty("CSV_FILE_BASE");
    String CORRECT_SHIF_ORI = System.getProperty("CORRECT_SHIF"); //00:00:03 OR -00:00:03
    String CORRECT_SHIF = CORRECT_SHIF_ORI;
    String VIDEO_RECORD_BEGIN_TIME = System.getProperty("VIDEO_RECORD_BEGIN_TIME"); //2019-08-16 09:44:06
    String VIDEO_HOURS = System.getProperty("VIDEO_HOURS"); //12

    String PATTERN = "yyyy-MM-dd HH:mm:ss"; //HH: 24h, hh:12h

    long beginTimestamp = 0;
    long currentTimestamp = 0;
    long currentBeginTimestamp = 0;
    long currentEndTimstamp =  0;

    boolean IS_DEBUG = true;


    @Test
    private void correctTransTime() throws Exception {

        if (IS_DEBUG) {
            TRANS_FILE = "/Volumes/Fast SSD/0816/remark/常丰0816.csv";

            CSV_FILE_BASE = "baiguoyuan-0816";
            CORRECT_SHIF_ORI = "-00:03:00";
            CORRECT_SHIF = CORRECT_SHIF_ORI;
            VIDEO_RECORD_BEGIN_TIME = "2019-08-16 09:44:06";
            VIDEO_HOURS = "12";
        }

        List<String> lines = fileUtil.getFileContent(TRANS_FILE);
        filterDataToCsv(lines);



    }

    private void filterDataToCsv(List<String> linesArray) throws Exception {

        int num = 0;
        CSV_FILE_BASE = TRANS_FILE.substring(0, TRANS_FILE.lastIndexOf("/")) + File.separator + CSV_FILE_BASE;
        String csvFile = CSV_FILE_BASE + "_" + num + ".csv";

        long oneHourMs = 3600 * 1000;
        currentTimestamp = System.currentTimeMillis();
        beginTimestamp = Long.valueOf(dt.dateToTimestamp(PATTERN, VIDEO_RECORD_BEGIN_TIME));
        currentBeginTimestamp = beginTimestamp;
        currentEndTimstamp =  currentBeginTimestamp + oneHourMs;

        List<String> csvContent = new ArrayList<>();

        for (String line : linesArray) {

            if (StringUtils.isEmpty(line)) {
                logger.error("line is empty, return directly");
                continue;
            }

            if (! line.contains("线下")) {
                logger.info("line is NOT 线下 type, return");
                continue;
            }

            if (currentTimestamp > currentEndTimstamp) {
                if (num >= 1) {
                    fileUtil.writeContentToFile(csvFile, csvContent);
                    //initial time range of filter
                    currentBeginTimestamp = currentEndTimstamp;
                    currentEndTimstamp =  currentBeginTimestamp + oneHourMs;
                }

                if (num == Integer.parseInt(VIDEO_HOURS)) {
                    logger.info(CSV_FILE_BASE + "_*.csv " + VIDEO_HOURS + " files has been generated, done!");
                    return;
                }

                //initial new csv file
                csvContent.clear();
                num++;
                csvFile = CSV_FILE_BASE + "_" + num + ".csv";
                logger.info("start to generate " + csvFile);
            }

            //filter line
            filterDataByTime(line, csvContent);

        }

    }

    private void filterDataByTime(String line, List<String> csvContent) throws Exception {

        //交易编号0，日期1，时间2，付款方式3，会员卡号(手机号)4，交易类型5
        String[] items = line.split(",");

        String ymd = items[1].trim().replaceAll("/", "-");
        String hms = items[2].trim();
        currentTimestamp = Long.valueOf(dt.dateToTimestamp(PATTERN, ymd + " " + hms));

        String phone = items[4].trim();
        String sep = ",";

        String pattern = "^\\d{6,}";
        if (phone.length() < 6 && ! Pattern.matches(pattern, phone)) {
            phone = "no_phone";
        }

        if (currentTimestamp >= currentBeginTimestamp && currentTimestamp <= currentEndTimstamp) {
            //write line to current csv file
            String timeShift = dt.getTimestampDistance(currentBeginTimestamp, currentTimestamp);
            String csvLine = phone + sep
                            + timeShift + "-" + timeShift + sep
                            + "0";
            csvContent.add(csvLine);
        }



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
        if (CORRECT_SHIF_ORI.indexOf("-") > -1) {
            String[] correctHMS = CORRECT_SHIF_ORI.split(":");
            correctHMS[1] = "-"+correctHMS[1];
            correctHMS[2] = "-"+correctHMS[2];
            CORRECT_SHIF = String.join(":", correctHMS);
        }
    }


}
