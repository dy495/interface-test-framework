package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.sale;

import lombok.Getter;

/**
 * 卡券状态
 */
public enum EnumVoucherStatus {
    INVALID("已作废"),

    UNSENT("未发出"),

    SENT("已发出"),
    ;

    EnumVoucherStatus(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
