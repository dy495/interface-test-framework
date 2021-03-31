package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.enumerator;

import lombok.Getter;

public enum EnumAccount {

    YUE_XIU_DAILY("yuexiu@test.com", "yuexiu", true, "13874653765", "越秀测试账号", 2, "总管理员");

    EnumAccount(String username, String password, boolean isDaily, String phone, String name, Integer roleId, String roleName) {
        this.username = username;
        this.password = password;
        this.isDaily = isDaily;
        this.phone = phone;
        this.name = name;
        this.roleId = roleId;
        this.roleName = roleName;
    }

    @Getter
    private final String username;

    @Getter
    private final String password;

    @Getter
    private final boolean isDaily;

    @Getter
    private final String phone;

    @Getter
    private final String name;

    @Getter
    private final Integer roleId;

    @Getter
    private final String roleName;

}
