package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.config;

import lombok.Getter;

public enum EnumAddress {

    JIAOCHEN_DAILY("http://dev.dealer-jc.winsenseos.cn"),

    JIAOCHEN_ONLINE("http://nb.jiaochenclub.cn"),
    ;

    EnumAddress(String address) {
        this.address = address;
    }

    @Getter
    private final String address;
}
