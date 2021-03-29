package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.Integral.*;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 积分状态枚举
 *
 * @author wangmin
 * @date 2020/12/30 10:25 AM
 */
public enum IntegralExchangeStatusEnum {

    NOT_START("未开始", new NoStartIntegral.Builder()),
    WORKING("进行中", new WorkingIntegral.Builder()),
    CLOSE("已关闭", new CloseIntegral.Builder()),
    EXPIRED("已过期", new ExpiredIntegral.Builder()),
    INVALID("已失效", null);

    @Getter
    private final String desc;
    @Getter
    private final AbstractIntegral.BaseBuilder integralBuilder;

    IntegralExchangeStatusEnum(String desc, AbstractIntegral.BaseBuilder integralBuilder) {
        this.desc = desc;
        this.integralBuilder = integralBuilder;
    }

    public static IntegralExchangeStatusEnum findByDesc(String statusName) {
        Preconditions.checkArgument(null != statusName, "积分兑换状态不存在");
        Optional<IntegralExchangeStatusEnum> any = Arrays.stream(values()).filter(v -> statusName.equals(v.getDesc())).findAny();
        Preconditions.checkArgument(any.isPresent(), "积分兑换状态不存在");
        return any.get();
    }
}
