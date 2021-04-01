package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.enumerator;

import lombok.Getter;

import java.util.Arrays;

/**
 * 测试产品枚举
 *
 * @author wangmin
 * @date 2020/7/24 15:11
 */
public enum IpPortEnum {

    CRM_DAILY("http://dev.porsche.dealer-ydauto.winsenseos.cn"),

    CRM_ONLINE("http://porsche.dealer-ydauto.winsenseos.com"),

    CRM_ONLINE_PORSCHE(CRM_ONLINE.getAddress()),

    JIAOCHEN_DAILY("http://dev.dealer-jc.winsenseos.cn"),

    JIAOCHEN_ONLINE("http://nb.jiaochenclub.com"),

    MENDIAN_DAILY("http://http:/dev.inspect.store.winsenseos.cn"),

    INS_DAILY("http://api.litemall.winsenseos.com"),

    RISK_CONTROL_DAILY("http://127.0.0.1"),
    ;

    IpPortEnum(String ipPort) {
        this.address = ipPort;
    }

    @Getter
    private final String address;

    public static String getContainAddress(String url) {
        return Arrays.stream(IpPortEnum.values()).filter(e -> url.contains(e.getAddress())).map(IpPortEnum::getAddress).findFirst().orElse(JIAOCHEN_DAILY.getAddress());
    }
}
