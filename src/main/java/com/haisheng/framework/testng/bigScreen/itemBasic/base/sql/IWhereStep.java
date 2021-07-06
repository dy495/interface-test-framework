package com.haisheng.framework.testng.bigScreen.itemBasic.base.sql;

public interface IWhereStep {

    <T> IWhereStep and(String field, String compareTo, T value);

    <T> IWhereStep or(String field, String compareTo, T value);

    IOtherStep limit();

    IOtherStep limit(Integer limit);

    Sql end();
}
