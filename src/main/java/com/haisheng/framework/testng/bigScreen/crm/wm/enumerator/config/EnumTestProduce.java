package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * 测试产品枚举
 *
 * @author wangmin
 * @date 2020/7/24 15:11
 */
public enum EnumTestProduce {

    CRM_DAILY("汽车-保时捷 日常 ", "https://servicewechat.com/wx5102264595be8c23/0/page-frame.html", "http://dev.porsche.dealer-ydauto.winsenseos.cn", "22728", "BSJ", ""),

    CRM_ONLINE("汽车-保时捷 赢识线上 ", "https://servicewechat.com/wx0cf070e8eed63e90/5/page-frame.html", "http://porsche.dealer-ydauto.winsenseos.com", "12928", "BSJ", ""),

    CRM_ONLINE_PORSCHE("汽车-保时捷 保时捷线上 ", "", CRM_ONLINE.getAddress(), "12732", "BSJ", ""),

    JIAOCHEN_DAILY("汽车-轿辰 日常 ", "https://servicewechat.com/wxbd41de85739a00c7/", "http://dev.dealer-jc.winsenseos.cn", "-1", "JC", "603"),

    JIAOCHEN_ONLINE("汽车-轿辰 赢识线上 ", "https://servicewechat.com/wxbd41de85739a00c7/24/page-frame.html", "http://nb.jiaochenclub.com/", "-1", "JC", "395"),

    MENDIAN_DAILY("门店 日常", "", "", "4116", "XD", ""),
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
