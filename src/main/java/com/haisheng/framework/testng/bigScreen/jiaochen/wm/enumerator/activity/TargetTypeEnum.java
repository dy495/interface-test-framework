package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

/**
 * @author : wangmin
 * @date :  2020/12/23  16:26
 */
public enum  TargetTypeEnum {

    /**
     * 用户拉新
     */
    ATTRACT_NEW(true),
    /**
     * 用户留存
     */
    REPEAT_CUSTOMERS(false),
    /**
     * 客单提升
     */
    ENHANCE_CUSTOMER_ORDERS(false);

    private boolean isEnable;

    TargetTypeEnum(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public boolean isEnable() {
        return isEnable;
    }
}
