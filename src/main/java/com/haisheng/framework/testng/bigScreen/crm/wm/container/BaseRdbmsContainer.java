package com.haisheng.framework.testng.bigScreen.crm.wm.container;

import com.haisheng.framework.testng.bigScreen.crm.wm.table.DbTable;

import java.sql.Statement;
import java.util.List;

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
        log.info("sql is：{}", getPath());
        table = new DbTable.Builder().path(getPath()).statement(statement).build().getTable();
        return true;
    }

    @Override
    public <T> List<T> getTable(Class<T> clazz) {
        Statement statement = connect();
        log.info("sql is：{}", getPath());
        return new DbTable.Builder().path(getPath()).statement(statement).build().getTable(clazz);
    }
}
