package com.haisheng.framework.testng.bigScreen.crmDaily.wm.enumerator.config;

import lombok.Getter;

public enum EnumRefer {

    JIAOCHEN("https://servicewechat.com/wx4071a91527930b48/0/page-frame.html"),
    ;

    EnumRefer(String refer) {
        this.refer = refer;
    }

    @Getter
    private final String refer;
}
