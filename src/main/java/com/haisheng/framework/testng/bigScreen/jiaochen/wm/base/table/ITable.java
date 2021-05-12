package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.table;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.property.IProperty;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.row.IRow;

public interface ITable extends IProperty {

    boolean load();

    boolean addRow(IRow row);

    /**
     * 获取指定的行
     *
     * @param key 行的key，大小写不敏感
     * @return IRow
     */
    IRow getRow(String key);

    /**
     * 返回所有的行数据
     *
     * @return IRow[] 所有行数据，有顺序
     */
    IRow[] getRows();
}
