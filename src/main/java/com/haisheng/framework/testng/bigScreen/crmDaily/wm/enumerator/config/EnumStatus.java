package com.haisheng.framework.testng.bigScreen.crmDaily.wm.enumerator.config;

import lombok.Getter;

/**
 * 自动化工具状态标识
 * 中间状态
 * status=0 表示 true
 * status=1 表示 false
 */
public enum EnumStatus {

    TRUE("0", 0, true),

    FALSE("1", 1, false),
    ;

    EnumStatus(String status, Integer type, boolean b) {
        this.status = status;
        this.type = type;
        this.b = b;
    }

    @Getter
    private final String status;

    @Getter
    private final Integer type;

    @Getter
    private final boolean b;
}
