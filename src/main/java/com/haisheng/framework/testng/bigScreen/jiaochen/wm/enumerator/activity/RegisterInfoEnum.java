package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import lombok.Getter;

public enum RegisterInfoEnum {

    PHONE(0,"手机号"),
    NAME(1,"姓名"),
    REGISTER_COUNT(2,"报名人数"),
    EMAIL(3,"邮箱"),
    ;
    RegisterInfoEnum(Integer id,String name){
        this.id=id;
        this.name=name;
    }
    @Getter
    private final Integer id;
    @Getter
    private final String name;
}
