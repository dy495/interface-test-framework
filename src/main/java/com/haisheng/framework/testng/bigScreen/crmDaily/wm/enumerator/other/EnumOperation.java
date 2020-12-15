package com.haisheng.framework.testng.bigScreen.crmDaily.wm.enumerator.other;

import lombok.Getter;

/**
 * 内容运营页
 */
public enum EnumOperation {
    ACTIVITY_1("活动1"),
    ACTIVITY_2("活动2"),
    ACTIVITY_3("活动3"),
    PURCHASE_GUIDE("购买指南"),
    BRAND_CULTURE("品牌文化"),
    CAR_ACTIVITY("看车页"),
    ;

    EnumOperation(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
