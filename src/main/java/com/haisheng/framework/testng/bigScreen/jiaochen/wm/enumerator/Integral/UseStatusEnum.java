package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral;

/**
 * @author wangmin
 * @date 2020/12/16 11:09 AM
 * @desc
 */
public enum UseStatusEnum {
    /**
     * 启用
     */
    ENABLE("启用"),

    /**
     * 禁用
     */
    DISABLE("禁用");

    private final String name;

    UseStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
