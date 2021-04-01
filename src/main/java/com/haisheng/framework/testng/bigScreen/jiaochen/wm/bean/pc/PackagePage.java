package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 套餐表单
 */
@Data
public class PackagePage implements Serializable {
    /**
     * 客户有效期
     */
    @JSONField(name = "customer_use_validity")
    private String customerUseValidity;

    /**
     * 审核状态
     */
    @JSONField(name = "audit_status_name")
    private String auditStatusName;

    /**
     * 审核状态
     */
    @JSONField(name = "audit_status")
    private String auditStatus;

    /**
     * 套餐名称
     */
    @JSONField(name = "package_name")
    private String packageName;

    /**
     * 套餐id
     */
    @JSONField(name = "package_id")
    private Long packageId;
    /**
     * 套餐价格
     */
    @JSONField(name = "price")
    private String price;
    /**
     * 卡券数量
     */
    @JSONField(name = "voucher_number")
    private Integer voucherNumber;
    /**
     * 售出数量
     */
    @JSONField(name = "sold_number")
    private Integer soldNumber;

    /**
     * 赠送数量
     */
    @JSONField(name = "give_number")
    private Integer giveNumber;

    /**
     * 创建时间
     */
    @JSONField(name = "create_time")
    private String createTime;
}

