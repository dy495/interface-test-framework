package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class AfterSaleCustomerPageVO implements Serializable {
    @JSONField(name = "car_id")
    private Long carId;
    @JSONField(name = "shop_id")
    private Long shopId;
    @JSONField(name = "newest_miles")
    private Integer newestMiles;
    @JSONField(name = "repair_customer_name")
    private String repairCustomerName;
    @JSONField(name = "repair_customer_phone")
    private String repairCustomerPhone;
    @JSONField(name = "import_date")
    private String importDate;
}
