package com.haisheng.framework.model.experiment.enumerator;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2020/8/10 21:14
 */
public enum EnumAppletCode {

    WM("w", "qa_need_not_delete2", "odOAr5AhHkB3AyxP35UJpSMXcmh8"),
    XMF("x", "qa_need_not_delete", ""),
    LXQ("l", "qa_need_not_delete1", "");

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
