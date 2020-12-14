package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

/**
 * 小程序我的卡券状态
 */
public enum EnumAppletVoucherStatus {
    NORMAL("未使用"),
    USED("已使用"),
    NEAR_EXPIRED("快过期"),
    EXPIRED("已过期"),
    ;

    EnumAppletVoucherStatus(String name) {
        this.name = name;
    }

    @Getter
    private final String name;

}
