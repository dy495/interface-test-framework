package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker;

/**
 * Maker接口
 *
 * @author wangmin
 * @date 2021/3/8 18:42
 */
public interface IMarker {

    /**
     * 执行创建
     */
    void execute();

    /**
     * 放入数据结构
     *
     * @param structure 数据结构
     */
    void setStructure(Structure structure);

    /**
     * 获取数据结构
     *
     * @return 数据结构
     */
    Structure getStructure();

}
