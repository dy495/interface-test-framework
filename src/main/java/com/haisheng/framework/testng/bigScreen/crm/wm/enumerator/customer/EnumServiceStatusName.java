package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer;

import lombok.Getter;

/**
 * 客户接待状态
 */
public enum EnumServiceStatusName {

    /**
     * 接待中
     */
    IN_SERVICE("接待中", "#A4F0B8"),
    /**
     * 离店
     */
    LEAVE("完成接待", "#D2ECFF"),
    /**
     * 等待中
     */
    WAITING("等待中", "#FFBDBD"),

    /**
     * 他人接待 不要存不要存不要存
     */
    OTHER_IN_SERVICE("他人接待", "#DDE1E5"),

    /**
     * 订单客户
     */
    ORDER_CUSTOMER("订单客户", "#E2E3F1");

    EnumServiceStatusName(String name, String colour) {
        this.name = name;
    }

    @Getter
    private final String name;

}
