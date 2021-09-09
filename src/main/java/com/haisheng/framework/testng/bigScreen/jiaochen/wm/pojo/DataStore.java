package com.haisheng.framework.testng.bigScreen.jiaochen.wm.pojo;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppTodayDataDto;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app.AppTodayTaskDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class DataStore implements Serializable {

    /**
     * pc预约列表总数
     */
    private Integer pcReceptionTotal = 0;

    /**
     * 预约列表总数
     */
    private Integer appAppointmentTotal = 0;

    /**
     * 销售接待列表总数
     */
    private Integer appPreReceptionTotal = 0;

    /**
     * 售后接待列表总数
     */
    private Integer afterReceptionTotal = 0;

    /**
     * 今日任务总数
     */
    private AppTodayTaskDto appTodayTaskDto;

    /**
     * 今日数据总数
     */
    private AppTodayDataDto appTodayDataDto;

}