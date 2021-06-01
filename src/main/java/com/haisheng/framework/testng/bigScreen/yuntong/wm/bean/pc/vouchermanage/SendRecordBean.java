package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 7.15. 领取记录 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class SendRecordBean implements Serializable {
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
     * 描述 记录id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 领取时间
     * 版本 v2.0
     */
    @JSONField(name = "send_time")
    private String sendTime;

    /**
     * 描述 有效期
     * 版本 v2.0
     */
    @JSONField(name = "validity_time")
    private String validityTime;

    /**
     * 描述 领取人
     * 版本 v2.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 领取人手机号
     * 版本 v2.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 优惠券使用状态
     * 版本 v2.0
     */
    @JSONField(name = "voucher_use_status")
    private String voucherUseStatus;

    /**
     * 描述 优惠券使用状态
     * 版本 v2.0
     */
    @JSONField(name = "voucher_use_status_name")
    private String voucherUseStatusName;

    /**
     * 描述 发出渠道
     * 版本 v2.0
     */
    @JSONField(name = "send_channel")
    private String sendChannel;

    /**
     * 描述 发出渠道名称
     * 版本 v2.0
     */
    @JSONField(name = "send_channel_name")
    private String sendChannelName;

    /**
     * 描述 客户标签
     * 版本 v2.0
     */
    @JSONField(name = "customer_label")
    private String customerLabel;

    /**
     * 描述 客户标签名称
     * 版本 v2.0
     */
    @JSONField(name = "customer_label_name")
    private String customerLabelName;

    /**
     * 描述 卡券码
     * 版本 v2.0
     */
    @JSONField(name = "voucher_code")
    private String voucherCode;

    /**
     * 描述 卡券名称
     * 版本 v2.2
     */
    @JSONField(name = "voucher_name")
    private String voucherName;

    /**
     * 描述 核销账号
     * 版本 v2.2
     */
    @JSONField(name = "verify_sale_account")
    private String verifySaleAccount;

    /**
     * 描述 核销日期
     * 版本 v2.2
     */
    @JSONField(name = "verify_time")
    private String verifyTime;

    /**
     * 描述 作废账号
     * 版本 v2.2
     */
    @JSONField(name = "invalid_sale_account")
    private String invalidSaleAccount;

    /**
     * 描述 作废日期
     * 版本 v2.2
     */
    @JSONField(name = "invalid_time")
    private String invalidTime;

    /**
     * 描述 主体名称
     * 版本 v2.2
     */
    @JSONField(name = "subject_name")
    private String subjectName;

    /**
     * 描述 门店名称
     * 版本 -
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 微信open_id
     * 版本 -
     */
    @JSONField(name = "open_id")
    private String openId;

    /**
     * 描述 是否可以作废
     * 版本 -
     */
    @JSONField(name = "if_can_invalid")
    private Boolean ifCanInvalid;

    /**
     * 描述 核销角色名称
     * 版本 -
     */
    @JSONField(name = "verification_role_name")
    private String verificationRoleName;

    /**
     * 描述 核销人员名称
     * 版本 -
     */
    @JSONField(name = "verification_sale_name")
    private String verificationSaleName;

    /**
     * 描述 发出方式
     * 版本 -
     */
    @JSONField(name = "send_type")
    private String sendType;

    /**
     * 描述 发出方式名称
     * 版本 -
     */
    @JSONField(name = "send_type_name")
    private String sendTypeName;

    /**
     * 描述 发出人
     * 版本 -
     */
    @JSONField(name = "sender")
    private String sender;

}