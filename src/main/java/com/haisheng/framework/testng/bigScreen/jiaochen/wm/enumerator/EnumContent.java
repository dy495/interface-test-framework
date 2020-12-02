package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

public enum EnumContent {

    A("2020马上就要过去，赶快来保养一下爱车吧，开开心心过年！！！"),

    B("使用此券可在适用门店享受立减50元活动！！！")
    ;

    EnumContent(String content) {
        this.content = content;
    }

    @Getter
    private final String content;
}
