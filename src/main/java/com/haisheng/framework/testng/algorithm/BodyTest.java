package com.haisheng.framework.testng.algorithm;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.PVUVAccuracy;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.DingChatbot;

import com.haisheng.framework.util.HttpExecutorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class BodyTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine       = new LogMine(logger);
    HttpExecutorUtil executor = new HttpExecutorUtil();
//    private String hostPort = "http://192.168.50.3:7777";
    private String hostPort = "http://localhost:8080";
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE;
    private int SERVICE_ID = ChecklistDbInfo.DB_SERVICE_ID_BODY_SERVICE;


    @Test
    public void runChecklist() {
        logMine.info("start to run checklist");
        int total = getCaseTotal(0,1);
        if (total <= 50) {
            String[] idArray = getCaseIdArray(0, total);
            runCaseById(idArray);
        } else {
            int loop = total/50 + 1;
            for (int i=0; i<loop; i++) {
                String[] idArray = getCaseIdArray(i*50, 50);
                runCaseById(idArray);
            }
        }

        logMine.printImportant("CASE RESULT LINK: " + hostPort);
    }

    private boolean runCaseById(String[] idArray) {

        if (null == idArray || 0 == idArray.length) {
            return false;
        }

        String json = "{\"applicationId\":\"" + APP_ID
                + "\",\"configId\":\"" + SERVICE_ID
                + "\",\"idList\":" + idArray + "}";

        String URL = hostPort + "/application/" + APP_ID + "/casesrun";
        try {
            executor.doPostWithHeaderAppJson(URL, json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private String[] getCaseIdArray(int offset, int limit) {
        String[] idArray = null;
        String URL = hostPort + "/application/"
                + APP_ID
                + "/config/"
                + SERVICE_ID
                + "/caseidlist?"
                + "offset=" + offset
                + "&limit=" + limit;
        try {
            executor.doGet(URL);
            JSONObject jsonObject = JSONObject.parseObject(executor.getResponse());
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            idArray = (String[]) jsonArray.toArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return idArray;
    }

    private int getCaseTotal(int offset, int limit) {
        int total = 0;
        String URL = hostPort + "/application/"
                + APP_ID
                + "/config/"
                + SERVICE_ID
                + "/caseidlist?"
                + "offset=" + offset
                + "&limit=" + limit;
        try {
            executor.doGet(URL);
            JSONObject jsonObject = JSONObject.parseObject(executor.getResponse());
            total = jsonObject.getInteger("total");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return total;
    }



}
