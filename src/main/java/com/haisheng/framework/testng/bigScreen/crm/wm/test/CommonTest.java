package com.haisheng.framework.testng.bigScreen.crm.wm.test;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.DbContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.sql.Sql;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class CommonTest {
    @Test
    public void testA() {
        String driverName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://rm-2zeg4an1kr1437xu6no.mysql.rds.aliyuncs.com/business-porsche";
        String username = "read_only";
        String password = "read_only";
        String sql = "select * from activity where id=19";
        DbContainer db = new DbContainer.Builder().driverName(driverName).jdbcUrl(url).password(password).username(username).path(sql).build();
        db.init();
        List<Map<String, Object>> list = db.getTable();
        System.err.println(list.get(0).get("id"));
    }

    @Test()
    public void testInsert() {
        JSONObject object = new JSONObject();
        object.put("1", 1);
        object.put("2", "455");
        String sql = Sql.instance().insert().from("t_porsche_deliver_car")
                .field("a", "b", "b", "d", "f")
                .value(null, 1.99, "3", "4", object)
                .end().getSql();
        System.err.println(sql);
    }

    @Test
    public void testSelect() {
        String sql = Sql.instance().select("deliver_time")
                .from("t_porsche_deliver_car")
                .where("deliver_time", "=", "2020-10-14")
                .and("customer_type", "=", "PERSON")
                .end().getSql();
        List<Map<String, Object>> result = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
        System.err.println(result.get(0));
    }
}
