package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class VoucherInfoVO implements Serializable {

    @JSONField(name = "voucher_id")
    private Long voucherId;
    /**
     * 卡券名称
     */
    @JSONField(name = "voucher_name")
    private String voucherName;
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
    /**
     *
     */
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

}
