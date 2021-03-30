package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.testcase;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.enumerator.IpPortEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.SceneAttribute;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.SceneMarker;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse.SceneParser;
import org.testng.annotations.Test;

import java.util.Arrays;

public class TestFreeMarker {

    @Test
    public void createScene() {
        String[] htmlList = {"http://192.168.50.3/api-doc/business-patrol/wechat/"};
        Arrays.stream(htmlList).forEach(html -> {
            SceneAttribute[] sceneAttributes = new SceneParser.Builder().htmlUrl(html).buildParser().getAttributes();
            Arrays.stream(sceneAttributes).forEach(sceneAttribute -> new SceneMarker.Builder()
                    .templatePath("src\\main\\resources\\template")
                    .templateName("sceneTemplate.ftl")
                    .parentPath("src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/wm/scene/applet")
                    .sceneAttribute(sceneAttribute)
                    .buildMarker()
                    .execute());
        });
    }

    @Test
    public void test() {
        String path = "http://http:/dev.inspect.store.winsenseos.cn/patrol/check-list/detail";
        String str = path.replace(IpPortEnum.getContainAddress(path) + "/", "");
        System.err.println(str);
        String[] p = str.split("/");
        System.out.println(p.length);
        System.err.println(Arrays.toString(p));
    }
}
