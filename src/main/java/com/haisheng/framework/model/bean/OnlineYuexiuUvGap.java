package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yuhaisheng
 */

@Data
public class OnlineYuexiuUvGap implements Serializable {
    private String date;
    private String hour; //all--oneday, 1--00:00~01:00  24--23:00~24:00
    private int datangUvEnter;
    private int shopUvEnter;
    private Timestamp updateTime;
    private int diffUvEnterHourDay;
    private float diffUvEnterRangeHourDay;
}