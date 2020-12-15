package com.haisheng.framework.testng.bigScreen.crmDaily.wm.enumerator.config;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2020/7/24 15:03
 */
public enum EnumChecklistGateway {
    GATEWAY("http://dev.api.winsenseos.cn/retail/api/data/biz");

    EnumChecklistGateway(String gateway) {
        this.gateway = gateway;
    }

    @Getter
    private final String gateway;
}
