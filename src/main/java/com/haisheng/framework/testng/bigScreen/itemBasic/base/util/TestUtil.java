package com.haisheng.framework.testng.bigScreen.itemBasic.base.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.haisheng.framework.dao.IAppointmentDataDao;
import com.haisheng.framework.model.bean.AppointmentData;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.sql.SqlFactory;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.marker.attribute.SceneAttribute;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.marker.marker.SceneMarker;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.marker.parse.SceneParser;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.container.ExcelContainer;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.container.IContainer;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.field.IField;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.table.CsvTable;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.enumerator.EnumContainer;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.entity.IEntity;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.util.FileUtil;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.testng.annotations.Test;

import java.io.*;
import java.util.*;

/**
 * @author wangmin
 * @date 2021/3/15 9:59
 */
public class TestUtil {

    @Test
    public void createScene() {
        String[] htmlPaths = {
                "http://192.168.50.3/api-doc/jiedai/app/"
        };
        Arrays.stream(htmlPaths).forEach(htmlPath -> {
            SceneAttribute[] sceneAttributeList = new SceneParser.Builder().htmlUrl(htmlPath).build().getAttributes();
            Arrays.stream(sceneAttributeList).forEach(sceneAttribute -> new SceneMarker.Builder()
                    .templatePath("src\\main\\resources\\template")
                    .templateName("sceneTemplate.ftl")
                    .parentPath("src/main/java/com/haisheng/framework/testng/bigScreen/itemYuntong/common/scene")
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
        System.err.println(result);
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

    @Test
    public void testReadDb() {
        String sql = "select * from t_case limit 10";
        IEntity<?, ?>[] entities = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql);
        System.err.println(entities.length);
        String caseName = entities[0].getFieldValue("case_name");
        System.err.println(caseName);
    }

    @Test
    public void testReadDb2() {
        String sql = "select * from t_case limit 10";
        List<B> bs = new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().toJavaObjectList(sql, B.class);
        String caseName = bs.get(0).getCaseName();
        System.err.println(caseName);
    }

    @Test
    public void testReadExcel() {
        String path = FileUtil.getResourcePath("/excel/数据.xlsx");
        IContainer container = new ExcelContainer.Builder().path(path).buildContainer();
        container.init();
        ITable table = container.getTables()[0];
        table.load();
        IRow[] rows = table.getRows();
        Arrays.stream(rows).map(row -> row.getField("卡券号")).forEach(row -> row.setValue(row.getValue() + "改动"));
        container.setTable(table);
        //修改后验证
        IContainer container1 = new ExcelContainer.Builder().path(path).buildContainer();
        container1.init();
        ITable table1 = container1.getTables()[0];
        table1.load();
        IRow[] rows1 = table1.getRows();
        Arrays.stream(rows1).forEach(e -> System.err.println(e.getField("卡券号")));
    }

    @Test
    public void testReadCsv() {
        String path = "/excel/卡券数据-新模板-所有去重数据-未使用状态（3家门店除外）0510.csv";
        IEntity<?, ?>[] entities = new Factory.Builder().build().createCsv(path);
        System.err.println(entities.length);
        String voucherName = entities[0].getFieldValue("卡券名称");
        System.err.println(voucherName);
    }

    @Data
    static
    class B implements Serializable {
        @JSONField(name = "case_name")
        private String caseName;
    }

    @Test
    public void sss() {
        String path = "/excel/规则文件.xlsx";
        String resourcePath = FileUtil.getResourcePath(path);
        IContainer container = new ExcelContainer.Builder().path(resourcePath).build();
        container.init();
        ITable table = container.getTables()[0];
        table.load();
        IRow[] rows = table.getRows();
        Arrays.stream(rows).forEach(e -> System.err.println(Arrays.toString(e.getFieldsValue())));
        IRow row = rows[0];
        IField count = row.getField("COUNT");
        IField index = row.getField("INDEX");
        index.setValue(String.valueOf(Integer.parseInt(count.getValue()) + Integer.parseInt(index.getValue())));
        container.setTable(table);
        IEntity<?, ?> entity = new Factory.Builder().container(EnumContainer.EXCEL.getContainer()).build().createExcel(path)[0];
        String newIndex = entity.getFieldValue("INDEX");
        System.err.println(newIndex);
    }

    @Test
    public void ss() {
        String path = FileUtil.getResourcePath("csv/dcbbde6f-2002-43fe-9857-3ef41da24c4b.csv");
        ITable table = new CsvTable.Builder().path(path).build();
        table.load();
        IRow[] rows = table.getRows();
        Map<String, JSONObject> map = new HashMap<>();
        Arrays.stream(rows).forEach(row -> JSONArray.parseArray(row.getField("region").getValue()).stream()
                .map(e -> (JSONObject) e).filter(e -> rule(e, "region_id", "55503", "status", "PASS"))
                .forEach(e -> map.put(row.getField("user_id").getValue(), e)));
        long count = map.entrySet().stream().filter(e -> !e.getKey().equals("N")).count();
        System.err.println(count);
    }

    public boolean rule(@NotNull JSONObject jsonObject, String key1, String value1, String key2, String value2) {
        return jsonObject.getString(key1).equals(value1) && jsonObject.getString(key2).equals(value2);
    }
}
