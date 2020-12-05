package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

/**
 * 成员账号
 */
public enum EnumAccount {

    MARKETING("15321527989", "000000", "营销管理", "daily"),

    ADMINISTRATOR("15711300001", "000000", "系统管理员", "daily"),
    ;

    EnumAccount(String phone, String password, String name, String environment) {
        this.phone = phone;
        this.password = password;
        this.environment = environment;
    }

    @Getter
    private final String phone;

    @Getter
    private final String password;

    @Getter
    private final String environment;

}
