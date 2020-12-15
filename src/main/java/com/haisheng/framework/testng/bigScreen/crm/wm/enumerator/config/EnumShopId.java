package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * shop_id
 * 不分项目
 * 统一管理
 *
 * @author wangmin
 * @date 2020/7/24 15:24
 */
public enum EnumShopId {
    /**
     * 保时捷shop_id
     */
    PORSCHE_DAILY("22728"),
    /**
     * 赢识线上shop_id
     */
    WINSENSE_PORSCHE_ONLINE("12928"),

    /**
     * 保时捷线上
     */
    PORSCHE_ONLINE("12732"),

    /**
     * 轿辰日常
     */
    JIAOCHEN_DAILY("-1"),

    /**
     * 轿辰线上
     */
    JIAOCHEN_ONLINE("-1"),
    ;


    EnumShopId(String shopId) {
        this.shopId = shopId;
    }

    @Getter
    private final String shopId;
}
