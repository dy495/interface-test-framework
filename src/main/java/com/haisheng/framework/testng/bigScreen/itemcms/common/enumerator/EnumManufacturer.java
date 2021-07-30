package com.haisheng.framework.testng.bigScreen.itemcms.common.enumerator;

import lombok.Getter;

public enum EnumManufacturer {
    HIKVISION("海康威视"),

    ;

    EnumManufacturer(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
