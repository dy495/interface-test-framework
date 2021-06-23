package com.haisheng.framework.testng.bigScreen.crm.wm.base.sql;

public interface ISql<T> {

    ISelectSelect select();

    ISelectSelect select(String... fields);

    T end();
}