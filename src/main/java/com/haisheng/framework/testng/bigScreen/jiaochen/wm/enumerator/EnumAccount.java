package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

/**
 * 成员账号
 */
public enum EnumAccount {

    MARKETING_DAILY("15321527989", "000000", "营销管理", true, "", "", ""),

    ALL_AUTHORITY_DAILY("13402050025", "000000", "角色全部", true, "预约应答13402050025", "2942", "46522"),

    ALL_AUTHORITY_DAILY_LXQ("13114785236", "000000", "系统管理员", true, "轿辰（赢识测试）", "603", "46522"),

    ALL_AUTHORITY_ONLINE("15037286013", "000000", "所有权限", false, "接待顾问2", "2227", "20034"),

    MARKETING_ONLINE("15321527989", "000000", "营销管理", false, "", "", ""),

    ALL_JC_ONLINE("15711200001", "000000", "所有权限", false, "轿辰线上", "395", "-1"),

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
