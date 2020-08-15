package com.haisheng.framework.model.experiment.enumerator;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2020/8/10 21:14
 */
public enum EnumAppletCode {

    WM("w", "qa_need_not_delete2");

    EnumAppletCode(String weChatName, String code) {
        this.code = code;
        this.weChatName = weChatName;
    }

    @Getter
    private final String code;
    @Getter
    private final String weChatName;
}
