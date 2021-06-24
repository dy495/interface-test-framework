package com.haisheng.framework.testng.bigScreen.crm.wm.base.sql;

public interface IInsertStep {

    IInsertStep set(String key, Object value);

    Sql end();
}
