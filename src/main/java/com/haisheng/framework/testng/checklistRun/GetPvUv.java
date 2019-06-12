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
import com.haisheng.framework.model.bean.PVUVAccuracy;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.DingChatbot;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetPvUv {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SqlSession sqlSession = null;
    String sampleVideo = System.getProperty("SAMPLE_VIDEO");
    String IS_PUSH_MSG = System.getProperty("IS_PUSH_MSG");
//    debug
//    String sampleVideo = "152";
//    String IS_PUSH_MSG = "true";

    private int mapId = 143;
    private int regionId   = 144;
    private int entranceId = 145;

    @Test
    public void getPvUv() {

        String json = "{\"shop_id\":\"134\"}";


        try {
            apiCustomerRequest("/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1", json);
            if (IS_PUSH_MSG.toLowerCase().equals("true")) {
                pushToDingdingGrp();
            }
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
        int expectPv   = 0;
        int expectUv   = 0;

        int expectPv_enter   = 0;
        int expectUv_enter   = 0;

        int expectPv_leave   = 0;
        int expectUv_leave   = 0;

        String jsonPathPV = "$..passing_times.shop_entrance_total";
        String jsonPathUV = "$..person_number.shop_entrance_total";
        String jsonPathEnterPV = "$..passing_times.shop_entrance_enter_total";
        String jsonPathEnterUV = "$..person_number.shop_entrance_enter_total";
        String jsonPathLeavePV = "$..passing_times.shop_entrance_leave_total";
        String jsonPathLeaveUV = "$..person_number.shop_entrance_leave_total";
        if (sampleVideo.contains("all_204")) {
            expectPv = 218;
            expectUv = 215;

            expectPv_enter = 107;
            expectPv_leave = 111;

            expectUv_enter = 106;
            expectUv_leave = 109;
        } else if (sampleVideo.contains("all_34")) {
            expectPv = 409;
            expectUv = 404;

            expectPv_enter = 321;
            expectPv_leave = 88;

            expectUv_enter = 317;
            expectUv_leave = 87;

        } else if (sampleVideo.contains("152")) {
            expectPv = 70;
            expectUv = 16;

            expectPv_enter = 34;
            expectPv_leave = 36;

            expectUv_enter = 8;
            expectUv_leave = 8;

        } else if (sampleVideo.contains("baihua_2019-5-22")) {
            expectPv = 62;
            expectUv = 60;

            expectPv_enter = 32;
            expectPv_leave = 30;

            expectUv_enter = 31;
            expectUv_leave = 29;
        }

        //total
        String[] pvTotal = calcAccuracy(response, jsonPathPV, expectPv);
        String[] uvTotal = calcAccuracy(response, jsonPathUV, expectUv);

        //enter
        String[] pvEnter = calcAccuracy(response, jsonPathEnterPV, expectPv_enter);
        String[] uvEnter = calcAccuracy(response, jsonPathEnterUV, expectUv_enter);

        //leave
        String[] pvLeave = calcAccuracy(response, jsonPathLeavePV, expectPv_leave);
        String[] uvLeave = calcAccuracy(response, jsonPathLeaveUV, expectUv_leave);


        logger.info("");
        logger.info("");
        logger.info("\n=========================================================="
                + "\n\tmap id: " + mapId
                + "\n\tregion id: " + regionId
                + "\n\tentrance id: " + entranceId
                + "\n\tvideo: " + sampleVideo

                + "\n\tpv: " + pvTotal[0]
                + "\n\texpect pv: " + pvTotal[1]
                + "\n\tpv accuracy rate: " + pvTotal[2]
                + "\n\tuv: " + uvTotal[0]
                + "\n\texpect uv: " + uvTotal[1]
                + "\n\tuv accuracy rate: " + uvTotal[2]

                + "\n\tenter pv: " + pvEnter[0]
                + "\n\texpect enter pv: " + pvEnter[1]
                + "\n\tpv enter accuracy rate: " + pvEnter[2]
                + "\n\tenter uv: " + uvEnter[0]
                + "\n\texpect enter uv: " + uvEnter[1]
                + "\n\tuv enter accuracy rate: " + uvEnter[2]

                + "\n\tleave pv: " + pvLeave[0]
                + "\n\texpect leave pv: " + pvLeave[1]
                + "\n\tpv leave accuracy rate: " + pvLeave[2]
                + "\n\tleave uv: " + uvLeave[0]
                + "\n\texpect leave uv: " + uvLeave[1]
                + "\n\tuv leave accuracy rate: " + uvLeave[2]

                + "\n==========================================================");
        logger.info("");
        logger.info("");

        saveTodb("TOTAL", pvTotal, uvTotal);
        saveTodb("ENTER", pvEnter, uvEnter);
        saveTodb("LEAVE", pvLeave, uvLeave);

    }

    private void saveTodb(String status, String[] accuracyPVArray, String[] accuracyUVArray) {
        if (null == accuracyPVArray[0]) {
            //do NOT save now, until get correct expect pv/uv for enter and leave
            return;
        }


        IPvUvDao pvUvDao = sqlSession.getMapper(IPvUvDao.class);

        PVUV pvuv = new PVUV();
        pvuv.setMapId(mapId);
        pvuv.setRegionId(regionId);
        pvuv.setEntranceId(entranceId);
        pvuv.setStatus(status);
        pvuv.setVideo(sampleVideo);
        pvuv.setPv(Integer.parseInt(accuracyPVArray[0]));
        pvuv.setUv(Integer.parseInt(accuracyUVArray[0]));
        pvuv.setExpectPV(Integer.parseInt(accuracyPVArray[1]));
        pvuv.setExpectUV(Integer.parseInt(accuracyUVArray[1]));
        pvuv.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        pvuv.setPvAccuracyRate(accuracyPVArray[2]);
        pvuv.setUvAccuracyRate(accuracyUVArray[2]);
        pvuv.setImage(System.getProperty("IMAGE_EDGE"));
        pvUvDao.insert(pvuv);
        sqlSession.commit();
    }

    private String[] calcAccuracy(String response, String jsonPath, int expectNum) {
        String[] accuracy = new String[3];

        if (0 == expectNum) {
            return accuracy;
        }

        DecimalFormat df = new DecimalFormat("#.00");
        JSONArray arr = (JSONArray) JSONPath.read(response, jsonPath);
        int num = arr.getInteger(0);
        accuracy[0] = String.valueOf(num);
        accuracy[1] = String.valueOf(expectNum);
        accuracy[2] = String.valueOf(df.format((float)num*100/(float) expectNum)) + "%";

        return accuracy;
    }

    private void dingdingPush(List<PVUVAccuracy> pvuvAccuracyList) {
        DingChatbot.WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=0837493692d7a7e41f6da3fda6ed8e42f8015210b1fad450a415afbcbc7b5907";
        DateTimeUtil dt = new DateTimeUtil();

        String summary = "准确率简报";
        String msg = "### " + summary + "\n";
        String lastDay = "2019-01-01";
        for ( PVUVAccuracy item : pvuvAccuracyList) {
            String day = item.getUpdateTime().substring(0,10);
            if (! day.equals(lastDay)) {
                msg += "\n\n#### " + day + " 记录信息\n";
                lastDay = day;
            }
            msg += ">##### 样本：" + item.getVideo() + ", " + item.getUpdateTime()
                    + "\n>##### " + item.getStatus() + ", PV准确率：" + item.getPvAccuracyRate() + "\n>##### UV准确率：" + item.getUvAccuracyRate() + "\n\n";

        }
        DingChatbot.sendMarkdown(msg);
    }

    private void pushToDingdingGrp() {
        IPvUvDao pvUvDao = sqlSession.getMapper(IPvUvDao.class);

        //sent latest two days pv/uv accuracy
        DateTimeUtil dt = new DateTimeUtil();
        List<PVUVAccuracy> pvuvAccuracyList = pvUvDao.getAccuracyByDay(dt.getHistoryDate(-1));
        dingdingPush(pvuvAccuracyList);
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

