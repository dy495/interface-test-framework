package com.haisheng.framework.testng.bigScreen.crm.wm.dao;

import lombok.Data;

/**
 * 每日交车表
 */
@Data
public class TPorscheDeliverInfo {

    private Long id;
    private String shopId;
    private String customerId;
    private String customerName;
    private String idNumber;
    private String birthday;
    private String address;
    private String gender;
    private String age;
    private String phones;
    private String subjectTypeName;
    private String saleName;
    private String saleId;
    private String carStyle;
    private String carModel;
    private String deliverDate;
    private String plateTypeName;
    private String defrayTypeName;
    private String sourceChannelName;
    private String payTypeName;
    private String plateNumber;
    private String vehicleChassisCode;
    private java.sql.Timestamp gmtCreate;
}
