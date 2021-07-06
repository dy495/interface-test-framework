package com.haisheng.framework.testng.bigScreen.itemBasic.base.sql;

public interface IFromStep {

    <T> IWhereStep where(String field, String compareTo, T value);

    Sql end();
}
