package com.haisheng.framework.testng.bigScreen.crm.wm.base.command;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Response {

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
