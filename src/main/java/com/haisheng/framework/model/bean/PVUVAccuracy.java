package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;


@Data
public class PVUVAccuracy implements Serializable {

    private String status;
    private String pvAccuracyRate;
    private String uvAccuracyRate;
    private String updateTime;
    private String image;
    private String video;

}
