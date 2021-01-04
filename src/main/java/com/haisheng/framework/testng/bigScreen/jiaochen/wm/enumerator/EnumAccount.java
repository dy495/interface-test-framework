package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

/**
 * 成员账号
 */
public enum EnumAccount {

    MARKETING("15321527989", "000000", "营销管理", true, ""),

    ADMINISTRATOR("15711300001", "000000", "系统管理员", true, "轿辰"),

    MARKETING_ONLINE("15321527989", "000000", "营销管理", false, ""),

    ADMINISTRATOR_ONLINE("15711200001", "000000", "系统管理员", false, "轿辰线上测试"),
    ;

    EnumAccount(String phone, String password, String role, boolean isDaily, String name) {
        this.phone = phone;
        this.password = password;
        this.isDaily = isDaily;
        this.name = name;
    }

    @Getter
    private final String phone;

    @Getter
    private final String password;

    @Getter
    private final boolean isDaily;

    @Getter
    private final String name;

}
