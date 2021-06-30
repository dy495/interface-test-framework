package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.tarot.property;

/**
 * 公共的基本属性接口
 *
 * @author wngmin
 * @date 2021-05-11
 */
public interface IProperty {

    /**
     * 获取标识
     *
     * @return String 标识符 当BaseProperty存入name时，key=name，未存入时 key自增
     */
    String getKey();

    /**
     * 获取值
     *
     * @return Object
     */
    String getValue();

    /**
     * 设置值
     */
    void setValue(String value);
}
