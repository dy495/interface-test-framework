package com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.customer;

import lombok.Getter;

/**
 * 车系枚举
 */
public enum EnumCarStyle {

    PANAMERA("1", "Panamera"),

    MACAN("2", "Macan"),

    TAYCAN("3", "Taycan"),

    SEVEN_ONE_EIGHT("4", "718"),

    CAYENNE("5", "Cayenne"),

    NINE_ONE_ONE("6", "911"),

    ALL(null, "全部车系");

    EnumCarStyle(String styleId, String name) {
        this.styleId = styleId;
        this.name = name;
    }

    @Getter
    private final String styleId;

    @Getter
    private final String name;

    public static String getValue(String name) {
        for (EnumCarStyle e : EnumCarStyle.values()) {
            if (e.getName().equals(name)) {
                return e.getName();
            }
        }
        return "不存在此车型";
    }
}
