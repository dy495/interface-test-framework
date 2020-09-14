package com.haisheng.framework.model.experiment.enumerator.customer;

import lombok.Getter;

/**
 * 客户等级
 */
public enum EnumCustomerLevel {

    H("H级", 1, "H"),

    A("A级", 2, "A"),

    B("B级", 2, "B"),

    C("C级", 2, "C"),

    G("公海", 8, "G"),

    F("战败", 6, "F"),

    D("订车", 4, "D");

    EnumCustomerLevel(String name, int id, String level) {
        this.name = name;
        this.id = id;
        this.level = level;
    }

    @Getter
    private final String name;

    @Getter
    private final int id;

    @Getter
    private final String level;

}
