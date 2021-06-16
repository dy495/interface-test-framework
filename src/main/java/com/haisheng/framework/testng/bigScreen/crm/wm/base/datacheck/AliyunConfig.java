package com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class AliyunConfig implements Serializable {

    /**
     * ak
     */
    private String accessKeyId;
    /**
     * as
     */
    private String accessKeySecret;
    /**
     * 节点
     */
    private String endPoint;


    public AliyunConfig getConfig(String path) {
        JSONObject jsonObject = JSONObject.parseObject(path);
        this.accessKeyId = jsonObject.getString("accessKeyId");
        this.accessKeySecret = jsonObject.getString("accessKeySecret");
        this.endPoint = jsonObject.getString("endPoint");
        return this;
    }
}