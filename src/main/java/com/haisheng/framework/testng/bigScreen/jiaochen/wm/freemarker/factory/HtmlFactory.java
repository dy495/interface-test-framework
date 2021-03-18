package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.factory;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse.BeanParser;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse.IParser;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse.SceneParser;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangmin
 * @date 2021/3/10 10:33
 */
public class HtmlFactory {

    /**
     * 通过解析器获取属性
     *
     * @param parser 解析器
     * @param <T>    T
     * @return 各自的属性
     */
    public <T> T[] getAttribute(@NotNull IParser<?> parser) {
        if (parser instanceof SceneParser || parser instanceof BeanParser) {
            return (T[]) parser.getAttributeList();
        } else {
            return null;
        }
    }
}
