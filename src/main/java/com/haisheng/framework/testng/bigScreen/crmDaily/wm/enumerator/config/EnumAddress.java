package com.haisheng.framework.testng.bigScreen.crmDaily.wm.enumerator.config;

import lombok.Getter;

/**
 * 产品域名
 * 不分项目
 * 统一管理
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
    PORSCHE_ONLINE("http://porsche.dealer-ydauto.winsenseos.com"),

    /**
     * 轿辰日常
     */
    JIAOCHEN_DAILY("http://dev.dealer-jc.winsenseos.cn"),

    /**
     * 轿辰线上
     */
    JIAOCHEN_ONLINE("http://nb.jiaochenclub.com/"),
    ;

    EnumAddress(String address) {
        this.address = address;
    }

    @Getter
    private final String address;
}
