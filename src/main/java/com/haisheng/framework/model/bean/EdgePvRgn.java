package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yuhaisheng
 */

@Data
public class EdgePvRgn implements Serializable {
    private int pv;
    private int expectPV;
    private String date;
    private String video;
    private String status;
    private String pvAccuracyRate;
    private String image;
    private Timestamp updateTime;
}