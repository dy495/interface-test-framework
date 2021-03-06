package com.haisheng.framework.testng.bigScreen.itemBasic.base.scene;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口返回值
 */
@Data
public class Response implements Serializable {
    @JSONField(name = "message")
    private String message;
    @JSONField(name = "msg")
    private String msg;
    @JSONField(name = "code")
    private Integer code;
    @JSONField(name = "data")
    private JSONObject data;
    @JSONField(name = "request_id")
    private String requestId;
}
