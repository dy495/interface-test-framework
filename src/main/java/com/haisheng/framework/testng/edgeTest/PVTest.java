package com.haisheng.framework.testng.edgeTest;


import com.haisheng.framework.dao.IPvUvDao;
import com.haisheng.framework.model.bean.PVUV;
import lombok.Data;
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
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;




public class PVTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SqlSession sqlSession = null;

//    @Resource
//    private IPvUvDao pvUvDao;

    @Test
    public void statisticPV() {

        String jsonDir = System.getProperty("JSONDIR");
//        jsonDir = "/Users/yuhaisheng/jason/document/work";
        jsonDir = "/Users/yuhaisheng/jason/document";
        FileUtil fileUtil = new FileUtil();
        List<File> files = fileUtil.getFiles(jsonDir, ".json");
        ConcurrentHashMap<Integer, List<Region>> countHm = new ConcurrentHashMap<Integer, List<Region>>();
        for (File file : files) {
            logger.debug("file: " + file.getName());
            countPV(file, countHm);
        }

        logger.debug("jsonDir: " + jsonDir);
        logger.info("get " + files.size() + " files");

        printPVbyMapId(countHm);
        pringPVbyRegionId(countHm);
        logger.debug("test2");

    }

    private void printPVbyMapId(ConcurrentHashMap<Integer, List<Region>> countHm) {
        logger.info("");
        logger.info("");
        for (Map.Entry<Integer, List<Region>> entry : countHm.entrySet()) {
            logger.info("map id: " + entry.getKey());
            int pv = 0;
            for (Region region : entry.getValue()) {
                if (region.status.equals("ENTER")) {
                    pv += region.count;
                }
            }
            logger.info(">>>>current map id PV: " + pv);
        }

    }

    private void pringPVbyRegionId(ConcurrentHashMap<Integer, List<Region>> countHm) {
        logger.info("");
        logger.info("");

        for (Map.Entry<Integer, List<Region>> entry : countHm.entrySet()) {
            int mapId = entry.getKey();
            logger.info("map id: " + mapId);
            //regionid -> PV
            ConcurrentHashMap<String, Integer> regionHm = new ConcurrentHashMap<String, Integer>();
            for (Region region : entry.getValue()) {
                if (! region.getStatus().equals("ENTER")) {
                    continue;
                }
                String regionId = region.getId();
                int regionCount = region.getCount();
                if (regionHm.containsKey(region.getId())) {
                    //region id 相同
                    regionHm.put(regionId, regionHm.get(regionId)+regionCount);
                } else {
                    //region id 不同
                    regionHm.put(regionId, regionCount);
                }
            }

            IPvUvDao pvUvDao = sqlSession.getMapper(IPvUvDao.class);

            for (Map.Entry<String, Integer> reginPvEntry : regionHm.entrySet() ) {
                logger.info(">>>>region id: " + reginPvEntry.getKey() + ", PV: " + reginPvEntry.getValue());
                PVUV pvuv = new PVUV();
                pvuv.setMapId(mapId);
                pvuv.setRegionId(Integer.parseInt(reginPvEntry.getKey()));
                pvuv.setRegionPv(reginPvEntry.getValue());
                pvUvDao.insert(pvuv);
                sqlSession.commit();
            }

        }
    }


    private void countPV(File file,
                         ConcurrentHashMap<Integer, List<Region>> countHm ) {

        try {
            String content= null;
            content = FileUtils.readFileToString(file,"UTF-8");
            JSONObject jsonObject = new JSONObject(content);

            JSONObject data = jsonObject.getJSONObject("biz_data");
            JSONArray trace = data.getJSONArray("trace");
            for (int i=0; i<trace.length(); i++) {
                JSONObject item = trace.getJSONObject(i);
                JSONObject position = item.getJSONObject("position");
                Integer mapId = position.getInt("map_id");
                JSONArray region = position.getJSONArray("region");
                for (int j=0; j<region.length(); j++) {
                    JSONObject regionItem = region.getJSONObject(j);
                    String regionId = regionItem.getString("region_id");
                    String status = regionItem.getString("status");
                    Region regionObj = new Region();
                    regionObj.setId(regionId);
                    regionObj.setStatus(status);
                    regionObj.setCount(1);

                    if (countHm.containsKey(mapId)) {
                        boolean isExistInArray = false;
                        for (Region reginItem : countHm.get(mapId)) {
                            if (reginItem.equals(regionObj)) {
                                reginItem.countIncreaseOne();
                                isExistInArray = true;
                                regionObj = null;
                            }
                        }
                        if (! isExistInArray) {
                            countHm.get(mapId).add(regionObj);
                        }

                    } else {
                        List<Region> list = new ArrayList<Region>();
                        list.add(regionObj);
                        countHm.put(mapId, list);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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


    @Data
    private class Region {
        String id;
        String status;
        int count;

        public void countIncreaseOne() {
            this.count++;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Region){
                Region region = (Region) obj;
                return region.id.equals(id) && region.status.equals(status);
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return id.hashCode()+status.hashCode();
        }


    }

}
