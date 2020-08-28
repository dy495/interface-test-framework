package com.haisheng.framework.model.experiment.enumerator;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2020/8/10 21:14
 */
public enum EnumAppletCode {

    WM("w", "imFQ2WTJAryT4LZYncXb/Q==", "oQwmJ5Lm1mlt4HXbxYuZi8L7CW90"),
    XMF("@@@", "e5ZFeKDMd1Y77hZ6s77TGA==", ""),
    LXQ("泡芙", "sTyWUt/pmhwGAV1sf0YWrQ==", ""),
    GLY("Max","AFhk0ye7mJLjpTdCJfnxAA==","");

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
