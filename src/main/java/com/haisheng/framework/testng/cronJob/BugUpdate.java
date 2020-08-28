package com.haisheng.framework.testng.cronJob;

import com.haisheng.framework.dao.IBugDao;
import com.haisheng.framework.dao.IZentaoBugDao;
import com.haisheng.framework.model.bean.Bug;
import com.haisheng.framework.model.bean.ZentaoBug;
import com.haisheng.framework.testng.commonDataStructure.Env;
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


/**
 * @author yu
 * @deprecated
 *
 * @update bug统计直接从数据表用sql过滤，无效代码再次统计，且此部分代码未完工, 2020.08.26
 *
 *
 * */
public class BugUpdate {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SqlSession sqlSession = null;
    private DateTimeUtil dt = new DateTimeUtil();


    @Test
    private void saveZentaoBugToDb() {


        IZentaoBugDao zentaoBugDao = sqlSession.getMapper(IZentaoBugDao.class);
        String today = dt.getHistoryDate(0); //debug -1, valid 0
        List<ZentaoBug> bugList = zentaoBugDao.getAllBugs(today);

        ConcurrentHashMap<Integer, Bug> appBugMap = statisticBug(bugList);

        saveAppBugToDb(appBugMap);

    }



    /**
     * ConcurrentHashMap<Integer(product in zentao), Bug>
     * List<ZentaoBug> 所有禅道bug列表，根据时间戳过滤，默认为今天禅道上所有bug
     * */
    private ConcurrentHashMap<Integer, Bug> statisticBug(List<ZentaoBug> bugList) {
        ConcurrentHashMap<Integer, Bug> bugStatistic = new ConcurrentHashMap<>();
        mapAllProd(bugStatistic);

        //遍历禅道bug列表
        for (ZentaoBug bugInfo : bugList) {

            //根据禅道product统计bug
            int prod = bugInfo.getProduct();
            if (bugStatistic.containsKey(prod)) {
                //统计每个product的bug
                Bug bug = bugStatistic.get(prod);

                if (bugInfo.getTitle().contains("线上")) {
                    //线上bug统计
                    bug.setAllOnlineTotalNum(bug.getAllOnlineTotalNum()+1);
                    //多维度统计：级别、打开关闭、bug类型
                    caculateBugSeverity(Env.ONLINE, bug, bugInfo);
                    caculateBugStatus(Env.ONLINE, bug, bugInfo);
                    caculateBugType(Env.ONLINE, bug, bugInfo);
                } else {
                    //日常bug统计
                    bug.setAllTestTotalNum(bug.getAllTestTotalNum()+1);
                    //多维度统计：级别、打开关闭、bug类型、团队归属
                    caculateBugSeverity(Env.DAILY, bug, bugInfo);
                    caculateBugStatus(Env.DAILY, bug, bugInfo);
                    caculateBugType(Env.DAILY, bug, bugInfo);
                }

            }


        }

        return bugStatistic;
    }

    /**
     * 根据bug等级统计
     * 区分线下/线下
     * */
    private void caculateBugSeverity(String env, Bug bug, ZentaoBug bugInfo) {

    }

    /**
     * 根据bug打开关闭状态统计
     * 区分线下/线下
     * */
    private void caculateBugStatus(String env, Bug bug, ZentaoBug bugInfo) {

    }

    /**
     * 根据bug类型统计
     * 区分线下/线下
     * */
    private void caculateBugType(String env, Bug bug, ZentaoBug bugInfo) {

    }

    /**
     * ConcurrentHashMap<Integer(product in zentao), Bug>
     */
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
