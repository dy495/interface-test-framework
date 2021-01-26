package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.voucher;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 卡券领取记录
 *
 * @author wangmin
 * @date 2021/1/26 11:08
 */
@Data
public class VoucherSendRecord implements Serializable {
    /**
     * 优惠券使用状态
     */
    @JSONField(name = "voucher_use_status")
    private String voucherUseStatus;

    /**
     * 优惠券使用状态名称
     */
    @JSONField(name = "voucher_use_status_name")
    private String voucherUseStatusName;

    /**
     * 领取人手机号
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 领取人
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 发出渠道
     */
    @JSONField(name = "send_channel")
    private String sendChannel;

    /**
     * 发出渠道名称
     */
    @JSONField(name = "send_channel_name")
    private String sendChannelName;

    /**
     * 有效期
     */
    @JSONField(name = "validity_time")
    private String validityTime;

    /**
     * 客户标签
     */
    @JSONField(name = "customer_label")
    private String customerLabel;

    /**
     * 客户标签名称
     */
    @JSONField(name = "customer_label_name")
    private String customerLabelName;


}
