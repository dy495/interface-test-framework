package com.haisheng.framework.testng.bigScreen.crm.wm.entity;

import com.haisheng.framework.testng.bigScreen.crm.wm.dao.TPorscheReceptionData;
import com.haisheng.framework.testng.bigScreen.crm.wm.sql.Sql;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

public class Entity {
    private final Map<String, IEntity> entity;
    Map<String, Object> map = new LinkedHashMap<>();

    public Entity(Builder builder) {
        this.entity = builder.entity;
    }


    public IEntity getEntity() {
        return (IEntity) this.map;
    }


    public void setEntity(Map<String, Object> map) {
        this.map.putAll(map);
    }


    public IEntity[] create() {
        List<IEntity> temp = new LinkedList<>();
        for (String key : entity.keySet()) {
            temp.add(entity.get(key));
        }
        final int size = temp.size();
        return temp.toArray(new IEntity[size]);
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder {
        private final Map<String, IEntity> entity = new LinkedHashMap<>(1024);

        public Entity build() {
            return new Entity(this);
        }
    }


    public static <T> List<T> dss(String sql, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            Connection connect;
            String driverName = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://rm-2zeg4an1kr1437xu6no.mysql.rds.aliyuncs.com/onepiece";
            String username = "qa_wr";
            String password = "qa_wr1234";
            Class.forName(driverName).newInstance();
            connect = DriverManager.getConnection(url, username, password);
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                T t = clazz.getConstructor().newInstance();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    String fName = metaData.getColumnLabel(i + 1);
                    Object fValue = resultSet.getObject(fName);
                    String[] s = fName.split("_");
                    StringBuilder sb = new StringBuilder();
                    sb.append("set");
                    for (String str : s) {
                        sb.append(str.replaceFirst(str.charAt(0) + "", (str.charAt(0) + "").toUpperCase()));
                    }
                    if (fValue != null) {
                        Method setMethod = clazz.getMethod(sb.toString(), fValue.getClass());
                        setMethod.invoke(t, fValue);
                    }
                }
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
//        String fName = "shop_id_oo";
//        String[] s = fName.split("_");
//        StringBuilder sb = new StringBuilder();
//        sb.append("set");
//        for (String str : s) {
//            sb.append(str.replaceFirst(str.charAt(0) + "", new String(str.charAt(0) + "").toUpperCase()));
//        }
//        sb.append("(").append(")");
//        System.out.println(sb.toString());
//        new TPorscheReceptionData().setId();
        String sql = Sql.instance().select()
                .from(TPorscheReceptionData.class)
                .where("reception_date", "=", "2020-11-10")
                .and("shop_id", "=", "22728")
                .end().getSql();
        System.out.println(sql);
        List<TPorscheReceptionData> list = dss(sql, TPorscheReceptionData.class);
        int customerId = list.get(0).getCustomerId();
        System.out.println(customerId);
    }
}
