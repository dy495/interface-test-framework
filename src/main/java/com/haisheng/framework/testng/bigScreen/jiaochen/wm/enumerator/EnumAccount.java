package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

/**
 * 成员账号
 */
public enum EnumAccount {

    JC_ALL_AUTHORITY_DAILY("13402050025", "000000", "全部角色", true, "预约应答13402050025", "2942", "46522"),

    JC_ALL_AUTHORITY_DAILY_LXQ("13114785236", "000000", "系统管理员", true, "轿辰（赢识测试）", "603", "46522"),

    JC_ALL_AUTHORITY_ONLINE("15037286012", "000000", "所有权限", false, "石矶娘娘", "2227", "20034"),

    JC_ALL_ONLINE("15711200001", "000000", "所有权限", false, "轿辰线上", "395", "-1"),

    YT_ALL_DAILY("13700000002", "000000", "超级管理员", true, "运通（赢识测试）", "7529", "-1"),

    YT_RECEPTION_DAILY("18989494435", "000000", "全部角色", true, "江远", "7881", "56666"),

    YT_RECEPTION_DAILY_LXQ("13406250003", "000000", "全部角色", true, "自动化13406250003", "7881", "56721"),

    YT_ALL_ONLINE("13700000002", "000000", "超级管理员", true, "运通（赢识测试）", "5505", "-1"),

    YT_ALL_ONLINE_LXQ("13406250004", "000000", "全部权限", true, "四号演员", "5511", "-1"),

    ;

    EnumAccount(String phone, String password, String role, boolean isDaily, String name, String roleId, String receptionShopId) {
        this.role = role;
        this.phone = phone;
        this.password = password;
        this.isDaily = isDaily;
        this.name = name;
        this.roleId = roleId;
        this.receptionShopId = receptionShopId;
    }

    @Getter
    private final String role;

    @Getter
    private final String phone;

    @Getter
    private final String password;

    @Getter
    private final boolean isDaily;

    @Getter
    private final String name;

    @Getter
    private final String roleId;

    @Getter
    private final String receptionShopId;

}
