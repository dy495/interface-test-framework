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

    /**
     * 卡券id
     */
    @JSONField(name = "voucher_id")
    private Long voucherId;

    /**
     * 剩余库存
     */
    @JSONField(name = "surplus_inventory")
    private Long surplusInventory;

    /**
     * 可用库存
     */
    @JSONField(name = "allow_use_inventory")
    private Long allowUseInventory;

    /**
     * 累计发出
     */
    @JSONField(name = "cumulative_delivery")
    private Long cumulativeDelivery;

    /**
     * 是否可增发
     */
    @JSONField(name = "is_additional")
    private Boolean isAdditional;

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
