package com.haisheng.framework.testng.bigScreen.crm.wm.dao;

import lombok.Data;

/**
 * 每日接待表
 */
@Data
public class TPorscheReceptionData {
    @Column("id")
    private Integer id;
    private String shopId;
    private String receptionSale;
    private Integer batchId;
    private String receptionSaleId;
    private String receptionStartTime;
    private String receptionEndTime;
    private Integer receptionDuration;
    private Integer customerId;
    private String customerName;
    private String customerTypeName;
    private String customerPhone;
    private String receptionDate;
    private java.sql.Timestamp gmtCreate;
}