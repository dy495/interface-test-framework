package com.haisheng.framework.testng.bigScreen.feidanDaily;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class FeidanMiniApiToday24BDaily {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    Feidan feidan = new Feidan();

//    ------------------------------------------------------非创单验证（其他逻辑）-------------------------------------


    @Test
    public void todayData(){
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String message = "";

        Case aCase = new Case();
        String failReason = "";
        String caseDesc = "取今天24点前数据";

        JSONObject historyRuleDetail = null;
        try {
            historyRuleDetail = feidan.historyRuleDetail();
            int today_natural_visitor= historyRuleDetail.getInteger("natural_visitor"); //风控数据-截至目前-自然登记人数
            int today_channel_visitor = historyRuleDetail.getInteger("channel_visitor"); //风控数据-截至目前-渠道报备人数
            int today_unknown_order= historyRuleDetail.getInteger("unknow_order"); //风控数据-截至目前-未知订单数
            int today_normal_order= historyRuleDetail.getInteger("normal_order"); //风控数据-截至目前-正常订单数
            int today_risk_order= historyRuleDetail.getInteger("risk_order"); //风控数据-截至目前-风险订单数

            JSONObject reptstatistics = feidan.channelReptstatistics();
            int today_customer_total = reptstatistics.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int today_record_total = reptstatistics.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int today_customer_today = reptstatistics.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int today_record_today = reptstatistics.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量

            message = "风控数据-截至目前-自然登记人数=" + today_natural_visitor+
                    "\n\n风控数据-截至目前-渠道报备人数=" + today_channel_visitor+
                    "\n\n风控数据-截至目前-未知订单数=" + today_unknown_order+
                    "\n\n风控数据-截至目前-正常订单数=" + today_normal_order+
                    "\n\n风控数据-截至目前-风险订单数=" + today_risk_order+

                    "\n\n\n渠道管理-渠道报备统计-累计报备顾客数量=" + today_customer_total+
                    "\n\n渠道管理-渠道报备统计-今日新增报备顾客数量=" + today_customer_today+
                    "\n\n渠道管理-渠道报备统计-累计报备信息数量=" + today_record_total+
                    "\n\n渠道管理-渠道报备统计-今日新增报备信息数量=" + today_record_today;

            logger.info(message);
            dingPush(message);
        }  catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @BeforeClass
    public void login() {

        feidan.login();

    }

    public void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();
        alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        alarmPush.dailyRgn(msg);
    }
}