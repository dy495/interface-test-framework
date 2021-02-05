package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 套餐状态
 *
 * @author wangmin
 * @date 2021/1/20 11:53
 */
public enum PackageStatusEnum {

    AUDITING(0, "审核中"),
    AGREE(1, "已通过"),
    REFUSAL(2, "已拒绝"),
    CANCEL(3, "已取消"),
    EXPIRED(4, "已过期");

    PackageStatusEnum(Integer id, String name) {
        this.name = name;
        this.id = id;
    }

    @Getter
    private final String name;
    @Getter
    private final Integer id;

    public static PackageStatusEnum findById(Integer id) {
        Preconditions.checkArgument(null != id, "套餐状态不存在");
        Optional<PackageStatusEnum> any = Arrays.stream(values()).filter(v -> id.equals(v.getId())).findAny();
        Preconditions.checkArgument(any.isPresent(), "套餐状态不存在");
        return any.get();
    }

    public static String getNameById(Integer id) {
        return Arrays.stream(PackageStatusEnum.values()).filter(e -> e.getId().equals(id)).map(PackageStatusEnum::getName).findFirst().orElse(null);
    }

}