package com.haisheng.framework.util.operator;

import org.jooq.Condition;
import org.jooq.Field;

/**
 * 是否为空
 *
 * @author wangmin
 * @date 2020/7/24 17:12
 */
public class OptIsNull implements IOperator {
    @SafeVarargs
    @Override
    public final <T> Condition operator(Field<T> field, T... values) {
        return field.isNotNull();
    }

    @SafeVarargs
    @Override
    public final <T> boolean compare(T actual, T... expect) {
        return actual == null;
    }
}
