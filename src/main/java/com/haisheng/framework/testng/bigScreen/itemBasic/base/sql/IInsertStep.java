package com.haisheng.framework.testng.bigScreen.itemBasic.base.sql;

public interface IInsertStep {

    IInsertStep set(String key, Object value);

    Sql end();
}
