package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app;

import lombok.Data;

/**
 * 今日任务
 */
@Data
public class AppTodayTaskDto {

    /**
     * 预约
     */
    private String preAppointment;

    /**
     * 售后接待
     */
    private String afterReception;

    /**
     * 销售接待
     */
    private String preReception;

    /**
     * 跟进
     */
    private String preFollow;
}
