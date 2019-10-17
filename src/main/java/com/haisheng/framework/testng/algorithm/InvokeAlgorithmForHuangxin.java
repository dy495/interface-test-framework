package com.haisheng.framework.testng.algorithm;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import com.haisheng.framework.util.HttpExecutorUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InvokeAlgorithmForHuangxin {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private FileUtil fileUtil = new FileUtil();


    private String ARCHIVE_LOG_DIR_PATH = System.getProperty("ARCHIVE_LOG");



    @Test
    private void algorithmCallForHuangxin() throws Exception {
        //调用统计接口查看当前数据,将结果以日志的方式输出
        String logName = "log-after-upload-edge-json.log";
        invokeStatisticPostMethodAndSaveLog("http://47.95.71.16/statistic?scope=1922", logName);

        //调用fullMerge接口
        logger.info("");
        logger.info("");
        logger.info("调用fullMerge接口");
        sendRequestPost("http://47.95.69.163/gate/manage/fullMerge");

        //sleep(10分钟)
        logger.info("sleep 10m");
        Thread.sleep(10*60*1000);

        //调用统计接口查看当前数据,将结果以日志的方式输出
        logName = "log-after-fullMerge.log";
        invokeStatisticPostMethodAndSaveLog("http://47.95.71.16/statistic?scope=1922", logName);

        //调用simplifyImages接口
        logger.info("");
        logger.info("");
        logger.info("调用simplifyImages接口");
        sendRequestPost("http://47.95.69.163/gate/manage/simplifyImages");

        //sleep(10分钟)
        logger.info("sleep 10m");
        Thread.sleep(10*60*1000);

        //调用统计接口查看当前数据,将结果以日志的方式输出
        logName = "log-after-simplifyImages.log";
        invokeStatisticPostMethodAndSaveLog("http://47.95.71.16/statistic?scope=1922", logName);
    }


    private void invokeStatisticPostMethodAndSaveLog(String url, String logName) {
        logger.info("");
        logger.info("");
        logger.info("调用统计接口查看当前数据,将结果以日志的方式输出");
        String resopnse = "";
        for (int i=0; i<5; i++) {
            if (StringUtils.isEmpty(resopnse)) {
                resopnse = sendRequestGet(url);
            } else {
                break;
            }
        }
        String logFullName = ARCHIVE_LOG_DIR_PATH + File.separator + logName;
        fileUtil.writeContentToFile(logFullName, resopnse);
    }
    private String sendRequestPost(String url) {
        try {
            String json = " {" +
                    "\"request_id\":\"" + UUID.randomUUID().toString() + "\"," +
                    "\"scope\":\"1922\"" +
                    "}";
            HttpExecutorUtil executorUtil = new HttpExecutorUtil();
            executorUtil.doPostJson(url, json);

            return executorUtil.getResponse();
        } catch (Exception e) {
            logger.error(e.toString());
        }

        return "";

    }

    private String sendRequestGet(String url) {
        try {
            HttpExecutorUtil executorUtil = new HttpExecutorUtil();
            executorUtil.doGet(url);

            return executorUtil.getResponse();
        } catch (Exception e) {
            logger.error(e.toString());
        }

        return "";

    }

}
