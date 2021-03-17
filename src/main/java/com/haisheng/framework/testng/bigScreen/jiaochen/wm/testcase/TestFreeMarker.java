package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.factory.HtmlFactory;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.SceneAttribute;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.SceneMarker;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse.BeanParser;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse.IParser;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse.SceneParser;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author wangmin
 * @date 2021/3/15 9:59
 */
public class TestFreeMarker {

    @Test
    public void createScene() {
        String htmlPath = "http://192.168.50.3/api-doc/business-jiaochen/pc/index.html";
        IParser parse = new SceneParser.Builder().build();
        List<SceneAttribute> sceneAttributeList = new HtmlFactory().getObjectAttribute(htmlPath, parse);
        sceneAttributeList.forEach(e -> new SceneMarker.Builder()
                .templatePath("src\\main\\resources\\template")
                .templateName("sceneTemplate.ftl")
                .parentPath("src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/s")
                .sceneAttribute(e)
                .buildMarker()
                .execute());
    }

    @Test
    public void createBean() {
        String htmlPath = "http://192.168.50.3/api-doc/business-jiaochen/pc/index.html";
        IParser parse = new BeanParser.Builder().build();
        List<SceneAttribute> sceneAttributeList = new HtmlFactory().getObjectAttribute(htmlPath, parse);
        sceneAttributeList.forEach(e -> new SceneMarker.Builder()
                .templatePath("src\\main\\resources\\template")
                .templateName("beanTemplate.ftl")
                .parentPath("src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/b")
                .sceneAttribute(e)
                .buildMarker()
                .execute());
    }
}
