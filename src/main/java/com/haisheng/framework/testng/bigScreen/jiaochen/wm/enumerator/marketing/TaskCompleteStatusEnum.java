package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

/**
 * @author wangmin
 * @date 2020/12/30 12:15
 */
public enum TaskCompleteStatusEnum {

    /**
     * 任务完成状态
     *
     */
    FINISHED("已完成"),
    UNFINISHED("未完成");

    private String name;

    TaskCompleteStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
