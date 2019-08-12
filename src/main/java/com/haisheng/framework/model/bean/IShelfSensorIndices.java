package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by liaoxiangru
 */

@Data
public class IShelfSensorIndices implements Serializable {
    private String unitCode;
    private String action;
    private String date;
    private int standardWeight;
    private int repeatTimes;
    private double avg;
    private double variance;
    private double stdDeviation;
    private double max;
    private double min;
}