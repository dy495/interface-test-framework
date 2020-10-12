package com.haisheng.framework.testng.bigScreen.crm.wm.container;

import java.util.List;
import java.util.Map;

public interface IContainer {

    boolean init();

    String getPath();

    List<Map<String, Object>> getTable();

    void setPath(String path);
}
