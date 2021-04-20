package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.message;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author zhangxiaolong
 * @date 2021/3/29 4:53 PM
 * @desc
 */
public enum ConsumeTypeEnum {
    /**
     * 0
     */
    ZERO("0", 0.00, 0.00),
    INNER_THOUSAND("1-999", 0.01, 999.99),
    INNER_TWO_THOUSAND("1000-1999", 1000.00, 1999.99),
    INNER_THREE_THOUSAND("2000-2999", 2000.00, 2999.99),
    INNER_FOUR_THOUSAND("3000-3999", 3000.00, 3999.99),
    INNER_FIVE_THOUSAND("4000-4999", 4000.00, 4999.99),
    UP_FIVE_THOUSAND("5000以上", 5000.00, Double.MAX_VALUE),
    ;

    private String desc;

    private Double min;

    private Double max;

    ConsumeTypeEnum(String desc, Double min, Double max) {
        this.desc = desc;
        this.min = min;
        this.max = max;
    }

    public String getDesc() {
        return desc;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public static ConsumeTypeEnum findByConsume(Double consume) {
        Preconditions.checkArgument(null != consume, "消费金额不能为空");
        Optional<ConsumeTypeEnum> any = Arrays.stream(values()).filter(v -> consume >= v.getMin() && consume <= v.getMax()).findAny();
        Preconditions.checkArgument(any.isPresent(), "消费类型不正确");
        return any.get();
    }
}
