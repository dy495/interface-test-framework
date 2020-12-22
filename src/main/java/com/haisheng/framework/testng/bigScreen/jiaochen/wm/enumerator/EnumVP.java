package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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


    public static boolean isContains(String str) {
        List<String> list = Arrays.stream(EnumVP.values()).map(EnumVP::getPackageName).filter(e -> e.equals(str)).collect(Collectors.toList());
        return list.size() != 0;
    }
}
