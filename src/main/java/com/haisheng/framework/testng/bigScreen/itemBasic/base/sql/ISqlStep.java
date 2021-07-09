package com.haisheng.framework.testng.bigScreen.itemBasic.base.sql;

import org.jetbrains.annotations.NotNull;

public interface ISqlStep {

    ISelectStep select(String... fields);

    IInsertStep insert(String tableName);

    <T> IInsertStep insert(@NotNull Class<T> clazz);

    IUpdateStep update(String tableName);
}