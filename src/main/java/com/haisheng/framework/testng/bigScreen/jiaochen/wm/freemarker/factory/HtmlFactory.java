package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.factory;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.SceneAttribute;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse.IParser;

/**
 * @author wangmin
 * @date 2021/3/10 10:33
 */
public class HtmlFactory {

    /**
     * 通过解析器获取属性
     *
     * @param htmlPath htmlPath
     * @param clazz    解析器所在类
     * @param <T>      T
     * @return 各自的属性
     */
    public <T> SceneAttribute[] getAttribute(String htmlPath, IParser<T> parser) {
        parser.setHtmlUrl(htmlPath);
        return (SceneAttribute[]) parser.getAttributes();
    }
}
