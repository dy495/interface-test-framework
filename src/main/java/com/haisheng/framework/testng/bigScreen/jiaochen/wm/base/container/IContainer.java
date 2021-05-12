package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.container;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.property.IProperty;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.table.ITable;

public interface IContainer extends IProperty {

    /**
     * 初始化
     */
    boolean init();

    /**
     * 获取文件路径或者sql
     *
     * @return String 路径
     */
    String getPath();

    /**
     * 添加一个表
     *
     * @param table 表
     * @return boolean
     */
    boolean addTable(ITable table);

    /**
     * 获取所有表
     *
     * @return tables
     */
    ITable[] getTables();
}
