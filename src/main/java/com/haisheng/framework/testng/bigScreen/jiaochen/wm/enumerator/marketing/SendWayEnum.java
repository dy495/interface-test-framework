package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/11/16 11:14 AM
 * @desc 发出方式
 */
public enum SendWayEnum {
    PRESENT(0, "赠送"),
    SOLD(1, "售出"),
    PUSH(2, "推送");

    private Integer id;

    private String name;

    SendWayEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static SendWayEnum findById(Integer id) {
        Preconditions.checkArgument(null != id, "核销类型不存在");
        Optional<SendWayEnum> any = Arrays.stream(values()).filter(v -> v.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "类型类型不存在");
        return any.get();
    }
}
