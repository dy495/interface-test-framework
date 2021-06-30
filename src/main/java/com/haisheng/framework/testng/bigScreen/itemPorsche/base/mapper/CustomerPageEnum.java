package com.haisheng.framework.testng.bigScreen.itemPorsche.base.mapper;

import lombok.Getter;

import java.util.Arrays;

public enum CustomerPageEnum implements IEnum {

    CUSTOMER_TYPE("customer_type", "subject_type"),

//    CUSTOMER_NAME("customer_name", "customer_name"),
    ;

    CustomerPageEnum(String requestParam, String responseHeader) {
        this.requestParam = requestParam;
        this.responseHeader = responseHeader;
    }

    @Getter
    private final String requestParam;
    @Getter
    private final String responseHeader;

    @Override
    public String findAttributeByKey(String key) {
        return Arrays.stream(CustomerPageEnum.values()).filter(e -> e.getRequestParam().equals(key)).map(CustomerPageEnum::getResponseHeader).findFirst().orElse(key);
    }
}
