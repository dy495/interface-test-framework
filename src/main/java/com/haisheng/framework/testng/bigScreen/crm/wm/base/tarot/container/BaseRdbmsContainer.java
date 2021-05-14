package com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.table.DbTable;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.util.ContainerConstants;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseRdbmsContainer extends BaseContainer {

    protected BaseRdbmsContainer(BaseBuilder<?, ?> builder) {
        super(builder);
    }

    abstract Statement connect();

    @Override
    public boolean init() {
        Statement statement = connect();
        if (statement == null) {
            return false;
        }
        try {
            logger.info("sql is：{}", getPath());
            if (getPath().contains(ContainerConstants.SELECT)) {
                ResultSet rs = statement.executeQuery(getPath());
                while (rs.next()) {
                    String tableName = rs.getString(1);
                    ITable table = new DbTable.Builder().path(getPath()).name(tableName).statement(statement).buildTable();
                    addTable(table);
                }
            } else {
                new DbTable.Builder().path(getPath()).statement(statement).buildTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
