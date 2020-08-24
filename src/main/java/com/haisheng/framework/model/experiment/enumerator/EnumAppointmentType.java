package com.haisheng.framework.model.experiment.enumerator;

import lombok.Getter;

/**
 * 预约类型
 */
public enum EnumAppointmentType {
    /**
     * 试驾
     */
    TEST_DRIVE("TEST_DRIVE"),
    /**
     * 保养
     */
    MAINTAIN("MAINTAIN"),
    /**
     * 维修
     */
    REPAIR("REPAIR");


    EnumAppointmentType(String type) {
        this.type = type;
    }

    @Getter
    private final String type;
}
