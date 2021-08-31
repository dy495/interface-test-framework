package com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator;

import lombok.Getter;

public enum AccountEnum {

    MALL_DAILY("yuexiu@test.com", "yuexiu", true, "18513118484", "越秀测试账号", null, 2, "总管理员"),
    MALL_ONLINE("mall@mall.com", "mall", false, "18888888488", "管理员", null, 2, "总管理员"),


    MALL_ONLINE_ZD("zhengda@zhengda.com", "zhengda", false, "12345678924", "正大", "3115", 12278, "购物中心总管理员"),
    MALL_ONLINE_LY("zhicheng@winsense.ai", "zhicheng", false, "13521654152", "温州领悦广场", "36507", 12316, "购物中心总管理员"),
    ;

    AccountEnum(String username, String password, boolean isDaily, String phone, String name, String mallId, Integer roleId, String roleName) {
        this.username = username;
        this.password = password;
        this.isDaily = isDaily;
        this.phone = phone;
        this.name = name;
        this.mallId = mallId;
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
    private final String mallId;

    @Getter
    private final Integer roleId;

    @Getter
    private final String roleName;

}
