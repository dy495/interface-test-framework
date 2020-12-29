package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

public enum EnumApplyTypeName {

    FIRST_PUBLISH("增发"),

    ADDITIONAL_ISSUE("首发"),
    ;

    EnumApplyTypeName(String name) {
        this.name = name;
    }

    @Getter
    private final String name;

}
