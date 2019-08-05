package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by liaoxiangru
 */

@Data
public class OnlinePVUV implements Serializable {
    private int pvEnter;
    private int pvLeave;
    private int uvEnter;
    private int uvLeave;
    private String com;
    private String date;
    private Timestamp updateTime;
    private String gender;
    private String age;
}