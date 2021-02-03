package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息记录页
 *
 * @author wangmin
 * @date 2021-02-03
 */
@Data
public class PushMsgRecord implements Serializable {

    /**
     * 推送原因
     */
    @JSONField(name = "message_type")
    private String messageType;

    /**
     * 推送原因名称
     */
    @JSONField(name = "message_type_name")
    private String messageTypeName;

    /**
     * 推送时间
     */
    @JSONField(name = "send_time")
    private String sendTime;

    /**
     * 消息内容
     */
    @JSONField(name = "content")
    private String content;

    /**
     * 联系电话
     */
    @JSONField(name = "phone")
    private String phone;

    /**
     * 接收人
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 客户查看
     */
    @JSONField(name = "is_read")
    private String isRead;

    /**
     * 客户类型
     */
    @JSONField(name = "customerTypeName")
    private String customerTypeName;
}
