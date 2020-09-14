package com.haisheng.framework.model.experiment.enumerator.customer;

import lombok.Getter;

/**
 * 客户到店状态
 *
 * @author wangmin
 * @date 2020/8/12 18:27
 */
public enum EnumReceptionType {

    FIRST_VISIT("FIRST_VISIT", "首次到店"),

    INVITATION("INVITATION", "邀约"),

    AGAIN_VISIT("AGAIN_VISIT", "再次到店");

    EnumReceptionType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @Getter
    private final String type;
    @Getter
    private final String name;
}
