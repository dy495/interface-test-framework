package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

/**
 * 轿辰token
 */
public enum EnumAppletCode {

    WM("Y10XjV7RMJjhjHdbCGH9WA==", true),
    XMF("3QQYlO1DtjV5mwp2hP/cwg==", true),

    WM_ONLINE("", true),
    XMF_ONLINE("zu+0zdqBw70D0R9WQK9C+A==", false);



    EnumAppletCode(String token, boolean isDaily) {
        this.token = token;
        this.isDaily = isDaily;
    }

    @Getter
    private final String token;

    @Getter
    private final boolean isDaily;

}
