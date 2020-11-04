package com.haisheng.framework.testng.bigScreen.crm.wm.sql;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.ContainerConstants;
import com.haisheng.framework.testng.bigScreen.crm.wm.exception.SqlCreateException;

import java.util.Arrays;

/**
 * sql类
 *
 * @author wangmin
 */
public class Sql {

    private static volatile Sql.Builder instance = null;

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

    /**
     * 语法头
     */
    private final String grammar;

    /**
     * 表名
     */
    private final String tableName;

    /**
     * 条件语句
     */
    private final StringBuilder condition;

    public Sql(Builder builder) {
        this.condition = builder.condition;
        this.tableName = builder.tableName;
        this.grammar = builder.grammar;
    }

    public String getSql() {
        String result = grammar + tableName + condition;
        clear(condition);
        return result;
    }

    public static class Builder {
        private final String blank = " ";
        private final StringBuilder condition = new StringBuilder();
        private String valueList;
        private String fieldList;
        private String tableName;
        private String grammar;

        @SafeVarargs
        public final <T> Builder field(T... field) {
            StringBuilder fieldList = new StringBuilder();
            Arrays.stream(field).forEach(key -> fieldList.append(key).append(",").append(blank));
            this.fieldList = fieldList.toString();
            this.condition.append("(").append(blank)
                    .append(fieldList.toString(), 0, fieldList.length() - 2)
                    .append(blank).append(")").append(blank);
            return this;
        }

        @SafeVarargs
        public final <T> Builder value(T... value) {
            StringBuilder valueList = new StringBuilder();
            Arrays.stream(value).forEach(val -> valueList.append(setSqlValue(val)).append(",").append(blank));
            this.valueList = valueList.toString();
            this.condition.append("value").append(blank).append("(").append(blank)
                    .append(valueList.toString(), 0, valueList.length() - 2)
                    .append(blank).append(")");
            return this;
        }

        public Builder insert() {
            this.grammar = "insert into ";
            return this;
        }

        public Builder select() {
            this.grammar = "select * ";
            return this;
        }

        public Builder select(String... f) {
            StringBuilder fList = new StringBuilder();
            fList.append("select").append(blank);
            Arrays.stream(f).forEach(key -> fList.append(key).append(",").append(blank));
            this.grammar = fList.toString().substring(0, fList.length() - 2) + blank;
            return this;
        }

        public Builder from(String tableName) {
            if (grammar.contains(ContainerConstants.INSERT)) {
                this.tableName = tableName + blank;
            }
            if (grammar.contains(ContainerConstants.SELECT)) {
                this.tableName = "from" + blank + tableName + blank;
            }
            return this;
        }

        public Builder where(String condition) {
            this.condition.append("where").append(blank).append(condition).append(blank);
            return this;
        }

        public <T> Builder where(String field, String compareTo, T value) {
            this.condition.append("where").append(blank).append(field).append(blank)
                    .append(compareTo).append(blank).append(setSqlValue(value)).append(blank);
            return this;
        }

        public Builder and(String condition) {
            this.condition.append("and").append(blank).append(condition).append(blank);
            return this;
        }

        public <T> Builder and(String field, String compareTo, T value) {
            this.condition.append("and").append(blank).append(field).append(blank)
                    .append(compareTo).append(blank).append(setSqlValue(value)).append(blank);
            return this;
        }

        public Sql end() {
            if (StringUtils.isEmpty(grammar)) {
                throw new SqlCreateException("sql语句头为空");
            }
            if (StringUtils.isEmpty(tableName)) {
                throw new SqlCreateException("tableName为空");
            }
            if (grammar.contains(ContainerConstants.INSERT)) {
                if (StringUtils.isEmpty(fieldList)) {
                    throw new SqlCreateException("field为空");
                }
                if (StringUtils.isEmpty(valueList)) {
                    throw new SqlCreateException("value为空");
                }
                if (valueList.split(", ").length != fieldList.split(", ").length) {
                    throw new SqlCreateException("value个数与field个数不相同，请检查");
                }
            }
            return new Sql(this);
        }
    }

    /**
     * 条件语句清空
     *
     * @param condition 条件语句
     */
    private static void clear(StringBuilder condition) {
        condition.setLength(0);
    }

    /**
     * 值处理
     *
     * @param value value
     * @param <T>   object
     * @return object
     */
    private static <T> Object setSqlValue(T value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return value;
        } else {
            return "'" + value.toString().replaceAll("'", "\\\\'") + "'";
        }
    }
}