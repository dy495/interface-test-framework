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
    private SqlSessionFactory sessionFactory = null;
    private SqlSessionFactory rdSessionFactory = null;


    public QADbUtil() {
        logger.debug("initial QADbUtil");
    }

    public void openConnection() {
        logger.debug("open db connection");
//        SqlSessionFactory sessionFactory = null;
        String resource = "configuration.xml";
        try {
            sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(
                    resource));
            sqlSession = sessionFactory.openSession();
            logger.debug("sqlSession: " + sqlSession);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void getNewSqlSession() {
        sqlSession.close();
        sqlSession = sessionFactory.openSession();
    }

    public void closeConnection() {
        logger.debug("close db connection");
        sqlSession.close();
    }

    public void openConnectionRdDaily() {
        logger.debug("open rd daily db connection");
//        SqlSessionFactory sessionFactory = null;
        String resource = "configuration-rd-daily.xml";
        try {
            rdSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(
                    resource));
            rdDailySqlSession = rdSessionFactory.openSession();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openConnectionRdDailyEnvironment() {
        logger.debug("open rd daily db connection");
//        SqlSessionFactory sessionFactory = null;
        String resource = "configuration-rd-daily.xml";
        try {
            rdSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(
                    resource),"readonly");
            rdDailySqlSession = rdSessionFactory.openSession();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getNewRdSqlSession() {
        rdDailySqlSession.close();
        rdDailySqlSession = rdSessionFactory.openSession();
    }

    public void closeConnectionRdDaily() {
        logger.debug("close rd daily db connection");
        rdDailySqlSession.close();
    }

    public void updateRetrunVisitTimeToToday(long customerId) {
        getNewRdSqlSession();
        IReturnVisitDao returnVisitDao = rdDailySqlSession.getMapper(IReturnVisitDao.class);

        DateTimeUtil dt = new DateTimeUtil();
        ReturnVisitTime returnVisitTime = new ReturnVisitTime();
        returnVisitTime.setCustomerId(customerId);;
        returnVisitTime.setReturnVisitDate(dt.getHistoryDate(0));

        returnVisitDao.updateReturnVisitTime(returnVisitTime);
        rdDailySqlSession.commit();
    }

    public void updateReportTime(ReportTime reportTime) {
        getNewRdSqlSession();
        IReportTimeDao reportTimeDao = rdDailySqlSession.getMapper(IReportTimeDao.class);
        reportTimeDao.updateReportTime(reportTime);
        rdDailySqlSession.commit();
    }

    public void updateProtectTime(ProtectTime protectTime) {
        getNewRdSqlSession();
        IProtectTimeDao protectTimeDao = rdDailySqlSession.getMapper(IProtectTimeDao.class);
        protectTimeDao.updateProtectTime(protectTime);
        rdDailySqlSession.commit();
    }

    public void saveToCaseTable(Case aCase) {

        if (null == aCase) {
            logger.info("case class is null, do NOT save data to db");
            return;
        } else {
            logger.info("save case result to db");
        }
        getNewSqlSession();
        logger.debug("sqlSession: " + sqlSession);
        ICaseDao caseDao = sqlSession.getMapper(ICaseDao.class);

        List<Integer> listId = caseDao.queryCaseByName(aCase.getApplicationId(),
                aCase.getConfigId(),
                aCase.getCaseName());
        if (listId.size() > 0) {
            aCase.setId(listId.get(0));
            logger.debug("case already existed: " + aCase.getCaseName());
        }
        aCase.setEditTime(new Timestamp(System.currentTimeMillis()));

        caseDao.insert(aCase);
        sqlSession.commit();
        logger.debug("insert commit done");
    }

    public void saveShelfAccuracy(Shelf shelf) {
        getNewSqlSession();
        IShelfDao shelfDao = sqlSession.getMapper(IShelfDao.class);

        shelfDao.insert(shelf);
        sqlSession.commit();

    }

    public List<Shelf> getShelfAccuracy(String date) {
        getNewSqlSession();
        IShelfDao shelfDao = sqlSession.getMapper(IShelfDao.class);

        List<Shelf> accuracyList = shelfDao.query(date);

        return accuracyList;

    }

    public List<BaiguoyuanBindUser> getBaiguoyuanBindAccuracy(String date, String shopId) {
        getNewSqlSession();
        IBaiguoyuanUserDao baiguoyuanDao = sqlSession.getMapper(IBaiguoyuanUserDao.class);

        List<BaiguoyuanBindUser> userList = baiguoyuanDao.getUserList(date, shopId);

        return userList;

    }

    public int removeBaiguoyuanBindUser(String date, String shopId) {
        getNewSqlSession();
        IBaiguoyuanUserDao baiguoyuanDao = sqlSession.getMapper(IBaiguoyuanUserDao.class);

        int num = baiguoyuanDao.removeData(date, shopId);
        sqlSession.commit();

        logger.info("delete shop id " + shopId + " today data " + num + " rows from bind user table");
        return num;
    }

    public void saveBaiguoyuanMetrics(BaiguoyuanBindMetrics bindMetrics) {
        getNewSqlSession();
        IBaiguoyuanMetricsDao metricsDao = sqlSession.getMapper(IBaiguoyuanMetricsDao.class);

        metricsDao.insert(bindMetrics);
        sqlSession.commit();

    }

    public void saveBaiguoyuanMetrics(List<BaiguoyuanBindMetrics> bindMetricsList) {
        getNewSqlSession();
        IBaiguoyuanMetricsDao metricsDao = sqlSession.getMapper(IBaiguoyuanMetricsDao.class);

        for (BaiguoyuanBindMetrics bindMetrics : bindMetricsList) {
            metricsDao.insert(bindMetrics);
        }

        sqlSession.commit();

    }

    public List<BaiguoyuanBindMetrics> getBaiguoyuanMetrics(String date, String shopId) {
        getNewSqlSession();
        IBaiguoyuanMetricsDao metricsDao = sqlSession.getMapper(IBaiguoyuanMetricsDao.class);

        return metricsDao.getMetricsAccuracy(date, shopId);

    }

    public OnlinePvuvCheck selectOnlinePvUv(String com, String date, String hour) {
        getNewSqlSession();
        IOnlinePvUvDao onlinePvUvDao = sqlSession.getMapper(IOnlinePvUvDao.class);

        return onlinePvUvDao.selectData(com, date, hour);
    }

    public void updateOnlinePvUvDiff(OnlinePVUV onlinePVUV) {
        getNewSqlSession();
        IOnlinePvUvDao onlinePvUvDao = sqlSession.getMapper(IOnlinePvUvDao.class);
        onlinePvUvDao.updateDiff(onlinePVUV);

        sqlSession.commit();

    }

    public void saveOnlinePvUv(OnlinePVUV onlinePVUV) {
        getNewSqlSession();
        IOnlinePvUvDao onlinePvUvDao = sqlSession.getMapper(IOnlinePvUvDao.class);
        onlinePvUvDao.insert(onlinePVUV);

        sqlSession.commit();

    }

    public void saveYuexiuOnlineUvGap(OnlineYuexiuUvGap onlineYuexiuUvGap) {
        getNewSqlSession();
        IOnlineYuexiuShopDatangGapDao onlineYuexiuShopDatangGapDao = sqlSession.getMapper(IOnlineYuexiuShopDatangGapDao.class);
        onlineYuexiuShopDatangGapDao.insert(onlineYuexiuUvGap);

        sqlSession.commit();

    }

    public void saveYuexiuOnlineCustomerSearch(OnlineYuexiuCustomerSearch onlineYuexiu) {
        getNewSqlSession();
        IOnlineYuexiuCustomerSearchDao onlineYuexiuDao = sqlSession.getMapper(IOnlineYuexiuCustomerSearchDao.class);
        onlineYuexiuDao.insert(onlineYuexiu);

        sqlSession.commit();

    }

    public void saveDataToDb(IShelfSensorIndices sensorIndex) {
        getNewSqlSession();
        IShelfSensorIndicesDao sensorTestDao = sqlSession.getMapper(IShelfSensorIndicesDao.class);
        sensorTestDao.insert(sensorIndex);

        sqlSession.commit();
    }

    public void saveEdgePvRgn(EdgePvRgn edgePvRgn) {
        getNewSqlSession();
        IEdgePvDao edgePvDao = sqlSession.getMapper(IEdgePvDao.class);
        edgePvDao.insert(edgePvRgn);

        sqlSession.commit();
    }

    public List<EdgePvAccuracy> getEdgePvAccuracy(String day) {
        getNewSqlSession();
        IEdgePvDao edgePvDao = sqlSession.getMapper(IEdgePvDao.class);
        return edgePvDao.getAccuracyByDay(day);
    }


    public List<String> selectOnlineReqDeviceList(String date) {
        getNewSqlSession();
        IOnlineReqNumDao dao = sqlSession.getMapper(IOnlineReqNumDao.class);

        return dao.getDeviceIdList(date);
    }

    public Integer selectOnlineReqNum(String deviceId, String date, String hour) {
        getNewSqlSession();
        IOnlineReqNumDao dao = sqlSession.getMapper(IOnlineReqNumDao.class);

        return dao.selectData(deviceId, date, hour);
    }

    public List<OnlineReqNum> selectOnlineReqNumByDate(String date, String hour) {
        getNewSqlSession();
        IOnlineReqNumDao dao = sqlSession.getMapper(IOnlineReqNumDao.class);

        return dao.selectDataByDate(date, hour);
    }

    public void updateOnlineReqNumDiff(OnlineReqNum onlineData) {
        getNewSqlSession();
        IOnlineReqNumDao dao = sqlSession.getMapper(IOnlineReqNumDao.class);
        dao.updateDiff(onlineData);

        sqlSession.commit();

    }

    public List<OnlineScopeInfo> selectScopeInfo() {
        getNewSqlSession();
        IOnlineScopeInfoDao dao = sqlSession.getMapper(IOnlineScopeInfoDao.class);

        return dao.selectData();
    }

    public List<OnlineAlgorithmMerge> selectAlgorithmMergeData(String date) {
        getNewSqlSession();
        IOnlineAlgorithmMergeDao dao = sqlSession.getMapper(IOnlineAlgorithmMergeDao.class);

        return dao.selectDataByDate(date);
    }

    public void saveFeidanPicSearch(FeidanPicSearch feidanPicSearch) {
        getNewSqlSession();
        IFeidanPicSearchDao feidanPicSearchDao = sqlSession.getMapper(IFeidanPicSearchDao.class);
        feidanPicSearchDao.insert(feidanPicSearch);

        sqlSession.commit();

    }

    public List<Config> selectOnlineAlarmSummary() {
        getNewSqlSession();
        IConfigDao dao = sqlSession.getMapper(IConfigDao.class);

        return dao.queryOnlineConfigSummary();
    }

    public List<Config> selectDailyAlarmSummary() {
        getNewSqlSession();
        IConfigDao dao = sqlSession.getMapper(IConfigDao.class);

        return dao.queryDailyConfigSummary();
    }


    public DataTemp selsetDataTemp(String dataName) {
        getNewSqlSession();
        IDataTempDao dao = sqlSession.getMapper(IDataTempDao.class);

        return dao.queryDataByName(dataName);
    }
    public Integer selsetDataTempOne(String column_name,String dataName) {
        getNewSqlSession();
        IDataTempDao dao = sqlSession.getMapper(IDataTempDao.class);

        return dao.queryDataOneByName(column_name,dataName);
    }

    public void updateDataNum(String dataName,Integer pcAppointmentRecordNum) {
        getNewSqlSession();
        IDataTempDao dao = sqlSession.getMapper(IDataTempDao.class);
        dao.updateDataNum(dataName,pcAppointmentRecordNum);

        sqlSession.commit();

    }

    public void updateDataAll(DataTemp dataTemp) {
        getNewSqlSession();
        IDataTempDao dao = sqlSession.getMapper(IDataTempDao.class);
        dao.updateDataAll(dataTemp);
//        System.out.println(dataTemp.getPcAppointmentRecordNum());
        sqlSession.commit();

    }
    public void updateAppletCustomer(String  wechatId) {
        getNewRdSqlSession();
        IAppletReturnNewDao AppletReturnNewDao = rdDailySqlSession.getMapper(IAppletReturnNewDao.class);
        AppletReturnNewDao.updateAppletCustomer(wechatId);
        rdDailySqlSession.commit();
    }

    public AppletCustomer selectAppletCustomer(String  wechatId) {
        getNewRdSqlSession();
        IAppletReturnNewDao AppletReturnNewDao = rdDailySqlSession.getMapper(IAppletReturnNewDao.class);
        return AppletReturnNewDao.selectAppletCustomer(wechatId);

    }

    public void saveDeviceInfo(List<OnlineScopeDevice> list) {
        if (null == list || list.size() == 0) {
            return;
        }
        getNewSqlSession();
        IOnlineScopeDeviceDao dao = sqlSession.getMapper(IOnlineScopeDeviceDao.class);
        for (OnlineScopeDevice data : list) {
            dao.insert(data);
        }
        sqlSession.commit();

    }

    public String selectTransIdBynumber(String trans_number){
        getNewRdSqlSession();
        return rdDailySqlSession.getMapper(transIdDao.class).SelectIdByNumber(trans_number);
    }
    public String SelectFaceUrlByTransId(String trans_id){
        getNewRdSqlSession();
        return rdDailySqlSession.getMapper(transIdDao.class).SelectFaceUrlByTransId(trans_id);
    }
}
