package com.haisheng.framework.testng.bigScreen.crm.wm.base.table;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.container.ContainerConstants;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.property.BasicProperty;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.DingPushUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DbTable extends BaseTable {
    private static final Logger logger = LoggerFactory.getLogger(DbTable.class);
    private static ResultSet resultSet;
    private final Statement statement;
    private final String path;
    private final String tableName;

    public DbTable(Builder builder) {
        super();
        this.statement = builder.statement;
        this.path = builder.path;
        this.tableName = builder.tableName;
    }

    @Override
    public List<Map<String, Object>> getTable() {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            if (load() && resultSet != null) {
                ResultSetMetaData md = resultSet.getMetaData();
                int count = md.getColumnCount();
                while (resultSet.next()) {
                    Map<String, Object> map = new HashMap<>();
                    for (int i = 1; i <= count; i++) {
                        String columnName = md.getColumnName(i);
                        Object rsValue = resultSet.getObject(i);
                        map.put(columnName, rsValue);
                    }
                    list.add(map);
                }
            }
        } catch (Exception e) {
            exceptionCollect(e);
        }
        return list;
    }

    @Override
    public <T> List<T> getTable(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            if (load() && resultSet != null) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                while (resultSet.next()) {
                    T t = clazz.getConstructor().newInstance();
                    for (int i = 0; i < metaData.getColumnCount(); i++) {
                        String fName = metaData.getColumnLabel(i + 1);
                        Object fValue = resultSet.getObject(fName);
                        String[] s = fName.split("_");
                        StringBuilder sb = new StringBuilder();
                        sb.append("set");
                        for (String str : s) {
                            sb.append(str.replaceFirst(str.charAt(0) + "", (str.charAt(0) + "").toUpperCase()));
                        }
                        if (fValue != null) {
                            Method setMethod = clazz.getMethod(sb.toString(), fValue.getClass());
                            setMethod.invoke(t, fValue);
                        }
                    }
                    list.add(t);
                }
            }
        } catch (Exception e) {
            exceptionCollect(e);
        }
        return list;
    }

    @Override
    public boolean load() {
        String sql = !StringUtils.isEmpty(path) ? getPath() : String.format(ContainerConstants.DB_TABLE_DEFAULT_SQL, tableName);
        if (statement != null) {
            try {
                if (sql.contains(ContainerConstants.UPDATE) || sql.contains(ContainerConstants.DELETE)) {
                    statement.executeUpdate(sql);
                    return true;
                }
                if (sql.contains(ContainerConstants.INSERT)) {
                    statement.execute(sql);
                    return true;
                }
                resultSet = statement.executeQuery(sql);
                return true;
            } catch (Exception e) {
                exceptionCollect(e);
            }
        }
        return false;
    }

    @Override
    public void sendDing() {
        DingPushUtil.sendText(errorMsg.toString(), getPath(), getMethodName());
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BasicProperty.Builder {
        private String path;
        private String tableName;
        private Statement statement;

        public DbTable build() {
            return new DbTable(this);
        }
    }
}
