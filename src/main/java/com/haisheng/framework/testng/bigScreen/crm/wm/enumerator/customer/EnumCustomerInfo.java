package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer;

import lombok.Getter;

/**
 * 客户信息枚举
 */
public enum EnumCustomerInfo {

    CUSTOMER_1("王先生", "MALE", "15555555555", "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。"),

    CUSTOMER_2("刘先生", "MALE", "17777777777", ""),

    CUSTOMER_3("王先生", "MALE", "15321527989", "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔。"),

    CUSTOMER_4("李先生", "MALE", "18888888888", "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔。");

    EnumCustomerInfo(String name, String gender, String phone, String remark) {
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.remark = remark;
    }

    @Getter
    private final String name;
    @Getter
    private final String gender;
    @Getter
    private final String phone;
    @Getter
    private final String remark;
}
