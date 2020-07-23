package com.haisheng.framework.util.operator;

import org.jooq.Condition;
import org.jooq.Field;

/**
 * @author wangmin
 * @date 2020/7/20 10:57
 */
public interface IOperator {
    <T> Condition operator(Field<T> field, T... values);

    /**
     * 比较
     *
     * @param actual 实际值
     * @param expect 预期值
     * @param <T>    T
     * @return boolean
     */
    <T> boolean compare(T actual, T... expect);
}
