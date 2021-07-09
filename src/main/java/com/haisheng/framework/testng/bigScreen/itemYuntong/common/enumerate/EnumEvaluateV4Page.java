package com.haisheng.framework.testng.bigScreen.itemYuntong.common.enumerate;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.mapper.IEnum;
import lombok.Getter;

import java.util.Arrays;

public enum EnumEvaluateV4Page implements IEnum {
    SERVICE_SALE_ID("service_sale_id", "receptor_name"),
    SHOP_ID("shop_id", "shop_id"),
    CUSTOMER_NAME("customer_name", "customer_name"),
    SCORE("score", "score"),
    ;

    EnumEvaluateV4Page(String requestParam, String responseHeader) {
        this.requestParam = requestParam;
        this.responseHeader = responseHeader;
    }

    @Getter
    private final String requestParam;
    @Getter
    private final String responseHeader;

    @Override
    public String findAttributeByKey(String key) {
        return Arrays.stream(EnumEvaluateV4Page.values()).filter(e -> e.getRequestParam().equals(key)).map(EnumEvaluateV4Page::getResponseHeader).findFirst().orElse(key);
    }
}
