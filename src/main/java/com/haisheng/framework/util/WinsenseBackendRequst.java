package com.haisheng.framework.util;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.commonDataStructure.GateRequstPara;
import com.haisheng.framework.testng.commonDataStructure.LogMine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WinsenseBackendRequst {
    private Logger logger       = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine     = new LogMine(logger);

    private String AUTH_URL         = "http://dev.sso.winsenseos.cn/sso/login";
    private String USER             = "1400893423@qq.com";
    private String PASSWD           = "2a2d705f837ad4b895b3d50965a6e1f7";
    private String AUTHORIZATION    = null;
    private ConcurrentHashMap<String, Object> HEADER = new ConcurrentHashMap();

    private String GATE_URL     = "http://dev.api.winsenseos.cn/retail/api/data/biz";


    public WinsenseBackendRequst() {
        genAuth();
    }

    public WinsenseBackendRequst(String authUrl) {
        this.AUTH_URL = authUrl;
        genAuth();
    }

    public WinsenseBackendRequst(String authUrl, String user, String passwd) {
        this.AUTH_URL = authUrl;
        this.USER = user;
        this.PASSWD = passwd;
        genAuth();
    }


    private ApiResponse sendRequestWithGate(GateRequstPara para) throws Exception {
        ApiRequest apiRequest = null;
        Credential credential = new Credential(para.getAK(), para.getSK());
        // 封装request对象
        String requestId = UUID.randomUUID().toString();
        apiRequest = new ApiRequest.Builder()
                .uid(para.getUID())
                .appId(para.getAPP_CODE())
                .requestId(requestId)
                .version(para.getVersion())
                .router(para.getRouter())
                .dataResource(para.getResource())
                .dataBizData(JSON.parseObject(para.getJson()))
                .build();

        // client 请求
        ApiClient apiClient = new ApiClient(GATE_URL, credential);
        ApiResponse apiResponse = apiClient.doRequest(apiRequest);
        logMine.printImportant("apiRequest" + JSON.toJSONString(apiRequest));
        logMine.printImportant("apiResponse" + JSON.toJSONString(apiResponse));

        return apiResponse;
    }


    public String sendRequestWithLoginToken(String URL, String json) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJsonWithHeaders(URL, json, HEADER);

        return executor.getResponse();
    }

    public String genAuth() {
        logger.info("");
        logger.info("-------------------------gen authrization!-----------------------");
        String json =
                "{" +
                        "\"email\":\"" + USER + "\"," +
                        "\"password\":\"" + PASSWD + "\"" +
                "}";
        try {
            HttpExecutorUtil executor = new HttpExecutorUtil();
            executor.doPostJsonWithHeaders(AUTH_URL, json, HEADER);
            JSONObject data = JSON.parseObject(executor.getResponse()).getJSONObject("data");

            AUTHORIZATION = data.getString("token");
            HEADER.put("Authorization", AUTHORIZATION);

        } catch (Exception e) {
            logger.info(e.toString());

        } finally {
            return AUTHORIZATION;
        }
    }
}
