package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yuhaisheng
 */

@Data
public class OnlineAlgorithmMerge implements Serializable {
    private String scope;
    private String date;
//    private String scopeName;
//    private int personNum;
//    private int recordSum;
    private float memberPercent;
    private float avgRecord;
    private Timestamp updateTime;
    private int alarm; //0 OR 1
}