package com.haisheng.framework.testng.bigScreen.itemPorsche.base.sql;

public interface IFromStep {

    <T> IWhereStep where(String field, String compareTo, T value);

    Sql end();
}
