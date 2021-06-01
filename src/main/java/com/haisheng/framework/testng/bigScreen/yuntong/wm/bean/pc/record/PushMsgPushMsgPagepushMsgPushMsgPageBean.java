package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.record;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 6.5. 消息记录 v1.0 (池)
 *
 * @author wangmin
 * @date 2021-06-01 18:39:23
 */
@Data
public class PushMsgPushMsgPagepushMsgPushMsgPageBean implements Serializable {
    /**
     * 描述 当前页
     * 版本 v1.0
     */
    @JSONField(name = "page")
    private Integer page;

    /**
     * 描述 当前页的数量
     * 版本 v1.0
     */
    @JSONField(name = "size")
    private Integer size;

    /**
     * 描述 每页的数量
     * 版本 v1.0
     */
    @JSONField(name = " page_size")
    private Integer  pageSize;

    /**
     * 描述 总数
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 总页数
     * 版本 v1.0
     */
    @JSONField(name = "pages")
    private Integer pages;

    /**
     * 描述 详细数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 唯一标识
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 推送原因
     * 版本 v1.0
     */
    @JSONField(name = "message_type")
    private String messageType;

    /**
     * 描述 推送原因名称
     * 版本 v1.0
     */
    @JSONField(name = "message_type_name")
    private String messageTypeName;

    /**
     * 描述 推送时间
     * 版本 v1.0
     */
    @JSONField(name = "send_time")
    private String sendTime;

    /**
     * 描述 消息内容
     * 版本 v1.0
     */
    @JSONField(name = "content")
    private String content;

    /**
     * 描述 联系电话
     * 版本 v1.0
     */
    @JSONField(name = "phone")
    private String phone;

    /**
     * 描述 接收人
     * 版本 v1.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 客户查看
     * 版本 -
     */
    @JSONField(name = "is_read")
    private Boolean isRead;

    /**
     * 描述 客户类型
     * 版本 v1.0
     */
    @JSONField(name = "customer_type_name")
    private String customerTypeName;

    /**
     * 描述 状态
     * 版本 -
     */
    @JSONField(name = "status")
    private Boolean status;

    /**
     * 描述 状态
     * 版本 -
     */
    @JSONField(name = "status_name")
    private String statusName;

}