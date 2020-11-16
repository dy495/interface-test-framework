package com.haisheng.framework.testng.alarm;

import com.haisheng.framework.model.bean.Config;
import com.haisheng.framework.testng.commonDataStructure.LogMine;
import com.haisheng.framework.util.*;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class AlarmSummary {

    private LogMine logger = new LogMine(LoggerFactory.getLogger(this.getClass()));

    private ServiceChecklistRun checklistRun = new ServiceChecklistRun();
    private String alarmSummaryTmp = "http://192.168.50.2:7777/html/application/APP_ID/config/CONF_ID/alarm/overview";
    private String rgnLinkTmp = "http://192.168.50.2:7777/html/application/APP_ID/config/CONF_ID/overview";
    private String confIdTmp = "CONF_ID";
    private String appIdTmp = "APP_ID";

    private QADbProxy qaDbProxy = QADbProxy.getInstance();
    public QADbUtil qaDbUtil = qaDbProxy.getQaUtil();


    @Test
    public void onlineAlarmSummary() {

        List<Config> configList = qaDbUtil.selectOnlineAlarmSummary();
        List<com.haisheng.framework.testng.commonDataStructure.AlarmSummary> alarmSummaryList = constructAlarmList(configList);
        if (alarmSummaryList.size() > 0) {
            //失败才推送
            AlarmPush alarmPush = new AlarmPush();
            alarmPush.onlineAlarmSummary(alarmSummaryList);
        }
    }

    @Test
    public void dailyAlarmSummary() {

        List<Config> configList = qaDbUtil.selectDailyAlarmSummary();
        List<com.haisheng.framework.testng.commonDataStructure.AlarmSummary> alarmSummaryList = constructAlarmList(configList);
        if (alarmSummaryList.size() > 0) {
            AlarmPush alarmPush = new AlarmPush();
            alarmPush.dailyAlarmSummary(alarmSummaryList);
        }
    }

    private List<com.haisheng.framework.testng.commonDataStructure.AlarmSummary> constructAlarmList(List<Config> configList) {
        List<com.haisheng.framework.testng.commonDataStructure.AlarmSummary> alarmSummaryList = new ArrayList<>();

        for (Config config : configList) {
            com.haisheng.framework.testng.commonDataStructure.AlarmSummary alarmSummary = new com.haisheng.framework.testng.commonDataStructure.AlarmSummary();

            String configAppId = String.valueOf(config.getApplicationId());
            String configId = String.valueOf(config.getId());

            String alarmSummaryLink = alarmSummaryTmp.replace(appIdTmp, configAppId);
            alarmSummaryLink = alarmSummaryLink.replace(confIdTmp, configId);
            alarmSummary.setAlarmSumLink(alarmSummaryLink);
            String rgnLink = rgnLinkTmp.replace(appIdTmp, configAppId);
            rgnLink = rgnLink.replace(confIdTmp, configId);
            alarmSummary.setRgnLink(rgnLink);

            int totalNum = Integer.parseInt(config.getCaseTotal());
            int passNum = Integer.parseInt(config.getPassTotal());
            int failNum = totalNum - passNum;

            if (failNum > 0) {
                //失败才推送
                List<String> passRate = new ArrayList<>();
                passRate.add(StringUtil.calAccuracyString(passNum, totalNum));
                passRate.add(String.valueOf(failNum));
                passRate.add(config.getCaseTotal());
                alarmSummary.setPassRate(passRate);

                String product = config.getService();
                product = product.substring(0, product.lastIndexOf("-"));
                alarmSummary.setProduct(product);
                alarmSummary.setRd(config.getRdOwner());

                alarmSummaryList.add(alarmSummary);
            }

        }


        return alarmSummaryList;
    }


    @BeforeSuite
    public void initial() {
        logger.debug("initial");
        qaDbUtil.openConnection();


    }

    @AfterSuite
    public void clean() {
        logger.info("clean");
        qaDbUtil.closeConnection();
    }
}
