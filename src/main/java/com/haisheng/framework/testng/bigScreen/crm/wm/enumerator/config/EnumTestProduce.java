package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * 测试产品枚举
 *
 * @author wangmin
 * @date 2020/7/24 15:11
 */
public enum EnumTestProduce {

    PORSCHE_DAILY("汽车-保时捷 日常 ", "https://servicewechat.com/wx5102264595be8c23/0/page-frame.html", "http://dev.porsche.dealer-ydauto.winsenseos.cn", "22728", "BSJ", ""),

    PORSCHE_ONLINE("汽车-保时捷 赢识线上 ", "https://servicewechat.com/wx0cf070e8eed63e90/5/page-frame.html", "http://porsche.dealer-ydauto.winsenseos.com", "12928", "BSJ", ""),

    PORSCHE_ONLINE_CLIENT("汽车-保时捷 保时捷线上 ", "", "http://porsche.dealer-ydauto.winsenseos.com", "12732", "BSJ", ""),

    JC_DAILY("汽车-轿辰 日常 ", "https://servicewechat.com/wxbd41de85739a00c7/0/page-frame.html", "http://dev.dealer-jc.winsenseos.cn", "-1", "JC", "603"),

    JC_ONLINE("汽车-轿辰 赢识线上 ", "https://servicewechat.com/wxbd41de85739a00c7/31/page-frame.html", "http://nb.jiaochenclub.com", "-1", "JC", "395"),

    XD_DAILY("门店 日常", "", "", "4116", "XD", ""),

    INS_DAILY("门店-ins 日常", "", "", "", "INS", ""),

    INS_ONLINE("门店-ins 赢识线上", "", "", "", "INS", ""),
    ;

    EnumTestProduce(String desc, String referer, String address, String shopId, String abbreviation, String roleId) {
        this.desc = desc;
        this.referer = referer;
        this.address = address;
        this.shopId = shopId;
        this.abbreviation = abbreviation;
        this.roleId = roleId;
    }

    @Getter
    private final String desc;
    @Getter
    private final String referer;
    @Getter
    private final String address;
    @Getter
    private final String shopId;
    @Getter
    private final String abbreviation;
    @Getter
    private final String roleId;
}
