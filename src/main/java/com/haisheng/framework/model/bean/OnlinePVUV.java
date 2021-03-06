package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yuhaisheng
 */

@Data
public class OnlinePVUV implements Serializable {
    private int pvEnter = 0;
    private int pvLeave = 0;
    private int uvEnter = 0;
    private int uvLeave = 0;
    private String com;
    private String date;
    private String hour; //all--oneday, 1--00:00~01:00  24--23:00~24:00
    private Timestamp updateTime;
    private String gender;
    private String age;

    private int alarm; //0 OR 1
    private int diffPvEnterHourDay;
    private int diffUvEnterHourDay;
    private float diffPvEnterRangeHourDay;
    private float diffUvEnterRangeHourDay;
}