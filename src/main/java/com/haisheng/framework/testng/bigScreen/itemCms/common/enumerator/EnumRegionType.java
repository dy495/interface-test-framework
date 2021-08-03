package com.haisheng.framework.testng.bigScreen.itemCms.common.enumerator;

import lombok.Getter;

public enum EnumRegionType {
    GENERAL("普通区域");;

    EnumRegionType(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
