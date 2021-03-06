package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class PorscheDeliverInfo implements Serializable {

    private long id;
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
