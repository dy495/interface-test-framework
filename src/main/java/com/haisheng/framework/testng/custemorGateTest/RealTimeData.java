package com.haisheng.framework.testng.custemorGateTest;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.exception.SdkClientException;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.OnlinePVUV;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.UUID;

public class RealTimeData {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);
    ApiResponse apiResponse = null;
    QADbUtil qaDbUtil = new QADbUtil();

    @Test
    public void test() {
        String com = "test";
        String appId = "a4d4d18741a8";
        String uid = "uid_e0d1ebec";
        String ak = "e0709358d368ee13";
        String sk = "ef4e751487888f4a7d5331e8119172a3";
        String url = "http://dev.api.winsenseos.com/retail/api/data/biz";
        String shopId = "134";

        getRealTimeData(com, url, uid, appId, shopId, ak, sk);
    }

    public void getRealTimeData(String com, String URL, String UID, String APP_ID, String SHOP_ID, String AK, String SK) {
//        当日统计查询
        String router = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1";
//        实时人物列表
        String router1 = "/business/customer/QUERY_CURRENT_CUSTOMER_HISTORY/v1.1";
        String[] resources = null;

        String json =
                "{\n" +
                        "        \"shop_id\":\"" + SHOP_ID + "\"\n" +
                        "}";

        try {
            ApiResponse response = sendRequest(router, resources, json, UID, APP_ID, AK, SK, URL);

            checkCode(response, router, StatusCode.SUCCESS);

            getRealTimeData(response, com);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("");
        }

    }

    private void getRealTimeData(ApiResponse response, String com) throws Exception {
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        OnlinePVUV onlinePVUV = new OnlinePVUV();
        String ressponseStr = JSON.toJSONString(response);
        JSONObject responseJo = JSON.parseObject(ressponseStr);
        JSONArray statistics = responseJo.getJSONObject("data").getJSONArray("statistics");

        if (statistics != null) {
            int size = statistics.size();
            if (size == 0) {
                throw new Exception("返回数据为空！");
            }

        }

        JSONObject statisticsSingle = statistics.getJSONObject(0);

        JSONObject gender = statisticsSingle.getJSONObject("gender");
        JSONObject age = statisticsSingle.getJSONObject("age");

        String uvLeaveStr = statisticsSingle.getJSONObject("person_number").getString("entrance_leave_total");
        int uvLeave = Integer.parseInt(uvLeaveStr);

        String uvEnterStr = statisticsSingle.getJSONObject("person_number").getString("entrance_enter_total");
        int uvEnter = Integer.parseInt(uvEnterStr);

        String pvLeaveStr = statisticsSingle.getJSONObject("passing_times").getString("entrance_leave_total");
        int pvLeave = Integer.parseInt(pvLeaveStr);

        String pvEnterStr = statisticsSingle.getJSONObject("passing_times").getString("entrance_enter_total");
        int pvEnter = Integer.parseInt(pvEnterStr);

        onlinePVUV.setCom(com);
        String date = dateTimeUtil.getHistoryDate(0);
        onlinePVUV.setDate(date);
        onlinePVUV.setGender(gender);
        onlinePVUV.setAge(age);
        onlinePVUV.setPvEnter(pvEnter);
        onlinePVUV.setPvLeave(pvLeave);
        onlinePVUV.setUvEnter(uvEnter);
        onlinePVUV.setUvLeave(uvLeave);

        qaDbUtil.saveOnlinePvUv(onlinePVUV);

    }

    private ApiResponse sendRequest(String router, String[] resource, String json, String UID, String APP_ID, String AK, String SK, String URL) throws Exception {
        logMine.logStep("send Request！");
        try {
            Credential credential = new Credential(AK, SK);
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient(URL, credential);
            apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant("apiRequest" + JSON.toJSONString(apiRequest));
            logMine.printImportant("apiResponse" + JSON.toJSONString(apiResponse));
        } catch (SdkClientException e) {
            String msg = e.getMessage();
            throw new Exception(msg);
        } catch (Exception e) {
            throw e;
        }
        return apiResponse;
    }

    public void checkCode(ApiResponse apiResponse, String router, int expectCode) throws Exception {
        try {
            String requestId = apiResponse.getRequestId();
            if (apiResponse.getCode() != expectCode) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse) +
                        "actual code: " + apiResponse.getCode() + " expect code: " + expectCode + ".";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @BeforeSuite
    public void initial() throws Exception {
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }
}
