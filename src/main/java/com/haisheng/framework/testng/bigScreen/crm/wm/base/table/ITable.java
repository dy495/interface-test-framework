package com.haisheng.framework.testng.bigScreen.crm.wm.base.table;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ITable {

    List<Map<String, Object>> getTable() throws SQLException;

    String getPath();

    <T> List<T> getTable(Class<T> clazz);

    boolean load();
}
