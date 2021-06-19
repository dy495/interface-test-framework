package com.haisheng.framework.testng.bigScreen.crm.wm.base.sql;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionManager;

import java.io.IOException;
import java.io.InputStream;

public class SqlFactory {
    private static String configPath;

    public SqlFactory(Builder builder) {
        configPath = builder.configPath;
    }

    private SqlSession sqlSession = null;

    public <T> T execute(Class<T> tClass) {
        try {
            InputStream inputStream = Resources.getResourceAsStream(configPath);
            SqlSessionManager sqlSessionManager = SqlSessionManager.newInstance(inputStream);
            sqlSession = sqlSessionManager.openSession(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSession.getMapper(tClass);
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder {

        private String configPath = "configuration.xml";

        public SqlFactory build() {
            return new SqlFactory(this);
        }
    }
}
