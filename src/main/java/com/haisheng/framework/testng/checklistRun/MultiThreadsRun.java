package com.haisheng.framework.testng.checklistRun;

import com.haisheng.framework.util.HttpExecutorUtil;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.*;

public class MultiThreadsRun {

    int threadNum = Integer.parseInt(System.getProperty("QA_THREAD_NUM"));
    String applicationId = System.getProperty("QA_APP_ID");
    String configId = System.getProperty("QA_CONFIG_ID");
    int duration = Integer.parseInt(System.getProperty("QA_DURATION_M"));
    String caseIds = System.getProperty("QA_CASE_IDS");
    String URL = "http://192.168.50.3:7777/application/" + applicationId + "/casesrun";
//    String URL = "http://192.168.50.181:8080/application/" + applicationId + "/casesrun";
    HttpExecutorUtil executor = new HttpExecutorUtil();
    String json = null;

//    int threadNum = 1;
//    String applicationId = "2";
//    String configId = "2";
//    String caseIds = "2422,2427";
//    String URL = "http://192.168.50.181:8080/application/" + applicationId + "/casesrun";
//    int duration = 1;


    @Test
    public void runChecklist() {
        try {
            json = "{\"applicationId\":\"" + applicationId
                    + "\",\"configId\":\"" + configId
                    + "\",\"idList\":[" + caseIds+ "]}";
            ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
            executorService.submit(new TestRunable());
            Thread.sleep(10*1000);
            long startTime = System.currentTimeMillis();
            while(true) {
                long past = (System.currentTimeMillis()-startTime)/1000/60;
                if (past >= duration) {
                    executorService.shutdown();
                    break;
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    class TestRunable implements Runnable {
        public void run() {
            try {
                long startTime = System.currentTimeMillis();
                while(true) {
                    long past = (System.currentTimeMillis()-startTime)/1000/60;
                    executor.doPostWithHeaderAppJson(URL, json);
                    if (past >= duration) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

