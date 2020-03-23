package com.haisheng.framework.model.bean;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProtectTime {

    private int shopId;
    private int channelId;
    private int channelStaffId;
    private String customerName;
    private String phone;
    private String protectTime;

}
