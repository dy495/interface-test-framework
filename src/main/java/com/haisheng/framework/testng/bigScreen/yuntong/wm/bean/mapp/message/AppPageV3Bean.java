package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.message;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 12.2. 获取消息分页 （池）（2021-01-29）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppPageV3Bean implements Serializable {
    /**
     * 描述 总数 首次查询或刷新时返回
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 本次查询最后一条数据主键
     * 版本 v1.0
     */
    @JSONField(name = "last_value")
    private JSONObject lastValue;

    /**
     * 描述 展示列（部分接口返回列按权限展示时需要）
     * 版本 v4.0
     */
    @JSONField(name = "key_list")
    private JSONArray keyList;

    /**
     * 描述 key名称（展示列名称）
     * 版本 v4.0
     */
    @JSONField(name = "key_name")
    private String keyName;

    /**
     * 描述 key值（实际取值key）
     * 版本 v4.0
     */
    @JSONField(name = "key_value")
    private String keyValue;

    /**
     * 描述 返回的结果list
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 消息id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 消息标题
     * 版本 v2.0
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 描述 消息发送时间
     * 版本 v2.0
     */
    @JSONField(name = "time")
    private String time;

    /**
     * 描述 是否已读
     * 版本 v2.0
     */
    @JSONField(name = "is_read")
    private Boolean isRead;

    /**
     * 描述 消息类型 type(MAINTAIN_OVERTIME:保养超时提醒，CONSULT_REPLY_OVERTIME：咨询回复超时提醒)
     * 版本 -
     */
    @JSONField(name = "message_type")
    private String messageType;

}