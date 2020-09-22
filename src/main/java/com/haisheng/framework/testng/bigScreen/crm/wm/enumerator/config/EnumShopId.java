package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * shop_id
 *
 * @author wangmin
 * @date 2020/7/24 15:24
 */
public enum EnumShopId {
    /**
     * 保时捷shop_id
     */
    PORSCHE_SHOP("22728"),
    /**
     * 保时捷线上shop_id
     */
    PORSCHE_SHOP_ONLINE("12928");

    EnumShopId(String shopId) {
        this.shopId = shopId;
    }

    @Getter
    private final String shopId;
}
