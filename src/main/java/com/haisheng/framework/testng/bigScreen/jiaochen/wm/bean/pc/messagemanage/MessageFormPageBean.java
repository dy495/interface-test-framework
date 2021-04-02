package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.messagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 12.2. 消息分页 （张）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class MessageFormPageBean implements Serializable {
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
     * 描述 消息id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 消息发送门店
     * 版本 v1.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 消息类型名
     * 版本 v1.0
     */
    @JSONField(name = "message_type_name")
    private String messageTypeName;

    /**
     * 描述 车牌号码
     * 版本 v1.0
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 描述 客户名称
     * 版本 v1.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 联系方式
     * 版本 v1.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 状态
     * 版本 v1.0
     */
    @JSONField(name = "status")
    private Integer status;

    /**
     * 描述 状态描述
     * 版本 v1.0
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 描述 发送条数
     * 版本 v1.0
     */
    @JSONField(name = "send_count")
    private Integer sendCount;

    /**
     * 描述 收到条数
     * 版本 v1.0
     */
    @JSONField(name = "receive_count")
    private Integer receiveCount;

    /**
     * 描述 推送时间
     * 版本 v1.0
     */
    @JSONField(name = "push_time")
    private String pushTime;

    /**
     * 描述 发送账号
     * 版本 v1.0
     */
    @JSONField(name = "send_account")
    private String sendAccount;

    /**
     * 描述 发送状态
     * 版本 v1.0
     */
    @JSONField(name = "send_status")
    private String sendStatus;

    /**
     * 描述 发送状态描述
     * 版本 v1.0
     */
    @JSONField(name = "send_status_name")
    private String sendStatusName;

    /**
     * 描述 消息内容
     * 版本 v1.0
     */
    @JSONField(name = "content")
    private String content;

    /**
     * 描述 消息标题
     * 版本 v3.0
     */
    @JSONField(name = "message_title")
    private String messageTitle;

    /**
     * 描述 发送时间
     * 版本 v3.0
     */
    @JSONField(name = "send_time")
    private String sendTime;

}