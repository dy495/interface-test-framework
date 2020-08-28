package com.haisheng.framework.model.experiment.enumerator;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2020/8/10 21:14
 */
public enum EnumAppletCode {

    WM("w", "qa_need_not_delete2"),
    XMF("x", "qa_need_not_delete"),
    LXQ("l", "qa_need_not_delete1"),
    GLY("Max","AFhk0ye7mJLjpTdCJfnxAA==");

    EnumAppletCode(String weChatName, String code) {
        this.code = code;
        this.weChatName = weChatName;
    }

    @Getter
    private final String code;
    @Getter
    private final String weChatName;
}
