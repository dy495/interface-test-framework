package com.haisheng.framework.testng.bigScreen.crm.wm.container;

import com.haisheng.framework.testng.bigScreen.crm.wm.table.DbTable;

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
        log.info("sql is：{}", getPath());
        table = new DbTable.Builder().path(getPath()).statement(statement).build().getTable();
        return true;
    }
}
