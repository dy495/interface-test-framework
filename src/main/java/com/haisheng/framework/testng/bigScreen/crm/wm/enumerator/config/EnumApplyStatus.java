package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * 卡券申请审核状态
 */
public enum EnumApplyStatus {

    AGREE("已通过"),

    AUDITING("审核中"),

    REFUSAL("已拒绝"),

    CANCEL("取消"),
    ;

    EnumApplyStatus(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
