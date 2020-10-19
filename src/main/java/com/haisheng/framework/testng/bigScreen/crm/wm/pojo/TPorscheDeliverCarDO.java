package com.haisheng.framework.testng.bigScreen.crm.wm.pojo;

import lombok.Data;

@Data
public class TPorscheDeliverCarDO {

    private long id;
    private String shopId;
    private long customerId;
    private String customerName;
    private String customerGender;
    private String customerPhone;
    private String customerAge;
    private String customerRegion;
    private String customerType;
    private String saleName;
    private String saleId;
    private String carStyle;
    private String carModel;
    private String deliverTime;
    private java.sql.Timestamp gmtCreate;
}
