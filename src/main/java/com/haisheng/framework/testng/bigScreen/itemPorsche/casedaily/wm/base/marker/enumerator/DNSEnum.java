package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.marker.enumerator;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * 测试产品枚举
 *
 * @author wangmin
 * @date 2020/7/24 15:11
 */
public enum DNSEnum {

    CRM_DAILY("http://dev.porsche.dealer-ydauto.winsenseos.cn"),

    CRM_ONLINE("http://porsche.dealer-ydauto.winsenseos.com"),

    CRM_ONLINE_PORSCHE(CRM_ONLINE.getAddress()),

    JIAOCHEN_DAILY("http://dev.dealer-jc.winsenseos.cn"),

    JIAOCHEN_ONLINE("http://nb.jiaochenclub.com"),

    MENDIAN_DAILY("http://http:/dev.inspect.store.winsenseos.cn"),

    INS_DAILY("http://api.litemall.winsenseos.com"),

    RISK_CONTROL_DAILY("http://127.0.0.1"),

    YUNTONG_DAILY("http://192.168.50.3:7700"),

    YUNTONG_DAILY_2("待定"),
    ;

    DNSEnum(String ipPort) {
        this.address = ipPort;
    }

    @Getter
    private final String address;

    public static String getContainAddress(@NotNull String url) {
        return Arrays.stream(DNSEnum.values()).map(DNSEnum::getAddress).filter(url::contains).findFirst().orElse(JIAOCHEN_DAILY.getAddress());
    }
}
