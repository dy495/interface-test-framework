package com.haisheng.framework.testng.bigScreen.itemCms.common.enumerator;

import lombok.Getter;

public enum EnumDeviceType {
    WEB_CAMERA("网络摄像机"),

    ;

    EnumDeviceType(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
