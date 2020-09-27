package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.other;

import lombok.Getter;

/**
 * 查询类型
 */
public enum EnumFindType {

    DAY("DAY", "日"),
    WEEK("WEEK", "周"),
    MONTH("MONTH", "月"),
    QUARTER("QUARTER", "季"),
    YEAR("YEAR", "年"),
    ALL("ALL", "全部");

    EnumFindType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @Getter
    private final String type;

    @Getter
    private final String name;
}
