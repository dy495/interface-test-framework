package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * 人员枚举
 *
 * @author wangmin
 * @date 2020/7/24 14:31
 */
public enum EnumChecklistUser {
    WM("王敏"),

    QQ("青青"),

    GLY("郭丽雅");
    ;

    EnumChecklistUser(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
