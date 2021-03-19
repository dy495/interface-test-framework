package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 套餐详情
 */
@Data
public class PackageDetail implements Serializable {

    /**
     * 客户有效期
     */
    @JSONField(name = "customer_use_validity")
    private Integer customerUseValidity;


    /**
     * 套餐名称
     */
    @JSONField(name = "package_name")
    private String packageName;

    /**
     * 套餐说明
     */
    @JSONField(name = "package_description")
    private String packageDescription;

    /**
     * 主体类型
     */
    @JSONField(name = "subject_type")
    private String subjectType;

    /**
     * 主体详情
     */
    @JSONField(name = "subject_id")
    private Long subjectId;

    /**
     * 套餐价格
     */
    @JSONField(name = "package_price")
    private String packagePrice;

    /**
     * 卡券表单
     */
    @JSONField(name = "voucher_list")
    private JSONArray voucherList;

    @JSONField(name = "expiry_date")
    private Integer expiryDate;

    @JSONField(name = "expire_type")
    private Integer expireType;
}

