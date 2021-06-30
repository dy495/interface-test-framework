package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.sql;

import org.jetbrains.annotations.NotNull;

public interface ISelectStep {

    IFromStep from(String tableName);

    <T> IFromStep from(@NotNull Class<T> clazz);

    Sql end();
}