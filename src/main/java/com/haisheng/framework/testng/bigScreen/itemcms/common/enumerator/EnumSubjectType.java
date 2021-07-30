package com.haisheng.framework.testng.bigScreen.itemcms.common.enumerator;

import com.google.common.base.Preconditions;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum EnumSubjectType {
    SHOP_CLOSE(2, "门店(封闭型)"),
    MALL(3, "购物中心"),
    SHOP_OPEN(4, "门店(开放型)"),
    ;

    EnumSubjectType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Getter
    private final int id;
    @Getter
    private final String name;

    public static EnumSubjectType findEnumByName(String name) {
        Optional<EnumSubjectType> any = Arrays.stream(EnumSubjectType.values()).filter(e -> e.getName().equals(name)).findAny();
        Preconditions.checkArgument(any.isPresent(), "主体类型不存在");
        return any.get();
    }
}
