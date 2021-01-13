package com.haisheng.framework.testng.bigScreen.crm.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 每日接待表
 */
@Data
public class TPorscheReceptionData implements Serializable {
    @JSONField(name = "auto_increment")
    private Integer id;
    private String shopId;
    @JSONField(name = "sale_name")
    private String receptionSale;
    private Integer batchId;
    private String receptionSaleId;
    @JSONField(name = "reception_time_str")
    private String receptionStartTime;
    @JSONField(name = "leave_time_str")
    private String receptionEndTime;
    private Integer receptionDuration;
    @JSONField(name = "customer_id")
    private Integer customerId;
    @JSONField(name = "customer_name")
    private String customerName;
    @JSONField(name = "customer_type_name")
    private String customerTypeName;
    @JSONField(name = "customer_phone")
    private String customerPhone;
    @JSONField(name = "day_date")
    private String receptionDate;
    private java.sql.Timestamp gmtCreate;
}
