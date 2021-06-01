package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.noticemessage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 32.1. 拉取全局提醒消息内容
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class PullBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 全局消息提醒内容
     * 版本 v5.3
     */
    @JSONField(name = "message_content")
    private String messageContent;

}