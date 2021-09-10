package com.haisheng.framework.testng.bigScreen.jiaochen.mc.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class AfterReceptionBean implements Serializable {
    @JSONField(name = "id")
    private Long id;
    @JSONField(name = "customer_id")
    private Long customerId = null;
    @JSONField(name = "customer_phone")
    private String customerPhone = null;
    @JSONField(name = "customer_name")
    private String customerName;
    @JSONField(name = "reception_sale_id")
    private String receptorId;
    @JSONField(name = "reception_sale_name")
    private String receptionSaleName;
    @JSONField(name = "shop_id")
    private Long shopId;
    @JSONField(name = "after_sale_type")
    private String afterSaleType = null;
    @JSONField(name = "plate_number")
    private String plateNumber = null;

}
