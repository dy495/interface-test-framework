package com.haisheng.framework.testng.bigScreen.itemPorsche.base.sql;

public interface IUpdateStep {

    IUpdateStep set(String field, String compareTo, Object value);

    <T> IWhereStep where(String field, String compareTo, T value);

    Sql end();
}
