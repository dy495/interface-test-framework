package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.table;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.row.IRow;

public interface ITable {

    boolean init();

    String getKey();

    boolean addRow(IRow row);

    IRow getRow(String key);

    IRow[] getRows();
}
