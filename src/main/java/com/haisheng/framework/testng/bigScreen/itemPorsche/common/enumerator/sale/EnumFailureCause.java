package com.haisheng.framework.testng.bigScreen.itemPorsche.common.enumerator.sale;

import lombok.Getter;

/**
 * 战败原因枚举
 */
public enum EnumFailureCause {
    OTHER_STORE_PURCHASE_CAR("他店购车", "OTHER_STORE_PURCHASE_CAR"),

    BUY_COMPETING_PRODUCTS("购买竞品", "BUY_COMPETING_PRODUCTS"),

    GIVE_UP_TO_BUY("放弃购车", "GIVE_UP_TO_BUY");

    EnumFailureCause(String name, String cause) {
        this.name = name;
        this.cause = cause;
    }

    @Getter
    private final String name;
    @Getter
    private final String cause;

}
