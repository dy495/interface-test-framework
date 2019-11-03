package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yuhaisheng
 */

@Data
public class OnlineReqNum implements Serializable {
    private String deviceId;
    private String date;
    private String hour; //1-23 and all
    private int reqNum;
    private Timestamp updateTime;
    private int alarm; //0 OR 1
    private int diffReqNumHourDay;
    private float diffReqNumRangeHourDay;

    private String deviceName;
    private String shopName;
}