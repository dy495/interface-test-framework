package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/11/20 10:55 AM
 * @desc 使用范围
 */
public enum UseTypeEnum {
    FIXED(0, "固定套餐"),
    TEMPORARY(1, "临时套餐"),
    ;

    private Integer id;

    private String name;

    UseTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static UseTypeEnum findById(Integer id) {
        Preconditions.checkArgument(null != id, "类型不存在");
        Optional<UseTypeEnum> any = Arrays.stream(values()).filter(v -> v.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "类型不存在");
        return any.get();
    }
}
