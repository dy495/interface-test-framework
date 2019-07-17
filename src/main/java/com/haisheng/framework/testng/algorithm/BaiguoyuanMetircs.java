package com.haisheng.framework.testng.algorithm;

import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import com.haisheng.framework.util.HttpExecutorUtil;
import com.haisheng.framework.util.QADbUtil;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BaiguoyuanMetircs {
    private QADbUtil qaDbUtil = new QADbUtil();
    private DateTimeUtil dt = new DateTimeUtil();
    private FileUtil fileUtil = new FileUtil();

    private String TRANS_REPORT_FILE = System.getProperty("TRANS_REPORT_FILE");;
    private String EDGE_LOG = System.getProperty("EDGE_LOG");
    private String IS_PUSH_MSG = System.getProperty("IS_PUSH_MSG");
    private String VIDEO_START_KEY = System.getProperty("VIDEO_START_KEY");
    private String KEY_GENDER = "gender";
    private String KEY_START_TIME = "startTime";
    private String KEY_END_TIME = "endTime";
    private String KEY_USER_ID = "userId";
    private String KEY_TOTAL_USER = "userTotal";

    private boolean IS_DEBUG = true;

    private String URL = "http://{{HOST}}/bind/receive";



    @Test
    private void uploadTransData() throws InterruptedException {
        if (IS_DEBUG) {
            EDGE_LOG = "src/main/resources/csv/yuhaisheng/demo2.csv";
            TRANS_REPORT_FILE = "src/main/resources/test-res-repo/baiguoyuan-metircs/debug.csv";
            VIDEO_START_KEY = "start to play video";
            IS_PUSH_MSG = "false";
        }


        //get video playing time
        String beginTime = getVideoStartTime();
        Assert.assertNotNull(beginTime, "NOT found actually video start playing time");

        //upload transaction data to cloud
        boolean result = postTransData(beginTime);
        Assert.assertTrue(result, "no expect transaction data");

        //get bind-accuracy and bind-success-accuracy
        Thread.sleep(2*60*1000);
        //result = getAndPrintMetrics();
        Assert.assertTrue(result, "no expect transaction data");

        //push msg
    }

    private String sendRequestOnly(String URL, String json) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJson(URL, json);
        return executor.getResponse();
    }

    private boolean postTransData(String beginTime) {
        //交易记录上传 http://{{host}}/bind/receive

        ConcurrentHashMap<String, String> hm = new ConcurrentHashMap<>();
        boolean isDataExist = getTransValueFromFile(hm, beginTime);
        if (! isDataExist) {
            return false;
        }
        String json = generateTransValue(hm);
        try {
            String response = sendRequestOnly(URL, json);
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    boolean getTransValueFromFile(ConcurrentHashMap<String, String> hm, String beginTime) {
        //beginTime: 10:31:55
        //get file content
        List<String> fileContent = fileUtil.getFileContent(TRANS_REPORT_FILE);
        if (null == fileContent || fileContent.size() == 0) {
            System.out.println("no expect user data, return");
            return false;
        }
        int totalBindLine = 0;
        for (String line : fileContent) {
            if (line.trim().startsWith("#")) {
                continue;
            } else {
                totalBindLine++;
                String[] items = line.split(",");
                //gaiguoyuan_1,00:00-01:26,女
                hm.put(KEY_USER_ID, items[0]);
                hm.put(KEY_START_TIME, items[1]);
                hm.put(KEY_END_TIME, items[2]);
                hm.put(KEY_GENDER, items[3]);
            }

        }
        hm.put(KEY_TOTAL_USER, String.valueOf(totalBindLine));

        fileContent = null;
        return true;
    }

    String generateTransValue(ConcurrentHashMap<String, String> hm) {
        //gender: 0-male, 1-female
        //shopid: 134
        //userid: customer id
        //{"age":35,"endTime":1563244347527,"gender":0,"matchTimes":0,"shopId":"134","startTime":1563244327527,"userId":"baiguoyuancustomer1","requestId":"abctest"}

        String json = "{\"age\":30," +
                "\"endTime\":" + hm.get(KEY_END_TIME) + "," +
                "\"gender\":" + hm.get(KEY_GENDER) + "," +
                "\"matchTimes\":0," +
                "\"shopId\":\"134\"," +
                "\"startTime\":" + hm.get(KEY_START_TIME) + "," +
                "\"userId\":\"" + hm.get(KEY_USER_ID)+ "\"," +
                "\"requestId\":\"" + UUID.randomUUID().toString() + "\"}";

        System.out.println("post transaction data: " + json);
        return json;
    }

    private String getVideoStartTime() {
        String beginTime = "";
        String line = fileUtil.findLineByKey(EDGE_LOG, VIDEO_START_KEY);
        //[36moffice-150    |[0m W0716 10:31:55.850082        start to play video
        beginTime = line.substring(line.indexOf(":")-2, line.indexOf("."));

        System.out.println("get video playing begin time: " + beginTime);


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
