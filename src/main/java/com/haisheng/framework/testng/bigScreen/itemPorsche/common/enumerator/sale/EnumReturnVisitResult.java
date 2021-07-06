package com.haisheng.framework.testng.bigScreen.itemPorsche.common.enumerator.sale;

import lombok.Getter;

/**
 * 回访结果枚举
 */
public enum EnumReturnVisitResult {

    NO_ONE_ANSWER("无人接听", "NO_ONE_ANSWER"),
    ANSWER("接听", "ANSWER"),
    HANG_UP("挂断", "HANG_UP"),
    OTHER("其他", "OTHER"),
    CONTACT_LATER("稍后联系", "CONTACT_LATER"),
    WRONG_NUMBER("错号", "WRONG_NUMBER"),
    FAILURE("战败", "FAILURE");

    EnumReturnVisitResult(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Getter
    private final String name;

    @Getter
    private final String type;
}
