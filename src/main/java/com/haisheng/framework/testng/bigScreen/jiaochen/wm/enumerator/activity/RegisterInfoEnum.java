package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import lombok.Getter;

public enum RegisterInfoEnum {

    NAME(1, "姓名"),
    PHONE(2, "手机号"),
    GENDER(3, "性别"),
    AGE(4, "年龄"),
    EMAIL(5, "邮箱"),
    REGISTER_COUNT(6, "报名人数"),
    ;

    RegisterInfoEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Getter
    private final Integer id;
    @Getter
    private final String name;
}
