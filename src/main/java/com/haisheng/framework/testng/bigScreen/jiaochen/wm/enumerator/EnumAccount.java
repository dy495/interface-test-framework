package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

/**
 * 成员账号
 */
public enum EnumAccount {

    MARKETING("15321527989", "000000", "营销管理", "daily"),
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
