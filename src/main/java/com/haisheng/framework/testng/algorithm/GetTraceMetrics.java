package com.haisheng.framework.testng.algorithm;

import com.haisheng.framework.dao.ITraceMetricsDao;
import com.haisheng.framework.model.bean.TraceMetrics;
import com.haisheng.framework.testng.CommonDataStructure.ConstantVar;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.DingChatbot;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class GetTraceMetrics {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SqlSession sqlSession = null;
    String sampleVideo = System.getProperty("SAMPLE_VIDEO");
    String IS_PUSH_MSG = System.getProperty("IS_PUSH_MSG");
    String IS_SAVE_TO_DB = System.getProperty("IS_SAVE_TO_DB");

    //debug trigger var
    boolean IS_DEBUG = false;


    @Test
    public void GetTraceMetrics() {

        if (IS_DEBUG) {
            IS_PUSH_MSG = "true";
        }

        try {
            if (IS_PUSH_MSG.toLowerCase().equals("true")) {
                pushToDingdingGrp();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void pushToDingdingGrp() {
        ITraceMetricsDao traceMetricsDao = sqlSession.getMapper(ITraceMetricsDao.class);

        DateTimeUtil dt = new DateTimeUtil();
        List<TraceMetrics> traceMetricsList = traceMetricsDao.getTraceMetricsByDay(dt.getHistoryDate(-1), 6);
        dingdingPush(traceMetricsList);
    }

    private void dingdingPush(List<TraceMetrics> traceMetricsList) {
        DingChatbot.WEBHOOK_TOKEN = DingWebhook.DAILY_PV_UV_ACCURACY_GRP;

        if (IS_DEBUG) {
            DingChatbot.WEBHOOK_TOKEN = DingWebhook.AD_GRP;
        }

        String summary = "Trace指标回归";
        String msg = "### " + summary + "\n";
        String lastDay = "2019-01-01";
        String link = ConstantVar.GRAPH_DASHBORD;
        DecimalFormat df = new DecimalFormat("#.00");
        String userName = "none";
        DateTimeUtil dt = new DateTimeUtil();

        for ( TraceMetrics item : traceMetricsList) {
            String day = item.getUpdateTime().substring(0,10);
            if (! day.equals(lastDay)) {
                msg += "\n\n#### " + day + " 记录信息\n";
                lastDay = day;
            }

            if (! item.getUserName().contains(userName)) {
                msg += ">##### " + item.getUserName();
                userName = item.getUserName();
            }

            msg += "\n>###### >>设备：" + item.getDeviceId() + ", 请求数: " + item.getReqNum();
            msg += "\n>###### ----->抓拍率：" + df.format(item.getCapRatio()*100) + "%" + "\n";
            msg += "\n>###### ----->归档率：" + df.format(item.getArcRatio()*100) + "%" + "\n";
            msg += "\n>###### ----->入库率：" + df.format(item.getDbRatio()*100) + "%" + "\n";
        }
        msg += "\n##### 历史信息请点击[链接](" + link +")";

        logger.info("\n\n============================================\n"
                + msg
                + "\n============================================\n\n");
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

