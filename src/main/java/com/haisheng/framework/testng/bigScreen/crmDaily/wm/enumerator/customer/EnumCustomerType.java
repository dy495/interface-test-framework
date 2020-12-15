package com.haisheng.framework.testng.bigScreen.crmDaily.wm.enumerator.customer;

import lombok.Getter;

/**
 * 客户状态
 */
public enum EnumCustomerType {
    CHANGE_HANDS("成交"),

    PROSPECTIVE_CUSTOMER("潜客"),

    APPOINTMENT("预约");

    EnumCustomerType(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
