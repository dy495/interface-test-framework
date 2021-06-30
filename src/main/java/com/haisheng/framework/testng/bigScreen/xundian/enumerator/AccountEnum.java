package com.haisheng.framework.testng.bigScreen.xundian.enumerator;

import lombok.Getter;

public enum AccountEnum {

    YUE_XIU_DAILY("yuexiu@test.com", "yuexiu", true, "18513118484", "越秀测试账号", 2, "总管理员"),

    YUE_XIU_ONLINE("storedemo@winsense.ai", "storedemo", false, "12313123132", "管理员", 2, "总管理员"),

    ZD("zhengda@zhengda.com", "zhengda", true, null, "正大", null, null),

    JKQS("jiekeqiongsi@jiekeqiongsi.com", "jiekeqiongsi", true, null, "杰克琼斯", null, null),
    ;

    AccountEnum(String username, String password, boolean isDaily, String phone, String name, Integer roleId, String roleName) {
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
