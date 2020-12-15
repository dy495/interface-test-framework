package com.haisheng.framework.testng.bigScreen.crmDaily.wm.bean;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Activity {

    private BigInteger id;
    private Long shopId;
    private String activityStart;
    private String activityEnd;
    private String activityName;
    private String receptionName;
    private String receptionPhone;
    private Long customerMax;
    private String signQrCode;
    private Integer simulationCount;
    private Long roleId;
    private Long taskCustomerNum;
    private Long isDelete;
    private java.sql.Timestamp gmtCreate;
    private java.sql.Timestamp gmtModified;
}
