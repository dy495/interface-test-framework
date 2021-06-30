package com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.customer;

import lombok.Getter;

/**
 * 客户等级
 */
public enum EnumCustomerLevel {

    H("H级", 7, "H", ""),

    A("A级", 1, "A", ""),

    B("B级", 2, "B", ""),

    C("C级", 3, "C", ""),

    D("D级", 4, "D", "交车"),

    O("O级", 5, "O", "订车"),

    F("F级", 6, "F", "战败"),

    G("G级", 14, "G", "公海");

    EnumCustomerLevel(String name, int id, String level, String des) {
        this.name = name;
        this.id = id;
        this.level = level;
        this.des = des;
    }

    @Getter
    private final String name;

    @Getter
    private final int id;

    @Getter
    private final String level;

    @Getter
    private final String des;

}
