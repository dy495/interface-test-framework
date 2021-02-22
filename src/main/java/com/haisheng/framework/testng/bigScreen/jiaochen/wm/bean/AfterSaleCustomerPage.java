package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class AfterSaleCustomerPage implements Serializable {
    @JSONField(name = "car_id")
    private Long carId;
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 最新里程数
     */
    @JSONField(name = "newest_miles")
    private Integer newestMiles;

    /**
     * 总消费/元
     */
    @JSONField(name = "total_price")
    private Double totalPrice;

    @JSONField(name = "repair_customer_name")
    private String repairCustomerName;
    @JSONField(name = "repair_customer_phone")
    private String repairCustomerPhone;
    @JSONField(name = "import_date")
    private String importDate;

    /**
     * 底盘号
     */
    @JSONField(name = "vehicle_chassis_code")
    private String vehicleChassisCode;
}
