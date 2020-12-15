package com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.enumerator;

import lombok.Getter;

/**
 * 轿辰token
 */
public enum EnumAppletCode {

    WM("Y10XjV7RMJjhjHdbCGH9WA=="),
    ;


    EnumAppletCode(String token) {
        this.token = token;
    }

    @Getter
    private final String token;

}
