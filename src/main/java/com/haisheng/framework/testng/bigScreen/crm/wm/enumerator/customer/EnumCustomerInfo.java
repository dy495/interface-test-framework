package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer;

import lombok.Getter;

/**
 * 客户信息枚举
 */
public enum EnumCustomerInfo {

    CUSTOMER_1("王先生", "15555555555", "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。"),

    CUSTOMER_2("刘先生", "17777777777", "");

    EnumCustomerInfo(String name, String phone, String remark) {
        this.name = name;
        this.phone = phone;
        this.remark = remark;
    }

    @Getter
    private final String name;
    @Getter
    private final String phone;
    @Getter
    private final String remark;
}
