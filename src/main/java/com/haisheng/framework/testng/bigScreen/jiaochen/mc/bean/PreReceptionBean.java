package com.haisheng.framework.testng.bigScreen.jiaochen.mc.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class PreReceptionBean implements Serializable {
    @JSONField(name = "id")
    private Long id;
    @JSONField(name = "belong_sale_name")
    private String belongSaleName = null;
    @JSONField(name = "customer_id")
    private Long customerId = null;
    @JSONField(name = "customer_phone")
    private String customerPhone = null;
    @JSONField(name = "customer_name")
    private String customerName;
    @JSONField(name = "receptor_id")
    private String receptorId;
    @JSONField(name = "reception_sale_name")
    private String receptionSaleName;
    @JSONField(name = "shop_id")
    private Long shopId;
    @JSONField(name = "estimated_buy_time")
    private String estimatedBuyTime = null;

}
