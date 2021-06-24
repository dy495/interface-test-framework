package com.haisheng.framework.testng.bigScreen.crm.wm.base.sql;

public interface IInserterStep {

    IInserterStep set(String field, Object value);

    SqlPlus end();
}
