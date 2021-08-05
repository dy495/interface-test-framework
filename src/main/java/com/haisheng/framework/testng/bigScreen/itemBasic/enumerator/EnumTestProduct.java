package com.haisheng.framework.testng.bigScreen.itemBasic.enumerator;

import lombok.Getter;

/**
 * 测试产品枚举
 *
 * @author wangmin
 * @date 2020/7/24 15:11
 */
public enum EnumTestProduct {

    PORSCHE_DAILY("汽车-保时捷 日常 ", "https://servicewechat.com/wx5102264595be8c23/0/page-frame.html", "http://dev.porsche.dealer-ydauto.winsenseos.cn", "22728", "BSJ", "", true),

    PORSCHE_ONLINE("汽车-保时捷 赢识线上 ", "https://servicewechat.com/wx0cf070e8eed63e90/5/page-frame.html", "http://porsche.dealer-ydauto.winsenseos.com", "12928", "BSJ", "", false),

    PORSCHE_ONLINE_CLIENT("汽车-保时捷 保时捷线上 ", "", "http://porsche.dealer-ydauto.winsenseos.com", "12732", "BSJ", "", false),

    XD_DAILY("门店 日常 ", "https://servicewechat.com/wx937d85ff683db555/0/page-frame.html", "http://dev.inspect.store.winsenseos.cn", "43072", "MD", "", true),

    XD_ONLINE("门店 线上 ", "http://inspect.store.winsenseos.com/authpage/login", "http://inspect.store.winsenseos.com", "14630", "MD", "", false),

    INS_DAILY("门店-INS 日常 ", "https://servicewechat.com/wx937d85ff683db555/0/page-frame.html", "http://dev.inspect.store.winsenseos.cn", "", "INS", "", true),

    INS_ONLINE("门店-INS 线上 ", "http://inspect.store.winsenseos.com/authpage/login", "http://inspect.store.winsenseos.com", "", "INS", "", false),

    FK_DAILY("风控 赢识日常 ", "http://39.105.17.58/page/shoprisk/cashriskcontrol/", "http://39.105.17.58", "43072", "FK", "4944", true),

    FK_ONLINE("风控 赢识线上 ", "http://39.105.77.122/page/shoprisk/cashriskcontrol/", "http://39.105.77.122", "13260", "FK", "3515", false),

    MD_ONLINE("门店管理中心 监控 ", "http://inspect.store.winsenseos.com/", "http://inspect.store.winsenseos.com", null, "MD", null, true),

    YT_DAILY("汽车-运通 日常 ", "http://inspect.store.winsenseos.com/", "http://inspect.store.winsenseos.com", null, "YT", null, true),

    JC_DAILY_JD("汽车-轿辰 日常 ", "https://servicewechat.com/wxbd41de85739a00c7/0/page-frame.html", "http://dev.jiedai.auto.winsenseos.cn", "-1", "JC", "603", true),
    JC_DAILY_GK("汽车-轿辰 日常 ", "https://servicewechat.com/wxbd41de85739a00c7/0/page-frame.html", "http://dev.huiting.auto.winsenseos.cn", "-1", "JC", "603", true),
    JC_DAILY_ZH("汽车-轿辰 日常 ", "https://servicewechat.com/wxbd41de85739a00c7/0/page-frame.html", "http://dev.zhanghao.auto.winsenseos.cn", "-1", "JC", "603", true),
    JC_DAILY_APPLET("汽车-轿辰 日常 ", "https://servicewechat.com/wxbd41de85739a00c7/0/page-frame.html", "http://dev.dealer-jc.winsenseos.cn", "-1", "JC", "603", true),


    JC_ONLINE_JD("汽车-轿辰 线上 ", "http://jiedai.auto.winsenseos.com", "http://jiedai.auto.winsenseos.com", "-1", "JC", "395", false),
    JC_ONLINE_GK("汽车-轿辰 线上 ", "http://huiting.auto.winsenseos.com", "http://huiting.auto.winsenseos.com", "-1", "JC", "395", false),
    JC_ONLINE_ZH("汽车-轿辰 线上 ", "http://zhanghao.auto.winsenseos.com", "http://zhanghao.auto.winsenseos.com", "-1", "JC", "395", false),


    YT_DAILY_JD("汽车-运通 日常 ", "http://jiedai.auto.winsenseos.com", "http://jiedai.auto.winsenseos.com", "-1", "YT", null, true),
    YT_DAILY_GK("汽车-运通 日常 ", "http://huiting.auto.winsenseos.com", "http://huiting.auto.winsenseos.com", "-1", "YT", null, true),
    YT_DAILY_ZH("汽车-运通 日常 ", "http://zhanghao.auto.winsenseos.com", "http://zhanghao.auto.winsenseos.com", "-1", "YT", null, true),

    YT_ONLINE_JD("汽车-运通 线上 ", "http://jiedai.auto.winsenseos.com", "http://jiedai.auto.winsenseos.com", "-1", "YT", null, false),
    YT_ONLINE_GK("汽车-运通 线上 ", "http://huiting.auto.winsenseos.com", "http://huiting.auto.winsenseos.com", "-1", "YT", null, false),
    YT_ONLINE_ZH("汽车-运通 线上 ", "http://zhanghao.auto.winsenseos.com", "http://zhanghao.auto.winsenseos.com", "-1", "YT", null, false),


    MALL_DAILY("购物中心 日常 ", "", "http://dev.mall.store.winsenseos.cn", "", "MALL", null, true),
    MALL_ONLINE("购物中心 线上 ", "", "http://mall.store.winsenseos.cn", "", "MALL", null, false),


    CMS_DAILY("管理后台 日常 ", "", "http://39.106.253.190", "", "CMS", "", true),
    CMS_ONLINE("管理后台 线上 ", "", "", "", "CMS", "", false),

    ;

    EnumTestProduct(String desc, String referer, String ip, String shopId, String abbreviation, String roleId, Boolean isDaily) {
        this.desc = desc;
        this.referer = referer;
        this.ip = ip;
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
    private final String ip;
    @Getter
    private final String shopId;
    @Getter
    private final String abbreviation;
    @Getter
    private final String roleId;
    @Getter
    private final Boolean isDaily;
}
