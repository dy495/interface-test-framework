package com.haisheng.framework.testng.bigScreen.crm.wm.pojo;

import lombok.Data;

/**
 * 每日接待记录表
 */
@Data
public class TPorscheReceptionDataDO {

    private long id;
    private String shopId;
    private int batchId;
    private String receptionSale;
    private String receptionSaleId;
    private String receptionStartTime;
    private String receptionEndTime;
    private long receptionDuration;
    private long customerId;
    private String customerName;
    private String customerTypeName;
    private String customerPhone;
    private String receptionDate;
    private java.sql.Timestamp gmtCreate;
}
