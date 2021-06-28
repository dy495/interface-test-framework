package com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.table;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.config.OTSPrimaryKeyBuilder;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.property.IProperty;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.row.IRow;

public interface ITable extends IProperty {

    /**
     * 数据表加载
     *
     * @return boolean
     */
    boolean load();

    /**
     * 添加一行
     *
     * @param row 行
     * @return boolean
     */
    boolean addRow(IRow row);

    /**
     * 获取表的路径
     *
     * @return String 路径信息
     */
    String getPath();

    /**
     * 设置表的路径，由实现类赋予实际含义
     *
     * @param path 路径信息
     */
    void setPath(String path);

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

    /**
     * 清空表
     */
    void clear();

    /**
     * 设置主键，仅供ots使用
     *
     * @param otsPrimaryKeyBuilder 主键构造器
     */
    void setOTSPrimaryKeyBuilder(OTSPrimaryKeyBuilder otsPrimaryKeyBuilder);
}
