package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

public enum EnumActivity {

    ACTIVITY("长期活动", 4144L),
    ;

    EnumActivity(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    @Getter
    private final String name;
    @Getter
    private final Long id;
}
