package com.haisheng.framework.util.operator;

import org.jooq.Condition;
import org.jooq.Field;

/**
 * 集合内比较
 *
 * @author wangmin
 * @date 2020/7/20 11:02
 */
public class OptIn implements IOperator {
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
