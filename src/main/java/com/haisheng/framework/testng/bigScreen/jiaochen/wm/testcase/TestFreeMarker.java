package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.haisheng.framework.dao.IAppointmentDataDao;
import com.haisheng.framework.model.bean.AppointmentData;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.sql.SqlFactory;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.attribute.SceneAttribute;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.SceneMarker;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse.BeanParser;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse.SceneParser;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * @author wangmin
 * @date 2021/3/15 9:59
 */
public class TestFreeMarker {

    @Test
    public void createScene() {
        String[] htmlPaths = {
                "http://192.168.50.3/api-doc/intelligent-control/app/index.html",
                "http://192.168.50.3/api-doc/intelligent-control/pc/index.html",
        };
        Arrays.stream(htmlPaths).forEach(htmlPath -> {
            SceneAttribute[] sceneAttributeList = new SceneParser.Builder().htmlUrl(htmlPath).build().getAttributes();
            Arrays.stream(sceneAttributeList).forEach(sceneAttribute -> new SceneMarker.Builder()
                    .templatePath("src\\main\\resources\\template")
                    .templateName("sceneTemplate.ftl")
                    .parentPath("src/main/java/com/haisheng/framework/testng/bigScreen/yuntong/wm/scene")
                    .sceneAttribute(sceneAttribute)
                    .buildMarker()
                    .execute());
        });
    }

    @Test
    public void createBean() {
        String[] htmlPaths = {
                "http://192.168.50.3/api-doc/business-jiaochen/applet/index.html",
                "http://192.168.50.3/api-doc/business-jiaochen/pc/index.html",
                "http://192.168.50.3/api-doc/business-jiaochen/app/index.html",
        };
        Arrays.stream(htmlPaths).forEach(htmlPath -> {
            SceneAttribute[] sceneAttributeList = new BeanParser.Builder().htmlUrl(htmlPath).build().getAttributes();
            Arrays.stream(sceneAttributeList).forEach(sceneAttribute -> new SceneMarker.Builder()
                    .templatePath("src\\main\\resources\\template")
                    .templateName("beanTemplate.ftl")
                    .parentPath("src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/b")
                    .sceneAttribute(sceneAttribute)
                    .buildMarker()
                    .execute());
        });
    }

    @Test
    public void createRisk() {
        String[] htmlPaths = {"http://192.168.50.3/api-doc/business-risk-platform/index.html#_7_1_%E7%89%B9%E6%AE%8A%E4%BA%BA%E5%91%98%E5%88%86%E9%A1%B5"};
        Arrays.stream(htmlPaths).forEach(htmlPath -> {
            SceneAttribute[] sceneAttributeList = new SceneParser.Builder().htmlUrl(htmlPath).build().getAttributes();
            Arrays.stream(sceneAttributeList).forEach(sceneAttribute -> new SceneMarker.Builder()
                    .templatePath("src\\main\\resources\\template")
                    .templateName("sceneTemplate.ftl")
                    .parentPath("src/main/java/com/haisheng/framework/testng/bigScreen/fengkongdaily/scene")
                    .sceneAttribute(sceneAttribute)
                    .buildMarker()
                    .execute());
        });
    }

    @Test
    public void testJooq() {
        DSLContext create = DSL.using("jdbc:mysql://rm-2zeg4an1kr1437xu6no.mysql.rds.aliyuncs.com/onepiece", "qa_wr", "qa_wr1234");
        create.selectQuery().addConditions();
        Result<Record> result = create.selectFrom("t_case").where("id=1").limit(100).fetch();
    }

    @Test
    public void testMybatis() {
        AppointmentData data = new AppointmentData();
        data.setAppointmentDate("2021-04-21");
        data.setAppointmentId(1L);
        data.setAppointmentType("保养");
        data.setProduct("轿辰");
        data.setShopId(2222L);
        data.setAppointmentStatus(1);
        new SqlFactory.Builder().build().execute(IAppointmentDataDao.class).insert(data);
    }

    @Test   //参数四个以内的接口
    public void createScene2() {
        String[] htmlPaths = {
                "http://192.168.50.3/api-doc-v2.3/business-jiaochen/app/index.html",
                "http://192.168.50.3/api-doc/business-jiaochen/applet/index.html",
                "http://192.168.50.3/api-doc/business-jiaochen/pc/index.html",
        };
        Arrays.stream(htmlPaths).forEach(htmlPath -> {
            SceneAttribute[] sceneAttributeList = new SceneParser.Builder().htmlUrl(htmlPath).build().getAttributes();
            Arrays.stream(sceneAttributeList).forEach(sceneAttribute -> new SceneMarker.Builder()
                    .templatePath("src\\main\\resources\\template")
                    .templateName("sceneTemplate2.ftl")
                    .templateFile("src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\jiaochen\\xmf\\file\\JcMethod")
                    .ExitFile("src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\jiaochen\\ScenarioUtil.java")
                    .parentPath("src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf")
                    .sceneAttribute(sceneAttribute)
                    .buildMarker()
                    .execute2());
        });
    }

}
