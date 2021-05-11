package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.row;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.field.IField;

public interface IRow {

    IRow init();

    /**
     * 增加一个字段
     *
     * @param field 字段
     * @return boolean
     */
    boolean addField(IField field);

    /**
     * 获取字段集
     *
     * @return 字段集
     */
    IField[] getFields();

    /**
     * 获取标识
     *
     * @return String 标识
     */
    String getKey();
}
