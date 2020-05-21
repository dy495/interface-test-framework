package com.haisheng.framework.testng.algorithm;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.haisheng.framework.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

public class UploadEdgeRawJsonToCloud {

    private Logger logger     = LoggerFactory.getLogger(this.getClass());
    private FileUtil fileUtil = new FileUtil();
    private String JSON_DIR   = System.getProperty("JSON_DIR");
    private String DEBUG      = System.getProperty("DEBUG", "true");


    @Test
    private void uploadEdgeJsonToCloud() throws Exception {

        if(DEBUG.equals("true")) {
            JSON_DIR = "/Users/yuhaisheng/jason/document/work/项目/百果园/test1459json";
        }

        printPropertyParam();

        //get the json files
        List<File> fileList = getFileList();

        //send json to cloud
        sendJsonFileDataToCloud(fileList);

    }


    private void printPropertyParam() {
        logger.info("JSON_DIR: " + JSON_DIR);
    }

    private List<File> getFileList() throws Exception {
        List<File> fileList = fileUtil.getCurrentDirFilesWithoutDeepTraverse(JSON_DIR, ".json");
        if (null == fileList || fileList.size() < 1) {
            //generate time shift json files
            throw new Exception("no json files found");
        }

        return fileList;

    }

    private void sendJsonFileDataToCloud(List<File> fileList) throws Exception {
        for (int i=0; i<fileList.size(); i++) {
            apiSdkPostToCloud(fileList.get(i));
        }
    }

    private boolean apiSdkPostToCloud(File file) throws Exception {
        // 传入签名参数
        Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
        String jsonString = FileUtils.readFileToString(file,"UTF-8");
        com.haisheng.framework.testng.service.ApiRequest haisheng
                = JSON.parseObject(jsonString, com.haisheng.framework.testng.service.ApiRequest.class);

        ApiRequest json = new ApiRequest.Builder().build();
        BeanUtils.copyProperties(haisheng, json);

        // client 请求
        String gateway = "http://dev.api.winsenseos.cn/retail/api/data/device";
        ApiClient apiClient = new ApiClient(gateway, credential);
        ApiResponse apiResponse = apiClient.doRequest(json);
        String response = JSON.toJSONString(apiResponse);
        logger.info("request id: " + haisheng.getRequestId());
        logger.info(response);
        if(apiResponse.isSuccess()) {
            return true;
        } else {
            logger.error("resoponse return error, request json: " + file.getName());
        }

        return false;
    }



}
