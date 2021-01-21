package com.haisheng.framework.testng.bigScreen.crm.wm.base.container;

import java.util.List;
import java.util.Map;

public interface IContainer {

    boolean init();

    String getPath();

    List<Map<String, Object>> getTable();

    <T> List<T> getTable(Class<T> clazz);

    void setPath(String path);
}
