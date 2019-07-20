package com.haisheng.framework.testng.algorithm;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Type;

public class UploadEdgeJsonToCloud {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private DateTimeUtil dt = new DateTimeUtil();
    private FileUtil fileUtil = new FileUtil();

    private String JSON_DIR = System.getProperty("JSON_DIR");


    @Test
    private void uploadEdgeJsonToCloud() throws Exception {

        //get all json files


        //send json to cloud
        File fh = new File("src/main/resources/test-res-repo/baiguoyuan-metircs/10.json");
        apiSdkPostToCloud(fh);


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
        String gateway = "http://dev.api.winsenseos.com/retail/api/data/device";
        ApiClient apiClient = new ApiClient(gateway, credential);
        ApiResponse apiResponse = apiClient.doRequest(json);
        String response = JSON.toJSONString(apiResponse);
        logger.info(response);
        if(apiResponse.isSuccess()) {
            return true;
        }

        return false;
    }

}
