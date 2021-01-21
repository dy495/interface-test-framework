package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral;

/**
 * @author wangmin
 * @date 2020/12/15 3:26 PM
 * @desc 排序方式枚举
 */
public enum SortTypeEnum {
    /**
     * 升序
     */
    UP(0, "升序"),

    /**
     * 降序
     */
    DOWN(1, "降序");

    private Integer id;

    private String desc;

    SortTypeEnum(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
