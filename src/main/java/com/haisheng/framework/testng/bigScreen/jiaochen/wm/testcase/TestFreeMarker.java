package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.SceneAttribute;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.SceneMarker;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse.SceneParser;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * @author wangmin
 * @date 2021/3/15 9:59
 */
public class TestFreeMarker {

    @Test
    public void createScene() {
        String htmlPath = "http://192.168.50.3/api-doc/business-jiaochen/applet/index.html";
        SceneAttribute[] sceneAttributeList = new SceneParser.Builder().htmlUrl(htmlPath).build().getAttributes();
        Arrays.stream(sceneAttributeList).forEach(sceneAttribute -> new SceneMarker.Builder()
                .templatePath("src\\main\\resources\\template")
                .templateName("sceneTemplate.ftl")
                .parentPath("src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/sense")
                .sceneAttribute(sceneAttribute)
                .buildMarker()
                .execute());
    }

//    @Test
//    public void testSql() {
//        DSLContext create = DSL.using("jdbc:mysql://rm-2zeg4an1kr1437xu6no.mysql.rds.aliyuncs.com/onepiece", "qa_wr", "qa_wr1234");
//        create.selectQuery().addConditions();
//        Result<Record> result = create.selectFrom("t_case").where("id=1").limit(100).fetch().;
//        Row row = result.fieldsRow();
//        System.err.println(field);
//    }
}
