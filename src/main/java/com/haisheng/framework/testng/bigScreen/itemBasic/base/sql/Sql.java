package com.haisheng.framework.testng.bigScreen.itemBasic.base.sql;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.util.ContainerConstants;
import com.haisheng.framework.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * sql构建类
 * 提供基础的sql构建
 *
 * @author wangmin
 * @date 2021/1/11 13:36
 */
public class Sql {

    private static volatile Sql.Builder instance = null;

    //双重🔒
    public static Sql.Builder instance() {
        if (instance == null) {
            synchronized (Sql.class) {
                if (instance == null) {
                    instance = new Sql.Builder();
                }
            }
        }
        return instance;
    }

    private final StringBuilder grammar;
    private final String tableName;
    private final StringBuilder condition;

    public Sql(@NotNull Builder builder) {
        this.condition = builder.condition;
        this.tableName = builder.tableName;
        this.grammar = builder.grammar;
    }

    public String getSql() {
        String result = grammar + tableName + condition;
        clear();
        return result;
    }

    public static class Builder implements ISqlControl {
        private final String blank = " ";
        private final Map<String, Object> map = new HashMap<>();
        private final StringBuilder condition = new StringBuilder();
        private final StringBuilder grammar = new StringBuilder();
        private String tableName;

        private void initCondition() {
            StringBuilder keySb = new StringBuilder();
            StringBuilder valueSb = new StringBuilder();
            map.forEach((key, value) -> keySb.append(key).append(blank).append(",").append(blank));
            map.forEach((key, value) -> valueSb.append(setSqlValue(value)).append(blank).append(",").append(blank));
            this.condition.append("(").append(blank).append(keySb.toString(), 0, keySb.length() - 2)
                    .append(")").append(blank).append("value").append(blank).append("(").append(blank)
                    .append(valueSb.toString(), 0, valueSb.length() - 2)
                    .append(")");
        }

        @Override
        public ISelectStep select(String... fields) {
            if (fields.length == 0) {
                this.grammar.append("select *").append(blank);
            } else {
                this.grammar.append("select ");
                Arrays.stream(fields).forEach(field -> this.grammar.append(field).append(","));
                this.grammar.replace(grammar.length() - 1, grammar.length(), "");
                this.grammar.append(blank);
            }
            return this;
        }

        @Override
        public IFromStep from(String tableName) {
            this.tableName = "from" + blank + tableName + blank;
            return this;
        }

        public <T> IFromStep from(@NotNull Class<T> clazz) {
            String tableName = clazz.getSimpleName();
            return from(humpToLine(tableName));
        }

        @Override
        public IInsertStep insert(String tableName) {
            this.grammar.append("insert into").append(blank);
            this.tableName = tableName;
            return this;
        }

        @Override
        public <T> IInsertStep insert(@NotNull Class<T> clazz) {
            String tableName = clazz.getSimpleName();
            this.tableName = humpToLine(tableName);
            return this;
        }

        @Override
        public IUpdateStep update(String tableName) {
            this.grammar.append("update").append(blank);
            this.tableName = tableName;
            return this;
        }

        @Override
        public IUpdateStep set(String field, String compareTo, Object value) {
            return this;
        }

        @Override
        public IInsertStep set(String key, Object value) {
            Preconditions.checkNotNull(key, "key 不可为空");
            map.put(key, value);
            return this;
        }

        @Override
        public <T> IWhereStep where(String field, String compareTo, T value) {
            Preconditions.checkArgument(!StringUtils.isEmpty(field), "where语句字段不可为空");
            this.condition.append("where").append(blank).append(field).append(blank)
                    .append(compareTo).append(blank).append(setSqlValue(value)).append(blank);
            return this;
        }

        @Override
        public <T> IWhereStep and(String field, String compareTo, T value) {
            Preconditions.checkArgument(!StringUtils.isEmpty(field), "and语句字段不可为空");
            this.condition.append("and").append(blank).append(field).append(blank)
                    .append(compareTo).append(blank).append(setSqlValue(value)).append(blank);
            return this;
        }

        @Override
        public <T> IWhereStep or(String field, String compareTo, T value) {
            Preconditions.checkArgument(!StringUtils.isEmpty(field), "or语句字段不可为空");
            this.condition.append("or").append(blank).append(field).append(blank)
                    .append(compareTo).append(blank).append(setSqlValue(value)).append(blank);
            return this;
        }

        @Override
        public IOtherStep limit() {
            return limit(200);
        }

        @Override
        public IOtherStep limit(Integer limit) {
            this.condition.append("limit").append(blank).append("(").append(blank).append(limit).append(blank).append(")");
            return this;
        }

        public Sql end() {
            Preconditions.checkNotNull(grammar, "sql语句头为空");
            Preconditions.checkNotNull(tableName, "tableName为空");
            if (grammar.toString().contains(ContainerConstants.INSERT)) {
                initCondition();
            }
            Preconditions.checkNotNull(condition, "条件语句为空");
            return new Sql(this);
        }
    }

    /**
     * 条件语句清空
     */
    private void clear() {
        this.condition.setLength(0);
        this.grammar.setLength(0);
    }

    /**
     * 值处理
     *
     * @param value value
     * @param <T>   object
     * @return object
     */
    private static <T> Object setSqlValue(T value) {
        return value == null ? null : value instanceof Integer ?
                value : "'" + value.toString().replaceAll("'", "\\\\'") + "'";
    }

    /**
     * 驼峰转下划线
     *
     * @param str str
     * @return result
     */
    @NotNull
    private static String humpToLine(String str) {
        return CommonUtil.humpToLine(str).replaceFirst("_", "");
    }

}
