package com.haisheng.framework.testng.bigScreen.itemCms.common.enumerator;

import com.google.common.base.Preconditions;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum EnumDataLayout {
    B1(-1),
    B2(-2),
    B3(-3),
    B4(-4),
    B5(-5),
    B6(-6),
    L1(1),
    L2(2),
    L3(3),
    L4(4),
    L5(5),
    L6(6),
    L7(7),
    L8(8),
    L9(9),
    ;

    EnumDataLayout(Integer floorId) {
        this.floorId = floorId;
    }

    @Getter
    private final Integer floorId;

    public static EnumDataLayout finEnumByName(String name) {
        Optional<EnumDataLayout> any = Arrays.stream(EnumDataLayout.values()).filter(e -> e.name().equals(name)).findAny();
        Preconditions.checkArgument(any.isPresent(), "楼层不存在");
        return any.get();
    }
}
