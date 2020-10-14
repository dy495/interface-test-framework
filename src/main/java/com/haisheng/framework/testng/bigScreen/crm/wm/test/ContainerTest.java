package com.haisheng.framework.testng.bigScreen.crm.wm.test;

import com.haisheng.framework.testng.bigScreen.crm.wm.container.DbContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.Factory;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class ContainerTest {
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

    @Test
    public void testB() {
        String sql = "alter table 表名 ADD 字段 类型 NOT NULL Default 0";
        List<Map<String, Object>> list = new Factory.Builder().container(EnumContainer.BUSINESS_PORSCHE.getContainer()).build().create(sql);
        System.err.println(list.get(0).get("id"));
    }

    @Test
    public void testC() throws Exception {
        CommonUtil.uploadShopCarPlate("京AMD238", 3);
    }
}
