package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment;

/**
 * @author wangmin
 * @date  2020/11/19  21:20
 */
public enum DayTypeEnum {
    /**
     * 工作日
     */
    WEEKDAY,
    WEEKEND;

    public static DayTypeEnum findByType(String type) {
        if (WEEKDAY.name().equals(type)) {
            return WEEKDAY;
        }
        if (WEEKEND.name().equals(type)) {
            return WEEKEND;
        }
        throw new IllegalArgumentException("日期类型不正确");
    }
}
