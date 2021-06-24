package com.haisheng.framework.testng.bigScreen.crm.wm.base.sql;

public interface ISqlStep<T> {

    ISelectSelect select();

    ISelectSelect select(String... fields);

    IInserterStep insert(String tableName);

    IUpdateStep update(String tableName);

    T end();
}