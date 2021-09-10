package com.haisheng.framework.testng.bigScreen.itemBasic.enumerator;

import lombok.Getter;

/**
 * 成员账号
 * receptionShopId 为接待门店id、receptionShopName 为接待门店名称 只有接待权限的用例使用这两个参数
 */
public enum EnumAccount {
    //赢识主账号
    JC_DAILY_YS("13114785236", "000000", true, "轿辰（赢识测试）", "", "603", "系统管理员", "-1", "46522", ""),
    JC_DAILY_LXQ("13402050025", "000000", true, "预约应答13402050025", "uid_df9293ba", "2942", "全部角色", "-1", "46522", "吕雪晴的门店简称"),
    JC_DAILY_MC("13402050036", "000000", true, "花生", "", "11361", "新所有权限", "-1", "57912", ""),

    //赢识主账号
    JC_ONLINE_YS("15711200001", "000000", false, "轿辰线上", "", "395", "所有权限", "-1", "-1", ""),
    JC_ONLINE_LXQ("15037286012", "000000", false, "石矶娘娘", "", "2227", "所有权限", "-1", "20032", "中关村店简"),
    JC_ONLINE_MC("13412090005", "000000", false, "接待", "", "7138", "所有权限", "-1", "36319", "自动化-mc"),

    //赢识主账号
    YT_DAILY_YS("13700000002", "000000", true, "运通（赢识测试）", "", "7529", "超级管理员", "-1", "-1", ""),
    YT_DAILY_WM("18989494435", "000000", true, "江远", "", "7881", "全部角色", "-1", "56666", ""),
    YT_DAILY_LXQ("13406250003", "000000", true, "自动化13406250003", "", "7881", "全部角色", "-1", "56721", ""),
    YT_DAILY_MC("13402050043", "000000", true, "自动化使用账户", "", "10322", "接待个人", "-1", "57279", ""),

    //赢识主账号
    YT_ONLINE_YS("13700000002", "000000", false, "运通（赢识测试）", "", "5505", "超级管理员", "-1", "-1", ""),
    YT_ONLINE_LXQ("13406250004", "000000", false, "四号演员", "", "5511", "全部权限", "-1", "35250", ""),
    YT_ONLINE_MC("13406250005", "000000", false, "五号", "", "5511", "全部权限", "-1", "35827", ""),

    //管理后台主账号
    CMS_DAILY("wangmin@winsense.ai", "wangmin", true, "wangmin", "", "", "", "", "", ""),
    CMS_ONLINE("wangmin@winsense.ai", "wangmin", false, "wangmin", "", "", "", "", "", ""),

    ;

    EnumAccount(String phone, String password, boolean isDaily, String name, String uid, String roleId, String roleName, String shopId, String receptionShopId, String receptionShopName) {
        this.phone = phone;
        this.password = password;
        this.isDaily = isDaily;
        this.name = name;
        this.uid = uid;
        this.roleId = roleId;
        this.roleName = roleName;
        this.shopId = shopId;
        this.receptionShopId = receptionShopId;
        this.receptionShopName = receptionShopName;
    }

    @Getter
    private final String phone;

    @Getter
    private final String password;

    @Getter
    private final boolean isDaily;

    @Getter
    private final String name;

    @Getter
    private final String uid;

    @Getter
    private final String roleId;

    @Getter
    private final String roleName;

    @Getter
    private final String shopId;

    @Getter
    private final String receptionShopId;

    @Getter
    private final String receptionShopName;
}
