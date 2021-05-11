package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.container;

public interface IContainer {

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
}
