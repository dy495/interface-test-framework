package com.haisheng.framework.testng.bigScreen.itemPorsche.common.enumerator.customer;

import lombok.Getter;

public enum EnumChannelName {
    OLD("老客户重购"),
    NATURE("自然到访"),
    RELATIVE("亲友推荐"),
    LINE("线上垂媒"),
    APPLET("小程序"),
    ;

    EnumChannelName(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
