package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.factory;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse.IParser;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author wangmin
 * @date 2021/3/10 10:33
 */
public class HtmlFactory {
    public static final Logger logger = LoggerFactory.getLogger(HtmlFactory.class);

    public <T> List<T> getObjectAttribute(String htmlUrl, @NotNull IParser parse) {
        parse.setHtmlUrl(htmlUrl);
        return (List<T>) parse.getSceneAttributeList();
    }
}
