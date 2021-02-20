package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

/**
 * 卡卷生效规则
 *
 * @author wangmin
 * @date 2021/1/26 11:35
 */
public enum TaskEffectTypeEnum {

    /**
     * 任务完成状态
     */
    TIME("时间段"),
    DAY("天数生效");

    private final String name;

    TaskEffectTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
