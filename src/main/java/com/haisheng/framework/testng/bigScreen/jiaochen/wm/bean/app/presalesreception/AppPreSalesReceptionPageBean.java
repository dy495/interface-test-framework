package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.presalesreception;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.2.接待页
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class AppPreSalesReceptionPageBean implements Serializable {
    /**
     * 描述 接待id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 客户id
     * 版本 v1.0
     */
    @JSONField(name = "customer_id")
    private Long customerId;

    /**
     * 描述 客户电话
     * 版本 v1.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    @JSONField(name = "customer_name")
    private String customerName;
}