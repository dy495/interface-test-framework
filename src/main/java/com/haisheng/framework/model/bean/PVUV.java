package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yuhaisheng
 */

@Data
public class PVUV implements Serializable {
    private int id;
    private int mapId;
    private int regionId;
    private int entranceId;
    private int pv;
    private int uv;
    private int expectPV;
    private int expectUV;
    private String status;
    private String pvAccuracyRate;
    private String uvAccuracyRate;
    private String image;
    private String video;
    private Timestamp updateTime;
}