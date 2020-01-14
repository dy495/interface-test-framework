package com.haisheng.framework.model.bean;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReportTime {

    private int shopId;
    private int channelId;
    private int channelStaffId;
    private String customerName;
    private String phone;
    private String reportTime;
    private Timestamp gmtCreate;

}
