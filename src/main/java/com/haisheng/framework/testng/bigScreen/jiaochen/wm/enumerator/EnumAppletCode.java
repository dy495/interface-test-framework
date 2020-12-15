package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

/**
 * 轿辰token
 */
public enum EnumAppletCode {

    WM("Y10XjV7RMJjhjHdbCGH9WA==", true),

    WM_ONLINE("", false);


    EnumAppletCode(String token, boolean isDaily) {
        this.token = token;
        this.isDaily = isDaily;
    }

    @Getter
    private final String token;

    @Getter
    private final boolean isDaily;

}
