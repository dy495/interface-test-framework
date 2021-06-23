package com.haisheng.framework.testng.bigScreen.crm.wm.base.sql;

import org.jetbrains.annotations.NotNull;

public interface ISelectSelect {

    IFromStep from(String tableName);

    <T> IFromStep from(@NotNull Class<T> clazz);

    SqlPlus end();
}