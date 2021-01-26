package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2021/1/19  16:36
 */
public enum CustomerLabelTypeEnum {
    COMMON(0, "普通会员"),
    VIP(100, "vip会员"),
    APPLET(1000, "小程序用户"),
    PRE_SALES(2000, "售前用户"),
    AFTER_SALES(3000, "售后用户");

    private final Integer id;

    private final String typeName;

    public String getTypeName() {
        return typeName;
    }

    public Integer getId() {
        return id;
    }

    CustomerLabelTypeEnum(Integer id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public static CustomerLabelTypeEnum findById(Integer id) {
        Optional<CustomerLabelTypeEnum> any = Arrays.stream(values()).filter(t -> t.getId().equals(id))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "客户标签不存在");
        return any.get();
    }
}
