package com.haisheng.framework.testng.bigScreen.itemCms.common.enumerator;

import lombok.Getter;

public enum EnumCloudSceneType {
    AUTO_DEFAULT("默认"),

    ;

    EnumCloudSceneType(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
