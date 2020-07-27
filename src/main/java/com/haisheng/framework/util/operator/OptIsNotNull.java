package com.haisheng.framework.util.operator;

import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.Field;

/**
 * 不为空
 *
 * @author wangmin
 * @date 2020/7/22 11:25
 */
@Slf4j
public class OptIsNotNull implements IOperator {
    private final OptIsNull optIsNull = new OptIsNull();

    @SafeVarargs
    @Override
    public final <T> Condition operator(Field<T> field, T... values) {
        return field.isNotNull();
    }

    private static final int PARAM_COUNT = 1;

    @SafeVarargs
    @Override
    public final <T> boolean compare(T actual, T... expect) {
        return !optIsNull.compare(actual, expect);
    }


    /**
     * 对预期值判断
     *
     * @param expect 预期值
     * @param <T>    T
     * @return boolean
     */
    @SafeVarargs
    private final <T> boolean equal(T... expect) {
        if (expect == null || expect.length < PARAM_COUNT || expect[0] == null) {
            log.error("相等操作至少需要2个操作数，且均不能为null");
            return false;
        }
        return true;
    }
}
