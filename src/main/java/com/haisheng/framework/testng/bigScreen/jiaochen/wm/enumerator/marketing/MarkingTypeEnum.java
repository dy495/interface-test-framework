package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * 活动类型
 *
 * @author wangmin
 * @date 2020/11/20 3:18 PM
 */
public enum MarkingTypeEnum {
    CAR_WELFARE("车福利"),
    CAR_INFORMATION("车资讯"),
    CAR_LIFE("车生活"),
    CAR_ACVITITY("车活动"),
    CAR_KNOWLEDGE("车知识");;

    private String name;

    MarkingTypeEnum(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public static MarkingTypeEnum findById(String name) {
        Preconditions.checkArgument(null != name, "类型不能为空");
        Optional<MarkingTypeEnum> any = Arrays.stream(values()).filter(v -> v.getName().equals(name)).findAny();
        Preconditions.checkArgument(any.isPresent(), "类型不能为空");
        return any.get();
    }
}
