package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.app;

import lombok.Data;

/**
 * 今日数据
 */
@Data
public class AppTodayDataDto {

    /**
     * 销售接待
     */
    private String prePendingReception;

    /**
     * 售后接待
     */
    private String afterPendingReception;

    /**
     * 预约
     */
    private String prePendingAppointment;

    /**
     * 跟进
     */
    private String prePendingFollow;
}
