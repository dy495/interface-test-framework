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
    String CORRECT_SHIF = System.getProperty("CORRECT_SHIF"); //+10 or -10 unit: sec
    String VIDEO_RECORD_BEGIN_TIME = System.getProperty("VIDEO_RECORD_BEGIN_TIME"); //2019-08-16 09:44:06
    String VIDEO_HOURS = System.getProperty("VIDEO_HOURS"); //12

    String PATTERN = "yyyy-MM-dd HH:mm:ss"; //HH: 24h, hh:12h

    long beginTimestamp = 0;
    long currentTimestamp = 0;
    long currentBeginTimestamp = 0;
    long currentEndTimstamp =  0;

    boolean IS_DEBUG = false;


    @Test
    private void correctTransTime() throws Exception {

        if (IS_DEBUG) {
            TRANS_FILE = "/Volumes/Fast SSD/0816/remark/常丰0816.csv";
            TRANS_FILE = "/Volumes/Fast SSD/0819/remark/常丰0819.csv";
            TRANS_FILE = "/Users/yuhaisheng/jason/document/work/项目/百果园/trans/常丰0827.csv";

            CSV_FILE_BASE = "baiguoyuan-15H-0827_3";
            VIDEO_RECORD_BEGIN_TIME = "2019-08-16 09:44:06";
            VIDEO_RECORD_BEGIN_TIME = "2019-08-19 07:52:06";
            VIDEO_RECORD_BEGIN_TIME = "/Users/yuhaisheng/jason/document/work/项目/百果园/yuhaishengNFS/baiguoyuan-15H-0827/video/baiguoyuan-15H-0827_3.log";
            CORRECT_SHIF = "-5";
            VIDEO_HOURS = "1";
        }

        List<String> lines = fileUtil.getFileContent(TRANS_FILE);
        filterDataToCsv(lines);



    }

    private void filterDataToCsv(List<String> linesArray) throws Exception {

        int num = 0;
        if (TRANS_FILE.contains(File.separator)) {
            CSV_FILE_BASE = TRANS_FILE.substring(0, TRANS_FILE.lastIndexOf(File.separator)) + File.separator + CSV_FILE_BASE;
        }
        String csvFile = getCsvFile(num);

        long oneHourMs = 3600 * 1000;
        currentTimestamp = System.currentTimeMillis();
//        beginTimestamp = Long.valueOf(dt.dateToTimestamp(PATTERN, VIDEO_RECORD_BEGIN_TIME));
        beginTimestamp = getTransBeginTime(VIDEO_RECORD_BEGIN_TIME);
        currentBeginTimestamp = beginTimestamp;
        currentEndTimstamp =  currentBeginTimestamp + oneHourMs;

        List<String> csvContent = new ArrayList<>();

        for (String line : linesArray) {

            if (StringUtils.isEmpty(line)) {
                logger.error("line is empty, return directly");
                continue;
            }

            if (! line.contains("线下") || line.contains("小票号,交易日期,交易时间") || line.contains("小票查询,,,")) {
                logger.info("line is NOT 线下 type, return");
                continue;
            }

            if (currentTimestamp > currentEndTimstamp) {
                //save csv lines to file
                if (num >= 1) {
                    fileUtil.writeContentToFile(csvFile, csvContent);
                    //initial time range of filter
                    currentBeginTimestamp = currentEndTimstamp;
                    currentEndTimstamp =  currentBeginTimestamp + oneHourMs;
                }

                if (num == Integer.parseInt(VIDEO_HOURS)) {
                    //end program
                    logger.info(CSV_FILE_BASE + "*.csv " + VIDEO_HOURS + " files has been generated, done!");
                    return;
                }

                //initial new csv file
                csvContent.clear();
                num++;
                csvFile = getCsvFile(num);
                logger.info("start to generate " + csvFile);
            }

            //filter line
            filterDataByTime(line, csvContent);

        }

    }

    private long getTransBeginTime(String beginTimeParam) throws Exception {

        long beginTime = 0;
        if (fileUtil.isFileExist(beginTimeParam)) {
            List<String> lines = fileUtil.getFileContent(beginTimeParam);
            beginTime = Long.valueOf(dt.linuxDateToTimestamp(lines.get(0)));

        } else {
            beginTime = Long.valueOf(dt.dateToTimestamp(PATTERN, beginTimeParam));
        }

        if (! StringUtils.isEmpty(CORRECT_SHIF) && ! CORRECT_SHIF.equals("0")) {
            CORRECT_SHIF = CORRECT_SHIF.trim();
            if (CORRECT_SHIF.contains("-")) {
                //减去秒数
                long shift = Long.valueOf(CORRECT_SHIF.substring(CORRECT_SHIF.indexOf("-")+1));
                beginTime -= shift*1000;
            } else if (CORRECT_SHIF.contains("+")) {
                //加上秒数
                long shift = Long.valueOf(CORRECT_SHIF.substring(CORRECT_SHIF.indexOf("+")+1));
                beginTime += shift*1000;
            } else {
                //加上秒数
                beginTime += Long.valueOf(CORRECT_SHIF)*1000;
            }
        }

        return beginTime;
    }


    private String getCsvFile(int num) {
        String csvFile = null;

        if (Integer.parseInt(VIDEO_HOURS) > 1) {
            csvFile = CSV_FILE_BASE + "_" + num + ".csv";
        } else {
            csvFile = CSV_FILE_BASE + ".csv";
        }

        return csvFile;
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
                            + "0" + sep
                            + items[3].trim();
            csvContent.add(csvLine);
        }

    }

}
