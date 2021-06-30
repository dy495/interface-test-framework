package com.haisheng.framework.testng.bigScreen.itemPorsche.base.sql;

import org.jetbrains.annotations.NotNull;

public interface ISqlControl extends ISqlStep, ISelectStep, IFromStep, IWhereStep, IOtherStep, IInsertStep, IUpdateStep {

    @Override
    ISelectStep select();

    @Override
    ISelectStep select(String... fields);

    @Override
    IInsertStep insert(String tableName);

    @Override
    <T> IInsertStep insert(@NotNull Class<T> clazz);

    @Override
    IUpdateStep update(String tableName);

    @Override
    IFromStep from(String tableName);

    @Override
    <T> IFromStep from(@NotNull Class<T> clazz);

    @Override
    <T> IWhereStep where(String field, String compareTo, T value);

    @Override
    <T> IWhereStep and(String field, String compareTo, T value);

    @Override
    <T> IWhereStep or(String field, String compareTo, T value);

    @Override
    IUpdateStep set(String field, String compareTo, Object value);

    @Override
    IInsertStep set(String key, Object value);

    @Override
    IOtherStep limit();

    @Override
    IOtherStep limit(Integer limit);
}
