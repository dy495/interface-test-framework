package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer;

import lombok.Getter;

/**
 * 客户等级
 */
public enum EnumCustomerLevel {

    H("H级", 1, "H", ""),

    A("A级", 2, "A", ""),

    B("B级", 3, "B", ""),

    C("C级", 4, "C", ""),

    G("G级", 14, "G", "公海"),

    F("F级", 6, "F", "战败"),

    D("D级", 4, "D", "");

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
