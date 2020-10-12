package com.haisheng.framework.testng.bigScreen.crm.wm.container;

public class ContainerConstants {
    /**
     * INSERT标识，增加一个空格，防止以关键词命名的开头字段
     */
    public static final String INSERT = "insert into ";
    /**
     * DELETE标识，增加一个空格，防止以关键词命名的开头字段
     */
    public static final String DELETE = "delete ";

    /**
     * update标识，增加一个空格，防止以关键词命名的开头字段
     */
    public static final String UPDATE = "update ";

    /**
     * 数据库表默认sql
     */
    public static final String DB_TABLE_DEFAULT_SQL = "select * from %s limit 200;";
}
