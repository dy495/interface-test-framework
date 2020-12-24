package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;


@Data
public class EdgePvAccuracy implements Serializable {

    private String status;
    private String actual;
    private String expect;
    private String pvAccuracyRate;
    private String updateTime;
    private String image;
    private String video;

}
