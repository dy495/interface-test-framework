package com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.other;

import lombok.Getter;

/**
 * 查询类型
 */
public enum EnumFindType {

    DAY("DAY", "按日查询"),
    WEEK("WEEK", "按周查询"),
    MONTH("MONTH", "按月查询"),
    QUARTER("QUARTER", "按季查询"),
    YEAR("YEAR", "按年查询"),
    ALL("ALL", "按全部查询");

    EnumFindType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @Getter
    private final String type;

    @Getter
    private final String name;
}
