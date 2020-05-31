package com.haisheng.framework.testng.edgeTest;

import com.haisheng.framework.dao.ICaptureDao;
import com.haisheng.framework.model.bean.Capture;
import com.haisheng.framework.model.bean.CaptureRatio;
import com.haisheng.framework.testng.commonDataStructure.ConstantVar;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.testng.commonDataStructure.LogMine;
import com.haisheng.framework.testng.commonDataStructure.RegionStatus;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.DingChatbot;
import com.haisheng.framework.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CaptureRatioTest {


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SqlSession sqlSession = null;
    private LogMine logMine = new LogMine(logger);
    private String IS_SAVE_TO_DB = System.getProperty("IS_SAVE_TO_DB");
    private String SAMPLE_VIDEO = System.getProperty("SAMPLE_VIDEO");
    private String IMAGE_EDGE = System.getProperty("IMAGE_EDGE");
    private String IS_PUSH_MSG = System.getProperty("IS_PUSH_MSG");
    private String JSON_DIR = System.getProperty("JSON_DIR");
    private String RATIO_FACE_DATA = "faceDataNum";
    private String RATIO_CAPTURE_ALL = "captureNum";

    private int mapId = 0;
    private int regionId   = 0;
    private int entranceId = 0;
    private String status = RegionStatus.ENTER;

    private boolean IS_DEBUG = false;




    @Test
    public void runTest() {

        if (IS_DEBUG) {
            JSON_DIR = "/Users/yuhaisheng/Downloads/6523883981800448";
            JSON_DIR = "/Users/yuhaisheng/jason/document/work/regressionTest/edge/debugFiles/6523883981800448";
            SAMPLE_VIDEO = "test-video";
            IMAGE_EDGE = "test-image";
            IS_PUSH_MSG = "false";
            IS_SAVE_TO_DB = "false";
        }

        statisticCaptureRatio();


    }

    @Test
    public void pushMsg() {

        if (IS_DEBUG) {
            IS_PUSH_MSG = "true";
        }

        if (IS_PUSH_MSG.toLowerCase().equals("true")) {
            pushToDingdingGrp();
        }
    }

    private void statisticCaptureRatio() {
        FileUtil fileUtil = new FileUtil();
        List<File> files = fileUtil.getFiles(JSON_DIR, ".json");
        ConcurrentHashMap<String, Integer> countHm = new ConcurrentHashMap<>();
        //initial countHm
        countHm.put(RATIO_CAPTURE_ALL, 0);
        countHm.put(RATIO_FACE_DATA, 0);

        for (File file : files) {
            countEnterCaptureRatio(file, countHm);
        }
        logger.info("get " + files.size() + " files");

        printCaptureRatioInfo(countHm);
    }

    private void countEnterCaptureRatio(File file, ConcurrentHashMap<String, Integer> countHm) {
        logMine.logStep("get edge capture info from json file: " + file.getName());
        try {
            String content        = FileUtils.readFileToString(file,"UTF-8");
            JSONObject jsonObject = new JSONObject(content);
            JSONObject data       = jsonObject.getJSONObject("data").getJSONObject("biz_data");
            JSONArray trace       = data.getJSONArray("trace");


            //count denominator
            for (int i=0; i<trace.length(); i++) {
                JSONObject item = trace.getJSONObject(i);
                if (item.isNull("position")) {
                    continue;
                }

                JSONObject position = item.getJSONObject("position");
                if (position.isNull("region")) {
                    continue;
                }
                JSONArray region = position.getJSONArray("region");
                for (int j=0; j<region.length(); j++) {
                    if (region.getJSONObject(j).isNull("entrance_id") || region.getJSONObject(j).isNull("status")) {
                        continue;
                    }
                    String status = region.getJSONObject(j).getString("status");
                    String entranceId = region.getJSONObject(j).getString("entrance_id");
                    if (null != status && null != entranceId && RegionStatus.ENTER.equals(status)) {

                        //count numerator
                        if (!data.isNull("face_data") && !data.getString("face_data").trim().toLowerCase().equals("null")) {
                            countHm.put(RATIO_FACE_DATA, countHm.get(RATIO_FACE_DATA)+1);
                            logger.info(file + " found face data, current face data num: " + countHm.get(RATIO_FACE_DATA));
                        }

                        //increase capture denominator
                        countHm.put(RATIO_CAPTURE_ALL, countHm.get(RATIO_CAPTURE_ALL)+1);
                        logger.info(file + " found capture, current capture num: " + countHm.get(RATIO_CAPTURE_ALL));
                        if (! position.isNull("map_id")) {
                            this.mapId = position.getInt("map_id");
                        }
                        if (! position.isNull("region_id")) {
                            this.regionId = region.getJSONObject(j).getInt("region_id");
                        }
                        this.entranceId = Integer.parseInt(entranceId);
                    }
                }
            }
            logger.info("current json data get done.");
        } catch (Exception e) {
            logger.error(e.toString());
            Assert.assertTrue(false, "filter json file " + file + " exception");
        }
    }

    private void printCaptureRatioInfo(ConcurrentHashMap<String, Integer> countHm) {
        logMine.logStep("print capture ratio info and save to db");

        if (0 == countHm.get(RATIO_CAPTURE_ALL)) {
            logger.info("NO FACE BE CAPTURED");
            return;
        }
        int faceDataNum = countHm.get(RATIO_FACE_DATA);
        int capAllNum   = countHm.get(RATIO_CAPTURE_ALL);
        DecimalFormat df = new DecimalFormat("#.00");
        String capRatio = String.valueOf(df.format((float)faceDataNum*100/(float) capAllNum)) + "%";



        logger.info("");
        logger.info("");
        logger.info("\n=========================================================="
                + "\n\tenter face data not null num: " + faceDataNum
                + "\n\tenter capture total num: " + capAllNum
                + "\n\tenter capture ratio: " + capRatio
                + "\n\timage: " + IMAGE_EDGE
                + "\n\tvideo: " + SAMPLE_VIDEO
                + "\n==========================================================");
        logger.info("");
        logger.info("");


        saveTodb(capAllNum, faceDataNum, capRatio);
    }
    private void saveTodb(int capTotal, int faceDataNum, String capRatio) {
        if ( null != IS_SAVE_TO_DB && IS_SAVE_TO_DB.toLowerCase().contains("false")) {
            logger.info("Do NOT save result to db");
            return;
        }

        ICaptureDao captureDao = sqlSession.getMapper(ICaptureDao.class);

        Capture capture = new Capture();
        capture.setMapId(mapId);
        capture.setRegionId(regionId);
        capture.setEntranceId(entranceId);
        capture.setStatus(status);

        capture.setCaptureTotal(capTotal);
        capture.setFaceDataNotNull(faceDataNum);
        capture.setCaptureRatio(capRatio);

        capture.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        capture.setVideo(SAMPLE_VIDEO);
        capture.setImage(IMAGE_EDGE);
        captureDao.insert(capture);
        sqlSession.commit();
    }

    private void pushToDingdingGrp() {
        ICaptureDao captureDao = sqlSession.getMapper(ICaptureDao.class);

        //sent latest two days pv/uv accuracy
        DateTimeUtil dt = new DateTimeUtil();
        List<CaptureRatio> captureRatioList = captureDao.getCaptureRatioByDay(dt.getHistoryDate(0));
        dingdingPush(captureRatioList);
    }

    private void dingdingPush(List<CaptureRatio> captureRatioList) {
        DingChatbot.WEBHOOK_TOKEN = DingWebhook.DAILY_PV_UV_ACCURACY_GRP;

        if (IS_DEBUG) {
           DingChatbot.WEBHOOK_TOKEN = DingWebhook.QA_TEST_GRP;
        }

        DateTimeUtil dt = new DateTimeUtil();
        String summary = "边缘端抓拍率简报";
        String msg = "### " + summary + "\n";
        String lastDay = "2019-01-01";
        String lastVideo = "none";
        String link = ConstantVar.GRAPH_DASHBORD;

        for ( CaptureRatio item : captureRatioList) {
            String day = item.getUpdateTime().substring(0,10);
            if (! day.equals(lastDay)) {
                msg += "\n\n#### " + day + " 记录信息\n";
                lastDay = day;
            }

            if (item.getStatus().contains("TOTAL")) {
                item.setStatus("进+出 整体抓拍率：");
            } else if (item.getStatus().contains("ENTER")) {
                item.setStatus("进入抓拍率：");
            } else if (item.getStatus().contains("LEAVE")) {
                item.setStatus("出去抓拍率：");
            }

            if (! item.getVideo().contains(lastVideo)) {
                //new video
                msg += ">##### 样本：" + item.getVideo() + "\n";
                lastVideo = item.getVideo();

                String image = item.getImage();
                int index = image.lastIndexOf("/");
                if (index > 0) {
                    image = image.substring(index+1);
                }
                msg += ">###### >>Image: " + image + "\n";
            }



            msg += ">###### >>" + item.getStatus() + item.getCaptureRatio() + "\n";
        }
        msg += "##### 抓拍率历史信息请点击[链接](" + link +")" + "\n";

        logger.info(msg);
        DingChatbot.sendMarkdown(msg);
    }

    @BeforeSuite
    public void initial() {
        logger.debug("initial");
        SqlSessionFactory sessionFactory = null;
        String resource = "configuration.xml";
        try {
            sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(
                    resource));
            sqlSession = sessionFactory.openSession();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @AfterSuite
    public void clean() {
        logger.debug("clean");
        sqlSession.close();
    }

}
