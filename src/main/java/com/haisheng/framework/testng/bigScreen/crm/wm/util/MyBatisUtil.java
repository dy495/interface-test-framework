package com.haisheng.framework.testng.bigScreen.crm.wm.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtil {
    static SqlSessionFactory sqlSessionFactory = null;

    static {
        try {
            //加载mybatis配置文件，并创建SqlSessionFactory实例
            String resource = "configuration.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            //这个build方法可以接受几种不同的参数，如Reader/InputSteam等
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }

    public static void closeSqlSession(SqlSession sqlSession) {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }
}
