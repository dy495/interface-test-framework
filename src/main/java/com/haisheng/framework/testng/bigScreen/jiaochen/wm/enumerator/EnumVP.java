package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

/**
 * 特定卡券&套餐
 */
public enum EnumVP {
    ONE("凯迪拉克无限卡券", "凯迪拉克无限套餐"),

    TWO("剩余库存不足卡券", "剩余卡券不足套餐"),
    ;

    EnumVP(String voucherName, String packageName) {
        this.voucherName = voucherName;
        this.packageName = packageName;
    }

    @Getter
    private final String voucherName;
    @Getter
    private final String packageName;
}
