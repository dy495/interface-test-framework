package com.haisheng.framework.testng.dataCenter.interfaceUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.util.CommonUtil;
import org.apache.http.client.HttpClient;
import org.testng.annotations.BeforeClass;

import java.util.List;

public class logicLayerUtil extends TestCaseCommon {
    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */
    private static volatile logicLayerUtil instance = null;

    private logicLayerUtil() {
    }
    public static logicLayerUtil getInstance() {

        if (null == instance) {
            synchronized (logicLayerUtil.class) {
                if (null == instance) {
                    //这里
                    instance = new logicLayerUtil();
                }
            }
        }
        return instance;
    }
    HttpClient client = null;
    @BeforeClass
    public  void init() throws HttpProcessException {
        client = HCB.custom()
                .pool(50, 10)
                .retry(3).build();
    }

    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String IpPort = "http://47.95.69.163";

    public String httpPost1(String path, String json, String IpPort) throws Exception {
        String requestId = "127c81fd-d0b1-4c77-adad"+ CommonUtil.getRandom(5);
        String res = getRequest(json,requestId,IpPort);
        initHttpConfig();
        String queryUrl = IpPort + path;
        config.url(queryUrl).json(res);
        logger.info("{} json param: {}", path, res);
        long start = System.currentTimeMillis();
        response = HttpClientUtil.post(config);
        logger.info("response: {}", response);
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        caseResult.setResponse(response);
        return response;
    }

    public String getRequest(String data, String requestId, String service){
        JSONObject jsonObject=new JSONObject();
        JSONObject system=new JSONObject();

        JSONArray scope = new JSONArray();
        scope.add(0,"22728");
        system.put("app_id","88590052b177");
        system.put("scope",scope);
        system.put("service",service);
        jsonObject.put("data",data);
        jsonObject.put("system",system);
        jsonObject.put("request_id",requestId);
        jsonObject.put("resource",requestId);
        return jsonObject.toJSONString();
    }

    /**
     * @author qingqing
     * @description 1.1. 特殊人物注册、新增人脸
     */
    /**
     * app checks submit 1. 特殊人物注册、新增人脸
     */
    public JSONObject customer_dealData(String shop_id, String trans_id, String trans_time, List trans_type,String user_id,double total_price
            ,double real_price,String openid,String orderNumber,String memberName,String receipt_type,String posId,JSONArray commodityList
    ) throws Exception {
        String url = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("trans_id", trans_id);
        json.put("trans_time", trans_time);
        json.put("trans_type", trans_type);
        json.put("user_id", user_id);
        json.put("total_price", total_price);
        json.put("real_price", real_price);
        json.put("openid", openid);
        json.put("orderNumber", orderNumber);
        json.put("memberName", memberName);
        json.put("receipt_type", receipt_type);
        json.put("posId", posId);
        json.put("commodityList", commodityList);
        String res = httpPost1(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }



}
