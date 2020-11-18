package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.config;

import lombok.Getter;

public enum EnumAddress {

    JIAOCHEN_DAILY(""),

    JIAOCHE_ONLINE(""),
    ;

    EnumAddress(String address) {
        this.address = address;
    }

    @Getter
    private final String address;
}
