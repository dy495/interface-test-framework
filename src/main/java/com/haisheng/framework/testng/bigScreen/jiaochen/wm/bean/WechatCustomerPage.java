package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class WechatCustomerPage implements Serializable {
    @JSONField(name = "customer_phone")
    private String customerPhone;
    @JSONField(name = "total_price")
    private Double totalPrice;
    @JSONField(name = "customer_name")
    private String customerName;
}
