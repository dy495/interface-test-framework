package com.haisheng.framework.testng.edgeTest;


import com.haisheng.framework.dao.IPvUvDao;
import com.haisheng.framework.model.bean.PVUV;
import com.haisheng.framework.testng.commonDataStructure.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import com.haisheng.framework.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;


import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class PVTestEdge {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SqlSession sqlSession = null;
    private LogMine logMine = new LogMine(logger);


    @Test
    public void edgePvStatistic() {
        logMine.logCase("testStatisticPv");
        String jsonDir = "src/main/resources/test-res-repo/pv-post/edge-scenario";
        FileUtil fileUtil = new FileUtil();
        List<File> files = fileUtil.getFiles(jsonDir, ".json");
        ConcurrentHashMap<MapRegionEntranceUnit, Integer> countHm = new ConcurrentHashMap<>();

        for (File file : files) {
            countPV(file, countHm);
        }
        logger.info("get " + files.size() + " files");

        printPvInfo(countHm, 34, 34);
    }


    private void countPV(File file, ConcurrentHashMap<MapRegionEntranceUnit, Integer> countHm) {
        logMine.logStep("get edge pv info from json file: " + file.getName());
        try {
            String content        = FileUtils.readFileToString(file,"UTF-8");
            JSONObject jsonObject = new JSONObject(content);
            JSONObject data       = jsonObject.getJSONObject("data").getJSONObject("biz_data");
            JSONArray trace       = data.getJSONArray("trace");

            for (int i=0; i<trace.length(); i++) {
                JSONObject item = trace.getJSONObject(i);
                JSONObject position = item.getJSONObject("position");
                String mapId = position.getString("map_id");
                if (position.isNull("region")) {
                    continue;
                }
                JSONArray region = position.getJSONArray("region");
                for (int j=0; j<region.length(); j++) {
                    String status = region.getJSONObject(j).getString("status");
                    if (!RegionStatus.ENTER.equals(status) && !RegionStatus.LEAVE.equals(status)) {
                        //不处理除ENTER和LEAVE之外的状态
                        //STAY状态由LEAVE个数-ENTER个数计算得到
                        continue;
                    }
                    String regionId = region.getJSONObject(j).getString("region_id");
                    String entranceId = region.getJSONObject(j).getString("entrance_id");
                    saveEnterLeaveInfo(mapId, regionId, entranceId, status, countHm, 1);
                }
            }
            logger.info("current json data get done.");
        } catch (Exception e) {
            logger.error(e.toString());
            Assert.assertTrue(false);
        }
    }

    private void saveEnterLeaveInfo(String mapId, String regionId, String entranceId, String status,
                                    ConcurrentHashMap<MapRegionEntranceUnit, Integer> unitHm,
                                    int num
    ) {
        //save enter/leave info
        MapRegionEntranceUnit unit = new MapRegionEntranceUnit(mapId, regionId,entranceId,status);
        if (unitHm.containsKey(unit)) {
            unitHm.put(unit, unitHm.get(unit)+num);
        } else {
            unitHm.put(unit, num);
        }
    }

    private void printPvInfo(ConcurrentHashMap<MapRegionEntranceUnit, Integer> countHm, int expectIn,int expectOut) {
        logMine.logStep("print pv info and save to db");
        IPvUvDao pvUvDao = sqlSession.getMapper(IPvUvDao.class);
        for (Map.Entry<MapRegionEntranceUnit, Integer> unit : countHm.entrySet()) {
            int mapId = Integer.parseInt(unit.getKey().getMapId());
            int regionId   = Integer.parseInt(unit.getKey().getRegionId());
            int entranceId = Integer.parseInt(unit.getKey().getEntranceId());
            String status  = unit.getKey().getStatus();
            int pv         = unit.getValue();
            int expectPv   = 0;
            String pvAccuracyRate = "";

            DecimalFormat df   = new DecimalFormat("#.00");
            if (status.toUpperCase().equals("ENTER")) {
                pvAccuracyRate = String.valueOf(df.format((float)pv*100/(float) expectIn)) + "%";
                expectPv = expectIn;
            } else if (status.toUpperCase().equals("LEAVE")) {
                pvAccuracyRate = String.valueOf(df.format((float)pv*100/(float) expectOut)) + "%";
                expectPv = expectOut;
            }
            logMine.printImportant("map id: " + mapId
                    + ", region id: " + regionId
                    + ", entrance id: " + entranceId
                    + ", status: " + status
                    + ", pv: " + pv
                    + ", expect pv: " + expectPv
                    + ", accuracy rate: " + pvAccuracyRate);
            PVUV pvuv = new PVUV();
            pvuv.setMapId(mapId);
            pvuv.setRegionId(regionId);
            pvuv.setEntranceId(entranceId);
            pvuv.setStatus(status);
            pvuv.setPv(pv);
            pvuv.setExpectPV(expectPv);
            pvuv.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            pvuv.setPvAccuracyRate(pvAccuracyRate);
            pvuv.setImage(System.getProperty("IMAGE_EDGE"));
            pvUvDao.insert(pvuv);
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
