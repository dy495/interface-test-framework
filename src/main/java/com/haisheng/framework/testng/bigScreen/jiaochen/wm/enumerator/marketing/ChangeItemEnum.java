package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2021/1/22 14:31
 */
public enum ChangeItemEnum {
    CREATE("创建"),

    EDIT("编辑优惠券"),

    ADD("增发"),

    INVALIDED("作废"),
    ;

    ChangeItemEnum(String name) {
        this.name = name;
    }

    @Getter
    private final String name;

    public static ChangeItemEnum findByName(String name) {
        Preconditions.checkArgument(null != name, "变更事项不存在");
        Optional<ChangeItemEnum> any = Arrays.stream(values()).filter(v -> name.equals(v.getName())).findAny();
        Preconditions.checkArgument(any.isPresent(), "变更事项不存在");
        return any.get();
    }

}
