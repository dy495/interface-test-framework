package com.haisheng.framework.testng.bigScreen.crm.wm.table;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.ContainerConstants;
import com.haisheng.framework.testng.bigScreen.crm.wm.property.BasicProperty;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.DingPushUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.*;

@Getter
public class DbTable extends BasicProperty implements ITable {
    private static final Logger logger = LoggerFactory.getLogger(DbTable.class);
    private final Statement statement;
    private final String path;
    private final String tableName;

    public DbTable(Builder builder) {
        super(builder);
        this.statement = builder.statement;
        this.path = builder.path;
        this.tableName = builder.tableName;
    }

    @Override
    public List<Map<String, Object>> getTable() {
        if (statement != null) {
            String sql = !StringUtils.isEmpty(path) ? getPath() : String.format(ContainerConstants.DB_TABLE_DEFAULT_SQL, tableName);
            try {
                if (sql.contains(ContainerConstants.UPDATE) || sql.contains(ContainerConstants.DELETE)) {
                    statement.executeUpdate(sql);
                    return data(sql);
                }
                if (sql.contains(ContainerConstants.INSERT)) {
                    statement.execute(sql);
                    return data(sql);
                }
                ResultSet rs = statement.executeQuery(sql);
                ResultSetMetaData md = rs.getMetaData();
                int count = md.getColumnCount();
                List<Map<String, Object>> list = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    for (int i = 1; i <= count; i++) {
                        String columnName = md.getColumnName(i);
                        Object rsValue = rs.getObject(i);
                        map.put(columnName, rsValue);
                    }
                    list.add(map);
                }
                return list;
            } catch (Exception e) {
                e.printStackTrace();
                errorMsg.append(e.getMessage());
            } finally {
                if (errorMsg.length() > 0) {
                    DingPushUtil.sendText(errorMsg.toString(), sql);
                }
            }
        }
        return null;
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

    private List<Map<String, Object>> data(String sql) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sql", sql);
        list.add(map);
        return list;
    }
}
