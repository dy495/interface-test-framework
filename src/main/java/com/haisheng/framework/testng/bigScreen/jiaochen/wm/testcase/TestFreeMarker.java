package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.SceneMarker;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.util.HtmlUtil;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author wangmin
 * @date 2021/3/15 9:59
 */
public class TestFreeMarker {

    @Test
    public void testFreeMaker() {
        String[] htmlPath = {"http://192.168.50.3/api-doc/business-jiaochen/pc/index.html"};
        Arrays.stream(htmlPath).forEach(html -> Objects.requireNonNull(HtmlUtil.parseHtml(html)).forEach(e -> new SceneMarker.Builder()
                .templatePath("src\\main\\resources\\template")
                .templateName("sceneTemplate.ftl")
                .parentPath("src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/sense")
                .sceneAttribute(e)
                .buildMarker()
                .execute()));
    }
}
