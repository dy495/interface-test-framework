package com.haisheng.framework.util.operator;

import org.jooq.Condition;
import org.jooq.Field;

/**
 * @author wangmin
 * @date 2020/7/22 11:25
 */
public class OptIsNotNull implements IOperator {

    @SafeVarargs
    @Override
    public final <T> Condition operator(Field<T> field, T... values) {
        return null;
    }

    @SafeVarargs
    @Override
    public final <T> boolean compare(T actual, T... expect) {
        return false;
    }
}
