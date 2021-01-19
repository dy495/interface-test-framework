package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum VoucherTypeEnum {
    /**
     * 满减券
     */
    FULL_DISCOUNT("满减券"),
    /**
     * 折扣券
     */
    COUPON("折扣券"),
    /**
     * 商品兑换券
     */
    COMMODITY_EXCHANGE("商品兑换券"),
    /**
     * 自定义券
     */
    CUSTOM("自定义券");

    private final String desc;

    VoucherTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static VoucherTypeEnum findByType(String type) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(type), "优惠券类型不能为空");
        Optional<VoucherTypeEnum> any = Arrays.stream(values()).filter(v -> type.equals(v.name())).findAny();
        Preconditions.checkArgument(any.isPresent(), "优惠券类型不存在");
        return any.get();
    }
}
