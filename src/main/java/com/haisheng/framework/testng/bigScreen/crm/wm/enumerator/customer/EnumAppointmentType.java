package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer;

import lombok.Getter;

/**
 * 预约类型
 */
public enum EnumAppointmentType {
    /**
     * 试驾
     */
    TEST_DRIVE("TEST_DRIVE", "试驾"),
    /**
     * 保养
     */
    MAINTAIN("MAINTAIN", "保养"),
    /**
     * 维修
     */
    REPAIR("REPAIR", "维修");

    EnumAppointmentType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @Getter
    private final String type;
    @Getter
    private final String name;
}
