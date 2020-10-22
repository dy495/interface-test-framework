package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2020/8/10 21:14
 */
public enum EnumAppletCode {

    WM("w", "7t7orXVrbzO2YkPYrDIt1w==", "oQwmJ5Lm1mlt4HXbxYuZi8L7CW90", "kCrDHnR0m+ey1x/og8XDoQ=="),
    WM_SMALL("纯牛奶", "mMzbGVrYnoPRmXCs4nfPdg==", "", "e7KgTxVJJTGokmjSk2exQA=="),

    XMF("@@@", "cM/ZzMuU5QJqMUEZWEI8vg==", "", ""),

    BB("北北", "DJi5GeKsEMIMe3H4XEeqmw==", "", ""),    //请勿使用

    LXQ("泡芙", "sTyWUt/pmhwGAV1sf0YWrQ==", "", ""),

    GLY("Max", "AFhk0ye7mJLjpTdCJfnxAA==", "", "");

    EnumAppletCode(String weChatName, String code, String weChatId, String codeOnline) {
        this.code = code;
        this.weChatName = weChatName;
        this.weChatId = weChatId;
        this.codeOnline = codeOnline;
    }

    @Getter
    private final String code;
    @Getter
    private final String weChatName;
    @Getter
    private final String weChatId;

    @Getter
    private final String codeOnline;
}
