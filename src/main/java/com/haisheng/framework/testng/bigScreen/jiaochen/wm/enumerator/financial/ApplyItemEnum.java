package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.financial;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/11/17 2:45 PM
 * @desc 申请项目
 */
public enum ApplyItemEnum {
    VOUCHER(0, "卡券"),
    ACTIVITY(1, "活动"),
    ;

    private Integer id;

    private String name;

    ApplyItemEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ApplyItemEnum findById(Integer id) {
        Preconditions.checkArgument(null != id, "类型不存在");
        Optional<ApplyItemEnum> any = Arrays.stream(values()).filter(v -> v.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "类型不存在");
        return any.get();
    }
}
