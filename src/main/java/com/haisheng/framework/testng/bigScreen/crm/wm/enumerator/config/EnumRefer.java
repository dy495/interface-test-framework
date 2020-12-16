package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

public enum EnumRefer {

    PORSCHE_REFERER_ONLINE("https://servicewechat.com/wx0cf070e8eed63e90/5/page-frame.html"),

    JIAOCHEN_REFER_DAILY("https://servicewechat.com/wx4071a91527930b48/0/page-frame.html"),

    JIAOCHEN_REFER_ONLINE("https://servicewechat.com/wxbd41de85739a00c7/24/page-frame.html"),
    ;

    EnumRefer(String referer) {
        this.referer = referer;
    }

    @Getter
    private final String referer;

}
