package com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.*;
import java.util.*;

public class DbDataHeleper {
    static Connection conn = null;

    public static String driverClassName = "com.mysql.cj.jdbc.Driver";
    public static String url = "jdbc:mysql://rm-2zeg4an1kr1437xu6no.mysql.rds.aliyuncs.com/business-porsche";
    public static String username = "read_only";
    public static String password = "read_only";

    /**
     * 执行sql
     * @param jdbcUrl 数据库配置连接
     * @param sql     sql语句
     * @return
     */
    public static List<Map<String, String>> getDataList(String jdbcUrl, String sql) {
        List<Map<String, String>> paramList = new ArrayList<Map<String, String>>();
        Map<String, String> param = new HashMap<>();
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(driverClassName);
            // 打开链接
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            // 执行查询
            stmt = conn.createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery(sql);
            String columns[] = {"id", "shop_id", "article_title"};
            // 展开结果集数据库
            while (rs.next()) {
                Map<String, String> map = new LinkedHashMap<String, String>();
                for (int i = 0; i < columns.length; i++) {
                    String cellData = rs.getString(columns[i]);
                    map.put(columns[i], cellData);
                }
                paramList.add(map);
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            System.out.println("处理 JDBC 错误!");
        } catch (Exception e) {
            // 处理 Class.forName 错误
            System.out.println("处理 Class.forName 错误");
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return paramList;
    }

    @DataProvider
    public Object[][] dbDataMethod() {
        String sql = "SELECT * FROM `account`;";
        List<Map<String, String>> result = getDataList(url, sql);
        Object[][] files = new Object[result.size()][];
        for (int i = 0; i < result.size(); i++) {
            files[i] = new Object[]{result.get(i)};
        }
        return files;
    }
    @Test
    public void sqltest(){
        String sql="SELECT * from `article`where article_title=\"bb\";";
        List<Map<String, String>> result = getDataList(url, sql);
        Object[][] files = new Object[result.size()][];
        for (int i = 0; i < result.size(); i++) {
            files[i] = new Object[]{result.get(i)};
        }
        for(Object s:result){
            System.out.println(s);
        }

    }
}
