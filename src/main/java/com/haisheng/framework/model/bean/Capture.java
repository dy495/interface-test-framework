package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
public class Capture implements Serializable {

    private int id;
    private int mapId;
    private int regionId;
    private int entranceId;
    private String status;
    private int captureTotal;
    private int faceDataNotNull;
    private String captureRatio;
    private Timestamp updateTime;
    private String image;
    private String video;

}
