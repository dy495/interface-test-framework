package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.model.bean.ReportTime;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Member;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class FeidanMiniApiToday24BDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
            feidan.saveData(aCase, ciCaseName, caseName, caseDesc);
        }
    }

    @BeforeClass
    public void login() {

        feidan.login();

    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();
        alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        alarmPush.dailyRgn(msg);
    }
}