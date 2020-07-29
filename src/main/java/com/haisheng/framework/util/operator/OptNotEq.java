package com.haisheng.framework.util.operator;

import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.Field;

/**
 * @author wangmin
 * @date 2020/7/25 13:53
 */
@Slf4j
public class OptNotEq implements IOperator {

    private final OptEq optNotEq = new OptEq();

    @Override
    public <T> Condition operator(Field<T> field, T... values) {
        return null;
    }

    @Override
    public <T> boolean compare(T actual, T... expect) {
        return !optNotEq.compare(actual, expect);
    }

    private static final int PARAM_COUNT = 1;

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
