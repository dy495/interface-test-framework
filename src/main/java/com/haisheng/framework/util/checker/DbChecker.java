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
import java.util.List;
import java.util.Map;

/**
 * 数据库校验
 *
 * @author wangmin
 * @date 2020/7/20 10:55
 */
public class DbChecker implements IChecker {
    private final static Logger log = LoggerFactory.getLogger(DbChecker.class);
    private final long waitTime;
    private final List<Map<String, Object>> list;
    private final Map<String, Map<EnumOperator, Object[]>> checkEnumMap = new HashMap<>(16);
    private final StringBuilder errorMessage = new StringBuilder();

    /**
     * 构造函数
     *
     * @param builder builder
     */
    DbChecker(Builder builder) {
        this.waitTime = builder.waitTime;
        this.list = builder.list;
        this.checkEnumMap.putAll(builder.checkEnumMap);
    }

    @Override
    public void check() {
        waitBeforeCheck();
        if (!fieldCheck()) {
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

    @Override
    public String getCaseName() {
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
     * 字段校验
     *
     * @return boolean
     */
    private boolean fieldCheck() {
        checkEnumMap.forEach((name, check) ->
                check.forEach((opt, expect) -> {
                    String actualValue = (String) list.get(0).get(name);
                    String[] expectValue = Arrays.stream(expect).map(String::valueOf).toArray(String[]::new);
                    if (!opt.compare(actualValue, expectValue)) {
                        errorMessage.append(String.format("字段name=%s判定错误，actual=%s，operator=%s，expect=%s",
                                name, actualValue, opt.name(), Arrays.toString(expectValue)));
                    }
                }));
        return errorMessage.length() <= 0;
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder implements IBuilder<DbChecker> {
        private long waitTime;
        private List<Map<String, Object>> list;
        private final Map<String, Map<EnumOperator, Object[]>> checkEnumMap = new HashMap<>(16);

        public Builder list(List<Map<String, Object>> list) {
            this.list = list;
            return this;
        }

        /**
         * 校验数据库字段
         *
         * @param name     字段名
         * @param operator 比较方法
         * @param expect   预期值
         * @param <T>      T
         * @return builder
         */
        @SafeVarargs
        public final <T> Builder check(String name, EnumOperator operator, T... expect) {
            if (!checkEnumMap.containsKey(name)) {
                checkEnumMap.put(name, new HashMap<>(16));
            }
            checkEnumMap.get(name).put(operator, expect);
            return this;
        }

        @Override
        public DbChecker build() {
            if (list == null) {
                throw new CheckExcept("数据库为空");
            }
            if (checkEnumMap.isEmpty()) {
                throw new CheckExcept("校验值为空");
            }
            return new DbChecker(this);
        }
    }
}
