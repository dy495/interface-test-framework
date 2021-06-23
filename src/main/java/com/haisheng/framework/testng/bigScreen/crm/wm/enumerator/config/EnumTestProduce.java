package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * 测试产品枚举
 *
 * @author wangmin
 * @date 2020/7/24 15:11
 */
public enum EnumTestProduce {

    PORSCHE_DAILY("汽车-保时捷 日常 ", "https://servicewechat.com/wx5102264595be8c23/0/page-frame.html", "http://dev.porsche.dealer-ydauto.winsenseos.cn", "22728", "BSJ", "", true),

    PORSCHE_ONLINE("汽车-保时捷 赢识线上 ", "https://servicewechat.com/wx0cf070e8eed63e90/5/page-frame.html", "http://porsche.dealer-ydauto.winsenseos.com", "12928", "BSJ", "", false),

    PORSCHE_ONLINE_CLIENT("汽车-保时捷 保时捷线上 ", "", "http://porsche.dealer-ydauto.winsenseos.com", "12732", "BSJ", "", false),

    JC_DAILY("汽车-轿辰 日常 ", "https://servicewechat.com/wxbd41de85739a00c7/0/page-frame.html", "http://dev.dealer-jc.winsenseos.cn", "-1", "JC", "603", true),

    JC_ONLINE("汽车-轿辰 赢识线上 ", "https://servicewechat.com/wxbd41de85739a00c7/0/page-frame.html", "http://www.4001000574.com", "-1", "JC", "395", false),

    XD_DAILY("门店 日常 ", "", "", "4116", "XD", "", true),

    INS_DAILY("门店-INS 日常 ", "https://servicewechat.com/wx937d85ff683db555/0/page-frame.html", "http://dev.inspect.store.winsenseos.cn", "", "INS", "", true),

    INS_ONLINE("门店-INS 线上 ", "http://inspect.store.winsenseos.com/authpage/login", "http://inspect.store.winsenseos.com", "", "INS", "", false),

    FK_DAILY("风控 赢识日常 ", "http://39.105.17.58/page/shoprisk/cashriskcontrol/", "http://39.105.17.58", "43072", "FK", "4944", true),

    FK_ONLINE("风控 赢识线上 ", "http://39.105.77.122/page/shoprisk/cashriskcontrol/", "http://39.105.77.122", "13260", "FK", "3515", false),

    MD_ONLINE("门店管理中心 监控 ", "http://inspect.store.winsenseos.com/", "http://inspect.store.winsenseos.com", null, "MD", null, true),

    YT_DAILY("汽车-运通 日常 ", "http://inspect.store.winsenseos.com/", "http://inspect.store.winsenseos.com", null, "YT", null, true),


    YT_DAILY_ZT("汽车-运通 日常 ", "http://dev.car.yuntong.winsenseos.cn/", "http://dev.car.yuntong.winsenseos.cn", "-1", "YT", null, true),
    YT_DAILY_HT("汽车-运通 日常 ", "http://dev.control.yuntong.winsenseos.cn/", "http://dev.control.yuntong.winsenseos.cn", "-1", "YT", null, true),
    YT_DAILY_ZH("汽车-运通 日常 ", "http://dev.sso.yuntong.winsenseos.cn/", "http://dev.sso.yuntong.winsenseos.cn", "-1", "YT", null, true),
    ;

    EnumTestProduce(String desc, String referer, String address, String shopId, String abbreviation, String roleId, Boolean isDaily) {
        this.desc = desc;
        this.referer = referer;
        this.address = address;
        this.shopId = shopId;
        this.abbreviation = abbreviation;
        this.roleId = roleId;
        this.isDaily = isDaily;
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
    @Getter
    private final Boolean isDaily;
}
