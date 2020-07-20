package com.haisheng.framework.model.experiment.operator;

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
    IN(new OptIn());


    EnumOperator(IOperator operator) {
        this.operator = operator;
    }

    public <T> Condition operator(Field<T> field, T... values) {
        return operator.operator(field, values);
    }

    public <T> boolean compare(T actual, T expect) {
        return operator.compare(actual, expect);
    }

    @Getter
    private final IOperator operator;
}
