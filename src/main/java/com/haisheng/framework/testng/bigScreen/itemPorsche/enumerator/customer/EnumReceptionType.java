package com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.customer;

import lombok.Getter;

/**
 * 前台分配客户状态
 *
 * @author wangmin
 * @date 2020/8/12 18:27
 */
public enum EnumReceptionType {

    FIRST_VISIT("FIRST_VISIT", "首次到店"),

    INVITATION("INVITATION", "邀约到店"),

    AGAIN_VISIT("AGAIN_VISIT", "再次到店");

    //todo 无需接待、非客、售后客户、为接待离店

    EnumReceptionType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @Getter
    private final String type;
    @Getter
    private final String name;
}
