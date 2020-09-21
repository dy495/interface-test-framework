package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer;

import lombok.Getter;

/**
 * 车系枚举
 */
public enum EnumCarModel {

    PANAMERA("1", "36"),

    PANAMERA_TEN_YEARS_EDITION("1", "37"),


    PANAMERA_TURBO_S_E_HYBRID_SPORT_TURISMO("1", "53");

    EnumCarModel(String styleId, String modelId) {
        this.styleId = styleId;
        this.modelId = modelId;
    }

    @Getter
    private final String styleId;

    @Getter
    private final String modelId;

}