package com.haisheng.framework.testng.bigScreen.crm.wm.base.sql;

public interface IFromStep {

    <T> IWhereStep where(String field, String compareTo, T value);

    SqlPlus end();
}
