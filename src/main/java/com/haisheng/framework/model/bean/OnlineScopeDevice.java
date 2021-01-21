package com.haisheng.framework.model.bean;

import com.haisheng.framework.util.DateTimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
public class OnlineScopeDevice implements Serializable {

    private DateTimeUtil dateTimeUtil = new DateTimeUtil();

    String day = dateTimeUtil.getHistoryDate(0); //default value, current day, pk, nn
    Long scopeId; //pk,nn
    String scopeName;
    String deviceId = "no"; //pk,nn
    String deviceName;
    String deviceStatus;
    String deviceType;
    Timestamp deviceCreateTime;
    Timestamp createTime;
    Timestamp editTime;

}
