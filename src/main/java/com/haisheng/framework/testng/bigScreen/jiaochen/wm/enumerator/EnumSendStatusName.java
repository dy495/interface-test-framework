package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

/**
 * 消息发送状态
 */
public enum EnumSendStatusName {
    SCHEDULE("排期中"),
    SENT("发送成功"),
    ;

    EnumSendStatusName(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
