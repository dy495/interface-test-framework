package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.other;

import lombok.Getter;

/**
 * 查询类型
 */
public enum EnumFindType {

    DAY("DAY"),
    WEEK("WEEK"),
    MONTH("MONTH"),
    QUARTER("QUARTER"),
    YEAR("YEAR"),
    ALL("ALL");

    EnumFindType(String type) {
        this.type = type;
    }

    @Getter
    private final String type;
}
