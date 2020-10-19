package com.haisheng.framework.testng.bigScreen.crm.wm.pojo;

import lombok.Data;

/**
 * 每日接待订车表
 */
@Data
public class TPorscheOrderCarDO {

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
    private String orderTime;
    private java.sql.Timestamp gmtCreate;
}
