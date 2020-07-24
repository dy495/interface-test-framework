package com.haisheng.framework.model.experiment.enumerator;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2020/7/24 15:24
 */
public enum EnumShopId {
    PORSCHE_SHOP("22728");

    EnumShopId(String shopId) {
        this.shopId = shopId;
    }

    @Getter
    private final String shopId;
}
