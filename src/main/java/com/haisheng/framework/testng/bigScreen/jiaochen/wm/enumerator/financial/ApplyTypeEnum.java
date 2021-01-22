package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.financial;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/11/17 2:59 PM
 * @desc 申请类型
 */
public enum ApplyTypeEnum {
    /**
     * 首发
     */
    VOUCHER(0, "首发"),
    /**
     * 增发
     */
    ADDITIONAL(1, "增发"),
    ;

    private final Integer id;

    private final String name;

    ApplyTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ApplyTypeEnum findById(Integer id) {
        Preconditions.checkArgument(null != id, "类型不存在");
        Optional<ApplyTypeEnum> any = Arrays.stream(values()).filter(v -> v.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "类型不存在");
        return any.get();
    }
}
