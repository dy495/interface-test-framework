package com.haisheng.framework.util;

import com.haisheng.framework.dao.ICaseDao;
import com.haisheng.framework.dao.IPvUvDao;
import com.haisheng.framework.model.bean.Case;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class QADbUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SqlSession sqlSession = null;

    public void openConnection() {
        logger.debug("open db connection");
        SqlSessionFactory sessionFactory = null;
        String resource = "configuration.xml";
        try {
            sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(
                    resource));
            sqlSession = sessionFactory.openSession();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void closeConnection() {
        logger.debug("close db connection");
        sqlSession.close();
    }

    public void saveToCaseTable(Case aCase) {
        ICaseDao caseDao = sqlSession.getMapper(ICaseDao.class);
        caseDao.insert(aCase);
        sqlSession.commit();
    }
}
