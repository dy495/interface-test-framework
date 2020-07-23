package com.haisheng.framework.util.checker;

import com.haisheng.framework.model.experiment.base.IBuilder;
import com.haisheng.framework.model.experiment.except.CheckExcept;
import com.haisheng.framework.util.operator.EnumOperator;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 纯值校验
 *
 * @author wangmin
 * @date 2020/7/22 10:53
 */
public class ValueChecker implements IChecker {
    private final static Logger log = LoggerFactory.getLogger(DbChecker.class);
    private final long waitTime;
    private final Map<Object, Map<EnumOperator, Object[]>> checkMap = new HashMap<>(16);
    private final StringBuilder errorMessage = new StringBuilder();

    /**
     * 构造函数
     *
     * @param builder builder
     */
    ValueChecker(Builder builder) {
        this.waitTime = builder.waitTime;
        this.checkMap.putAll(builder.checkMap);
    }

    @Override
    public void check() {
        waitBeforeCheck();
        boolean result = valueCheck();
        if (!result) {
            throw new CheckExcept(errorMessage.toString());
        }
    }

    @Override
    public String getErrorMsg() {
        return null;
    }

    @Override
    public String getCheckerInfo() {
        return null;
    }

    /**
     * check前等待
     */
    private void waitBeforeCheck() {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            String msg = String.format("check前等待发生异常:%s", e.getMessage());
            e.printStackTrace();
            log.info(msg);
        }
    }

    /**
     * 值校验
     *
     * @return boolean
     */
    private boolean valueCheck() {
        checkMap.forEach((key, map) ->
                map.forEach(((operator, objects) -> {
                    String actualValue = key.toString();
                    String[] expectValue = Arrays.stream(objects).map(String::valueOf).toArray(String[]::new);
                    if (!operator.compare(actualValue, expectValue)) {
                        errorMessage.append(String.format("字段判定错误，actual=%s，operator=%s，expect=%s",
                                actualValue, operator.name(), Arrays.toString(expectValue)));
                    }
                })));
        return errorMessage.length() <= 0;
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder implements IBuilder<ValueChecker> {
        private long waitTime;
        private final Map<Object, Map<EnumOperator, Object[]>> checkMap = new HashMap<>(16);

        /**
         * 值与值比较
         *
         * @param actual   实际值
         * @param operator 比较方法
         * @param expect   预期值
         * @param <T>      T
         * @return builder
         */
        @SafeVarargs
        public final <T> Builder check(T actual, EnumOperator operator, T... expect) {
            if (!checkMap.containsKey(actual)) {
                checkMap.put(actual, new HashMap<>(16));
            }
            checkMap.get(actual).put(operator, expect);
            return this;
        }

        @Override
        public ValueChecker build() {
            if (checkMap.isEmpty()) {
                throw new CheckExcept("校验值为空");
            }
            return new ValueChecker(this);
        }
    }
}
