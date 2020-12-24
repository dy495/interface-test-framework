package com.haisheng.framework.testng.bigScreen.crm.wm.util;

import com.haisheng.framework.dao.IPorscheDeliverDao;
import com.haisheng.framework.model.bean.PorscheDeliverInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

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

    public PorscheDeliverInfo[] ss() {
        SqlSession sqlSession = getSqlSession();
        IPorscheDeliverDao iPorscheDeliverDao = sqlSession.getMapper(IPorscheDeliverDao.class);
        List<PorscheDeliverInfo> porscheDeliverInfos = iPorscheDeliverDao.selectPorscheDeliver();
        List<PorscheDeliverInfo> newList = new LinkedList<>(porscheDeliverInfos);
        return newList.toArray(new PorscheDeliverInfo[porscheDeliverInfos.size()]);
    }

    @Test
    public void test() {
        PorscheDeliverInfo s = ss()[0];
        System.err.println(s);
    }
}
