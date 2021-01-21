package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2021/1/20 13:36
 * @desc 客户卡券状态
 */
public enum CustomerVoucherStatusEnum {
    /**
     * 快过期
     */
    NEAR_EXPIRE(0, "快过期"),
    NO_USE(100, "未使用"),
    IS_USED(200, "已使用"),
    EXPIRE(300, "已过期"),
    INVALIDED(400, "已失效");

    private final Integer id;

    private final String desc;

    CustomerVoucherStatusEnum(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public static CustomerVoucherStatusEnum findById(Integer id) {
        Preconditions.checkArgument(null != id, "客户卡券状态不存在");
        Optional<CustomerVoucherStatusEnum> any = Arrays.stream(values()).filter(c -> c.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "客户卡券状态不存在");
        return any.get();
    }

    public static CustomerVoucherStatusEnum findByType(String type) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(type), "客户卡券状态不存在");
        Optional<CustomerVoucherStatusEnum> any = Arrays.stream(values()).filter(c -> c.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "客户卡券状态不存在");
        return any.get();
    }
}