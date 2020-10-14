package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * 产品网址枚举
 *
 * @author wangmin
 * @date 2020/7/24 15:33
 */
public enum EnumAddress {
    /**
     * 保时捷日常
     */
    PORSCHE("http://dev.porsche.dealer-ydauto.winsenseos.cn"),
    /**
     * 保时捷线上
     */
    PORSCHE_ONLINE("http://porsche.dealer-ydauto.winsenseos.com");

    EnumAddress(String address) {
        this.address = address;
    }

    @Getter
    private final String address;
}
