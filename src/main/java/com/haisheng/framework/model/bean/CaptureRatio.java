package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
public class CaptureRatio implements Serializable {

    private String status;
    private String captureRatio;
    private String updateTime;
    private String image;
    private String video;
}
