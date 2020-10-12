package com.haisheng.framework.testng.bigScreen.crm.wm.table;

import java.util.List;
import java.util.Map;

public interface ITable {

    List<Map<String, Object>> getTable();

    String getPath();
}
