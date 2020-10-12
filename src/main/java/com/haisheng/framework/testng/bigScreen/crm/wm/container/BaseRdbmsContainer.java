package com.haisheng.framework.testng.bigScreen.crm.wm.container;

import com.haisheng.framework.testng.bigScreen.crm.wm.table.DbTable;

import java.sql.Statement;
import java.util.List;
import java.util.Map;

public abstract class BaseRdbmsContainer extends BaseContainer {
    private static List<Map<String, Object>> table;

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
        log.info("sql:{}", getPath());
        table = new DbTable.Builder().path(getPath()).statement(statement).build().getTable();
        return true;
    }

    @Override
    public List<Map<String, Object>> getTable() {
        return table;
    }
}
