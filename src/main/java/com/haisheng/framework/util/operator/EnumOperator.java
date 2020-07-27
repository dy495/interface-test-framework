package com.haisheng.framework.util.operator;

import lombok.Getter;
import org.jooq.Condition;
import org.jooq.Field;

/**
 * 比较方法枚举
 *
 * @author wangmin
 * @date 2020/7/20 11:03
 */
public enum EnumOperator {
    /**
     * 相等比较
     */
    EQ(new OptEq()),
    /**
     * 集合中
     */
    IN(new OptIn()),
    /**
     * 不为空
     */
    IS_NOT_NULL(new OptIsNotNull()),
    /**
     * 为空
     */
    IS_NULL(new OptIsNull());


    EnumOperator(IOperator operator) {
        this.operator = operator;
    }

    @SafeVarargs
    public final <T> Condition operator(Field<T> field, T... values) {
        return operator.operator(field, values);
    }

    @SafeVarargs
    public final <T> boolean compare(T actual, T... expect) {
        return operator.compare(actual, expect);
    }

    @Getter
    private final IOperator operator;
}
