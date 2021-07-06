package com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.table;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.util.ContainerConstants;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.field.IField;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.field.SimpleField;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.row.SimpleRow;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

@Getter
public class DbTable extends BaseTable {
    private final Statement statement;

    public DbTable(Builder builder) {
        super(builder);
        this.statement = builder.statement;
    }

    @Override
    public boolean load() {
        String sql = !StringUtils.isEmpty(getPath()) ? getPath() : String.format(ContainerConstants.DB_TABLE_DEFAULT_SQL, getKey());
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
                ResultSet rs = statement.executeQuery(sql);
                clear();
                ResultSetMetaData md = rs.getMetaData();
                int count = md.getColumnCount();
                int index = 1;
                while (rs.next()) {
                    IRow row = new SimpleRow.Builder().index(index++).build();
                    for (int i = 1; i <= count; i++) {
                        String columnName = md.getColumnName(i);
                        Object rsValue = rs.getObject(i);
                        String value = (rsValue == null) ? null : String.valueOf(rsValue);
                        IField field = new SimpleField.Builder().name(columnName).value(value).build();
                        row.addField(field);
                    }
                    addRow(row);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("数据库表加载错误：statement为空，数据库没有初始化");
        }
        return false;
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder<Builder, DbTable> {
        private Statement statement;

        @Override
        public DbTable buildTable() {
            return new DbTable(this);
        }
    }
}
