package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * 产品枚举
 */
public enum EnumProduce {
    BSJ("保时捷"),

    JC("轿辰"),
    ;

    EnumProduce(String name) {
        this.name = name;
    }

    @Getter
    private final String name;

}
