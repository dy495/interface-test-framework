package com.haisheng.framework.model.experiment.operator;

import org.jooq.Condition;
import org.jooq.Field;

/**
 * 集合内比较
 *
 * @author wangmin
 * @date 2020/7/20 11:02
 */
public class OptIn implements IOperator {
    @Override
    public <T> Condition operator(Field<T> field, T... values) {
        return null;
    }

    @Override
    public <T> boolean compare(T actual, T... expect) {
        return false;
    }
}
