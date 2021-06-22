package com.haisheng.framework.testng.bigScreen.yuntong.wm.enumerate;

import lombok.Getter;

public enum EnumDataCycleType {
    ALL(0),

    SEVEN(100),

    THIRTY(200),

    THIS_MONTH(300),

    LAST_MONTH(400),

    THIS_YEAR(500),

    LAST_YEAR(600),

    CUSTOM(700),
    ;

    EnumDataCycleType(int id) {
        this.id = id;
    }

    @Getter
    private final Integer id;

}
