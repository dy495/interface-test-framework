package com.haisheng.framework.testng.checklistRun;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPath;
import com.haisheng.framework.dao.IPvUvDao;
import com.haisheng.framework.model.bean.PVUV;
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
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.UUID;

public class GetPvUv {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SqlSession sqlSession = null;
    String sampleVideo = System.getProperty("SAMPLE_VIDEO");
//    String sampleVideo = "204";

    @Test
    public void getPvUv() {

        String json = "{\"shop_id\":\"134\"}";


        try {
            apiCustomerRequest("/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1", json);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void apiCustomerRequest(String router, String json) throws Exception {
        try {

            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid("uid_e0d1ebec")
                    .appId("a4d4d18741a8")
                    .version(SdkConstant.API_VERSION)
                    .requestId(requestId)
                    .router(router)
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logger.info(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
            printPvUvInfo(JSON.toJSONString(apiResponse));


        } catch (Exception e) {
            throw e;
        }
    }

    private void printPvUvInfo(String response) {
        IPvUvDao pvUvDao = sqlSession.getMapper(IPvUvDao.class);

        int mapId = 143;
        int regionId   = 144;
        int entranceId = 145;
        String status  = "TOTAL";
        int pv         = 0;
        int expectPv   = 0;
        String pvAccuracyRate = "";
        int uv         = 0;
        int expectUv   = 0;
        String uvAccuracyRate = "";

        DecimalFormat df = new DecimalFormat("#.00");
        String jsonPathPV = "$..passing_times.shop_entrance_total";
        String jsonPathUV = "$..person_number.shop_entrance_total";
        if (sampleVideo.contains("all_204")) {
            expectPv = 226;
            expectUv = 223;
        } else if (sampleVideo.contains("all_34")) {
            expectPv = 420;
            expectUv = 415;
        } else if (sampleVideo.contains("152")) {
            expectPv = 34;
            expectUv = 16;
        } else if (sampleVideo.contains("baihua_2019-5-22")) {
            expectPv = 73;
            expectUv = 71;
        }
        JSONArray arrPv = (JSONArray) JSONPath.read(response, jsonPathPV);
        JSONArray arrUV = (JSONArray) JSONPath.read(response, jsonPathUV);
        pv = arrPv.getInteger(0);
        uv = arrUV.getInteger(0);
        pvAccuracyRate = String.valueOf(df.format((float)pv*100/(float) expectPv)) + "%";
        uvAccuracyRate = String.valueOf(df.format((float)uv*100/(float) expectUv)) + "%";

        logger.info("");
        logger.info("");
        logger.info("\n=========================================================="
                + "\n\tmap id: " + mapId
                + "\n\tregion id: " + regionId
                + "\n\tentrance id: " + entranceId
                + "\n\tstatus: " + status
                + "\n\tvideo: " + sampleVideo
                + "\n\tpv: " + pv
                + "\n\texpect pv: " + expectPv
                + "\n\tpv accuracy rate: " + pvAccuracyRate
                + "\n\tuv: " + uv
                + "\n\texpect uv: " + expectUv
                + "\n\tuv accuracy rate: " + uvAccuracyRate
                + "\n==========================================================");
        logger.info("");
        logger.info("");
        PVUV pvuv = new PVUV();
        pvuv.setMapId(mapId);
        pvuv.setRegionId(regionId);
        pvuv.setEntranceId(entranceId);
        pvuv.setStatus(status);
        pvuv.setVideo(sampleVideo);
        pvuv.setPv(pv);
        pvuv.setUv(uv);
        pvuv.setExpectPV(expectPv);
        pvuv.setExpectUV(expectUv);
        pvuv.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        pvuv.setPvAccuracyRate(pvAccuracyRate);
        pvuv.setUvAccuracyRate(uvAccuracyRate);
        pvuv.setImage(System.getProperty("IMAGE_EDGE"));
        pvUvDao.insert(pvuv);
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

