package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment;

/**
 * @author wangmin
 * @date  2020/11/19  21:12
 */

public enum HalfDayTypeEnum {
    /**
     * 上午时段
     */
    MORNING("上午时段"),
    AFTERNOON("上午时段");


    private String title;

    HalfDayTypeEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
