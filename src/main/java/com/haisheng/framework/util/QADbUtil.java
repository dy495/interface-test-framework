package com.haisheng.framework.util;

import com.haisheng.framework.dao.IBaiguoyuanMetricsDao;
import com.haisheng.framework.dao.IBaiguoyuanUserDao;
import com.haisheng.framework.dao.ICaseDao;
import com.haisheng.framework.dao.IShelfDao;
import com.haisheng.framework.model.bean.BaiguoyuanBindMetrics;
import com.haisheng.framework.model.bean.BaiguoyuanBindUser;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.model.bean.Shelf;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

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

        List<Integer> listId = caseDao.queryCaseByName(aCase.getApplicationId(),
                aCase.getConfigId(),
                aCase.getCaseName());
        if (listId.size() > 0) {
            aCase.setId(listId.get(0));
            //System.out.println("case already existed: " + aCase.getCaseName());
        }
        aCase.setEditTime(new Timestamp(System.currentTimeMillis()));

        caseDao.insert(aCase);
        sqlSession.commit();
    }

    public void saveShelfAccuracy(Shelf shelf) {
        IShelfDao shelfDao = sqlSession.getMapper(IShelfDao.class);

        shelfDao.insert(shelf);
        sqlSession.commit();

    }

    public List<Shelf> getShelfAccuracy(String date) {
        IShelfDao shelfDao = sqlSession.getMapper(IShelfDao.class);

        List<Shelf> accuracyList = shelfDao.query(date);

        return accuracyList;

    }

    public List<BaiguoyuanBindUser> getBaiguoyuanBindAccuracy(String date) {
        IBaiguoyuanUserDao baiguoyuanDao = sqlSession.getMapper(IBaiguoyuanUserDao.class);

        List<BaiguoyuanBindUser> userList = baiguoyuanDao.getUserList(date);

        return userList;

    }

    public void saveBaiguoyuanMetrics(BaiguoyuanBindMetrics bindMetrics) {
        IBaiguoyuanMetricsDao metricsDao = sqlSession.getMapper(IBaiguoyuanMetricsDao.class);

        metricsDao.insert(bindMetrics);
        sqlSession.commit();

    }
}
