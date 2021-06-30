package com.haisheng.framework.testng.bigScreen.itemPorsche.base.sql;

public interface IInsertStep {

    IInsertStep set(String key, Object value);

    Sql end();
}
