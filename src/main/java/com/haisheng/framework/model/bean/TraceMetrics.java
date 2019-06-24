package com.haisheng.framework.model.bean;


import lombok.Data;

@Data
public class TraceMetrics {

    private int   reqNum;
    private float capRatio;
    private float arcRatio;
    private float dbRatio;
    private String updateTime;
    private String userName = "百花";
    private String deviceId;
}
