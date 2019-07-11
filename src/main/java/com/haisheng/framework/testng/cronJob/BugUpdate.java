package com.haisheng.framework.testng.cronJob;

import com.haisheng.framework.dao.IBugDao;
import com.haisheng.framework.dao.IZentaoBugDao;
import com.haisheng.framework.model.bean.Bug;
import com.haisheng.framework.model.bean.ZentaoBug;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ZentaoBugStaticVar;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BugUpdate {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SqlSession sqlSession = null;


    @Test
    private void saveZentaoBugToDb() {


        IZentaoBugDao zentaoBugDao = sqlSession.getMapper(IZentaoBugDao.class);
        List<ZentaoBug> bugList = zentaoBugDao.getAllBugs();

        ConcurrentHashMap<Integer, Bug> appBugMap = statisticBug(bugList);

        saveAppBugToDb(appBugMap);

    }

    private ConcurrentHashMap<Integer, Bug> statisticBug(List<ZentaoBug> bugList) {
        ConcurrentHashMap<Integer, Bug> bugConcurrentHashMap = new ConcurrentHashMap<>();

        mapAllProd(bugConcurrentHashMap);

        for (ZentaoBug bugInfo : bugList) {
            int prod = bugInfo.getProduct();

            if (bugConcurrentHashMap.containsKey(prod)) {
                Bug bug = bugConcurrentHashMap.get(prod);

                //ALL TOTAL BUG NUM
                bug.setAllTotalNum(bug.getAllTotalNum()+1);

                //NOW, NO ONLINE BUG
                bug.setAllTestTotalNum(bug.getAllTestTotalNum()+1);

                //OPEN STATUS BUG NUM
                if (! bugInfo.getStatus().trim().toLowerCase().equals(ZentaoBugStaticVar.STATUS_CLOSE)) {
                    bug.setOpenTotalNum(bug.getOpenTotalNum()+1);
                }

                if (bugInfo.getSeverity() == ZentaoBugStaticVar.SEV_BLOCK) {
                    //BLOCK LEVEL BUG
                    bug.setAllBlockerNum(bug.getAllBlockerNum()+1);
                    bug.setAllTestBlockerNum(bug.getAllTestBlockerNum()+1);
                    if (! bugInfo.getStatus().trim().toLowerCase().equals(ZentaoBugStaticVar.STATUS_CLOSE)) {
                        bug.setOpenBlockerNum(bug.getOpenBlockerNum()+1);
                    }
                } else if (bugInfo.getSeverity() == ZentaoBugStaticVar.SEV_CRITICAL) {
                    //CRITICAL LEVEL BUG
                    bug.setAllCriticalNum(bug.getAllCriticalNum()+1);
                    bug.setAllTestCriticalNum(bug.getAllTestCriticalNum()+1);
                    if (! bugInfo.getStatus().trim().toLowerCase().equals(ZentaoBugStaticVar.STATUS_CLOSE)) {
                        bug.setOpenCriticalNum(bug.getOpenCriticalNum()+1);
                    }
                }
            }


        }

        return bugConcurrentHashMap;
    }

    private void mapAllProd(ConcurrentHashMap<Integer, Bug> bugConcurrentHashMap) {
        ZentaoBugStaticVar zentaoBug = new ZentaoBugStaticVar();
        DateTimeUtil dt = new DateTimeUtil();

        ConcurrentHashMap<Integer, Integer> prodAppMap = zentaoBug.getProdAppMap();
        for(Map.Entry<Integer, Integer> prodApp : prodAppMap.entrySet()) {
            Bug bug = new Bug();
            bug.setAppId(prodApp.getValue());
            bug.setYear(dt.getHistorYear(0));
            bug.setMonth(dt.getHistoryMonth(0));
            bug.setWeek(dt.getHistoryWeek(0));
            bug.setDay(dt.getHistoryDay(0));
            bugConcurrentHashMap.put(prodApp.getKey(), bug);
        }

        dt = null;
        prodAppMap = null;
        zentaoBug = null;

    }

    private void saveAppBugToDb(ConcurrentHashMap<Integer, Bug> appBugMap) {
        IBugDao bugDao = sqlSession.getMapper(IBugDao.class);

        for (Bug bug : appBugMap.values()) {
            bugDao.insert(bug);
        }
        sqlSession.commit();

    }



    @BeforeSuite
    public void initial() {
        logger.debug("initial");
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

    @AfterSuite
    public void clean() {
        logger.debug("clean");
        sqlSession.close();
    }

}
