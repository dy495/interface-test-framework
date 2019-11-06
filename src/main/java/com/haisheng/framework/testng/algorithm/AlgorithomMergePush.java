package com.haisheng.framework.testng.algorithm;

import com.haisheng.framework.model.bean.OnlineAlgorithmMerge;
import com.haisheng.framework.model.bean.OnlineScopeInfo;
import com.haisheng.framework.testng.CommonDataStructure.ConstantVar;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.DingChatbot;
import com.haisheng.framework.util.QADbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.text.DecimalFormat;
import java.util.List;

public class AlgorithomMergePush {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private QADbUtil qaDbUtil = new QADbUtil();
    private DateTimeUtil dt   = new DateTimeUtil();
    private String DEBUG  = System.getProperty("DEBUG", "true");


    @BeforeSuite
    public void initial() {
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }

    @Test
    private void pushDailyData() {
        String today = dt.getHistoryDate(-1);
        //get data from db
        List<OnlineAlgorithmMerge> algorithmMergeList = qaDbUtil.selectAlgorithmMergeData(today);
        List<OnlineScopeInfo> scopeInfoList = qaDbUtil.selectScopeInfo();

        //push to daily rgn grp
        dingPush(algorithmMergeList, scopeInfoList);
    }


    private void dingPush(List<OnlineAlgorithmMerge> algorithmMergeList, List<OnlineScopeInfo> scopeInfoList) {
        DingChatbot.WEBHOOK_TOKEN = DingWebhook.DAILY_PV_UV_ACCURACY_GRP;

        if (DEBUG.toLowerCase().equals("true")) {
            DingChatbot.WEBHOOK_TOKEN = DingWebhook.AD_GRP;
        }

        String day = dt.getHistoryDate(-1);
        String summary = "算法聚类回归";
        String msg = "### " + summary + "\n";
        msg += "\n\n#### " + day + " 记录信息\n";
        String link = ConstantVar.GRAPH_DASHBORD;
        DecimalFormat df = new DecimalFormat("#.00");

        for ( OnlineAlgorithmMerge item : algorithmMergeList) {
            String scopeName = item.getScope();
            for (OnlineScopeInfo scopeInfo : scopeInfoList) {
                if (scopeInfo.getScope().equals(item.getScope())) {
                    scopeName = scopeInfo.getScopeName();
                    break;
                }
            }
            msg += "\n>##### " + scopeName;
            msg += "\n>###### member_percent：" + df.format(item.getMemberPercent()*100) + "%"+ "\n";
            msg += "\n>###### avg_record：" + item.getAvgRecord() + "\n";
        }
        msg += "\n##### 历史信息请点击[链接](" + link +")";
        msg += "\n##### 请 @18810332354 关注";
        logger.info("\n\n============================================\n"
                + msg
                + "\n============================================\n\n");
        //18810332354 刘峤
        String[] atArray = {"18810332354"};
        DingChatbot.sendMarkdown(msg, atArray, false);
    }

}
