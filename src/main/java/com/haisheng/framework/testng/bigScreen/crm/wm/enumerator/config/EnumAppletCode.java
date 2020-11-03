package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2020/8/10 21:14
 */
public enum EnumAppletCode {

    WM("w", "OFITFs6XY8f0Hhk+otiK1g==", "oQwmJ5Lm1mlt4HXbxYuZi8L7CW90"),

    WM_SMALL("纯牛奶", "LuglNhOUrOOumkcBk6miRQ==", ""),

    XMF("@@@", "gYFGLyRAjw6KzxuudVBcGg==", ""),

    XMFONLINE("@@@", "DCvScjgLT/QX7fIrcSbvhA==", ""),

    BB("北北", "DJi5GeKsEMIMe3H4XEeqmw==", ""),    //请勿使用

    LXQ("泡芙", "sTyWUt/pmhwGAV1sf0YWrQ==", ""),

    GLY("Max", "AFhk0ye7mJLjpTdCJfnxAA==", ""),

    WM_ONLINE("w", "kCrDHnR0m+ey1x/og8XDoQ==", ""),

    WM_SMALL_ONLINE("纯牛奶", "e7KgTxVJJTGokmjSk2exQA==", "");

    EnumAppletCode(String weChatName, String code, String weChatId) {
        this.code = code;
        this.weChatName = weChatName;
        this.weChatId = weChatId;
    }

    @Getter
    private final String code;
    @Getter
    private final String weChatName;
    @Getter
    private final String weChatId;
}
