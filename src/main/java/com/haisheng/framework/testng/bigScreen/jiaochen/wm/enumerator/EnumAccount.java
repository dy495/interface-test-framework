package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

/**
 * 成员账号
 */
public enum EnumAccount {

    MARKETING("15321527989", "000000", "营销管理", true),

    ADMINISTRATOR("15711300001", "000000", "系统管理员", true),

    MARKETING_ONLINE("15321527989", "000000", "营销管理", false),

    ADMINISTRATOR_ONLINE("15711200001", "000000", "系统管理员", false),
    ;

    EnumAccount(String phone, String password, String name, boolean isDaily) {
        this.phone = phone;
        this.password = password;
        this.isDaily = isDaily;
    }

    @Getter
    private final String phone;

    @Getter
    private final String password;

    @Getter
    private final boolean isDaily;

}
