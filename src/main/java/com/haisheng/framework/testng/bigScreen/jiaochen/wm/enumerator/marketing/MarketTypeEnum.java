package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

/**
 * @author wangmin
 * @date 2020/11/25 3:55 PM
 * @desc
 */
public enum MarketTypeEnum {
    /**
     * 卡券
     */
    VOUCHER(0, "卡券"),
    PACKAGE(1, "套餐");

    private Integer id;

    private String name;

    MarketTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
