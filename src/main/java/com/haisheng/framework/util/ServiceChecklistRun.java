package com.haisheng.framework.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.commonDataStructure.LogMine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

public class ServiceChecklistRun {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine       = new LogMine(logger);
    HttpExecutorUtil executor = new HttpExecutorUtil();
    private String hostPort = "http://localhost:8080";


    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }

    public void runChecklist(int appId, int configId) {
        logMine.info("start to run checklist");
        int total = getCaseTotal(appId, configId,0,1000);
        if (total <= 50) {
            String[] idArray = getCaseIdArray(appId, configId,0, total);
            runCaseById(appId, configId,idArray);
        } else {
            int loop = total/50 + 1;
            for (int i=0; i<loop; i++) {
                String[] idArray = getCaseIdArray(appId, configId,i*50, 50);
                runCaseById(appId, configId, idArray);
            }
        }
        logMine.info(total + " cases has been run");

        logMine.printImportant("CASE RESULT LINK: " + hostPort);
    }

    public boolean runCaseById(int appId, int configId, String[] idArray) {

        if (null == idArray || 0 == idArray.length) {
            return false;
        }

        String json = "{\"applicationId\":\"" + appId
                + "\",\"configId\":\"" + configId
                + "\",\"idList\":" + Arrays.toString(idArray) + "}";

        String URL = hostPort + "/application/" + appId + "/casesrun";
        try {
            executor.doPostWithHeaderAppJson(URL, json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public String[] getCaseIdArray(int appId, int configId, int offset, int limit) {
        String[] idArray = null;
        String URL = hostPort + "/application/"
                + appId
                + "/config/"
                + configId
                + "/caseidlist?"
                + "offset=" + offset
                + "&limit=" + limit;
        try {
            executor.doGet(URL);
            JSONObject jsonObject = JSONObject.parseObject(executor.getResponse());
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            idArray = new String[jsonArray.size()];
            for (int i=0; i<jsonArray.size(); i++) {
                idArray[i] =  jsonArray.getString(i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return idArray;
    }

    public int getCaseTotal(int appId, int configId, int offset, int limit) {
        int total = 0;
        String URL = hostPort + "/application/"
                + appId
                + "/config/"
                + configId
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


    public String[] getPassRate(int appId, int configId) {
        String[] passRate = new String[]{"", "", ""};
        String URL = hostPort + "/application/"
                + appId
                + "/config/"
                + configId
                +"/config";
        try {
            executor.doGet(URL);
            JSONObject jsonObject = JSONObject.parseObject(executor.getResponse());
            JSONObject data = jsonObject.getJSONObject("data");
            int caseTotal = data.getInteger("caseTotal");
            int passTotal = data.getInteger("passTotal");
            DecimalFormat decimalFormat=new DecimalFormat(".00");
            passRate[0] = decimalFormat.format((float)passTotal*100/caseTotal) + "%";
            passRate[1] = String.valueOf(caseTotal - passTotal);
            passRate[2] = String.valueOf(caseTotal);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return passRate;
    }

    public String[] getBugInfo(int appId) {
        String[] bugInfo = new String[]{"", "", ""};
        DateTimeUtil dt = new DateTimeUtil();
        String start = dt.getHistoryDate(-14).replace("-", "");
        String today = dt.getHistoryDate(0).replace("-", "");
        String URL = hostPort + "/"
                + appId
                + "/bug"
                + "?periodUnit=day"
                + "&start="+ start
                + "&end=" + today;
        try {
            executor.doGet(URL);
            JSONObject jsonObject = JSONObject.parseObject(executor.getResponse());
            int index = jsonObject.getInteger("total") -1; //the last one is today data
            JSONArray data = jsonObject.getJSONArray("data");
            int bugTotal = data.getJSONObject(index).getInteger("allTotalNum");
            int notFixTotal = data.getJSONObject(index).getInteger("openTotalNum");
            DecimalFormat decimalFormat=new DecimalFormat(".00");
            bugInfo[0] = decimalFormat.format((float)(bugTotal-notFixTotal)*100/bugTotal) + "%";
            bugInfo[1] = String.valueOf(notFixTotal);
            bugInfo[2] = String.valueOf(bugTotal);


        } catch (IOException e) {
            e.printStackTrace();
        }


        return bugInfo;
    }


}
