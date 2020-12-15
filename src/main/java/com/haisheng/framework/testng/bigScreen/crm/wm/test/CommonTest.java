package com.haisheng.framework.testng.bigScreen.crm.wm.test;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.Activity;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.TPorscheDeliverInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.TPorscheReceptionData;
import com.haisheng.framework.testng.bigScreen.crm.wm.sql.Sql;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.AddressUtil;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

public class CommonTest {
    @Test
    public void testSelectA() {
        String sql = "select * from activity where id=19";
        List<Activity> list = new Factory.Builder().container(EnumContainer.BUSINESS_PORSCHE.getContainer()).build().create(sql, Activity.class);
        System.err.println(list.get(0).getGmtCreate());
    }

    @Test()
    public void testInsert() {
        JSONObject object = new JSONObject();
        object.put("1", 1);
        object.put("2", "455");
        Sql sql = Sql.instance().insert()
                .from(TPorscheDeliverInfo.class)
                .field("a", "b", "b", "d", "f")
                .setValue(null, 1.99, "3", "4", object)
                .end();
        System.err.println(sql.getSql());
        new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
    }

    @Test
    public void testSelect() {
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        String sql = Sql.instance().select()
                .from(TPorscheDeliverInfo.class)
                .where("deliver_date", "=", date)
                .and("shop_id", "=", "22728")
                .end().getSql();
        List<TPorscheDeliverInfo> list = new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql, TPorscheDeliverInfo.class);
        System.err.println(list);
    }

    @Test
    public void testGetNativePlace() {
        System.err.println(AddressUtil.getNativePlace(340822));
    }


    @Test
    public void testHumpToLine() {
        String name = CommonUtil.humpToLine(TPorscheReceptionData.class.getSimpleName());
        System.out.println(name);
    }

    @Test
    public void testIsContainChinese() {
        boolean b = CommonUtil.isContainChinese(null);
        System.out.println(b);
    }
}
