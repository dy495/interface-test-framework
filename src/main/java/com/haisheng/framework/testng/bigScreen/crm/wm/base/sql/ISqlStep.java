package com.haisheng.framework.testng.bigScreen.crm.wm.base.sql;

public interface ISqlStep<T> {

    ISelectStep select();

    ISelectStep select(String... fields);

    IInsertStep insert(String tableName);

    IUpdateStep update(String tableName);

    T end();
}