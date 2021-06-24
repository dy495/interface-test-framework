package com.haisheng.framework.testng.bigScreen.crm.wm.base.sql;

public interface IUpdateStep {

    IUpdateStep set(String field, String compareTo, Object value);

    <T> IWhereStep where(String field, String compareTo, T value);

    SqlPlus end();
}
