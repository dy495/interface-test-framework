package com.haisheng.framework.model.experiment.operator;

import org.jooq.Condition;
import org.jooq.Field;

/**
 * @author wangmin
 * @date 2020/7/20 10:57
 */
public interface IOperator {
    <T> Condition operator(Field<T> field, T... values);

    <T> boolean compare(T actual, T... expect);
}
