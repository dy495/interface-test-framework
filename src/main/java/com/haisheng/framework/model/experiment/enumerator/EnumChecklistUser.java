package com.haisheng.framework.model.experiment.enumerator;

import lombok.Getter;

/**
 * 人员枚举
 *
 * @author wangmin
 * @date 2020/7/24 14:31
 */
public enum EnumChecklistUser {
    WM("王敏");

    EnumChecklistUser(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
