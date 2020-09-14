package com.haisheng.framework.model.experiment.enumerator;

import lombok.Getter;

/**
 * 网址枚举
 *
 * @author wangmin
 * @date 2020/7/24 15:33
 */
public enum EnumAddress {
    PORSCHE("http://dev.porsche.dealer-ydauto.winsenseos.cn"),
    PORSCHEONLINE("http://porsche.dealer-ydauto.winsenseos.com");

    EnumAddress(String address) {
        this.address = address;
    }

    @Getter
    private final String address;
}
