package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * 卡券来源枚举
 *
 * @author wangmin
 * @date 2020/11/23 2:31 PM
 * @desc
 */
public enum VoucherSourceEnum {

    /**
     *
     */
    ACTIVITY("活动"),

    TRANSFER("转移"),

    MESSAGE("消息"),

    PURCHASE("购买"),

    PRESENT("赠送"),

    INTELLIGENT_REMIND("智能提醒"),

    SHOPPING_MALL("商城购买"),

    EVALUATE_REWARD("评价奖励"),

    INTEGRAL_PURCHASE("积分购买");

    private String name;

    VoucherSourceEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static VoucherSourceEnum findByName(String name) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(name), "来源不存在");
        Optional<VoucherSourceEnum> any = Arrays.stream(values()).filter(v -> v.name().equals(name)).findAny();
        Preconditions.checkArgument(any.isPresent(), "来源不存在");
        return any.get();
    }
}
