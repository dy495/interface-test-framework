package com.haisheng.framework.model.experiment.enumerator;

import lombok.Getter;

/**
 * 客户等级枚举
 */
public enum EnumCustomerLevel {
    G("公海", 8, "G"),

    F("战败", 6, "F");

    EnumCustomerLevel(String name, int customerLevel, String customerLevelName) {
        this.name = name;
        this.customerLevel = customerLevel;
        this.customerLevelName = customerLevelName;
    }

    @Getter
    private final String name;

    @Getter
    private final int customerLevel;

    @Getter
    private final String customerLevelName;

}
