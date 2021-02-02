package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 卡券页信息
 *
 * @author wangmin
 * @date 2021-01-19
 */
@Data
public class VoucherPage implements Serializable {

    @JSONField(name = "voucher_id")
    private Long voucherId;

    /**
     * 增发库存
     */
    @JSONField(name = "additional_inventory")
    private Long additionalInventory;
    /**
     * 发行库存
     */
    @JSONField(name = "issue_inventory")
    private Long issueInventory;
    /**
     * 剩余库存
     */
    @JSONField(name = "surplus_inventory")
    private Long surplusInventory;
    /**
     * 累计发出
     */
    @JSONField(name = "cumulative_delivery")
    private Long cumulativeDelivery;
    /**
     * 累计过期
     */
    @JSONField(name = "cumulative_overdue")
    private Long cumulativeOverdue;

    @JSONField(name = "if_can_invalid")
    private Boolean ifCanInvalid;

    @JSONField(name = "invalid_status_name")
    private String invalidStatusName;

    @JSONField(name = "is_additional")
    private Boolean isAdditional;
    /**
     * 成本
     */
    @JSONField(name = "cost")
    private Double cost;
    /**
     * 创建时间
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     * 审核状态
     */
    @JSONField(name = "audit_status_name")
    private String auditStatusName;

    /**
     * 优惠券类型
     */
    @JSONField(name = "voucher_type")
    private String voucherType;

    /**
     * 状态
     */
    @JSONField(name = "voucher_status")
    private String voucherStatus;

    /**
     * 卡券状态名称
     */
    @JSONField(name = "voucher_status_name")
    private String voucherStatusName;

    /**
     * 卡券名称
     */
    @JSONField(name = "voucher_name")
    private String voucherName;

    /**
     * 归属
     */
    @JSONField(name = "subject_name")
    private String subjectName;

    /**
     * 创建者姓名
     */
    @JSONField(name = "creator_name")
    private String creatorName;

    /**
     * 创建者账号
     */
    @JSONField(name = "creator_account")
    private String creatorAccount;

}
