package com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator;

import com.haisheng.framework.util.MD5Util;
import lombok.Getter;

public enum AccountEnum {

    YUE_XIU_DAILY("yuexiu@test.com", "yuexiu", true, "18513118484", "越秀测试账号", "", 2, "总管理员"),
    YUE_XIU_ONLINE("storedemo@winsense.ai", "storedemo", false, "12313123132", "管理员", "", 2, "总管理员"),


    ZD("zhengda@zhengda.com", encrypt("zhengda"), false, null, "正大", "正大", null, null),
    LZ("jiekeqiongsi@jiekeqiongsi.com", encrypt("jiekeqiongsi"), true, null, "杰克琼斯", "绫致", null, null),
    DDC("ddc@ddc.com", encrypt("ddc"), false, "", "电动车", "电动车", null, ""),
    BGY("baiguoyuan@winsense.ai", encrypt("baiguoyuan"), false, "", "百果园管理员", "百果园", null, ""),
    ;

    AccountEnum(String username, String password, boolean isDaily, String phone, String name, String subjectName, Integer roleId, String roleName) {
        this.username = username;
        this.password = password;
        this.isDaily = isDaily;
        this.phone = phone;
        this.name = name;
        this.subjectName = subjectName;
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
    private final String subjectName;

    @Getter
    private final Integer roleId;

    @Getter
    private final String roleName;

    private static String encrypt(String password) {
        return new MD5Util().getMD5(password);
    }
}
