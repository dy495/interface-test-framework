package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.sql;

public interface IFromStep {

    <T> IWhereStep where(String field, String compareTo, T value);

    Sql end();
}
