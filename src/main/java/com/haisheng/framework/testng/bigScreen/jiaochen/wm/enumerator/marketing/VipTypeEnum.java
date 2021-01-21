package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * vip类型
 *
 * @author wangmin
 * @date 2020/11/12 20:27
 */
public enum VipTypeEnum {
    /**
     * 普通vip
     */
    COMMON(1, "普通会员"),
    /**
     * vip
     */
    VIP(10, "vip会员");


    private Integer id;

    private String value;

    VipTypeEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getId() {
        return id;
    }

    public static VipTypeEnum findById(Integer id) {
        Optional<VipTypeEnum> any = Arrays.stream(values()).filter(t -> t.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "会员类型不存在");
        return any.get();
    }

}
