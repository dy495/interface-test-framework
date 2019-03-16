package com.haisheng.framework.testng.edgeTest;


import com.alibaba.fastjson.JSON;
import com.haisheng.framework.dao.IPvUvDao;
import com.haisheng.framework.model.bean.PVUV;
import com.haisheng.framework.util.StatusCode;
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
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import java.util.UUID;


import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author yuhaisheng
 * @date 2019-03-14
 **/

public class PVTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SqlSession sqlSession = null;
    private ConcurrentHashMap<Integer, List<Region>> countHm = new ConcurrentHashMap<Integer, List<Region>>();
    private String START_TIME = "123456789";
    private String END_TIME = "987654321";


    @Test
    public void statisticPV() {

        String jsonDir = System.getProperty("JSONDIR");
        jsonDir = "/Users/yuhaisheng/jason/workspace/git/interface-test-framework/src/main/resources/test-res-repo/pv-post";
        FileUtil fileUtil = new FileUtil();
        List<File> files = fileUtil.getFiles(jsonDir, ".json");
        for (File file : files) {
            logger.debug("file: " + file.getName());
//            countPV(file, countHm);
            apiSdkPostToCloud(file, "/scenario/who/ANALYSIS_PERSON/v1.0");
            String json = "{" +
                    "\"shop_id\":\"134\"," +
                    "\"start_time\":" + String.valueOf(System.currentTimeMillis()-3600000) + "," +
                    "\"end_time\":"+ String.valueOf(System.currentTimeMillis()) +
                    "}";
            apiCustomerRequest(json, "/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1");
//            printPVbyMapId(countHm);
//            pringPVbyRegionId(countHm);
        }

        logger.debug("jsonDir: " + jsonDir);
        logger.info("get " + files.size() + " files");
    }

    private void apiCustomerRequest(String json, String router) {
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid("uid_e0d1ebec")
                    .appId("a4d4d18741a8")
                    .version(SdkConstant.API_VERSION)
                    .requestId(UUID.randomUUID().toString())
                    .router(router)
//                    .clientInfoIp("ip")
//                    .clientInfoHostName("hostName")
//                    .clientInfoLocation("location")
//                    .clientInfoMac("mac address")
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            printImportant(JSON.toJSONString(apiResponse.getData()));
//            Assert.assertTrue(apiResponse.isSuccess());
//            logger.info(JSON.toJSONString(apiResponse));
        } catch (Exception e) {
            logger.error(e.toString());
            Assert.assertTrue(false);
        }
    }

    private void printImportant(String info){
        logger.info("");
        logger.info("");
        logger.info(info);
        logger.info("");
        logger.info("");
    }

    private void apiSdkPostToCloud(File file, String router) {
        // 传入签名参数
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            String json = FileUtils.readFileToString(file,"UTF-8");
            json = json.replaceAll("\n\\s*", "")
                        .replace(START_TIME, String.valueOf(System.currentTimeMillis()-1800000))
                        .replace(END_TIME, String.valueOf(System.currentTimeMillis()));
            // 封装request对象
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid("uid_e0d1ebec")
                    .appId("a4d4d18741a8")
                    .version(SdkConstant.API_VERSION)
                    .requestId(UUID.randomUUID().toString())
                    .dataDeviceId("6254834559910912")
                    .router(router)
//                    .clientInfoIp("ip")
//                    .clientInfoHostName("hostName")
//                    .clientInfoLocation("location")
//                    .clientInfoMac("mac address")
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            String gateway = "http://dev.api.winsenseos.com/retail/api/data/device";
            ApiClient apiClient = new ApiClient(gateway, credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            printImportant(JSON.toJSONString(apiResponse.getData()));
//            Assert.assertTrue(apiResponse.isSuccess());
//            logger.info(JSON.toJSONString(apiResponse));
        } catch (IOException e) {
            logger.error(e.toString());
            Assert.assertTrue(false);
        }
    }

//    private String getAuthorization(Long timestamp, String nonce, String router) {
//        String uid = "uid_e0d1ebec";
//        String app_id = "a4d4d18741a8";
//        String ak = "e0709358d368ee13";
//        String sk = "ef4e751487888f4a7d5331e8119172a3";
//        String authorization = "";
//
//        try {
//            String signStr = uid + "." + app_id + "." + ak + "." + router + "." + timestamp + "." + nonce;
//            Mac sha256_HMAC = null;
//            sha256_HMAC = Mac.getInstance("HmacSHA256");
//            SecretKeySpec encodeSecretKey = new SecretKeySpec(sk.getBytes("utf-8"), "HmacSHA256");
//            sha256_HMAC.init(encodeSecretKey);
//            byte[] hash = sha256_HMAC.doFinal(signStr.getBytes("utf-8"));
//            authorization = Base64.getEncoder().encodeToString(hash);
//
//        } catch (Exception e) {
//            logger.error(e.toString());
//        }
//        return authorization;
//
//    }

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
                pvuv.setUpdateTime(new Timestamp(System.currentTimeMillis()));
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
            JSONObject jsonObject = new JSONObject(content).getJSONObject("data");

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
            logger.error(e.toString());
            Assert.assertTrue(false);
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
