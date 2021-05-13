package com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.property;

/**
 * 公共的基本属性接口
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
}
