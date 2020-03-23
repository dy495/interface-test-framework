package com.haisheng.framework.util;

import com.haisheng.framework.dao.*;
import com.haisheng.framework.model.bean.*;
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
    private SqlSession rdDailySqlSession = null;

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

    public void openConnectionRdDaily() {
        logger.debug("open rd daily db connection");
        SqlSessionFactory sessionFactory = null;
        String resource = "configuration-rd-daily.xml";
        try {
            sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(
                    resource));
            rdDailySqlSession = sessionFactory.openSession();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void closeConnectionRdDaily() {
        logger.debug("close rd daily db connection");
        rdDailySqlSession.close();
    }

    public void updateReportTime(ReportTime reportTime) {
        IReportTimeDao reportTimeDao = rdDailySqlSession.getMapper(IReportTimeDao.class);
        reportTimeDao.updateReportTime(reportTime);
        rdDailySqlSession.commit();
    }

    public void updateProtectTime(ProtectTime protectTime) {
        httpPostWithCheckCode(url, json,url); protectTimeDao = rdDailySqlSession.getMapper(IProtectTimeDao.class);
        protectTimeDao.updateProtectTime(protectTime);
        rdDailySqlSession.commit();
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

    public List<BaiguoyuanBindUser> getBaiguoyuanBindAccuracy(String date, String shopId) {
        IBaiguoyuanUserDao baiguoyuanDao = sqlSession.getMapper(IBaiguoyuanUserDao.class);

        List<BaiguoyuanBindUser> userList = baiguoyuanDao.getUserList(date, shopId);

        return userList;

    }

    public int removeBaiguoyuanBindUser(String date, String shopId) {
        IBaiguoyuanUserDao baiguoyuanDao = sqlSession.getMapper(IBaiguoyuanUserDao.class);

        int num = baiguoyuanDao.removeData(date, shopId);
        sqlSession.commit();

        logger.info("delete shop id " + shopId + " today data " + num + " rows from bind user table");
        return num;
    }

    public void saveBaiguoyuanMetrics(BaiguoyuanBindMetrics bindMetrics) {
        IBaiguoyuanMetricsDao metricsDao = sqlSession.getMapper(IBaiguoyuanMetricsDao.class);

        metricsDao.insert(bindMetrics);
        sqlSession.commit();

    }

    public void saveBaiguoyuanMetrics(List<BaiguoyuanBindMetrics> bindMetricsList) {
        IBaiguoyuanMetricsDao metricsDao = sqlSession.getMapper(IBaiguoyuanMetricsDao.class);

        for (BaiguoyuanBindMetrics bindMetrics : bindMetricsList) {
            metricsDao.insert(bindMetrics);
        }

        sqlSession.commit();

    }

    public List<BaiguoyuanBindMetrics> getBaiguoyuanMetrics(String date, String shopId) {
        IBaiguoyuanMetricsDao metricsDao = sqlSession.getMapper(IBaiguoyuanMetricsDao.class);

        return metricsDao.getMetricsAccuracy(date, shopId);

    }

    public OnlinePvuvCheck selectOnlinePvUv(String com, String date, String hour) {
        IOnlinePvUvDao onlinePvUvDao = sqlSession.getMapper(IOnlinePvUvDao.class);

        return onlinePvUvDao.selectData(com, date, hour);
    }

    public void updateOnlinePvUvDiff(OnlinePVUV onlinePVUV) {
        IOnlinePvUvDao onlinePvUvDao = sqlSession.getMapper(IOnlinePvUvDao.class);
        onlinePvUvDao.updateDiff(onlinePVUV);

        sqlSession.commit();

    }

    public void saveOnlinePvUv(OnlinePVUV onlinePVUV) {
        IOnlinePvUvDao onlinePvUvDao = sqlSession.getMapper(IOnlinePvUvDao.class);
        onlinePvUvDao.insert(onlinePVUV);

        sqlSession.commit();

    }

    public void saveYuexiuOnlineUvGap(OnlineYuexiuUvGap onlineYuexiuUvGap) {
        IOnlineYuexiuShopDatangGapDao onlineYuexiuShopDatangGapDao = sqlSession.getMapper(IOnlineYuexiuShopDatangGapDao.class);
        onlineYuexiuShopDatangGapDao.insert(onlineYuexiuUvGap);

        sqlSession.commit();

    }

    public void saveYuexiuOnlineCustomerSearch(OnlineYuexiuCustomerSearch onlineYuexiu) {
        IOnlineYuexiuCustomerSearchDao onlineYuexiuDao = sqlSession.getMapper(IOnlineYuexiuCustomerSearchDao.class);
        onlineYuexiuDao.insert(onlineYuexiu);

        sqlSession.commit();

    }

    public void saveDataToDb(IShelfSensorIndices sensorIndex) {

        IShelfSensorIndicesDao sensorTestDao = sqlSession.getMapper(IShelfSensorIndicesDao.class);
        sensorTestDao.insert(sensorIndex);

        sqlSession.commit();
    }

    public void saveEdgePvRgn(EdgePvRgn edgePvRgn) {
        IEdgePvDao edgePvDao = sqlSession.getMapper(IEdgePvDao.class);
        edgePvDao.insert(edgePvRgn);

        sqlSession.commit();
    }

    public List<EdgePvAccuracy> getEdgePvAccuracy(String day) {
        IEdgePvDao edgePvDao = sqlSession.getMapper(IEdgePvDao.class);
        return edgePvDao.getAccuracyByDay(day);
    }


    public List<String> selectOnlineReqDeviceList(String date) {
        IOnlineReqNumDao dao = sqlSession.getMapper(IOnlineReqNumDao.class);

        return dao.getDeviceIdList(date);
    }

    public Integer selectOnlineReqNum(String deviceId, String date, String hour) {
        IOnlineReqNumDao dao = sqlSession.getMapper(IOnlineReqNumDao.class);

        return dao.selectData(deviceId, date, hour);
    }

    public List<OnlineReqNum> selectOnlineReqNumByDate(String date, String hour) {
        IOnlineReqNumDao dao = sqlSession.getMapper(IOnlineReqNumDao.class);

        return dao.selectDataByDate(date, hour);
    }

    public void updateOnlineReqNumDiff(OnlineReqNum onlineData) {
        IOnlineReqNumDao dao = sqlSession.getMapper(IOnlineReqNumDao.class);
        dao.updateDiff(onlineData);

        sqlSession.commit();

    }

    public List<OnlineScopeInfo> selectScopeInfo() {
        IOnlineScopeInfoDao dao = sqlSession.getMapper(IOnlineScopeInfoDao.class);

        return dao.selectData();
    }

    public List<OnlineAlgorithmMerge> selectAlgorithmMergeData(String date) {
        IOnlineAlgorithmMergeDao dao = sqlSession.getMapper(IOnlineAlgorithmMergeDao.class);

        return dao.selectDataByDate(date);
    }

    public void saveFeidanPicSearch(FeidanPicSearch feidanPicSearch) {
        IFeidanPicSearchDao feidanPicSearchDao = sqlSession.getMapper(IFeidanPicSearchDao.class);
        feidanPicSearchDao.insert(feidanPicSearch);

        sqlSession.commit();

    }
}
