package com.haisheng.framework.testng.bigScreen.itemPorsche.base.datacheck;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 阿里云的相关配置
 *
 * @author wanmin
 * @date 2021-06-16
 */
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


    public AliyunConfig initConfig(String path) {
        JSONObject jsonObject = JSONObject.parseObject(path);
        this.accessKeyId = jsonObject.getString("accessKeyId");
        this.accessKeySecret = jsonObject.getString("accessKeySecret");
        this.endPoint = jsonObject.getString("endPoint");
        return this;
    }
}