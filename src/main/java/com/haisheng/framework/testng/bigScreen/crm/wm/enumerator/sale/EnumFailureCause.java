package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale;

import lombok.Getter;

/**
 * 战败原因枚举
 */
public enum EnumFailureCause {
    OTHER_STORE_PURCHASE_CAR("他店购车"),

    BUY_COMPETING_PRODUCTS("购买竞品"),

    GIVE_UP_TO_BUY("放弃购车");

    EnumFailureCause(String name) {
        this.name = name;
    }

    @Getter
    private final String name;

}
