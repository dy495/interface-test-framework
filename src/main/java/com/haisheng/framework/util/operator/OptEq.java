package com.haisheng.framework.util.operator;

import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.Field;
import com.google.common.base.Objects;

/**
 * 相等比较
 *
 * @author wangmin
 * @date 2020/7/20 11:01
 */
@Slf4j
public class OptEq implements IOperator {
    @SafeVarargs
    @Override
    public final <T> Condition operator(Field<T> field, T... values) {
        return values.length == 1 ? field.eq(values[0]) : null;
    }

    @SafeVarargs
    @Override
    public final <T> boolean compare(T actual, T... expect) {
        return equal(expect) && Objects.equal(actual, expect[0]);
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
