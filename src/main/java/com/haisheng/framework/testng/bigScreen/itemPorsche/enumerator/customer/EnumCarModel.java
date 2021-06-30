package com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.customer;

import lombok.Getter;

/**
 * 车型枚举
 */
public enum EnumCarModel {

    PANAMERA(EnumCarStyle.PANAMERA.getStyleId(), "36"),

    PANAMERA_TEN_YEARS_EDITION(EnumCarStyle.PANAMERA.getStyleId(), "37"),

    PANAMERA_TURBO_S_E_HYBRID_SPORT_TURISMO(EnumCarStyle.PANAMERA.getStyleId(), "53"),

    MACAN(EnumCarStyle.MACAN.getStyleId(), ""),

    TAYCAN(EnumCarStyle.TAYCAN.getStyleId(), ""),

    SEVEN_ONE_EIGHT(EnumCarStyle.SEVEN_ONE_EIGHT.getStyleId(), ""),

    CAYENNE(EnumCarStyle.CAYENNE.getStyleId(), ""),

    NINE_ONE_ONE(EnumCarStyle.NINE_ONE_ONE.getStyleId(), ""),

    PANAMERA_ONLINE(EnumCarStyle.PANAMERA.getStyleId(), "82");

    EnumCarModel(String styleId, String modelId) {
        this.styleId = styleId;
        this.modelId = modelId;
    }

    @Getter
    private final String styleId;

    @Getter
    private final String modelId;
}
