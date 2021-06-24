package com.haisheng.framework.testng.bigScreen.crm.wm.base.sql;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql构建类
 *
 * @author wangmin
 * @date 2021/1/11 13:36
 */
public class SqlPlus {

    private static volatile SqlPlus.Builder instance = null;


    public static SqlPlus.Builder instance() {
        if (instance == null) {
            synchronized (SqlPlus.class) {
                if (instance == null) {
                    instance = new SqlPlus.Builder();
                }
            }
        }
        return instance;
    }

    private final Map<String, Object> map = new HashMap<>();
    private final StringBuilder grammar;
    private final String tableName;
    private final StringBuilder condition;

    public SqlPlus(@NotNull Builder builder) {
        this.condition = builder.condition;
        this.tableName = builder.tableName;
        this.grammar = builder.grammar;
        this.map.putAll(builder.map);
    }

    public String getSql() {
        String result = grammar + tableName + condition;
        clear();
        return result;
    }

    public static class Builder implements ISqlStep<SqlPlus>, ISelectSelect, IFromStep, IWhereStep, IOtherStep,
            IInserterStep, IUpdateStep {
        private final Map<String, Object> map = new HashMap<>();
        private final String blank = " ";
        private final StringBuilder condition = new StringBuilder();
        private final StringBuilder grammar = new StringBuilder();
        private String tableName;

        public SqlPlus build() {
            return new SqlPlus(this);
        }

        @Override
        public IFromStep from(String tableName) {
            this.tableName = "from" + blank + tableName;
            return this;
        }

        public <T> IFromStep from(@NotNull Class<T> clazz) {
            String tableName = clazz.getSimpleName();
            return from(humpToLine(tableName));
        }

        @Override
        public ISelectSelect select(String... fields) {
            this.grammar.append("select (");
            Arrays.stream(fields).forEach(field -> this.grammar.append(setSqlValue(field)).append(","));
            this.grammar.replace(grammar.length() - 1, grammar.length(), "");
            this.grammar.append(")").append(blank);
            return this;
        }

        @Override
        public IInserterStep insert(String tableName) {
            this.grammar.append("insert into").append(blank);
            this.tableName = tableName;
            return this;
        }

        @Override
        public IUpdateStep update(String tableName) {
            this.grammar.append("update").append(blank);
            this.tableName = tableName;
            return this;
        }

        @Override
        public ISelectSelect select() {
            this.grammar.append("select *").append(blank);
            return this;
        }

        @Override
        public IUpdateStep set(String field, String compareTo, Object value) {
            return null;
        }

        @Override
        public <T> IWhereStep where(String field, String compareTo, T value) {
            this.condition.append("where").append(blank).append(field).append(blank)
                    .append(compareTo).append(blank).append(setSqlValue(value)).append(blank);
            return this;
        }

        @Override
        public <T> IWhereStep and(String field, String compareTo, T value) {
            return null;
        }

        @Override
        public <T> IWhereStep or(String field, String compareTo, T value) {
            return null;
        }

        @Override
        public IOtherStep limit() {
            return null;
        }

        @Override
        public IOtherStep limit(Integer limit) {
            return null;
        }

        public SqlPlus end() {
            if (StringUtils.isEmpty(grammar)) {
                throw new RuntimeException("sql语句头为空");
            }
            if (StringUtils.isEmpty(tableName)) {
                throw new RuntimeException("tableName为空");
            }
//            if (grammar.toString().contains(ContainerConstants.INSERT)) {
//                if (StringUtils.isEmpty(fieldList)) {
//                    throw new RuntimeException("field为空");
//                }
//                if (StringUtils.isEmpty(valueList)) {
//                    throw new RuntimeException("value为空");
//                }
//                if (valueList.split(", ").length != fieldList.split(", ").length) {
//                    throw new RuntimeException("value个数与field个数不相同，请检查");
//                }
//            }
            return new SqlPlus(this);
        }

        @Override
        public IInserterStep set(String field, Object value) {
            map.put(field, value);
            return null;
        }


    }


    /**
     * 条件语句清空
     */
    private void clear() {
        this.condition.setLength(0);
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

    @NotNull
    private static String humpToLine(String str) {
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString().replaceFirst("_", "");
    }

}
