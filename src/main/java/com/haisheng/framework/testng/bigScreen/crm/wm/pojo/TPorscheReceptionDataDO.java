package com.haisheng.framework.testng.bigScreen.crm.wm.pojo;

import lombok.Data;

@Data
public class TPorscheReceptionDataDO {

    private long id;
    private String shopId;
    private String receptionSale;
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
