package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse;

/**
 * 解析接口
 *
 * @author wangmin
 * @date 2021/3/15 13:40
 */
public interface IParser<T> {

    String getSuffix();

    T[] getAttributes();
}
