package com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.entity;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.row.IRow;

/**
 * 实体接口
 *
 * @param <S> sql类
 * @param <F> 工厂类
 * @author wangmin
 * @date 2021-05-11
 */
public interface IEntity<S, F> {

    /**
     * 获取当前实体
     *
     * @return IRow
     */
    IRow getCurrent();

    /**
     * 刷新实体
     *
     * @return row
     */
    IRow refresh();

    /**
     * 获取实体生成器
     *
     * @return R
     */
    F getFactory();

    /**
     * 获取sql
     *
     * @return String
     */
    S getSql();

    /**
     * 获取字段值
     *
     * @param fieldName 字段名
     * @return 字段值
     */
    String getFieldValue(String fieldName);

    /**
     * 取值方法
     */
    long getLongField(String fieldName);

    int getIntField(String fieldName);

    float getFloatField(String fieldName);

    double getDoubleField(String fieldName);
}
