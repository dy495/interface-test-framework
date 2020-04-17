package com.haisheng.framework.testng.edgeTest;

import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.EdgePvAccuracy;
import com.haisheng.framework.model.bean.EdgePvRgn;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EdgeRegressionTest {

    private Logger logger     = LoggerFactory.getLogger(this.getClass());
    private QADbUtil qaDbUtil = new QADbUtil();
    private DateTimeUtil dt   = new DateTimeUtil();

    private String VIDEO = System.getProperty("VIDEO");
    private String IMAGE = System.getProperty("IMAGE");

    private String JSON_DIR_PATH  = System.getProperty("JSON_DIR_PATH");
    private String SAVE_TO_DB     = System.getProperty("SAVE_TO_DB");
    private String PUSH_MSG       = System.getProperty("PUSH_MSG");

    private int EXPECT_ENTER = 100;
    private int EXPECT_LEAVE = 100;
    private int EXPECT_PASS  = 100;

    private FileUtil fileUtil     = new FileUtil();

    private final String ENTER    = "ENTER";
    private final String LEAVE    = "LEAVE";
    private final String PASS     = "PASS";



    private boolean DEBUG = false;


    //进过店
    @Test
    private void runRgnShopEnterLeaveCross() throws IOException {

        if (DEBUG) {
            JSON_DIR_PATH = "/Users/yuhaisheng/jason/document/work/regressionTest/edge/debugFiles/request/6798286133101568";
            VIDEO = "cross_enter_shop_personLes_youyiku.mov-6798286133101568";
            IMAGE = "TEST";
        }
        logger.info("runRgnShopEnterLeaveCross");
        ConcurrentHashMap<String, Integer> statisticHm = statisticShopData(JSON_DIR_PATH);

        //print and save satistic data to db
        printAndSaveData(statisticHm);

        //compare current data to the avg value of last 7 days'data
        checkData(statisticHm);

    }

    //出入口
    @Test
    private void runRgnEntranceEnterLeave() throws IOException {

        if (DEBUG) {
            JSON_DIR_PATH = "/Users/yuhaisheng/jason/document/work/regressionTest/edge/debugFiles/request/6798411419452416";
            VIDEO = "entry_personMore_jinjie.mov-6798411419452416";
            IMAGE = "TEST";
        }

        logger.info("runRgnEntranceEnterLeave");
        ConcurrentHashMap<String, Integer> statisticHm = statisticEntranceData(JSON_DIR_PATH);

        //print and save satistic data to db
        printAndSaveData(statisticHm);

    }

    @Test
    private void pushMsg() {
        if (DEBUG) {
            PUSH_MSG = "true";
            SAVE_TO_DB = "true";
            initial();


        }
        if (! isPushMsg()) {
            return;
        }

        AlarmPush alarmPush = new AlarmPush();

        alarmPush.setDingWebhook(DingWebhook.DAILY_EDGE);
        List<EdgePvAccuracy> accuracyList = qaDbUtil.getEdgePvAccuracy(dt.getHistoryDate(0));
        alarmPush.edgeRgnAlarm(accuracyList);
    }

    private ConcurrentHashMap<String, Integer> statisticShopData(String dirPath) throws IOException {

        ConcurrentHashMap<String, Integer> statisticHm = new ConcurrentHashMap();

        //get all json files from dir
        List<File> fileList = fileUtil.getCurrentDirFilesWithoutDeepTraverse(dirPath, ".json");

        for (File jsonFile : fileList) {
            //get data from each file
            statisticShopData(jsonFile, statisticHm);
        }
        Preconditions.checkArgument(!CollectionUtils.isEmpty(statisticHm),
                "$..position.region[*].entrance_type found 0 data in json files");
        logger.info("statistic json files' result, hm size: " + statisticHm.size());

        return statisticHm;
    }

    private void statisticShopData(File jsonFile, ConcurrentHashMap<String, Integer> statisticHm) throws IOException {
        String jsonString = FileUtils.readFileToString(jsonFile);

//        List<String> data = JsonpathUtil.readListUsingJsonPath(jsonString, "$..position.region[*].entrance_type");
        List<String> data = JsonpathUtil.readListUsingJsonPath(jsonString, "$..position.region[*].status");
        for (String item : data) {
            putDataToHm(statisticHm, item);
        }
    }

    private ConcurrentHashMap<String, Integer> statisticEntranceData(String dirPath) throws IOException {

        ConcurrentHashMap<String, Integer> statisticHm = new ConcurrentHashMap();

        //get all json files from dir
        List<File> fileList = fileUtil.getCurrentDirFilesWithoutDeepTraverse(dirPath, ".json");

        for (File jsonFile : fileList) {
            //get data from each file
            statisticEntranceData(jsonFile, statisticHm);
        }

        Preconditions.checkArgument(!CollectionUtils.isEmpty(statisticHm),
                "$..position.region[*].status found 0 data in json files");

        logger.info("statistic json files' result, hm size: " + statisticHm.size());
        return statisticHm;
    }

    private void statisticEntranceData(File jsonFile, ConcurrentHashMap<String, Integer> statisticHm) throws IOException {
        String jsonString = FileUtils.readFileToString(jsonFile);

        List<String> data = JsonpathUtil.readListUsingJsonPath(jsonString, "$..position.region[*].status");
        for (String item : data) {
            putDataToHm(statisticHm, item);
        }
    }

    private void putDataToHm(ConcurrentHashMap<String, Integer> statisticHm, String key) {
        if (statisticHm.containsKey(key)) {
            statisticHm.put(key, statisticHm.get(key)+1);
        } else {
            statisticHm.put(key, 1);
        }
    }

    private void printAndSaveData(ConcurrentHashMap<String, Integer> hashMapData) {
        for (String status : hashMapData.keySet()) {
            EdgePvRgn edgePvRgn = new EdgePvRgn();
            edgePvRgn.setDate(dt.getHistoryDate(0));
            edgePvRgn.setVideo(VIDEO);
            edgePvRgn.setStatus(status);
            edgePvRgn.setImage(IMAGE);

            int actualPv = hashMapData.get(status);
            edgePvRgn.setPv(actualPv);

            try {
                edgePvRgn.setUpdateTime(dt.currentDateToTimestamp());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (status.contains(PASS)) {
                edgePvRgn.setExpectPV(EXPECT_PASS);

            } else if (status.contains(ENTER)) {
                edgePvRgn.setExpectPV(EXPECT_ENTER);

            } else if (status.contains(LEAVE)) {
                edgePvRgn.setExpectPV(EXPECT_LEAVE);
            }

            //set accuracy
            String accuracyPercent = StringUtil.calAccuracyString(actualPv, edgePvRgn.getExpectPV());
            edgePvRgn.setPvAccuracyRate(accuracyPercent);

            //print info
            printInfo(edgePvRgn);

            //save data
            if (isSaveToDb()) {
                qaDbUtil.saveEdgePvRgn(edgePvRgn);
            }
        }
    }


    private void printInfo(EdgePvRgn edgePvRgn) {
        logger.info("");
        logger.info("");
        logger.info("\n=========================================================\n" +
                        "DATE     : " + edgePvRgn.getDate() + "\n" +
                        "VIDEO    : " + edgePvRgn.getVideo() + "\n" +
                        "IMAGE    : " + edgePvRgn.getImage() + "\n" +
                        "STATUS   : " + edgePvRgn.getStatus() + "\n" +
                        "PV       : " + edgePvRgn.getPv() + "\n" +
                        "EXPECT_PV: " + edgePvRgn.getExpectPV() + "\n" +
                        "ACCURACY : " + edgePvRgn.getPvAccuracyRate() + "\n" +
                        "UPDATE_T : " + edgePvRgn.getUpdateTime() + "\n" +
                    "========================================================="
                );
        logger.info("");
    }

    private void checkData(ConcurrentHashMap<String, Integer> statisticHm) {
        return;
        //get the avg value of latest 7 days' data

        //compare


        //alarm if (current value - avg value)/current value > 10%
    }


    private boolean isSaveToDb() {
        if (! StringUtils.isEmpty(SAVE_TO_DB) && SAVE_TO_DB.contains("true")) {
            return true;
        } else {
            logger.info("do NOT save result to db");
            return false;
        }
    }

    private boolean isPushMsg() {
        if (! StringUtils.isEmpty(PUSH_MSG) && PUSH_MSG.contains("true")) {
            return true;
        } else {
            logger.info("do NOT push msg");
            return false;
        }
    }

    private void setExpect() {
        if (StringUtils.isEmpty(VIDEO)) {
            return;
        } else if (VIDEO.contains("cross_enter_shop_personLes_youyiku")) {
            EXPECT_ENTER      = 9;
            EXPECT_LEAVE      = 8;
            EXPECT_PASS       = 50;

        } else if (VIDEO.contains("cross_enter_shop_personMore_moco")) {
            EXPECT_ENTER      = 2;
            EXPECT_LEAVE      = 4;
            EXPECT_PASS       = 79;

        } else if (VIDEO.contains("entry_personLess_anquantongdao")) {
            EXPECT_ENTER  = 11;
            EXPECT_LEAVE  = 11;

        } else if (VIDEO.contains("entry_personMore_jinjie")) {
            EXPECT_ENTER  = 16;
            EXPECT_LEAVE  = 16;

        }

    }

    @BeforeSuite
    public void initial() {
        logger.debug("initial");

        setExpect();

        if (isSaveToDb()) {
            qaDbUtil.openConnection();
        }

    }

    @AfterSuite
    public void clean() {
        logger.debug("clean");

        if (isSaveToDb()) {
            qaDbUtil.closeConnection();
        }

    }


}
