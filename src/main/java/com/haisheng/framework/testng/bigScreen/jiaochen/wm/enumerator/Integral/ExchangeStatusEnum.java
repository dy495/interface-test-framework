package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/12/15 5:47 PM
 * @desc 兑换状态
 */
public enum ExchangeStatusEnum {
    /**
     * 待发货
     */
    TO_BE_DELIVERED("待发货", OrderStatusEnum.WAITING),

    /**
     * 待收货
     */
    TO_BE_RECEIVED("待收货", OrderStatusEnum.SEND),

    /**
     * 已完成
     */
    FINISHED("已完成", OrderStatusEnum.FINISHED);

    private String name;

    private OrderStatusEnum orderStatusEnum;

    ExchangeStatusEnum(String name, OrderStatusEnum orderStatusEnum) {
        this.name = name;
        this.orderStatusEnum = orderStatusEnum;
    }

    public String getName() {
        return name;
    }

    public OrderStatusEnum getOrderStatusEnum() {
        return orderStatusEnum;
    }

    public static ExchangeStatusEnum findByType(String type) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(type), "兑换状态不能为空");
        Optional<ExchangeStatusEnum> any = Arrays.stream(values()).filter(v -> v.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "兑换状态不能为空");
        return any.get();
    }
}
